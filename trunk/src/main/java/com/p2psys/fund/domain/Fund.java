package com.p2psys.fund.domain;

/**
 * 基金信托实体类
 
 * @version 1.0
 * @since 2013-10-22
 */
public class Fund {

	private long id;
	private long userId;
	private byte type; //1：基金，2：信托
	private byte status;//0：关闭，1：正在进行，8：认购完成
	private String name;
	private String pic;
	private double account;//万
	private double accountYes;//万
	private int tenderTime;
	private int visitTime;
	private double apr;//预期年化收益
	private double lowestAccount;//最低认购金额
	private double mostAccount;//最大认购金额
	private byte isDay;//产品期限是否为天,0:非,1:是
	private String timeLimit;//产品期限
	private String validTime;//有效时间
	private byte style;//收益分配方式
	private String issuer;//发行机构
	private String guaranty;//担保机构
	private byte counterGuaranty;//反担保方式
	private String phone;
	private String content;//项目简介
	private int addTime;
	private String addIp;
	
	private double trustLowestAccount;//最低认购金额-信托
	private double trustMostAccount;//最大认购金额-信托
	private String timeLimitDay;//产品期限-天
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public double getAccount() {
		return account;
	}
	public void setAccount(double account) {
		this.account = account;
	}
	public double getAccountYes() {
		return accountYes;
	}
	public void setAccountYes(double accountYes) {
		this.accountYes = accountYes;
	}
	public int getTenderTime() {
		return tenderTime;
	}
	public void setTenderTime(int tenderTime) {
		this.tenderTime = tenderTime;
	}
	public int getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(int visitTime) {
		this.visitTime = visitTime;
	}
	public double getApr() {
		return apr;
	}
	public void setApr(double apr) {
		this.apr = apr;
	}
	public double getLowestAccount() {
		return lowestAccount;
	}
	public void setLowestAccount(double lowestAccount) {
		this.lowestAccount = lowestAccount;
	}
	public double getMostAccount() {
		return mostAccount;
	}
	public void setMostAccount(double mostAccount) {
		this.mostAccount = mostAccount;
	}
	public byte getIsDay() {
		return isDay;
	}
	public void setIsDay(byte isDay) {
		this.isDay = isDay;
	}
	public String getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public byte getStyle() {
		return style;
	}
	public void setStyle(byte style) {
		this.style = style;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getGuaranty() {
		return guaranty;
	}
	public void setGuaranty(String guaranty) {
		this.guaranty = guaranty;
	}
	public byte getCounterGuaranty() {
		return counterGuaranty;
	}
	public void setCounterGuaranty(byte counterGuaranty) {
		this.counterGuaranty = counterGuaranty;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getAddTime() {
		return addTime;
	}
	public void setAddTime(int addTime) {
		this.addTime = addTime;
	}
	public String getAddIp() {
		return addIp;
	}
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	public double getTrustLowestAccount() {
		return trustLowestAccount;
	}
	public void setTrustLowestAccount(double trustLowestAccount) {
		this.trustLowestAccount = trustLowestAccount;
	}
	public double getTrustMostAccount() {
		return trustMostAccount;
	}
	public void setTrustMostAccount(double trustMostAccount) {
		this.trustMostAccount = trustMostAccount;
	}
	public String getTimeLimitDay() {
		return timeLimitDay;
	}
	public void setTimeLimitDay(String timeLimitDay) {
		this.timeLimitDay = timeLimitDay;
	}
	
}
