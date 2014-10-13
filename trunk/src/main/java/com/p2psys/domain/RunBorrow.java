package com.p2psys.domain;

public class RunBorrow {
    private long id;
    private String money;
    private String username;
    private String tel;
    private String description;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
	