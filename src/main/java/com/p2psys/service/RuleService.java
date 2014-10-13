package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.Rule;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

/**
 * 规则Service
 */
public interface RuleService {

	/**
	 * 根据规则表主键ID查询数据
	 * @param id 主键ID
	 * @return 规则实体
	 */
	public Rule getRuleById(Long id);
	
	
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
	
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 start
	/**
	 * 根据规则Id查询规则
	 * @param id id
	 * @return rule
	 */
	Rule getRule(Long id);
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 end
	
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-25 start 
	/**
	 * 规则配置查询
	 * @param page
	 * @param id
	 * @return
	 */
	public PageDataList getList(int page, String id); 
	public PageDataList ruleList(int page, SearchParam param) ;
	public Rule getRuleById(String rid);
	public void updateRule(Rule rule);
	public void addRule(Rule rule);
	//v1.6.7.1RDPROJECT-510 cx 2013-12-25 end
}
