package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseAccountLog;

/**
 * 扣款
 * 
 
 * @version 1.0
 * @since 2013-9-4
 */
public class DeductAccountBackFreezeLog extends BaseAccountLog {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7194222489345232822L;

	/**
	 * 类型：扣款
	 */
	private String templateNid = Constant.ACCOUNT_BACK_FREEZE;

	/**
	 * 扣款
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public DeductAccountBackFreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 扣款
	 * @param money money
	 * @param act act
	 */
	public DeductAccountBackFreezeLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 扣款
	 */
	public DeductAccountBackFreezeLog() {
		super();
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
