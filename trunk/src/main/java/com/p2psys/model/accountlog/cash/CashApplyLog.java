package com.p2psys.model.accountlog.cash;

import java.util.Map;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountCash;
import com.p2psys.domain.AccountSum;
import com.p2psys.model.accountlog.BaseCashFrostLog;

public class CashApplyLog extends BaseCashFrostLog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6735305842324043154L;

	private String templateNid = Constant.CASH_FROST;
	
	public CashApplyLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public CashApplyLog(double money, Account act) {
		super(money, act);
	}

	public CashApplyLog() {
		super();
	}
	

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public void accountSumProperty() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		
		Map<String , Object> map = Global.getTransfer();
		AccountCash cash = (AccountCash) map.get("cash");
		
		// 取出可能扣款的值
		double usedRecharge = accountSum.getUsed_recharge();
		double usedAward = accountSum.getUsed_award();
		double usedInterest = accountSum.getUsed_interest();
		double usedBorrowCash = accountSum.getUsed_borrow_cash();

		double recharge_money = cash.getRecharge_cash();
		if (recharge_money > 0) {
			// 增加recharge sum log 日志
			double currRecharge = accountSum.getRecharge() - usedRecharge;
			recharge_money = recharge_money > currRecharge ? currRecharge : recharge_money;
			Global.setTransfer("user_cash", recharge_money);
			this.setAccountSumLogRemarkTemplate(usedRechargeRemark);
			String logType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			String sumType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			this.sumUpdate(recharge_money, usedRecharge, logType,sumType);
		}
		
		double award_money =  cash.getAward_cash();
		if (award_money > 0) {
			// 增加award sum log 使用日志
			double currAward = accountSum.getAward() - accountSum.getUsed_award();
			award_money = award_money > currAward ? currAward : award_money;
			Global.setTransfer("user_cash", award_money);
			this.setAccountSumLogRemarkTemplate(usedAwardRemark);
			String logType = EnumAccountSumProperty.USED_AWARD.getValue();
			String sumType = EnumAccountSumProperty.USED_AWARD.getValue();
			this.sumUpdate(award_money, usedAward, logType,sumType);
		}
		
		double interest_money = cash.getInterest_cash();
		if (interest_money > 0) {
			// 增加interest sum log使用日志
			double currInterest = accountSum.getInterest() - accountSum.getUsed_interest();
			interest_money = interest_money > currInterest ? currInterest : interest_money;
			Global.setTransfer("user_cash", interest_money);
			this.setAccountSumLogRemarkTemplate(usedInterestRemark);
			String logType = EnumAccountSumProperty.USED_INTEREST.getValue();
			String sumType = EnumAccountSumProperty.USED_INTEREST.getValue();
			this.sumUpdate(interest_money, usedInterest, logType,sumType);
		}
		
		double borrowcash_money = cash.getBorrow_cash();
		if (borrowcash_money > 0) {
			// 增加BorrowCash sum log使用日志
			double currBorrowCash = accountSum.getBorrow_cash() - accountSum.getUsed_borrow_cash();
			borrowcash_money = borrowcash_money > currBorrowCash ? currBorrowCash : borrowcash_money;
			Global.setTransfer("user_cash", borrowcash_money);
			this.setAccountSumLogRemarkTemplate(usedBorrowCashRemark);
			String logType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			String sumType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			this.sumUpdate(borrowcash_money, usedBorrowCash, logType,sumType);
		}

		//扣除回款的account_sum_log
		double huikuan_money = cash.getHuikuan_cash();
		if (huikuan_money > 0) {
			this.huikuanManage(huikuan_money);
		}
	}


}
