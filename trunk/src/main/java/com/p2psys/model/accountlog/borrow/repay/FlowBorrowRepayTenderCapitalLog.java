package com.p2psys.model.accountlog.borrow.repay;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 还款投资人收到还款本金log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class FlowBorrowRepayTenderCapitalLog extends BaseBorrowLog{

	private static final long serialVersionUID = 1879381929093016631L;
	private String sumLogRemarkTemplate = "回款合计：收到还款本金${money}元，标ID：[${borrow.id}]";	
	private String templateNid=Constant.CAPITAL_COLLECT;
	public FlowBorrowRepayTenderCapitalLog() {
		super();
	}
	public FlowBorrowRepayTenderCapitalLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public FlowBorrowRepayTenderCapitalLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public void addAccountSumLog() {
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getHuikuan());
		double afterMoney = sum.getHuikuan() + this.getMoney();
		sumLog.setAfter_money(afterMoney);
		sumLog.setType(Constant.HUIKUAN_CAPITAL);
		sumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(sumLog);
	} 
	
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void accountSumProperty() {
		accountSumDao.editSumByProperty(EnumAccountSumProperty.HUIKUAN.getValue(), this.getMoney(), this.getUser_id());

	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(0, this.getMoney(), 0, -this.getMoney(), this.getUser_id());
	}
	
	
	
}
