package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 好友邀请奖励
 
 *
 */
public class AwardInviteLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.INVITE_AWARD;
	
	public AwardInviteLog() {
		super();
	}

	public AwardInviteLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardInviteLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}
	
	
	
}
