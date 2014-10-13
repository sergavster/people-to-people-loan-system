package com.p2psys.service;


public interface NoticePayBorrowService {
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	//TODO RDPROJECT-314 DELETE
	/**
	 * 还款提现通知 （一周以内）
	 */
	//public void autoNoticePayBorrow( );
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	/**
	 * 计算逾期的利息和天数
	 */
	public void CalcLateInterest();
	/**
	 * 流转标到期提醒
	 */
	public void autoNoticeFlowBorrow();
	/**
	 * VIP到期生日到期生日提醒
	 */
	public void autoNoticeVIPBirthday();
	
	/**
	 * 还款提醒 （提前一天）
	 */
	public void autoBorrowerRepayNotice( );
	
	/**
	 * 收款提醒 （提前一天）
	 */
	public void autoLoanerRepayNotice( );	
	
}
