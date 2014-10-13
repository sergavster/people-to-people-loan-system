package com.p2psys.common.enums;

/**
 *  
 * 水印图片位置枚举(watermark_place)：1右上角,2右下角,3左上角,4左下角,5中心
 * 
 
 * @version 1.0
 * @since 2013-8-6
 */
public enum EnumWatermarkPlace {
	
	PLACE_RIGHT_UP((byte) 1), //1右上角
	
	PLACE_RIGHT_DOWN((byte) 2),//2右下角
	
	PLACE_LEFT_UP((byte) 3),//3左上角
	
	PLACE_LEFT_DOWN((byte) 4),//4左下角
	
	PLACE_CENTER((byte) 5);//5中心
	
	EnumWatermarkPlace(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
}
