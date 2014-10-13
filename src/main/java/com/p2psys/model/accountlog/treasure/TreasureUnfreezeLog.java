package com.p2psys.model.accountlog.treasure;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseTreasureLog;

/**
 * 理财宝资金赎回，解冻本金
 
 *
 */
public class TreasureUnfreezeLog extends BaseTreasureLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8597323998503109792L;
	
	private String templateNid = Constant.TREASURE_UNFREEZE;
	
	public TreasureUnfreezeLog() {
		super();
	}
	
	public TreasureUnfreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	
	public TreasureUnfreezeLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
