package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

/**
 * 交易手续费
 * 
 
 * @version 1.0
 * @since 2013-9-4
 */
public class DeductTransactionFeeLog extends DeductSuccessLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2406147548880262016L;

	/**
	 * 类型：交易手续费
	 */
	private String templateNid = Constant.TRANSACTION_FEE;

	/**
	 * 交易手续费
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public DeductTransactionFeeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 交易手续费
	 * @param money money
	 * @param act act
	 */
	public DeductTransactionFeeLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 交易手续费
	 */
	public DeductTransactionFeeLog() {
		super();
	}

	@Override
	public void updateAccount() {
		accountDao.updateAccount(-this.getMoney(), 0, -this.getMoney(), 0, this.getUser_id());
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
