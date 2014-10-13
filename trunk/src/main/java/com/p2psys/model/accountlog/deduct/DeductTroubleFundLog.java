package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

/**
 * 急难基金捐款金额
 * 
 
 * @version 1.0
 * @since 2013-9-4
 */
public class DeductTroubleFundLog extends DeductSuccessLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1373242641075017666L;
	
	/**
	 * 类型：捐赠急难基金
	 */
	private String templateNid = Constant.TROUBLE_DONATE;

	/**
	 * 急难基金捐赠金额
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public DeductTroubleFundLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * 急难基金捐赠金额
	 * @param money money
	 * @param act act
	 */
	public DeductTroubleFundLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * 急难基金捐赠金额
	 */
	public DeductTroubleFundLog() {
		super();
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
