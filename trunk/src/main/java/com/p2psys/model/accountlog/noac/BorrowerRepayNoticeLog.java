package com.p2psys.model.accountlog.noac;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;

public class BorrowerRepayNoticeLog extends BaseSimpleNoticeLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6837617034387772559L;

	public BorrowerRepayNoticeLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BorrowerRepayNoticeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
		// TODO Auto-generated constructor stub
	}

	public BorrowerRepayNoticeLog(double money, Account act) {
		super(money, act);
		// TODO Auto-generated constructor stub
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start

	/*@Override
	public void sendNotice() {
		Map<String, Object> transferMap = transfer();
		
		long userid = this.getUser_id();
		User user = userDao.getUserByUserid(userid);
		UserCache userCache = userCacheDao.getUserCacheByUserid(userid);
		
		HashMap sendData = new HashMap();
		sendData.put("webname", Global.getValue("webname"));
		sendData.put("user", user);
		sendData.put("userCache", userCache);
		noticeService.sendNotice(Constant.NOTICE_BORROWER_REPAY_NOTICE, sendData);
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end
	private final static String noticeTypeNid = Constant.NOTICE_BORROWER_REPAY_NOTICE;
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	} 
}
