package com.p2psys.model;

public class Newpay implements java.io.Serializable {

	private static final long serialVersionUID = -6911724551127602185L;
	private String new_repay_time;
	private double new_repay_account;
	public String getNew_repay_time() {
		return new_repay_time;
	}
	public void setNew_repay_time(String new_repay_time) {
		this.new_repay_time = new_repay_time;
	}
	public double getNew_repay_account() {
		return new_repay_account;
	}
	public void setNew_repay_account(double new_repay_account) {
		this.new_repay_account = new_repay_account;
	}
	
}
