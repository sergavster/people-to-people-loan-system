package com.p2psys.exception;

public class ManageAuthException extends RuntimeException {
	
	private static final long serialVersionUID = -7400559552805824955L;
	
	public ManageAuthException(String msg, RuntimeException ex) {
		super(msg, ex);
	}

	public ManageAuthException() {
		super();
	}

	public ManageAuthException(String message) {
		super(message);
	}
	
	

}
