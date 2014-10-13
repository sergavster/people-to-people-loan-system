package com.p2psys.model.accountlog.borrow;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;

public class TenderNewBorrowLog extends BaseSimpleNoticeLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6837617034387772559L;

	private final static String noticeTypeNid = Constant.NOTICE_NEW_BORROW;
	
	public TenderNewBorrowLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TenderNewBorrowLog(double money, Account act, long toUser) {
		super(money, act, toUser);
		// TODO Auto-generated constructor stub
	}

	public TenderNewBorrowLog(double money, Account act) {
		super(money, act);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	} 
}
