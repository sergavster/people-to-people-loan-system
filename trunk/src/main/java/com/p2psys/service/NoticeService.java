package com.p2psys.service;

import java.util.Map;

import com.p2psys.domain.Notice;

//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start

public interface NoticeService {

	/*
	 * 生成通知的对外接口，用于生成通知对象，事件生产者调用
	 */
	public void sendNotice(String noticeTypeNid, Map<String,Object> sendData);
	 
	/*
	 * 发送通知的对外接口，用于定时器调用，实际发送通知
	 */
	public void sendNotice(Notice notice);

	
}
//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
