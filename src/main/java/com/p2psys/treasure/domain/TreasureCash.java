package com.p2psys.treasure.domain;

import java.io.Serializable;

/**
 * 理财宝转出
 
 * @version 1.0
 * @since 2013-11-27
 */
public class TreasureCash implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3269766527510794222L;
	
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
	
	//利息 
	private double interest;
	
	//手续费 
	private double fee;
	
	//状态：0未审核，1审核通过，2审核不通过，3无用数据 
	private byte status;
	
	//添加时间 
	private long add_time;
	
	//审核时间 
	private long verify_time;
	
	//审核人 
	private String verify_user;
	
	//审核人ID 
	private long verify_user_id;
	
	//备注 
	private String remark;
	
	// 理财宝投资ID
	private long treasure_recharge_id;

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

	public long getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(long verify_time) {
		this.verify_time = verify_time;
	}

	public String getVerify_user() {
		return verify_user;
	}

	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}

	public long getVerify_user_id() {
		return verify_user_id;
	}

	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getTreasure_recharge_id() {
		return treasure_recharge_id;
	}

	public void setTreasure_recharge_id(long treasure_recharge_id) {
		this.treasure_recharge_id = treasure_recharge_id;
	}

}
