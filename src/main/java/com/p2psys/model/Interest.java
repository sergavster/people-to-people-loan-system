package com.p2psys.model;

import java.io.Serializable;

public class Interest implements Serializable {

	private static final long serialVersionUID = -8468307448739140269L;

	private int status;
	private double total_repay_account;
	private double total_interest_account;
	private double total_capital_account;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getTotal_repay_account() {
		return total_repay_account;
	}
	public void setTotal_repay_account(double total_repay_account) {
		this.total_repay_account = total_repay_account;
	}
	public double getTotal_interest_account() {
		return total_interest_account;
	}
	public void setTotal_interest_account(double total_interest_account) {
		this.total_interest_account = total_interest_account;
	}
	public double getTotal_capital_account() {
		return total_capital_account;
	}
	public void setTotal_capital_account(double total_capital_account) {
		this.total_capital_account = total_capital_account;
	}

	
}
