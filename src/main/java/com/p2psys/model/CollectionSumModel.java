package com.p2psys.model;

import java.io.Serializable;

/**
 * 本金合计、本息合计、利息合计
 * 
 
 * @version 1.0
 * @since 2013-8-29
 */
public class CollectionSumModel implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2696016012607532524L;

	/**
	 * 本息合计
	 */
	private double account;
	
	/**
	 * 本金合计
	 */
	private double capital;
	
	/**
	 * 利息合计
	 */
	private double interest;

	/**
	 * 获取account
	 * 
	 * @return account
	 */
	public double getAccount() {
		return account;
	}

	/**
	 * 设置account
	 * 
	 * @param account 要设置的account
	 */
	public void setAccount(double account) {
		this.account = account;
	}

	/**
	 * 获取capital
	 * 
	 * @return capital
	 */
	public double getCapital() {
		return capital;
	}

	/**
	 * 设置capital
	 * 
	 * @param capital 要设置的capital
	 */
	public void setCapital(double capital) {
		this.capital = capital;
	}

	/**
	 * 获取interest
	 * 
	 * @return interest
	 */
	public double getInterest() {
		return interest;
	}

	/**
	 * 设置interest
	 * 
	 * @param interest 要设置的interest
	 */
	public void setInterest(double interest) {
		this.interest = interest;
	}
	
}
