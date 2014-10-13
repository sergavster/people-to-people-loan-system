package com.p2psys.model;

import java.util.List;

import com.p2psys.tool.Page;

public class PageDataList {
	private Page page;
	private List list;
	private int type;
	
	public PageDataList() {
		super();
	}
	public PageDataList(Page page, List list) {
		super();
		this.page = page;
		this.list = list;
	}
	
	public PageDataList(Page page, List list,int type) {
		super();
		this.page = page;
		this.list = list;
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	

}
