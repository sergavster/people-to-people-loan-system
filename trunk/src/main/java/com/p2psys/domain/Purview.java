package com.p2psys.domain;

import java.io.Serializable;

public class Purview implements Serializable,java.lang.Comparable<Purview> {
	
	private static final long serialVersionUID = -540832868245774144L;
	
	private long id;
	private long pid;
	private String name;
	private int level;
	private String purview;
	private String url;
	private String remark;
	
	//Extend
	private boolean checked;
	private long user_type_id;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public String getPurview() {
		return purview;
	}
	public void setPurview(String purview) {
		this.purview = purview;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public long getUser_type_id() {
		return user_type_id;
	}
	public void setUser_type_id(long user_type_id) {
		this.user_type_id = user_type_id;
		if(this.user_type_id>0){
			this.setChecked(true);
		}else{
			this.setChecked(false);
		}
	}
	@Override
	public String toString() {
		return "Purview [id=" + id + ", pid=" + pid + ", name=" + name
				+ ", level=" + level + ", purview=" + purview + ", url=" + url
				+ ", remark=" + remark + "]";
	}
	@Override
	public int compareTo(Purview p) {
		if(p.getId()==this.getId()){
			return 0;
		}else if(p.getId()>this.getId()){
			return 1;
		}else{
			return -1;
		}
	}
	
	
	
	
	
}
