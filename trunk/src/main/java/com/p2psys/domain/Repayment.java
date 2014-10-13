package com.p2psys.domain;

import java.io.Serializable;

import com.p2psys.util.DateUtils;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class Repayment implements Serializable {

	private static final long serialVersionUID = 6636946067550231040L;
	private long id;
	private long site_id;
	private int status;
	private int webstatus;
	private int order;
	// v1.6.7.1 添加借款人用户ID xx 2013-11-11 start
	private long user_id;
	// v1.6.7.1 添加借款人用户ID xx 2013-11-11 start
	private long borrow_id;
	private String repayment_time;
	private String repayment_yestime;
	private String repayment_account;
	private String repayment_yesaccount;
	private int late_days;
	private String late_interest;
	private String interest;
	private String capital;
	private String bonus;
	private String forfeit;
	private String reminder_fee;
	private String addtime;
	private String addip;
	private String borrow_style;
	private String borrow_name;
	private String time_limit;
	private String username;
	private int isday;
	private int time_limit_day;
	private String verify_time;
	// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//	private int is_fast;
//	private int is_xin;
//	private int is_mb;
//	private int is_jin;
//	private int is_flow;
//	private int is_offvouch;
//	private int is_pledge;
	private int type;
	
	//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
	private String award;
	private String late_award; //还款后奖励
	private String part_account;// 1、 投标奖励
	private String funds; //比例
	private String expire_time; //到期时间，区分一个sql中包含两个repayment_time
	private String qsCount;//期数 order+1/time_limit
	public String getQsCount() {
		return qsCount;
	}
	public void setQsCount(String qsCount) {
		this.qsCount = qsCount;
	}
	public String getExpire_time() {
		if(expire_time!=null){
			return DateUtils.dateStr4(expire_time);
		}
		return null;
	}
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}
	public String getFunds() {
		return funds;
	}
	public void setFunds(String funds) {
		this.funds = funds;
	}
	public String getPart_account() {
		return part_account;
	}
	public void setPart_account(String part_account) {
		this.part_account = part_account;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getLate_award() {
		return late_award;
	}
	public void setLate_award(String late_award) {
		this.late_award = late_award;
	}
	//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
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
	// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
	private String realname;
	private String card_id;
	//应还本金
	private String repayment_money;
	private long tender_id;
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
	private double extension_interest;
	private long real_extension_day;
	public long getReal_extension_day() {
		return real_extension_day;
	}
	public void setReal_extension_day(long real_extension_day) {
		this.real_extension_day = real_extension_day;
	}
	public double getExtension_interest() {
		return extension_interest;
	}
	public void setExtension_interest(double extension_interest) {
		this.extension_interest = extension_interest;
	}
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
	private long real_order;
	public long getReal_order() {
		return real_order;
	}
	public void setReal_order(long real_order) {
		this.real_order = real_order;
	}
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 end
	public long getTender_id() {
		return tender_id;
	}
	public void setTender_id(long tender_id) {
		this.tender_id = tender_id;
	}
	public String getRepayment_money() {
		return repayment_money;
	}
	public void setRepayment_money(String repayment_money) {
		this.repayment_money = repayment_money;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//	public int getIs_offvouch() {
//		return is_offvouch;
//	}
//	public void setIs_offvouch(int is_offvouch) {
//		this.is_offvouch = is_offvouch;
//	}
//	public int getIs_pledge() {
//		return is_pledge;
//	}
//	public void setIs_pledge(int is_pledge) {
//		this.is_pledge = is_pledge;
//	}
	// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
	public String getUnRepayTimeOverdue() {
		return unRepayTimeOverdue;
	}
	public void setUnRepayTimeOverdue(String unRepayTimeOverdue) {
		this.unRepayTimeOverdue = unRepayTimeOverdue;
	}
	public String getOverdueTime() {
		return OverdueTime;
	}
	public void setOverdueTime(String overdueTime) {
		OverdueTime = overdueTime;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	//逾期天数(临时定义)
	private String unRepayTimeOverdue;
	private String OverdueTime;
	private String account;

	// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//	public int getIs_flow() {
//		return is_flow;
//	}
//	public void setIs_flow(int is_flow) {
//		this.is_flow = is_flow;
//	}
	// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSite_id() {
		return site_id;
	}
	public void setSite_id(long site_id) {
		this.site_id = site_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getWebstatus() {
		return webstatus;
	}
	public void setWebstatus(int webstatus) {
		this.webstatus = webstatus;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(long borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getRepayment_time() {
		return repayment_time;
	}
	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}
	public String getRepayment_yestime() {
		return repayment_yestime;
	}
	public void setRepayment_yestime(String repayment_yestime) {
		this.repayment_yestime = repayment_yestime;
	}
	public String getRepayment_account() {
		return repayment_account;
	}
	public void setRepayment_account(String repayment_account) {
		this.repayment_account = repayment_account;
	}
	public String getRepayment_yesaccount() {
		return repayment_yesaccount;
	}
	public void setRepayment_yesaccount(String repayment_yesaccount) {
		this.repayment_yesaccount = repayment_yesaccount;
	}
	public int getLate_days() {
		return late_days;
	}
	public void setLate_days(int late_days) {
		this.late_days = late_days;
	}
	public String getLate_interest() {
		return late_interest;
	}
	public void setLate_interest(String late_interest) {
		this.late_interest = late_interest;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public String getForfeit() {
		return forfeit;
	}
	public void setForfeit(String forfeit) {
		this.forfeit = forfeit;
	}
	public String getReminder_fee() {
		return reminder_fee;
	}
	public void setReminder_fee(String reminder_fee) {
		this.reminder_fee = reminder_fee;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getIsday() {
		return isday;
	}
	public void setIsday(int isday) {
		this.isday = isday;
	}
	public int getTime_limit_day() {
		return time_limit_day;
	}
	public void setTime_limit_day(int time_limit_day) {
		this.time_limit_day = time_limit_day;
	}
	public String getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}
	// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//	public int getIs_fast() {
//		return is_fast;
//	}
//	public void setIs_fast(int is_fast) {
//		this.is_fast = is_fast;
//	}
//	public int getIs_xin() {
//		return is_xin;
//	}
//	public void setIs_xin(int is_xin) {
//		this.is_xin = is_xin;
//	}
//	public int getIs_mb() {
//		return is_mb;
//	}
//	public void setIs_mb(int is_mb) {
//		this.is_mb = is_mb;
//	}
//	public int getIs_jin() {
//		return is_jin;
//	}
//	public void setIs_jin(int is_jin) {
//		this.is_jin = is_jin;
//	}
	// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
	public String getBorrow_style() {
		return borrow_style;
	}
	public void setBorrow_style(String borrow_style) {
		this.borrow_style = borrow_style;
	}
	@Override
	public boolean equals(Object obj) {
		return (obj!=null)&&(obj instanceof Repayment) && (getId()==((Repayment)obj).getId());  
	}
	@Override
	public int hashCode() {
		 int result = 17;  
		 result = 31*result +(int)getId(); 
		 return result;
	};
}