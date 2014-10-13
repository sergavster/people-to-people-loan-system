package com.p2psys.report.web.action.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import com.p2psys.model.IdentifySearchParam;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.report.model.ReportNewRegisterModel;
import com.p2psys.report.model.ReportUserModel;
import com.p2psys.report.service.ReportUserService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.EntityUtil;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 用户相关统计action
 
 * @version 1.0
 * @since 2013-11-13
 */
public class ReportUserAction extends BaseAction{

	@Resource
	private ReportUserService reportUserService;
	
	
	public String userStatistics() {
		//获取用户所有类型
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username=StringUtils.isNull(request.getParameter("username"));
	    String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
	    String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		SearchParam param=new IdentifySearchParam();
		param.setUsername(username);
	    param.setDotime1(dotime1);
	    param.setDotime2(dotime2);
		if("export".equals(actionType)){//导出报表
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="user_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			String[] names= EntityUtil.findFields(ReportUserModel.class);
			String[] titles=EntityUtil.getExcelTitle(ReportUserModel.class);
			List<ReportUserModel> list=reportUserService.getUserList(param);
			try {
				ExcelHelper.writeExcel(infile, list, ReportUserModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}else{
			PageDataList pageDataList = reportUserService.getUserList(page,param);
			this.setPageAttribute(pageDataList, param);
			return SUCCESS;
		}
	}

	
	public String monthUserNum(){
		
		// 查询参数
		int year_time = this.paramInt("year_time");
		int month_time = this.paramInt("month_time");
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
		year_time = DateUtils.getTimeYear(monthFirstDay);
		month_time = DateUtils.getTimeMonth(monthFirstDay);
		
		ReportNewRegisterModel model = reportUserService.getNewRegisterUserNum(monthFirstDay.getTime() / 1000,monthLastDay.getTime() / 1000 );
		model.setRecord_time(year_time+"-"+month_time);
		if("export".equals(actionType)){//导出报表
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="RegisterUser_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			String[] names= EntityUtil.findFields(ReportNewRegisterModel.class);
			String[] titles=EntityUtil.getExcelTitle(ReportNewRegisterModel.class);
			try {
				List<ReportNewRegisterModel> list= new ArrayList<ReportNewRegisterModel>();
				list.add(model);
				ExcelHelper.writeExcel(infile, list, ReportNewRegisterModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
		request.setAttribute("year_time", year_time);
		request.setAttribute("month_time", month_time);
		request.setAttribute("item", model);
		return SUCCESS;
	}
	
}
