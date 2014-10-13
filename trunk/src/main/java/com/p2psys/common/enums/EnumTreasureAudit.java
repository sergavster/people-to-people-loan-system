package com.p2psys.common.enums;

/**
 * 理财宝审核状态
 
 * @version 1.0
 * @since 2013-11-28
 */
public enum EnumTreasureAudit {

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
	NOT_PASS((byte)2);
	
	EnumTreasureAudit(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
	
}
