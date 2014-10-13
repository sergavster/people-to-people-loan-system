package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.NoticeConfigDao;
import com.p2psys.domain.NoticeConfig;

public class NoticeConfigDaoImpl extends BaseDaoImpl implements  NoticeConfigDao {
	private static Logger logger = Logger.getLogger(NoticeConfigDaoImpl.class);

	RowMapper mapper=new RowMapper(){
		@Override
		public Object mapRow(ResultSet rs, int num) throws SQLException {
			NoticeConfig c=new NoticeConfig();
			c.setId(rs.getLong("id"));
			c.setType(rs.getString("type"));
			c.setEmail(rs.getLong("email"));
			c.setLetters(rs.getLong("letters"));
			c.setSms(rs.getLong("sms"));
			return c;
		}
	};
	
	@Override
	public List getList() {
		String sql="select * from dw_notice_config";
		List list=new ArrayList();
		list=getJdbcTemplate().query(sql, mapper);
		return list;
	}
	
	@Override
	public int getListCount() {
		int total=0;
		String sql="select count(1) from dw_notice_config";
		logger.debug("SQL:" + sql);
		try {
			total = getJdbcTemplate().queryForInt(sql, new Object[] { });
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}
	@Override
	public void add(NoticeConfig n){
		String sql="insert into dw_notice_config(type,sms,email,letters) " +
				"values(?,?,?,?)";
		this.getJdbcTemplate().update(sql,n.getType(),n.getSms(),n.getEmail(),n.getLetters());
	}

}
