package com.p2psys.model.accountlog.treasure;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.BaseTreasureLog;

/**
 * 理财宝获得利息处理
 
 *
 */
public class TreasureInterestLog extends BaseTreasureLog {

	private static final long serialVersionUID = 1879381929093016631L;
	
	private String interestSumLogRemarkTemplate="利息合计：收到利息${money}元。";
	
	private String templateNid = Constant.TREASURE_INTEREST;
	
	public TreasureInterestLog() {
		super();
	}
	public TreasureInterestLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public TreasureInterestLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public void addAccountSumLog() {
		AccountSum interestSum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(interestSumLogRemarkTemplate);
		AccountSumLog interestSumLog = this.baseAccountSumLog();
		interestSumLog.setBefore_money( interestSum.getInterest());
		double afterMoney = interestSum.getInterest() + this.getMoney();
		interestSumLog.setAfter_money(afterMoney);
		interestSumLog.setType(EnumAccountSumProperty.INTEREST.getValue());
		interestSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(interestSumLog);
	} 

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void accountSumProperty() {
		accountSumDao.editSumByProperty(EnumAccountSumProperty.INTEREST.getValue(), this.getMoney(), this.getUser_id());
	}
	

	
}
