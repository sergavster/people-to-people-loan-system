package com.p2psys.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.util.StringUtils;

public class RongPay {

	private final static Logger logger = Logger.getLogger(RongPay.class);
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private String merchant_ID;// 合作身份者ID，由纯数字组成的字符串

	private String key;// 交易安全检验码，由数字和字母组成的32位字符串

	private String seller_email;// 签约融宝支付账号或卖家收款融宝支付帐户

	private String notify_url;// notify_url 交易过程中服务器通知的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数

	private String return_url;// 付完款后跳转的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数

	private String mainname;// 收款方名称，如：公司名称、网站名称、收款人姓名等

	private String service;// 接口服务名称，目前固定值：online_pay（网上支付）

	private String payment_type;// 支付类型，目前固定值：1

	private String rongpay_url;// 支付提交地址
	//private String rongpay_url="http://192.168.0.58:18170/portal";

	private String verify_url="http://interface.reapal.com/verify/notify?";// 返回验证订单地址
	// private String verify_url="http://192.168.0.79:8080/mapi/verify/notify?";

	private String charset;// 字符编码格式 目前支持 gbk 或 utf-8

	private String sign_type;// 签名方式 不需修改

	private String transport;// 访问模式,根据自己的服务器是否支持ssl访问，若支持请选择https；若不支持请选择http

	private String title;// 商品名称

	private String body;// 商品描述

	private String total_fee;// 付款金额

	private String buyer_email = "";// 默认买家融宝支付账号

	private String paymethod = "";// 支付方式

	private String defaultbank;// 网银代码

	private String sign;// 签名

