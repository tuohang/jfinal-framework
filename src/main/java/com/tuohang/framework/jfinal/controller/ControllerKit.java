package com.tuohang.framework.jfinal.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;
import com.tuohang.framework.jfinal.controller.interactive.BaseResult;
import com.tuohang.framework.jfinal.controller.interactive.ResultType;
import com.tuohang.framework.jfinal.controller.interactive.STATECODE;
import com.tuohang.framework.jfinal.ext.plugin.tablebind.TableBind;

/**
 * ControllerKit，封装基本对象，可自行扩展
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.4
 * @param <M>
 *            Model
 */
public abstract class ControllerKit<M extends Model<M>> extends Controller {

	protected final Logger log = Logger.getLogger(getClass());

	protected M model;
	protected Class<M> modelClass;

	/**
	 * 表示该model对应的数据库表名，一般情况建议不使用该值
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
	 * 在构造器中注入值
	 */
	public ControllerKit() {
		try {
			// 把class的变量保存起来，不用每次去取
			this.setModelClass(getModelClazz());
			this.model = getModelImpl(modelClass);
			initAboutTable();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
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
	 * 实例化model（这个方式无法获取到前台封装过来的model）
	 * 
	 * @param modelClass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private M getModelImpl(Class<M> modelClass) throws InstantiationException,
			IllegalAccessException {
		model = modelClass.newInstance();
		return model;
	}

	/**
	 * 获取M的class
	 * 
	 * @return Class<M>
	 */
	@SuppressWarnings("unchecked")
	private Class<M> getModelClazz() {
		Type t = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		return (Class<M>) params[0];
	}

	/**
	 * 给model赋值（从前台获取的参数封装为model）
	 */
	protected void instanceModel() {
		model.setAttrs(getModel(modelClass));
	}

	/**
	 * 输出Json成功提示消息
	 * 
	 * @param message
	 *            提示消息
	 */
	public void renderJsonSuccessMessage(String message) {
		renderJsonMessage(ResultType.SUCCESS, message);
	}

	/**
	 * 输出Json成功提示消息
	 */
	public void renderJsonSuccessMessage() {
		renderJsonMessage(ResultType.SUCCESS);
	}

	/**
	 * 输出json失败提示消息
	 * 
	 * @param message
	 *            提示消息
	 */
	public void renderJsonFailMessage(String message) {
		renderJsonMessage(ResultType.FAIL, message);
	}

	/**
	 * 输出Json失败提示消息
	 */
	public void renderJsonFailMessage() {
		renderJsonMessage(ResultType.FAIL);
	}

	/**
	 * 输出Json警告提示消息
	 * 
	 * @param message
	 */
	public void renderJsonWarnMessage(String message) {
		renderJsonMessage(ResultType.WARN, message);
	}

	/**
	 * 输出Json警告提示消息
	 */
	public void renderJsonWarnMessage() {
		renderJsonMessage(ResultType.WARN, null);
	}

	/**
	 * 输出操作提示消息
	 * 
	 * @param type
	 *            结果类型
	 * @param message
	 *            输出的提示消息
	 */
	public void renderJsonMessage(String type, String message) {
		BaseResult brJson = getBaseResultJson(type);
		if (StrKit.notBlank(message))
			brJson.setPromptInfo(message);
		renderJson(brJson);
	}

	/**
	 * 输出成功提示消息，并输出内容Object content
	 * 
	 * @param content
	 *            内容
	 */
	public void renderJsonContent(Object content) {
		BaseResult brJson = new BaseResult();
		brJson.setContent(content);
		brJson.setType(ResultType.SUCCESS);
		brJson.setStateCode(STATECODE.SUCCESS);
		renderJson(brJson);
	}

	/**
	 * 输出操作提示消息
	 * 
	 * @param type
	 *            结果类型
	 */
	public void renderJsonMessage(String type) {
		renderJsonMessage(type, null);
	}

	/**
	 * 获取baseResultJson（内部方法）
	 * 
	 * @param type
	 *            提示消息类型
	 * @return
	 */
	private BaseResult getBaseResultJson(String type) {
		BaseResult brJson = new BaseResult();
		if (type == null)
			brJson.setType(ResultType.SUCCESS);
		else
			brJson.setType(type);
		if (brJson.getType().equals(ResultType.SUCCESS)) {
			brJson.setStateCode(STATECODE.SUCCESS);
			brJson.setPromptInfo("操作成功");
		} else if (brJson.getType().equals(ResultType.FAIL)) {
			brJson.setStateCode(STATECODE.ERROR);
			brJson.setPromptInfo("操作失败");
		} else {
			brJson.setStateCode(STATECODE.WARN);
			brJson.setPromptInfo("警告");
		}
		return brJson;
	}

}
