package com.p2psys.web.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.SystemDao;
import com.p2psys.domain.User;
import com.p2psys.model.purview.PurviewSet;
import com.p2psys.tool.iphelper.IPUtils;
import com.p2psys.web.action.BaseAction;

public class ManageAuthInterceptor extends BaseInterceptor {  
  
	private static final long serialVersionUID = -2239644443711524657L;
	
	private static final Logger logger=Logger.getLogger(ManageAuthInterceptor.class);
	
	@Override  
    public String intercept(ActionInvocation ai) throws Exception {  
		ActionContext ctx = ActionContext.getContext();  
        Map session = ctx.getSession();  
        User user = (User) session.get(Constant.AUTH_USER);  
        String servletPath=getServletPath();
        
		String isAllowIp_enable=Global.getValue("isAllowIp_enable");
		if(isAllowIp_enable!=null&&isAllowIp_enable.equals("1")){
			ServletContext context = ServletActionContext.getServletContext();
			ApplicationContext appctx= WebApplicationContextUtils.getRequiredWebApplicationContext(context);
			SystemDao systemDao = (SystemDao)appctx.getBean("systemDao");
			List ipList=systemDao.getAllowIp();
			String realip=IPUtils.getRemortIP(ServletActionContext.getRequest());
			if(!ipList.contains(realip)){
				logger.debug("未授权IP："+realip+"没有权限访问后台！");
        		message("未授权IP："+realip+"没有权限访问后台！", "");
        		return BaseAction.ADMINMSG;
			}
		}
		// v16.7.2 RDPROJECT-614 lhm 2013-12-20 start
		// 不需要权限校验的页面中增加欢迎页面
        if(servletPath.startsWith("/admin/auth.html")||servletPath.startsWith("/admin/index.html")||servletPath.startsWith("/admin/logout.html")
        		|| servletPath.startsWith("/admin/modifyPwd.html") || servletPath.startsWith("/admin/system/welcome.html")){
        	 return ai.invoke(); 
        }
     // v16.7.2 RDPROJECT-614 lhm 2013-12-20 end
        if (user == null) {
        	message("请先登录！", "/admin/index.html");
    		return BaseAction.ADMINMSG;  
        } else {  
        	PurviewSet session_purviewset=(PurviewSet)session.get(Constant.AUTH_PURVIEW);
        	if(!session_purviewset.hasList()){
        		logger.debug(user.getUsername()+"没有权限访问后台！");
        		message(user.getUsername()+"没有权限访问后台！", "");
        		return BaseAction.ADMINMSG;
        	}else{
        		
        		if(session_purviewset.contains(servletPath)){
        			 return ai.invoke();  
        		}
        	}
        	message(user.getUsername()+"没有权限访问后台！", "");
    		return BaseAction.ADMINMSG;
        }  
    }  
	
	
	
	
	protected String getServletPath(){
		HttpServletRequest request =ServletActionContext.getRequest();
		String servletPath=request.getServletPath();
		String extension=ServletActionContext.getActionMapping().getExtension();
		servletPath=servletPath.replace("."+extension, ".html");
		return servletPath;
	}
  
}  