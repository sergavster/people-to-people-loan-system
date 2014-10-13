package com.p2psys.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumTreasureAudit;
import com.p2psys.common.enums.EnumTreasureStatus;
import com.p2psys.context.Global;
import com.p2psys.domain.Advanced;
import com.p2psys.domain.Article;
import com.p2psys.domain.CreditCard;
import com.p2psys.domain.QuickenLoans;
import com.p2psys.domain.Rule;
import com.p2psys.domain.RunBorrow;
import com.p2psys.domain.Site;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RankModel;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserBorrowModel;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.ArticleService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.CreditCardService;
import com.p2psys.service.QuickenLoansService;
import com.p2psys.service.RewardStatisticsService;
import com.p2psys.service.UserService;
import com.p2psys.treasure.model.TreasureModel;
import com.p2psys.treasure.service.TreasureService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;


public class IndexAction extends BaseAction implements ServletRequestAware{
	private static Logger logger = Logger.getLogger(IndexAction.class);  
	// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 start
	//private ServletRequest request;
	// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 end
	private List<UserBorrowModel> borrowList;
	/**
	 * 通过Spring容器注入service
	 */
	private BorrowService borrowService;
	private ArticleService articleService;
	private CreditCardService creditCardService;
	private QuickenLoansService quickenLoansService;
	
	// v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
	private RewardStatisticsService rewardStatisticsService;
	// v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
	@Resource
	private TreasureService treasureService;

	// v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
	public RewardStatisticsService getRewardStatisticsService() {
		return rewardStatisticsService;
	}
   
	public void setRewardStatisticsService(
			RewardStatisticsService rewardStatisticsService) {
		this.rewardStatisticsService = rewardStatisticsService;
	}
	// v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
	private BorrowModel model=new BorrowModel();
	private RunBorrow runBorrow;
	private StringBuffer msg = new StringBuffer();
	private UserService userService;
	public RunBorrow getRunBorrow() {
		return runBorrow;
	}

	public void setRunBorrow(RunBorrow runBorrow) {
		this.runBorrow = runBorrow;
	}

	public BorrowModel getModel() {
		return model;
	}

	public void setModel(BorrowModel model) {
		this.model = model;
	}

