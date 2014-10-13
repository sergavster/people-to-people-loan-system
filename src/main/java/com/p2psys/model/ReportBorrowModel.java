package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.Borrow;

/**
 * 报表borrow
 
 * 2013-12-20
 *
 */
public class ReportBorrowModel extends Borrow implements Serializable{
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 start
	private String limitDay; 
	private String allTBMoney;//投标总金额
	private Integer countFB; //发标笔数
	private Integer countTB; //投标笔数
	private Integer countRS; //投标人数
	private String bl; //比率  标总额/所有总额
	private Integer tbCount;//天标总数
	private Integer ybCount; //月标总数
	public Integer getTbCount() {
		return tbCount;
	}
	public void setTbCount(Integer tbCount) {
		this.tbCount = tbCount;
	}
	public Integer getYbCount() {
		return ybCount;
	}
	public void setYbCount(Integer ybCount) {
		this.ybCount = ybCount;
	}
	public String getBl() {
		return bl;
	}
	public void setBl(String bl) {
		this.bl = bl;
	}
	public String getLimitDay() {
		return limitDay;
	}
	public void setLimitDay(String limitDay) {
		this.limitDay = limitDay;
	}
		
	public String getAllTBMoney() {
		return allTBMoney;
	}
	public void setAllTBMoney(String allTBMoney) {
		this.allTBMoney = allTBMoney;
	}
	public Integer getCountFB() {
		return countFB;
	}
	public void setCountFB(Integer countFB) {
		this.countFB = countFB;
	}
	public Integer getCountTB() {
		return countTB;
	}
	public void setCountTB(Integer countTB) {
		this.countTB = countTB;
	}
	public Integer getCountRS() {
		return countRS;
	}
	public void setCountRS(Integer countRS) {
		this.countRS = countRS;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 end
}
