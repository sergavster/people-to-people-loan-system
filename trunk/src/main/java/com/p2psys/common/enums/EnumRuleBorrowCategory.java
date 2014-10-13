package com.p2psys.common.enums;

/**
 * 标种分类（天标、月标）
 */
public enum EnumRuleBorrowCategory {
	NO_BORROW_CATEGORY((byte) -1), // 无类型
	BORROW_MONTH((byte) 0), // 月标
	BORROW_DAY((byte) 1); // 天标
	EnumRuleBorrowCategory(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
	public boolean equal(int param){
		return false;
	}
}
