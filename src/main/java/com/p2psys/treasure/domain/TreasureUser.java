package com.p2psys.treasure.domain;

import java.io.Serializable;

/**
 * 用户理财宝信息
 
 * @version 1.0
 * @since 2013-11-27
 */
public class TreasureUser implements Serializable {

	private static final long serialVersionUID = -0L;
	
	//主键ID 
	private long id;
	
	//会员ID 
	private long user_id;
	
	//总额 
	private double total;
	
	//获得利息总额 
	private double interest_total;
	
	//可用金额 
	private double use_moeny;
	
	//添加时间 
	private long add_time;

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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getInterest_total() {
		return interest_total;
	}

	public void setInterest_total(double interest_total) {
		this.interest_total = interest_total;
	}

	public double getUse_moeny() {
		return use_moeny;
	}

	public void setUse_moeny(double use_moeny) {
		this.use_moeny = use_moeny;
	}

	public long getAdd_time() {
		return add_time;
	}

	public void setAdd_time(long add_time) {
		this.add_time = add_time;
	}
	
}
