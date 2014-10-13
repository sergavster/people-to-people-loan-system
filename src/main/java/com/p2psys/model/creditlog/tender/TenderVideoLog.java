package com.p2psys.model.creditlog.tender;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.model.creditlog.BaseTenderCreditLog;

/**
 * 视频认证获得积分
 
 * @version 1.0
 * @since 2013-10-22
 */
public class TenderVideoLog  extends BaseTenderCreditLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4809316942890136365L;

	// 投资积分类型
	private String logType = EnumCreditTemplateNid.TENDER_VIDEO.getValue();
	// 综合积分类型
	private String totalLogType = EnumCreditTemplateNid.VIDEO.getValue();
	
	public TenderVideoLog(){
		super();
	}
	
	public TenderVideoLog(long op, long op_user, long credit_type, long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
		this.setLogType(logType);
		this.setTotalLogType(totalLogType);
	}
}
