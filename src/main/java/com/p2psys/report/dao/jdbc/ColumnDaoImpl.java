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

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.report.dao.ColumnDao;
import com.p2psys.report.domain.Column;
import com.p2psys.report.model.ColumnModel;

/**
 * 导出报表列名表DAO接口实现类
 
 * @version 1.0
 * @since 2013-11-5
 */
public class ColumnDaoImpl extends BaseDaoImpl implements ColumnDao {

	private static Logger logger = Logger.getLogger(ColumnDaoImpl.class);
	
	/**
	 * 导出报表table分页数
	 * @param column
	 * @return
	 */
	public int getColumnCount(Column column){
		StringBuffer sql = new StringBuffer("select count(id) from dw_column p1 where 1 = 1");
		Map<String , Object> map = new HashMap<String, Object>();
		if(column.getId() > 0){
			sql.append(" and p1.id = :id");
			map.put("id", column.getId());
		}
		if(column.getChina_name() != null && column.getChina_name().length() > 0){
			sql.append(" and p1.china_name like concat('%',:china_name,'%')");
			map.put("china_name", column.getChina_name());
		}
		if(column.getColumn_name() != null && column.getColumn_name().length() > 0){
			sql.append(" and p1.column_name like concat('%',:column_name,'%')");
			map.put("column_name", column.getColumn_name());
		}
		if(column.getStatus() >= 0){
			sql.append(" and p1.status = :status");
			map.put("status", column.getStatus());
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
	 * @param column
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public List<Column> getColumnPage(int page, int max,Column column){
		StringBuffer sql = new StringBuffer("select id, china_name, column_name, status, addtime, update_time, operator from dw_column p1 where 1 = 1");
		Map<String , Object> map = new HashMap<String, Object>();
		if(column.getId() > 0){
			sql.append(" and p1.id = :id");
			map.put("id", column.getId());
		}
		if(column.getChina_name() != null && column.getChina_name().length() > 0){
			sql.append(" and p1.china_name like concat('%',:china_name,'%')");
			map.put("china_name", column.getChina_name());
		}
		if(column.getColumn_name() != null && column.getColumn_name().length() > 0){
			sql.append(" and p1.column_name like concat('%',:column_name,'%')");
			map.put("column_name", column.getColumn_name());
		}
		if(column.getStatus() >= 0){
			sql.append(" and p1.status = :status");
			map.put("status", column.getStatus());
		}
		sql.append(" order by p1.update_time desc limit :page,:max");
		map.put("page", page);
		map.put("max", max);
		try {
			return getNamedParameterJdbcTemplate().query(sql.toString(), map,getBeanMapper(Column.class));
		} catch (DataAccessException e) {
			logger.error("查询错误。");
		}
		return null;
	}
	
	/**
	 * 根据条件查询信息
	 * @param column 查询参数
	 * @return 返回model List
	 */
	@SuppressWarnings("unchecked")
	public List<ColumnModel> getColumnModelList(ColumnModel column){
		StringBuffer sql = new StringBuffer("select p1.id, ifnull(p2.china_name,p1.china_name) as china_name, p1.column_name, p1.status, p1.addtime, p1.update_time, p1.operator,");
		sql.append(" p2.id as report_column_id, p2.status as report_column_status, p2.ordering, p3.id as report_id,");
		sql.append(" p3.name,p3.report_name, p3.nid as report_nid, p3.status as report_status from dw_column p1");
		sql.append(" left join dw_report_column p2 on p1.id = p2.column_id");
		sql.append(" left join dw_report p3 on p2.report_id = p3.id where 1 =1");
		Map<String , Object> map = new HashMap<String, Object>();
		if(column.getId() > 0){
			sql.append(" and p1.id = :id");
			map.put("id", column.getId());
		}
		if(column.getChina_name() != null && column.getChina_name().length() > 0){
			sql.append(" and p1.china_name like concat('%',:china_name,'%')");
			map.put("china_name", column.getChina_name());
		}
		if(column.getColumn_name() != null && column.getColumn_name().length() > 0){
			sql.append(" and p1.column_name like concat('%',:column_name,'%')");
			map.put("column_name", column.getColumn_name());
		}
		if(column.getStatus() >= 0){
			sql.append(" and p1.status = :status");
			map.put("status", column.getStatus());
		}
		if(column.getReport_column_status() >= 0){
			sql.append(" and p2.status = :report_column_status");
			map.put("report_column_status", column.getReport_column_status());
		}
		if(column.getReport_id() > 0){
			sql.append(" and p3.id = :report_id");
			map.put("report_id", column.getReport_id());
		}
		if(column.getName() != null && column.getName().length() > 0){
			sql.append(" and p3.name like concat('%',:name,'%')");
			map.put("name", column.getName());
		}
		if(column.getReport_name() != null && column.getReport_name().length() > 0){
			sql.append(" and p3.report_name like concat('%',:report_name,'%')");
			map.put("report_name", column.getReport_name());
		}
		if(column.getReport_nid() != null && column.getReport_nid().length() > 0){
			sql.append(" and p3.nid like concat('%',:report_nid,'%')");
			map.put("report_nid", column.getReport_nid());
		}
		if(column.getReport_status() >= 0){
			sql.append(" and p3.status = :report_status");
			map.put("report_status", column.getReport_status());
		}
		sql.append(" order by p2.ordering asc");
		try {
			return getNamedParameterJdbcTemplate().query(sql.toString(), map,getBeanMapper(ColumnModel.class));
		} catch (DataAccessException e) {
			logger.error("查询错误。");
		}
		return null;
	}
	
	/**
	 * 根据条件查询信息
	 * @param column 查询参数
	 * @return 返回实体List
	 */
	@SuppressWarnings("unchecked")
	public List<Column> getColumnList(Column column){
		StringBuffer sql = new StringBuffer("select id, china_name, column_name, status, addtime, update_time, operator from dw_column p1 where 1 = 1");
		Map<String , Object> map = new HashMap<String, Object>();
		if(column.getId() > 0){
			sql.append(" and p1.id = :id");
			map.put("id", column.getId());
		}
		if(column.getChina_name() != null && column.getChina_name().length() > 0){
			sql.append(" and p1.china_name like concat('%',:china_name,'%')");
			map.put("china_name", column.getChina_name());
		}
		if(column.getColumn_name() != null && column.getColumn_name().length() > 0){
			sql.append(" and p1.column_name like concat('%',:column_name,'%')");
			map.put("column_name", column.getColumn_name());
		}
		if(column.getStatus() >= 0){
			sql.append(" and p1.status = :status");
			map.put("status", column.getStatus());
		}
		sql.append(" order by p1.update_time desc");
		try {
			return getNamedParameterJdbcTemplate().query(sql.toString(), map,getBeanMapper(Column.class));
		} catch (DataAccessException e) {
			logger.error("查询错误。");
		}
		return null;
	}

	/**
	 * 根据条件查询单条列名信息
	 * @param column
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Column getColumnById(long id){
		StringBuffer sql = new StringBuffer("select id, china_name, column_name, status, addtime, update_time, operator from dw_column where id = :id");
		try {
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("id", id);
			return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, getBeanMapper(Column.class));
		} catch (Exception e) {
			logger.error("查询错误。");
		}
		return null;
	}
	
	/**
	 * 添加信息
	 * @param column 需要处理的参数
	 * @return 返回主键ID
	 */
	public long insertColumn(Column column){
		StringBuffer sql = new StringBuffer("insert into dw_column (china_name, column_name, status, addtime, update_time, operator) value ");
		sql.append("( :china_name, :column_name, :status, :addtime, :update_time, :operator)");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(column);
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
	public void editColumnById(Column column){
		StringBuffer sql = new StringBuffer("update dw_column set china_name = :china_name , column_name = :column_name , status = :status , ");
		sql.append(" addtime = :addtime , update_time = :update_time , operator = :operator where id = :id");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(column);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

	/**
	 * 删除列表
	 * @param id
	 */
	public void deleteById(long id){
		StringBuffer sql = new StringBuffer("delete from  dw_column where id = :id");
		try {
			SqlParameterSource ps = new BeanPropertySqlParameterSource(id);
			this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
		} catch (Exception e) {
			logger.error("删除信息错误。");
		} 
	}
	
}
