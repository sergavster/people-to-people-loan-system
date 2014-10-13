package com.p2psys.common.enums;

/**
 * 抽奖结果种类
 */
public enum EnumAwardErrorType {
	/**
	 * 没有登录
	 */
	RESULT_NO_REGISTER,
	/**
	 * 参数错误
	 */
	RESULT_PARAMETER_ERROR,
	/**
	 * 规则ID不存在
	 */
	RESULT_INVALID_RULE_ID,
	/**
	 * 当前时间在抽奖开始时间之前
	 */
	RESULT_BEFORE_START_TIME,
	/**
	 * 当前时间在抽奖结束时间之后
	 */
	RESULT_AFTER_END_TIME,
	/**
	 * 用户可用积分不足
	 */
	RESULT_POINT_LIMIT,
	/**
	 * 用户可用次数不足
	 */
	RESULT_TIME_LIMIT,
	/**
	 * 没有中奖
	 */
	RESULT_NO_AWARD,
	/**
	 * 没有奖品
	 */
	RESULT_NO_AWARD_OBJ,
	/**
	 * 抽奖可用金额不足
	 */
	RESULT_MONEY_LIMIT;
}
