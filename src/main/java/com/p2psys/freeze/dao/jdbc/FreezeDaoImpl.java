package com.p2psys.freeze.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.freeze.dao.FreezeDao;
import com.p2psys.freeze.domain.Freeze;
import com.p2psys.freeze.model.FreezeModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.StringUtils;

public class FreezeDaoImpl extends BaseDaoImpl implements FreezeDao {

	@Override
	public int count(SearchParam param) {
		StringBuffer sql = new StringBuffer(
				"select count(1) from dw_freeze p1,dw_user p2 where p1.user_id = p2.user_id and 1 = 1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getKeywords())) {
				sql.append(" and p2.username like concat('%',:username,'%')");
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getStatus()) && !"99".equals(param.getStatus())) {
				sql.append(" and p1.status = :status");
				map.put("status", Integer.parseInt(param.getStatus()));
			}
		}
		return getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public List<FreezeModel> list(int start, int pernum, SearchParam param) {
		StringBuffer sql = new StringBuffer(
				"select p1.*,p2.username from dw_freeze p1,dw_user p2 where p1.user_id = p2.user_id and 1 = 1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p2.username like concat('%',:username,'%')");
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getStatus()) && !"99".equals(param.getStatus())) {
				sql.append(" and p1.status = :status");
				map.put("status", Integer.parseInt(param.getStatus()));
			}
		}
		sql.append(" order by p1.id desc");
		// 分页查询，若为0，则取所有（导出报表时取所有）
		if (pernum > 0) {
			sql.append(" LIMIT :start,:pernum");
			map.put("start", start);
			map.put("pernum", pernum);
		}
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(FreezeModel.class));
	}

	@Override
	public int add(Freeze model) {
		StringBuffer sql = new StringBuffer(
				"insert into dw_freeze(user_id,verify_user,status,mark,remark,add_time,add_ip)");
		sql.append("value (:userId,:verifyUserId,:status,:mark,:remark,:addTime,:addIp)");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(model);
		return this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

	@Override
	@SuppressWarnings({ "unchecked"})
	public FreezeModel get(long id) {
		StringBuffer sql = new StringBuffer(
				"select p1.*,p2.username from dw_freeze p1,dw_user p2 where p1.user_id = p2.user_id and p1.id = :id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		try {
			return getNamedParameterJdbcTemplate()
					.queryForObject(sql.toString(), map, getBeanMapper(FreezeModel.class));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@SuppressWarnings({ "unchecked"})
	public FreezeModel getByUserId(long userId) {
		StringBuffer sql = new StringBuffer(
				"select p1.user_id,p1.status,p1.mark from dw_freeze p1 where p1.user_id = :userId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		try {
			return getNamedParameterJdbcTemplate()
					.queryForObject(sql.toString(), map, getBeanMapper(FreezeModel.class));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@SuppressWarnings({ "unchecked"})
	public FreezeModel getByUserName(String username) {
		StringBuffer sql = new StringBuffer(
				"select p1.user_id,p1.status,p1.mark from dw_freeze p1,dw_user p2 where p1.user_id = p2.user_id and p2.username = :username");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		try {
			return getNamedParameterJdbcTemplate()
					.queryForObject(sql.toString(), map, getBeanMapper(FreezeModel.class));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean isExistsByUserName(String username) {
		StringBuffer sql = new StringBuffer(
				"select count(1) from dw_freeze p1 left join dw_user p2 on p1.user_id = p2.user_id where p2.username = :username");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		int r = getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
		return r == 0 ? false : true;
	}

	@Override
	public int modify(Freeze model) {
		StringBuffer sql = new StringBuffer("update dw_freeze set status = :status,mark = :mark,remark = :remark");
		sql.append(" where id = :id");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(model);
		return this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

	@Override
	public int modify(String column, Object value, long id) {
		StringBuffer sql = new StringBuffer("update dw_freeze set ");
		sql.append(column);
		sql.append(" = :");
		sql.append(column);
		sql.append(" where id = :id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(column, value);
		map.put("id", id);
		return this.getNamedParameterJdbcTemplate().update(sql.toString(), map);
	}

}
