package com.p2psys.model.creditlog.convert;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.model.creditlog.BaseCreditConvertLog;

/**
 * 积分兑换失败
 
 * @version 1.0
 * @since 2013-10-18
 */
public class CreditConvertVipFailLog extends BaseCreditConvertLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3406670208538351421L;
	
	private String logType = EnumCreditTemplateNid.CONVERT_VIP_FAIL.getValue();
	
	public CreditConvertVipFailLog() {
		super();
	}
	
	public CreditConvertVipFailLog(long op, long op_user, long credit_type, long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
		this.setLogType(logType);
	}

}
