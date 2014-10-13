package com.p2psys.payment;

import com.p2psys.tool.coder.MD5;

/**
 * @TODO 汇潮支付接口(java bean)
 
 * @2013-5-15
 */
public class EcpssPay {
	/**
	 * key
	 */
	private String md5Key;
	/**
	 * 商户ID
	 */
	private String merNo;
	/**
	 * 订单编号
	 */
	private String billNo;
	/**
	 * 支付金额
	 */
	private String amount;
	/**
	 * 返回路径（web页面跳转显示支付结果）
	 */
	private String returnUrl;
	/**
	 * 返回路径（后台接收支付结果）
	 */
	private String adviceUrl;
	/**
	 * 银行标识
	 */
	private String defaultBankNumber = "";
	/**
	 * 交易时间
	 */
	private String orderTime = "";
	/**
	 * 交易物品信息
	 */
	private String products = "";
	/**
	 * md5加密信息
	 */
	private String md5Info;
	public String getMerNo() {
		return merNo;
	}

	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getAdviceUrl() {
		return adviceUrl;
	}

	public void setAdviceUrl(String adviceUrl) {
		this.adviceUrl = adviceUrl;
	}

	public String getDefaultBankNumber() {
		return defaultBankNumber;
	}

	public void setDefaultBankNumber(String defaultBankNumber) {
		this.defaultBankNumber = defaultBankNumber;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getProducts() {
		return products;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getMd5Info() {
		//md5加密
		String md5Src;
		md5Src = this.merNo+this.billNo+this.amount+this.returnUrl+this.md5Key;
		MD5 md5 = new MD5();
		md5Info = md5.getMD5ofStr(md5Src);
		return md5Info;
	}

	public void setMd5Info(String md5Info) {
		this.md5Info = md5Info;
	}

}
