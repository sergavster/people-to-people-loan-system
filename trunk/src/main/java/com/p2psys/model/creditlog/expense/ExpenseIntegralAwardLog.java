package com.p2psys.model.creditlog.expense;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.model.creditlog.BaseExpenseLog;

/**
 * 抽奖消费积分
 
 * @version 1.0
 * @since 2013-10-23
 */
public class ExpenseIntegralAwardLog extends BaseExpenseLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logType = EnumCreditTemplateNid.EXPENSE_AWARD.getValue();
	
	public ExpenseIntegralAwardLog() {
		super();
	}
	
	public ExpenseIntegralAwardLog(long op, long op_user, long credit_type, long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
		this.setLogType(logType);
	}
}