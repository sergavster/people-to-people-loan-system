package com.p2psys.model.creditlog.convert;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.model.creditlog.BaseCreditConvertLog;

/**
 * 积分兑换申请
 
 * @version 1.0
 * @since 2013-10-18
 */
public class CreditConvertVipApplyLog extends BaseCreditConvertLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String logType = EnumCreditTemplateNid.CONVERT_VIP_APPLY.getValue();
	
	public CreditConvertVipApplyLog() {
		super();
	}
	
	public CreditConvertVipApplyLog(long op, long op_user, long credit_type, long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
		this.setLogType(logType);
	}

}
