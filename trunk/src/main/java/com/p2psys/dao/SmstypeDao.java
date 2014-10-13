package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Smstype;
import com.p2psys.model.SearchParam;

public interface SmstypeDao extends BaseDao {

	//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 start
	public List getList(int start, int pernum, SearchParam param);
	//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 end
	public List getList();
	public int getListCount() ;
	public void add(Smstype smstype);
	public void update(Smstype smstype);
	public List getAllSendSmstype();
	public Smstype getSmsTypeByNid(String nid);
}
