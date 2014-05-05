package tw.com.mds.hessian;

import java.util.List;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.SerializerFactory;

public class DelegatingSerializerFactory extends SerializerFactory {

	public void setDelegateFactories(List<AbstractSerializerFactory> serializers) {
		for (AbstractSerializerFactory s : serializers) {
			addFactory(s);
		}
	}
	
}
