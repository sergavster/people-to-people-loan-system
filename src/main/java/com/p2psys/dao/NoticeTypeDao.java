package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.NoticeType;
import com.p2psys.model.SearchParam;

public interface NoticeTypeDao extends BaseDao {

	public List<NoticeType> getList(int start, int pernum, SearchParam param);
	public List<NoticeType> getList();
	public int getListCount(SearchParam param) ;
	public void update(NoticeType noticeType);
	public List<NoticeType> getAllSendNoticeType();
	public NoticeType getNoticeTypeByNid(String nid, byte notice_type);
	//v1.6.7.2  RDPROJECT-535 liukun 2013-12-09 start
	//public List<NoticeType> getList(String nid);
	//v1.6.7.2  RDPROJECT-535 liukun 2013-12-09 end
}
