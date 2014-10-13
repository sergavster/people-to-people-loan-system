package com.p2psys.common.constant;
//v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
//新增
//v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
/**
 * 状态记录
 
 * @version 1.0
 * @since 2013年12月11日 下午8:27:16
 */
public class ConsStatusRecord {

	/** 操作类型-还款 */
	public static byte SR_TYPE_REPAY = 1;
	/** 操作类型-流转标还款 */
	public static byte SR_TYPE_FLOWREPAY = 2;
	
	/** 处理结果-成功 */
	public static byte SR_RESULT_FAILL = 0;
	/** 处理结果-失败 */
	public static byte SR_RESULT_SUCCESS = 1;
	/** 处理结果-失败，可用余额不足 */
	public static byte SR_RESULT_NOENOUCHMONEY = 99;
	
	/** 记录状态-失效的 */
	public static byte SR_STATUS_INVALID = 0;
	/** 记录状态-起效的 */
	public static byte SR_STATUS_VALID = 1;
	
	
	
}
