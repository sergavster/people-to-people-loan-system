package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.UserCreditLog;

public class UserCreditLogModel extends UserCreditLog implements Serializable {
	
	private static final long serialVersionUID = 5093702455445890306L;
	
	private String username;
	private String realname;
	private String typeName;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
