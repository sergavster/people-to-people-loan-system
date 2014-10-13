package com.p2psys.model.accountlog;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;

public class AwardSuccessLog extends BaseAccountLog{

	private static final long serialVersionUID = -8411459430896299958L;
	
	protected String logAwardRemarkTemplate = "奖励总和:收入奖励${award}元。";

	public AwardSuccessLog() {
		super();
	}
	
	public AwardSuccessLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardSuccessLog(double money, Account act) {
		super(money, act);
	}

	public AwardSuccessLog(double money, Account act, long toUser, String type) {
		super(money, act);
		this.setTo_user(toUser);
		this.setType(type);
		this.setRemark(this.getLogRemark());
	}

	@Override
	public void accountSumProperty() {
		accountSumDao.editSumByProperty( EnumAccountSumProperty.AWARD.getValue(), this.getMoney(), this.getUser_id());
	}

	@Override
	public String getAccountSumLogRemarkTemplate() {
		return logAwardRemarkTemplate;
	}

	@Override
	public void addAccountSumLog() {
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getAward() );
		double afterMoney = sum.getAward() + this.getMoney();
		sumLog.setAfter_money(afterMoney);
		accountSumLogDao.addAccountSumLog(sumLog);
	}

}
