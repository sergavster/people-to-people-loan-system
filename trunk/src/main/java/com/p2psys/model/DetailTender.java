package com.p2psys.model;

import com.p2psys.domain.Tender;

public class DetailTender extends Tender implements java.io.Serializable{
    private static final long serialVersionUID = 8876350238707212045L;
    private String tender_account;
    private String tender_money;
    private long borrow_userid;
    private String op_username;
    private String username;
    private double apr;
    private String time_limit;
    private int time_limit_day;
    private String borrow_name;
    private long borrow_id;
    private String borrow_account;
    private String borrow_account_yes;
    private int credit_jifen;
    private String credit_pic;
    private String verify_time;
    
    // v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start
    private String isday;
    private String award; //投标奖励
    private String late_award; //还款奖励（比例）
    private String part_account;
    private String funds;
    private String fhMoney;
    
    private String tbbz; //投标标准
    private String skbz; //收款标准
    // v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start
    
    // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
    /**
     * 真实姓名
     */
    private String realname;
    // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
    
    public String getTender_account() {
        return tender_account;
    }
    public void setTender_account(String tender_account) {
        this.tender_account = tender_account;
    }
    public String getTender_money() {
        return tender_money;
    }
    public void setTender_money(String tender_money) {
        this.tender_money = tender_money;
    }
    public long getBorrow_userid() {
        return borrow_userid;
    }
    public void setBorrow_userid(long borrow_userid) {
        this.borrow_userid = borrow_userid;
    }
    public String getOp_username() {
        return op_username;
    }
    public void setOp_username(String op_username) {
        this.op_username = op_username;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public double getApr() {
        return apr;
    }
    public void setApr(double apr) {
        this.apr = apr;
    }
    public String getTime_limit() {
        return time_limit;
    }
    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }
    public int getTime_limit_day() {
        return time_limit_day;
    }
    public void setTime_limit_day(int time_limit_day) {
        this.time_limit_day = time_limit_day;
    }
    public String getBorrow_name() {
        return borrow_name;
    }
    public void setBorrow_name(String borrow_name) {
        this.borrow_name = borrow_name;
    }
    public long getBorrow_id() {
        return borrow_id;
    }
    public void setBorrow_id(long borrow_id) {
        this.borrow_id = borrow_id;
    }
    public String getBorrow_account() {
        return borrow_account;
    }
    public void setBorrow_account(String borrow_account) {
        this.borrow_account = borrow_account;
    }
    public String getBorrow_account_yes() {
        return borrow_account_yes;
    }
    public void setBorrow_account_yes(String borrow_account_yes) {
        this.borrow_account_yes = borrow_account_yes;
    }
    public int getCredit_jifen() {
        return credit_jifen;
    }
    public void setCredit_jifen(int credit_jifen) {
        this.credit_jifen = credit_jifen;
    }
    public String getCredit_pic() {
        return credit_pic;
    }
    public void setCredit_pic(String credit_pic) {
        this.credit_pic = credit_pic;
    }
    public String getVerify_time() {
        return verify_time;
    }
    public void setVerify_time(String verify_time) {
        this.verify_time = verify_time;
    }
    /**
     * 获取realname
     * 
     * @return realname
     */
    public String getRealname() {
        return realname;
    }
    /**
     * 设置realname
     * 
     * @param realname 要设置的realname
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getAward() {
        return award;
    }
    public void setAward(String award) {
        this.award = award;
    }
    public String getLate_award() {
        return late_award;
    }
    public void setLate_award(String late_award) {
        this.late_award = late_award;
    }
    public String getPart_account() {
        return part_account;
    }
    public void setPart_account(String part_account) {
        this.part_account = part_account;
    }
    public String getFunds() {
        return funds;
    }
    public void setFunds(String funds) {
        this.funds = funds;
    }
    public String getIsday() {
        return isday;
    }
    public void setIsday(String isday) {
        this.isday = isday;
    }
    public String getFhMoney() {
        return fhMoney;
    }
    public void setFhMoney(String fhMoney) {
        this.fhMoney = fhMoney;
    }
    public String getTbbz() {
        return tbbz;
    }
    public void setTbbz(String tbbz) {
        this.tbbz = tbbz;
    }
    public String getSkbz() {
        return skbz;
    }
    public void setSkbz(String skbz) {
        this.skbz = skbz;
    }
}
