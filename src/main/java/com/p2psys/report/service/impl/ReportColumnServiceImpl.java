package com.p2psys.report.service.impl;

import java.util.List;

import com.p2psys.report.dao.ReportColumnDao;
import com.p2psys.report.domain.ReportColumn;
import com.p2psys.report.service.ReportColumnService;

public class ReportColumnServiceImpl implements ReportColumnService {

	private ReportColumnDao reportColumnDao;
	
	@Override
	public List<ReportColumn> getListByReportId(long report_id) {
		// TODO Auto-generated method stub
		return reportColumnDao.getListByReportId(report_id);
	}

	@Override
	public List<ReportColumn> getListByColumnId(long column_id) {
		// TODO Auto-generated method stub
		return reportColumnDao.getListByColumnId(column_id);
	}

	@Override
	public ReportColumn getReportColumn(long report_id, long column_id) {
		// TODO Auto-generated method stub
		return reportColumnDao.getReportColumn(report_id, column_id);
	}

	@Override
	public long insertReportColumn(ReportColumn reportColumn) {
		// TODO Auto-generated method stub
		return reportColumnDao.insertReportColumn(reportColumn);
	}

	@Override
	public void editReportColumn(ReportColumn reportColumn) {
		// TODO Auto-generated method stub
		reportColumnDao.editReportColumn(reportColumn);
	}

	@Override
	public void deleteById(long id) {
		// TODO Auto-generated method stub
		reportColumnDao.deleteById(id);
	}

	@Override
	public void deleteByReportId(long report_id) {
		// TODO Auto-generated method stub
		reportColumnDao.deleteByReportId(report_id);
	}

	@Override
	public void deleteByColumnId(long column_id) {
		// TODO Auto-generated method stub
		reportColumnDao.deleteByColumnId(column_id);
	}

	public ReportColumnDao getReportColumnDao() {
		return reportColumnDao;
	}

	public void setReportColumnDao(ReportColumnDao reportColumnDao) {
		this.reportColumnDao = reportColumnDao;
	}
	
}
