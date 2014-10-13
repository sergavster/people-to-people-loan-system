package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.Protocol;

public class ProtocolModel extends Protocol implements Serializable {
	private static final long serialVersionUID = 2737053575621240588L;
	private String username;
	private String protocol_type_name;
    private String realname;
    private String card_id;
    //提现银行账号
    private String bank_account;
    //开户用户名
    private String bank_username;
    //开户行
    private String bank_branch;
    //成功借款时间
    private String verify_time;
    //借款实际金额
    private double borrow_account;
    //借款标名称
    private String borrowname;
	

	public String getBorrowname() {
		return borrowname;
	}

	public void setBorrowname(String borrowname) {
		this.borrowname = borrowname;
	}

	public double getBorrow_account() {
		return borrow_account;
	}

	public void setBorrow_account(double borrow_account) {
		this.borrow_account = borrow_account;
	}

	public String getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}

	public String getBank_account() {
		return bank_account;
	}

	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

	public String getBank_username() {
		return bank_username;
	}

	public void setBank_username(String bank_username) {
		this.bank_username = bank_username;
	}

	public String getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getProtocol_type_name() {
		return protocol_type_name;
	}

	public void setProtocol_type_name(String protocol_type_name) {
		this.protocol_type_name = protocol_type_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
