package com.p2psys.domain;

import java.io.Serializable;

/**
 * 自动投标排名
 * 
 
 * @version 1.0
 * @since 2013-11-15
 */
public class AutoTenderOrder implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2603972601624997167L;

	/**
	 * AutoTenderOrder
	 */
	public AutoTenderOrder() {
	}

	/**
	 * id
	 */
	private long id;
	
	/**
	 * 用户id
	 */
	private long user_id;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 自动投标排名
	 */
	private long auto_order;
	
	/**
	 * 自动投标积分
	 */
	private long auto_score;
	
	/**
	 * 用户可用余额
	 */
	private double use_money;
	
	/**
	 * 可用余额排名
	 */
	private long use_money_order;
	
	/**
	 * 自动投标设置金额
	 */
	private long auto_money;
	
	/**
	 * 自动投标设置金额排名
	 */
	private long auto_money_order;
	
	/**
	 * 用户积分
	 */
	private long user_jifen;
	
	/**
	 * 用户积分排名
	 */
	private long user_jifen_order;
	
	/**
	 * 最后一次自动投标时间
	 */
	private String last_tender_time;

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
	 * 获取user_id
	 * 
	 * @return user_id
	 */
	public long getUser_id() {
		return user_id;
	}

	/**
	 * 设置user_id
	 * 
	 * @param user_id 要设置的user_id
	 */
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	/**
	 * 获取username
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置username
	 * 
	 * @param username 要设置的username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取auto_order
	 * 
	 * @return auto_order
	 */
	public long getAuto_order() {
		return auto_order;
	}

	/**
	 * 设置auto_order
	 * 
	 * @param auto_order 要设置的auto_order
	 */
	public void setAuto_order(long auto_order) {
		this.auto_order = auto_order;
	}

	/**
	 * 获取auto_score
	 * 
	 * @return auto_score
	 */
	public long getAuto_score() {
		return auto_score;
	}

	/**
	 * 设置auto_score
	 * 
	 * @param auto_score 要设置的auto_score
	 */
	public void setAuto_score(long auto_score) {
		this.auto_score = auto_score;
	}

	/**
	 * 获取use_money
	 * 
	 * @return use_money
	 */
	public double getUse_money() {
		return use_money;
	}

	/**
	 * 设置use_money
	 * 
	 * @param use_money 要设置的use_money
	 */
	public void setUse_money(double use_money) {
		this.use_money = use_money;
	}

	/**
	 * 获取use_money_order
	 * 
	 * @return use_money_order
	 */
	public long getUse_money_order() {
		return use_money_order;
	}

	/**
	 * 设置use_money_order
	 * 
	 * @param use_money_order 要设置的use_money_order
	 */
	public void setUse_money_order(long use_money_order) {
		this.use_money_order = use_money_order;
	}

	/**
	 * 获取auto_money
	 * 
	 * @return auto_money
	 */
	public long getAuto_money() {
		return auto_money;
	}

	/**
	 * 设置auto_money
	 * 
	 * @param auto_money 要设置的auto_money
	 */
	public void setAuto_money(long auto_money) {
		this.auto_money = auto_money;
	}

	/**
	 * 获取auto_money_order
	 * 
	 * @return auto_money_order
	 */
	public long getAuto_money_order() {
		return auto_money_order;
	}

	/**
	 * 设置auto_money_order
	 * 
	 * @param auto_money_order 要设置的auto_money_order
	 */
	public void setAuto_money_order(long auto_money_order) {
		this.auto_money_order = auto_money_order;
	}

	/**
	 * 获取user_jifen
	 * 
	 * @return user_jifen
	 */
	public long getUser_jifen() {
		return user_jifen;
	}

	/**
	 * 设置user_jifen
	 * 
	 * @param user_jifen 要设置的user_jifen
	 */
	public void setUser_jifen(long user_jifen) {
		this.user_jifen = user_jifen;
	}

	/**
	 * 获取user_jifen_order
	 * 
	 * @return user_jifen_order
	 */
	public long getUser_jifen_order() {
		return user_jifen_order;
	}

	/**
	 * 设置user_jifen_order
	 * 
	 * @param user_jifen_order 要设置的user_jifen_order
	 */
	public void setUser_jifen_order(long user_jifen_order) {
		this.user_jifen_order = user_jifen_order;
	}

	/**
	 * 获取last_tender_time
	 * 
	 * @return last_tender_time
	 */
	public String getLast_tender_time() {
		return last_tender_time;
	}

	/**
	 * 设置last_tender_time
	 * 
	 * @param last_tender_time 要设置的last_tender_time
	 */
	public void setLast_tender_time(String last_tender_time) {
		this.last_tender_time = last_tender_time;
	}

}
