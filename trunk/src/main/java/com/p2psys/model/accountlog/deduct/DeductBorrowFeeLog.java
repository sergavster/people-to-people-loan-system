package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

/**
 * 借款手续费
 * 
 
 * @version 1.0
 * @since 2013-9-4
 */
public class DeductBorrowFeeLog extends DeductSuccessLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3454258014338158048L;

	/**
	 * 类型：借款手续费
	 */
	private String templateNid = Constant.BORROW_FEE;

	/**
	 * 借款手续费
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public DeductBorrowFeeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 借款手续费
	 * @param money money
	 * @param act act
	 */
	public DeductBorrowFeeLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 借款手续费
	 */
	public DeductBorrowFeeLog() {
		super();
	}

	public String getTemplateNid(){
		return templateNid;
	}
}
