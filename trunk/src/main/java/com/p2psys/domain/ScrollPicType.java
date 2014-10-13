package com.p2psys.domain;

import java.io.Serializable;
/**
 * 图片类型实体类
 
 *
 */
public class ScrollPicType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	/**
	 * 主键id
	 */
	private long id;
	/**
	 * 图片类型名称
	 */
	private String typename;
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
	 * @return the typename
	 */
	public String getTypename() {
		return typename;
	}
	/**
	 * @param typename the typename to set
	 */
	public void setTypename(String typename) {
		this.typename = typename;
	}
}
