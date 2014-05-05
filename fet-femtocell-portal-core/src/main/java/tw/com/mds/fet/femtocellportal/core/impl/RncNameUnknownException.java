package tw.com.mds.fet.femtocellportal.core.impl;

import tw.com.mds.fet.femtocellportal.core.ServiceException;

public class RncNameUnknownException extends ServiceException {

	private static final long serialVersionUID = 1L;
	
	private final String rncName;

	public RncNameUnknownException(String rncName) {
		super();
		this.rncName = rncName;
	}

	public String getRncName() {
		return rncName;
	}
	
}
