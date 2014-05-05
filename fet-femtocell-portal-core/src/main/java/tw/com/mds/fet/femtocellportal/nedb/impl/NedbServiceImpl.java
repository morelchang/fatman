package tw.com.mds.fet.femtocellportal.nedb.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.ApmRecord;
import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurableService;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.Position;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.dao.ApmRecordDao;
import tw.com.mds.fet.femtocellportal.core.dao.CellDao;
import tw.com.mds.fet.femtocellportal.core.dao.FemtoProfileDao;
import tw.com.mds.fet.femtocellportal.core.dao.RncDao;
import tw.com.mds.fet.femtocellportal.nedb.NedbException;
import tw.com.mds.fet.femtocellportal.nedb.NedbService;
import tw.com.mds.fet.femtocellportal.util.Utils;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = ServiceException.class)
public class NedbServiceImpl implements NedbService, ConfigurableService {

	private static final Log logger = LogFactory.getLog(NedbServiceImpl.class);

	private DataSource dataSource;
	private RncDao rncDao;
	private CellDao cellDao;
	private ProvisionService provisionService;
	private FemtoProfileDao femtoProfileDao;
	private ApmRecordDao apmRecordDao;
	private NedbConfigDao nedbConfigDao;
	private NedbConfig config;

	public void init() throws ConfigurationException {
		reloadConfig();
	}

	private List<Cell> fetchCellsFromNedb() throws NedbException {
		Date startTime = new Date();
		Utils.debug(logger, "fetching cells from nedb, started at:{0}",
				startTime);

		// get rnc lookup table
		List<Rnc> rncs = rncDao.findAll();
		Utils.debug(logger,
				"local rnc table size:{0}, as lookup table for cell.rncId",
				rncs.size());

		// query cells from nedb
		JdbcTemplate fromTmeplate = new JdbcTemplate(dataSource);
		CellRowMapper cellRowMapper = new CellRowMapper(rncs,
				config.getDefaultPlmnId(),
				config.getEnableRncNameConsistencyCheck(),
				config.getEnableRncIdConsistencyCheck());
		List<Cell> result;
		try {
			Utils.debug(logger, "executing sql query:{0}",
					config.getBatchQueryCellSql());
			result = fromTmeplate.query(config.getBatchQueryCellSql(),
					cellRowMapper);
		} catch (Exception e) {
			Utils.error(logger, "failed to query cells from nedb:{0}", e,
					e.getMessage());
			throw new NedbException(13001, Utils.format(
					"failed to query cells from NEDB:{0}", e.getMessage()), e);
		}

		Utils.debug(logger, "executed query, fetched result size:{0}",
				result.size());
		removeNullValue(result);

		Date endTime = new Date();
		Utils.debug(
				logger,
				"fetching cells from nedb, completed at:{0}, total records:{1}, elapsed:{2}sec",
				endTime, result.size(), elapsedTime(startTime, endTime));
		return result;
	}

	private void removeNullValue(List<Cell> result) {
		Iterator<Cell> it = result.iterator();
		while (it.hasNext()) {
			if (it.next() == null) {
				it.remove();
			}
		}
	}

