package com.p2psys.dao.jdbc;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.RuleKeyDao;
import com.p2psys.domain.RuleKey;

public class RuleKeyDaoImpl extends BaseDaoImpl implements RuleKeyDao {

	private static Logger logger = Logger.getLogger(RuleKeyDaoImpl.class);

	@Override
	public RuleKey getRuleKeyById(Long id) {
		String sql = "select * from dw_rule_key where id = ?";
		RuleKey ruleKey = null;
		try {
			ruleKey = this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, getBeanMapper(RuleKey.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return ruleKey;
	}

}
