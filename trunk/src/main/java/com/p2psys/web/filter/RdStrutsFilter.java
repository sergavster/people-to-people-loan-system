package com.p2psys.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
/**
 * 
 * struts2自带的文件上传过滤器会过滤掉百度编辑器，如果图片上传，不经过Struts过滤器
 *
 * 未经授权不得进行修改、复制、出售及商业使用。
 *  (c)</b> 杭州科技有限公司-<br/>
 *
 * @ClassName: RdStrutsFilter
 * @Description: 
 
 * @date 2013-7-17 上午9:15:49
 */
public class RdStrutsFilter extends StrutsPrepareAndExecuteFilter {
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String url = request.getRequestURI();
		if ("/imageUp.jsp".equals(url)) {
			chain.doFilter(req, res);
		} else {
			super.doFilter(req, res, chain);
		}
	}
}