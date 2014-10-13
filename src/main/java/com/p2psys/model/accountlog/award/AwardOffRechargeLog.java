package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 线下充值奖励
 
 *
 */
public class AwardOffRechargeLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.OFFRECHARGE_AWARD;
	
	public AwardOffRechargeLog() {
		super();
	}

	public AwardOffRechargeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardOffRechargeLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}
}
