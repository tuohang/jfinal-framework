package com.tuohang.framework.jfinal.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.tuohang.framework.jfinal.ext.plugin.tablebind.TableBind;
import com.tuohang.framework.jfinal.kits.UUIDGenerator;
import com.tuohang.framework.jfinal.model.query.Paging;
import com.tuohang.framework.jfinal.model.query.QueryCondition;
import com.tuohang.framework.jfinal.model.query.Sort;

/**
 * 增强model
 * 
 * @author Lims
 * @date 2015年10月7日
 * @version 1.2
 * @param <M>
 *            modelPlus
 */
public class ModelPlus<M extends ModelPlus<M>> extends Model<M> {

	private static final long serialVersionUID = -1676245940397130983L;

	/**
	 * 日志对象
	 */
	public static Logger log = Logger.getLogger(ModelPlus.class);

	/**
	 * modelClass
	 */
	private Class<M> modelClass;

	/**
	 * 表示该model对应的数据库表名，如果没有在Model中的@TableBind注解中设置值，默认为model名小写
	 */
	protected String tableName;

	/**
	 * 表示主键字段名字，如果没有在Model中的@TableBind注解中设置值，默认为"id"
	 */
	protected String idFieldName;

	public Class<M> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<M> modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * 实例化ModelPlus得到modelClass
	 */
	public ModelPlus() {
		this.setModelClass(getClazz());
		initAboutTable();
	}

	/**
	 * 初始化tableName和idFiledName
	 */
	private void initAboutTable() {
		// 判断Model是否使用了@TableBind注解
		if (modelClass.isAnnotationPresent(TableBind.class)) {
			TableBind tBind = modelClass.getAnnotation(TableBind.class);

			// 判断value（tableName）是否为空
			if (StrKit.notBlank(tBind.value())) {
				this.tableName = tBind.value();
			} else
				this.tableName = modelClass.getSimpleName().toLowerCase();

			// 判断pkName是否为空
			if (StrKit.notBlank(tBind.pkName()))
				this.idFieldName = tBind.pkName();
			else
				this.idFieldName = "id";
		} else {
			// 如果没有该注解，默认表名为该model名
			this.idFieldName = "id";
			this.tableName = modelClass.getSimpleName().toLowerCase();
		}
	}

	/**
	 * 获取M的class
	 * 
	 * @return Class<M>
	 */
	@SuppressWarnings("unchecked")
	private Class<M> getClazz() {
		Type t = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		return (Class<M>) params[0];
	}

	/**
	 * 添加
	 * 
	 * @param model
	 * @return boolean：是否成功
	 */
	public boolean save(M model) {
		return model.save();
	}

	/**
	 * 修改
	 * 
	 * @param model
	 * @return boolean：是否成功
	 */
	public boolean update(M model) {
		return model.update();
	}

	/**
	 * 添加或修改
	 * 
	 * @param model
	 * @return boolean：是否成功
	 */
	public boolean saveOrUpdate(M model) {
		String id = model.get(idFieldName);
		boolean result = false;
		if (StrKit.isBlank(id)) {
			// 添加
			model.set(idFieldName, UUIDGenerator.getUUID());
			result = model.save();
		} else {
			result = model.update();
		}
		return result;
	}

	/**
	 * 单个删除
	 * 
	 * @param id
	 * @return boolean：是否成功
	 */
	public boolean delete(String id) {
		return deleteById(id);
	}

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return M
	 */
	public M findById(String id) {
		return findById(id);
	}

	/**
	 * 根据条件查询（不分页，不排序）
	 * 
	 * @param condition
	 *            查询条件
	 * @return List<M>
	 */
	public List<M> findByParam(QueryCondition condition) {
		return find(condition, null, null);
	}

	/**
	 * 根据条件查询（分页但不排序）
	 * 
	 * @param condition
	 *            查询条件
	 * @param paging
	 *            分页条件
	 * @return List<M>
	 */
	public List<M> findByParam(QueryCondition condition, Paging paging) {
		return find(condition, paging, null);
	}

	/**
	 * 根据条件查询（排序但不分页）
	 * 
	 * @param condition
	 *            查询条件
	 * @param sort
	 *            排序条件
	 * @return List<M>
	 */
	public List<M> findByParam(QueryCondition condition, Sort sort) {
		return find(condition, null, sort);
	}

