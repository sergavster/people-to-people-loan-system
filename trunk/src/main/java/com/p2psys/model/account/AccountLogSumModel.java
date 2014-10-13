package com.p2psys.model.account;

import com.p2psys.domain.AccountLog;

public class AccountLogSumModel extends AccountLog{
	
	private static final long serialVersionUID = -3239716062224960269L;
	private String typename;
	private double sum;
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	
}
