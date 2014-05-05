package tw.com.mds.fet.femtocellportal.web;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.dao.PersistentObject;
import tw.com.mds.fet.femtocellportal.util.Utils;

public abstract class CrudAction<T extends PersistentObject> extends IdentityAction {

	private static final long serialVersionUID = 1L;
	
	public static enum Mode {
		SEARCH,
		VIEW,
		EDIT
	}

	private T searchCriteria;
	private List<T> searchResult = new ArrayList<T>();
	private T original;
	private T current;
	private Mode mode;

	public CrudAction() {
		super();
	}

	@Override
	public void prepare() throws Exception {
		if (current == null) {
			current = createDefaultNew();
		}
		if (searchCriteria == null) {
			searchCriteria = createDefaultSearchCriteria();
		}
		super.prepare();
	}

	public String display() {
		return SUCCESS;
	}

	public String search() {
		if(!validateSearch(searchCriteria)) {
			searchResult.clear();
			return ERROR;
		}
		
		try {
			searchResult = search(searchCriteria);
			logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
					"查詢條件:{0}, 結果筆數:{1}", searchCriteria, searchResult.size()));
			addActionMessage("查詢結果共:{0}筆", searchResult.size());
			
		} catch (Exception e) {
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
					"錯誤:{0}, 查詢條件:{0}", e.getMessage(), searchCriteria));
			return ERROR;
		}
		mode = Mode.SEARCH;
		return SUCCESS;
	}

	public String save() {
		if(!validateSave(current)) {
			return ERROR;
		}
		
		try {
			beforeSaving(current);
			T saved = save(current);
			afterSaving(saved);
			
			if (original == null) {
				logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
						"新增成功:{0}", current));
				addActionMessage("新增成功");
			} else {
				logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
						"修改成功, 修改前:{0}, 修改為:{1}", original, current));
				addActionMessage("修改成功");
			}
			current = null;
			original = null;
			
		} catch (Exception e) {
			logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
					"儲存錯誤:{0}, 原因:{1}", current, e.getMessage()));
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			return ERROR;
		}
		mode = Mode.SEARCH;
		return search();
	}

	protected void beforeSaving(T saving) {
		return;
	}

	public String delete() {
		try {
			T deleting = findByOid(current);
			if (!validateDelete(deleting)) {
				return ERROR;
			}
			
			if (deleting == null) {
				addActionError("資料已不存在, 請重新操作");
				return ERROR;
			}
			
			T deleted = delete(deleting);
			current = null;
			afterDeleting(deleted);
			
			logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
					"刪除成功:{0}", deleted));
			addActionMessage("刪除成功");
			
		} catch (Exception e) {
			logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
					"儲存錯誤:{0}, 原因:{1}", searchCriteria, e.getMessage()));
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			return ERROR;
		}
		mode = Mode.SEARCH;
		return search();
	}

	protected boolean validateDelete(T current2) {
		return true;
	}

	protected void afterDeleting(T deleted) throws Exception {
		return;
	}

	public String edit() {
		// search to modify entity
		T editing = null;
		try {
			// create a new entity
			editing = findByOid(current);
			if (editing == null) {
				editing = createDefaultNew();
			}
			
			beforeEditing(editing);
			// to keep another copy for original value
			original = findByOid(current);
			current = editing;
			
		} catch (Exception e) {
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			return ERROR;
		}
		
		mode = Mode.EDIT;
		return SUCCESS;
	}

	protected void beforeEditing(T editing) {
		return;
	}

	public String view() {
		try {
			current = findByOid(current);
		} catch (Exception e) {
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			return ERROR;
		}
		
		if (current == null) {
			addActionError("資料已不存在，請重新操作");
			return ERROR;
		}
		
		mode = Mode.VIEW;
		return SUCCESS;
	}

	protected T createDefaultSearchCriteria() throws Exception {
		return createDefaultNew();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected T createDefaultNew() throws Exception {
		return (T) ((Class) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0])
				.newInstance();
	}

	@SuppressWarnings("unchecked")
	protected T findByOid(T byEntity) throws Exception {
		return (T) administrationService.findEntityByOid(byEntity.getOid(), byEntity.getClass());
	}
	
	protected boolean validateSearch(T criteria) {
		return true;
	}
	
	protected abstract List<T> search(T criteria) throws Exception;

	protected boolean validateSave(T saving) {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	protected T save(T saving) throws Exception {
		return (T) administrationService.saveEntity(saving);
	}
	
	protected void afterSaving(T saved) throws Exception {
		return;
	}

	@SuppressWarnings("unchecked")
	protected T delete(T deleting) throws Exception {
		return (T) administrationService.removeEntity(deleting);
	}

	public T getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(T searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public List<T> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(List<T> searchResult) {
		this.searchResult = searchResult;
	}

	public T getCurrent() {
		return current;
	}

	public void setCurrent(T current) {
		this.current = current;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public T getOriginal() {
		return original;
	}

}