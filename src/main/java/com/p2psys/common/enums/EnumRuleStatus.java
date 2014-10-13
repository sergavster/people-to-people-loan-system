package com.p2psys.common.enums;

public enum EnumRuleStatus {

	RULE_STATUS_YES((byte) 1), // 规则启用
	RULE_STATUS_NO((byte) 0); // 规则停用
	
	EnumRuleStatus(byte value) {
		this.value = value;
	}

	private byte value;

	public byte getValue() {
		return this.value;
	}
}
