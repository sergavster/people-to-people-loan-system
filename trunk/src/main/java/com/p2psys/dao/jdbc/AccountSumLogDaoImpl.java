package com.p2psys.dao.jdbc;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.AccountSumLogDao;
import com.p2psys.domain.AccountSumLog;

public class AccountSumLogDaoImpl extends BaseDaoImpl implements AccountSumLogDao {

	@Override
	public void addAccountSumLog(AccountSumLog accountSumLog) {
		StringBuffer sql = new  StringBuffer("insert into dw_account_sum_log(user_id,to_user_id,before_money,money,after_money,type,addtime,addip,remark) value ( ");
		sql.append(" :user_id,:to_user_id,:before_money,:money,:after_money,:type,:addtime,:addip,:remark )");
	    SqlParameterSource ps = new BeanPropertySqlParameterSource(accountSumLog);
	    this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

}
