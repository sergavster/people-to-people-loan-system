package com.p2psys.report.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.report.dao.ReportDao;
import com.p2psys.report.domain.Report;

/**
 * 导出报表table Dao接口实现类
 
 * @version 1.0
 * @since 2013-11-5
 */
public class ReportDaoImpl extends BaseDaoImpl implements ReportDao {

	private static Logger logger = Logger.getLogger(ReportDaoImpl.class);
	
	/**
	 * 导出报表table分页数
	 * @param report
	 * @return
	 */
	public int getReportCount(Report report){
		
		StringBuffer sql = new StringBuffer("select count(id) from dw_report where 1 = 1");
		Map<String , Object> map = new HashMap<String, Object>();
		if(report.getName() != null && report.getName().length() > 0){
			sql.append(" and name like concat('%',:name,'%')");
			map.put("name", report.getName());
		}
		if(report.getReport_name() != null && report.getReport_name().length() > 0){
			sql.append(" and report_name like concat('%',:report_name,'%')");
			map.put("name", report.getReport_name());
		}
		if(report.getNid() != null && report.getNid().length() > 0){
			sql.append(" and nid like concat('%',:nid,'%')");
			map.put("name", report.getNid());
		}
		if(report.getStatus() >= 0){
			sql.append(" and status = :status");
			map.put("status", report.getStatus());
		}
		try {
			return getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
		} catch (DataAccessException e) {
			logger.error("查询错误。");
		}
		return 0;
	}
	
	/**
	 * 导出报表table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param report
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Report> getReportPage(int page, int max,Report report){
		
		StringBuffer sql = new StringBuffer("select id, name, status, report_name, nid, type, url, report_url, remark, addtime, update_time, operator from dw_report where 1 = 1");
		Map<String , Object> map = new HashMap<String, Object>();
		if(report.getName() != null && report.getName().length() > 0){
			sql.append(" and name like concat('%',:name,'%')");
			map.put("name", report.getName());
		}
		if(report.getReport_name() != null && report.getReport_name().length() > 0){
			sql.append(" and report_name like concat('%',:report_name,'%')");
			map.put("name", report.getReport_name());
		}
		if(report.getNid() != null && report.getNid().length() > 0){
			sql.append(" and nid like concat('%',:nid,'%')");
			map.put("name", report.getNid());
		}
		if(report.getStatus() >= 0){
			sql.append(" and status = :status");
			map.put("status", report.getStatus());
		}
		sql.append(" order by update_time desc limit :page,:max");
		map.put("page", page);
		map.put("max", max);
		try {
			return getNamedParameterJdbcTemplate().query(sql.toString(), map,getBeanMapper(Report.class));
		} catch (DataAccessException e) {
			logger.error("查询错误。");
		}
		return null;
	}
	
	/**
	 * 根据主键id查询信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Report getReportById(long id){
		StringBuffer sql = new StringBuffer("select id, name, status, report_name, nid, type, url, report_url, remark, addtime, update_time, operator from dw_report where id = :id");
		try {
			SqlParameterSource ps = new BeanPropertySqlParameterSource(id);
			return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), ps, getBeanMapper(Report.class));
		} catch (Exception e) {
			logger.error("查询错误。");
		}
		return null;
	}
	
	/**
	 * 根据nid和状态查询信息
	 * @param nid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Report getReportByNid(String nid, Byte status){
		StringBuffer sql = new StringBuffer("select id, name, status, report_name, nid, type, url, report_url, remark, addtime, update_time, operator from dw_report where 1 =1 ");
		Map<String , Object> map = new HashMap<String, Object>();
		if( nid != null && nid.length() > 0 ){
			map.put("nid", nid);
			sql.append(" and nid = :nid");
		}
		if( status != null && status > 0 ){
			map.put("status", status);
			sql.append(" and status = :status");
		}
		try {
			return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, getBeanMapper(Report.class));
		} catch (Exception e) {
			logger.error("查询错误。");
		}
		return null;
	}
	
	/**
	 * 添加信息
	 * @param report 需要处理的参数
	 * @return 返回主键ID
	 */
	public long inserReport(Report report){
		StringBuffer sql = new StringBuffer("insert into dw_report (name, status, report_name, nid, type, url, report_url, remark, addtime, update_time, operator)");
		sql.append(" value ( :name, :status, :report_name, :nid, :type, :url, :report_url, :remark, :addtime, :update_time, :operator)");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(report);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	/**
	 * 根据id修改信息
	 * @param report
	 * @return
	 */
	public void editReportById(Report report){
		StringBuffer sql = new StringBuffer("update set dw_report name = :name, status = :status, report_name = :report_name, nid = :nid, ");
		sql.append(" type = :type, url = :url, :report_url, remark = :remark, update_time = :update_time , operator = :operator where id = :id ");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(report);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}
	
	/**
	 * 根据主键id删除信息
	 * @param id
	 * @return
	 */
	public void deleteById(long id){
		StringBuffer sql = new StringBuffer("delete from  dw_report where id = :id");
		try {
			SqlParameterSource ps = new BeanPropertySqlParameterSource(id);
			this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
		} catch (Exception e) {
			logger.error("删除信息错误。");
		} 
	}
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 start
	/**
	 * 根据注册时间统计人数
	 */
	public Integer registerCount(long startDate,long endDate){
		String sql="SELECT COUNT(DISTINCT p1.user_id) as total FROM  dw_user p1 left join dw_user_cache p2 on p2.user_id = p1.user_id WHERE 1 = 1 and p1.addtime between '"+startDate+"' and '"+endDate+"'";
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		if(rs.next()){
			return rs.getInt("total");
		}
		return null;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 end
}
