package com.p2psys.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.AccountSumDao;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.AccountSumModel;
import com.p2psys.model.SearchParam;

public class AccountSumDaoImpl extends BaseDaoImpl implements AccountSumDao  {

	@Override
	public void editSumByProperty(String type, double value, long user_id) {
		String sql="update dw_account_sum set "+type+"= ifnull("+type+" , 0 )+ :value where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", value);
		map.put("user_id", user_id);
		this.getNamedParameterJdbcTemplate().update(sql,map);
	}

	/**
	 * 添加信息
	 * @param accountSum
	 */
	public void addAccountSum(AccountSum accountSum){
		StringBuffer sql = new  StringBuffer("insert into dw_account_sum(user_id,recharge,cash,interest,interest_fee,award,deduct,used_recharge,used_interest,used_award,huikuan,used_huikuan,borrow_cash,used_borrow_cash ,repay_cash) value ( ");
		sql.append(" :user_id, :recharge, :cash, :interest, :interest_fee, :award, :deduct, :used_recharge, :used_interest, :used_award, :huikuan, :used_huikuan, :borrow_cash, used_borrow_cash, repay_cash)");
	    SqlParameterSource ps = new BeanPropertySqlParameterSource(accountSum);
	    this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}
	
	/**
	 * 根据用户ID查询信息
	 * @param user_id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AccountSum getAccountSum(long user_id){
		String sql = "select * from dw_account_sum where user_id = :user_id";
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql, map , getBeanMapper(AccountSum.class));
	}
	
	/**
	 * 资金合计分页汇总
	 * @param p
	 * @return
	 */
	public int getAccountSumCount(SearchParam p){
		String sql = "select count(*)  from  dw_account_sum as p1 left join dw_user as p2 on p1.user_id=p2.user_id  where 1=1";
		sql += p.getSearchParamSql();
		try {
			return getNamedParameterJdbcTemplate().queryForInt(sql , new BeanPropertySqlParameterSource(Integer.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/** 根据搜索条件,获取资金合计列表
	 * @param page,max,SearchParam
	 * @return list
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccountSumModel> getAccountSum(int page, int max, SearchParam p){
		String sql = "select p1.*,p2.username  from  dw_account_sum as p1 left join dw_user as p2 on p1.user_id=p2.user_id  where 1=1";
		sql += p.getSearchParamSql();
		sql += "  LIMIT :page,:max";
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("max", max);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(AccountSumModel.class));
	} 
	
	/**
	 * 资金合计日志分页汇总
	 * @param p
	 * @return
	 */
	public int getAccountSumLogCount(SearchParam p ,long user_id){
		String sql = "select count(*)  from  dw_account_sum_log where 1=1 and user_id = :user_id ";
		sql += p.getSearchParamSql();
		Map<String , Object> map = new HashMap<String, Object>();
		try {
			map.put("user_id", user_id);
			return getNamedParameterJdbcTemplate().queryForInt(sql ,map);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/** 根据搜索条件,获取资金合计日志列表
	 * @param page,max,SearchParam
	 * @return list
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccountSumLog> getAccountSumLogPage(int page, int max, long user_id , SearchParam p){
		String sql = "select *  from  dw_account_sum_log where user_id = :user_id ";
		sql += p.getSearchParamSql();
		sql += " order by id desc LIMIT :page,:max";
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("max", max);
		map.put("user_id", user_id);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(AccountSumLog.class));
	}
	
	// v1.6.7.1 用户资金统计加导出 zza 2013-11-26 start
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<AccountSumModel> getAccountSum(SearchParam p) {
		String sql = "select p1.*,p2.username from dw_account_sum as p1 "
				+ "left join dw_user as p2 on p1.user_id=p2.user_id  where 1=1";
		sql += p.getSearchParamSql();
		return getJdbcTemplate().query(sql, getBeanMapper(AccountSumModel.class));
	}
	// v1.6.7.1 用户资金统计加导出 zza 2013-11-26 end
	
}
