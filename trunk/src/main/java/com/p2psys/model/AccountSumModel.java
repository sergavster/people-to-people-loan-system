package com.p2psys.model;

import com.p2psys.domain.AccountSum;

/**
 * 资金合计model类
 
 * @version 1.0
 * @since 2013-9-6
 */
public class AccountSumModel extends AccountSum {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3514497272111606181L;
	
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

}
