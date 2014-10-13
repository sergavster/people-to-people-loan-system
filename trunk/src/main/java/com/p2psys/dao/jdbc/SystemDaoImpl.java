package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.SystemDao;
import com.p2psys.domain.SystemConfig;

public class SystemDaoImpl extends BaseDaoImpl implements SystemDao {

	@Override
	public List getsystem() {
		String sql = "select * from dw_system where status=1";
		List list = new ArrayList();
		list = this.getJdbcTemplate().query(sql, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				SystemConfig sys = new SystemConfig();
				sys.setId(rs.getLong("id"));
				sys.setName(rs.getString("name"));
				sys.setNid(rs.getString("nid"));
				sys.setStatus(rs.getString("status"));
				sys.setStyle(rs.getInt("style"));
				sys.setType(rs.getInt("type"));
				sys.setValue(rs.getString("value"));
				return sys;
			}
		});
		return list;
	}

	@Override
	public void updateSystemById(List<SystemConfig> list) {
		String sql = "update dw_system set value=? where id=?";
		List argList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			SystemConfig sysconfig = list.get(i);
			if (sysconfig == null) {
				//System.out.println(i);
				continue;
			}
			Object[] args = new Object[] { sysconfig.getValue(), sysconfig.getId() };
			argList.add(args);
		}
		getJdbcTemplate().batchUpdate(sql, argList);
	}

	@Override
	public List getSystemListBySytle(int i) {
		String sql = "select * from dw_system where `status`=?";
		List list = new ArrayList();
		list = this.getJdbcTemplate().query(sql, new Object[] { i }, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				SystemConfig sys = new SystemConfig();
				sys.setId(rs.getLong("id"));
				sys.setName(rs.getString("name"));
				sys.setNid(rs.getString("nid"));
				sys.setStatus(rs.getString("status"));
				sys.setStyle(rs.getInt("style"));
				sys.setType(rs.getInt("type"));
				sys.setValue(rs.getString("value"));
				return sys;
			}
		});
		return list;
	}

	@Override
	public void addSystemConfig(SystemConfig systemConfig) {
		String sql = "insert into dw_system(`name`,nid,`value`,`type`,`style`,`status`) VALUES(?,?,?,?,?,?)";
		getJdbcTemplate().update(
				sql,
				new Object[] { systemConfig.getName(), systemConfig.getNid(), systemConfig.getValue(),
						systemConfig.getType(), systemConfig.getStyle(), systemConfig.getStatus() });
	}

	@Override
	public List getAllowIp() {
		String sql = "select * from dw_admin_allowip";
		List list = new ArrayList();
		list = getJdbcTemplate().query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int num) throws SQLException {
				return rs.getString("allowip");
			}
		});
		return list;
	}

	// v1.6.7.1 RDPROJECT-395 zza 2013-11-06 start
	/**
	 * 根据nid查询
	 * 
	 * @param nid nid
	 * @return SystemConfig
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SystemConfig getByNid(String nid) {
		String sql = "select * from dw_system where nid = :nid";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nid", nid);
		try {
			return this.getNamedParameterJdbcTemplate().queryForObject(sql, map,
					getBeanMapper(SystemConfig.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-06 end
	
	// v1.6.7.1 RDPROJECT-439 zza 2013-11-12 start
	@Override
	public void modifySystemConfig(SystemConfig systemConfig) {
		String sql = "update dw_system set value = :value where nid = :nid";
		SqlParameterSource ps = new BeanPropertySqlParameterSource(systemConfig);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}
	// v1.6.7.1 RDPROJECT-439 zza 2013-11-12 end

}
