package com.p2psys.report.web.action.admin;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.p2psys.model.PageDataList;
import com.p2psys.report.model.MonthTenderModel;
import com.p2psys.report.service.ReportTenderService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.EntityUtil;
import com.p2psys.util.NumberUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 标相关的统计导出
 
 * @version 1.0
 * @since 2013-11-12
 */
public class ReportTenderAction extends BaseAction{

	private static final Logger logger=Logger.getLogger(ReportTenderAction.class);
	@Resource
	private ReportTenderService reportTenderService;
	
	/**
	 * 每月用户投标数据统计
	 */
	@SuppressWarnings({ "unchecked"})
	public String monthTenderPage(){
		// 查询参数
		int year_time = paramInt("year_time");
		int month_time =paramInt("month_time");
		String type = paramString("type");
		String username = paramString("username");
		int pageNo = NumberUtils.getInt(request.getParameter("page"));
		// 查询时间处理
		Date monthFirstDay = new Date();
		Date monthLastDay = new Date();
		if(year_time > 0 && month_time > 0){
			monthFirstDay = DateUtils.monthFirstDay(year_time, month_time);
			monthLastDay = DateUtils.monthLastDay(year_time, month_time);
		}else{
			monthFirstDay = DateUtils.currMonthFirstDay(null);
			monthLastDay = DateUtils.currMonthLastDay(null);
		}
		
		//查询参数封装
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("startTime", monthFirstDay.getTime() / 1000);
		map.put("endTime", monthLastDay.getTime() / 1000);
		map.put("type",NumberUtils.getInt(type));
		map.put("username", username);
		if("export".equals(actionType)){//导出报表
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="month_tender_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			String[] names= EntityUtil.findFields(MonthTenderModel.class);
			String[] titles=EntityUtil.getExcelTitle(MonthTenderModel.class);
			List<MonthTenderModel> monthTenderList =  reportTenderService.getMonthTenderList(map);
			try {
				ExcelHelper.writeExcel(infile,monthTenderList, MonthTenderModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("每月用户投标数据统计异常："+e.getMessage());
			}
			return null;
		}else{
			PageDataList page = reportTenderService.getMonthTenderPage(pageNo, map);
			List<MonthTenderModel> list = page.getList();
			request.setAttribute("tenderList", list);
			request.setAttribute("page", page.getPage());
			// 分页参数封装
			Map<String , Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("year_time", DateUtils.getTimeYear(monthFirstDay)+"");
			paramsMap.put("month_time", DateUtils.getTimeMonth(monthFirstDay)+"");
			paramsMap.put("type", type);
			paramsMap.put("username", username);
			request.setAttribute("params", paramsMap);
			return "success";
			
		}
	}
}