package tw.com.mds.fet.femtocellportal.nedb.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import tw.com.mds.fet.femtocellportal.core.ApmRecord;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;

public class NedbServiceImplTest extends DefaultTestCase {

	private NedbServiceImpl nedbService = new NedbServiceImpl();
	
	@Test
	public void testImportApmFile() throws Exception {
		String filePath = "D:\\projects\\FET Femto\\interface\\APM\\CMExport_10.79.201.4_2011051910_NO_01.xml";
		List<ApmRecord> apms = nedbService.importApmFile(filePath, "utf8", false);

		String gzFilePath = "D:\\projects\\FET Femto\\interface\\APM\\CMExport_10.79.201.4_2011051910_NO_01.xml.gz";
		List<ApmRecord> gzApms = nedbService.importApmFile(gzFilePath, "utf8", true);
		
		Assert.assertTrue(!apms.isEmpty());
		Assert.assertEquals(apms.size(), gzApms.size());
	}
	
}
