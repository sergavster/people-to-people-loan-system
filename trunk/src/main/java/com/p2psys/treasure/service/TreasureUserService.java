package com.p2psys.treasure.service;

import java.util.Map;

import com.p2psys.model.PageDataList;
import com.p2psys.treasure.domain.TreasureUser;

public interface TreasureUserService {

	/**
	 * 理财宝用户信息分页
	 * @param page 分页起始页
	 * @param map 查询参数
	 * @return
	 */
	public PageDataList getTreasureUserPage(int page,Map<String , Object> map);
	

	/**
	 * 理财宝用户信息查询
	 * @param user_id
	 * @return
	 */
	public TreasureUser getTreasureUserByUserId(long user_id);
}
