package com.p2psys.model.accountlog.borrow.repay;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.DeductSuccessLog;
/**
 * 标还款扣除利息log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowRepayInterestLog extends DeductSuccessLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	
	private String sumLogRemarkTemplate = "已还款合计：已还款利息${money}元，标ID：[${borrow.id}]。";
	
	private String templateNid = Constant.REPAID_INTEREST;
	
	public BorrowRepayInterestLog() {
		super();
	}
	public BorrowRepayInterestLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowRepayInterestLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(-this.getMoney(), 0,-this.getMoney(), 0, this.getUser_id());
	}
	
	@Override
	public void accountSumProperty() {
		this.sumManage();
		accountSumDao.editSumByProperty(EnumAccountSumProperty.REPAY_CASH.getValue(), this.getMoney(), this.getUser_id());

	}
	
	@Override
	public void addAccountSumLog() {
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getRepay_cash());
		double afterMoney = sum.getRepay_cash() + this.getMoney();
		sumLog.setAfter_money(afterMoney);
		accountSumLogDao.addAccountSumLog(sumLog);
	} 
	public String getTemplateNid(){
		return templateNid;
	}
}
