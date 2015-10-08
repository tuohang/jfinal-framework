package com.tuohang.framework.jfinal.uicomponent.datatables;

import java.io.Serializable;
import java.util.List;

/**
 * dataTables封装对象
 * 
 * @author Lims
 * @date 2015年10月6日
 * @version 1.0
 * @param <M>
 *            model
 */
public class DataTablesData<M> implements Serializable {

	private static final long serialVersionUID = -8369594860882499954L;

	/**
	 * 数量
	 */
	private long iTotalRecords;
	private long iTotalDisplayRecords;

	/**
	 * 表示第几次查询
	 */
	private String sEcho;

	/**
	 * 数据
	 */
	private List<M> aaData; // rows

	public long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	public List<M> getAaData() {
		return aaData;
	}

	public void setAaData(List<M> aaData) {
		this.aaData = aaData;
	}

	public DataTablesData() {
	}

	public DataTablesData(long iTotalRecords, long iTotalDisplayRecords,
			String sEcho, List<M> aaData) {
		super();
		this.iTotalRecords = iTotalRecords;
		this.iTotalDisplayRecords = iTotalDisplayRecords;
		this.sEcho = sEcho;
		this.aaData = aaData;
	}

}
