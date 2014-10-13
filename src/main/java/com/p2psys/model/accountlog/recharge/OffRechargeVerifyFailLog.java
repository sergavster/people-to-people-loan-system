package com.p2psys.model.accountlog.recharge;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;

public class OffRechargeVerifyFailLog extends BaseSimpleNoticeLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6837617034387772559L;

	public OffRechargeVerifyFailLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OffRechargeVerifyFailLog(double money, Account act, long toUser) {
		super(money, act, toUser);
		// TODO Auto-generated constructor stub
	}

	public OffRechargeVerifyFailLog(double money, Account act) {
		super(money, act);
		// TODO Auto-generated constructor stub
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start

	/*@Override
	public void sendNotice() {
		Map<String, Object> transferMap = transfer();
		AccountRecharge recharge = (AccountRecharge)transferMap.get("recharge");
		
		
		long userid = this.getUser_id();
		User user = userDao.getUserByUserid(userid);
		UserCache userCache = userCacheDao.getUserCacheByUserid(userid);
		
		HashMap sendData = new HashMap();
		sendData.put("webname", Global.getValue("webname"));
		sendData.put("user", user);
		sendData.put("userCache", userCache);
		sendData.put("recharge", recharge);
		noticeService.sendNotice(Constant.NOTICE_DOWN_RECHARGE_VERIFY_FAIL, sendData);
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end
	private final static String noticeTypeNid = Constant.NOTICE_DOWN_RECHARGE_VERIFY_FAIL;
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	} 
}
