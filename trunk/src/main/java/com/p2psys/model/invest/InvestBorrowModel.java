package com.p2psys.model.invest;

public class InvestBorrowModel implements java.io.Serializable{
	private static final long serialVersionUID = -6010522210993109924L;
	private String repayment_yesaccount;
	private String repayment_account;
	private String tender_time;
	private String anum;
	private String inter;
	private String borrow_name;
	private String account;
	private String time_limit;
	private int isday;
	private int time_limit_day;
	private double apr;
	private String username;
	private int  credit;
	private long id;
	
	private long user_id;
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
	private long extension_day;
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 start
	private double extension_apr;
	public double getExtension_apr() {
		return extension_apr;
	}
	public void setExtension_apr(double extension_apr) {
		this.extension_apr = extension_apr;
	}
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 end
	public long getExtension_day() {
		return extension_day;
	}
	public void setExtension_day(long extension_day) {
		this.extension_day = extension_day;
	}
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
	public String getRepayment_yesaccount() {
		return repayment_yesaccount;
	}
	public void setRepayment_yesaccount(String repayment_yesaccount) {
		this.repayment_yesaccount = repayment_yesaccount;
	}
	public String getRepayment_account() {
		return repayment_account;
	}
	public void setRepayment_account(String repayment_account) {
		this.repayment_account = repayment_account;
	}
	public String getTender_time() {
		return tender_time;
	}
	public void setTender_time(String tender_time) {
		this.tender_time = tender_time;
	}
	public String getAnum() {
		return anum;
	}
	public void setAnum(String anum) {
		this.anum = anum;
	}
	public String getInter() {
		return inter;
	}
	public void setInter(String inter) {
		this.inter = inter;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
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
	public double getApr() {
		return apr;
	}
	public void setApr(double apr) {
		this.apr = apr;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
}
