package tw.com.mds.fet.femtocellportal.translator.impl;

import org.junit.Assert;
import org.junit.Test;

import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class JaxbModelTest extends DefaultTestCase {
	
	@Test
	public void testMarshall() throws Exception {
		FetDefaultTranslatorRequest r = new SgwA015XmlRequest();
		((SgwA015XmlRequest) r).setSubscriberId("123");
		String xmlcontent = Utils.marshalByJaxb(r);
		System.out.println(xmlcontent);
		SgwA015XmlRequest rx = Utils.unmarshalByJaxb(xmlcontent, SgwA015XmlRequest.class);
		Assert.assertEquals("123", rx.getSubscriberId());
		
		SgwA004XmlRequest r2 = new SgwA004XmlRequest();
		r2.setMsisdn("456");
		xmlcontent = Utils.marshalByJaxb(r2);
		System.out.println(xmlcontent);
		SgwA004XmlRequest rx2 = Utils.unmarshalByJaxb(xmlcontent, SgwA004XmlRequest.class);
		Assert.assertEquals("456", rx2.getMsisdn());
		
		SgwA004XmlResponse rs004 = new SgwA004XmlResponse();
		rs004.setSubscriberId("123");
		xmlcontent = Utils.marshalByJaxb(rs004);
		System.out.println(xmlcontent);
		Assert.assertEquals("123", Utils.unmarshalByJaxb(xmlcontent, SgwA004XmlResponse.class).getSubscriberId());

		SgwA015XmlResponse rs015 = new SgwA015XmlResponse();
		rs015.setImsi("456");
		xmlcontent = Utils.marshalByJaxb(rs015);
		System.out.println(xmlcontent);
		Assert.assertEquals("456", Utils.unmarshalByJaxb(xmlcontent, SgwA015XmlResponse.class).getImsi());
	}
	
}
