package com.p2psys.treasure.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.treasure.domain.Treasure;
import com.p2psys.treasure.model.TreasureModel;

/**
 * 理财宝信息DAO接口
 
 * @version 1.0
 * @since 2013-11-27
 */
public interface TreasureDao {

	/**
	 * 理财宝信息table分页数
	 * @param map
	 * @return
	 */
	public int getTreasureCount(Map<String , Object> map);
	
	/**
	 * 理财宝信息table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	public List<TreasureModel> getTreasurePage(int page, int max,Map<String , Object> map);
	
	/**
	 * 查询理财宝信息
	 * @param map
	 * @return
	 */
	public List<TreasureModel> getTreasure(Map<String , Object> map);
	
	/**
	 * 查询理财宝信息
	 * @param map
	 * @return
	 */
	public Treasure getTreasureById(long id);
	
	/**
	 * 修改理财宝信息
	 * @param model
	 * @return
	 */
	public boolean editTreasure(Treasure item);
	
	/**
	 * 添加理财宝信息
	 * @param model
	 * @return
	 */
	public long addTreasure(Treasure item);
	
	/**
	 * 修改特殊信息方法
	 * 审核信息or停止/启用理财宝信息
	 * @param model
	 * @return
	 */
	public boolean specialEdit(Map<String,Object> map);
	
	/**
	 * 修改理财宝信息投资额度
	 * @param invest 投资额度
	 * @param id
	 * @return
	 */
	public boolean editTreasureInvest(double invest, long id);
}
