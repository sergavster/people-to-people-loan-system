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
 * 还款投资人收到还款本金log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowRepayTenderCapitalLog extends BaseBorrowLog{

	private static final long serialVersionUID = 1879381929093016631L;
	private String sumLogRemarkTemplate = "回款合计：回款本金${money}元，标ID：[${borrow.id}]。";	
	private String templateNid=Constant.CAPITAL_COLLECT;
	public BorrowRepayTenderCapitalLog() {
		super();
	}
	public BorrowRepayTenderCapitalLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowRepayTenderCapitalLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(0, this.getMoney(), 0, -this.getMoney(), this.getUser_id());
	}
	
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void accountSumProperty() {
		accountSumDao.editSumByProperty(EnumAccountSumProperty.HUIKUAN.getValue(), this.getMoney(), this.getUser_id());
		
		Map tranData = transfer();
		BorrowModel borrow = (BorrowModel)(tranData.get("borrow"));
		
		// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 start
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.HUIKUAN_CONF.getValue()));
		String borrowTypeOfAutoUse = rule.getValueStrByKey("borrowTypeOfAutoUse");//还款时自动消耗掉回款的标种
		if (!StringUtils.isBlank(borrowTypeOfAutoUse) && borrowTypeOfAutoUse.contains(borrow.getType()+"")){
		// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 end
		//accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_HUIKUAN.getValue(), this.getMoney(), this.getUser_id());
			this.huikuanUpdate(this.getMoney());//如果秒标和净值标，将立马减去获得的回款
		}
	}
	@Override
	public void addAccountSumLog() {
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getHuikuan());
		double afterMoney = sum.getHuikuan() + this.getMoney();
		sumLog.setAfter_money(afterMoney);
		sumLog.setType(Constant.HUIKUAN_CAPITAL);
		sumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(sumLog);
		
		/*Map tranData = transfer();
		BorrowModel borrow = (BorrowModel)(tranData.get("borrow"));
		//if(borrow.getIs_mb()==1 || borrow.getIs_jin() == 1){
		// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 start
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.HUIKUAN_CONF.getValue()));
		String borrowTypeOfAutoUse = rule.getValueStrByKey("borrowTypeOfAutoUse");//还款时自动消耗掉回款的标种
		if (!StringUtils.isBlank(borrowTypeOfAutoUse) && borrowTypeOfAutoUse.contains(borrow.getType()+"")){
		//if(borrow.getType()==Constant.TYPE_SECOND || borrow.getType() ==Constant.TYPE_PROPERTY){
		// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 end
			AccountSum usedHuikuanSum = accountSumDao.getAccountSum(this.getUser_id());
			this.setAccountSumLogRemarkTemplate(usedHuikuanRemark);
			AccountSumLog usedHuikuanSumLog = this.baseAccountSumLog();
			usedHuikuanSumLog.setBefore_money( usedHuikuanSum.getUsed_huikuan());
			usedHuikuanSumLog.setMoney(this.getMoney());
			afterMoney = usedHuikuanSum.getUsed_huikuan() + this.getMoney();
			usedHuikuanSumLog.setAfter_money(afterMoney);
			Global.setTransfer("user_cash", this.getMoney());
			usedHuikuanSumLog.setRemark(this.getAccountSumLogRemark());
			usedHuikuanSumLog.setType(EnumAccountSumProperty.USED_HUIKUAN.getValue());
			accountSumLogDao.addAccountSumLog(usedHuikuanSumLog);
		}*/
	}
	
	
}
