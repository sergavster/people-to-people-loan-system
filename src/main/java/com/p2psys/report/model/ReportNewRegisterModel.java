package com.p2psys.report.model;

import java.io.Serializable;

import com.p2psys.util.AnnotExcelTitle;

/**
 * 每月新会员注册统计报表
 
 * @version 1.0
 * @since 2013-11-14
 */
public class ReportNewRegisterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8570009133167331112L;
	
	@AnnotExcelTitle("记录时间")
	private String record_time;
	
	@AnnotExcelTitle("注册人数")
	private int register_num;
	
	@AnnotExcelTitle("实名认证人数")
	private int real_num;
	
	@AnnotExcelTitle("手机认证人数")
	private int phone_num;
	
	@AnnotExcelTitle("VIP认证统计")
	private int vip_num;
	
	@AnnotExcelTitle("投标人次")
	private int tender_num;
	
	@AnnotExcelTitle("上月注册用户统计")
	private int before_register_num;
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-05 start
	@AnnotExcelTitle("邮箱认证人数")
	private int email_num;

	public int getEmail_num() {
		return email_num;
	}
	public void setEmail_num(int email_num) {
		this.email_num = email_num;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-05 end
	
	public int getRegister_num() {
		return register_num;
	}

	public void setRegister_num(int register_num) {
		this.register_num = register_num;
	}

	public int getReal_num() {
		return real_num;
	}

	public void setReal_num(int real_num) {
		this.real_num = real_num;
	}

	public int getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(int phone_num) {
		this.phone_num = phone_num;
	}

	public int getVip_num() {
		return vip_num;
	}

	public void setVip_num(int vip_num) {
		this.vip_num = vip_num;
	}

	public int getTender_num() {
		return tender_num;
	}

	public void setTender_num(int tender_num) {
		this.tender_num = tender_num;
	}

	public int getBefore_register_num() {
		return before_register_num;
	}

	public void setBefore_register_num(int before_register_num) {
		this.before_register_num = before_register_num;
	}

	public String getRecord_time() {
		return record_time;
	}

	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}
}
