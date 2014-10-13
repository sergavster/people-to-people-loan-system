package com.p2psys.fund.model;

import com.p2psys.fund.domain.Fund;

/**
 * 基金信托Model
 
 * @version 1.0
 * @since 2013-10-24
 */
public class FundModel extends Fund {

	//会员名
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