	private long elapsedTime(Date startTime, Date endTime) {
		return (endTime.getTime() - startTime.getTime()) / 1000;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void syncCells() throws NedbException {
		List<Cell> cells = fetchCellsFromNedb();
		replaceCells(cells);
	}

	private void replaceCells(List<Cell> cells) throws NedbException {
		Utils.debug(logger, "importing cells...");

		// create/update cell
		Date batchStartTime = new Date();
		int createCount = 0;
		int updateCount = 0;
		for (Cell imported : cells) {
			Cell current = cellDao.findByCellName(imported.getCellName());
			if (current != null) {
				current.copy(imported);
				updateCount++;
			} else {
				current = imported;
				current.setCreateTime(batchStartTime);
				createCount++;
			}
			current.setUpdateTime(batchStartTime);
			cellDao.persist(current);

			// flush and clear cache
			int totalCount = updateCount + createCount;
			if (totalCount % 1000 == 0) {
				cellDao.flushAndClear();
				Utils.debug(logger, "imported cell:{0}...", totalCount);
			}
		}
		Utils.debug(logger, "imported cell:{0}...", updateCount + createCount);
		cellDao.flushAndClear();

		// remove relationship of Cells and FemtoProfile
		List<Cell> expired = cellDao.findExpired(batchStartTime);
		List<FemtoProfile> removingCellProfiles = femtoProfileDao
				.findHavingCells(expired);
		for (FemtoProfile p : removingCellProfiles) {
			p.getCells().removeAll(expired);
		}
		femtoProfileDao.persist(removingCellProfiles);
		femtoProfileDao.flushAndClear();

		// delete cell
		cellDao.remove(expired);
		cellDao.flushAndClear();

		Date batchEndTime = new Date();
		Utils.info(
				logger,
				"batch import cell completed, created:{0}, updated:{1}, deleted:{2}, duration:{3}sec",
				createCount, updateCount, expired.size(),
				durationOfSecond(batchStartTime, batchEndTime));
	}

	private long durationOfSecond(Date batchStartTime, Date batchEndTime) {
		return (batchEndTime.getTime() - batchStartTime.getTime()) / 1000;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void syncFemtoProfiles() throws NedbException,
			ConfigurationException {
		Date batchStartTime = new Date();
		Utils.debug(logger, "exporting ApmRecord batch started at:{0}",
				batchStartTime);

		final String fileEncoding = config.getImportApmFileEncoding();
		final String apmFilePath = config
				.explainImportApmFilePath(batchStartTime);
		final boolean apmFileGzip = (config.getImportApmFileGzip() == null) ? (false)
				: (config.getImportApmFileGzip());
		List<ApmRecord> imported = importApmFile(apmFilePath, fileEncoding,
				apmFileGzip);
		List<ApmRecord> persisted = transformAndPersistApmRecords(imported);
		exportApmFile(persisted, batchStartTime);

		Date batchEndTime = new Date();
		Utils.info(logger, "ApmRecord exported:{0}, duration:{1}",
				persisted.size(),
				durationOfSecond(batchStartTime, batchEndTime));
	}

	private List<ApmRecord> transformAndPersistApmRecords(
			List<ApmRecord> imported) {
		apmRecordDao.removeAll();
		apmRecordDao.flushAndClear();

		List<ApmRecord> persisted = new ArrayList<ApmRecord>();
		int count = 0;
		for (ApmRecord apm : imported) {
			// select profile from document
			FemtoProfile profile = provisionService.findProfileByApei(apm
					.getApei());
			if (profile == null) {
				Utils.warn(
						logger,
						"profile with apei:{0} not found! skip this APM record",
						apm.getApei());
			} else {
				// transform profile data
				Position p = profile.getPosition();
				apm.setLongitude(p.getLongitude().toString());
				apm.setLatitude(p.getLatitude().toString());
			}

			if (apm.getRnc_id() == null) {
				apm.setRnc_id(config.getExportDefaultRncId());
			}
			if (apm.getBeamdirection() == null) {
				apm.setBeamdirection(config.getExportDefaultBeamDirection());
			}

			// insert into local database
			ApmRecord persistedApm = apmRecordDao.persist(apm);
			persistedApm.setApei(apm.getApei());
			persisted.add(persistedApm);
			count++;

			if (count % 1000 == 0) {
				apmRecordDao.flushAndClear();
				Utils.debug(logger, "processed and saved ApmRecord:{0}...",
						count);
			}
		}

		Utils.info(logger, "total processed and saved ApmRecord:{0}", count);
		return persisted;
	}

	private void exportApmFile(List<ApmRecord> persisted, Date executeTime)
			throws NedbException, ConfigurationException {
		final String exportFilePath = config.explainExportFilePath(executeTime);
		final String exportNewLine = "\r\n";
		final String separator = config.getExportSeparator();

		// export to csv file
		File exportFile = new File(exportFilePath);
		if (!exportFile.getParentFile().exists()
				|| !exportFile.getParentFile().isDirectory()
				|| !exportFile.getParentFile().canWrite()) {
			throw new NedbException(
					13003,
					Utils.format(
							"can't not access directory:{0}, directory not found? or no right to write file?",
							exportFile.getParentFile().getPath()));
		}
		if (exportFile.exists()) {
			Utils.info(logger, "file:{0} exists, removing it", exportFilePath);
			exportFile.delete();
		}
		Utils.info(logger, "exporting file:{0}", exportFilePath);

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(exportFile)));
		} catch (FileNotFoundException e1) {
			throw new NedbException(13003, Utils.format(
					"failed to export apm file, filed not found:{0}",
					exportFilePath));
		}

