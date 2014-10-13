package com.p2psys.common.enums;

public enum EnumCashType {

	/**
	 * 第一提现规则：例如温州贷
	 */
	FIRST_CASH_RULE((int) 1),
	/**
	 * 第二提现规则：例如爱贷
	 */
	SECOND_CASH_RULE((int) 2),
	
	/**
	 * 第三提现规则：例如中贷信创
	 */
	THREE_CASH_RULE((int) 3);

	EnumCashType(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return this.value;
	}
}
