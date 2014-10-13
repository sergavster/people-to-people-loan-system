package com.p2psys.report.domain;

import java.io.Serializable;

/**
 * 导出报表列名表
 
 * @version 1.0
 * @since 2013-11-5
 */
public class Column implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5462951832096783811L;
	
	//主键ID 
	private long id;
	
	//中文列名 
	private String china_name;
	
	//英文列名 
	private String column_name;
	
	//状态：0,停用，1启用
	private byte status;
	
	//添加时间 
	private Integer addtime;
	
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

	public String getChina_name() {
		return china_name;
	}

	public void setChina_name(String china_name) {
		this.china_name = china_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
