package com.p2psys.payment;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;

/**
 * 
 * @ClassName: Dinpay
 * @Description: TODO(智付支付 javabean)
 
 * @date 2013-4-7 下午17：22
 * 
 */
public class Dinpay {
	/**
	 * 商户号
	 */
	private String m_id;
	/**
	 * 交易日期
	 */
	private String m_date;
	/**
	 * 订单号
	 */
	private String m_orderID;
	/**
	 * 订单金额
	 */
	private String m_amount;
	/**
	 * 支付币种
	 */
	private String m_currency;
	/**
	 * 返回路径
	 */
	private String m_url;
	/**
	 * 语言选择
	 */
	private String m_language;
	/**
	 * 消费者姓名
	 */
	private String s_name = "";
	/**
	 * 消费者住址
	 */
	private String s_address = "";
	/**
	 * 消费者邮政编码
	 */
	private String s_postcode = "";
	/**
	 * 消费者联系电话
	 */
	private String s_telephone = "";
	/**
	 * 消费者电子邮件地址
	 */
	private String s_email = "";
	/**
	 * 收货人姓名
	 */
	private String r_name = "";
	/**
	 * 收货人住址
	 */
	private String r_address = "";
	/**
	 * 收货人邮政编码
	 */
	private String r_postcode = "";
	/**
	 * 收货人联系电话
	 */
	private String r_telephone = "";
	/**
	 * 收货人电子邮件地址
	 */
	private String r_email = "";
	/**
	 * 备注
	 */
	private String m_comment = "";
	/**
	 * 支付状态
	 */
	private String state;
	/**
	 * 银行标识
	 */
	private String p_Bank="";

	private String md5Sign;

	private String orderInfo;
    public void init(Dinpay dinpay,PaymentInterface paymentInterface){
    	String weburl = Global.getValue("weburl");
		String key ="";
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			dinpay.setM_id(paymentInterface.getMerchant_id());
			dinpay.setM_url(weburl +paymentInterface.getReturn_url());
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
    } 
	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public String getM_date() {
		return m_date;
	}

	public void setM_date(String m_date) {
		this.m_date = m_date;
	}

	public String getM_orderID() {
		return m_orderID;
	}

	public void setM_orderID(String m_orderID) {
		this.m_orderID = m_orderID;
	}

	public String getM_amount() {
		return m_amount;
	}

	public void setM_amount(String m_amount) {
		this.m_amount = m_amount;
	}

	public String getM_currency() {
		return m_currency;
	}

	public void setM_currency(String m_currency) {
		this.m_currency = m_currency;
	}

	public String getM_url() {
		return m_url;
	}

	public void setM_url(String m_url) {
		this.m_url = m_url;
	}

	public String getM_language() {
		return m_language;
	}

	public void setM_language(String m_language) {
		this.m_language = m_language;
	}

	public String getS_name() {
		return s_name;
	}

	public void setS_name(String s_name) {
		this.s_name = s_name;
	}

	public String getS_address() {
		return s_address;
	}

	public void setS_address(String s_address) {
		this.s_address = s_address;
	}

	public String getS_postcode() {
		return s_postcode;
	}

	public void setS_postcode(String s_postcode) {
		this.s_postcode = s_postcode;
	}

	public String getS_telephone() {
		return s_telephone;
	}

	public void setS_telephone(String s_telephone) {
		this.s_telephone = s_telephone;
	}

	public String getS_email() {
		return s_email;
	}

	public void setS_email(String s_email) {
		this.s_email = s_email;
	}

	public String getR_name() {
		return r_name;
	}

	public void setR_name(String r_name) {
		this.r_name = r_name;
	}

	public String getR_address() {
		return r_address;
	}

	public void setR_address(String r_address) {
		this.r_address = r_address;
	}

	public String getR_postcode() {
		return r_postcode;
	}

	public void setR_postcode(String r_postcode) {
		this.r_postcode = r_postcode;
	}

	public String getR_telephone() {
		return r_telephone;
	}

	public void setR_telephone(String r_telephone) {
		this.r_telephone = r_telephone;
	}

	public String getR_email() {
		return r_email;
	}

	public void setR_email(String r_email) {
		this.r_email = r_email;
	}

	public String getM_comment() {
		return m_comment;
	}

	public void setM_comment(String m_comment) {
		this.m_comment = m_comment;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getP_Bank() {
		return p_Bank;
	}

	public void setP_Bank(String p_Bank) {
		this.p_Bank = p_Bank;
	}

	public String getMd5Sign() {
		return md5Sign;
	}

	public void setMd5Sign(String md5Sign) {
		this.md5Sign = md5Sign;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

}
