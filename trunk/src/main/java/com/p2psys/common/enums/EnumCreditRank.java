package com.p2psys.common.enums;

/**
 * 积分等级
 * @version 1.0
 * @since 2013-10-23
 */
public enum EnumCreditRank {

	RANK_1(1),
	
	RANK_2(2),
	
	RANK_3(3),
	
	RANK_4(4),
	
	RANK_5(5),
	
	RANK_6(6),
	
	RANK_7(7),
	
	RANK_8(8),
	
	RANK_9(9),
	
	RANK_10(10);
	
	EnumCreditRank(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return this.value;
	}

}
