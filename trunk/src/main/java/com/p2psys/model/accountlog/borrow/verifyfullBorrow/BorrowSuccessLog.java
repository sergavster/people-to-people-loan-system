package com.p2psys.model.accountlog.borrow.verifyfullBorrow;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 借款成功log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowSuccessLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	
	private String sumLogRemarkTemplate = "借款合计：借款入账${money}元，标ID：[${borrow.id}]";
	
	private String templateNid=Constant.BORROW_SUCCESS;
	
	public BorrowSuccessLog() {
		super();
	}
	
	public BorrowSuccessLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	
	public BorrowSuccessLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public void accountSumProperty() {
		accountSumDao.editSumByProperty(EnumAccountSumProperty.BORROW_CASH.getValue(),  this.getMoney(), this.getUser_id());
	}
	

	@Override
	public void addAccountSumLog() {
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getBorrow_cash());
		double afterMoney = sum.getBorrow_cash() + this.getMoney();
		sumLog.setAfter_money(afterMoney);
		accountSumLogDao.addAccountSumLog(sumLog);
	} 
	
	@Override
	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}
	
	@Override
	public String getTemplateNid(){
		return templateNid;
	}

	private final static String noticeTypeNid = Constant.NOTICE_BORROW_FULL_SUCC;
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(this.getMoney(), this.getMoney(), 0, 0, this.getUser_id());
	}
	
}
