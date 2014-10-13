package com.p2psys.model.accountlog.borrow.verifyfullBorrow;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 普通标满标审核失败解冻投资人的资金log
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowFailLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4595726664653420452L;
	private String templateNid = Constant.UNFREEZE_NO_PASS;
	public BorrowFailLog() {
		super();
	}
	public BorrowFailLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowFailLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	private final static String noticeTypeNid = Constant.NOTICE_INVEST_FAIL;
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	}
	@Override
	public void updateAccount() {
		accountDao.updateAccount(0, this.getMoney(), -this.getMoney(), 0, this.getUser_id());
	} 
	
	
}
