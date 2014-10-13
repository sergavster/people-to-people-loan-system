package com.p2psys.model;

import java.io.Serializable;


public class PaymentSumModel implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -316673577463591122L;

	/**
	 * 第三方名称
	 */
	private String name;
	
	/**
	 * 充值总额
	 */
	private double account;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the account
	 */
	public double getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(double account) {
		this.account = account;
	}

}
