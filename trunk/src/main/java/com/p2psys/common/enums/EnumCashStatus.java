package com.p2psys.common.enums;

/**
 * 提现规则状态
 * @version 1.0
 * @since 2013-9-27
 */
public enum EnumCashStatus {

	CASH_STATUS_NO((byte) 0), // 停用
	CASH_STATUS_YES((byte) 1); // 启用
	
	EnumCashStatus(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
}
