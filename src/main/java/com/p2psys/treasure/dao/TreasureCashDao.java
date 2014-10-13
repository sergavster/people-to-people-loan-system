package com.p2psys.treasure.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.treasure.domain.TreasureCash;
import com.p2psys.treasure.model.TreasureCashModel;

public interface TreasureCashDao {

	/**
	 * 理财宝转出信息table分页数
	 * @param map
	 * @return
	 */
	public int getTreasureCashCount(Map<String , Object> map);
	
	/**
	 * 理财宝转出信息table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	public List<TreasureCashModel> getTreasureCashPage(int page, int max,Map<String , Object> map);
	
	/**
	 * 添加理财宝转出信息
	 * @param item
	 * @return
	 */
	public long addTreasureCash(TreasureCash item);
	
	/**
	 * 查询转出信息
	 * @param id
	 * @return
	 */
	public TreasureCashModel getTreasureCash(long id);
	
	
	/**
	 * 修改转出信息
	 * @param id
	 * @return
	 */
	public boolean eidtTreasureCash(Map<String , Object> map);
	
}
