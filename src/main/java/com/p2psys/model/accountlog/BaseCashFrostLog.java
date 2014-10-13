package com.p2psys.model.accountlog;

import com.p2psys.domain.Account;

public class BaseCashFrostLog extends BaseAccountLog {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4704143336827461817L;

	/**
	 * 调用父类
	 */
	public BaseCashFrostLog() {
		super();
	}
	
	public BaseCashFrostLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public BaseCashFrostLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}

}
