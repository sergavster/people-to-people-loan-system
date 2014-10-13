package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.AwardDao;
import com.p2psys.domain.ObjAward;
import com.p2psys.domain.RuleAward;
import com.p2psys.domain.UserAward;
import com.p2psys.model.SearchParam;
import com.p2psys.util.DateUtils;

public class AwardDaoImpl extends BaseDaoImpl implements AwardDao {
	/** 日志 */
	private static Logger logger = Logger.getLogger(AwardDaoImpl.class);

	@Override
	public RuleAward getRuleAwardById(long ruleId) {
		return (RuleAward) this.findById(RuleAward.class, ruleId);
	}

	@Override
	public long getRuleIdByAwardType(int awardType) {
		String currentTime = DateUtils.dateStr2(new Date());
		String sql = "select t.id from dw_rule_award t where t.award_type=? and ? between t.start_date and t.end_date";
		return this.getJdbcTemplate().queryForLong(sql, new Object[] { awardType, currentTime });
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RuleAward getRuleAwardByAwardType(int awardType) {
		String sql = "select * from dw_rule_award t where t.award_type=? ";
		RuleAward ruleAward = null;
		try {
			ruleAward = this.getJdbcTemplate().queryForObject(sql, new Object[] { awardType },
					getBeanMapper(RuleAward.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return ruleAward;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RuleAward> getRuleAwardList() {
		String sql = "select * from dw_rule_award";
		List<RuleAward> list = new ArrayList<RuleAward>();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(RuleAward.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return list;
	}

	@Override
	public void updateRuleAwardById(RuleAward ruleAward) {
		this.modify(RuleAward.class, ruleAward);
	}

	@Override
	public void updateBestowMoney(long ruleId, double money) {
		// update 抽奖规则表 set 领用金额 = 领用金额 +? where 总金额 > 领用金额 and id=xxx
		String sql = "update dw_rule_award set bestow_money=ifnull(bestow_money,0)+? where total_money>bestow_money and id=?";
		this.getJdbcTemplate().update(sql, money, ruleId);

	}

	@Override
	public void updateTotalMoney(long ruleId, double money) {
		String sql = "update dw_rule_award set total_money=? where id=?";
		this.getJdbcTemplate().update(sql, money, ruleId);

	}

	@Override
	public void addRuleAward(RuleAward ruleAward) {
		this.insert(ruleAward);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ObjAward> getObjectAwardListByRuleId(long ruleId) {
		String sql = "select * from dw_obj_award where rule_id=? order by rate";
		List<ObjAward> list = new ArrayList<ObjAward>();
		try {
			list = this.getJdbcTemplate().query(sql, new Object[] { ruleId }, getBeanMapper(ObjAward.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return list;
	}

	@Override
	public ObjAward getObjectAwardById(long awardId) {
		return (ObjAward) this.findById(ObjAward.class, awardId);
	}

	@Override
	public void updateObjAward(ObjAward data) {
		this.modify(ObjAward.class, data);
	}

	@Override
	public void updateBestow(long ruleId, long awardId) {
		// update 奖品规则表 set 抽中数量 = 抽中数量 +1 where 总数量 > 抽中数量 and 奖品id=xxx
		String sql = "update dw_obj_award set bestow=ifnull(bestow,0)+1 where total>bestow and id=? and rule_id=?";
		this.getJdbcTemplate().update(sql, awardId, ruleId);

	}

	@Override
	public void addObjAward(ObjAward data) {
		this.insert(data);
	}

	@Override
	public List<UserAward> getAwardeeList(long ruleId, int num, boolean isOrderByLevel) {
		String sql = "";
		if (isOrderByLevel) {
			sql = "select * from dw_user_award where status=1 and rule_id=? order by level asc,addtime desc limit ?";
		} else {
			sql = "select * from dw_user_award where status=1 and rule_id=? order by addtime desc limit ?";
		}
		List<UserAward> list = new ArrayList<UserAward>();
		try {
			list = this.getJdbcTemplate().query(sql, new Object[] { ruleId, num }, getBeanMapper(UserAward.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return list;
	}

	@Override
	public int getUserAwardDayCnt(long ruleId, long userId) {
		String sql = "select count(1) from dw_user_award where rule_id=? and user_id=? and addtime>="
				+ DateUtils.getIntegralTime().getTime() / 1000 + " and addtime<="
				+ DateUtils.getLastIntegralTime().getTime() / 1000;
		int count = this.getJdbcTemplate().queryForInt(sql, new Object[] { ruleId, userId });
		return count;
	}

	@Override
	public int getUserAwardTotalCnt(long ruleId, long userId) {
		String sql = "select count(1) from dw_user_award where rule_id=? and user_id=?";
		int count = this.getJdbcTemplate().queryForInt(sql, new Object[] { ruleId, userId });
		return count;
	}

	@Override
	public void addUserAward(UserAward data) {
		this.insert(data);
	}

	@Override
	public List<UserAward> getUserAwardList(int start, int end, SearchParam param) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select * from dw_user_award p1 where 1=1 ");
		sbSql.append(param.getSearchParamSql());
		sbSql.append(" order by p1.addtime desc ");
		sbSql.append("limit ?,?");
		List<UserAward> list = new ArrayList<UserAward>();
		list = getJdbcTemplate().query(sbSql.toString(), new Object[] { start, end }, getBeanMapper(UserAward.class));
		return list;
	}

	public List<UserAward> getAllUserAwardList(SearchParam param) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select * from dw_user_award p1 where 1=1 ");
		sbSql.append(param.getSearchParamSql());
		sbSql.append(" order by p1.addtime desc ");
		List<UserAward> list = new ArrayList<UserAward>();
		list = getJdbcTemplate().query(sbSql.toString(), new Object[] {}, getBeanMapper(UserAward.class));
		return list;
	}

	@Override
	public int getUserAwardCount(SearchParam param) {
		String sql = "select count(1) from dw_user_award p1 where 1=1 ";
		sql = sql + param.getSearchParamSql();
		int count = 0;
		count = this.count(sql, new Object[] {});
		return count;
	}

	@Override
	public double getUserAwardSum(SearchParam param) {
		// v1.6.7.1 抽奖总金额统计加上类型条件（type=0），只统计现金
		String sql = "select sum(p2.obj_value) as num from dw_user_award p1 left join dw_obj_award p2 on p1.award_id=p2.id  where p2.type = 0";
		sql = sql + param.getSearchParamSql();
		double total = 0;
		try {
			total = sum(sql);
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return total;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<UserAward> getMyAwardList(long ruleId, long userId) {
		String sql = "select * from dw_user_award where status=1 and rule_id=? and user_id=? order by addtime desc";
		List<UserAward> list = new ArrayList<UserAward>();
		try {
			list = this.getJdbcTemplate().query(sql, new Object[] { ruleId, userId }, getBeanMapper(UserAward.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return list;
	}

	@Override
	public int getUserAwardTotalCnt(long ruleId, long userId, long status, SearchParam param) {
		String sql = "select count(1) from dw_user_award p1 where p1.rule_id=? and p1.user_id=? and p1.status=?  ";
		sql += param.getSearchParamSql();
		int count = this.getJdbcTemplate().queryForInt(sql, new Object[] { ruleId, userId, status });
		return count;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getUserAwardList(int start, int end, SearchParam param, int ruleId, int status, int userId) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select * from dw_user_award p1 where p1.rule_id=? and p1.user_id=? and p1.status=? ");
		sbSql.append(param.getSearchParamSql());
		sbSql.append(" order by p1.addtime desc ");
		sbSql.append("limit ?,?");
		List list = new ArrayList();
		list = getJdbcTemplate().query(sbSql.toString(), new Object[] { ruleId, userId, status, start, end },
				getBeanMapper(UserAward.class));
		return list;
	}
}
