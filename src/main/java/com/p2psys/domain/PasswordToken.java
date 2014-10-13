package com.p2psys.domain;

import java.io.Serializable;

/**
 * 密保
 
 */
public class PasswordToken implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4978559276371568191L;
	
	/**
	 * id
	 */
	private long id;
	/**
	 * 用户id
	 */
	private long user_id;
	/**
	 * 问题
	 */
	private String question;
	/**
	 * 答案
	 */
	private String answer;
	
	//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc start
	/**
	 * 用户名
	 */
	private String username;
	//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc end
		
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc start
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc end
	
}
