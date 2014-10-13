package com.p2psys.model.accountlog;

import java.util.Date;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.util.DateUtils;

/**
 * 返还利息管理费
 
 *
 */
public class BaseBackManageFeeLog extends BaseAccountLog{

	private String sumLogRemarkTemplate = "回款合计：利息管理费优惠，获得回款利息${money}元。";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7180519456974456277L;

	
	/**
	 * 利息管理费
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public BaseBackManageFeeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 利息管理费
	 * @param money money
	 * @param act act
	 */
	public BaseBackManageFeeLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 利息管理费
	 */
	public BaseBackManageFeeLog() {
		super();
	}
	
	@Override
	public String getAccountSumLogRemarkTemplate() {
		return this.sumLogRemarkTemplate;
	}

	@Override
	public void accountSumProperty() {
		//先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		
		double usedInterest = accountSum.getUsed_interest();
		double interestFee=accountSum.getInterest_fee();
		double huikuan = accountSum.getHuikuan();
		
		//增加account_sum_log日志
		//扣费的顺序是充值、奖励、利息、借款
		double money = this.getMoney();
		

		long user_id = this.getUser_id();
		long to_user_id = this.getTo_user();
		String addtime = String.valueOf(DateUtils.getTime(new Date()));
		String addip = "";
		
		
		//增加interest sum log使用日志
		
		//退回利息管理费会增加两条sum_log，一条是减少已用利息，一条是减少利息管理费，
		
		//减少interest_fee项
		double interestfee_before_money = interestFee;
		double interestfee_money = -money;
		double interestfee_after_money = interestFee + interestfee_money;
		
		AccountSumLog interestFeeAccountSumLog = new AccountSumLog();
		interestFeeAccountSumLog.setUser_id(user_id);
		interestFeeAccountSumLog.setTo_user_id(to_user_id);
		interestFeeAccountSumLog.setBefore_money(interestfee_before_money);
		interestFeeAccountSumLog.setMoney(interestfee_money);
		interestFeeAccountSumLog.setAfter_money(interestfee_after_money);
		interestFeeAccountSumLog.setAddip(addip);
		interestFeeAccountSumLog.setAddtime(addtime);
		interestFeeAccountSumLog.setType(EnumAccountSumProperty.INTEREST_FEE.getValue());
		this.setAccountSumLogRemarkTemplate(interestFeeRemark);
		Global.setTransfer("money", interestfee_money);
		interestFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(interestFeeAccountSumLog);
		accountSumDao.editSumByProperty(EnumAccountSumProperty.INTEREST_FEE.getValue(), interestfee_money, this.getUser_id());
		
		//减少used interest项
		double interest_before_money = usedInterest;
		double interest_money = - money;
		double interest_after_money = usedInterest + interest_money;
		AccountSumLog interestAccountSumLog = new AccountSumLog();
		interestAccountSumLog.setUser_id(user_id);
		interestAccountSumLog.setTo_user_id(to_user_id);
		interestAccountSumLog.setBefore_money(interest_before_money);
		interestAccountSumLog.setMoney(interest_money);
		interestAccountSumLog.setAfter_money(interest_after_money);
		interestAccountSumLog.setAddip(addip);
		interestAccountSumLog.setAddtime(addtime);
		interestAccountSumLog.setType(EnumAccountSumProperty.USED_INTEREST.getValue());
		this.setAccountSumLogRemarkTemplate(usedInterestRemark);
		Global.setTransfer("user_cash", interest_money);
		interestAccountSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(interestAccountSumLog);
		accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_INTEREST.getValue(), interest_money, this.getUser_id());
		
		//增加回款金额
		double huikuan_before_money = huikuan;
		double huikuan_money = money;
		double huikuan_after_money = huikuan + huikuan_money;
		Global.setTransfer("user_cash", interest_money);
		AccountSumLog huikuanAccountSumLog = new AccountSumLog();
		huikuanAccountSumLog.setUser_id(user_id);
		huikuanAccountSumLog.setTo_user_id(to_user_id);
		huikuanAccountSumLog.setBefore_money(huikuan_before_money);
		huikuanAccountSumLog.setMoney(huikuan_money);
		huikuanAccountSumLog.setAfter_money(huikuan_after_money);
		huikuanAccountSumLog.setAddip(addip);
		huikuanAccountSumLog.setAddtime(addtime);
		huikuanAccountSumLog.setType(Constant.BACK_HUIKUAN_INTEREST);
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		Global.setTransfer("money", huikuan_money);
		huikuanAccountSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(huikuanAccountSumLog);
		accountSumDao.editSumByProperty(EnumAccountSumProperty.HUIKUAN_INTEREST.getValue(), huikuan_money, this.getUser_id());
	
	}
	
}
