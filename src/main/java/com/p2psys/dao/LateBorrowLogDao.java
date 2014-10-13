package com.p2psys.dao;

import java.util.List;
import java.util.Map;

/**
 * 催款记录dao
 
 *
 */

public interface LateBorrowLogDao extends BaseDao {
	/**
	 * 添加催款记录
	 * @param params
	 * @return
	 */
	public boolean addLateBorrowLogDetail(Map<String, Object> params);
	/**
	 * 获取  当前借款标的所有催款记录
	 */
	public List<Map<String, Object>> queryLateBorrowDetails(String borrow_id);
}
