package tw.com.mds.fet.femtocellportal.gis.impl;

import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

public class JsonParserTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testParse() throws Exception {
		String cells = 
			"[" +
			"  {" +
			"    \"CELLID\": \"50831\"," +
			"    \"CELLNAME\": \"N5083A\"," +
			"    \"LON\": \"121.6165833\"," +
			"    \"LAT\": \"25.06669444\"," +
			"    \"RNCNAME\": \"PC01R\"" +
			"  }," +
			"  {" +
			"    \"CELLID\": \"50833\"," +
			"    \"CELLNAME\": \"N5083C\"," +
			"    \"LON\": \"121.6165833\"," +
			"    \"LAT\": \"25.06669444\"," +
			"    \"RNCNAME\": \"PC01R\"" +
			"  }," +
			"  {" +
			"    \"CELLID\": \"10997\"," +
			"    \"CELLNAME\": \"NY1099A\"," +
			"    \"LON\": \"121.6176722\"," +
			"    \"LAT\": \"25.06983889\"," +
			"    \"RNCNAME\": \"PC01R\"" +
			"  }," +
			"  {" +
			"    \"CELLID\": \"10998\"," +
			"    \"CELLNAME\": \"NY1099B\"," +
			"    \"LON\": \"121.6176722\"," +
			"    \"LAT\": \"25.06983889\"," +
			"    \"RNCNAME\": \"PC01R\"" +
			"  }," +
			"  {" +
			"    \"CELLID\": \"10999\"," +
			"    \"CELLNAME\": \"NY1099C\"," +
			"    \"LON\": \"121.6176722\"," +
			"    \"LAT\": \"25.06983889\"," +
			"    \"RNCNAME\": \"PC01R\"" +
			"  }," +
			"  {" +
			"    \"CELLID\": \"41291\"," +
			"    \"CELLNAME\": \"N4129A\"," +
			"    \"LON\": \"121.6201944\"," +
			"    \"LAT\": \"25.06758333\"," +
			"    \"RNCNAME\": \"PC01R\"" +
			"  }" +
			"]";
		
		JSONParser parser = new JSONParser();
		List o = (List) parser.parse(cells);
		Assert.assertEquals(6, o.size());
		
		Map c1 = (Map) o.get(0);
		Map c2 = (Map) o.get(1);
		Map c3 = (Map) o.get(2);
		Map c4 = (Map) o.get(3);
		Map c5 = (Map) o.get(4);
		Map c6 = (Map) o.get(5);
		Assert.assertEquals("50831", c1.get("CELLID"));
		Assert.assertEquals("50833", c2.get("CELLID"));
		Assert.assertEquals("10997", c3.get("CELLID"));
		Assert.assertEquals("10998", c4.get("CELLID"));
		Assert.assertEquals("10999", c5.get("CELLID"));
		Assert.assertEquals("41291", c6.get("CELLID"));
		
		Assert.assertEquals("NY1099B", c4.get("CELLNAME"));
		Assert.assertEquals("121.6176722", c4.get("LON"));
		Assert.assertEquals("25.06983889", c4.get("LAT"));
		Assert.assertEquals("PC01R", c4.get("RNCNAME"));
	}
}
