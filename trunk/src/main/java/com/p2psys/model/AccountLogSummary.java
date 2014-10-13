package com.p2psys.model;

import java.io.Serializable;

public class AccountLogSummary implements Serializable {

	private static final long serialVersionUID = -5290637298310888850L;
	
	//account_log
	private double account_other;
	private double award_add;
	private double award_add_error;
	private double award_lower;
	private double borrow_fee;
	private double borrow_frost;
	private double borrow_kouhui;
	private double borrow_success;
	private double cash_cancel;
	private double cash_frost;
	private double collection;
	private double fee;
	private double invest;
	private double invest_false;
	private double invest_repayment;
	private double late_collection;
	private double late_rate;
	private double late_repayment;
	private double margin;
	private double realname;
	private double recharge;
	private double recharge_false;
	private double recharge_success;
	private double repayment;
	private double scene_account;
	private double system_repayment;
	private double tender;
	private double tender_mange;
	private double ticheng;
	private double video;
	private double vip;
	private double vouch_advanced;
	private double vouch_award;
	private double vouch_awardpay;
	//borrow
	//借款总额
	private double borrow_account;
	//借款次数
	private long payment_times;
	//借款额度
	private double amount;
	
	//利息部分
	private double collection_account0;
	private double collection_interest0;
	private double collection_capital0;
	private double collection_account1;
	private double collection_interest1;
	private double collection_capital1;
	private double collection_account2;
	private double collection_interest2;
	private double collection_capital2;
	
	private double success_account;
	
	//最近还款时间和总额
	private String new_repay_time;
	private double new_repay_account;
	//最近收款时间和总额
	private String new_collection_time;
	private double new_collection_account;
	
