package com.p2psys.model.account;

public class CollectSummary {
	private String collectTime;
	private double collectTotal;
	private double collectInterest;
	private double collectMoney;
	private long user_id;
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public double getCollectTotal() {
		return collectTotal;
	}
	public void setCollectTotal(double collectTotal) {
		this.collectTotal = collectTotal;
	}
	public double getCollectInterest() {
		return collectInterest;
	}
	public void setCollectInterest(double collectInterest) {
		this.collectInterest = collectInterest;
	}
	public double getCollectMoney() {
		return collectMoney;
	}
	public void setCollectMoney(double collectMoney) {
		this.collectMoney = collectMoney;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
}
