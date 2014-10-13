package com.p2psys.model.accountlog.cash;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseCashUnfrostLog;

public class CashFailLog extends BaseCashUnfrostLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7652989665222720130L;

	private String templateNid=Constant.CASH_FAIL;
	
	private final static String noticeTypeNid = Constant.NOTICE_CASH_VERIFY_FAIL;
	
	public CashFailLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public CashFailLog(double money, Account act) {
		super(money, act);
	}

	public CashFailLog() {
		super();
	}
	

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	} 
	
	@Override
	public void addOperateLog() {
		// TODO Auto-generated method stub
		
	}
	
	
}