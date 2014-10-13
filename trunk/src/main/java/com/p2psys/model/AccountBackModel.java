package com.p2psys.model;
//v1.6.7.2 RDPROJECT-625 lx 2013-12-23 start
//新增类
//v1.6.7.2 RDPROJECT-625 lx 2013-12-23 end
import java.io.Serializable;

import com.p2psys.domain.AccountBack;
import com.p2psys.domain.RewardRecord;

/**
 * 扣款类
 
 * @version 1.0
 * @since 2013年12月23日
 */
public class AccountBackModel extends AccountBack implements Serializable {

	private static final long serialVersionUID = 5567840202564657915L;
	/**
	 * 被扣款用户
	 */
	private String username;
	/**
	 * 用户真实姓名
	 */
	private String realname;
	/**
	 * 审核人姓名
	 */
	private String verify_username;
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getVerify_username() {
		return verify_username;
	}

	public void setVerify_username(String verify_username) {
		this.verify_username = verify_username;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
