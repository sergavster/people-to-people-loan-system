package com.p2psys.domain;

import java.io.Serializable;

/**
 * 规则表实体
 
 *
 */
public class Rule implements Serializable {

	private static final long serialVersionUID = 8971537069152829541L;
	
	/** 主键ID */
	private long id;
	
	/** 规则名称 */
	private String name;
	
	/** 规则状态：1启用，0停用 */
	private byte status;
	
	/** 创建时间 */
	private String addtime;
	
	/** 规则类型名 */
	private String nid;
	
	/** 规则备注 */
	private String remark;
	
	/** 规则约束，数据库存JSON */
	private String rule_check;
	
	/**
	 * 获取id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置id
	 * 
	 * @param id 要设置的id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置name
	 * 
	 * @param name 要设置的name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取status
	 * 
	 * @return status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * 设置status
	 * 
	 * @param status 要设置的status
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * @return the addtime
	 */
	public String getAddtime() {
		return addtime;
	}

	/**
	 * @param addtime the addtime to set
	 */
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	/**
	 * 获取nid
	 * 
	 * @return nid
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置nid
	 * 
	 * @param nid 要设置的nid
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}

	/**
	 * 获取remark
	 * 
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置remark
	 * 
	 * @param remark 要设置的remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取rule_check
	 * 
	 * @return rule_check
	 */
	public String getRule_check() {
		return rule_check;
	}

	/**
	 * 设置rule_check 
	 * 
	 * @param rule_check 要设置的rule_check
	 */
	public void setRule_check(String rule_check) {
		this.rule_check = rule_check;
	}
	
}
