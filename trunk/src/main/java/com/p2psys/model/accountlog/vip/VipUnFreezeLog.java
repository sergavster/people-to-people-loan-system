package com.p2psys.model.accountlog.vip;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseAccountLog;

/**
 * vip免年费续期
 * 
 
 * @version 1.0
 * @since 2013-11-11
 */
public class VipUnFreezeLog extends BaseAccountLog {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1737659197816330571L;
	
	/**
	 * 类型：vip免年费续期
	 */
	private String templateNid = Constant.VIP_UNFREEZE;

	/**
	 * vip免年费续期
	 * @param money money
	 * @param act act
	 * @param toUser toUser
	 */
	public VipUnFreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	/**
	 * vip免年费续期
	 * @param money money
	 * @param act act
	 */
	public VipUnFreezeLog(double money, Account act) {
		super(money, act);
	}

	/**
	 * vip免年费续期
	 */
	public VipUnFreezeLog() {
		super();
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
}
