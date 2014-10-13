package com.p2psys.creditassignment.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.creditassignment.domain.CreditAssignment;
import com.p2psys.creditassignment.model.CreditAssignmentModel;

public interface CreditAssignmentDao {
	
	/**
	 * 添加债权
	 * @param creditAssignment
	 */
	public void add(CreditAssignment creditAssignment);

	/**
	 * 根据主键查询债权
	 * @param caId
	 * @return
	 */
	public CreditAssignment getOne(long caId);
	
	/**
	 * 修改债权信息
	 * @param creditAssignment
	 */
	public void modifyCa(CreditAssignment creditAssignment);
	
	/**
	 * 查询债权转让信息
	 * @param map 查询参数
	 * @return
	 */
	public List<CreditAssignment> getList(Map<String, Object> map);
	
	
	/**
	 * 债权转让分页信息
	 * @param page
	 * @param max
	 * @param map 查询参数
	 * @return
	 */
	public List<CreditAssignmentModel> getPageList(int page , int max ,Map<String, Object> map);
	
	/**
	 * 债权转让分页统计
	 * @param map
	 * @return
	 */
	public int getPageCount(Map<String, Object> map);
	
}
