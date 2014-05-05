package tw.com.mds.fet.femtocellportal.core.impl;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;

@Entity
public class SchedulingConfig extends Config {

	private static final long serialVersionUID = 1L;

	private String dailySyncCellsTime;
	private String dailySyncFemtoProfilesTime;

	public SchedulingConfig() {
		super();
	}

	@Column(length = 5, nullable = false)
	public String getDailySyncCellsTime() {
		return dailySyncCellsTime;
	}

	public void setDailySyncCellsTime(String dailySyncCellsTime) {
		this.dailySyncCellsTime = dailySyncCellsTime;
	}

	@Column(length = 5, nullable = false)
	public String getDailySyncFemtoProfilesTime() {
		return dailySyncFemtoProfilesTime;
	}

	public void setDailySyncFemtoProfilesTime(String dailySyncFemtoProfilesTime) {
		this.dailySyncFemtoProfilesTime = dailySyncFemtoProfilesTime;
	}

	@Override
	public void validate() throws ConfigurationException {
		if (StringUtils.isBlank(getDailySyncCellsTime())) {
			throw new ConfigurationException("dailySyncCellsTime can't not be empty");
		}
		if (!validateTimeFormat(getDailySyncCellsTime())) {
			throw new ConfigurationException("dailySyncCellsTime format incorrect, should be HH:MM (HH:00~23, MM:00~59)");
		}
		if (StringUtils.isBlank(getDailySyncFemtoProfilesTime())) {
			throw new ConfigurationException("dailySyncFemtoProfilesTime can't not be empty");
		}
		if (!validateTimeFormat(getDailySyncFemtoProfilesTime())) {
			throw new ConfigurationException("dailySyncFemtoProfilesTime format incorrect, should be HH:MM (HH:00~23, MM:00~59)");
		}
	}

	private boolean validateTimeFormat(String time) {
		String[] values = time.split(":");
		if (values.length != 2) {
			return false;
		}
		
		if (!StringUtils.isNumeric(values[0]) || !StringUtils.isNumeric(values[1])) {
			return false;
		}
		
		int h = Integer.valueOf(values[0]);
		int m = Integer.valueOf(values[1]);
		if (h < 0 || h > 23 || m < 0 || m > 59) {
			return false;
		}
		
		return true;
	}

	public void format() {
		setDailySyncCellsTime(formatTimeString(getDailySyncCellsTime()));
		setDailySyncFemtoProfilesTime(formatTimeString(getDailySyncFemtoProfilesTime()));
	}

	private String formatTimeString(String timeValue) {
		String[] values = timeValue.split(":");
		return StringUtils.leftPad(values[0], 2, "0") + ":" + StringUtils.leftPad(values[1], 2, "0");
	}

	@Override
	public String toString() {
		return "SchedulingConfig [dailySyncCellsTime=" + dailySyncCellsTime
				+ ", dailySyncFemtoProfilesTime=" + dailySyncFemtoProfilesTime
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((dailySyncCellsTime == null) ? 0 : dailySyncCellsTime
						.hashCode());
		result = prime
				* result
				+ ((dailySyncFemtoProfilesTime == null) ? 0
						: dailySyncFemtoProfilesTime.hashCode());
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
		SchedulingConfig other = (SchedulingConfig) obj;
		if (dailySyncCellsTime == null) {
			if (other.dailySyncCellsTime != null)
				return false;
		} else if (!dailySyncCellsTime.equals(other.dailySyncCellsTime))
			return false;
		if (dailySyncFemtoProfilesTime == null) {
			if (other.dailySyncFemtoProfilesTime != null)
				return false;
		} else if (!dailySyncFemtoProfilesTime
				.equals(other.dailySyncFemtoProfilesTime))
			return false;
		return true;
	}

}
