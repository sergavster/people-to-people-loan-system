package com.p2psys.tool.interest;

import java.util.ArrayList;
import java.util.List;

import com.p2psys.tool.Utils;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 等额还息计算工具类
 * 
 
 * @date 2012-9-7 下午2:45:46
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class MonthEqualCalculator implements InterestCalculator{
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
	public MonthEqualCalculator() {
		this(0.0,0.0,0);
	}
	
	public MonthEqualCalculator(double account,double apr, int period) {
		super();
		this.account = account;
		this.apr = apr;
		this.period = period;
		monthList=new ArrayList();
	}
	  //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
	public MonthEqualCalculator(double account, double apr, int period, MonthInterest monthInterest) {
		super();
		this.account = account;
		this.period = period;
		this.manage_fee_percentage = monthInterest.getManage_fee_percentage()/100;
		this.tender_time=monthInterest.getTender_time();
		this.tender_award_percentage=monthInterest.getTender_award_percentage()/100;
		this.offline_award_percentage=monthInterest.getOffline_award_percentage()/100;
		this.is_APR=monthInterest.getIs_APR();
		this.is_month=monthInterest.getIs_month();
		if(this.is_month==1){
		     if(is_APR==1){
		        this.apr=apr/12;
		     }else{
		         this.apr=apr*30;
		     }
		}else{
		    if(is_APR==1){
                this.apr=apr/12/30;
             }else{
                 this.apr=apr;
             }
		}
		monthList=new ArrayList();

	}
	  //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
	@Override
	public double getTotalAccount() {
		return moneyPerMonth*period;
	}

	@Override
	public List each(){
	  //计算平均每月还款
        moneyPerMonth=Utils.Mrpi(account, apr, period);
        //浮点型格式化
        moneyPerMonth=NumberUtils.format6(moneyPerMonth);
        //总共需要还款金额
        double totalRemain=moneyPerMonth*period;
        //每期还款后剩余金额
        double remain=account;
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
        sb.append("Month Money:"+moneyPerMonth);
        sb.append("\n");
        //除去最后一期之外利息总和
        double interestTotal=0;
        //除去最后一期之外待还金额总和
        double repayAccountTotal=0;
        //循环计算accountPerMon、interest、totalRemain
        for(int i=0;i<period;i++){  
            //计算每月需要支付的利息
            interest=NumberUtils.format6(remain*apr/12);
            //若不是最后一期
            if(i+1!=period){
                interestTotal+=interest;
                repayAccountTotal+=moneyPerMonth;  
            }else{
              //若是最后一期
                 interest=NumberUtils.format6((getTotalAccount()-account)-interestTotal);
                 moneyPerMonth=NumberUtils.format6(getTotalAccount()-repayAccountTotal);
            }
            //用于计算利息的剩余金额
            remain=NumberUtils.format6(remain+interest-moneyPerMonth);
            //计算每月需要还款中的本金
            accountPerMon=NumberUtils.format6(moneyPerMonth-interest);
            //实际支付的金额扣除本月已经支付的金额
            totalRemain=NumberUtils.format6(totalRemain-moneyPerMonth);
            
            MonthInterest mi=new MonthInterest( accountPerMon,  interest,totalRemain);
            monthList.add(mi);
            
            
            sb.append("每月还钱:"+moneyPerMonth+" 月还款本金："+mi.getAccountPerMon()
                    +" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
            sb.append("\n");
        }
        eachString=sb.toString();
        return monthList;
	}
   @Override
    public String eachDay() {
        //总共需要还款金额
        double totalRemain=account*(apr/12/30)*period+account;
        StringBuffer sb=new StringBuffer("");
        sb.append("Total Money:"+totalRemain);
        sb.append("\n");
        sb.append("Month Apr:"+apr);
        sb.append("\n");
        return "借款额度为："+account+"  到期需偿还"+totalRemain;
    }
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
	//投资人
	@Override
    public List invest_each_month(){
        //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
        //总利息
        total_interest=0.0;
        //投标奖励
        tender_award=NumberUtils.format6(account*tender_award_percentage);
        //线下/续投奖励
        offline_award=NumberUtils.format6(account*offline_award_percentage);
        //净利息
        netInterest=0.0;
        //净待收
        netCollection=0.0;
        //计算平均每月回款款
        moneyPerMonth=Utils.NewMrpi(account, apr, period);
        //浮点型格式化
        moneyPerMonth=NumberUtils.format6(moneyPerMonth);
        //总共需要回款金额
        double totalRemain=moneyPerMonth*period;
        //每期回款款后剩余金额
        double remain=account;
        //每期需要回款中的本金
        double accountPerMon=0.0;
        //每期需要回款中的利息
        double interest=0.0;
        //用于控制台输出的字符串
        StringBuffer sb=new StringBuffer("");
        sb.append("Total Money:"+totalRemain);
        sb.append("\n");
        sb.append("Month Apr:"+apr);
        sb.append("\n");
        sb.append("Month Money:"+moneyPerMonth);
        sb.append("\n");
        //除去最后一期之外利息总和
        double interestTotal=0;
        //除去最后一期之外待收金额总和
        double repayAccountTotal=0;
        if(!StringUtils.isBlank(tender_time)){
        tender_time=Long.toString(DateUtils.valueOf(tender_time).getTime()/1000);
        }
        //循环计算accountPerMon、interest、totalRemain
        for(int i=0;i<period;i++){  
            //计算每月需要支付的利息
            interest=NumberUtils.format6(remain*apr);
            //若不是最后一期
            if(i+1!=period){
                interestTotal+=interest;
                repayAccountTotal+=moneyPerMonth;  
            }else{
              //若是最后一期
                 interest=NumberUtils.format6((getTotalAccount()-account)-interestTotal);
                 moneyPerMonth=NumberUtils.format6(getTotalAccount()-repayAccountTotal);
            }
            //用于计算利息的剩余金额
            remain=NumberUtils.format6(remain+interest-moneyPerMonth);
            //计算每月需要回款中的本金
            accountPerMon=NumberUtils.format6(moneyPerMonth-interest);
            //实际支付的金额扣除本月已经支付的金额
            totalRemain=NumberUtils.format6(totalRemain-moneyPerMonth);
            
            netInterest=NumberUtils.format6(interest*(1-manage_fee_percentage));
            netCollection=NumberUtils.format6(moneyPerMonth-(interest*manage_fee_percentage));
            if(!StringUtils.isBlank(tender_time)){
               expire_time=DateUtils.rollMonth(tender_time, i+1+"");
            }
            MonthInterest mi=new MonthInterest( accountPerMon,  interest,totalRemain,netInterest,netCollection,expire_time);
            monthList.add(mi);
            total_interest+=interest;
            
            sb.append("每月回款:"+moneyPerMonth+" 月回款本金："+mi.getAccountPerMon()
                    +" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
            sb.append("\n");
        }
        total_interest=NumberUtils.format6(total_interest);
        eachString=sb.toString();
        earn_total=NumberUtils.format6(tender_award+offline_award+total_interest);
        manage_fee=NumberUtils.format6(total_interest*manage_fee_percentage);
        net_earn_total=NumberUtils.format6(earn_total-manage_fee);
          //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
        return monthList;
    }
	
	@Override
    public List invest_each_day() {
	 /* //总共需要回款款金额
        double totalRemain=account*(apr/12/30)*period+account;
        StringBuffer sb=new StringBuffer("");
        sb.append("Total Money:"+totalRemain);
        sb.append("\n");
        sb.append("Month Apr:"+apr);
        sb.append("\n");
        return "回款额度为："+account+"  到期需回款"+totalRemain;*/
	  //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
        //总利息
        total_interest=0.0;
        //投标奖励
        tender_award=NumberUtils.format6(account*tender_award_percentage);
        //线下/续投奖励
        offline_award=NumberUtils.format6(account*offline_award_percentage);
        //净利息
        netInterest=0.0;
        //净待收
        netCollection=0.0;
        //计算平均每月回款款
        moneyPerMonth=account*apr*period+account;
        //浮点型格式化
        moneyPerMonth=NumberUtils.format6(moneyPerMonth);
        //总共需要回款金额
        double totalRemain=moneyPerMonth*1;
        //每期回款款后剩余金额
        double remain=account;
        //每期需要回款中的本金
        double accountPerMon=0.0;
        //每期需要回款中的利息
        double interest=0.0;
        //用于控制台输出的字符串
        StringBuffer sb=new StringBuffer("");
        sb.append("Total Money:"+totalRemain);
        sb.append("\n");
        sb.append("Month Apr:"+apr);
        sb.append("\n");
        sb.append("Month Money:"+moneyPerMonth);
        sb.append("\n");
        //除去最后一期之外利息总和
        double interestTotal=0;
        //除去最后一期之外待收金额总和
        double repayAccountTotal=0;
        if(!StringUtils.isBlank(tender_time)){
          tender_time=Long.toString(DateUtils.valueOf(tender_time).getTime()/1000);
          expire_time=DateUtils.rollDay(tender_time, period+"");
        }
        //循环计算accountPerMon、interest、totalRemain 
        //计算每月需要支付的利息
        interest=NumberUtils.format6(remain*apr*period);
        //若不是最后一期
        //用于计算利息的剩余金额
        remain=NumberUtils.format6(remain+interest-moneyPerMonth);
        //计算每月需要回款中的本金
        accountPerMon=NumberUtils.format6(moneyPerMonth-interest);
        //实际支付的金额扣除本月已经支付的金额
        totalRemain=NumberUtils.format6(totalRemain-moneyPerMonth);
        
        netInterest=NumberUtils.format6(interest*(1-manage_fee_percentage));
        netCollection=NumberUtils.format6(moneyPerMonth-(interest*manage_fee_percentage));
        MonthInterest mi=new MonthInterest( accountPerMon,  interest,totalRemain,netInterest,netCollection,expire_time);
        monthList.add(mi);
        total_interest+=interest;
        
        sb.append("每月回款:"+moneyPerMonth+" 月回款本金："+mi.getAccountPerMon()
                +" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
        sb.append("\n");
        total_interest=NumberUtils.format6(total_interest);
        eachString=sb.toString();
        earn_total=NumberUtils.format6(tender_award+offline_award+total_interest);
        manage_fee=NumberUtils.format6(total_interest*manage_fee_percentage);
        net_earn_total=NumberUtils.format6(earn_total-manage_fee);
          //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
        return monthList;
    }
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
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

	public List getMonthList() {
		return monthList;
	}

	public void setMonthList(List monthList) {
		this.monthList = monthList;
	}
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
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

	public String getEachString() {
		return eachString;
	}

	public void setEachString(String eachString) {
		this.eachString = eachString;
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

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

    public int getIs_APR() {
        return is_APR;
    }

    public void setIs_APR(int is_APR) {
        this.is_APR = is_APR;
    }

    public MonthInterest getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(MonthInterest monthInterest) {
        this.monthInterest = monthInterest;
    }

    public int getIs_month() {
        return is_month;
    }

    public void setIs_month(int is_month) {
        this.is_month = is_month;
    }
	
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
}
