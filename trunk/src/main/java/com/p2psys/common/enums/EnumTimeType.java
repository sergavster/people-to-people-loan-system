package com.p2psys.common.enums;

/**
 * 时间类型
 * @version 1.0
 * @since 2013-10-24
 */
public enum EnumTimeType {

	IS_DAY(1), // 天
	
	IS_MONTH(2),//月
	
	IS_YEAR(3); //年
	
	EnumTimeType(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return this.value;
	}

}
