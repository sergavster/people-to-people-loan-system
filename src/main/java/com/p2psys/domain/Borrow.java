package com.p2psys.domain;

import java.io.Serializable;

/**
 * 借款实体类
 * 
 
 * @date 2012-7-5-下午12:46:16
 * @version  (c)</b> 2012-51-<br/>
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class Borrow implements Serializable {

	/** 序列 */
	private static final long serialVersionUID = 7029710509016395903L;
	/** 主键 */
	private long id;
	/** 用户ID */
	private long user_id;
	/** 标题 */
	private String name;
	/** 状态 */
	private int status;
	/** 排序 */
	private int order;
	/** ?? */
	private String flag;

	/** 标种 */
	private int type;

	// //////////////
	/** ?? */
	private int view_type;

	// //////////////担保标相关
	private String vouch_award;
	private String vouch_user;
	private String vouch_account;
	private int vouch_times;
	// //////////////担保标相关
	/** 来源 */
	private String source;
	/** 发布时间 */
	private String publish;
	/** 审核人 */
	private String verify_user;
	/** 复审时间 */
	private String full_verifytime;
	/** 初审时间 */
	private String verify_time;
	/** 审核备注 */
	private String verify_remark;
	/** 应还本金 */
	private double repayment_account;
	/** 实还本金 */
	private double repayment_yesaccount;
	/** 实还利息 */
	private double repayment_yesinterest;
	/** 还款时间 */
	private String repayment_time;
	/** 还款备注 */
	private String repayment_remark;
	/** ?? */
	private double payment_account;
	/** 用途 */
	private String use;
	/** 借款期限（月标使用） */
	private String time_limit;
	/** 还款方式 */
	private String style;
	/** 借贷总金额 */
	private double account;
	/** 实还总金额 */
	private double account_yes;
	/** 投标次数 */
	private int tender_times;
	/** 年利率 */
	private double apr;
	/** 最低投标金额 */
	private double lowest_account;
	/** 最高投标金额 */
	private double most_account;
	/** 有效时间 */
	private String valid_time;
	/** 投标奖励 */
	private int award;
	/** 分摊奖励金额 */
	private double part_account;
	/** 比例奖励的比例 */
	private double funds;
	/** 公开我的帐户资金情况 */
	private String open_account;
	/** 公开我的借款资金情况 */
	private String open_borrow;
	/** 公开我的投标资金情况 */
	private String open_tender;
	/** 公开我的信用额度情况 */
	private String open_credit;
	/** 详细说明 */
	private String content;
	/** 添加时间 */
	private String addtime;
	/** 添加IP */
	private String addip;
	/** 定向密码 */
	private String pwd;
	/** 是否天标 */
	private int isday;
	/** 借款期限（天标使用） */
	private int time_limit_day;
	/** 是否推荐标 */
	private int is_recommend;
	/** 流转标的总份数 */
	private int flow_count;
	/** 流转标已经购买的份数 */
	private int flow_yescount;
	/** 流转标 */
	private int flow_money;
	/** 流转标的状态 */
	private int flow_status;
	// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 start
	/** 流转标允许流转次数 */
	private int flow_time;
	/** 流转标累计已购买的份数 */
	private int flow_totalyescount;
	// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 end
	/** 借款时间 ，美贷网 */
	private String borrow_time;
	/** 贷款额度，美贷网 */
	private double borrow_account;
	/** 贷款周期，美贷网 */
	private String borrow_time_limit;
	/** 投标待收限制，美贷网 */
	private String collection_limit;
	/** 还款后发放奖励(比例) */
	private double late_award;
	// v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
	/** 展期天数 */
	private int extension_day;
	/** 展期利率 */
	private double extension_apr;
	// v1.6.6.2 RDPROJECT-333 wcw 2013.10.19 start
	/** 单笔最低限额 */
	private double lowest_single_limit;
	/** 单笔最高限额 */
	private double most_single_limit;
	// v1.6.6.2 RDPROJECT-333 wcw 2013.10.19 end
	

	//V1.6.7.1 RDPROJECT-406 liukun 2013-11-06 start
	/** 仅限VIP投标 */
	private int vip_tender_limit;
	/** 仅限VIP投标时要求VIP的最小生效时间 */
	private int vip_tender_limit_days;
	//V1.6.7.1 RDPROJECT-406 liukun 2013-11-06 end
	//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 start
	/** 担保物、抵押物，和信贷 */
	private String collateral;
	//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 end
	
	
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
	private double convest_collection;//待收金额
	private Integer most_tender_times;//投标次数
	private Integer tender_days;//投标天数
	
	public double getConvest_collection() {
		return convest_collection;
	}
	public void setConvest_collection(double convest_collection) {
		this.convest_collection = convest_collection;
	}
	public Integer getMost_tender_times() {
		return most_tender_times;
	}
	public void setMost_tender_times(Integer most_tender_times) {
		this.most_tender_times = most_tender_times;
	}
	public Integer getTender_days() {
		return tender_days;
	}
	public void setTender_days(Integer tender_days) {
		this.tender_days = tender_days;
	}
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
	
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
	 * 获取order
	 * 
	 * @return order
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * 设置order
	 * 
	 * @param order 要设置的order
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	/**
	 * 获取flag
	 * 
	 * @return flag
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * 设置flag
	 * 
	 * @param flag 要设置的flag
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
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
	/**
	 * 获取view_type
	 * 
	 * @return view_type
	 */
	public int getView_type() {
		return view_type;
	}
	/**
	 * 设置view_type
	 * 
	 * @param view_type 要设置的view_type
	 */
	public void setView_type(int view_type) {
		this.view_type = view_type;
	}
	/**
	 * 获取vouch_award
	 * 
	 * @return vouch_award
	 */
	public String getVouch_award() {
		return vouch_award;
	}
	/**
	 * 设置vouch_award
	 * 
	 * @param vouch_award 要设置的vouch_award
	 */
	public void setVouch_award(String vouch_award) {
		this.vouch_award = vouch_award;
	}
	/**
	 * 获取vouch_user
	 * 
	 * @return vouch_user
	 */
	public String getVouch_user() {
		return vouch_user;
	}
	/**
	 * 设置vouch_user
	 * 
	 * @param vouch_user 要设置的vouch_user
	 */
	public void setVouch_user(String vouch_user) {
		this.vouch_user = vouch_user;
	}
	/**
	 * 获取vouch_account
	 * 
	 * @return vouch_account
	 */
	public String getVouch_account() {
		return vouch_account;
	}
	/**
	 * 设置vouch_account
	 * 
	 * @param vouch_account 要设置的vouch_account
	 */
	public void setVouch_account(String vouch_account) {
		this.vouch_account = vouch_account;
	}
	/**
	 * 获取vouch_times
	 * 
	 * @return vouch_times
	 */
	public int getVouch_times() {
		return vouch_times;
	}
	/**
	 * 设置vouch_times
	 * 
	 * @param vouch_times 要设置的vouch_times
	 */
	public void setVouch_times(int vouch_times) {
		this.vouch_times = vouch_times;
	}
	/**
	 * 获取source
	 * 
	 * @return source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * 设置source
	 * 
	 * @param source 要设置的source
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * 获取publish
	 * 
	 * @return publish
	 */
	public String getPublish() {
		return publish;
	}
	/**
	 * 设置publish
	 * 
	 * @param publish 要设置的publish
	 */
	public void setPublish(String publish) {
		this.publish = publish;
	}
	/**
	 * 获取verify_user
	 * 
	 * @return verify_user
	 */
	public String getVerify_user() {
		return verify_user;
	}
	/**
	 * 设置verify_user
	 * 
	 * @param verify_user 要设置的verify_user
	 */
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	/**
	 * 获取full_verifytime
	 * 
	 * @return full_verifytime
	 */
	public String getFull_verifytime() {
		return full_verifytime;
	}
	/**
	 * 设置full_verifytime
	 * 
	 * @param full_verifytime 要设置的full_verifytime
	 */
	public void setFull_verifytime(String full_verifytime) {
		this.full_verifytime = full_verifytime;
	}
	/**
	 * 获取verify_time
	 * 
	 * @return verify_time
	 */
	public String getVerify_time() {
		return verify_time;
	}
	/**
	 * 设置verify_time
	 * 
	 * @param verify_time 要设置的verify_time
	 */
	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}
	/**
	 * 获取verify_remark
	 * 
	 * @return verify_remark
	 */
	public String getVerify_remark() {
		return verify_remark;
	}
	/**
	 * 设置verify_remark
	 * 
	 * @param verify_remark 要设置的verify_remark
	 */
	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
	}
	/**
	 * 获取repayment_account
	 * 
	 * @return repayment_account
	 */
	public double getRepayment_account() {
		return repayment_account;
	}
	/**
	 * 设置repayment_account
	 * 
	 * @param repayment_account 要设置的repayment_account
	 */
	public void setRepayment_account(double repayment_account) {
		this.repayment_account = repayment_account;
	}
	/**
	 * 获取repayment_yesaccount
	 * 
	 * @return repayment_yesaccount
	 */
	public double getRepayment_yesaccount() {
		return repayment_yesaccount;
	}
	/**
	 * 设置repayment_yesaccount
	 * 
	 * @param repayment_yesaccount 要设置的repayment_yesaccount
	 */
	public void setRepayment_yesaccount(double repayment_yesaccount) {
		this.repayment_yesaccount = repayment_yesaccount;
	}
	/**
	 * 获取repayment_yesinterest
	 * 
	 * @return repayment_yesinterest
	 */
	public double getRepayment_yesinterest() {
		return repayment_yesinterest;
	}
	/**
	 * 设置repayment_yesinterest
	 * 
	 * @param repayment_yesinterest 要设置的repayment_yesinterest
	 */
	public void setRepayment_yesinterest(double repayment_yesinterest) {
		this.repayment_yesinterest = repayment_yesinterest;
	}
	/**
	 * 获取repayment_time
	 * 
	 * @return repayment_time
	 */
	public String getRepayment_time() {
		return repayment_time;
	}
	/**
	 * 设置repayment_time
	 * 
	 * @param repayment_time 要设置的repayment_time
	 */
	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}
	/**
	 * 获取repayment_remark
	 * 
	 * @return repayment_remark
	 */
	public String getRepayment_remark() {
		return repayment_remark;
	}
	/**
	 * 设置repayment_remark
	 * 
	 * @param repayment_remark 要设置的repayment_remark
	 */
	public void setRepayment_remark(String repayment_remark) {
		this.repayment_remark = repayment_remark;
	}
	/**
	 * 获取payment_account
	 * 
	 * @return payment_account
	 */
	public double getPayment_account() {
		return payment_account;
	}
	/**
	 * 设置payment_account
	 * 
	 * @param payment_account 要设置的payment_account
	 */
	public void setPayment_account(double payment_account) {
		this.payment_account = payment_account;
	}
	/**
	 * 获取use
	 * 
	 * @return use
	 */
	public String getUse() {
		return use;
	}
	/**
	 * 设置use
	 * 
	 * @param use 要设置的use
	 */
	public void setUse(String use) {
		this.use = use;
	}
	/**
	 * 获取time_limit
	 * 
	 * @return time_limit
	 */
	public String getTime_limit() {
		return time_limit;
	}
	/**
	 * 设置time_limit
	 * 
	 * @param time_limit 要设置的time_limit
	 */
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}
	/**
	 * 获取style
	 * 
	 * @return style
	 */
	public String getStyle() {
		return style;
	}
	/**
	 * 设置style
	 * 
	 * @param style 要设置的style
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	/**
	 * 获取account
	 * 
	 * @return account
	 */
	public double getAccount() {
		return account;
	}
	/**
	 * 设置account
	 * 
	 * @param account 要设置的account
	 */
	public void setAccount(double account) {
		this.account = account;
	}
	/**
	 * 获取account_yes
	 * 
	 * @return account_yes
	 */
	public double getAccount_yes() {
		return account_yes;
	}
	/**
	 * 设置account_yes
	 * 
	 * @param account_yes 要设置的account_yes
	 */
	public void setAccount_yes(double account_yes) {
		this.account_yes = account_yes;
	}
	/**
	 * 获取tender_times
	 * 
	 * @return tender_times
	 */
	public int getTender_times() {
		return tender_times;
	}
	/**
	 * 设置tender_times
	 * 
	 * @param tender_times 要设置的tender_times
	 */
	public void setTender_times(int tender_times) {
		this.tender_times = tender_times;
	}
	/**
	 * 获取apr
	 * 
	 * @return apr
	 */
	public double getApr() {
		return apr;
	}
	/**
	 * 设置apr
	 * 
	 * @param apr 要设置的apr
	 */
	public void setApr(double apr) {
		this.apr = apr;
	}
	/**
	 * 获取lowest_account
	 * 
	 * @return lowest_account
	 */
	public double getLowest_account() {
		return lowest_account;
	}
	/**
	 * 设置lowest_account
	 * 
	 * @param lowest_account 要设置的lowest_account
	 */
	public void setLowest_account(double lowest_account) {
		this.lowest_account = lowest_account;
	}
	/**
	 * 获取most_account
	 * 
	 * @return most_account
	 */
	public double getMost_account() {
		return most_account;
	}
	/**
	 * 设置most_account
	 * 
	 * @param most_account 要设置的most_account
	 */
	public void setMost_account(double most_account) {
		this.most_account = most_account;
	}
	/**
	 * 获取valid_time
	 * 
	 * @return valid_time
	 */
	public String getValid_time() {
		return valid_time;
	}
	/**
	 * 设置valid_time
	 * 
	 * @param valid_time 要设置的valid_time
	 */
	public void setValid_time(String valid_time) {
		this.valid_time = valid_time;
	}
	/**
	 * 获取award
	 * 
	 * @return award
	 */
	public int getAward() {
		return award;
	}
	/**
	 * 设置award
	 * 
	 * @param award 要设置的award
	 */
	public void setAward(int award) {
		this.award = award;
	}
	/**
	 * 获取part_account
	 * 
	 * @return part_account
	 */
	public double getPart_account() {
		return part_account;
	}
	/**
	 * 设置part_account
	 * 
	 * @param part_account 要设置的part_account
	 */
	public void setPart_account(double part_account) {
		this.part_account = part_account;
	}
	/**
	 * 获取funds
	 * 
	 * @return funds
	 */
	public double getFunds() {
		return funds;
	}
	/**
	 * 设置funds
	 * 
	 * @param funds 要设置的funds
	 */
	public void setFunds(double funds) {
		this.funds = funds;
	}
	/**
	 * 获取open_account
	 * 
	 * @return open_account
	 */
	public String getOpen_account() {
		return open_account;
	}
	/**
	 * 设置open_account
	 * 
	 * @param open_account 要设置的open_account
	 */
	public void setOpen_account(String open_account) {
		this.open_account = open_account;
	}
	/**
	 * 获取open_borrow
	 * 
	 * @return open_borrow
	 */
	public String getOpen_borrow() {
		return open_borrow;
	}
	/**
	 * 设置open_borrow
	 * 
	 * @param open_borrow 要设置的open_borrow
	 */
	public void setOpen_borrow(String open_borrow) {
		this.open_borrow = open_borrow;
	}
	/**
	 * 获取open_tender
	 * 
	 * @return open_tender
	 */
	public String getOpen_tender() {
		return open_tender;
	}
	/**
	 * 设置open_tender
	 * 
	 * @param open_tender 要设置的open_tender
	 */
	public void setOpen_tender(String open_tender) {
		this.open_tender = open_tender;
	}
	/**
	 * 获取open_credit
	 * 
	 * @return open_credit
	 */
	public String getOpen_credit() {
		return open_credit;
	}
	/**
	 * 设置open_credit
	 * 
	 * @param open_credit 要设置的open_credit
	 */
	public void setOpen_credit(String open_credit) {
		this.open_credit = open_credit;
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
	/**
	 * 获取pwd
	 * 
	 * @return pwd
	 */
	public String getPwd() {
		return pwd;
	}
	/**
	 * 设置pwd
	 * 
	 * @param pwd 要设置的pwd
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * 获取isday
	 * 
	 * @return isday
	 */
	public int getIsday() {
		return isday;
	}
	/**
	 * 设置isday
	 * 
	 * @param isday 要设置的isday
	 */
	public void setIsday(int isday) {
		this.isday = isday;
	}
	/**
	 * 获取time_limit_day
	 * 
	 * @return time_limit_day
	 */
	public int getTime_limit_day() {
		return time_limit_day;
	}
	/**
	 * 设置time_limit_day
	 * 
	 * @param time_limit_day 要设置的time_limit_day
	 */
	public void setTime_limit_day(int time_limit_day) {
		this.time_limit_day = time_limit_day;
	}
	/**
	 * 获取is_recommend
	 * 
	 * @return is_recommend
	 */
	public int getIs_recommend() {
		return is_recommend;
	}
	/**
	 * 设置is_recommend
	 * 
	 * @param is_recommend 要设置的is_recommend
	 */
	public void setIs_recommend(int is_recommend) {
		this.is_recommend = is_recommend;
	}
	/**
	 * 获取flow_count
	 * 
	 * @return flow_count
	 */
	public int getFlow_count() {
		return flow_count;
	}
	/**
	 * 设置flow_count
	 * 
	 * @param flow_count 要设置的flow_count
	 */
	public void setFlow_count(int flow_count) {
		this.flow_count = flow_count;
	}
	/**
	 * 获取flow_yescount
	 * 
	 * @return flow_yescount
	 */
	public int getFlow_yescount() {
		return flow_yescount;
	}
	/**
	 * 设置flow_yescount
	 * 
	 * @param flow_yescount 要设置的flow_yescount
	 */
	public void setFlow_yescount(int flow_yescount) {
		this.flow_yescount = flow_yescount;
	}
	/**
	 * 获取flow_money
	 * 
	 * @return flow_money
	 */
	public int getFlow_money() {
		return flow_money;
	}
	/**
	 * 设置flow_money
	 * 
	 * @param flow_money 要设置的flow_money
	 */
	public void setFlow_money(int flow_money) {
		this.flow_money = flow_money;
	}
	/**
	 * 获取flow_status
	 * 
	 * @return flow_status
	 */
	public int getFlow_status() {
		return flow_status;
	}
	/**
	 * 设置flow_status
	 * 
	 * @param flow_status 要设置的flow_status
	 */
	public void setFlow_status(int flow_status) {
		this.flow_status = flow_status;
	}
	/**
	 * 获取flow_time
	 * 
	 * @return flow_time
	 */
	public int getFlow_time() {
		return flow_time;
	}
	/**
	 * 设置flow_time
	 * 
	 * @param flow_time 要设置的flow_time
	 */
	public void setFlow_time(int flow_time) {
		this.flow_time = flow_time;
	}
	/**
	 * 获取flow_totalyescount
	 * 
	 * @return flow_totalyescount
	 */
	public int getFlow_totalyescount() {
		return flow_totalyescount;
	}
	/**
	 * 设置flow_totalyescount
	 * 
	 * @param flow_totalyescount 要设置的flow_totalyescount
	 */
	public void setFlow_totalyescount(int flow_totalyescount) {
		this.flow_totalyescount = flow_totalyescount;
	}
	/**
	 * 获取borrow_time
	 * 
	 * @return borrow_time
	 */
	public String getBorrow_time() {
		return borrow_time;
	}
	/**
	 * 设置borrow_time
	 * 
	 * @param borrow_time 要设置的borrow_time
	 */
	public void setBorrow_time(String borrow_time) {
		this.borrow_time = borrow_time;
	}
	/**
	 * 获取borrow_account
	 * 
	 * @return borrow_account
	 */
	public double getBorrow_account() {
		return borrow_account;
	}
	/**
	 * 设置borrow_account
	 * 
	 * @param borrow_account 要设置的borrow_account
	 */
	public void setBorrow_account(double borrow_account) {
		this.borrow_account = borrow_account;
	}
	/**
	 * 获取borrow_time_limit
	 * 
	 * @return borrow_time_limit
	 */
	public String getBorrow_time_limit() {
		return borrow_time_limit;
	}
	/**
	 * 设置borrow_time_limit
	 * 
	 * @param borrow_time_limit 要设置的borrow_time_limit
	 */
	public void setBorrow_time_limit(String borrow_time_limit) {
		this.borrow_time_limit = borrow_time_limit;
	}
	/**
	 * 获取collection_limit
	 * 
	 * @return collection_limit
	 */
	public String getCollection_limit() {
		return collection_limit;
	}
	/**
	 * 设置collection_limit
	 * 
	 * @param collection_limit 要设置的collection_limit
	 */
	public void setCollection_limit(String collection_limit) {
		this.collection_limit = collection_limit;
	}
	/**
	 * 获取late_award
	 * 
	 * @return late_award
	 */
	public double getLate_award() {
		return late_award;
	}
	/**
	 * 设置late_award
	 * 
	 * @param late_award 要设置的late_award
	 */
	public void setLate_award(double late_award) {
		this.late_award = late_award;
	}
	/**
	 * 获取extension_day
	 * 
	 * @return extension_day
	 */
	public int getExtension_day() {
		return extension_day;
	}
	/**
	 * 设置extension_day
	 * 
	 * @param extension_day 要设置的extension_day
	 */
	public void setExtension_day(int extension_day) {
		this.extension_day = extension_day;
	}
	/**
	 * 获取extension_apr
	 * 
	 * @return extension_apr
	 */
	public double getExtension_apr() {
		return extension_apr;
	}
	/**
	 * 设置extension_apr
	 * 
	 * @param extension_apr 要设置的extension_apr
	 */
	public void setExtension_apr(double extension_apr) {
		this.extension_apr = extension_apr;
	}
	/**
	 * 获取lowest_single_limit
	 * 
	 * @return lowest_single_limit
	 */
	public double getLowest_single_limit() {
		return lowest_single_limit;
	}
	/**
	 * 设置lowest_single_limit
	 * 
	 * @param lowest_single_limit 要设置的lowest_single_limit
	 */
	public void setLowest_single_limit(double lowest_single_limit) {
		this.lowest_single_limit = lowest_single_limit;
	}
	/**
	 * 获取most_single_limit
	 * 
	 * @return most_single_limit
	 */
	public double getMost_single_limit() {
		return most_single_limit;
	}
	/**
	 * 设置most_single_limit
	 * 
	 * @param most_single_limit 要设置的most_single_limit
	 */
	public void setMost_single_limit(double most_single_limit) {
		this.most_single_limit = most_single_limit;
	}
	
	//V1.6.7.1 RDPROJECT-406 liukun 2013-11-06 start
		
	public int getVip_tender_limit() {
		return vip_tender_limit;
	}
	public void setVip_tender_limit(int vip_tender_limit) {
		this.vip_tender_limit = vip_tender_limit;
	}
	
	public int getVip_tender_limit_days() {
		return vip_tender_limit_days;
	}
	public void setVip_tender_limit_days(int vip_tender_limit_days) {
		this.vip_tender_limit_days = vip_tender_limit_days;
	}
	//V1.6.7.1 RDPROJECT-406 liukun 2013-11-06 end
	//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 start
	public String getCollateral() {
		return collateral;
	}
	public void setCollateral(String collateral) {
		this.collateral = collateral;
	}
	//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 end
}
