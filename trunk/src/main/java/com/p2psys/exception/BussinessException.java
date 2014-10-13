package com.p2psys.exception;

public class BussinessException extends RuntimeException {
	private static final long serialVersionUID = 538922474277376456L;

	private String url;
	
	public BussinessException(String msg, RuntimeException ex) {
		super(msg, ex);
	}
	
	public BussinessException() {
		super();
	}

	public BussinessException(String message) {
		super(message);
	}
	
	public BussinessException(String message,String url) {
		super(message);
		//v1.6.7.2 RDPROJECT-616 sj 2013-12-20 start
		this.url = url;
		//v1.6.7.2 RDPROJECT-616 sj 2013-12-20 end
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
