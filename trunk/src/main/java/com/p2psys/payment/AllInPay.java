package com.p2psys.payment;

import com.allinpay.ets.client.RequestOrder;
import com.allinpay.ets.client.SecurityUtil;
import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;

public class AllInPay {
	//	表单参数
	private String inputCharset;		//	字符集			1 代表UTF-8 2 代表GBK 3 代表GB2312 默认值为1 
	private String pickupUrl;			//	客 户 的 取 货 地 址 。
	private String receiveUrl;			//	服务器接受支付结果的后台地址  通知商户网站支付结果的url 地址。
	private String version;				//	网关接收支付  		固定值：v1.0 
	private String language; 			//	网关页面显示	 	固定值：1 1代表中文显示 
	private String signType;  			//	签名类型		固定值：1 1代表证书签名验签方式 

	//	买卖双方信息参数 
	private String merchantId;			//	本参数用来指定接收受理商户号，由通联分配 
	private String payerName;			//	付款人姓名		当payType为3、issuerId为telpshx“直连模式”时，该值不可空，为办理银行卡时的所使用的姓名 
	private String payerEmail;			//	付款人邮件联系方式 
	private String payerTelephone;		//	付款人电话联系方式 	当payType为3、issuerId不为空“直连模式”时，该值不可空，为付款人支付时所使用的手机号码 
	private String payerIDCard;  		//	付款人证件号  使用通联公钥加密后进行Base64编码 当payType为3、issuerId为telpshx“直连模式”时，该值不可空 
	private String pid;  				//	合作伙伴的商 用于商户与第三方合作伙伴拓展支付业务

	//	业务参数 
	private String orderNo;				//	商户订单号		只允许使用字母、数字、- 、_,并以字母或数字开头 每商户提交的订单号，必须在当天的该商户所有交易中唯一 
	private String orderAmount;			//	商户订单金额   	以分为单位。
	private String orderCurrency;		//	订单金额类型  		 默认为人民币类型 0 
	private String orderDatetime;		//	商户订单提交时间  	数字串，一共14位  格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位] 
	private String orderExpireDatetime;	//	订单过期时间		用于支持提交订单和支付订单行为分开的支付业务，例如后台提交订单 
	private String productName;  		//	商品名称	 英文或中文字符串 
	private String productPrice;		//	商品价格	以分为单位。 
	private String productNum;  		//	商品数量	整型数字  
	private String productId;  			//	商品代码  	字母、数字或 - 、_ 的组合 用于使用产品数据中心的产品数据，或用于市场活动的优惠 
	private String productDesc;	//	商品描述  英文或中文字符串 
	private String ext1;				//	 扩展字段1
	private String ext2;				//	扩展字段2
	private String extTL;				//	业务扩展字段
	private String payType;				//	支付方式 	固定选择值：0、1、2、3、4 	0代表组合支付方式 	1代表个人网银支付 	2代表行业卡支付 	3代表电话支付 4代表企业网银支付  非直连模式，设置为0；直连模式，值为非0的固定选择值 
	private String issuerId;  			//	银行或预付卡发卡机构代码，用于指定支付使用的付款方机构。 非直连模式，值为空；直连模式，先指定payType为1或2或3或4，issuerId
	private String pan;  				//	付款人支付卡号 当payType为3、issuerId为telpshx 	“直连模式”时，该值不可空   使用通联公钥加密后进行Base64编码	（加密前请用PKCS#1进行填充） 	
	private String signMsg;				//	签名字符串   	以上所有非空参数及其值与密钥组合，经加密后生成。

	private String key;


