package com.p2psys.common.enums;

/**
 * 理财宝投资信息状态
 
 *
 */
public enum EnumTreasureRechStatus {

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
	 * 3转出审核中
	 */
	BACK_AUDIT((byte)3),
	
	/**
	 * 4已转出
	 */
	BACK_SUCCESS((byte)4),
	
	/**
	 * 5转出失败
	 */
	BACK_FAIL((byte)5);;
	
	EnumTreasureRechStatus(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
	
}
