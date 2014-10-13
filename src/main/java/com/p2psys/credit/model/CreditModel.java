package com.p2psys.credit.model;

import java.io.Serializable;

public class CreditModel implements Serializable{
	
	/**
	 * 用户Id
	 */
	private Integer user_id;
	/**
	 *  积分值
	 */
	private Integer value;
	/**
	 * 操作者 
	 */
	private Integer op_user;
	/**
	 * 添加时间
	 */
	private Integer addtime;
	/**
	 * 添加IP
	 */
	private String addip;
	/**
	 * 修改时间
	 */
	private String updatetime;
	/**
	 * 修改IP
	 */
	private String updateip;
	/**
	 * 发标积分  
	 */
	private Integer tender_value;
	/**
	 * 借款积分
	 */
	private Integer borrow_value;
	/**
	 * 赠送积分  
	 */
	private Integer gift_value;
	/**
	 * 消费积分
	 */
	private Integer expense_value;
	/**
	 * 有效积分 
	 */
	private Integer valid_value;
	/**
	 * 论坛积分  
	 */
	private Integer bbs_value;
	/**
	 * 等级
	 */
	private long user_credit_level;
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
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
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdateip() {
		return updateip;
	}
	public void setUpdateip(String updateip) {
		this.updateip = updateip;
	}
	public Integer getTender_value() {
		return tender_value;
	}
	public void setTender_value(Integer tender_value) {
		this.tender_value = tender_value;
	}
	public Integer getBorrow_value() {
		return borrow_value;
	}
	public void setBorrow_value(Integer borrow_value) {
		this.borrow_value = borrow_value;
	}
	public Integer getGift_value() {
		return gift_value;
	}
	public void setGift_value(Integer gift_value) {
		this.gift_value = gift_value;
	}
	public Integer getExpense_value() {
		return expense_value;
	}
	public void setExpense_value(Integer expense_value) {
		this.expense_value = expense_value;
	}
	public Integer getValid_value() {
		return valid_value;
	}
	public void setValid_value(Integer valid_value) {
		this.valid_value = valid_value;
	}
	public Integer getBbs_value() {
		return bbs_value;
	}
	public void setBbs_value(Integer bbs_value) {
		this.bbs_value = bbs_value;
	}
	public long getUser_credit_level() {
		return user_credit_level;
	}
	public void setUser_credit_level(long user_credit_level) {
		this.user_credit_level = user_credit_level;
	}
	
}
