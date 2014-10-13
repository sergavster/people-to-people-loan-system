package com.p2psys.web.action.member;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

public class PaymentAction implements ServletRequestAware{
	
	private final static Logger logger=Logger.getLogger(PaymentAction.class);
	
	private HttpServletRequest request;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}

	public String gopay(){
		String param=request.getQueryString();
		logger.info(param);
		return "success";
	}
}
