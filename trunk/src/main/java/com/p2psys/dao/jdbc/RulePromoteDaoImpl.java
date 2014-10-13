package com.p2psys.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.RulePromoteDao;
import com.p2psys.domain.RulePromote;
import com.p2psys.model.SearchParam;

/**
 * 推广奖励规则实现类
 * 
 
 * @version 1.0
 * @since 2013-10-16
 */
public class RulePromoteDaoImpl extends BaseDaoImpl implements RulePromoteDao {

	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(UserCacheDaoImpl.class);

	@Override
	public void addRulePromote(RulePromote rulePromote) {
		StringBuffer sql = new StringBuffer("insert into dw_rule_promote( name, status, "
				+ "count_up, count_down, award_percent, addtime, remark) value ( ");
		sql.append(" :name,:status,:count_up,:count_down,:award_percent,:addtime,:remark)");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(rulePromote);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);

	}

	@Override
	public void modifyRulePromote(RulePromote rulePromote) {
		String sql = "update dw_rule_promote set name = :name, "
				+ "status = :status, count_up = :count_up, count_down = :count_down, award_percent = :award_percent, "
				+ "remark = :remark where id = :id";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + rulePromote.getId());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(rulePromote);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);

	}

	/**
	 * 根据用户id查询信息
	 * 
	 * @param id id
	 * @return rulePromote
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RulePromote getRulePromoteById(long id) {
		String sql = "select * from dw_rule_promote where id = :id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql, map,
				getBeanMapper(RulePromote.class));
	}

	@Override
	public void delRulePromote(long id) {
		String sql = "delete from dw_rule_promote where id = :id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		getNamedParameterJdbcTemplate().update(sql, map);
	}

	/**
	 * 获取规则列表
	 * 
	 * @param start start
	 * @param pernum pernum
	 * @param param param
	 * @return list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RulePromote> getAllRulePromote(int start, int pernum, SearchParam param) {
		String sql = "select * from dw_rule_promote where 1=1";
		sql += param.getSearchParamSql();
		sql += " limit :start,:pernum";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("pernum", pernum);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(RulePromote.class));
	}

	@Override
	public int rulePromoteTotal(SearchParam param) {
		String sql = "select count(*) from dw_rule_promote where 1=1";
		sql += param.getSearchParamSql();
		try {
			return getNamedParameterJdbcTemplate().queryForInt(sql, new BeanPropertySqlParameterSource(Integer.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 根据状态取列表
	 * 
	 * @param status status
	 * @return list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RulePromote> getRuleByStatus(long status) {
		String sql = "select * from dw_rule_promote where status = :status";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(RulePromote.class));
	}

}
