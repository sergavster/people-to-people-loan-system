package com.p2psys.model.creditlog.handedit;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.model.creditlog.BaseHandEditLog;

/**
 * 手动修改有效积分
 
 * @version 1.0
 * @since 2013-10-22
 */
public class HandEditValidLog extends BaseHandEditLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 354871947361508996L;

	private String logType = EnumCreditTemplateNid.INTEGRAL_VALID_EDIT.getValue();
	
	public HandEditValidLog() {
		super();
	}
	
	public HandEditValidLog(long op , long op_user , long credit_type , long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
		this.setLogType(logType);
	}

}
