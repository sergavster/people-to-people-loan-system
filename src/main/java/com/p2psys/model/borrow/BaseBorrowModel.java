package com.p2psys.model.borrow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.BorrowDao;
import com.p2psys.dao.CollectionDao;
import com.p2psys.dao.RepaymentDao;
import com.p2psys.dao.TenderDao;
import com.p2psys.dao.UserCacheDao;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.BorrowConfig;
import com.p2psys.domain.Collection;
import com.p2psys.domain.Repayment;
import com.p2psys.domain.Rule;
import com.p2psys.domain.Tender;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.CreditGive;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCacheModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.DiscountInterestLog;
import com.p2psys.model.accountlog.InterestLog;
import com.p2psys.model.accountlog.award.AwardRepayLog;
import com.p2psys.model.accountlog.award.AwardTenderAwardLog;
import com.p2psys.model.accountlog.borrow.BackManageFeeVipLog;
import com.p2psys.model.accountlog.borrow.BorrowerRepaySuccLog;
import com.p2psys.model.accountlog.borrow.TenderRepaySuccLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepayCapitalLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepayExtensionInterestLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepayInterestLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepayLateInterestLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepaySystemLateInterestLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepayTenderCapitalLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepayTenderExtensionInterestLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepayTenderInterestLog;
import com.p2psys.model.accountlog.borrow.repay.BorrowRepayTenderLateInterestLog;
import com.p2psys.model.accountlog.borrow.repay.FlowBorrowRepayCapitalLog;
import com.p2psys.model.accountlog.borrow.repay.FlowBorrowRepayInterestLog;
import com.p2psys.model.accountlog.borrow.repay.FlowBorrowRepayTenderCapitalLog;
import com.p2psys.model.accountlog.borrow.verifyfullBorrow.BorrowDecuctFreezeLog;
import com.p2psys.model.accountlog.borrow.verifyfullBorrow.BorrowFailLog;
import com.p2psys.model.accountlog.borrow.verifyfullBorrow.BorrowSuccessLog;
import com.p2psys.model.accountlog.borrow.verifyfullBorrow.BorrowWaitInterestLog;
import com.p2psys.model.accountlog.deduct.DeductAwardLog;
import com.p2psys.model.accountlog.deduct.DeductManageFeeLog;
import com.p2psys.model.award.FirstInviteAward;
import com.p2psys.model.award.FirstTenderAward;
import com.p2psys.model.award.InviteInvestAward;
import com.p2psys.model.award.PrecentInvestAward;
import com.p2psys.model.award.PromoteAward;
import com.p2psys.model.fee.BackManageFeeByUserLevel;
import com.p2psys.tool.Page;
import com.p2psys.tool.interest.EndInterestCalculator;
import com.p2psys.tool.interest.FullverifyAndMonthCalculator;
import com.p2psys.tool.interest.InterestCalculator;
import com.p2psys.tool.interest.MonthEqualCalculator;
import com.p2psys.tool.interest.MonthInterest;
import com.p2psys.tool.interest.MonthInterestCalculator;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

public class BaseBorrowModel extends BorrowModel  {

	private static final long serialVersionUID = 5486891425298146179L;
	
	private final static Logger logger = Logger.getLogger(BaseBorrowModel.class);

	private BorrowModel model;
	
	public BaseBorrowModel(BorrowModel model) {
		this.model=model;
		borrowType=Constant.TYPE_ALL;
		init();
	}
	public void init(){
		model.setConfig(Global.getBorrowConfig(model.getBorrowType()));
		if(model.getParam()==null){
			model.setParam(new SearchParam(model.getUse(),model.getTime_limit(),model.getSearchkeywords()));
		}
	}
	
	@Override
	public int getBorrowType(){
		return model.getBorrowType();
	}
	
	public BorrowConfig getConfig(){
		return model.getConfig();
	}
	
