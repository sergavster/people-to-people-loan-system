package com.p2psys.common.enums;

/**
 * 联合登陆类型枚举
 
 *
 */
public enum EnumCooperationLoginType {

	COOPERATION_QQ((byte) 1), // QQ联合登陆
	COOPERATION_SINA((byte) 2); // sina微博联合登陆
	
	EnumCooperationLoginType(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
	
}
