package com.p2psys.tool.interest;

import java.util.ArrayList;
import java.util.List;

import com.p2psys.util.NumberUtils;


/**
 * 提前还息到期一次性还本
 
 * @version 1.0
 * @since 2013年12月9日 下午3:24:16
 * 
 * 5个月的标，按月付息，到期还本还款方式：
 *	投资人的收款情况：
 *	1，满标通过： 收第一个月利息
 *	2，5个月到期：收本金
 * 
 */
public class FullverifyAndEndCalculator implements InterestCalculator{
	private double account;
	private double apr;
	private int period;
	private double moneyPerMonth;
	private String eachString;
	@SuppressWarnings("rawtypes")
	private List monthList;
	
	public FullverifyAndEndCalculator() {
		this(0.0,0.0,0,0);
	}
	
	@SuppressWarnings("rawtypes")
	public FullverifyAndEndCalculator(double account,double apr, int period,int isDay) {
		super();
		this.account = account;
		if(isDay==InterestCalculator.TYPE_DAY_END){
			this.apr = apr/360;
		}else{
			this.apr = apr/12;
		}
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
		//提前还利息
		interest=NumberUtils.format6(apr*account*period);
		accountPerMon=0;
		totalRemain=NumberUtils.format6(totalRemain-interest);
		MonthInterest mi=new MonthInterest( accountPerMon,  interest,totalRemain);
		monthList.add(mi);
		sb.append("每期还钱:"+interest+" 期还款本金："+mi.getAccountPerMon()
				+" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
		sb.append("\n");
		
		//到期还本金
		interest=0;
		accountPerMon=NumberUtils.format6(account);
		totalRemain=NumberUtils.format6(totalRemain-accountPerMon);
		mi=new MonthInterest( accountPerMon,  interest,totalRemain);
		monthList.add(mi);
		sb.append("每期还钱:"+accountPerMon+" 期还款本金："+mi.getAccountPerMon()
				+" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
		sb.append("\n");
		
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
	
	public static void main(String[] args){
		FullverifyAndEndCalculator c=new FullverifyAndEndCalculator(1000,0.12,2,1);
		c.each();
		System.out.println(c.eachString);
		List<MonthInterest> list=c.getMonthList();
		for(MonthInterest mi:list){
			System.out.println(mi.getAccountPerMon()+"\t"
					+mi.getInterest()+"\t"+mi.getTotalRemain());
		}
	}
	
}
