package com.p2psys.model.accountlog.borrow.tender;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 生成待收利息log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class FlowBorrowWaitInterestLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	private String templateNid=Constant.WAIT_INTEREST;
	public FlowBorrowWaitInterestLog() {
		super();
	}
	public FlowBorrowWaitInterestLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public FlowBorrowWaitInterestLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void updateAccount() {
		accountDao.updateAccount(this.getMoney(), 0, 0, this.getMoney(), this.getUser_id());
	}
	
}
