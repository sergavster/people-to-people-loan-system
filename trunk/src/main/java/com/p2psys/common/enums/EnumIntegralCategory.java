package com.p2psys.common.enums;

/**
 * 
 * 会员积分种类
 * 
 
 * @version 1.0
 * @since 2013-7-23
 */
public enum EnumIntegralCategory {

	/**
	 * 投资积分
	 */
	INTEGRAL_TENDER((byte) 1),
	/**
	 * 借款积分
	 */
	INTEGRAL_BORROW((byte) 2),
	/**
	 * 赠送积分
	 */
	INTEGRAL_GIFT((byte) 3),
	/**
	 * 消费积分
	 */
	INTEGRAL_EXPENSE((byte) 4),
	/**
	 * 可用积分
	 */
	INTEGRAL_VALID((byte) 5),
	
	/**
	 * 总积分
	 */
	INTEGRAL_VALUE((byte) 6),
	
	/**
	 *论坛积分 
	 */
	BBS_VALUE((byte) 7); 
	
	private byte value;
	
	EnumIntegralCategory(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return this.value;
	}
}
