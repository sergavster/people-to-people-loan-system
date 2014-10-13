package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Account;
import com.p2psys.domain.AccountBank;
import com.p2psys.domain.Huikuan;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAmount;
import com.p2psys.model.HuikuanModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.account.AccountModel;
import com.p2psys.model.account.AccountReconciliationModel;
import com.p2psys.model.account.AccountSumModel;
import com.p2psys.model.account.BorrowSummary;
import com.p2psys.model.account.CollectSummary;
import com.p2psys.model.account.InvestSummary;
import com.p2psys.model.account.RepaySummary;


public interface AccountDao extends BaseDao{
//	
//	/**
//	 * 个人账户统计
//	 * @param user_id
//	 * @return
//	 */
//	public List getAccountLogSummary(long user_id);
	
//	public UserAccountSummary getUserAccountSummary(long user_id);
//	/**
//	 * 个人借款统计
//	 * @param user_id
//	 * @return
//	 */
//	public double getBorrowSum(long user_id);
	
//	/**
//	 * 个人借款次数
//	 * @param id
//	 * @return
//	 */
//	public int getBorrowTimes(long id);
	
//	/**
//	 * 
//	 * @param user_id
//	 * @return
//	 */
//	public double getBorrowAmount(long user_id);
	/**
	 * 个人信用额度
	 * @param user_id
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
	 */
	public UserAmount getUserAmount(long user_id);
	
	/**
	 * 个人账户信息包括银行卡信息
	 * @param user_id
	 * @return
	 */
	public AccountModel getAccount(long user_id);
	public Account getAccountByUserId(long user_id);
//	public AccountModel getAccount_hongbao(long user_id) ;
	public AccountModel getAccountByBankAccount(long user_id,String bankAccount);

//	/**
//	 * 获取个人利息统计
//	 * @param user_id
//	 * @return
//	 */
//	public List getInterest(long user_id);
//	/**
//	 * 获取个人最近还款信息统计
//	 * @param user_id
//	 * @return
//	 */
//	public Newpay getNewpay(long user_id);
	
//	/**
//	 * 获取个人最近收款信息统计
//	 * @param user_id
//	 * @return
//	 */
//	public NewCollection getNewCollection(long user_id);
//	/**
//	 * 
//	 * @param user_id
//	 * @return
//	 */
//	public List getWaitpayment(long user_id);
	
	/**
	 * 获取用户的客服信息
	 * @param user_id
	 * @return
	 */
	public User getKf(long user_id);
	
	
	
	
	
	/**
	 * 新增用户账户信息
	 * @param account
	 * @return
	 */
	public Account addAccount(Account account);
	/**
	 * 新增用户的银行卡信息
	 * @param bank
	 * @return
	 */
	public AccountBank addBank(AccountBank bank);
	/**
	 * 统计用户银行卡数
	 * @param user_id
	 * @return
	 */
	public int getAccountBankCount(long user_id);
	/**
	 * 更新用户银行卡信息
	 * @param bank
	 * @return
	 */
	public AccountBank updateBank(AccountBank bank);
	
	/**
	 * 更新用户的账户信息
	 * @param act
	 */
	public void updateAccount(Account act);
	/**
	 * 更新用户的账户信息，防止数据库幻读
	 * @param act
	 */
	public void updateAccount(double totalVar,double useVar,double nouseVar,long user_id);
	
	public void updateAccount(double totalVar,double useVar,double nouseVar,long user_id,double hongbao,int aa);

	public int updateAccountNotZero(double totalVar,double useVar,double nouseVar,long user_id);
	
	public void updateAccount(double totalVar,double useVar,double nouseVar,double collectVar,long user_id);
	
	/**
	 * 管理后台中获取所有账号列表
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List getUserAcount(int start,int end,SearchParam param);

	/**
	 * 管理后台中获取所有dw_account_tj 表中用户的账户信息
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List getUserOneDayAcount(int start,int end,SearchParam param);

	/**
	 * 管理后台中获取所有账号的个数
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	
	public int getUserAccountCount(SearchParam param);

	/**
	 * 管理后台中获取所有dw_account_tj 表中用户的账户信息
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public int getUserOneDayAccountCount(SearchParam param);

	public int getTiChengAccountCount(SearchParam param);
	public List getTiChengAccountList(int start,int end,SearchParam param);
	public List getFriendTiChengAccountList(int start, int end, SearchParam param) ;
	/**
	 * 好友提成
	 * @param param
	 * @return
	 */
	public int getFriendTiChengAccountCount(SearchParam param);
	public List getFriendTiChengAcount(SearchParam param) ;
	public List getUserAcount(SearchParam param);
	
	public double getSum(String sql,long user_id);
	
	public int getCount(String sql,long user_id);
	
	/**
	 * 包含银行卡信息
	 * @param user_id
	 * @return
	 */
	public List<AccountModel> getAccountList(int start,int end,SearchParam param);
	
	/**
	 * 查询提现银行卡信息
	 */
	public List<AccountModel> getAccountList(SearchParam param);
	
