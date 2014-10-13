package com.p2psys.report.service;

import java.util.List;
import java.util.Map;

import com.p2psys.model.PageDataList;
import com.p2psys.report.model.MonthTenderModel;

/**
 * 标相关的统计导出
 
 * @version 1.0
 * @since 2013-11-12
 */
public interface ReportTenderService {

	/**
	 * 每月用户投标数据统计
	 * @param page 分页起始页
	 * @param map 查询参数
	 * @return
	 */
	public PageDataList getMonthTenderPage(int page,Map<String , Object> map);
	
	/**
	 * 每月用户投标数据统计查询
	 * @param map 查询参数
	 * @return
	 */
	public List<MonthTenderModel> getMonthTenderList(Map<String , Object> map);
	
}
