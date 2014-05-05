package tw.com.mds.fet.femtocellportal.core;


public class ExceptionNotifyAspect {

	private ExceptionHandler exceptionHandler;
	
	public void noitify(ServiceException e) {
		exceptionHandler.handleException(e);
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
}
