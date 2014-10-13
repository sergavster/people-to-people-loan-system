package com.p2psys.domain;

import java.io.Serializable;

public class AccountRecharge implements Serializable {

	private static final long serialVersionUID = -125906918083036989L;
	private long id;
	private String trade_no;
	private long user_id;
	private int status;
	private double money;
	private String payment;
	private String returntext;
	private String type;
	private String remark;
	private String fee;
	private long verify_userid;
	private String verify_time;
	private String verify_remark;
	//标识
	private int yes_no;  // 1为第一次充值金奖励金额已经给邀请人   0 为否   
	//审核人
	private String verify_username;
	
	public String getVerify_username() {
		return verify_username;
	}
	public void setVerify_username(String verify_username) {
		this.verify_username = verify_username;
	}
	public int getYes_no() {
		return yes_no;
	}
	public void setYes_no(int yes_no) {
		this.yes_no = yes_no;
	}
	public AccountRecharge(String trade_no, long user_id,
			String payment, String type, String fee, long verify_userid,
			 String addtime,
			String addip, String paymentname) {
		super();
		this.trade_no = trade_no;
		this.user_id = user_id;
		this.payment = payment;
		this.type = type;
		this.fee = fee;
		this.verify_userid = verify_userid;
		this.addtime = addtime;
		this.addip = addip;
		this.paymentname = paymentname;
	}
	public AccountRecharge() {
		super();
	}
	private String addtime;
	private String addip;
	
	//扩展
	private String username;
	private String realname;
	private String paymentname;
	private double total;
	private String card_id;
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	//充值客服
	private long recharge_kefuid; 
	private String recharge_kefu_username;
	public String getRecharge_kefu_username() {
		return recharge_kefu_username;
	}
	public void setRecharge_kefu_username(String recharge_kefu_username) {
		this.recharge_kefu_username = recharge_kefu_username;
	}
	public long getRecharge_kefuid() {
		return recharge_kefuid;
	}
	public void setRecharge_kefuid(long recharge_kefuid) {
		this.recharge_kefuid = recharge_kefuid;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
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
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getReturntext() {
		return returntext;
	}
	public void setReturntext(String returntext) {
		this.returntext = returntext;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public long getVerify_userid() {
		return verify_userid;
	}
	public void setVerify_userid(long verify_userid) {
		this.verify_userid = verify_userid;
	}
	public String getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}
	public String getVerify_remark() {
		return verify_remark;
	}
	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPaymentname() {
		return paymentname;
	}
	public void setPaymentname(String paymentname) {
		this.paymentname = paymentname;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
}
