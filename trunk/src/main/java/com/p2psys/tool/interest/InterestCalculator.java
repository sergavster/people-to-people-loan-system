package com.p2psys.tool.interest;

import java.util.List;

public interface InterestCalculator {
	
	public int TYPE_DAY_END=1;
	public int TYPE_MONTH_END=2;
	public int TYPE_MONTH_EQUAL=3;
	public int TYPE_MONTH_INTEREST=4;
	
	// v1.6.7.2 RDPROJECT-526 xx 2013-12-10 start
	/** 第1种还款方式 按月分期还款 */
	public int REPAY_MON_INSTALMENT=0;
	/** 第2种还款方式 一次性还款 */
	public int REPAY_ONTIME=2;
	/** 第3种还款方式 每月还息到期还本 */
	public int REPAY_MON_INTEREST_END_CAPITAL=3;
	/** 第4种还款方式 提前付息到期一次性还本 */
	public int REPAY_ADVANCE_INTEREST_END_CAPITAL=4;
	/** 第5种还款方式 每月提前还息到期还本 */
	public int REPAY_MONADVANCE_INTEREST_END_CAPITAL=5;
	// v1.6.7.2 RDPROJECT-526 xx 2013-12-10 end
	/**
	 * 计算每期还款信息
	 * @return
	 */
	public List each();
	
	public double getMoneyPerMonth();
	
	public int getPeriod();
	
	public List getMonthList();
	
	public double getTotalAccount();
	
	public String eachDay();
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
	public List invest_each_month();
	public List invest_each_day();
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
}
