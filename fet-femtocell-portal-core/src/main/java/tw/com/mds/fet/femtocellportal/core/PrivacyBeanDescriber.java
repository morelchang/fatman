package tw.com.mds.fet.femtocellportal.core;

import tw.com.mds.fet.femtocellportal.util.BeanDescriber;

public class PrivacyBeanDescriber implements BeanDescriber {
	
	private BeanDescriber beanDescriber;
	
	public PrivacyBeanDescriber(BeanDescriber beanDescriber) {
		super();
		this.beanDescriber = beanDescriber;
	}

	public String describe(Object o) {
		if (o instanceof FemtoUser) {
			o = new MaskedFemtoUserWrapper((FemtoUser) o);
		}
		if (o instanceof FemtoProfile) {
			o = new MaskedFemtoProfileWrapper((FemtoProfile) o);
		}
		return beanDescriber.describe(o);
	}

}
