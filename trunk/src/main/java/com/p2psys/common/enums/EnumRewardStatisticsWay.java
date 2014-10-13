package com.p2psys.common.enums;

/**
 * 奖励统计返现
 */
public enum EnumRewardStatisticsWay {
	
	MONEY((int) 1), // 金额返现
	PERCENT((int) 2);; // 百分比返现
	
	EnumRewardStatisticsWay(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return this.value;
	}
}
