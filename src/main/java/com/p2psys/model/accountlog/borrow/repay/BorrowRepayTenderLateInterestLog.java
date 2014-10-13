package com.p2psys.model.accountlog.borrow.repay;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 逾期利息log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowRepayTenderLateInterestLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	private String sumLogRemarkTemplate = "回款合计：收到逾期利息${money}元，标ID：[${borrow.id}]";	
	private String interestSumLogRemarkTemplate = "利息合计：获得逾期利息${money}元，标ID：[${borrow.id}]";


	private String templateNid=Constant.LATE_REPAYMENT_INCOME;
	public BorrowRepayTenderLateInterestLog() {
		super();
	}
	public BorrowRepayTenderLateInterestLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowRepayTenderLateInterestLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(this.getMoney(), this.getMoney(), 0, 0, this.getUser_id());
	}
	
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void accountSumProperty() {
		double realHuikuan = this.getMoney();
		accountSumDao.editSumByProperty(EnumAccountSumProperty.HUIKUAN_INTEREST.getValue(), realHuikuan, this.getUser_id());
		accountSumDao.editSumByProperty(EnumAccountSumProperty.INTEREST.getValue(), this.getMoney(), this.getUser_id());
	} 
	
	
	@Override
	public void addAccountSumLog() {
		double realHuikuan = this.getMoney();
		
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getHuikuan());
		sumLog.setMoney(realHuikuan);
		double afterMoney = sum.getHuikuan() + realHuikuan;
		sumLog.setAfter_money(afterMoney);
		sumLog.setType(Constant.HUIKUAN_INTEREST);
		sumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(sumLog);
		
		AccountSum interestSum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(interestSumLogRemarkTemplate);
		AccountSumLog interestSumLog = this.baseAccountSumLog();
		interestSumLog.setBefore_money( interestSum.getInterest());
		 afterMoney = interestSum.getInterest() + this.getMoney();
		interestSumLog.setAfter_money(afterMoney);
		interestSumLog.setType(EnumAccountSumProperty.INTEREST.getValue());
		interestSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(interestSumLog);
	}
 	
}
