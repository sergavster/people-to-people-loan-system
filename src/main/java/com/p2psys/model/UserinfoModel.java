package com.p2psys.model;

import java.io.File;
import java.io.Serializable;

import com.p2psys.domain.Userinfo;

/**
 * 用户个人资料信息更新的Model,user和usrinfo
 * 
 
 * 
 */
public class UserinfoModel extends Userinfo implements Serializable {

	private static final long serialVersionUID = 6026604036412862478L;

	// 查询还是修改
	private String type;

	// 个人资料的首页信息属性
	//private long user_id;
	private String realname;
	private String sex;
	private String username;

	// 实名认证
	// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 start
	/**用户性质 1:自然人,2:公司法人*/
	private byte nature;
	// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 end
	private String nation;
	private String birthday;
	private String card_type;
	private String card_id;
	private File card_pic1;
	private File card_pic2;
	private String card_pic1FileName;
	private String card_pic2FileName;
	private String card_pic1_path;
	private String card_pic2_path;
	//
	private String provincetext;
	private String citytext;
	private String areatext;
	private String usertype;
	private String email;
	// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
	private int real_status;
	private int email_status;
	private int phone_status;
	private int video_status;
	private int scene_status;
	private int vip_status;
	// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
	private String vip_remark;
	
	//v1.6.7.1 安全优化 sj 2013-11-26 start
	private String firstpwd;
	private String password;
	
	public String getFirstpwd() {
		return firstpwd;
	}
	public void setFirstpwd(String firstpwd) {
		this.firstpwd = firstpwd;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	//v1.6.7.1 安全优化 sj 2013-11-26 end
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public byte getNature() {
		return nature;
	}
	public void setNature(byte nature) {
		this.nature = nature;
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
	public File getCard_pic1() {
		return card_pic1;
	}
	public void setCard_pic1(File card_pic1) {
		this.card_pic1 = card_pic1;
	}
	public File getCard_pic2() {
		return card_pic2;
	}
	public void setCard_pic2(File card_pic2) {
		this.card_pic2 = card_pic2;
	}
	public String getCard_pic1FileName() {
		return card_pic1FileName;
	}
	public void setCard_pic1FileName(String card_pic1FileName) {
		this.card_pic1FileName = card_pic1FileName;
	}
	public String getCard_pic2FileName() {
		return card_pic2FileName;
	}
	public void setCard_pic2FileName(String card_pic2FileName) {
		this.card_pic2FileName = card_pic2FileName;
	}
	public String getCard_pic1_path() {
		return card_pic1_path;
	}
	public void setCard_pic1_path(String card_pic1_path) {
		this.card_pic1_path = card_pic1_path;
	}
	public String getCard_pic2_path() {
		return card_pic2_path;
	}
	public void setCard_pic2_path(String card_pic2_path) {
		this.card_pic2_path = card_pic2_path;
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
	public int getReal_status() {
		return real_status;
	}
	public void setReal_status(int real_status) {
		this.real_status = real_status;
	}
	public int getEmail_status() {
		return email_status;
	}
	public void setEmail_status(int email_status) {
		this.email_status = email_status;
	}
	public int getPhone_status() {
		return phone_status;
	}
	public void setPhone_status(int phone_status) {
		this.phone_status = phone_status;
	}
	public int getVideo_status() {
		return video_status;
	}
	public void setVideo_status(int video_status) {
		this.video_status = video_status;
	}
	public int getScene_status() {
		return scene_status;
	}
	public void setScene_status(int scene_status) {
		this.scene_status = scene_status;
	}
	public int getVip_status() {
		return vip_status;
	}
	public void setVip_status(int vip_status) {
		this.vip_status = vip_status;
	}
	public String getVip_remark() {
		return vip_remark;
	}
	public void setVip_remark(String vip_remark) {
		this.vip_remark = vip_remark;
	}
}
