package com.p2psys.model.accountlog.borrow.repay;

import java.util.Map;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 还款投资人收到还款利息log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class FlowBorrowRepayTenderInterestLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	private String sumLogRemarkTemplate = "回款合计：收到还款利息${money-borrowFee}元，标ID：[${borrow.id}]";	
	private String interestSumLogRemarkTemplate="利息合计：收到利息${money}元，标ID：[${borrow.id}]";
	private String templateNid = Constant.INTEREST_COLLECT;
	public FlowBorrowRepayTenderInterestLog() {
		super();
	}
	public FlowBorrowRepayTenderInterestLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public FlowBorrowRepayTenderInterestLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public void addAccountSumLog() {
		Map tranData = transfer();
		double borrowFee = Double.parseDouble(tranData.get("borrowFee").toString());
		double realHuikuan = this.getMoney() - borrowFee;
		
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

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void accountSumProperty() {
		Map tranData = transfer();
		double borrowFee = Double.parseDouble(tranData.get("borrowFee").toString());
		double realHuikuan = this.getMoney() - borrowFee;
		accountSumDao.editSumByProperty(EnumAccountSumProperty.HUIKUAN_INTEREST.getValue(), realHuikuan, this.getUser_id());
		accountSumDao.editSumByProperty(EnumAccountSumProperty.INTEREST.getValue(), this.getMoney(), this.getUser_id());
	}
	
}
