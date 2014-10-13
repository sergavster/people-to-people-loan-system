package com.p2psys.tool.interest;
/**
 * 每月还利息，到期一次性还本金的还款方式计算器
 
 * @date 2012-12-22 下午3:45:16
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class MonthInterest {
	//本金
	private double accountPerMon; 
	//利息
	private double interest;
	//剩余借款
	private double totalRemain;
	  //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
    //净利息
    private double netInterest;
    //净待收
    private double netCollection;
    //到期时间
    private String expire_time;
  //投标奖励比例
    private double tender_award_percentage;
    //续投/线下奖励比例
    private double offline_award_percentage;
    //利息管理费比例
    private double manage_fee_percentage;
    //是否月标
    private int is_month;
    //投标时间
    private String tender_time;
    //是否年利率
    private int is_APR;
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
	  //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
	public MonthInterest() {
		super();
	}
	public MonthInterest(double accountPerMon, double interest,
			double totalRemain) {
		super();
		this.accountPerMon = accountPerMon;
		this.interest = interest;
		this.totalRemain = totalRemain;
	}
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
	public MonthInterest(double accountPerMon, double interest,
			double totalRemain, double netInterest, double netCollection,String expire_time) {
		super();
		this.accountPerMon = accountPerMon;
		this.interest = interest;
		this.totalRemain = totalRemain;
		this.netInterest = netInterest;
		this.netCollection = netCollection;
		this.expire_time=expire_time;
	}
	//v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
	public double getAccountPerMon() {
		return accountPerMon;
	}
	public void setAccountPerMon(double accountPerMon) {
		this.accountPerMon = accountPerMon;
	}
	public double getInterest() {
		return interest;
	}
	public void setInterest(double interest) {
		this.interest = interest;
	}
	  //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 start
	public String getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
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
    public double getManage_fee_percentage() {
        return manage_fee_percentage;
    }
    public void setManage_fee_percentage(double manage_fee_percentage) {
        this.manage_fee_percentage = manage_fee_percentage;
    }
    public int getIs_month() {
        return is_month;
    }
    public void setIs_month(int is_month) {
        this.is_month = is_month;
    }
    public String getTender_time() {
        return tender_time;
    }
    public void setTender_time(String tender_time) {
        this.tender_time = tender_time;
    }
    public int getIs_APR() {
        return is_APR;
    }
    public void setIs_APR(int is_APR) {
        this.is_APR = is_APR;
    }
    
    //v1.6.7.2 RDPROJECT-490 wcw 2013-12-09 end
    public double getTotalRemain() {
        return totalRemain;
    }
    public void setTotalRemain(double totalRemain) {
        this.totalRemain = totalRemain;
    }
	
}
