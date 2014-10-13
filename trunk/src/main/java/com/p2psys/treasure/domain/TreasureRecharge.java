package com.p2psys.treasure.domain;

import java.io.Serializable;

/**
 * 理财宝转入
 
 * @version 1.0
 * @since 2013-11-27
 */
public class TreasureRecharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1720669287548059135L;
	
	//主键ID 
	private long id;
	
	//会员ID 
	private long user_id;
	
	//理财宝ID 
	private long treasure_id;
	
	//可用金额 
	private double use_money;
	
	//操作金额 
	private double money;
	
	//利率 
	private double apr;
	
	//状态：0未审核，1审核通过，2审核不通过，3转出 审核中,4已转出 
	private byte status;
	
	//添加时间 
	private long add_time;
	
	//修改时间 
	private long update_time;
	
	//备注 
	private String remark;
	
	//应收利息
	private double interest;
	
	//手续费
	private double fee;
	
	//实际所得利息
	private double use_interest;
	
	//投资天数
	private int tender_day;
	
	//计息开始时间
	private long interest_start_time;
	
	//计息结束时间
	private long interest_end_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public long getTreasure_id() {
		return treasure_id;
	}

	public void setTreasure_id(long treasure_id) {
		this.treasure_id = treasure_id;
	}

	public double getUse_money() {
		return use_money;
	}

	public void setUse_money(double use_money) {
		this.use_money = use_money;
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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public long getAdd_time() {
		return add_time;
	}

	public void setAdd_time(long add_time) {
		this.add_time = add_time;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public double getUse_interest() {
		return use_interest;
	}

	public void setUse_interest(double use_interest) {
		this.use_interest = use_interest;
	}

	public int getTender_day() {
		return tender_day;
	}

	public void setTender_day(int tender_day) {
		this.tender_day = tender_day;
	}

	public long getInterest_start_time() {
		return interest_start_time;
	}

	public void setInterest_start_time(long interest_start_time) {
		this.interest_start_time = interest_start_time;
	}

	public long getInterest_end_time() {
		return interest_end_time;
	}

	public void setInterest_end_time(long interest_end_time) {
		this.interest_end_time = interest_end_time;
	}
}
