package com.p2psys.common.enums;

/**
 * 积分兑换信息状态
 
 * @version 1.0
 * @since 2013-10-16
 */
public enum EnumConvertStatus {

	/**
	 * 0未审核
	 */
	WAIT_AUDIT((byte) 0), 
	
	/**
	 * 1审核通过
	 */
	PASS_AUDIT((byte) 1),
	
	/**
	 * 2审核不通过
	 */
	NOT_PASS_AUDIT((byte)2),
	
	/**
	 * 3无用数据
	 */
	FAIL_AUDIT((byte)3);
	
	EnumConvertStatus(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
}
