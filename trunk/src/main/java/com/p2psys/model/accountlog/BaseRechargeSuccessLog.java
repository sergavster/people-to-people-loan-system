package com.p2psys.model.accountlog;

import java.util.Date;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Global;
import com.p2psys.dao.UserCacheDao;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.util.DateUtils;

public class BaseRechargeSuccessLog extends BaseAccountLog {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -648644944435247169L;
//
//	protected UserCacheDao userCacheDao;
//	protected UserDao userDao;
	
	
	/**
	 * 调用父类
	 */
	public BaseRechargeSuccessLog() {
		super();
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
			userDao=(UserDao)ctx.getBean("userDao");
		}
	}
	
	public BaseRechargeSuccessLog(double money, Account act, long toUser) {
		super(money, act, toUser);
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
			userDao=(UserDao)ctx.getBean("userDao");
		}
	}

	public BaseRechargeSuccessLog(double money, Account act) {
		super(money, act);
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
			userDao=(UserDao)ctx.getBean("userDao");
		}
	}


	/**
	 * 扣款成功日志
	 * @param money money
	 * @param act act
	 * @param type type
	 * @param toUser toUser
	 */
	public BaseRechargeSuccessLog(double money, Account act, String type, long toUser) {
		super(money, act);

		this.setType(type);
		this.setRemark(this.getLogRemark());
		this.setTo_user(toUser);
	}
	
	@Override
	public void accountSumProperty() {
		//先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());	
		double recharge = accountSum.getRecharge();
		double currRecharge = this.getMoney();;
		if(currRecharge > 0){
			long user_id = this.getUser_id();
			long to_user_id = this.getTo_user();
			String addtime = String.valueOf(DateUtils.getTime(new Date()));
			String addip = "";
			
			//增加recharge sum log 日志
			double recharge_before_money = recharge;
			double recharge_money = currRecharge;
			double recharge_after_money = recharge + currRecharge;
			Global.setTransfer("money", recharge_money);
			AccountSumLog rechargeAccountSumLog = new AccountSumLog();
			rechargeAccountSumLog.setUser_id(user_id);
			rechargeAccountSumLog.setTo_user_id(to_user_id);
			rechargeAccountSumLog.setBefore_money(recharge_before_money);
			rechargeAccountSumLog.setMoney(recharge_money);
			rechargeAccountSumLog.setAfter_money(recharge_after_money);
			rechargeAccountSumLog.setAddip(addip);
			rechargeAccountSumLog.setAddtime(addtime);
			this.setAccountSumLogRemarkTemplate(rechargeRemark);
			rechargeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			rechargeAccountSumLog.setType(EnumAccountSumProperty.RECHARGE.getValue());
			accountSumLogDao.addAccountSumLog(rechargeAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.RECHARGE.getValue(), recharge_money, this.getUser_id());
		}
	}
	
	
	public UserCacheDao getUserCacheDao() {
		return userCacheDao;
	}

	public void setUserCacheDao(UserCacheDao userCacheDao) {
		this.userCacheDao = userCacheDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start
	//移到 BaseAccountLog中去
	/*public String getNoticeTypeNid(){
		return "";
	}
	
	@Override
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
		noticeService.sendNotice(getNoticeTypeNid(), sendData);
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end
	@Override
	public Map<String, Object> transfer() {
		super.transfer();
		Map<String,Object> data=Global.getTransfer();
		if(userCacheDao==null){
			userCacheDao=(UserCacheDao)data.get("userCacheDao");
		}

		return data;
	}
	
	@Override
	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}
}
