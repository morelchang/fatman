package tw.com.mds.fet.femtocellportal.translator.impl;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ErrorMappingConverterTest {

	private ErrorMappingConverter c = new ErrorMappingConverter();
	
	@Test
	public void testConvert() {
		Assert.assertTrue(c.convert("").isEmpty());
		Assert.assertTrue(c.convert("123").isEmpty());
		Assert.assertTrue(c.convert("1,1,1").isEmpty());
		Assert.assertTrue(c.convert("    ").isEmpty());
		Assert.assertTrue(c.convert("  1  ,  , 22 2,  1").isEmpty());
		Assert.assertTrue(c.convert(" 1=,=2").isEmpty());

		String value = 
			"a=1,\nb=2,\nc=3";
		Map<String, String> result = c.convert(value);
		Assert.assertTrue(result.size() == 3);
		Assert.assertEquals("1", result.get("a"));
		Assert.assertEquals("2", result.get("b"));
		Assert.assertEquals("3", result.get("c"));
	}
}
