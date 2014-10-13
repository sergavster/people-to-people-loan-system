package com.p2psys.tool.interest;
// v1.6.7.2 RDPROJECT-526 xx start
// 新增类
// v1.6.7.2 RDPROJECT-526 xx end
import java.util.ArrayList;
import java.util.List;

import com.p2psys.util.NumberUtils;


/**
 * 满标还息每月还息到期还本
 
 * @version 1.0
 * @since 2013年12月4日 下午3:52:16
 * 
 * 5个月的标，按月付息，到期还本还款方式：
	投资人的收款情况：
	1，满标通过： 收第一个月利息
	2，第一个月到期：收第二月利息
	3，第二个月到期：收第三月利息
	4，第三个月到期：收第四月利息
	5，第四个月到期：收第五月利息
	6，第五个月到期：收本金
 * 
 */
public class FullverifyAndMonthCalculator implements InterestCalculator{
	private double account;
	private double apr;
	private int period;
	private double moneyPerMonth;
	private String eachString;
	@SuppressWarnings("rawtypes")
	private List monthList;
	
	public FullverifyAndMonthCalculator() {
		this(0.0,0.0,0);
	}
	
	@SuppressWarnings("rawtypes")
	public FullverifyAndMonthCalculator(double account,double apr, int period) {
		super();
		this.account = account;
		this.apr = apr/12;
		this.period = period;
		monthList=new ArrayList();
	}
	
	@Override
	public double getTotalAccount() {
		return account*apr*period+account;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List each(){
		//总共需要还款金额
		double totalRemain=account*apr*period+account;
		//每期需要还款中的本金
		double accountPerMon=0.0;
		//每期需要还款中的利息
		double interest=0.0;
		//用于控制台输出的字符串
		StringBuffer sb=new StringBuffer("");
		sb.append("Total Money:"+totalRemain);
		sb.append("\n");
		sb.append("Month Apr:"+apr);
		sb.append("\n");
		//除去最后一期利息总额
		double interestTotal=0;
		//除去最后一期待还总额
		double repayAccountTotal=0;
		//循环计算accountPerMon、interest、totalRemain
		for(int i=0;i<period+1;i++){	
			//计算每月需要支付的利息
			interest=NumberUtils.format6(account*apr);
			moneyPerMonth=interest;
			//非最后一期
			if(i!=period){
			   interestTotal+=interest;
			   repayAccountTotal+=interest;
			} else {//若是最后一期
				 interest=NumberUtils.format6((getTotalAccount()-account)-interestTotal);
				 moneyPerMonth=interest;
			}
			//计算每月需要还款中的本金
			accountPerMon=0;
			//实际支付的金额扣除本月已经支付的金额
			totalRemain=NumberUtils.format6(totalRemain-moneyPerMonth);
			if(i==period){
				accountPerMon=account;
				totalRemain=totalRemain-account;
			}
			
			MonthInterest mi=new MonthInterest( accountPerMon,interest,totalRemain);
			monthList.add(mi);
			
			sb.append("每月还钱:"+moneyPerMonth+" 月还款本金："+mi.getAccountPerMon()
					+" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain()+" 除去最后一期利息总额："+interestTotal);
			sb.append("\n");
		}
		eachString=sb.toString();
		return monthList;
	}
	
	@Override
    public List invest_each_month() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List invest_each_day() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public String toString() {
		return this.eachString;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public double getMoneyPerMonth() {
		return moneyPerMonth;
	}

	public void setMoneyPerMonth(double moneyPerMonth) {
		this.moneyPerMonth = moneyPerMonth;
	}

	@SuppressWarnings("rawtypes")
	public List getMonthList() {
		return monthList;
	}

	@SuppressWarnings("rawtypes")
	public void setMonthList(List monthList) {
		this.monthList = monthList;
	}

	public String eachDay() {
		//总共需要还款金额
		double totalRemain=account*(apr/12/30)*period+account;
		//System.out.println(totalRemain);
		StringBuffer sb=new StringBuffer("");
		sb.append("Total Money:"+totalRemain);
		sb.append("\n");
		sb.append("Month Apr:"+apr);
		sb.append("\n");
		return "借款额度为："+account+"  到期需偿还"+totalRemain;
	}
}
