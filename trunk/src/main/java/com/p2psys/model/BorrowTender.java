package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.Tender;

public class BorrowTender extends Tender implements Serializable {
	private static final long serialVersionUID = 2737053575621240588L;
	private String username;
	//待收时间
	private String repay_time;
	//待收金额
	private String repay_account;
	//标名
	private String name;
	//借款金额
	private double borrow_account;
	//投标人真实姓名
	private String realname;
	//投标人身份证号
	private String card_id;
	
	// V1.6.6.2 RDPROJECT-353 ljd 2013-10-21 start
	//实际还款时间
	private String repay_yestime;
	
	public String getRepay_yestime() {
		return repay_yestime;
	}

	public void setRepay_yestime(String repay_yestime) {
		this.repay_yestime = repay_yestime;
	}
	// V1.6.6.2 RDPROJECT-353 ljd 2013-10-21 end

	// V1.6.6.2 RDPROJECT-380 lhm 2013-10-21 start
	private String award;
	
	/**
	 * 获取award
	 * 
	 * @return award
	 */
	public String getAward() {
		return award;
	}

	/**
	 * 设置award
	 * 
	 * @param award 要设置的award
	 */
	public void setAward(String award) {
		this.award = award;
	}
	// V1.6.6.2 RDPROJECT-380 lhm 2013-10-21 end

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public double getBorrow_account() {
		return borrow_account;
	}

	public void setBorrow_account(double borrow_account) {
		this.borrow_account = borrow_account;
	}

	public String getRepay_account() {
		return repay_account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRepay_account(String repay_account) {
		this.repay_account = repay_account;
	}

	public String getRepay_time() {
		return repay_time;
	}

	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
