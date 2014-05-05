package tw.com.mds.fet.femtocellportal.core;

import java.math.BigDecimal;

import org.junit.Test;

public class PositionTest {

	@Test
	public void testToTw97() throws Exception {
		System.out.println(new Position(new BigDecimal("0"), new BigDecimal("0")).toTw97());
		System.out.println(new Position(new BigDecimal("-1"), new BigDecimal("0")).toTw97());
		System.out.println(new Position(new BigDecimal("10"), new BigDecimal("10")).toTw97());
		System.out.println(new Position(new BigDecimal("370"), new BigDecimal("0")).toTw97());
		System.out.println(new Position(new BigDecimal("120"), new BigDecimal("23.51234567")).toTw97());
		System.out.println(new Position(new BigDecimal("0"), new BigDecimal("0")).toTw97());
		System.out.println(new Position(new BigDecimal("0"), new BigDecimal("0")).toTw97());
		System.out.println(new Position(new BigDecimal("0"), new BigDecimal("0")).toTw97());
		System.out.println(new Position(new BigDecimal("0"), new BigDecimal("0")).toTw97());
		System.out.println(new Position(new BigDecimal("0"), new BigDecimal("0")).toTw97());
	}
}
