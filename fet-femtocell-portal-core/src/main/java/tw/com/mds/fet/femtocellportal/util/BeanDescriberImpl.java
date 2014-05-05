package tw.com.mds.fet.femtocellportal.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ObjectUtils;

/**
 * bean describer
 * <p>
 * convert specified bean to a customized description string according to
 * resource file: beanDescriber.properties located at the root of classpath
 * </p>
 * <h3>describe definition file</h3>
 * <p>
 * the format of beanDescriber.properties is as follows:<br/>
 * <code>
 * <i>[BeanName].[propertName] = [description]</i>
 * </code><br/>
 * </p>
 * <p>
 * beanDescriber.properties example:<br/>
 * <code>
 * # class name description<br/>
 * FemtoProfile = Femto AP information<br/>
 * # basic property description<br/>
 * FemtoProfiile.apei = APEI Code<br/>
 * FemtoUser.address = Location of the Femto AP<br/>
 * </code>
 * </p>
 * <p>
 * if the property name is not found in beanDescriber.properties file, the
 * property description will be ignored.
 * </p>
 * <h3>describe policy</h3>
 * <p>
 * if the property is not a basic type (boolean, character, byte, short,
 * integer, long, float, double, void, String, BigDecimal), it will try to
 * describe it deeper into the property.
 * </p>
 * 
 * @author morel
 * 
 */
public class BeanDescriberImpl implements BeanDescriber {
	
	private static final Log logger = LogFactory.getLog(BeanDescriberImpl.class);

	private static final Set<Class<?>> BASIC_TYPES = new HashSet<Class<?>>();
	static {
		BASIC_TYPES.add(Boolean.class);
		BASIC_TYPES.add(Character.class);
		BASIC_TYPES.add(Byte.class);
		BASIC_TYPES.add(Short.class);
		BASIC_TYPES.add(Integer.class);
		BASIC_TYPES.add(Long.class);
		BASIC_TYPES.add(Float.class);
		BASIC_TYPES.add(Double.class);
		BASIC_TYPES.add(Void.class);
		BASIC_TYPES.add(BigDecimal.class);
		BASIC_TYPES.add(String.class);
	}

	private ResourceBundle resource;

	public BeanDescriberImpl() {
		super();
		resource = ResourceBundle.getBundle("beanDescriber");
	}

	/* (non-Javadoc)
	 * @see tw.com.mds.fet.femtocellportal.util.BeanDescriber#describe(java.lang.Object)
	 */
	public String describe(Object o) {
		if (o == null) {
			return "";
		} else if (o.getClass().isArray()) {
			return describeAsArray(o);
		} else if (o instanceof Collection) {
			return describeAsCollection(o);
		} else if (o instanceof Enum) {
			return describeAsEnum(o);
		} else if (BASIC_TYPES.contains(o.getClass()) || o.getClass().isPrimitive()) {
			return o.toString();
		} 
		return describeAsObject(o);
	}

	@SuppressWarnings("rawtypes")
	private String describeAsObject(Object o) {
		// describe class name
		String className = o.getClass().getSimpleName();
		StringBuilder sb = new StringBuilder(StringUtils.defaultString(describeByPropertyKey(className)) + "[");
		
		// retrieve properties of specified object
		Map props;
		try {
			props = PropertyUtils.describe(o);
		} catch (Exception e) {
			Utils.error(logger, "failed to describe object:{0}, reason:{1}", e, o, e.getMessage());
			return "";
		}
		
		// check every property
		Iterator iterator = props.entrySet().iterator();
		boolean havingResolvedProp = false;
		while (iterator.hasNext()) {
			// no description found, ignore this property
			Object prop = iterator.next();
			String propName = (String) ((Map.Entry) prop).getKey();
			String resolvedPropName = describeByPropertyKey(className + "." + propName);
			if (resolvedPropName == null) {
				continue;
			}

			// prop separator
			if (havingResolvedProp) {
				sb.append(", ");
			}
			
			// describe into 'resolvedPropName=resolvedPropValue' format
			Object propValue = ((Map.Entry) prop).getValue();
			sb.append(resolvedPropName + ((StringUtils.isEmpty(resolvedPropName)) ? ("") : ("=")) + describe(propValue));
			havingResolvedProp = true;
		}
		
		sb.append("]");
		return sb.toString();
	}

	private String describeAsEnum(Object object) {
		String resolved = describeByPropertyKey(object.getClass().getSimpleName() + "." + object.toString());
		if (resolved == null) {
			resolved = object.toString();
		}
		return resolved;
	}

	private String describeAsCollection(Object object) {
		StringBuilder descValue = new StringBuilder("[");
		Collection<?> collection = (Collection<?>) object;
		for (Object o : collection) {
			descValue.append(describe(o) + ", ");
		}
		if (!collection.isEmpty()) {
			descValue.deleteCharAt(descValue.length() - 1);
			descValue.deleteCharAt(descValue.length() - 1);
		}
		descValue.append("]");
		return descValue.toString();
	}

	private String describeAsArray(Object object) {
		return describeAsCollection(Arrays.asList(ObjectUtils.toObjectArray(object)));
	}
	

	private String describeByPropertyKey(String key) {
		try {
			return resource.getString(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
	
}
