package tw.com.mds.fet.femtocellportal.nedb.impl;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.util.Utils;

@Entity
public class NedbConfig extends Config {

	private static final long serialVersionUID = 1L;

	private String batchQueryCellSql;
	private Boolean enableRncNameConsistencyCheck;
	private Boolean enableRncIdConsistencyCheck;
	private String defaultPlmnId;

	private String importApmFilePath;
	private Boolean importApmFileGzip;
	private String importApmFileEncoding;
	private String exportFilePath;
	private String exportNewLine;
	private String exportSeparator;
	private String exportDefaultBeamDirection;
	private String exportDefaultRncId;

	public NedbConfig() {
		super();
	}
	
	public String explainImportApmFilePath(Date executeTime) throws ConfigurationException {
		if (StringUtils.isBlank(getImportApmFilePath())) {
			throw new ConfigurationException("importApmFilePath is not set");
		}
		return parsePath(executeTime, getImportApmFilePath());
	}
	
	public String explainExportFilePath(Date executeTime) throws ConfigurationException {
		if (StringUtils.isBlank(getExportFilePath())) {
			throw new ConfigurationException("exportFilePath is not set");
		}
		return parsePath(executeTime, getExportFilePath());
	}

	private String parsePath(Date executeTime, String path) {
		String result = path;
		Calendar c = Calendar.getInstance();
		c.setTime(executeTime);
		int yyyy = c.get(Calendar.YEAR);
		int mm = c.get(Calendar.MONTH) + 1;
		int dd = c.get(Calendar.DATE);
		result = result.replace("${yyyy}", StringUtils.leftPad(String.valueOf(yyyy), 4, "0"));
		result = result.replace("${mm}", StringUtils.leftPad(String.valueOf(mm), 2, "0"));
		result = result.replace("${dd}", StringUtils.leftPad(String.valueOf(dd), 2, "0"));
		result = result.replace("${y}", String.valueOf(yyyy));
		result = result.replace("${m}", String.valueOf(mm));
		result = result.replace("${d}", String.valueOf(dd));
		return result;
	}

	@Column(nullable = false, length = 500)
	public String getBatchQueryCellSql() {
		return batchQueryCellSql;
	}

	public void setBatchQueryCellSql(String batchQueryCellSql) {
		this.batchQueryCellSql = batchQueryCellSql;
	}

	@Column(nullable = false)
	public Boolean getEnableRncNameConsistencyCheck() {
		return enableRncNameConsistencyCheck;
	}

	public void setEnableRncNameConsistencyCheck(
			Boolean enableRncNameConsistencyCheck) {
		this.enableRncNameConsistencyCheck = enableRncNameConsistencyCheck;
	}

	@Column(nullable = false)
	public Boolean getEnableRncIdConsistencyCheck() {
		return enableRncIdConsistencyCheck;
	}

	public void setEnableRncIdConsistencyCheck(
			Boolean enableRncIdConsistencyCheck) {
		this.enableRncIdConsistencyCheck = enableRncIdConsistencyCheck;
	}

	@Column(nullable = false, length = 50)
	public String getDefaultPlmnId() {
		return defaultPlmnId;
	}

	public void setDefaultPlmnId(String defaultPlmnId) {
		this.defaultPlmnId = defaultPlmnId;
	}

	@Column(nullable = false, length = 200)
	public String getImportApmFilePath() {
		return importApmFilePath;
	}

	public void setImportApmFilePath(String importApmFilePath) {
		this.importApmFilePath = importApmFilePath;
	}

	@Column(nullable = false)
	public Boolean getImportApmFileGzip() {
		return importApmFileGzip;
	}

	public void setImportApmFileGzip(Boolean importApmFileGzip) {
		this.importApmFileGzip = importApmFileGzip;
	}

	@Column(length = 50, nullable = false)
	public String getImportApmFileEncoding() {
		return importApmFileEncoding;
	}

	public void setImportApmFileEncoding(String importApmFileEncoding) {
		this.importApmFileEncoding = importApmFileEncoding;
	}

	@Column(nullable = false, length = 200)
	public String getExportFilePath() {
		return exportFilePath;
	}

	public void setExportFilePath(String exportFilePath) {
		this.exportFilePath = exportFilePath;
	}

	@Column(nullable = false, length = 10)
	public String getExportNewLine() {
		return exportNewLine;
	}

	public void setExportNewLine(String exportNewLine) {
		this.exportNewLine = exportNewLine;
	}

	@Column(nullable = false, length = 10)
	public String getExportSeparator() {
		return exportSeparator;
	}

	public void setExportSeparator(String exportSeparator) {
		this.exportSeparator = exportSeparator;
	}

	@Column(nullable = false, length = 5)
	public String getExportDefaultBeamDirection() {
		return exportDefaultBeamDirection;
	}

	public void setExportDefaultBeamDirection(String exportDefaultBeamDirection) {
		this.exportDefaultBeamDirection = exportDefaultBeamDirection;
	}

	@Column(nullable = false, length = 10)
	public String getExportDefaultRncId() {
		return exportDefaultRncId;
	}

	public void setExportDefaultRncId(String exportDefaultRncId) {
		this.exportDefaultRncId = exportDefaultRncId;
	}

