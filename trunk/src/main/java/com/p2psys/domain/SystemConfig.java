package com.p2psys.domain;

public class SystemConfig {
	private long id;
	private String name;
	private String nid;
	private String value;
	private int type;
	private int style;
	private String status;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStyle() {
		return style;
	}
	public void setStyle(int style) {
		this.style = style;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "SystemConfig [id=" + id + ", name=" + name + ", nid=" + nid
				+ ", value=" + value + ", type=" + type + ", style=" + style
				+ ", status=" + status + "]";
	}
	
	
}
