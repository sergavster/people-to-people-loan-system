package com.p2psys.model.accountlog.noac;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;

public class UserCertifyFailLog extends BaseSimpleNoticeLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6837617034387772559L;

	public UserCertifyFailLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserCertifyFailLog(double money, Account act, long toUser) {
		super(money, act, toUser);
		// TODO Auto-generated constructor stub
	}

	public UserCertifyFailLog(double money, Account act) {
		super(money, act);
		// TODO Auto-generated constructor stub
	}
	
	public UserCertifyFailLog(long user_id) {
		super();
		this.setUser_id(user_id);
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start

	/*@Override
	public void sendNotice() {
		Map<String, Object> transferMap = transfer();
		String type = transferMap.get("type").toString(); 
		
		long userid = this.getUser_id();
		User user = userDao.getUserByUserid(userid);
		UserCache userCache = userCacheDao.getUserCacheByUserid(userid);
		
		HashMap sendData = new HashMap();
		sendData.put("webname", Global.getValue("webname"));
		sendData.put("user", user);
		sendData.put("userCache", userCache);
		sendData.put("type", type);
		noticeService.sendNotice(Constant.NOTICE_CERTIFY_FAIL, sendData);
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end
	private final static String noticeTypeNid = Constant.NOTICE_CERTIFY_FAIL;
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	} 
}
