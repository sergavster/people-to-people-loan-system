package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.dao.UserNoticeConfigDao;
import com.p2psys.domain.UserNoticeConfig;

public class UserNoticeConfigDaoImpl extends BaseDaoImpl implements  UserNoticeConfigDao {
	private static Logger logger = Logger.getLogger(UserNoticeConfigDaoImpl.class);

	@Override
	public UserNoticeConfig getUNConfig(long user_id, String noticeTypeNid) {
		List uncList =  this.findByProperty(UserNoticeConfig.class, new String[]{"user_id", "nid"}, new Object[]{user_id, noticeTypeNid});
		if (uncList.size() >= 1){
			return (UserNoticeConfig)uncList.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List getAllUNConfigs(long user_id) {
		return this.findByProperty(UserNoticeConfig.class, "user_id", user_id);
	}

	@Override
	public void updateUNConfigs(ArrayList<UserNoticeConfig> uncList, long user_id) {
		// TODO Auto-generated method stub
		String sql = " delete from  dw_user_notice_config where user_id = :user_id ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		getNamedParameterJdbcTemplate().update(sql, map);
		
		for (UserNoticeConfig userNoticeConfig : uncList) {
			insert(userNoticeConfig);
		}
		
	}

	
	
	

}
