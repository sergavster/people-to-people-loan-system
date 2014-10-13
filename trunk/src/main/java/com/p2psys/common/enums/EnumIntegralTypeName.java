package com.p2psys.common.enums;

/**
 * 会员积分类型
 
 * @version 1.0
 * @since 2013-7-23
 */
public enum EnumIntegralTypeName {

	/**
	 * 邮箱认证NID名
	 */
	INTEGRAL_EMAIL("email"),
	/**
	 * 电话认证
	 */
	INTEGRAL_PHONE("phone"),
	/**
	 * 视频认证
	 */
	INTEGRAL_VIDEO("video"),
	/**
	 * 实名认证
	 */
	INTEGRAL_REAL_NAME("realname"),
	/**
	 * 现场认证
	 */
	INTEGRAL_SCENE("scene"),
	/**
	 * 证件资料
	 */
	INTEGRAL_CERTIFICATE("zhengjian"),
	/**
	 * 投标成功
	 */
	INTEGRAL_INVEST("invest_success"),
	/**
	 * 借款成功
	 */
	INTEGRAL_BORROW("borrow_success"),
	
	/**
	 * 积分兑换
	 */
	INTEGRAL_CONVERT("integral_convert"),
	
	/**
	 * 积分抽奖
	 */
	INTEGRAL_AWARD("integral_award"),
	
	/**
	 * 论坛积分
	 */
	BBS_INTEGRAL("bbs_integral"),
	/**
	 * 综合积分
	 */
	INTEGRAL_TOTAL("integral_total"),
	
	/**
	 * 手动修改有效积分
	 */
	VALID_CHANGE("valid_change"),
	
	/**
	 *  手动修改消费积分
	 */
	EXPENSE_CHANGE("expense_change"),
	
	/**
	 * 中奖奖励积分
	 */
	AWARD_INTEGRAL("award_integral"),
	
	/**
	 * 积分兑换VIP
	 */
	INTEGRAL_VIP("integral_vip"),; 
	
	private String value;
	
	EnumIntegralTypeName(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
