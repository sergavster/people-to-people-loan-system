package com.p2psys.service;

import java.util.List;
import java.util.Map;

import com.p2psys.domain.Account;
import com.p2psys.domain.AccountBack;
import com.p2psys.domain.AccountBank;
import com.p2psys.domain.AccountCash;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.AccountRecharge;
import com.p2psys.domain.AutoTenderOrder;
import com.p2psys.domain.HongBao;
import com.p2psys.domain.Huikuan;
import com.p2psys.domain.OperationLog;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.domain.Protocol;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAmount;
import com.p2psys.model.AccountBackModel;
import com.p2psys.model.HuikuanModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.PaymentSumModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.account.AccountCashList;
import com.p2psys.model.account.AccountLogList;
import com.p2psys.model.account.AccountModel;
import com.p2psys.model.account.AccountRechargeList;
import com.p2psys.model.account.AccountReconciliationModel;
import com.p2psys.model.account.OperationLogModel;


public interface AccountService {
	/**
	 * 获取所有详情
	 * @param user_id
	 * @return
	 */
	public UserAccountSummary getUserAccountSummary(long user_id); 
	 // v1.6.7.2 RDPROJECT-272 wcw 2013-12-26 start
    /**
     * 获取账号详情
     * @return
     */
    public UserAccountSummary getAccountDetails(UserAccountSummary uas);
    /**
     * 待收待还账户详情
     * @return
     */
    public UserAccountSummary getWaitRepayAndCollection(UserAccountSummary uas);
    /**
     * 投资详情
     */
    public UserAccountSummary getInvestDetail(UserAccountSummary uas);
    /**
     * 借款详情
     */
    public UserAccountSummary getBorrowMoneyDetail(UserAccountSummary uas);
    /**
     * 提现详情
     * @param uas
     * @return
     */
    public UserAccountSummary getCashDetail(UserAccountSummary uas);
    /**
     * 充值详情
     * @param uas
     * @return
     */
     public UserAccountSummary getRechargeDetail(UserAccountSummary uas);
 // v1.6.7.2 RDPROJECT-272 wcw 2013-12-26 end
	/**
	 * 获取用户信息额度情况
	 * @param user_id
	 * @return
	 */
	public UserAmount getUserAmount(long user_id);
	
	/**
	 * 获取用的账户情况，含提现的银行卡信息
	 * @param user_id
	 * @return
	 */
	public AccountModel getAccount(long user_id);
	/**
	 * 获取用户的客服
	 * @param user_id
	 * @return
	 */
	public User getKf(long user_id);
	/**
	 * 获取资金记录列表,无法分页
	 * @param user_id
	 * @return
	 */
	public List getAccountLogList(long user_id) ;
	/**
	 * 获取用户的资金记录表，含分页
	 * @param user_id
	 * @param startPage
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public AccountLogList getAccountLogList(long user_id,int startPage,SearchParam param) ;
	
	public List getAccountLogList(SearchParam param) ;
	/**
	 * 获取用户的资金记录表，无分页
	 * @param user_id
	 * @param param
	 * @return
	 */
	public List getAccountLogList(long user_id,SearchParam param);
	/**
	 * 获取用户的资金流水总量
	 * @param user_id
	 * @return
	 */
	public double getAccountLogTotalMoney(long user_id);
	
	/**
	 * 获取用户的提现记录表，无分页
	 * @param user_id
	 * @return
	 */
	public List getAccountCashList(long user_id) ;
	
	/**
	 * 查询提现次数
	 * 
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public int getAccountCashList(long user_id,int status);

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
	 * 获取用户的提现记录表，有分页
	 * @param user_id
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public AccountCashList getAccountCashList(long user_id,int startPage,SearchParam param) ;
	
	/**
	 * 获取用户的提现总额
	 * @param user_id 用户id
	 * @param status 提现状态
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public double getAccountApplyCashSum(long user_id, long startTime, long endTime);
	
	// V1.6.6.1 liukun 2013-09-10 start
	//public void cancelCash(long id,AccountLog log);
	public void cancelCash(long id);
	// V1.6.6.1 liukun 2013-09-10 end
	/**
	 * 新增提现
	 * @param cash
	 * @param act
	 * @param money
	 * @param isCash 是否计算提现费用，如果是，则不进行申请提现操作
	 */
	public void newCash(AccountCash cash,Account act,double money,boolean isCash) ;
/*	public void newCash(AccountCash cash,Account act,double money,boolean isCash,AccountLog log) ;
*/	/**
	 * 新增账户
	 * @param act
	 * @return
	 */
	public Account addAccount(Account act);
	/**
	 * 新增提现银行卡
	 * @param bank
	 * @return
	 */
	public AccountBank addBank(AccountBank bank);
	/**
	 *	统计用户银行卡数量 
	 * @param user_id
	 * @return
	 */
	public int getAccountBankCount(long user_id);
	/**
	 * 修改银行卡信息
	 * @param bank
	 * @return
	 */
	public AccountBank modifyBank(AccountBank bank);
	
