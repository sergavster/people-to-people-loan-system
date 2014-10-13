package com.p2psys.treasure.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.treasure.domain.TreasureRecharge;
import com.p2psys.treasure.model.TreasureRechargeModel;

/**
 * 理财宝转入信息DAO
 
 * @version 1.0
 * @since 2013-11-30
 */
public interface TreasureRechargeDao {

	/**
	 * 理财宝转入信息table分页数
	 * @param map
	 * @return
	 */
	public int getTreasureRechargeCount(Map<String , Object> map);
	
	/**
	 * 理财宝转入信息table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	public List<TreasureRechargeModel> getTreasureRechargePage(int page, int max,Map<String , Object> map);
	
	/**
	 * 添加理财宝转入信息
	 * @param item
	 * @return
	 */
	public long addTreasureRecharge(TreasureRecharge item);
	
	/**
	 * 查询理财宝转入信息
	 * @param id
	 * @return
	 */
	public TreasureRecharge getTreasureRecharge(long id);
	
	/**
	 * 修改理财宝转入信息
	 * @param money 转出额度
	 * @param id
	 * @return
	 */
	public boolean editRechargeUserMoney(double money, long id);
	
	/**
	 * 修改理财宝转入信息
	 * @param status 转入信息状态
	 * @param id
	 * @return
	 */
	public boolean editRechargeStatus(byte status, long id);
	
	/**
	 * 理财宝资金转入信息分页
	 * @param map 查询参数
	 * @return
	 */
	public List<TreasureRechargeModel> getTreasureRechargeList(Map<String , Object> map);
	
	/**
	 * 修改理财宝转入信息
	 * @param map 修改信息
	 * @return
	 */
	public boolean editRecharge(Map<String , Object> map);
	
	/**
	 * 用户投资金额查询
	 * @param map
	 * @return
	 */
	public double getRechargeMoney(Map<String , Object> map);
	
}
