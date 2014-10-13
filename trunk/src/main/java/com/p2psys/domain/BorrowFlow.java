package com.p2psys.domain;

import java.io.Serializable;

public class BorrowFlow implements Serializable {

	private static final long serialVersionUID = -4030197731989621101L;
	
	private long id;
	private long borrow_id;
	private long user_id;
	private int flow_count;
	private int valid_count;
	private String repayment_account;
	private String interest;
	private long buy_time;
	private long back_time;
	private int auto_repurchase;
	private int status;
	private String addip;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(long borrow_id) {
		this.borrow_id = borrow_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public int getFlow_count() {
		return flow_count;
	}
	public void setFlow_count(int flow_count) {
		this.flow_count = flow_count;
	}
	public String getRepayment_account() {
		return repayment_account;
	}
	public void setRepayment_account(String repayment_account) {
		this.repayment_account = repayment_account;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public long getBuy_time() {
		return buy_time;
	}
	public void setBuy_time(long buy_time) {
		this.buy_time = buy_time;
	}
	public long getBack_time() {
		return back_time;
	}
	public void setBack_time(long back_time) {
		this.back_time = back_time;
	}
	public int getAuto_repurchase() {
		return auto_repurchase;
	}
	public void setAuto_repurchase(int auto_repurchase) {
		this.auto_repurchase = auto_repurchase;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public int getValid_count() {
		return valid_count;
	}
	public void setValid_count(int valid_count) {
		this.valid_count = valid_count;
	}
	
}
