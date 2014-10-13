package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Rule;
import com.p2psys.model.SearchParam;

/**
 * 规则DAO
 */
public interface RuleDao extends BaseDao {

	/**
	 * 根据规则表主键ID查询数据
	 * @param id 主键ID
	 * @return 规则实体
	 */
	public Rule getRuleById(long id);
	
	
	/**
	 * 根据规则类型名查询数据
	 * @param nid 规则类型名
	 * @return 规则实体
	 */
	public Rule getRuleByNid(String nid);
	
	
	/**
	 * 根据状态获取所有启用的Rule规则
	 * @param status
	 * @return
	 */
	public List<Rule> getRuleList(int status);
	
	/**
	 * 查询所有的规则
	 * @param status
	 * @return
	 */
	public List<Rule> getRuleAll();
	
	/**
	 * 根据nid修改规则JSON
	 * @param nid
	 * @param rule_check
	 */
	public void updateRuleCheck(String nid , String rule_check);
	
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 start
	/**
	 * 根据规则表主键ID查询数据
	 * @param id 主键ID
	 * @return 规则实体
	 */
	Rule getRule(long id);
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 end
	
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-25 start 
	public List<Rule> getList(String id);
	public int getListCount(SearchParam param) ;
	public List<Rule> getList(int start, int pernum, SearchParam param);
	public Rule getRuleById(String rid);
	public void updateRule(Rule rule);
	public void addRule(Rule rule);
	//v1.6.7.1RDPROJECT-510 cx 2013-12-25 end
}
