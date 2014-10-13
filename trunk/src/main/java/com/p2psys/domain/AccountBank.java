package com.p2psys.domain;

import java.io.Serializable;

public class AccountBank implements Serializable {

	private static final long serialVersionUID = 8096802588572261837L;
	private long id;
	private long user_id;
	private String account;
	private String bank;
	private String branch;
	private int province;
	private int city;
	private int area;
	private String addtime;
	private String addip;
	private String modify_username;
	//银行法人
	private String bank_realname;
	//线下银行代表值
	private String payment;
	//排序
	private long order;
	public String getBank_realname() {
		return bank_realname;
	}
	public void setBank_realname(String bank_realname) {
		this.bank_realname = bank_realname;
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
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
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
	public String getModify_username() {
		return modify_username;
	}
	public void setModify_username(String modify_username) {
		this.modify_username = modify_username;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public long getOrder() {
		return order;
	}
	public void setOrder(long order) {
		this.order = order;
	}
	
	
	

}
