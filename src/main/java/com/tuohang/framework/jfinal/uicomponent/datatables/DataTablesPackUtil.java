package com.tuohang.framework.jfinal.uicomponent.datatables;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * DataTablesResult数据工具类
 * 
 * @author Lims
 * @date 2015年10月6日
 * @version 1.0
 */
public class DataTablesPackUtil {

	/**
	 * 将JFinal的page对象转为DataTables需要的数据格式
	 * 
	 * @param page
	 * @return DataTablesResult
	 */
	public static <M> DataTablesData<M> pack(Page<M> page) {
		return pack(page, null);
	}

	/**
	 * 将JFinal的page对象转为DataTables需要的数据格式
	 * 
	 * @param page
	 * @param sEcho
	 * @return DataTablesResult
	 */
	public static <M> DataTablesData<M> pack(Page<M> page, String sEcho) {
		DataTablesData<M> dtr = new DataTablesData<M>();
		if (StrKit.notBlank(sEcho))
			dtr.setsEcho(sEcho);
		dtr.setAaData(page.getList());
		dtr.setiTotalRecords(page.getTotalRow());
		dtr.setiTotalDisplayRecords(page.getTotalRow());
		return dtr;
	}

}
