package com.p2psys.report.dao;

import java.util.List;

import com.p2psys.report.domain.ReportColumn;

/**
 * 报表与列名关系表
 
 * @version 1.0
 * @since 2013-11-6
 */
public interface ReportColumnDao {

	/**
	 * 根据条件查询信息
	 * @param report_id 查询参数
	 * @return 返回实体List
	 */
	public List<ReportColumn> getListByReportId(long report_id);

	/**
	 * 根据column_id查询信息
	 * @param column_id
	 * @return
	 */
	public List<ReportColumn> getListByColumnId(long column_id);
	
	/**
	 * 根据report_id,column_id查询信息
	 * @param report_id
	 * @param column_id
	 * @return
	 */
	public ReportColumn getReportColumn(long report_id ,long column_id);
	
	/**
	 * 添加信息
	 * @param reportColumn 需要处理的参数
	 * @return 返回主键ID
	 */
	public long insertReportColumn(ReportColumn reportColumn);
	
	/**
	 * 根据id修改信息
	 * @param column
	 * @return
	 */
	public void editReportColumn(ReportColumn reportColumn);
	
	/**
	 * 删除列表
	 * @param id
	 */
	public void deleteById(long id);
	
	/**
	 * 删除列表
	 * @param report_id
	 */
	public void deleteByReportId(long report_id);
	
	/**
	 * 删除列表
	 * @param column_id
	 */
	public void deleteByColumnId(long column_id);
	
}
