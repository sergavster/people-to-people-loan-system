package com.p2psys.model.accountlog.creditassignment;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseAccountLog;
/**
 * 认购债权转让时冻结认购费用
 * 
 
 * @version 1.0
 * @since 2013-12-17
 */
public class BuyCaFreezeLog extends BaseAccountLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 208290003628121004L;
	
	private String templateNid = Constant.LT_CA_BUY_FREEZE;
	public BuyCaFreezeLog() {
		super();
	}
	public BuyCaFreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BuyCaFreezeLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	
}