	@Override
	public void validate() throws ConfigurationException {
		if (StringUtils.isBlank(getBatchQueryCellSql())) {
			throw new ConfigurationException("batchQueryCellSql can't not be blank");
		}
		if (getEnableRncIdConsistencyCheck() == null) {
			throw new ConfigurationException("enableRncIdConsistencyCheck can't not be empty");
		}
		if (getEnableRncNameConsistencyCheck() == null) {
			throw new ConfigurationException("enableRncNameConsistencyCheck can't not be empty");
		}
		if (getExportFilePath() == null) {
			throw new ConfigurationException("exportFilePath can't not be empty");
		}
		if (getExportNewLine() == null) {
			throw new ConfigurationException("exportNewLine can't not be empty");
		}
		if (getExportSeparator() == null) {
			throw new ConfigurationException("exportSeparator can't not be empty");
		}
		if (getImportApmFileGzip() == null) {
			throw new ConfigurationException("importApmFileGzip can't not be empty");
		}
		if (getImportApmFileEncoding() == null) {
			throw new ConfigurationException("importApmFileGzip can't not be empty");
		}
		if (!Charset.isSupported(getImportApmFileEncoding())) {
			throw new ConfigurationException(Utils.format(
					"unsupported encoding:{0}", getImportApmFileEncoding()));
		}
	}

	@Override
	public String toString() {
		return "NedbConfig [batchQueryCellSql=" + batchQueryCellSql
				+ ", enableRncNameConsistencyCheck="
				+ enableRncNameConsistencyCheck
				+ ", enableRncIdConsistencyCheck="
				+ enableRncIdConsistencyCheck + ", defaultPlmnId="
				+ defaultPlmnId + ", importApmFilePath=" + importApmFilePath
				+ ", ImportApmFileGzip=" + importApmFileGzip
				+ ", importApmFileEncoding=" + importApmFileEncoding
				+ ", exportFilePath=" + exportFilePath + ", exportNewLine="
				+ exportNewLine + ", exportSeparator=" + exportSeparator
				+ ", exportDefaultBeamDirection=" + exportDefaultBeamDirection
				+ ", exportDefaultRncId=" + exportDefaultRncId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((importApmFileGzip == null) ? 0 : importApmFileGzip
						.hashCode());
		result = prime
				* result
				+ ((batchQueryCellSql == null) ? 0 : batchQueryCellSql
						.hashCode());
		result = prime * result
				+ ((defaultPlmnId == null) ? 0 : defaultPlmnId.hashCode());
		result = prime
				* result
				+ ((enableRncIdConsistencyCheck == null) ? 0
						: enableRncIdConsistencyCheck.hashCode());
		result = prime
				* result
				+ ((enableRncNameConsistencyCheck == null) ? 0
						: enableRncNameConsistencyCheck.hashCode());
		result = prime
				* result
				+ ((exportDefaultBeamDirection == null) ? 0
						: exportDefaultBeamDirection.hashCode());
		result = prime
				* result
				+ ((exportDefaultRncId == null) ? 0 : exportDefaultRncId
						.hashCode());
		result = prime * result
				+ ((exportFilePath == null) ? 0 : exportFilePath.hashCode());
		result = prime * result
				+ ((exportNewLine == null) ? 0 : exportNewLine.hashCode());
		result = prime * result
				+ ((exportSeparator == null) ? 0 : exportSeparator.hashCode());
		result = prime
				* result
				+ ((importApmFileEncoding == null) ? 0 : importApmFileEncoding
						.hashCode());
		result = prime
				* result
				+ ((importApmFilePath == null) ? 0 : importApmFilePath
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NedbConfig other = (NedbConfig) obj;
		if (importApmFileGzip == null) {
			if (other.importApmFileGzip != null)
				return false;
		} else if (!importApmFileGzip.equals(other.importApmFileGzip))
			return false;
		if (batchQueryCellSql == null) {
			if (other.batchQueryCellSql != null)
				return false;
		} else if (!batchQueryCellSql.equals(other.batchQueryCellSql))
			return false;
		if (defaultPlmnId == null) {
			if (other.defaultPlmnId != null)
				return false;
		} else if (!defaultPlmnId.equals(other.defaultPlmnId))
			return false;
		if (enableRncIdConsistencyCheck == null) {
			if (other.enableRncIdConsistencyCheck != null)
				return false;
		} else if (!enableRncIdConsistencyCheck
				.equals(other.enableRncIdConsistencyCheck))
			return false;
		if (enableRncNameConsistencyCheck == null) {
			if (other.enableRncNameConsistencyCheck != null)
				return false;
		} else if (!enableRncNameConsistencyCheck
				.equals(other.enableRncNameConsistencyCheck))
			return false;
		if (exportDefaultBeamDirection == null) {
			if (other.exportDefaultBeamDirection != null)
				return false;
		} else if (!exportDefaultBeamDirection
				.equals(other.exportDefaultBeamDirection))
			return false;
		if (exportDefaultRncId == null) {
			if (other.exportDefaultRncId != null)
				return false;
		} else if (!exportDefaultRncId.equals(other.exportDefaultRncId))
			return false;
		if (exportFilePath == null) {
			if (other.exportFilePath != null)
				return false;
		} else if (!exportFilePath.equals(other.exportFilePath))
			return false;
		if (exportNewLine == null) {
			if (other.exportNewLine != null)
				return false;
		} else if (!exportNewLine.equals(other.exportNewLine))
			return false;
		if (exportSeparator == null) {
			if (other.exportSeparator != null)
				return false;
		} else if (!exportSeparator.equals(other.exportSeparator))
			return false;
		if (importApmFileEncoding == null) {
			if (other.importApmFileEncoding != null)
				return false;
		} else if (!importApmFileEncoding.equals(other.importApmFileEncoding))
			return false;
		if (importApmFilePath == null) {
			if (other.importApmFilePath != null)
				return false;
		} else if (!importApmFilePath.equals(other.importApmFilePath))
			return false;
		return true;
	}
	
}
