package com.p2psys.black.service;

import java.util.List;

import com.p2psys.black.domain.LoanBlack;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

public interface LoanBlackService {
	/**
	 * 查询黑名单列表-分页查询
	 * @param param
	 * @param startPage
	 * @return
	 */
	public PageDataList getLoanBlackList(SearchParam param, int startPage);
	
	/**
	 * 查询黑名单列表-
	 * @param param
	 * @return
	 */
	public List<LoanBlack> getLoanBlackList(SearchParam param);
	/**
	 * 查询黑名单
	 * @param username
	 * @param identity
	 * @param mobile
	 * @param email
	 * @param qq
	 * @return
	 */
	public List<LoanBlack> getLoanBlack(String username, String identity,
			String mobile, String email, String qq);
	/**
	 * 设置为黑名单
	 * @param username
	 */
	public void addLoanBlack(String username);
}
