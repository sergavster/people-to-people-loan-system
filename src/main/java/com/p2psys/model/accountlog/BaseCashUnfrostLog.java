package com.p2psys.model.accountlog;

import java.util.Date;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.common.enums.EnumHuiKuanStatus;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.UserCacheDao;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountCash;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.domain.Huikuan;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;

public class BaseCashUnfrostLog extends BaseAccountLog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1225684219521873356L;
	
//	protected UserCacheDao userCacheDao;
//	protected UserDao userDao;
//	protected AccountDao accountDao;
	
	protected String rechargeFailRemark = "提现失败：扣除已使用充值金额${cash.recharge_cash}元。";
	protected String awardFailRemark = "提现失败：扣除已使用奖励金额${cash.award_cash}元。";
	protected String interestFailRemark = "提现失败：扣除已使用利息金额${cash.interest_cash}元。";
	protected String borrowCashFailRemark = "提现失败：扣除已使用借款金额${cash.borrow_cash}元。";
	protected String huikuanFailRemark = "提现失败：扣除已使用回款金额${cash.huikuan_cash}元。";
	
	/**
	 * 调用父类
	 */
	public BaseCashUnfrostLog() {
		super();
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
			userDao=(UserDao)ctx.getBean("userDao");
			accountDao = (AccountDao) ctx.getBean("accountDao");
		}
	}
	
	public BaseCashUnfrostLog(double money, Account act, long toUser) {
		super(money, act, toUser);
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
			userDao=(UserDao)ctx.getBean("userDao");
			accountDao = (AccountDao) ctx.getBean("accountDao");
		}
	}

	public BaseCashUnfrostLog(double money, Account act) {
		super(money, act);
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
			userDao=(UserDao)ctx.getBean("userDao");
			accountDao = (AccountDao) ctx.getBean("accountDao");
		}
	}

	
	@Override
	public void accountSumProperty() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		
		Map<String , Object> map = Global.getTransfer();
		AccountCash cash = (AccountCash) map.get("cash");
		
		long user_id = this.getUser_id();
		long to_user_id = this.getTo_user();
		String addtime = String.valueOf(DateUtils.getTime(new Date()));
		String addip = "";

		// 本次提现使用的可用充值金额处理
		if (cash.getRecharge_cash() > 0) {
			// 增加recharge sum log 日志
			double recharge_before_money = accountSum.getUsed_recharge();
			double recharge_money = cash.getRecharge_cash();
			double recharge_after_money = recharge_before_money - recharge_money;
			Global.setTransfer("user_cash", recharge_money);
			AccountSumLog rechargeAccountSumLog = new AccountSumLog();
			rechargeAccountSumLog.setUser_id(user_id);
			rechargeAccountSumLog.setTo_user_id(to_user_id);
			rechargeAccountSumLog.setBefore_money(recharge_before_money);
			rechargeAccountSumLog.setMoney(-recharge_money);
			rechargeAccountSumLog.setAfter_money(recharge_after_money);
			rechargeAccountSumLog.setAddip(addip);
			rechargeAccountSumLog.setAddtime(addtime);
			this.setAccountSumLogRemarkTemplate(rechargeFailRemark);
			rechargeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			rechargeAccountSumLog.setType(EnumAccountSumProperty.USED_RECHARGE.getValue());
			accountSumLogDao.addAccountSumLog(rechargeAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_RECHARGE.getValue(), -recharge_money,this.getUser_id());

		}
		
		// 本次提现使用的可用奖励金额处理
		if (cash.getAward_cash() > 0) {
			// 增加award sum log 使用日志
			double award_before_money = accountSum.getUsed_award();
			double award_money =  cash.getAward_cash();
			double award_after_money = award_before_money -  award_money;
			Global.setTransfer("user_cash", award_money);
			AccountSumLog awardAccountSumLog = new AccountSumLog();
			awardAccountSumLog.setUser_id(user_id);
			awardAccountSumLog.setTo_user_id(to_user_id);
			awardAccountSumLog.setBefore_money(award_before_money);
			awardAccountSumLog.setMoney(-award_money);
			awardAccountSumLog.setAfter_money(award_after_money);
			awardAccountSumLog.setAddip(addip);
			awardAccountSumLog.setAddtime(addtime);
			this.setAccountSumLogRemarkTemplate(awardFailRemark);
			awardAccountSumLog.setRemark(this.getAccountSumLogRemark());
			awardAccountSumLog.setType(EnumAccountSumProperty.USED_AWARD.getValue());
			accountSumLogDao.addAccountSumLog(awardAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_AWARD.getValue(), -award_money,this.getUser_id());
		}
		
		// 本次提现使用的可用利息金额处理
		if (cash.getInterest_cash() > 0) {
			// 增加interest sum log使用日志
			double interest_before_money = accountSum.getUsed_interest();
			double interest_money = cash.getInterest_cash();
			double interest_after_money = interest_before_money - interest_money;
			Global.setTransfer("user_cash", interest_money);
			AccountSumLog interestAccountSumLog = new AccountSumLog();
			interestAccountSumLog.setUser_id(user_id);
			interestAccountSumLog.setTo_user_id(to_user_id);
			interestAccountSumLog.setBefore_money(interest_before_money);
			interestAccountSumLog.setMoney(-interest_money);
			interestAccountSumLog.setAfter_money(interest_after_money);
			interestAccountSumLog.setAddip(addip);
			interestAccountSumLog.setAddtime(addtime);
			this.setAccountSumLogRemarkTemplate(interestFailRemark);
			interestAccountSumLog.setRemark(this.getAccountSumLogRemark());
			interestAccountSumLog.setType(EnumAccountSumProperty.USED_INTEREST.getValue());
			accountSumLogDao.addAccountSumLog(interestAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_INTEREST.getValue(), -interest_money,this.getUser_id());
		}
		
		// 本次提现使用的可用借款金额处理
		if (cash.getBorrow_cash() > 0) {
			double borrowcash_before_money = accountSum.getUsed_borrow_cash();
			double borrowcash_money = cash.getBorrow_cash();
			double borrowcash_after_money = borrowcash_before_money - borrowcash_money;
			Global.setTransfer("user_cash", borrowcash_money);
			AccountSumLog borrowCashAccountSumLog = new AccountSumLog();
			borrowCashAccountSumLog.setUser_id(user_id);
			borrowCashAccountSumLog.setTo_user_id(to_user_id);
			borrowCashAccountSumLog.setBefore_money(borrowcash_before_money);
			borrowCashAccountSumLog.setMoney(-borrowcash_money);
			borrowCashAccountSumLog.setAfter_money(borrowcash_after_money);
			borrowCashAccountSumLog.setAddip(addip);
			borrowCashAccountSumLog.setAddtime(addtime);
			this.setAccountSumLogRemarkTemplate(borrowCashFailRemark);
			borrowCashAccountSumLog.setRemark(this.getAccountSumLogRemark());
			borrowCashAccountSumLog.setType(EnumAccountSumProperty.USED_BORROW_CASH.getValue());
			accountSumLogDao.addAccountSumLog(borrowCashAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_BORROW_CASH.getValue(), -borrowcash_money,this.getUser_id());
		}

		// 本次提现使用的可用回款处理
		if (cash.getHuikuan_cash() > 0) {
			double huikuan_before_money = accountSum.getUsed_huikuan();
			double huikuan_money = cash.getHuikuan_cash();
			double huikuan_after_money = huikuan_before_money - huikuan_money;
			Global.setTransfer("user_cash", huikuan_money);
			AccountSumLog caseFeeAccountSumLog = new AccountSumLog();
			caseFeeAccountSumLog.setUser_id(user_id);
			caseFeeAccountSumLog.setTo_user_id(to_user_id);
			caseFeeAccountSumLog.setBefore_money(huikuan_before_money);
			caseFeeAccountSumLog.setMoney(-huikuan_money);
			caseFeeAccountSumLog.setAfter_money(huikuan_after_money);
			caseFeeAccountSumLog.setAddip(addip);
			caseFeeAccountSumLog.setAddtime(addtime);
			this.setAccountSumLogRemarkTemplate(huikuanFailRemark);
			caseFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			caseFeeAccountSumLog.setType(EnumAccountSumProperty.USED_HUIKUAN.getValue());
			accountSumLogDao.addAccountSumLog(caseFeeAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_HUIKUAN.getValue(), -huikuan_money,this.getUser_id());
			this.autoHuikuanForCash(user_id , cash.getId() , huikuan_money);
		}
	}
	
	private void autoHuikuanForCash(long user_id,long cash_id , double huikuan_money){
		if(huikuan_money>=0.01){
			Huikuan huikuan = accountDao.getHuikuanByCashid(cash_id);
			if(huikuan != null){
				huikuan.setRemark("提现取消/审核不成功,返回回款额度"+NumberUtils.format2Str(huikuan_money)+"元");
				huikuan.setStatus(EnumHuiKuanStatus.HUIKUAN_CASH.getValue());
				accountDao.verifyhuikuan(huikuan);
			}
		}
	}
	
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

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	@Override
	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}
}
