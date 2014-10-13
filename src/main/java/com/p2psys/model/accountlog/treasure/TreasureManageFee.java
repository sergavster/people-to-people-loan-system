package com.p2psys.model.accountlog.treasure;

import java.util.Date;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.BaseTreasureLog;
import com.p2psys.util.DateUtils;

/**
 * 投资理财宝利息管理费
 
 *
 */
public class TreasureManageFee extends BaseTreasureLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2406147548880262016L;

	private String logInterestFeeRemark ="利息手续费合计：扣除利息管理费 ${money} 元。";
	
	/**
	 * 类型：理财宝利息管理费
	 */
	private String templateNid = Constant.TREASURE_MANAGE_FEE;

	/**
	 * 理财宝利息管理费
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public TreasureManageFee(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 理财宝利息管理费
	 * @param money money
	 * @param act act
	 */
	public TreasureManageFee(double money, Account act) {
		super(money, act);
	}

	/**
	 * 理财宝利息管理费
	 */
	public TreasureManageFee() {
		super();
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public void accountSumProperty() {
		//先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		
		//利息管理费只能从利息中扣除
		double usedInterest = accountSum.getUsed_interest();
		double interestFee=accountSum.getInterest_fee();
		
		//增加account_sum_log日志
		//扣费的顺序是充值、奖励、利息、借款
		double money = this.getMoney();

		//利息管理费只从利息中扣除,而且不存在不够的情况，所以已用金额就只是直接赋值为日志的金额即可
		double currInterest = money;
		
		long user_id = this.getUser_id();
		long to_user_id = this.getTo_user();
		String addtime = String.valueOf(DateUtils.getTime(new Date()));
		String addip = "";
		
		if(currInterest > 0){
			//增加interest sum log使用日志
			//扣除利息管理费会增加两条sum_log，一条是增加已用利息，一条是增加利息管理费，
			
			//增加interest_fee项
			double interestfee_before_money = interestFee;
			double interestfee_money = currInterest;
			double interestfee_after_money = interestFee + currInterest;
			
			AccountSumLog interestFeeAccountSumLog = new AccountSumLog();
			interestFeeAccountSumLog.setUser_id(user_id);
			interestFeeAccountSumLog.setTo_user_id(to_user_id);
			interestFeeAccountSumLog.setBefore_money(interestfee_before_money);
			interestFeeAccountSumLog.setMoney(interestfee_money);
			interestFeeAccountSumLog.setAfter_money(interestfee_after_money);
			interestFeeAccountSumLog.setAddip(addip);
			interestFeeAccountSumLog.setAddtime(addtime);
			interestFeeAccountSumLog.setType(Constant.MANAGE_FEE);
			this.setAccountSumLogRemarkTemplate(logInterestFeeRemark);
			interestFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.addAccountSumLog(interestFeeAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.INTEREST_FEE.getValue(), interestfee_money, this.getUser_id());
			
			//增加used interest项
			double interest_before_money = usedInterest;
			double interest_money = currInterest;
			double interest_after_money = usedInterest + currInterest;
			Global.setTransfer("user_cash", interest_money);
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
			interestAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.addAccountSumLog(interestAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_INTEREST.getValue(), interest_money, this.getUser_id());
		}
	}
}
