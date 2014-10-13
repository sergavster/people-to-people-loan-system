package com.p2psys.model.account;

import com.p2psys.domain.OperationLog;

public class OperationLogModel extends OperationLog{
	
	private static final long serialVersionUID = 784451181230576969L;
	private String verify_username;
	private String username;
	private String typename;
	public String getUsertypename() {
		return usertypename;
	}
	public void setUsertypename(String usertypename) {
		this.usertypename = usertypename;
	}
	private String usertypename;
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
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	
	
	
}
