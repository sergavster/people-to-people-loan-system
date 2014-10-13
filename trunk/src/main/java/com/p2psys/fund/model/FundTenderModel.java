package com.p2psys.fund.model;

import com.p2psys.fund.domain.FundTender;

/**
 * 基金信托认购信息Model
 
 * @version 1.0
 * @since 2013-10-27
 */
public class FundTenderModel extends FundTender {

	//基金信托
	private String name;
	private int type;
	//会员名
	private String username;
	//实名
	private String	realname;
	//手机号
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
