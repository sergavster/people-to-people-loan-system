package com.p2psys.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.p2psys.dao.HongBaoDao;
import com.p2psys.domain.HongBao;

public class HongBaoDaoImpl extends BaseDaoImpl implements HongBaoDao {

	private static Logger logger = Logger.getLogger(BorrowDaoImpl.class);  
	
	@Override
	public HongBao addHongbao(HongBao hongbao) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into dw_hongbao(user_id,hongbao_money,type,remark,addtime,addip) values(?,?,?,?,?,?)";
		final HongBao a=hongbao;
		this.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, a.getUser_id());
				ps.setDouble(2, a.getHongbao_money());
				ps.setString(3, a.getType());
				ps.setString(4, a.getRemark());
				ps.setString(5, a.getAddtime());
				ps.setString(6, a.getAddip());
				return ps;
			}
		}, keyHolder);
		long key=keyHolder.getKey().longValue();
		hongbao.setId(key);
		return hongbao;
	}
}
