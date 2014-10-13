package com.p2psys.model.creditlog.bbs;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.model.creditlog.BaseBBSLog;

/**
 * 抽象抽中奖励积分
 
 * @version 1.0
 * @since 2013-10-25
 */
public class BBSIntegralLog extends BaseBBSLog {

	private static final long serialVersionUID = 1L;

	private String logType = EnumCreditTemplateNid.BBS_INTEGRAL.getValue();
	
	public BBSIntegralLog() {
		super();
	}
	
	public BBSIntegralLog(long op, long op_user, long credit_type, long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
		this.setLogType(logType);
	}
}
