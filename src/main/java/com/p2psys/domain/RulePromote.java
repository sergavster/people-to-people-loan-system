package com.p2psys.domain;

import java.io.Serializable;

/**
 * 推广奖励规则
 * 
 
 * @version 1.0
 * @since 2013-10-16
 */
public class RulePromote implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8534703256181055554L;
	
	/**
	 * 规则id
	 */
	private long id;
	
	/**
	 * 规则名
	 */
	private String name;
	
	/**
	 * 状态：0不启用，1启用
	 */
	private long status;
	
	/**
	 * 邀请成功需要达到的人数(开始)
	 */
	private long count_up;
	
	/**
	 * 邀请成功需要达到的人数（结束）
	 */
	private long count_down;
	
	/**
	 * 获得奖励的百分比
	 */
	private double award_percent;
	
	/**
	 * 添加时间
	 */
	private String addtime;
	
	/**
	 * 备注
	 */
	private String remark;

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
	 * 获取status
	 * 
	 * @return status
	 */
	public long getStatus() {
		return status;
	}

	/**
	 * 设置status
	 * 
	 * @param status 要设置的status
	 */
	public void setStatus(long status) {
		this.status = status;
	}

	/**
	 * 获取count_up
	 * 
	 * @return count_up
	 */
	public long getCount_up() {
		return count_up;
	}

	/**
	 * 设置count_up
	 * 
	 * @param count_up 要设置的count_up
	 */
	public void setCount_up(long count_up) {
		this.count_up = count_up;
	}

	/**
	 * 获取count_down
	 * 
	 * @return count_down
	 */
	public long getCount_down() {
		return count_down;
	}

	/**
	 * 设置count_down
	 * 
	 * @param count_down 要设置的count_down
	 */
	public void setCount_down(long count_down) {
		this.count_down = count_down;
	}

	/**
	 * 获取award_percent
	 * 
	 * @return award_percent
	 */
	public double getAward_percent() {
		return award_percent;
	}

	/**
	 * 设置award_percent
	 * 
	 * @param award_percent 要设置的award_percent
	 */
	public void setAward_percent(double award_percent) {
		this.award_percent = award_percent;
	}

	/**
	 * 获取addtime
	 * 
	 * @return addtime
	 */
	public String getAddtime() {
		return addtime;
	}

	/**
	 * 设置addtime
	 * 
	 * @param addtime 要设置的addtime
	 */
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	/**
	 * 获取remark
	 * 
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置remark
	 * 
	 * @param remark 要设置的remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
