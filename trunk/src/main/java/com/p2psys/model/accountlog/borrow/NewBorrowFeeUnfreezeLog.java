package com.p2psys.model.accountlog.borrow;

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
public class NewBorrowFeeUnfreezeLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7275352019794841735L;
	private String templateNid = Constant.NEW_BORROW_FEE_UNFREEZE;
	public NewBorrowFeeUnfreezeLog() {
		super();
	}
	public NewBorrowFeeUnfreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public NewBorrowFeeUnfreezeLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccountNotZero(0, this.getMoney(), -this.getMoney(), this.getTo_user());
	}
	
	
}
