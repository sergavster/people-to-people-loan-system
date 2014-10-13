package com.p2psys.model.borrow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.BorrowDao;
import com.p2psys.dao.RepaymentDao;
import com.p2psys.dao.TenderDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Collection;
import com.p2psys.domain.Repayment;
import com.p2psys.domain.Tender;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.CreditGive;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.award.AwardTenderAwardLog;
import com.p2psys.model.accountlog.borrow.TenderFlowBuySuccLog;
import com.p2psys.model.accountlog.borrow.tender.FlowBorrowDecuctFreezeLog;
import com.p2psys.model.accountlog.borrow.tender.FlowBorrowSuccessLog;
import com.p2psys.model.accountlog.borrow.tender.FlowBorrowWaitInterestLog;
import com.p2psys.model.accountlog.deduct.DeductAwardLog;
import com.p2psys.model.award.FirstInviteAward;
import com.p2psys.model.award.FirstTenderAward;
import com.p2psys.model.award.InviteInvestAward;
import com.p2psys.model.award.PrecentInvestAward;
import com.p2psys.tool.interest.EndInterestCalculator;
import com.p2psys.tool.interest.InterestCalculator;
import com.p2psys.tool.interest.MonthEqualCalculator;
import com.p2psys.tool.interest.MonthInterest;
import com.p2psys.tool.interest.MonthInterestCalculator;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;


