package com.p2psys.model.award;

import java.io.Serializable;

import com.p2psys.common.enums.EnumAwardErrorType;

/**
 * 抽奖结果类
 */
public class AwardResult implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = 1708885841467808090L;
	/** 是否成功 */
	private String is_success = "F";
	/** 错误代码 */
	private EnumAwardErrorType error;
	/** 奖品等级 */
	private String level_no;
	/** 奖品名 */
	private String name;
	/** 中奖金额 */
	private String money;
	/** 剩余抽奖次数 */
	private String times;

	/**
	 * 获取is_success
	 * 
	 * @return is_success
	 */
	public String getIs_success() {
		return is_success;
	}

	/**
	 * 设置is_success
	 * 
	 * @param is_success 要设置的is_success
	 */
	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	/**
	 * 获取error
	 * 
	 * @return error
	 */
	public EnumAwardErrorType getError() {
		return error;
	}

	/**
	 * 设置error
	 * 
	 * @param error 要设置的error
	 */
	public void setError(EnumAwardErrorType error) {
		this.error = error;
	}

	/**
	 * 获取level_no
	 * 
	 * @return level_no
	 */
	public String getLevel_no() {
		return level_no;
	}

	/**
	 * 设置level_no
	 * 
	 * @param level_no 要设置的level_no
	 */
	public void setLevel_no(String level_no) {
		this.level_no = level_no;
	}

	/**
	 * 获取name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置name
	 * 
	 * @param name 要设置的name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取money
	 * 
	 * @return money
	 */
	public String getMoney() {
		return money;
	}

	/**
	 * 设置money
	 * 
	 * @param money 要设置的money
	 */
	public void setMoney(String money) {
		this.money = money;
	}

	/**
	 * 获取times
	 * 
	 * @return times
	 */
	public String getTimes() {
		return times;
	}

	/**
	 * 设置times
	 * 
	 * @param times 要设置的times
	 */
	public void setTimes(String times) {
		this.times = times;
	}

}
