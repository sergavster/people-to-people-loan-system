package com.p2psys.domain;

import java.io.Serializable;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class Collection implements Serializable {

	private static final long serialVersionUID = -3844803595420384816L;

	private long id;
	private long site_id;
	private int status;
	private int order;
	// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 start
	private long user_id;
	private long borrow_id;
	// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 end
	private long tender_id;
	private String repay_time;
	private String repay_yestime;
	private String repay_account;
	private String repay_yesaccount;
	private String interest;
	private String capital;
	private String bonus;
	private long late_days;
	private String late_interest;
	private String addtime;
	private String addip;
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
	
	// v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 start
	public double repay_award;
	public int repay_award_status;
	
	public double getRepay_award() {
		return repay_award;
	}
	public void setRepay_award(double repay_award) {
		this.repay_award = repay_award;
	}
	public int getRepay_award_status() {
		return repay_award_status;
	}
	public void setRepay_award_status(int repay_award_status) {
		this.repay_award_status = repay_award_status;
	}
	// v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 end

	//利息管理费
	private String manage_fee;

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	private String phone;
	private String area;
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
	public long getTender_id() {
		return tender_id;
	}
	public void setTender_id(long tender_id) {
		this.tender_id = tender_id;
	}
	public String getRepay_time() {
		return repay_time;
	}
	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}
	public String getRepay_yestime() {
		return repay_yestime;
	}
	public void setRepay_yestime(String repay_yestime) {
		this.repay_yestime = repay_yestime;
	}
	public String getRepay_account() {
		return repay_account;
	}
	public void setRepay_account(String repay_account) {
		this.repay_account = repay_account;
	}
	public String getRepay_yesaccount() {
		return repay_yesaccount;
	}
	public void setRepay_yesaccount(String repay_yesaccount) {
		this.repay_yesaccount = repay_yesaccount;
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
	public long getLate_days() {
		return late_days;
	}
	public void setLate_days(long late_days) {
		this.late_days = late_days;
	}
	public String getLate_interest() {
		return late_interest;
	}
	public void setLate_interest(String late_interest) {
		this.late_interest = late_interest;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
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
	public String getManage_fee() {
		return manage_fee;
	}
	public void setManage_fee(String manage_fee) {
		this.manage_fee = manage_fee;
	}
	@Override
	public boolean equals(Object obj) {
		return (obj!=null)&&(obj instanceof Collection) && (getId()==((Collection)obj).getId());  
	}
	@Override
	public int hashCode() {
		 int result = 17;  
		 result = 31*result +(int)getId(); 
		 return result;
	};
}