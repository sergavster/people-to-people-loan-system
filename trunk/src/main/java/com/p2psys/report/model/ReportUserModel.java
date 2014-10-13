package com.p2psys.report.model;

import java.io.Serializable;

import com.p2psys.util.AnnotExcelTitle;

public class ReportUserModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -107506352887238841L;
	
	@AnnotExcelTitle("会员ID")
	private long user_id;
	
	@AnnotExcelTitle("会员名")
	private String username;
	
	@AnnotExcelTitle("会员真实姓名")
	private String realname;
	
	@AnnotExcelTitle("实名认证状态(1通过)")
	private String real_status;
	
	@AnnotExcelTitle("邮箱")
	private String email;
	
	@AnnotExcelTitle("邮箱认证状态(1通过)")
    private String email_status;
    
	@AnnotExcelTitle("电话")
	private String phone ;
	
	@AnnotExcelTitle("电话认证状态(1通过)")
    private String phone_status ;
    
	@AnnotExcelTitle("是否VIP(1是vip,否则普通用户)")
    private int vip_status;

	@AnnotExcelTitle("VIP申请时间")
	private String vip_verify_time;
	
	@AnnotExcelTitle("会员注册时间")
	private String addtime ;
	
	@AnnotExcelTitle("会员地域")
	private String address;
	
	@AnnotExcelTitle("会员投标次数")
	private int tender_num; 

	@AnnotExcelTitle("会员投标金额合计")
	private double tender_sum;

	@AnnotExcelTitle("会员出生日期")
	private String birthday;

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
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

	public String getReal_status() {
		return real_status;
	}

	public void setReal_status(String real_status) {
		this.real_status = real_status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail_status() {
		return email_status;
	}

	public void setEmail_status(String email_status) {
		this.email_status = email_status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone_status() {
		return phone_status;
	}

	public void setPhone_status(String phone_status) {
		this.phone_status = phone_status;
	}

	public int getVip_status() {
		return vip_status;
	}

	public void setVip_status(int vip_status) {
		this.vip_status = vip_status;
	}

	public String getVip_verify_time() {
		return vip_verify_time;
	}

	public void setVip_verify_time(String vip_verify_time) {
		this.vip_verify_time = vip_verify_time;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getTender_num() {
		return tender_num;
	}

	public void setTender_num(int tender_num) {
		this.tender_num = tender_num;
	}

	public double getTender_sum() {
		return tender_sum;
	}

	public void setTender_sum(double tender_sum) {
		this.tender_sum = tender_sum;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
