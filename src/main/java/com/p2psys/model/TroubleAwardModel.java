package com.p2psys.model;

import java.io.Serializable;

import com.p2psys.domain.TroubleAwardRecord;
/**
 * 急难基金奖励收支实体的子类
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-7-26
 */
public class TroubleAwardModel extends TroubleAwardRecord implements Serializable {
	
	private static final long serialVersionUID = 2737053575621240588L;
	//会员用户名
	private String username;
	//会员真实姓名
	private String realname;
	//奖励金额
	private String award_money;  
	//转入基金
	private String into_funds; 

	public String getAward_money() {
		return award_money;
	}

	public void setAward_money(String award_money) {
		this.award_money = award_money;
	}

	public String getInto_funds() {
		return into_funds;
	}

	public void setInto_funds(String into_funds) {
		this.into_funds = into_funds;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	

	
}
