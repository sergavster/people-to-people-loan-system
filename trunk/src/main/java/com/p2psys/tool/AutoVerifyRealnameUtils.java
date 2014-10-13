package com.p2psys.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 *自动审核实名认证 
 * autor : sj
 * 
 */
//v1.6.7.2 RDPROJECT-571 sj 2013-12-12 start
public class AutoVerifyRealnameUtils {
	
	private static Logger logger = Logger.getLogger(AutoVerifyRealnameUtils.class);
	
	/**
	 * 自动审核实名认证验证接口-GET访问方式
	 * 2013-12-11
	 * @param url
	 * @param sn_id
	 * @param dym_password
	 */
	public static String checkAutoVerifyRealname(String url) {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url); 
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			//执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("checkAutoVerifyRealname failed: " + getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			return new String(responseBody);
		} catch (HttpException e) {
			logger.error(e);
		} catch (IOException e) {
			//发生网络异常
			logger.error(e);
		} finally {
			//释放连接
			getMethod.releaseConnection();
		}
		return "";
	}
	
	public static void main(String[] args) {
		String url = "";
		try {
			url = "http://sfzopen.erongdu.com/sfzcha.php?username=test&password=test12345&sfznum=330183198401101734&truename="+URLEncoder.encode("汪锋", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //System.out.println(checkAutoVerifyRealname(url));
	}
	//v1.6.7.2 RDPROJECT-571 sj 2013-12-12 end
}
