package com.p2psys.payment;

import com.p2psys.context.Global;
import com.p2psys.domain.AccountRecharge;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.model.paymentInterface.BaofooOrder;
import com.p2psys.tool.coder.MD5;
import com.p2psys.util.HttpUtils;

/**
 * 
* @ClassName: BaoFooPay
* @Description: TODO(宝付支付 javabean)

* @date 2013-3-17 上午8:31:54
*
*/

public class BaoFooPay{
	
	/**选择支付方式  这里默认使用银联  编号 是：1080
	 * 
	 */
    private String payID;
    /**
     * 分配的商户号
     */
    private String merchantID;
    /**
     * 交易时间
     */
    private String tradeDate;
    /**
     * 生成商户流水号
     */
    private String transID;
    /**
     * 订单金额
     */
    private String orderMoney;
    /**
     * 商品名称
     */
    private String productName = "";
    /**
     * 商品数量  默认为 1  便于计算。
     */
    private String amount = "1";
    /**
     * 产品logo    默认为空  必要字段
     */
    private String productLogo = "";
    /**
     * 支付用户名: 默认为空  必要字段
     */
    private String username = "";
    /**
     *  Email 默认为空  必要字段
     */
    private String email = "";
    /**
     * Mobile 默认为空  必要字段
     */
    private String mobile = "";
    /**
     * 订单附加消息: 默认为空  必要字段
     */
    private String additionalInfo = "";
    /**
     * 页面跳转地址
     */
    private String merchant_url;
    /**
     * 底层访问地址:
     */
    private String return_url;
    /**
     * 通知方式
     */
    private  String noticeType;
    public void init(BaoFooPay baoFoo,PaymentInterface paymentInterface){
		String weburl = Global.getValue("weburl");
    	String Md5key="";
    	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			// 页面跳转地址
			baoFoo.setMerchant_url(weburl +paymentInterface.getNotice_url());
			// 分配的商户号
			baoFoo.setMerchantID(paymentInterface.getMerchant_id());
			// 底层通知 url
			baoFoo.setReturn_url(weburl +paymentInterface.getReturn_url());
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
    }
    // v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 start 
    public BaofooOrder getOrder(PaymentInterface paymentInterface,AccountRecharge r){
    	String md5key=paymentInterface.getKey();
		String MerchantID=paymentInterface.getMerchant_id();
			String TransID=r.getTrade_no();
			String Md5Sign=MerchantID+TransID+md5key;
			MD5 md5 = new MD5();
		    Md5Sign = md5.getMD5ofStr(Md5Sign);// 计算MD5值
		  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
			String url=paymentInterface.getOrderInquireUrl();
			//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
			StringBuffer sb=new StringBuffer();
			sb.append(url).append("?MerchantID=").append(MerchantID)
			  .append("&TransID=").append(TransID)
			  .append("&Md5Sign=").append(Md5Sign);
			String submitUrl=sb.toString();
			String s = HttpUtils.getHttpResponse(submitUrl);
			BaofooOrder baofooOrder=new BaofooOrder();
			String ss=s.replace('|', ',');
			String[] returnParamter=ss.split(",");
			baofooOrder.setMerchantID(returnParamter[0]);
			baofooOrder.setTransID(returnParamter[1]);
			baofooOrder.setCheckResult(returnParamter[2]);
			baofooOrder.setFactMoney(returnParamter[3]);
			baofooOrder.setSuccTime(returnParamter[4]);
			baofooOrder.setMd5Sign(returnParamter[5]);
			return baofooOrder;
    }
    // v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 end
    /**
     * @return
     */
    private String md5Sign;
    
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMd5Sign() {
		return this.md5Sign;
	}
	public void setMd5Sign(String md5Sign) {
		this.md5Sign = md5Sign;
	}
	public String getPayID() {
		return this.payID;
	}
	public void setPayID(String payID) {
		this.payID = payID;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getTradeDate() {
		return this.tradeDate;
	}
	public void setTradeDate(String TradeDate) {
		this.tradeDate = TradeDate;
	}
	public String getTransID() {
		return this.transID;
	}
	public void setTransID(String transID) {
		this.transID = transID;
	}
	public String getOrderMoney() {
		return this.orderMoney;
	}
	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}
	public String getProductName() {
		return this.productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductLogo() {
		return this.productLogo;
	}
	public void setProductLogo(String productLogo) {
		this.productLogo = productLogo;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return this.mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAdditionalInfo() {
		return this.additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public String getMerchant_url() {
		return this.merchant_url;
	}
	public void setMerchant_url(String merchant_url) {
		this.merchant_url = merchant_url;
	}
	public String getReturn_url() {
		return this.return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	public String getNoticeType() {
		return this.noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	} 
	
	
}
