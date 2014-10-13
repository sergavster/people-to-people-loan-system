package com.p2psys.domain;

import java.io.Serializable;

/**
 * dw_advanced 实体类
 * 
 
 * @version 1.0
 * @since 2013-12-16
 */ 
public class Advanced implements Serializable {
	/**
	 * 主键
	 */
	private int id;
	/**
	 * 风险准备金
	 */
	private double advance_reserve;
	/**
	 * 垫付风险金
	 */
	private double no_advanced_account;
	/**
	 * 融资总额
	 */
	private double borrow_total;
	/**
	 * 待还款总额
	 */
	private double wait_total;
	/**
	 * 
	 */
	private double borrow_day_total;

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public int getId(){
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(int id){
		this.id=id;
	}

	/**
	 * 获取风险准备金
	 * 
	 * @return 风险准备金
	 */
	public double getAdvance_reserve(){
		return advance_reserve;
	}

	/**
	 * 设置风险准备金
	 * 
	 * @param advance_reserve 要设置的风险准备金
	 */
	public void setAdvance_reserve(double advance_reserve){
		this.advance_reserve=advance_reserve;
	}

	/**
	 * 获取垫付风险金
	 * 
	 * @return 垫付风险金
	 */
	public double getNo_advanced_account(){
		return no_advanced_account;
	}

	/**
	 * 设置垫付风险金
	 * 
	 * @param no_advanced_account 要设置的垫付风险金
	 */
	public void setNo_advanced_account(double no_advanced_account){
		this.no_advanced_account=no_advanced_account;
	}

	/**
	 * 获取融资总额
	 * 
	 * @return 融资总额
	 */
	public double getBorrow_total(){
		return borrow_total;
	}

	/**
	 * 设置融资总额
	 * 
	 * @param borrow_total 要设置的融资总额
	 */
	public void setBorrow_total(double borrow_total){
		this.borrow_total=borrow_total;
	}

	/**
	 * 获取待还款总额
	 * 
	 * @return 待还款总额
	 */
	public double getWait_total(){
		return wait_total;
	}

	/**
	 * 设置待还款总额
	 * 
	 * @param wait_total 要设置的待还款总额
	 */
	public void setWait_total(double wait_total){
		this.wait_total=wait_total;
	}

	/**
	 * 获取
	 * 
	 * @return 
	 */
	public double getBorrow_day_total(){
		return borrow_day_total;
	}

	/**
	 * 设置
	 * 
	 * @param borrow_day_total 要设置的
	 */
	public void setBorrow_day_total(double borrow_day_total){
		this.borrow_day_total=borrow_day_total;
	}
}

