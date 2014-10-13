package com.p2psys.report.model;

import com.p2psys.report.domain.Column;

/**
 * 导出报表列名表Model
 
 * @version 1.0
 * @since 2013-11-5
 */
public class ColumnModel extends Column {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 关系表主键ID
	private long report_column_id;
	
	// 关系表状态
	private byte report_column_status;
	
	// 排序：越小越排在前面
	private byte ordering;
	
	//报表主键ID 
	private long report_id;
	
	//报表名 
	private String name;
	
	//报表名称 
	private String report_name;
	
	//报表类型名 
	private String report_nid;
	
	// 报表状态
	private byte report_status;

	public long getReport_id() {
		return report_id;
	}

	public void setReport_id(long report_id) {
		this.report_id = report_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public String getReport_nid() {
		return report_nid;
	}

	public void setReport_nid(String report_nid) {
		this.report_nid = report_nid;
	}

	public byte getReport_column_status() {
		return report_column_status;
	}

	public void setReport_column_status(byte report_column_status) {
		this.report_column_status = report_column_status;
	}

	public byte getReport_status() {
		return report_status;
	}

	public void setReport_status(byte report_status) {
		this.report_status = report_status;
	}

	public long getReport_column_id() {
		return report_column_id;
	}

	public void setReport_column_id(long report_column_id) {
		this.report_column_id = report_column_id;
	}

	public byte getOrdering() {
		return ordering;
	}

	public void setOrdering(byte ordering) {
		this.ordering = ordering;
	}
}
