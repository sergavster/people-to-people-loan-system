package com.p2psys.payment;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;

// v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 start
//新增类
//v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 end
/**
 * 双乾支付
 * 
 
 * @version 1.0
 * @since 2013-10-21
 */
public class Epay {

	/**
	 * 该笔订单总金额(元,精确到小数两位)
	 */
	private String amount;

	/**
	 * 商户网站产生的订单号
	 */
	private String billNo;

	/**
	 * 商户ID
	 */
	private String merNo;

	/**
	 * 商户前台页面跳转通知URL
	 */
	private String returnURL;

	/**
	 * 参数加密串
	 */
	private String md5info;

	/**
	 * 支付方式. CSPAY:网银支付 ; NCPAY:无卡支付;UNION:银联无卡网银合并
	 */
	private String payType;

	/**
	 * 服务端后台支付状态通知
	 */
	private String notifyURL;

	/**
	 * 支付银行类型. ICBC (空值为跳转到银联支付)
	 */
	private String paymentType;

	/**
	 * 商户备注信息
	 */
	private String merRemark;

	/**
	 * 商户备注信息
	 */
	private String products;

	/**
	 * 数据初始化
	 * 
	 * @param epay 双乾支付实体类
	 */
	public void init(Epay epay,PaymentInterface paymentInterface) {
	  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			// 必须设定的参数
			epay.setMerNo(paymentInterface.getMerchant_id());
			epay.setPayType(paymentInterface.getPay_style());
			epay.setNotifyURL(Global.getValue("weburl") + paymentInterface.getNotice_url());
			epay.setReturnURL(Global.getValue("weburl") + paymentInterface.getReturn_url());
    		// 可选参数
    		epay.setMerRemark(paymentInterface.getOrder_description());
    	}
        epay.setProducts(Global.getValue("webid"));
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
	}

	/**
	 * 获取该笔订单总金额
	 * 
	 * @return 该笔订单总金额
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * 设置该笔订单总金额
	 * 
	 * @param amount 要设置的该笔订单总金额
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * 获取商户网站产生的订单号
	 * 
	 * @return 商户网站产生的订单号
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * 设置商户网站产生的订单号
	 * 
	 * @param billNo 要设置的商户网站产生的订单号
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * 获取商户ID
	 * 
	 * @return 商户ID
	 */
	public String getMerNo() {
		return merNo;
	}

	/**
	 * 设置商户ID
	 * 
	 * @param merNo 要设置的商户ID
	 */
	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}

	/**
	 * 获取商户前台页面跳转通知URL
	 * 
	 * @return 商户前台页面跳转通知URL
	 */
	public String getReturnURL() {
		return returnURL;
	}

	/**
	 * 设置商户前台页面跳转通知URL
	 * 
	 * @param returnURL 要设置的商户前台页面跳转通知URL
	 */
	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	/**
	 * 获取参数加密串
	 * 
	 * @return 参数加密串
	 */
	public String getMd5info() {
		return md5info;
	}

	/**
	 * 设置参数加密串
	 * 
	 * @param md5info 要设置的参数加密串
	 */
	public void setMd5info(String md5info) {
		this.md5info = md5info;
	}

	/**
	 * 获取支付方式
	 * 
	 * @return 支付方式
	 */
	public String getPayType() {
		return payType;
	}

	/**
	 * 设置支付方式
	 * 
	 * @param payType 要设置的支付方式
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}

	/**
	 * 获取服务端后台支付状态通知
	 * 
	 * @return 服务端后台支付状态通知
	 */
	public String getNotifyURL() {
		return notifyURL;
	}

	/**
	 * 设置服务端后台支付状态通知
	 * 
	 * @param notifyURL 要设置的服务端后台支付状态通知
	 */
	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	/**
	 * 获取支付银行类型
	 * 
	 * @return 支付银行类型
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * 设置支付银行类型
	 * 
	 * @param paymentType 要设置的支付银行类型
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * 获取商户备注信息
	 * 
	 * @return 商户备注信息
	 */
	public String getMerRemark() {
		return merRemark;
	}

	/**
	 * 设置商户备注信息
	 * 
	 * @param merRemark 要设置的商户备注信息
	 */
	public void setMerRemark(String merRemark) {
		this.merRemark = merRemark;
	}

	/**
	 * 获取商户备注信息
	 * 
	 * @return 商户备注信息
	 */
	public String getProducts() {
		return products;
	}

	/**
	 * 设置商户备注信息
	 * 
	 * @param products 要设置的商户备注信息
	 */
	public void setProducts(String products) {
		this.products = products;
	}

}
