package com.p2psys.model.account;

import com.p2psys.domain.Account;

public class AccountModel extends Account {

	private static final long serialVersionUID = 3261895775405456319L;
	
	private String bank;
	private String bankaccount;
	private String bankname;
	private String branch;
   // v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start 
	private String province_name;
	private String city_name;
	private String area_name;
	private int province;
	private int city;
	private int area;
  // v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end 
	private String addtime;
	private String addip;
	private String username;
	private String realname;
	private String modify_username;
	private long bank_id;
	public long getBank_id() {
		return bank_id;
	}
	public void setBank_id(long bank_id) {
		this.bank_id = bank_id;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	 // v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start 
	
	   // v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
	public String getAddtime() {
		return addtime;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
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
	
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getModify_username() {
		return modify_username;
	}
	public void setModify_username(String modify_username) {
		this.modify_username = modify_username;
	}
	//业务方法
	/**
	 * 冻结资金
	 * @param money
	 */
	public synchronized void freeze(double money){
		this.setNo_use_money(this.getNo_use_money()+money);
		this.setUse_money(this.getUse_money()-money);
	}
	

}
