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

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.util.StringUtils;

// v1.6.5.5 RDPROJECT-148 xx 2013-09-23 start
// 新增类
// v1.6.5.5 RDPROJECT-148 xx 2013-09-23 end
/**
 * 易生支付
 
 * @version 1.0
 * @since 2013-9-23
*/ 

public class YiShengPay {
	
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		
	//接口服务名称，目前固定值：create_direct_pay_by_user（网上支付）
	public String service="create_direct_pay_by_user";
	
	// 合作身份者ID，由纯数字组成的字符串
	public String partner = "";
	
	//交易安全检验码，由数字和字母组成的32位字符串
	public String key = "";
	//签约易生支付账号或卖家收款易生支付帐户
	public String seller_email = "";
	//买家收款易生支付帐户
	public String buyer_email = "";
	
	//notify_url 交易过程中服务器通知的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
	public String notify_url = "";
	
	//付完款后跳转的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
	public String return_url = "";
	
	//收款方名称，如：公司名称、网站名称、收款人姓名等
	public String mainname = "易生支付";
	
	//版本号，固定值：PRECARD_1.0（预付费卡支付）
	public String version="PRECARD_1.0";
	
	//支付类型，目前固定值：1
	public String payment_type="1";
	
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	//支付提交地址
	public String easypay_url="";
	//返回验证订单地址
	public String verify_url="";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public String _input_charset = "";
	
	// 签名方式 不需修改
	public String sign_type = "MD5";
	
	//访问模式,根据自己的服务器是否支持ssl访问，若支持请选择https；若不支持请选择http
	public String transport = "";
	
	//支付方式
	private String paymethod = "";

	//网银代码
	private String defaultbank;
	
	private String subject;// 商品名称

	private String body;// 商品描述

	//外部交易号
	private String out_trade_no;
	
	private String total_fee;// 付款金额
	
	private String sign;// 签名
	
	public void init(YiShengPay ysp,PaymentInterface paymentInterface){
		ysp.setService("create_direct_pay_by_user");
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
    		ysp.setPartner(paymentInterface.getMerchant_id());
	    	ysp.setKey(paymentInterface.getKey());
	    	ysp.setTransport(paymentInterface.getTransport());
	    	ysp.setNotify_url(Global.getValue("weburl")+paymentInterface.getNotice_url());
	    	ysp.setReturn_url(Global.getValue("weburl")+paymentInterface.getReturn_url());
	    	ysp.setBody(paymentInterface.getOrder_description());
	    	ysp.set_input_charset(StringUtils.isBlank(paymentInterface.getChartset())?"UTF-8":paymentInterface.getChartset());
	    	ysp.setSeller_email(paymentInterface.getSeller_email());
    	}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
    	ysp.setMainname(Global.getValue("webname"));
    	ysp.setPayment_type("1");
    	ysp.setEasypay_url("https://cashier.bhecard.com/portal");
    	ysp.setVerify_url("http://mapi.bhecard.com/verify/notify?");
    	ysp.setSign_type("MD5");
    	ysp.setSubject(Global.getValue("webname"));
    	ysp.setDefaultbank("");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getSign(YiShengPay ysp,PaymentInterface paymentInterface) {
		Map sPara = new HashMap();
		sPara.put("service", ysp.getService());
		sPara.put("payment_type", ysp.getPayment_type());
		sPara.put("partner", ysp.getPartner());
		sPara.put("seller_email", ysp.getSeller_email());
		sPara.put("return_url", ysp.getReturn_url());
		sPara.put("notify_url", ysp.getNotify_url());
		sPara.put("_input_charset", ysp.get_input_charset());
		sPara.put("out_trade_no", ysp.getOut_trade_no());
		sPara.put("subject", ysp.getSubject());
		sPara.put("body", ysp.getBody());
		sPara.put("total_fee", ysp.getTotal_fee());
		sPara.put("buyer_email", ysp.getBuyer_email());
		sPara.put("paymethod", ysp.getPaymethod());
		sPara.put("defaultbank", ysp.getDefaultbank());
		String sign = BuildMysign(sPara, ysp.getKey(),paymentInterface);// 生成签名结果
		return sign;
	}
	
