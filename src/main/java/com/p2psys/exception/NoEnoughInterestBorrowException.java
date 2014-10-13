package com.p2psys.exception;

public class NoEnoughInterestBorrowException extends BorrowException {
	
	private static final long serialVersionUID = -7400559552805824955L;

	public NoEnoughInterestBorrowException() {
		super("没有足够的利息.");
	}

	public NoEnoughInterestBorrowException(String message) {
		super("没有足够的利息."+message);
	}
	
	

}