	/**
	 * 根据条件查询（分页且排序）
	 * 
	 * @param condition
	 *            查询条件
	 * @param paging
	 *            分页条件
	 * @param sort
	 *            排序条件
	 * @return List<M>
	 */
	public List<M> findByParam(QueryCondition condition, Paging paging,
			Sort sort) {
		// 总共分为8种情况

		// 1.三个都不为空
		if (!isBlankQueryCondition(condition) && paging != null && sort != null)
			return find(condition, paging, sort);

		// 2.三个都为空
		if (isBlankQueryCondition(condition) && paging == null && sort == null)
			return findAll();

		// 3.两个为空，一个不为空
		// ①1,2为空，3不空
		if (isBlankQueryCondition(condition) && paging == null && sort != null)
			return findAll(sort);
		// ②2,3为空，1不空
		if (!isBlankQueryCondition(condition) && paging == null && sort == null)
			return findByParam(condition);
		// ③1,3为空，2不空
		if (isBlankQueryCondition(condition) && paging != null && sort == null)
			return findByPage(paging).getList();

		// 4.两个不为空，一个为空
		// ①1为空，2,3不空
		if (isBlankQueryCondition(condition) && paging != null && sort != null)
			return findByPage(paging, sort).getList();
		// ②2为空，1,3不空
		if (!isBlankQueryCondition(condition) && paging == null && sort != null)
			return findByParam(condition, sort);
		// ③3为空，1,2不空
		if (!isBlankQueryCondition(condition) && paging != null && sort == null)
			return findByParam(condition, paging);

		return null;
	}

	/**
	 * 分页查询（排序）
	 * 
	 * @param paging
	 *            分页条件
	 * @param sort
	 *            排序条件
	 * @return Page<M>
	 */
	public Page<M> findByPage(Paging paging, Sort sort) {
		int pageNo = 1;
		int pageSize = 10;
		if (paging != null) {
			if (paging.getPageNo() != null)
				pageNo = paging.getPageNo();
			if (paging.getPageSize() != null)
				pageSize = paging.getPageSize();
		} else {
			log.warn("由于传递的Paging对象为空，默认pageNo为：" + pageNo + "，pageSize为："
					+ pageSize);
		}
		String selectHead = "select *";
		StringBuilder sb = new StringBuilder();
		sb.append("from " + tableName);
		String selectBody = sb.toString() + getSortSql(sort);
		return paginate(pageNo, pageSize, selectHead, selectBody);
	}

	/**
	 * 分页查询（不排序）
	 * 
	 * @param paging
	 *            分页条件
	 * @return Page<M>
	 */
	public Page<M> findByPage(Paging paging) {
		return findByPage(paging, null);
	}

	/**
	 * 查询所有（排序）
	 * 
	 * @param sort
	 *            排序条件
	 * @return List<M>
	 */
	public List<M> findAll(Sort sort) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from " + tableName);
		String sql = sb.toString() + getSortSql(sort);
		return find(sql);
	}

	/**
	 * 查询所有（不排序）
	 * 
	 * @return List<M>
	 */
	public List<M> findAll() {
		return findAll(null);
	}

	/**
	 * 工具方法：根据Sort生成排序语句
	 * 
	 * @param sort
	 * @return String：排序语句
	 */
	private String getSortSql(Sort sort) {
		StringBuilder sb = new StringBuilder();
		if (sort != null) {
			String orderField = sort.getOrderField();
			String direction = sort.getDirection();
			sb.append(" order by " + orderField);
			if (StrKit.notBlank(direction))
				sb.append(" " + direction);
		}
		String sortSql = sb.toString();
		return sortSql;
	}

	/**
	 * 工具方法（condition不为空）
	 * 
	 * @param condition
	 * @param paging
	 * @param sort
	 * @return List<M>
	 */
	private List<M> find(QueryCondition condition, Paging paging, Sort sort) {

		List<Object> paramList = condition.getParamList();
		// 按照两种条件
		if (paging == null) {
			String sql = "select * from " + tableName + " where 1=1 "
					+ condition.getSql() + getSortSql(sort);
			return find(sql, paramList.toArray());
		} else {
			int pageNo = 1;
			int pageSize = 10;
			if (paging.getPageNo() != null)
				pageNo = paging.getPageNo();
			if (paging.getPageSize() != null)
				pageSize = paging.getPageSize();
			String selectHead = "select *";
			StringBuilder sb = new StringBuilder();
			sb.append("from " + tableName + " where 1=1 " + condition.getSql());
			String selectBody = sb.toString() + getSortSql(sort);
			Page<M> page = paginate(pageNo, pageSize, selectHead, selectBody,
					paramList.toArray());
			return page.getList();
		}
	}

	/**
	 * 工具方法，判断queryCondtion是否为空
	 * 
	 * @param condition
	 * @return boolean：true表示为空，false表示不为空
	 */
	private boolean isBlankQueryCondition(QueryCondition condition) {
		if (StrKit.isBlank(condition.getSql())
				&& condition.getParamList() == null)
			return true;
		else
			return false;
	}

}
