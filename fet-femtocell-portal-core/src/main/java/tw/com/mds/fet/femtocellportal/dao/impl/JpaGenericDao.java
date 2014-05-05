package tw.com.mds.fet.femtocellportal.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.dao.GenericDao;
import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

/**
 * default transaction mechanism is {@link Propagation#SUPPORTS} and
 * <code>readOnly = true</code>
 * 
 * @author morel
 * 
 * @param <T>
 */
public class JpaGenericDao<T extends PersistentObject> extends
		JpaEntityManagerDao implements GenericDao<T> {

	private Class<T> entityBeanType;

	public JpaGenericDao() {
		this.entityBeanType = fetchEntityType();
	}

	/**
	 * fetch entity type this generic eao handled
	 * <p>
	 * overwrite this method to change the entity type the sub eao class handled
	 * </p>
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> fetchEntityType() {
		// Only support JDK interface proxy
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getEntityBeanType() {
		return entityBeanType;
	}

	/* (non-Javadoc)
	 * @see com.inqgen.iqlis.persistence.eao.GenericEao#makePersistent(java.lang.Object)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public T persist(T entity) {
		return getEntityManager().merge(entity);
	}
	
	public T refresh(T entity) {
		getEntityManager().refresh(entity);
		return entity;
	}

	/* (non-Javadoc)
	 * @see com.inqgen.iqlis.persistence.eao.GenericEao#makePersistent(java.util.Collection)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<T> persist(Collection<T> entities) {
		List<T> results = new ArrayList<T>();
		for (T entity : entities) {
			results.add(persist(entity));
		}
		return results;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void remove(T entity) {
		getEntityManager().remove(entity);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void remove(List<T> entities) {
		for (T t : entities) {
			remove(t);
		}
	}

	public T findByOid(Long oid) {
		Class<T> clazz = getEntityBeanType();
		return findByOid(oid, clazz);
	}

	public <E extends PersistentObject> E findByOid(Long oid, Class<E> clazz) {
		if(oid == null) {
			return null;
		}
		E entity = getEntityManager().find(clazz, oid);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getEntityManager().createQuery("select e from " + getEntityBeanType().getName() + " e")
					.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected List<T> query(String jpql, Object... params) {
		return super.query(jpql, params);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T queryForFirst(String jpql, Object... params) {
		return (T) super.queryForFirst(jpql, params);
	}

	/**
	 * return the first entity of the specified list. or return null if the list
	 * is null or is empty.
	 *
	 * @param entities
	 * @return the first entity of the list, or null.
	 * @author Morel
	 */
	protected T returnFirstOrNull(List<T> entities) {
		if(entities != null && entities.size() > 0) {
			return entities.get(0);
		}
		return null;
	}

	protected String quoteLike(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		return "%" + value + "%";
	}

}
