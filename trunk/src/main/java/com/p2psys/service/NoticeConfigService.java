package com.p2psys.service;

import com.p2psys.domain.NoticeConfig;
import com.p2psys.model.PageDataList;


public interface NoticeConfigService {
	public PageDataList noticeConfigList(int page) ;
	public void add(NoticeConfig noticeConfig);
	
}
