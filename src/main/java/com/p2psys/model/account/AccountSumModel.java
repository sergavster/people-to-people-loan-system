package com.p2psys.model.account;

import com.p2psys.domain.Account;

public class AccountSumModel extends Account {
	
	private static final long serialVersionUID = 4038060732774453500L;
	
	private double wait_collect;
	
	private double wait_repay;
	
	private String username;
	
	private String realname;
	
	private double net_assets;
	
	// v1.6.7.1 RDPROJECT-467 2013-11-19 start
	private double jin_wait_repay;
	
	public double getJin_wait_repay() {
		return jin_wait_repay;
	}
	public void setJin_wait_repay(double jin_wait_repay) {
		this.jin_wait_repay = jin_wait_repay;
	}
	// v1.6.7.1 RDPROJECT-467 2013-11-19 end

	public double getWait_collect() {
		return wait_collect;
	}
	public void setWait_collect(double wait_collect) {
		this.wait_collect = wait_collect;
	}
	public double getWait_repay() {
		return wait_repay;
	}
	public void setWait_repay(double wait_repay) {
		this.wait_repay = wait_repay;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public double getNet_assets() {
		return net_assets;
	}
	public void setNet_assets(double net_assets) {
		this.net_assets = net_assets;
	}
}
