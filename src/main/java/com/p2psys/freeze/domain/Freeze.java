package com.p2psys.freeze.domain;

/**
 * 用户冻结实体类
 
 * @version 1.0
 * @since 2013-10-29
 */
public class Freeze {

	/** 主键*/
	private long id;
	/** 用户ID*/
	private long userId;
	/** 操作人用户ID*/
	private long verifyUserId;
	/** 状态(0:未启用,1:启用)*/
	private byte status;
	/** 冻结标示*/
	private String mark;
	/** 备注*/
	private String remark;
	/** 添加时间*/
	private int addTime;
	/** 添加IP*/
	private String addIp;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getVerifyUserId() {
		return verifyUserId;
	}
	public void setVerifyUserId(long verifyUserId) {
		this.verifyUserId = verifyUserId;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getAddTime() {
		return addTime;
	}
	public void setAddTime(int addTime) {
		this.addTime = addTime;
	}
	public String getAddIp() {
		return addIp;
	}
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	
	
}
