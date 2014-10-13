package com.p2psys.report.model;

import java.io.Serializable;

import com.p2psys.util.AnnotExcelTitle;

public class MonthTenderModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 419591644221421956L;

	//投资人用户名
	@AnnotExcelTitle("投资人用户名")
	private String username;
	
	//标ID
	@AnnotExcelTitle("标ID")
	private long id;
	
	//标名称	
	@AnnotExcelTitle("标名称	")
	private String name;
	
	//还款金额
	@AnnotExcelTitle("还款金额")
	private double repayment_account;
	
	//有效投资金额
	@AnnotExcelTitle("有效投资金额")
	private double account;
	
	//操作金额
	@AnnotExcelTitle("操作金额")
	private double money;
	
	//利率
	@AnnotExcelTitle("利率")
	private double apr;
	
	//利息
	@AnnotExcelTitle("利息")
	private double interest;
	
	//是否天标
	@AnnotExcelTitle("是否天标")
	private String isday;
	
	//天标天数
	@AnnotExcelTitle("天标天数")
	private int time_limit_day;
	
	//月标时间
	@AnnotExcelTitle("月标时间")
	private int time_limit;
	
	//投标时间
	@AnnotExcelTitle("投标时间")
	private String addtime;
	
	@AnnotExcelTitle("标类型名")
	private String type_name;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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

	public double getRepayment_account() {
		return repayment_account;
	}

	public void setRepayment_account(double repayment_account) {
		this.repayment_account = repayment_account;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public String getIsday() {
		return isday;
	}

	public void setIsday(String isday) {
		this.isday = isday;
	}

	public int getTime_limit_day() {
		return time_limit_day;
	}

	public void setTime_limit_day(int time_limit_day) {
		this.time_limit_day = time_limit_day;
	}

	public int getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(int time_limit) {
		this.time_limit = time_limit;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

}
