package com.p2psys.web.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.p2psys.service.UserLogService;
import com.p2psys.util.StringUtils;

public class UserLogInterceptor extends BaseInterceptor {

	private static final long serialVersionUID = -6325242223825713099L;
	private static final Logger logger = Logger.getLogger(UserLogInterceptor.class);
	//private UserLogService userLogService;
	
	public void init() {
		super.init();
	}

	@Override
	public String intercept(ActionInvocation ai) throws Exception {
		ServletContext context = ServletActionContext.getServletContext();
		ApplicationContext ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		UserLogService userLogService = (UserLogService)ctx.getBean("userLogService");
		
		String namespace = this.getNamespce();
		String url = this.getServletPath();
		String retMsg = getRetMsg();
		String result = ai.invoke();
		
		return result;
	}
	
	private String getRetMsg(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String retMsg = StringUtils.isNull(request.getAttribute("errorMsg"));
		if(retMsg.trim().isEmpty()){
			retMsg = StringUtils.isNull(request.getAttribute("msg"));
		}
		if(retMsg.trim().isEmpty()){
			return "1";
		}
		return retMsg;
	}
	
	
	
	

}
