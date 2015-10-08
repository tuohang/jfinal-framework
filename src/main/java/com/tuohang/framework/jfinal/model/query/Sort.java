package com.tuohang.framework.jfinal.model.query;

import java.io.Serializable;

/**
 * 排序查询参数
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.0
 */
public class Sort implements Serializable {

	private static final long serialVersionUID = 3220399892363602028L;

	/**
	 * 排序字段
	 */
	private String orderField;

	/**
	 * 排序方式
	 */
	private String direction;

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Sort(String orderField, String direction) {
		super();
		this.orderField = orderField;
		this.direction = direction;
	}

}
