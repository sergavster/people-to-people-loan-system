package com.p2psys.domain;

import java.io.Serializable;

/**
 * 日志备注模板
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-10-11
 */
public class LogTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7391106171964637892L;
	
	private long id;
	
	//信息类型:1资金日志，2合计日志，3会员日志
	private byte type;
	
	//日志类型
	private String log_type;
	
	//模板信息
	private String value;
	
	//模板备注
	private String remark;
	
	// 模板类型
	private String nid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getLog_type() {
		return log_type;
	}

	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}
}
