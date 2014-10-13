package com.p2psys.report.model;

import java.util.ArrayList;
import java.util.List;

import com.p2psys.report.domain.Column;
import com.p2psys.report.domain.Report;
import com.p2psys.report.domain.ReportColumn;
/**
* 导出报表model

* @version 1.0
* @since 2013-11-5
*/
public class ReportModel extends Report {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9181054232815065829L;
	
	private List<ReportColumn> reportColumnList = new ArrayList<ReportColumn>();
	
	private List<Column> columnList = new ArrayList<Column>();

	public List<ReportColumn> getReportColumnList() {
		return reportColumnList;
	}

	public void setReportColumnList(List<ReportColumn> reportColumnList) {
		this.reportColumnList = reportColumnList;
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}
	
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 start
	private String month; //月份
	private String totalPay; //成交总额
	private String countBorrow; //借款项目个数
	private String countFB; //发标笔数
	private String countInvestor; //投资人数
	private String countHY; //注册会员数

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(String totalPay) {
		this.totalPay = totalPay;
	}

	public String getCountBorrow() {
		return countBorrow;
	}

	public void setCountBorrow(String countBorrow) {
		this.countBorrow = countBorrow;
	}

	public String getCountFB() {
		return countFB;
	}

	public void setCountFB(String countFB) {
		this.countFB = countFB;
	}

	public String getCountInvestor() {
		return countInvestor;
	}

	public void setCountInvestor(String countInvestor) {
		this.countInvestor = countInvestor;
	}

	public String getCountHY() {
		return countHY;
	}

	public void setCountHY(String countHY) {
		this.countHY = countHY;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 end
	
}
