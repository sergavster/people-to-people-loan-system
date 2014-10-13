package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.Smstype;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;


public interface SmstypeService {
	//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 start
	//public PageDataList smstypeList(int page) ;
	public PageDataList smstypeList(int page, SearchParam param) ;
	//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 end
	public void add(Smstype smstype);
	public void update(Smstype smstype);
	public List getAllSendSmstype();
	public Smstype getSmsTypeByNid(String nid);
	//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 start
	public List getList(); 
	//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 end
}
