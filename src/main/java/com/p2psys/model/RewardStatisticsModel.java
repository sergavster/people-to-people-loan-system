package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.RewardStatistics;

/**
 * 奖励统计实体类
 */
public class RewardStatisticsModel extends RewardStatistics implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 544257563371787345L;

	// v1.6.6.2 RDPROJECT-338 zza 2013-10-16 start
	/**
	 * 获得奖励用户
	 */
	private String username;
	
	/**
	 * 发放奖励者
	 */
	private String passive_username;
	
	private long type_fk_id;
	
	/**
	 * 投资总额
	 */
	private double sumMoney;
	
	/**
	 * 达到多少投资总额
	 */
	private double check_money;
	
	/**
	 * 规则nid
	 */
	private String nid;
	
	// v1.6.6.2 RDPROJECT-338 zza 2013-10-16 end

	/**
	 * 获取username
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置username
	 * 
	 * @param username 要设置的username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取passive_username
	 * 
	 * @return passive_username
	 */
	public String getPassive_username() {
		return passive_username;
	}

	/**
	 * 设置passive_username
	 * 
	 * @param passive_username 要设置的passive_username
	 */
	public void setPassive_username(String passive_username) {
		this.passive_username = passive_username;
	}

	/**
	 * 获取type_fk_id
	 * 
	 * @return type_fk_id
	 */
	public long getType_fk_id() {
		return type_fk_id;
	}

	/**
	 * 设置type_fk_id
	 * 
	 * @param type_fk_id 要设置的type_fk_id
	 */
	public void setType_fk_id(long type_fk_id) {
		this.type_fk_id = type_fk_id;
	}

	/**
	 * 获取sumMoney
	 * 
	 * @return sumMoney
	 */
	public double getSumMoney() {
		return sumMoney;
	}

	/**
	 * 设置sumMoney
	 * 
	 * @param sumMoney 要设置的sumMoney
	 */
	public void setSumMoney(double sumMoney) {
		this.sumMoney = sumMoney;
	}

	/**
	 * 获取check_money
	 * 
	 * @return check_money
	 */
	public double getCheck_money() {
		return check_money;
	}

	/**
	 * 设置check_money
	 * 
	 * @param check_money 要设置的check_money
	 */
	public void setCheck_money(double check_money) {
		this.check_money = check_money;
	}

	/**
	 * 获取nid
	 * 
	 * @return nid
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置nid
	 * 
	 * @param nid 要设置的nid
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}
	
}
