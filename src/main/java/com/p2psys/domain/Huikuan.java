package com.p2psys.domain;

public class Huikuan {
	private long id;
	private String huikuan_money;
	private String huikuan_award;
    private long user_id;
    private String status;
    private String addtime;
    private long cash_id;
    private long is_day;  //1:天标，0:月标
 // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 START
    private String verify_remark;
    public String getVerify_remark() {
		return verify_remark;
	}
	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
	}
	// v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 end
    public long getIs_day() {
		return is_day;
	}
	public void setIs_day(long is_day) {
		this.is_day = is_day;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String remark;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getHuikuan_money() {
		return huikuan_money;
	}
	public void setHuikuan_money(String huikuan_money) {
		this.huikuan_money = huikuan_money;
	}
	public String getHuikuan_award() {
		return huikuan_award;
	}
	public void setHuikuan_award(String huikuan_award) {
		this.huikuan_award = huikuan_award;
	}
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getCash_id() {
		return cash_id;
	}
	public void setCash_id(long cash_id) {
		this.cash_id = cash_id;
	}
}
