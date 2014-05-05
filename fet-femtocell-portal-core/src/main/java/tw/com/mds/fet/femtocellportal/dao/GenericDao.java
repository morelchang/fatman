package tw.com.mds.fet.femtocellportal.dao;

import java.util.Collection;
import java.util.List;

public interface GenericDao<T extends PersistentObject> {

	/**
	 * persist and return the specified entity, entity will be updated if it has
	 * been persisted already. the entity argument won't be modified. you should
	 * use the return one instead of the passed in argument instance.
	 * 
	 * @param entity
	 *            the entity to persist/merge
	 * @return the persisted/merged entity
	 */
	T persist(T entity);

	/**
	 * persist and return the specified entities, entities will be updated if it
	 * has been persisted already. the element of collection argument won't be
	 * modified. you should use the return one instead of the passed in argument
	 * instance.
	 * 
	 * @param entities
	 *            the entity collection to persist/merge
	 * @return the persisted/merged entity
	 */
	List<T> persist(Collection<T> entities);

	T refresh(T entity);

	/**
	 * remove specified entity from persistence context 
	 * 
	 * @param entity entity to be removed
	 */
	void remove(T entity);
	
	void remove(List<T> entities);
	
	/**
	 * find entity by oid.
	 * 
	 * @param oid
	 * @return null if not found
	 * @exception IllegalArgumentException
	 *                if the oid is null
	 */
	T findByOid(Long oid);

	List<T> findAll();

	void flushAndClear();

	void flush();

	void clear();

	<E extends PersistentObject> E findByOid(Long oid, Class<E> clazz);

}
