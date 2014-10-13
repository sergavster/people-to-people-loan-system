package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.Tender;

public class ProtocolBorrowTender extends Tender implements Serializable{
	
	private static final long serialVersionUID = -7563140606006344646L;
	
	private String username;
	
	private String repay_time;
	
	private String verify_time;
	
	private String repay_account;
	
	private int time_limit;
	
	private double apr;

	private int c_order;
	
	public int getC_order() {
		return c_order;
	}

	public void setC_order(int c_order) {
		this.c_order = c_order;
	}

	public String getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRepay_time() {
		return repay_time;
	}

	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}

	public String getRepay_account() {
		return repay_account;
	}

	public void setRepay_account(String repay_account) {
		this.repay_account = repay_account;
	}

	public int getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(int time_limit) {
		this.time_limit = time_limit;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

}
