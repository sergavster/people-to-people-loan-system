package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 第一次投标奖励
 
 *
 */
public class AwardFirstTenderLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.FIRST_TENDER_AWARD;
	
	public AwardFirstTenderLog() {
		super();
	}

	public AwardFirstTenderLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardFirstTenderLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}

}
