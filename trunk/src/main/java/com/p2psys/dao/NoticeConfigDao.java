package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.NoticeConfig;

public interface NoticeConfigDao extends BaseDao {

	public List getList();
	public int getListCount() ;
	public void add(NoticeConfig noticeConfig);
}
