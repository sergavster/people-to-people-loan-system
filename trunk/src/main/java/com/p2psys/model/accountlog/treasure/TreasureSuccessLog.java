package com.p2psys.model.accountlog.treasure;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.BaseTreasureLog;

public class TreasureSuccessLog extends BaseTreasureLog {
	
	private static final long serialVersionUID = 1879381929093016631L;
	private String sumLogRemarkTemplate = "借款合计：借款入账${money}元。";
	private String templateNid=Constant.TREASURE_SUCCESS;
	public TreasureSuccessLog() {
		super();
	}
	public TreasureSuccessLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public TreasureSuccessLog(double money, Account act) {
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
		accountSumDao.editSumByProperty(EnumAccountSumProperty.BORROW_CASH.getValue(), this.getMoney(), this.getUser_id());
	}
}
