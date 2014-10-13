package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 邀请充值奖励
 
 *
 */
public class AwardInviteRechargeLog extends AwardSuccessLog{

	private static final long serialVersionUID = -1609634676532690085L;
	
	private String templateNid = Constant.Invite_RECHARGE_AWARD;
	
	public AwardInviteRechargeLog() {
		super();
	}

	public AwardInviteRechargeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardInviteRechargeLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}
}
