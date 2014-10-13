package com.p2psys.domain;

import java.io.Serializable;

/**
 * 规则的json key表
 */
public class RuleKey implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3699904413691849255L;

	/** 主键ID */
	private long id;
	
	/** key值 */
	private String key;
	
	/** 名称 */
	private String name;
	
	/** key的类型 */
	private String type;
	
	/** value值 */
	private String value;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
