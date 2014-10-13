package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.RuleDao;
import com.p2psys.domain.Rule;
import com.p2psys.model.SearchParam;

public class RuleDaoImpl extends BaseDaoImpl implements RuleDao {

	private static Logger logger = Logger.getLogger(RuleDaoImpl.class);

	@Override
	public Rule getRuleById(long id) {
		String sql = "select * from dw_rule where id = ? and status = 1";
		Rule rule = null;
		try {
			rule = this.getJdbcTemplate().queryForObject(sql, new Object[] { id },
					getBeanMapper(Rule.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return rule;
	}

	@Override
	public Rule getRuleByNid(String nid) {
		String sql = "select * from dw_rule where nid = ?";
		Rule rule = null;
		try {
			rule = this.getJdbcTemplate().queryForObject(sql, new Object[] { nid }, getBeanMapper(Rule.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return rule;
	}
	
	@Override
	public List<Rule> getRuleList(int status) {
		String sql = "select * from dw_rule where status=?";
		List<Rule> list = new ArrayList<Rule>();
		try {
			list = this.getJdbcTemplate().query(sql, new Object[] { status }, getBeanMapper(Rule.class));
		} catch (Exception e) {
		}
		return list;
	}
	/**
	 * 查询所有的规则
	 * @param status
	 * @return
	 */
	public List<Rule> getRuleAll(){
		String sql = "select * from dw_rule ";
		List<Rule> list = new ArrayList<Rule>();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(Rule.class));
		} catch (Exception e) {
		}
		return list;
	}
	
	/**
	 * 根据nid修改规则JSON
	 * @param nid
	 * @param rule_check
	 */
	public void updateRuleCheck(String nid , String rule_check){
		String sql = " update dw_rule set rule_check = ? where nid = ? ";
		this.getJdbcTemplate().update(sql, new Object[] { rule_check ,nid});
	}
	
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 start
	/**
	 * 根据规则id查询规则
	 * @param id id
	 * @return rulePromote
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Rule getRule(long id) {
		String sql = "select * from dw_rule where id = :id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(Rule.class));
	}
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 end
	
	
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-25 start
	/**
	 * 规则管理
	 */
	@Override
	public List<Rule> getList(String id) {
		String sql = "select * from dw_rule where id = :id ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(Rule.class));
	}
	@Override
	public int getListCount(SearchParam param) {
		String sql = "select count(1) from dw_rule where 1=1 ";
		sql += param.getSearchParamSql();
		try {
			return namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public List<Rule> getList(int start, int pernum, SearchParam param){
		String sql = "select * from dw_rule where 1=1 ";
		sql += param.getSearchParamSql();
		sql += " order by id desc limit :start,:pernum";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("pernum", pernum);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(Rule.class));
	}

	@Override
	public Rule getRuleById(String rid) {
		String sql = "select * from dw_rule where id = :id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", rid);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(Rule.class));
	}

	@Override
	public void updateRule(Rule rule) {
		String sql="update dw_rule set name=?,status=?,addtime=?,nid=?,remark=?,rule_check=? where id=?";
		getJdbcTemplate().update(sql,new Object[]{rule.getName(),rule.getStatus(),rule.getAddtime(),rule.getNid(),rule.getRemark(),rule.getRule_check(),rule.getId()});
		
	}

	@Override
	public void addRule(Rule rule) {
		String sql="insert into dw_rule(name,status,addtime,nid,remark,rule_check) values(?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, rule.getName(),rule.getStatus(),rule.getAddtime(),rule.getNid(),rule.getRemark(),rule.getRule_check());
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-25 end
	
}
