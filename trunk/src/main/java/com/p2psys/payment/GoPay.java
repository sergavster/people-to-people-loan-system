package com.p2psys.payment;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.util.StringUtils;


public class GoPay extends Pay {
	
	private final static Logger logger=Logger.getLogger(GoPay.class);
	
	private String version; //网关版本号
	private String charset;//字符集 1 GBK 2 UTF-8,默认为 1
	private String language;//网关语言版本 1 中文 2 英文
	private String signType; //报文加密方式,1 MD5 2 SHA,默认为 1
	private String isRepeatSubmit; //订单是否允许重复提交
	private String signValue ;//密文串
	private String currencyType; //币种
	
	private String virCardNoIn; //国付宝账号
	private String merchantID; //国付宝的商户代码
	private String tranCode;
	private String tranIP; //交易IP
	
	private String bankCode;
	private String userType;
	
	private String servetime;
	private String respCode;
	
	//private String submitUrl="https://211.88.7.30/PGServer/Trans/WebClientAction.do";
	//private String serverTimeUrl="https://211.88.7.30/PGServer/time";
	
	private String submitUrl="https://www.gopay.com.cn/PGServer/Trans/WebClientAction.do";
	private String serverTimeUrl="https://www.gopay.com.cn/PGServer/time";
	
//	private String privateKey="youyou168";
	private String privateKey;
	
	private String orderId;
	private String gopayOutOrderId;
	
