package com.p2psys.model;

import java.io.Serializable;

public class NewCollection implements Serializable {

	private static final long serialVersionUID = -4276897186874619101L;

	private String new_collection_time;
	private double new_collection_account;
	public String getNew_collection_time() {
		return new_collection_time;
	}
	public void setNew_collection_time(String new_collection_time) {
		this.new_collection_time = new_collection_time;
	}
	public double getNew_collection_account() {
		return new_collection_account;
	}
	public void setNew_collection_account(double new_collection_account) {
		this.new_collection_account = new_collection_account;
	}
	
	
	
}
