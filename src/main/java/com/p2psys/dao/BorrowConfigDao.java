package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.BorrowConfig;

public interface BorrowConfigDao extends BaseDao {
	/**
	 * 借款标配置列表
	 * @return
	 */
	public List getList();
	/**
	 * 借款标配置通过id显示
	 */
	public BorrowConfig getBorrowConfigById(long id);
	/**
	 * 借款标配置通过id修改
	 */
	public void updateBorrowConfig(BorrowConfig borrowConfig);
}