	private double wait_payment;
	private double borrow_num;
	private double use_amount;
	
	
	public double getAccount_other() {
		return account_other;
	}
	public void setAccount_other(double account_other) {
		this.account_other = account_other;
	}
	public double getAward_add() {
		return award_add;
	}
	public void setAward_add(double award_add) {
		this.award_add = award_add;
	}
	public double getAward_add_error() {
		return award_add_error;
	}
	public void setAward_add_error(double award_add_error) {
		this.award_add_error = award_add_error;
	}
	public double getAward_lower() {
		return award_lower;
	}
	public void setAward_lower(double award_lower) {
		this.award_lower = award_lower;
	}
	public double getBorrow_fee() {
		return borrow_fee;
	}
	public void setBorrow_fee(double borrow_fee) {
		this.borrow_fee = borrow_fee;
	}
	public double getBorrow_frost() {
		return borrow_frost;
	}
	public void setBorrow_frost(double borrow_frost) {
		this.borrow_frost = borrow_frost;
	}
	public double getBorrow_kouhui() {
		return borrow_kouhui;
	}
	public void setBorrow_kouhui(double borrow_kouhui) {
		this.borrow_kouhui = borrow_kouhui;
	}
	public double getBorrow_success() {
		return borrow_success;
	}
	public void setBorrow_success(double borrow_success) {
		this.borrow_success = borrow_success;
	}
	public double getCash_cancel() {
		return cash_cancel;
	}
	public void setCash_cancel(double cash_cancel) {
		this.cash_cancel = cash_cancel;
	}
	public double getCash_frost() {
		return cash_frost;
	}
	public void setCash_frost(double cash_frost) {
		this.cash_frost = cash_frost;
	}
	public double getCollection() {
		return collection;
	}
	public void setCollection(double collection) {
		this.collection = collection;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public double getInvest() {
		return invest;
	}
	public void setInvest(double invest) {
		this.invest = invest;
	}
	public double getInvest_false() {
		return invest_false;
	}
	public void setInvest_false(double invest_false) {
		this.invest_false = invest_false;
	}
	public double getInvest_repayment() {
		return invest_repayment;
	}
	public void setInvest_repayment(double invest_repayment) {
		this.invest_repayment = invest_repayment;
	}
	public double getLate_collection() {
		return late_collection;
	}
	public void setLate_collection(double late_collection) {
		this.late_collection = late_collection;
	}
	public double getLate_rate() {
		return late_rate;
	}
	public void setLate_rate(double late_rate) {
		this.late_rate = late_rate;
	}
	public double getLate_repayment() {
		return late_repayment;
	}
	public void setLate_repayment(double late_repayment) {
		this.late_repayment = late_repayment;
	}
	public double getMargin() {
		return margin;
	}
	public void setMargin(double margin) {
		this.margin = margin;
	}
	public double getRealname() {
		return realname;
	}
	public void setRealname(double realname) {
		this.realname = realname;
	}
	public double getRecharge() {
		return recharge;
	}
	public void setRecharge(double recharge) {
		this.recharge = recharge;
	}
	public double getRecharge_false() {
		return recharge_false;
	}
	public void setRecharge_false(double recharge_false) {
		this.recharge_false = recharge_false;
	}
	public double getRecharge_success() {
		return recharge_success;
	}
	public void setRecharge_success(double recharge_success) {
		this.recharge_success = recharge_success;
	}
	public double getRepayment() {
		return repayment;
	}
	public void setRepayment(double repayment) {
		this.repayment = repayment;
	}
	public double getScene_account() {
		return scene_account;
	}
	public void setScene_account(double scene_account) {
		this.scene_account = scene_account;
	}
	public double getSystem_repayment() {
		return system_repayment;
	}
	public void setSystem_repayment(double system_repayment) {
		this.system_repayment = system_repayment;
	}
	public double getTender() {
		return tender;
	}
	public void setTender(double tender) {
		this.tender = tender;
	}
	public double getTender_mange() {
		return tender_mange;
	}
	public void setTender_mange(double tender_mange) {
		this.tender_mange = tender_mange;
	}
	public double getTicheng() {
		return ticheng;
	}
	public void setTicheng(double ticheng) {
		this.ticheng = ticheng;
	}
	public double getVideo() {
		return video;
	}
	public void setVideo(double video) {
		this.video = video;
	}
	public double getVip() {
		return vip;
	}
	public void setVip(double vip) {
		this.vip = vip;
	}
	public double getVouch_advanced() {
		return vouch_advanced;
	}
	public void setVouch_advanced(double vouch_advanced) {
		this.vouch_advanced = vouch_advanced;
	}
	public double getVouch_award() {
		return vouch_award;
	}
	public void setVouch_award(double vouch_award) {
		this.vouch_award = vouch_award;
	}
	public double getVouch_awardpay() {
		return vouch_awardpay;
	}
	public void setVouch_awardpay(double vouch_awardpay) {
		this.vouch_awardpay = vouch_awardpay;
	}
	public double getBorrow_account() {
		return borrow_account;
	}
	public void setBorrow_account(double borrow_account) {
		this.borrow_account = borrow_account;
	}
	public long getPayment_times() {
		return payment_times;
	}
	public void setPayment_times(long payment_times) {
		this.payment_times = payment_times;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getCollection_account0() {
		return collection_account0;
	}
	public void setCollection_account0(double collection_account0) {
		this.collection_account0 = collection_account0;
	}
	public double getCollection_interest0() {
		return collection_interest0;
	}
	public void setCollection_interest0(double collection_interest0) {
		this.collection_interest0 = collection_interest0;
	}
	public double getCollection_capital0() {
		return collection_capital0;
	}
	public void setCollection_capital0(double collection_capital0) {
		this.collection_capital0 = collection_capital0;
	}
	public double getCollection_account1() {
		return collection_account1;
	}
	public void setCollection_account1(double collection_account1) {
		this.collection_account1 = collection_account1;
	}
	public double getCollection_interest1() {
		return collection_interest1;
	}
	public void setCollection_interest1(double collection_interest1) {
		this.collection_interest1 = collection_interest1;
	}
	public double getCollection_capital1() {
		return collection_capital1;
	}
	public void setCollection_capital1(double collection_capital1) {
		this.collection_capital1 = collection_capital1;
	}
	public double getCollection_account2() {
		return collection_account2;
	}
	public void setCollection_account2(double collection_account2) {
		this.collection_account2 = collection_account2;
	}
	public double getCollection_interest2() {
		return collection_interest2;
	}
	public void setCollection_interest2(double collection_interest2) {
		this.collection_interest2 = collection_interest2;
	}
	public double getCollection_capital2() {
		return collection_capital2;
	}
	public void setCollection_capital2(double collection_capital2) {
		this.collection_capital2 = collection_capital2;
	}
	public double getSuccess_account() {
		return success_account;
	}
	public void setSuccess_account(double success_account) {
		this.success_account = success_account;
	}
	public String getNew_repay_time() {
		return new_repay_time;
	}
	public void setNew_repay_time(String new_repay_time) {
		this.new_repay_time = new_repay_time;
	}
	public double getNew_repay_account() {
		return new_repay_account;
	}
	public void setNew_repay_account(double new_repay_account) {
		this.new_repay_account = new_repay_account;
	}
	public String getNew_collection_time() {
		return new_collection_time;
	}
	public void setNew_collection_time(String new_collection_time) {
		this.new_collection_time = new_collection_time;
	}
	public double getNew_collection_account() {
		return new_collection_account;
	}
	public void setNew_collection_account(double new_collection_account) {
		this.new_collection_account = new_collection_account;
	}
	public double getWait_payment() {
		return wait_payment;
	}
	public void setWait_payment(double wait_payment) {
		this.wait_payment = wait_payment;
	}
	public double getBorrow_num() {
		return borrow_num;
	}
	public void setBorrow_num(double borrow_num) {
		this.borrow_num = borrow_num;
	}
	public double getUse_amount() {
		return use_amount;
	}
	public void setUse_amount(double use_amount) {
		this.use_amount = use_amount;
	}
	
	

}
