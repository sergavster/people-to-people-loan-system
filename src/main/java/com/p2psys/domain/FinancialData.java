package com.p2psys.domain;

import java.io.Serializable;

/**
 * dw_financial_data 实体类
 * 
 
 * @version 1.0
 * @since 2013-12-12
 */
public class FinancialData implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -8016497578289915726L;
	/**
	 * 主键ID
	 */
	private int id;
	/**
	 * 数据类型:1一周内应收回欠款，2逾期10天内未归还，3逾期10天以上未归还，4逾期已归还
	 */
	private byte type;
	/**
	 * 借款ID
	 */
	private int borrow_id;
	/**
	 * 借款人
	 */
	private String username;
	/**
	 * 应归还金额
	 */
	private double repayment_account;
	/**
	 * 应归还日期
	 */
	private int repayment_time;
	/**
	 * 已归还日期
	 */
	private int repayment_yestime;

	/**
	 * 获取主键ID
	 * 
	 * @return 主键ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * 设置主键ID
	 * 
	 * @param id 要设置的主键ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 获取数据类型:1一周内应收回欠款，2逾期10天内未归还，3逾期10天以上未归还，4逾期已归还
	 * 
	 * @return 数据类型
	 */
	public byte getType() {
		return type;
	}

	/**
	 * 设置数据类型:1一周内应收回欠款，2逾期10天内未归还，3逾期10天以上未归还，4逾期已归还
	 * 
	 * @param type 要设置的数据类型
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * 获取借款ID
	 * 
	 * @return 借款ID
	 */
	public int getBorrow_id() {
		return borrow_id;
	}

	/**
	 * 设置借款ID
	 * 
	 * @param borrow_id 要设置的借款ID
	 */
	public void setBorrow_id(int borrow_id) {
		this.borrow_id = borrow_id;
	}

	/**
	 * 获取借款人
	 * 
	 * @return 借款人
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置借款人
	 * 
	 * @param username 要设置的借款人
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取应归还金额
	 * 
	 * @return 应归还金额
	 */
	public double getRepayment_account() {
		return repayment_account;
	}

	/**
	 * 设置应归还金额
	 * 
	 * @param repayment_account 要设置的应归还金额
	 */
	public void setRepayment_account(double repayment_account) {
		this.repayment_account = repayment_account;
	}

	/**
	 * 获取应归还日期
	 * 
	 * @return 应归还日期
	 */
	public int getRepayment_time() {
		return repayment_time;
	}

	/**
	 * 设置应归还日期
	 * 
	 * @param repayment_time 要设置的应归还日期
	 */
	public void setRepayment_time(int repayment_time) {
		this.repayment_time = repayment_time;
	}

	/**
	 * 获取已归还日期
	 * 
	 * @return 已归还日期
	 */
	public int getRepayment_yestime() {
		return repayment_yestime;
	}

	/**
	 * 设置已归还日期
	 * 
	 * @param repayment_yestime 要设置的已归还日期
	 */
	public void setRepayment_yestime(int repayment_yestime) {
		this.repayment_yestime = repayment_yestime;
	}
}

