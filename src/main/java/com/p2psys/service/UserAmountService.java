package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.UserAmount;
import com.p2psys.domain.UserAmountApply;
import com.p2psys.domain.UserAmountLog;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

public interface UserAmountService {

	/**
	 * 获取信用额度申请列表
	 * @param user_id
	 * @return
	 */
	public List getUserAmountApply(long user_id);
	
	/**
	 * 新增信用额度申请
	 * @param amount
	 */
	public void add(UserAmountApply amount);
	/**
	 * 获取所有的额度申请
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getUserAmountApply(int page,SearchParam param);
	
	public PageDataList getAmountApplyByUserid(long user_id,int page, SearchParam param);
	
	/**
	 * 
	 * @param user_id
	 * @return
	 */
	public UserAmount getUserAmount(long user_id);
	
	/**
	 * 获取信用额度申请列表
	 * @param id
	 * @return
	 */
	public UserAmountApply getUserAmountApplyById(long id);
	
	/**
	 * 审核额度申请
	 * @param amount
	 * @param apply
	 */
	public void verifyAmountApply(UserAmountApply apply,UserAmountLog log);
	
	public PageDataList getUserAmount(int page,SearchParam param);
	
	//v1.6.6.2 RDPROJECT-360 liukun 2013-10-25 start
	/**
	 * 
	 * @param long user_id
	 * @param double amount 操作的额度数额，只为正
	 * @param String opType 额度操作类型
	 * @return
	 */
	public boolean updateUserAmount(long user_id, double amount, String opType);	
	//v1.6.6.2 RDPROJECT-360 liukun 2013-10-25 end
}
