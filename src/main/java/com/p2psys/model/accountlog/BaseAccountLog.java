package com.p2psys.model.accountlog;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.common.enums.EnumLogTemplateType;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.AccountLogDao;
import com.p2psys.dao.AccountSumDao;
import com.p2psys.dao.AccountSumLogDao;
import com.p2psys.dao.UserCacheDao;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.fund.web.action.front.FundAction;
import com.p2psys.service.NoticeService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.FreemarkerUtil;

/**
 * 抽象资金日志类
 * 
 * @version 1.0
 * @since 2013-8-26
 */
public class BaseAccountLog extends AccountLog implements AccountLogEvent {
	
	private static final long serialVersionUID = 8585423859178082248L;

	protected AccountSumDao accountSumDao;
	
	protected AccountLogDao accountLogDao;
	
	protected AccountSumLogDao accountSumLogDao;
	
	protected AccountDao accountDao;
	
	protected String sumLogRemarkTemplate;
	
	protected String usedRechargeRemark = "已使用充值：扣除充值${user_cash}元!";
	
	protected String usedInterestRemark= "已使用利息：扣除获得利息${user_cash}元!";
	
	protected String usedAwardRemark= "已使用奖励：扣除获得奖励${user_cash}元!";
	
	protected String usedHuikuanRemark= "已使用回款本金：扣除回款${user_cash}元!";
	
	protected String usedHuikuanInterestRemark= "已使用回款利息：扣除回款${user_cash}元!";
	
	protected String usedBorrowCashRemark= "已使用借款额：扣除借款额${user_cash}元!";
	
	protected String cashfeeRemark= "提现收费合计：提现${cash.total}元,手续费${cash.fee}元!";
	
	protected String cashRemark= "提现金额合计：提现${cash.total}元!";
	
	protected String rechargeRemark= "充值合计：增加充值${recharge.money}元!";
	
	protected String usedRemark= "扣款合计：扣除${money}元!";
	
	protected String huikuanRemark= "回款合计：增加${money}元!";
	//v1.6.6.2 RDPROJECT-347 liukun 2013-10-24 start
	protected String interestFeeRemark= "利息管理费合计：增加${money}元!";
	//v1.6.6.2 RDPROJECT-347 liukun 2013-10-24 end
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	protected UserDao userDao;
	protected UserCacheDao userCacheDao;
	protected NoticeService noticeService;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	
	public static boolean DEBUG=false;
	
	private static final Logger logger=Logger.getLogger(FundAction.class);
	
