package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.TroubleFundDonateRecord;
/**
 * 急难基金 捐赠记录实体 子类
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-7-26
 */
public class TroubleFundModel extends TroubleFundDonateRecord implements Serializable {
	
	private static final long serialVersionUID = 2737053575621240588L;
	//会员用户名
	private String username;
	//会员真实姓名
	private String realname;
	//转入基金
	private double into_funds; 
	
	public double getInto_funds() {
		return into_funds;
	}

	public void setInto_funds(double into_funds) {
		this.into_funds = into_funds;
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

	

	
}
