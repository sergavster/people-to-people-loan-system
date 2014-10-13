package com.p2psys.domain;

import java.io.Serializable;

public class Reservation implements Serializable{

	public Reservation() {
		// TODO Auto-generated constructor stub
	}
	private long id;
	private long reservation_user;
	private String borrow_apr;
	private double tender_account;
	private String addtime;
	private String addip;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getReservation_user() {
		return reservation_user;
	}
	public void setReservation_user(long reservation_user) {
		this.reservation_user = reservation_user;
	}
	public String getBorrow_apr() {
		return borrow_apr;
	}
	public void setBorrow_apr(String borrow_apr) {
		this.borrow_apr = borrow_apr;
	}
	public double getTender_account() {
		return tender_account;
	}
	public void setTender_account(double tender_account) {
		this.tender_account = tender_account;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}

}
