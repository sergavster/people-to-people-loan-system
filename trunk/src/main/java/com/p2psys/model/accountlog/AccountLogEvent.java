package com.p2psys.model.accountlog;

import java.util.Map;

/**
 * 
 * 实现系统资金日志监听操作
 * 
 
 * @version 1.0
 * @since 2013-8-26
 */
public interface AccountLogEvent {

	/**
	 * 日志事件执行
	 */
	void doEvent();
	
	/**
	 * 监听日志操作，并修改AccountSum数据
	 */
	void accountSumProperty();
	
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
	
	void sendNotice();
	
	void addOperateLog();
	
	void addAccountSumLog();
	
	void updateAccount();
	
	void extend();
}
