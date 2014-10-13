package com.p2psys.dao.jdbc;

import java.util.List;
import java.util.Map;

import com.p2psys.dao.LateBorrowLogDao;

public class LateBorrowLogDaoImpl extends BaseDaoImpl implements LateBorrowLogDao {

	@Override
	public boolean addLateBorrowLogDetail(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into dw_late_borrow_log (borrow_id, phone_type, phone_num, phone_status, relation_type, relation_name, repay_time, memo, ts)  ");
		sql.append("                         VALUES (?,         ?,          ?,         ?,             ?,             ?,            ?,           ?,   now()) ");
		Object[] args = new Object[]{params.get("borrow_id"), params.get("phone_type"), params.get("phone_num"),params.get("phone_status")
				                    ,params.get("relation_type"),params.get("relation_name"),params.get("repay_time"),params.get("memo")};
		int a  = this.getJdbcTemplate().update(sql.toString(), args);
		return a !=0;
	}

	@Override
	public List<Map<String, Object>> queryLateBorrowDetails(String borrow_id) {
		String sql = " SELECT id , phone_type, phone_num, phone_status, relation_type, relation_name, repay_time, memo,ts  FROM dw_late_borrow_log WHERE borrow_id = ? order by id desc ";
		List<Map<String,Object>> list = this.getJdbcTemplate().queryForList(sql, borrow_id);
		return list;
	}

}