		try {
			int count = 0;
			for (ApmRecord apm : persisted) {
				bw.write(apm.toString(separator) + exportNewLine);
				if (count % 1000 == 0) {
					Utils.debug(logger, "exported ApmRecord count:{0}...",
							count);
				}
			}
			Utils.info(logger, "total exported ApmRecord count:{0}, file:{1}",
					count, exportFilePath);

		} catch (IOException e) {
			throw new NedbException(13003, Utils.format(
					"failed to write file:{0}", exportFilePath), e);
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				Utils.error(logger, "failed to close file:{0}", exportFilePath);
			}
		}
		Utils.info(logger, "completing exporting file:{0}", exportFilePath);
	}

	List<ApmRecord> importApmFile(final String apmFilePath,
			final String fileEncoding, final boolean apmFileGzip)
			throws NedbException {
		// read apm file in
		InputStream fis = null;
		try {
			fis = new FileInputStream(apmFilePath);
			if (apmFileGzip) {
				fis = new GZIPInputStream(fis);
			}
		} catch (FileNotFoundException e) {
			throw new NedbException(13004, Utils.format(
					"apmFile does not exists:{0}", apmFilePath));
		} catch (IOException e) {
			throw new NedbException(13004,
					Utils.format("unable to read apmFile in gzip format:{0}",
							e.getMessage()), e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
		Utils.format("importing apmFile file:{0}", apmFilePath);

		// create xml parser
		Document apmDocument = null;
		SAXReader xmlReader = new SAXReader();
		try {
			apmDocument = xmlReader.read(new BufferedReader(
					new InputStreamReader(fis, fileEncoding)));
		} catch (DocumentException e) {
			throw new NedbException(13005, Utils.format(
					"failed to parse xml file:{0}, reason:{1}", apmFilePath,
					e.getMessage()), e);
		} catch (UnsupportedEncodingException e) {
			throw new NedbException(13005, Utils.format(
					"unsupported apmFile encoding:{0}", fileEncoding));
		}

		// set namespace
		final String namespace = "http://www.3gpp.org/ftp/specs/archive/32_series/32.584#configData";
		Map<String, String> map = new HashMap<String, String>();
		map.put("n", namespace);
		xmlReader.getDocumentFactory().setXPathNamespaceURIs(map);

		// reading from xml
		List<ApmRecord> imported = new ArrayList<ApmRecord>();
		@SuppressWarnings("rawtypes")
		List configDatas = apmDocument
				.selectNodes("/n:configDataFile/n:configData[n:Modifier='create']");
		int count = 0;
		for (Object configData : configDatas) {
			Element configDataElement = (Element) configData;
			String apei = configDataElement.selectSingleNode(
					".//n:managedElement/@apei").getStringValue();
			String apName = configDataElement.selectSingleNode(
					"./n:managedElement/@apName").getStringValue();
			String cellId = configDataElement.selectSingleNode(
					"./n:FAPService/n:CellConfig/n:UMTS/n:RAN/n:CellID")
					.getText();
			String lac = configDataElement
					.selectSingleNode(
							"./n:FAPService/n:CellConfig/n:UMTS/n:CN/n:LACRAC")
					.getText().split(":")[0];
			String mcc = configDataElement
					.selectSingleNode(
							"./n:FAPService/n:CellConfig/n:UMTS/n:CN/n:PLMNID")
					.getText().substring(0, 3);
			String mnc = configDataElement
					.selectSingleNode(
							"./n:FAPService/n:CellConfig/n:UMTS/n:CN/n:PLMNID")
					.getText().substring(3);
			String rac = configDataElement
					.selectSingleNode(
							"./n:FAPService/n:CellConfig/n:UMTS/n:CN/n:LACRAC")
					.getText().split(":")[1];
			String pschpower = configDataElement
					.selectSingleNode(
							"./n:FAPService/n:CellConfig/n:UMTS/n:RAN/n:FDDFAP/n:RF/n:PSCHPower")
					.getText();

			ApmRecord record = new ApmRecord();
			record.setApei(apei);
			record.setNodebname(apName);
			record.setCellid(cellId);
			record.setLac(lac);
			record.setMcc(mcc);
			record.setMnc(mnc);
			record.setRac(rac);
			record.setSac(cellId);
			record.setPschpower(pschpower);

			imported.add(record);
			count++;

			if (count % 1000 == 0) {
				Utils.debug(logger, "imported ApmRecord:{0}...", count);
			}
		}

		Utils.info(logger, "complete importing ApmRecord, total imported:{0}",
				count);
		return imported;
	}

	public boolean isRncInUse(Rnc rnc) {
		long count = cellDao.countByRnc(rnc);
		return count > 0;
	}

	public void reloadConfig() throws ConfigurationException {
		NedbConfig loaded = nedbConfigDao.load();
		if (loaded == null) {
			Utils.warn(logger, "no NedbConfig found, using current config:{0}",
					this.config);
			return;
		}

		loaded.validate();
		this.config = loaded;
		Utils.info(logger, "reloaded NedbConfig:{0}", loaded);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void applyConfig(Config config) throws ConfigurationException {
		NedbConfig newConfig = (NedbConfig) config;
		newConfig.validate();

		newConfig.setOid(this.config.getOid());
		newConfig.setUpdateTime(new Date());

		newConfig = nedbConfigDao.save(newConfig);
		this.config = newConfig;
		Utils.info(logger, "applied NedbConfig:{0}", newConfig);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public RncDao getRncDao() {
		return rncDao;
	}

	public void setRncDao(RncDao rncDao) {
		this.rncDao = rncDao;
	}

	public NedbConfig getConfig() {
		try {
			return (NedbConfig) BeanUtils.cloneBean(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setConfig(NedbConfig config) {
		this.config = config;
	}

	public NedbConfigDao getNedbConfigDao() {
		return nedbConfigDao;
	}

	public void setNedbConfigDao(NedbConfigDao nedbConfigDao) {
		this.nedbConfigDao = nedbConfigDao;
	}

	public CellDao getCellDao() {
		return cellDao;
	}

	public void setCellDao(CellDao cellDao) {
		this.cellDao = cellDao;
	}

	public FemtoProfileDao getFemtoProfileDao() {
		return femtoProfileDao;
	}

	public void setFemtoProfileDao(FemtoProfileDao femtoProfileDao) {
		this.femtoProfileDao = femtoProfileDao;
	}

	public ApmRecordDao getApmRecordDao() {
		return apmRecordDao;
	}

	public void setApmRecordDao(ApmRecordDao apmRecordDao) {
		this.apmRecordDao = apmRecordDao;
	}

	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
	}

}