	/**
	 * 获取充值记录列表，无分页
	 * @param user_id
	 * @return
	 */
	public List getRechargeList(long user_id);
	
	/**
	 * 获取充值记录列表，含分页
	 * @param user_id
	 * @param startPage
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public AccountRechargeList getRechargeList(long user_id,int startPage,SearchParam param);
	
	/**
	 * 新增资金记录
	 * @param log
	 */
	public void addAccountLog(AccountLog log);
	/**
	 * 修改个人账户信息
	 * @param act
	 */
	public void modifyAccount(Account act);
	
	/**
	 * 更新充值记录,并将充值金额到账
	 * @param r
	 * @param log
	 */
	public void newRecharge(String orderId,String returnText,AccountLog log,PaymentInterface paymentInterface);
	/*public void recharge(String orderId, String returnText,AccountLog log,AccountLog newlog);*/
	public void failRecharge(String orderId,String returnText,AccountLog log);
	/**
	 * 新增充值记录，但是Account记录并未到账
	 * @param r
	 * @param log
	 */
	public void addRecharge(AccountRecharge r) ;
	
	public PageDataList getRechargeList(int page,SearchParam param);
	
	
	 // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 start
	/**
	 * 新增扣款记录
	 * @param b
	 */
	public void addBack(AccountBack b) ;
	/**
	 * 获取扣款记录列表
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getBackList(int page,SearchParam param);
	/**
	 * 获取扣款记录列表
	 * @param param
	 * @return
	 */
	public List getBackList(SearchParam param);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public AccountBackModel getBack(long id);
	 // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 end
	
	public List getRechargeList(SearchParam param);
	
	public AccountRecharge getRecharge(long id);
	
	public void verifyRecharge(AccountRecharge r,AccountLog log,OperationLog operationLog);
	// v1.6.7.2 RDPROJECT-548 lx 2013-12-13 start
	public void verifyBack(AccountBackModel b,AccountLog log,OperationLog operationLog);
	 // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 end
	public void verifyRecharge(AccountRecharge r, AccountLog log,HongBao hongbao,OperationLog operationLog);
	
	public PageDataList getAccountLogList(int page,SearchParam param);
	
	/**
	 * 新增后台充值记录
	 * @param r
	 * @param accountLog
	 */
	public void addRecharge(AccountRecharge r,AccountLog accountLog);
	
	public void updateAccount(double totalVar,double useVar,double nouseVar,long user_id);
	
	public void updateAccount(double totalVar,double useVar,double nouseVar,double collectVar,long user_id);
	// V1.6.6.1 liukun 2013-09-18 start
	//public void newCash(AccountCash cash,Account act,double money,AccountLog log);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public void newCash(AccountCash cash,Account act,double money);
	// V1.6.6.1 liukun 2013-09-18 end
	
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public PageDataList getUserAccountModel(int page,SearchParam param);
	/**
	 * 查询提现银行卡信息
	 */
	public List<AccountModel> getAccountList(SearchParam param);
	/**
	 * 账户资金统计
	 * @param param
	 * @param ids
	 * @return
	 */
	public double getRechargesum(SearchParam param,int ids) ;
	/**
	 * 奖励统计
	 * @param user_id
	 * @return
	 */
	public double  getAwardSum(long user_id);
	/**
	 * 已收利息统计
	 * @param user_id
	 * @return
	 */
	public double  getInvestInterestSum(long user_id);
	
	public PageDataList getTenderLogList(int page,SearchParam param);
	
	public List getTenderLogList(SearchParam param);
	
	/**
	 * 申请回款
	 * @param huikuan
	 */
	public void huikuan(Huikuan huikuan);
	public PageDataList huikuanlist(int page, SearchParam param);
	public List huikuanlist( SearchParam param);
	public HuikuanModel viewHuikuan(int id);
	public HuikuanModel verifyHuikuan(HuikuanModel huikuan,AccountLog log,AccountRecharge recharge);
	
	public List<AccountRecharge> addExcelRecharge(String xls, User verifyUser, List<AccountRecharge> list, int status);
	
