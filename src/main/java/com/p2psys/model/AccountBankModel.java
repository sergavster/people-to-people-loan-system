package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.AccountBank;

public class AccountBankModel extends AccountBank implements Serializable {
	private static final long serialVersionUID = 2737053575621240588L;
	
	private String bank_name;

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
}
