package com.p2psys.domain;

import java.io.Serializable;

public class BorrowConfig implements Serializable {

	private static final long serialVersionUID = -2956372149296138178L;

	private long id;
	private String name;
	private double most_account;
	private double lowest_account;
	private double most_apr;
	private double lowest_apr;
	private double most_award_apr;
	private double lowest_award_apr;
	private double most_award_funds;
	private double lowest_award_funds;
	private int is_trail;
	private int is_review;
	private String identify;
	private String remark;
	
	private double managefee;
	private double daymanagefee;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMost_account() {
		return most_account;
	}
	public void setMost_account(double most_account) {
		this.most_account = most_account;
	}
	public double getLowest_account() {
		return lowest_account;
	}
	public void setLowest_account(double lowest_account) {
		this.lowest_account = lowest_account;
	}
	public double getMost_apr() {
		return most_apr;
	}
	public void setMost_apr(double most_apr) {
		this.most_apr = most_apr;
	}
	public double getLowest_apr() {
		return lowest_apr;
	}
	public void setLowest_apr(double lowest_apr) {
		this.lowest_apr = lowest_apr;
	}
	public double getMost_award_apr() {
		return most_award_apr;
	}
	public void setMost_award_apr(double most_award_apr) {
		this.most_award_apr = most_award_apr;
	}
	public double getLowest_award_apr() {
		return lowest_award_apr;
	}
	public void setLowest_award_apr(double lowest_award_apr) {
		this.lowest_award_apr = lowest_award_apr;
	}
	public double getMost_award_funds() {
		return most_award_funds;
	}
	public void setMost_award_funds(double most_award_funds) {
		this.most_award_funds = most_award_funds;
	}
	public double getLowest_award_funds() {
		return lowest_award_funds;
	}
	public void setLowest_award_funds(double lowest_award_funds) {
		this.lowest_award_funds = lowest_award_funds;
	}
	public int getIs_trail() {
		return is_trail;
	}
	public void setIs_trail(int is_trail) {
		this.is_trail = is_trail;
	}
	public int getIs_review() {
		return is_review;
	}
	public void setIs_review(int is_review) {
		this.is_review = is_review;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	public double getManagefee() {
		return managefee;
	}
	public void setManagefee(double managefee) {
		this.managefee = managefee;
	}
	public double getDaymanagefee() {
		return daymanagefee;
	}
	public void setDaymanagefee(double daymanagefee) {
		this.daymanagefee = daymanagefee;
	}
}
