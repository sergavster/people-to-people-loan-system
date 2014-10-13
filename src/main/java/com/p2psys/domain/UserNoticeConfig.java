package com.p2psys.domain;

public class UserNoticeConfig {

	public UserNoticeConfig() {
		// TODO Auto-generated constructor stub
	}
	private long id;
	private long user_id;
	//private String nidName;
	private String nid;
	private int sms;//短信
	private int email;//邮件
	private int  message;//站内信
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public int getSms() {
		return sms;
	}
	public void setSms(int sms) {
		this.sms = sms;
	}
	public int getEmail() {
		return email;
	}
	public void setEmail(int email) {
		this.email = email;
	}
	public int getMessage() {
		return message;
	}
	public void setMessage(int message) {
		this.message = message;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
//	public String getNidName() {
//		return nidName;
//	}
//	public void setNidName(String nidName) {
//		this.nidName = nidName;
//	}


	
	
}
