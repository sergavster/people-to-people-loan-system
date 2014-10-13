package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.Userinfo;

public class AdminInfoModel extends Userinfo implements Serializable{
	
	private static final long serialVersionUID = 6026604036412862478L;
	
	private String realname;
	private String sex;
	private String username;
	private String nation;
	private String birthday;
	private String provincetext;
	private String citytext;
	private String areatext;
	private String usertype;
	private String email;
	private String card_type;
	private String card_id;
	
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getProvincetext() {
		return provincetext;
	}
	public void setProvincetext(String provincetext) {
		this.provincetext = provincetext;
	}
	public String getCitytext() {
		return citytext;
	}
	public void setCitytext(String citytext) {
		this.citytext = citytext;
	}
	public String getAreatext() {
		return areatext;
	}
	public void setAreatext(String areatext) {
		this.areatext = areatext;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
