package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.QuickenLoans;
import com.p2psys.model.SearchParam;

/**
 * 快速贷款相关的Dao操作类
 
 * 
 */
public interface QuickenLoansDao extends BaseDao {
	/**
	 * 查询所有的快速贷款信息
	 * @return
	 */
	public List getList(int page, int max, SearchParam p);
	
	public int getSearchCard(SearchParam p);
	
	/**
	 * 新增快速贷款信息
	 * @param quickenLoans
	 * @return 
	 */
	public void addQuickenLoans(QuickenLoans quickenLoans);
	
	/**
	 * 根据ID取得对应的快速贷款信息
	 * @param loansId
	 * @return
	 */
	public QuickenLoans getLoansById(int loansId);
	
	/**
	 * 删除
	 * @param loansId
	 */
	public void delQuickenLoans(int loansId);
}
