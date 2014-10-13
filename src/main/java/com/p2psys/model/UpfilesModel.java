package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.Upfiles;

/**
 * model
 * 
 
 * @version 1.0
 * @since 2013-8-22
 */
public class UpfilesModel extends Upfiles implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5440441194298359680L;
	
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 获取username
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置username
	 * 
	 * @param username 要设置的username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
