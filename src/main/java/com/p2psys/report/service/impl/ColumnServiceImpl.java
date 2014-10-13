package com.p2psys.report.service.impl;

import java.util.List;

import com.p2psys.model.PageDataList;
import com.p2psys.report.dao.ColumnDao;
import com.p2psys.report.dao.ReportColumnDao;
import com.p2psys.report.domain.Column;
import com.p2psys.report.model.ColumnModel;
import com.p2psys.report.service.ColumnService;
import com.p2psys.tool.Page;

public class ColumnServiceImpl implements ColumnService {

	private ColumnDao columnDao;
	
	private ReportColumnDao reportColumnDao;
	
	@Override
	public PageDataList getColumnPage(int page, Column column) {
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(columnDao.getColumnCount(column) , page );
		pageDataList.setList(columnDao.getColumnPage(pages.getStart(), pages.getPernum() , column));
		pageDataList.setPage(pages);
		return pageDataList;
	}

	@Override
	public List<ColumnModel> getColumnModelList(ColumnModel column) {
		// TODO Auto-generated method stub
		return columnDao.getColumnModelList(column);
	}

	@Override
	public List<Column> getColumnList(Column column) {
		// TODO Auto-generated method stub
		return columnDao.getColumnList(column);
	}

	@Override
	public Column getColumnById(long id) {
		// TODO Auto-generated method stub
		return columnDao.getColumnById(id);
	}

	@Override
	public long insertColumn(Column column) {
		// TODO Auto-generated method stub
		return columnDao.insertColumn(column);
	}

	@Override
	public void editColumnById(Column column) {
		// TODO Auto-generated method stub
		columnDao.editColumnById(column);
	}

	@Override
	public void deleteById(long id) {
		// TODO Auto-generated method stub
		// 列名删除时，将其关联信息也删除
		reportColumnDao.deleteByColumnId(id);
		columnDao.deleteById(id);
	}

	/**
	 * 根据status条件查询信息
	 * @param status 查询参数
	 * @return 返回实体List
	 */
	public List<Column> getColumnListByStatus(Byte status){
		Column column = new Column();
		column.setStatus(status);
		return columnDao.getColumnList(column);
	}
	
	public ReportColumnDao getReportColumnDao() {
		return reportColumnDao;
	}

	public void setReportColumnDao(ReportColumnDao reportColumnDao) {
		this.reportColumnDao = reportColumnDao;
	}

	public ColumnDao getColumnDao() {
		return columnDao;
	}

	public void setColumnDao(ColumnDao columnDao) {
		this.columnDao = columnDao;
	}
}
