package com.p2psys.model.accountlog.noac;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseBorrowLog;

public class AutoTenderLog extends BaseBorrowLog {



	/**
	 * 
	 */
	private static final long serialVersionUID = 4267197583072218613L;
	
	private String templateNid=Constant.TENDER;
	

	public AutoTenderLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AutoTenderLog(double money, Account act, long toUser) {
		super(money, act, toUser);
		// TODO Auto-generated constructor stub
	}

	public AutoTenderLog(double money, Account act) {
		super(money, act);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start

	/*@Override
	public void sendNotice() {
		Map<String, Object> transferMap = transfer();
		
		long userid = this.getUser_id();
		User user = userDao.getUserByUserid(userid);
		UserCache userCache = userCacheDao.getUserCacheByUserid(userid);
		
		transferMap.put("webname", Global.getValue("webname"));
		transferMap.put("noticeTime", DateUtils.getNowTime());
		transferMap.put("user", user);
		transferMap.put("userCache", userCache);
		noticeService.sendNotice(getNoticeTypeNid(), transferMap);
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end

	private final static String noticeTypeNid = Constant.NOTICE_AUTO_TENDER;
	public String getNoticeTypeNid(){
		return noticeTypeNid;
	}
}
