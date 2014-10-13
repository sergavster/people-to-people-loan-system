package com.p2psys.common.enums;

/**
 * 抽奖类型
 */
public enum EnumAwardType {
	/**
	 * 按积分抽奖
	 */
	AWARD_TYPE_POINT((int) 1),
	/**
	 * 按次数抽奖
	 */
	AWARD_TYPE_TIMES((int) 2),
	/**
	 * 按倍率抽奖
	 */
	AWARD_TYPE_RATIO((int) 3);

	/**
	 * 抽奖类型
	 */
	private final int value;

	/**
	 * 设置抽奖类型
	 * 
	 * @param value 抽奖类型
	 */
	EnumAwardType(int value) {
		this.value = value;
	}

	/**
	 * 获取抽奖类型
	 * 
	 * @return 抽奖类型
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * 重写equals
	 * 
	 * @param value 抽奖类型
	 * @return 比较结果
	 */
	public boolean equals(int value) {
		if (this.value == value) {
			return true;
		}
		return false;
	}
}
