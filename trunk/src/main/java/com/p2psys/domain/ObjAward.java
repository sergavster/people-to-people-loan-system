package com.p2psys.domain;

/**
 * 奖品规则类
 * 
 
 * @version 1.0
 * @since 2013-7-23
 */
public class ObjAward {
	/** 主键 */
	private long id;
	/** 奖品名 */
	private String name;
	/** 规则ID */
	private int rule_id;
	/** 奖品级别 */
	private int level;
	/** 奖品中奖率(数据库保存的数据时概率*一亿） */
	private int rate;
	/** 抽奖点数限制 */
	private long point_limit;
	/** 领用数量 */
	private long bestow;
	/** 奖品总量 */
	private long total;
	/** 奖品限制(0:不限制 1：限制奖品数量 */
	private int award_limit;
	/** 奖品描述 */
	private String description;
	/** 倍率 */
	private double ratio;
	/** 奖品属性值(如面额),抽奖规则中选择金额限制时，必须填写 */
	private int obj_value;
	/** 图片 */
	private String pic_url;
	/** 奖品规则描述 */
	private String object_rule;
	/** 创建时间 */
	private String addtime;
	/** 创建IP */
	private String addip;

	// v1.6.6.2 RDPROJECT-285 lhm 2013-10-25 start
	/** 奖品类型(0:现金 1：积分 */
	private int type;

	// v1.6.6.2 RDPROJECT-285 lhm 2013-10-25 end

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取奖品名
	 * 
	 * @return 奖品名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置奖品名
	 * 
	 * @param name 要设置的奖品名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取rule_id
	 * 
	 * @return rule_id
	 */
	public int getRule_id() {
		return rule_id;
	}

	/**
	 * 设置rule_id
	 * 
	 * @param rule_id 要设置的rule_id
	 */
	public void setRule_id(int rule_id) {
		this.rule_id = rule_id;
	}

	/**
	 * 获取奖品级别
	 * 
	 * @return 奖品级别
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 设置奖品级别
	 * 
	 * @param level 要设置的奖品级别
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 获取奖品中奖率
	 * 
	 * @return 奖品中奖率
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * 设置奖品中奖率
	 * 
	 * @param rate 要设置的奖品中奖率
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * 获取point_limit
	 * 
	 * @return point_limit
	 */
	public long getPoint_limit() {
		return point_limit;
	}

	/**
	 * 设置point_limit
	 * 
	 * @param point_limit 要设置的point_limit
	 */
	public void setPoint_limit(long point_limit) {
		this.point_limit = point_limit;
	}

	/**
	 * 获取bestow
	 * 
	 * @return bestow
	 */
	public long getBestow() {
		return bestow;
	}

	/**
	 * 设置bestow
	 * 
	 * @param bestow 要设置的bestow
	 */
	public void setBestow(long bestow) {
		this.bestow = bestow;
	}

	/**
	 * 获取total
	 * 
	 * @return total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * 设置total
	 * 
	 * @param total 要设置的total
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * 获取award_limit
	 * 
	 * @return award_limit
	 */
	public int getAward_limit() {
		return award_limit;
	}

	/**
	 * 设置award_limit
	 * 
	 * @param award_limit 要设置的award_limit
	 */
	public void setAward_limit(int award_limit) {
		this.award_limit = award_limit;
	}

	/**
	 * 获取description
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置description
	 * 
	 * @param description 要设置的description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取ratio
	 * 
	 * @return ratio
	 */
	public double getRatio() {
		return ratio;
	}

	/**
	 * 设置ratio
	 * 
	 * @param ratio 要设置的ratio
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	/**
	 * 获取obj_value
	 * 
	 * @return obj_value
	 */
	public int getObj_value() {
		return obj_value;
	}

	/**
	 * 设置obj_value
	 * 
	 * @param obj_value 要设置的obj_value
	 */
	public void setObj_value(int obj_value) {
		this.obj_value = obj_value;
	}

	/**
	 * 获取pic_url
	 * 
	 * @return pic_url
	 */
	public String getPic_url() {
		return pic_url;
	}

	/**
	 * 设置pic_url
	 * 
	 * @param pic_url 要设置的pic_url
	 */
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	/**
	 * 获取object_rule
	 * 
	 * @return object_rule
	 */
	public String getObject_rule() {
		return object_rule;
	}

	/**
	 * 设置object_rule
	 * 
	 * @param object_rule 要设置的object_rule
	 */
	public void setObject_rule(String object_rule) {
		this.object_rule = object_rule;
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

	// v1.6.6.2 RDPROJECT-285 lhm 2013-10-25 start
	/**
	 * 获取type
	 * 
	 * @return type
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置type
	 * 
	 * @param type 要设置的type
	 */
	public void setType(int type) {
		this.type = type;
	}
	// v1.6.6.2 RDPROJECT-285 lhm 2013-10-25 end
}
