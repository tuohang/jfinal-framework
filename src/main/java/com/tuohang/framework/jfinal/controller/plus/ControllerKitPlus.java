package com.tuohang.framework.jfinal.controller.plus;

import com.jfinal.kit.StrKit;
import com.tuohang.framework.jfinal.controller.ControllerKit;
import com.tuohang.framework.jfinal.model.ModelPlus;
import com.tuohang.framework.jfinal.model.query.DIRECTION;
import com.tuohang.framework.jfinal.model.query.Paging;
import com.tuohang.framework.jfinal.model.query.QueryCondition;
import com.tuohang.framework.jfinal.model.query.Sort;

/**
 * ControllerKitPlus，拓展ControllerKit
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.5
 * @param <M>
 *            ModelPlus
 */
public class ControllerKitPlus<M extends ModelPlus<M>> extends ControllerKit<M> {
	
	/**
	 * 获取查询条件
	 * 
	 * @return 查询条件queryCondition
	 */
	protected QueryCondition getQueryCondition() {
		instanceModel();
		// 默认全都以=号来查询
		QueryCondition condition = new QueryCondition();
		condition.modelToCondition(model);
		return condition;
	}

	/**
	 * 获取前台传的分页参数，封装为分页参数对象
	 * 
	 * @param PAGE_NO_KEY
	 *            页码key
	 * @param PAGE_SIZE_KEY
	 *            页大小key
	 * @return Paging
	 */
	protected Paging getPaging(String PAGE_NO_KEY, String PAGE_SIZE_KEY) {
		// 分页
		Integer pageNo = getParaToInt(PAGE_NO_KEY);
		Integer pageSize = getParaToInt(PAGE_SIZE_KEY);

		Paging paging = null;
		if (pageNo != null && pageSize != null) {
			paging = new Paging(pageNo, pageSize);
		}
		return paging;
	}

	/**
	 * 取前台传递的排序参数，封装为排序参数对象
	 * 
	 * @param ORDER_FIELD_KEY
	 *            排序字段key
	 * @param ORDER_DIRE_KEY
	 *            排序方向key
	 * @return Sort
	 */
	protected Sort getSort(String ORDER_FIELD_KEY, String ORDER_DIRE_KEY) {
		// 是否排序
		String orderField = getPara(ORDER_FIELD_KEY);
		Sort sort = null;
		if (StrKit.notBlank(orderField)) {
			String direction = getPara(ORDER_DIRE_KEY, DIRECTION.ASC);
			sort = new Sort(orderField, direction);
		}
		return sort;
	}
}
