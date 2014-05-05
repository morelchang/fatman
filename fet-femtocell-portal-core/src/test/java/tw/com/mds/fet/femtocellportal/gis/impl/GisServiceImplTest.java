package tw.com.mds.fet.femtocellportal.gis.impl;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;
import tw.com.mds.fet.femtocellportal.ws.AxisWebServiceBean;

public class GisServiceImplTest extends DefaultTestCase {

	private AxisWebServiceBean wsGetPositionByAddress;
	
	private AxisWebServiceBean wsGetReversePoi;
	
	@Ignore
	@Test
	public void testWebservice() throws RemoteException, ServiceException {
		System.out.println("===");
		System.out.println(wsGetPositionByAddress.call("忠孝東路四段100號"));
		System.out.println(wsGetPositionByAddress.call("台北市內湖區東湖路0號"));
		System.out.println(wsGetPositionByAddress.call("台北市內湖區東湖路1000號"));
		System.out.println("===");
	}
	
	@Test
	@Ignore
	public void testGetReversePoi() throws RemoteException, ServiceException {
		String strX= "121.617614";
		String strY = "25.068051";
		System.out.println(wsGetReversePoi.call(strX, strY));
	}

	@Autowired
	public void setWsGetPositionByAddress(@Qualifier("wsGetPositionByAddress") AxisWebServiceBean wsGetPositionByAddress) {
		this.wsGetPositionByAddress = wsGetPositionByAddress;
	}

	@Autowired
	public void setWsGetReversePoi(@Qualifier("wsGetReversePoi")AxisWebServiceBean wsGetReversePoi) {
		this.wsGetReversePoi = wsGetReversePoi;
	}
}
