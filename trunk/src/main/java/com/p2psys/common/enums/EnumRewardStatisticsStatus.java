package com.p2psys.common.enums;

/**
 * 奖励统计（返现状态:1未返现，2已返现，3返现审核不通过，4返现失败，5无用数据）
 */
public enum EnumRewardStatisticsStatus {
	
	CASH_UN_BACK((byte) 1), // 未返现
	CASH_BACK((byte) 2), // 已返现
	VERIFY_UN_PASS((byte) 3), // 返现审核不通过
	CASH_BACK_FAIL((byte) 4), // 返现失败
	NO_USE((byte) 5); // 无用数据
	
	EnumRewardStatisticsStatus(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
	
}
