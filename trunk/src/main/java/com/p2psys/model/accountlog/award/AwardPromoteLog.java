package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 及时雨推广奖励日志
 * 
 
 * @version 1.0
 * @since 2013-10-18
 */
public class AwardPromoteLog extends AwardSuccessLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1411673260755202276L;
	
	/**
	 * 类型：及时雨推广奖励
	 */
	private String templateNid = Constant.PROMOTE_AWARD;
	
	public AwardPromoteLog() {
		super();
	}

	public AwardPromoteLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardPromoteLog(double money, Account act) {
		super(money, act);
	}
	
	public String getTemplateNid(){
		return templateNid;
	}
}
