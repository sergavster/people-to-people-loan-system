package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.HongBao;

/**
 * 用户个人资料信息更新的Model,user和usrinfo
 * 
 
 * 
 */
public class HongBaoModel extends HongBao implements Serializable {

	private static final long serialVersionUID = 6026604036412862478L;

	private String username;
	private String typename;
	private String hongbao;

	public String getHongbao() {
		return hongbao;
	}

	public void setHongbao(String hongbao) {
		this.hongbao = hongbao;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
