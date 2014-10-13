package com.p2psys.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.tool.coder.MD5;
import com.p2psys.util.DateUtils;


/**
 * 银生支付接口类
 * 
 
 */
public class Unspay {
	private static Logger logger = Logger.getLogger(Unspay.class);

	private String version="3.0.0"; //接口版本号
	private String time="";//订单时间
	private String remark="";//备注，附加信息
	private String mac="";//MAC值
	private String bankCode="";//支付方式（通过代码区分）
	private String b2b="false";//是否B2B网上银行支付
	private String merchantId="";//商户编号
	private String merchantUrl="";//商户接收银生反馈数据的响应url
	private String responseMode="3";//商户要求银生反馈信息的响应方式
	private String orderId="";//订单号
	private String currencyType="CNY";//货币类型
	private String amount;//支付金额
	private String assuredPay="";//是否通过银生担保支付
	private String commodity="";//商品名称
	private String orderUrl="";//订单url
	private String merchantKey=""; 
	private HttpServletRequest request;
	private HttpServletResponse response;
	/** 请求的参数 */
	private SortedMap parameters=new TreeMap() ;
	public void init(Unspay unspay,PaymentInterface paymentInterface,String amount,String tradeNo){
		String weburl = Global.getValue("weburl");
		//String weburl="http://www.yushangct.com";
        String nowTime=DateUtils.dateStr3(DateUtils.getNowTimeStr());
        StringBuffer s = new StringBuffer();
        s.append("merchantId=").append(paymentInterface.getMerchant_id());
        s.append("&merchantUrl=").append(weburl+paymentInterface.getReturn_url());
        s.append("&responseMode=").append(responseMode);
              s.append("&orderId=").append(tradeNo);
        s.append("&currencyType=").append(currencyType);
        s.append("&amount=").append(amount);
        s.append("&assuredPay=").append(assuredPay);
        s.append("&time=").append(nowTime);
        s.append("&remark=").append(remark);
        s.append("&merchantKey=").append(paymentInterface.getKey());
        logger.info("macString==="+s);
        //md5加密
        String mac = new MD5().getMD5ofStr(s.toString());		
        logger.info("mac 加密======="+mac);
	     unspay.setMac(mac);
		 unspay.setOrderId(tradeNo);
	     unspay.setMerchantId(paymentInterface.getMerchant_id());		        //商户号
		unspay.setMerchantUrl(weburl+paymentInterface.getReturn_url());		    //交易完成后跳转的URL
		unspay.setOrderUrl(weburl+paymentInterface.getNotice_url());		    //接收后台通知的URL
		unspay.setCurrencyType(currencyType);	//货币种类
	    unspay.setAmount(amount);
		unspay.setResponseMode(responseMode); //创建订单的客户端IP（消费者电脑公网IP，用于防钓鱼支付）
		unspay.setVersion(version);				//接口版本
    	unspay.setAssuredPay(assuredPay);			//签名算法（暂时只支持SHA256）
    	unspay.setTime(nowTime);	        		//直连银行参数，例子是直接转跳到招商银行时的参数
    	unspay.setRemark(remark);		//订单备注的BASE64编码
    	unspay.setMerchantKey(paymentInterface.getKey());
    	unspay.setCommodity(Global.getValue("webname"));
    	unspay.setB2b(b2b);
    	String aa="https://www.unspay.com/unspay/page/linkbank/payRequest.do?version=3.0.0";
    	StringBuffer ss = new StringBuffer();
    	ss.append("&merchantId=").append(paymentInterface.getMerchant_id());
    	ss.append("&merchantUrl=").append(weburl+paymentInterface.getReturn_url());
    	ss.append("&responseMode=").append(responseMode);
    	ss.append("&orderId=").append(tradeNo);
    	ss.append("&currencyType=").append(currencyType);
    	ss.append("&amount=").append(amount);
    	ss.append("&assuredPay=").append(assuredPay);
    	ss.append("&time=").append(nowTime);
    	ss.append("&remark=").append(remark);
        ss.append("&mac=").append(mac);
        String submitUrl=aa+ss.toString();
        logger.info("submitUrl======="+submitUrl);
	}

	/**
	 * 获取请求的URL地址，此地址包含参数和签名串
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public String getMacStr() throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		String enc = getCharacterEncoding(this.request, this.response);
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
		}
		// 去掉最后一个&
		String reqPars = sb.substring(0, sb.lastIndexOf("&"));

		return reqPars;

	}
	/**
	 * 获取编码字符集
	 * 
	 * @param request
	 * @param response
	 * @return String
	 */
	public static String getCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response) {

		if (null == request || null == response) {
			return "gbk";
		}

		String enc = request.getCharacterEncoding();
		if (null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}

		if (null == enc || "".equals(enc)) {
			enc = "gbk";
		}

		return enc;
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
	 * 获取参数值
	 * @param parameter 参数名称
	 * @return String 
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getB2b() {
		return b2b;
	}

	public void setB2b(String b2b) {
		this.b2b = b2b;
	}

	public String getResponseMode() {
		return responseMode;
	}

	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantUrl() {
		return merchantUrl;
	}
	public void setMerchantUrl(String merchantUrl) {
		this.merchantUrl = merchantUrl;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getAssuredPay() {
		return assuredPay;
	}
	public void setAssuredPay(String assuredPay) {
		this.assuredPay = assuredPay;
	}
	public String getCommodity() {
		return commodity;
	}
	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}
	public String getOrderUrl() {
		return orderUrl;
	}
	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}
	

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		Unspay.logger = logger;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public SortedMap getParameters() {
		return parameters;
	}

	public void setParameters(SortedMap parameters) {
		this.parameters = parameters;
	}

	public void setResponseMode(String responseMode) {
		this.responseMode = responseMode;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	

}
