package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.UserAmount;
import com.p2psys.domain.UserAmountApply;
import com.p2psys.domain.UserAmountLog;
import com.p2psys.model.SearchParam;

public interface UserAmountDao extends BaseDao {
	public void add(UserAmountApply amount);
	
	public List getUserAmountApply(long user_id);
	
	public List getAmountApplyListByUserid(long user_id,int start, int pernum, SearchParam param) ;
	
	public int getAmountApplyCountByUserid(long user_id,SearchParam param) ;
	
	public List getUserMountApply(int start,int pernum,SearchParam param);
	
	public int getUserMountApplyCount(SearchParam param);
	
	public UserAmount getUserAmount(long user_id);
	
	public UserAmountApply getUserAmountApplyById(long id);
	
	/**
	 * 修改信用额度
	 * @param totalVar
	 * @param useVar
	 * @param nouseVar
	 * @param amount
	 */
	public void updateCreditAmount(double totalVar, double useVar,double nouseVar,UserAmount amount);
	
	/**
	 * 更新额度申请
	 * @param apply
	 */
	public void updateApply(UserAmountApply apply);
	
	/**
	 * 新增额度变更记录
	 * @param log
	 */
	public void addAmountLog(UserAmountLog log);
	
	public UserAmount getUserAmountById(long id);
	
	public void addAmount(UserAmount amount);
	
	public int getUserAmountCount(SearchParam param);
	
	public List getUserAmount(int start,int pernum,SearchParam param);
	
	
}
