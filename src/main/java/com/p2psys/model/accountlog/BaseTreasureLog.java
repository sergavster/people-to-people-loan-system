package com.p2psys.model.accountlog;

import com.p2psys.domain.Account;

public class BaseTreasureLog extends BaseAccountLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6935432949174425461L;

	public BaseTreasureLog() {
		super();
	}
	public BaseTreasureLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BaseTreasureLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getLogRemarkTemplate() {
		return super.getLogRemarkTemplate();
	}
	
	@Override
	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}
	
	@Override
	public String getType() {
		return super.getType();
	}
	@Override
	public void addAccountSumLog() {
		super.addAccountSumLog();
	}
	@Override
	public void accountSumProperty() {
		super.accountSumProperty();
	}  
	
}
