package com.p2psys.domain;

import java.io.Serializable;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class UserAmount implements Serializable {

	private static final long serialVersionUID = -840183699529388115L;
	private long id;
	private long user_id;
	private double credit;
	private double credit_use;
	private double credit_nouse;
	private double borrow_vouch;
	private double borrow_vouch_use;
	private double borrow_vouch_nouse;
	private double tender_vouch;
	private double tender_vouch_use;
	private double tender_vouch_nouse;

	public UserAmount() {
		super();
	}
	public UserAmount(long user_id, double credit, double credit_use,
			double credit_nouse, double borrow_vouch, double borrow_vouch_use,
			double borrow_vouch_nouse, double tender_vouch,
			double tender_vouch_use, double tender_vouch_nouse) {
		super();
		this.user_id = user_id;
		this.credit = credit;
		this.credit_use = credit_use;
		this.credit_nouse = credit_nouse;
		this.borrow_vouch = borrow_vouch;
		this.borrow_vouch_use = borrow_vouch_use;
		this.borrow_vouch_nouse = borrow_vouch_nouse;
		this.tender_vouch = tender_vouch;
		this.tender_vouch_use = tender_vouch_use;
		this.tender_vouch_nouse = tender_vouch_nouse;
	}  
	private String username;
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
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public double getCredit_use() {
		return credit_use;
	}
	public void setCredit_use(double credit_use) {
		this.credit_use = credit_use;
	}
	public double getCredit_nouse() {
		return credit_nouse;
	}
	public void setCredit_nouse(double credit_nouse) {
		this.credit_nouse = credit_nouse;
	}
	public double getBorrow_vouch() {
		return borrow_vouch;
	}
	public void setBorrow_vouch(double borrow_vouch) {
		this.borrow_vouch = borrow_vouch;
	}
	public double getBorrow_vouch_use() {
		return borrow_vouch_use;
	}
	public void setBorrow_vouch_use(double borrow_vouch_use) {
		this.borrow_vouch_use = borrow_vouch_use;
	}
	public double getBorrow_vouch_nouse() {
		return borrow_vouch_nouse;
	}
	public void setBorrow_vouch_nouse(double borrow_vouch_nouse) {
		this.borrow_vouch_nouse = borrow_vouch_nouse;
	}
	public double getTender_vouch() {
		return tender_vouch;
	}
	public void setTender_vouch(double tender_vouch) {
		this.tender_vouch = tender_vouch;
	}
	public double getTender_vouch_use() {
		return tender_vouch_use;
	}
	public void setTender_vouch_use(double tender_vouch_use) {
		this.tender_vouch_use = tender_vouch_use;
	}
	public double getTender_vouch_nouse() {
		return tender_vouch_nouse;
	}
	public void setTender_vouch_nouse(double tender_vouch_nouse) {
		this.tender_vouch_nouse = tender_vouch_nouse;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}