package com.p2psys.payment;

import jxl.common.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;

public class IPSPay {
	private static Logger logger=Logger.getLogger(IPSPay.class); 
	private String mer_code;
	private String billno;
	private String amount;
	private String date;
	private String currency_Type;
	private String gateway_Type;
	private String lang;
	private String merchanturl;
	private String failUrl;
	private String errorUrl;
	private String attach;
	private String orderEncodeType;
	private String retEncodeType;
	private String rettype;
	private String serverUrl;
	private String signMD5;
	private String mer_key;
	private String doCredit;
	private String bankco;
	
	//callback 新增的
	private String succ;
	private String msg;
	private String ipsbillno;
	private String signature;
	
	public IPSPay() {
		super();
	}
	
	public String getMer_code() {
		return mer_code;
	}
	public void setMer_code(String mer_code) {
		this.mer_code = mer_code;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCurrency_Type() {
		return currency_Type;
	}
	public void setCurrency_Type(String currency_Type) {
		this.currency_Type = currency_Type;
	}
	public String getGateway_Type() {
		return gateway_Type;
	}
	public void setGateway_Type(String gateway_Type) {
		this.gateway_Type = gateway_Type;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getMerchanturl() {
		return merchanturl;
	}
	public void setMerchanturl(String merchanturl) {
		this.merchanturl = merchanturl;
	}
	public String getFailUrl() {
		return failUrl;
	}
	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}
	public String getErrorUrl() {
		return errorUrl;
	}
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOrderEncodeType() {
		return orderEncodeType;
	}
	public void setOrderEncodeType(String orderEncodeType) {
		this.orderEncodeType = orderEncodeType;
	}
	public String getRetEncodeType() {
		return retEncodeType;
	}
	public void setRetEncodeType(String retEncodeType) {
		this.retEncodeType = retEncodeType;
	}
	public String getRettype() {
		return rettype;
	}
	public void setRettype(String rettype) {
		this.rettype = rettype;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public String getSignMD5() {
		return signMD5;
	}
	public void setSignMD5(String signMD5) {
		this.signMD5 = signMD5;
	}
	public String getMer_key() {
		return mer_key;
	}
	public void setMer_key(String mer_key) {
		this.mer_key = mer_key;
	}
	public String getDoCredit() {
		return doCredit;
	}
	public void setDoCredit(String doCredit) {
		this.doCredit = doCredit;
	}
	public String getBankco() {
		return bankco;
	}
	public void setBankco(String bankco) {
		this.bankco = bankco;
	}
	public String getSucc() {
		return succ;
	}
	public void setSucc(String succ) {
		this.succ = succ;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getIpsbillno() {
		return ipsbillno;
	}
	public void setIpsbillno(String ipsbillno) {
		this.ipsbillno = ipsbillno;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
    public void init(IPSPay ips,PaymentInterface paymentInterface){
		String weburl = Global.getValue("weburl");
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
    	if(paymentInterface!=null){
			ips.setMer_code(paymentInterface.getMerchant_id());
			ips.setMer_key(paymentInterface.getKey());
			ips.setMerchanturl(weburl + paymentInterface.getNotice_url());
			ips.setFailUrl(weburl + paymentInterface.getNotice_url());
			ips.setServerUrl(weburl +paymentInterface.getReturn_url());
		}
    	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
    }
	public String encodeSignMD5(){
		cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
		//订单加密的明文 billno+【订单编号】+ currencytype +【币种】+ amount +【订单金额】+
		// date +【订单日期】+ orderencodetype +【订单支付接口加密方式】+【商户内部证书字符串】 
		String value="billno"+billno +"currencytype"+currency_Type+"amount"+ amount + 
				"date" +date +"orderencodetype"+orderEncodeType + mer_key;
		logger.info(value);
		String signMD5 = b.toMD5(value).toLowerCase();
		logger.info("IPSPay signMD5:"+signMD5);
		this.signMD5=signMD5;
		return signMD5;
	}

	public String callbackSign(){
		String content="billno"+billno + "currencytype"+currency_Type+"amount"+amount
				+"date"+date+"succ"+succ+"ipsbillno"+ipsbillno+"retencodetype"+retEncodeType;  
		return content;
	}
	
	@Override
	public String toString() {
		return "IPSPay [mer_code=" + mer_code + ", billno=" + billno
				+ ", amount=" + amount + ", date=" + date + ", currency_Type="
				+ currency_Type + ", gateway_Type=" + gateway_Type + ", lang="
				+ lang + ", merchanturl=" + merchanturl + ", failUrl="
				+ failUrl + ", errorUrl=" + errorUrl + ", attach=" + attach
				+ ", orderEncodeType=" + orderEncodeType + ", retEncodeType="
				+ retEncodeType + ", rettype=" + rettype + ", serverUrl="
				+ serverUrl + ", signMD5=" + signMD5 + ", mer_key=" + mer_key
				+ "]";
	}
	

}
