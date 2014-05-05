package tw.com.mds.fet.femtocellportal.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;

public class EntityPropertyGenerator {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws Exception {
		List clazzes = Arrays.asList(FemtoUser.class, FemtoProfile.class, AdminUser.class);
		for (Object c : clazzes) {
			describeClass(c);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void describeClass(Object c) throws InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object u = ((Class) c).newInstance();
		Map<String, Object> map = BeanUtils.describe(u);
		for (String prop : map.keySet()) {
			System.out.println(u.getClass().getSimpleName() + "." + prop);
		}
	}
}
