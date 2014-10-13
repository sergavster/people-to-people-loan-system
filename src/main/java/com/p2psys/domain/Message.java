package com.p2psys.domain;

import java.io.Serializable;

import com.p2psys.util.DateUtils;

/**
 * 
 * 消息实体类
 
 * @date 2012-8-29 下午4:29:40
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class Message extends Notice implements Serializable {

	private static final long serialVersionUID = -4537010262059676447L;
	
	private long id;
	private long sent_user;
	private long receive_user;
	private String name;
	private int status;
	private String type;
	private String sented;
	private int deltype;
	private String content;
	private String addtime;
	private String addip;
	
	private String sent_username;
	private String receive_username;
	private String is_Authenticate;
	
	public Message() {
		super();
		this.setName("message");
		this.setAddtime(DateUtils.getNowTimeStr());
	}
	public String getIs_Authenticate() {
		return is_Authenticate;
	}
	public void setIs_Authenticate(String is_Authenticate) {
		this.is_Authenticate = is_Authenticate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSent_user() {
		return sent_user;
	}
	public void setSent_user(long sent_user) {
		this.sent_user = sent_user;
	}
	public long getReceive_user() {
		return receive_user;
	}
	public void setReceive_user(long receive_user) {
		this.receive_user = receive_user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSented() {
		return sented;
	}
	public void setSented(String sented) {
		this.sented = sented;
	}
	public int getDeltype() {
		return deltype;
	}
	public void setDeltype(int deltype) {
		this.deltype = deltype;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getSent_username() {
		return sent_username;
	}
	public void setSent_username(String sent_username) {
		this.sent_username = sent_username;
	}
	public String getReceive_username() {
		return receive_username;
	}
	public void setReceive_username(String receive_username) {
		this.receive_username = receive_username;
	}
	@Override
	public String warpContent() {
		return this.getContent();
	}
	
}
