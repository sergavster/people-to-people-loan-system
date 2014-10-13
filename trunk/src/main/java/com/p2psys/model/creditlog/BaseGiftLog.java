package com.p2psys.model.creditlog;

import java.util.HashMap;
import java.util.Map;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.common.enums.EnumIntegralCategory;
import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.common.enums.EnumLogTemplateType;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Global;
import com.p2psys.domain.CreditType;
import com.p2psys.model.RuleModel;
import com.p2psys.util.BigDecimalUtil;
import com.p2psys.util.FreemarkerUtil;

/**
 * 赠送积分父类
 
 * @version 1.0
 * @since 2013-10-25
 */
public class BaseGiftLog extends BaseCreditLog {

	private static final long serialVersionUID = -141858044155521465L;

	// 投资积分类型
	private String totalLogType = EnumCreditTemplateNid.INTEGRAL_TOTAL.getValue();
	
	// 投资综合积分模板
	protected String totalLogRemarkTemplate;
	
	public BaseGiftLog() {
		super();
	}
	
	public BaseGiftLog(long op , long op_user , long credit_type , long user_id,long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
	}
	
	/**
	 * 赠送积分日志处理
	 */
	@Override
	public void addCreditLog() {
		
		//赠送积分日志处理
		logRemarkTemplate = Global.getLogTempValue(EnumLogTemplateType.CREDIT_LOG.getValue(), logType);
		this.setRemark(this.getLogRemark());
		userCreditDao.addUserCreditLog(this);
		
		// 如果是赠送积分，则需要添加过得投资积分的日志，并添加综合积分日志
		CreditType type = userCreditDao.getCreditTypeById(this.getType_id());
		if(type != null && type.getCredit_category() == EnumIntegralCategory.INTEGRAL_GIFT.getValue()){
			
			// 综合积分日志处理
			double rate = 1;
			RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INTEGRAL_PERCENT.getValue()));
			if(rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
				rate = rule.getValueDoubleByKey("gift_percent");
			}
			CreditType totalType = userCreditDao.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_TOTAL.getValue());
			// 数据转换，避免计算错误
			double value =Double.parseDouble( this.getValue()+"");
			double total_value_ =BigDecimalUtil.mul(value, rate);
			int total_value = (int) BigDecimalUtil.round(total_value_, 0);
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("total_value", total_value);
			map.put("valid_value", total_value);
			this.setValue(total_value);
			totalLogRemarkTemplate = Global.getLogTempValue(EnumLogTemplateType.CREDIT_LOG.getValue(), totalLogType);
			this.setRemark(getLogRemark(map));
			this.setType_id(totalType.getId());
			userCreditDao.addUserCreditLog(this);
		}
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