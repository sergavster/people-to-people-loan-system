package com.p2psys.model;

import com.p2psys.domain.RewardStatistics;

public class RewardStatisticsSumModel extends RewardStatistics {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8024157626597130231L;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 投资总额
	 */
	private double account;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
