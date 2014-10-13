package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.NoticeType;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;


public interface NoticeTypeService {
	public PageDataList noticeTypeList(int page, SearchParam param) ;
	public void update(NoticeType noticeType);
	public List getAllSendNoticeType();
	public NoticeType getNoticeTypeByNid(String nid, byte notice_type);
	public List getList(); 
	//v1.6.7.2  RDPROJECT-535 liukun 2013-12-09 start
	//public PageDataList getList(int page, String nid); 
	//v1.6.7.2  RDPROJECT-535 liukun 2013-12-09 end
}
