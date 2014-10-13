package com.p2psys.creditassignment.domain;

import java.io.Serializable;

/**
 * dw_ca_trade_serial 实体类
 * 
 
 * @version 1.0
 * @since 2013-12-20
 */ 
public class CaTradeSerial implements Serializable {
	/** 主键id */
	private int id;
	/** 债权转让id */
	private int ca_id;
	/** 状态：0未成交，1已成交，2撤回 */
	private byte status;
	/** 购买债权的用户id */
	private int buy_user_id;
	/** 购买时间 */
	private int buy_time;
	/** 购买金额 */
	private double buy_account;
	/** 认购债权手续费 */
	private double buy_fee;
	/** 增加时间 */
	private int addtime;
	/** 增加ip */
	private String addip;
	/** 状态：0冻结中，1已收取，2已退回 */
	private byte buy_fee_status;
	/** 销售债权手续费 */
	private double sell_fee;
	/** 状态：0未收取，1已收取 */
	private byte sell_fee_status;

	/**
	 * 获取主键id
	 * 
	 * @return 主键id
	 */
	public int getId(){
		return id;
	}

	/**
	 * 设置主键id
	 * 
	 * @param id 要设置的主键id
	 */
	public void setId(int id){
		this.id=id;
	}

	/**
	 * 获取债权转让id
	 * 
	 * @return 债权转让id
	 */
	public int getCa_id(){
		return ca_id;
	}

	/**
	 * 设置债权转让id
	 * 
	 * @param ca_id 要设置的债权转让id
	 */
	public void setCa_id(int ca_id){
		this.ca_id=ca_id;
	}

	/**
	 * 获取状态：0未成交，1已成交，2撤回
	 * 
	 * @return 状态：0未成交，1已成交，2撤回
	 */
	public byte getStatus(){
		return status;
	}

	/**
	 * 设置状态：0未成交，1已成交，2撤回
	 * 
	 * @param status 要设置的状态：0未成交，1已成交，2撤回
	 */
	public void setStatus(byte status){
		this.status=status;
	}

	/**
	 * 获取购买债权的用户id
	 * 
	 * @return 购买债权的用户id
	 */
	public int getBuy_user_id(){
		return buy_user_id;
	}

	/**
	 * 设置购买债权的用户id
	 * 
	 * @param buy_user_id 要设置的购买债权的用户id
	 */
	public void setBuy_user_id(int buy_user_id){
		this.buy_user_id=buy_user_id;
	}

	/**
	 * 获取购买时间
	 * 
	 * @return 购买时间
	 */
	public int getBuy_time(){
		return buy_time;
	}

	/**
	 * 设置购买时间
	 * 
	 * @param buy_time 要设置的购买时间
	 */
	public void setBuy_time(int buy_time){
		this.buy_time=buy_time;
	}

	/**
	 * 获取购买金额
	 * 
	 * @return 购买金额
	 */
	public double getBuy_account(){
		return buy_account;
	}

	/**
	 * 设置购买金额
	 * 
	 * @param buy_account 要设置的购买金额
	 */
	public void setBuy_account(double buy_account){
		this.buy_account=buy_account;
	}

	/**
	 * 获取认购债权手续费
	 * 
	 * @return 认购债权手续费
	 */
	public double getBuy_fee(){
		return buy_fee;
	}

	/**
	 * 设置认购债权手续费
	 * 
	 * @param buy_fee 要设置的认购债权手续费
	 */
	public void setBuy_fee(double buy_fee){
		this.buy_fee=buy_fee;
	}

	/**
	 * 获取增加时间
	 * 
	 * @return 增加时间
	 */
	public int getAddtime(){
		return addtime;
	}

	/**
	 * 设置增加时间
	 * 
	 * @param addtime 要设置的增加时间
	 */
	public void setAddtime(int addtime){
		this.addtime=addtime;
	}

	/**
	 * 获取增加ip
	 * 
	 * @return 增加ip
	 */
	public String getAddip(){
		return addip;
	}

	/**
	 * 设置增加ip
	 * 
	 * @param addip 要设置的增加ip
	 */
	public void setAddip(String addip){
		this.addip=addip;
	}

	/**
	 * 获取状态：0冻结中，1已收取，2已退回
	 * 
	 * @return 状态：0冻结中，1已收取，2已退回
	 */
	public byte getBuy_fee_status(){
		return buy_fee_status;
	}

	/**
	 * 设置状态：0冻结中，1已收取，2已退回
	 * 
	 * @param buy_fee_status 要设置的状态：0冻结中，1已收取，2已退回
	 */
	public void setBuy_fee_status(byte buy_fee_status){
		this.buy_fee_status=buy_fee_status;
	}

	/**
	 * 获取销售债权手续费
	 * 
	 * @return 销售债权手续费
	 */
	public double getSell_fee(){
		return sell_fee;
	}

	/**
	 * 设置销售债权手续费
	 * 
	 * @param sell_fee 要设置的销售债权手续费
	 */
	public void setSell_fee(double sell_fee){
		this.sell_fee=sell_fee;
	}

	/**
	 * 获取状态：0未收取，1已收取
	 * 
	 * @return 状态：0未收取，1已收取
	 */
	public byte getSell_fee_status(){
		return sell_fee_status;
	}

	/**
	 * 设置状态：0未收取，1已收取
	 * 
	 * @param sell_fee_status 要设置的状态：0未收取，1已收取
	 */
	public void setSell_fee_status(byte sell_fee_status){
		this.sell_fee_status=sell_fee_status;
	}
}

