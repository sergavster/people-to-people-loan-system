package com.p2psys.domain;

import java.io.Serializable;

public class UserCreditLog implements Serializable {
	
	private static final long serialVersionUID = 4019823126620424066L;
	private long id;
	private long user_id;
	private long type_id;
	private long op;
	private long value;
	
	/**
	 * 有效积分
	 */
	private long valid_value;
	private String remark;
	private long op_user;
	private long addtime;
	private String addip;

	public UserCreditLog() {
		super();
	}
	
	public UserCreditLog(long user_id, long op,long addtime,String addip) {
		super();
		this.user_id = user_id;
		this.op = op;
		this.addtime=addtime;
		this.addip=addip;
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

	public long getType_id() {
		return type_id;
	}

	public void setType_id(long type_id) {
		this.type_id = type_id;
	}

	public long getOp() {
		return op;
	}

	public void setOp(long op) {
		this.op = op;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getValid_value() {
		return valid_value;
	}

	public void setValid_value(long valid_value) {
		this.valid_value = valid_value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getOp_user() {
		return op_user;
	}

	public void setOp_user(long op_user) {
		this.op_user = op_user;
	}

	public long getAddtime() {
		return addtime;
	}

	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}
	
}
