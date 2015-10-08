package com.tuohang.framework.jfinal.controller.plus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.tuohang.framework.jfinal.controller.interactive.BaseParamKey;
import com.tuohang.framework.jfinal.controller.interactive.BaseTableResult;
import com.tuohang.framework.jfinal.exception.ServiceBindAnnotationException;
import com.tuohang.framework.jfinal.model.ModelPlus;
import com.tuohang.framework.jfinal.model.query.Paging;
import com.tuohang.framework.jfinal.model.query.Sort;
import com.tuohang.framework.jfinal.service.IBaseService;
import com.tuohang.framework.jfinal.service.annotation.ServiceBind;

/**
 * ControllerKitPlusExt（在ControllerKitPlus基础上集成了Service）
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.4
 * @param <M>
 *            ModelExt
 * @param <S>
 *            Service
 */
public abstract class ControllerKitPlusExt<M extends ModelPlus<M>, S extends IBaseService<M>>
		extends ControllerKitPlus<M> {

	protected S service;
	private Class<S> serviceClass;

	public Class<S> getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(Class<S> serviceClass) {
		this.serviceClass = serviceClass;
	}

	public Class<M> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<M> modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * 在构造器中注入service值
	 */
	public ControllerKitPlusExt() {
		try {
			// 把class的变量保存起来，不用每次去取
			this.setServiceClass(getServiceClazz());
			this.service = getServiceImpl(serviceClass);
		} catch (ServiceBindAnnotationException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取S的class
	 * 
	 * @return Class<S>
	 */
	@SuppressWarnings("unchecked")
	private Class<S> getServiceClazz() {
		Type t = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		return (Class<S>) params[1];
	}

	/**
	 * 注入Service
	 * 
	 * @return
	 * @throws ServiceBindAnnotationException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	private S getServiceImpl(Class<S> serviceClass)
			throws ServiceBindAnnotationException, InstantiationException,
			IllegalAccessException {
		// 判断S是否包含ServiceBind注解
		if (!serviceClass.isAnnotationPresent(ServiceBind.class)) {
			service = null;
			log.error("未在" + serviceClass.getName() + "中找到ServcieBind注解！");
			throw new ServiceBindAnnotationException("未在"
					+ serviceClass.getName() + "中找到ServcieBind注解！");
		} else {
			// 获取注解值
			ServiceBind serAnnotation = serviceClass
					.getAnnotation(ServiceBind.class);

			// 加入事务支持
			// service = (S) TxBindProxy.newProxy(serAnnotation.value());
			service = (S) serAnnotation.value().newInstance();
		}
		return service;
	}

	/**
	 * 获取前台传的分页参数，封装为分页参数对象（内部使用）
	 * 
	 * @return Paging
	 */
	private Paging getPaging() {
		return getPaging(BaseParamKey.PAGE_NO, BaseParamKey.PAGE_SIZE);
	}

	/**
	 * 获取前台传递的排序参数，封装为排序参数对象（内部使用）
	 * 
	 * @return Sort
	 */
	private Sort getSort() {
		return getSort(BaseParamKey.ORDER_FIELD, BaseParamKey.ORDER_DIRECTION);
	}

	/**
	 * 通用按条件查询
	 */
	public void findByParam() {
		List<M> list = service.findByParam(getQueryCondition(), getPaging(),
				getSort());
		setAttr(modelClass.getSimpleName() + "List	", list);
		BaseTableResult<M> btr = new BaseTableResult<M>();
		btr.setData(list);
		renderJson(btr);
	}

	/**
	 * 查询所有
	 */
	public void findAll() {
		Sort sort = getSort();
		List<M> list = service.findAll(sort);
		setAttr(modelClass.getSimpleName() + "List	", list);
		BaseTableResult<M> btr = new BaseTableResult<M>();
		btr.setData(list);
		renderJson(btr);
	}

	/**
	 * 分页查询
	 */
	public void findByPage() {
		Page<M> page = service.findByPage(getPaging(), getSort());
		setAttr("pager", page);
		renderJson(page);
	}

	/**
	 * 通用添加
	 */
	public void save() {
		instanceModel();
		boolean result = service.save(model);
		if (result) {
			renderJsonContent(model.getStr(idFieldName));
		} else
			renderJsonFailMessage("添加操作失败！");
	}

	/**
	 * 通用修改
	 */
	public void update() {
		instanceModel();
		boolean result = service.update(model);
		if (result) {
			renderJsonContent(model);
		} else
			renderJsonFailMessage("修改操作失败！");
	}

	/**
	 * 通用删除
	 */
	public void delete() {
		String[] ids = getParaValues("ids");
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				if (service.delete(id)) {
					renderJsonSuccessMessage("删除操作成功！");
				} else {
					renderJsonFailMessage("删除操作失败！");
				}
			}
		}
	}

	/**
	 * 通用根据id查找
	 */
	public void findById() {
		String id = getPara();
		M m = service.findById(id);
		setAttr(modelClass.getSimpleName(), m);
		renderJson(m);
	}

}
