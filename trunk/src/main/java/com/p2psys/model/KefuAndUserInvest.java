package com.p2psys.model;


import com.p2psys.domain.Tender;

public class KefuAndUserInvest extends Tender{
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 3514497272111606182L;
	//用户名
	private String username;
	//真实姓名
	private String realname;
    //客服名称
	private String kefu_username;
	//注册时间
	private String regiter_time;
	//vip审核时间
	private String vip_verify_time;
	public String getVip_verify_time() {
		return vip_verify_time;
	}
	public void setVip_verify_time(String vip_verify_time) {
		this.vip_verify_time = vip_verify_time;
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
	public String getKefu_username() {
		return kefu_username;
	}
	public void setKefu_username(String kefu_username) {
		this.kefu_username = kefu_username;
	}
	public String getRegiter_time() {
		return regiter_time;
	}
	public void setRegiter_time(String regiter_time) {
		this.regiter_time = regiter_time;
	}
	
	
	
}
