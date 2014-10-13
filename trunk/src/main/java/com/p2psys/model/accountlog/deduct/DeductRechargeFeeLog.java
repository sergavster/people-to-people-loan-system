package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

public class DeductRechargeFeeLog extends DeductSuccessLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7703802281386638471L;

	private String templateNid = Constant.ACCOUNT_RECHARGE_FEE;
	
	public DeductRechargeFeeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public DeductRechargeFeeLog(double money, Account act) {
		super(money, act);
	}

	public DeductRechargeFeeLog() {
		super();
	}
	

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
}
