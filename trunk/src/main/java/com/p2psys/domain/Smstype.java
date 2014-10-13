package com.p2psys.domain;

public class Smstype {
	public Smstype(){
		
	}

	private long id;
	private String nid;
	private String name;
	private byte type;
	private byte canswitch;
	private String templet;
	private String remark;
	private long addtime;
	private String addip;
	private long updatetime;
	private String updateip;
	private byte send;
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

	
}
