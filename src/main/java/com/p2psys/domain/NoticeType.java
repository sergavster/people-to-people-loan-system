package com.p2psys.domain;

public class NoticeType {

	public NoticeType(){
		
	}

	private long id;
	private String nid;
	private byte notice_type;
	private String name;
	private byte type;
	private byte canswitch;
	private byte send;
	private String title_templet;
	private String templet;
	private String remark;
	private long addtime;
	private String addip;
	private long updatetime;
	private String updateip;
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
	private String send_route;
	
	public String getSend_route() {
		return send_route;
	}
	public void setSend_route(String send_route) {
		this.send_route = send_route;
	}
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getCanswitch() {
		return canswitch;
	}
	public void setCanswitch(byte canswitch) {
		this.canswitch = canswitch;
	}
	public String getTemplet() {
		return templet;
	}
	public void setTemplet(String templet) {
		this.templet = templet;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getAddtime() {
		return addtime;
	}
	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdateip() {
		return updateip;
	}
	public void setUpdateip(String updateip) {
		this.updateip = updateip;
	}
	public byte getSend() {
		return send;
	}
	public void setSend(byte send) {
		this.send = send;
	}
	public String getTitle_templet() {
		return title_templet;
	}
	public void setTitle_templet(String title_templet) {
		this.title_templet = title_templet;
	}
	public byte getNotice_type() {
		return notice_type;
	}
	public void setNotice_type(byte notice_type) {
		this.notice_type = notice_type;
	}

	
}
