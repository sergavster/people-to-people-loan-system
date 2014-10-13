package com.p2psys.domain;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class Tender {
	private long id;
	private long site_id;
	private long user_id;
	private int	status = 1;
	private long borrow_id;
	private String	money;
	private String	account;
	private String	repayment_account;
	private String	interest;	
	private String part_account;
	private String	repayment_yesaccount;
	private String	wait_account;
	private String	wait_interest;
	private String	repayment_yesinterest;
	private String	addtime;
	private String	addip;
	
	private int auto_repurchase;
	private int is_auto_tender;
	
	public int getIs_auto_tender() {
		return is_auto_tender;
	}
	public void setIs_auto_tender(int is_auto_tender) {
		this.is_auto_tender = is_auto_tender;
	}
	//奖励推后发放是否启用
	private int award_after_push;
	
	public int getAward_after_push() {
		return award_after_push;
	}
	public void setAward_after_push(int award_after_push) {
		this.award_after_push = award_after_push;
	}
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
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(long borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRepayment_account() {
		return repayment_account;
	}
	public void setRepayment_account(String repayment_account) {
		this.repayment_account = repayment_account;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getPart_account() {
		return part_account;
	}
	public void setPart_account(String part_account) {
		this.part_account = part_account;
	}
	public String getRepayment_yesaccount() {
		return repayment_yesaccount;
	}
	public void setRepayment_yesaccount(String repayment_yesaccount) {
		this.repayment_yesaccount = repayment_yesaccount;
	}
	public String getWait_account() {
		return wait_account;
	}
	public void setWait_account(String wait_account) {
		this.wait_account = wait_account;
	}
	public String getWait_interest() {
		return wait_interest;
	}
	public void setWait_interest(String wait_interest) {
		this.wait_interest = wait_interest;
	}
	public String getRepayment_yesinterest() {
		return repayment_yesinterest;
	}
	public void setRepayment_yesinterest(String repayment_yesinterest) {
		this.repayment_yesinterest = repayment_yesinterest;
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
	public int getAuto_repurchase() {
		return auto_repurchase;
	}
	public void setAuto_repurchase(int auto_repurchase) {
		this.auto_repurchase = auto_repurchase;
	}
}
