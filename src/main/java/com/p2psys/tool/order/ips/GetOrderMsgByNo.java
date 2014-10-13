package com.p2psys.tool.order.ips;


import jxl.common.Logger;

import com.p2psys.domain.PaymentInterface;
/**
 * 环迅接口通过银行订单号对账
 
 * 2013-11-15
 */
public class GetOrderMsgByNo extends OrderRecord{
	private static Logger logger=Logger.getLogger(GetOrderMsgByNo.class); 
    private String MerCode;
    private String sign;
    private String TradeType;
    private String StartNo;
    private String EndNo;
    public int Page;
	private int Max; 
	public String orderByNo(GetOrderMsgByNo getOrderByNo,PaymentInterface paymentInterface){
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
		String submitUrl=paymentInterface.getOrderInquireUrl()+"/GetOrderByNo";
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
		String sign=paymentInterface.getMerchant_id()+getOrderByNo.getFlag()+
    			getOrderByNo.getTradeType()+getOrderByNo.getStartNo()+
    			    getOrderByNo.getEndNo()+getOrderByNo.getPage()+
    			    getOrderByNo.getMax()+paymentInterface.getKey();
    	cryptix.jce.provider.MD5 b = new cryptix.jce.provider.MD5();
    	String SignMD5 = b.toMD5(sign).toLowerCase();
    	getOrderByNo.setSign(SignMD5);
    	StringBuffer aa=new StringBuffer();
    	aa.append("MerCode="+paymentInterface.getMerchant_id()+"&")
    	  .append("Sign="+getOrderByNo.getSign()+"&")
    	  .append("Flag="+getOrderByNo.getFlag()+"&")
    	  .append("TradeType="+getOrderByNo.getTradeType()+"&")
    	  .append("StartNo="+getOrderByNo.getStartNo()+"&")
    	  .append("EndNo="+getOrderByNo.getEndNo()+"&")
    	  .append("Page="+getOrderByNo.getPage()+"&")
    	  .append("Max="+getOrderByNo.getMax()+"");
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
	
	public String getStartNo() {
		return StartNo;
	}
	public void setStartNo(String startNo) {
		StartNo = startNo;
	}
	public String getEndNo() {
		return EndNo;
	}
	public void setEndNo(String endNo) {
		EndNo = endNo;
	}
	public GetOrderMsgByNo() {
		super();
	}
	public GetOrderMsgByNo(String merCode, String sign, String tradeType,
			String startNo, String endNo, int page, int max) {
		super();
		MerCode = merCode;
		this.sign = sign;
		TradeType = tradeType;
		StartNo = startNo;
		EndNo = endNo;
		Page = page;
		Max = max;
	}
}
