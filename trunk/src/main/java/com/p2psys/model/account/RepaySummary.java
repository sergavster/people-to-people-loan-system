package com.p2psys.model.account;

public class RepaySummary {
	private double repayTotal;
	private double repayInterest;
	private String repayTime;
	private double repayAccount;//待还金额
	private long user_id;
	public double getRepayTotal() {
		return repayTotal;
	}
	public void setRepayTotal(double repayTotal) {
		this.repayTotal = repayTotal;
	}
	public double getRepayInterest() {
		return repayInterest;
	}
	public void setRepayInterest(double repayInterest) {
		this.repayInterest = repayInterest;
	}
	public String getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public double getRepayAccount() {
		return repayAccount;
	}
	public void setRepayAccount(double repayAccount) {
		this.repayAccount = repayAccount;
	}
	
}
