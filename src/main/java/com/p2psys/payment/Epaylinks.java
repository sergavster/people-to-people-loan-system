package com.p2psys.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.tool.coder.BASE64Encoder;

/**
 * 易票联支付接口类
 * 
 
 */
public class Epaylinks {
	// 商户号
	// String partner = request.getParameter("partner");
	String partner = "130"; // 130测试商户号只能在219.136.207.190 测试服务器上使用
	// 商户订单号，此处用系统当前毫秒数，商户根据自己情况调整，确保该订单号在商户系统中的全局唯一
	String out_trade_no = ""; // 这是采用系统毫秒数作为订单号样例
	// 支付金额
	String total_fee = "";
	// 币种(目前只支持人民币)
	String currency_type = "RMB";
	// 创建订单的客户端IP（消费者电脑公网IP）
	// String order_create_ip = request.getRemoteAddr();
	// //创建订单的客户端IP（消费者电脑公网IP，用于防钓鱼支付）
	String order_create_ip = "";
	// 商户密钥
	String key = ""; // 这是130测试商户密钥
	// 接口版本
	String version = "3.0";
	// 签名类型
	String sign_type = "";
	// 交易完成后跳转的URL，用来接收易票联网关的页面转跳即时通知结果
	String return_url = "";
	// 接收易票联支付网关异步通知的URL
	String notify_url = "";
	// 直连银行参数（不停留易票联支付网关页面，直接转跳到银行支付页面）
	// String pay_id = "zhaohang"; //直连招商银行参数值
	String pay_id = "";
	// 订单备注，该信息使用64位编码提交服务器，并将在支付完成后随支付结果原样返回
	String memo = "测试备注信息";
	String gatewayUrl = "http://219.136.207.190:80/paycenter/v2.0/getoi.do";
	/** 调试信息 */
	private String debugMsg;
	private String urlEncoding;
	/** 请求的参数 */
	private SortedMap parameters=new TreeMap() ;

	private HttpServletRequest request;
	private HttpServletResponse response;
    public void init(Epaylinks epaylinks,PaymentInterface paymentInterface){
    	BASE64Encoder encoder = new BASE64Encoder();
    	String base64_memo="";
		String weburl = Global.getValue("weburl");
		try {
			base64_memo = encoder.encode(memo.getBytes("ISO-8859-1")).toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-gen erated catch block
			e.printStackTrace();
		}
    	epaylinks.setKey(paymentInterface.getKey());
    	epaylinks.setGatewayUrl(paymentInterface.getGatewayUrl());
    	//设置支付请求参数
    	epaylinks.setParameter("partner", paymentInterface.getMerchant_id());		        //商户号
    	epaylinks.setParameter("return_url", weburl+paymentInterface.getReturn_url());		    //交易完成后跳转的URL
    	epaylinks.setParameter("notify_url", weburl+paymentInterface.getNotice_url());		    //接收后台通知的URL
    	epaylinks.setParameter("currency_type", currency_type);	//货币种类
    	epaylinks.setParameter("order_create_ip",order_create_ip); //创建订单的客户端IP（消费者电脑公网IP，用于防钓鱼支付）
    	epaylinks.setParameter("version", version);				//接口版本
    	epaylinks.setParameter("sign_type", paymentInterface.getSignType());			//签名算法（暂时只支持SHA256）

    	//可选参数
    	epaylinks.setParameter("pay_id", pay_id);	        		//直连银行参数，例子是直接转跳到招商银行时的参数
    	epaylinks.setParameter("base64_memo", base64_memo);		//订单备注的BASE64编码
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

	public SortedMap getParameters() {
		return parameters;
	}

	public void setParameters(SortedMap parameters) {
		this.parameters = parameters;
	}

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public String getDebugMsg() {
		return debugMsg;
	}

	public void setDebugMsg(String debugMsg) {
		this.debugMsg = debugMsg;
	}

	/**
	 * 获取请求的URL地址，此地址包含参数和签名串
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public String getRequestURL() throws UnsupportedEncodingException {

		this.buildRequestSign();

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

		return this.getGatewayUrl() + "?" + reqPars;

	}

	/**
	 * 使用SHA256算法生成签名结果,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public void buildRequestSign() {
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

		String enc = getCharacterEncoding(this.request, this.response);
		String sign = SHA256Encode(sb.toString(), enc).toLowerCase();

		this.setParameter("sign", sign);

		// 调试信息
		this.setDebugMsg(sb.toString() + " => sign:" + sign);

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
	/**
	 * 使用SHA256算法验证签名。规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * @return boolean
	 */
	public boolean verifySign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		
		sb.append("key=" + this.getKey());
		
		//算出摘要
		String enc = getCharacterEncoding(this.request, this.response);
		String sign = SHA256Encode(sb.toString(), enc).toLowerCase();
		
		String tenpaySign = this.getParameter("sign").toLowerCase();
		
		//debug信息
		this.setDebugMsg(sb.toString() + " => sign:" + sign +
				" epaylinksSign:" + tenpaySign);
		
		return tenpaySign.equals(sign);
	}
	public static String SHA256Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

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

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getCurrency_type() {
		return currency_type;
	}

	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}

	public String getOrder_create_ip() {
		return order_create_ip;
	}

	public void setOrder_create_ip(String order_create_ip) {
		this.order_create_ip = order_create_ip;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getUrlEncoding() {
		return urlEncoding;
	}
	public void setUrlEncoding(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}

}