	public String execute() throws Exception {
		
		long s=System.currentTimeMillis();
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INDEX.getValue()));//首页规则
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 start
		Rule loginRule=Global.getRule(EnumRuleNid.LOGIN_VALIDE_ENABLE.getValue());//登录验证码
		if(loginRule!=null && loginRule.getStatus()==1){
			request.setAttribute("loginRule", loginRule);
		}
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 end
		//获取首页借款清单
		borrowList=borrowService.getList();
		request.setAttribute("borrowList", borrowList);
		 
		//成功案例
		int isSuccess=rule.getValueIntByKey("success");
		if(isSuccess==1){
			List successList=borrowService.getSuccessListForIndex("", 10);
			request.setAttribute("successList", successList);
		}
		
		//最新借款
		int newTender=rule.getValueIntByKey("newTender");
		if(newTender==1){
			List newTenderList=borrowService.getNewTenderList();
			request.setAttribute("newTenderList", newTenderList);
		}
		
		//获取首页推荐标列表
		int recommend=rule.getValueIntByKey("recommend");
		if (recommend==1) {
			List recommendList = borrowService.getRecommendList();
			request.setAttribute("recommendList", recommendList);
		}
		//   v1.6.7.2 理财宝显示 zhangyz 2013-12-09 start
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("audit_status", EnumTreasureAudit.PASS_AUDIT.getValue());
		map.put("status", EnumTreasureStatus.START.getValue());
		List<TreasureModel> treasureList = treasureService.getTreasure(map);
		request.setAttribute("treasureList", treasureList);
		//   v1.6.7.2 理财宝显示 zhangyz 2013-12-09 end
		Date nowDay = DateUtils.getLastSecIntegralTime(new Date());
		Date lastDay = DateUtils.rollDay(nowDay, -1);
		Date lastWeek = DateUtils.rollDay(nowDay, -7);
		Date lastMon = DateUtils.rollMon(nowDay, -1);
		// v1.6.6.1 RDPROJECT-265 zza 2013-10-08 start
		Date yesterday = DateUtils.rollDay(nowDay, -2);
		// v1.6.6.1 RDPROJECT-265 zza 2013-10-08 end
		
		//首页显示统计的相关
		int statics=rule.getValueIntByKey("statics");
		if (statics==1) {
			//当前注册用户人数
			int registerCount = userService.userCount();
			request.setAttribute("registerCount", registerCount);
			
			//成交笔数
			int borrowCount=0;
			borrowCount = borrowService.getBorrowCountForSuccess();
			request.setAttribute("borrowCount", borrowCount);
			// v1.6.6.1 RDPROJECT-265 zza 2013-10-08 start
			//昨日投资有效金额
			/*国平说晋商贷那边这个加上去以后网站速度会变慢，晋商贷也没写这段代码了
			 * double yesterdayTenderAccount = borrowService.countTenderAccount(
					DateUtils.getTime(yesterday) + "", DateUtils.getTime(lastDay) + "");
			request.setAttribute("yesterdayTenderAccount", yesterdayTenderAccount);*/
			// v1.6.6.1 RDPROJECT-265 zza 2013-10-08 end
			//单日投资有效金额
			double dayTenderAccount=borrowService.countTenderAccount(DateUtils.getTime(lastDay)+"", DateUtils.getTime(nowDay)+"");
			request.setAttribute("dayTenderAccount", dayTenderAccount);
			//总投标有效金额
			double totalTenderAccount=borrowService.countTenderAccount("0", DateUtils.getTime(nowDay)+"");
			request.setAttribute("totalTenderAccount", totalTenderAccount);
			//商富贷申请借入总额
			double applyBorrowTotal = 0.0;
			applyBorrowTotal = borrowService.getApplyBorrowTotal();
			request.setAttribute("applyBorrowTotal", applyBorrowTotal);
			//商富贷申请借入笔数
			int applyBorrowCount = 0;
			applyBorrowCount = borrowService.getApplyBorrowCount();
			request.setAttribute("applyBorrowCount", applyBorrowCount);
			// v1.6.7.1 RDPROJECT-439 zza 2013-11-12 start
			double tenderCount = 0;
			double collectionCount = 0;
			double laterCount = 0;
			double laterBackCount = 0;
			if (StringUtils.isNumber(Global.getValue("tender_count"))) {
				tenderCount = Double.valueOf(Global.getValue("tender_count"));
			}
			if (StringUtils.isNumber(Global.getValue("collection_count"))) {
				collectionCount = Double.valueOf(Global.getValue("collection_count"));
			}
			if (StringUtils.isNumber(Global.getValue("later_count"))) {
				laterCount = Double.valueOf(Global.getValue("later_count"));
			}
			if (StringUtils.isNumber(Global.getValue("later_back_count"))) {
				laterBackCount = Double.valueOf(Global.getValue("later_back_count"));
			}
			request.setAttribute("tenderCount", tenderCount); //投资总金额
			request.setAttribute("collectionCount", collectionCount); //待收总金额
			request.setAttribute("laterCount", laterCount); //逾期垫付金额
			request.setAttribute("laterBackCount", laterBackCount); //逾期收回金额
			// v1.6.7.1 RDPROJECT-439 zza 2013-11-12 end
			
			// v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
//				request.setAttribute("investTotal", borrowService.getInvestTotal());//和信贷融资总金额（不统计秒还标）
//				//为投资人总赚得利息（和信贷，不统计秒还标）
//				double interestTotal=borrowService.getInterestTotal();
//				request.setAttribute("interestTotal", interestTotal);
			// v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
		}
		
		// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 start
		rankList(rule, nowDay, lastDay, lastWeek, lastMon);
		// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 end
		
		//文章模块 暂时做成配置  modify by fxx;
		RuleModel articleRule=rule.getRuleByKey("article");
		if(articleRule!=null){
			List nidList=articleRule.getValueListByKey("nid");
			List numList=articleRule.getValueListByKey("num");
			int default_num=articleRule.getValueIntByKey("default_num");  //默认大小，如果是组合表示标种个数
			boolean isDefaultNum=false; 
			if(nidList.size()!=numList.size()){
				isDefaultNum=true;
			}
			for(int i=0;i<nidList.size();i++){
				Site site=articleService.getSiteByNid(StringUtils.isNull(nidList.get(i)));
				if(null!=site){
					int pageNum=default_num; //默认大小
					if(!isDefaultNum) pageNum=(Integer)numList.get(i) ; //每个标种自定义个数
					List<Article> list = articleService.getList(site.getSite_id()+"", 0, pageNum);
					request.setAttribute(site.getNid(), list);
				}
			}
		}

		// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 start
