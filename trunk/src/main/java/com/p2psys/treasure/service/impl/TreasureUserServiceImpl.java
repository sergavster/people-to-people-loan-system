package com.p2psys.treasure.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.p2psys.model.PageDataList;
import com.p2psys.tool.Page;
import com.p2psys.treasure.dao.TreasureUserDao;
import com.p2psys.treasure.domain.TreasureUser;
import com.p2psys.treasure.service.TreasureUserService;

@Service
public class TreasureUserServiceImpl implements TreasureUserService {

	@Resource
	private TreasureUserDao treasureUserDao;
	
	@Override
	public PageDataList getTreasureUserPage(int page, Map<String, Object> map) {
		if(map == null) map = new HashMap<String, Object>();
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(treasureUserDao.getTreasureUserCount(map) , page );
		pageDataList.setList(treasureUserDao.getTreasureUserPage(pages.getStart(), pages.getPernum() , map));
		pageDataList.setPage(pages);
		return pageDataList;
	}
	
	/**
	 * 理财宝用户信息查询
	 * @param user_id
	 * @return
	 */
	public TreasureUser getTreasureUserByUserId(long user_id){
		return treasureUserDao.getTreasureUserByUserId(user_id);
	}

}
