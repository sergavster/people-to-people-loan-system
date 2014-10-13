package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

/**
 * 充值手续费
 * 
 
 * @version 1.0
 * @since 2013-9-4
 */
public class DeductFeeLog extends DeductSuccessLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2406147548880262016L;

	/**
	 * 类型：充值手续费
	 */
	private String templateNid = Constant.FEE;

	/**
	 * 充值手续费
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public DeductFeeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 充值手续费
	 * @param money money
	 * @param act act
	 */
	public DeductFeeLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 充值手续费
	 */
	public DeductFeeLog() {
		super();
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
