package com.p2psys.model.accountlog.recharge;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseRechargeSuccessLog;

public class OffRechargeSuccessLog extends BaseRechargeSuccessLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2729521833285820558L;

	private String templateNid = Constant.AL_OFF_RECHARGE;
	
	private final static String noticeTypeNid = Constant.NOTICE_DOWN_RECHARGE_VERIFY_SUCC;
	
	public OffRechargeSuccessLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public OffRechargeSuccessLog(double money, Account act) {
		super(money, act);
	}

	public OffRechargeSuccessLog() {
		super();
	}
	

	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	}
	
	@Override
	public void addOperateLog() {
		// TODO Auto-generated method stub
		
	}
	
}
