package com.p2psys.model.borrow;

import java.io.Serializable;



public class FastExpireModel implements Serializable{

	private static final long serialVersionUID = 7722045733769001011L;
	
	private long borrow_id;
	private String borrow_user;
	private String borrow_name;
	private String repayment_time;
	private String repayment_account;
	private int late_days;
	private String forfeit;
	public long getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(long borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getBorrow_user() {
		return borrow_user;
	}
	public void setBorrow_user(String borrow_user) {
		this.borrow_user = borrow_user;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getRepayment_time() {
		return repayment_time;
	}
	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}
	public String getRepayment_account() {
		return repayment_account;
	}
	public void setRepayment_account(String repayment_account) {
		this.repayment_account = repayment_account;
	}
	public int getLate_days() {
		return late_days;
	}
	public void setLate_days(int late_days) {
		this.late_days = late_days;
	}
	public String getForfeit() {
		return forfeit;
	}
	public void setForfeit(String forfeit) {
		this.forfeit = forfeit;
	}
	

}
