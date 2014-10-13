package com.p2psys.exception;

public class BorrowException extends BussinessException {
	
	private static final long serialVersionUID = -7400559552805824955L;

	public BorrowException() {
		super();
	}

	public BorrowException(String message) {
		super(message);
	}

	public BorrowException(String message, String url) {
		super(message, url);
	}
	
	

}
