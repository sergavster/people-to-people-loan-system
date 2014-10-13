package com.p2psys.domain;
//及时雨协议
public class Protocol {

	public Protocol() {
		// TODO Auto-generated constructor stub
	}
 
	private long id;
	private long pid;
	private String protocol_type;
	private String addtime;
	private String addip;
	private long user_id;
	private double money;
	//应还本金
	private double repayment_account;
	//应还时间
	private String repayment_time;
	//应还利息
	private double interest;
	private long borrow_id;
	//银行卡号
	private String bank_account;
	//银行开户行
	private String bank_branch;
	public String getBank_account() {
		return bank_account;
	}
	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}
	public String getBank_branch() {
		return bank_branch;
	}
	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}
	public String getRepayment_time() {
		return repayment_time;
	}
	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}
    
	public double getRepayment_account() {
		return repayment_account;
	}
	public void setRepayment_account(double repayment_account) {
		this.repayment_account = repayment_account;
	}
	public double getInterest() {
		return interest;
	}
	public void setInterest(double interest) {
		this.interest = interest;
	}
	public long getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(long borrow_id) {
		this.borrow_id = borrow_id;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}

	private String remark;
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public Protocol(long pid, String protocol_type, String addtime,
			String addip, String remark) {
		super();
		this.pid = pid;
		this.protocol_type = protocol_type;
		this.addtime = addtime;
		this.addip = addip;
		this.remark = remark;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	
	public String getProtocol_type() {
		return protocol_type;
	}
	public void setProtocol_type(String protocol_type) {
		this.protocol_type = protocol_type;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
