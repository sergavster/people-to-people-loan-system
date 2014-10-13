package com.p2psys.model.accountlog.borrow.repay;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

public class BorrowRepayExtensionInterestLog extends DeductSuccessLog{

	private static final long serialVersionUID = 4329082898265462494L;
	private String templateNid = Constant.BORROW_REPAY_EXT_INTEREST;
	public BorrowRepayExtensionInterestLog() {
		super();
	}
	public BorrowRepayExtensionInterestLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowRepayExtensionInterestLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(-this.getMoney(), 0,-this.getMoney(), 0, this.getUser_id());
	}
	
	public String getTemplateNid(){
		return templateNid;
	}
	
}
