package com.p2psys.model.account;

public class InvestSummary {
	private double investTotal;
	private double investInterest;
	private int investTimes;
	private long user_id;
	public double getInvestTotal() {
		return investTotal;
	}
	public void setInvestTotal(double investTotal) {
		this.investTotal = investTotal;
	}
	public double getInvestInterest() {
		return investInterest;
	}
	public void setInvestInterest(double investInterest) {
		this.investInterest = investInterest;
	}
	public int getInvestTimes() {
		return investTimes;
	}
	public void setInvestTimes(int investTimes) {
		this.investTimes = investTimes;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
}
