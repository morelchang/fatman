package tw.com.mds.fet.femtocellportal.core.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.CellType;
import tw.com.mds.fet.femtocellportal.core.DataLoadService;
import tw.com.mds.fet.femtocellportal.core.Permission;
import tw.com.mds.fet.femtocellportal.core.Position;
import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.dao.AdminUserDao;
import tw.com.mds.fet.femtocellportal.core.dao.ApZoneDao;
import tw.com.mds.fet.femtocellportal.core.dao.CellDao;
import tw.com.mds.fet.femtocellportal.core.dao.RncDao;
import tw.com.mds.fet.femtocellportal.util.Utils;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = ServiceException.class)
public class DataLoadServiceImpl implements DataLoadService {

	private static final Log logger = LogFactory.getLog(DataLoadServiceImpl.class);
	
	private ApZoneDao apZoneDao;
	private AdminUserDao adminUserDao;
	private CellDao cellDao;
	private RncDao rncDao;
	
	private List<String> defaultApZoneList = new ArrayList<String>();
	private List<String> defaultAdminUserList = new ArrayList<String>();
	private List<String> defaultRncList = new ArrayList<String>();
	private String defaultCellsFilePath;
	private Map<String, CellType> cellTypeMap = new HashMap<String, CellType>();
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public List<ApZone> loadApZones() throws ServiceException {
		if (!apZoneDao.findAll().isEmpty()) {
			Utils.warn(logger, "apZone table is not empty, skip load apZone data");
			return new ArrayList<ApZone>();
		}
		// init apZone
		for (String apZoneName : defaultApZoneList) {
			ApZone zone = apZoneDao.findExactlyByName(apZoneName);
			if (zone == null) {
				Date updateTime = new Date();
				zone = new ApZone(apZoneName);
				zone.setCreateTime(updateTime);
				zone.setUpdateTime(updateTime);
				apZoneDao.persist(zone);
			}
		}
		apZoneDao.flush();
		return apZoneDao.findAll();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public List<AdminUser> loadAdminUsers() throws ServiceException {
		if (!adminUserDao.findAll().isEmpty()) {
			Utils.warn(logger, "adminUser table is not empty, skip load adminUser data");
			return new ArrayList<AdminUser>();
		}
		List<AdminUser> result = new ArrayList<AdminUser>();
		for (String adminUserInfo : defaultAdminUserList) {
			if (StringUtils.countMatches(adminUserInfo, ",") != 3) {
				throw new ServiceException("incorrect AdminUser config string:" + adminUserInfo);
			}
			
			String[] infos = StringUtils.split(adminUserInfo, ",");
			AdminUser au = adminUserDao.findById(infos[0]);
			if (au == null) {
				au = new AdminUser(StringUtils.trim(infos[0]), StringUtils.trim(infos[2]));
				au.setUserName(StringUtils.trim(infos[1]));
				au.setPassword(StringUtils.trim(infos[3]));
				Date updateTime = new Date();
				au.setCreateTime(updateTime);
				au.setUpdateTime(updateTime);
				grantPermission(au);
				adminUserDao.persist(au);
			}
			result.add(au);
		}
		adminUserDao.flush();
		return result;
	}

	private void grantPermission(AdminUser au) {
		List<String> resourceIds = Arrays.asList(
				"provision.create_user_profile", "provision.create_profile",
				"provision.search_profile", "provision.change_permissionlsit",
				"provision.change_uepermissionmode",
				"provision.change_profile", "provision.suspend_profile",
				"provision.resume_profile", "provision.delete_profile",
				"admin.manage_announcement", "admin.manage_adminusesr",
				"admin.manage_rnc", "admin.manage_apzone",
				"admin.search_userlog", "admin.manage_config", "provision.change_locationdetectmode");
		for (String id : resourceIds) {
			Permission p = new Permission(au, id, true, new Date());
			au.getPermissions().add(p);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public List<Rnc> loadRncs() throws ServiceException {
		List<Rnc> result = new ArrayList<Rnc>();
		if (!rncDao.findAll().isEmpty()) {
			Utils.warn(logger, "rnc table is not empty, skip load rnc data");
			return result;
		}
		
		for (String rncValue : defaultRncList) {
			String[] rncProps = rncValue.split(",");
			Rnc rnc = new Rnc(rncProps[0], rncProps[1]);
			Date now = new Date();
			rnc.setCreateTime(now);
			rnc.setUpdateTime(now);
			result.add(rncDao.persist(rnc));
		}
		rncDao.flush();
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public List<Cell> loadCells() throws ServiceException {
		List<Cell> result = new ArrayList<Cell>();
		if (StringUtils.isEmpty(defaultCellsFilePath)) {
			return result;
		}
		
		File csvFile = new File(defaultCellsFilePath);
		if (!csvFile.exists() || !csvFile.isFile() || !csvFile.canRead()) {
			Utils.warn(logger, 
					"file:{0} does not exists, or is not a file, or cant access, skipping load Cells", 
					defaultCellsFilePath);
			return result;
		}
		
		if (!cellDao.findAll().isEmpty()) {
			Utils.warn(logger, "cell table is not empty, skip load cell data");
			return result;
		}
		
		int total = 0;
		int imported = 0;
		int unknownRncName = 0;
		int skipped2G = 0;
		String line = null;
		Set<String> unknownRncNames = new HashSet<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
			// skip header line
			br.readLine();
			Cell c = null;
			while ((line = br.readLine()) != null) {
				total++;
				if (total % 1000 == 0) {
					Utils.info(logger, "reading {0} records...", total);
					cellDao.flushAndClear();
				}
				String[] props = line.split(",");
				if (props.length < 16) {
					Utils.warn(logger, 
							"incorrect cell length:{0}, value:{1} in file:{2}", props.length, line, defaultCellsFilePath);
					continue;
				}
				
				try {
					c = mapToCell(props);
					if (c.getType() == CellType.TwoG) {
						skipped2G++;
						continue;
					}
					cellDao.persist(c);
					imported++;
				} catch (RncNameUnknownException e) {
					unknownRncNames.add(e.getRncName());
					unknownRncName++;
				} catch (Exception e) {
					Utils.warn(logger, "failed to import, skip this line:{0}, cell:{1}, reason:{2}", line, c, e.getMessage());
					throw e;
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Utils.format("parsing line:{0}", line), e);
		} finally {
			IOUtils.closeQuietly(br);
		}
		Utils.info(logger, "imported cell, total read:{0}, imported:{1}, skipped2G:{2}, unknownRncName", total, imported, skipped2G, unknownRncName);
		if (!unknownRncNames.isEmpty()) {
			Utils.info(logger, "{0} unknown rncNames:{1}", unknownRncNames.size(), unknownRncNames);
		}
		cellDao.flush();
		return result;
	}

	private Cell mapToCell(String[] props) throws ServiceException {
		Date now = new Date();
		Cell c = new Cell();
		// do not persist 2G cell
		CellType type = mapToCellType(props[7]);
		if (type == null) {
			throw new ServiceException(Utils.format(
					"incorrect cell type value:{0}", props[7]));
		}
		c.setType(type);
		if (type == CellType.TwoG) {
			return c;
		}
		
		c.setCellId(props[0]);
		c.setCellName(props[1]);
		c.setCreateTime(now);
		if (StringUtils.isNotEmpty(props[14]) && StringUtils.isNumeric(props[14])) {
			c.setDir(Integer.valueOf(props[14]));
		}
		c.setLacId(props[2]);
		c.setMcc(props[8]);
		c.setMnc(props[9]);
		c.setPosition(new Position(new BigDecimal(props[5]), new BigDecimal(props[6])));
		Position tw97p = c.getPosition().toTw97();
		c.setLonIdx(tw97p.getLongitude().intValue());
		c.setLatIdx(tw97p.getLatitude().intValue());
		c.setUpdateTime(now);
		c.setVen(props[7]);
		c.setPlmId("46601");
		
		if (!StringUtils.isBlank(props[16])) {
			Rnc rnc = mapToRnc(props[16]);
			c.setRnc(rnc);
		}
		return c;
	}

	private CellType mapToCellType(String typeValue) {
		if (cellTypeMap.isEmpty()) {
			cellTypeMap.put("2G", CellType.TwoG);
			cellTypeMap.put("3G", CellType.ThreeG);
		}
		Iterator<Entry<String, CellType>> it = cellTypeMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, CellType> entry = it.next();
			if (typeValue.contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	private Rnc mapToRnc(String rncName) throws ServiceException {
		List<Rnc> rncs = rncDao.findbyRncName(rncName);
		if (rncs.isEmpty()) {
			throw new RncNameUnknownException(rncName);
		}
		if (rncs.size() != 1) {
			throw new ServiceException(Utils.format("duplicated rnc name:{0}", rncName));
		}
		return rncs.get(0);
	}

	public List<String> getDefaultApZoneList() {
		return defaultApZoneList;
	}

	public void setDefaultApZoneList(List<String> defaultApZoneList) {
		this.defaultApZoneList = defaultApZoneList;
	}

	public List<String> getDefaultAdminUserList() {
		return defaultAdminUserList;
	}

	public void setDefaultAdminUserList(List<String> defaultAdminUserList) {
		this.defaultAdminUserList = defaultAdminUserList;
	}

	public ApZoneDao getApZoneDao() {
		return apZoneDao;
	}

	public void setApZoneDao(ApZoneDao apZoneDao) {
		this.apZoneDao = apZoneDao;
	}

	public AdminUserDao getAdminUserDao() {
		return adminUserDao;
	}

	public void setAdminUserDao(AdminUserDao adminUserDao) {
		this.adminUserDao = adminUserDao;
	}

	public CellDao getCellDao() {
		return cellDao;
	}

	public void setCellDao(CellDao cellDao) {
		this.cellDao = cellDao;
	}

	public RncDao getRncDao() {
		return rncDao;
	}

	public void setRncDao(RncDao rncDao) {
		this.rncDao = rncDao;
	}

	public String getDefaultCellsFilePath() {
		return defaultCellsFilePath;
	}

	public void setDefaultCellsFilePath(String defaultCellsFilePath) {
		this.defaultCellsFilePath = defaultCellsFilePath;
	}

	public List<String> getDefaultRncList() {
		return defaultRncList;
	}

	public void setDefaultRncList(List<String> defaultRncList) {
		this.defaultRncList = defaultRncList;
	}

}
