package com.p2psys.model.award;

import java.io.Serializable;

public class Awardee implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = -1299030586222972967L;
	/** 时间 */
	private String time;
	/** 中奖者名 */
	private String name;
	/** 奖品信息 */
	private String award;

	/**
	 * 获取time
	 * 
	 * @return time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * 设置time
	 * 
	 * @param time 要设置的time
	 */
	public void setTime(String time) {
		this.time = time;
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
	 * 获取award
	 * 
	 * @return award
	 */
	public String getAward() {
		return award;
	}

	/**
	 * 设置award
	 * 
	 * @param award 要设置的award
	 */
	public void setAward(String award) {
		this.award = award;
	}

}
