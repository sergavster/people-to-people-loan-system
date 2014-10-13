package com.p2psys.model.accountlog.borrow.repay;

import java.util.Map;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.RuleModel;
import com.p2psys.model.accountlog.BaseBorrowLog;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.util.StringUtils;
/**
 * 还款投资人收到还款利息log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowRepayTenderInterestLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	
	private String interestSumLogRemarkTemplate = "利息合计：获得利息${money}元，标ID：[${borrow.id}]";
	
	private String sumLogRemarkTemplate = "回款合计：收到还款利息${money-borrowFee}元，标ID：[${borrow.id}]";	
	
	private String templateNid=Constant.INTEREST_COLLECT;
	
	public BorrowRepayTenderInterestLog() {
		super();
	}
	public BorrowRepayTenderInterestLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowRepayTenderInterestLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(0, this.getMoney(), 0, -this.getMoney(), this.getUser_id());
	}
	
	@Override
	public void accountSumProperty() {
		Map tranData = transfer();
		double borrowFee = Double.parseDouble(tranData.get("borrowFee").toString());
		double realHuikuan = this.getMoney() - borrowFee;
		
		accountSumDao.editSumByProperty(EnumAccountSumProperty.HUIKUAN_INTEREST.getValue(), realHuikuan, this.getUser_id());
		accountSumDao.editSumByProperty(EnumAccountSumProperty.INTEREST.getValue(), this.getMoney(), this.getUser_id());
		
		//V1.6.6.2 RDPROJECT-408 liukun 2013-10-31 start
		BorrowModel borrow = (BorrowModel)(tranData.get("borrow"));
		//if(borrow.getIs_mb()==1 || borrow.getIs_jin() == 1){
		// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 start
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.HUIKUAN_CONF.getValue()));
		String borrowTypeOfAutoUse = rule.getValueStrByKey("borrowTypeOfAutoUse");//还款时自动消耗掉回款的标种
		if (!StringUtils.isBlank(borrowTypeOfAutoUse) && borrowTypeOfAutoUse.contains(borrow.getType()+"")){
		//if(borrow.getType()==Constant.TYPE_SECOND || borrow.getType() ==Constant.TYPE_PROPERTY){
		// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 end
			//accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue(), realHuikuan, this.getUser_id());
			this.huikuanInterestUpdate(realHuikuan);
		}
		//V1.6.6.2 RDPROJECT-408 liukun 2013-10-31 end
	} 
	
	
	@Override
	public void addAccountSumLog() {
		Map tranData = transfer();
		double borrowFee = Double.parseDouble(tranData.get("borrowFee").toString());
		double realHuikuan = this.getMoney() - borrowFee;
		
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getHuikuan());
		sumLog.setMoney(realHuikuan);
		double afterMoney = sum.getHuikuan() + realHuikuan;
		sumLog.setAfter_money(afterMoney);
		sumLog.setType(Constant.HUIKUAN_INTEREST);
		sumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(sumLog);
		
		AccountSum interestSum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(interestSumLogRemarkTemplate);
		AccountSumLog interestSumLog = this.baseAccountSumLog();
		interestSumLog.setBefore_money( interestSum.getInterest());
		//V1.6.6.2 RDPROJECT-408 liukun 2013-10-31 start
		interestSumLog.setMoney(this.getMoney());
		//V1.6.6.2 RDPROJECT-408 liukun 2013-10-31 end
		afterMoney = interestSum.getInterest() + this.getMoney();
		interestSumLog.setAfter_money(afterMoney);
		interestSumLog.setType(EnumAccountSumProperty.INTEREST.getValue());
		interestSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(interestSumLog);
		
		/*//V1.6.6.2 RDPROJECT-408 liukun 2013-10-31 start
		BorrowModel borrow = (BorrowModel)(tranData.get("borrow"));
		//if(borrow.getIs_mb()==1 || borrow.getIs_jin() == 1){
		// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 start
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.HUIKUAN_CONF.getValue()));
		String borrowTypeOfAutoUse = rule.getValueStrByKey("borrowTypeOfAutoUse");//还款时自动消耗掉回款的标种
		if (!StringUtils.isBlank(borrowTypeOfAutoUse) && borrowTypeOfAutoUse.contains(borrow.getType()+"")){
		//if(borrow.getType()==Constant.TYPE_SECOND || borrow.getType() ==Constant.TYPE_PROPERTY){
		// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 end
			AccountSum usedHuikuanSum = accountSumDao.getAccountSum(this.getUser_id());
			
			this.setAccountSumLogRemarkTemplate(usedHuikuanInterestRemark);
			AccountSumLog usedHuikuanSumLog = this.baseAccountSumLog();
			usedHuikuanSumLog.setBefore_money( usedHuikuanSum.getUsed_huikuan());
			usedHuikuanSumLog.setMoney(realHuikuan);
			afterMoney = usedHuikuanSum.getUsed_huikuan() + realHuikuan;
			usedHuikuanSumLog.setAfter_money(afterMoney);
			Global.setTransfer("user_cash", realHuikuan);
			usedHuikuanSumLog.setRemark(this.getAccountSumLogRemark());
			usedHuikuanSumLog.setType(EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue());
			accountSumLogDao.addAccountSumLog(usedHuikuanSumLog);
		}
		//V1.6.6.2 RDPROJECT-408 liukun 2013-10-31 end
*/	}
 
}
