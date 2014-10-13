package com.p2psys.model;

import java.io.Serializable;

public class RankModel implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6632945396689637373L;
	private long user_id;
	private String username;
	private String realname;
	private double tenderMoney;
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
	private String tenderCount;//投标次数
	private String lastTenderTime;//最后投标时间
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
	
	/**
	 * @return the user_id
	 */
	public long getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
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
	 * @return the realname
	 */
	public String getRealname() {
		return realname;
	}
	/**
	 * @param realname the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
	/**
	 * @return the tenderMoney
	 */
	public double getTenderMoney() {
		return tenderMoney;
	}
	/**
	 * @param tenderMoney the tenderMoney to set
	 */
	public void setTenderMoney(double tenderMoney) {
		this.tenderMoney = tenderMoney;
	}
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
	public String getTenderCount() {
		return tenderCount;
	}
	public void setTenderCount(String tenderCount) {
		this.tenderCount = tenderCount;
	}
	public String getLastTenderTime() {
		return lastTenderTime;
	}
	public void setLastTenderTime(String lastTenderTime) {
		this.lastTenderTime = lastTenderTime;
	}
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
}
