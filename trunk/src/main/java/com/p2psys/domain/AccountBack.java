package com.p2psys.domain;

import java.io.Serializable;

/**
 * dw_account_back 实体类
 * 
 
 * @version 1.0
 * @since 2013-12-12
 */ 
public class AccountBack implements Serializable{
	private static final long serialVersionUID = -5888748038183103223L;
	private long id;
	private String trade_no;
	private long user_id;
	private int status;
	private double money;
	private String type;
	private String remark;
	private long verify_userid;
	private String verify_time;
	private String verify_remark;
	private String addtime;
	private String addip;
	public AccountBack(String trade_no, long user_id,
			String payment, String type, String fee, long verify_userid,
			 String addtime,
			String addip, String paymentname) {
		super();
		this.trade_no = trade_no;
		this.user_id = user_id;
		this.type = type;
		this.verify_userid = verify_userid;
		this.addtime = addtime;
		this.addip = addip;
	}
	public AccountBack() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getVerify_userid() {
		return verify_userid;
	}
	public void setVerify_userid(long verify_userid) {
		this.verify_userid = verify_userid;
	}
	public String getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}
	public String getVerify_remark() {
		return verify_remark;
	}
	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
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

