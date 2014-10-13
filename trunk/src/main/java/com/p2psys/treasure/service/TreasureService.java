package com.p2psys.treasure.service;

import java.util.List;
import java.util.Map;

import com.p2psys.model.PageDataList;
import com.p2psys.treasure.domain.Treasure;
import com.p2psys.treasure.domain.TreasureRecharge;
import com.p2psys.treasure.model.TreasureModel;

/**
 * 理财宝信息Service接口
 
 * @version 1.0
 * @since 2013-11-27
 */
public interface TreasureService {

	/**
	 * 理财宝信息分页
	 * @param page 分页起始页
	 * @param map 查询参数
	 * @return
	 */
	public PageDataList getTreasurePage(int page,Map<String , Object> map);
	
	/**
	 * 根据主键ID查询理财宝信息
	 * @param id
	 * @return
	 */
	public TreasureModel getTreasureById(long id);
	
	/**
	 * 根据主键ID查询理财宝信息
	 * @param id
	 * @return
	 */
	public Treasure getTreasureItemById(long id);
	
	/**
	 * 修改理财宝信息
	 * @param model
	 * @return
	 */
	public boolean editTreasure(Treasure model);
	
	/**
	 * 添加理财宝信息
	 * @param model
	 * @return
	 */
	public long addTreasure(Treasure item);
	
	/**
	 * 审核信息理财宝信息
	 * @param item
	 * @return
	 */
	public boolean auditTreasure(Treasure item);
	
	/**
	 * 修改理财宝信息状态
	 * @param id 主键
	 * @param status 0停用/1启用
	 * @param operator 操作者
	 * @return
	 */
	public boolean editTreasureStatus(long id , byte status ,String operator);
	
	/**
	 * 理财宝投资
	 * @param item
	 * @return
	 */
	public boolean invest(TreasureRecharge item);
	
	/**
	 * 查询理财宝信息
	 * @param map
	 * @return
	 */
	public List<TreasureModel> getTreasure(Map<String , Object> map);
	
	/**
	 * 赎回审核停用/启用
	 * @param id 主键
	 * @param back_verify_type 0不需要审核/1需要审核
	 * @param operator 操作者
	 * @return
	 */
	public boolean editBackStatus(long id , byte back_verify_type ,String operator);
	
}
