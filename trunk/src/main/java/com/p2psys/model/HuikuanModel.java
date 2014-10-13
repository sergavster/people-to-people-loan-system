package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.Huikuan;

/**
 * 用户个人资料信息更新的Model,user和usrinfo
 * 
 
 * 
 */
public class HuikuanModel extends Huikuan implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6026604036412862478L;

	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 真实姓名
	 */
	private String realname;

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

	/**
	 * 获取realname
	 * 
	 * @return realname
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * 设置realname
	 * 
	 * @param realname 要设置的realname
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
}
