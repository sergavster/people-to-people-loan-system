package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.RulePromote;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

/**
 * 推广奖励规则Service
 * 
 
 * @version 1.0
 * @since 2013-10-16
 */
public interface RulePromoteService {
	/**
	 * 添加规则
	 * @param rulePromote rulePromote
	 */
	void addRulePromote(RulePromote rulePromote);
	
	/**
	 * 修改规则
	 * @param rulePromote rulePromote
	 */
	void modifyRulePromote(RulePromote rulePromote);
	
	/**
	 * 根据规则ID取得抽奖规则信息
	 * @param id 规则ID
	 * @return 抽奖规则信息
	 */
	RulePromote getRulePromoteById(long id);
	
	/**
	 * 根据id删除规则
	 * @param id id
	 */
	void delRulePromote(long id);
	
	/**
	 * 获取规则列表
	 * @param page page
	 * @param param param
	 * @return list
	 */
	PageDataList getAllRulePromote(int page, SearchParam param);
	
	/**
	 * 根据状态取列表
	 * @param status status
	 * @return list
	 */
	List<RulePromote> getRuleByStatus(long status);
}
