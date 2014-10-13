package com.p2psys.model;

public class UserAccountSummary {

	private long user_id;

	// 资金记录汇总
	private AccountLogSummary logSummary;
	// 个人资金详情
	// 账户总额
	private double accountTotal;
	// 可用余额
	private double accountUseMoney;
	// 冻结金额
	private double accountNoUseMoney;
	//净资产
	private double accountOwnMoney;
	
	// 投标冻结总额
	private double borrowNoUseTotal;

	// 投资资金详情
	// 投标总额
	private double investTotal;
	
	private double investInterest;
	// 借出总额
	private double lendTotal;
	// 奖励收入总额
	private double awardTotal;
    //已赚利息总额
	private double yes_interest;
	//最大建议提现额
	private double max_cash;
	//红包
	private double hongbao;
	//还款总额
	private double collect;
	//回款总额
	private double huikuanSum;
	// 已收总额
	private double acceptSum;
	
	/**
	 * @return the acceptSum
	 */
	public double getAcceptSum() {
		return acceptSum;
	}

	/**
	 * @param acceptSum the acceptSum to set
	 */
	public void setAcceptSum(double acceptSum) {
		this.acceptSum = acceptSum;
	}

	public double getCollect() {
		return collect;
	}

	public void setCollect(double collect) {
		this.collect = collect;
	}

	public double getHuikuanSum() {
		return huikuanSum;
	}

	public void setHuikuanSum(double huikuanSum) {
		this.huikuanSum = huikuanSum;
	}
	public double getHongbao() {
		return hongbao;
	}

	public void setHongbao(double hongbao) {
		this.hongbao = hongbao;
	}

	//免费提现额
	private double free_cash;
	
	public double getFree_cash() {
		return free_cash;
	}

	public void setFree_cash(double free_cash) {
		this.free_cash = free_cash;
	}

	public double getMax_cash() {
		return max_cash;
	}

	public void setMax_cash(double max_cash) {
		this.max_cash = max_cash;
	}

	public double getYes_interest() {
		return yes_interest;
	}

	public void setYes_interest(double yes_interest) {
		this.yes_interest = yes_interest;
	}

	// 收款资金详情
	// 待回收总额
	private double collectTotal;
	// 待回收利息
	private double collectInterest;

	// 逾期的本金
	private double lateTotal;
	// 逾期的利息
	private double lateInterest;
	// 逾期罚金收入
	private double overdueInterest;
	// 网站垫付总额
	private double advanceTotal;

	// 损失利息总额
	private double lossInterest;
	// 最近收款日期
	private String newestCollectDate;
	
	private double newestCollectMoney;

	// 贷款资金详情
	// 借款总额
	private double borrowTotal;
	
	private double borrowInterest;
	// 借款标次数
	private int borrowTimes;
	private double repayTotal;
	private double repayInterest;
	// 已还总额
	private double repaidTotal;
	// 未还总额
	private double notRepayTotal;

	// 投资次数
	private int investTimes;
	// 还款标数
	private int repayTimes;
	// 待还笔数
	private int waitRepayTimes;
	// 最近还款日期
	private String newestRepayDate;
	// 最近应还款金额
	private double newestRepayMoney;

	// 充值成功总额
	private double rechargeTotal;
	// 提现成功总额
	private double cashTotal;
	// 在线充值总额
	private double onlineRechargeTotal;
	
	//当天在线充值总额
	private double todayOnlineRechargeTotal;
	public double getTodayOnlineRechargeTotal() {
		return todayOnlineRechargeTotal;
	}

	public void setTodayOnlineRechargeTotal(double todayOnlineRechargeTotal) {
		this.todayOnlineRechargeTotal = todayOnlineRechargeTotal;
	}

	// 线下充值总额
	private double offlineRechargeTotal;
	// 收到充值总额
	private double manualRechargeTotal;
	// 手续费总额
	private double feeTotal;
	// 充值手续费
	private double rechargeFee;
	// 提现手续费
	private double cashFee;
    //提现到账金额
	private double cashCredited;
	// 最高额度
	private double mostAmount;
	// 最低额度
	private double leastAmount;
	public double getCashCredited() {
		return cashCredited;
	}

	public void setCashCredited(double cashCredited) {
		this.cashCredited = cashCredited;
	}

	// 可用额度
	private double useAmount;

	private int waitRepayCount;
	
	private int hadRepayCount;
	
	private int hadDueRepayCount;
	
	private int waitDueRepayCount;
	
	// v1.6.7.1 RDPROJECT-399 zza 2013-11-06 start
	/**
	 * 未收还款奖励
	 */
	private double repayAward;
	
