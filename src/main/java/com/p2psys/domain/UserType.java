package com.p2psys.domain;

public class UserType {
	private long type_id;
	private String name;
	private String purview;
	private String order;
	private int status;
	private int type;
	private String summary;
	private String remark;
	private String addtime;
	private String addip;
	
	
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public long getType_id() {
		return type_id;
	}
	public void setType_id(long type_id) {
		this.type_id = type_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPurview() {
		return purview;
	}
	public void setPurview(String purview) {
		this.purview = purview;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	@Override
	public String toString() {
		return "UserType [type_id=" + type_id + ", name=" + name + ", purview="
				+ purview + ", order=" + order + ", status=" + status
				+ ", type=" + type + ", summary=" + summary + ", remark="
				+ remark + ", addtime=" + addtime + "]";
	}
	

}
