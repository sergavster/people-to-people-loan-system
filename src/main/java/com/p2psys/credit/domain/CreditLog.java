package com.p2psys.credit.domain;

import java.io.Serializable;

/**
 * 积分明细表
 
 *	2013-12-19
 * v1.6.7.2 RDPROJECT-569
 */
public class CreditLog implements Serializable {
	private Integer id;
	private Integer user_id;  //用户ID
	private Integer type_id;  //积分类型
	private long op;    //变动类型 1：增加 2：减少
	private Integer value;  //操作积分
	private Integer valid_value; //有效积分+/- 操作积分
	private String remark;   //备注
	private Integer op_user;  //操作者id
	private Integer addtime;  //操作时间
	private String addip;  //ip
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getType_id() {
		return type_id;
	}
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}
	
	public long getOp() {
		return op;
	}
	public void setOp(long op) {
		this.op = op;
	}
	public Integer getValid_value() {
		return valid_value;
	}
	public void setValid_value(Integer valid_value) {
		this.valid_value = valid_value;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOp_user() {
		return op_user;
	}
	public void setOp_user(Integer op_user) {
		this.op_user = op_user;
	}
	public Integer getAddtime() {
		return addtime;
	}
	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
}
