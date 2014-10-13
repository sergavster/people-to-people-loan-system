package com.p2psys.common.enums;

public enum EnumLogTemplateType {

	/**
	 * 资金合计日志
	 */
	SUM_LOG((byte) 1),
	
	/**
	 * 资金日志
	 */
	ACCOUNT_LOG((byte) 2),
	
	/**
	 * 操作日志
	 */
	OPERATION_LOG((byte) 3),

	/**
	 * 站内信
	 */
	MESSAGE_LOG((byte) 4),
	
	/**
	 * 积分日志模板
	 */
	CREDIT_LOG((byte) 5);
	
	EnumLogTemplateType(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
}
