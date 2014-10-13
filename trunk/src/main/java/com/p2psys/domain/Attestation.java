package com.p2psys.domain;

import java.io.Serializable;

public class Attestation implements Serializable {

	private static final long serialVersionUID = -4876274026037570038L;

	private long id;
	private long user_id;
	private int type_id;
	private String name;
	private int status;
	private String litpic;
	private String content;
	private int jifen;
	private String pic;
	private String pic2;
	private String pic3;
	private String verify_time;
	private long verify_user;
	private String verify_remark;
	private String addtime;
	private String addip;
	
	private String type_name;
	
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
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
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
	public String getLitpic() {
		return litpic;
	}
	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getJifen() {
		return jifen;
	}
	public void setJifen(int jifen) {
		this.jifen = jifen;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPic2() {
		return pic2;
	}
	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}
	public String getPic3() {
		return pic3;
	}
	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}
	public String getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}
	public long getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(long verify_user) {
		this.verify_user = verify_user;
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
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	
	
}
