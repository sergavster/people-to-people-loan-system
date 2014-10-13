package com.p2psys.common.enums;

/**
 * 奖励统计（返现状态:1未返现，2已返现，3返现审核不通过，4返现失败，5无用数据）
 */
public enum EnumTroubleFund {
	
	FIRST((byte) 1), //无偿捐赠   admin用户id    判断是否是奖金类型      奖励支出
	ZERO((byte) 0), //有偿捐赠   抽奖奖励初始化     判断是否是基金类型       奖励收入
	SECOND((byte) 2); //非0、1
	
	EnumTroubleFund(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
	
}