	/**
	 * 功能：构造表单提交HTML
	 * @param partner 合作身份者ID
	 * @param seller_email 签约易生支付账号或卖家易生支付帐户
	 * @param return_url 付完款后跳转的页面 要用 以http开头格式的完整路径，不允许加?id=123这类自定义参数
	 * @param notify_url 交易过程中服务器通知的页面 要用 以http开格式的完整路径，不允许加?id=123这类自定义参数
	 * @param out_trade_no 请与贵网站订单系统中的唯一订单号匹配
	 * @param subject 订单名称，显示在易生支付收银台里的“商品名称”里，显示在易生支付的交易管理的“商品名称”的列表里。
	 * @param body 订单描述、订单详细、订单备注，显示在易生支付收银台里的“商品描述”里
	 * @param total_fee 订单总金额，显示在易生支付收银台里的“交易金额”里
	 * @param buyer_email 默认买家易生支付账号
	 * @param input_charset 字符编码格式 目前支持 GBK 或 utf-8
	 * @param key 安全校验码
	 * @param sign_type 签名方式 不需修改
	 * @return 表单提交HTML文本
	 */
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	public String BuildForm(String service,
			String payment_type,
			String partner,
			String seller_email,
			String return_url,
			String notify_url,
			String out_trade_no,
			String subject,
			String body,
			String total_fee,
            String buyer_email,
            String input_charset,
            String paymethod,
            String defaultbank,
            String key,
            String sign_type,
            PaymentInterface paymentInterface
            ){
		Map<String, Object> sPara = new HashMap<String, Object>();
		sPara.put("service",service);
		sPara.put("payment_type",payment_type);
		sPara.put("partner", partner);
		sPara.put("seller_email", seller_email);
		sPara.put("return_url", return_url);
		sPara.put("notify_url", notify_url);
		sPara.put("_input_charset", input_charset);
		sPara.put("out_trade_no", out_trade_no);
		sPara.put("subject", subject);
		sPara.put("body", body);
		sPara.put("total_fee", total_fee);
		sPara.put("buyer_email", buyer_email);
		sPara.put("paymethod", paymethod);
		sPara.put("defaultbank", defaultbank);
		
		String mysign = BuildMysign(sPara, key,paymentInterface);//生成签名结果
		
		StringBuffer sbHtml = new StringBuffer();
		List<String> keys = new ArrayList<String>(sPara.keySet());
		String gateway = easypay_url;
		
		//GET方式传递
		sbHtml.append("<form id=\"easypaysubmit\" name=\"easypaysubmit\" action=\"").append(gateway).append("\" method=\"get\">");
		String name ="";
		String value ="";
		for (int i = 0; i < keys.size(); i++) {
			name=(String) keys.get(i);
			value=(String) sPara.get(name);
			if(value!=null && !"".equals(value)){
				sbHtml.append("<input type=\"hidden\" name=\"").append(name).append("\" value=\"" + value + "\"/>");
			}
		}
        sbHtml.append("<input type=\"hidden\" name=\"sign\" value=\"").append(mysign).append("\"/>");
        sbHtml.append("<input type=\"hidden\" name=\"sign_type\" value=\"").append(sign_type).append("\"/>");
        
        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"易生支付确认付款\"></form>");
		return sbHtml.toString();
	}
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	/** 
	 * 功能：生成签名结果
	 * @param sArray 要签名的数组
	 * @param key 安全校验码
	 * @return 签名结果字符串
	 */
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	public String BuildMysign(Map sArray, String key,PaymentInterface paymentInterface) {
		if(sArray!=null && sArray.size()>0){
			StringBuilder prestr = CreateLinkString(sArray);  //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
			return md5(prestr.append(key).toString(),paymentInterface);//把拼接后的字符串再与安全校验码直接连接起来,并且生成加密串
		}
		return null;
	}
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	/** 
	 * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public StringBuilder CreateLinkString(Map params){
			List keys = new ArrayList(params.keySet());
			Collections.sort(keys);
	
			StringBuilder prestr = new StringBuilder();
			String key="";
			String value="";
			for (int i = 0; i < keys.size(); i++) {
				key=(String) keys.get(i);
				value = (String) params.get(key);
				if("".equals(value) || value == null || 
						key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")){
					continue;
				}
				prestr.append(key).append("=").append(value).append("&");
			}
			return prestr.deleteCharAt(prestr.length()-1);
	}
	
	/**
	 * 将易生支付POST过来反馈信息转换一下
	 * @param requestParams 返回参数信息
	 * @return Map 返回一个只有字符串值的MAP
	 * */
	public Map transformRequestMap(Map requestParams){
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
	* invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
	* true 返回正确信息
	* false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	*/
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	public String Verify(String notify_id,PaymentInterface paymentInterface){
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		//获取远程服务器ATN结果，验证是否是易生支付服务器发来的请求
		String transport = paymentInterface.getTransport();
		String partner = paymentInterface.getMerchant_id();
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		StringBuilder veryfy_url = new StringBuilder();
		if(transport.equalsIgnoreCase("https")){
			veryfy_url.append("https://mapi.bhecard.com/verify/notify?");
		} else{
			veryfy_url.append("http://mapi.bhecard.com/verify/notify?");
		}
		veryfy_url.append("partner=").append(partner).append("&notify_id=").append(notify_id);
		////System.out.println(veryfy_url);
		String responseTxt = CheckUrl(veryfy_url.toString());
		return responseTxt;

	}  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	
	/**
	* *功能：获取远程服务器ATN结果
	* @param urlvalue 指定URL路径地址
	* @return 服务器ATN结果
	* 验证结果集：
	* invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
	* true 返回正确信息
	* false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	*/
	private String CheckUrl(String urlvalue){
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
	
	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * 
	 * @return 密文
	 */
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
	public String md5(String text,PaymentInterface paymentInterface) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}

		try {
		    //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		    String chartset=StringUtils.isNull(paymentInterface.getChartset());
		    chartset=StringUtils.isBlank(chartset)?"UTF-8":chartset;
			msgDigest.update(text.getBytes(chartset));    //注意改接口是按照指定编码形式签名
			  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
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
	 * */
	private char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}

	
	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getEasypay_url() {
		return easypay_url;
	}

	public void setEasypay_url(String easypay_url) {
		this.easypay_url = easypay_url;
	}

	public String getVerify_url() {
		return verify_url;
	}

	public void setVerify_url(String verify_url) {
		this.verify_url = verify_url;
	}

	public String get_input_charset() {
		return _input_charset;
	}

	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

}
