package com.p2psys.report.domain;

import java.io.Serializable;

/**
 * 报表与列名关系表
 
 * @version 1.0
 * @since 2013-11-6
 */
public class ReportColumn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6719174380075706383L;
	
	//主键ID 
	private int id;
	
	//报表主键ID 
	private long report_id;
	
	//报表列名ID 
	private long column_id;
	
	//中文列名 
	private String china_name;
	
	//排序 :越小越在前面
	private int ordering;
	
	//添加时间 
	private int addtime;
	
	//状态：0停用，1启用 
	private byte status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getReport_id() {
		return report_id;
	}

	public void setReport_id(long report_id) {
		this.report_id = report_id;
	}

	public long getColumn_id() {
		return column_id;
	}

	public void setColumn_id(long column_id) {
		this.column_id = column_id;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	public int getAddtime() {
		return addtime;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getChina_name() {
		return china_name;
	}

	public void setChina_name(String china_name) {
		this.china_name = china_name;
	}

}
