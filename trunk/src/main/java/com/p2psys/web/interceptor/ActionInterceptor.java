package com.p2psys.web.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.p2psys.context.Global;
import com.p2psys.exception.BussinessException;
import com.p2psys.exception.ManageBussinessException;
import com.p2psys.tool.iphelper.IPUtils;
import com.p2psys.util.StringUtils;

import freemarker.core.ParseException;

public class ActionInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1008901298342362080L;
	private static final Logger log = Logger.getLogger(ActionInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request =ServletActionContext.getRequest();
		String ip=IPUtils.getRemortIP(request);
		Global.ipThreadLocal.set(ip);
		log.debug("Set Ip:"+ip);
		String actionName = invocation.getInvocationContext().getName();
		try {
			String result = invocation.invoke();
			return result;
		} catch (BussinessException e) {
			log.error(e);
			String urlback= "<a href='javascript:history.go(-1)'>返回上一页</a>" ;
			//v1.6.7.2 RDPROJECT-616 sj 2013-12-20 start
			if(!StringUtils.isBlank(e.getUrl())){
				urlback = "<a href='"+e.getUrl()+"'>返回上一页</a>" ;
			}
			//v1.6.7.2 RDPROJECT-616 sj 2013-12-20 end
			request.setAttribute("backurl",urlback);
			request.setAttribute("rsmsg",e.getMessage());
			return "msg";  // 这里要分  前台  和  后台
		} catch (ManageBussinessException e) {
			log.error(actionName, e);
			String urlback = "<a href='javascript:history.go(-1)'>返回上一页</a>" ;
			request.setAttribute("backurl",urlback);
			request.setAttribute("rsmsg",e.getMessage());
			return "adminmsg";  //  后台
		}catch (ParseException e) {
			log.error(actionName, e);
			String urlback = "<a href='javascript:history.go(-1)'>返回上一页</a>" ;
			request.setAttribute("backurl",urlback);
			request.setAttribute("rsmsg","页面显示异常，联系管理员！");
			return "msg";  // 这里要分  前台  和  后台
		} catch (Exception e) {
			log.error(actionName, e);
			e.printStackTrace();
			String urlback = "<a href='javascript:history.go(-1)'>返回上一页</a>" ;
			request.setAttribute("backurl",urlback);
			request.setAttribute("rsmsg","系统异常联系管理员！");
			return "msg";  // 这里要分  前台  和  后台
		}
	}
}