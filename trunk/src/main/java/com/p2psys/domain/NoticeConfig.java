package com.p2psys.domain;

public class NoticeConfig {

	public NoticeConfig() {
		// TODO Auto-generated constructor stub
	}
	private long id;
	private String type;
	private long sms;//短信
	private long email;//邮件
	private long  letters;//站内信
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getSms() {
		return sms;
	}
	public void setSms(long sms) {
		this.sms = sms;
	}
	public long getEmail() {
		return email;
	}
	public void setEmail(long email) {
		this.email = email;
	}
	public long getLetters() {
		return letters;
	}
	public void setLetters(long letters) {
		this.letters = letters;
	}

}
