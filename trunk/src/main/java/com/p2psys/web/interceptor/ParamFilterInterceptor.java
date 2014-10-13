package com.p2psys.web.interceptor;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.blogspot.radialmind.html.HTMLParser;
import com.blogspot.radialmind.html.HandlingException;
import com.blogspot.radialmind.xss.XSSFilter;
import com.opensymphony.xwork2.ActionInvocation;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class ParamFilterInterceptor extends BaseInterceptor {

	private static final long serialVersionUID = -6325242223825713099L;
	private static final Logger logger = Logger.getLogger(ParamFilterInterceptor.class);
	
	private static final XSSFilter xssFilter=new XSSFilter();
	
	private static String[] denied_texts={"'",";","insert","declare","drop","create",
		"alter","rename","join","where","like","cast","script","iframe","%27",
		"%3b","%22","</","<",">","grant","%3c","%3e","unicode","alert","confirm","\""};
	private static String[] denied_htmls={"select","insert","declare","drop","delete","create",
		"modify","alter","rename","join","where","like","cast","script","iframe","update","grant","%3c","%3e","unicode"};
	public void init() {
		super.init();
	}

	@Override
	public String intercept(ActionInvocation ai) throws Exception {
		if(denied_texts.length>0){
			HttpServletRequest request =ServletActionContext.getRequest();
			Enumeration names=request.getParameterNames();
			boolean hasDeniedText=false;
			while(names.hasMoreElements()){
				String name=(String)names.nextElement();
				String[] values=request.getParameterValues(name);
				for(int i=0;i<values.length;i++){
					//富文本特殊处理
					if(StringUtils.isNull(name).equals("content")){
						hasDeniedText=isDeniedHtml(values[i]);
						if(!hasDeniedText) request.setAttribute("content", fliterXSS(StringUtils.isNull(values[i])));
					}else{
						hasDeniedText=isDeniedText(values[i]);
					}
					if(hasDeniedText) break;
				}
				if(hasDeniedText) break;
			}
			if(hasDeniedText){
				message("存在非法字符！","");
				return BaseAction.MSG;
			}
		}
		
		String result = ai.invoke();
		return result;
	}
	/**
	 * 检查是否已经存在xss的字符
	 * @param text
	 * @return 不存在返回true
	 */
	public String fliterXSS(String text){
		StringBuffer sb=new StringBuffer("<html>").append(text).append("</html>");
		StringReader reader = new StringReader(sb.toString());
	    StringWriter writer = new StringWriter();
	    try {
	        HTMLParser.process( reader, writer,xssFilter , true );
	    } catch (HandlingException e) {
	    }
	    return writer.toString();
	}
	/**
	 * 是否存在需要过滤的字符
	 * @param text
	 * @return 存在返回true
	 */
	public boolean isDeniedText(String text){
		for(int j=0;j<denied_texts.length;j++){
			String denied_Text=StringUtils.isNull(denied_texts[j]);
			if(StringUtils.isNull(text).indexOf(denied_Text)>-1){
				logger.info("存在非法字符:"+text);
				return true;
			}
		}
		return false;
	}
	/**
	 * 是否存在需要过滤的字符
	 * @param text
	 * @return 存在返回true
	 */
	public boolean isDeniedHtml(String text){
		for(int j=0;j<denied_htmls.length;j++){
			String denied_Text=StringUtils.isNull(denied_htmls[j]);
			if(StringUtils.isNull(text).indexOf(denied_Text)>-1){
				logger.info("存在非法字符:"+text);
				return true;
			}
		}
		return false;
	}
}
