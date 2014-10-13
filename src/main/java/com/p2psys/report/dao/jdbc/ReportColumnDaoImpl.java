package com.p2psys.report.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.report.dao.ReportColumnDao;
import com.p2psys.report.domain.ReportColumn;

public class ReportColumnDaoImpl extends BaseDaoImpl implements ReportColumnDao {

	private static Logger logger = Logger.getLogger(ReportDaoImpl.class);
	
	/**
	 * 根据条件查询信息
	 * @param report_id 查询参数
	 * @return 返回实体List
	 */
	@SuppressWarnings("unchecked")
	public List<ReportColumn> getListByReportId(long report_id){
		String sql = "select id,report_id,column_id,ordering,addtime,status from dw_report_column where report_id = :report_id";
		try {
			SqlParameterSource ps = new BeanPropertySqlParameterSource(report_id);
			return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), ps, getBeanMapper(ReportColumn.class));
		} catch (Exception e) {
			logger.error("查询错误。");
		}
		return null;
	}

	/**
	 * 根据column_id查询信息
	 * @param column_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReportColumn> getListByColumnId(long column_id){
		String sql = "select id,report_id,column_id,ordering,addtime,status from dw_report_column where column_id = :column_id";
		try {
			SqlParameterSource ps = new BeanPropertySqlParameterSource(column_id);
			return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), ps, getBeanMapper(ReportColumn.class));
		} catch (Exception e) {
			logger.error("查询错误。");
		}
		return null;
	}
	
	/**
	 * 根据report_id,column_id查询信息
	 * @param report_id
	 * @param column_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ReportColumn getReportColumn(long report_id ,long column_id){
		String sql = "select id,report_id,column_id,china_name,ordering,addtime,status from dw_report_column where report_id = :report_id and column_id = :column_id";
		try {
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("report_id", report_id);
			map.put("column_id", column_id);
			return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, getBeanMapper(ReportColumn.class));
		} catch (Exception e) {
			logger.error("查询错误。");
		}
		return null;
	}
	
	/**
	 * 添加信息
	 * @param reportColumn 需要处理的参数
	 * @return 返回主键ID
	 */
	public long insertReportColumn(ReportColumn reportColumn){
		StringBuffer sql = new StringBuffer("insert into dw_report_column (report_id,column_id,china_name,ordering,addtime,status) value ");
		sql.append("( :report_id,:column_id,:ordering,:addtime,:status)");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(reportColumn);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			this.getNamedParameterJdbcTemplate().update(sql.toString(), ps, keyHolder);
			return keyHolder.getKey().intValue();
		} catch (Exception e) {
			logger.error("添加信息错误。");
		} 
		return 0;
	}
	
	/**
	 * 根据id修改信息
	 * @param column
	 * @return
	 */
	public void editReportColumn(ReportColumn reportColumn){
		StringBuffer sql = new StringBuffer("update dw_report_column set report_id = :report_id, column_id = :column_id,");
		sql.append(" china_name = :china_name,ordering = :ordering, addtime = :addtime, status = :status where id = :id");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(reportColumn);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}
	
	/**
	 * 删除列表
	 * @param id
	 */
	public void deleteById(long id){
		StringBuffer sql = new StringBuffer("delte from dw_report_column where id = :id");
		try {
			SqlParameterSource ps = new BeanPropertySqlParameterSource(id);
			this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
		} catch (Exception e) {
			logger.error("删除信息错误。");
		} 
	}
	
	/**
	 * 删除列表
	 * @param report_id
	 */
	public void deleteByReportId(long report_id){
		StringBuffer sql = new StringBuffer("delte from dw_report_column where report_id = :report_id");
		try {
			SqlParameterSource ps = new BeanPropertySqlParameterSource(report_id);
			this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
		} catch (Exception e) {
			logger.error("删除信息错误。");
		} 
	}
	
	/**
	 * 删除列表
	 * @param column_id
	 */
	public void deleteByColumnId(long column_id){
		StringBuffer sql = new StringBuffer("delte from dw_report_column where column_id = :column_id");
		try {
			SqlParameterSource ps = new BeanPropertySqlParameterSource(column_id);
			this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
		} catch (Exception e) {
			logger.error("删除信息错误。");
		} 
	}

}
