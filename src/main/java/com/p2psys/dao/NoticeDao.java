package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Notice;
import com.p2psys.model.SearchParam;

public interface NoticeDao extends BaseDao {

	public List<Notice> getList(int start, int pernum, SearchParam param);
	public int getListCount(SearchParam param) ;
	public void add(Notice notice);
}
