package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.model.borrow.BorrowModel;

public class UserBorrowModel extends BorrowModel implements Serializable{

	private static final long serialVersionUID = 6956108421611997455L;
	// v1.6.6.2 RDPROJECT-151 lhm 2013-10-22 start
//	private int isqiye;
//	private long fastid;
	/** 最近还款时间 */
	private String min_repayment_time;
	// v1.6.6.2 RDPROJECT-151 lhm 2013-10-22 end
	private String username;
	private String user_area;
	private String kefu_username;
	private String qq;
	private int credit_jifen;
	private String credit_pic;
	private String add_area;
	private double scales;
	private double Surplus;
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-05 start
	private String tbMoney; //投标金额
	private String qslimit; //期数
	private String timeLimitStr;
	private String typeStr;
	private String isdayStr;
	public String getTbMoney() {
		return tbMoney;
	}
	public void setTbMoney(String tbMoney) {
		this.tbMoney = tbMoney;
	}
	public String getIsdayStr() {
		return isdayStr;
	}
	public void setIsdayStr(String isdayStr) {
		this.isdayStr = isdayStr;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public String getTimeLimitStr() {
		return timeLimitStr;
	}
	public void setTimeLimitStr(String timeLimitStr) {
		this.timeLimitStr = timeLimitStr;
	}
	public String getQslimit() {
		return qslimit;
	}
	public void setQslimit(String qslimit) {
		this.qslimit = qslimit;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-05 end
	public double getSurplus() {
		return Surplus;
	}
	public void setSurplus(double surplus) {
		Surplus = surplus;
	}
	private String usetypename;
	private String realname;
	
	// v1.6.6.2 RDPROJECT-151 lhm 2013-10-22 start
//	public int getIsqiye() {
//		return isqiye;
//	}
//	public void setIsqiye(int isqiye) {
//		this.isqiye = isqiye;
//	}
//	public long getFastid() {
//		return fastid;
//	}
//	public void setFastid(long fastid) {
//		this.fastid = fastid;
//	}

	/**
	 * 获取min_repayment_time
	 * 
	 * @return min_repayment_time
	 */
	public String getMin_repayment_time() {
		return min_repayment_time;
	}
	/**
	 * 设置min_repayment_time
	 * 
	 * @param min_repayment_time 要设置的min_repayment_time
	 */
	public void setMin_repayment_time(String min_repayment_time) {
		this.min_repayment_time = min_repayment_time;
	}
	// v1.6.6.2 RDPROJECT-151 lhm 2013-10-22 end
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getArea() {
		return user_area;
	}
	public void setUser_area(String user_area) {
		this.user_area = user_area;
	}
	public String getKefu_username() {
		return kefu_username;
	}
	public void setKefu_username(String kefu_username) {
		this.kefu_username = kefu_username;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public int getCredit_jifen() {
		return credit_jifen;
	}
	public void setCredit_jifen(int credit_jifen) {
		this.credit_jifen = credit_jifen;
	}
	public String getCredit_pic() {
		return credit_pic;
	}
	public void setCredit_pic(String credit_pic) {
		this.credit_pic = credit_pic;
	}
	public String getAdd_area() {
		return add_area;
	}
	public void setAdd_area(String add_area) {
		this.add_area = add_area;
	}
	public double getScales() {
		return scales;
	}
	public void setScales(double scales) {
		this.scales = scales;
	}
	public String getUsetypename() {
		return usetypename;
	}
	public void setUsetypename(String usetypename) {
		this.usetypename = usetypename;
	}
	public String getUser_area() {
		return user_area;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	

}
