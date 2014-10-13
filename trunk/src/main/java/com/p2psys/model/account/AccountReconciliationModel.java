package com.p2psys.model.account;

import com.p2psys.domain.Account;

/**
 * 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-18
 */
public class AccountReconciliationModel extends Account {
	
	private static final long serialVersionUID = 4038060732774453500L;
	private String username;
	private double recharge_money;
	private double log_recharge_money;
	private double up_recharge_money;
	private double down_recharge_money;
	private double houtai_recharge_money;
	private double allcollection;
	private double cash_money;
	private double invest_award;
	private double invest_yeswait_interest;
	private double wait_interest;
	private double borrow_award;
	private double borrow_fee;
	private double manage_fee;
	private double system_fee;
	private double invite_money;
	private double vip_money;
	private double repayment_interest;
	private double flow_repayment_interest;
	private double repayment_principal;
	// v1.6.5.3 RDPROJECT-175 zza 2013-09-18 start
	private double flow_repayment_principal;
	// v1.6.5.3 RDPROJECT-175 zza 2013-09-18 end
	private double yes_repayment_interest;
	private double flow_yes_repayment_interest;
	
	// v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
	/**
	 * 真实姓名
	 */
	private String realname;
	// v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getYes_repayment_interest() {
		return yes_repayment_interest;
	}
	public void setYes_repayment_interest(double yes_repayment_interest) {
		this.yes_repayment_interest = yes_repayment_interest;
	}
	public double getRepayment_interest() {
		return repayment_interest;
	}
	public void setRepayment_interest(double repayment_interest) {
		this.repayment_interest = repayment_interest;
	}
	public double getRepayment_principal() {
		return repayment_principal;
	}
	public void setRepayment_principal(double repayment_principal) {
		this.repayment_principal = repayment_principal;
	}
	public double getRecharge_money() {
		return recharge_money;
	}
	public void setRecharge_money(double recharge_money) {
		this.recharge_money = recharge_money;
	}
	public double getLog_recharge_money() {
		return log_recharge_money;
	}
	public void setLog_recharge_money(double log_recharge_money) {
		this.log_recharge_money = log_recharge_money;
	}
	public double getUp_recharge_money() {
		return up_recharge_money;
	}
	public void setUp_recharge_money(double up_recharge_money) {
		this.up_recharge_money = up_recharge_money;
	}
	public double getDown_recharge_money() {
		return down_recharge_money;
	}
	public void setDown_recharge_money(double down_recharge_money) {
		this.down_recharge_money = down_recharge_money;
	}
	public double getHoutai_recharge_money() {
		return houtai_recharge_money;
	}
	public void setHoutai_recharge_money(double houtai_recharge_money) {
		this.houtai_recharge_money = houtai_recharge_money;
	}
	public double getAllcollection() {
		return allcollection;
	}
	public void setAllcollection(double allcollection) {
		this.allcollection = allcollection;
	}
	public double getCash_money() {
		return cash_money;
	}
	public void setCash_money(double cash_money) {
		this.cash_money = cash_money;
	}
	public double getInvest_award() {
		return invest_award;
	}
	public void setInvest_award(double invest_award) {
		this.invest_award = invest_award;
	}
	public double getInvest_yeswait_interest() {
		return invest_yeswait_interest;
	}
	public void setInvest_yeswait_interest(double invest_yeswait_interest) {
		this.invest_yeswait_interest = invest_yeswait_interest;
	}
	public double getWait_interest() {
		return wait_interest;
	}
	public void setWait_interest(double wait_interest) {
		this.wait_interest = wait_interest;
	}
	public double getBorrow_award() {
		return borrow_award;
	}
	public void setBorrow_award(double borrow_award) {
		this.borrow_award = borrow_award;
	}
	public double getBorrow_fee() {
		return borrow_fee;
	}
	public void setBorrow_fee(double borrow_fee) {
		this.borrow_fee = borrow_fee;
	}
	public double getManage_fee() {
		return manage_fee;
	}
	public void setManage_fee(double manage_fee) {
		this.manage_fee = manage_fee;
	}
	public double getSystem_fee() {
		return system_fee;
	}
	public void setSystem_fee(double system_fee) {
		this.system_fee = system_fee;
	}
	public double getInvite_money() {
		return invite_money;
	}
	public void setInvite_money(double invite_money) {
		this.invite_money = invite_money;
	}
	public double getVip_money() {
		return vip_money;
	}
	public void setVip_money(double vip_money) {
		this.vip_money = vip_money;
	}
	public double getFlow_repayment_interest() {
		return flow_repayment_interest;
	}
	public void setFlow_repayment_interest(double flow_repayment_interest) {
		this.flow_repayment_interest = flow_repayment_interest;
	}
	public double getFlow_yes_repayment_interest() {
		return flow_yes_repayment_interest;
	}
	public void setFlow_yes_repayment_interest(double flow_yes_repayment_interest) {
		this.flow_yes_repayment_interest = flow_yes_repayment_interest;
	}
	/**
	 * 获取flow_repayment_principal
	 * 
	 * @return flow_repayment_principal
	 */
	public double getFlow_repayment_principal() {
		return flow_repayment_principal;
	}
	
	/**
	 * 设置flow_repayment_principal
	 * 
	 * @param flow_repayment_principal 要设置的flow_repayment_principal
	 */
	public void setFlow_repayment_principal(double flow_repayment_principal) {
		this.flow_repayment_principal = flow_repayment_principal;
	}
	/**
	 * 获取realname
	 * 
	 * @return realname
	 */
	public String getRealname() {
		return realname;
	}
	
	/**
	 * 设置realname
	 * 
	 * @param realname 要设置的realname
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

}
