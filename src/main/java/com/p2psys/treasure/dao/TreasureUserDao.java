package com.p2psys.treasure.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.treasure.domain.TreasureUser;
import com.p2psys.treasure.model.TreasureUserModel;

public interface TreasureUserDao {
	
	/**
	 * 理财宝信息table分页数
	 * @param map
	 * @return
	 */
	public int getTreasureUserCount(Map<String , Object> map);
	
	/**
	 * 理财宝信息table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	public List<TreasureUserModel> getTreasureUserPage(int page, int max,Map<String , Object> map);
	
	/**
	 * 添加用户理财宝信息
	 * @param model
	 * @return
	 */
	public long addTreasure(TreasureUser item);
	
	/**
	 * 查询用户理财宝信息
	 * @param 
	 * @return
	 */
	public TreasureUser getTreasureUserByUserId(long user_id);

	/**
	 * 修改理财宝信息
	 * @param model
	 * @return
	 */
	public boolean editTreasureUser(double total,double interest_total,double  use_moeny ,long user_id);
}
