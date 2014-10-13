package com.p2psys.domain;

import java.io.Serializable;

public class UserAmountApply implements Serializable {
	private static final long serialVersionUID = -1428496877548363188L;
	private long id;
	private long user_id;
	private String type;
	private double account;
	private double account_new;
	private double account_old;
	private int status;
	private String addtime;
	private String content;
	private String remark;
	private String verify_remark;
	private String verify_time;
	private long verify_user;
	private String addip;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAccount() {
		return account;
	}
	public void setAccount(double account) {
		this.account = account;
	}
	public double getAccount_new() {
		return account_new;
	}
	public void setAccount_new(double account_new) {
		this.account_new = account_new;
	}
	public double getAccount_old() {
		return account_old;
	}
	public void setAccount_old(double account_old) {
		this.account_old = account_old;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getVerify_remark() {
		return verify_remark;
	}
	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
	}
	public String getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}
	public long getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(long verify_user) {
		this.verify_user = verify_user;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
