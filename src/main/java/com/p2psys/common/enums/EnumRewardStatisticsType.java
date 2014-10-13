package com.p2psys.common.enums;

/**
 * 奖励统计（信息状态:1充值奖励，2邀请人奖励，3被邀请人奖励，4投标奖励，5回款续投奖励,6第一次投标奖励）
 */
public enum EnumRewardStatisticsType {
	// v1.6.7.2 RDPROJECT-547 lx 2013-12-6 start
	/**充值奖励*/
	RECHARGE_AWARD((byte) 1),
	/**邀请人奖励*/
	INVITE_AWARD((byte) 2),
	/**被邀请人奖励*/
	INVITER_AWARD((byte) 3),
	/**投标奖励*/
	TENDER_AWARD((byte) 4),
	/**回款续投奖励*/
	HUIKUAN_AWARD((byte) 5),
	/**第一次投标奖励*/
	FIRST_TENDER_REWARD((byte) 6),
	// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 start
	/**【民生创投】邀请好友首次投标奖励*/
	INVITE_FIRST_TENDER_AWARD((byte) 7), //民生创投邀请好友首次投标奖励
	// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 end
	// v1.6.6.1 RDPROJECT-235 zza 2013-10-18 start
	/**【及时雨】推广奖励*/
	RULE_PROMOTE((byte) 8),//及时雨推广奖励规则
	// v1.6.6.1 RDPROJECT-235 zza 2013-10-18 end
	/**还款奖励*/
	REPAY_AWARD((byte) 9);
	// v1.6.7.2 RDPROJECT-547 lx 2013-12-6 end
	
	EnumRewardStatisticsType(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
}
