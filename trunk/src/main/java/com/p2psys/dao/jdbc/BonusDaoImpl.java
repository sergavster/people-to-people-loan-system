package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.BonusDao;
import com.p2psys.domain.BonusApr;
import com.p2psys.domain.Borrow;
import com.p2psys.util.NumberUtils;

public class BonusDaoImpl extends BaseDaoImpl implements BonusDao {

	@Override
	public void addBonusApr(Borrow borrow) {
		String sql="insert into dw_bonus_apr(borrow_id,`order`,apr) values(?,?,?)";
		int order=NumberUtils.getInt(borrow.getTime_limit());
		List argsList=new ArrayList();
		for(int i=0;i<order;i++){
			Object[] arg=new Object[]{borrow.getId(),i,0.0};
			argsList.add(arg);
		}
		getJdbcTemplate().batchUpdate(sql, argsList);
	}

	@Override
	public void modifyBonusAprById(long id, double apr) {
		String sql="update dw_bonus_apr set apr=? where id=?";
		getJdbcTemplate().update(sql, apr,id);
	}
	
	RowMapper<BonusApr> mapper=new RowMapper(){
		@Override
		public Object mapRow(ResultSet rs, int num) throws SQLException {
			BonusApr ba=new BonusApr();
			ba.setId(rs.getLong("id"));
			ba.setBorrow_id(rs.getLong("borrow_id"));
			ba.setOrder(rs.getInt("order"));
			ba.setApr(rs.getDouble("apr"));
			return ba;
		}
	};
	
	@Override
	public List getBonusAprList(long borrow_id){
		String sql="select * from dw_bonus_apr where borrow_id=?";
		List list=new ArrayList();
		list=getJdbcTemplate().query(sql, new Object[]{borrow_id}, mapper);
		return list;
	}

	
	@Override
	public BonusApr getBonus(long id) {
		String sql="select * from dw_bonus_apr where id=?";
		BonusApr ba=new BonusApr();
		ba=getJdbcTemplate().queryForObject(sql, new Object[]{id}, mapper);
		return ba;
	}
	
	
	
}
