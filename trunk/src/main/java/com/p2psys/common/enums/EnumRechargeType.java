package com.p2psys.common.enums;

/**
 * 充值枚举类型
 
 * @version 1.0
 * @since 2013-8-21
 */
public enum EnumRechargeType {

	RECHARGE_ONLINE((byte) 1), // 线上充值
	
	RECHARGE_OFFLINE((byte) 2), // 线下充值
	
	RECHARGE_BANK((byte) 21), // 银行充值
	
	RECHARGE_OFFLINE_REWARD((byte) 22), // 线下充值奖励
	
	RECHARGE_CONTINUE((byte) 23), // 回款续投奖励充值
	
	RECHARGE_SINGLE((byte) 24), // 掉单充值
	
	RECHARGE_ACTIVITE((byte) 25), // 活动奖励充值
	
	RECHARGE_OTHER((byte) 26); // 其他充值
	
	EnumRechargeType(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}	
}
