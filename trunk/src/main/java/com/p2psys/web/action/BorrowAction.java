package com.p2psys.web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumPic;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.AccountRecharge;
import com.p2psys.domain.Article;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.BorrowFlow;
import com.p2psys.domain.Rule;
import com.p2psys.domain.Site;
import com.p2psys.domain.Tender;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAmount;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.BorrowTender;
import com.p2psys.model.DetailUser;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.account.AccountModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.award.AwardInviteRechargeLog;
import com.p2psys.model.borrow.BorrowHelper;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.model.borrow.protocol.BorrowProtocol;
import com.p2psys.service.AccountService;
import com.p2psys.service.ArticleService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.RewardStatisticsService;
import com.p2psys.service.UserAmountService;
import com.p2psys.service.UserService;
import com.p2psys.tool.coder.MD5;
import com.p2psys.tool.interest.InterestCalculator;
import com.p2psys.tool.itext.PdfHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

public class BorrowAction extends BaseAction implements  ModelDriven<BorrowModel> {
	private static Logger logger = Logger.getLogger(BorrowAction.class);
	
	private BorrowService borrowService;
	private UserService userService;
	private AccountService accountService;
	private UserAmountService userAmountService;
	private ArticleService articleService;
	
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 start
	private RewardStatisticsService rewardStatisticsService;
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 end
	private String webid=Global.getValue("webid");
	private String pwd;
	private BorrowModel borrow = new BorrowModel();

