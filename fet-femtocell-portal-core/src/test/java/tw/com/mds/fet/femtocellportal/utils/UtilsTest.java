package tw.com.mds.fet.femtocellportal.utils;

import org.junit.Assert;
import org.junit.Test;

import tw.com.mds.fet.femtocellportal.util.Utils;


public class UtilsTest {

	@Test
	public void testMaskLeft() {
		Assert.assertEquals("A12345****", Utils.maskRight("A123456789", 4, "*"));
		Assert.assertEquals("**********", Utils.maskRight("A123456789", 12, "*"));
		Assert.assertEquals("A123456789", Utils.maskRight("A123456789", 0, "*"));
		Assert.assertEquals("A123456789", Utils.maskRight("A123456789", -1, "*"));
		Assert.assertEquals("張**", Utils.maskRight("張小名", 2, "*"));
		Assert.assertEquals("***", Utils.maskRight("張小名", 5, "*"));
		Assert.assertEquals("***", Utils.maskRight("張小名", 3, "*"));
		Assert.assertEquals("", Utils.maskRight("", 3, "*"));
		Assert.assertEquals(null, Utils.maskRight(null, 3, "*"));
		Assert.assertEquals("***", Utils.maskRight("   ", 3, "*"));
	}

	@Test
	public void testMask() {
		Assert.assertEquals("A****56789", Utils.mask("A123456789", 2, 5, "*"));
		Assert.assertEquals("**********", Utils.mask("A123456789", 1, 10, "*"));
		Assert.assertEquals("**********", Utils.mask("A123456789", 1, 15, "*"));
		Assert.assertEquals("A12345678*", Utils.mask("A123456789", 10, 10, "*"));
		Assert.assertEquals("A12345678*", Utils.mask("A123456789", 10, 11, "*"));
		Assert.assertEquals("*123456789", Utils.mask("A123456789", 1, 1, "*"));
	}

	@Test
	public void testMaskAfter() {
		Assert.assertEquals("A1234*****", Utils.maskAfter("A123456789", 5, "*"));
		Assert.assertEquals("A123456789", Utils.maskAfter("A123456789", 10, "*"));
		Assert.assertEquals("**********", Utils.maskAfter("A123456789", 0, "*"));
		Assert.assertEquals("A*********", Utils.maskAfter("A123456789", 1, "*"));
		Assert.assertEquals("A123456789", Utils.maskAfter("A123456789", 11, "*"));
	}

	@Test
	public void testHourBetween() {
		Assert.assertEquals(false, Utils.isHourBetween(0, 4, 10));
		Assert.assertEquals(false, Utils.isHourBetween(3, 4, 10));
		Assert.assertEquals(true, Utils.isHourBetween(4, 4, 10));
		Assert.assertEquals(true, Utils.isHourBetween(5, 4, 10));
		Assert.assertEquals(false, Utils.isHourBetween(10, 4, 10));
		Assert.assertEquals(false, Utils.isHourBetween(11, 4, 10));
		try {
			Utils.isHourBetween(25, 4, 10);
			Assert.fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
		}
		
		Assert.assertEquals(false, Utils.isHourBetween(19, 20, 4));
		Assert.assertEquals(false, Utils.isHourBetween(20, 20, 4));
		Assert.assertEquals(true, Utils.isHourBetween(23, 20, 4));
		Assert.assertEquals(true, Utils.isHourBetween(0, 20, 4));
		Assert.assertEquals(true, Utils.isHourBetween(1, 20, 4));
		Assert.assertEquals(true, Utils.isHourBetween(4, 20, 4));
		Assert.assertEquals(false, Utils.isHourBetween(5, 20, 4));
	}
	
}
