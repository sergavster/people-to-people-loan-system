package com.p2psys.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.LogTemplateDao;
import com.p2psys.domain.LogTemplate;

public class LogTemplateDaoImpl extends BaseDaoImpl implements LogTemplateDao {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LogTemplate getLogTemplate(byte type, String log_type) {
		String sql = "select * from  dw_log_template where type = :type and log_type = :log_type";
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("log_type", log_type);
		return getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(LogTemplate.class));
	}

	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<LogTemplate> getLogTemplateAll() {
		String sql = "select * from  dw_log_template";
		return getNamedParameterJdbcTemplate().query(sql , new BeanPropertySqlParameterSource(LogTemplate.class),getBeanMapper(LogTemplate.class));
	}

	@Override
	public void editLogTemplate(LogTemplate logTemplate) {
		StringBuffer sql = new  StringBuffer("update dw_log_template set type = :type, log_type = :log_type, value = :value, remark = :remark where id = :id ;");
	    SqlParameterSource ps = new BeanPropertySqlParameterSource(logTemplate);
	    this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

	@Override
	public void insertLogTemplate(LogTemplate logTemplate) {
		StringBuffer sql = new  StringBuffer("insert into dw_log_template (type, log_type, value, remark ) value (:type, :log_type, :value, :remark );");
	    SqlParameterSource ps = new BeanPropertySqlParameterSource(logTemplate);
	    this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

}