	public GoPay() {
		super();
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getIsRepeatSubmit() {
		return isRepeatSubmit;
	}
	public void setIsRepeatSubmit(String isRepeatSubmit) {
		this.isRepeatSubmit = isRepeatSubmit;
	}
	public String getSignValue() {
		return signValue;
	}
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getTranCode() {
		return tranCode;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	public String getVirCardNoIn() {
		return virCardNoIn;
	}
	public void setVirCardNoIn(String virCardNoIn) {
		this.virCardNoIn = virCardNoIn;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getSubmitUrl() {
		return submitUrl;
	}
	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getGopayOutOrderId() {
		return gopayOutOrderId;
	}

	public void setGopayOutOrderId(String gopayOutOrderId) {
		this.gopayOutOrderId = gopayOutOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTranIP() {
		return tranIP;
	}
	public void setTranIP(String tranIP) {
		this.tranIP = tranIP;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getServetime() {
		return servetime;
	}

	public void setServetime(String servetime) {
		this.servetime = servetime;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	//国付宝的默认信息
	public void init(GoPay pay,PaymentInterface paymentInterface){
		this.version="2.1";
		this.charset="2";
		this.language="1";
		this.signType="1";
		this.currencyType="156";
		this.isRepeatSubmit="1";
		//设置账号，测试环境
		//this.merchantID="0000003358";
		//this.virCardNoIn="0000000001000000584";
		//this.tranCode="8888";
		
	//	this.merchantID="0000048927";
	//	this.virCardNoIn="0000000002000117775";
		//this.merchantID="0000050990";
		//this.virCardNoIn="0000000002000119609";
		this.tranCode="8888";
		
		this.servetime=this.getGopayServerTime();
		String callback="";
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			pay.setPrivateKey(paymentInterface.getKey());
			pay.setMerchantID(paymentInterface.getMerchant_id());
			pay.setVirCardNoIn(paymentInterface.getInterface_Into_account());
			String weburl = Global.getValue("weburl");
		    callback = weburl + paymentInterface.getReturn_url();
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		logger.debug("CallbacK:" + callback);
		pay.setFrontMerUrl(callback);
		pay.setBackgroundMerUrl(callback);
		String _v=this.getSignVal();
		logger.debug("Sign明文:"+_v);
		this.signValue=getGopaySignValueByMD5(_v);
		logger.debug("Sign密文:"+signValue);
		
	}
	
	private String getSignVal(){
		StringBuffer sb=new StringBuffer();
		sb.append("version=[").append(this.getVersion()).append("]");
		sb.append("tranCode=[").append(this.getTranCode()).append("]");
		sb.append("merchantID=[").append(this.getMerchantID()).append("]");
		sb.append("merOrderNum=[").append(this.getGood().getMerOrderNum()).append("]");
		sb.append("tranAmt=[").append(this.getGood().getTranAmt()).append("]");
		if(StringUtils.isBlank(this.getGood().getFeeAmt())){
			sb.append("feeAmt=[]");
		}else{
			sb.append("feeAmt=[").append(this.getGood().getFeeAmt()).append("]");
		}
		sb.append("tranDateTime=[").append(this.getGood().getTranDateTime()).append("]");
		sb.append("frontMerUrl=[").append(this.getFrontMerUrl()).append("]");
		sb.append("backgroundMerUrl=[").append(this.getBackgroundMerUrl()).append("]");
		sb.append("orderId=[]");
		sb.append("gopayOutOrderId=[]");
		sb.append("tranIP=[").append(this.getTranIP()).append("]");
		sb.append("respCode=[]");
		sb.append("gopayServerTime=[").append(com.p2psys.util.StringUtils.isNull(servetime)).append("]");
		sb.append("VerficationCode=[").append(getPrivateKey()).append("]");
		return sb.toString();
	}
	
	public String getCallbackSignVal(){
		StringBuffer sb=new StringBuffer();
		sb.append("version=[").append(this.getVersion()).append("]");
		sb.append("tranCode=[").append(this.getTranCode()).append("]");
		sb.append("merchantID=[").append(this.getMerchantID()).append("]");
		sb.append("merOrderNum=[").append(this.getGood().getMerOrderNum()).append("]");
		sb.append("tranAmt=[").append(this.getGood().getTranAmt()).append("]");
		sb.append("feeAmt=[").append(this.getGood().getFeeAmt()).append("]");
		sb.append("tranDateTime=[").append(this.getGood().getTranDateTime()).append("]");
		sb.append("frontMerUrl=[").append(this.getFrontMerUrl()).append("]");
		sb.append("backgroundMerUrl=[").append(this.getBackgroundMerUrl()).append("]");
		sb.append("orderId=[").append(this.getOrderId()).append("]");
		sb.append("gopayOutOrderId=[").append(this.getGopayOutOrderId()).append("]");
		sb.append("tranIP=[").append(this.getTranIP()).append("]");
		sb.append("respCode=[").append(respCode).append("]");
		sb.append("gopayServerTime=[]");
		sb.append("VerficationCode=[").append(getPrivateKey()).append("]");
		return sb.toString();
	}
	
	public String getCallbackMd5SignVal(){
		String callbackSignVal=getCallbackSignVal();
		logger.debug("Callback Sign明文:"+getCallbackSignVal());
		String callbackMd5SignVal=getGopaySignValueByMD5(callbackSignVal);
		logger.debug("Callback Sign密文:"+callbackMd5SignVal);
		return callbackMd5SignVal;
	}
	
	
	
	
	public String submitGet(){
		StringBuffer url=new StringBuffer();
		url.append(this.getSubmitUrl()).append("?");
		url.append("version=").append(this.getVersion()).append("&");//网关版本号
		url.append("charset=").append(this.getCharset()).append("&");//字符集 1 GBK 2 UTF-8
		url.append("language=").append(this.getLanguage()).append("&");//1 中文 2 英文
		url.append("signType=").append(this.getSignType()).append("&");//报文加密方式 1 MD5 2 SHA
		url.append("tranCode=").append(this.getTranCode()).append("&");//交易代码 本域指明了交易的类型，支付网关接口必须为8888
		url.append("merchantID=").append(this.getMerchantID()).append("&");//商户代码
		url.append("merOrderNum=").append(this.getGood().getMerOrderNum()).append("&");//订单号
		url.append("tranAmt=").append(this.getGood().getTranAmt()).append("&");//交易金额
		url.append("feeAmt=&");//
		
		url.append("currencyType=").append(this.getCurrencyType()).append("&");//156，代表人民币
		url.append("frontMerUrl=").append(this.getFrontMerUrl()).append("&");//商户前台通知地址
		url.append("backgroundMerUrl=").append(this.getBackgroundMerUrl()).append("&");
		
		url.append("tranDateTime=").append(this.getGood().getTranDateTime()).append("&");//本域为订单发起的交易时间
		url.append("virCardNoIn=").append(this.getVirCardNoIn()).append("&");//本域指卖家在国付宝平台开设的国付宝账户号
		url.append("tranIP=").append(this.getTranIP()).append("&");//发起交易的客户IP地址
		url.append("isRepeatSubmit=").append(this.getIsRepeatSubmit()).append("&");//	0不允许重复 1 允许重复 
		
		url.append("goodsName=").append(this.getGood().getGoodsName()).append("&");
		url.append("buyerName=").append(this.getGood().getBuyerName()).append("&");
		url.append("signValue=").append(this.getSignValue()).append("&");
		url.append("userType=").append(StringUtils.isNull(getUserType())).append("&");
		url.append("bankCode=").append(StringUtils.isNull(getBankCode())).append("&");
		url.append("gopayServerTime=").append(servetime).append("&");
		logger.info(url.toString());
		return url.toString();
	}
	
	public String getGopayServerTime() {
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 10000); 
		GetMethod getMethod = new GetMethod(serverTimeUrl);
		getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");  
		// 执行getMethod
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(getMethod);			
			if (statusCode == HttpStatus.SC_OK){
				String respString = org.apache.commons.lang.StringUtils.trim((new String(getMethod.getResponseBody(),"UTF-8")));
				return respString;
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	
	private String getGopaySignValueByMD5(String signvalue){
		String val="";
		try {
			val=DigestUtils.md5Hex(signvalue);
		} catch (Exception e) {
			logger.error("getGopaySignValueByMD5() has error");
			e.printStackTrace();
		}
		return val;
	}
	
}
