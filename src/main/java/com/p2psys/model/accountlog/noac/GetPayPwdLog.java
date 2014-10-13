package com.p2psys.model.accountlog.noac;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;

public class GetPayPwdLog extends BaseSimpleNoticeLog {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8115081360693021279L;

	public GetPayPwdLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GetPayPwdLog(double money, Account act, long toUser) {
		super(money, act, toUser);
		// TODO Auto-generated constructor stub
	}

	public GetPayPwdLog(double money, Account act) {
		super(money, act);
		// TODO Auto-generated constructor stub
	}
	
	public GetPayPwdLog(long user_id) {
		super();
		this.setUser_id(user_id);
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start

	/*@Override
	public void sendNotice() {
		Map<String, Object> transferMap = transfer();
		String getUrl = transferMap.get("getUrl").toString(); 
		
		long userid = this.getUser_id();
		User user = userDao.getUserByUserid(userid);
		UserCache userCache = userCacheDao.getUserCacheByUserid(userid);
		
		HashMap sendData = new HashMap();
		sendData.put("host", Global.getValue("weburl"));
		sendData.put("webname", Global.getValue("webname"));
		sendData.put("user", user);
		sendData.put("userCache", userCache);
		sendData.put("getUrl", getUrl);
		noticeService.sendNotice(Constant.NOTICE_GET_PAYPWD, sendData);
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end
	private final static String noticeTypeNid = Constant.NOTICE_GET_PAYPWD;
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	} 
}
