package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 急难资金奖励
 
 *
 */
public class AwardTroubleLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.TROUBLE_AWARD;
	
	public AwardTroubleLog() {
		super();
	}

	public AwardTroubleLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardTroubleLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}
}
