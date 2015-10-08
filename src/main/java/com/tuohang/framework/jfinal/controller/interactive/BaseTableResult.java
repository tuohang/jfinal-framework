package com.tuohang.framework.jfinal.controller.interactive;

import java.io.Serializable;
import java.util.List;

import com.tuohang.framework.jfinal.model.ModelPlus;

/**
 * 基础返回数据格式
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.0
 * @param <M>
 */
public class BaseTableResult<M extends ModelPlus<M>> implements Serializable {

	private static final long serialVersionUID = -1490437196653652743L;

	private List<M> data;

	public List<M> getData() {
		return data;
	}

	public void setData(List<M> data) {
		this.data = data;
	}

}