	public boolean isTrial() {
		BorrowConfig config=model.getConfig();
		if(config.getIs_trail()==0){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean isReview() {
		BorrowConfig config=model.getConfig();
		if(config==null||config.getIs_review()==0){
			return false;
		}else{
			return true;
		}
	}

	
	
	public BorrowModel getModel() {
		return model;
	}
	public void setModel(BorrowModel model) {
		this.model = model;
	}
	@Override
	public String getTypeSql() {
		String typeSql="";
		typeSql=model.getTypeSql();
		return typeSql;
	}

	@Override
	public String getStatusSql() {
		String statusSql="";
		statusSql=model.getStatusSql();
		return statusSql;
	}
	/**
	 * 根据不同的排序获取SQL
	 * 
	 * @param order
	 * @return
	 */
	@Override
	public String getOrderSql() {
		String orderSql="";
		orderSql=getModel().getOrderSql();
		return orderSql;
	}

	@Override
	public String getSearchParamSql() {
		String searchSql="";
		searchSql=model.getSearchParamSql();
		return searchSql;
	}

	@Override
	public String getLimitSql() {
		String limitSql="";
		limitSql=model.getLimitSql();
		return limitSql;
	}
	
	/**
	 * 生产分页信息
	 * @param p
	 * @return
	 */
	public String getPageStr(Page p){
		String pageSql="";
		pageSql=model.getPageStr(p);
		return pageSql;
	}
	
	//拼装search参数
	public String getSerachStr(){
		String searchSql="";
		searchSql=model.getSerachStr();
		return searchSql;
	}
	
	//拼装排序参数
	public String getOrderStr(){
		String orderSql="";
		orderSql=model.getOrderStr();
		return orderSql;
	}
	/**
	 * 根据系统参数中的配置决定是否跳过初审
	 */
	@Override
	public void skipTrial(){
		if(this.isTrial()){
			int enableAutoTender=Global.getInt("enableAutoTender");
			if(enableAutoTender==1){
				//V1.6.7.1 RDPROJECT-345 liukun 2013-11-07 start
				model.setStatus(1);
				//必须配置自动投标规则
				// v1.6.7.2 改用FastJson xx 2013-12-12 start
				RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.AUTO_TENDER_CONF.getValue()));
				if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
					try {
						int xinEnable = rule.getValueIntByKey("xinEnable");
						int jinEnable = rule.getValueIntByKey("jinEnable");
						int fastEnable = rule.getValueIntByKey("fastEnable");
						int offvouchEnable = rule.getValueIntByKey("offvouchEnable");
						int flowEnable = rule.getValueIntByKey("flowEnable");
						
						//只有当两者都成立的时候继续往下走
						if(model.getType() == Constant.TYPE_CREDIT && xinEnable==1){
							model.setStatus(19);
						}else if(model.getType() == Constant.TYPE_PROPERTY && jinEnable==1){
							model.setStatus(19);
						}else if(model.getType() == Constant.TYPE_MORTGAGE && fastEnable==1){
							model.setStatus(19);
						}else if(model.getType() == Constant.TYPE_OFFVOUCH && offvouchEnable==1 ){
							model.setStatus(19);
						}else if(model.getType() == Constant.TYPE_FLOW && flowEnable==1){
							model.setStatus(19);
						}
					} catch (Exception e) {
						logger.error(e);
					}
				}
				// v1.6.7.2 改用FastJson xx 2013-12-12 end
				//V1.6.7.1 RDPROJECT-345 liukun 2013-11-07 end
			}else{
				model.setStatus(1);
			}
			model.setVerify_time(DateUtils.getNowTimeStr());
			model.setVerify_user(Constant.ADMIN_ID+"");
		}
	}
	/**
	 * 根据系统参数中的配置决定是否跳过复审
	 */
	@Override
	public void skipReview(){
		if ((isReview()) && (allowFullSuccess())){
			model.setStatus(3);
			model.setFull_verifytime(DateUtils.getNowTimeStr());
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			BorrowDao borrowDao = (BorrowDao)ctx.getBean("borrowDao");
            borrowDao.updateFullBorrow(model);
		}
	}
	
	@Override
	public void skipStatus() {
		model.setStatus(model.getStatus()+1);
	}
	@Override
	public void verify(int status, int verifyStatus) {
		if(verifyStatus==2){
			if(status==0) model.setStatus(2);
		}else if(verifyStatus==1){
			if(status==0){
				int enableAutoTender=Global.getInt("enableAutoTender");
				if(enableAutoTender==1){
					//V1.6.7.1 RDPROJECT-345 liukun 2013-11-07 start
					model.setStatus(1);
					//必须配置自动投标规则
					// v1.6.7.2 改用FastJson xx 2013-12-12 start
					RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.AUTO_TENDER_CONF.getValue()));
					if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
						try {
							int xinEnable = rule.getValueIntByKey("xinEnable");
							int jinEnable = rule.getValueIntByKey("jinEnable");
							int fastEnable = rule.getValueIntByKey("fastEnable");
							int offvouchEnable = rule.getValueIntByKey("offvouchEnable");
							int flowEnable = rule.getValueIntByKey("flowEnable");
							
							//只有当两者都成立的时候继续往下走
							if(model.getType() == Constant.TYPE_CREDIT && xinEnable==1){
								model.setStatus(19);
							}else if(model.getType() == Constant.TYPE_PROPERTY && jinEnable==1){
								model.setStatus(19);
							}else if(model.getType() == Constant.TYPE_MORTGAGE && fastEnable==1){
								model.setStatus(19);
							}else if(model.getType() == Constant.TYPE_OFFVOUCH && offvouchEnable==1 ){
								model.setStatus(19);
							}else if(model.getType() == Constant.TYPE_FLOW && flowEnable==1){
								model.setStatus(19);
							}
						} catch (Exception e) {
							logger.error(e);
						}
					}
					// v1.6.7.2 改用FastJson xx 2013-12-12 end
					
					/*if(model.getType() == Constant.TYPE_MORTGAGE || model.getType() == Constant.TYPE_CREDIT
							|| model.getType() == Constant.TYPE_OFFVOUCH || model.getType() == Constant.TYPE_PROPERTY){
						model.setStatus(19);
					}else{
						model.setStatus(1);
					}*/	 
					//V1.6.7.1 RDPROJECT-345 liukun 2013-11-07 end
				}else{
					model.setStatus(1);
				}
			}
		}else if(verifyStatus==3){
			if(status==1) model.setStatus(3);
		}else if(verifyStatus==4){
			if(status==1) model.setStatus(4);
		}else if(verifyStatus==5){
			model.setStatus(5);
		}
	}
	
	/**
	 * 计算借款标的利息
	 * @return
	 */
	@Override
	public double calculateInterest(){
		InterestCalculator ic= interestCalculator();
		double interest=ic.getMoneyPerMonth()*ic.getPeriod()-getModel().getAccount();
		return interest;
	}
	
	@Override
	public double calculateInterest(double validAccount) {
		InterestCalculator ic= interestCalculator(validAccount);
		double interest=0;
		if("3".equals(model.getStyle())){
			interest=ic.getMoneyPerMonth()*ic.getPeriod();
		}else{
			interest=ic.getMoneyPerMonth()*ic.getPeriod()-validAccount;
		}
		return interest;
	}
	@Override
	public InterestCalculator interestCalculator(){
		BorrowModel model=getModel();
		double account=model.getAccount();
		InterestCalculator ic=interestCalculator(account);
		return ic;
	}
	
	@Override
	public InterestCalculator interestCalculator(double validAccount){
		BorrowModel model=getModel();
		InterestCalculator ic=null;
		double apr=model.getApr()/100;
		// v1.6.7.2 RDPROJECT-526 xx start
		if(model.getIsday()==1){//天标,一次性还款
			int time_limit_day=model.getTime_limit_day();
			ic =new EndInterestCalculator(validAccount,apr,time_limit_day);
		}else if("2".equals(model.getStyle())){//一次性还款
			int time_limit=NumberUtils.getInt(model.getTime_limit());
			ic =new EndInterestCalculator(validAccount,apr,time_limit,InterestCalculator.TYPE_MONTH_END);
		}else if("3".equals(model.getStyle())){//每月还息到期还本
			int time_limit=NumberUtils.getInt(model.getTime_limit());
			ic =new MonthInterestCalculator(validAccount,apr,time_limit);
		}else if("5".equals(model.getStyle())){//满标还息每月还息到期还本
			int time_limit=NumberUtils.getInt(model.getTime_limit());
			ic =new FullverifyAndMonthCalculator(validAccount,apr,time_limit);
		}else{
			int time_limit=NumberUtils.getInt(model.getTime_limit());
			ic =new MonthEqualCalculator(validAccount,apr,time_limit);
		}
		// v1.6.7.2 RDPROJECT-526 xx end
		ic.each();
		return ic;
	}
	
	/**
	 * 计算借款标的手续费
	 * @return
	 */
	@Override
	public double calculateBorrowFee(){
		if(isNeedBorrowFee()){
			BorrowModel model=getModel();
			double account=model.getAccount();	
			return account*getBorrow_fee();
		}else{
			return 0.0;
		}
	}
	/**
	 * 计算奖励资金
	 */
	@Override
	public double calculateBorrowAward() {
		// v1.6.7.2 奖励金额加上还款时奖励 2013-12-09 start
		double awardValue = model.getAccount()*model.getLate_award()/100;
		double tenderAward = 0.0;
		if(model.getAward()==1){
			double account=model.getAccount();	
			tenderAward = model.getPart_account()/100*account;
		}else if(model.getAward()==2){
			tenderAward = model.getFunds();
		}
		return tenderAward + awardValue;
		// v1.6.7.2 奖励金额加上还款时奖励 2013-12-09 end
	}
	
	private boolean toBool(String identify){
		if(model.getConfig()==null||model.getConfig().getIdentify()==null){
			throw new BorrowException("该类借款标的配置参数不对！");
		}
		String cfgIdentify=model.getConfig().getIdentify();
		int i1=Integer.parseInt(identify, 2);
		int i2=Integer.parseInt(cfgIdentify, 2);
		int ret=i1&i2;
		if(ret>0) return true;
		return false;
	}
	
	public boolean isNeedRealName() {
		String is="100000";
		return toBool(is);
	}
	public boolean isNeedVIP() {
		String is="010000";
		return toBool(is);
	}
	public boolean isNeedEmail() {
		String is="001000";
		return toBool(is);
	}
	public boolean isNeedPhone() {
		String is="000100";
		return toBool(is);
	}
	public boolean isNeedVideo() {
		String is="000010";
		return toBool(is);
	}
	public boolean isNeedScene() {
		String is="000001";
		return toBool(is);
	}
	@Override
	public boolean checkIdentify(User u) {
		if(isNeedRealName()){
			//v1.6.7.2 RDPROJECT-616 sj 2013-12-20 start
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			if(u.getReal_status()!=1){throw new BorrowException("需要实名认证！","/member/index.html");}
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
		}
		if(isNeedVIP()){
			if(u.getVip_status()!=1){throw new BorrowException("需要VIP认证！","/member/index.html");}
		}
		if(isNeedEmail()){
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			if(u.getEmail_status()!=1){throw new BorrowException("需要邮箱认证！","/member/index.html");}
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
		}
		if(isNeedPhone()){
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			if(u.getPhone_status()!=1){throw new BorrowException("需要手机认证！","/member/index.html");}
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
		}
		if(isNeedVideo()){
			if(u.getVideo_status()!=1){throw new BorrowException("需要视频认证！","/member/index.html");}
		}
		if(isNeedScene()){
			if(u.getScene_status()!=1){throw new BorrowException("需要现场认证！","/member/index.html");}
		}
		//v1.6.7.2 RDPROJECT-616 sj 2013-12-20 end
		return true;
	}

	@Override
	public boolean checkModelData(RuleModel rule) {
		//v1.6.7.1 RDPROJECT-170 wcw 2013-11-21 start
		if("4".equals(model.getStyle())&&model.getIsday()==0){
			throw new BorrowException("该还款方式不支持月标");
		}
		//v1.6.7.1 RDPROJECT-170 wcw 2013-11-21 end
		BorrowModel model = getModel();
		RuleModel mdwBorrowCheckRule = rule.getRuleByKey("mdwBorrowCheck");
		int mdwBorrowCheckStatus = mdwBorrowCheckRule.getValueIntByKey("status");
		if (mdwBorrowCheckStatus == 1) {
			if (StringUtils.isNull(model.getBorrow_time()).equals("")) {
				throw new BorrowException("借款时间不能为空");
			}
			if (StringUtils.isNull(model.getBorrow_account()).equals("")) {
				throw new BorrowException("借款额度不能为空");
			}
			if (StringUtils.isNull(model.getBorrow_time_limit()).equals("")) {
				throw new BorrowException("借款周期不能为空");
			}
		}
		if (model.getAccount()  % 1.0 != 0) {
			throw new BorrowException("借贷总金额必须为整数");
		}

		if (StringUtils.isEmpty(model.getName())) {
			throw new BorrowException("标题不能为空");
		}
		RuleModel aprLimitRule = rule.getRuleByKey("aprLimit");
		int aprLimitStatus = aprLimitRule.getValueIntByKey("status");
		if (aprLimitStatus == 1) {
			BorrowConfig config = Global.getBorrowConfig(model.getBorrowType());
			if (config != null) {
				double lowest_apr = 0;
				if (config.getLowest_apr() != 0) {
					lowest_apr = config.getLowest_apr();
				} else {
					lowest_apr = 0;
				}
				double most_apr = 0;
				if (config.getMost_apr() != 0) {
					most_apr = config.getMost_apr();
				} else {
					most_apr = 22.4;
				}
				double apr = model.getApr();
				if (apr < lowest_apr) {
					throw new BorrowException("借款利率不能低于" + lowest_apr + "%");
				}
				if (apr > most_apr) {
					throw new BorrowException("借款利率不能高于" + most_apr + "%");
				}
			}
		} else {
			String minErrorMsg = "最低利率不能低于1%";
			String sixMonthErrorMsg = "六个月份以内(含六个月)的贷款年利率不能高于22.4%";
			String oneYearErrorMsg = "六个月至一年(含一年)的贷款年利率不能高于24%";
			String threeYearErrorMsg = "一年至三年(含三年)的贷款年利率不能高于24.6%";
			String fiveYearErrorMsg = "三年至五年(含五年)的贷款年利率不能高于25.6%";
			String moreYearErrorMsg = "五年以上的贷款年利率不能高于26.2%";

//			if (model.getApr() < 1 && model.getIs_donation() != 1) {
			if (model.getApr() < 1 && model.getType() == Constant.TYPE_DONATION) {
				throw new BorrowException(minErrorMsg);
			}

			// 秒标或者天标按照借款期限一个月来算，1-6月最高利率是5.6%*4
//			if (model.getIs_mb() == 1) {
			if (model.getType()  == Constant.TYPE_SECOND) {
				if (model.getApr() > 22.4) {
					throw new BorrowException(sixMonthErrorMsg);
				}
			} else {
				int time_limit = NumberUtils.getInt(model.getTime_limit());
				double apr = model.getApr();
				if (time_limit <= 6 && apr > 22.4) {
					throw new BorrowException(sixMonthErrorMsg);
				}
				if (time_limit <= 12 && time_limit > 6 && apr > 24) {
					throw new BorrowException(oneYearErrorMsg);
				}
				if (time_limit <= 36 && time_limit > 12 && apr > 24.6) {
					throw new BorrowException(threeYearErrorMsg);
				}
				if (time_limit <= 60 && time_limit > 36 && apr > 25.6) {
					throw new BorrowException(fiveYearErrorMsg);
				}
				if (time_limit > 60 && apr > 26.2) {
					throw new BorrowException(moreYearErrorMsg);
				}
			}
		}
		BorrowConfig config = Global.getBorrowConfig(model.getBorrowType());
		if (config != null) {
			double lowest_account = 0;
			if (config.getLowest_account() != 0) {
				lowest_account = config.getLowest_account();
			} else {
				lowest_account = 500;
			}
			double most_account = 0;
			if (config.getMost_account() != 0) {
				most_account = config.getMost_account();
			} else {
				most_account = 5000000;
			}
			double account =model.getAccount();
			if (account < lowest_account) {
				throw new BorrowException("借款金额不能低于" + lowest_account + "元");
			}
			if (account > most_account) {
				throw new BorrowException("借款金额不能高于" + most_account + "元");
			}
		}
		//v1.6.6.2 RDPROJECT-333 wcw  2013-10-22 start
		//最高单笔限制额度不能高于最高投标累计限额额度
		double most_account=NumberUtils.getDouble(StringUtils.isNull(model.getMost_account()));
		double most_single_limit=model.getMost_single_limit();
		if(most_account<most_single_limit&&most_account>0){
			 throw new BorrowException("最高单笔限额不能高于最高累计限额！！！");
		}
		//v1.6.6.2 RDPROJECT-333 wcw  2013-10-22 end
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Repayment[] getRepayment() {
		InterestCalculator ic=interestCalculator();
		List<MonthInterest> monthList = ic.getMonthList();
		// v1.6.7.2 RDPROJECT-526 xx start
		Repayment[] repays = new Repayment[monthList.size()];
		// v1.6.7.2 RDPROJECT-526 xx end
		int i = 0;
		for (MonthInterest mi : monthList) {
			Repayment repay=new Repayment();
			// v1.6.7.1 添加借款人用户ID xx 2013-11-11 start
			repay.setUser_id(model.getUser_id());
			// v1.6.7.1 添加借款人用户ID xx 2013-11-11 end
			repay.setBorrow_id(model.getId());
			repay.setOrder(i++);
			if(model.getType()!=Constant.TYPE_FLOW){
				repay.setRepayment_time(getRepayTime(repay.getOrder()).getTime()/1000+"");
			}else{
				repay.setRepayment_time(getFlowRepayTime(repay.getOrder()).getTime()/1000+"");
			}
			// v1.6.7.2 RDPROJECT-526 xx start
			if (!StringUtils.isBlank(model.getStyle())
					&& Integer.parseInt(model.getStyle()) == InterestCalculator.REPAY_MONADVANCE_INTEREST_END_CAPITAL
					&& i == 1) {
				repay.setWebstatus(1);
			}
			// v1.6.7.2 RDPROJECT-526 xx end
			double repayment_account=(mi.getAccountPerMon()+mi.getInterest());
			//repayment_account.substring(0,repayment_account.indexOf(".")+6);
			repay.setRepayment_account(NumberUtils.format6(repayment_account)+"");
			double repaymeng_interest=mi.getInterest();
			repay.setInterest(NumberUtils.format6(repaymeng_interest)+"");
			double repaymeng_accountPerMon=mi.getAccountPerMon();
			repay.setCapital(NumberUtils.format6(repaymeng_accountPerMon)+"");
			repay.setAddtime(DateUtils.getNowTimeStr());
			repays[i-1]=repay;
		}
		return repays;
	}
	@Override
	public Date getRepayTime(int period) {
		// v1.6.6.1 RDPROJECT-189  wcw 2013-10-08 start
		if(StringUtils.isBlank(model.getFull_verifytime())){
			model.setFull_verifytime(DateUtils.getNowTimeStr());
		}
		Date d = DateUtils.getDate(model.getFull_verifytime());
		// v1.6.6.1 RDPROJECT-189  wcw 2013-10-08 end
		Date repayDate=DateUtils.getLastSecIntegralTime(d);
		if (model.getType()  == Constant.TYPE_SECOND) {
			return d;
		}else if(model.getIsday()==1){
			repayDate=DateUtils.rollDay(repayDate, model.getTime_limit_day());
			return repayDate;
		}else{
			if("2".equals(model.getStyle())){//一次性还款
				repayDate=DateUtils.rollMon(repayDate, NumberUtils.getInt(model.getTime_limit()));
			} 
			// v1.6.7.2 RDPROJECT-526 xx start
			else if("5".equals(model.getStyle())){//满标还息每月还息到期还本
				if(period==0){
					repayDate=DateUtils.rollMinute(new Date(),5);//5分钟后还款
				} else {
					repayDate=DateUtils.rollMon(repayDate, period);
				}
			} 
			// v1.6.7.2 RDPROJECT-526 xx end
			else{
				repayDate=DateUtils.rollMon(repayDate, period+1);
			}
			return repayDate;
		}
	}
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
	public Date getAddExtensionRepayTime(Date repayDate){
		Rule rule = Global.getRule(EnumRuleNid.EXTENSION.getValue());
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			RuleModel extensionRule = new RuleModel(rule);
			int extension_enable = extensionRule.getValueIntByKey("extension_enable");
			if (extension_enable == 1) {
				repayDate = DateUtils.rollDay(repayDate, model.getExtension_day());
				return repayDate;
			}
			return repayDate;
		}
		return repayDate;
	}
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
	@Override
	public Date getFlowRepayTime(int period) {
		Date d = DateUtils.getDate(model.getFull_verifytime());
		Date repayDate=DateUtils.getLastSecIntegralTime(d);
		if (model.getType()  == Constant.TYPE_SECOND) {
			return d;
		}else if(model.getIsday()==1){
			repayDate=DateUtils.rollDay(repayDate, model.getTime_limit_day());
			return repayDate;
		}else{
			//一次性还款
			if(StringUtils.isNull(model.getStyle()).equals("2")){
				repayDate=DateUtils.rollMon(repayDate, NumberUtils.getInt(model.getTime_limit()));
			}else{
				repayDate=DateUtils.rollMon(repayDate, period+1);
			}
			return repayDate;
		}
	}
	
	@Override
	public boolean isLastPeriod(int order){
		// v1.6.7.2 RDPROJECT-526 xx start
		int time_limit = NumberUtils.getInt(model.getTime_limit());
		if (model.getType() == Constant.TYPE_SECOND || model.getIsday() == 1 || "2".equals(model.getStyle())) {
			return true;
		} else if("5".equals(model.getStyle())){//满标还息每月还息到期还本
			return (time_limit == order);
		} else {
			return (time_limit == (order + 1));
		}
		// v1.6.7.2 RDPROJECT-526 xx end
	}
	
	@Override
	public double getManageFee() {
		double fee=0.0;
		double account=model.getAccount();
		fee=getManageFee(account);
		return fee;
	}
	
	
	@Override
	public double getManageFee(double account) {
		double fee = 0.0;
		//v1.6.7.2 RDPROJECT-624 sj 2013-12-27 start
		RuleModel manageFeeRule = new RuleModel(Global.getRule(EnumRuleNid.BORROWMANAGEFEE.getValue()));
		if(manageFeeRule != null && manageFeeRule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
			//固定比例收取借款手续费
			int cal_style = manageFeeRule.getValueIntByKey("cal_style");
			if(cal_style == 1){
				BorrowConfig cfg = model.getConfig();
				if(cfg != null){
					if(model.getIsday() == 1){
						fee = account*cfg.getDaymanagefee()*model.getTime_limit_day()/3000;
					}else{
						fee = account*cfg.getManagefee()*NumberUtils.getInt(model.getTime_limit())/100;
					}	
				}
			}
			//不固定比例收取借款手续费
			if(cal_style == 2){
				RuleModel rule = manageFeeRule.getRuleByKey("month_rate");
				double rate = 0.0;
				if(rule != null){
					if(model.getIsday() == 1 || (model.getIsday() == 0 && NumberUtils.getInt(model.getTime_limit()) == 1)){
						//获得一个月的利率
						rate = NumberUtils.getDouble(rule.getValueStrByKey("1"));
						fee = account*rate;
					}else{
						//获得多个月的利率
						rate = NumberUtils.getDouble(rule.getValueStrByKey(model.getTime_limit()));
						fee = account*rate;
					}
				}
			}
		}
		//v1.6.7.2 RDPROJECT-624 sj 2013-12-27 end
		return fee;
	}
	
	@Override
	public double getTransactionFee() {
		return 0;
	}
	@Override
	public double calculateAward() {
		return calculateAward(model.getAccount());
	}
	@Override
	public double calculateAward(double account) {
		double awardValue=0;
		if(model.getAward()==1){// 按投标金额比例
			awardValue=account*model.getPart_account()/100;
		}else if(model.getAward()==2){// 按固定金额分摊奖励
			awardValue=model.getFunds()/model.getAccount()*account;
		}else{
			awardValue=0;
		}
		return awardValue;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Collection> createCollectionList(Tender tender,InterestCalculator ic) {
		List<Collection> collectList = new ArrayList<Collection>(); 
        List<MonthInterest> monthList = ic.getMonthList();
        // 拼装Collection对象 批量插入还款表
        int i = 0;
        double flow_repayment_account=0;
        for (MonthInterest mi : monthList) {
            Collection c = fillCollection(mi, tender, ic);
            c.setOrder(i++);
	        c.setRepay_time(calCollectionRepayTime(tender,i));
	        flow_repayment_account+=NumberUtils.getDouble(StringUtils.isNull(c.getRepay_account()));
	        model.setRepayment_account(flow_repayment_account);
	        model.setRepayment_yesaccount(0);
	        model.setRepayment_yesinterest(0);
            collectList.add(c);
        }
        // 修改账户资金
        this.collectionList=collectList;
		return collectList;
	}
	
	@Override
	public String calCollectionRepayTime(Tender tender, int period) {
		String repayTime="";
		if (model.getIsday() == 1) {
            repayTime = DateUtils.rollDay(tender.getAddtime(), model.getTime_limit_day() + "");
        } else {
            if("2".equals(model.getStyle())){
            	repayTime = DateUtils.rollMonth(tender.getAddtime(), model.getTime_limit());
            } else{
            	repayTime = DateUtils.rollMonth(tender.getAddtime(), period+"");
            }
        }
		return repayTime;
	}
	
	@Override
	public List<Repayment> createFlowRepaymentList(List<Collection> clist) {
		List<Repayment> repayList=new ArrayList<Repayment>(clist.size());
		for(Collection c:clist){
			Repayment repay = new Repayment();
	        repay.setUser_id(model.getUser_id());
	        repay.setBorrow_id(c.getBorrow_id());
	        repay.setOrder(c.getOrder());
	        repay.setStatus(0);
	        repay.setWebstatus(0);
	        repay.setRepayment_time(c.getRepay_time());
	        repay.setRepayment_account(c.getRepay_account());
	        repay.setInterest(c.getInterest());
	        repay.setCapital(c.getCapital());
	        repay.setAddtime(c.getAddtime());
	        repay.setTender_id(c.getTender_id());
	        repayList.add(repay);
		}
		this.repaymentList=repayList;
		return repayList;
	}
	
	private Collection fillCollection(MonthInterest mi, Tender t, InterestCalculator ic) {
        Collection c = new Collection();
        c.setBorrow_id(t.getBorrow_id());
        c.setTender_id(t.getId());
        // c.setRepay_time(repay_time) 何时写入数据库
        c.setInterest(mi.getInterest() + "");
        c.setCapital(mi.getAccountPerMon() + "");
        c.setRepay_account((mi.getInterest() + mi.getAccountPerMon()) + "");
        c.setAddtime(new Date().getTime() / 1000 + "");
        c.setSite_id(0);
        c.setStatus(0);
        c.setRepay_yesaccount("0");
        c.setLate_days(0);
        c.setLate_interest("0");
        c.setUser_id(t.getUser_id());
        //还的时候才会最终却VIP 手续费，这里就没必要进行区分VIP
        c.setManage_fee(mi.getInterest() * Global.getDouble("borrow_fee") + "");
        return c;
    }
	
	@Override
	public void prepareTender(Tender tender) {
		
	}
	@Override
	public double validAccount(Tender tender) {
		double validAccount = 0.0;
		double tenderAccount = NumberUtils.getDouble(tender.getMoney());
		double account_val = model.getAccount();
		double account_yes_val = model.getAccount_yes();

		if (tenderAccount + account_yes_val >= account_val) {
			validAccount = account_val - account_yes_val;
			skipReview();
		} else {
			validAccount = tenderAccount;
		}
		model.setAccount_yes(account_yes_val + validAccount);
		model.setTender_times(model.getTender_times() + 1);

		double lowestSingleLimit = model.getLowest_single_limit();
		double mostSingleLimit = model.getMost_single_limit();
		if (validAccount < lowestSingleLimit && lowestSingleLimit > 0) {
			if (account_val - account_yes_val < lowestSingleLimit) {
				if (account_val - account_yes_val < validAccount) {
					validAccount = account_val - account_yes_val;
				}
			} else {
				throw new BorrowException("投标金额不能少于单笔最小限额");
			}
		}
		if (mostSingleLimit > 0 && validAccount > mostSingleLimit) {
			validAccount = mostSingleLimit;
		}
		return validAccount;
	}
	
	
	@Override
	public void checkTender(Tender tender) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		UserDao userDao = (UserDao)ctx.getBean("userDao");
		AccountDao accountDao = (AccountDao)ctx.getBean("accountDao");
		UserCacheDao userCacheDao = (UserCacheDao)ctx.getBean("userCacheDao");
		BorrowDao borrowDao = (BorrowDao)ctx.getBean("borrowDao");
		TenderDao tenderDao = (TenderDao)ctx.getBean("tenderDao");
		
		User user = userDao.getUserById(tender.getUser_id());
		Account act = accountDao.getAccountByUserId(user.getUser_id());
		BorrowModel model = borrowDao.getBorrowById(tender.getBorrow_id());
		double account = NumberUtils.getDouble(tender.getAccount());
		
		//用户
		if(user.getIslock()==1){
			throw new BorrowException("您的账号当前处于锁定状态，投标失败！");
		}
		//资金
		if(account > act.getUse_money()){
			throw new BorrowException("您的可用余额不足，投标失败！ ");
		}
		double lowest_single_limit = model.getLowest_single_limit();
		if(lowest_single_limit > 0 && account < lowest_single_limit || account <= 0){
			throw new BorrowException("投标金额不能小于单笔最低投标金额，投标失败！ ");
		}
		double most_single_account_num = model.getMost_single_limit();
		if(most_single_account_num > 0 && account > most_single_account_num || account <= 0){
			throw new BorrowException("投标金额不能大于单笔最多投标总额，投标失败！ ");
		}
		double most_account_num = model.getMost_account();
        double hasTender = borrowDao.hasTenderTotalPerBorrowByUserid(model.getId(), tender.getUser_id());
        if (most_account_num > 0 && account + hasTender > most_account_num) {
            double difference = most_account_num-hasTender;
            if(difference==0){
            	throw new BorrowException("您对该标的投资已达到最多投标总额(￥"+most_account_num+")，不能继续投标，投标失败！");
            }
        	throw new BorrowException("投资金额不能高于最多投标总额,您当前还可以投" + difference + "元");
        }
		
		//标
		if(model.getStatus()!=1 && model.getStatus()!=19){
			throw new BorrowException("该标目前无法投标，投标失败！");
		}
		if(model.getUser_id()==user.getUser_id()){
			throw new BorrowException("您不能投自己发布的标，投标失败！ ");
		}
		if(model.getAccount_yes() >= model.getAccount()){
			throw new BorrowException("此标已满，投标失败！");
		}
		if(model.getVip_tender_limit() == 1){
			UserCacheModel userCacheModel = userCacheDao.getUserCacheByUserid(user.getUser_id());
			int vipStatus  = userCacheModel.getVip_status();
			if (vipStatus != 1){
				throw new BorrowException("此标仅限VIP会员投标，投标失败！ ");
			}else{
				//要求VIP资格至少是设置天数前审批通过的
				//这里的判断方法是，如果VIP审核时间向后推设置的天数的最后一秒大于当前日期的最后一秒，说明VIP期限不到
				int vipDays = model.getVip_tender_limit_days();
				if(vipDays > 0){
					String vipTime = userCacheModel.getVip_verify_time();
					Date vipNeedDate = DateUtils.rollDay(DateUtils.getDate(vipTime), vipDays);
					if (DateUtils.getTime(DateUtils.getLastSecIntegralTime(vipNeedDate)) > DateUtils.getTime(DateUtils.getLastSecIntegralTime(new Date()))){
						throw new BorrowException("此标仅限VIP会员投标（VIP生效时间要大于等于"+vipDays+"天），投标失败！ ");
					}
				}
			}
		}
		
		//投标 3大认证状态限制
		Rule rule = null;
		RuleModel ruleModel = null;
		rule = Global.getRule(EnumRuleNid.TENDER_BEFORE_VALID.getValue());
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			ruleModel = new RuleModel(rule);
			RuleModel realValidRule = ruleModel.getRuleByKey("real_valid");
			int realEnable = realValidRule.getValueIntByKey("real_enable");
			if (realEnable == 1) {
				if (user.getReal_status() != 1) {
					throw new BorrowException("您还未通过实名认证，投标失败！");
				}
			}
			RuleModel emailValidRule = ruleModel.getRuleByKey("email_valid");
			int emailEnable = emailValidRule.getValueIntByKey("email_enable");
			if (emailEnable == 1) {
				if (user.getEmail_status() != 1) {
					throw new BorrowException("您还未通过邮箱认证，投标失败！");
				}
			}
			RuleModel phoneValidRule = ruleModel.getRuleByKey("phone_valid");
			int phoneEnable = phoneValidRule.getValueIntByKey("phone_enable");
			if (phoneEnable == 1) {
				if (user.getPhone_status() != 1) {
					throw new BorrowException("您还未通过手机认证，投标失败！");
				}
			}
		}
		//投标 投标次数、待收等限制
		ruleModel = new RuleModel(Global.getRule(EnumRuleNid.TENDERLIMIT.getValue()));
		if (ruleModel != null && ruleModel.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			//v1.6.7.2 RDPROJECT-473 sj 2013-12-23 start
			if("1".equals(ruleModel.getValueStrByKey("most_tender_times"))){
				//投标次数的限制
		  		int most_tender_times = model.getMost_tender_times();
		  		if(most_tender_times > 0){
		  			int times = tenderDao.getTenderTimes(model.getId(),user.getUser_id());
		  			if(times >= most_tender_times){
		  				throw new BorrowException("超过此标最大投标次数（"+most_tender_times+"次），投标失败！");
		  			}
		  		}
			}
	  		//待收金额的限制
	  		double convest_collection = model.getConvest_collection();
	  		int tender_days = model.getTender_days();
	  		double collection = 0;
	  		if("1".equals(ruleModel.getValueStrByKey("convest_collection"))){
	  			if("1".equals(ruleModel.getValueStrByKey("tender_days")) && tender_days > 0){
	  				if(convest_collection > 0){
		  	  			collection = borrowDao.getCollectionByBorrowTimeLimit(tender_days);
		  	  			if(convest_collection > collection){
		  	  				throw new BorrowException("您当前"+tender_days+"天以上的标的待收金额小于此标待收金额(￥"+convest_collection+")限制，投标失败！");
		  	  			}
		  	  		}
	  			}else{
	  				collection = act.getCollection();
	  				//v1.6.7.2 RDPROJECT-637 sj 2013-12-26 start
		  			if(convest_collection > collection && convest_collection > 0){
		  				throw new BorrowException("您当前的待收金额(￥"+collection+")小于此标待收金额(￥"+convest_collection+"元)限制，投标失败！");
		  			//v1.6.7.2 RDPROJECT-637 sj 2013-12-26 end
		  			}
	  			}
	  		}
	  		//v1.6.7.2 RDPROJECT-473 sj 2013-12-23 end
	  		RuleModel limitRule = ruleModel.getRuleByKey("miao_tender_day_limit");
			if(limitRule != null){
				String status = limitRule.getValueStrByKey("status");
				String times = limitRule.getValueStrByKey("times");
				int days = Integer.parseInt(times);
				if("1".equals(status)){
					//当是秒标时候，判断有没有超过当天投标次数限制
					if(model.getType() == Constant.TYPE_SECOND){
						Map tenderHm = borrowDao.getUserTenderNum(user.getUser_id(), "miao", new Date(), new Date());
						int total_tendernum = Integer.parseInt(tenderHm.get("tender_num").toString());
						//如果设置了秒标投标限制，且今天已经投标的次数大于等于这个限制，就不让继续投标，这里成功投标是指还没复审或者已经复审的投标，撤销的不算
						if (days > 0 && total_tendernum >= days){
							throw new BorrowException("已达到今日秒标最大投标次数（" + days + "次）限制，投标失败！");
						}
					}
				}
			}
			
			List list = ruleModel.getValueListByKey("tender_collection_limit");
			if(list != null && list.size() > 0 && (Integer)list.get(0) == 1){
		        // 信达财富月标待收金额小于flowTenderAccount值，无法进行投流转天标
		        if (Global.getWebid().equals("xdcf") || Global.getWebid().equals("zrzb")) {
		            if (model.getType() == Constant.TYPE_FLOW && model.getIsday() == 1 && model.getTime_limit_day() == 7) {
		                double flow_month_tender_collection = accountDao.getFlowMonthTenderCollection(tender.getUser_id());
		                double flow_day_tender_collection = accountDao.getFlowDayTenderCollection(tender.getUser_id(), 7);
		                if (flow_day_tender_collection + account > flow_month_tender_collection) {
		                    throw new BorrowException("您的待收金额小于" + account + ",不能进行投流转标，投标失败！");
		                }
		            }
		        }
	        }
	        if(list != null && list.size() > 0 && (Integer)list.get(1) == 1){
		        if (Global.getWebid().equals("xdcf") || Global.getWebid().equals("zrzb")) {
		            if (model.getType() == Constant.TYPE_FLOW && model.getIsday() == 1 && model.getTime_limit_day() == 28) {
		                double flow_month_tender_collection = accountDao.getFlowMonthTenderCollection(tender.getUser_id());
		                double flow_day_collection = accountDao.getFlowDayTenderCollection(tender.getUser_id(), 28);
		                if (flow_day_collection + account > flow_month_tender_collection) {
		                    throw new BorrowException("您的待收金额小于" + account + ",不能进行投流转标，投标失败！");
		                }
		            }
		        }
	        }
	        if(list != null && list.size() > 0 && (Integer)list.get(2) == 1){
		        // 待收金额小于5000，无法进行投秒
		        double mbTenderAccount = NumberUtils.getDouble(Global.getValue("mb_tender_account"));
		        if (mbTenderAccount > 0 && NumberUtils.format2(act.getCollection()) < mbTenderAccount
		                && model.getType() == Constant.TYPE_SECOND) {
		            throw new BorrowException("您的待收金额小于" + mbTenderAccount + ",不能进行投秒标，投标失败！");
		        }
	        }
	        if(list != null && list.size() > 0 && (Integer)list.get(3) == 1){
		        //待收金额小于指定金额，不允许投质押标（奔富金融需求）
		        double fastTenderAccount = NumberUtils.getDouble(Global.getValue("fast_tender_account"));
		        if (fastTenderAccount > 0 && NumberUtils.format2(act.getCollection()) < fastTenderAccount
		                && model.getType() == Constant.TYPE_MORTGAGE) {
		            throw new BorrowException("您的待收金额小于" + fastTenderAccount + ",不允许投质押标，投标失败！");
		        }
	        }
	        if(list != null && list.size() > 0 && (Integer)list.get(4) == 1){
		        // 美贷网投标待收限制
		        double collectionLimit = NumberUtils.getDouble(model.getCollection_limit());
		        if (collectionLimit > 0
		                && NumberUtils.format2(act.getCollection()) < NumberUtils.format2(collectionLimit)) {
		            throw new BorrowException("您的待收金额小于该借款标最小待收限制" + collectionLimit + ",不能进行投标，投标失败！");
		        }
	        }
		}
	}
	
	@Override
	public void tenderSuccess(Tender tender,InterestCalculator ic) {
		// 总共需要还款金额
        double repayment_account = ic.getTotalAccount();
        double repayment_interest = repayment_account - NumberUtils.getDouble(tender.getAccount());
        tender.setRepayment_account(NumberUtils.format6(repayment_account) + "");
        tender.setInterest(NumberUtils.format6(repayment_interest) + "");
        tender.setStatus(0);
        WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
        TenderDao tenderDao=(TenderDao)ctx.getBean("tenderDao");
        tenderDao.modifyTender(tender);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void handleTenderAfterFullSuccess() {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		TenderDao tenderDao=(TenderDao)ctx.getBean("tenderDao");
		List tenderList = tenderDao.allTenderListBybid(model.getId());
		for(int i=0;i<tenderList.size();i++){
			Tender tender = (Tender) tenderList.get(i);
			Global.setTransfer("tender", tender);
			
			double account = NumberUtils.getDouble(tender.getAccount());
			double interest = NumberUtils.getDouble(tender.getInterest());
			AccountDao accountDao = (AccountDao)ctx.getBean("accountDao");
			Account act = accountDao.getAccount(tender.getUser_id());
	        
	        // 扣除冻结/生产待收本金
			Global.setTransfer("money", account);
			Global.setTransfer("borrow", model);
	        BaseAccountLog deductFreezeLog=new BorrowDecuctFreezeLog(account, act, model.getUser_id());
			deductFreezeLog.doEvent();
			
			// 生产待收利息
			Global.setTransfer("money", interest);
			Global.setTransfer("borrow", model);
			BaseAccountLog waitInterestLog=new BorrowWaitInterestLog(interest, act, model.getUser_id());
			waitInterestLog.doEvent();
			
			// 投标奖励 
	        double awardValue =  calculateAward(account);
			if (awardValue > 0) {
				// 给予投资人奖励
				Global.setTransfer("award", awardValue);
				Global.setTransfer("borrow", model);
				BaseAccountLog bLog = new AwardTenderAwardLog(awardValue, act, model.getUser_id());
				bLog.doEvent();
			}
			
			//修改Tender表中的待收利息
			tender.setWait_account(tender.getRepayment_account());
			tender.setWait_interest(tender.getInterest());
			tender.setStatus(1);
	        tenderDao.modifyTender(tender);
			
	        CollectionDao collectionDao = (CollectionDao)ctx.getBean("collectionDao");
			@SuppressWarnings("unchecked")
			List<Collection> clist = collectionDao.getCollectionListByTender(tender.getId());
			for(Collection c:clist){
				BaseBorrowModel baseBorrow=new BaseBorrowModel(model);
				c.setRepay_time(baseBorrow.getRepayTime(c.getOrder()).getTime()/1000+"");
			}
			// v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 start
			//计算还款奖励，并存放到collection中
			//目前还款奖励只有一种还法，最后一期偿还，所以只需要更新最后一期里的信息即可,这是一种很简单的处理办法，不支持还款奖励分期支付
			//late_award 和 repay_award是一个意思
			Collection lastC = clist.get(clist.size() - 1);
			lastC.setRepay_award(Double.parseDouble(tender.getAccount()) * model.getLate_award() / 100);
			// v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 end
			collectionDao.modifyBatchCollection(clist);
			
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
		}
	}
	
	@Override
	public void handleBorrowAfterFullSuccess() {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		AccountDao accountDao = (AccountDao)ctx.getBean("accountDao");
		Account actBorrow = accountDao.getAccount(model.getUser_id());

		// 借款人资金入账
		Global.setTransfer("money", model.getAccount());
		Global.setTransfer("borrow", model);
		BaseAccountLog borrowSuccessLog = new BorrowSuccessLog(model.getAccount(), actBorrow, 1);
		borrowSuccessLog.doEvent();
		// 支付投标奖励
		double totalAwardValue = calculateAward(model.getAccount());
		if (totalAwardValue > 0) {
			Global.setTransfer("money", totalAwardValue);
			Global.setTransfer("borrow", model);
			BaseAccountLog bLog = new DeductAwardLog(totalAwardValue, actBorrow);
			bLog.doEvent();
		}
		// 还款计划
		RepaymentDao repaymentDao = (RepaymentDao)ctx.getBean("repaymentDao");
		if (model.getBorrowType() != Constant.TYPE_DONATION) {
			repaymentDao.addBatchRepayment(getRepayment());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void handleTenderAfterFullFail() {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		TenderDao tenderDao=(TenderDao)ctx.getBean("tenderDao");
		tenderDao.updateTenderStatusByBorrowId(model.getId(), 2);
		List tenderList = tenderDao.allTenderListBybid(model.getId());
		for (int i = 0; i < tenderList.size(); i++) {
			Tender tender = (Tender) tenderList.get(i);
			
			double account = NumberUtils.getDouble(tender.getAccount());
			AccountDao accountDao = (AccountDao)ctx.getBean("accountDao");
			Account act = accountDao.getAccount(tender.getUser_id());
			Global.setTransfer("money", account);
			
			Global.setTransfer("tenderAccount", account);
			Global.setTransfer("borrow", model);
			Global.setTransfer("tender", tender);
			BaseAccountLog borrowFailLog = new BorrowFailLog(account, act, model.getUser_id());
			borrowFailLog.doEvent();
		}
	}
	
	@Override
	public double calExtensionInterest(Repayment repay) {
		double extensionInterest = 0;
		Rule rule = Global.getRule(EnumRuleNid.EXTENSION.getValue());
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			RuleModel extensionRule = new RuleModel(rule);
			int extension_enable = extensionRule
					.getValueIntByKey("extension_enable");
			if (extension_enable == 1) {
				long nowTime = NumberUtils.getLong(StringUtils.isNull(DateUtils.getNowTimeStr()));
				long repay_time = NumberUtils.getLong(StringUtils.isNull(repay.getRepayment_time()));
				long extension_day = model.getExtension_day();
				long now_extension_day = 0;
				if (model.getExtension_day() > 0) {
					if (nowTime - repay_time > 0) {
						now_extension_day = (nowTime - repay_time) / (24 * 60 * 60) ;
						if (now_extension_day > extension_day) {
							extensionInterest = (NumberUtils.getDouble(repay.getCapital()) * extension_day * model.getExtension_apr());
							repay.setReal_extension_day(extension_day);
						} else {
							extensionInterest = (NumberUtils.getDouble(repay.getCapital()) * now_extension_day * model.getExtension_apr());
							repay.setReal_extension_day(now_extension_day);
						}
						if (extensionInterest > 0) {
							repay.setExtension_interest(extensionInterest);
							WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
							RepaymentDao repaymentDao=(RepaymentDao)ctx.getBean("repaymentDao");
							repaymentDao.modifyRepaymentExtensionInterest(repay);
						}
					}
				}
			}
		}
		return extensionInterest;
	}
	
	@Override
	public void borrowRepayHandleBorrow(Repayment repay, double lateInterest, double repayLateInterest) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		AccountDao accountDao = (AccountDao)ctx.getBean("accountDao");
		Account act = accountDao.getAccount(model.getUser_id());
		
		double capital = NumberUtils.getDouble(repay.getCapital());
		double interest = NumberUtils.getDouble(repay.getInterest());
		double extensionInterest = repay.getExtension_interest();
		
		// 从用户冻结账户中扣除还款本金
		if(capital>0){
			Global.setTransfer("money", capital);
			Global.setTransfer("borrow", model);
			BaseAccountLog deductCapitalLog=new BorrowRepayCapitalLog(capital, act, 1);
			deductCapitalLog.doEvent();
		}
		// 扣除还款利息
		if(interest>0){
			Global.setTransfer("money", interest);
			Global.setTransfer("borrow", model);
			BaseAccountLog deductInterest=new BorrowRepayInterestLog(interest, act, 1);
			deductInterest.doEvent();
		}
		// 扣除展期利息
		if(extensionInterest>0){
			Global.setTransfer("money", extensionInterest);
			Global.setTransfer("borrow", model);
			BaseAccountLog deductInterest=new BorrowRepayExtensionInterestLog(extensionInterest, act, 1);
			deductInterest.doEvent();
		}
		// 逾期利息还款给平台
		if(lateInterest > 0){
			Global.setTransfer("money", lateInterest);
			Global.setTransfer("borrow", model);
			BaseAccountLog deductInterest = new BorrowRepayInterestLog(lateInterest, act, 1);
			deductInterest.doEvent();
		} 
		// 逾期利息
		else if (repayLateInterest > 0) {
			// 扣除
			Global.setTransfer("money", repayLateInterest);
			Global.setTransfer("borrow", model);
			BaseAccountLog borrowerlateLog = new BorrowRepayLateInterestLog(repayLateInterest, act, 1);
			borrowerlateLog.doEvent();
			// 50%给平台
			Global.setTransfer("money", repayLateInterest / 2);
			Global.setTransfer("borrow", model);
			BaseAccountLog systemLateLog = new BorrowRepaySystemLateInterestLog(repayLateInterest / 2, accountDao.getAccount(1l), 1);
			systemLateLog.doEvent();
		}
		
		// 向借款人发送还款成功通知
		Global.setTransfer("repay", repay);
		Global.setTransfer("borrow", model);
		BaseAccountLog blog = new BorrowerRepaySuccLog(0, act, model.getUser_id());
		blog.doEvent();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void borrowRepayHandleTender(Repayment repay, double repayLateInterest) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		AccountDao accountDao = (AccountDao)ctx.getBean("accountDao");
		TenderDao tenderDao = (TenderDao)ctx.getBean("tenderDao");
		CollectionDao collectionDao = (CollectionDao)ctx.getBean("collectionDao");
		
		Account borrowAct = accountDao.getAccountByUserId(repay.getUser_id());
		
		List<DetailCollection> collectList = null;
		List<Tender> tenderList = new ArrayList<Tender>();
		// v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 start
		// 为了支持还款奖励分期支付，每一次还款时，算出实际要支付的还款奖励，而且 不是根据借款标计算
		double totalLateAwardValue = 0;
		// v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 end
		boolean isWebPay = false;
		if(!StringUtils.isBlank(repay.getLate_interest()) && repay.getWebstatus() == 3 && repay.getStatus() == 2){// 逾期,并且平台已垫付
			isWebPay = true;
		}
		// 循环投资人还款
		if(!isWebPay){
			collectList = collectionDao.getCollectionLlistByBorrow(model.getId(),repay.getOrder());
			for(DetailCollection c:collectList){
				double cCapital = NumberUtils.format6(NumberUtils.getDouble(c.getCapital()));
				double cInterest = NumberUtils.format6(NumberUtils.getDouble(c.getInterest()));
				Tender tender = tenderDao.getTenderById(c.getTender_id());
				Account tenderAct = accountDao.getAccount(tender.getUser_id());
				tenderList.add(tender);
				// 归还投资人本金
				if (cCapital > 0) {
					Global.setTransfer("money", cCapital);
					Global.setTransfer("borrow", model);
					BaseAccountLog repayTenderCapital=new BorrowRepayTenderCapitalLog(cCapital, tenderAct, model.getUser_id());
					repayTenderCapital.doEvent();
				}
				// 归还投资人利息
				c = getInvestRepayInterest(c, tender);
				// 还投资人逾期的利息  按投资的比例来计算
				double totalRepay = 1;
				String totalRepayStr = repay.getCapital();
				if(!StringUtils.isBlank(totalRepayStr)){
					totalRepay = Double.parseDouble(totalRepayStr);
				}
				// 逾期利息
				if(repayLateInterest > 0){
					double tenderLateInterest = NumberUtils.format2(repayLateInterest*(cCapital/totalRepay));
					if(tenderLateInterest>0){
						Global.setTransfer("money", tenderLateInterest);
						Global.setTransfer("borrow", model);
						BaseAccountLog tenderLateLog = new BorrowRepayTenderLateInterestLog(tenderLateInterest, tenderAct, model.getUser_id());
						tenderLateLog.doEvent();
					}
					c.setLate_days(Long.valueOf(repay.getLate_days()));
					c.setLate_interest(String.valueOf(tenderLateInterest));
				}
				// 展期利息
				double userTenderExtensionInterest=0;
				double extensionInterest = repay.getExtension_interest();
				if (extensionInterest > 0) {
					double tenderExtensionInterest = NumberUtils.format2(extensionInterest * (cCapital / totalRepay));
					double tender_apr = 0;
					Rule rule = Global.getRule(EnumRuleNid.EXTENSION.getValue());
					if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
						RuleModel extensionRule = new RuleModel(rule);
						int extension_enable = extensionRule.getValueIntByKey("extension_enable");
						if (extension_enable == 1) {
							tender_apr = extensionRule.getValueDoubleByKey("tender_apr");
						}
					}
					userTenderExtensionInterest = (tenderExtensionInterest * tender_apr);
					if (userTenderExtensionInterest > 0) {
						Global.setTransfer("money", userTenderExtensionInterest);
						Global.setTransfer("borrow", model);
						BaseAccountLog deductInterest = new BorrowRepayTenderExtensionInterestLog(userTenderExtensionInterest, tenderAct, model.getUser_id());
						deductInterest.doEvent();
					}
					c.setExtension_interest(userTenderExtensionInterest);
				}
				tenderDao.modifyRepayTender(Double.valueOf(c.getRepay_account()), cInterest, tender.getId());
				
				//更新collection
				c.setStatus(1);
				c.setRepay_yestime(DateUtils.getNowTimeStr());
				c.setRepay_yesaccount(c.getRepay_account());
				
				// 还款奖励数额直接从待收里获取，因为这个金额已经在复审时计算好，债权转让时也更新成对应的新值
				double awardValue = c.getRepay_award();
				if (awardValue > 0 && c.getRepay_award_status() == Constant.REPAY_AWARD_STATUS_NORAML){
					Global.setTransfer("award", awardValue);
					BaseAccountLog bLog = new AwardRepayLog(awardValue, tenderAct, model.getUser_id());
					bLog.doEvent();
					c.setRepay_award_status(Constant.REPAY_AWARD_STATUS_PAYED);
					totalLateAwardValue += awardValue;
				}
				collectionDao.modifyCollection(c);
				
				// 向投资人发送还款成功通知
				Global.setTransfer("collection", c);
				BaseAccountLog tlog=new TenderRepaySuccLog(0, tenderAct, c.getUser_id());
				tlog.doEvent();
				
				// 【及时雨】推广奖励
				PromoteAward promoteAward = new PromoteAward(c);
				promoteAward.giveAward();
			}
		}else{
			//网站垫付
		}
		
		// 扣除发标人需要支付的还款奖励
		if (totalLateAwardValue > 0) {
			Global.setTransfer("money", totalLateAwardValue);
			Global.setTransfer("borrow", model);
			Global.setTransfer("award", totalLateAwardValue);
			BaseAccountLog bLog = new DeductAwardLog(totalLateAwardValue, borrowAct);
			bLog.doEvent();
		}
	}
	
	@Override
	public DetailCollection getInvestRepayInterest(DetailCollection c, Tender tender) {
		double cInterest = NumberUtils.format6(NumberUtils.getDouble(c.getInterest()));
		// 归还投资人利息
		if (cInterest > 0) {
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			AccountDao accountDao = (AccountDao)ctx.getBean("accountDao");
			Account tenderAct = accountDao.getAccount(tender.getUser_id());
			UserCacheDao userCacheDao = (UserCacheDao)ctx.getBean("userCacheDao");
			CollectionDao collectionDao = (CollectionDao)ctx.getBean("collectionDao");
			
			double borrow_fee = Global.getDouble("borrow_fee");
			double borrowFee = cInterest * borrow_fee;
			
			// 收回利息
			Global.setTransfer("money", cInterest);
			Global.setTransfer("borrow", model);
			Global.setTransfer("tender", tender);
			Global.setTransfer("borrowFee", borrowFee);
			BaseAccountLog bLog = new BorrowRepayTenderInterestLog(cInterest, tenderAct, model.getUser_id());
			bLog.doEvent();
			
			// 扣除投资人利息管理费
			if (borrowFee > 0) {
				Global.setTransfer("money", borrowFee);
				Global.setTransfer("borrow", model);
				BaseAccountLog manageFeeLog = new DeductManageFeeLog(borrowFee, tenderAct);
				manageFeeLog.doEvent();
				c.setManage_fee(StringUtils.isNull(borrowFee));
			}
			// 利息管理费优惠返还     VIP
			UserCache uc = userCacheDao.getUserCacheByUserid(tender.getUser_id());
			if (NumberUtils.getDouble(c.getManage_fee())>0 && uc.getVip_status() == 1 && !StringUtils.isBlank(Global.getString("vip_borrow_fee"))) {
				double vip_borrow_fee = Global.getDouble("vip_borrow_fee");
				if (vip_borrow_fee < borrow_fee) {
					double vipBorrowFee = cInterest * vip_borrow_fee;
					double backBorrowFee = borrowFee - vipBorrowFee;
					Global.setTransfer("money", backBorrowFee);
					Global.setTransfer("borrow", model);
					BaseAccountLog backManageFeeLog = new BackManageFeeVipLog(backBorrowFee, tenderAct);
					backManageFeeLog.doEvent();
					c.setManage_fee(StringUtils.isNull(vipBorrowFee));
				}
			}
			// 利息管理费优惠返还     会员积分等级
			BackManageFeeByUserLevel backManageFeeByUserLevel = new BackManageFeeByUserLevel(c);
			c.setManage_fee(StringUtils.isNull(backManageFeeByUserLevel.backFee()));
			
			
			// 利息管理费优惠返还     【民生创投】累积投标金额
			InterestLog interestLog = new DiscountInterestLog(model, tender, cInterest);
			interestLog.doEvent();
			Map<String, Object> map = Global.getTransfer();
			Object obj = map.get("manage_fee");
			if(obj!=null){
				double manage_fee = (Double) obj;
				c.setManage_fee(StringUtils.isNull(manage_fee));
			}
			collectionDao.modifyCollection(c);
		}
		return c;
	}
	
	@Override
	public void flowBorrowRepay(DetailCollection c) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		AccountDao accountDao = (AccountDao)ctx.getBean("accountDao");
		BorrowDao borrowDao = (BorrowDao)ctx.getBean("borrowDao");
		TenderDao tenderDao = (TenderDao)ctx.getBean("tenderDao");
		
		double capital = NumberUtils.getDouble(c.getCapital());
		double interest = NumberUtils.getDouble(c.getInterest());
		
		BorrowModel model = borrowDao.getBorrowById(c.getBorrow_id());
		Global.setTransfer("borrow", model);
		Tender tender = tenderDao.getTenderById(c.getTender_id());
		
		Account borrowAct = accountDao.getAccountByUserId(model.getUser_id());
		Account tenderAct = accountDao.getAccountByUserId(tender.getUser_id());
		
		// 本金
		if(capital>0){
			// 扣除
			Global.setTransfer("money", capital);
			Global.setTransfer("borrow", model);
			BaseAccountLog borrowLog=new FlowBorrowRepayCapitalLog(capital, borrowAct, tender.getUser_id());
			borrowLog.doEvent();
			
			// 归还投资人本金
			BaseAccountLog investLog=new FlowBorrowRepayTenderCapitalLog(capital, tenderAct, model.getUser_id());
			investLog.doEvent();
		}
		// 利息
		if(interest>0){
			// 扣除
			Global.setTransfer("money", interest);
			Global.setTransfer("borrow", model);
			BaseAccountLog borrowLog=new FlowBorrowRepayInterestLog(interest, borrowAct, tender.getUser_id());
			borrowLog.doEvent();
			// 归还投资人利息
			c = getInvestRepayInterest(c, tender);
		}
		tenderDao.modifyRepayTender(NumberUtils.getDouble(c.getRepay_account()), interest, tender.getId());
		
		//更新collection记录
		c.setStatus(1);
		c.setRepay_yestime(DateUtils.getNowTimeStr());
		c.setRepay_yesaccount(c.getRepay_account());
		CollectionDao collectionDao = (CollectionDao)ctx.getBean("collectionDao");
		collectionDao.modifyCollection(c);
		// 【及时雨】推广奖励
		PromoteAward promoteAward = new PromoteAward(c);
		promoteAward.giveAward();
		
		//更新repayment记录
		RepaymentDao repaymentDao = (RepaymentDao)ctx.getBean("repaymentDao");
		Repayment flowRepay = repaymentDao.getRepaymentByTenderIdAndPeriod(c.getTender_id(), c.getOrder());
		if(flowRepay!=null){
			flowRepay.setStatus(1);
			flowRepay.setWebstatus(1);
		    flowRepay.setRepayment_yestime(c.getRepay_yestime());
			flowRepay.setRepayment_yesaccount(c.getRepay_account());
			repaymentDao.modifyFlowRepaymentYes(flowRepay);
		}
		// 释放流转标已经回款的金额
		// 不知何意，先注释掉，请勿删除
		/*if(model.getIsday()==1 || "2".equals(model.getStyle())){
			int count = borrowDao.releaseFlowBorrow(NumberUtils.getDouble(c.getCapital()),model.getId());
			if(count!=1){
				throw new BorrowException("释放流转标已经回款的金额，操作失败！");
			}
		}else{
			if((c.getOrder()+1)==NumberUtils.getInt(model.getTime_limit())){
				int count=borrowDao.releaseFlowBorrow(NumberUtils.getDouble(tender.getAccount()),model.getId());
				if(count<1){
					throw new BorrowException("释放流转标已经回款的金额，操作失败！");
				}
			}
		}*/
		
		// 还款奖励数额直接从待收里获取，因为这个金额已经在复审时计算好，债权转让时也更新成对应的新值
		double awardValue = c.getRepay_award();
		if (awardValue > 0 && c.getRepay_award_status() == Constant.REPAY_AWARD_STATUS_NORAML){
			Account borrowerAct = accountDao.getAccount(model.getUser_id());
			Global.setTransfer("money", interest);
			Global.setTransfer("borrow", model);
			Global.setTransfer("deduct", model);
			BaseAccountLog deductLog = new DeductAwardLog(awardValue, borrowerAct);
			deductLog.doEvent();
			
			Global.setTransfer("money", interest);
			Global.setTransfer("borrow", model);
			Global.setTransfer("award", awardValue);
			BaseAccountLog bLog = new AwardRepayLog(awardValue, tenderAct, model.getUser_id());
			bLog.doEvent();
			
			c.setRepay_award_status(Constant.REPAY_AWARD_STATUS_PAYED);
		}
		collectionDao.modifyCollection(c);
	}
	
	
	
}
