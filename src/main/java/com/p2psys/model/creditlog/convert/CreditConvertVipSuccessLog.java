package com.p2psys.model.creditlog.convert;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.model.creditlog.BaseCreditConvertLog;

public class CreditConvertVipSuccessLog extends BaseCreditConvertLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876174264156514032L;
	
	private String logType = EnumCreditTemplateNid.CONVERT_VIP_SUCCESS.getValue();
	
	public CreditConvertVipSuccessLog() {
		super();
	}
	
	public CreditConvertVipSuccessLog(long op, long op_user, long credit_type, long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
		this.setLogType(logType);
	}

}
