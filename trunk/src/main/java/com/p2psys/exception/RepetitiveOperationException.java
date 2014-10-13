package com.p2psys.exception;

/**
 * 异常实体-重复操作
 
 * @version 1.0
 * @since 2013年12月19日 下午9:14:39
 */
public class RepetitiveOperationException extends BorrowException {
	
	private static final long serialVersionUID = -7400559552805824955L;

	public RepetitiveOperationException() {
		super("重复操作！");
	}

	public RepetitiveOperationException(String message) {
		super("重复操作，"+message);
	}
	
	

}
