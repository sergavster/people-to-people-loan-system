package com.p2psys.model.accountlog.treasure;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseTreasureLog;

/**
 * 理财宝投资冻结资金
 
 *
 */
public class TreasureFreezeLog extends BaseTreasureLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3421345633422627294L;
	/**
	 * 
	 */
	private String templateNid = Constant.TREASURE_FREEZE;
	
	public TreasureFreezeLog() {
		super();
	}
	public TreasureFreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public TreasureFreezeLog(double money, Account act) {
		super(money, act);
	}
	public String getTemplateNid(){
		return templateNid;
	}
}
