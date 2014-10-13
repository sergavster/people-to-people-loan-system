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

public class TreasureRepaidLog extends BaseTreasureLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	private String sumLogRemarkTemplate = "还款合计：扣除还款本息${money}元。";		
	private String templateNid=Constant.TREASURE_REPAID;
	public TreasureRepaidLog() {
		super();
	}
	public TreasureRepaidLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public TreasureRepaidLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public void addAccountSumLog() {
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getBorrow_cash());
		double afterMoney = sum.getBorrow_cash() + this.getMoney();
		sumLog.setAfter_money(afterMoney);
		accountSumLogDao.addAccountSumLog(sumLog);
	} 
	
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void accountSumProperty() {
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
		accountSumDao.editSumByProperty(EnumAccountSumProperty.REPAY_CASH.getValue(), this.getMoney(), this.getUser_id());

	}
}

