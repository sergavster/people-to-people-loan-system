package com.p2psys.report.service.impl;

import com.p2psys.model.PageDataList;
import com.p2psys.report.dao.ReportColumnDao;
import com.p2psys.report.dao.ReportDao;
import com.p2psys.report.domain.Report;
import com.p2psys.report.service.ReportService;
import com.p2psys.tool.Page;

public class ReportServiceImpl implements ReportService {

	private ReportDao reportDao;
	
	private ReportColumnDao reportColumnDao;
	
	@Override
	public PageDataList getReportPage(int page, Report report) {
		if(report == null) report = new Report();
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(reportDao.getReportCount(report) , page );
		pageDataList.setList(reportDao.getReportPage(pages.getStart(), pages.getPernum() , report));
		pageDataList.setPage(pages);
		return pageDataList;
	}

	@Override
	public Report getReportById(long id) {
		// TODO Auto-generated method stub
		return reportDao.getReportById(id);
	}

	@Override
	public Report getReportByNid(String nid, Byte status) {
		// TODO Auto-generated method stub
		return reportDao.getReportByNid(nid, status);
	}

	@Override
	public long inserReport(Report report) {
		// TODO Auto-generated method stub
		return reportDao.inserReport(report);
	}

	@Override
	public void editReportById(Report report) {
		// TODO Auto-generated method stub
		reportDao.editReportById(report);
	}

	@Override
	public void deleteById(long id) {
		// TODO Auto-generated method stub
		reportColumnDao.deleteByReportId(id);
		reportDao.deleteById(id);
	}

	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 start
	/**
	 * 根据注册时间统计人数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Integer registerCount(long startDate,long endDate){
		return reportDao.registerCount(startDate, endDate);
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 end
	
	public ReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}

	public ReportColumnDao getReportColumnDao() {
		return reportColumnDao;
	}

	public void setReportColumnDao(ReportColumnDao reportColumnDao) {
		this.reportColumnDao = reportColumnDao;
	}
	
}
