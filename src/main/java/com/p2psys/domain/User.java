package com.p2psys.domain;

import com.p2psys.util.StringUtils;

/**
 * 
 
 *
 * @date 2012-7-2-下午5:09:28
 *
 * @version  
 *
 *  (c)</b> 2012-51-<br/>
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */

public class User {
	private long user_id;
	private int type_id;
	// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 start
	/**用户性质 1:自然人,2:公司法人*/
	private byte nature;
	// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 end
	private long order;
	private String purview;
	private String username;
	private String password;
	private String paypassword;
	private int islock;
	private String	invite_userid;
	private String	invite_money;
	// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
	private int	real_status;//-1:未通过,0:未认证,1:通过,2:待审核
	// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
	private String	card_type;
	private String	card_id;
	private String	card_pic1;
	private String	card_pic2;
	private String	nation;
	private String	realname;
	private String	integral;
	private int	status;
	private int	avatar_status;
	// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
	private int	email_status;
	private int	phone_status;
	// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
	private int	video_status;
	private int	scene_status;
	private String	email;
	private String	sex;
	private String litpic;
	private String tel;
	private String	phone;
	private String	qq;
	private String wangwang;
	private String question;
	private String answer;
	private String birthday;
	private String province;
	private String	city;
	private String	area;
	private String address;
	private String remind;
	private String privacy;
	private long logintime;
	private String addtime;
	private String addip;
	private String uptime;
	private String upip;
	private String lasttime;
	private String lastip;
	private long is_phone;
	private long memberLevel;
	/**动态口令编号*/
	private String serial_id;
	private String serial_status;
	private String invite_username;
	private int vip_status;
	
	//v1.6.7.1 sj 2013.11.13 start
	private String firstpwd;
	
	public String getFirstpwd() {
		return firstpwd;
	}
	public void setFirstpwd(String firstpwd) {
		this.firstpwd = firstpwd;
	}
	//v1.6.7.1 sj 2013.11.13 end
	
	// v1.6.6.1 RDPROJECT-235 zza 2013-10-17 start
	/**
	 * 推广奖励规则Id
	 */
	private long rule_promote_id;
	// v1.6.6.1 RDPROJECT-235 zza 2013-10-17 end
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long userid) {
		this.user_id = userid;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public byte getNature() {
		return nature;
	}
	public void setNature(byte nature) {
		this.nature = nature;
	}
	public long getOrder() {
		return order;
	}
	public void setOrder(long order) {
		this.order = order;
	}
	public String getPurview() {
		return purview;
	}
	public void setPurview(String purview) {
		this.purview = purview;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPaypassword() {
		return paypassword;
	}
	public void setPaypassword(String paypassword) {
		this.paypassword = paypassword;
	}
	public int getIslock() {
		return islock;
	}
	public void setIslock(int islock) {
		this.islock = islock;
	}
	public String getInvite_userid() {
		return invite_userid;
	}
	public void setInvite_userid(String invite_userid) {
		this.invite_userid = invite_userid;
	}
	public String getInvite_money() {
		return invite_money;
	}
	public void setInvite_money(String invite_money) {
		this.invite_money = invite_money;
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
	public String getCard_pic1() {
		return card_pic1;
	}
	public void setCard_pic1(String card_pic1) {
		this.card_pic1 = card_pic1;
	}
	public String getCard_pic2() {
		return card_pic2;
	}
	public void setCard_pic2(String card_pic2) {
		this.card_pic2 = card_pic2;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAvatar_status() {
		return avatar_status;
	}
	public void setAvatar_status(int avatar_status) {
		this.avatar_status = avatar_status;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getLitpic() {
		return litpic;
	}
	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWangwang() {
		return wangwang;
	}
	public void setWangwang(String wangwang) {
		this.wangwang = wangwang;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRemind() {
		return remind;
	}
	public void setRemind(String remind) {
		this.remind = remind;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public long getLogintime() {
		return logintime;
	}
	public void setLogintime(long logintime) {
		this.logintime = logintime;
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
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public String getUpip() {
		return upip;
	}
	public void setUpip(String upip) {
		this.upip = upip;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	public String getLastip() {
		return lastip;
	}
	public void setLastip(String lastip) {
		this.lastip = lastip;
	}
	public long getIs_phone() {
		return is_phone;
	}
	public void setIs_phone(long is_phone) {
		this.is_phone = is_phone;
	}
	public long getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(long memberLevel) {
		this.memberLevel = memberLevel;
	}
	public String getSerial_id() {
		return serial_id;
	}
	public void setSerial_id(String serial_id) {
		this.serial_id = serial_id;
	}
	public String getInvite_username() {
		return invite_username;
	}
	public void setInvite_username(String invite_username) {
		this.invite_username = invite_username;
	}
	public int getVip_status() {
		return vip_status;
	}
	public void setVip_status(int vip_status) {
		this.vip_status = vip_status;
	}
	
	/**
	 * 获取rule_promote_id
	 * 
	 * @return rule_promote_id
	 */
	public long getRule_promote_id() {
		return rule_promote_id;
	}
	/**
	 * 设置rule_promote_id
	 * 
	 * @param rule_promote_id 要设置的rule_promote_id
	 */
	public void setRule_promote_id(long rule_promote_id) {
		this.rule_promote_id = rule_promote_id;
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
	public String getSerial_status() {
		return serial_status;
	}
	public void setSerial_status(String serial_status) {
		this.serial_status = serial_status;
	}
	// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
	public void hideChar(){
		if(getReal_status()==1){
			if(!StringUtils.isBlank(getCard_id())){
				setCard_id(StringUtils.hideLastChar(getCard_id(), 4));
			}
			if(!StringUtils.isBlank(getRealname())){
				int len=2;
				if(getRealname().length()<3) len=1;
				setRealname(StringUtils.hideFirstChar(getRealname(), len));
			}
		}
		if(getPhone_status()==1){
			if(!StringUtils.isBlank(getPhone())){
				setPhone(StringUtils.hideLastChar(getPhone(), 4));
			}
		}
		if(getEmail_status()==1){
			if(!StringUtils.isBlank(getEmail())){
				String[] temp=getEmail().split("@");
				if(temp.length>1){
					setEmail(StringUtils.hideChar(temp[0],temp[0].length())+"@"+temp[1]);
				}
			}
		}
	}
	// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
}
