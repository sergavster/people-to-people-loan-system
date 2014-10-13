package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

/**
 * 借款奖励扣除
 * 
 
 * @version 1.0
 * @since 2013-9-4
 */
public class DeductAwardLog extends DeductSuccessLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2406147548880262016L;

	/**
	 * 类型：借款奖励扣除
	 */
	private String templateNid = Constant.AWARD_DEDUCT;

	/**
	 * 借款奖励扣除
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public DeductAwardLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 借款奖励扣除
	 * @param money money
	 * @param act act
	 */
	public DeductAwardLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 借款奖励扣除
	 */
	public DeductAwardLog() {
		super();
	}

	public String getTemplateNid(){
		return templateNid;
	}

	private final static String noticeTypeNid = Constant.NOTICE_DEDUCT_BORROWER_AWARD;
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	}

	@Override
	public void updateAccount() {
		accountDao.updateAccount(-this.getMoney(), -this.getMoney(), 0, 0, this.getUser_id());
	}
	
	
}
