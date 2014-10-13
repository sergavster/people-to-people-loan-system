package com.p2psys.tool.interest;

import java.util.ArrayList;
import java.util.List;

import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 一次性还款
 *
 */
public class EndInterestCalculator implements InterestCalculator {

	private double account;
	private double apr;
	private int period;
	private double moneyPerMonth;
	private String eachString;
	private List monthList;
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
    //投标奖励百分比
    private double tender_award_percentage;
    //续投/线下奖励百分比
    private double offline_award_percentage;
    //投标奖励
    private double tender_award;
    //续投/线下奖励
    private double offline_award;
    //利息管理费费率
    private double manage_fee;
    //利息管理费
    private double manage_fee_percentage;
    //净利息
    private double netInterest;
    //净待收
    private double netCollection;
    //到期时间
    private String expire_time;
    //投标时间
    private String tender_time;
    //收益总和(未除去利息管理费)
    private double  earn_total;
    //净收益总和（除去利息管理费）
    private double net_earn_total;
    //利息总和
    private double total_interest;
    //是否年利率
    private int is_APR;
    //是否月标
    private int is_month;
    private MonthInterest monthInterest=new MonthInterest();
    //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
	public EndInterestCalculator() {
		this(0.0,0.0,0);
	}
	
	public EndInterestCalculator(double account,double apr, int day) {
		super();
		this.account = account;
		this.apr =apr*day/360;
		this.period = 1;
		monthList=new ArrayList();
	}
	
	public EndInterestCalculator(double account, double apr, int period, MonthInterest monthInterest) {
        super();
        this.account = account;
        this.apr = apr;
        this.period = period;
        this.manage_fee_percentage = monthInterest.getManage_fee_percentage()/100;
        this.tender_time=monthInterest.getTender_time();
        this.tender_award_percentage=monthInterest.getTender_award_percentage()/100;
        this.offline_award_percentage=monthInterest.getOffline_award_percentage()/100;
        this.is_APR=monthInterest.getIs_APR();
        this.is_month=monthInterest.getIs_month();
        if(this.is_month==1){
             if(is_APR==1){
                this.apr=apr*period/12;
             }else{
                 this.apr=apr*period*30;
             }
        }else{
            if(is_APR==1){
                this.apr=apr*period/12/30;
             }else{
                 this.apr=apr*period;
             }
        }
        this.monthInterest = monthInterest;
        monthList=new ArrayList();
    }

    public EndInterestCalculator(double account,double apr, int period,int type) {
		super();
		this.account = account;
		if(type==InterestCalculator.TYPE_DAY_END){
			this.apr = apr*period/360;
		}else{
			this.apr = apr*period/12;
		}
		this.period = 1;
		monthList=new ArrayList();
	}

	@Override
	public double getTotalAccount() {
		moneyPerMonth=NumberUtils.format6(account*(1+apr));
		return moneyPerMonth;
	}