	public String getInputCharset() {
		return inputCharset;
	}
	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}
	public String getPickupUrl() {
		return pickupUrl;
	}
	public void setPickupUrl(String pickupUrl) {
		this.pickupUrl = pickupUrl;
	}
	public String getReceiveUrl() {
		return receiveUrl;
	}
	public void setReceiveUrl(String receiveUrl) {
		this.receiveUrl = receiveUrl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerEmail() {
		return payerEmail;
	}
	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}
	public String getPayerTelephone() {
		return payerTelephone;
	}
	public void setPayerTelephone(String payerTelephone) {
		this.payerTelephone = payerTelephone;
	}
	public String getPayerIDCard() {
		return payerIDCard;
	}
	public void setPayerIDCard(String payerIDCard) {
		this.payerIDCard = payerIDCard;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
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
	public String getOrderCurrency() {
		return orderCurrency;
	}
	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}
	public String getOrderDatetime() {
		return orderDatetime;
	}
	public void setOrderDatetime(String orderDatetime) {
		this.orderDatetime = orderDatetime;
	}
	public String getOrderExpireDatetime() {
		return orderExpireDatetime;
	}
	public void setOrderExpireDatetime(String orderExpireDatetime) {
		this.orderExpireDatetime = orderExpireDatetime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExtTL() {
		return extTL;
	}
	public void setExtTL(String extTL) {
		this.extTL = extTL;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getIssuerId() {
		return issuerId;
	}
	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getSignMsg() {
		return signMsg;
	}
	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}



	public void init(AllInPay tpay,PaymentInterface paymentInterface){
		String weburl=Global.getValue("weburl");
		this.inputCharset = "1";
		this.version = "v1.0";
		this.signType = "1";
		this.language = "1"; 
		this.orderCurrency = "0";
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			tpay.setKey(paymentInterface.getKey());
			tpay.setPickupUrl(weburl+paymentInterface.getNotice_url());
			tpay.setReceiveUrl(weburl+paymentInterface.getReturn_url());
			tpay.setMerchantId(paymentInterface.getMerchant_id());		
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	}


	public String getSignMsg(AllInPay tlPay,PaymentInterface paymentInterface){
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(null!=payerIDCard&&!"".equals(payerIDCard)){
			try{
			    if(paymentInterface!=null){
				   payerIDCard = SecurityUtil.encryptByPublicKey(paymentInterface.getCert_position(), payerIDCard);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(null!=pan&&!"".equals(pan)){
			try{
			    if(paymentInterface!=null){
				   pan = SecurityUtil.encryptByPublicKey(paymentInterface.getCert_position(), pan);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		//构造订单请求对象，生成signMsg。
		RequestOrder requestOrder = new RequestOrder();
		if(null!=inputCharset&&!"".equals(inputCharset)){
			requestOrder.setInputCharset(Integer.parseInt(inputCharset));
		}
		requestOrder.setPickupUrl(pickupUrl);
		requestOrder.setReceiveUrl(receiveUrl); 
		requestOrder.setVersion(version);
		if(null!=language&&!"".equals(language)){
			requestOrder.setLanguage(Integer.parseInt(language));
		}
		requestOrder.setSignType(Integer.parseInt(signType));
		requestOrder.setPayType(Integer.parseInt(payType));
		requestOrder.setIssuerId(issuerId);
		requestOrder.setMerchantId(merchantId);
		requestOrder.setPayerName(payerName);
		requestOrder.setPayerEmail(payerEmail);
		requestOrder.setPayerTelephone(payerTelephone);
		requestOrder.setPayerIDCard(payerIDCard);
		requestOrder.setPid(pid);
		requestOrder.setOrderNo(orderNo);
		requestOrder.setOrderAmount(Long.parseLong(orderAmount));
		requestOrder.setOrderCurrency(orderCurrency);
		requestOrder.setOrderDatetime(orderDatetime);
		requestOrder.setOrderExpireDatetime(orderExpireDatetime);
		requestOrder.setProductName(productName);
		if(null!=productPrice&&!"".equals(productPrice)){
			requestOrder.setProductPrice(Long.parseLong(productPrice));
		}
		if(null!=productNum&&!"".equals(productNum)){
			requestOrder.setProductNum(Integer.parseInt(productNum));
		}	
		requestOrder.setProductId(productId);
		requestOrder.setProductDesc(productDesc);
		requestOrder.setExt1(ext1);
		requestOrder.setExt2(ext2);
		requestOrder.setPan(pan);
		requestOrder.setKey(key); //key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。

		String strSignMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。

		return strSignMsg;
	}


}