	public BaseAccountLog() {
		super();
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			accountSumDao=(AccountSumDao)ctx.getBean("accountSumDao");
			accountLogDao=(AccountLogDao)ctx.getBean("accountLogDao");
			accountSumLogDao = (AccountSumLogDao)ctx.getBean("accountSumLogDao");
			accountDao=(AccountDao)ctx.getBean("accountDao");
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
			userDao=(UserDao)ctx.getBean("userDao");
			userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
			noticeService=(NoticeService)ctx.getBean("noticeService");
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
		}
	}
	
	public BaseAccountLog(double money,Account act) {
		this();
		Account item = accountDao.getAccount(act.getUser_id());
		this.setMoney(money);
		this.setTotal(item.getTotal());
		this.setUse_money(item.getUse_money());
		this.setNo_use_money(item.getNo_use_money());
		this.setCollection(item.getCollection());
		this.setUser_id(act.getUser_id());
		this.setAddtime(DateUtils.getNowTimeStr());
		this.setTo_user(1L);
	}
	
	public AccountSumLog baseAccountSumLog(){
		AccountSumLog sumLog = new AccountSumLog();
		sumLog.setAddip(this.getAddip());
		sumLog.setAddtime(this.getAddtime());
		sumLog.setMoney(this.getMoney());
		sumLog.setRemark(this.getAccountSumLogRemark());
		sumLog.setUser_id(this.getUser_id());
		sumLog.setTo_user_id(this.getTo_user());
		sumLog.setType(this.getType());
		return sumLog;
	}
	
	public BaseAccountLog(double money, Account act,long toUser) {
		this(money, act);
		this.setTo_user(toUser);
	}
	
	public void refresh(){
		Account item = accountDao.getAccount(this.getUser_id());
		this.setTotal(item.getTotal());
		this.setUse_money(item.getUse_money());
		this.setNo_use_money(item.getNo_use_money());
		this.setCollection(item.getCollection());
	}
	
	/**
	 * 默认的事件执行
	 */
	@Override
	public void doEvent() {
		//调试时手动传参，服务器上通过Spring容器获取
		if(DEBUG){
			transfer();
		}
		updateAccount();
		addAccountSumLog();
		accountSumProperty();
		//资金记录
		Global.setTransfer("weburl", Global.getString("weburl"));
		this.setRemark(this.getLogRemark());
		this.setType(this.getType());
		refresh();
		accountLogDao.addAccountLog(this);
		//短信
		sendNotice();
		//操作日志
		addOperateLog();
		
	}

	/**
	 * 实现默认的接口方法
	 */
	@Override
	public void accountSumProperty() {
		
	}
	
	public String getLogRemark(){
		try {
			return FreemarkerUtil.renderTemplate(this.getLogRemarkTemplate(), transfer());
		} catch (Exception e) {
		}
		return "";
	}
	
	public String getAccountSumLogRemark(){
		try {
			return FreemarkerUtil.renderTemplate(getAccountSumLogRemarkTemplate(), transfer());
		} catch (Exception e) {
		}
		return "";
	}
	
	@Override
	public Map<String, Object> transfer() {
		Map<String,Object> data=Global.getTransfer();
		if(accountSumDao==null){
			accountSumDao=(AccountSumDao)data.get("accountSumDao");
		}
		if(accountLogDao==null){
			accountLogDao=(AccountLogDao)data.get("accountLogDao");
		}
		if(accountSumLogDao == null){
			accountSumLogDao = (AccountSumLogDao)data.get("accountSumLogDao");
		}

		return data;
	}

	public String getLogRemarkTemplate(){
		return Global.getLogTempValue(EnumLogTemplateType.ACCOUNT_LOG.getValue(), this.getTemplateNid());
	}
	
	public String getAccountSumLogRemarkTemplate(){
		return "";
	}
	
	public void setAccountSumLogRemarkTemplate(String sumLogRemarkTemplate) {
		this.sumLogRemarkTemplate = sumLogRemarkTemplate;
	}
	
	public String getType(){
		return getAccountLogType(this.getTemplateNid());
	}
	@Override
	public void sendNotice() {
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start
		//能取到noticeTypeNid就可以发送
		if (!"".equals(getNoticeTypeNid())){
			Map<String, Object> transferMap = transfer();
			
			long userid = this.getUser_id();
			User user = userDao.getUserByUserid(userid);
			UserCache userCache = userCacheDao.getUserCacheByUserid(userid);
			
			transferMap.put("host", Global.getValue("weburl"));
			transferMap.put("webname", Global.getValue("webname"));
			transferMap.put("noticeTime", DateUtils.getNowTime());
			transferMap.put("user", user);
			transferMap.put("userCache", userCache);
			noticeService.sendNotice(getNoticeTypeNid(), transferMap);
		}
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end
	}

	
	//v1.6.7.2 资金合计封装 zhangyz 2013-12-18 start
	/**
	 * sum和sum日志更新
	 * @param money 操作金额
	 * @param sumMoney 当前已有的金额
	 * @param logType 日志类型
	 * @param sumType sum修改字段类型
	 * @return
	 * 扣费的顺序是充值、奖励、利息、借款，如果不同，则在子类中写
	 */
	public void sumUpdate(double money,double sumMoney,String logType,String sumType){
		AccountSumLog sumLog = new AccountSumLog();
		long user_id = this.getUser_id();
		long to_user_id = this.getTo_user();
		double after_money = money + sumMoney;
		sumLog.setUser_id(user_id);
		sumLog.setTo_user_id(to_user_id);
		sumLog.setBefore_money(sumMoney);
		sumLog.setMoney(money);
		sumLog.setAfter_money(after_money);
		sumLog.setAddip(this.getAddip());
		sumLog.setAddtime(DateUtils.getNowTimeStr());
		sumLog.setType(logType);
		sumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(sumLog);
		accountSumDao.editSumByProperty(sumType, money, this.getUser_id());
	}
	
	/**
	 * sum数据处理
	 * @return
	 */
	public void sumManage(){
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		// 取出可能扣款的值
		double recharge = accountSum.getRecharge();
		double usedRecharge = accountSum.getUsed_recharge();
		double useRecharge = recharge - usedRecharge;

		double award = accountSum.getAward();
		double usedAward = accountSum.getUsed_award();
		double useAward = award - usedAward;

		double interest = accountSum.getInterest();
		double usedInterest = accountSum.getUsed_interest();
		double useInterest = interest - usedInterest;

		double usedBorrowCash = accountSum.getUsed_borrow_cash();

		// 增加account_sum_log日志
		// 扣费的顺序是充值、奖励、利息、借款
		double money = this.getMoney();
		double currRecharge = 0;
		double currAward = 0;
		double currInterest = 0;
		double currBorrowCash = 0;

		if (useRecharge >= money) {
			currRecharge = money;
		} else {
			if ((useRecharge + useAward) >= money) {
				currRecharge = useRecharge;
				currAward = money - useRecharge;
			} else {
				if ((useRecharge + useAward + useInterest) >= money) {
					currRecharge = useRecharge;
					currAward = useAward;
					currInterest = money - (useRecharge + useAward);
				} else {
					currRecharge = useRecharge;
					currAward = useAward;
					currInterest = useInterest;
					currBorrowCash = money - (useRecharge + useAward + useInterest);
				}
			}
		}

		if (currRecharge > 0) {
			// 增加recharge sum log 日志
			Global.setTransfer("user_cash", currRecharge);
			this.setAccountSumLogRemarkTemplate(usedRechargeRemark);
			String logType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			String sumType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			this.sumUpdate(currRecharge, usedRecharge, logType,sumType);
		}
		if (currAward > 0) {
			// 增加award sum log 使用日志
			Global.setTransfer("user_cash", currAward);
			this.setAccountSumLogRemarkTemplate(usedAwardRemark);
			String logType = EnumAccountSumProperty.USED_AWARD.getValue();
			String sumType = EnumAccountSumProperty.USED_AWARD.getValue();
			this.sumUpdate(currAward, usedAward, logType,sumType);
		}
		if (currInterest > 0) {
			// 增加interest sum log使用日志
			Global.setTransfer("user_cash", currInterest);
			this.setAccountSumLogRemarkTemplate(usedInterestRemark);
			String logType = EnumAccountSumProperty.USED_INTEREST.getValue();
			String sumType = EnumAccountSumProperty.USED_INTEREST.getValue();
			this.sumUpdate(currInterest, usedInterest, logType,sumType);
		}
		if (currBorrowCash > 0) {
			// 增加interest sum log使用日志
			Global.setTransfer("user_cash", currBorrowCash);
			this.setAccountSumLogRemarkTemplate(usedBorrowCashRemark);
			String logType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			String sumType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			this.sumUpdate(currBorrowCash, usedBorrowCash, logType,sumType);
		}
		// 扣除回款的account_sum_log
		this.huikuanManage(null);
	}
	
	/**
	 * 回款处理
	 * @param money 操作金额，如果为空，默认值就是account log中的money值
	 */
	public void huikuanManage(Double money){
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		double huikuan = accountSum.getHuikuan();
		double usedHuikuan = accountSum.getUsed_huikuan();
		double useHuikuan = huikuan - usedHuikuan;

		double huikuanInterest = accountSum.getHuikuan_interest();
		double usedHuikuanInterest = accountSum.getUsed_huikuan_interest();
		double useHuikuanInterest = huikuanInterest - usedHuikuanInterest;
		
		if(money == null) money = this.getMoney();
		double currHuikuan = 0;
		double currHuikuanInterest = 0;
		double moenyHKuan = useHuikuanInterest - money;
		currHuikuanInterest = moenyHKuan >= 0 ? money : useHuikuanInterest;
		if(moenyHKuan < 0){// 本次使用的回款最大不能超过可用回款，
			currHuikuan = money - useHuikuanInterest;
			currHuikuan = currHuikuan >= useHuikuan ? useHuikuan : currHuikuan;
		}
		if (currHuikuanInterest > 0) {
			this.huikuanInterestUpdate(currHuikuanInterest);
		}
		if (currHuikuan > 0) {
			this.huikuanUpdate(currHuikuan);
		}
	}
	
	// 回款更新
	public void huikuanUpdate(Double currHuikuan){
		if(currHuikuan == null) currHuikuan = this.getMoney();
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		double huikuan = accountSum.getHuikuan() - accountSum.getUsed_huikuan();
		currHuikuan = currHuikuan >= huikuan ?  huikuan : currHuikuan;
		Global.setTransfer("user_cash", currHuikuan);
		this.setAccountSumLogRemarkTemplate(usedHuikuanRemark);
		String logType = EnumAccountSumProperty.USED_HUIKUAN.getValue();
		String sumType = EnumAccountSumProperty.USED_HUIKUAN.getValue();
		this.sumUpdate(currHuikuan, accountSum.getUsed_huikuan(), logType,sumType);
	}
	
	// 回款利息更新
	public void huikuanInterestUpdate(Double currHuikuanInterest){
		if(currHuikuanInterest == null) currHuikuanInterest = this.getMoney();
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		double huikuanInterest = accountSum.getHuikuan_interest() - accountSum.getUsed_huikuan_interest();
		currHuikuanInterest = currHuikuanInterest >= huikuanInterest ? huikuanInterest : currHuikuanInterest;
		Global.setTransfer("user_cash", currHuikuanInterest);
		this.setAccountSumLogRemarkTemplate(usedHuikuanInterestRemark);
		String logType = EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue();
		String sumType = EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue();
		this.sumUpdate(currHuikuanInterest, accountSum.getUsed_huikuan_interest(), logType,sumType);
	}
	//v1.6.7.2 资金合计封装 zhangyz 2013-12-18 end
	
	@Override
	public void addOperateLog() {
		
	}
	
	public void addAccountSumLog(){
		
	}
	/**
	 * 更新账户表
	 */
	@Override
	public void updateAccount() {
		
	}

	public String getTemplateNid() {
		return "";
	}

	public String getAccountLogType(String nid){
		return Global.getLogType(EnumLogTemplateType.ACCOUNT_LOG.getValue(), nid);
	}

	public AccountSumDao getAccountSumDao() {
		return accountSumDao;
	}

	public void setAccountSumDao(AccountSumDao accountSumDao) {
		this.accountSumDao = accountSumDao;
	}

	public AccountLogDao getAccountLogDao() {
		return accountLogDao;
	}

	public void setAccountLogDao(AccountLogDao accountLogDao) {
		this.accountLogDao = accountLogDao;
	}

	public AccountSumLogDao getAccountSumLogDao() {
		return accountSumLogDao;
	}

	public void setAccountSumLogDao(AccountSumLogDao accountSumLogDao) {
		this.accountSumLogDao = accountSumLogDao;
	}

	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	public UserCacheDao getUserCacheDao() {
		return userCacheDao;
	}

	public void setUserCacheDao(UserCacheDao userCacheDao) {
		this.userCacheDao = userCacheDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public NoticeService getNoticeService() {
		return noticeService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	public String getNoticeTypeNid(){
		return "";
	}

	@Override
	public void extend() {
		//做些扩展动作
	}
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	
	
}
