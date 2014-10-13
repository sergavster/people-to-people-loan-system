package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.OperationLog;
import com.p2psys.model.SearchParam;
import com.p2psys.model.account.OperationLogModel;


public interface OperationLogDao extends BaseDao {
	
	public void addOperationLog(OperationLog log) ;
	public int getOperationLogCount(SearchParam param) ;
	public List<OperationLogModel> getOperationLogList(int start, int pernum, SearchParam param) ;
	public List<OperationLogModel> getOperationLogList(SearchParam param) ;
}
