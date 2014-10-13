package com.p2psys.common.enums;

/**
 * 规则（百分比奖励返给邀请人，现金返给邀请人，现金返给被邀请人）
 */
public enum EnumRuleNid {
	
	PERCENT_AWARD("percent_award"), // 百分比奖励返给邀请人
	
	INVITE_AWARD("invite_award"), // 现金返给邀请人
	
	INVITER_AWARD("inviter_award"), // 现金返给被邀请人
	
	INDEX("index"), // 首页规则
	
	TENDER_AWARD("tender_award"),//投标奖励规则 
	
	INTEGRAL_TENDER("integral_tender"), //投标成功返现积分规则
	
	COMMENT("comment"),// 投标评论规则
	
	INVEST_VIEW_DETAIL("invest_view_detail"),//投资查看详情规则
	
	IMAGE_WATERMARK("image_watermark"), //默认水印处理规则
	
	EXTRACT_CASH_CHARGE("extract_cash_charge"), //提现手续费规则
	
	NEWRECHARGE_ATTESTATION("newrecharge_attestation"),//充值认证
	
	RECHARGE_VIP_GIVETIME("recharge_vip_givetime"),//vip赠送时间 
	
	VALIDE_ENABLE("valide_enable"),//验证码
	
	BORROW_APR_LIMIT("borrow_apr_limit"), 
	
	PROTOCOL_TEMPLATE_TYPE("protocol_template_type"),//协议模板类型
	
	REGISTER_SUCCESS_LOGIN("register_success_login"),//注册成功自动登录
	
	FIRST_TENDER_REWARD("first_tender_reward"),//第一次投标奖励
	
	TIME_HOUR_ENABLE("time_hour_enable"),//时间搜索启用时分秒
	
	JIN_LIMIT("jin_limit"),//净值标限制
	
	PHONE_REALNAME("phone_realname"),
	//v1.6.6.1 RDPROJECT-178 liukun 2013-09-17 begin
	CASH_VERIFY_CONF("cash_verify_conf"),
	//v1.6.6.1 RDPROJECT-178 liukun 2013-09-17 end
	//v1.6.6.1 RDPROJECT-183 liukun 2013-09-17 begin
	HUIKUAN_CONF("huikuan_conf"),
	//v1.6.6.1 RDPROJECT-183 liukun 2013-09-17 end
	// v1.6.5.5 RDPROJECT-135 wcw 2013-09-25 START
	KUAIXIAN_SMS("kuaixian_sms"),   //快线400短信
	// v1.6.5.5 RDPROJECT-135 wcw 2013-09-25 end
	// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 start
	INVITE_FIRST_TENDER_AWARD("invite_first_tender_award"), // 民生创投邀请好友首次投标奖励
	// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 end
	//V1.6.6.1 RDPROJECT-171 wcw 2013-10-08 start
    KEFU_USER_INVEST("kefu_user_invest"),
	//V1.6.6.1 RDPROJECT-171 wcw 2013-10-08 end
	// v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start 
	INVEST_RANKLIST("invest_ranklist"),//投资排行榜
	// v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
	INTEGRAL_CONVERT("integral_convert"),//积分兑换
	EXTENSION("extension"), //展期
	// v1.6.6.1 RDPROJECT-235 zza 2013-10-17 start
	RULE_PROMOTE("rule_promote"),// 及时雨推广奖励规则
	// v1.6.6.1 RDPROJECT-235 zza 2013-10-17 end
	INTEGRAL_PERCENT("integral_percent"),//综合积分计算比重规则
	// v1.6.6.1 RDPROJECT-365 yl 2013-10-22 start
	LOGIN_VALIDE_ENABLE("login_valide_enable"),//登录验证码
	// v1.6.6.1 RDPROJECT-365 yl 2013-10-22 end
	CREDIT_LEVE_QUARTZ("credit_leve_quartz"),//及时雨定时扫描更新会员积分等级
	
	CREDIT_LEVE_FREE("credit_leve_free"),//会员等级手续费优惠
	// v1.6.7.1 RDPROJECT-425 wcw 2013-11-08 start
	TENDER_BEFORE_VALID("tender_before_valid"),//投标前验证
	//v1.6.7.1 RDPROJECT-425 wcw 2013-11-08 end
	//V1.6.7.1 RDPROJECT-345 liukun 2013-11-06 start
	AUTO_TENDER_CONF("auto_tender_conf"),//
	//V1.6.7.1 RDPROJECT-345 liukun 2013-11-06 end
	
	//V1.6.7.1 RDPROJECT-418 liukun 2013-11-11 start
	AC_RIGHT_SIDE_CONF("ac_right_side_conf"),//会员等级手续费优惠
	//V1.6.7.1 RDPROJECT-418 liukun 2013-11-11 start
	
	// v1.6.7.1 RDPROJECT-284 xx 2013-11-04 start
	FREEZE("freeze"),//冻结
	// v1.6.7.1 RDPROJECT-284 xx 2013-11-04 end
	// v1.6.7.1 RDPROJECT-355 zza 2013-11-08 start
	/** 德赛投资排行榜（天标超过30天也算在投资排行榜里） */
	MONTH_RANK("month_rank"),
	// v1.6.7.1 RDPROJECT-355 zza 2013-11-08 end
	// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 start
	RSA_FORM_ENCRYPT("rsa_form_encrypt"),//RSA表单提交加密
	// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 start
	// v1.6.7.1 zhangyz 2013-11-16 start
	INTEGRAL_VIP("integral_vip"),//积分兑换vip规则
	// v1.6.7.1 zhangyz 2013-11-16 end
	
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
	CHINA_SMS("china_sms"),
	ERONGDU_SMS("erongdu_sms"),
	YND_SMS("ynd_sms"),
	YUN_SMS("yun_sms"),
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
	
	// v1.6.7.1 安全优化 sj 2013-11-20 start
	SAFETY("safety"),//安全优化规则
	// v1.6.7.1  安全优化 sj 2013-11-20 end
	
	// v1.6.7.2 RDPROJECT-86 liukun 2013-12-17 start
	CA_ADD_CHECK("ca_add_check"),//发布债权规则
	// v1.6.7.2 RDPROJECT-86 liukun 2013-12-17 end
	
	// v1.6.7.2 RDPROJECT-571 sj 2013-12-12 start
	REALCARDCHECK("real_card_check"),
	// v1.6.7.2 RDPROJECT-571 sj 2013-12-12 end
	
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
	TENDERLIMIT("tender_limit"),
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
	
	//v1.6.7.2 RDPROJECT-624 sj 2013-12-27 start
	BORROWMANAGEFEE("borrow_manage_fee");
	//v1.6.7.2 RDPROJECT-624 sj 2013-12-27 end
	
	EnumRuleNid(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return this.value;
	}	
}
