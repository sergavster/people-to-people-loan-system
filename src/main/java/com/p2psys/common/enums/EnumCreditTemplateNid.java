package com.p2psys.common.enums;

/**
 * 
 * 积分日志模板类型
 * @version 1.0
 * @since 2013-10-17
 */
public enum EnumCreditTemplateNid {

	CONVERT_APPLY("convert_apply"),//积分兑换申请
	
	CONVERT_FAIL("convert_fail"),//积分兑换失败
	
	CONVERT_SUCCESS("convert_success"),//积分兑换成功
	
	EMAIL("email"),//积分日志：邮箱认证通过，综合积分有效积分日志。
	
	REALNAME("realname"),//积分日志：实名认证通过，综合积分有效积分日志。
	
	PHONE("phone"),//积分日志：手机认证通过，综合积分有效积分日志。
	
	VIDEO("video"),//积分日志：视频认证通过，综合积分有效积分日志。
	
	SCENE("scene"),//积分日志：现场认证通过，综合积分有效积分日志。
	
	ZHENGJIAN("zhengjian"),//积分日志：邮箱认证通过，综合积分有效积分日志。
	
	INVEST_SUCCESS("invest_success"),//积分日志：投标成功，综合积分有效积分日志。

	TENDER_EMAIL("tender_email"),//积分日志：邮箱认证通过，投资积分日志。
	
	TENDER_REALNAME("tender_realname"),//积分日志：实名认证通过，投资积分日志。
	
	TENDER_PHONE("tender_phone"),//积分日志：手机认证通过，投资积分日志。
	
	TENDER_VIDEO("tender_video"),//积分日志：视频认证通过，投资积分日志。
	
	TENDER_SCENE("tender_scene"),//积分日志：现场认证通过，投资积分日志。
	
	TENDER_ZHENGJIAN("tender_zhengjian"),//积分日志：邮箱认证通过，投资积分日志。
	
	TENDER_INVEST_SUCCESS("tender_invest_success"),//积分日志：投标成功，投资积分日志。
	
	INTEGRAL_TOTAL("integral_total"),//获得综合积分和有效积分
	
	INTEGRAL_TOTAL_EDIT("integral_total_edit"),//积分日志：后台手动修改综合积分日志"
	
	INTEGRAL_EXPENSE_EDIT("integral_expense_edit"),//积分日志：后台手动修改消费积分日志"
	
	INTEGRAL_VALID_EDIT("integral_valid_edit"),//积分日志：后台手动修改有效积分日志"
	
	EXPENSE_AWARD("expense_award"),//积分日志：抽奖消费积分日志"
	
	AWARD_INTEGRAL("award_integral"),//积分日志：参加活动，奖励积分
	
	BBS_INTEGRAL("bbs_integral"),// 积分日志：论坛积分日志
	
	CONVERT_VIP_APPLY("convert_vip_apply"),//积分兑换VIP申请
	
	CONVERT_VIP_FAIL("convert_vip_fail"),//积分兑换VIP失败
	
	CONVERT_VIP_SUCCESS("convert_vip_success");//积分兑换VIP成功

	EnumCreditTemplateNid(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return this.value;
	}
}
