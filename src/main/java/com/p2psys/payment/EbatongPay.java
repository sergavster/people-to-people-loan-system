package com.p2psys.payment;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.mypay.merchantutil.Md5Encrypt;
import com.mypay.merchantutil.UrlHelper;
import com.mypay.merchantutil.timestamp.TimestampResponseParser;
import com.mypay.merchantutil.timestamp.TimestampResponseResult;
import com.mypay.merchantutil.timestamp.TimestampUtils;
import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.tool.order.ebatong.EbatongOrder;
import com.p2psys.tool.order.ebatong.EbatongResult;
import com.p2psys.util.StringUtils;

/**
 * 易八通支付接口
 
 * 2013-12-02
 */
public class EbatongPay {
	private static Logger logger = Logger.getLogger(EbatongPay.class);
	//基本参数
    private String sign_type; //签名方式
    private String sign;  //签名
    private String service; //接口名称
    private String partner;//合作者身份ID
    private String input_charset; //参数编码字符集
    private String notify_url;//服务器异步通知页面路径
    private String return_url;//页面跳转同步通知页面路径
    private String error_notify_url;//请求出错时的通知页面路径
    //业务参数
    private String out_trade_no;//商户网站唯一订单号
    private String subject;//商品名称
    private String payment_type;//支付类型
    private String exter_invoke_ip;//订单IP
    private String anti_phishing_key;//防钓鱼时间戳
    private String seller_email;//卖家易八通用户名
    private String seller_id;//卖家易八通用户ID
    private String buyer_email;//买家易八通用户名
    private String buyer_id;//买家易八通用户ID
    private String price; //商品单价
    private String quantity;//购买数量
    private String total_fee;//交易金额
    private String body;//商品描述
    private String show_url;//商品展示网址 
    private String pay_method;//默认支付方式
    private String extra_common_param;//公用回传参数
    private String extend_param;//公用业务扩展参数
    private String it_b_pay;//超时时间
    private String royalty_type;//提成类型
    private String royalty_parameters;//分润用户名集
    private String default_bank;//默认网银
	private HttpServletRequest request;
	private HttpServletResponse response;
    public String init(EbatongPay ebatongPay,PaymentInterface paymentInterface,String amount,String tradeNo,String ip){
    	 String weburl=Global.getValue("weburl");
    	 service = "create_direct_pay_by_user"; // 服务名称：即时交易
    	 partner = paymentInterface.getMerchant_id(); // 合作者商户ID
    	 input_charset = paymentInterface.getChartset(); // 字符集
    	 input_charset=StringUtils.isBlank(input_charset)?"UTF-8":input_charset;
    	 sign_type = paymentInterface.getSignType(); // 签名算法HTTP://MERCHANT.WEB.SITE/MERCHANT_CGI?
    	 sign_type=StringUtils.isBlank(sign_type)?"MD5":sign_type;
    	 notify_url = weburl+paymentInterface.getNotice_url(); // 商户上传的服务器异步通知页面路径
    	 return_url =weburl+ paymentInterface.getReturn_url(); // 页面跳转同步通知页面路径
    	 error_notify_url = ""; // 请求出错时的通知页面路径，可空

    	// 反钓鱼用参数
    	 anti_phishing_key = ""; // 通过时间戳查询接口（见AskForTimestampDemo），获取的加密系统时间戳，有效时间：30秒
    	 exter_invoke_ip = ip; // 用户在外部系统创建交易时，由外部系统记录的用户IP地址

    	// 以下为业务参数
    	//String out_trade_no = "xxx-123456-noaa"; 
    	
    	// 易八通合作商户网站唯一订单号
    	 out_trade_no = tradeNo;
    	
    	 subject = Global.getValue("webid"); // 商品名称
    	 payment_type = "1"; // 支付类型，默认值为：1（商品购买）
    	/**
    	 * ”卖家易八通用户ID“优先于”卖家易八通用户名“
    	 * 两者不可同时为空
    	 */
    	 seller_email = ""; // 卖家易八通用户名
    	 seller_id = paymentInterface.getMerchant_id(); // 卖家易八通用户ID

    	 buyer_email = ""; // 买家易八通用户名，可空
    	 buyer_id = ""; // 买家易八通用户ID，可空

    	 price = ""; // 商品单价
    	 total_fee = amount; // 交易金额
    	 quantity = ""; // 购买数量

    	 body =""; // 商品描述，可空
    	 show_url = ""; // 商品展示网址，可空
    	 pay_method = "bankPay"; // 支付方式，directPay(余额支付)、bankPay(网银支付)，可空
    	 default_bank = StringUtils.isBlank(ebatongPay.getDefault_bank())?"":ebatongPay.getDefault_bank(); // 默认网银，可空
    	/**
    	 ABC_B2C=农行
    	 BJRCB_B2C=北京农村商业银行
    	 BOC_B2C=中国银行
    	 CCB_B2C=建行
    	 CEBBANK_B2C=中国光大银行
    	 CGB_B2C=广东发展银行
    	 CITIC_B2C=中信银行
    	 CMB_B2C=招商银行
    	 CMBC_B2C=中国民生银行
    	 COMM_B2C=交通银行
    	 FDB_B2C=富滇银行
    	 HXB_B2C=华夏银行
    	 HZCB_B2C_B2C=杭州银行
    	 ICBC_B2C=工商银行网
    	 NBBANK_B2C=宁波银行
    	 PINGAN_B2C=平安银行
    	 POSTGC_B2C=中国邮政储蓄银行
    	 SDB_B2C=深圳发展银行
    	 SHBANK_B2C=上海银行
    	 SPDB_B2C=上海浦东发展银行
    	 */
    	  royalty_parameters = ""; // 最多10组分润明细。示例：100001=0.01|100002=0.02 表示id为100001的用户要分润0.01元，id为100002的用户要分润0.02元。
    	  royalty_type = ""; // 提成类型，目前只支持一种类型：10，表示卖家给第三方提成；
    	  Map<String,String[]> reqMap=new HashMap<String ,String[]>();
    	  reqMap.put("service",new String[]{ service});
    	  reqMap.put("partner",new String[]{ partner});
    	  reqMap.put("input_charset",new String[]{ input_charset});
    	  reqMap.put("sign_type",new String[]{sign_type});
    	  reqMap.put("notify_url",new String[]{ notify_url});
    	  reqMap.put("return_url",new String[]{ return_url});
    	  reqMap.put("error_notify_url",new String[]{ error_notify_url});
    	  reqMap.put("anti_phishing_key",new String[]{ anti_phishing_key});
    	  reqMap.put("exter_invoke_ip",new String[]{ exter_invoke_ip});
    	  reqMap.put("out_trade_no",new String[]{ out_trade_no});
    	  reqMap.put("subject",new String[]{ subject});
    	  reqMap.put("payment_type",new String[]{ payment_type});
    	  reqMap.put("seller_email",new String[]{ seller_email});
    	  reqMap.put("seller_id",new String[]{ seller_id});
    	  reqMap.put("buyer_email",new String[]{ buyer_email});
    	  reqMap.put("buyer_id",new String[]{ buyer_id});
    	  reqMap.put("total_fee",new String[]{ total_fee});
    	  reqMap.put("quantity",new String[]{ quantity});
    	  reqMap.put("body",new String[]{ body});
    	  reqMap.put("show_url",new String[]{ show_url});
    	  reqMap.put("pay_method",new String[]{ pay_method});
    	  reqMap.put("default_bank",new String[]{ default_bank});
    	  reqMap.put("royalty_parameters",new String[]{royalty_parameters});
    	  reqMap.put("royalty_type",new String[]{ royalty_type});
    	  try {
	            anti_phishing_key = reqTimeStr(paymentInterface);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
    	  reqMap.put("anti_phishing_key",new String[]{ anti_phishing_key});
          String key = paymentInterface.getKey(); // 商户加密字符串 
          String paramStr = UrlHelper.sortParamers(reqMap);
          
          String plaintext = TimestampUtils.mergePlainTextWithMerKey(paramStr, key);
          
          // 加密(MD5加签)，默认已取UTF-8字符集，如果需要更改，可调用Md5Encrypt.setCharset(inputCharset)
          String sign = Md5Encrypt.encrypt(plaintext); 
          String encodedParamString="";
		try {
			encodedParamString = UrlHelper.encode(reqMap, input_charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
          String gateway = paymentInterface.getGatewayUrl(); // ebatong商户网关
          String url="";
          try {
			 url = gateway + "?" + encodedParamString + "&sign=" + URLEncoder.encode(sign, input_charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info(gateway + "?" + paramStr + "&sign=" + sign);
        logger.info("url=======" + url);
        return url;
    }
    /**
	 * 请求时间戳
	 * @return
	 * @throws Exception 
	 */
	public String reqTimeStr(PaymentInterface paymentInterface) throws Exception {
		String key = paymentInterface.getKey(); // 商户加密字符串
		// String ask_for_time_stamp_gateway = "https://www.ebatong.com/gateway.htm"; // ebatong商户网关
		String ask_for_time_stamp_gateway = "http://www.ebatong.com/gateway.htm"; // ebatong商户网关
		String service = "query_timestamp"; // 服务名称：请求时间戳
		String partner = this.partner; // 合作者商户ID
		String input_charset =this.input_charset; // 字符集
		String sign_type =this.sign_type; // 摘要签名算法
		// 请求参数排序
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("service",new String[]{service});
		params.put("partner",new String[]{partner});
		params.put("input_charset",new String[]{input_charset});
		params.put("sign_type",new String[]{sign_type});
		String paramStr = UrlHelper.sortParamers(params);	
		String plaintext = TimestampUtils.mergePlainTextWithMerKey(paramStr, key);	
		// 加密(MD5加签)，默认已取UTF-8字符集，如果需要更改，可调用Md5Encrypt.setCharset(inputCharset)
		String sign = Md5Encrypt.encrypt(plaintext); 
		// 拼接请求
		String url = ask_for_time_stamp_gateway + "?" + paramStr + "&sign=" + sign;
		// 通过HttpClient获取响应字符串
		HttpClient httpClient = new HttpClient();
		HttpMethod method = new GetMethod(url);
		String askForTimestampResponseString = null;
		try {
			httpClient.executeMethod(method);
			// 如果响应码为200，从响应中获取响应字符串
			if (method.getStatusCode() == 200) {				
				askForTimestampResponseString = method.getResponseBodyAsString();
				logger.info("askForTimestampResponseString=========="+askForTimestampResponseString);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection(); // 释放连接
		}
		// 验证时间戳有效性
		TimestampResponseResult result = TimestampResponseParser.parse(askForTimestampResponseString);
		String timestamp=null;
		if (result.isSuccess()) {
			timestamp = result.getTimestamp();
			String resultMd5 = result.getResultMd5();
			String timestampMergeWithMerKey = TimestampUtils.mergePlainTextWithMerKey(timestamp, key);
			logger.info("时间戳：" + timestamp);
			logger.info("有效性：" + resultMd5.equals(Md5Encrypt.encrypt(timestampMergeWithMerKey)));
		}
		return timestamp;
	}
	/**
	 * 将xml字符串转化成list集合形式
	 * @param xml xml字符串
	 * @return  list集合
	 */
	public EbatongOrder getOrderList(String xml) {
		/**
		 * 申明一个SAXBuilder解析对象
		 */
		SAXBuilder builder = new SAXBuilder();
		// 实例化map集合
		List<EbatongOrder> list = new ArrayList<EbatongOrder>();
		EbatongOrder order=null;
		try {

			// 申明一个EbatongOrder对象来保存读到的数据(先根据XML节点编写相应的javaBean实体类)
			EbatongOrder ebatongOrder = null;
			// 用SAXBuilder对象读到参数路径的文件到DOC对象中
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document

			Document document = sb.build(source);
			// 得到根节点
			 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
			Element eltRoot=document.getRootElement();
			 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
			// 得到子节点集合，得到的不包括属性
			List<Element> listChildNode = eltRoot.getChildren();
			order=new EbatongOrder();
			if(null!=eltRoot.getChildText("charset")){
				order.setCharset(eltRoot.getChildText("charset"));
			}
			if(null!=eltRoot.getChildText("sign")){
				order.setSign(eltRoot.getChildText("sign"));
			}
			if(null!=eltRoot.getChildText("status")){
				order.setStatus(eltRoot.getChildText("status"));
			}
			EbatongResult ebatongResult=null;
			for (Element e1 : listChildNode) {
				List<Element> newListChildNode = e1.getChildren();
				if (newListChildNode.size() > 0) {
						// 构造实体对象，保存数据
						ebatongResult = new EbatongResult();
						String outTradeNo=e1.getChildText("outTradeNo");
						logger.error("outTradeNo===="+outTradeNo);
						// 如果子节点名为“outTradeNo”的子节点内容不为空就用EbatongResult对象保存起来
						if (e1.getChildText("outTradeNo") != null)
							ebatongResult.setOuttradeNo(e1
									.getChildText("outTradeNo"));
						// 如果子节点名为“subject”的子节点内容不为空就用EbatongResult对象保存起来
						if (e1.getChildText("subject") != null)
							ebatongResult.setSubject(e1
									.getChildText("subject"));
						// 如果子节点名为“totalFee”的子节点内容不为空就用EbatongResult对象保存起来
						if (e1.getChildText("totalFee") != null)
							ebatongResult.setTotalFee(e1
									.getChildText("totalFee"));
						// 如果子节点名为“tradeNo”的属性内容不为空就用EbatongResult对象保存起来
						if (e1.getChildText("tradeNo") != null)
							ebatongResult.setTradeNo(e1
									.getChildText("tradeNo"));
						// 如果子节点名为“tradeStatus”的属性内容不为空就用EbatongResult对象保存起来
						if (null != e1.getChildText("tradeStatus"))
							ebatongResult.setTradeStatus(e1
									.getChildText("tradeStatus"));
						order.setEbatongResult(ebatongResult);

				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return order;

	}
	public String getSign_type() {
		return sign_type;
	}   
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getInput_charset() {
		return input_charset;
	}
	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
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
	public String getError_notify_url() {
		return error_notify_url;
	}
	public void setError_notify_url(String error_notify_url) {
		this.error_notify_url = error_notify_url;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getExter_invoke_ip() {
		return exter_invoke_ip;
	}
	public void setExter_invoke_ip(String exter_invoke_ip) {
		this.exter_invoke_ip = exter_invoke_ip;
	}
	public String getAnti_phishing_key() {
		return anti_phishing_key;
	}
	public void setAnti_phishing_key(String anti_phishing_key) {
		this.anti_phishing_key = anti_phishing_key;
	}
	public String getSeller_email() {
		return seller_email;
	}
	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getBuyer_email() {
		return buyer_email;
	}
	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getShow_url() {
		return show_url;
	}
	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}
	public String getPay_method() {
		return pay_method;
	}
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}
	public String getExtra_common_param() {
		return extra_common_param;
	}
	public void setExtra_common_param(String extra_common_param) {
		this.extra_common_param = extra_common_param;
	}
	public String getExtend_param() {
		return extend_param;
	}
	public void setExtend_param(String extend_param) {
		this.extend_param = extend_param;
	}
	public String getIt_b_pay() {
		return it_b_pay;
	}
	public void setIt_b_pay(String it_b_pay) {
		this.it_b_pay = it_b_pay;
	}
	public String getRoyalty_type() {
		return royalty_type;
	}
	public void setRoyalty_type(String royalty_type) {
		this.royalty_type = royalty_type;
	}
	public String getRoyalty_parameters() {
		return royalty_parameters;
	}
	public void setRoyalty_parameters(String royalty_parameters) {
		this.royalty_parameters = royalty_parameters;
	}
	public String getDefault_bank() {
		return default_bank;
	}
	public void setDefault_bank(String default_bank) {
		this.default_bank = default_bank;
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
