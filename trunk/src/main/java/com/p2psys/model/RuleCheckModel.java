package com.p2psys.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 奖励规则JSON KEY实体
 
 *
 */
public class RuleCheckModel implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 528325888832622757L;

	private List<Integer> borrow_type = new ArrayList<Integer>();//标种集合List
	
	//标的分类：-1无标种，0月标，1天标。相关枚举：EnumRuleBorrowCategory
	private String borrow_category;
	
	//返现方式：1现金，2百分比。相关枚举EnumRewardStatisticsWay
	private int receive_way;
	
	private double receive_account;//返现金额
	
	private double receive_rate;//返现百分比
	
	//返现类型:1定时返现，2自动返现，3人工返现。相关枚举EnumRewardStatisticsBackType
	private int back_type;
	
	private double tender_check_money;//投标限制金额
	
	private int validTime; //有效时间
	
	// v1.6.5.5 RDPROJECT-254 zza 2013-09-29 start
	private String start_time; //开始时间
	
	private String end_time; //结束时间
	// v1.6.5.5 RDPROJECT-254 zza 2013-09-29 end

	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 start
	/**
	 * 用来标记显示的是投标总额还是待还总额(1：待还总额)
	 */
	private int repay_count;
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 end
	
	/**
	 * @return the borrow_type
	 */
	public List<Integer> getBorrow_type() {
		return borrow_type;
	}

	/**
	 * @param borrow_type the borrow_type to set
	 */
	public void setBorrow_type(List<Integer> borrow_type) {
		this.borrow_type = borrow_type;
	}

	public String getBorrow_category() {
		return borrow_category;
	}

	public void setBorrow_category(String borrow_category) {
		this.borrow_category = borrow_category;
	}

	public int getReceive_way() {
		return receive_way;
	}

	public void setReceive_way(int receive_way) {
		this.receive_way = receive_way;
	}

	/**
	 * @return the receive_account
	 */
	public double getReceive_account() {
		return receive_account;
	}

	/**
	 * @param receive_account the receive_account to set
	 */
	public void setReceive_account(double receive_account) {
		this.receive_account = receive_account;
	}

	public double getReceive_rate() {
		return receive_rate;
	}

	public void setReceive_rate(double receive_rate) {
		this.receive_rate = receive_rate;
	}

	/**
	 * @return the back_type
	 */
	public int getBack_type() {
		return back_type;
	}

	/**
	 * @param back_type the back_type to set
	 */
	public void setBack_type(int back_type) {
		this.back_type = back_type;
	}

	/**
	 * @return the tender_check_money
	 */
	public double getTender_check_money() {
		return tender_check_money;
	}

	/**
	 * @param tender_check_money the tender_check_money to set
	 */
	public void setTender_check_money(double tender_check_money) {
		this.tender_check_money = tender_check_money;
	}

	/**
	 * @return the validTime
	 */
	public int getValidTime() {
		return validTime;
	}

	/**
	 * @param validTime the validTime to set
	 */
	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}

	/**
	 * 获取start_time
	 * 
	 * @return start_time
	 */
	public String getStart_time() {
		return start_time;
	}

	/**
	 * 设置start_time
	 * 
	 * @param start_time 要设置的start_time
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	/**
	 * 获取end_time
	 * 
	 * @return end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * 设置end_time
	 * 
	 * @param end_time 要设置的end_time
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * 获取repay_count
	 * 
	 * @return repay_count
	 */
	public int getRepay_count() {
		return repay_count;
	}

	/**
	 * 设置repay_count
	 * 
	 * @param repay_count 要设置的repay_count
	 */
	public void setRepay_count(int repay_count) {
		this.repay_count = repay_count;
	}
	
}
