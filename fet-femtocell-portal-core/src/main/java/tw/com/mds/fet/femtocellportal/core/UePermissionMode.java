package tw.com.mds.fet.femtocellportal.core;

import javax.xml.bind.annotation.XmlEnum;

/**
 * femtocell user equipment permission mode
 * 
 * @author morel
 *
 */
@XmlEnum
public enum UePermissionMode {

	// -1 for INVALID
	CLOSE,
	OPEN,
	GROUP
	
}
