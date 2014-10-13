package com.p2psys.domain;

import java.io.Serializable;

public class AttestationType implements Serializable {

	private static final long serialVersionUID = -4876274026037570038L;

	private int type_id;
	private String name;
	private String order;
	private int status;
	private int jifen;
	private String summary;
	private String remark;
	private String addtime;
	private String addip;
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
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getJifen() {
		return jifen;
	}
	public void setJifen(int jifen) {
		this.jifen = jifen;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	@Override
	public String toString() {
		return "AttestationType [type_id=" + type_id + ", name=" + name
				+ ", order=" + order + ", status=" + status + ", jifen="
				+ jifen + ", summary=" + summary + ", remark=" + remark
				+ ", addtime=" + addtime + ", addip=" + addip + "]";
	}
	
	

}
