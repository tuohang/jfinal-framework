package com.tuohang.framework.jfinal.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.tuohang.framework.jfinal.model.query.Paging;
import com.tuohang.framework.jfinal.model.query.QueryCondition;
import com.tuohang.framework.jfinal.model.query.Sort;

/**
 * IbaseServcie接口
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.3
 * @param <M>
 *            Model model
 */
public interface IBaseService<M extends Model<M>> {

	/**
	 * 添加
	 * 
	 * @param model
	 * @return boolean：是否成功
	 */
	public boolean save(M model);

	/**
	 * 修改
	 * 
	 * @param model
	 * @return boolean：是否成功
	 */
	public boolean update(M model);

	/**
	 * 添加或修改
	 * 
	 * @param model
	 * @return boolean：是否成功
	 */
	public boolean saveOrUpdate(M model);

	/**
	 * 单个删除
	 * 
	 * @param id
	 * @return boolean：是否成功
	 */
	public boolean delete(String id);

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return M
	 */
	public M findById(String id);

	/**
	 * 根据条件查询（不分页，不排序）
	 * 
	 * @param condition
	 *            查询条件
	 * @return List<M>
	 */
	public List<M> findByParam(QueryCondition condition);

	/**
	 * 根据条件查询（分页但不排序）
	 * 
	 * @param condition
	 *            查询条件
	 * @param paging
	 *            分页条件
	 * @return List<M>
	 */
	public List<M> findByParam(QueryCondition condition, Paging paging);

	/**
	 * 根据条件查询（排序但不分页）
	 * 
	 * @param condition
	 *            查询条件
	 * @param sort
	 *            排序条件
	 * @return List<M>
	 */
	public List<M> findByParam(QueryCondition condition, Sort sort);

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
			Sort sort);

	/**
	 * 分页查询（不排序）
	 * 
	 * @param paging
	 *            分页条件
	 * @return Page<M>
	 */
	public Page<M> findByPage(Paging paging);

	/**
	 * 分页查询（排序）
	 * 
	 * @param paging
	 *            分页条件
	 * @param sort
	 *            排序条件
	 * @return Page<M>
	 */
	public Page<M> findByPage(Paging paging, Sort sort);

	/**
	 * 查询所有（排序）
	 * 
	 * @param sort
	 *            排序条件
	 * @return List<M>
	 */
	public List<M> findAll(Sort sort);

	/**
	 * 查询所有（不排序）
	 * 
	 * @return List<M>
	 */
	public List<M> findAll();
}
