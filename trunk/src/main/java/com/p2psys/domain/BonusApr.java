package com.p2psys.domain;

public class BonusApr {
	private long id;
	private long borrow_id;
	private int order;
	private double apr;
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public double getApr() {
		return apr;
	}
	public void setApr(double apr) {
		this.apr = apr;
	}
	
	
}
