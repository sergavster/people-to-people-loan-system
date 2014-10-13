package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.UserLog;

public class UserLogModel extends UserLog implements Serializable {

	private static final long serialVersionUID = -6799451812033085927L;

	private String userName;
	private String realName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}
