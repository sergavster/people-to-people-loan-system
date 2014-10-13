package com.p2psys.model.borrow;

import com.p2psys.tool.Page;

public interface SqlQueryable {
	
	public String getTypeSql();
	
	public String getStatusSql();
	
	public String getOrderSql();
	
	public String getSearchParamSql();
	
	public String getLimitSql();
	
	public String getPageStr(Page p);
	
	public String getSerachStr();
	
	public String getOrderStr();
	
}
