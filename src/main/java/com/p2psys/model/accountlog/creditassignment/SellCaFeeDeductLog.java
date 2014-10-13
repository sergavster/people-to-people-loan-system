package com.p2psys.model.accountlog.creditassignment;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.BaseBorrowLog;
/**
 * 认购债权转让时冻结认购费用
 * 
 
 * @version 1.0
 * @since 2013-12-17
 */
public class SellCaFeeDeductLog extends BaseAccountLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 208290003628121004L;
	
	private String templateNid = Constant.LT_CA_SELL_FEE_DEDUCT;
	public SellCaFeeDeductLog() {
		super();
	}
	public SellCaFeeDeductLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public SellCaFeeDeductLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	
}
