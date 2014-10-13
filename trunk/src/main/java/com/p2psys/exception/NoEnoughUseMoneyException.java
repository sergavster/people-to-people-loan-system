package com.p2psys.exception;

/**
 * 异常实体-可用余额不足
 
 * @version 1.0
 * @since 2013年12月10日 下午11:52:00
 */
public class NoEnoughUseMoneyException extends BorrowException {
	
	private static final long serialVersionUID = -7400559552805824955L;

	public NoEnoughUseMoneyException() {
		super("可用余额不足！");
	}

	public NoEnoughUseMoneyException(String message) {
		super("可用余额不足，"+message);
	}
	
	

}
