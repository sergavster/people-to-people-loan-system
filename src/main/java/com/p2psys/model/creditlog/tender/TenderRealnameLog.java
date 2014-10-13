package com.p2psys.model.creditlog.tender;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.model.creditlog.BaseTenderCreditLog;

/**
 * 实名认证通过
 
 * @version 1.0
 * @since 2013-10-22
 */
public class TenderRealnameLog extends BaseTenderCreditLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4809316942890136365L;

	// 投资积分类型
	private String logType = EnumCreditTemplateNid.TENDER_REALNAME.getValue();
	// 综合积分类型
	private String totalLogType = EnumCreditTemplateNid.REALNAME.getValue();
	
	public TenderRealnameLog(){
		super();
	}
	
	public TenderRealnameLog(long op , long op_user , long credit_type , long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
		this.setLogType(logType);
		this.setTotalLogType(totalLogType);
	}
}
