package com.p2psys.model;

import com.p2psys.domain.Collection;

public class DetailCollection extends Collection {

	private static final long serialVersionUID = -5620227157548250710L;
	
	private String borrow_name;
	private long borrow_id;
	private String time_limit;
	private String username;
	private String tendertime;
	//v1.6.7.1 报表格式 cx 2013-12-03 start
	private String award;
	private String late_award;
	private String part_account;// 1、 投标奖励
	private String funds; //比例
	private String account;
	private String qsCount;//期数 order+1/time_limit
	public String getQsCount() {
		return qsCount;
	}
	public void setQsCount(String qsCount) {
		this.qsCount = qsCount;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPart_account() {
		return part_account;
	}
	public void setPart_account(String part_account) {
		this.part_account = part_account;
	}
	public String getFunds() {
		return funds;
	}
	public void setFunds(String funds) {
		this.funds = funds;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getLate_award() {
		return late_award;
	}
	public void setLate_award(String late_award) {
		this.late_award = late_award;
	}
	//v1.6.7.1 报表格式 cx 2013-12-03 end
	
	private String borrow_style;
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 START
	private long real_order;
	public long getReal_order() {
		return real_order;
	}
	public void setReal_order(long real_order) {
		this.real_order = real_order;
	}
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 end
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(long borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getTendertime() {
		return tendertime;
	}
	public void setTendertime(String tendertime) {
		this.tendertime = tendertime;
	}
	public String getBorrow_style() {
		return borrow_style;
	}
	public void setBorrow_style(String borrow_style) {
		this.borrow_style = borrow_style;
	}
}
