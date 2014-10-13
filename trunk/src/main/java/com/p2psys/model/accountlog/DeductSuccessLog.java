package com.p2psys.model.accountlog;

import java.util.Date;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.util.DateUtils;

/**
 * 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-2
 */
public class DeductSuccessLog extends BaseAccountLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1619557956342886141L;
	
	/**
	 * 调用父类
	 */
	public DeductSuccessLog() {
		super();
	}
	
	/**
	 * 调用父类
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public DeductSuccessLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 调用父类
	 * @param money money
	 * @param act act
	 */
	public DeductSuccessLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}
	
	@Override 
	public void accountSumProperty() {
		this.sumManage();
		double money = this.getMoney();
		//扣款合计
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		AccountSumLog sumLog = this.baseAccountSumLog();
		Global.setTransfer("money", money);
		this.setAccountSumLogRemarkTemplate(usedRemark);
		sumLog.setRemark(this.getAccountSumLogRemark());
		sumLog.setBefore_money( sum.getDeduct() );
		double afterMoney = sum.getDeduct() + this.getMoney();
		sumLog.setAfter_money(afterMoney);
		accountSumLogDao.addAccountSumLog(sumLog);
		accountSumDao.editSumByProperty(EnumAccountSumProperty.DEDUCT.getValue(),	money, this.getUser_id());
	}
	
}
