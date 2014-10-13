package com.p2psys.domain;

import com.p2psys.util.DateUtils;

/**
 * 急难基金 捐赠记录实体 TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-7-26
 */
public class TroubleFundDonateRecord {
	
	private long id;
	//会员id
	private long user_id;
	//捐赠金额
	private double money;
	//捐赠方式  0：否 1：是
	private long giving_way;
	//显示方式0：匿名1：显示用户名2：显示真实姓名3：显示用户名和真实姓名
	private long display_way;
	//奖励金额
	private double award_money;
	//备注
	private String remark;
	
	private String addtime;
	// 提取现有时间，set addtime
	public TroubleFundDonateRecord() {
		this.setAddtime(DateUtils.getNowTimeStr());
	}

	public double getAward_money() {
		return award_money;
	}

	public void setAward_money(double award_money) {
		this.award_money = award_money;
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

	public long getGiving_way() {
		return giving_way;
	}

	public void setGiving_way(long giving_way) {
		this.giving_way = giving_way;
	}

	public long getDisplay_way() {
		return display_way;
	}

	public void setDisplay_way(long display_way) {
		this.display_way = display_way;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
}