	/**
	 * 我要投资的首页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String index() throws Exception {
		int page=NumberUtils.getInt(Global.getValue("index_other_num"));
		page=page>0?page:5;
		int ggpage=NumberUtils.getInt(Global.getValue("index_gonggao_num"));
		ggpage=ggpage>0?ggpage:5;
		List<Article> ggList = articleService.getList("22", 0, ggpage);
		request.setAttribute("ggList", ggList);
		// 媒体报道
		List<Article> bdList = articleService.getList("59", 0, page);
		request.setAttribute("bdList", bdList);
		
		// 常见问题
		List<Article> cjList = articleService.getList("11", 0, page);
		request.setAttribute("cjList", cjList);
		
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INDEX.getValue()));//首页规则
		//首页滚动图片
		RuleModel scrollPicRule=rule.getRuleByKey("scrollPic");
		List scrollPic =getRule(scrollPicRule,EnumPic.FIRST.getValue());
		request.setAttribute("scrollPic", scrollPic);
		return "index";
	}
	
	/**
	 * 首页滚动图片，友情链接，合作伙伴
	 */
	private List getRule(RuleModel ruleModel,int type){
		List list=new ArrayList();
		if(null!=ruleModel){
			int status=ruleModel.getValueIntByKey("status");
			if(1==status){
				int num=ruleModel.getValueIntByKey("num");
				 list = articleService.getScrollPicList(type,0, num);
			     return list;
			}
		}
		return list;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loan() throws Exception {		
		int borrowType = 0;
		String typeStr = StringUtils.isNull(request.getParameter("typeStr"));
		String borrowId=StringUtils.isNull(request.getParameter("borrowId"));
		//V1.6.6.1 RDPROJECT-137 wcw 2013-09-26 start
		// v1.6.5.5 RDPROJECT-8 wcw 2013-09-24 start 
		if(StringUtils.isBlank(borrowId)){
		// v1.6.5.5 RDPROJECT-8 wcw 2013-09-24 end 
			borrowType = Global.getBorrowType(StringUtils.isNull(typeStr));
			int jin_restrict = Global.getInt("jin_restrict");
			if (jin_restrict != 0 && borrowType == Constant.TYPE_PROPERTY) { //净值标限制是否启用，1启用，0不启用 并且发的标是净值标
				// 净值标发标的时候要做判断，如果该用户已经存在两个未满标复审通过的标便不能发标
				checkUnfinshJin();
			}
			//检查是否有标未满标,有则不能发标。
			//checkHasUnFinsh();
			//检查是否未完成的校验
			BorrowModel model = BorrowHelper.getHelper(borrowType, new BorrowModel());
			User sessionUser = userService.getDetailUser(getSessionUser().getUser_id());
			model.checkIdentify(sessionUser);
			request.setAttribute("btype", borrowType);
			
			//v1.6.7.2 RDPROJECT-473 sj 2013-12-19 start
			RuleModel r = new RuleModel(Global.getRule(EnumRuleNid.TENDERLIMIT.getValue()));
			if(r != null && r.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
				String convest_collection = r.getValueStrByKey("convest_collection");
				String most_tender_times = r.getValueStrByKey("most_tender_times");
				String tender_days = r.getValueStrByKey("tender_days");
				if("1".equals(convest_collection)){
					request.setAttribute("convestCollection", convest_collection);
				}
				if("1".equals(most_tender_times)){
					request.setAttribute("mostTenderTimes", most_tender_times);
				}
				if("1".equals(tender_days)){
					request.setAttribute("tenderDays", tender_days);
				}
			}
			//v1.6.7.2 RDPROJECT-473 sj 2013-12-19 end
			
			//获取用户账户资金
			UserAccountSummary summary = accountService.getUserAccountSummary(getSessionUser().getUser_id());
			request.setAttribute("summary", summary);
			request.setAttribute("borrow_type", "");
			request.setAttribute("typeStr", typeStr);
			// v1.6.5.5 RDPROJECT-8 wcw 2013-09-24 start 
		}else{
			long borrow_id=NumberUtils.getLong(borrowId);
			BorrowModel borrow=borrowService.getBorrow(borrow_id);
//			borrowType = Global.getBorrowType(borrow.getType());
		    request.setAttribute("borrow", borrow);
		    request.setAttribute("typeStr", typeStr);
		    request.setAttribute("btype", borrow.getType());
		}
		//获取审贷负责人
		List list=new ArrayList();
		list = userService.getVerifyUser();
		request.setAttribute("VerifyUser", list);
		// v1.6.5.5 RDPROJECT-8 wcw 2013-09-24 end 
		//V1.6.6.1 RDPROJECT-137 wcw 2013-09-26 end
		//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
		Rule rule=Global.getRule(EnumRuleNid.EXTENSION.getValue());
		//v1.6.7.2 RDPROJECT-602 wcw 2013-12-17 start
		if(rule!=null&&rule.getStatus()==EnumRuleStatus.RULE_STATUS_YES.getValue()){
		//v1.6.7.2 RDPROJECT-602 wcw 2013-12-17 end
    			RuleModel extensionRule=new RuleModel(rule);
    			int extension_enable=extensionRule.getValueIntByKey("extension_enable");
    			if(extension_enable==1){
    				request.setAttribute("extensionRule", extensionRule);
    			}
		}
		//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
		return "detail";
	}
	/**
	 * 增加新的标
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		BorrowModel model=null;
		User sessionUser=userService.getDetailUser(getSessionUser().getUser_id());
		//标种
		int btype=paramInt("btype");
		borrow.setType(btype);
		if(!StringUtils.isBlank(borrow.getBorrow_time())){
			borrow.setBorrow_time(DateUtils.getTimeStr(borrow.getBorrow_time(), "yyyy-MM-dd"));
		}
		model=BorrowHelper.getHelper(btype, borrow);
		model.checkIdentify(sessionUser);
		RuleModel rule=new RuleModel(Global.getRule(EnumRuleNid.BORROW_APR_LIMIT.getValue()));
		model.checkModelData(rule);
		//v1.6.6.2 RDPROJECT-360 liukun 2013-10-24 start
		//validate是被add和upate共用的，功能的扩充，validate肯定需要新旧数据对比来验证，发标的时候没有旧标数据，所以传入空
		validate(null);
		//v1.6.6.2 RDPROJECT-360 liukun 2013-10-24 start
		// v1.6.7.2 RDPROJECT-526 xx 2013-12-10 start
		// 根据标种/是否天标校验还款方式
		checkBorrowStyle();
		// v1.6.7.2 RDPROJECT-526 xx 2013-12-10 end
		if(btype!=114){
			fillBorrow(model);
		}else{
			if(sessionUser.getType_id()==3){
				throw new BorrowException("爱心捐助标必须是由平台所发");
			}
			fillDonation(model);
		}
		//生成资金记录对象
		AccountLog log=new AccountLog(getSessionUser().getUser_id(),Constant.FREEZE,getSessionUser().getUser_id(),
				this.getTimeStr(),this.getRequestIp());
		borrowService.addBorrow(model,log);
		
		if(model.getConfig()!=null && model.getConfig().getIs_trail()==1) {//不需要审核
			message("发布成功!","/member/index.html","进入用户中心>>");
		} else {
			message("发布成功，请等待审核!","/member/index.html","进入用户中心>>");
		}
		return SUCCESS;
	}
	
	// v1.6.7.2 RDPROJECT-526 xx 2013-12-10 start
	/**
	 * 根据标种/是否天标校验还款方式
	 * 后期改成关系型数据存储
	 */
	private void checkBorrowStyle(){
		int btype = borrow.getType();
		String styleStr = borrow.getStyle();
		int isday = borrow.getIsday();
		if(StringUtils.isBlank(styleStr)){
			throw new BorrowException("请选择还款方式！");
		}
		int style = Integer.parseInt(styleStr);
		int style0 = InterestCalculator.REPAY_MON_INSTALMENT;
		int style2 = InterestCalculator.REPAY_ONTIME;
		int style3 = InterestCalculator.REPAY_MON_INTEREST_END_CAPITAL;
		int style4 = InterestCalculator.REPAY_ADVANCE_INTEREST_END_CAPITAL;
		int style5 = InterestCalculator.REPAY_MONADVANCE_INTEREST_END_CAPITAL;
		int styleArr[] = null;
		if(btype==Constant.TYPE_SECOND && style != style2){//秒标
			throw new BorrowException("发布失败，还款方式设置错误，秒标只能为一次性还款！");
		}else if(isday==0){//月标
			if(btype==103 || btype==112){//抵押标、担保标
				styleArr = new int[4];
				styleArr[0] = style0;
				styleArr[1] = style2;
				styleArr[2] = style3;
				styleArr[3] = style5;
			} else {
				styleArr = new int[3];
				styleArr[0] = style0;
				styleArr[1] = style2;
				styleArr[2] = style3;
			}
		}else if(isday==1){//天标
			styleArr = new int[2];
			styleArr[0] = style2;
			styleArr[1] = style4;
		}
		if(styleArr==null || !ArrayUtils.contains(styleArr, style)){
			throw new BorrowException("发布失败，还款方式设置错误！");
		}
	}
	// v1.6.7.2 RDPROJECT-526 xx 2013-12-10 end
	
	/**
	 * 撤回
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		User user = this.getSessionUser();
		String id = request.getParameter("id");
		BorrowModel borrow = borrowService.getBorrow(Long.parseLong(id));
		// v1.6.6.1 RDPROJECT-230 zza 2013-09-27 start
//		if (borrow.getIs_flow() == 1) {
		if (borrow.getType() == Constant.TYPE_FLOW) {
			logger.info("如需撤回流转标，请联系平台客服:"+borrow.getStatus());
			message("如需撤回流转标，请联系平台客服！");
			return MSG;
		}
		// v1.6.6.1 RDPROJECT-230 zza 2013-09-27 end
		if(borrow.getStatus()!=1&&borrow.getStatus()!=0){
			logger.info("借款标的状态不允许撤回:"+borrow.getStatus());
			message("借款标的状态不允许撤回");
			return MSG;
		}
		borrow.setUser_id(user.getUser_id());
		borrow.setStatus(5);
		AccountLog log=new AccountLog(getSessionUser().getUser_id(),Constant.UNFREEZE,getSessionUser().getUser_id(),
				this.getTimeStr(),this.getRequestIp());
		borrowService.deleteBorrow(borrow,log);
		message("撤回成功！","/member/borrow/borrow.html");
		return MSG;
	}

	/**
	 * 更新标
	 * 
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception {
		boolean isOk=true;
		String checkMsg="";
		try {
			//v1.6.6.2 RDPROJECT-360 liukun 2013-10-24 start
			//为了后面的流程简化，这里直接将信用标占用的额度退回，检验大小后再重新占用
			String borrowId = request.getParameter("id");
			BorrowModel b = borrowService.getBorrow(Long.parseLong(borrowId));
			BorrowModel model= BorrowHelper.getHelper(b.getType(),b);
			HashMap oldBorrow = new HashMap();
			oldBorrow.put("account", b.getAccount());

			//v1.6.6.2 RDPROJECT-360 liukun 2013-10-24 end
			
			isOk=validate(oldBorrow);
			//v1.6.6.2 RDPROJECT-360 liukun 2013-10-24 start
			/*String borrowId = request.getParameter("id");
			BorrowModel b = borrowService.getBorrow(Long.parseLong(borrowId));*/
			//v1.6.6.2 RDPROJECT-360 liukun 2013-10-24 start
			BorrowModel wrapModel = BorrowHelper.getHelper(b.getType(),b);
			fillBorrow(wrapModel);
			//v1.6.6.2 RDPROJECT-360 liukun 2013-10-25 start
//			int btype=NumberUtils.getInt(request.getParameter("btype"));
//			BorrowModel model=BorrowHelper.getHelper(btype,borrow);
			//v1.6.6.2 RDPROJECT-360 liukun 2013-10-25 end
			RuleModel rule=new RuleModel(Global.getRule(EnumRuleNid.BORROW_APR_LIMIT.getValue()));
			model.checkModelData(rule);
			borrowService.updateBorrow(wrapModel.getModel(), oldBorrow);
		} catch (Exception e) {
			isOk=false;
			checkMsg=e.getMessage();
			logger.error(e.getMessage(), e.getCause());
		}
		if (isOk) {
			message("修改成功","/member/borrow/borrow.html?type=unpublish");
		}else{
			message(checkMsg, "/member/borrow/borrow.html?type=unpublish");
		}
		return SUCCESS;
	}
	//v1.6.6.2 RDPROJECT-360 liukun 2013-10-24 start
