package com.p2psys.quartz.notice;

public interface LoanTask {
	
	public static String LOAN_STATUS="false";
	
	public static String SMS_STATUS="false";
	
	public static String MESSAGE_STATUS="false";
	
	public void execute();
	
	public void doLoan();
	
	public void stop();
	
	public Object getLock();
	
}
