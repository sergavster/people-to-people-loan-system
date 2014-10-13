package com.p2psys.domain;

import java.io.Serializable;

/**
 * 积分兑换
 
 * @version 1.0
 * @since 2013-10-15
 */
public class CreditConvert implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 981365383914411084L;
	
	private long id;
	
	//会员ID
	private long user_id;
	
	//兑换的积分数值
	private long credit_value;
	
	//理想的兑换接
	private double convert_money;
	
	//实际的兑换金额
	private double money;
	
	//状态：0未审核，1审核通过，2审核不通过，3无用数据
	private byte status;
	
	//添加时间
	private String addtime;
	
	//审核时间
	private String verify_time;
	
	//审核人
	private String verify_user;
	
	//审核人ID
	private long verify_user_id;
	
	// 备注
	private String remark;
	
	//积分兑换类型
	private String type;
	
	// 兑换数
	private int convert_num;
	
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 start
	/**
	 * 兑换商品ID
	 */
	private Integer goods_id;
	private String goods_name;
	private String pic_url;
	private String valid_value; //用户有效积分
	private String goods_credit_value;//商品单个积分
	public String getGoods_credit_value() {
		return goods_credit_value;
	}
	public void setGoods_credit_value(String goods_credit_value) {
		this.goods_credit_value = goods_credit_value;
	}
	public String getValid_value() {
		return valid_value;
	}
	public void setValid_value(String valid_value) {
		this.valid_value = valid_value;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 end
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

	public long getCredit_value() {
		return credit_value;
	}

	public void setCredit_value(long credit_value) {
		this.credit_value = credit_value;
	}

	public double getConvert_money() {
		return convert_money;
	}

	public void setConvert_money(double convert_money) {
		this.convert_money = convert_money;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}

	public String getVerify_user() {
		return verify_user;
	}

	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}

	public long getVerify_user_id() {
		return verify_user_id;
	}

	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getConvert_num() {
		return convert_num;
	}

	public void setConvert_num(int convert_num) {
		this.convert_num = convert_num;
	}
	
}
