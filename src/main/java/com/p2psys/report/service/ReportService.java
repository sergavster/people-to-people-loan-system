package com.p2psys.report.service;

import com.p2psys.model.PageDataList;
import com.p2psys.report.domain.Report;

/**
 * 导出报表table Dao接口
 
 * @version 1.0
 * @since 2013-11-5
 */
public interface ReportService {

	/**
	 * 导出报表table分页
	 * @param page分页开始页
	 * @param report
	 * @return
	 */
	public PageDataList getReportPage(int page,Report report);
	
	/**
	 * 根据主键id查询信息
	 * @param id
	 * @return
	 */
	public Report getReportById(long id);
	
	/**
	 * 根据nid查询信息
	 * @param nid
	 * @return
	 */
	public Report getReportByNid(String nid , Byte status);
	
	/**
	 * 添加信息
	 * @param report 需要处理的参数
	 * @return 返回主键ID
	 */
	public long inserReport(Report report);
	
	/**
	 * 根据id修改信息
	 * @param report
	 * @return
	 */
	public void editReportById(Report report);
	
	/**
	 * 根据主键id删除信息
	 * @param id
	 * @return
	 */
	public void deleteById(long id);
	
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 start
	/**
	 * 根据注册时间统计人数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Integer registerCount(long startDate,long endDate);
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 end
}
