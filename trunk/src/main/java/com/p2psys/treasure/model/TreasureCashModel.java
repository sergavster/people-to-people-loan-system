package com.p2psys.treasure.model;

import com.p2psys.treasure.domain.TreasureCash;

public class TreasureCashModel extends TreasureCash {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2696845149146174291L;

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