	@Override
	public List each() {
		getTotalAccount();
		double interest=moneyPerMonth-account;
		
		StringBuffer sb=new StringBuffer("");
		sb.append("Total Money:"+account);
		sb.append("\n");
		sb.append("Month Apr:"+apr);
		sb.append("\n");
		sb.append("Month Money:"+moneyPerMonth);
		sb.append("\n");

		MonthInterest mi=new MonthInterest( account,  interest,0);
		monthList.add(mi);
		
		sb.append("每期还钱:"+moneyPerMonth+" 期还款本金："+mi.getAccountPerMon()
				+" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
		sb.append("\n");
		eachString=sb.toString();
		
		return monthList;
	}

	@Override
    public List invest_each_month() {
	  //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
        //投标奖励
        tender_award=NumberUtils.format6(account*tender_award_percentage);
        //线下/续投奖励
        offline_award=NumberUtils.format6(account*offline_award_percentage);
        //净利息
        netInterest=0.0;
        //净待收
        netCollection=0.0;
	    getTotalAccount();
        double interest=moneyPerMonth-account;
        
        StringBuffer sb=new StringBuffer("");
        sb.append("Total Money:"+account);
        sb.append("\n");
        sb.append("Month Apr:"+apr);
        sb.append("\n");
        sb.append("Month Money:"+moneyPerMonth);
        sb.append("\n");
        if(!StringUtils.isBlank(tender_time)){
             tender_time=Long.toString(DateUtils.valueOf(tender_time).getTime()/1000);
             expire_time=DateUtils.rollMonth(tender_time, period+"");
        }
        netInterest=NumberUtils.format6(interest*(1-manage_fee_percentage));
        netCollection=NumberUtils.format6(moneyPerMonth-(interest*manage_fee_percentage));
        
        MonthInterest mi=new MonthInterest( account,  interest,0,netInterest,netCollection,expire_time);
        monthList.add(mi);
        
        sb.append("每期还钱:"+moneyPerMonth+" 期还款本金："+mi.getAccountPerMon()
                +" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
        sb.append("\n");
        eachString=sb.toString();
        total_interest=NumberUtils.format6(interest);
        earn_total=NumberUtils.format6(tender_award+offline_award+total_interest);
        manage_fee=NumberUtils.format6(interest*manage_fee_percentage);
        net_earn_total=NumberUtils.format6(earn_total-manage_fee);
        return monthList;
    }

    @Override
    public List invest_each_day() {
        //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
        //投标奖励
        tender_award=NumberUtils.format6(account*tender_award_percentage);
        //线下/续投奖励
        offline_award=NumberUtils.format6(account*offline_award_percentage);
        //净利息
        netInterest=0.0;
        //净待收
        netCollection=0.0;
        getTotalAccount();
        double interest=NumberUtils.format6(moneyPerMonth-account);
        
        StringBuffer sb=new StringBuffer("");
        sb.append("Total Money:"+account);
        sb.append("\n");
        sb.append("Month Apr:"+apr);
        sb.append("\n");
        sb.append("Month Money:"+moneyPerMonth);
        sb.append("\n");
        
       
        netInterest=NumberUtils.format6(interest*(1-manage_fee_percentage));
        netCollection=NumberUtils.format6(moneyPerMonth-(interest*manage_fee_percentage));
        if(!StringUtils.isBlank(tender_time)){
            tender_time=Long.toString(DateUtils.valueOf(tender_time).getTime()/1000);
            expire_time=DateUtils.rollDay(tender_time, period+"");
        }
        MonthInterest mi=new MonthInterest( account,  interest,0,netInterest,netCollection,expire_time);
        monthList.add(mi);
        
        sb.append("每期还钱:"+moneyPerMonth+" 期还款本金："+mi.getAccountPerMon()
                +" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
        sb.append("\n");
        total_interest=NumberUtils.format6(interest);
        eachString=sb.toString();
        earn_total=NumberUtils.format6(tender_award+offline_award+total_interest);
        manage_fee=NumberUtils.format6(total_interest*manage_fee_percentage);
        net_earn_total=NumberUtils.format6(earn_total-manage_fee);
        return monthList;
    }

    public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
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

	public String getEachString() {
		return eachString;
	}

	public void setEachString(String eachString) {
		this.eachString = eachString;
	}

	public List getMonthList() {
		return monthList;
	}

	public void setMonthList(List monthList) {
		this.monthList = monthList;
	}

	@Override
	public String eachDay() {
		// TODO Auto-generated method stub
		return null;
	}
    //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
    public double getTender_award_percentage() {
        return tender_award_percentage;
    }

    public void setTender_award_percentage(double tender_award_percentage) {
        this.tender_award_percentage = tender_award_percentage;
    }

    public double getOffline_award_percentage() {
        return offline_award_percentage;
    }

    public void setOffline_award_percentage(double offline_award_percentage) {
        this.offline_award_percentage = offline_award_percentage;
    }

    public double getTender_award() {
        return tender_award;
    }

    public void setTender_award(double tender_award) {
        this.tender_award = tender_award;
    }

    public double getOffline_award() {
        return offline_award;
    }

    public void setOffline_award(double offline_award) {
        this.offline_award = offline_award;
    }

    public double getManage_fee() {
        return manage_fee;
    }

    public void setManage_fee(double manage_fee) {
        this.manage_fee = manage_fee;
    }

    public double getManage_fee_percentage() {
        return manage_fee_percentage;
    }

    public void setManage_fee_percentage(double manage_fee_percentage) {
        this.manage_fee_percentage = manage_fee_percentage;
    }

    public double getNetInterest() {
        return netInterest;
    }

    public void setNetInterest(double netInterest) {
        this.netInterest = netInterest;
    }

    public double getNetCollection() {
        return netCollection;
    }

    public void setNetCollection(double netCollection) {
        this.netCollection = netCollection;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getTender_time() {
        return tender_time;
    }

    public void setTender_time(String tender_time) {
        this.tender_time = tender_time;
    }

    public double getEarn_total() {
        return earn_total;
    }

    public void setEarn_total(double earn_total) {
        this.earn_total = earn_total;
    }

    public double getNet_earn_total() {
        return net_earn_total;
    }

    public void setNet_earn_total(double net_earn_total) {
        this.net_earn_total = net_earn_total;
    }

    public double getTotal_interest() {
        return total_interest;
    }

    public void setTotal_interest(double total_interest) {
        this.total_interest = total_interest;
    }

    //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
	
}
