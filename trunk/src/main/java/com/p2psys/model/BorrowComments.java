package com.p2psys.model;

import com.p2psys.domain.Comments;

public class BorrowComments  extends Comments{

	private static final long serialVersionUID = 6096539246733265840L;
	private String username;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "BorrowComments [username=" + username + ", getId()=" + getId()
				+ ", getPid()=" + getPid() + ", getUser_id()=" + getUser_id()
				+ ", getModule_code()=" + getModule_code()
				+ ", getArticle_id()=" + getArticle_id() + ", getFlag()="
				+ getFlag() + ", getOrder()=" + getOrder() + ", getStatus()="
				+ getStatus() + ", getAddip()=" + getAddip()
				+ ", getAddtime()=" + getAddtime() + ", getComment()="
				+ getComment() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	
	
	
}
