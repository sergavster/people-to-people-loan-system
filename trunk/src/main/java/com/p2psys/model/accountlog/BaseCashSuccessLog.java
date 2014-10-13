package com.p2psys.model.accountlog;

import java.util.Date;
import java.util.Map;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.UserCacheDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountCash;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.util.DateUtils;

public class BaseCashSuccessLog extends BaseAccountLog {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8855780355016265315L;
	
	/**
	 * 调用父类
	 */
	public BaseCashSuccessLog() {
		super();
	}
	
	public BaseCashSuccessLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public BaseCashSuccessLog(double money, Account act) {
		super(money, act);
	}

	@Override
	public void accountSumProperty() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		// 取出可能扣款的值
		double cashFee = accountSum.getCash_fee();
		double money = this.getMoney();

		long user_id = this.getUser_id();
		long to_user_id = this.getTo_user();
		String addtime = String.valueOf(DateUtils.getTime(new Date()));
		String addip = "";
		
		Map<String, Object> transferMap = transfer();
		AccountCash cash = (AccountCash)transferMap.get("cash");
		
		//添加提现费的account_sum_log
		String fee = cash.getFee();
		if(fee == null || fee.length() <= 0){
			fee = "0";
		}
		double cashFeeMoney = Double.parseDouble(fee);
		if(cashFeeMoney > 0){
			double cashfee_before_money = cashFee;
			double cashfee_money = cashFeeMoney;
			double cashfee_after_money = cashFee + cashFeeMoney;
			
			AccountSumLog caseFeeAccountSumLog = new AccountSumLog();
			caseFeeAccountSumLog.setUser_id(user_id);
			caseFeeAccountSumLog.setTo_user_id(to_user_id);
			caseFeeAccountSumLog.setBefore_money(cashfee_before_money);
			caseFeeAccountSumLog.setMoney(cashfee_money);
			caseFeeAccountSumLog.setAfter_money(cashfee_after_money);
			caseFeeAccountSumLog.setAddip(addip);
			caseFeeAccountSumLog.setAddtime(addtime);
			this.setAccountSumLogRemarkTemplate(cashfeeRemark);
			caseFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			caseFeeAccountSumLog.setType(EnumAccountSumProperty.CASH_FEE.getValue());
			accountSumLogDao.addAccountSumLog(caseFeeAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.CASH_FEE.getValue(),	cashfee_money, this.getUser_id());
		}
		
		//提现成功增加account sum 中的case 
		//生成account_sum_log
		double currCash = money;
		if(currCash > 0){
			double cash_before_money = accountSum.getCash();
			double cash_money = currCash;
			double cash_after_money = accountSum.getCash() + currCash;
			
			AccountSumLog cashAccountSumLog = new AccountSumLog();
			cashAccountSumLog.setUser_id(user_id);
			cashAccountSumLog.setTo_user_id(to_user_id);
			cashAccountSumLog.setBefore_money(cash_before_money);
			cashAccountSumLog.setMoney(cash_money);
			cashAccountSumLog.setAfter_money(cash_after_money);
			cashAccountSumLog.setAddip(addip);
			cashAccountSumLog.setAddtime(addtime);
			this.setAccountSumLogRemarkTemplate(cashRemark);
			cashAccountSumLog.setRemark(this.getAccountSumLogRemark());
			cashAccountSumLog.setType(EnumAccountSumProperty.CASH.getValue());
			accountSumLogDao.addAccountSumLog(cashAccountSumLog);
			accountSumDao.editSumByProperty(EnumAccountSumProperty.CASH.getValue(),	money, this.getUser_id());
		}
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 start
	
	/*@Override
	public void sendNotice() {
		Map<String, Object> transferMap = transfer();
		AccountCash cash = (AccountCash)transferMap.get("cash");
		
		long userid = this.getUser_id();
		User user = userDao.getUserByUserid(userid);
		UserCache userCache = userCacheDao.getUserCacheByUserid(userid);
		
		HashMap sendData = new HashMap();
		sendData.put("webname", Global.getValue("webname"));
		sendData.put("user", user);
		sendData.put("userCache", userCache);
		sendData.put("cash", cash);
		noticeService.sendNotice(Constant.NOTICE_CASH_VERIFY_SUCC, sendData);	
		
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-27 end
	private final static String noticeTypeNid = Constant.NOTICE_CASH_VERIFY_SUCC;
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	} 
	
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
