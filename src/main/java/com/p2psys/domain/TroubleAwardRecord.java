package com.p2psys.domain;

import java.io.Serializable;

import com.p2psys.util.DateUtils;

/**
 * 
 * TODO 急难基金 奖励收支实体
 * 
 
 * @version 1.0
 * @since 2013-7-25
 */
public class TroubleAwardRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	// 会员ID
	private long user_id;
	
	// 奖励金额
	private double money;
	
	private long status;
	
	private String addtime;

	// 提取现有时间，set addtime
	public TroubleAwardRecord() {
		this.setAddtime(DateUtils.getNowTimeStr());
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
}
