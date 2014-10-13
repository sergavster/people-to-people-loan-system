package com.p2psys.model.account;

import com.p2psys.domain.OnlineBank;

public class OnlineBankModel extends OnlineBank {

	private static final long serialVersionUID = 3261895775405456319L;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