//	private boolean validate() {
	private boolean validate(HashMap oldBorrow) {
	//v1.6.6.2 RDPROJECT-360 liukun 2013-10-24 end
		//checkHasUnFinsh();
		
		if(!checkValidImg(StringUtils.isNull(request.getParameter("valicode")))){
			throw new BorrowException("验证码不正确！");
		}
		
		/*if(borrow.getIs_jin()==1&&(Global.getWebid().equals("huidai")||Global.getWebid().equals("hexindai"))){
			UserAccountSummary uas = accountService.getUserAccountSummary(getSessionUser().getUser_id());
			if (uas==null||uas.getAccountOwnMoney()<(borrowService.getRepayTotalWithJin(getSessionUser().getUser_id())+
						   NumberUtils.getDouble(borrow.getAccount()))) {
				throw new BorrowException("你的净资产小于净值标待还总额,发标失败!");
			}
		}*/
		
//		if(borrow.getIs_jin()==1){
		if(borrow.getType() == Constant.TYPE_PROPERTY){
			// v1.6.7.1 RDPROJECT-447 2013-11-12 start
			// 净值标发标的时候要做判断，如果该用户已经存在两个未满标复审通过的标便不能发标
			if (Global.getInt("jin_restrict")  != 0) {
				checkUnfinshJin();
			}
			// v1.6.7.1 RDPROJECT-447 2013-11-12 end
			RuleModel rule=new RuleModel(Global.getRule(EnumRuleNid.JIN_LIMIT.getValue()));
			RuleModel accountOwnMoneyRule=rule.getRuleByKey("accountOwnMoney");
			int status=accountOwnMoneyRule.getValueIntByKey("status");
			if(status==1){
				double accountOwnMoneyLimit=accountOwnMoneyRule.getValueDoubleByKey("accountOwnMoneyLimit");
				UserAccountSummary uas = accountService.getUserAccountSummary(getSessionUser().getUser_id());
				if(uas.getAccountOwnMoney()<accountOwnMoneyLimit){
					throw new BorrowException("你的净资产小于"+accountOwnMoneyLimit+"元,发标失败!");
				}
			}
			RuleModel repayTotalByJinRule=rule.getRuleByKey("repayTotalByJin");
			int repayTotalStatus=repayTotalByJinRule.getValueIntByKey("repayTotalStatus");
			if(repayTotalStatus==1){
				UserAccountSummary uas = accountService.getUserAccountSummary(getSessionUser().getUser_id());
				if (uas==null||uas.getAccountOwnMoney()<(borrowService.getRepayTotalWithJin(getSessionUser().getUser_id())+
							   borrow.getAccount())) {
					throw new BorrowException("你的净资产小于净值标待还总额,发标失败!");
				}
			}
		}
		
		// v1.6.7.1 RDPROJECT-350 xx 2013-11-07 start
		if(borrow.getType() == Constant.TYPE_PROPERTY && (Global.getWebid().equals("jsdai"))){
			//发净值标的账户成功充值总额减去已成功提现总额必须大于5万元才能发布   净值标。
			UserAccountSummary uas = accountService.getUserAccountSummary(getSessionUser().getUser_id());
			double userownmoney = NumberUtils.getDouble(StringUtils.isNull(Global.getValue("userownmoney")));
			if (uas==null||(uas.getRechargeTotal()-uas.getCashTotal()< userownmoney )) {
				throw new BorrowException("你的(成功充值总额-已成功提现总额)小于"+userownmoney+"元,发标失败!");
			}
			
			//在账户成功充值总额减去已成功提现总额大于5万元的情况下，本次净值标最大发标金额为（成功充值总额减去已成功提现总额）*净值借款倍率的3	倍再减去净值标待还总额。
			double borrowOwnmoneyRatio = NumberUtils.getDouble(StringUtils.isNull(Global.getValue("borrow_ownmoney_ratio")));
			if (uas==null||(borrowService.getRepayTotalWithJin(getSessionUser().getUser_id())+
					borrow.getAccount()> (uas.getRechargeTotal()-uas.getCashTotal())*borrowOwnmoneyRatio )) {
				throw new BorrowException("你本次发标金额和净值待还金额总额为"+(borrowService.getRepayTotalWithJin(getSessionUser().getUser_id())+
						borrow.getAccount())+"元,大于你的(成功充值总额-已成功提现总额)乘以净值借款倍率总额"+(uas.getRechargeTotal()-uas.getCashTotal())*borrowOwnmoneyRatio+"元,发标失败!");
			}
		}
		// v1.6.7.1 RDPROJECT-350 xx 2013-11-07 end 

		UserAmount amount=userAmountService.getUserAmount(getSessionUser().getUser_id());
		//V1.6.7.1 RDPROJECT-433 liukun 2013-11-07 start
//		if (borrow.getIs_xin() == 1 || borrow.getIs_student() == 1) {
//		if (borrow.getType() == Constant.TYPE_STUDENT) {
		if (borrow.getType() == Constant.TYPE_CREDIT || borrow.getType() == Constant.TYPE_STUDENT) {
		//V1.6.7.1 RDPROJECT-433 liukun 2013-11-07 end

			if (null != oldBorrow) {
				double oldAccount = Double.parseDouble(oldBorrow.get("account")
						.toString());
				double newAccount = borrow.getAccount();
				if (newAccount > oldAccount) {
					if (amount == null || amount.getCredit_use() < (newAccount - oldAccount)) {
						throw new BorrowException("可用信用额度不足!");
					}
				}
			} else {
				if (amount == null || amount.getCredit_use() < borrow.getAccount()) {
					throw new BorrowException("可用信用额度不足!");
				}
			}
		}
		return true;
	}

	private boolean checkHasUnFinsh() {
		List unfinshList=borrowService.unfinshBorrowList(getSessionUser().getUser_id());
		if(unfinshList!=null&&unfinshList.size()>0){
			throw new BorrowException("还存在未完成的标！");
		}
		return true;
	}
	
	/**
	 * 净值标发标时判断是否存在未完成的标
	 * @return
	 */
	private boolean checkUnfinshJin() {
		List<Borrow> unfinshJinList = borrowService.unfinshJinBorrowList(getSessionUser().getUser_id());
		//如果查询出来的条数不为空，并且等于后台配置的限制条数，提示还有未完成的标
		if(unfinshJinList != null && unfinshJinList.size() >= Global.getInt("jin_restrict")){
			throw new BorrowException("还存在未完成的标！");
		}
		return true;
	}
	
	/**
	 * 投标的方法
	 * @return
	 * @throws Exception
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public String tender() throws Exception {
		boolean isOk=true;
		String checkMsg="";
		User user = getSessionUser();
		long borrow_id=borrow.getId();
		//String dateStr=DateUtils.dateStr3(new Date());
		//String tenderFlag=user.getUser_id()+"_"+borrow_id+"_"+dateStr;
		//检查同一个用户是否结束投标，没有返回错误
		/*boolean isTendering=Global.TENDER_SET.add(tenderFlag);
		if(!isTendering){
			message("请勿重复投标！", "");
			return MSG;
		}*/
		try {
		logger.info("Begin tender! uid="+user.getUser_id()+",bid="+borrow_id+",tender_money="+borrow.getMoney());
		BorrowModel model=borrowService.getBorrow(borrow_id);
		BorrowModel wrapModel=BorrowHelper.getHelper(model.getType(),model);
		
		double tenderNum=borrow.getMoney();//表单提交为money,tender和borrow共有一个名称
		double tenderCount=borrow.getFlow_count();
		
		//获取投标人的账户信息
		Account act=borrowService.getAccount(user.getUser_id());
		//投标之前的各类校验
		double useMoney=act.getUse_money();
		if(tenderNum>useMoney){
			tenderNum=useMoney;
			borrow.setMoney(tenderNum);
		}
		String errormsg=checkTender(model,user,act,tenderNum,tenderCount);
		if(errormsg.equals("error")){
			//Global.TENDER_SET.remove(tenderFlag);
			return "error";
		}
		// 表单防重复提交
		String tokenMsg=checkToken("borrow_token");
		if(!StringUtils.isBlank(tokenMsg)){
			message(tokenMsg);
			return MSG;
		}
		
		//填充投标
		Tender tender=fillTender(wrapModel);
		//BorrowFlow flow=fillFlow(wrapModel);
		//生成资金记录对象
		/*AccountLog log=new AccountLog(user.getUser_id(),Constant.TENDER,model.getUser_id(),
				this.getTimeStr(),this.getRequestIp());
		log.setRemark(getLogRemark(wrapModel.getModel()));*/
		/*//验证码校验
		validate();*/
		// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 start
		if(model.getBorrowType() == Constant.TYPE_FLOW && !borrowService.checkFlowTime(model)){
			message("投标失败！超过流转标可流转次数,还剩"+ ((model.getFlow_time()*model.getFlow_count())-model.getFlow_totalyescount())+"份可以投标","/invest/detail.html?borrowid="+model.getId());
			return MSG;
		}
		// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 end
			//投标的核心方法
			//Protocol protocol=new Protocol(tender.getId(), Constant.TENDER_PROTOCOL, this.getTimeStr(), this.getRequestIp(), "");
			borrowService.addTender(tender, wrapModel, act);
			//是否启用借款协议发送到投资者邮箱,1是启用
			if(Global.getInt("protocol_toEmail_enable") == 1 
					// v1.6.6.2 RDPROJECT-106 yl 2013-10-21 start
					&& StringUtils.isNull(user.getEmail_status()).equals("1")
					// v1.6.6.2 RDPROJECT-106 yl 2013-10-21 end
					){
				//如果是流转标，需要生成借款协议并发送其到投资人邮箱
				if(model.getBorrowType() == Constant.TYPE_FLOW){
					Long tender_id = tender.getId();
					BorrowProtocol bp = new BorrowProtocol(user,borrow_id,tender_id);
					File pdfFile = new File(bp.getInPdfName());
					if(pdfFile.exists()){
						try {
							bp.createPdf();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//发送邮件
					this.sendMailWithAttachment(user,borrow_id,tender_id);
				}
			}
		} catch (BorrowException e) {
			isOk=false;
			checkMsg=e.getMessage();
			logger.error(e.getMessage(), e.getCause());
		} catch (Exception e) {
			isOk=false;
			checkMsg="系统繁忙，投标失败,请稍后再试！";
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally{
			//Global.TENDER_SET.remove(tenderFlag);
			logger.info("End tender! uid="+user.getUser_id()+",bid="+borrow_id+",tender_money="+borrow.getMoney());
		}
		if (isOk) {
			Borrow bl= borrowService.getBorrow(borrow_id);
//			if(bl.getIs_jin()==1){
			if(bl.getType() == Constant.TYPE_PROPERTY){
				if(bl.getAccount()==bl.getAccount_yes()){
				    bl.setVerify_time(new Date().getTime()/1000+"");
				    bl.setVerify_remark("净值标满标自动审核！");
				    borrowService.updateJinBorrow(bl);
				}
			}
			message("投标成功！","/invest/detail.html?borrowid="+borrow.getId());
			logger.info("Show tender success msg! uid="+user.getUser_id()+",bid="+borrow_id+",tender_money="+borrow.getMoney());
		}else{
			message(checkMsg, "");
			logger.info("Show tender fail msg! uid="+user.getUser_id()+"bid="+borrow_id+",tender_money="+borrow.getMoney());
		}
		//提示信息
		if(Global.getWebid().equals("ssjb")){
			//邀请好友第一次充值奖励是否给邀请人
			SearchParam param=new SearchParam();
			param.setStatus("1");
			param.setUsername(user.getUsername());
			List list=accountService.getRechargeList(param);
			if(list.size()>0){	
				AccountRecharge accountRecharge=accountService.getMinRechargeMoney(user.getUser_id());
				if(accountRecharge.getYes_no()==0){
					double minRechargeMoney=accountRecharge.getMoney();
					String min_recharge_award_apr_string=Global.getValue("min_recharge_award_apr");
					double min_recharge_award_apr=NumberUtils.getDouble(min_recharge_award_apr_string);
					double minRechargeAward=minRechargeMoney*min_recharge_award_apr;
					String invite_userid=user.getInvite_userid();
					User newuser=userService.getUserById(NumberUtils.getLong(invite_userid));
					if(!StringUtils.isBlank(invite_userid)){
					     accountService.updateAccount(minRechargeAward, minRechargeAward, 0.00, NumberUtils.getLong(invite_userid));
					    AccountModel inviteAccount= accountService.getAccount(NumberUtils.getLong(invite_userid));
					     Global.setTransfer("award", minRechargeAward);
					     Global.setTransfer("username", newuser.getUsername());
					     BaseAccountLog bLog = new AwardInviteRechargeLog(minRechargeAward, inviteAccount);
					     bLog.doEvent();
					     accountRecharge.setYes_no(1);
					     accountService.updateAccountRechargeYes_no(accountRecharge);
					}
				}
			}
		}
		return SUCCESS;
	}
	
	private int createPdf(String pdfName,Borrow b) throws Exception{
		int size=0;
		PdfHelper pdf=PdfHelper.instance(pdfName);
		User user=getSessionUser();
	   if(b==null){
		  throw new BorrowException("该借款标不存在！");
	   }
	   if(webid!=null&&webid.equals("mdw")&&b.getType() == Constant.TYPE_FLOW){
			List<BorrowTender> list=borrowService.getTenderList(b.getId(), user.getUser_id());
			for(BorrowTender bt:list){
				 addPdfHeader(pdf,b,bt);
		         addPdfContent(pdf,b,bt);
			}
	   }else{
	             addPdfHeader(pdf,b);
	             addPdfTable(pdf,b);
	             addPdfContent(pdf,b);
	   }
	   pdf.exportPdf();
	   return size;
	}
	private void addPdfHeader(PdfHelper pdf,Borrow b) throws DocumentException{
		DetailUser u = userService.getDetailUser(b.getUser_id());
		User user=getSessionUser();
		//String webid=Global.getValue("webid");
		String title="借款协议书\n";
		String wztitle="合同编号: MDW-"+DateUtils.dateStr2(b.getAddtime()) +b.getId()+"\n";
//		if(webid!=null&&webid.equals("mdw")&&b.getIs_flow()==1){
		if(webid!=null&&webid.equals("mdw")&&b.getType() == Constant.TYPE_FLOW){
		 pdf.addTitle(wztitle);	
		}else{
		pdf.addTitle(title);
		}
		String content=Html2Text(b.getContent());
		String h="借款协议号："+b.getId()+"     借款人："+u.getRealname()+"     出借人：详见本协议第一条 \n签订日期："+DateUtils.dateStr2(BorrowHelper.getBorrowProtocolTime(b)) +
	   		"\n借款人通过"+Global.getValue("webname")+"网站(以下简称\"本网站\")的居间,就有关借款事项与各出借人达成如下协议：\n" +
	   		"第一条：借款详情如下表所示：\n";
		
		String p="借款协议号："+b.getId()+"    借款人（以下简称乙方）："+u.getRealname()+"  出借人（以下简称甲方）：详见本协议第一条\n"+
		"签订日期："+DateUtils.dateStr2(BorrowHelper.getBorrowProtocolTime(b)) +
		"借款人通过"+Global.getValue("webname")+"网站(以下简称\"本网站\")的居间服务,就有关借款事项与各出借人达成如下协议：\n\n" +
				"鉴于：\n " +
				"1、丙方是一家在厦门湖里区合法成立并有效存续的有限责任公司，拥有www.youyoudai.com 网站（以下简称“该网站”）的经营权，提供信用咨询，为交易提供信息服务；" +
				"2、乙方已在该网站注册，并承诺其提供给丙方的信息是完全真实的；" +
				"3、甲方承诺对本协议涉及的借款具有完全的支配能力，是其自有闲散资金，为其合法所得；并承诺其提供给丙方的信息是完全真实的；" +
				"4、乙方有借款需求，甲方亦同意借款，双方有意成立借贷关系；" +
				"第一条 借款详细信息 （注：因计算中存在四舍五入，最后一期应收本息与之前略有不同）";

		String q="借款协议号："+b.getId()+"     借款人："+StringUtils.hideStr(u.getRealname(),0,1)+"     出借人：详见本协议第一条 \n签订日期："+DateUtils.dateStr2(BorrowHelper.getBorrowProtocolTime(b)) +
		   		"\n借款人通过"+Global.getValue("webname")+"网站(以下简称\"本网站\")的居间,就有关借款事项与各出借人达成如下协议：\n" +
		   		"第一条：借款详情如下表所示：\n";
		
	    if(webid!=null&&webid.equals("yydai")){
			pdf.addText(p);
		}else if(webid!=null&&webid.equals("ssjb")){
			pdf.addText(q);
		}else{
			pdf.addText(h);
		}
		
	}
	
	private void addPdfHeader(PdfHelper pdf,Borrow b,BorrowTender bt) throws DocumentException{
		logger.debug("borrow_time===="+b.getBorrow_time());
		logger.debug("borrow_time===="+DateUtils.dateStr2(b.getBorrow_time()));
		DetailUser u = userService.getDetailUser(b.getUser_id());
		User user=getSessionUser();
		String wztitle="合同编号: MDW-"+DateUtils.dateStr2(b.getAddtime()) +b.getId()+"\n";
		 pdf.addTitle(wztitle);	
		String content=Html2Text(b.getContent());
				if(bt.getUser_id()==user.getUser_id()){
					 String w="债权转让及回购协议\n"+"甲方(债权人)：沈**\n"+"乙方(第三人)："+user.getRealname()+"\n"+
								"丙方(担保人)：深圳市鼎和资产管理有限公司\n"+"1.借款条款\n"+"（1）"+DateUtils.dateStr2(b.getBorrow_time())+"，甲方和"+u.getRealname()+"签订借款协议（见附件一），甲方出借给"+u.getRealname()+"人民币"+b.getBorrow_account()+"元。\n"+
							"截止本协议签订之日，"+u.getRealname()+"尚欠甲方人民币"+b.getBorrow_account()+"元未归还,借款期限"+b.getBorrow_time_limit()+""+
							"个月，还款方式为到期一次性返本金和利息。\n"+"（2）现甲方将以上债权中的部分债权计人民币（￥"+bt.getAccount()+"）元（“出让债权”）转让给乙方，借款利息"+b.getApr()+"%，并承诺"+
							"并保证其转让的债权系合法、有效。乙方表示愿意受让上述部分债权。\n"+
							"（3）在本协签订之日，甲方应按照附件二的格式给"+u.getRealname()+"发出债权转让通知书，并确认债务人收到本债"+
							"权转让通知书。\n"+"（4）上述转让自本协议签订之日起立即生效。借款利息拆分将按天计算，本协议签订之前的利息归甲"+
							"方所有，本协议签订之后的利息将归乙方所有。\n"+"2.回购条款。\n"+
							"自本协议签订"+b.getTime_limit()+"个月后，甲方承诺将无条件从乙方回购出让债权。在回购前，出让债权的利息归乙方所"+
							"有。在甲方回购后，乙方将不在享有出让债权的任何权利。\n"+
							"丙方保证甲方将按照本协议约定条款回购乙方受让的债权，如甲方违约，丙方将代甲方履行回购义务"+
							"。\n"+"3.陈述与承诺\n"+"甲方明确声明本协议项下的债权无任何第三人主张权利，且权利不受限制，即不存在被法院保全、查"+
							"封或强制执行的情况或已设担保。签订本协议之前，甲方没有与第三方签订过本协议项下债权转让合"+
							"同。\n"+"4.保证其提供的所有证照、资料真实、准确、完整、合法；\n"+
							"5.违约责任\n"+"各方同意，如果一方违反其在本协议中所作的承诺或任何其他义务，致使其他方遭受或发生损害、损"+
							"失、索赔、处罚、诉讼仲裁、费用、义务和/或责任，违约方须向另一方作出全面补偿。\n"+"6.其他规定\n"+"（1）对本协议所作的任何修改及补充，必须采用书面形式并由各方签署。\n"+
							"（2）在本协议履行过程中发生的纠纷，双方应友好协商解决;协商不成的，任何一方均有权向深圳市"+
							"人民法院提请诉讼。\n"+
							"（3）本协议自双方签字盖章后生效，一式三份，甲乙丙三方各执一份，具有同等法律效力。\n"+"甲方　　　沈**\n"+
							"乙方　　　"+user.getRealname()+"\n"+
							"丙方　　　深圳市鼎和资产管理有限公司\n"+
							"签订地点：广东省深圳市\n"+
							"签订日期："+DateUtils.dateStr2(bt.getAddtime())+"\n\n\n\n"+
							"附件一：借款协议\n"+
					        "借出人：沈**\n"+
					        "借入人："+u.getRealname()+"\n"+
							"担保人：深圳市鼎和资产管理有限公司\n"+
							"重要提示：\n"+
							"借出人、借入人、保证人请认真阅读本协议项下的全部条款。借出人、借入人、保证人，一旦签订本协议，即认为借入人、保证人已理解并同意本协议的所有条款。\n"+
							 "1.借贷条款\n"+
							 "（1）贷款金额：人民币￥"+b.getBorrow_account()+"元。\n"+
							 "（2）贷款期限："+b.getBorrow_time_limit()+"个月。\n"+
							 "（3）还款方式：一次性返本息。";
					   pdf.addText(w);
		}
		
	}
	private   String Html2Text(String inputString) { 
        String htmlStr = inputString; //含html标签的字符串 
            String textStr =""; 
      java.util.regex.Pattern p_script; 
      java.util.regex.Matcher m_script; 
      java.util.regex.Pattern p_style; 
      java.util.regex.Matcher m_style; 
      java.util.regex.Pattern p_html; 
      java.util.regex.Matcher m_html; 
   
      try { 
       String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> } 
       String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> } 
          String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式 
      
          p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
          m_script = p_script.matcher(htmlStr); 
          htmlStr = m_script.replaceAll(""); //过滤script标签 

          p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
          m_style = p_style.matcher(htmlStr); 
          htmlStr = m_style.replaceAll(""); //过滤style标签 
      
          p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
          m_html = p_html.matcher(htmlStr); 
          htmlStr = m_html.replaceAll(""); //过滤html标签 
      
       textStr = htmlStr; 
      
      }catch(Exception e) { 
               System.err.println("Html2Text: " + e.getMessage()); 
      } 
   
      return textStr;//返回文本字符串 
       }   

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addPdfTable(PdfHelper pdf,Borrow b) throws DocumentException{
		List<BorrowTender> list=borrowService.getTenderList(borrow.getId());
		   List cellList=null;
		   List[] args=new List[list.size()+1];
		//   出借人(id)							
		   cellList=new ArrayList();
		   cellList.add("出借人(id)");
		   cellList.add("借款金额");
		   cellList.add("借款期限");
		   cellList.add("年利率");
		   cellList.add("借款开始日");
		   cellList.add("借款到期日");
		   cellList.add("截止还款日");
		   cellList.add("还款本息");
		   args[0]=cellList;
		   for(int i=1;i<list.size()+1;i++){
			   BorrowTender t=list.get(i-1);
			   cellList=new ArrayList();
			   cellList.add(t.getUsername());
			   cellList.add(t.getAccount()+"元");
			   if(b.getIsday()==1){
				   cellList.add(b.getTime_limit_day()+"天");
			   }else{
				   cellList.add(b.getTime_limit()+"个月");
			   }
			   cellList.add(b.getApr()+"%");
			   cellList.add(DateUtils.dateStr2(BorrowHelper.getBorrowVerifyTime(b,t)));
			   Date d=BorrowHelper.getBorrowRepayTime(b,t);
			   cellList.add(DateUtils.dateStr2(d));
			   cellList.add("每月截止"+DateUtils.getDay(d)+"日");
			   cellList.add(NumberUtils.ceil(Double.valueOf(t.getRepayment_account())));
			   args[i]=cellList;
		   }
		   pdf.addTable(args, 80, 7);
	}
	private int addPdfContent(PdfHelper pdf,Borrow b) throws DocumentException{
		int size=0;
			Site site=articleService.getSiteById(32);
			String content=site.getContent();
			pdf.addText(content);
			if(StringUtils.isNull(webid).equals("xdcf")||StringUtils.isNull(webid).equals("jsy")||StringUtils.isNull(webid).equals("mszb")){
				try {
					Image image = Image.getInstance(""+Global.getValue("weburl")+Global.getValue("theme_dir")+"/images/zhang.jpg");
				pdf.addImage(image);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		size=pdf.getPageNumber();
		return size;
	}
	
	private int addPdfContent(PdfHelper pdf,Borrow b,BorrowTender bt) throws DocumentException{
		int size=0;
			Site site=articleService.getSiteById(31);
			String content=site.getContent();
			pdf.addText(content);
			DetailUser u = userService.getDetailUser(b.getUser_id());
			User user=getSessionUser();
				if(bt.getUser_id()==user.getUser_id()){
					String endtext="借入人："+u.getRealname()+"\n"+
                            "保证人：深圳市鼎和资产管理有限公司\n"+
                            "协议签订地：广东省深圳市\n" +	
                            "协议签署日期："+DateUtils.dateStr2(b.getBorrow_time())+"\n\n附件二：债权转让通知书\n"+""+u.getRealname()+"：\n"+
                            "根据我方与贵方在"+DateUtils.dateStr2(b.getBorrow_time())+"签订的《借款协议》，我方向贵方出借了人民币\n"+
                            "￥"+b.getBorrow_account()+"元借款，目前还在借款过程中。我方决定将上述债权中部分合计人民币（"+bt.getAccount()+"）元的\n"+
                            "债权转让给第三方自然人("+user.getRealname()+")，上述转让立即生效。借款利息拆分将按天结算，今天之前的利息\n"+
                            "归我	方所有，今天之后的利息将归新债权人所有。\n"+"特此告知！\n"+"沈**\n"+""+DateUtils.dateStr2(bt.getAddtime())+"";
                   pdf.addText(endtext);
			}
			try {
				Image image = Image.getInstance(""+Global.getValue("weburl")+Global.getValue("theme_dir")+"/images/zhang.jpg");
			pdf.addImage(image);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		size=pdf.getPageNumber();
		return size;
	}
	public String getWebid() {
		return webid;
	}

	public void setWebid(String webid) {
		this.webid = webid;
	}

	public String protocol(){
		String type=paramString("typeStr");
		if(type.equals("new")){
			return newprotocol();
		}
		User user=getSessionUser();
		boolean isOk=true;
		String checkMsg="";
		
		//检查改用户是否已经投资此借款标,否则无权访问
		/*double hasTenderMoney=borrowService.hasTenderTotalPerBorrowByUserid(borrow.getId(), this.getSessionUser().getUser_id());
		if(hasTenderMoney<=0){
			message("您不是投资人，无权访问该借款协议书！","");
			return MSG;
		}*/
		
		double hasTenderMoney=borrowService.hasTenderTotalPerBorrowByUserid(borrow.getId(), this.getSessionUser().getUser_id());
		int  borrowCount =borrowService.hasBorrowCountByUserid(borrow.getId(), this.getSessionUser().getUser_id());

		// v1.6.7.2 if和else里面的逻辑一样 lhm 2013-12-11 start
//		String webid=Global.getValue("webid");
//		if(webid!=null&&webid.equals("jsy")){
//			if(hasTenderMoney<=0 && borrowCount <=0){
//				message("您不是投资人或借款人，无权访问该借款协议书！","");
//				return MSG;
//			}
//			
//		}else {
//			//v1.6.6.2 RDPROJECT-75 liukun 2013-10-24 start
//			/*if(hasTenderMoney<=0){
//				message("您不是投资人，无权访问该借款协议书！","");
//				return MSG;
//			}*/
//			if(hasTenderMoney<=0 && borrowCount <=0){
//				message("您不是投资人或借款人，无权访问该借款协议书！","");
//				return MSG;
//			}
//			//v1.6.6.2 RDPROJECT-75 liukun 2013-10-24 end
//		}
//		
		if(hasTenderMoney<=0 && borrowCount <=0){
			message("您不是投资人或借款人，无权访问该借款协议书！","");
			return MSG;
		}
		// v1.6.7.2 if和else里面的逻辑一样 lhm 2013-12-11 end
		
		String contextPath = ServletActionContext.getServletContext().getRealPath("/");
		String downloadFile=borrow.getId()+".pdf";
		String inPdfName=contextPath+"/data/protocol/"+borrow.getId()+".pdf";
		String image=""+Global.getValue("weburl")+Global.getValue("theme_dir")+"/images/zhang.jpg";
		String outPdfName=contextPath+"data/protocol/"+borrow.getId()+".pdf";
		logger.info(inPdfName);
		BorrowModel borrowModel=borrowService.getBorrow(borrow.getId());
		
		File pdfFile=new File(inPdfName);
		if(pdfFile.exists()){
			boolean flug=pdfFile.delete();
			if(flug==true){
				try {
					int size=0;
					size=createPdf(inPdfName,borrowModel);
					//PdfHelper.addPdfMark(inPdfName, outPdfName, contextPath+"data/protocol/pool.jpg",size);
				} catch (Exception e) {
					isOk=false;
					checkMsg="生成pdf文件出错！";
					logger.info(checkMsg+"-3-"+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		if(!pdfFile.exists()||borrowModel.getType() == Constant.TYPE_FLOW){
			try {
				int size=0;
				size=createPdf(inPdfName,borrowModel);
			//	PdfHelper.addPdfMark(inPdfName, outPdfName, contextPath+"data/protocol/pool.jpg",size);
			} catch (Exception e) {
				isOk=false;
				checkMsg="生成pdf文件出错！";
				logger.info(checkMsg+"-4-"+e.getMessage());
				e.printStackTrace();
			}
		}
		if(!isOk){
			message(checkMsg);
			return MSG;
		}
		InputStream ins=null;
		  try {
			  //PdfHelper pdfHelper=new PdfHelper(inPdfName);
			 // pdfHelper.addPdfMark(inPdfName, inPdfName, image, 3);
			  ins= new BufferedInputStream(new FileInputStream(inPdfName));
			  byte [] buffer = new byte[ins.available()];
			  ins.read(buffer);
			  ins.close();
			  HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
			  response.reset();
			  response.addHeader("Content-Disposition", "attachment;filename=" + new String(downloadFile.getBytes()));
			  response.addHeader("Content-Length", "" + pdfFile.length());
			  OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			  response.setContentType("application/octet-stream");
			  ous.write(buffer);
			  ous.flush();
			  ous.close();
		} catch (FileNotFoundException e) {
			logger.error("协议pdf文件"+downloadFile+"未找到！");
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String newprotocol() {
		User user=getSessionUser();
		boolean isOk=true;
		String checkMsg="";
		//检查改用户是否已经投资此借款标,否则无权访问
		double hasTenderMoney=borrowService.hasTenderTotalPerBorrowByUserid(borrow.getId(), this.getSessionUser().getUser_id());
		int  borrowCount =borrowService.hasBorrowCountByUserid(borrow.getId(), this.getSessionUser().getUser_id());

		// v1.6.7.2 if和else里面的逻辑一样 lhm 2013-12-11 start
//		String webid=Global.getValue("webid");
//		if(webid!=null&&webid.equals("jsy")){
//			if(hasTenderMoney<=0 && borrowCount <=0){
//				message("您不是投资人或借款人，无权访问该借款协议书！","");
//				return MSG;
//			}
//			
//		}else {
//			//v1.6.6.2 RDPROJECT-75 liukun 2013-10-24 start
//			/*if(!(hasTenderMoney>0||user.getUser_id()==borrow.getUser_id())){
//				message("您不是投资人，无权访问该借款协议书！","");
//				return MSG;
//			}*/
//			if(hasTenderMoney<=0 && borrowCount <=0){
//				message("您不是投资人或借款人，无权访问该借款协议书！","");
//				return MSG;
//			}
//			//v1.6.6.2 RDPROJECT-75 liukun 2013-10-24 end
//		}
		if(hasTenderMoney<=0 && borrowCount <=0){
			message("您不是投资人或借款人，无权访问该借款协议书！","");
			return MSG;
		}
		// v1.6.7.2 if和else里面的逻辑一样 lhm 2013-12-11 end
		
		Long tender_id=paramLong("tender_id");
		BorrowProtocol bp=new BorrowProtocol(user,borrow.getId(),tender_id);

		File pdfFile=new File(bp.getInPdfName());
		if(pdfFile.exists()){
			try {
				bp.createPdf();
			} catch (Exception e) {
				isOk=false;
				checkMsg="生成pdf文件出错！";
				logger.info(checkMsg+"-5-"+e.getMessage());
				e.printStackTrace();
			}
		}
		if(!isOk){
			message(checkMsg);
			return MSG;
		}
		try {
			generateDownloadFile(bp.getInPdfName(),bp.getDownloadFileName());
		} catch (FileNotFoundException e) {
			logger.error("协议pdf文件"+bp.getDownloadFileName()+"未找到！");
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String checkTender(Borrow b,User u,Account act,double tenderNum,double tenderCount){
		u = userService.getDetailUser(u.getUser_id());
		String url="/invest/detail.html?borrowid="+b.getId();
		String pwd = paramString("pwd");
		
		if(StringUtils.isNull(Global.getValue("webid")).equals(Constant.HUIZHOUDAI)){
			if(StringUtils.isBlank(request.getParameter("valicode"))){
				throw new BorrowException("验证码不能为空！",url);
			}
			if(!checkValidImg(StringUtils.isNull(request.getParameter("valicode")))){
				throw new BorrowException("验证码不正确！",url);
			}
		}
		if(StringUtils.isNull(borrow.getPaypassword()).equals("")){
			throw new BorrowException("支付交易密码不能为空! ",url);
		}
		if(!StringUtils.isBlank(b.getPwd())){
			if(StringUtils.isNull(pwd).equals("")){
				throw new BorrowException("定向标密码不能为空! ",url);
			}
			if (!b.getPwd().equals(pwd)) {
				throw new BorrowException("定向标密码不正确! ",url);
			}
		}
		if(StringUtils.isNull(u.getPaypassword()).equals("")){
			throw new BorrowException("请先设一个支付交易密码! ",url);
		}
		MD5 md5=new MD5();
		if(!md5.getMD5ofStr(borrow.getPaypassword()).equals(u.getPaypassword())){
			throw new BorrowException("支付交易密码不正确! ",url);
		}
		return "";
	}
	
	private Tender fillTender(BorrowModel b){
		//汇款续投标记，为1说明此次为汇款续投
		int auto_repurchase=NumberUtils.getInt(request.getParameter("auto_repurchase"));
		//投标奖励推后发放
		int award_after_push=NumberUtils.getInt(request.getParameter("award_after_push"));
		BorrowModel model=b.getModel();
		User user=getSessionUser();
		Tender tender=new Tender();
		tender.setBorrow_id(model.getId());
		if(model.getType() == Constant.TYPE_FLOW){
			tender.setMoney(borrow.getFlow_count()*model.getFlow_money()+"");
			tender.setStatus(1);
		}else{
			tender.setMoney(borrow.getMoney()+"");
			tender.setStatus(0);
		}
		tender.setAddtime(new Date().getTime()/1000+"");
		tender.setAddip(this.getRequestIp());
		tender.setUser_id(user.getUser_id());
		tender.setAuto_repurchase(auto_repurchase);
		tender.setAward_after_push(award_after_push);
		return tender;
	}
	
	private BorrowFlow fillFlow(BorrowModel b){
		BorrowModel model=b.getModel();
		User user=getSessionUser();
		BorrowFlow flow=new BorrowFlow();
		flow.setBorrow_id(model.getId());
		Date now=Calendar.getInstance().getTime();
		Date back=DateUtils.rollDate(now, 0, NumberUtils.getInt(model.getTime_limit()), 0);
		flow.setBuy_time(now.getTime()/1000);
		flow.setBack_time(back.getTime()/1000);
		flow.setUser_id(user.getUser_id());
		flow.setFlow_count(borrow.getFlow_count());
		flow.setInterest(NumberUtils.format4(b.calculateInterest())+"");
		flow.setAddip(this.getRequestIp());
		return flow;
	}
	
	private Borrow fillBorrow(BorrowModel model){
		BorrowModel b=model.getModel();
		User user = this.getSessionUser();
		b.setUser_id(user.getUser_id());
		// 借款信息
		b.setType(b.getType());
		b.setAccount(borrow.getAccount());
		b.setAccount_yes(0);
		b.setName(borrow.getName());
		b.setContent(borrow.getContent());
		b.setUse(borrow.getUse());
		b.setLowest_account(borrow.getLowest_account());
		b.setMost_account(borrow.getMost_account());
		b.setValid_time(borrow.getValid_time());
		b.setPwd(StringUtils.isNull(borrow.getPwd()));
		// 投标奖励
		b.setAward(borrow.getAward());
		b.setFunds(borrow.getFunds());
		b.setPart_account(borrow.getPart_account());
		// 担保奖励
		b.setVouch_user(StringUtils.isNull(borrow.getVouch_user()));
		b.setVouch_award(StringUtils.isNull(borrow.getVouch_award()));
		b.setVouch_account(StringUtils.isNull(borrow.getVouch_account()));
		b.setRepayment_account(0);
		
		// 信息公开
		b.setOpen_tender(borrow.getOpen_tender());
		// v1.6.5.3 RDPROJECT-107  zza 2013-09-11 start
		b.setOpen_borrow(borrow.getOpen_borrow());
		b.setOpen_account(borrow.getOpen_account());
		b.setOpen_credit(borrow.getOpen_credit());
		// v1.6.5.3 RDPROJECT-107  zza 2013-09-11 end
		
		//还款金额
		InterestCalculator ic=model.interestCalculator();
		double repayAccount=ic.getTotalAccount();
		b.setRepayment_account(repayAccount);
		//年利率
		b.setApr(borrow.getApr());
		//还款方式
		b.setStyle(StringUtils.isNull(borrow.getStyle()));
		// IP
		b.setAddip(this.getRequestIp());
		b.setAddtime(this.getTimeStr());
		// v1.6.5.5 RDPROJECT-8 wcw 2013-09-24 start 
		//借款期限
		b.setIsday(borrow.getIsday());
		// v1.6.5.5 RDPROJECT-232 wcw 2013-09-26 start 
		if(borrow.getIsday()==1){
			borrow.setTime_limit("1");
		}else{
			borrow.setTime_limit_day(0);
		}
		b.setFlow_money(borrow.getFlow_money());
		// v1.6.5.5 RDPROJECT-232 wcw 2013-09-26 end 
		b.setTime_limit(borrow.getTime_limit());
		b.setTime_limit_day(borrow.getTime_limit_day());
		b.setLate_award(borrow.getLate_award());
		// v1.6.5.5 RDPROJECT-8 wcw 2013-09-24 end 
		//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
		b.setExtension_day(borrow.getExtension_day());
		//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
		//v1.6.6.2 RDPROJECT-333 wcw 2013-10-19 start
		b.setLowest_single_limit(borrow.getLowest_single_limit());
		b.setMost_single_limit(borrow.getMost_single_limit());
		b.setRepayment_yesaccount(0);
		//v1.6.6.2 RDPROJECT-333 wcw 2013-10-19 end
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 start
		b.setCollateral(borrow.getCollateral());
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 end
		return b;
	}
	
	private Borrow fillDonation(BorrowModel model){
		BorrowModel b=model.getModel();
		User user = this.getSessionUser();
		b.setUser_id(user.getUser_id());
		// 借款信息
		b.setType(b.getType());
		b.setAccount(borrow.getAccount());
		b.setAccount_yes(0);
		b.setName(borrow.getName());
		b.setContent(borrow.getContent());
		b.setUse(borrow.getUse());
		b.setLowest_account(borrow.getLowest_account());
		b.setMost_account(borrow.getMost_account());
		b.setValid_time(borrow.getValid_time());
		b.setPwd(StringUtils.isNull(borrow.getPwd()));
		// 投标奖励
		b.setAward(borrow.getAward());
		b.setFunds(borrow.getFunds());
		b.setPart_account(borrow.getPart_account());
		// 担保奖励
		b.setVouch_user(StringUtils.isNull(borrow.getVouch_user()));
		b.setVouch_award(StringUtils.isNull(borrow.getVouch_award()));
		b.setVouch_account(StringUtils.isNull(borrow.getVouch_account()));
		b.setRepayment_account(0);
		
		// 信息公开
		b.setOpen_tender(borrow.getOpen_tender());
		// IP
		b.setAddip(this.getRequestIp());
		b.setAddtime(this.getTimeStr());
		
		b.setLate_award(borrow.getLate_award());

		return b;
	}
	
	@Override
	public BorrowModel getModel() {
		return borrow;
	}

	public BorrowService getBorrowService() {
		return borrowService;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public UserAmountService getUserAmountService() {
		return userAmountService;
	}

	public void setUserAmountService(UserAmountService userAmountService) {
		this.userAmountService = userAmountService;
	}

	public ArticleService getArticleService() {
		return articleService;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public RewardStatisticsService getRewardStatisticsService() {
		return rewardStatisticsService;
	}

	public void setRewardStatisticsService(
			RewardStatisticsService rewardStatisticsService) {
		this.rewardStatisticsService = rewardStatisticsService;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
