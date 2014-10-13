package com.p2psys.model.account;

import java.util.List;

import com.p2psys.domain.AccountRecharge;
import com.p2psys.tool.Page;

public class AccountRechargeList {
	private List<AccountRecharge> list;
	private Page page;
	public List<AccountRecharge> getList() {
		return list;
	}
	public void setList(List<AccountRecharge> list) {
		this.list = list;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
}
