package com.p2psys.domain;

import java.io.Serializable;

import com.p2psys.util.DateUtils;
/**
 * 急难基金 基金收支实体
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-7-26
 */
public class TroubleDonateRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	//会员id
	private long user_id;
	//基金收支金额
	private double money;
	//借款日期
	private String borrow_time;
	//用途
	private String borrow_use;
    //约定还款日期
	private String repayment_time;
	//借款情况
	private String borrow_content;
	//备注
	private String remark;
	//收支方式 0：收入 1：支出
	private long status;
	
	private String addtime;
	// 提取现有时间，set addtime
	public TroubleDonateRecord() {
		this.setAddtime(DateUtils.getNowTimeStr());
	}
	
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getBorrow_time() {
		return borrow_time;
	}

	public void setBorrow_time(String borrow_time) {
		this.borrow_time = borrow_time;
	}

	public String getBorrow_use() {
		return borrow_use;
	}

	public void setBorrow_use(String borrow_use) {
		this.borrow_use = borrow_use;
	}

	public String getRepayment_time() {
		return repayment_time;
	}

	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}

	public String getBorrow_content() {
		return borrow_content;
	}

	public void setBorrow_content(String borrow_content) {
		this.borrow_content = borrow_content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

}
