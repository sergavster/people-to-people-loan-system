package com.p2psys.model.accountlog.award;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;

/**
 * 好友邀请奖励
 
 *
 */
public class AwardGiveInviteInvestLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.INVITE_AWARD;
	
	public AwardGiveInviteInvestLog() {
		super();
	}

	public AwardGiveInviteInvestLog(double money, long user, long toUser) {
		super(money, new Account(user), toUser);
	}

	public AwardGiveInviteInvestLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}

	@Override
	public void updateAccount() {
		accountDao.updateAccount(this.getMoney(), this.getMoney(), 0,this.getUser_id());
	}
	
	
	
}
