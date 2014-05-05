package tw.com.mds.fet.femtocellportal.core;

import java.io.IOException;

import com.enterprisedt.net.ftp.FTPException;


public interface ExceptionHandler {

	public void handleException(ServiceException e);

	public void testFtpConnection() throws IOException, FTPException;

}