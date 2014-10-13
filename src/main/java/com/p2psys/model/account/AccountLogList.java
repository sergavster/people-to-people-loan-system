package com.p2psys.model.account;

import java.util.List;

import com.p2psys.tool.Page;

public class AccountLogList {
	private Page page;
	private List<AccountLogModel> list;
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public List<AccountLogModel> getList() {
		return list;
	}
	public void setList(List<AccountLogModel> list) {
		this.list = list;
	}
	
	
	
}
