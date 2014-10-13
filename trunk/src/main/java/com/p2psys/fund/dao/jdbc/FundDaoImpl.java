package com.p2psys.fund.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.fund.dao.FundDao;
import com.p2psys.fund.domain.Fund;
import com.p2psys.fund.model.FundModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;

@Repository
public class FundDaoImpl extends BaseDaoImpl implements FundDao{
	
	/**
	 * 根据SearchParam的order 获得ordersql
	 * 
	 * @param order
	 * @return
	 */
	private String getOrderSql(int order) {
		String sql = "";
		switch (order) {
			case 0:
				sql = " order by p1.id DESC";
				break;
			case 1:
				sql = " order by p1.account+0 asc";
				break;
			case -1:
				sql = " order by p1.account+0 desc";
				break;
			case 2:
				sql = " order by p1.apr asc";
				break;
			case -2:
				sql = " order by p1.apr desc";
				break;
			case 3:
				sql = " order by p1.account_yes/p1.account asc";
				break;
			case -3:
				sql = " order by p1.account_yes/p1.account desc";
				break;
			default:
				sql = "";
		}
		return sql;
	}

	@Override
	public int count(SearchParam param) {
		StringBuffer sql = new StringBuffer("select count(1) from dw_fund as p1 where 1 = 1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getKeywords())) {
				sql.append(" and p1.name like concat('%',:name,'%')"); // '%:name%'
				map.put("name", param.getKeywords());
			}
			if (!StringUtils.isBlank(param.getDotime1())) {
				sql.append(" and p1.add_time >= :dotime1");
				map.put("dotime1", DateUtils.valueOf(param.getDotime1()).getTime() / 1000);
			}
			if (!StringUtils.isBlank(param.getDotime2())) {
				sql.append(" and p1.add_time <= :dotime2");
				map.put("dotime2", DateUtils.valueOf(param.getDotime2()).getTime() / 1000);
			}
			if (!StringUtils.isBlank(param.getType()) && !"0".equals(param.getType())) {
				sql.append(" and p1.type = :type");
				map.put("type", Integer.parseInt(param.getType()));
			}
			if (!StringUtils.isBlank(param.getStatus()) && !"0".equals(param.getStatus())) {
				sql.append(" and p1.status = :status");
				map.put("status", Integer.parseInt(param.getStatus()));
			}
		}
		return getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FundModel> list(int start, int pernum, SearchParam param) {
		StringBuffer sql = new StringBuffer(
				"select p1.*,p2.username from dw_fund p1,dw_user p2 where p1.user_id = p2.user_id and 1 = 1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getKeywords())) {
				sql.append(" and p1.name like concat('%',:name,'%')");
				map.put("name", param.getKeywords());
			}
			if (!StringUtils.isBlank(param.getDotime1())) {
				sql.append(" and p1.add_time >= :dotime1");
				map.put("dotime1", DateUtils.valueOf(param.getDotime1()).getTime() / 1000);
			}
			if (!StringUtils.isBlank(param.getDotime2())) {
				sql.append(" and p1.add_time <= :dotime2");
				map.put("dotime2", DateUtils.valueOf(param.getDotime2()).getTime() / 1000);
			}
			if (!StringUtils.isBlank(param.getType()) && !"0".equals(param.getType())) {
				sql.append(" and p1.type = :type");
				map.put("type", Integer.parseInt(param.getType()));
			}
			if (!StringUtils.isBlank(param.getStatus()) && !"0".equals(param.getStatus())) {
				sql.append(" and p1.status = :status");
				map.put("status", Integer.parseInt(param.getStatus()));
			}
			sql.append(getOrderSql(param.getOrder()));
		}
		if (pernum > 0) {// 分页查询，若为0，则取所有（导出报表时取所有）
			sql.append(" LIMIT :start,:pernum");
			map.put("start", start);
			map.put("pernum", pernum);
		}
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(FundModel.class));
	}

	@Override
	public FundModel get(long id) {
		return (FundModel)findById(FundModel.class, id);
	}

	@Override
	public int add(Fund model) {
		/*StringBuffer sql = new StringBuffer(
				"insert into dw_fund(user_id,type,status,name,pic,account,apr,lowest_account,most_account,is_day,time_limit,valid_time,style,issuer,guaranty,counter_guaranty,phone,content,add_time,add_ip)");
		sql.append("value (:userId,:type,:status,:name,:pic,:account,:apr,:lowestAccount,:mostAccount,:isDay,:timeLimit,:validTime,:style,:issuer,:guaranty,:counterGuaranty,:phone,:content,:addTime,:addIp)");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(model);*/
		return (int)insertHoldKey(model);
	}

	@Override
	public int modify(Fund model) {
		StringBuffer sql = new StringBuffer(
				"update dw_fund set user_id = :userId,type = :type,status = :status,name = :name,pic = :pic,account = :account,apr = :apr,lowest_account = :lowestAccount,most_account = :mostAccount,is_day = :isDay,time_limit = :timeLimit,valid_time = :validTime,style = :style,issuer = :issuer,guaranty = :guaranty,counter_guaranty = :counterGuaranty,phone = :phone,content = :content");
		sql.append(" where id = :id");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(model);
		return this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

	@Override
	public int modify(String column, Object value, long id) {
		StringBuffer sql = new StringBuffer("update dw_fund set ");
		sql.append(column);
		sql.append(" = :");
		sql.append(column);
		sql.append(" where id = :id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(column, value);
		map.put("id", id);
		return this.getNamedParameterJdbcTemplate().update(sql.toString(), map);
	}

	@Override
	public int updateTener(long id, double account) {
		StringBuffer sql = new StringBuffer(
				"update dw_fund set tender_time = tender_time+1,account_yes = (account_yes + :account)");
		sql.append(" where (account_yes + :account) <= account and id = :id and status=1");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account", account);
		map.put("id", id);
		return this.getNamedParameterJdbcTemplate().update(sql.toString(), map);
	}

}
