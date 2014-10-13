package com.p2psys.domain;

import java.io.Serializable;

/**
 * 用户资金统计
 
 * @version 1.0
 * @since 2013-8-27
 */
public class AccountSum implements Serializable{

	private static final long serialVersionUID = -7680538646986306804L;

	// 
	private long id;
	
	//用户id 
	private long user_id;
	
	//充值总和 
	private double recharge;
	
	//提现总和 
	private double cash;
	
	//利息总和 
	private double interest;
	
	//扣除的利息手续费 
	private double interest_fee;
	
	//奖励总和 
	private double award;
	
	//扣款总和 
	private double deduct;
	
	//已使用充值 
	private double used_recharge;
	
	//已使用利息 
	private double used_interest;
	
	//已使用奖励 
	private double used_award;
	
	//回款统计 
	private double huikuan;
	
	//使用的回款 
	private double used_huikuan;
	
	//提现手续费总和
	private double cash_fee;
	
	//借款入账合计
	private double borrow_cash;
	
	//已使用借款入账合计
	private double used_borrow_cash;
	
	//已还款 
	private double repay_cash;

	//回款利息合计
	private double huikuan_interest;
	
	//已使用回款利息合计
	private double used_huikuan_interest;
	
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

	public double getRecharge() {
		return recharge;
	}

	public void setRecharge(double recharge) {
		this.recharge = recharge;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getInterest_fee() {
		return interest_fee;
	}

	public void setInterest_fee(double interest_fee) {
		this.interest_fee = interest_fee;
	}

	public double getAward() {
		return award;
	}

	public void setAward(double award) {
		this.award = award;
	}

	public double getDeduct() {
		return deduct;
	}

	public void setDeduct(double deduct) {
		this.deduct = deduct;
	}

	public double getUsed_recharge() {
		return used_recharge;
	}

	public void setUsed_recharge(double used_recharge) {
		this.used_recharge = used_recharge;
	}

	public double getUsed_interest() {
		return used_interest;
	}

	public void setUsed_interest(double used_interest) {
		this.used_interest = used_interest;
	}

	public double getUsed_award() {
		return used_award;
	}

	public void setUsed_award(double used_award) {
		this.used_award = used_award;
	}

	public double getHuikuan() {
		return huikuan;
	}

	public void setHuikuan(double huikuan) {
		this.huikuan = huikuan;
	}

	public double getUsed_huikuan() {
		return used_huikuan;
	}

	public void setUsed_huikuan(double used_huikuan) {
		this.used_huikuan = used_huikuan;
	}

	public double getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(double cash_fee) {
		this.cash_fee = cash_fee;
	}

	public double getBorrow_cash() {
		return borrow_cash;
	}

	public void setBorrow_cash(double borrow_cash) {
		this.borrow_cash = borrow_cash;
	}

	public double getUsed_borrow_cash() {
		return used_borrow_cash;
	}

	public void setUsed_borrow_cash(double used_borrow_cash) {
		this.used_borrow_cash = used_borrow_cash;
	}
    public double getRepay_cash() {
		return repay_cash;
	}

	public void setRepay_cash(double repay_cash) {
		this.repay_cash = repay_cash;
	}

	public double getHuikuan_interest() {
		return huikuan_interest;
	}

	public void setHuikuan_interest(double huikuan_interest) {
		this.huikuan_interest = huikuan_interest;
	}

	public double getUsed_huikuan_interest() {
		return used_huikuan_interest;
	}

	public void setUsed_huikuan_interest(double used_huikuan_interest) {
		this.used_huikuan_interest = used_huikuan_interest;
	}

}
