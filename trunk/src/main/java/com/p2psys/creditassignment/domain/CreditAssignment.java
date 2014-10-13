package com.p2psys.creditassignment.domain;

import java.io.Serializable;

/**
 * dw_credit_assignment 实体类
 * 
 
 * @version 1.0
 * @since 2013-12-16
 */ 
public class CreditAssignment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1099444559543089083L;

	/** 主键id */
	private int id;
	
	/** 出售债权用户id */
	private long sell_user_id;
	
	/** 债权相关借款标id */
	private long related_borrow_id;
	
	/** 债权预期价值 */
	private double credit_value;
	
	/** 债权发布价格 */
	private double credit_price;
	
	/** 已销售金额 */
	private double sold_account;
	
	/** 债权转让截止期限 */
	private long buy_end_time;
	
	/** 债权状态：0发布、1审核通过、2审核未通过、3复审通过、4复审不通过、5撤回、6满额待进入复审 */
	private byte status;
	
	/** 债权等级：1普通、2优先 */
	private byte level;
	
	/** 审核时间 */
	private long verify_time;
	
	/** 审核用户id */
	private long verify_user_id;
	
	/** 审核备注 */
	private String verify_remark;
	
	/** 复审时间 */
	private long full_verify_time;
	
	/** 复审用户id */
	private long full_verify_user_id;
	
	/** 复审备注 */
	private String full_verify_remark;
	
	/** 发布时间 */
	private int addtime;
	
	/** 发布者ip */
	private String addip;
	
	/** 债权转让定向密码 */
	private String pwd;

	/** 债权转让投标ID */
	private long related_tender_id;
	
	/** 债权转让待收ID */
	private long related_collection_id;
	
	/** 债权转让类型：1标级别转让，2tender级别转让，3collection级别转出 */
	private byte type;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSell_user_id() {
		return sell_user_id;
	}

	public void setSell_user_id(long sell_user_id) {
		this.sell_user_id = sell_user_id;
	}

	public long getRelated_borrow_id() {
		return related_borrow_id;
	}

	public void setRelated_borrow_id(long related_borrow_id) {
		this.related_borrow_id = related_borrow_id;
	}

	public double getCredit_value() {
		return credit_value;
	}

	public void setCredit_value(double credit_value) {
		this.credit_value = credit_value;
	}

	public double getCredit_price() {
		return credit_price;
	}

	public void setCredit_price(double credit_price) {
		this.credit_price = credit_price;
	}

	public double getSold_account() {
		return sold_account;
	}

	public void setSold_account(double sold_account) {
		this.sold_account = sold_account;
	}

	public long getBuy_end_time() {
		return buy_end_time;
	}

	public void setBuy_end_time(long buy_end_time) {
		this.buy_end_time = buy_end_time;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
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

	public long getFull_verify_time() {
		return full_verify_time;
	}

	public void setFull_verify_time(long full_verify_time) {
		this.full_verify_time = full_verify_time;
	}

	public long getFull_verify_user_id() {
		return full_verify_user_id;
	}

	public void setFull_verify_user_id(long full_verify_user_id) {
		this.full_verify_user_id = full_verify_user_id;
	}

	public String getFull_verify_remark() {
		return full_verify_remark;
	}

	public void setFull_verify_remark(String full_verify_remark) {
		this.full_verify_remark = full_verify_remark;
	}

	public int getAddtime() {
		return addtime;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public long getRelated_tender_id() {
		return related_tender_id;
	}

	public void setRelated_tender_id(long related_tender_id) {
		this.related_tender_id = related_tender_id;
	}

	public long getRelated_collection_id() {
		return related_collection_id;
	}

	public void setRelated_collection_id(long related_collection_id) {
		this.related_collection_id = related_collection_id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
}

