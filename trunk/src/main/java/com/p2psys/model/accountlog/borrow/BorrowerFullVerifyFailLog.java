package com.p2psys.model.accountlog.borrow;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;

public class BorrowerFullVerifyFailLog extends BaseSimpleNoticeLog {
	private static final long serialVersionUID = -6837617034387772559L;

	public BorrowerFullVerifyFailLog() {
		super();
	}

	public BorrowerFullVerifyFailLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public BorrowerFullVerifyFailLog(double money, Account act) {
		super(money, act);
	}
	private final static String noticeTypeNid = Constant.NOTICE_BORROW_FULL_FAIL;
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	} 

}
