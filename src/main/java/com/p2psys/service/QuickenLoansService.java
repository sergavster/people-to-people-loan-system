package com.p2psys.service;

import com.p2psys.domain.QuickenLoans;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

public interface QuickenLoansService {
	/**
	 * 获取快速贷款信息列表
	 * @return
	 */
	public PageDataList getList(int page, SearchParam p);
	
	/**
	 * 新增快速贷款信息
	 * @param quickenLoans
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
