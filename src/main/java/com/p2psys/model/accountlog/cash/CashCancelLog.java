package com.p2psys.model.accountlog.cash;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseCashUnfrostLog;

public class CashCancelLog extends BaseCashUnfrostLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8953353763137558058L;

	private String templateNid = Constant.CASH_CANCEL;
	
	public CashCancelLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public CashCancelLog(double money, Account act) {
		super(money, act);
	}

	public CashCancelLog() {
		super();
	}
	

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
