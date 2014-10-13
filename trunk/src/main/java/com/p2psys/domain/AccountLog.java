package com.p2psys.domain;

import java.io.Serializable;

/**
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class AccountLog implements Serializable {

	private static final long serialVersionUID = 220564258543158764L;
	private long id;
	private long user_id;
	private String type;
	private double total;
	private double money;
	private double use_money;
	private double no_use_money;
	private double collection;
	private long to_user;
	private String remark;
	private String addtime;
	private String addip;
	
	public AccountLog() {
		super();
	}
	
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public AccountLog(long user_id, String type, long to_user,String addtime,String addip) {
		super();
		this.user_id = user_id;
		this.type = type;
		this.to_user = to_user;
		this.addtime=addtime;
		this.addip=addip;
	}


	public AccountLog(long user_id, String type, double total, double money,
			double use_money, double no_use_money, double collection,
			long to_user, String remark, String addtime, String addip) {
		super();
		this.user_id = user_id;
		this.type = type;
		this.total = total;
		this.money = money;
		this.use_money = use_money;
		this.no_use_money = no_use_money;
		this.collection = collection;
		this.to_user = to_user;
		this.remark = remark;
		this.addtime = addtime;
		this.addip = addip;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getUse_money() {
		return use_money;
	}
	public void setUse_money(double use_money) {
		this.use_money = use_money;
	}
	public double getNo_use_money() {
		return no_use_money;
	}
	public void setNo_use_money(double no_use_money) {
		this.no_use_money = no_use_money;
	}
	public double getCollection() {
		return collection;
	}
	public void setCollection(double collection) {
		this.collection = collection;
	}
	public long getTo_user() {
		return to_user;
	}
	public void setTo_user(long to_user) {
		this.to_user = to_user;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	
}
