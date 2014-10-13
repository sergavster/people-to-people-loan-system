package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.RulePromote;
import com.p2psys.model.SearchParam;

/**
 * 推广奖励规则Dao
 * 
 
 * @version 1.0
 * @since 2013-10-16
 */
public interface RulePromoteDao extends BaseDao {
	
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
	 * @param start start
	 * @param pernum pernum
	 * @param param param
	 * @return list
	 */
	List<RulePromote> getAllRulePromote(int start, int pernum, SearchParam param);
	
	/**
	 * 分页条数
	 * @param param param
	 * @return int
	 */
	int rulePromoteTotal(SearchParam param);
	
	/**
	 * 根据状态取列表
	 * @param status status
	 * @return list
	 */
	List<RulePromote> getRuleByStatus(long status);

}
