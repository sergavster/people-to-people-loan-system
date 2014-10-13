package com.p2psys.service;

import com.p2psys.domain.RuleKey;

/**
 * 规则key service
 */
public interface RuleKeyService {

	/**
	 * 根据规则key表主键ID查询数据
	 * @param id 主键ID
	 * @return 规则实体
	 */
	public RuleKey getRuleKeyById(Long id);
	
}
