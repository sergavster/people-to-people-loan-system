package com.p2psys.domain;

/**
 * 抽奖规则类
 * 
 
 * @version 1.0
 * @since 2013-7-23
 */
public class RuleAward {
	/** 主键 */
	private long id;
	/** 规则名 */
	private String name;
	/** 抽奖有效时间起 */
	private String start_date;
	/** 抽奖有效时间止 */
	private String end_date;
	/** 抽奖类型:1按积分抽奖,2按次数抽奖,3按倍率抽奖 */
	private int award_type;
	/** 抽奖次数限制:0不限制,1限制用户总次数,2限制当天总次数 */
	private int time_limit;
	/** 最多抽奖次数 */
	private int max_times;
	/** 基准积分 */
	private int base_point;
	/** 金额限制 */
	private int money_limit;
	/** 总金额 */
	private double total_money;
	/** 领用金额 */
	private double bestow_money;
	/** 是否100%中奖 */
	private int is_absolute;
	/** 中奖提醒 0:不需要,1:站内信,2:短信,3:邮件 */
	private int msg_type;
	/** 发送中奖提醒信息内容模板 */
	private String context;
	/** 发送中奖提醒信息内容模板标题 */
	private String subject;
	/** 规则描叙 */
	private String content;
	/** 返现方式 */
	private int back_type;
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
	 * 获取start_date
	 * 
	 * @return start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * 设置start_date
	 * 
	 * @param start_date 要设置的start_date
	 */
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	/**
	 * 获取end_date
	 * 
	 * @return end_date
	 */
	public String getEnd_date() {
		return end_date;
	}

	/**
	 * 设置end_date
	 * 
	 * @param end_date 要设置的end_date
	 */
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	/**
	 * 获取award_type
	 * 
	 * @return award_type
	 */
	public int getAward_type() {
		return award_type;
	}

	/**
	 * 设置award_type
	 * 
	 * @param award_type 要设置的award_type
	 */
	public void setAward_type(int award_type) {
		this.award_type = award_type;
	}

	/**
	 * 获取time_limit
	 * 
	 * @return time_limit
	 */
	public int getTime_limit() {
		return time_limit;
	}

	/**
	 * 设置time_limit
	 * 
	 * @param time_limit 要设置的time_limit
	 */
	public void setTime_limit(int time_limit) {
		this.time_limit = time_limit;
	}

	/**
	 * 获取max_times
	 * 
	 * @return max_times
	 */
	public int getMax_times() {
		return max_times;
	}

	/**
	 * 设置max_times
	 * 
	 * @param max_times 要设置的max_times
	 */
	public void setMax_times(int max_times) {
		this.max_times = max_times;
	}

	/**
	 * 获取base_point
	 * 
	 * @return base_point
	 */
	public int getBase_point() {
		return base_point;
	}

	/**
	 * 设置base_point
	 * 
	 * @param base_point 要设置的base_point
	 */
	public void setBase_point(int base_point) {
		this.base_point = base_point;
	}

	/**
	 * 获取money_limit
	 * 
	 * @return money_limit
	 */
	public int getMoney_limit() {
		return money_limit;
	}

	/**
	 * 设置money_limit
	 * 
	 * @param money_limit 要设置的money_limit
	 */
	public void setMoney_limit(int money_limit) {
		this.money_limit = money_limit;
	}

	/**
	 * 获取total_money
	 * 
	 * @return total_money
	 */
	public double getTotal_money() {
		return total_money;
	}

	/**
	 * 设置total_money
	 * 
	 * @param total_money 要设置的total_money
	 */
	public void setTotal_money(double total_money) {
		this.total_money = total_money;
	}

	/**
	 * 获取bestow_money
	 * 
	 * @return bestow_money
	 */
	public double getBestow_money() {
		return bestow_money;
	}

	/**
	 * 设置bestow_money
	 * 
	 * @param bestow_money 要设置的bestow_money
	 */
	public void setBestow_money(double bestow_money) {
		this.bestow_money = bestow_money;
	}

	/**
	 * 获取is_absolute
	 * 
	 * @return is_absolute
	 */
	public int getIs_absolute() {
		return is_absolute;
	}

	/**
	 * 设置is_absolute
	 * 
	 * @param is_absolute 要设置的is_absolute
	 */
	public void setIs_absolute(int is_absolute) {
		this.is_absolute = is_absolute;
	}

	/**
	 * 获取msg_type
	 * 
	 * @return msg_type
	 */
	public int getMsg_type() {
		return msg_type;
	}

	/**
	 * 设置msg_type
	 * 
	 * @param msg_type 要设置的msg_type
	 */
	public void setMsg_type(int msg_type) {
		this.msg_type = msg_type;
	}

	/**
	 * 获取context
	 * 
	 * @return context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * 设置context
	 * 
	 * @param context 要设置的context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * 获取subject
	 * 
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设置subject
	 * 
	 * @param subject 要设置的subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 获取content
	 * 
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置content
	 * 
	 * @param content 要设置的content
	 */
	public void setContent(String content) {
		this.content = content;
	}	

	/**
	 * 获取back_type
	 * 
	 * @return back_type
	 */
	public int getBack_type() {
		return back_type;
	}

	/**
	 * 设置back_type
	 * 
	 * @param back_type 要设置的back_type
	 */
	public void setBack_type(int back_type) {
		this.back_type = back_type;
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
