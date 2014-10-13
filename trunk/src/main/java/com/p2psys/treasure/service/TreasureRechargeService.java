package com.p2psys.treasure.service;

import java.util.List;
import java.util.Map;

import com.p2psys.model.PageDataList;
import com.p2psys.treasure.model.TreasureRechargeModel;

public interface TreasureRechargeService {

	/**
	 * 理财宝资金转入信息分页
	 * @param page 分页起始页
	 * @param map 查询参数
	 * @return
	 */
	public PageDataList getTreasureRechargePage(int page,Map<String , Object> map);
	
	/**
	 * 理财宝资金转入信息分页
	 * @param map 查询参数
	 * @return
	 */
	public List<TreasureRechargeModel> getTreasureRechargeList(Map<String , Object> map);
	
	/**
	 * 理财宝投资利息定时计算
	 * @return
	 */
	public boolean doTreasureInterest();
}
