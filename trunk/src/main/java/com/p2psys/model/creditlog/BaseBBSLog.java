package com.p2psys.model.creditlog;

import java.util.Map;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.common.enums.EnumLogTemplateType;
import com.p2psys.context.Global;
import com.p2psys.domain.CreditType;
import com.p2psys.util.FreemarkerUtil;

/**
 * 论坛积分父类
 
 * @version 1.0
 * @since 2013-10-25
 */
public class BaseBBSLog extends BaseCreditLog {

	private static final long serialVersionUID = -141858044155521465L;

	// 论坛积分类型
	private String totalLogType = EnumCreditTemplateNid.INTEGRAL_TOTAL.getValue();
	
	// 综合积分模板
	protected String totalLogRemarkTemplate;
	
	public BaseBBSLog() {
		super();
	}
	
	public BaseBBSLog(long op , long op_user , long credit_type , long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
	}
	
	/**
	 * 论坛积分日志处理
	 */
	@Override
	public void addCreditLog() {
		Map<String , Object> map = Global.getTransfer();
		String editRemark =  (String) map.get("editRemark");//修改前手动填写备注
		
		//赠送积分日志处理
		if(editRemark != null && editRemark.length() > 0 && editRemark.length() < 255){//如果手动填写了备注，就将备注填写日志表中
			this.setRemark(editRemark);
		}else {// 如果没有手动输入备注，则从模板中读取备注。
			logRemarkTemplate = Global.getLogTempValue(EnumLogTemplateType.CREDIT_LOG.getValue(), logType);
			this.setRemark(this.getLogRemark());
		}
		userCreditDao.addUserCreditLog(this);
		
		CreditType totalType = userCreditDao.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_TOTAL.getValue());
		int valid_value = (Integer) map.get("valid_value");
		this.setValue(valid_value);
		//赠送积分日志处理
		if(editRemark != null && editRemark.length() > 0 && editRemark.length() < 255){//如果手动填写了备注，就将备注填写日志表中
			this.setRemark(editRemark);
		}else {// 如果没有手动输入备注，则从模板中读取备注。
			logRemarkTemplate = Global.getLogTempValue(EnumLogTemplateType.CREDIT_LOG.getValue(), totalLogType);
			this.setRemark(this.getLogRemark());
		}
		this.setType_id(totalType.getId());
		userCreditDao.addUserCreditLog(this);
		Global.getTransfer().remove("editRemark");
	}
	
	public String getLogRemark(Map<String , Object> map){
		try {
			return FreemarkerUtil.renderTemplate(totalLogRemarkTemplate, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
}