package com.p2psys.model;

import com.p2psys.domain.Attestation;

public class AttestationModel extends Attestation {
	
	private static final long serialVersionUID = -5146300684213117838L;
	
	private String type_name;
	private String username;
	private String realname;
	
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

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
	
	
	
}