	/**
	 * 已收还款奖励
	 */
	private double repayYesAward;
	// v1.6.7.1 RDPROJECT-399 zza 2013-11-06 end
	
	public UserAccountSummary() {
		super();
	}

	public UserAccountSummary(long user_id) {
		super();
		this.user_id = user_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public AccountLogSummary getLogSummary() {
		return logSummary;
	}

	public void setLogSummary(AccountLogSummary logSummary) {
		this.logSummary = logSummary;
	}

	public double getAccountTotal() {
		return accountTotal;
	}

	public void setAccountTotal(double accountTotal) {
		this.accountTotal = accountTotal;
	}

	public double getAccountUseMoney() {
		return accountUseMoney;
	}

	public void setAccountUseMoney(double accountUseMoney) {
		this.accountUseMoney = accountUseMoney;
	}

	public double getAccountNoUseMoney() {
		return accountNoUseMoney;
	}

	public void setAccountNoUseMoney(double accountNoUseMoney) {
		this.accountNoUseMoney = accountNoUseMoney;
	}

	public double getBorrowNoUseTotal() {
		return borrowNoUseTotal;
	}

	public void setBorrowNoUseTotal(double borrowNoUseTotal) {
		this.borrowNoUseTotal = borrowNoUseTotal;
	}

	public double getInvestTotal() {
		return investTotal;
	}

	public void setInvestTotal(double investTotal) {
		this.investTotal = investTotal;
	}

	public double getLendTotal() {
		return lendTotal;
	}

	public void setLendTotal(double lendTotal) {
		this.lendTotal = lendTotal;
	}

	public double getAwardTotal() {
		return awardTotal;
	}

	public void setAwardTotal(double awardTotal) {
		this.awardTotal = awardTotal;
	}

	public double getCollectTotal() {
		return collectTotal;
	}

	public void setCollectTotal(double collectTotal) {
		this.collectTotal = collectTotal;
	}

	public double getCollectInterest() {
		return collectInterest;
	}

	public void setCollectInterest(double collectInterest) {
		this.collectInterest = collectInterest;
	}

	public double getLateTotal() {
		return lateTotal;
	}

	public void setLateTotal(double lateTotal) {
		this.lateTotal = lateTotal;
	}

	public double getLateInterest() {
		return lateInterest;
	}

	public void setLateInterest(double lateInterest) {
		this.lateInterest = lateInterest;
	}
	
	public double getOverdueInterest() {
		return overdueInterest;
	}

	public void setOverdueInterest(double overdueInterest) {
		this.overdueInterest = overdueInterest;
	}

	public double getAdvanceTotal() {
		return advanceTotal;
	}

	public void setAdvanceTotal(double advanceTotal) {
		this.advanceTotal = advanceTotal;
	}

	public double getLossInterest() {
		return lossInterest;
	}

	public void setLossInterest(double lossInterest) {
		this.lossInterest = lossInterest;
	}

	public String getNewestCollectDate() {
		return newestCollectDate;
	}

	public void setNewestCollectDate(String newestCollectDate) {
		this.newestCollectDate = newestCollectDate;
	}

	public double getBorrowTotal() {
		return borrowTotal;
	}

	public void setBorrowTotal(double borrowTotal) {
		this.borrowTotal = borrowTotal;
	}

	public int getBorrowTimes() {
		return borrowTimes;
	}

	public void setBorrowTimes(int borrowTimes) {
		this.borrowTimes = borrowTimes;
	}

	public double getRepaidTotal() {
		return repaidTotal;
	}

	public void setRepaidTotal(double repaidTotal) {
		this.repaidTotal = repaidTotal;
	}

	public double getNotRepayTotal() {
		return notRepayTotal;
	}

	public void setNotRepayTotal(double notRepayTotal) {
		this.notRepayTotal = notRepayTotal;
	}

	public int getInvestTimes() {
		return investTimes;
	}

	public void setInvestTimes(int investTimes) {
		this.investTimes = investTimes;
	}

	public int getRepayTimes() {
		return repayTimes;
	}

	public void setRepayTimes(int repayTimes) {
		this.repayTimes = repayTimes;
	}
	
	public String getNewestRepayDate() {
		return newestRepayDate;
	}

	public void setNewestRepayDate(String newestRepayDate) {
		this.newestRepayDate = newestRepayDate;
	}
	public double getNewestCollectMoney() {
		return newestCollectMoney;
	}

	public void setNewestCollectMoney(double newestCollectMoney) {
		this.newestCollectMoney = newestCollectMoney;
	}

	public double getNewestRepayMoney() {
		return newestRepayMoney;
	}

	public void setNewestRepayMoney(double newestRepayMoney) {
		this.newestRepayMoney = newestRepayMoney;
	}

	public double getRechargeTotal() {
		return rechargeTotal;
	}

	public void setRechargeTotal(double rechargeTotal) {
		this.rechargeTotal = rechargeTotal;
	}

	public double getCashTotal() {
		return cashTotal;
	}

	public void setCashTotal(double cashTotal) {
		this.cashTotal = cashTotal;
	}

	public double getOnlineRechargeTotal() {
		return onlineRechargeTotal;
	}

	public void setOnlineRechargeTotal(double onlineRechargeTotal) {
		this.onlineRechargeTotal = onlineRechargeTotal;
	}

	public double getOfflineRechargeTotal() {
		return offlineRechargeTotal;
	}

	public void setOfflineRechargeTotal(double offlineRechargeTotal) {
		this.offlineRechargeTotal = offlineRechargeTotal;
	}

	public double getManualRechargeTotal() {
		return manualRechargeTotal;
	}

	public void setManualRechargeTotal(double manualRechargeTotal) {
		this.manualRechargeTotal = manualRechargeTotal;
	}

	public double getFeeTotal() {
		return feeTotal;
	}

	public void setFeeTotal(double feeTotal) {
		this.feeTotal = feeTotal;
	}

	public double getRechargeFee() {
		return rechargeFee;
	}

	public void setRechargeFee(double rechargeFee) {
		this.rechargeFee = rechargeFee;
	}

	public double getCashFee() {
		return cashFee;
	}

	public void setCashFee(double cashFee) {
		this.cashFee = cashFee;
	}

	public double getMostAmount() {
		return mostAmount;
	}

	public void setMostAmount(double mostAmount) {
		this.mostAmount = mostAmount;
	}

	public double getLeastAmount() {
		return leastAmount;
	}

	public void setLeastAmount(double leastAmount) {
		this.leastAmount = leastAmount;
	}

	public double getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(double useAmount) {
		this.useAmount = useAmount;
	}

	public double getInvestInterest() {
		return investInterest;
	}

	public void setInvestInterest(double investInterest) {
		this.investInterest = investInterest;
	}

	public double getBorrowInterest() {
		return borrowInterest;
	}

	public void setBorrowInterest(double borrowInterest) {
		this.borrowInterest = borrowInterest;
	}

	public double getAccountOwnMoney() {
		return accountOwnMoney;
	}

	public void setAccountOwnMoney(double accountOwnMoney) {
		this.accountOwnMoney = accountOwnMoney;
	}

	public double getRepayTotal() {
		return repayTotal;
	}

	public void setRepayTotal(double repayTotal) {
		this.repayTotal = repayTotal;
	}

	public double getRepayInterest() {
		return repayInterest;
	}

	public void setRepayInterest(double repayInterest) {
		this.repayInterest = repayInterest;
	}

	public int getWaitRepayTimes() {
		return waitRepayTimes;
	}

	public void setWaitRepayTimes(int waitRepayTimes) {
		this.waitRepayTimes = waitRepayTimes;
	}

	public int getWaitRepayCount() {
		return waitRepayCount;
	}

	public void setWaitRepayCount(int waitRepayCount) {
		this.waitRepayCount = waitRepayCount;
	}

	public int getHadRepayCount() {
		return hadRepayCount;
	}

	public void setHadRepayCount(int hadRepayCount) {
		this.hadRepayCount = hadRepayCount;
	}

	public int getHadDueRepayCount() {
		return hadDueRepayCount;
	}

	public void setHadDueRepayCount(int hadDueRepayCount) {
		this.hadDueRepayCount = hadDueRepayCount;
	}

	public int getWaitDueRepayCount() {
		return waitDueRepayCount;
	}

	public void setWaitDueRepayCount(int waitDueRepayCount) {
		this.waitDueRepayCount = waitDueRepayCount;
	}

	/**
	 * 获取repayAward
	 * 
	 * @return repayAward
	 */
	public double getRepayAward() {
		return repayAward;
	}

	/**
	 * 设置repayAward
	 * 
	 * @param repayAward 要设置的repayAward
	 */
	public void setRepayAward(double repayAward) {
		this.repayAward = repayAward;
	}

	/**
	 * 获取repayYesAward
	 * 
	 * @return repayYesAward
	 */
	public double getRepayYesAward() {
		return repayYesAward;
	}

	/**
	 * 设置repayYesAward
	 * 
	 * @param repayYesAward 要设置的repayYesAward
	 */
	public void setRepayYesAward(double repayYesAward) {
		this.repayYesAward = repayYesAward;
	}
	
}
