package com.p2psys.model.rule;
//RDPROJECT-282 fxx 2013-10-18 start
//新增类
//RDPROJECT-282 fxx 2013-10-18 end
public class TenderRule {
	private int lower;
	private int upper;
	private double rate;
	public int getLower() {
		return lower;
	}
	public void setLower(int lower) {
		this.lower = lower;
	}
	public int getUpper() {
		return upper;
	}
	public void setUpper(int upper) {
		this.upper = upper;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
}