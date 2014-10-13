package com.p2psys.model.accountlog.vip;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseAccountLog;

/**
 * vip会员费
 * 
 
 * @version 1.0
 * @since 2013-9-4
 */
public class UnFreezeVipLog extends BaseAccountLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1334829709516990114L;

	/**
	 * 类型：vip会员费
	 */
	private String templateNid = Constant.VIP_FAIL;

	/**
	 * vip会员费
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public UnFreezeVipLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * vip会员费
	 * @param money money
	 * @param act act
	 */
	public UnFreezeVipLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * vip会员费
	 */
	public UnFreezeVipLog() {
		super();
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
