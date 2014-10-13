package com.p2psys.common.enums;

/**
 * 图片枚举
 
 *
 */
public enum EnumPic {
	ZERO((byte) 0), //所有图片
	FIRST((byte) 1), //首页滚动图片
	SECOND((byte) 2),//合作伙伴图片
	THREE((byte)3); //友情链接图片
	EnumPic(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
	
}
