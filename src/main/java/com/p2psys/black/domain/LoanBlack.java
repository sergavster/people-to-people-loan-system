package com.p2psys.black.domain;

import java.io.Serializable;

public class LoanBlack implements Serializable{
	
	private static final long serialVersionUID = 8495337558304572500L;
	/**
	 * 主键
	 */
	private long id;
	/**
	 *数据来源平台名称 
	 */
	private String platfrom;
	/**
	 * 是否上传
	 */
	private int is_upload;
	
	/**
	 *姓名
	 */
	private String username;
	/**
	 * 客户身份证号
	 */
	private String identity;
	/**
	 * 客户手机号
	 */
	private String mobile;
	/**
	 * 客户email
	 */
	private String email;
	/**
	 * 客户QQ
	 */
	private String qq;
	/**
	 * 所在地
	 */
	private String location;
	/**
	 *贷款总额 
	 */
	private int loan_count;
	/**
	 * 贷款笔数
	 */
	private int loan_number;
	/**
	 * 最近一笔逾期时间
	 */
	private int last_overdue_date;
	/**
	 * 逾期笔数
	 */
	private int overdue_number;
	/**
	 * 逾期总金额
	 */
	private int overdue_money;
	/**
	 * 逾期天数
	 */
	private int overdue_days;
	/**
	 * 添加时间
	 */
	private int addtime;
	/**
	 * 状态（0：正常，1：已删除）
	 */
	private int status;
	
	/**
	 * 外键ID
	 */
	private long fk_id;
	public LoanBlack() {
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPlatfrom() {
		return platfrom;
	}
	public void setPlatfrom(String platfrom) {
		this.platfrom = platfrom;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getLoan_count() {
		return loan_count;
	}
	public void setLoan_count(int loan_count) {
		this.loan_count = loan_count;
	}
	public int getLoan_number() {
		return loan_number;
	}
	public void setLoan_number(int loan_number) {
		this.loan_number = loan_number;
	}
	public int getLast_overdue_date() {
		return last_overdue_date;
	}
	public void setLast_overdue_date(int last_overdue_date) {
		this.last_overdue_date = last_overdue_date;
	}
	public int getOverdue_number() {
		return overdue_number;
	}
	public void setOverdue_number(int overdue_number) {
		this.overdue_number = overdue_number;
	}
	public int getOverdue_money() {
		return overdue_money;
	}
	public void setOverdue_money(int overdue_money) {
		this.overdue_money = overdue_money;
	}
	public int getOverdue_days() {
		return overdue_days;
	}
	public void setOverdue_days(int overdue_days) {
		this.overdue_days = overdue_days;
	}
	public int getAddtime() {
		return addtime;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIs_upload() {
		return is_upload;
	}
	public void setIs_upload(int is_upload) {
		this.is_upload = is_upload;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getFk_id() {
		return fk_id;
	}
	public void setFk_id(long fk_id) {
		this.fk_id = fk_id;
	}
	
}
