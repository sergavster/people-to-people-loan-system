package com.p2psys.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.p2psys.context.Global;
import com.p2psys.util.StringUtils;

/**
 * 用于session过滤，补充Struts2的sssion拦截器
 * 
 
 * 
 */
public class AdminFilter implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpSession session = httpServletRequest.getSession();
		String servletPath=getServletPath(httpServletRequest);
		String adminUrl=Global.getString("admin_url");		
        if(!StringUtils.isBlank(adminUrl)&&servletPath.startsWith(adminUrl)){
        	session.setAttribute("adminUrlCheck", "true");
        	request.setAttribute("source", "fliter");
        	httpServletRequest.getRequestDispatcher("/admin/auth.html").forward(request, response);
        }else{
        	boolean isAdminCheck=StringUtils.isBlank(StringUtils.isNull(session.getAttribute("adminUrlCheck")));
        	if(isAdminCheck){
        		httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/notfound.html");
        	}else{
        		chain.doFilter(request, response);
        	}
        }
	}
        protected String getServletPath(HttpServletRequest request){
        	String servletPath=request.getRequestURI();
    		return servletPath;
    	}
}