package tw.com.mds.fet.femtocellportal.translator.impl;

import javax.persistence.Column;
import javax.persistence.Entity;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;

@Entity
public class FlowControlConfig extends Config {

	private static final long serialVersionUID = 1L;

	private Double executePerSecond;
	private Boolean enableRetry;
	private Integer maxRertry;

	public FlowControlConfig() {
		super();
	}

	@Column(nullable = false, precision = 8, scale = 2)
	public Double getExecutePerSecond() {
		return executePerSecond;
	}

	public void setExecutePerSecond(Double executePerSecond) {
		this.executePerSecond = executePerSecond;
	}

	@Column(nullable = false)
	public Boolean getEnableRetry() {
		return enableRetry;
	}

	public void setEnableRetry(Boolean enableRetry) {
		this.enableRetry = enableRetry;
	}

	@Column(nullable = false)
	public Integer getMaxRertry() {
		return maxRertry;
	}

	public void setMaxRertry(Integer maxRertry) {
		this.maxRertry = maxRertry;
	}

	public void validate() throws ConfigurationException {
		if (getEnableRetry() == null) {
			throw new ConfigurationException("enableRetry is empty");
		}
		if (getExecutePerSecond() == null) {
			throw new ConfigurationException("executePerSecond is empty");
		}
		if (getMaxRertry() == null) {
			throw new ConfigurationException("masRetry is empty");
		}
		if (getMaxRertry() < 0) {
			throw new ConfigurationException("maxRetry must be 0 or a positive integer");
		}
	}
	
	@Override
	public String toString() {
		return "FlowControlConfig [executePerSecond=" + executePerSecond
				+ ", enableRetry=" + enableRetry + ", maxRertry=" + maxRertry
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((enableRetry == null) ? 0 : enableRetry.hashCode());
		result = prime
				* result
				+ ((executePerSecond == null) ? 0 : executePerSecond.hashCode());
		result = prime * result
				+ ((maxRertry == null) ? 0 : maxRertry.hashCode());
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
		FlowControlConfig other = (FlowControlConfig) obj;
		if (enableRetry == null) {
			if (other.enableRetry != null)
				return false;
		} else if (!enableRetry.equals(other.enableRetry))
			return false;
		if (executePerSecond == null) {
			if (other.executePerSecond != null)
				return false;
		} else if (!executePerSecond.equals(other.executePerSecond))
			return false;
		if (maxRertry == null) {
			if (other.maxRertry != null)
				return false;
		} else if (!maxRertry.equals(other.maxRertry))
			return false;
		return true;
	}

}
