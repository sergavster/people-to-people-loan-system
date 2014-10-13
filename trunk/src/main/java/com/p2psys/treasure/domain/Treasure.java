package com.p2psys.treasure.domain;

import java.io.Serializable;

/**
 * 理财宝实体
 
 * @version 1.0
 * @since 2013-11-27
 */
public class Treasure implements Serializable {

	
	private static final long serialVersionUID = -0L;
	
	//主键ID 
	private long id;
	
	//会员ID 
	private long user_id;
	
	//状态：0停用，1启用 
	private byte status;
	
	// 审核状态：0待审核，1审核通过，2审核不通过
	private byte audit_status;
	
	//标题 
	private String name;
	
	//总金额 
	private double account;
	
	//已投资总额度 
	private double invest;
	
	//类型 
	private String nid;
	
	//利率 
	private double apr;
	
	//利息管理费利率
	private double manager_apr;
	
	//生息间隔时间 ,为零，代表即时生息
	private int interest_time;
	
	//利息计算周期:以天为单位，0代表不启用 
	private int interest_cycle;
	
	//还款方式:0用户自动赎回，1到时一次性还款
	private byte style;
	
	//最多投资金额
	private double most_account;
	
	//最高投资金额
	private double max_account;
	
	//最低投资金额 
	private double min_account;
	
	//借款时间类型:0无限期，1是天，2是月，3是年
	private byte time_limit_type;
	
	//借款时间:0代表无限期 
	private int time_limit;
	
	//详细说明 
	private String content;
	
	//审核时间 
	private long verify_time;
	
	//审核人 
	private long verify_user_id;
	
	//审核备注 
	private String verify_remark;
	
	//赎回方式:0投资份额赎回，1分额赎回
	private long back_type;
	
	//赎回审核类型:0不需要审核，1一审
	private byte back_verify_type;
	
	//赎回限期时间:0代表不期限，随时转入，随时提取。单位：天
	private int back_time;
	
	//添加时间 
	private long add_time;
	
	//修改时间 
	private long update_time;
	
	//操作者 
	private String operator;
	
	//简介
	private String describe;
	
	//理财宝JSON限制条件
	private String rule_check;

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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public double getInvest() {
		return invest;
	}

	public void setInvest(double invest) {
		this.invest = invest;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public int getInterest_time() {
		return interest_time;
	}

	public void setInterest_time(int interest_time) {
		this.interest_time = interest_time;
	}

	public int getInterest_cycle() {
		return interest_cycle;
	}

	public void setInterest_cycle(int interest_cycle) {
		this.interest_cycle = interest_cycle;
	}

	public byte getStyle() {
		return style;
	}

	public void setStyle(byte style) {
		this.style = style;
	}

	public double getMin_account() {
		return min_account;
	}

	public void setMin_account(double min_account) {
		this.min_account = min_account;
	}

	public byte getTime_limit_type() {
		return time_limit_type;
	}

	public void setTime_limit_type(byte time_limit_type) {
		this.time_limit_type = time_limit_type;
	}

	public int getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(int time_limit) {
		this.time_limit = time_limit;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(long verify_time) {
		this.verify_time = verify_time;
	}

	public long getVerify_user_id() {
		return verify_user_id;
	}

	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	public String getVerify_remark() {
		return verify_remark;
	}

	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
	}

	public long getBack_type() {
		return back_type;
	}

	public void setBack_type(long back_type) {
		this.back_type = back_type;
	}

	public byte getBack_verify_type() {
		return back_verify_type;
	}

	public void setBack_verify_type(byte back_verify_type) {
		this.back_verify_type = back_verify_type;
	}

	public long getAdd_time() {
		return add_time;
	}

	public void setAdd_time(long add_time) {
		this.add_time = add_time;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public byte getAudit_status() {
		return audit_status;
	}

	public void setAudit_status(byte audit_status) {
		this.audit_status = audit_status;
	}

	public double getMax_account() {
		return max_account;
	}

	public void setMax_account(double max_account) {
		this.max_account = max_account;
	}

	public double getMost_account() {
		return most_account;
	}

	public void setMost_account(double most_account) {
		this.most_account = most_account;
	}

	public double getManager_apr() {
		return manager_apr;
	}

	public void setManager_apr(double manager_apr) {
		this.manager_apr = manager_apr;
	}

	public int getBack_time() {
		return back_time;
	}

	public void setBack_time(int back_time) {
		this.back_time = back_time;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getRule_check() {
		return rule_check;
	}

	public void setRule_check(String rule_check) {
		this.rule_check = rule_check;
	}
}
