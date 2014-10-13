package com.p2psys.model.accountlog.borrow.repay;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 撤回标解冻投资人资金log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowRepayFreezeLog extends BaseBorrowLog{
	private static final long serialVersionUID = -3421345633422627294L;
	private String templateNid = Constant.FREEZE;
	
	public BorrowRepayFreezeLog() {
		super();
	}
	public BorrowRepayFreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowRepayFreezeLog(double money, Account act) {
		super(money, act);
	}
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void updateAccount() {
		accountDao.updateAccount(0, -this.getMoney(), this.getMoney(), 0, this.getUser_id());
	}
}
