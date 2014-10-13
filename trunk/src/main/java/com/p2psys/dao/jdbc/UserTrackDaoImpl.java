package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.UserTrackDao;
import com.p2psys.domain.UserTrack;

/**
 * 用户信息跟踪表Dao类
 
 * @date 2012-7-5-下午12:52:19
 * @version  
 *
 *  (c)</b> 2012-51-<br/>
 *
 */
public class UserTrackDaoImpl extends BaseDaoImpl implements UserTrackDao {

	private static Logger logger = Logger.getLogger(UserTrackDaoImpl.class);  
	
	public void addUserTrack(UserTrack t) {
		String sql="insert into  dw_usertrack(user_id,login_time,login_ip) " +
				"values (?, ?, ?)";
		logger.debug("SQL:"+sql);
		try {
			getJdbcTemplate().update(sql,
					t.getUser_id(),
					t.getLogin_time(),
					t.getLogin_ip());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	public UserTrack getLastUserTrack(long userid) {
		UserTrack t=null;
		//String sql="select * from dw_usertrack where user_id = ? order by id desc limit 0,1";
		String sql="select * from dw_usertrack where user_id = ? order by id desc limit 1,1";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+userid);
		try {
			t= getJdbcTemplate().queryForObject(sql,
			        new Object[]{userid},
			        new RowMapper<UserTrack>() {
						public UserTrack mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							UserTrack t = new UserTrack();
							t.setUser_id(rs.getString("user_id"));
							t.setLogin_ip(rs.getString("login_ip"));
							t.setLogin_time(rs.getString("login_time"));
							return t;
						}
			        });
		} catch (Exception e) {
			logger.debug("数据库查询结果为空！");
			logger.error(e.getMessage());
		}
		return t;
	}

	@Override
	public int getUserTrackCount(long userid) {
		String sql="select count(1) from dw_usertrack where user_id=?";
		int total=0;
		total=this.count(sql, userid);
		return total;
	}
	
	
	
}
