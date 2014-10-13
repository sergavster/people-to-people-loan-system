package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

/**
 * 扣款
 * 
 
 * @version 1.0
 * @since 2013-9-4
 */
public class DeductAccountBackLog extends DeductSuccessLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1334829709516990114L;

	/**
	 * 类型：扣款
	 */
	private String templateNid = Constant.ACCOUNT_BACK;

	private final static String noticeTypeNid = Constant.NOTICE_HOUTAI_DEDUCT_SUCC;
	
	/**
	 * 扣款
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public DeductAccountBackLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 扣款
	 * @param money money
	 * @param act act
	 */
	public DeductAccountBackLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 扣款
	 */
	public DeductAccountBackLog() {
		super();
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	} 

}
