package com.p2psys.treasure.model;

import com.p2psys.treasure.domain.TreasureUser;

public class TreasureUserModel  extends TreasureUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 923414036125031167L;
	
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
