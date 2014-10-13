package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 抽奖奖励
 
 *
 */
public class AwardLotteryLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.LOTTERY_AWARD;
	
	public AwardLotteryLog() {
		super();
	}

	public AwardLotteryLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardLotteryLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}
}
