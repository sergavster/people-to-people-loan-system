package com.p2psys.domain;

import java.io.Serializable;

/**
 * 奖励统计实体类
 */
public class RewardStatistics implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 544257563371787345L;

	/** 主键ID */
	private long id;
	
	/**
	 * 信息状态：  1充值奖励， 2邀请人奖励，  3被邀请人奖励， 4投标奖励， 5回款续投奖励
	 */
	private Byte type;
	
	/**
	 * 收到奖励的会员ID
	 */
	private long reward_user_id;
	
	/**
	 * 发放奖励的会员ID
	 */
	private long passive_user_id;
	
	/**
	 * 应收奖励时间
	 */
	private String receive_time;
	
	/**
	 * 实收奖励时间
	 */
	private String receive_yestime;
	
	/**
	 * 应收金额
	 */
	private double receive_account;
	
	/**
	 * 实收金额
	 */
	private double receive_yesaccount;
	
	/**
	 * 奖励发放状态：1未返现，2已返现，3返现审核不通过，4返现失败，5无用数据
	 */
	private Byte receive_status;
	
	/**
	 * 创建时间
	 */
	private String addtime;
	
	/**
	 * 结束时间
	 */
	private String endtime;
	
	/**
	 * 规则主键ID
	 */
	private long rule_id;
	
	/**
	 * 返现方式：1定时返现，2自动返现，3人工返现
	 */
	private Byte back_type;
	
	/**
	 * 外键
	 */
	private long type_fk_id;
	
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-23 start
	/**
	 * 被邀请人投资总额
	 */
	private double tender_count;
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-23 end
	
	/**
	 * 奖励管理数据是否显示
	 * 0  不显示
	 * 1 显示
	 */
	private int is_show;
	
	public RewardStatistics() {
		super();
	}
	
	public RewardStatistics(long rule_id) {
		super();
		this.rule_id = rule_id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public Byte getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Byte type) {
		this.type = type;
	}

	/**
	 * @return the reward_user_id
	 */
	public long getReward_user_id() {
		return reward_user_id;
	}

	/**
	 * @param reward_user_id the reward_user_id to set
	 */
	public void setReward_user_id(long reward_user_id) {
		this.reward_user_id = reward_user_id;
	}

	/**
	 * @return the passive_user_id
	 */
	public long getPassive_user_id() {
		return passive_user_id;
	}

	/**
	 * @param passive_user_id the passive_user_id to set
	 */
	public void setPassive_user_id(long passive_user_id) {
		this.passive_user_id = passive_user_id;
	}

	/**
	 * @return the receive_time
	 */
	public String getReceive_time() {
		return receive_time;
	}

	/**
	 * @param receive_time the receive_time to set
	 */
	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}

	/**
	 * @return the receive_yestime
	 */
	public String getReceive_yestime() {
		return receive_yestime;
	}

	/**
	 * @param receive_yestime the receive_yestime to set
	 */
	public void setReceive_yestime(String receive_yestime) {
		this.receive_yestime = receive_yestime;
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

	/**
	 * @return the receive_yesaccount
	 */
	public double getReceive_yesaccount() {
		return receive_yesaccount;
	}

	/**
	 * @param receive_yesaccount the receive_yesaccount to set
	 */
	public void setReceive_yesaccount(double receive_yesaccount) {
		this.receive_yesaccount = receive_yesaccount;
	}

	/**
	 * @return the receive_status
	 */
	public Byte getReceive_status() {
		return receive_status;
	}

	/**
	 * @param receive_status the receive_status to set
	 */
	public void setReceive_status(Byte receive_status) {
		this.receive_status = receive_status;
	}

	/**
	 * @return the addtime
	 */
	public String getAddtime() {
		return addtime;
	}

	/**
	 * @param addtime the addtime to set
	 */
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime() {
		return endtime;
	}

	/**
	 * @param endtime the endtime to set
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	/**
	 * @return the rule_id
	 */
	public long getRule_id() {
		return rule_id;
	}

	/**
	 * @param rule_id the rule_id to set
	 */
	public void setRule_id(long rule_id) {
		this.rule_id = rule_id;
	}

	/**
	 * @return the back_type
	 */
	public Byte getBack_type() {
		return back_type;
	}

	/**
	 * @param back_type the back_type to set
	 */
	public void setBack_type(Byte back_type) {
		this.back_type = back_type;
	}

	/**
	 * @return the type_fk_id
	 */
	public long getType_fk_id() {
		return type_fk_id;
	}

	/**
	 * @param type_fk_id the type_fk_id to set
	 */
	public void setType_fk_id(long type_fk_id) {
		this.type_fk_id = type_fk_id;
	}
	
	/**
	 * 获取is_show
	 * 
	 * @return is_show
	 */
	public int getIs_show() {
		return is_show;
	}

	/**
	 * 设置is_show
	 * 
	 * @param is_show 要设置的is_show
	 */
	public void setIs_show(int is_show) {
		this.is_show = is_show;
	}

	/**
	 * 获取tender_count
	 * 
	 * @return tender_count
	 */
	public double getTender_count() {
		return tender_count;
	}

	/**
	 * 设置tender_count
	 * 
	 * @param tender_count 要设置的tender_count
	 */
	public void setTender_count(double tender_count) {
		this.tender_count = tender_count;
	}
}
