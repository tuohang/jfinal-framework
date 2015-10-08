package com.tuohang.framework.jfinal.model.query;

import java.io.Serializable;

/**
 * 分页查询参数
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.0
 */
public class Paging implements Serializable {

	private static final long serialVersionUID = -5877346675117302246L;

	/**
	 * 页码
	 */
	private Integer pageNo;

	/**
	 * 每页大小
	 */
	private Integer pageSize;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Paging(Integer pageNo, Integer pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

}
