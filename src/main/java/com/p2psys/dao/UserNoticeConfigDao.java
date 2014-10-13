package com.p2psys.dao;

import java.util.ArrayList;
import java.util.List;

import com.p2psys.domain.UserNoticeConfig;


public interface UserNoticeConfigDao extends BaseDao {
	public UserNoticeConfig getUNConfig(long user_id, String noticeTypeNid);
	public List getAllUNConfigs(long user_id);
	public void updateUNConfigs(ArrayList<UserNoticeConfig> uncList, long user_id);
	
}