	public List[] addBatchRecharge(List[] data) ;
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public AccountModel getAccountByBankAccount(long user_id,String bankAccount);
	public AccountBank modifyBankByAccount(AccountBank bank,String bankaccount);
	/**
	 * 红包列表
	 */
	public PageDataList getHongBaoList(int page, SearchParam param) ;
	public List getHongBaoList(SearchParam param) ;
	/**
	 * 资金对账
	 */
	public PageDataList getAccountReconciliationList(int pages, SearchParam param);
	// v1.6.5.3 RDPROJECT-175 zza 2013-09-18 start
	/**
	 * 资金对账导出excel报表
	 * @param param param
	 * @return list
	 */
	List<AccountReconciliationModel> getAccountReconciliationList(SearchParam param);
	// v1.6.5.3 RDPROJECT-175 zza 2013-09-18 end
	/**
	 * 获取第一次成功充值有效金额
	 * @param user_id
	 * @return
	 */
	public AccountRecharge getMinRechargeMoney(long user_id);
	//修改第一次充值奖励已给邀请人   1  ---yes_no
	public void updateAccountRechargeYes_no(AccountRecharge accountRecharge);
	public void updateAccount(double totalVar, double useVar, double nouseVar,double tender_award,long user_id,int a);
    //操作日志
	public PageDataList operationLog(int pages, SearchParam param);
	//操作日志导出
	public List<OperationLogModel> operationLog(SearchParam param);
	//操作日志添加
	public void addOperationLog(OperationLog log);
	//及时雨协议
	public void verifyRecharge(AccountRecharge r, AccountLog log,OperationLog operationLog,Protocol protocol);
	
	public double getAccountLogSum(String type);
	//查出逾期总额
	public double getAllSumLateMoney(int status);
	
	public double getRepaymentAccount(int status);
	//统计资金记录
	public List getAccountLogSumWithMonth(SearchParam param);
	/**
	 * 逾期未还总额
	 * @return
	 */
	public double getAllLateSumWithNoRepaid();
	/**
	 * 逾期已还款总额
	 * @return
	 */
	public double getAllLateSumWithYesRepaid();
	/**
	 * 第三方接口直连银行
	 * @param paymentInterfaceId
	 * @return
	 */
	public  List onlineBank(String paymentInterfaceId);
	/**
	 * 线下银行
	 * @return
	 */
	public  List downBank();
	
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
	 * 扣除用户费用
	 * @return
	 */
	public void deductFee(long userid, double fee, String feetype, String remark);
	
	/**
	 * 扣除用户费用
	 * @return
	 */
	public Map paySmsFee(long userid, int payModel);
	
	/**
	 * 新增提现
	 * @param cash
	 * @param act
	 * @param money
	 * @param isCash 是否计算提现费用，如果是，则不进行申请提现操作
	 */
	public void accountCash(AccountCash cash,Account act,double money,boolean isCash);
	/**
	 * 客服 -----用户投标情况
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList kefuUserInvest(int page, SearchParam param);
	public List kefuUserInvest(SearchParam param);
	public double getKefuUserInvestSum(SearchParam param);
	
	public  List paymentInterface_unsingle(int init);
	public  List paymentInterface_single(int init);
    public PaymentInterface getPaymentInterface(String interface_value);
  //v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 start
  	public AccountRecharge getAccountRechargeByTradeno(String trade_no);
	public void getAccountRechargeList();
	  //v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 end
	
	// v1.6.7.1 RDPROJECT-124 zza 2013-11-14 start
	/**
	 * 自动投标排名查询
	 * @param page page
	 * @param param param
	 * @return list
	 */
	PageDataList getAutoTenderLogList(int page, SearchParam param);
	
	/**
	 * 导出
	 * @param param param
	 * @return list
	 */
	List<AutoTenderOrder> getAutoTenderLogList(SearchParam param);
	// v1.6.7.1 RDPROJECT-124 zza 2013-11-14 end
	
	/**
	 * 总数count
	 * @param param
	 * @return
	 */
	// v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start
	public Integer getAccountlogCount(SearchParam param);
	public Integer  getTenderlogCount(SearchParam param);
	public Integer getRechargeCount(SearchParam param);
	// v1.6.7.1 RDPROJECT-510 cx 2013-12-04 end
	// v1.6.7.2 RDPROJECT-470 wcw 2013-12-04 start
	public AccountBank getAccountByBankAccount(long id);
	//v1.6.7.2 RDPROJECT-470 wcw 2013-12-04 end
}
