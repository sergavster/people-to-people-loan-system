package com.p2psys.domain;

import java.io.Serializable;

public class CreditType implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//
	private long id;
	
	//积分名称 
	private String name;
	
	//积分代码 
	private String nid;
	
	//积分数值 
	private long value;
	
	//积分周期，1:一次,2:每天,3:间隔分钟,4:不限 
	private byte cycle;
	
	//奖励次数,0:不限 
	private byte award_times;
	
	//时间间隔，单位分钟 
	private long interval;
	
	//备注 
	private String remark;
	
	//操作者 
	private long op_user;
	
	//添加时间 
	private long addtime;
	
	//添加IP 
	private String addip;
	
	//最后更新时间 
	private long updatetime;
	
	//最后更新ID 
	private String updateip;
	
	//规则ID 
	private String rule_nid;
	
	//积分种类:1投资积分,2借款积分,3赠送积分,4消费积分 ,5可用积分,6总积分,7论坛积分
	private byte credit_category;

	/**
	 * 获取id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置id
	 * 
	 * @param id 要设置的id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置name
	 * 
	 * @param name 要设置的name
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * 获取value
	 * 
	 * @return value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * 设置value
	 * 
	 * @param value 要设置的value
	 */
	public void setValue(long value) {
		this.value = value;
	}

	/**
	 * 获取cycle
	 * 
	 * @return cycle
	 */
	public byte getCycle() {
		return cycle;
	}

	/**
	 * 设置cycle
	 * 
	 * @param cycle 要设置的cycle
	 */
	public void setCycle(byte cycle) {
		this.cycle = cycle;
	}

	/**
	 * 获取award_times
	 * 
	 * @return award_times
	 */
	public byte getAward_times() {
		return award_times;
	}

	/**
	 * 设置award_times
	 * 
	 * @param award_times 要设置的award_times
	 */
	public void setAward_times(byte award_times) {
		this.award_times = award_times;
	}

	/**
	 * 获取interval
	 * 
	 * @return interval
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * 设置interval
	 * 
	 * @param interval 要设置的interval
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * 获取remark
	 * 
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置remark
	 * 
	 * @param remark 要设置的remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取op_user
	 * 
	 * @return op_user
	 */
	public long getOp_user() {
		return op_user;
	}

	/**
	 * 设置op_user
	 * 
	 * @param op_user 要设置的op_user
	 */
	public void setOp_user(long op_user) {
		this.op_user = op_user;
	}

	/**
	 * 获取addtime
	 * 
	 * @return addtime
	 */
	public long getAddtime() {
		return addtime;
	}

	/**
	 * 设置addtime
	 * 
	 * @param addtime 要设置的addtime
	 */
	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}

	/**
	 * 获取addip
	 * 
	 * @return addip
	 */
	public String getAddip() {
		return addip;
	}

	/**
	 * 设置addip
	 * 
	 * @param addip 要设置的addip
	 */
	public void setAddip(String addip) {
		this.addip = addip;
	}

	/**
	 * 获取updatetime
	 * 
	 * @return updatetime
	 */
	public long getUpdatetime() {
		return updatetime;
	}

	/**
	 * 设置updatetime
	 * 
	 * @param updatetime 要设置的updatetime
	 */
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}

	/**
	 * 获取updateip
	 * 
	 * @return updateip
	 */
	public String getUpdateip() {
		return updateip;
	}

	/**
	 * 设置updateip
	 * 
	 * @param updateip 要设置的updateip
	 */
	public void setUpdateip(String updateip) {
		this.updateip = updateip;
	}

	/**
	 * 获取rule_nid
	 * 
	 * @return rule_nid
	 */
	public String getRule_nid() {
		return rule_nid;
	}

	/**
	 * 设置rule_nid
	 * 
	 * @param rule_nid 要设置的rule_nid
	 */
	public void setRule_nid(String rule_nid) {
		this.rule_nid = rule_nid;
	}

	/**
	 * 获取credit_category
	 * 
	 * @return credit_category
	 */
	public byte getCredit_category() {
		return credit_category;
	}

	/**
	 * 设置credit_category
	 * 
	 * @param credit_category 要设置的credit_category
	 */
	public void setCredit_category(byte credit_category) {
		this.credit_category = credit_category;
	}

}
