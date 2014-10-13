package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 积分兑换奖励
 * @version 1.0
 * @since 2013-10-28
 */
public class AwardIntegralConvertLog extends AwardSuccessLog{

private static final long serialVersionUID = 6234630251774622982L;
	

	private String templateNid = Constant.CONVERT_SUCCESS;
	
	public AwardIntegralConvertLog() {
		super();
	}

	public AwardIntegralConvertLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardIntegralConvertLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}
}