	private String order_no;//请与贵网站订单系统中的唯一订单号匹配
    public void init(RongPay rp,PaymentInterface paymentInterface){
      //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
    	if(paymentInterface!=null){
    		rp.setMerchant_ID(paymentInterface.getMerchant_id());
        	rp.setKey(paymentInterface.getKey());
        	rp.setTransport(paymentInterface.getTransport());
        	rp.setNotify_url(Global.getValue("weburl")+paymentInterface.getNotice_url());
        	rp.setReturn_url(Global.getValue("weburl")+paymentInterface.getReturn_url());
        	rp.setSeller_email(paymentInterface.getSeller_email());    
        	rp.setBody(paymentInterface.getOrder_description());
        	rp.setCharset(StringUtils.isBlank(paymentInterface.getChartset())?"UTF-8":paymentInterface.getChartset());
    	}
    	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
    	rp.setMainname(Global.getValue("webname"));
    	rp.setService("online_pay");
    	rp.setPayment_type("1");
    	rp.setRongpay_url("http://epay.reapal.com/portal");
    	rp.setVerify_url("http://interface.reapal.com/verify/notify?");
    	rp.setSign_type("MD5");
    	rp.setTitle(Global.getValue("webname"));
    	rp.setDefaultbank("");
    }
	/**
	 * 功能：提交路径
	 * 
	 * @param merchant_ID 合作身份者ID
	 * @param seller_email 签约融宝支付账号或卖家融宝支付帐户
	 * @param return_url 付完款后跳转的页面 要用 以http开头格式的完整路径，不允许加?id=123这类自定义参数
	 * @param notify_url 交易过程中服务器通知的页面 要用 以http开格式的完整路径，不允许加?id=123这类自定义参数
	 * @param order_no 请与贵网站订单系统中的唯一订单号匹配
	 * @param title 订单名称，显示在融宝支付收银台里的“商品名称”里，显示在融宝支付的交易管理的“商品名称”的列表里。
	 * @param body 订单描述、订单详细、订单备注，显示在融宝支付收银台里的“商品描述”里
	 * @param total_fee 订单总金额，显示在融宝支付收银台里的“交易金额”里
	 * @param buyer_email 默认买家融宝支付账号
	 * @param charset 字符编码格式 目前支持 GBK 或 utf-8
	 * @param key 安全校验码
	 * @param sign_type 签名方式 不需修改
	 * @return 表单提交HTML文本
	 */
  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	public String BuildForm(RongPay rp,PaymentInterface paymentInterface) {
		if("No".equals(rp.getDefaultbank())){
        	paymethod="bankPay";
            defaultbank="";
            rp.setDefaultbank(defaultbank);
        }else{
        	paymethod="directPay";
        }
		rp.setPaymethod(paymethod);
		String sign=getSign(rp,paymentInterface);
		StringBuffer url=new StringBuffer();
		url.append(this.rongpay_url).append("?");
		url.append("service=").append(rp.getService()).append("&");
		url.append("payment_type=").append(rp.getPayment_type()).append("&");
		url.append("merchant_ID=").append(rp.getMerchant_ID()).append("&");
		url.append("seller_email=").append(rp.getSeller_email()).append("&");
		url.append("return_url=").append(rp.getReturn_url()).append("&");
		url.append("notify_url=").append(rp.getNotify_url()).append("&");
		url.append("charset=").append(rp.getCharset()).append("&");
		url.append("order_no=").append(rp.getOrder_no()).append("&");
		url.append("title=").append(rp.getTitle()).append("&");
		url.append("body=").append(rp.getBody()).append("&");
		url.append("total_fee=").append(rp.getTotal_fee()).append("&");
		url.append("buyer_email=").append(rp.getBuyer_email()).append("&");
		url.append("paymethod=").append(rp.getPaymethod()).append("&");
		url.append("defaultbank=").append(rp.getDefaultbank()).append("&");
		url.append("sign=").append(sign).append("&");
		url.append("sign_type=").append(this.getSign_type()).append("&");
		logger.info(url.toString());
		return url.toString();
	}
	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	@Override
	public String toString() {
		return "RongPay [merchant_ID=" + merchant_ID + ", key=" + key + ", seller_email=" + seller_email
				+ ", notify_url=" + notify_url + ", return_url=" + return_url + ", mainname=" + mainname + ", service="
				+ service + ", payment_type=" + payment_type + ", rongpay_url=" + rongpay_url + ", verify_url="
				+ verify_url + ", charset=" + charset + ", sign_type=" + sign_type + ", transport=" + transport
				+ ", title=" + title + ", body=" + body + ", total_fee=" + total_fee + ", buyer_email=" + buyer_email
				+ ", paymethod=" + paymethod + ", defaultbank=" + defaultbank + ", sign=" + sign + ", order_no="
				+ order_no + "]";
	}
	/**
	 * 功能：生成签名结果
	 * 
	 * @param sArray 要签名的数组
	 * @param key 安全校验码
	 * @return 签名结果字符串
	 */
	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	public String BuildMysign(Map sArray, String key,PaymentInterface paymentInterface) {
		if (sArray != null && sArray.size() > 0) {
			StringBuilder prestr = CreateLinkString(sArray); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
			return md5(prestr.append(key).toString(),paymentInterface);// 把拼接后的字符串再与安全校验码直接连接起来,并且生成加密串
		}
		return null;
	}
	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	/**
	 * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public StringBuilder CreateLinkString(Map params) {
		List keys = new ArrayList(params.keySet());
		Collections.sort(keys);

		StringBuilder prestr = new StringBuilder();
		String key = "";
		String value = "";
		for (int i = 0; i < keys.size(); i++) {
			key = (String) keys.get(i);
			value = (String) params.get(key);
			if ("".equals(value) || value == null || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			prestr.append(key).append("=").append(value).append("&");
		}
		return prestr.deleteCharAt(prestr.length() - 1);
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text 明文
	 * @return 密文
	 */
	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	public String md5(String text,PaymentInterface paymentInterface) {
		MessageDigest msgDigest = null;
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		charset=StringUtils.isNull(paymentInterface.getChartset());
        charset=StringUtils.isBlank(charset)?"utf-8":charset;
      //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes(charset)); // 注意改接口是按照指定编码形式签名

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException("System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}
	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	/**
	 * 十六进制转换
	 */
	private static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}
	/**
	 * 将融宝支付POST过来反馈信息转换一下
	 * @param requestParams 返回参数信息
	 * @return Map 返回一个只有字符串值的MAP
	 * */
	public  Map transformRequestMap(Map requestParams){
		Map params = null;
		if(requestParams!=null && requestParams.size()>0){
			params = new HashMap();
			String name ="";
			String[] values =null;
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				name= (String) iter.next();
				values= (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				params.put(name, valueStr);
			}
		}
		return params;
	}
	/**
	* *功能：获取远程服务器ATN结果,验证返回URL
	* @param notify_id 通知校验ID
	* @return 服务器ATN结果
	* 验证结果集：
	* invalid命令参数不对 出现这个错误，请检测返回处理中merchant_ID和key是否为空 
	* true 返回正确信息
	* false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	*/
	public  String Verify(String notify_id,PaymentInterface paymentInterface){
		
		//获取远程服务器ATN结果，验证是否是融宝支付服务器发来的请求
		StringBuilder veryfy_url_builder = new StringBuilder();
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	    transport=paymentInterface.getTransport();
	    merchant_ID=paymentInterface.getMerchant_id();
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		if(transport.equalsIgnoreCase("https")){
			veryfy_url_builder.append("https://interface.rongpay.com.cn/verify/notify?");
		} else{
			veryfy_url_builder.append(verify_url);
		}
		veryfy_url_builder.append("merchant_ID=").append(merchant_ID).append("&notify_id=").append(notify_id);
		
		String responseTxt = CheckUrl(veryfy_url_builder.toString());
		
		return responseTxt;

	}
	/**
	* *功能：获取远程服务器ATN结果
	* @param urlvalue 指定URL路径地址
	* @return 服务器ATN结果
	* 验证结果集：
	* invalid命令参数不对 出现这个错误，请检测返回处理中merchant_ID和key是否为空 
	* true 返回正确信息
	* false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	*/
	private static String CheckUrl(String urlvalue){
		String inputLine = "";
		try {
			URL url = new URL(urlvalue);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			if(in!=null){
				inputLine = in.readLine().toString();
			}
			in.close();
			urlConnection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputLine;
	}
	public String getMerchant_ID() {
		return merchant_ID;
	}

	public void setMerchant_ID(String merchant_ID) {
		this.merchant_ID = merchant_ID;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getMainname() {
		return mainname;
	}

	public void setMainname(String mainname) {
		this.mainname = mainname;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getRongpay_url() {
		return rongpay_url;
	}

	public void setRongpay_url(String rongpay_url) {
		this.rongpay_url = rongpay_url;
	}

	public String getVerify_url() {
		return verify_url;
	}

	public void setVerify_url(String verify_url) {
		this.verify_url = verify_url;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getDefaultbank() {
		return defaultbank;
	}

	public void setDefaultbank(String defaultbank) {
		this.defaultbank = defaultbank;
	}
	public String getSign(){
		return sign;
	}
	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	public String getSign(RongPay rp,PaymentInterface paymentInterface) {
		Map sPara = new HashMap();
		sPara.put("service", rp.getService());
		sPara.put("payment_type", rp.getPayment_type());
		sPara.put("merchant_ID", rp.getMerchant_ID());
		sPara.put("seller_email", rp.getSeller_email());
		sPara.put("return_url", rp.getReturn_url());
		sPara.put("notify_url", rp.getNotify_url());
		sPara.put("charset", rp.getCharset());
		sPara.put("order_no", rp.getOrder_no());
		sPara.put("title", rp.getTitle());
		sPara.put("body", rp.getBody());
		sPara.put("total_fee", rp.getTotal_fee());
		sPara.put("buyer_email", rp.getBuyer_email());
		sPara.put("paymethod", rp.getPaymethod());
		sPara.put("defaultbank", rp.getDefaultbank());
		String sign = BuildMysign(sPara, rp.getKey(),paymentInterface);// 生成签名结果
		return sign;
	}
	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
}
