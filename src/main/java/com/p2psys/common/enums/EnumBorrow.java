package com.p2psys.common.enums;

/**
 * 阿拉伯数字
 */
public enum EnumBorrow {
	
	NOPAYFLOW("nopayflow"),
	PAYFLOW("payflow");
	EnumBorrow(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return this.value;
	}
	
}
