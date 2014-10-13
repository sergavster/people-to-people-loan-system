package com.p2psys.domain;

import java.io.Serializable;

public class AccountSumLog implements Serializable{

	private static final long serialVersionUID = 1628341132323490065L;

	// 
	private long id;
	
	//获得资金的用户id 
	private long user_id;
	
	//提供资金的用户id 
	private long to_user_id;
	
	//变动前的金额 
	private double before_money;
	
	//变动金额 
	private double money;
	
	//变动后金额 
	private double after_money;
	
	//记录信息类型 
	private String type;
	
	//添加时间 
	private String addtime;
	
	//添加IP 
	private String addip;
	
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

	public long getTo_user_id() {
		return to_user_id;
	}

	public void setTo_user_id(long to_user_id) {
		this.to_user_id = to_user_id;
	}

	public double getBefore_money() {
		return before_money;
	}

	public void setBefore_money(double before_money) {
		this.before_money = before_money;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getAfter_money() {
		return after_money;
	}

	public void setAfter_money(double after_money) {
		this.after_money = after_money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
