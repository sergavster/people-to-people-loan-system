package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Protocol;
import com.p2psys.model.ProtocolModel;
import com.p2psys.model.SearchParam;


public interface ProtocolDao extends BaseDao {
	
	public void addProtocol(Protocol protocol) ;
	public int getProtocolCount(SearchParam param);
	public List getProtocolList(int start, int pernum, SearchParam param);
	public List getProtocolList(SearchParam param);
	public ProtocolModel getProtocolById(long id);
}
