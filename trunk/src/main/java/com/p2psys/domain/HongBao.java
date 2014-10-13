package com.p2psys.domain;

public class HongBao {

	public HongBao() {
		// TODO Auto-generated constructor stub
	}
    private long id;
    private String type;
    private String addtime;
    private String addip;
    private String remark;
    private long user_id;
    private double hongbao_money;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public HongBao(String type, String addtime, String addip,
			String remark, long user_id, double hongbao_money) {
		super();
		this.type = type;
		this.addtime = addtime;
		this.addip = addip;
		this.remark = remark;
		this.user_id = user_id;
		this.hongbao_money = hongbao_money;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public double getHongbao_money() {
		return hongbao_money;
	}
	public void setHongbao_money(double hongbao_money) {
		this.hongbao_money = hongbao_money;
	}
    
}
