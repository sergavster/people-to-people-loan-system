package com.p2psys.service;

import com.p2psys.domain.AutoTenderOrder;
import com.p2psys.domain.Repayment;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.borrow.BorrowModel;

public interface AutoBorrowService {
	/**
	 * 满标放款进程处理
	 * @param borrow
	 */
	public void autoVerifyFullSuccess(BorrowModel borrow);
	/**
	 * 满标复审失败处理
	 * @param borrow
	 */
	public void autoVerifyFullFail(BorrowModel borrow);
	/**
	 * 撤回标处理
	 * @param borrow
	 */
	public void autoCancel(BorrowModel borrow);
//	/**
//	 * 秒标处理
//	 * @param borrow
//	 */
//	public void autoDoRepayForSecond(BorrowModel borrow);
	/**
	 * 还款处理
	 * @param repay
	 */
	public void autoRepay(Repayment repay);
	/**
	 * 流转标还款处理
	 * @param c
	 */
	public void autoFlowRepay(DetailCollection c);
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	//TODO RDPROJECT-314 DELETE
	/**
	 * 批量提醒，站内信、邮件、短信
	 */
	/*public void batchRepayTimer();*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	
	
	public void autoDealTender(BorrowModel borrow);
	/**
	 * 爱心捐助标满标放款操作
	 * author:yinliang
	 * date:2013/06/07
	 * @param borrow
	 */
	public void autoVerifyFullSuccessForDonation(BorrowModel borrow);
//	public List getAutoTenderOrderList();
	
	public AutoTenderOrder getAutoTenderOrderByUserid(long user_id);
}
