package com.p2psys.report.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.p2psys.model.PageDataList;
import com.p2psys.report.dao.ReportTenderDao;
import com.p2psys.report.model.MonthTenderModel;
import com.p2psys.report.service.ReportTenderService;
import com.p2psys.tool.Page;

/**
 * 标相关的统计导出
 
 * @version 1.0
 * @since 2013-11-12
 */
@Service
public class ReportTenderServiceImpl implements ReportTenderService {

	@Resource
	private ReportTenderDao reportTenderDao;
	
	/**
	 * 每月用户投标数据统计
	 * @param page 分页起始页
	 * @param map 查询参数
	 * @return
	 */
	public PageDataList getMonthTenderPage(int page,Map<String , Object> map){
		if(map == null) map = new HashMap<String, Object>();
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(reportTenderDao.getMonthTenderCount(map) , page );
		pageDataList.setList(reportTenderDao.getMonthTenderPage(pages.getStart(), pages.getPernum() , map));
		pageDataList.setPage(pages);
		return pageDataList;
	}
	
	/**
	 * 每月用户投标数据统计查询
	 * @param map 查询参数
	 * @return
	 */
	public List<MonthTenderModel> getMonthTenderList(Map<String , Object> map){
		int countNo = reportTenderDao.getMonthTenderCount(map);
		return reportTenderDao.getMonthTenderPage(0, countNo , map);
	}
	
	
	
	
}