	public int getAccountCount(SearchParam param);
	/**
	 * 申请回款
	 * 
	 */
	public void addHuikuanMoney(Huikuan huikuan);
	public List huikuanlist(int start,int end,SearchParam param);
	public List huikuanlist(SearchParam param);
	public int gethuikuanCount(SearchParam param);
	public HuikuanModel viewhuikuan(int id);
	public void verifyhuikuan(Huikuan huikuan);
	public double gethuikuanSum(long user_id,int status);
	public double gethuikuanSum(long user_id,int status,long start);
	
	/**
	 * 回款续投奖励合计
	 * @param user_id
	 * @param status 状态
	 * @param start_time 开始时间
	 * @param end_time 结束时间
	 * @return
	 */
	public double getHuikuanRewardSum(long user_id,int status,long start_time , long end_time);
	
	public HuikuanModel getHuikuanByCashid(long cash_id);
	public double gethuikuanSum(long user_id);
	
	/**
	 * 信达投流转天标限制（流转标，抵押标）月标达到限额
	 */
	public double getFlowMonthTenderCollection(long user_id);
//	/**
//	 * 信达投流转天标限制（流转标，抵押标）天标达到限额
//	 */
//	public double getFlowDayTenderCollection(long user_id);
	/**
	 * 还款汇总信息
	 * @param user_id
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
	 */
	public RepaySummary getRepaySummary(long user_id);
	/**
	 * 借款信息汇总
	 * @param user_id
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
	 */
	public BorrowSummary getBorrowSummary(long user_id);
	/**
	 * 投资汇总信息
	 * @param user_id
	 * @return
	 */
	public InvestSummary getInvestSummary(long user_id);
	/**
	 * 待收汇总信息
	 * @param user_id
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
	 */
	public CollectSummary getCollectSummary(long user_id);
	public double getCollectionSum(long user_id,int status);
	
	public double getCollectionSum(long user_id,int status,long startTime);
	
	public double getCollectionSumNoJinAndSecond(long user_id, int status, long startTime);
	/**
	 * 回款统计
	 * @param user_id
	 * @param status
	 * @param isday 天标 如果为否只统计月标
	 * @param startTime
	 * @return
	 */
	public double getCollectionSumNoJinAndSecond(long user_id, int status, int isday,long startTime);

	public List getUserOneDayAcount(SearchParam param);

	public List getTiChengAcount(SearchParam param);

	public AccountBank updateBankByAccount(AccountBank bank,String bankaccount);
	
	public List getHongBaoList(int start, int end, SearchParam param) ;
	public int getHongBaoListCount(SearchParam param);
	public List getHongBaoList(SearchParam param) ;
	/**
	 * 资金对账
	 */
	public int getAccountReconciliationListCount(SearchParam param) ;
	public List<AccountReconciliationModel> getAccountReconciliationList(int start, int end, SearchParam param) ;
	// v1.6.5.3 RDPROJECT-175 zza 2013-09-18 start
	/**
	 * 资金对账导出excel报表
	 * @param param param
	 * @return list
	 */
	List<AccountReconciliationModel> getAccountReconciliationList(SearchParam param);
	// v1.6.5.3 RDPROJECT-175 zza 2013-09-18 end
	public void updateAccount(double totalVar,double useVar,double nouseVar,double tender_award,long user_id,int a);
	
	/**
	 * 当免费额度小于0的时候保留上次回款总额
	 */
	public void addFreeCash(double free_cash,long user_id);
	
	/**
	 * 获取用户提现之前的回款总额
	 * @param user_id
	 * @return
	 */
	public double getFreeCash(long user_id);
	
	/**
	 * 获取流转天标待收
	 * @param user_id
	 * @param day
	 */
	public double getFlowDayTenderCollection(long user_id,int day);
	
	public double getFreeCashSum(long user_id);
	//V1.6.6.1 RDPROJECT-171 wcw 2013-10-08 start
	/**
	 * 客服----用户投资情况
	 * @param param
	 * @param start
	 * @param end
	 * @return
	 */
	/**
	 * 修改线下银行卡
	 * @param bank
	 * @param init
	 * @return
	 */
	public AccountBank updateBank(AccountBank bank,int init);
	public List getKefuUserInvestList(SearchParam param,int start,int end);
	public int getKefuUserInvestCount(SearchParam param);
	public List getKefuUserInvestList(SearchParam param);
	public double getKefuUserInvestSum(SearchParam param);
	//V1.6.6.1 RDPROJECT-171 wcw 2013-10-08 end
	public PaymentInterface getPaymentInterface(String interface_value);
	
	// v1.6.7.1 RDPROJECT-434 zza 2013-11-13 start
	/**
	 * 后台资金合计（总余额、总可用资金、总冻结资金、总待收资金、总待还金额）
	 * @param param param
	 * @return AccountSumModel
	 */
	AccountSumModel getAccountSum(SearchParam param);
	// v1.6.7.1 RDPROJECT-434 zza 2013-11-13 end
	//v1.6.7.2 RDPROJECT-470 wcw 2013-12-04 start
	public AccountBank getAccountByBankAccount(long id) ;
	//v1.6.7.2 RDPROJECT-470 wcw 2013-12-04 end
	
}
