package com.p2psys.domain;

import java.io.Serializable;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class AccountCash implements Serializable {

	private static final long serialVersionUID = -8321395313750326323L;
	private long id;
	private long user_id;
	private int status;
	private String account;
	private String bank;
	private String branch;
	private String total;
	private String credited;
	private String fee;
	// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 start
	/**
	 * 修改前的提现手续费
	 */
	private String old_fee;
	// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 end
	private long verify_userid;
	
	private String verify_time;
	
	private String verify_remark;
	
	private String addtime;
	
	private String addip;
	
	// 额外新增
	private String bankname;
	
	private String username;
	
	private String realname;
	
	private String verify_username;

	// 提现红包操作金额
	private double hongbao;
	
	// 判断是投资人提现还是借款人提现    1:代表投资人   2:代表借款人 0：代表默认
	private long cash_type;
	// 免费提现额度
	private double freecash;
	
	//提现扣除的利息金额
	private double interest_cash;
	
	//提现扣除的奖励金额
	private double award_cash ;
	
	//提现扣除的充值金额
	private double recharge_cash;
	
	//提现扣除的借款金额
	private double borrow_cash;
	
	//提现扣除的可用回款
	private double huikuan_cash;
	//V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 start
	private int bank_id;
	public int getBank_id() {
		return bank_id;
	}

	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	//V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 end 

	

	public double getHongbao() {
		return hongbao;
	}

	public void setHongbao(double hongbao) {
		this.hongbao = hongbao;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getCredited() {
		return credited;
	}

	public void setCredited(String credited) {
		this.credited = credited;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
	
	public String getOld_fee() {
		return old_fee;
	}

	public void setOld_fee(String old_fee) {
		this.old_fee = old_fee;
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

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getVerify_username() {
		return verify_username;
	}

	public void setVerify_username(String verify_username) {
		this.verify_username = verify_username;
	}

	public double getFreecash() {
		return freecash;
	}

	public void setFreecash(double freecash) {
		this.freecash = freecash;
	}

	public long getCash_type() {
		return cash_type;
	}

	public void setCash_type(long cash_type) {
		this.cash_type = cash_type;
	}

	public double getInterest_cash() {
		return interest_cash;
	}

	public void setInterest_cash(double interest_cash) {
		this.interest_cash = interest_cash;
	}

	public double getAward_cash() {
		return award_cash;
	}

	public void setAward_cash(double award_cash) {
		this.award_cash = award_cash;
	}

	public double getRecharge_cash() {
		return recharge_cash;
	}

	public void setRecharge_cash(double recharge_cash) {
		this.recharge_cash = recharge_cash;
	}

	public double getBorrow_cash() {
		return borrow_cash;
	}

	public void setBorrow_cash(double borrow_cash) {
		this.borrow_cash = borrow_cash;
	}

	public double getHuikuan_cash() {
		return huikuan_cash;
	}

	public void setHuikuan_cash(double huikuan_cash) {
		this.huikuan_cash = huikuan_cash;
	}

}
