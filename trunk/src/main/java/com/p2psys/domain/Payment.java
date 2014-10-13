package com.p2psys.domain;

import java.io.Serializable;

public class Payment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private long status;
    private String nid;
    private String style;
    private String config;
    private String escription;
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
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getEscription() {
		return escription;
	}
	public void setEscription(String escription) {
		this.escription = escription;
	}
    
}
