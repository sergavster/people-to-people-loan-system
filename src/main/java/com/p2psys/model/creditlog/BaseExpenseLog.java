package com.p2psys.model.creditlog;

/**
 * 消费积分父类
 
 * @version 1.0
 * @since 2013-10-23
 */
public class BaseExpenseLog  extends BaseCreditLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -141858044155521465L;

	
	public BaseExpenseLog() {
		super();
	}
	
	public BaseExpenseLog(long op , long op_user , long credit_type , long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
	}
	
}
