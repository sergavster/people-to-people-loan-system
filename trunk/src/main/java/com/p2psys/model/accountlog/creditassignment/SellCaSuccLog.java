package com.p2psys.model.accountlog.creditassignment;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseAccountLog;
/**
 * 债权出售费用入帐
 * 
 
 * @version 1.0
 * @since 2013-12-17
 */
public class SellCaSuccLog extends BaseAccountLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 208290003628121004L;
	
	private String templateNid = Constant.LT_CA_SELL_SUCC;
	public SellCaSuccLog() {
		super();
	}
	public SellCaSuccLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public SellCaSuccLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	
}
