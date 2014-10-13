package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.User;

public class DetailUser extends User implements Serializable  {
	
	private static final long serialVersionUID = 5543205481644066198L;
	
	private int credit_jifen;
	private double use_money;
	private String credit_pic;
	private long vip_verify_time;
	private long kefu_addtime;
	private String provincetext;
	private String citytext;
	private String areatext;
	private String typename;
	private String kefu_username;
	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
	private String phone_verify_time;
	private String realname_verify_time;
	private String video_verify_time;
	private String scene_verify_time;
	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
	// v1.6.7.1 RDPROJECT-446 zza 2013-11-20 start
	/**
	 * vip结束时间
	 */
	private String vip_end_time;
	// v1.6.7.1 RDPROJECT-446 zza 2013-11-20 end
	// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 start
	/**用户性质 1:自然人,2:公司法人*/
	private byte nature;
	// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 end
	//vip类型
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 start
	private String perferTotalMoney; //优惠利息管理费用
	public String getPerferTotalMoney() {
		return perferTotalMoney;
	}
	public void setPerferTotalMoney(String perferTotalMoney) {
		this.perferTotalMoney = perferTotalMoney;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 end
	
	private long type;
	
	
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
	public String getKefu_username() {
		return kefu_username;
	}
	public void setKefu_username(String kefu_username) {
		this.kefu_username = kefu_username;
	}
	public double getUse_money() {
		return use_money;
	}
	public void setUse_money(double use_money) {
		this.use_money = use_money;
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
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public long getVip_verify_time() {
		return vip_verify_time;
	}
	public void setVip_verify_time(long vip_verify_time) {
		this.vip_verify_time = vip_verify_time;
	}
	public long getKefu_addtime() {
		return kefu_addtime;
	}
	public void setKefu_addtime(long kefu_addtime) {
		this.kefu_addtime = kefu_addtime;
	}
	

	@Override
	public String toString() {
		return "DetailUser [credit_jifen=" + credit_jifen + ", credit_pic="
				+ credit_pic + ", vip_verify_time=" + vip_verify_time + ", kefu_addtime="
				+ kefu_addtime + ", provincetext=" + provincetext
				+ ", citytext=" + citytext + ", areatext=" + areatext
				+ ", typename=" + typename + ", getUser_id()=" + getUser_id()
				+ ", getType_id()=" + getType_id() + ", getOrder()="
				+ getOrder() + ", getPurview()=" + getPurview()
				+ ", getUsername()=" + getUsername() + ", getPassword()="
				+ getPassword() + ", getPaypassword()=" + getPaypassword()
				+ ", getIslock()=" + getIslock() + ", getInvite_userid()="
				+ getInvite_userid() + ", getInvite_money()="
				+ getInvite_money() + ", getReal_status()=" + getReal_status()
				+ ", getCard_type()=" + getCard_type() + ", getCard_id()="
				+ getCard_id() + ", getCard_pic1()=" + getCard_pic1()
				+ ", getCard_pic2()=" + getCard_pic2() + ", getNation()="
				+ getNation() + ", getRealname()=" + getRealname()
				+ ", getIntegral()=" + getIntegral() + ", getStatus()="
				+ getStatus() + ", getAvatar_status()=" + getAvatar_status()
				+ ", getEmail_status()=" + getEmail_status()
				+ ", getPhone_status()=" + getPhone_status()
				+ ", getVideo_status()=" + getVideo_status()
				+ ", getScene_status()=" + getScene_status() + ", getEmail()="
				+ getEmail() + ", getSex()=" + getSex() + ", getLitpic()="
				+ getLitpic() + ", getTel()=" + getTel() + ", getPhone()="
				+ getPhone() + ", getQq()=" + getQq() + ", getWangwang()="
				+ getWangwang() + ", getQuestion()=" + getQuestion()
				+ ", getAnswer()=" + getAnswer() + ", getBirthday()="
				+ getBirthday() + ", getProvince()=" + getProvince()
				+ ", getCity()=" + getCity() + ", getArea()=" + getArea()
				+ ", getAddress()=" + getAddress() + ", getRemind()="
				+ getRemind() + ", getPrivacy()=" + getPrivacy()
				+ ", getLogintime()=" + getLogintime() + ", getAddtime()="
				+ getAddtime() + ", getAddip()=" + getAddip()
				+ ", getUptime()=" + getUptime() + ", getUpip()=" + getUpip()
				+ ", getLasttime()=" + getLasttime() + ", getLastip()="
				+ getLastip() + ", getIs_phone()=" + getIs_phone()
				+ ", getMemberLevel()=" + getMemberLevel()
				+ ", getSerial_id()=" + getSerial_id()
				+ ", getSerial_status()=" + getSerial_status()
				+ ", getInvite_username()=" + getInvite_username()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
	public String getPhone_verify_time() {
		return phone_verify_time;
	}
	public void setPhone_verify_time(String phone_verify_time) {
		this.phone_verify_time = phone_verify_time;
	}
	public String getRealname_verify_time() {
		return realname_verify_time;
	}
	public void setRealname_verify_time(String realname_verify_time) {
		this.realname_verify_time = realname_verify_time;
	}
	public String getVideo_verify_time() {
		return video_verify_time;
	}
	public void setVideo_verify_time(String video_verify_time) {
		this.video_verify_time = video_verify_time;
	}
	public String getScene_verify_time() {
		return scene_verify_time;
	}
	public void setScene_verify_time(String scene_verify_time) {
		this.scene_verify_time = scene_verify_time;
	}	
	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
	// v1.6.7.1 RDPROJECT-446 zza 2013-11-20 start
	/**
	 * 获取vip_end_time
	 * 
	 * @return vip_end_time
	 */
	public String getVip_end_time() {
		return vip_end_time;
	}
	/**
	 * 设置vip_end_time
	 * 
	 * @param vip_end_time 要设置的vip_end_time
	 */
	public void setVip_end_time(String vip_end_time) {
		this.vip_end_time = vip_end_time;
	}
	// v1.6.7.1 RDPROJECT-446 zza 2013-11-20 end
	
	public byte getNature() {
		return nature;
	}
	public void setNature(byte nature) {
		this.nature = nature;
	}
	
}
