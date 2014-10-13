package com.p2psys.model.accountlog.borrow.tender;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 撤回标解冻投资人资金log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowCancelLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4595726664653420452L;
	private String templateNid = Constant.UNFREEZE;
	public BorrowCancelLog() {
		super();
	}
	public BorrowCancelLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowCancelLog(double money, Account act) {
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
}
