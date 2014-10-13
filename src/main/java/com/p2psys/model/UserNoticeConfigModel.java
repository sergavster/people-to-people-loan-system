package com.p2psys.model;

import com.p2psys.domain.UserNoticeConfig;

public class UserNoticeConfigModel extends  UserNoticeConfig{

	public UserNoticeConfigModel() {
		// TODO Auto-generated constructor stub
	}

	private String nidName;
	
	private int smsSystem;
	
	private int emailSystem;
	
	private int messageSystem;
	
	private int smsSysSend;
	
	private int emailSysSend;
	
	private int messageSysSend;
	
	private int smsUserReceive;
	
	private int emailUserReceive;
	
	private int messageUserReceive;
	
	public String getNidName() {
		return nidName;
	}
	public void setNidName(String nidName) {
		this.nidName = nidName;
	}
	public int getSmsSystem() {
		return smsSystem;
	}
	public void setSmsSystem(int smsSystem) {
		this.smsSystem = smsSystem;
	}
	public int getEmailSystem() {
		return emailSystem;
	}
	public void setEmailSystem(int emailSystem) {
		this.emailSystem = emailSystem;
	}
	public int getMessageSystem() {
		return messageSystem;
	}
	public void setMessageSystem(int messageSystem) {
		this.messageSystem = messageSystem;
	}
	public int getSmsSysSend() {
		return smsSysSend;
	}
	public void setSmsSysSend(int smsSysSend) {
		this.smsSysSend = smsSysSend;
	}
	public int getEmailSysSend() {
		return emailSysSend;
	}
	public void setEmailSysSend(int emailSysSend) {
		this.emailSysSend = emailSysSend;
	}
	public int getMessageSysSend() {
		return messageSysSend;
	}
	public void setMessageSysSend(int messageSysSend) {
		this.messageSysSend = messageSysSend;
	}
	public int getSmsUserReceive() {
		return smsUserReceive;
	}
	public void setSmsUserReceive(int smsUserReceive) {
		this.smsUserReceive = smsUserReceive;
	}
	public int getEmailUserReceive() {
		return emailUserReceive;
	}
	public void setEmailUserReceive(int emailUserReceive) {
		this.emailUserReceive = emailUserReceive;
	}
	public int getMessageUserReceive() {
		return messageUserReceive;
	}
	public void setMessageUserReceive(int messageUserReceive) {
		this.messageUserReceive = messageUserReceive;
	}
	


	
	
}
