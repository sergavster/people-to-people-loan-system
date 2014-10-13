package com.p2psys.model.accountlog.deduct;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.DeductSuccessLog;

public class DeductSmsLog extends DeductSuccessLog {

	private static final long serialVersionUID = -5051474075711976740L;

	private String templateNid=Constant.SMS_FEE;
	
	public DeductSmsLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public DeductSmsLog(double money, Account act) {
		super(money, act);
	}

	public DeductSmsLog() {
		super();
	}
	

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
