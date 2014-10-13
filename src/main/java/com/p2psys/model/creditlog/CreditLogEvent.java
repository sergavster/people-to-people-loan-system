package com.p2psys.model.creditlog;

import java.util.Map;

/**
 * 
 * 实现系统积分日志监听操作
 * 
 
 * @version 1.0
 * @since 2013-8-26
 */
public interface CreditLogEvent {

	/**
	 * 日志事件执行
	 */
	void doEvent();
	
	/**
	 * 封装资金记录的备注
	 * @return
	 */
	String getLogRemark();
	
	/**
	 * 传输参数
	 * @return
	 */
	Map<String,Object> transfer();
	
	void addOperateLog();
	
	void addCreditLog();
	
}
