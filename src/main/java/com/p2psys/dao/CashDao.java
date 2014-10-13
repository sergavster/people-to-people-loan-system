package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.AccountCash;
import com.p2psys.model.SearchParam;

public interface CashDao extends BaseDao {
	/**
	 * 获取用户的提现记录
	 * @param user_id
	 * @return
	 */
	public List getAccountCashList(long user_id);
	
	/**
	 * 获取用户的提现记录
	 * @param user_id
	 * @return
	 */
	public int  getAccountCashCount(long user_id,SearchParam param);
	
	/**
	 * 获取用户的提现记录
	 * @param user_id
	 * @return
	 */
	public List getAccountCashList(long user_id,int start,int end,SearchParam param);
	
	/**
	 * 新增用户提现
	 * @param cash
	 * @return
	 */
	public AccountCash addCash(AccountCash cash);
	
	/**
	 * 获取所有的提现记录数量
	 * @param param
	 * @return
	 */
	public int getAllCashCount(SearchParam param);
	
	/**
	 * 获取所有的提现记录
	 * @param page
	 * @param param
	 * @return
	 */
	public List getAllCashList(int page,int pernum,SearchParam param);
	
	public List getAllCashList(SearchParam param);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public AccountCash getAccountCash(long id);
	
	public void updateCash(AccountCash cash);
	
	// v1.6.5.3 RDPROJECT-174 xx 2013.09.16 start
	/**
	 * 提现审核
	 * @param cash
	 * @return
	 */
	public int verifyCash(AccountCash cash, int preStatus);
	// v1.6.5.3 RDPROJECT-174 xx 2013.09.16 end
	
	public int getAccountCashNum(long user_id, int status);
	
	// v1.6.5.3 RDPROJECT-96 xx 2013.09.10 start
	/**
	 * 查询当日提现金额（包括提现成功的和正在申请的）
	 * @param user_id
	 * @param status
	 * @return
	 */
	public double getAccountCashDailySum(long user_id);
	// v1.6.5.3 RDPROJECT-96 xx 2013.09.10 end
	/**
	 * 获取正在申请中的提现记录
	 * @param user_id
	 * @return
	 */
	public List getAccountCashList(long user_id,int status);
	
	public List getAccountCashList(long user_id,int status,long startTime);
	
	public double getAccountCashSum(long user_id, int status);
	
	public double getAccountCashSum(long user_id, int status,long startTime);
	
	public double getAccountCashSum(long user_id, int status,long startTime,long endTime);
	
	/**
	 * 获取已经申请的提现，包括已审核和未审核的
	 * @param user_id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public double getAccountApplyCashSum(long user_id, long startTime,long endTime);
	
	public double getSumTotal();
	public double getSumUseMoney();
	public double getSumNoUseMoney();
	public double getSumCollection();
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	//垫付资金
//	public void getAdvance_insert(Advanced advanced);
//	public List getAdvanceList();
//	public void getAdvance_update(Advanced advanced);
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	
	
	public double getAccountNoCashSum(long user_id, int status,long startTime);
	
	public AccountCash addFreeCash(AccountCash cash);

	
}
