package com.p2psys.payment;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;


/**
 * 网银在线
 
 * 2013-07-03
 */
public class ChinaBankPay  {
	private final static Logger logger = Logger.getLogger(ChinaBankPay.class);

	// 商户编号
	private String v_mid;
	// 订单编号
	private String v_oid;
	// 订单总金额
	private String v_amount;
	// 币种（CNY为人民币）
	private String v_moneytype;
	// url地址
	private String v_url;
	// MD5校验码
	private String v_md5info;
	// 支付状态（20表示支付成功，30表示支付失败）
	private String v_pstatus;
	// 支付结果信息（支付完成、支付失败）
	private String v_pstring;
	// 支付方式
	private String v_pmode;
	// 订单MD5校验码
	private String v_md5str;
	// 备注1
	private String remark1;
	// 备注2
	private String remark2;
	//银行id
	private String pmode_id;
	public void init(ChinaBankPay cbp,PaymentInterface paymentInterface){
		String weburl = Global.getValue("weburl");
		String v_mid="";
		String key="";
		String returnurl="";
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			 v_mid =paymentInterface.getMerchant_id();
			 key = paymentInterface.getKey();
			 returnurl = weburl + paymentInterface.getReturn_url();
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		logger.debug("回调地址：" + returnurl);
		cbp.setV_mid(v_mid);
		cbp.setV_url(returnurl);
	}
	public String getPmode_id() {
		return pmode_id;
	}
	public void setPmode_id(String pmode_id) {
		this.pmode_id = pmode_id;
	}
	public String getV_mid() {
		return v_mid;
	}
	public void setV_mid(String v_mid) {
		this.v_mid = v_mid;
	}
	public String getV_oid() {
		return v_oid;
	}
	public void setV_oid(String v_oid) {
		this.v_oid = v_oid;
	}
	public String getV_amount() {
		return v_amount;
	}
	public void setV_amount(String v_amount) {
		this.v_amount = v_amount;
	}
	public String getV_moneytype() {
		return v_moneytype;
	}
	public void setV_moneytype(String v_moneytype) {
		this.v_moneytype = v_moneytype;
	}
	public String getV_url() {
		return v_url;
	}
	public void setV_url(String v_url) {
		this.v_url = v_url;
	}
	public String getV_md5info() {
		return v_md5info;
	}
	public void setV_md5info(String v_md5info) {
		this.v_md5info = v_md5info;
	}
	public String getV_pstatus() {
		return v_pstatus;
	}
	public void setV_pstatus(String v_pstatus) {
		this.v_pstatus = v_pstatus;
	}
	public String getV_pstring() {
		return v_pstring;
	}
	public void setV_pstring(String v_pstring) {
		this.v_pstring = v_pstring;
	}
	public String getV_pmode() {
		return v_pmode;
	}
	public void setV_pmode(String v_pmode) {
		this.v_pmode = v_pmode;
	}
	public String getV_md5str() {
		return v_md5str;
	}
	public void setV_md5str(String v_md5str) {
		this.v_md5str = v_md5str;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
}
