package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 累计投资奖励
 
 *
 */
public class AwardAccumulateTenderLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.TENDER_AWARD;
	
	public AwardAccumulateTenderLog() {
		super();
	}

	public AwardAccumulateTenderLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardAccumulateTenderLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}
}
