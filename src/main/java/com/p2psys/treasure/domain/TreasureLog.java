package com.p2psys.treasure.domain;

import java.io.Serializable;

/**
 * 理财宝日志实体
 
 * @version 1.0
 * @since 2013-11-27
 */
public class TreasureLog implements Serializable {
	
	private static final long serialVersionUID = -0L;
	
	//主键ID 
	private long id;
	
	//会员ID 
	private long user_id;
	
	//理财宝ID 
	private long treasure_log;
	
	//可用金额 
	private double use_money;
	
	//操作金额 
	private double money;
	
	//类型 
	private String type;
	
	//添加时间 
	private long add_time;
	
	//备注 
	private String remark;

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

	public long getTreasure_log() {
		return treasure_log;
	}

	public void setTreasure_log(long treasure_log) {
		this.treasure_log = treasure_log;
	}

	public double getUse_money() {
		return use_money;
	}

	public void setUse_money(double use_money) {
		this.use_money = use_money;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getAdd_time() {
		return add_time;
	}

	public void setAdd_time(long add_time) {
		this.add_time = add_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
