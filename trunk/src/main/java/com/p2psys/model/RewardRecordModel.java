package com.p2psys.model;
//v1.6.7.2 RDPROJECT-547 lx 2013-12-6 start
//新增类
//v1.6.7.2 RDPROJECT-547 lx 2013-12-6 end
import java.io.Serializable;

import com.p2psys.domain.RewardRecord;

/**
 * 奖励记录
 
 * @version 1.0
 * @since 2013年12月8日
 */
public class RewardRecordModel extends RewardRecord implements Serializable {

	private static final long serialVersionUID = 5567840202564657915L;

	/**
	 * 获得奖励用户
	 */
	private String username;
	/**
	 * 发放奖励者
	 */
	private String passive_username;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassive_username() {
		return passive_username;
	}
	
	public void setPassive_username(String passive_username) {
		this.passive_username = passive_username;
	}
	
}
