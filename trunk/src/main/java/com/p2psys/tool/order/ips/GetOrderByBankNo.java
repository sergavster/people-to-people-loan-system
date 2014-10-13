package com.p2psys.tool.order.ips;


import jxl.common.Logger;

import com.p2psys.domain.PaymentInterface;
/**
 * 环迅接口通过银行订单号对账
 
 * 2013-11-15
 */
public class GetOrderByBankNo extends OrderRecord{
	private static Logger logger=Logger.getLogger(GetOrderByBankNo.class); 
    private String MerCode;
    private String sign;
    private String TradeType;
    private String StartTime;
    private String EndTime;
    public int Page;
    private String BankNo;
	private int Max; 
	public String orderByBankNo(GetOrderByBankNo getOrderByBankNo,PaymentInterface paymentInterface){
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
		String submitUrl=paymentInterface.getOrderInquireUrl()+"/GetOrderByBankNo";
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
		String sign=paymentInterface.getMerchant_id()+getOrderByBankNo.getFlag()+
    			getOrderByBankNo.getTradeType()+getOrderByBankNo.getBankNo()+
    			    getOrderByBankNo.getPage()+
    			    getOrderByBankNo.getMax()+paymentInterface.getKey();
    	cryptix.jce.provider.MD5 b = new cryptix.jce.provider.MD5();
    	String SignMD5 = b.toMD5(sign).toLowerCase();
    	getOrderByBankNo.setSign(SignMD5);
    	StringBuffer aa=new StringBuffer();
    	aa.append("MerCode="+paymentInterface.getMerchant_id()+"&")
    	  .append("Sign="+getOrderByBankNo.getSign()+"&")
    	  .append("Flag="+getOrderByBankNo.getFlag()+"&")
    	  .append("TradeType="+getOrderByBankNo.getTradeType()+"&")
    	  .append("StartTime="+getOrderByBankNo.getStartTime()+"&")
    	  .append("EndTime="+getOrderByBankNo.getEndTime()+"&")
    	  .append("Page="+getOrderByBankNo.getPage()+"&")
    	  .append("BankNo="+getOrderByBankNo.getBankNo()+"&")
    	  .append("Max="+getOrderByBankNo.getMax()+"");
    	String url=aa.toString();
    	submitUrl+="?"+url;
    	return submitUrl;
    }
	public int getPage() {
		return Page;
	}
	public void setPage(int page) {
		Page = page;
	}
	public int getMax() {
		return Max;
	}
	public void setMax(int max) {
		Max = max;
	}
	public String getMerCode() {
		return MerCode;
	}
	public void setMerCode(String merCode) {
		MerCode = merCode;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTradeType() {
		return TradeType;
	}
	public void setTradeType(String tradeType) {
		TradeType = tradeType;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	public String getBankNo() {
		return BankNo;
	}
	public void setBankNo(String bankNo) {
		BankNo = bankNo;
	}
	public GetOrderByBankNo() {
		super();
	}
	public GetOrderByBankNo(String merCode, String sign, String tradeType,
			String startTime, String endTime, int page, String bankNo, int max) {
		super();
		MerCode = merCode;
		this.sign = sign;
		TradeType = tradeType;
		StartTime = startTime;
		EndTime = endTime;
		Page = page;
		BankNo = bankNo;
		Max = max;
	}
    
}
