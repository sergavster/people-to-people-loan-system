package com.p2psys.tool.order.ips;


import jxl.common.Logger;

import com.p2psys.domain.PaymentInterface;
/**
 * 环迅接口通过时间查询对账
 
 * 2013-11-15
 */
public class GetOrderByTime extends OrderRecord{
	private static Logger logger=Logger.getLogger(GetOrderByTime.class); 
    private String MerCode;
    private String sign;
    private String TradeType;
    private String StartTime;
    private String EndTime;
    public int Page;
	private int Max; 
    public GetOrderByTime(String merCode, String sign, String tradeType,
			String startTime, String endTime, int page, int max) {
		super();
		MerCode = merCode;
		this.sign = sign;
		TradeType = tradeType;
		StartTime = startTime;
		EndTime = endTime;
		Page = page;
		Max = max;
	}
	public String orderByTime(GetOrderByTime getOrderByTime,PaymentInterface paymentInterface){
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
		String submitUrl=paymentInterface.getOrderInquireUrl()+"/GetOrderByTime";
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
		String sign=paymentInterface.getMerchant_id()+getOrderByTime.getFlag()+
    			    getOrderByTime.getTradeType()+getOrderByTime.getStartTime()+
    			    getOrderByTime.getEndTime()+getOrderByTime.getPage()+
    			    getOrderByTime.getMax()+paymentInterface.getKey();
    	cryptix.jce.provider.MD5 b = new cryptix.jce.provider.MD5();
    	String SignMD5 = b.toMD5(sign).toLowerCase();
    	getOrderByTime.setSign(SignMD5);
    	StringBuffer aa=new StringBuffer();
    	aa.append("MerCode="+paymentInterface.getMerchant_id()+"&")
    	  .append("Sign="+getOrderByTime.getSign()+"&")
    	  .append("Flag="+getOrderByTime.getFlag()+"&")
    	  .append("TradeType="+getOrderByTime.getTradeType()+"&")
    	  .append("StartTime="+getOrderByTime.getStartTime()+"&")
    	  .append("EndTime="+getOrderByTime.getEndTime()+"&")
    	  .append("Page="+getOrderByTime.getPage()+"&")
    	  .append("Max="+getOrderByTime.getMax()+"");
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
	public GetOrderByTime() {
		super();
	}
    
}
