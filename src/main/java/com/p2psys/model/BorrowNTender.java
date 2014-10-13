package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.Tender;

public class BorrowNTender extends Tender implements Serializable {
	private static final long serialVersionUID = 2737053575621240588L;
	private String username;
	private String borrowname;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBorrowname() {
		return borrowname;
	}

	public void setBorrowname(String borrowname) {
		this.borrowname = borrowname;
	}

	
}
