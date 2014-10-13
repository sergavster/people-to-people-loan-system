package com.p2psys.domain;

import java.io.Serializable;

/**
 * 联合登陆实体
 
 */

public class CooperationLogin implements Serializable {

	private static final long serialVersionUID = -7665220614505321585L;

	//编号
	private long id;
	
	//合作登陆类型:1QQ,2sina
	private Byte type;
	
	//会员编号
	private long user_id;
	
	//外部ID
	private String open_id;
	
	//外部Key
	private String open_key;
	
	//创建时间
	private String gmt_create;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getOpen_key() {
		return open_key;
	}

	public void setOpen_key(String open_key) {
		this.open_key = open_key;
	}

	public String getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

}
