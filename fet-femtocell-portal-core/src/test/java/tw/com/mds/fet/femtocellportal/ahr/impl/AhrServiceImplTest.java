package tw.com.mds.fet.femtocellportal.ahr.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.ahr.impl.messages.MessageBuilder;
import tw.com.mds.fet.femtocellportal.ahr.impl.messages.SoapEnvelope;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.gis.AddressFormatException;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;
import tw.com.mds.fet.femtocellportal.test.TestData;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class AhrServiceImplTest extends DefaultTestCase {
	
	@Autowired
	AhrServiceImpl ahrService;
	
	@Autowired
	ProvisionService provisionService;

	public static void main(String[] a) throws Exception {
		AhrServiceImplTest t = new AhrServiceImplTest();
		t.marshall();
		t.unmarshall();
	}

	@Test
	@Ignore
	public void testAhrIntegration() throws AddressFormatException, ServiceException {
		TestData data = new TestData();
		
//		FemtoProfile profile = data.createProfile("2102317951109C000032", "466025990000163");
//		FemtoProfile profile = data.createProfile("12345678901234567890", "000000296025396");
		FemtoProfile profile = data.createProfile("12345678901234567890", "466025990000163");
		FemtoUser user = data.createFemtoUser("張小明");
		
		user.getProfiles().add(profile);
		profile.setUser(user);
		
//		provisionService.createUser(user, profile, data.createAdminUser());

		ahrService.deleteProfile(profile);
//		ahrService.addProfile(user, profile);
//		ahrService.setLocation(profile);
//		ahrService.queryPermissionList(profile);
//		ahrService.suspend(profile);
//		ahrService.resume(profile);
//		ahrService.deleteProfile(profile);
	}

	@Ignore
	@Test
	public void marshall() throws Exception {
		FemtoUser femtoUser = data.createFemtoUser("張明煌");
		femtoUser.setProfiles(Arrays.asList(data.createProfile(femtoUser, "APEI", "IMSI")));
		
		SoapEnvelope registerAp = new MessageBuilder().newMessage("1", 1).registerAp(femtoUser, femtoUser.getProfiles().get(0));
		String xml = Utils.marshalByJaxb(registerAp);
		System.out.println(xml);
		
		SoapEnvelope unRegisterAp = new MessageBuilder().newMessage("1", 1).unRegisterAp(femtoUser.getProfiles().get(0));
		xml = Utils.marshalByJaxb(unRegisterAp);
		System.out.println(xml);
		
		SoapEnvelope modifyUeList = new MessageBuilder().newMessage("1", 1).modifyUeList(femtoUser.getProfiles().get(0), new Date());
		xml = Utils.marshalByJaxb(modifyUeList);
		System.out.println(xml);
		
		SoapEnvelope setLocation = new MessageBuilder().newMessage("1", 1).setLocation(femtoUser.getProfiles().get(0));
		xml = Utils.marshalByJaxb(setLocation);
		System.out.println(xml);

		SoapEnvelope queryPermissionList = new MessageBuilder().newMessage("1", 1).QueryPermissionList(femtoUser.getProfiles().get(0));
		xml = Utils.marshalByJaxb(queryPermissionList);
		System.out.println(xml);
	}

	@Ignore
	@Test
	public void unmarshall() throws Exception {
		String xml = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>" + 
			"<env:Envelope xmlns:env=\"http://www.w3.org/2001/06/soap-envelope\">" + 
			"  <env:Body>" + 
			"	<registerAPResponse>" + 
			"		<interfaceVersion>1</interfaceVersion>" + 
			"		<serialNo>12</serialNo>" + 
			"		<returnCode>0</returnCode>" + 
			"		<APIdentity>" + 
			"			<APEI>111111111111111</APEI>" + 
			"			<IMSI>111111111111111</IMSI>" + 
			"		</APIdentity>" + 
			"	</registerAPResponse>" + 
			"  </env:Body>" + 
			"</env:Envelope>";
		System.out.println(Utils.unmarshalByJaxb(xml, SoapEnvelope.class));
		
		xml = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>" +
			"<env:Envelope xmlns:env=\"http://www.w3.org/2001/06/soap-envelope\">" +
			"  <env:Body>" +
			"	<queryPermissionListResponse>" +
			"		<interfaceVersion>1</interfaceVersion>" +
			"		<serialNo>12</serialNo>" +
			"		<returnCode>0</returnCode>" +
			"		<APIdentity>" +
			"			<APEI>12345678998765432112</APEI>" +
			"			<IMSI>45345345346</IMSI>" +
			"		</APIdentity>" +
			"		<permissionMode>0</permissionMode>" +
			"		<maxUEUserCount>4</maxUEUserCount>" +
			"		<UEPermissionList>" +
			"			<UEInfo>" +
			"				<IMSI>100001</IMSI>" +
			"				<MSISDN>213123213213213</MSISDN>" +
			"			</UEInfo>" +
			"			<UEInfo>" +
			"				<IMSI>100002</IMSI>" +
			"				<MSISDN>324234324324</MSISDN>" +
			"			</UEInfo>" +
			"		</UEPermissionList>" +
			"	</queryPermissionListResponse>" +
			"  </env:Body>" +
			"</env:Envelope>";
		System.out.println(Utils.unmarshalByJaxb(xml, SoapEnvelope.class));
	}

	@Ignore
	@Test
	public void testConnectionWithHttpClient() throws Exception {
		// message to send
//		String xml = 
//			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n" +
//			"<env:Envelope xmlns:env=\"http://www.w3.org/2001/06/soap-envelope\">\n" +
//			"<env:Body>\n" +
//			"<queryPermissionList>\n" +
//			"<interfaceVersion>1</interfaceVersion>\n" +
//			"<serialNo>12</serialNo>\n" +
//			"<APIdentity>\n" +
//			"<APEI>12345678998765432112</APEI>\n" +
//			"<IMSI>45345345346</IMSI>\n" +
//			"</APIdentity>\n" +
//			"</queryPermissionList>\n" +
//			"</env:Body>\n" +
//			"</env:Envelope>\n";
		
		String xml = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
			"<env:Envelope xmlns:env=\"http://www.w3.org/2001/06/soap-envelope\">" +
			"    <env:Body>" +
			"        <registerAP>" +
			"            <interfaceVersion>1</interfaceVersion>" +
			"            <serialNo>1829500658</serialNo>" +
			"            <APIdentity>" +
			"                <APEI>12345678901234567890</APEI>" +
			"                <IMSI>000000296025396</IMSI>" +
			"            </APIdentity>" +
			"            <permissionMode>0</permissionMode>" +
			"            <locationDetectMode>0</locationDetectMode>" +
			"            <maxUEUserCount>4</maxUEUserCount>" +
			"            <UEPermissionList>" +
			"                <UEPermission>" +
			"                    <IMSI>000000963703477</IMSI>" +
			"                    <MSISDN>0921123456</MSISDN>" +
			"                </UEPermission>" +
			"            </UEPermissionList>" +
			"            <userName>morel chang</userName>" +
			"            <APZoneName>Blind Spot Scenario</APZoneName>" +
			"            <mobile>0921123456</mobile>" +
			"        </registerAP>" +
			"    </env:Body>" +
			"</env:Envelope>";

		// prepare http request
		HttpClient httpClient = new DefaultHttpClient();
		URI uri = new URI("http://10.76.39.17:34995/");
//		URI uri = new URI("http://10.88.128.8:34995/");
		HttpPost httpRequest = new HttpPost(uri);
		httpRequest.setHeader("Connection", "Close");
		httpRequest.setHeader("Content-Type", "text/xml; charset=UTF-8");
		httpRequest.setEntity(new StringEntity(xml, HTTP.UTF_8));

		// submit and wait for response
		System.out.println("Waiting response...");
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		InputStream responseSream = httpResponse.getEntity().getContent();
		System.out.println(IOUtils.toString(responseSream)); // direct xml content
		System.out.println("responsed!");
	}

	@Ignore
	@Test
	public void testConnectionWithAhrServiceSubmitMethod() throws Exception {
		String xml = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
			"<env:Envelope xmlns:env=\"http://www.w3.org/2001/06/soap-envelope\">" +
			"    <env:Body>" +
			"        <registerAP>" +
			"            <interfaceVersion>1</interfaceVersion>" +
			"            <serialNo>1829500658</serialNo>" +
			"            <APIdentity>" +
			"                <APEI>12345678901234567890</APEI>" +
			"                <IMSI>000000296025396</IMSI>" +
			"            </APIdentity>" +
			"            <permissionMode>0</permissionMode>" +
			"            <locationDetectMode>0</locationDetectMode>" +
			"            <maxUEUserCount>4</maxUEUserCount>" +
			"            <UEPermissionList>" +
			"                <UEPermission>" +
			"                    <IMSI>000000963703477</IMSI>" +
			"                    <MSISDN>0921123456</MSISDN>" +
			"                </UEPermission>" +
			"            </UEPermissionList>" +
			"            <userName>morel chang</userName>" +
			"            <APZoneName>Blind Spot Scenario</APZoneName>" +
			"            <mobile>0921123456</mobile>" +
			"        </registerAP>" +
			"    </env:Body>" +
			"</env:Envelope>";

		// prepare http request
		String response = ahrService.submitXmlHttpRequest(xml);
		System.out.println("responsed!! \n==\n" + response);
	}
}
