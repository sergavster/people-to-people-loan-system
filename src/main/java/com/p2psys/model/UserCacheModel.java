package com.p2psys.model;

import com.p2psys.domain.UserCache;

/**
 * 增加客服用户名、真实姓名属性
 * 
 
 * @date 2012-7-18-下午4:33:53
 * @version
 * 
 *           (c)</b> 2012-51-<br/>
 * 
 */
public class UserCacheModel extends UserCache implements java.io.Serializable {

	private static final long serialVersionUID = -889562087705028166L;

	private String kefu_name;
	private String kefu_realname;
	private String credit_pic;
	private String credit_jifen;
	//用户name
	private String username;
	private String type_id;
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	/*private String smstype_config;
	private String smspay_endtime;*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	
	//v1.6.7.1 RDPROJECT-384 wcw 2013-11-05 start
	private String valid_vip_time;
	private String last_vip_time;
	
	
	public String getLast_vip_time() {
		return last_vip_time;
	}

	public void setLast_vip_time(String last_vip_time) {
		this.last_vip_time = last_vip_time;
	}

	public String getValid_vip_time() {
		return valid_vip_time;
	}

	public void setValid_vip_time(String valid_vip_time) {
		this.valid_vip_time = valid_vip_time;
	}
	//v1.6.7.1 RDPROJECT-384 wcw 2013-11-05 end

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getKefu_name() {
		return kefu_name;
	}

	public void setKefu_name(String kefu_name) {
		this.kefu_name = kefu_name;
	}

	public String getKefu_realname() {
		return kefu_realname;
	}

	public void setKefu_realname(String kefu_realname) {
		this.kefu_realname = kefu_realname;
	}

	public String getCredit_pic() {
		return credit_pic;
	}

	public void setCredit_pic(String credit_pic) {
		this.credit_pic = credit_pic;
	}

	public String getCredit_jifen() {
		return credit_jifen;
	}

	public void setCredit_jifen(String credit_jifen) {
		this.credit_jifen = credit_jifen;
	}
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
	/*public String getSmstype_config() {
		return smstype_config;
	}

	public void setSmstype_config(String smstype_config) {
		this.smstype_config = smstype_config;
	}

	public String getSmspay_endtime() {
		return smspay_endtime;
	}

	public void setSmspay_endtime(String smspay_endtime) {
		this.smspay_endtime = smspay_endtime;
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 end



}
