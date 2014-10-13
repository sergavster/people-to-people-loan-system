package com.p2psys.common.enums;

public enum EnumHuiKuanStatus {

	HUIKUAN_AUDIT("0"),//回款待审核
	
	HUIKUAN_SUCCESS("1"),// 回款审核成功
	
	HUIKUAN_CASH("3"),// 提现失败或用户提现额度返还
	
	HUIKUAN_FAIL("4"),// 回款审核失败
	
	HUIKUAN_BACK("5");// 回款撤回
	
	EnumHuiKuanStatus(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return this.value;
	}
	
}
