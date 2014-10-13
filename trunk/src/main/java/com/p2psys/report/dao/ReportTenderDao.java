package com.p2psys.report.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.report.model.MonthTenderModel;

/**
 * 标相关的统计导出
 
 * @version 1.0
 * @since 2013-11-12
 */
public interface ReportTenderDao {

	/**
	 * 每月用户投标数据统计
	 * @param map
	 * @return
	 */
	public int getMonthTenderCount(Map<String , Object> map);
	
	/**
	 * 每月用户投标数据统计
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	public List<MonthTenderModel> getMonthTenderPage(int page, int max , Map<String , Object> map);
}
