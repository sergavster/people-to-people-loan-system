package com.p2psys.report.web.action.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.p2psys.model.PageDataList;
import com.p2psys.report.domain.Column;
import com.p2psys.report.domain.Report;
import com.p2psys.report.model.ReportModel;
import com.p2psys.report.service.ColumnService;
import com.p2psys.report.service.ReportColumnService;
import com.p2psys.report.service.ReportService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 报表基础数据action
 
 * @version 1.0
 * @since 2013-11-6
 */
public class ReportAction extends BaseAction {

	private ReportService reportService;
	
	private ColumnService columnService;
	
	private ReportColumnService reportColumnService;
	
	private Column column;
	
	private ReportModel reportModel;
	
	/**
	 * 报表基础数据管理分页查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String column(){
		Column column = new Column();
		int pageNo = NumberUtils.getInt(request.getParameter("page"));
		String china_name = request.getParameter("china_name");
		column.setChina_name(china_name);
		String column_name = request.getParameter("column_name");
		column.setColumn_name(column_name);
		String status = request.getParameter("status");
		if(status != null && status.length() > 0){
			column.setStatus(Byte.parseByte(status));
		}else column.setStatus((byte)-1);
		
		PageDataList page = columnService.getColumnPage(pageNo, column);
		//SearchParam param = new SearchParam();
		//查询所有的积分；类型
		List<Report> list = page.getList();
		request.setAttribute("columnList", list);
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("column", column);
		request.setAttribute("page", page.getPage());
		request.setAttribute("params", map);
		return "success";
	}
	
	/**
	 * 添加列名基础数据
	 * @return
	 */
	public String addColumn(){
		if(column != null){
			column.setUpdate_time(DateUtils.getNowTime());
			column.setAddtime(DateUtils.getNowTime());
			column.setOperator(getAuthUserName());
			columnService.insertColumn(column);
			message("添加列名基础数据成功！", "/admin/report/column.html");	
			return ADMINMSG;
		}
		return "add";
	}
	
	/**
	 * 修改列名基础数据
	 * @return
	 */
	public String editColumn(){
		long id = NumberUtils.getLong(request.getParameter("id"));
		if(id > 0 && column == null){
			Column column = columnService.getColumnById(id);
			request.setAttribute("column", column);
			return "edit";
		}else if(column != null && column.getId() > 0){
			column.setUpdate_time(DateUtils.getNowTime());
			column.setOperator(getAuthUserName());
			columnService.editColumnById(column);
		}
		message("修改列名基础数据成功！", "/admin/report/column.html");	
		return ADMINMSG;
	}
	
	/**
	 * 报表基础数据管理分页查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String report(){
		Report report = new Report();
		int pageNo = NumberUtils.getInt(request.getParameter("page"));
		String name = request.getParameter("name");
		report.setName(name);
		String report_name = request.getParameter("report_name");
		report.setReport_name(report_name);
		String status = request.getParameter("status");
		if(status != null && status.length() > 0){
			report.setStatus(Byte.parseByte(status));
		}else report.setStatus((byte)-1);
		
		PageDataList page = reportService.getReportPage(pageNo, report);
		//查询所有的积分；类型
		List<Report> list = page.getList();
		request.setAttribute("reportList", list);
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("report", report);
		request.setAttribute("page", page.getPage());
		request.setAttribute("params", map);
		return "success";
	}
	
	/**
	 * 添加报表基础数据
	 * @return
	 */
	public String addReport(){
		if(reportModel != null){
			message("添加列名基础数据成功！", "/admin/report/column.html");	
			return ADMINMSG;
		}
		List<Column> columnList = columnService.getColumnListByStatus((byte)1);
		request.setAttribute("columnList", columnList);
		return "add";
	}
	
	
	/**
	 * 报表分页查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		Report report = new Report();
		int pageNo = NumberUtils.getInt(request.getParameter("page"));
		String name = request.getParameter("name");
		report.setName(name);
		String report_name = request.getParameter("report_name");
		report.setReport_name(report_name);
		String status = request.getParameter("status");
		if(status != null && status.length() > 0){
			report.setStatus(Byte.parseByte(status));
		}else report.setStatus((byte)-1);
		
		PageDataList page = reportService.getReportPage(pageNo, report);
		//SearchParam param = new SearchParam();
		//查询所有的积分；类型
		List<Report> list = page.getList();
		request.setAttribute("reportList", list);
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("report", report);
		request.setAttribute("page", page.getPage());
		request.setAttribute("params", map);
		return "success";
	}
	
	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public ColumnService getColumnService() {
		return columnService;
	}

	public void setColumnService(ColumnService columnService) {
		this.columnService = columnService;
	}

	public ReportColumnService getReportColumnService() {
		return reportColumnService;
	}

	public void setReportColumnService(ReportColumnService reportColumnService) {
		this.reportColumnService = reportColumnService;
	}

	public ReportService getReportService() {
		return reportService;
	}
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public ReportModel getReportModel() {
		return reportModel;
	}

	public void setReportModel(ReportModel reportModel) {
		this.reportModel = reportModel;
	}
}
