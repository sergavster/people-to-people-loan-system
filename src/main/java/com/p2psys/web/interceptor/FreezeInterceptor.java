package com.p2psys.web.interceptor;
// v1.6.7.1 RDPROJECT-284 xx 2013-11-04 start
// 新增类
// v1.6.7.1 RDPROJECT-284 xx 2013-11-04 end
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.User;
import com.p2psys.freeze.model.FreezeModel;
import com.p2psys.freeze.service.FreezeService;
import com.p2psys.model.RuleModel;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 冻结拦截器
 
 * @version 1.0
 * @since 2013-11-4
 */
public class FreezeInterceptor extends BaseInterceptor {
	private static final long serialVersionUID = 1008901298342362080L;
	private static final Logger logger = Logger.getLogger(FreezeInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.FREEZE.getValue()));//冻结
			if(rule!=null && rule.getStatus()==1){//启用
				ServletContext context = ServletActionContext.getServletContext();
				ApplicationContext actx= WebApplicationContextUtils.getRequiredWebApplicationContext(context);
				String nameSpace = getNamespce();
				String servletPath = getServletPath();
				StringBuffer url = new StringBuffer();//请求的url
				url.append(nameSpace);
				url.append(servletPath);
				
				FreezeService freezeService = (FreezeService)actx.getBean("freezeService");
				FreezeModel fm = null;//冻结信息
				
				ActionContext ctx = ActionContext.getContext();  
				Map session = ctx.getSession();
				User user = (User)session.get(Constant.SESSION_USER);
				if(user==null && "/user/login.html".equals(url.toString())){
					String username = getParam("username")+"";
					fm = freezeService.getByUserName(username);
				}else if(user!=null){
					fm = freezeService.getByUserId(user.getUser_id());
				}
				if(fm!=null){
					if(fm!=null && fm.getStatus()==1 && !StringUtils.isBlank(fm.getMark())){//启用
						String[] marks = fm.getMark().split(",");
						for (String mark : marks) {
							String markUrl = rule.getValueStrByKey(mark);
							if(markUrl.equalsIgnoreCase(url.toString())){
								message("该功能处于冻结状态,无法操作！", "/index.html");
								return BaseAction.MSG;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("冻结拦截器异常："+e.getMessage());
		}
		
		String result = invocation.invoke();
		return result;
		
	}
}