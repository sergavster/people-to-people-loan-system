package com.p2psys.model;

import java.io.Serializable;

/**
 * model
 * 
 
 * @version 1.0
 * @since 2013-8-22
 */
public class UpfilesExcelModel implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4087862753805439641L;

	/**
	 * 用户ID
	 */
	private String user_id;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 金额
	 */
	private String money;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 状态
	 */
	private String status;

	/**
	 * 获取user_id
	 * 
	 * @return user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * 设置user_id
	 * 
	 * @param user_id 要设置的user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

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
	 * 获取money
	 * 
	 * @return money
	 */
	public String getMoney() {
		return money;
	}

	/**
	 * 设置money
	 * 
	 * @param money 要设置的money
	 */
	public void setMoney(String money) {
		this.money = money;
	}

	/**
	 * 获取remark
	 * 
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置remark
	 * 
	 * @param remark 要设置的remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取status
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置status
	 * 
	 * @param status 要设置的status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
