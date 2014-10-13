package com.p2psys.model.accountlog;

import com.p2psys.domain.Account;
/**
 * 还款父类
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BaseBorrowLog extends BaseAccountLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	
	public BaseBorrowLog() {
		super();
	}
	public BaseBorrowLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BaseBorrowLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getLogRemarkTemplate() {
		return super.getLogRemarkTemplate();
	}
	
	@Override
	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}
	
	@Override
	public String getType() {
		return super.getType();
	}
	@Override
	public void addAccountSumLog() {
		super.addAccountSumLog();
	}
	@Override
	public void accountSumProperty() {
		super.accountSumProperty();
	}  
	
	
}
