package com.p2psys.domain;

import java.io.Serializable;

/**
 * 好友类
 
 * @date 2012-8-31 下午6:53:52
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class Friend implements Serializable {

	private static final long serialVersionUID = -2603972601624997167L;
	private long id;
	private long user_id;
	private long friends_userid;
	private String friends_username;
	private int status;
	private int type;
	private String content;
	private String addtime;
	private String addip;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long userId) {
		user_id = userId;
	}

	public long getFriends_userid() {
		return friends_userid;
	}

	public void setFriends_userid(long friendsUserid) {
		friends_userid = friendsUserid;
	}

	public String getFriends_username() {
		return friends_username;
	}

	public void setFriends_username(String friendsUsername) {
		friends_username = friendsUsername;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}
}
