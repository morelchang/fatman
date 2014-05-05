package tw.com.mds.fet.femtocellportal.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA {@link EntityManager} based EAO
 * <p>
 * this could be used when non one-entity-per-table case. it won't limit the
 * entity type this EAO handles for. you can get entity manager directly and
 * have your own operation freely.
 * </p>
 * 
 * @author morel
 * 
 */
@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public abstract class JpaEntityManagerDao {

	private EntityManager entityManager;

	public JpaEntityManagerDao() {
		super();
	}

	@PersistenceContext
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected EntityManager getEntityManager() {
		if (entityManager == null)
			throw new IllegalStateException(
					"EntityManager has not been set on EAO before usage");
		return entityManager;
	}

	/**
	 * flush and clear entity manager
	 */
	public void flushAndClear() {
		getEntityManager().flush();
		getEntityManager().clear();
	}

	/**
	 * flush entity manager
	 */
	public void flush() {
		getEntityManager().flush();
	}

	/**
	 * clear entity manager
	 */
	public void clear() {
		getEntityManager().clear();
	}

	/**
	 * this is equivalent to
	 * <code>getEntityManager().createQuery(String jpql);</code>
	 * 
	 * @param jpql
	 * @return
	 */
	protected Query createQuery(String jpql) {
		return getEntityManager().createQuery(jpql);
	}

	/**
	 * create query by entity manager and set parameters accordingly
	 * 
	 * @param jpql
	 *            JPQL string, using '?' as parameter place holder
	 * @param params
	 * @return
	 */
	protected Query createQuery(String jpql, Object... params) {
		Query q = createQuery(jpql);
		for (int i = 0; i < params.length; i++) {
			q = q.setParameter(i + 1, params[i]);
		}
		return q;
	}

	/**
	 * query for a list of result by specified query expression and partameters
	 * 
	 * @param jpql
	 *            JPQL string, using '?' as parameter place holder
	 * @param params
	 *            the parameters for specified jpql
	 * @return if no result, an empty list will be returned
	 */
	@SuppressWarnings("rawtypes")
	protected List query(String jpql, Object... params) {
		return createQuery(jpql, params).getResultList();
	}

	/**
	 * query and get the first element from query result. return null if result
	 * is empty.
	 * 
	 * @param jpql
	 *            JPQL string, using '?' as parameter place holder
	 * @param params
	 *            the parameters for specified jpql
	 * @return null if result is empty
	 */
	@SuppressWarnings("rawtypes")
	protected Object queryForFirst(String jpql, Object... params) {
		List resultList = createQuery(jpql, params).getResultList();
		return returnFirstOrNull(resultList);
	}

	/**
	 * execute update/delete query string with specified parameters.
	 * 
	 * @param jpql
	 *            JPQL string, using '?' as parameter place holder
	 * @param params
	 *            the parameters for specified jpql
	 * @return the number of objects changed by the call
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected int executeUpdate(String jpql, Object... params) {
		return createQuery(jpql, params).executeUpdate();
	}

	/**
	 * return the first entity of the specified list. or return null if the list
	 * is null or is empty.
	 * 
	 * @param entities
	 * @return the first entity of the list, or null.
	 * @author Morel
	 */
	@SuppressWarnings("rawtypes")
	private Object returnFirstOrNull(List entities) {
		if (entities != null && entities.size() > 0) {
			return entities.get(0);
		}
		return null;
	}

}