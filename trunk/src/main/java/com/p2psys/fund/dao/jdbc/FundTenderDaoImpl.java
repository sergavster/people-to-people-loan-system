package com.p2psys.fund.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.fund.dao.FundTenderDao;
import com.p2psys.fund.domain.FundTender;
import com.p2psys.fund.model.FundTenderModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.StringUtils;

@Repository
public class FundTenderDaoImpl extends BaseDaoImpl implements FundTenderDao {

	@Override
	public int add(FundTender model) {
		return (int)insertHoldKey(model);
	}

	@Override
	public int count(SearchParam param) {
		StringBuffer sql = new StringBuffer(
				"select count(1) from dw_fund_tender as p1,dw_fund p2,dw_user p3 where p1.fund_id = p2.id and p1.user_id = p3.user_id ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getKeywords())) {
				sql.append(" and p2.name like concat('%',:name,'%')"); // '%:name%'
				map.put("name", param.getKeywords());
			}
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p3.username like concat('%',:username,'%')");
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getType()) && !"0".equals(param.getType())) {
				sql.append(" and p2.type = :type");
				map.put("type", Integer.parseInt(param.getType()));
			}
			if (!StringUtils.isBlank(param.getStatus()) && !"99".equals(param.getStatus())) {// 99为全部
				sql.append(" and p1.status = :status");
				map.put("status", Integer.parseInt(param.getStatus()));
			}
		}
		return getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FundTenderModel> list(int start, int pernum, SearchParam param) {
		StringBuffer sql = new StringBuffer(
				"select p1.*,p2.name,p2.type,p3.username,p3.phone from dw_fund_tender as p1,dw_fund p2,dw_user p3 where p1.fund_id = p2.id and p1.user_id = p3.user_id ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getKeywords())) {
				sql.append(" and p2.name like concat('%',:name,'%')"); // '%:name%'
				map.put("name", param.getKeywords());
			}
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p3.username like concat('%',:username,'%')");
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getType()) && !"0".equals(param.getType())) {
				sql.append(" and p2.type = :type");
				map.put("type", Integer.parseInt(param.getType()));
			}
			if (!StringUtils.isBlank(param.getStatus()) && !"99".equals(param.getStatus())) {// 99为全部
				sql.append(" and p1.status = :status");
				map.put("status", Integer.parseInt(param.getStatus()));
			}
		}
		sql.append(" order by p1.id DESC");
		if (pernum > 0) {// 分页查询，若为0，则取所有（导出报表时取所有）
			sql.append(" LIMIT :start,:pernum");
			map.put("start", start);
			map.put("pernum", pernum);
		}
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(FundTenderModel.class));
	}

	@Override
	@SuppressWarnings("unchecked")
	public FundTenderModel get(long id) {
		StringBuffer sql = new StringBuffer(
				"select p1.*,p2.name,p2.type,p3.username,p3.realname,p3.phone from dw_fund_tender as p1,dw_fund p2,dw_user p3 where p1.fund_id = p2.id and p1.user_id = p3.user_id and p1.id = :id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return getNamedParameterJdbcTemplate()
				.queryForObject(sql.toString(), map, getBeanMapper(FundTenderModel.class));
	}

	@Override
	public int modify(FundTender model) {
		StringBuffer sql = new StringBuffer("update dw_fund_tender set status = :status,remark = :remark");
		sql.append(" where id = :id");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(model);
		return this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

}