//		//首页滚动图片
//		RuleModel scrollPicRule=rule.getRuleByKey("scrollPic");
//		List scrollPic =getRule(scrollPicRule,EnumPic.FIRST.getValue());
//		request.setAttribute("scrollPic", scrollPic);
//		//合作伙伴
//		RuleModel cooperativePartnerRule=rule.getRuleByKey("cooperativePartnerPic");
//		List cooperativePartnerPic =getRule(cooperativePartnerRule,EnumPic.SECOND.getValue());
//		request.setAttribute("cooperativePartnerPic", cooperativePartnerPic);
//		//友情链接
//		RuleModel linksRule=rule.getRuleByKey("links");
//		List linksPic =getRule(linksRule,EnumPic.THREE.getValue());
//		request.setAttribute("linksPic", linksPic);
		
		// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 end
		
		RuleModel rateRule=rule.getRuleByKey("rate");
		if(rateRule!=null){
			RuleModel normalRule=rule.getRuleByKey("normal_rate");
			RuleModel overdueRule=rule.getRuleByKey("overdue_rate");
			RuleModel badRule=rule.getRuleByKey("bad_rate");
			request.setAttribute("normal_rate", normalRule.getValueDoubleByKey("value"));
			request.setAttribute("overdue_rate", overdueRule.getValueDoubleByKey("value"));
			request.setAttribute("bad_rate", badRule.getValueDoubleByKey("value"));
		}
		// V1.6.6.2 RDPROJECT-299 wfl 2013-10-23 start
		/*int totalOfMonthRule=rule.getValueIntByKey("totalRankListOfMonth");
		if(totalOfMonthRule==1){ 
			List<RankModel> totalRankListOfMonth = borrowService.getRankList(); 
			request.setAttribute("totalRankListOfMonth", totalRankListOfMonth); 
		}*/
		// V1.6.6.2 RDPROJECT-299 wfl 2013-10-23 start
        /* //当前系统融资总额（不包含流转标）
		double borrowTotal=borrowService.getBorrowTotal();
		request.setAttribute("borrowTotal", borrowTotal);
		//当天应还本息总额
		String todayStartTime=DateUtils.getIntegralTime().getTime()/1000+"";
		String todayEndTime=DateUtils.getLastIntegralTime().getTime()/1000+"";
		double todayRepayAccountAndInterest=borrowService.getTotalRepayAccountAndInterest(todayStartTime, todayEndTime);
		request.setAttribute("todayRepayAccountAndInterest", todayRepayAccountAndInterest);*/

		// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 start
		initRSAME();
		// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 end
		
		long e=System.currentTimeMillis();
		logger.info("IndexAction Cost Time:"+(e-s));
		
		return "success";
    }

	// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 start
	/**
	 * 首页排行榜（日、周、月、总投资排行榜）
	 * @param rule rule
	 * @param nowDay nowDay
	 * @param lastDay lastDay
	 * @param lastWeek lastWeek
	 * @param lastMon lastMon
	 */
	private void rankList(RuleModel rule, Date nowDay, Date lastDay,
			Date lastWeek, Date lastMon) {
		RuleModel rankRule = rule.getRuleByKey("rank");
		if (rankRule != null) {
			// 日、周、月、总投资排行榜
			int rankStatus = rankRule.getValueIntByKey("status");
			if (rankStatus == 1) {
				String start_time = rankRule.getValueStrByKey("start_time");
				String end_time = rankRule.getValueStrByKey("end_time");
				if (StringUtils.isBlank(start_time) && StringUtils.isBlank(end_time)) {
					start_time = "0";
					end_time = DateUtils.getTime(nowDay) + "";
				}
				String init = "";
				if (rankRule.getRule_check() != null) {
					init = rewardStatisticsService.getRule(rankRule, "", "0", "");
				}
				List<RankModel> dayRankList = borrowService.getRankListByTime(
					DateUtils.getTime(lastDay) + "", DateUtils.getTime(nowDay) + "", "", rule);
				request.setAttribute("dayRankList", dayRankList);
				List<RankModel> weekRankList = borrowService.getRankListByTime(
					DateUtils.getTime(lastWeek) + "", DateUtils.getTime(nowDay) + "", "", rule);
				request.setAttribute("weekRankList", weekRankList);
				List<RankModel> monthRankList = borrowService.getRankListByTime(
						DateUtils.getTime(lastMon) + "", DateUtils.getTime(nowDay) + "", "", rule);
				request.setAttribute("monthRankList", monthRankList);
				List<RankModel> totalRankList = borrowService.getRankListByTime(
						start_time, end_time, init, rule);
				request.setAttribute("totalRankList", totalRankList);
			}
		}
	}
	// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 end
	
