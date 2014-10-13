package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.AccountRecharge;
import com.p2psys.model.PaymentSumModel;
import com.p2psys.model.SearchParam;

public interface RechargeDao extends BaseDao {
	/**
	 * 获取充值记录，没有分页
	 * @param user_id
	 * @return
	 */
	public List getList(long user_id);
	/**
	 * 获取充值记录的总数
	 * @param user_id
	 * @return
	 */
	public int getCount(long user_id,SearchParam param);
	/**
	 * 获取充值记录，有分页
	 * @param user_id
	 * @param startPage
	 * @return
	 */
	public List getList(long user_id,int start,int end,SearchParam param);
	
	public List getAllList(SearchParam param);
	
	/**
	 * 新增充值
	 * @param recharge
	 */
	public void addRecharge(AccountRecharge recharge);
	
	/**
	 * 批量充值
	 * @param list list
	 */
	void addExcelRecharge(List<AccountRecharge> list);
	
	/**
	 * 根据交易订单号获取充值记录
	 * @param trade_no
	 * @return
	 */
	public AccountRecharge getRechargeByTradeno(String trade_no);
	/**
	 * 更新充值状态
	 * @param status
	 * @param trade_no
	 */
	public int updateRecharge(int status,String returnText,String trade_no);
	/**
	 * 更新充值状态
	 * @param status
	 * @param trade_no
	 */
	public int updateRechargeByStatus(int status,String returnText,String trade_no);
	
	/**
	 * 修改充值费用问题
	 * @param fee
	 * @param id
	 */
	public void updateRechargeFee(double fee,long id);
	
	/**
	 * 获取所有充值记录数
	 * @param param
	 * @return
	 */
	public int getAllCount(SearchParam param);
	/**
	 * 获取所有充值记录，无分页
	 * @param user_id
	 * @param startPage
	 * @return
	 */
	public List getAllList(int start,int end,SearchParam param);
	
	
	public AccountRecharge getRecharge(long id);
	
	/**
	 * 更新审核充值记录
	 * @param status
	 * @param trade_no
	 */
	public void updateRecharge(AccountRecharge r);
	
	public double getLastRechargeSum(long id);
	
	public List getLastOfflineRechargeList(long user_id);
	
	public abstract List getLastRechargeList(long user_id, long type, int days);
	
	public double getLastRechargeSum(long user_id,int day);
	
	/**
	 * 统计充值总额无线下后台充值
	 * @param user_id
	 * @param day
	 */
	public double getRechargeSumWithNoAdmin(long user_id,int day);

	/**
	 * 统计不同类型某时间段的充值总额
	 * @param user_id
	 * @param type 充值类型 ,如果type为-1，则为所有类型
	 * @param start 统计时间开始时间，如果值为-1，查询所有
	 * @param end 统计时间结束时间，如果值为-1，查询所有
	 * @return
	 */
	public double getLastRechargeSum(long user_id,int type, long start_time , long end_time);
	
	public double getAccount_sum(SearchParam param,int ids) ;
	
	//获取用户第一次充值成功的有效充值金额
	public AccountRecharge getMinRecharge(long user_id,String status);
	//修改第一次充值奖励已给邀请人   1  ---yes_no
	public void updateAccountRechargeYes_no(AccountRecharge accountRecharge);
	/**
	 * 统计15天之内并且在提现当日的线上充值总额
	 * @param user_id
	 * @return
	 */
	public double getTodayOnlineRechargeTotal(long user_id,int day);
	/**
	 * 统计15天之内并且在提现当日的充值总额
	 * @param user_id
	 * @param day
	 * @return
	 */
	public double getTodayRechargeTotal(long user_id,int day);
	
	/**
	 * 每日充值总额
	 * @return
	 */
	public double getDayRechargeAccount();
	
	/**
	 * 每日网上充值总额
	 * @return
	 */
	public double getDayOnlineRechargeAccount();
	
	/**
	 * 每日线下充值总额
	 * @return
	 */
	public double getDayOfflineRechargeAccount();
	
	/**
	 * 每日第三方支付充值总额
	 * @return
	 */
	public List<PaymentSumModel> getDayPaymentAccount();
	/**
	 * 支付接口补单程序开发
	 * @param type
	 * @return
	 */
	//v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 start
	public List<AccountRecharge> getRechargeList(int type);
	//v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 end
		
}
