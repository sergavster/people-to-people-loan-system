package com.p2psys.treasure.model;

import com.p2psys.treasure.domain.Treasure;

/**
 * 理财宝信息Model
 
 * @version 1.0
 * @since 2013-11-28
 */
public class TreasureModel extends Treasure {

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
