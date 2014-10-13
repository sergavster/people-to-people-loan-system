package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.SmstypeDao;
import com.p2psys.domain.Smstype;
import com.p2psys.model.SearchParam;

public class SmstypeDaoImpl extends BaseDaoImpl implements SmstypeDao {
	private static Logger logger = Logger.getLogger(SmstypeDaoImpl.class);

	RowMapper mapper = new RowMapper() {
		@Override
		public Object mapRow(ResultSet rs, int num) throws SQLException {
			Smstype c = new Smstype();
			c.setId(rs.getLong("id"));
			c.setNid(rs.getString("nid"));
			c.setName(rs.getString("name"));
			c.setType(rs.getByte("type"));
			c.setCanswitch(rs.getByte("canswitch"));
			c.setTemplet(rs.getString("templet"));
			c.setRemark(rs.getString("remark"));
			c.setAddtime(rs.getLong("addtime"));
			c.setAddip(rs.getString("addip"));
			c.setUpdatetime(rs.getLong("updatetime"));
			c.setUpdateip(rs.getString("updateip"));
			c.setSend(rs.getByte("send"));
			return c;
		}
	};
	
	//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 start
	@Override
	public List getList(int start, int pernum, SearchParam param) {
		String sql = "select * from dw_sms_type limit ?, ? ";
		List list = new ArrayList();
		list = getJdbcTemplate().query(sql, new Object[]{start,pernum}, mapper);
		return list;
	}	
	//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 end

	@Override
	public int getListCount() {
		int total = 0;
		String sql = "select count(1) from dw_sms_type";
		logger.debug("SQL:" + sql);
		try {
			total = getJdbcTemplate().queryForInt(sql, new Object[] {});
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}

	@Override
	public void add(Smstype n) {
		String sql = "insert into dw_sms_type(nid,name,type,canswitch,templet,remark,addtime,addtip,send) "
				+ "values(?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, n.getNid(), n.getName(),
				n.getType(), n.getCanswitch(), n.getTemplet(), n.getRemark(),
				n.getAddtime(), n.getAddip(), n.getSend());
	}

	@Override
	public void update(Smstype n) {
		String sql = "update dw_sms_type set name=? "
				+ ", type=?, templet=?, remark=? "
				+ ", updatetime=?, updateip=?, send=? where nid=?";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, n.getName(), n.getType(),
				n.getTemplet(), n.getRemark(), n.getUpdatetime(),
				n.getUpdateip(), n.getSend(), n.getNid());
	}

	@Override
	public List getAllSendSmstype() {
		//V1.6.6.1 RDPROJECT-245 liukun 2013-10-09 start
		//String sql = "select * from dw_sms_type where send = 1 and type = 2";
		//系统短信用户也可以配置为接收或者不接收，所以传回前台的配置类型不能只是用户类型短信了，要全部发送短信类型，但验证类的短信比较特殊，不能配置，所以只有可canswitch为1才能配置
		String sql = "select * from dw_sms_type where send = 1 and canswitch  = 1";
		//V1.6.6.1 RDPROJECT-245 liukun 2013-10-09 end
		List list = new ArrayList();
		list = getJdbcTemplate().query(sql, mapper);
		return list;
	}

	@Override
	public Smstype getSmsTypeByNid(String nid) {
		String sql = "select * from dw_sms_type where nid = ? ";
		Smstype smstype = null;
		try {
			smstype = getJdbcTemplate().queryForObject(sql,  new Object[]{nid}, mapper);
		} catch (DataAccessException e) {
			logger.debug("getSmsTypeByNid():"+e.getMessage());
		}
		return smstype;
	}

	@Override
	public List getList() {
		String sql = "select * from dw_sms_type  ";
		List list = new ArrayList();
		list = getJdbcTemplate().query(sql, mapper);
		return list;
	}	

}
