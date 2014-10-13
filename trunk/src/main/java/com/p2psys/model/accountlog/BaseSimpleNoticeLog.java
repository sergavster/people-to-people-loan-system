package com.p2psys.model.accountlog;

import com.p2psys.context.Global;
import com.p2psys.domain.Account;

public class BaseSimpleNoticeLog extends BaseAccountLog {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6629817870765991953L;

	/**
	 * 调用父类
	 */
	public BaseSimpleNoticeLog() {
		super();
	}
	
	public BaseSimpleNoticeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public BaseSimpleNoticeLog(double money, Account act) {
		super(money, act);
	}	


	@Override
	public void doEvent() {
		//调试时手动传参，服务器上通过Spring容器获取
		if(DEBUG){
			transfer();
		}
		//资金记录
		Global.setTransfer("weburl", Global.getString("weburl"));
		//短信
		sendNotice();
		//操作日志
		addOperateLog();
	}
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start
	//移到 BaseAccountLog中去
	/*@Override
	public void sendNotice() {
		Map<String, Object> transferMap = transfer();
		
		long userid = this.getUser_id();
		User user = userDao.getUserByUserid(userid);
		UserCache userCache = userCacheDao.getUserCacheByUserid(userid);
		
		transferMap.put("host", Global.getValue("weburl"));
		transferMap.put("webname", Global.getValue("webname"));
		transferMap.put("noticeTime", DateUtils.getNowTime());
		transferMap.put("user", user);
		transferMap.put("userCache", userCache);
		noticeService.sendNotice(getNoticeTypeNid(), transferMap);
	}

	public String getNoticeTypeNid(){
		return "";
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end
	

}
