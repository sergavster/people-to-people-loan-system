package com.p2psys.model.account;

import com.p2psys.domain.AccountLog;

/**
 * 资金使用记录
 * 
 * @version 1.0
 * @since 2013-8-8
 */
public class AccountLogModel extends AccountLog {
	/** serialVersionUID */
	private static final long serialVersionUID = 784451181230576969L;
	/** 用户名 */
	private String username;
	/** 真实姓名 */
	private String realname;
	/** 交易对方 */
	private String to_username;
	/** 交易类型 */
	private String typename;

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

	/**
	 * 获取to_username
	 * 
	 * @return to_username
	 */
	public String getTo_username() {
		return to_username;
	}

	/**
	 * 设置to_username
	 * 
	 * @param to_username 要设置的to_username
	 */
	public void setTo_username(String to_username) {
		this.to_username = to_username;
	}

	/**
	 * 获取typename
	 * 
	 * @return typename
	 */
	public String getTypename() {
		return typename;
	}

	/**
	 * 设置typename
	 * 
	 * @param typename 要设置的typename
	 */
	public void setTypename(String typename) {
		this.typename = typename;
	}

}
