package com.p2psys.tool.order.ips;

import jxl.common.Logger;
/**
 * 环迅返回部分结果
 
 * 2013-11-15
 */
public class OrderRecord {
	private static Logger logger=Logger.getLogger(OrderRecord.class); 
	private String OrderNo; //商户订单号
	private String TPSOrderNo; //IPS订单号
	private String Trd_Code; //交易代码
	private String Cr_Code; //交易币种
	private double Amount; //交易金额
	private String MerchantOrderTime; //商户时间 yyyymmdd
	private String IPSOrderTime; //IPS订单时间 yyyyMMddHHmmss
	private int Flag=3; //成功标志 2:失败1:成功3:所有
	private String Attach; //订单备注信息
	private String Sign; //订单签名
	private String IPSOrderNo;
	//Sign=Md5(OrderNo: +IPSOrderNo +Trd_Code +Cr_Code +Amount +MerchantOrderTime +IPSOrderTime +Flag +MerCert://商户在IPS的内部证书)
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public String getTPSOrderNo() {
		return TPSOrderNo;
	}
	public void setTPSOrderNo(String tPSOrderNo) {
		TPSOrderNo = tPSOrderNo;
	}
	public String getTrd_Code() {
		return Trd_Code;
	}
	public void setTrd_Code(String trd_Code) {
		Trd_Code = trd_Code;
	}
	public String getCr_Code() {
		return Cr_Code;
	}
	public void setCr_Code(String cr_Code) {
		Cr_Code = cr_Code;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public String getMerchantOrderTime() {
		return MerchantOrderTime;
	}
	public void setMerchantOrderTime(String merchantOrderTime) {
		MerchantOrderTime = merchantOrderTime;
	}
	public String getIPSOrderTime() {
		return IPSOrderTime;
	}
	public void setIPSOrderTime(String iPSOrderTime) {
		IPSOrderTime = iPSOrderTime;
	}
	public int getFlag() {
		return Flag;
	}
	public void setFlag(int flag) {
		Flag = flag;
	}
	public String getAttach() {
		return Attach;
	}
	public void setAttach(String attach) {
		Attach = attach;
	}
	public String getSign() {
		return Sign;
	}
	public void setSign(String sign) {
		Sign = sign;
	}
	public String getIPSOrderNo() {
		return IPSOrderNo;
	}
	public void setIPSOrderNo(String iPSOrderNo) {
		IPSOrderNo = iPSOrderNo;
	}
	
	
}
