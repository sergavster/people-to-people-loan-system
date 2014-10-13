package com.p2psys.common.enums;

/**
 * 奖励统计（返现类型:1定时返现，2自动返现，3人工返现）
 */
public enum EnumRewardStatisticsBackType {
	
	TIME_BACK((byte) 1), // 定时返现
	AUTO_BACK((byte) 2), // 自动返现
	ARTIFICIAL_BACK((byte) 3); // 人工返现
	
	EnumRewardStatisticsBackType(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
	
	public boolean equal(int param){
		return false;
	}
}
