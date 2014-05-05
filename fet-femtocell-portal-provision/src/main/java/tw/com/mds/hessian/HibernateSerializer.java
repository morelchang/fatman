package tw.com.mds.hessian;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.hibernate.Hibernate;
import org.hibernate.collection.PersistentMap;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import com.caucho.hessian.io.CollectionSerializer;
import com.caucho.hessian.io.MapSerializer;

public class HibernateSerializer extends AbstractSerializer {

	CollectionSerializer collectionSeiralizer = new CollectionSerializer();
	MapSerializer mapSerializer = new MapSerializer();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void writeObject(Object obj, AbstractHessianOutput out)
			throws IOException {
//		boolean init = Hibernate.isInitialized(obj);
//		if (init) {
//			out.writeObject(obj);
//			out.flush();
//			return;
//		}

		if (Hibernate.isInitialized(obj)) {
			collectionSeiralizer.writeObject(new ArrayList((Collection) obj), out);
			return;
		}
		
		if (PersistentMap.class.isAssignableFrom(obj.getClass())) {
			mapSerializer.writeObject(new HashMap(), out);
		} else { 
			collectionSeiralizer.writeObject(new ArrayList(), out);
		}
	}
}