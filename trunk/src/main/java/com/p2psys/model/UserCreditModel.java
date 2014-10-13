package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.UserCredit;

public class UserCreditModel extends UserCredit implements Serializable {

	private static final long serialVersionUID = -7898681921878574306L;
	
	private String creditTypeName;
	private String userName;
	private String realname;
	private String creditLogAddTime;
	private String creditLogRemark;
	private String  credit_pic;

	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 start
	private String creditLevel; //用户等级
	private String perferTotalMoney; //已优惠利息管理费总额
	public String getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}
	public String getPerferTotalMoney() {
		return perferTotalMoney;
	}
	public void setPerferTotalMoney(String perferTotalMoney) {
		this.perferTotalMoney = perferTotalMoney;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 end
	
	public String getCredit_pic() {
		return credit_pic;
	}
	public void setCredit_pic(String credit_pic) {
		this.credit_pic = credit_pic;
	}
	public String getCreditTypeName() {
		return creditTypeName;
	}
	public void setCreditTypeName(String creditTypeName) {
		this.creditTypeName = creditTypeName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getCreditLogAddTime() {
		return creditLogAddTime;
	}
	public void setCreditLogAddTime(String creditLogAddTime) {
		this.creditLogAddTime = creditLogAddTime;
	}
	public String getCreditLogRemark() {
		return creditLogRemark;
	}
	public void setCreditLogRemark(String creditLogRemark) {
		this.creditLogRemark = creditLogRemark;
	}
	

}
