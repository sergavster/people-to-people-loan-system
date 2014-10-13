package com.p2psys.common.enums;

/**
 * 理财宝信息状态 
 
 * @version 1.0
 * @since 2013-11-28
 */
public enum EnumTreasureStatus {

	/**
	 * 0停用
	 */
	STOP((byte) 0), 
	
	/**
	 * 1启用
	 */
	START((byte) 1);
	
	private byte value;
	
	EnumTreasureStatus(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return this.value;
	}
}
