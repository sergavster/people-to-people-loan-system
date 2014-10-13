package com.p2psys.web.action.admin;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.p2psys.common.enums.EnumBorrow;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.BorrowConfig;
import com.p2psys.domain.OperationLog;
import com.p2psys.domain.Protocol;
import com.p2psys.domain.Repayment;
import com.p2psys.domain.Reservation;
import com.p2psys.domain.TenderAwardYesAndNo;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAmount;
import com.p2psys.domain.UserAmountApply;
import com.p2psys.domain.UserAmountLog;
import com.p2psys.model.BorrowNTender;
import com.p2psys.model.BorrowTender;
import com.p2psys.model.CollectionSumModel;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.DetailUser;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RankModel;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserBorrowModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.award.AwardAccumulateTenderLog;
import com.p2psys.model.accountlog.borrow.BorrowerVerifyFailLog;
import com.p2psys.model.accountlog.borrow.BorrowerVerifySuccLog;
import com.p2psys.model.accountlog.borrow.TenderNewBorrowLog;
import com.p2psys.model.borrow.BorrowHelper;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.model.borrow.FastExpireModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.MemberBorrowService;
import com.p2psys.service.QuickenLoansService;
import com.p2psys.service.UserAmountService;
import com.p2psys.service.UserService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.BigDecimalUtil;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 贷款管理Action
 * 
 
 * @date 2012-9-14 下午2:54:43
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class ManageBorrowAction extends BaseAction {
	
	private Logger logger=Logger.getLogger(ManageBorrowAction.class);
	
	private BorrowService borrowService;
	private UserAmountService userAmountService;
	private MemberBorrowService memberBorrowService;
	private UserService userService;
	private QuickenLoansService quickenLoansService;
	public AccountService getAccountService() {
		return accountService;
	}
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	private AccountService accountService;

	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public QuickenLoansService getQuickenLoansService() {
		return quickenLoansService;
	}
	public void setQuickenLoansService(QuickenLoansService quickenLoansService) {
		this.quickenLoansService = quickenLoansService;
	}

	private long id;
	private int status;
	String username;  //用户名
	private String verify_user;
	
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	private int type; //标种期限 
	private String verify_remark;
	
	public BorrowService getBorrowService() {
		return borrowService;
	}
	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	public UserAmountService getUserAmountService() {
		return userAmountService;
	}
	public void setUserAmountService(UserAmountService userAmountService) {
		this.userAmountService = userAmountService;
	}
	public MemberBorrowService getMemberBorrowService() {
		return memberBorrowService;
	}
	public void setMemberBorrowService(MemberBorrowService memberBorrowService) {
		this.memberBorrowService = memberBorrowService;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getVerify_remark() {
		return verify_remark;
	}
	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
	}
	/**
	 * 查找所有的标
	 * @return
	 * @throws Exception
	 */
	public String showAllBorrow() throws Exception{
		BorrowModel model=new BorrowModel();
		BorrowModel wrapModel=BorrowHelper.getHelper(Constant.TYPE_ALL,model);
		String servletPath=request.getServletPath();
		logger.debug("ServletPath:"+servletPath);
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
		String status=StringUtils.isNull(request.getParameter("status"));
		String type=StringUtils.isNull(request.getParameter("type"));
		//showBorrowList(status);
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String verify_user = StringUtils.isNull(request.getParameter("verify_user"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		// 2013/08/27 zhengzhiai add end 复审时间查询 
		String fullVerifytime1 = StringUtils.isNull(request.getParameter("fullVerifytime1"));
		String fullVerifytime2 = StringUtils.isNull(request.getParameter("fullVerifytime2"));
		SearchParam param=new SearchParam();
		param.setType(String.valueOf(type));
		param.setUsername(username);
		param.setVerify_user(verify_user);
		param.setStatus(status);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setFullVerifytime1(fullVerifytime1);
		param.setFullVerifytime2(fullVerifytime2);
		PageDataList plist=borrowService.getAllBorrowList(page, param);
		setPageAttribute(plist, param);
		double total=borrowService.getSumBorrowAccount(param);
		// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 start
		double repayYesTotal=borrowService.getSumBorrowRepaymentYesAccount(param);
		double unRepayTotal=borrowService.getSumBorrowUnRepaymentAccount(param);
		request.setAttribute("repayYesTotal", repayYesTotal);
		request.setAttribute("unRepayTotal", unRepayTotal);
		// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 end
		// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 end
		request.setAttribute("borrow_total", total);
		request.setAttribute("borrowType", "all");
		//保存返回页面
		setMsgUrl("/admin/borrow/showAllBorrow.html");
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		if(actionType.isEmpty()){
			// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="fullBorrow_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"data/export/"+downloadFile;
			String[] names=new String[]{"id","username","credit_jifen","name","account","apr","time_limit","addtime","full_verifytime", "status"};
			String[] titles=new String[]{"ID","用户名称","用户积分","借款标题","借款金额","利率","借款期限","发布时间","复审时间", "状态"};
			List allBorrowList = borrowService.getAllBorrowList(param);
			ExcelHelper.writeExcel(infile,allBorrowList, UserBorrowModel.class, Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
		
	}
	
	/**
	 * 流标
	 * @return
	 * @throws Exception
	 */
	public String showElapseBorrow() throws Exception{
		BorrowModel model=new BorrowModel();
		BorrowModel wrapModel=BorrowHelper.getHelper(Constant.TYPE_ALL,model);
		SearchParam param=new SearchParam();
		param.setUsername(StringUtils.isNull(request.getParameter("username")));
		model.setPageStart(NumberUtils.getInt(request.getParameter("page")));
		model.setStatus(19);
		model.setParam(param);
		try {
			showBorrowList(wrapModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//保存返回页面
		setMsgUrl("/admin/borrow/showElapseBorrow.html");
		return SUCCESS;
	}
	
	/**
	 * 有效期内未投满的流标
	 * @return
	 * @throws Exception
	 */
	public String showSpreadBorrow() throws Exception{
		BorrowModel model = new BorrowModel();
		BorrowModel wrapModel = BorrowHelper.getHelper(Constant.TYPE_ALL,model);
		String type = StringUtils.isNull(request.getParameter("type"));
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String verify_user = StringUtils.isNull(request.getParameter("verify_user"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		SearchParam param = new SearchParam();
		param.setType(String.valueOf(type));
		param.setUsername(username);
		param.setVerify_user(verify_user);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		model.setPageStart(page);
		model.setStatus(9);
		model.setParam(param);
		try {
			showBorrowList(wrapModel);
			request.setAttribute("borrowType", "all");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setMsgUrl("/admin/borrow/showSpreadBorrow.html");
		return SUCCESS;
	}
	
	/**
	 * 撤回标
	 * @return
	 * @throws Exception
	 */
	public String showCancelBorrow() throws Exception{
		BorrowModel model=new BorrowModel();
		BorrowModel wrapModel=BorrowHelper.getHelper(Constant.TYPE_ALL,model);
		SearchParam param=new SearchParam();
		param.setUsername(StringUtils.isNull(request.getParameter("username")));
		model.setPageStart(NumberUtils.getInt(request.getParameter("page")));
//		String type = StringUtils.isNull(request.getParameter("type"));
		param.setType(StringUtils.isNull(request.getParameter("type")));
		model.setStatus(5);
		model.setParam(param);
		try {
			showBorrowList(wrapModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//保存返回页面
		setMsgUrl("/admin/borrow/showCancelBorrow.html");
		request.setAttribute("borrowType", "cancel");
		return SUCCESS;
	}
	
	// v1.6.5.3 RDPROJECT-180 xx 2013.09.17 start
	/**
	 * 查看标的情况
	 * @return
	 * @throws Exception
	 */
	/*public String viewBorrow() throws Exception {
		if(id<0){
			this.message("非法操作！");
			return ADMINMSG;
		}
		UserBorrowModel b=borrowService.getUserBorrow(id);
		if(b==null){
			this.message("非法操作！");
			return ADMINMSG;
		}
		request.setAttribute("b", b);
		return SUCCESS;
	}*/
	
	public String viewBorrow() throws Exception {
		UserBorrowModel b = new UserBorrowModel();
		String actionType = StringUtils.isNull(request.getParameter("type"));
		if (StringUtils.isBlank(actionType)) {
			if (id < 0) {
				this.message("非法操作！");
				return ADMINMSG;
			}
			b = borrowService.getUserBorrow(id);
			if (b == null) {
				this.message("非法操作！");
				return ADMINMSG;
			}
			request.setAttribute("b", b);
			return SUCCESS;
		}
		b = getBorrowParameter();
		borrowService.updateBorrow(b);
		this.message("操作成功！", "/admin/borrow/showAllBorrow.html");
		return ADMINMSG;
	}

	/**
	 * 获取标参数
	 */
	public UserBorrowModel getBorrowParameter() {
		double lowest_account = NumberUtils.getDouble(request
				.getParameter("lowest_account"));
		double most_account = NumberUtils.getDouble(request
				.getParameter("most_account"));
		long id = NumberUtils.getLong(StringUtils.isNull(request
				.getParameter("id")));
		UserBorrowModel b = borrowService.getUserBorrow(id);
		b.setId(id);
		b.setMost_account(most_account);
		b.setLowest_account(lowest_account);
		return b;
	}
	
	// v1.6.5.3 RDPROJECT-180 xx 2013.09.17 end
	/**
	 * 推荐
	 * @return
	 * @throws Exception
	 */
	public String recommend() throws Exception{
		int borrow_id= NumberUtils.getInt(request.getParameter("id"));
		BorrowModel b=borrowService.getBorrow(borrow_id);
		if(b==null||b.getStatus() == 2){
			message("非法操作！","");
			return ADMINMSG;
		}
		b.setIs_recommend(1);
		borrowService.updateRecommendBorrow(b);
		b=borrowService.getBorrow(borrow_id);
		message("操作成功！","");
		showAllBorrow();
		return ADMINMSG; 
	}
	
	/**
	 * 审核标
	 * @return
	 * @throws Exception
	 */
	public String verifyBorrow() throws Exception {
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		BorrowModel borrow=borrowService.getBorrow(id);
		//操作日志
		User auth_user=(User) session.get(Constant.AUTH_USER);
		OperationLog operationLog=new OperationLog(borrow.getUser_id(), auth_user.getUser_id(), "", this.getTimeStr(), this.getRequestIp(), "");

		//v1.6.7.2 RDPROJECT-473 sj 2013-12-19 start
		RuleModel r = new RuleModel(Global.getRule(EnumRuleNid.TENDERLIMIT.getValue()));
		if(r != null && r.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
			String convestCollection = r.getValueStrByKey("convest_collection");
			String mostTenderTimes = r.getValueStrByKey("most_tender_times");
			String tenderDays = r.getValueStrByKey("tender_days");
			if("1".equals(convestCollection)){
				request.setAttribute("convestCollection", convestCollection);
			}
			if("1".equals(mostTenderTimes)){
				request.setAttribute("mostTenderTimes", mostTenderTimes);
			}
			if("1".equals(tenderDays)){
				request.setAttribute("tenderDays", tenderDays);
			}
		}
		//v1.6.7.2 RDPROJECT-473 sj 2013-12-19 end
		
		if(actionType.equals("verify")){
			BorrowModel b=borrowService.getBorrow(id);
			String retMsg=this.checkBorrow(b);
			if(!StringUtils.isBlank(retMsg)){
				return ADMINMSG;
			}
			b.setVerify_time(DateUtils.getNowTimeStr());
			BorrowModel wrapModel=BorrowHelper.getHelper(b.getType(),b);
			//审核状态
			wrapModel.verify(wrapModel.getModel().getStatus(), status);
			
			AccountLog log=null;
			try {
				log=new AccountLog(b.getUser_id(),Constant.UNFREEZE,this.getAuthUser().getUser_id(),
						this.getTimeStr(),this.getRequestIp());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 start
			int flow_time = paramInt("flow_time");
//			if(flow_time>0 && wrapModel.getModel().getIs_flow()==1){
			if(flow_time>0 && wrapModel.getModel().getType() == Constant.TYPE_FLOW){
				wrapModel.getModel().setFlow_time(flow_time);
			}
			//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 start
			double extension_apr=NumberUtils.getDouble(paramString("extension_apr"));
			wrapModel.getModel().setExtension_apr(extension_apr);
			//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 end
			// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 end
			
			//V1.6.7.1 RDPROJECT-406 liukun 2013-11-06 start
			int vipTenderLimit = NumberUtils.getInt(paramString("vip_tender_limit"));
			int vipTenderLimitDays = NumberUtils.getInt(paramString("vip_tender_limit_days"));
			wrapModel.getModel().setVip_tender_limit(vipTenderLimit);
			wrapModel.getModel().setVip_tender_limit_days(vipTenderLimitDays);
			//V1.6.7.1 RDPROJECT-406 liukun 2013-11-06 end
			
			//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
			if(r != null){
				if(r.getStatus() == 1){
					double convest_collection = NumberUtils.getDouble(paramString("convest_collection"));
					int most_tender_times = NumberUtils.getInt(paramString("most_tender_times"));
					int tender_days = NumberUtils.getInt(paramString("tender_days"));
					wrapModel.getModel().setConvest_collection(convest_collection);
					wrapModel.getModel().setMost_tender_times(most_tender_times);
					wrapModel.getModel().setTender_days(tender_days);
				}
			}
			//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
			
			// v1.6.5.3 RDPROJECT-185 zza 2013-09-18 start
//			if (status == 1 && b.getIs_flow() == 1) {
			if (status == 1 && b.getType() == Constant.TYPE_FLOW) {	
				wrapModel.getModel().setFull_verifytime(b.getVerify_time());
			}
			// v1.6.5.3 RDPROJECT-185 zza 2013-09-18 end
			borrowService.verifyBorrow(wrapModel.getModel(),log,operationLog);
			message("审核成功！");
			
			//发送新标通知短信
			if (status == 1) {
				final BorrowModel _borrow = borrow;
				new Thread(new Runnable() {

					public void run() {
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
						//TODO RDPROJECT-314 DONE
						String timelimitdesc;
						int isday = _borrow.getIsday();
						if (isday == 1) {
							timelimitdesc = _borrow.getTime_limit_day() + "天";
						} else {
							timelimitdesc = _borrow.getTime_limit() + "个月";
						}
						
						List<User> userList = userService
								.getAllUser(Constant.NORMAL_CUSTOMER);
						
						for (User user : userList) {
							long userid = user.getUser_id();
							if (userid == _borrow.getUser_id()) {
								continue;
							}
							
							
							
							Account tact=accountService.getAccount(userid);
							Global.setTransfer("borrow", _borrow);
							Global.setTransfer("timelimitdesc", timelimitdesc);
							BaseAccountLog tlog=new TenderNewBorrowLog(0, tact,userid);
							tlog.doEvent();
							/*Smstype smstype = Global
									.getSmstype(Constant.SMS_NEW_BORROW);
							// 不需要发给发标人
							if (userid == _borrow.getUser_id()) {
								continue;
							}
							boolean isSmssend = userService.isSmssend(userid,
									smstype);
							if (isSmssend) {
								Notice s = new Sms();
								//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 start
								String p0 = Global.getValue("webname");
								String p1 = user.getUsername();
								String p2 = _borrow.getName();
								String p3 = _borrow.getAccount();
								String p4 = String.valueOf(_borrow.getApr());
								
								String p5;
								
								int isday = _borrow.getIsday();
								if (isday == 1) {
									p5 = _borrow.getTime_limit_day() + "天";
								} else {
									p5 = _borrow.getTime_limit() + "个月";
								}
								
//								String[] paras = new String[] { p0, p1, p2, p3,	p4, p5 };

								
								Global.setTransfer("webname", Global.getValue("webname"));
								Global.setTransfer("user", user);
								Global.setTransfer("borrow", _borrow);
								Global.setTransfer("timelimitdesc", p5);
								
								s.setAddtime(_borrow.getVerify_time());
//								s.setContent(StringUtils.fillTemplet(smstype.getTemplet(), Constant.SMS_TEMPLET_PHSTR, paras));
						    	s.setContent(StringUtils.fillTemplet(smstype.getTemplet()));

								//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 end
								s.setReceive_userid(userid);
								s.setSend_userid(1);
								NoticeJobQueue.NOTICE.offer(s);
							}*/
							//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end

						}
					}
				}).start();
			}
			
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
			//TODO RDPROJECT-314 DONE
			//向发标人发送审核结果通知
			long borrower_user_id = borrow.getUser_id();
			Account act=accountService.getAccount(borrower_user_id);
			Global.setTransfer("borrow", borrow);
			BaseAccountLog blog=null;
			if (status == 1) {
				blog=new BorrowerVerifySuccLog(0, act,borrower_user_id);
			}else{
				blog=new BorrowerVerifyFailLog(0, act,borrower_user_id);
			}
			blog.doEvent();				
			/*if (status == 1) {
				
				


				
				Smstype smstype = Global
						.getSmstype(Constant.SMS_BORROW_VERIFY_SUCC);

				long userid = borrow.getUser_id();
				User user = userService.getUserById(userid);

				boolean isSmssend = userService.isSmssend(userid, smstype);
				if (isSmssend) {
					
					Notice s = new Sms();
					//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 start

					
					Global.setTransfer("webname", Global.getValue("webname"));
					Global.setTransfer("user", user);
					Global.setTransfer("borrow", borrow);


					s.setAddtime(b.getVerify_time());

					s.setContent(StringUtils.fillTemplet(smstype.getTemplet()));
					//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 end
					s.setReceive_userid(userid);
					s.setSend_userid(1);
					NoticeJobQueue.NOTICE.offer(s);
				}
				


			} else if (status == 2) {
				

				/*Smstype smstype = Global
						.getSmstype(Constant.SMS_BORROW_VERIFY_FAIL);
				long userid = borrow.getUser_id();
				User user = userService.getUserById(userid);

				boolean isSmssend = userService.isSmssend(userid, smstype);
				if (isSmssend) {
					Notice s = new Sms();
					//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 start

					
					Global.setTransfer("webname", Global.getValue("webname"));
					Global.setTransfer("user", user);
					Global.setTransfer("borrow", borrow);


					s.setAddtime(b.getVerify_time());

					s.setContent(StringUtils.fillTemplet(smstype.getTemplet()));
					//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 end
					s.setReceive_userid(userid);
					s.setSend_userid(1);
					NoticeJobQueue.NOTICE.offer(s);
				}

			}*/
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
			return ADMINMSG;
		}else{
			if(id<0){
				this.message("非法操作！");
				/*//发标待审操作成功日志
				operationLog.setAddtime(this.getTimeStr());
				operationLog.setOperationResult(""+detailUser.getTypename()+"（"+operationLog.getAddip()+"）用户名为"+detailUser.getUsername()+"的操作员审核发标人用户名为"+newdetailUser.getUsername()+"的借款标（标名为"+borrow.getName()+"）提示非法操作");
				accountService.addOperationLog(operationLog);*/
				return ADMINMSG;
			}
			UserBorrowModel b=borrowService.getUserBorrow(id);
			if(b==null||(b.getStatus()!=0&&b.getStatus()!=1)){
				this.message("非法操作！");
				/*//发标待审操作成功日志
				operationLog.setAddtime(this.getTimeStr());
				operationLog.setType(Constant.BORROW_FIRST_VERIFY_SUCCESS);
				operationLog.setOperationResult(""+detailUser.getTypename()+"（"+operationLog.getAddip()+"）用户名为"+detailUser.getUsername()+"的操作员审核发标人用户名为"+newdetailUser.getUsername()+"的借款标（标名为"+b.getName()+"）提示非法操作");
				accountService.addOperationLog(operationLog);*/
				return ADMINMSG;
			}
			request.setAttribute("b", b);
		}
		return SUCCESS;
	}
	
	/**
	 * 撤回标，状态改成-1
	 * @return
	 * @throws Exception
	 */
	public String cancelBorrow() throws Exception {
		BorrowModel b=borrowService.getBorrow(id);
		if(b==null||(b.getStatus()>4||b.getStatus()==3)){
			this.message("非法操作！");
			return ADMINMSG;
		}
		BorrowModel wrapModel=BorrowHelper.getHelper(b.getType(),b);
		//审核状态
		wrapModel.verify(wrapModel.getModel().getStatus(), 5);
		
		AccountLog log=new AccountLog(wrapModel.getModel().getUser_id(),Constant.UNFREEZE,getAuthUser().getUser_id(),
				getTimeStr(),this.getRequestIp());
		borrowService.deleteBorrow(wrapModel.getModel(), log);
		message("撤回借款标成功！");
		return ADMINMSG;
	}
	
	private String checkBorrow(Borrow b){
		if(StringUtils.isBlank(verify_remark)){
			this.message("审核信息不能为空！");
			return ADMINMSG;
		}
		return "";
	}
	/**
	 * 显示需要初审的标
	 * @return
	 * @throws Exception
	 */
	public String showTrialBorrow() throws Exception{
		String status=StringUtils.isNull(request.getParameter("status"));
		if(StringUtils.isBlank(status)){
			status="0";
		}
		showBorrowList(status);
		request.setAttribute("borrowType", "trial");
		//保存返回页面
		setMsgUrl("/admin/borrow/showTrialBorrow.html");
		//v1.6.7.2 RDPROJECT-557 sj 2013-12-10 start
		request.setAttribute("ignoreMoney", "ignoreMoney");
		//v1.6.7.2 RDPROJECT-557 sj 2013-12-10 end
		return SUCCESS;
	}
	public String showNotTrialBorrow() throws Exception{
		showBorrowList("2");
		request.setAttribute("borrowType", "notTrial");
		//保存返回页面
		setMsgUrl("/admin/borrow/showTrialBorrow.html");
		//v1.6.7.2 RDPROJECT-557 sj 2013-12-10 start
		request.setAttribute("ignoreMoney", "ignoreMoney");
		//v1.6.7.2 RDPROJECT-557 sj 2013-12-10 start
		return SUCCESS;
	}
	
	/**
	 * 显示需要初审的标
	 * @return
	 * @throws Exception
	 */
	public String showReviewBorrow() throws Exception{
		int page=NumberUtils.getInt(request.getParameter("page"));
		String username =StringUtils.isNull(request.getParameter("username"));
		String status="1";
		SearchParam param=new SearchParam();
		param.setUsername(username);
		param.setStatus(status);
		PageDataList plist=borrowService.getFullBorrowList(page, param);
		request.setAttribute("page", plist.getPage());
		request.setAttribute("list", plist.getList());
		request.setAttribute("param", param.toMap());
		request.setAttribute("borrowType", "review");
		//保存返回页面
		setMsgUrl("/admin/borrow/showReviewBorrow.html");
		return SUCCESS;
	}
	
	/**
	 * 查看满标需要审核的标的情况
	 * @return String
	 * @throws Exception 异常
	 */
	public String viewFullBorrow() throws Exception {
		if (id < 0) {
			this.message("非法操作！");
			return ADMINMSG;
		}
		UserBorrowModel b = borrowService.getUserBorrow(id);
		if (b == null) {
			this.message("非法操作！");
			return ADMINMSG;
		}
		BorrowModel wrapModel=BorrowHelper.getHelper(b.getType(),b);
		Repayment[] repayList=wrapModel.getRepayment();
		
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam param = new SearchParam();
		PageDataList plist = borrowService.getTenderList(id, page, param);

		request.setAttribute("b", b);
		request.setAttribute("tenderlist", plist.getList());
		request.setAttribute("repayList", repayList);
		request.setAttribute("page", plist.getPage());
		request.setAttribute("param", param.toMap());
		return SUCCESS;
	}
	/**
	 * 审核满标
	 * @return
	 * @throws Exception
	 */
	public String verifyFullBorrow() throws Exception {
		BorrowModel model=borrowService.getBorrow(id);
		User user=getSessionUser();
		String retMsg=this.checkBorrow(model);
		if(!StringUtils.isBlank(retMsg)){
			return ADMINMSG;
		}
		model.setFull_verifytime(this.getTimeStr());
		model.setVerify_remark(StringUtils.isNull(request.getParameter("verify_time")));
		BorrowModel wrapModel=BorrowHelper.getHelper(model.getType(),model);
		//v1.6.7.1 RDPROJECT-461 wcw 2013-11-21 start
		if (!wrapModel.allowFullSuccess()) {
		      message("该标种不允许满标复审！");
		      return "adminmsg";
		    }
		//v1.6.7.1 RDPROJECT-461 wcw 2013-11-21 end
		//审核状态
		wrapModel.verify(wrapModel.getModel().getStatus(), status);
		AccountLog log=new AccountLog(model.getUser_id(),Constant.UNFREEZE,model.getUser_id(),
				this.getTimeStr(),this.getRequestIp());
		log.setRemark(getLogRemark(wrapModel.getModel()));
		
		User auth_user=(User) session.get(Constant.AUTH_USER);
		DetailUser detailUser=userService.getDetailUser(auth_user.getUser_id());
		DetailUser newdetailUser=userService.getDetailUser(model.getUser_id());
		OperationLog operationLog=new OperationLog(newdetailUser.getUser_id(), auth_user.getUser_id(), "", this.getTimeStr(), this.getRequestIp(), "");
        if(Global.getWebid().equals("jsy")){
            Protocol protocol=new Protocol(0, Constant.TENDER_PROTOCOL, this.getTimeStr(), this.getRequestIp(), "");
        	borrowService.verifyFullBorrow(wrapModel,operationLog,protocol);
        }else{
		    borrowService.verifyFullBorrow(wrapModel,operationLog);
		}
        //是否启用将协议发送至投资者邮箱,1为启用
//        if(Global.getInt("protocol_toEmail_enable") ==1){
//        	//生成借款协议并发送给投资者
//            List tenderList = borrowService.getAllTenderList(model.getId());
//            for(int i=0;i<tenderList.size();i++){
//            	Tender t = (Tender)tenderList.get(i);
//            	Long borrow_id = t.getBorrow_id();
//            	long tender_id = t.getId();
//            	User toUser = userService.getUserById(t.getUser_id());
//            	BorrowProtocol bp=new BorrowProtocol(toUser,borrow_id,tender_id);
//        		File pdfFile=new File(bp.getInPdfName());
//        		if(pdfFile.exists()){
//        			try {
//        				bp.createPdf();
//        			} catch (Exception e) {
//        				e.printStackTrace();
//        			}
//        		}
//        		//发送邮件
//                sendMailWithAttachment(toUser, borrow_id, tender_id);
//           }
//        }
		message("审核成功！");
		return ADMINMSG;
	}
	
	/**
	 * 显示正在招标的款
	 * @return
	 * @throws Exception
	 */
	public String showBorrowing() throws Exception{
		showBorrowList("1");
		//保存返回页面
		setMsgUrl("/admin/borrow/showBorrowing.html");
		request.setAttribute("borrowType", "borrowing");
		return SUCCESS;
	}
	
	/**
	 * 显示满标审核通过的款
	 * @return
	 * @throws Exception
	 */
	public String showIsFullBorrow() throws Exception{
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
		String type = StringUtils.isNull(request.getParameter("type"));
		String dotime1=StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2=StringUtils.isNull(request.getParameter("dotime2"));
		// 2013/08/27 zhengzhiai add end 复审时间查询 
		String fullVerifytime1=StringUtils.isNull(request.getParameter("fullVerifytime1"));
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		String fullVerifytime2=StringUtils.isNull(request.getParameter("fullVerifytime2"));
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
		int page=NumberUtils.getInt(request.getParameter("page"));
		BorrowModel model=new BorrowModel();
		SearchParam param=new SearchParam();
		
		param.setType(String.valueOf(type));
		param.setUsername(username);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setFullVerifytime1(fullVerifytime1);
		param.setFullVerifytime2(fullVerifytime2);
		
		param.setStatusArray(new int[]{3,6});
		model.setParam(param);
		model.setPageStart(page);
		BorrowModel wrapModel=BorrowHelper.getHelper(Constant.TYPE_ALL,model);
		
		try {
			showBorrowList(wrapModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//保存返回页面
		setMsgUrl("/admin/borrow/showIsFullBorrow.html");
		request.setAttribute("borrowType", "full");
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		if(actionType.isEmpty()){
			// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="fullBorrow_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"data/export/"+downloadFile;
			String[] names=new String[]{"id","username","credit_jifen","name","account","apr","time_limit","addtime","full_verifytime"};
			String[] titles=new String[]{"ID","用户名称","用户积分","借款标题","借款金额","利率","借款期限","发布时间","复审时间"};
			List fullBorrowList = borrowService.getBorrowList(wrapModel);
			ExcelHelper.writeExcel(infile,fullBorrowList, UserBorrowModel.class, Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
	}
	
	public String showNotFullBorrow() throws Exception{
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
		String type = StringUtils.isNull(request.getParameter("type"));
		String dotime1=StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2=StringUtils.isNull(request.getParameter("dotime2"));
		// 2013/08/27 zhengzhiai add end 复审时间查询 
		String fullVerifytime1=StringUtils.isNull(request.getParameter("fullVerifytime1"));
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		String fullVerifytime2=StringUtils.isNull(request.getParameter("fullVerifytime2"));
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
		int page=NumberUtils.getInt(request.getParameter("page"));
		BorrowModel model=new BorrowModel();
		SearchParam param=new SearchParam();
		param.setStatusArray(new int[]{4,49});
		param.setUsername(username);
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		param.setType(type);
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setFullVerifytime1(fullVerifytime1);
		param.setFullVerifytime2(fullVerifytime2);
		model.setParam(param);
		model.setPageStart(page);
		BorrowModel wrapModel=BorrowHelper.getHelper(Constant.TYPE_ALL,model);
		try {
			showBorrowList(wrapModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//保存返回页面
		setMsgUrl("/admin/borrow/showNotFullBorrow.html");
		request.setAttribute("borrowType", "notFull");
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		if(actionType.isEmpty()){
			// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="fullBorrow_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"data/export/"+downloadFile;
			String[] names=new String[]{"id","username","credit_jifen","name","account","apr","time_limit","addtime","full_verifytime"};
			String[] titles=new String[]{"ID","用户名称","用户积分","借款标题","借款金额","利率","借款期限","发布时间","复审时间"};
			List fullBorrowList = borrowService.getBorrowList(wrapModel);
			ExcelHelper.writeExcel(infile,fullBorrowList, UserBorrowModel.class, Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
	}
	/**
	 * 根据状态标的列表
	 * @param status
	 */
	private void showBorrowList(String status){
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		String type = StringUtils.isNull(request.getParameter("type"));
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String verify_user = StringUtils.isNull(request.getParameter("verify_user"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		// 2013/08/27 zhengzhiai add end 复审时间查询 
		String fullVerifytime1 = StringUtils.isNull(request.getParameter("fullVerifytime1"));
		String fullVerifytime2 = StringUtils.isNull(request.getParameter("fullVerifytime2"));
		SearchParam param=new SearchParam();
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 start
		param.setType(type);
		// v1.6.6.1 RDPROJECT-64 zza 2013-09-25 end
		param.setUsername(username);
		param.setVerify_user(verify_user);
		param.setStatus(status);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setFullVerifytime1(fullVerifytime1);
		param.setFullVerifytime2(fullVerifytime2);
		PageDataList plist=borrowService.getAllBorrowList(page, param);
		setPageAttribute(plist, param);
		double total=borrowService.getSumBorrowAccount(param);
		request.setAttribute("borrow_total", total);
	}
	
	private void showBorrowList(BorrowModel model){
		PageDataList plist=borrowService.getList(model);
		setPageAttribute(plist, model.getModel().getParam());
		double total=borrowService.getSumBorrowAccount(model.getModel().getParam());
		request.setAttribute("borrow_total", total);
	}
	
	public String amountApply() throws Exception{
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		PageDataList plist=userAmountService.getUserAmountApply(page, param);
		setPageAttribute(plist, param);
		//保存返回页面
		setMsgUrl("/admin/borrow/amountApply.html");
		return SUCCESS;
	}
	
	public String viewAmountApply() throws Exception{
		//v1.6.7.1 RDPROJECT-433 liukun 2013-11-08 start
		UserAmountApply apply=userAmountService.getUserAmountApplyById(id);
		if(apply==null){
			message("非法操作！");
			return ADMINMSG;
		}
		UserAmount ua=userAmountService.getUserAmount(apply.getUser_id());
		//v1.6.7.1 RDPROJECT-433 liukun 2013-11-08 end
		request.setAttribute("apply", apply);
		//v1.6.7.1 RDPROJECT-433 liukun 2013-11-08 start
		request.setAttribute("useramount", ua);
		//v1.6.7.1 RDPROJECT-433 liukun 2013-11-08 end
		return SUCCESS;
	}
	
	/**
	 * 
	 * @param apply
	 * @return
	 */
	private String checkAmountApply(UserAmountApply apply){
		if(status!=0&&status!=1){
			message("非法操作！");
			return ADMINMSG;
		}else if(apply==null){
			message("非法操作！");
			return ADMINMSG;
		}else if(apply.getStatus()==1){
			message("已经审核通过！");
			return ADMINMSG;
		}
		return "";
	}
	
	public String showAllTenderList() throws Exception{
		String type=StringUtils.isNull(request.getParameter("type"));
		int page= NumberUtils.getInt(request.getParameter("page"));
		String username=StringUtils.isNull(request.getParameter("username"));
		String dotime1=StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2=StringUtils.isNull(request.getParameter("dotime2"));
		SearchParam param = new SearchParam();
		param.setUsername(username);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		PageDataList plist =borrowService.getAllBorrowTenderList(page, param);
		setPageAttribute(plist, param);
		setMsgUrl("/admin/borrow/showAllTenderList.html");
		if(type.isEmpty()){
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="account_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"data/export/"+downloadFile;
			String[] names=new String[]{"username","borrowname",
					"account","money","interest","addip"};
			String[] titles=new String[]{"发标者","标名","金额",
					"操作金额","利息","IP"};
			List list=borrowService.getAllBorrowTenderList(param);
			ExcelHelper.writeExcel(infile, list, BorrowNTender.class, Arrays.asList(names), Arrays.asList(titles));
			export(infile, downloadFile);
			return null;
		}

	}
	/**
	 * 审核额度申请
	 * @return
	 * @throws Exception
	 */
	public String verifyAmountApply() throws Exception{
		UserAmountApply apply=userAmountService.getUserAmountApplyById(id);
		String msg=checkAmountApply(apply);
		if(!StringUtils.isBlank(msg)){
			return ADMINMSG;
		}
		UserAmount ua=userAmountService.getUserAmount(apply.getUser_id());
		if(ua==null){
			apply.setAccount_old(0);
		}else{
			apply.setAccount_old(ua.getCredit());
		}
		if(status==0){
			apply.setStatus(-1);
		}else if(status==1){
			double account=NumberUtils.getDouble(request.getParameter("account"));
			if(account<=0){
				message("申请额度不能小于0！");
				return ADMINMSG;
			}
			apply.setStatus(1);
			apply.setAccount(account);
			apply.setAccount_new(apply.getAccount_old()+account);
		}else{
			message("非法操作！");
			return ADMINMSG;
		}

		apply.setVerify_remark(verify_remark);
		apply.setVerify_time(getTimeStr());
		UserAmountLog log=new UserAmountLog();
		log.setAddtime(this.getTimeStr());
		log.setAddip(this.getRequestIp());
		try {
			userAmountService.verifyAmountApply(apply, log);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message("审核成功！");
		return ADMINMSG;
	}
	
	public String repaid() throws Exception{
		String type = StringUtils.isNull(request.getParameter("type"));
		String keywords = StringUtils.isNull(request.getParameter("keywords"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String repayment_time1 = StringUtils.isNull(request.getParameter("repayment_time1"));
		String repayment_time2 = StringUtils.isNull(request.getParameter("repayment_time2"));
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		// 已还时间
		String repayment_yestime1 = StringUtils.isNull(request.getParameter("repayment_yestime1"));
		String repayment_yestime2 = StringUtils.isNull(request.getParameter("repayment_yestime2"));
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
		int page = NumberUtils.getInt(request.getParameter("page"));
		String repay_type = StringUtils.isNull(request.getParameter("repay_type"));
		SearchParam param = new SearchParam();
		param.setRepayment_time1(repayment_time1);
	    param.setRepayment_time2(repayment_time2);
	    // v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
	    param.setRepayment_yestime1(repayment_yestime1);
	    param.setRepayment_yestime2(repayment_yestime2);
	    // v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
		param.setKeywords(keywords);
		param.setUsername(username);
		//已还，borrow状态：7,8 repayment状态：1
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 start
		//param.setStatus("8");
		param.setStatusArray(new int[]{7, 8});
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 end
		param.setRepayStatus("1");
		param.setType(type);
		request.setAttribute("repay_type", "repaid");
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 end
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		if (actionType.isEmpty()) {
			// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
			PageDataList plist = memberBorrowService.getRepaymentList(param, page);
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
			List<Repayment> list=plist.getList();
			for (Repayment rm : list) {
				if(("1").equals(rm.getAward())){
					rm.setPart_account((String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getPart_account()), 2))));
				}
				if(("2").equals(rm.getAward())){
					rm.setFunds((String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getFunds()), 2))));
				}
			}
			plist.setList(list);
			request.setAttribute("listsize", memberBorrowService.getRepaymentCount(param));
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
			this.setPageAttribute(plist, param);
			// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
			CollectionSumModel model = memberBorrowService.getRepaySum(param, "1");
			// 应还金额、应还利息、应还本息的统计
			request.setAttribute("model", model);
			// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
			return SUCCESS;
		}else{
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 end
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="repaid_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
			String[] names=new String[]{"username","borrow_name",
					// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 start
					"qsCount","expire_time","repayment_account","capital","interest","late_award","repayment_yestime","award"};
			// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 end
			String[] titles=new String[]{"借款者","借款标名称","期数",
					"到期时间","归还总额","归还本金","归还利息","还款奖励（比列）","归还时间","已付投标奖励"};
			DecimalFormat df = new DecimalFormat("#0.000"); 
			List<Repayment> list=memberBorrowService.getRepaymentList(param);
			for (Repayment rm :list) {
				if(!StringUtils.isEmpty(rm.getRepayment_account())){
					rm.setRepayment_account(df.format(Double.parseDouble(rm.getRepayment_account())));
				}
				if(!StringUtils.isEmpty(rm.getCapital())){
					rm.setCapital(df.format(Double.parseDouble(rm.getCapital())));
				}
				if(!StringUtils.isEmpty(rm.getInterest())){
					rm.setInterest(df.format(Double.parseDouble(rm.getInterest())));
				}
				rm.setQsCount((rm.getOrder()+1)+"/"+(rm.getTime_limit()));
				if(rm.getLate_award()!=null && new BigDecimal(rm.getLate_award()).intValue()==0){
					rm.setLate_award("没有");
				}else{
					rm.setLate_award(rm.getLate_award()+"%");
				}
				if(("0").equals(rm.getAward())){
					rm.setAward("没有");
				}else if(("1").equals(rm.getAward())){
					if(rm.getPart_account()!=null && new BigDecimal(rm.getPart_account()).intValue()==0){
						rm.setAward("没有");
					}else{
						//rm.setAward(String.valueOf(BigDecimalUtil.round((Double.parseDouble(rm.getAccount())*(Double.parseDouble(rm.getPart_account()))/100), 2))+"%");
						rm.setAward((String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getPart_account()), 2)))+"%");
					}
				}else if(("2").equals(rm.getAward())){
					if(rm.getFunds()!=null&&new BigDecimal(rm.getFunds()).intValue()==0){
						rm.setAward("没有");
					}else{
						rm.setAward("￥"+(String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getFunds()), 2))));
					}
				}
			}
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
			ExcelHelper.writeExcel(infile, list, Repayment.class, Arrays.asList(names), Arrays.asList(titles));
			export(infile, downloadFile);
			return null;
		}
	}
	
	public String repaying() throws Exception{
		String type=StringUtils.isNull(request.getParameter("type"));
		String keywords=StringUtils.isNull(request.getParameter("keywords"));
		String username=StringUtils.isNull(request.getParameter("username"));
		String succtime1=StringUtils.isNull(request.getParameter("succtime1"));
		String succtime2=StringUtils.isNull(request.getParameter("succtime2"));
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
		
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		param.setKeywords(keywords);
		param.setUsername(username);
		param.setSucctime1(succtime1);
		param.setSucctime2(succtime2);
		param.setType(type);
		param.setStatusArray(new int[]{6,7});
		//待还，borrow状态：6,7 repayment状态：0
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 start
		param.setRepayStatus("0");
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 end
		
		//及时雨还款时间段设置
		String repayment_time1=StringUtils.isNull(request.getParameter("repayment_time1"));
		String repayment_time2=StringUtils.isNull(request.getParameter("repayment_time2"));
		param.setRepayment_time1(repayment_time1);
		param.setRepayment_time2(repayment_time2);
		request.setAttribute("repay_type", "repaying");
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 start
		if(actionType.isEmpty()){
			PageDataList plist=memberBorrowService.getRepaymentList(param, page);
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
			List<Repayment> list=plist.getList();
			for (Repayment rm : list) {
				if(("1").equals(rm.getAward())){
					rm.setPart_account((String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getPart_account()), 2))));
				}
				if(("2").equals(rm.getAward())){
					rm.setFunds((String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getFunds()), 2))));
					//rm.setFunds((String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getFunds()), 2))));
				}
			}
			plist.setList(list);
			request.setAttribute("listsize", memberBorrowService.getRepaymentCount(param));
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
			this.setPageAttribute(plist, param);
			//待还款金额统计
			double sum =memberBorrowService.getRepaymentSum(param);
			request.setAttribute("sum", sum);
			// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
			CollectionSumModel model = memberBorrowService.getRepaySum(param, "0");
			// 应还金额、应还利息、应还本息的统计
			request.setAttribute("model", model);
			// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
			return SUCCESS;
		}else{
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 end
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="repaying_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
			String[] names=new String[]{"username","borrow_name",
					// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 start
					"qsCount","expire_time","repayment_account","capital","interest","late_award","repayment_yestime","award"};
			// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 end 
			String[] titles=new String[]{"借款者","借款标名称","期数",
					"到期时间","待归还金额","归还本金","归还利息","还款奖励（比例）","归还时间","已付投标奖励"};
			DecimalFormat df = new DecimalFormat("#0.000"); 
			List<Repayment> list=memberBorrowService.getRepaymentList(param);
			for (Repayment rm :list) {
				if(!StringUtils.isEmpty(rm.getRepayment_account())){
					rm.setRepayment_account(df.format(Double.parseDouble(rm.getRepayment_account())));
				}
				if(!StringUtils.isEmpty(rm.getCapital())){
					rm.setCapital(df.format(Double.parseDouble(rm.getCapital())));
				}
				if(!StringUtils.isEmpty(rm.getInterest())){
					rm.setInterest(df.format(Double.parseDouble(rm.getInterest())));
				}
				rm.setQsCount((rm.getOrder()+1)+"/"+(rm.getTime_limit()));
				if(rm.getLate_award()!=null && new BigDecimal(rm.getLate_award()).intValue()==0){
					rm.setLate_award("没有");
				}else{
					rm.setLate_award(rm.getLate_award()+"%");
				}
				if(("0").equals(rm.getAward())){
					rm.setAward("没有");
				}else if(("1").equals(rm.getAward())){
					if(rm.getPart_account()!=null && new BigDecimal(rm.getPart_account()).intValue()==0){
						rm.setAward("没有");
					}else{
						rm.setAward((String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getPart_account()), 2)))+"%");
					}
				}else if(("2").equals(rm.getAward())){
					if(rm.getFunds()!=null&&new BigDecimal(rm.getFunds()).intValue()==0){
						rm.setAward("没有");
					}else{
						rm.setAward("￥"+(String.valueOf(BigDecimalUtil.round(Double.parseDouble(rm.getFunds()), 2))));
					}
				}
			}
			ExcelHelper.writeExcel(infile, list, Repayment.class, Arrays.asList(names), Arrays.asList(titles));
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
			export(infile, downloadFile);
			return null;
		}
	}
	public String def() throws Exception{
		String keywords=StringUtils.isNull(request.getParameter("keywords"));
		String username=StringUtils.isNull(request.getParameter("username"));
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		param.setKeywords(keywords);
		param.setUsername(username);
		PageDataList plist=memberBorrowService.getRepaymentList(param, page);
		this.setPageAttribute(plist, param);
		return SUCCESS;
	}
	//到期还款的借款标
	public String late() throws Exception{
		String keywords=StringUtils.isNull(request.getParameter("keywords"));
		String username=StringUtils.isNull(request.getParameter("username"));
		String dotime1=StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2=StringUtils.isNull(request.getParameter("dotime2"));
		String type = StringUtils.isNull(request.getParameter("type"));
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		param.setKeywords(keywords);
		param.setUsername(username);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setType(type);
		PageDataList plist=memberBorrowService.getLateList(param, page);
		this.setPageAttribute(plist, param);
		return SUCCESS;
	}
	//逾期的借款标
	public String overdue() throws Exception{
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		String keywords=StringUtils.isNull(request.getParameter("keywords"));
		String username=StringUtils.isNull(request.getParameter("username"));
		String dotime1=StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2=StringUtils.isNull(request.getParameter("dotime2"));
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		param.setKeywords(keywords);
		param.setUsername(username);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setType(String.valueOf(type));
		PageDataList plist=memberBorrowService.getOverdueList(param, page);
		this.setPageAttribute(plist, param);
		//double total=borrowService.getSumBorrowAccount(param);
		//request.setAttribute("borrow_total", total);
		if(actionType.isEmpty()){
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="overdue_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"data/export/"+downloadFile;
			String[] names=new String[]{"username","borrow_name","account",
					"order","repayment_time","repayment_account","interest"};
			String[] titles=new String[]{"借款者","借款标名称","借款金额","期数",
					"到期时间","还款金额","还款利息"};
			List list=memberBorrowService.getOverdueList(param);
			ExcelHelper.writeExcel(infile, list, Repayment.class, Arrays.asList(names), Arrays.asList(titles));
			export(infile, downloadFile);
			return null;
		}
	}
	
	/**
	 * 到期还款的流转标
	 */
	public String unFinishFlow() throws Exception{
		String type =  StringUtils.isNull(request.getParameter("type"));
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 start
		String pay_status =  "nopayflow";
		request.setAttribute("pay_status", pay_status);
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 end
		String keywords = StringUtils.isNull(request.getParameter("keywords"));
		String username = StringUtils.isNull(request.getParameter("username"));
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam param = new SearchParam();
		param.setKeywords(keywords);
		param.setUsername(username);
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		String repay_time1 = StringUtils.isNull(request.getParameter("repay_time1"));
		String repay_time2 = StringUtils.isNull(request.getParameter("repay_time2"));
		param.setRepay_time1(repay_time1);
		param.setRepay_time2(repay_time2);
		String repay_yestime1 = StringUtils.isNull(request.getParameter("repay_yestime1"));
		String repay_yestime2 = StringUtils.isNull(request.getParameter("repay_yestime2"));
		param.setRepay_yestime1(repay_yestime1);
		param.setRepay_yestime2(repay_yestime2);
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
		PageDataList plist = memberBorrowService.getFlowList(param, page, pay_status);
		
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
		List<DetailCollection> dclist=plist.getList();
		for (DetailCollection dc : dclist) {
			if(("1").equals(dc.getAward())){
				dc.setPart_account((String.valueOf(BigDecimalUtil.round(Double.parseDouble(dc.getPart_account()), 2))));
			}
			if(("2").equals(dc.getAward())){
				dc.setFunds((String.valueOf(BigDecimalUtil.round(Double.parseDouble(dc.getFunds()), 2))));
			}
		}
		plist.setList(dclist);
		request.setAttribute("listsize", memberBorrowService.getFlowCount(param, pay_status));
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
		this.setPageAttribute(plist, param);
		// 2013/08/29 zhengzhiai update end 应还金额、应还利息、应还本息的统计
		String nowTime = DateUtils.getNowTimeStr();
		CollectionSumModel model = memberBorrowService.getFlowSum(param, nowTime, pay_status);
		// 应还金额、应还利息、应还本息的统计
		request.setAttribute("model", model);
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 end
		if (type.isEmpty()) {			
			return SUCCESS;
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "unFinishFlow_" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 start
			String[] names;
			String[] titles;
			//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
			if (EnumBorrow.NOPAYFLOW.getValue().equals(pay_status)) {
				names = new String[] { "username", "borrow_name", "qsCount","repay_time",
						"repay_account","capital", "interest","late_award"};
				titles = new String[] { "投资人", "借款标的名称","期数", "到期时间", "待还总额", "待还本金", "待还利息","待还还款奖励（比例）"};
			} else {
				names = new String[] { "username", "borrow_name","qsCount", "repay_time",
						"repay_yestime", "repay_account", "repay_yesaccount", "capital",
						"interest","late_award","award"};
				titles = new String[] { "投资人", "借款标的名称","期数", "应还时间", "实际还款时间",
						"应还金额", "实还金额", "归还本金","归还利息","还款奖励（比列）","已付投标奖励" };
			}
			DecimalFormat df = new DecimalFormat("#0.000"); 
			List<DetailCollection> list =  memberBorrowService.getFlowList(param, pay_status);
			for (DetailCollection dc :list) {
				if(!StringUtils.isEmpty(dc.getRepay_account())){
					dc.setRepay_account(df.format(Double.parseDouble(dc.getRepay_account())));
				}
				if(!StringUtils.isEmpty(dc.getRepay_yesaccount())){
					dc.setRepay_yesaccount(df.format(Double.parseDouble(dc.getRepay_yesaccount())));
				}
				if(!StringUtils.isEmpty(dc.getCapital())){
					dc.setCapital(df.format(Double.parseDouble(dc.getCapital())));
				}
				if(!StringUtils.isEmpty(dc.getInterest())){
					dc.setInterest(df.format(Double.parseDouble(dc.getInterest())));
				}
				
				dc.setQsCount((dc.getOrder()+1)+"/"+dc.getTime_limit());
				if(dc.getLate_award()!=null && new BigDecimal(dc.getLate_award()).intValue()==0){
					dc.setLate_award("没有");
				}else{
					dc.setLate_award(dc.getLate_award()+"%");
				}
				if(("0").equals(dc.getAward())){
					dc.setAward("没有");
				}else if(("1").equals(dc.getAward())){
					if(dc.getPart_account()!=null && new BigDecimal(dc.getPart_account()).intValue()==0){
						dc.setAward("没有");
					}else{
						dc.setAward((String.valueOf(BigDecimalUtil.round(Double.parseDouble(dc.getPart_account()), 2)))+"%");
					}
				}else if(("2").equals(dc.getAward())){
					if(dc.getFunds()!=null&&new BigDecimal(dc.getFunds()).intValue()==0){
						dc.setAward("没有");
					}else{
						dc.setAward("￥"+(String.valueOf(BigDecimalUtil.round(Double.parseDouble(dc.getFunds()), 2))));
					}
				}
			}
			//v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
			// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 end
			// 2013/08/13 linhuimin update start 待还款流转标增加应还时间查询
//			ExcelHelper.writeExcel(infile, list, BorrowTender.class, Arrays.asList(names), Arrays.asList(titles));
			ExcelHelper.writeExcel(infile, list, DetailCollection.class, Arrays.asList(names), Arrays.asList(titles));
			// 2013/08/13 linhuimin update end
			export(infile, downloadFile);
			return null;
		}
	
	}
	/**
	 * 到期还款的流转标
	 */
	public String finishFlow() throws Exception{
		String type =  StringUtils.isNull(request.getParameter("type"));
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 start
		String pay_status = "payflow";
		request.setAttribute("pay_status", pay_status);
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 end
		String keywords = StringUtils.isNull(request.getParameter("keywords"));
		String username = StringUtils.isNull(request.getParameter("username"));
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam param = new SearchParam();
		param.setKeywords(keywords);
		param.setUsername(username);
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		String repay_time1 = StringUtils.isNull(request.getParameter("repay_time1"));
		String repay_time2 = StringUtils.isNull(request.getParameter("repay_time2"));
		param.setRepay_time1(repay_time1);
		param.setRepay_time2(repay_time2);
		String repay_yestime1 = StringUtils.isNull(request.getParameter("repay_yestime1"));
		String repay_yestime2 = StringUtils.isNull(request.getParameter("repay_yestime2"));
		param.setRepay_yestime1(repay_yestime1);
		param.setRepay_yestime2(repay_yestime2);
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
		PageDataList plist = memberBorrowService.getFlowList(param, page, pay_status);
		
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-04 start
		List<DetailCollection> dclist=plist.getList();
		for (DetailCollection dc : dclist) {
			if(("1").equals(dc.getAward())){
				dc.setPart_account((String.valueOf(BigDecimalUtil.round(Double.parseDouble(dc.getPart_account()), 2))));
			}
			if(("2").equals(dc.getAward())){
				dc.setFunds((String.valueOf(BigDecimalUtil.round(Double.parseDouble(dc.getFunds()), 2))));
			}
		}
		plist.setList(dclist);
		request.setAttribute("listsize", memberBorrowService.getFlowCount(param, pay_status));
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-04 end
		this.setPageAttribute(plist, param);
		// 2013/08/29 zhengzhiai update end 应还金额、应还利息、应还本息的统计
		String nowTime = DateUtils.getNowTimeStr();
		CollectionSumModel model = memberBorrowService.getFlowSum(param, nowTime, pay_status);
		// 应还金额、应还利息、应还本息的统计
		request.setAttribute("model", model);
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 end
		if (type.isEmpty()) {			
			return SUCCESS;
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "unFinishFlow_" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 start
			String[] names;
			String[] titles;
			//v1.6.7.2 RDPROJECT-510 cx 2013-12-04 start
			if (EnumBorrow.NOPAYFLOW.getValue().equals(pay_status)) {
				names = new String[] { "username", "borrow_name", "qsCount","repay_time",
						"repay_account","capital", "interest","late_award"};
				titles = new String[] { "投资人", "借款标的名称","期数", "到期时间", "待还总额", "待还本金", "待还利息","待还还款奖励（比例）"};
			} else {
				names = new String[] { "username", "borrow_name","qsCount", "repay_time",
						"repay_yestime", "repay_account", "repay_yesaccount", "capital",
						"interest","late_award","award"};
				titles = new String[] { "投资人", "借款标的名称","期数", "应还时间", "实际还款时间",
						"应还金额", "实还金额", "归还本金","归还利息","还款奖励（比列）","已付投标奖励" };
			}
			DecimalFormat df = new DecimalFormat("#0.000"); 
			List<DetailCollection> list =  memberBorrowService.getFlowList(param, pay_status);
			for (DetailCollection dc :list) {
				if(!StringUtils.isEmpty(dc.getRepay_account())){
					dc.setRepay_account(df.format(Double.parseDouble(dc.getRepay_account())));
				}
				if(!StringUtils.isEmpty(dc.getRepay_yesaccount())){
					dc.setRepay_yesaccount(df.format(Double.parseDouble(dc.getRepay_yesaccount())));
				}
				if(!StringUtils.isEmpty(dc.getCapital())){
					dc.setCapital(df.format(Double.parseDouble(dc.getCapital())));
				}
				if(!StringUtils.isEmpty(dc.getInterest())){
					dc.setInterest(df.format(Double.parseDouble(dc.getInterest())));
				}
				
				dc.setQsCount((dc.getOrder()+1)+"/"+dc.getTime_limit());
				if(dc.getLate_award()!=null && new BigDecimal(dc.getLate_award()).intValue()==0){
					dc.setLate_award("没有");
				}else{
					dc.setLate_award(dc.getLate_award()+"%");
				}
				if(("0").equals(dc.getAward())){
					dc.setAward("没有");
				}else if(("1").equals(dc.getAward())){
					if(dc.getPart_account()!=null && new BigDecimal(dc.getPart_account()).intValue()==0){
						dc.setAward("没有");
					}else{
						dc.setAward((String.valueOf(BigDecimalUtil.round(Double.parseDouble(dc.getPart_account()), 2)))+"%");
					}
				}else if(("2").equals(dc.getAward())){
					if(dc.getFunds()!=null&& new BigDecimal(dc.getFunds()).intValue()==0){
						dc.setAward("没有");
					}else{
						dc.setAward("￥"+(String.valueOf(BigDecimalUtil.round(Double.parseDouble(dc.getFunds()), 2))));
					}
				}
			}
			// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 end
			// 2013/08/13 linhuimin update start 待还款流转标增加应还时间查询
//			ExcelHelper.writeExcel(infile, list, BorrowTender.class, Arrays.asList(names), Arrays.asList(titles));
			ExcelHelper.writeExcel(infile, list, DetailCollection.class, Arrays.asList(names), Arrays.asList(titles));
			// 2013/08/13 linhuimin update end
		//v1.6.7.2 RDPROJECT-510 cx 2013-12-04 start
			export(infile, downloadFile);
			return null;
		}
	
	}
	/**
	 * 还款管理
	 * @return 
	 */
	public String bonus() throws Exception{
		List list=borrowService.getList(Constant.TYPE_PROJECT, Constant.STATUS_REPAYING);
		request.setAttribute("list", list);
		return SUCCESS;
	}
	
	public String bonusmodify() throws Exception{
		String type=StringUtils.isNull(request.getParameter("type"));
		if(type.equals("json")){
			long id=NumberUtils.getLong(request.getParameter("id"));
			double apr=NumberUtils.getDouble(request.getParameter("apr"));
			String errorMsg="";
			try {
				borrowService.modifyBonusAprById(id, apr/100);
			} catch (Exception e) {
				e.printStackTrace();
				errorMsg=e.getMessage();
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();  
			StringBuffer sb=new StringBuffer();
			if(errorMsg.isEmpty()){
				sb.append("{error:0,msg:100}");
			}else{
				logger.error(errorMsg);
				sb.append("{error:1,msg:"+errorMsg+"}");
			}
	        out.flush();   
	        out.close();
	        return null;
		}
		long borrow_id=NumberUtils.getLong(request.getParameter("borrow_id"));
		Borrow b=borrowService.getBorrow(borrow_id);
		List list=borrowService.getBonusAprList(borrow_id);
		request.setAttribute("list", list);
		request.setAttribute("borrow", b);
		return SUCCESS;
	}
	
	/**
	 * 查看所有授信额度
	 * @return
	 * @throws Exception
	 */
	public String allLimit() throws Exception{
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam param = new SearchParam();
		PageDataList plist = userAmountService.getUserAmount(page, param);
		setPageAttribute(plist, param);
		//保存返回页面
		//setMsgUrl("/admin/borrow/allLimit.html");
		return SUCCESS;
	}
	
	/**
	 * 后台显示投标记录
	 * @return
	 * @throws Exception
	 */
	public String tenderList() throws Exception {
		String type=StringUtils.isNull(request.getParameter("type"));
		long borrow_id=this.paramLong("borrow_id");
		Borrow b=borrowService.getBorrow(borrow_id);
		if(b==null){
			message("非法操作！","");
			return ADMINMSG;
		}
		if(type.isEmpty()){
			int page=this.paramInt("page");
			SearchParam param=new SearchParam();
			PageDataList tenderList=borrowService.getTenderList(borrow_id, page, param);
			// V1.6.6.2 RDPROJECT-380 lhm 2013-10-21 start
			// 设定投标奖励
			setAward(tenderList.getList(), b);
			// V1.6.6.2 RDPROJECT-380 lhm 2013-10-21 end
			setPageAttribute(tenderList, param);
			request.setAttribute("borrow", b);
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="tender_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			// V1.6.6.2 RDPROJECT-353 ljd 2013-10-23 start
			/*String[] names=new String[]{"username","money","account","repay_account","addtime","repay_time"};
			String[] titles=new String[]{"投标人","投标金额","有效金额","回收本息","投标时间","回收时间"};*/
			String[] names, titles;
			// 如果是流转标则在导出表中增加"预计还款时间"和"实际还款时间"两列
//			if(b.getIs_flow() == 1) {
			if(b.getType() == Constant.TYPE_FLOW) {
				names=new String[]{"username","money","account","award","repay_account","is_auto_tender","addtime","repay_time","repay_yestime"};
				titles=new String[]{"投标人","投标金额","有效金额","奖励","回收本息","投标类型","投标时间","预计还款时间","实际还款时间"};
			} else {
				names=new String[]{"username","money","account","award","repay_account","is_auto_tender","addtime"};
				titles=new String[]{"投标人","投标金额","有效金额","奖励","回收本息","投标类型","投标时间"};
			}
			// V1.6.6.2 RDPROJECT-353 ljd 2013-10-23 end
			List<BorrowTender> list=borrowService.getAllTenderList(borrow_id);
			// V1.6.6.2 RDPROJECT-380 lhm 2013-10-21 start
			// 设定投标奖励
			setAward(list, b);
			// V1.6.6.2 RDPROJECT-380 lhm 2013-10-21 end

			/*List list=borrowService.getTenderList(borrow_id);*/
			ExcelHelper.writeExcel(infile, list, BorrowTender.class, Arrays.asList(names), Arrays.asList(titles));
			export(infile, downloadFile);
			return null;
		}
		
	}
	
	// V1.6.6.2 RDPROJECT-380 lhm 2013-10-21 start
	/**
	 * 设定投标奖励
	 * @param list 投标列表
	 * @param b 借款
	 */
	private void setAward(List<BorrowTender> list, Borrow b) {
		for (int i = 0; i < list.size(); i++) {
			BorrowTender tender = list.get(i);
			double award = 0;
			if (b.getAward() == 1) {
				tender.setAward(b.getPart_account() + "%");
			} else if (b.getAward() == 2) {
				award = BigDecimalUtil.div(Double.parseDouble(tender.getAccount()),b.getAccount());
				award = BigDecimalUtil.mul(b.getFunds(), award);
				tender.setAward(String.valueOf(BigDecimalUtil.round(award, 2)));
			}
			
		}
	}
	// V1.6.6.2 RDPROJECT-380 lhm 2013-10-21 end
	
	public String stopflow() throws Exception{
		long borrow_id=paramLong("borrow_id");
		BorrowModel b=borrowService.getBorrow(borrow_id);
		if(b==null||b.getStatus()!=1){
			message("非法操作！","");
			return ADMINMSG;
		}
		b.setStatus(8);
		borrowService.updateBorrow(b);
		// v1.6.6.1 RDPROJECT-113 zza 2013-09-26 start
		message("操作成功！", "/admin/borrow/showAllBorrow.html");
		// v1.6.6.1 RDPROJECT-113 zza 2013-09-26 end
		return ADMINMSG;
	}
	
	public String collect() throws Exception{
		String type=StringUtils.isNull(request.getParameter("type"));
		long borrow_id=this.paramLong("borrow_id");
		Borrow b=borrowService.getBorrow(borrow_id);
		if(b==null){
			message("非法操作！","");
			return ADMINMSG;
		}
		if(type.isEmpty()){
			int page=this.paramInt("page");
			SearchParam param=new SearchParam();
			PageDataList tenderList=borrowService.getCollectionListByBorrow(borrow_id, page, param);
			setPageAttribute(tenderList, param);
			request.setAttribute("borrow", b);
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="collect"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"data/export/"+downloadFile;
			String[] names=new String[]{"username","capital","tendertime","repay_account","repay_time","repay_yestime"};
			String[] titles=new String[]{"投标人","投标金额","投标时间","本息","待收时间","实收时间"};
			List list=borrowService.getCollectionListByBorrow(borrow_id);
			try {
				ExcelHelper.writeExcel(infile, list, DetailCollection.class, Arrays.asList(names), Arrays.asList(titles));
			} catch (Exception e) {
				e.printStackTrace();
			}
			export(infile, downloadFile);
			return null;
		}
	}
	public String showAllRunBorrow(){
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		PageDataList plist=borrowService.jk(page,param);
		List list=plist.getList();
		setPageAttribute(plist, param);
		request.setAttribute("list", list);
		return "success";
	}
	public String showExpriredBorrow(){
		BorrowModel model=new BorrowModel();
		BorrowModel wrapModel=BorrowHelper.getHelper(Constant.TYPE_ALL,model);
		SearchParam param=new SearchParam();
		param.setUsername(StringUtils.isNull(request.getParameter("username")));
		model.setPageStart(NumberUtils.getInt(request.getParameter("page")));
		model.setStatus(Constant.STATUS_EXPIRED);
		model.setParam(param);
		try {
			showBorrowList(wrapModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//保存返回页面
		setMsgUrl("/admin/borrow/showAllBorrow.html");
		return SUCCESS;
	}
	public String showTenderAward(){
		String actiontype=request.getParameter("actiontype");
		if(StringUtils.isBlank(actiontype)){
			return "success";
		}else{
		//用户累计投资金额达标奖励分配
			String tender_award_yes_no=request.getParameter("tender_award_yes_no");
			TenderAwardYesAndNo tenderAwardYesAndNo=borrowService.TenderAward(1);
			if(tenderAwardYesAndNo.getTender_award_yes_no()==0){
				tenderAwardYesAndNo.setTender_award_yes_no(NumberUtils.getInt(tender_award_yes_no));
				tenderAwardYesAndNo.setId(1);
				borrowService.updateTenderAward(tenderAwardYesAndNo);
			}
			TenderAwardYesAndNo tenderAwardYesAndNo2=borrowService.TenderAward(1);
			if(tenderAwardYesAndNo2.getTender_award_yes_no()==1){
				String nowtimestring=DateUtils.getNowTimeStr();
				long nowtime=NumberUtils.getLong(nowtimestring);
				long award_start_time=NumberUtils.getLong(Global.getValue("award_start_time"));
				long award_end_time=NumberUtils.getLong(Global.getValue("award_end_time"));
				if(nowtime<award_end_time&&nowtime>award_start_time){
					String starttime=Global.getValue("tender_start_time");
					String endtime=Global.getValue("tender_end_time");
					List<User> list=userService.getAllUser(2);
					for(User user:list){
					    double tenderTotal=borrowService.hasTenderListByUserid(user.getUser_id(), starttime, endtime);
					    double tender_award=tender_award(tenderTotal);
					    if(tenderTotal-0.00>0){
					    	accountService.updateAccount(tender_award, tender_award, 0.00,tender_award, user.getUser_id(),1);
					    	Account account=accountService.getAccount(user.getUser_id());
					    	Global.setTransfer("username", user.getUsername());
					    	Global.setTransfer("award", tender_award);
					    	BaseAccountLog bLog = new AwardAccumulateTenderLog(tender_award, account);
					    	bLog.doEvent();
					    }
					    
				    }
					tenderAwardYesAndNo.setId(1);
			    	tenderAwardYesAndNo.setTender_award_yes_no(2);
					borrowService.updateTenderAward(tenderAwardYesAndNo);
				}
			}
		}
		 return "success";
	 }
	 private double tender_award(double tenderTotal){
			if(tenderTotal-1000000>=0.00){
				return tenderTotal*0.005;
			}else if(tenderTotal-500000>=0.00&&1000000-tenderTotal>0.00){
				return tenderTotal*0.004;
			}else if(tenderTotal-200000>=0.00&&500000-tenderTotal>0.00){
				return tenderTotal*0.003;
			}else if(tenderTotal-50000>=0.00&&200000-tenderTotal>0.00){
				return tenderTotal*0.002;
			}else{
				return tenderTotal*0.001;
			}
			
		}
	//预约用户列表
	public String reservation_list()throws Exception{
		String username=StringUtils.isNull(request.getParameter("username"));
		String dotime1=StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2=StringUtils.isNull(request.getParameter("dotime2"));
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		param.setUsername(username);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		PageDataList plist=borrowService.reservation_list(param, page);
		this.setPageAttribute(plist, param);
		return "success";
	}
	//预约用户添加
    public String reservation_add()throws Exception{
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		if(StringUtils.isBlank(actionType)){
			return "success";
		}
		Reservation reservation=new Reservation();
		String reservation_user_string=StringUtils.isNull(request.getParameter("reservation_user"));
		User user=userService.getUserByName(reservation_user_string);
		String borrow_apr=StringUtils.isNull(request.getParameter("borrow_apr"));
		String tender_account_string=StringUtils.isNull(request.getParameter("tender_account"));
		if(user!=null){
			reservation.setReservation_user(user.getUser_id());
			if(StringUtils.isBlank(tender_account_string)){
				double tender_account=NumberUtils.getDouble(tender_account_string);
				if(tender_account-0.000000==0){
					 message("请添加有效预约标投标金额，！","/admin/borrow/reservation_add.html"); 
						return ADMINMSG;
				}
				reservation.setTender_account(tender_account);
			}
			reservation.setBorrow_apr(borrow_apr);
			reservation.setAddtime(this.getTimeStr());
			reservation.setAddip(this.getRequestIp());
		    borrowService.reservation_add(reservation);
	        message("添加预约用户成功！","/admin/borrow/reservation_add.html"); 
			return ADMINMSG;
		}
		 message("预约用户不存在！","/admin/borrow/reservation_add.html"); 
		return ADMINMSG;
	}
   
    
    //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
    //TODO RDPROJECT-314 DELETE OTHER DONE autoSmsBorrowerRepayNotice
    /*
    public String  overdueReceivables(){
    	long borrow_id = Long.parseLong(request.getParameter("borrowid"));
    	List<Repayment> list = memberBorrowService.getLateRepaymentByBorrowid(borrow_id);
    	String subject = Global.getValue("webname") + "-还款通知";
    	User user = userService.getUserByName(list.get(0).getUsername());
		String to = user.getEmail();

		Mail mail = Mail.getInstance();
	
		mail.readOverdueReceivablesMsg();
		Map<String, String> m = new HashMap<String, String>();
		m.put("username", user.getUsername());
		m.put("email", user.getEmail());
		m.put("borrowname", list.get(0).getBorrow_name());
		m.put("account", NumberUtils.format2Str((Double
				.valueOf(list.get(0).getRepayment_account()))));
		m.put("order", String.valueOf(list.get(0).getOrder()));
    	m.put("latedays", list.get(0).getLate_days()+"");
    	m.put("lateInterest", NumberUtils.format2Str((Double
				.valueOf(list.get(0).getLate_interest()))));
    	String body = mail.bodyReplace(m);
    	try {
//			mail.sendMail(to, subject, body);
			message("邮件提醒成功，已发送邮件至用户提醒其还款！","/admin/borrow/late.html"); 
			return ADMINMSG;
		} catch (Exception e) {
			message("邮件提醒失败，发送邮件失败！","/admin/borrow/late.html");
			return ADMINMSG;
		}
    	

    }
    */
    //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
    /**
	 * 添加催款记录
	 * @param borrowid
	 * @return
	 */
	public String goLateLog(){
		String borrowid = paramString("borrowid");
		request.setAttribute("borrowid", borrowid);
		return SUCCESS;
	}
	
	public String addLateLog(){
		
		try {
			long borrow_id = paramLong("borrowid");  // 标  id
			String phone_type = paramString("phone_type");
			String phone_num = paramString("phone_num");
			String phone_status = paramString("phone_status");
			String relation_type = paramString("relation_type");
			String relation_name = paramString("relation_name");
			String repay_time = paramString("repay_time");
			String memo = paramString("content");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("borrow_id", borrow_id);
			params.put("phone_type", phone_type);
			params.put("phone_num", phone_num);
			params.put("phone_status", phone_status);
			params.put("relation_type", relation_type);
			params.put("relation_name", relation_name);
			params.put("repay_time", repay_time);
			params.put("memo", memo);
			borrowService.addLateLog(params);
			this.message("添加成功","/admin/borrow/late.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ADMINMSG;
	}
	/**
	 * get  催款记录
	 * @return
	 */
	public String getLateBorrowDetails(){
		try {
			String borrowid= paramString("borrowid");
			List<Map<String, Object>> list = borrowService.getLateLog(borrowid);
			request.setAttribute("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 查看所有贷款快速通道信息
	 * @return
	 * @throws Exception
	 */
	public String showAllQuickenLoans() throws Exception {
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam searchParam = new SearchParam();
		PageDataList pageDataList = quickenLoansService.getList(page, searchParam);
		request.setAttribute("list", pageDataList);
		setPageAttribute(pageDataList, searchParam);
		setMsgUrl("/admin/borrow/showAllQuickenLoans.html");
		return "success";
	}
	
	/**
	 * 根据Id删除贷款快速通道信息
	 * @return
	 * @throws Exception
	 */
	public String delQuickenLoans() throws Exception {
		int loansId=Integer.valueOf(request.getParameter("loansId"));
		quickenLoansService.delQuickenLoans(loansId);
		message("删除信息成功！","/admin/borrow/showAllQuickenLoans.html");
		return ADMINMSG;
	}
	/**
	 * 给力标到期
	 * @return
	 * @throws Exception
	 */
	public String showFastExpire() throws Exception {
		String type=StringUtils.isNull(request.getParameter("type"));
		int page=NumberUtils.getInt(request.getParameter("page"));
		String username =StringUtils.isNull(request.getParameter("username"));
		SearchParam param = new SearchParam();
		param.setUsername(username);
		PageDataList plist = borrowService.getFastExpireList(param, page);
		setPageAttribute(plist, param);
		setMsgUrl("/admin/borrow/showFastExpire.html");
		if(type.isEmpty()){
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="fastExpire_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"data/export/"+downloadFile;
			String[] names=new String[]{"borrow_id","borrow_user","borrow_name","repayment_time","repayment_account","late_days","forfeit"};
			String[] titles=new String[]{"ID","借款人","借款标题","应还时间","应还金额","逾期天数","罚金"};
			List fastExpireList = borrowService.getFastExpireList(param);
			ExcelHelper.writeExcel(infile,fastExpireList, FastExpireModel.class, Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
	}
	
	/**
	 * 排行榜（总、月、日）
	 * @return
	 * @throws Exception
	 */
	public String rank() throws Exception {
		String dotime1 = request.getParameter("dotime1");
		String dotime2 = request.getParameter("dotime2");
		int page = NumberUtils.getInt(request.getParameter("page"));
		String type = StringUtils.isNull(request.getParameter("type"));
		SearchParam param=new SearchParam();
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		// 总排行
		PageDataList plist = borrowService.getRankListByTime(page, param);
		this.setPageAttribute(plist, param);
		//保存返回页面
		setMsgUrl("/admin/borrow/rank.html");
		if(type.isEmpty()){
			return SUCCESS;
		}else{
			
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="account_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"data/export/"+downloadFile;
			String[] names=new String[]{"user_id","username","realname","tenderMoney"};
			String[] titles=new String[]{"用户ID","用户名","真实姓名","投资总额"};
			List<RankModel> list = borrowService.getRankListByTime(param);
			ExcelHelper.writeExcel(infile, list, RankModel.class, Arrays.asList(names), Arrays.asList(titles));
			export(infile, downloadFile);
			return null;
			
		}
	}
    /**
     * 借款配置
     */
	public String config(){
		List list= borrowService.getBorrowConfig();
		request.setAttribute("list", list);
		return SUCCESS;
	}
	/**
	 * 借款配置通过id显示
	 */
	public String viewConfig(){
		String idString=StringUtils.isNull(request.getParameter("id"));
		String actionType=StringUtils.isNull(request.getParameter("type"));
        long id=0;
		if(StringUtils.isBlank(actionType)){
        	if(!StringUtils.isBlank(idString)){}
        	id=Integer.parseInt(idString);
        	BorrowConfig config=borrowService.viewConfig(id);
            request.setAttribute("config", config);
		}else{
			BorrowConfig borrowConfig=getParameter();
			borrowService.updateConfig(borrowConfig);
			message("操作成功", "/admin/borrow/config.html"); 
			return ADMINMSG;
		} 
		return SUCCESS;
	}
	/**
	 * 获取borrowConfig对象
	 * @return
	 */
	public BorrowConfig getParameter(){
		BorrowConfig borrowConfig=new BorrowConfig();
		long id=NumberUtils.getLong(StringUtils.isNull(request.getParameter("id")));
	    double mostAccount=NumberUtils.getDouble(StringUtils.isNull(request.getParameter("most_account")));
	    double lowestAccount=NumberUtils.getDouble(StringUtils.isNull(request.getParameter("lowest_account")));
	    double mostApr=NumberUtils.getDouble(StringUtils.isNull(request.getParameter("most_apr")));
	    double lowestApr=NumberUtils.getDouble(StringUtils.isNull(request.getParameter("lowest_apr")));
		int isTrail=NumberUtils.getInt(StringUtils.isNull(request.getParameter("is_trail")));
		int isReview=NumberUtils.getInt(StringUtils.isNull(request.getParameter("is_review")));
		double managefee=NumberUtils.getDouble(StringUtils.isNull(request.getParameter("managefee")));
		double daymanagefee=NumberUtils.getDouble(StringUtils.isNull(request.getParameter("daymanagefee")));
		borrowConfig.setId(id);
		borrowConfig.setMost_account(mostAccount);
		borrowConfig.setLowest_account(lowestAccount);
		borrowConfig.setMost_apr(mostApr);
		borrowConfig.setLowest_apr(lowestApr);
		borrowConfig.setIs_review(isReview);
		borrowConfig.setIs_trail(isTrail);
		borrowConfig.setManagefee(managefee);
		borrowConfig.setDaymanagefee(daymanagefee);
		
        return borrowConfig;
	}
	
	/**
	 * 截标，只是将招标金额改为已招标金额
	 * @return
	 */
	public String stopTender() throws Exception{
		long borrow_id=paramLong("borrow_id");
		BorrowModel b=borrowService.getBorrow(borrow_id);
		if(b==null||b.getStatus()!=1){
			message("非法操作！","");
			return ADMINMSG;
		}
		borrowService.stopTender(b);
		message("操作成功！","");
		return ADMINMSG;
	}
	

}
