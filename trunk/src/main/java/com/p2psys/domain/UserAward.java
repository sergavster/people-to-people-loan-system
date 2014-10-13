package com.p2psys.domain;

/**
 * 抽奖用户类
 * 
 
 * @version 1.0
 * @since 2013-7-23
 */
public class UserAward {
	/** 主键 */
	private long id;
	/** 抽奖用户ID */
	private long user_id;
	/** 抽奖用户 */
	private String user_name;
	/** 奖品级别 */
	private int level;
	/** 奖品id */
	private long award_id;
	/** 抽奖消耗点数 */
	private long point_reduce;
	/** 规则ID */
	private long rule_id;
	/** 奖品名 */
	private String award_name;
	/** 是否中奖:0不中，1中 */
	private int status;
	/** 奖品发放状态:0未发送，1已发送 */
	private int receive_status;
	/** 创建时间 */
	private String addtime;
	/** 创建IP */
	private String addip;

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
	 * 获取user_name
	 * 
	 * @return user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * 设置user_name
	 * 
	 * @param user_name 要设置的user_name
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * 获取level
	 * 
	 * @return level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 设置level
	 * 
	 * @param level 要设置的level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 获取award_id
	 * 
	 * @return award_id
	 */
	public long getAward_id() {
		return award_id;
	}

	/**
	 * 设置award_id
	 * 
	 * @param award_id 要设置的award_id
	 */
	public void setAward_id(long award_id) {
		this.award_id = award_id;
	}

	/**
	 * 获取point_reduce
	 * 
	 * @return point_reduce
	 */
	public long getPoint_reduce() {
		return point_reduce;
	}

	/**
	 * 设置point_reduce
	 * 
	 * @param point_reduce 要设置的point_reduce
	 */
	public void setPoint_reduce(long point_reduce) {
		this.point_reduce = point_reduce;
	}

	/**
	 * 获取rule_id
	 * 
	 * @return rule_id
	 */
	public long getRule_id() {
		return rule_id;
	}

	/**
	 * 设置rule_id
	 * 
	 * @param rule_id 要设置的rule_id
	 */
	public void setRule_id(long rule_id) {
		this.rule_id = rule_id;
	}

	/**
	 * 获取award_name
	 * 
	 * @return award_name
	 */
	public String getAward_name() {
		return award_name;
	}

	/**
	 * 设置award_name
	 * 
	 * @param award_name 要设置的award_name
	 */
	public void setAward_name(String award_name) {
		this.award_name = award_name;
	}

	/**
	 * 获取status
	 * 
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置status
	 * 
	 * @param status 要设置的status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取receive_status
	 * 
	 * @return receive_status
	 */
	public int getReceive_status() {
		return receive_status;
	}

	/**
	 * 设置receive_status
	 * 
	 * @param receive_status 要设置的receive_status
	 */
	public void setReceive_status(int receive_status) {
		this.receive_status = receive_status;
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
	 * 获取addip
	 * 
	 * @return addip
	 */
	public String getAddip() {
		return addip;
	}

	/**
	 * 设置addip
	 * 
	 * @param addip 要设置的addip
	 */
	public void setAddip(String addip) {
		this.addip = addip;
	}

}
