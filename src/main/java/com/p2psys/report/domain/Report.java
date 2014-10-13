package com.p2psys.report.domain;

import java.io.Serializable;

/**
 * 导出报表table
 
 * @version 1.0
 * @since 2013-11-5
 */
public class Report implements Serializable{

	private static final long serialVersionUID = -0L;
	
	//主键ID 
	private long id;
	
	//报表名 
	private String name;
	
	//状态：0不启用，1启用
	private byte status;
	
	//报表名称 
	private String report_name;
	
	//报表类型名 
	private String nid;
	
	//报表类型:如exl,doc 
	private String type;
	
	//导出报表的url
	private String url;
	
	//报表存放的url
	private String report_url;
	
	//备注 
	private String remark;
	
	//添加时间 
	private int addtime;
	
	//修改时间 
	private int update_time;
	
	//操作者 
	private String operator;

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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getAddtime() {
		return addtime;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}

	public int getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReport_url() {
		return report_url;
	}

	public void setReport_url(String report_url) {
		this.report_url = report_url;
	}
	
}
