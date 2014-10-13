package com.p2psys.payment;

import java.security.MessageDigest;
import java.util.Random;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
/**
 * 
 *@ClassName:ShengPay
 *@Description: TODO(盛付通 javabean)
 *@author: chenbaike
 *@date 2013-6-27 上午11:22
 *
 */
 
public class ShengPay {
	
	private String name;//版本名称
	private String version;//版本号
	private String charset;//字符集
	private String msgSender;//发送方标识
	private String sendTime;//发送支付请求时间
	private String orderNo;//商户订单号
	private String orderAmount;//支付金额
	private String orderTime;//商户订单提交时间
	private String payType;//支付类型编码
	private String instCode;//银行编码
	private String pageUrl;//支付成功后回调地址
	private String notifyUrl;//通知发货地址
	private String productName;//商品名称
	private String buyerContact;//支付人联系方式
	private String buyerIp;//买家IP地址
	private String ext1;//扩展1
	private String signType;//签名类型
	private String signMsg;//签名串
	private String signKey;//密钥
	private String payChannel;//支付渠道
	private String backUrl;//在收银台跳转到商户指定的地址
	private String transNo;//盛付通交易号
	private String transAmount;//盛付通实际支付金额
	private String transStatus;//支付状态
	private String transType;//交易类型
	private String traceNo;//请求序列号
	private String ext2;//扩展2
	private String transTime;//交易时间
	private String merchantNo;//商户号
	private String errorCode;//错误代码
	private String errorMsg;//错误消息
	public void init(ShengPay spay,PaymentInterface paymentInterface){
		String weburl = Global.getValue("weburl");
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			// 商户号
			spay.setMsgSender(paymentInterface.getMerchant_id());
			// 回调地址
			spay.setPageUrl(weburl + paymentInterface.getReturn_url());
			spay.setNotifyUrl(weburl + paymentInterface.getNotice_url());
			// 密钥
			spay.setSignKey(paymentInterface.getKey());
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	}
	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public ShengPay() {
		name = "B2CPayment";
		version = "V4.1.1.1.1";

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getMsgSender() {
		return msgSender;
	}

	public void setMsgSender(String msgSender) {
		this.msgSender = msgSender;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBuyerContact() {
		return buyerContact;
	}

	public void setBuyerContact(String buyerContact) {
		this.buyerContact = buyerContact;
	}

	public String getBuyerIp() {
		return buyerIp;
	}

	public void setBuyerIp(String buyerIp) {
		this.buyerIp = buyerIp;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public String toSignStringRequest() {
		StringBuffer buf = new StringBuffer();
		buf.append(name != null ? name : "");
		buf.append(version != null ? version : "");
		buf.append(charset != null ? charset : "");
		buf.append(msgSender != null ? msgSender : "");
		buf.append(sendTime != null ? sendTime : "");
		buf.append(orderNo != null ? orderNo : "");
		buf.append(orderAmount != null ? orderAmount : "");
		buf.append(orderTime != null ? orderTime : "");
		buf.append(payType != null ? payType : "");
		buf.append(payChannel != null ? payChannel : "");
		buf.append(instCode != null ? instCode : "");
		buf.append(pageUrl != null ? pageUrl : "");
		buf.append(backUrl != null ? backUrl : "");
		buf.append(notifyUrl != null ? notifyUrl : "");
		buf.append(productName != null ? productName : "");
		buf.append(buyerContact != null ? buyerContact : "");
		buf.append(buyerIp != null ? buyerIp : "");
		buf.append(ext1 != null ? ext1 : "");
		buf.append(signType != null ? signType : "");
		return buf.toString();

	}
	
	public String toSignStringResponse() {
		StringBuffer buf = new StringBuffer();
		buf.append(name != null ? name : "");
		buf.append(version != null ? version : "");
		buf.append(charset != null ? charset : "");
		buf.append(traceNo != null ? traceNo : "");
		buf.append(msgSender != null ? msgSender : "");
		buf.append(sendTime != null ? sendTime : "");
		buf.append(instCode != null ? instCode : "");
		buf.append(orderNo != null ? orderNo : "");
		buf.append(orderAmount != null ? orderAmount : "");
		buf.append(transNo != null ? transNo : "");
		buf.append(transAmount != null ? transAmount : "");
		buf.append(transStatus != null ? transStatus : "");
		buf.append(transType != null ? transType : "");
		buf.append(transTime != null ? transTime : "");
		buf.append(merchantNo != null ? merchantNo : "");
		buf.append(errorCode != null ? errorCode : "");
		buf.append(errorMsg != null ? errorMsg : "");
		buf.append(ext1 != null ? ext1 : "");
		buf.append(signType != null ? signType : "");
		return buf.toString();

	}
	
	/**
	 * 直连，不显示收银台
	 * @param payType
	 * @param instCode
	 */
	public void setDirectPay(String payType,String instCode){
		this.payType = payType;
		this.instCode = instCode;
	}
	public static ShengPay signFormRequest(ShengPay form, String key) {
		String text = form.toSignStringRequest();
		String mac = sign(text, key, form.getCharset());
		form.setSignMsg(mac);
		return form;
	}
	public static ShengPay signFormResponse(ShengPay form, String key) {
		String text = form.toSignStringResponse();
		String mac = sign(text, key, form.getCharset());
		form.setSignMsg(mac);
		return form;
	}
	private static String sign(String data, String key, String charset) {
		MessageDigest msgDigest = null;
		data = data + key;
		byte[] enbyte = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
			msgDigest.update(data.getBytes(charset));
			enbyte = msgDigest.digest();
		} catch (Exception e) {

		}
		return bin2hex(enbyte);
	}
	private static String createRandomNumber(int length) {
		StringBuffer buffer = new StringBuffer();
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			int tempvalue = rand.nextInt(10);
			buffer.append(tempvalue);
		}
		return buffer.toString();
	}

	/**
	 * 字符串转换成十六进制值
	 * 
	 * @param bin
	 *            String 我们看到的要转换成十六进制的字符串
	 * @return
	 */
	public static String bin2hex(byte[] bs) {
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}
		return sb.toString();
	}
}