//	/**
//	 * 首页滚动图片，友情链接，合作伙伴
//	 */
//	private List getRule(RuleModel ruleModel,int type){
//		List list=new ArrayList();
//		if(null!=ruleModel){
//			int status=ruleModel.getValueIntByKey("status");
//			if(1==status){
//				int num=ruleModel.getValueIntByKey("num");
//				 list = articleService.getScrollPicList(type,0, num);
//			     return list;
//			}
//		}
//		return list;
//	}
	
   private void checkParams(){
		model.setPageStart(NumberUtils.getInt(request.getParameter("page")));
		if(model.getOrder()<-4||model.getOrder()>4){model.setOrder(0);}
		if(model.getPageStart()<1){model.setPageStart(1);}
		if(model.getStatus()<1){model.setStatus(1);}
	}
	public String blank() throws Exception {
		borrowList=borrowService.getList();
		request.setAttribute("borrowList", borrowList);
		return "success";
	}
	
	/**
	 * 查看信用卡
	 * @return
	 * @throws Exception
	 */
	public String viewCreditCard() throws Exception {
		int cardId =Integer.valueOf(request.getParameter("cardId"));
		CreditCard b=creditCardService.getCardById(cardId);
		request.setAttribute("creditCard", b);
		if(b==null){
			this.message("非法操作！");
			return ADMINMSG;
		}
		return SUCCESS;
	}
	
	public BorrowService getBorrowService() {
		return borrowService;
	}
	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	public ArticleService getArticleService() {
		return articleService;
	}
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public CreditCardService getCreditCardService() {
		return creditCardService;
	}

	public void setCreditCardService(CreditCardService creditCardService) {
		this.creditCardService = creditCardService;
	}

	public QuickenLoansService getQuickenLoansService() {
		return quickenLoansService;
	}

	public void setQuickenLoansService(QuickenLoansService quickenLoansService) {
		this.quickenLoansService = quickenLoansService;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	public String getTimeType(){
		return null;
	}
	/**
	 * 快速申请贷款？
	 * @return
	 * @throws Exception
	 */
	public String addjk() throws Exception {
		String money=request.getParameter("money");
		String username=request.getParameter("username");
		String tel=request.getParameter("tel");
		String description=request.getParameter("description");
		String validcode=request.getParameter("validcode");
		//v1.6.7.2 快速借款通道  wfl 2013-12-05 start
		/*if(StringUtils.isNull(money).equals("")){
			String errormsg="所需资金不能为空";
			request.setAttribute("errormsg",errormsg);
			return "fail";
		}
		if(StringUtils.isNull(username).equals("")){
			String errormsg="称呼不能为空";
			request.setAttribute("errormsg", errormsg);
			return "fail";
		}
		if(StringUtils.isNull(tel).equals("")){
			String errormsg="联系方式不能为空";
			request.setAttribute("errormsg", errormsg);
			return "fail";
		}
		if(StringUtils.isNull(description).equals("")){
			String errormsg="说明不能为空";
			request.setAttribute("errormsg", errormsg);
			return "fail";
		}
		
		if (StringUtils.isNull(validcode).equals("")) {
			request.setAttribute("errormsg", "验证码不能为空！");
		//	return "fail";
		}
		 // if (!this.checkValidImg(validcode)) {
			//	request.setAttribute("errormsg", "验证码不正确！");
			//	return "fail";
		//	} 
		runBorrow=new RunBorrow();
		runBorrow.setMoney(money);
		runBorrow.setUsername(username);
		runBorrow.setDescription(description);
		runBorrow.setTel(tel);
		borrowService.addJk(runBorrow);
		message("添加成功");
		return "success";*/
		String descriptionType=request.getParameter("descriptionType");
		String msg ="添加成功,请等待客服联系";
		if(StringUtils.isNull(money).equals("")){
			msg="所需资金不能为空";
		}
		if(StringUtils.isNull(username).equals("")){
			msg="称呼不能为空";
		}
		if(StringUtils.isNull(tel).equals("")){
			msg="联系方式不能为空";
		}
		if(StringUtils.isNull(description).equals("")){
			msg=descriptionType+"不能为空";
		}
		if (StringUtils.isNull(validcode).equals("")) {
			msg="验证码不能为空！";
		}
		if("添加成功,请等待客服联系".equals(msg)){
			runBorrow=new RunBorrow();
			runBorrow.setMoney(money);
			runBorrow.setUsername(username);
			runBorrow.setDescription(description);
			runBorrow.setTel(tel);
			borrowService.addJk(runBorrow);
		}	
		message(msg);
		return MSG;
		//v1.6.7.2 快速借款通道  wfl 2013-12-05 end
	}
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
	/**
	 * 实时财务
	 * 
	 * @return forward页面
	 * @throws Exception 异常
	 */
	public String realTimeFinancial() throws Exception {

		// 取得统计数据
		Advanced advanced = borrowService.getAdvanced();
		request.setAttribute("advanced", advanced);
		// 取得列表数据
		int type = this.paramInt("type");
		type = (type == 0) ? 1 : type;
		int page = this.paramInt("page");
		SearchParam param = new SearchParam();
		PageDataList list = borrowService.getFinancialList(type, page, param);
		request.setAttribute("borrowList", list.getList());
		request.setAttribute("type", type);
		request.setAttribute("p", list.getPage());
		request.setAttribute("param", param.toMap());
		return "success";
	}
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	
	/**
	 * 贷款快速通道
	 * @return
	 * @throws Exception
	 */
	public String addQuickenLoans() throws Exception {
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String area = request.getParameter("area");
		String remark = request.getParameter("remark");
		QuickenLoans quickenLoans = new QuickenLoans();
		quickenLoans.setName(name);
		quickenLoans.setPhone(phone);
		quickenLoans.setArea(area);
		quickenLoans.setRemark(remark);
		quickenLoans.setCreateTime(this.getTimeStr());
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		if(actionType.equals("add")){
			quickenLoansService.addQuickenLoans(quickenLoans);
			message("添加成功，等待客服确认！", "");
//			return SUCCESS;
		}
//		request.setAttribute("quickenLoans", quickenLoans);
		return SUCCESS;
	}
	/**
	 * 及时雨更多排名
	 */
	public String moreRank() throws Exception{
		//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INVEST_RANKLIST.getValue()));
		String init = "";
		if(rule.getRule_check()!=null){
			// v1.6.6.2 RDPROJECT-287 zza 2013-10-22 start
			init = rewardStatisticsService.getRule(rule, "", "0", "");
			// v1.6.6.2 RDPROJECT-287 zza 2013-10-22 end
		}
		Date nowDay=DateUtils.getLastSecIntegralTime(new Date());
		Date lastDay=DateUtils.rollDay(nowDay, -1);
		Date lastWeek=DateUtils.rollDay(nowDay, -7);
		Date lastMon=DateUtils.rollMon(nowDay, -1);
		//排名显示条数
		int rankNum = NumberUtils.getInt(request.getParameter("rankNum"));
		rankNum = rankNum > 0 ? rankNum : 10;
		List dayMoreRankList=borrowService.getMoreRankListByTime(DateUtils.getTime(lastDay)+"",DateUtils.getTime(nowDay)+"",rankNum, init);
		request.setAttribute("dayMoreRankList", dayMoreRankList);
		List weekMoreRankList=borrowService.getMoreRankListByTime(DateUtils.getTime(lastWeek)+"",DateUtils.getTime(nowDay)+"",rankNum, init);
		request.setAttribute("weekMoreRankList", weekMoreRankList);
		List monthMoreRankList=borrowService.getMoreRankListByTime(DateUtils.getTime(lastMon)+"",DateUtils.getTime(nowDay)+"",rankNum, init);
		request.setAttribute("monthMoreRankList", monthMoreRankList);
		List<RankModel> totalMoreRankList=borrowService.getMoreRankListByTime("0",DateUtils.getTime(nowDay)+"",rankNum,init);
		request.setAttribute("totalMoreRankList", totalMoreRankList);
		return SUCCESS;
		//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
	}
	
}
