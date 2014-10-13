package com.p2psys.model.paymentInterface;
/**
 * 宝付查询接口返回参数
 
 *
 */
public class BaofooOrder {

	private String MerchantID;  //商户号
	private String TransID;  //商户订单号
	private String CheckResult; //支付结果
    private String factMoney; //实际成功金额
    private String SuccTime; //支付完成时间
    private String Md5Sign; //Md5签名字段
	public String getMerchantID() {
		return MerchantID;
	}
	public void setMerchantID(String merchantID) {
		MerchantID = merchantID;
	}
	public String getTransID() {
		return TransID;
	}
	public void setTransID(String transID) {
		TransID = transID;
	}
	public String getCheckResult() {
		return CheckResult;
	}
	public void setCheckResult(String checkResult) {
		CheckResult = checkResult;
	}
	public String getFactMoney() {
		return factMoney;
	}
	public void setFactMoney(String factMoney) {
		this.factMoney = factMoney;
	}
	public String getSuccTime() {
		return SuccTime;
	}
	public void setSuccTime(String succTime) {
		SuccTime = succTime;
	}
	public String getMd5Sign() {
		return Md5Sign;
	}
	public void setMd5Sign(String md5Sign) {
		Md5Sign = md5Sign;
	}
    
}
