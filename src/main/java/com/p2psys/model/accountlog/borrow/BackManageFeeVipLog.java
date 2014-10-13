package com.p2psys.model.accountlog.borrow;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseBackManageFeeLog;

/**
 * 利息管理费优惠
 * 
 
 * @version 1.6.6.2
 * @since 2013-10-22
 */
public class BackManageFeeVipLog extends BaseBackManageFeeLog {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6276117799952711010L;

	
	/**
	 * 类型：利息管理费
	 */
	private String templateNid = Constant.BACK_MANAGE_FEE_VIP;

	/**
	 * 利息管理费
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public BackManageFeeVipLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 利息管理费
	 * @param money money
	 * @param act act
	 */
	public BackManageFeeVipLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 利息管理费
	 */
	public BackManageFeeVipLog() {
		super();
	}

	/**
	 * 利息管理费
	 * @return type
	 */
	public String getTemplateNid(){
		return templateNid;
	}

	@Override
	public void updateAccount() {
		accountDao.updateAccount(this.getMoney(), this.getMoney(), 0, 0, this.getUser_id());
	}
	
	
	
}
