package com.p2psys.creditassignment.model;

import com.p2psys.creditassignment.domain.CreditAssignment;

public class CreditAssignmentModel extends CreditAssignment{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9135607484931197986L;
	
	/** 债权转让人 */
	private String username;
	
	/** 债权初审人 **/
	private String verify_username;
	
	/** 债权复审人 */
	private String full_verify_username;
	
	/** 债权标名 */
	private String borrow_name;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVerify_username() {
		return verify_username;
	}

	public void setVerify_username(String verify_username) {
		this.verify_username = verify_username;
	}

	public String getFull_verify_username() {
		return full_verify_username;
	}

	public void setFull_verify_username(String full_verify_username) {
		this.full_verify_username = full_verify_username;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	
}
