package com.p2psys.model.accountlog.cash;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseCashSuccessLog;

public class CashSuccessLog extends BaseCashSuccessLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2742843986942001021L;

	private String templateNid = Constant.CASH_SUCCESS;
	
	public CashSuccessLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public CashSuccessLog(double money, Account act) {
		super(money, act);
	}

	public CashSuccessLog() {
		super();
	}
	

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public void addOperateLog() {
		// TODO Auto-generated method stub
		
	}
	
}
