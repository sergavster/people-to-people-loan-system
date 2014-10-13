package com.p2psys.model.accountlog;
//RDPROJECT-282 fxx 2013-10-18 start
//新增类
//RDPROJECT-282 fxx 2013-10-18 end
import java.util.Calendar;
import java.util.Date;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.AccountLogDao;
import com.p2psys.dao.TenderDao;
import com.p2psys.dao.UserCacheDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.domain.Tender;
import com.p2psys.domain.UserCache;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.model.rule.ManageFeeRuleModel;
import com.p2psys.model.rule.TenderRule;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;

/**
 * 累积的投标金额计算利息管理费并优惠利息管理费
 
 *
 */
public class DiscountInterestLog extends  InterestLog{
	
	private static final long serialVersionUID = -2744073939807182501L;
	
//	protected AccountLogDao accountLogDao;
//	protected TenderDao tenderDao;
//	protected UserCacheDao userCacheDao;
//	protected AccountDao accountDao;

	protected double interest;
	protected Tender tender;
	protected BorrowModel model;
	
	private String sumLogRemarkTemplate = "回款合计：利息管理费返还，获得回款利息${money}元。";
	
	protected String templateNid = Constant.BACK_MANAGE_FEE_LMTA;
	
	public DiscountInterestLog() {
		super();
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			accountLogDao=(AccountLogDao)ctx.getBean("accountLogDao");
			accountDao=(AccountDao)ctx.getBean("accountDao");
			tenderDao=(TenderDao)ctx.getBean("tenderDao");
			userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
		}
	}
	
	public DiscountInterestLog(BorrowModel model,Tender tender,double interest){
		this();
		this.interest=interest;
		this.tender=tender;
		this.model=model;
	}
	
	public void doEvent(){
		//UserCache uc = userCacheDao.getUserCacheByUserid(tender.getUser_id());
		int vip_status = userCacheDao.getUserVipStatus(tender.getUser_id());
		double borrow_fee_precent;
		if (vip_status == 1 && !StringUtils.isBlank(Global.getString("vip_borrow_fee"))) {
			borrow_fee_precent = Global.getDouble("vip_borrow_fee");
		} else {
			borrow_fee_precent = Global.getDouble("borrow_fee");
		}
		
		double borrow_fee=borrow_fee_precent*interest;
		double discount_fee=0;
		double lastMontTenderAccount=tenderAccount(tender.getUser_id());
		TenderRule rule=discountRate(tender.getUser_id(),lastMontTenderAccount);
		if(rule!=null){
			double new_borrow_fee=borrow_fee*rule.getRate(); //优惠后
			discount_fee=borrow_fee-new_borrow_fee; //优惠幅度
			borrow_fee=new_borrow_fee; 
		}
		
		if(discount_fee>=0.01){
			accountDao.updateAccount(discount_fee, discount_fee, 0, 0, tender.getUser_id());
			Account cAct=accountDao.getAccount(tender.getUser_id());
			AccountLog cLog=new AccountLog(tender.getUser_id(),this.getType(),1,DateUtils.getNowTimeStr(),"");
			fillAccountLog(cLog,cAct,discount_fee);
			Global.setTransfer("money", discount_fee);
			Global.setTransfer("borrow", model);
			cLog.setRemark(this.getLogRemark());
			accountLogDao.addAccountLog(cLog);
			Global.setTransfer("manage_fee", borrow_fee);
			this.accountLog(discount_fee,cAct);
			this.accountSumProperty();
		}
	}
	
	public double tenderAccount(long user_id){
		ManageFeeRuleModel model=ManageFeeRuleModel.instance();
		double tenderAccount=0.0;
		if(model.getStatus()==1){
			Calendar cal=Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date endDate=cal.getTime();
			Date startDate=DateUtils.rollMon(endDate, -1);
			tenderAccount=tenderDao.countTenderAccountByUserid(user_id,startDate.getTime()/1000, endDate.getTime()/1000, model.getRuleSql());
		}
		return tenderAccount;
	}
	
	public TenderRule discountRate(long userid,double tenderAccount){
		ManageFeeRuleModel model=ManageFeeRuleModel.instance();
		TenderRule rule=null;
		if(model.getStatus()==1){
			rule=model.getTenderRate(tenderAccount);
		}
		return rule;
	}
	
	
	protected void fillAccountLog(AccountLog log,Account act,double operateValue){
		log.setMoney(operateValue);
		log.setTotal(act.getTotal());
		log.setUse_money(act.getUse_money());
		log.setNo_use_money(act.getNo_use_money());
		log.setCollection(act.getCollection());
	}
	
	
	@Override
	public void accountSumProperty() {
		//先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		
		double usedInterest = accountSum.getUsed_interest();
		double interestFee=accountSum.getInterest_fee();
		double huikuan = accountSum.getHuikuan();
		
		//增加account_sum_log日志
		//扣费的顺序是充值、奖励、利息、借款
		double money = this.getMoney();
		

		long user_id = this.getUser_id();
		long to_user_id = this.getTo_user();
		String addtime = String.valueOf(DateUtils.getTime(new Date()));
		String addip = "";
		
		
		//增加interest sum log使用日志
		
		//退回利息管理费会增加两条sum_log，一条是减少已用利息，一条是减少利息管理费，
		
		//减少interest_fee项
		double interestfee_before_money = interestFee;
		double interestfee_money = -money;
		double interestfee_after_money = interestFee + interestfee_money;
		
		AccountSumLog interestFeeAccountSumLog = new AccountSumLog();
		interestFeeAccountSumLog.setUser_id(user_id);
		interestFeeAccountSumLog.setTo_user_id(to_user_id);
		interestFeeAccountSumLog.setBefore_money(interestfee_before_money);
		interestFeeAccountSumLog.setMoney(interestfee_money);
		interestFeeAccountSumLog.setAfter_money(interestfee_after_money);
		interestFeeAccountSumLog.setAddip(addip);
		interestFeeAccountSumLog.setAddtime(addtime);
		interestFeeAccountSumLog.setType(EnumAccountSumProperty.INTEREST_FEE.getValue());
		this.setAccountSumLogRemarkTemplate(interestFeeRemark);
		Global.setTransfer("money", interestfee_money);
		interestFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(interestFeeAccountSumLog);
		accountSumDao.editSumByProperty(EnumAccountSumProperty.INTEREST_FEE.getValue(), interestfee_money, this.getUser_id());
		
		//减少used interest项
		double interest_before_money = usedInterest;
		double interest_money = - money;
		double interest_after_money = usedInterest + interest_money;
		AccountSumLog interestAccountSumLog = new AccountSumLog();
		interestAccountSumLog.setUser_id(user_id);
		interestAccountSumLog.setTo_user_id(to_user_id);
		interestAccountSumLog.setBefore_money(interest_before_money);
		interestAccountSumLog.setMoney(interest_money);
		interestAccountSumLog.setAfter_money(interest_after_money);
		interestAccountSumLog.setAddip(addip);
		interestAccountSumLog.setAddtime(addtime);
		interestAccountSumLog.setType(EnumAccountSumProperty.USED_INTEREST.getValue());
		this.setAccountSumLogRemarkTemplate(usedInterestRemark);
		Global.setTransfer("user_cash", interest_money);
		interestAccountSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(interestAccountSumLog);
		accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_INTEREST.getValue(), interest_money, this.getUser_id());
		
		//增加回款金额
		double huikuan_before_money = huikuan;
		double huikuan_money = money;
		double huikuan_after_money = huikuan + huikuan_money;
		Global.setTransfer("user_cash", interest_money);
		AccountSumLog huikuanAccountSumLog = new AccountSumLog();
		huikuanAccountSumLog.setUser_id(user_id);
		huikuanAccountSumLog.setTo_user_id(to_user_id);
		huikuanAccountSumLog.setBefore_money(huikuan_before_money);
		huikuanAccountSumLog.setMoney(huikuan_money);
		huikuanAccountSumLog.setAfter_money(huikuan_after_money);
		huikuanAccountSumLog.setAddip(addip);
		huikuanAccountSumLog.setAddtime(addtime);
		huikuanAccountSumLog.setType(Constant.BACK_HUIKUAN_INTEREST);
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		Global.setTransfer("money", huikuan_money);
		huikuanAccountSumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.addAccountSumLog(huikuanAccountSumLog);
		accountSumDao.editSumByProperty(EnumAccountSumProperty.HUIKUAN_INTEREST.getValue(), huikuan_money, this.getUser_id());
	
	}
	
	@Override
	public String getAccountSumLogRemarkTemplate() {
		return this.sumLogRemarkTemplate;
	}
	
	public void accountLog(double money,Account act) {
		this.setMoney(money);
		this.setTotal(act.getTotal());
		this.setUse_money(act.getUse_money());
		this.setNo_use_money(act.getNo_use_money());
		this.setCollection(act.getCollection());
		this.setUser_id(act.getUser_id());
		this.setAddtime(DateUtils.getNowTimeStr());
		this.setTo_user(1L);
	}
	
	public String getTemplateNid(){
		return templateNid;
	}
}
