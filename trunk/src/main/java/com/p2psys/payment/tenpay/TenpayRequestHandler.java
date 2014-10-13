package com.p2psys.payment.tenpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.p2psys.util.tenpayUtil.MD5Util;
import com.p2psys.util.tenpayUtil.TenpayUtil;

/**
 * 请求处理类
 * 请求处理类继承此类，重写createSign方法即可。
 
 *
 */
/** 网关url地址 */
/** 密钥 */
/** 请求的参数 */
/** debug信息 */
/**
 * 构造函数
 * @param request
 * @param response
 */
/**
 *初始化函数。
 */
//nothing to do
/**
 *获取入口地址,不包含参数值
 */
/**
 *设置入口地址,不包含参数值
 */
/**
 *获取密钥
 */
/**
 *设置密钥
 */
/**
 * 获取参数值
 * @param parameter 参数名称
 * @return String 
 */
/**
 * 设置参数值
 * @param parameter 参数名称
 * @param parameterValue 参数值
 */
/**
 * 返回所有的参数
 * @return SortedMap
 */
/**
 *获取debug信息
 */
/**
 * 获取带参数的请求URL
 * @return String
 * @throws UnsupportedEncodingException 
 */
//去掉最后一个&
/**
 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
 */
//debug信息
/**
 *设置debug信息
 */
/**
 * 请求处理类
 * 请求处理类继承此类，重写createSign方法即可。
 
 *
 */
/** 网关url地址 */
/** 密钥 */
/** 请求的参数 */
/** debug信息 */
/**
 * 构造函数
 * @param request
 * @param response
 */
/**
 *初始化函数。
 */
//nothing to do
/**
 *获取入口地址,不包含参数值
 */
/**
 *设置入口地址,不包含参数值
 */
/**
 *获取密钥
 */
/**
 *设置密钥
 */
/**
 * 获取参数值
 * @param parameter 参数名称
 * @return String 
 */
/**
 * 设置参数值
 * @param parameter 参数名称
 * @param parameterValue 参数值
 */
/**
 * 返回所有的参数
 * @return SortedMap
 */
/**
 *获取debug信息
 */
/**
 * 获取带参数的请求URL
 * @return String
 * @throws UnsupportedEncodingException 
 */
//去掉最后一个&
/**
 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
 */
//debug信息
/**
 *设置debug信息
 */
/**
 * 请求处理类 请求处理类继承此类，重写createSign方法即可。
 * 
 
 * 
 */
public class TenpayRequestHandler {

	/** 网关url地址 */
	private String gateUrl;

	/** 密钥 */
	private String key;

	/** 请求的参数 */
	private SortedMap parameters;

	/** debug信息 */
	private String debugInfo;

	private HttpServletRequest request;

	private HttpServletResponse response;

	/**
	 * 构造函数
	 * 
	 * @param request
	 * @param response
	 */
	public TenpayRequestHandler(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;

		this.gateUrl = "https://gw.tenpay.com/gateway/pay.htm";
		this.key = "";
		this.parameters = new TreeMap();
		this.debugInfo = "";
	}

	/**
	 * 初始化函数。
	 */
	public void init() {
		// nothing to do
	}

	/**
	 * 获取入口地址,不包含参数值
	 */
	public String getGateUrl() {
		return gateUrl;
	}

	/**
	 * 设置入口地址,不包含参数值
	 */
	public void setGateUrl(String gateUrl) {
		this.gateUrl = gateUrl;
	}

	/**
	 * 获取密钥
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 设置密钥
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取参数值
	 * 
	 * @param parameter
	 *            参数名称
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = (String) this.parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	/**
	 * 设置参数值
	 * 
	 * @param parameter
	 *            参数名称
	 * @param parameterValue
	 *            参数值
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if (null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}

	/**
	 * 返回所有的参数
	 * 
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}

	/**
	 * 获取debug信息
	 */
	public String getDebugInfo() {
		return debugInfo;
	}

	/**
	 * 获取带参数的请求URL
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public String getRequestURL() throws UnsupportedEncodingException {

		this.createSign();

		StringBuffer sb = new StringBuffer();
		String enc = TenpayUtil.getCharacterEncoding(this.request,
				this.response);
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();

			if (!"spbill_create_ip".equals(k)) {
				sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
			} else {
				sb.append(k + "=" + v.replace(".", "%2E") + "&");
			}
		}

		// 去掉最后一个&
		String reqPars = sb.substring(0, sb.lastIndexOf("&"));

		return this.getGateUrl() + "?" + reqPars;

	}

	public void doSend() throws UnsupportedEncodingException, IOException {
		this.response.sendRedirect(this.getRequestURL());
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	protected void createSign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + this.getKey());

		String enc = TenpayUtil.getCharacterEncoding(this.request,
				this.response);
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();

		this.setParameter("sign", sign);

		// debug信息
		this.setDebugInfo(sb.toString() + " => sign:" + sign);

	}

	/**
	 * 设置debug信息
	 */
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

	protected HttpServletRequest getHttpServletRequest() {
		return this.request;
	}

	protected HttpServletResponse getHttpServletResponse() {
		return this.response;
	}

}

