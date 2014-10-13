package com.p2psys.domain;

import java.io.Serializable;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class UserCredit implements Serializable {
	
	private static final long serialVersionUID = 4019823126620424066L;
	
	//会员ID做主键
	private long user_id;
	
	//总计积分
	private int value;
	
	//操作者
	private int op_user;
	
	//添加时间
	private long addtime;
	
	//添加者IP
	private String addip;
	
	//修改时间
	private String updatetime;
	
	//修改者IP
	private String updateip;
	
	//投资积分
	private int tender_value;
	
	//借款积分
	private int borrow_value;
	
	//赠送积分
	private int gift_value;
	
	//消费积分
	private int expense_value;
	
	//有效积分
	private int valid_value;
	
	//论坛积分
	private int bbs_value;
	
	//会员信用等级
	private int user_credit_level;
	
	public UserCredit() {
		super();
	}

	public UserCredit(long user_id, int value, long addtime, String addip) {
		super();
		this.user_id = user_id;
		this.value = value;
		this.addtime = addtime;
		this.addip = addip;
	}
	
	public int getBbs_value() {
		return bbs_value;
	}

	public void setBbs_value(int bbs_value) {
		this.bbs_value = bbs_value;
	}

	public int getValid_value() {
		return valid_value;
	}

	public void setValid_value(int valid_value) {
		this.valid_value = valid_value;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getOp_user() {
		return op_user;
	}

	public void setOp_user(int op_user) {
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

	public int getTender_value() {
		return tender_value;
	}

	public void setTender_value(int tender_value) {
		this.tender_value = tender_value;
	}

	/**
	 * 获取borrow_value
	 * 
	 * @return borrow_value
	 */
	public int getBorrow_value() {
		return borrow_value;
	}

	/**
	 * 设置borrow_value
	 * 
	 * @param borrow_value 要设置的borrow_value
	 */
	public void setBorrow_value(int borrow_value) {
		this.borrow_value = borrow_value;
	}

	/**
	 * 获取gift_value
	 * 
	 * @return gift_value
	 */
	public int getGift_value() {
		return gift_value;
	}

	/**
	 * 设置gift_value
	 * 
	 * @param gift_value 要设置的gift_value
	 */
	public void setGift_value(int gift_value) {
		this.gift_value = gift_value;
	}

	/**
	 * 获取expense_value
	 * 
	 * @return expense_value
	 */
	public int getExpense_value() {
		return expense_value;
	}

	/**
	 * 设置expense_value
	 * 
	 * @param expense_value 要设置的expense_value
	 */  
	public void setExpense_value(int expense_value) {
		this.expense_value = expense_value;
	}

	public int getUser_credit_level() {
		return user_credit_level;
	}

	public void setUser_credit_level(int user_credit_level) {
		this.user_credit_level = user_credit_level;
	}
}
