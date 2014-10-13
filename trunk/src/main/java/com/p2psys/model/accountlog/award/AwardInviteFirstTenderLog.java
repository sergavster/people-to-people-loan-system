package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 民生创投邀请好友首次投标奖励
 
 *
 */
public class AwardInviteFirstTenderLog extends AwardSuccessLog {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6831632919632831263L;

	/**
	 * 类型:邀请好友首次投标奖励
	 */
	private String templateNid = Constant.INVITE_FIRST_TENDER_AWARD;
	
	/**
	 * 邀请好友首次投标奖励
	 */
	public AwardInviteFirstTenderLog() {
		super();
	}

	/**
	 * 邀请好友首次投标奖励
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public AwardInviteFirstTenderLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 邀请好友首次投标奖励
	 * @param money money
	 * @param act act
	 */
	public AwardInviteFirstTenderLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 类型:日志模板类型
	 * @return String
	 */
	public String getTemplateNid(){
		return templateNid;
	}

}
