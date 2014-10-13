package com.p2psys.model;

import com.p2psys.domain.UserCache;

public class VIPStatisticModel extends UserCache implements java.io.Serializable{
	
	private static final long serialVersionUID = 6104858028607800906L;
	private String username;
	private String realname;
	private String registertime;
    private String collection;
	
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
	public String getRegistertime() {
		return registertime;
	}
	public void setRegistertime(String registertime) {
		this.registertime = registertime;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	
}
