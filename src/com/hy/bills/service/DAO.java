package com.hy.bills.service;

import java.util.List;

public interface DAO<T> {
	/**
	 * 保存实体
	 * @param obj
	 */
	public void save(T obj);
	
	/**
	 * 更新实体
	 * @param obj
	 */
	public void update(T obj);
	
	/**
	 * 删除实体
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 查找实体
	 * @param id
	 * @return
	 */
	public T find(Integer id);

	/**
	 * 获取分页数据
	 * @param offset
	 * @param maxResult
	 * @return
	 */
	public List<T> getScrollData(Integer offset, Integer maxResult);
}