/**
 * 流转标
 * 
 
 * @date 2012-11-2 下午4:27:51
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class FlowBorrowModel extends BaseBorrowModel {
	
	private static final long serialVersionUID = 7375703874958748525L;

	private BorrowModel model;
	
	private RepaymentDao repaymentDao;

	public FlowBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setType(Constant.TYPE_FLOW);
		this.model.setFlow_status(0);
		this.model.setBorrowType(Constant.TYPE_FLOW);
		this.model.setNeedBorrowFee(false);
		init();
	}

	@Override
	public double calculateInterest() {
		InterestCalculator ic= interestCalculator();
		double interest=ic.getMoneyPerMonth()*ic.getPeriod()-getModel().getFlow_money()*getModel().getFlow_count();
		return interest;
	}

	@Override
	public InterestCalculator interestCalculator() {
		BorrowModel model=getModel();
		double account=model.getFlow_money()*model.getFlow_count();
		InterestCalculator ic=interestCalculator(account);
		return ic;
	}

	@Override
	public InterestCalculator interestCalculator(double validAccount) {
		BorrowModel model=getModel();
		InterestCalculator ic=null;
		double apr=model.getApr()/100;
		if(model.getIsday()==1){
			int time_limit_day=model.getTime_limit_day();
			ic =new EndInterestCalculator(validAccount,apr,time_limit_day);
		}else if(StringUtils.isNull(model.getStyle()).equals("3")){
			int time_limit=NumberUtils.getInt(model.getTime_limit());
			ic =new MonthInterestCalculator(validAccount,apr,time_limit);
		}else if(StringUtils.isNull(model.getStyle()).equals("2")){
			int period=NumberUtils.getInt(model.getTime_limit());
			ic =new EndInterestCalculator(validAccount,apr,period,InterestCalculator.TYPE_MONTH_END);
		}else{
			int time_limit=NumberUtils.getInt(model.getTime_limit());
			ic =new MonthEqualCalculator(validAccount,apr,time_limit);
		}
		ic.each();
		return ic;
	}

	// v1.6.7.1 RDPROJECT-461 wcw 2013-11-21 start
	public boolean allowFullSuccess() {
		return true;
	}
	// v1.6.7.1 RDPROJECT-461 wcw 2013-11-21 end

	public void prepareTender(Tender tender) {
		tender.setMoney(Math.floor(NumberUtils.getDouble(tender.getMoney()) / model.getFlow_money()) * model.getFlow_money() + "");
	}
	
	@Override
	public double validAccount(Tender tender) {
		double validAccount = super.validAccount(tender);
		double mostSingleLimit = model.getMost_single_limit();
		if (mostSingleLimit > 0 && validAccount > mostSingleLimit) {
			double one_flow_money = NumberUtils.getDouble(StringUtils.isNull(model.getFlow_money()));
			int flow_count = (int) (mostSingleLimit / one_flow_money);
			double real_flow_money = one_flow_money * flow_count;
			validAccount = real_flow_money;
		}
		return validAccount;
	}
	
	@Override
	public void checkTender(Tender tender) {
		super.checkTender(tender);
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		BorrowDao borrowDao = (BorrowDao)ctx.getBean("borrowDao");
		BorrowModel model = borrowDao.getBorrowById(tender.getBorrow_id());
		
		// 流转标流转次数限制
        int flow_count = ((Double)(NumberUtils.getDouble(tender.getAccount())/model.getFlow_money())).intValue();
        if(model.getFlow_time()>0 && (model.getFlow_totalyescount() + flow_count) > model.getFlow_time()*model.getFlow_count()){
            throw new BorrowException("投标失败！超过流转标可流转次数，还剩"+ ((model.getFlow_time()*model.getFlow_count())-model.getFlow_totalyescount())+"份可以投标！");
        }
        model.setFlow_totalyescount(model.getFlow_totalyescount() + flow_count);
        int count = borrowDao.updateBorrowFlowTime(model);
        if(count<1){
            throw new BorrowException("投标失败！超过流转标可流转次数，还剩"+ ((model.getFlow_time()*model.getFlow_count())-model.getFlow_totalyescount())+"份可以投标！");
        }
	}

	@Override
	public List<Collection> createCollectionList(Tender tender,InterestCalculator ic) {
		List<Collection> collectionlist = super.createCollectionList(tender, ic);
		if(collectionlist!=null && collectionlist.size()>0){
			//流转标投标的时候就生成还款奖励计划
			Collection lastC = collectionlist.get(collectionlist.size() - 1);
			lastC.setRepay_award(Double.parseDouble(tender.getAccount()) * model.getLate_award() / 100);
		}
		return collectionlist;
	}
	
	@Override
	public void tenderSuccess(Tender tender,InterestCalculator ic) {
		// 总共需要还款金额
        double repayment_account = ic.getTotalAccount();
        double repayment_interest = repayment_account - NumberUtils.getDouble(tender.getAccount());
        tender.setRepayment_account(NumberUtils.format6(repayment_account) + "");
        tender.setInterest(NumberUtils.format6(repayment_interest) + "");

        WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		tender.setWait_account(tender.getRepayment_account());
		tender.setWait_interest(tender.getInterest());
		tender.setStatus(1);
		TenderDao tenderDao=(TenderDao)ctx.getBean("tenderDao");
        tenderDao.modifyTender(tender);
	}

	/**
	 * 利息生息的执行过程
	 */
	@Override
	public void immediateInterestAfterTender(Tender tender) {
		// 扣除冻结/生产待收本金
		double validAccount = NumberUtils.getDouble(tender.getAccount());
		Account act = new Account(tender.getUser_id());
		Account actBorrow = new Account(model.getUser_id());
		BaseAccountLog flowDeductFreezeLog = new FlowBorrowDecuctFreezeLog(validAccount, act, model.getUser_id());
		flowDeductFreezeLog.doEvent();
        
        // 生产待收利息
        double interest = calculateInterest(validAccount);
        BaseAccountLog waitInterestLog=new FlowBorrowWaitInterestLog(interest, act, model.getUser_id());
        waitInterestLog.doEvent();
        // 借款人资金入账
        BaseAccountLog borrowSuccessLog=new FlowBorrowSuccessLog(validAccount, actBorrow, tender.getUser_id());
        borrowSuccessLog.doEvent();
        // 投标奖励 
        double awardValue =  calculateAward(validAccount);
        if (awardValue > 0) {
        	// 扣除借款人奖励
        	Global.setTransfer("award", awardValue);
            BaseAccountLog bLog = new DeductAwardLog(awardValue, actBorrow, tender.getUser_id());
            bLog.doEvent();
            // 给予投资人奖励
        	BaseAccountLog getAwardLog = new AwardTenderAwardLog(awardValue, act, model.getUser_id());
            getAwardLog.doEvent();
            
        }
        // 被邀请人投标获得的奖励
        InviteInvestAward inviter_award = new InviteInvestAward(tender);
        inviter_award.giveAward();
        // 邀请奖励（最开始是只有百分比发放的形式，现在有现金，也有根据投资总额、待收总额）
        PrecentInvestAward precent_award = new PrecentInvestAward(tender);
        precent_award.giveAward();
        // 首次邀请投标奖励（有邀请人）
        FirstInviteAward first_invest_award = new FirstInviteAward(tender);
        first_invest_award.giveAward();
        // 第一次投标奖励（无邀请人）
        FirstTenderAward first_tender_award = new FirstTenderAward(tender);
        first_tender_award.giveAward();
        
        CreditGive creditModel = new CreditGive(model,tender);
        creditModel.giveCredit();
        
        // 流转标投标后向repayment表插入数据，产生待还记录
        List<Repayment> list=createFlowRepaymentList(collectionList);
        WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
        repaymentDao=(RepaymentDao)ctx.getBean("repaymentDao");
        repaymentDao.addBatchRepayment(list.toArray(new Repayment[]{}));
        
        BaseAccountLog tlog=new TenderFlowBuySuccLog(0, act,tender.getUser_id());
        tlog.doEvent();
	}

	@Override
	public void immediateInviteAwardAfterTender() {
		//InviteInvestAward award=
	}
	
	

	
}
