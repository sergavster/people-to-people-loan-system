package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.BorrowConfigDao;
import com.p2psys.domain.BorrowConfig;

public class BorrowConfigDaoImpl extends BaseDaoImpl implements BorrowConfigDao {
	private static Logger logger = Logger.getLogger(BorrowConfigDaoImpl.class);

	RowMapper mapper=new RowMapper(){
		@Override
		public Object mapRow(ResultSet rs, int num) throws SQLException {
			BorrowConfig c=new BorrowConfig();
			c.setId(rs.getLong("id"));
			c.setName(rs.getString("name"));
			c.setMost_account(rs.getDouble("most_account"));
			c.setLowest_account(rs.getDouble("lowest_account"));
			c.setMost_apr(rs.getDouble("most_apr"));
			c.setLowest_apr(rs.getDouble("lowest_apr"));
			c.setMost_award_apr(rs.getDouble("most_award_apr"));
			c.setLowest_award_apr(rs.getDouble("lowest_award_apr"));
			c.setMost_award_funds(rs.getDouble("most_award_funds"));
			c.setLowest_award_funds(rs.getDouble("lowest_award_funds"));
			c.setIs_trail(rs.getInt("is_trail"));
			c.setIs_review(rs.getInt("is_review"));
			c.setRemark(rs.getString("remark"));
			c.setIdentify(rs.getString("identify"));
			c.setDaymanagefee(rs.getDouble("daymanagefee"));
			c.setManagefee(rs.getDouble("managefee"));
			return c;
		}
	};
	
	@Override
	public List getList() {
		String sql="select * from dw_borrow_config";
		List list=new ArrayList();
		list=getJdbcTemplate().query(sql, mapper);
		return list;
	}
	@Override
	public BorrowConfig getBorrowConfigById(long id) {
		String sql = "select * from dw_borrow_config   where id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + id);
		BorrowConfig b = null;
		try {
			b = this.getJdbcTemplate().queryForObject(sql, new Object[] { id },mapper);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return b;
	}
	@Override
	public void updateBorrowConfig(BorrowConfig borrowConfig){
		String sql="update dw_borrow_config set most_account=?,lowest_account=?,most_apr=?,lowest_apr=?,is_trail=?,is_review=?,managefee=?,daymanagefee=? where id=? ";
		this.getJdbcTemplate().update(sql, borrowConfig.getMost_account(),borrowConfig.getLowest_account(),borrowConfig.getMost_apr(),
				borrowConfig.getLowest_apr(),borrowConfig.getIs_trail(),borrowConfig.getIs_review(),borrowConfig.getManagefee(),borrowConfig.getDaymanagefee(),borrowConfig.getId());
	}
}
