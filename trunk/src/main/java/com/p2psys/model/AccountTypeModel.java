package com.p2psys.model;

import java.io.Serializable;


public class AccountTypeModel implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -316673577463591122L;
	
	/**
	 * 奖励总额
	 */
	private double account;
	
	/**
	 * 利息总额
	 */
	private double interest;
	
	/**
	 * 奖励总额
	 */
	private double award;
	
	/**
	 * 标种
	 */
	private String type;

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

	/**
	 * @return the interest
	 */
	public double getInterest() {
		return interest;
	}

	/**
	 * @param interest the interest to set
	 */
	public void setInterest(double interest) {
		this.interest = interest;
	}

	/**
	 * @return the award
	 */
	public double getAward() {
		return award;
	}

	/**
	 * @param award the award to set
	 */
	public void setAward(double award) {
		this.award = award;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
