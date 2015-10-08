package com.tuohang.framework.jfinal.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.tuohang.framework.jfinal.ext.plugin.tablebind.TableBind;
import com.tuohang.framework.jfinal.kits.UUIDGenerator;
import com.tuohang.framework.jfinal.model.ModelPlus;
import com.tuohang.framework.jfinal.model.query.Paging;
import com.tuohang.framework.jfinal.model.query.QueryCondition;
import com.tuohang.framework.jfinal.model.query.Sort;

/**
 * BaseService
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.3
 * @param <M>
 *            Model model
 */
public class BaseService<M extends ModelPlus<M>> implements IBaseService<M> {

	public final Logger log = Logger.getLogger(getClass());

	private Class<M> modelClass;

	/**
	 * 表示该model对应的数据库表名，如果没有在Model中的@TableBind注解中设置值，默认为model名小写
	 */
	protected String tableName;

	/**
	 * 表示主键字段名字，如果没有在Model中的@TableBind注解中设置值，默认为"id"
	 */
	protected String idFieldName;

	public M model;

	public Class<M> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<M> modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * 实例化BaseService得到modelClass
	 */
	public BaseService() {
		this.setModelClass(getClazz());
		try {
			initAboutTable(modelClass);
			// 实例化model
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			log.error("实例化Model失败！");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("实例化Model失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 初始化tableName和idFiledName
	 */
	private void initAboutTable(Class<M> modelClass) {
		// 判断Model是否使用了@TableBind注解
		if (modelClass.isAnnotationPresent(TableBind.class)) {
			TableBind tBind = modelClass.getAnnotation(TableBind.class);

			// 判断tableName是否为空
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
	 * @return M
	 */
	@SuppressWarnings("unchecked")
	private Class<M> getClazz() {
		Type t = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		return (Class<M>) params[0];
	}

	@Override
	public boolean save(M model) {
		String id = model.get(idFieldName);
		if (StrKit.isBlank(id)) {
			model.set(idFieldName, UUIDGenerator.getUUID());
		}
		return model.save(model);
	}

	@Override
	public boolean update(M model) {
		return model.update(model);
	}

	@Override
	public boolean saveOrUpdate(M model) {
		return model.saveOrUpdate(model);
	}

	@Override
	public boolean delete(String id) {
		return model.delete(id);
	}

	@Override
	public M findById(String id) {
		return model.findById(id);
	}

	@Override
	public List<M> findByParam(QueryCondition condition) {
		return model.findByParam(condition);
	}

	@Override
	public List<M> findByParam(QueryCondition condition, Paging paging) {
		return model.findByParam(condition, paging);
	}

	@Override
	public List<M> findByParam(QueryCondition condition, Sort sort) {
		return model.findByParam(condition, sort);
	}

	@Override
	public List<M> findByParam(QueryCondition condition, Paging paging,
			Sort sort) {
		return model.findByParam(condition, paging, sort);
	}

	@Override
	public Page<M> findByPage(Paging paging) {
		return model.findByPage(paging);
	}

	@Override
	public Page<M> findByPage(Paging paging, Sort sort) {
		return model.findByPage(paging, sort);
	}

	@Override
	public List<M> findAll(Sort sort) {
		return model.findAll(sort);
	}

	@Override
	public List<M> findAll() {
		return model.findAll();
	}

}
