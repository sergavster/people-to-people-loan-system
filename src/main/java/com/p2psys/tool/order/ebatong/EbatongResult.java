package com.p2psys.tool.order.ebatong;

import java.io.Serializable;
/**
 * 汇潮支付订单查询          RDPROJECT-28
 
 *
 */
public class EbatongResult implements Serializable{
	private static final long serialVersionUID = -5290637298310888840L;
   
	private String outtradeNo;  //商户订单号
	private String subject;//交易主题
	private String tradeNo;//易八通订单号
	private String tradeStatus;//交易状态   TRADE_FINISHED表示付款成功。TRADE_WAIT_PAY表示待付款。
	private String totalFee; //交易金额，单位：元
	public String getOuttradeNo() {
		return outtradeNo;
	}
	public void setOuttradeNo(String outtradeNo) {
		this.outtradeNo = outtradeNo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
}
