package com.p2psys.util.sms;

import java.util.Map;

public interface SmsPortal {
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
	public String getSPName();
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
	public String send(String phone, String content);
	public Map<String, Integer> getUseInfo();
}
