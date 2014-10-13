package com.p2psys.fund.domain;

/**
 * 基金信托认购信息实体类
 
 * @version 1.0
 * @since 2013-10-22
 */
public class FundTender {

	private long id;
	private long userId;
	private long fundId;
	private double account;//认购金额
	private byte status;//状态 0：未电联用户，1：已电联
	private String remark;
	private int addTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getFundId() {
		return fundId;
	}
	public void setFundId(long fundId) {
		this.fundId = fundId;
	}
	public double getAccount() {
		return account;
	}
	public void setAccount(double account) {
		this.account = account;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getAddTime() {
		return addTime;
	}
	public void setAddTime(int addTime) {
		this.addTime = addTime;
	}
	
}
