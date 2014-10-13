package com.p2psys.treasure.service;

import java.util.Map;

import com.p2psys.model.PageDataList;
import com.p2psys.treasure.domain.TreasureCash;
import com.p2psys.treasure.model.TreasureCashModel;

public interface TreasureCashService {

	/**
	 * 理财宝资金转出信息分页
	 * @param page 分页起始页
	 * @param map 查询参数
	 * @return
	 */
	public PageDataList getTreasureCashPage(int page,Map<String , Object> map);
	
	/**
	 * 资金转出
	 * @param recharge_id 转入资金ID
	 * @param user_id 会员ID
	 * @param treasure_id 理财宝ID
	 * @return
	 */
	public boolean treasureCash(long treasure_id , long recharge_id , long user_id);
	
	/**
	 * 查询转出信息
	 * @param id
	 * @return
	 */
	public TreasureCashModel getTreasureCash(long id);
	
	/**
	 * 理财宝资金转出审核
	 * @param cash
	 * @return
	 */
	public boolean auditCash(TreasureCash cash);
}
