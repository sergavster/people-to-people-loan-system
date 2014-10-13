package com.p2psys.dao;

import com.p2psys.domain.RuleKey;

/**
 * 规则key DAO
 */
public interface RuleKeyDao extends BaseDao {

	/**
	 * 根据规则key表主键ID查询数据
	 * @param id 主键ID
	 * @return 规则实体
	 */
	public RuleKey getRuleKeyById(Long id);
	
}
