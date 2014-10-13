package com.p2psys.web.action.admin;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.AuditType;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Attestation;
import com.p2psys.domain.AttestationType;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.OperationLog;
import com.p2psys.domain.Rule;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.model.DetailUser;
import com.p2psys.model.IdentifySearchParam;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCacheModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.noac.UserCertifyCancelLog;
import com.p2psys.model.accountlog.noac.UserCertifyFailLog;
import com.p2psys.model.accountlog.noac.UserCertifySuccLog;
import com.p2psys.service.AccountService;
import com.p2psys.service.AttestationService;
import com.p2psys.service.RewardStatisticsService;
import com.p2psys.service.RuleService;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.tool.AutoVerifyRealnameUtils;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class AttestationAction extends BaseAction implements ModelDriven<AttestationType> {
	private UserService userService;
	private UserinfoService userinfoService;
	private AttestationService attestationService;
	private List<AttestationType> attestationTypeslist;
	private AttestationType attestationType = new AttestationType();
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
	//private Message message = new Message();
	//private MessageService messageService;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
	private AccountService accountService;
	private RewardStatisticsService rewardStatisticsService;
	private UserCreditService userCreditService;
	private RuleService ruleService;
	
	public RuleService getRuleService() {
		return ruleService;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	public UserCreditService getUserCreditService() {
		return userCreditService;
	}

	public void setUserCreditService(UserCreditService userCreditService) {
		this.userCreditService = userCreditService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
	/*public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end

	public List<AttestationType> getAttestationTypeslist() {
		return attestationTypeslist;
	}

	public void setAttestationTypeslist(List<AttestationType> attestationTypeslist) {
		this.attestationTypeslist = attestationTypeslist;
	}

	public AttestationService getAttestationService() {
		return attestationService;
	}

	public void setAttestationService(AttestationService attestationService) {
		this.attestationService = attestationService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserinfoService getUserinfoService() {
		return userinfoService;
	}

	public void setUserinfoService(UserinfoService userinfoService) {
		this.userinfoService = userinfoService;
	}

	/**
	 * @return the rewardStatisticsService
	 */
	public RewardStatisticsService getRewardStatisticsService() {
		return rewardStatisticsService;
	}

	/**
	 * @param rewardStatisticsService the rewardStatisticsService to set
	 */
	public void setRewardStatisticsService(RewardStatisticsService rewardStatisticsService) {
		this.rewardStatisticsService = rewardStatisticsService;
	}

	public String index() {
		return SUCCESS;
	}

	/**
	 * 实名认证
	 * 
	 * @return 实名认证页面
	 */
	public String verifyRealname() {
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		String actiontype=paramString("type");
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		// 实名认证积分
		CreditType emailType = userCreditService.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_REAL_NAME.getValue());
		long creditValue = emailType.getValue();
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = paramString("username");
		// 真实姓名
		String realname = paramString("realname");
		// 证件号码
		String card_id = paramString("card_id");
		String status = paramString("status");
		IdentifySearchParam param = new IdentifySearchParam();
		// 默认情况下查找待审核的记录
		if (status.isEmpty()) {
			param.setReal_status("2");
		} else {
			param.setReal_status(status);
		}
		param.setUsername(username);
		// 真实姓名
		param.setRealname(realname);
		// 证件号码
		param.setCard_id(card_id);
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		param.setVerify_type(paramInt("verify_type"));
		param.setVerify_start_time(paramString("verify_start_time"));
		param.setVerify_end_time(paramString("verify_end_time"));
		
		//v1.6.7.2 sj 2013-12-23 start
		RuleModel realCardCheck = new RuleModel(Global.getRule(EnumRuleNid.REALCARDCHECK.getValue()));
		if(realCardCheck != null && realCardCheck.getStatus() == 1 && "1".equals(realCardCheck.getValueStrByKey("need_upload_card_pic"))){
			request.setAttribute("show_pic", "show_pic");
		}
		//v1.6.7.2 sj 2013-12-23 end
		
		if(StringUtils.isBlank(actiontype)){	
			//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
			PageDataList plist = null;
			plist = userService.getUserList(page, param);
			this.setPageAttribute(plist, param);
			request.setAttribute("verifyType", EnumIntegralTypeName.INTEGRAL_REAL_NAME.getValue());
			request.setAttribute("creditValue", creditValue);
			// v1.6.7.1 RDPROJECT-289 sj 2013-11-19 start
			request.setAttribute("isNotice", paramInt("isNotice"));
			// v1.6.7.1 RDPROJECT-289 sj 2013-11-19 end
			//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "verifyRealname" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			String[] names,titles;
			names = new String[] { "username", "realname","nature", "sex","birthday", "card_type", "card_id", "provincetext",
						"citytext", "areatext", "realname_verify_time", "real_status" };
			titles = new String[] { "用户名称", "真实姓名","会员类型(1:自然人,2:公司法人)", "性别（1:男，2:女）", "生日", "证件类型", "证件号码", "省","市","县", "审核时间", "状态(1:成功,2：审核中,-1：审核失败)"};
			List list=userService.getUserList(param);
			try {
				ExcelHelper.writeExcel(infile, list, DetailUser.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		return SUCCESS;
	}
	
	//v1.6.7.2 RDPROJECT-571 sj 2013-12-12 start
	/**
	 * 实名认证自动审核
	 * 
	 */
	public String autoVerifyRealname() throws Exception{
		String card_id = paramString("card_id");
		long user_id = paramLong("user_id");
		String realname = paramString("realname");
		int count = userService.getCountCard(card_id);
		if(count > 1){
			message("多个用户使用该证件号码申请实名认证，请手动审核！", "/admin/attestation/verifyRealname.html");
			return ADMINMSG;
		}
		//证件号码自动审核规则
		RuleModel realCardCheck = new RuleModel(Global.getRule(EnumRuleNid.REALCARDCHECK.getValue()));
		String result = "";
		if(realCardCheck != null && realCardCheck.getStatus() == 1){
			String check_url = realCardCheck.getValueStrByKey("check_url");
			String username = realCardCheck.getValueStrByKey("username");
			String password = realCardCheck.getValueStrByKey("password");
			result = AutoVerifyRealnameUtils.checkAutoVerifyRealname(
					check_url+"?username="+username+"&password="+password+"&sfznum="+card_id+"&truename="+URLEncoder.encode(realname,"UTF-8"));
			String[] s = result.split("&");
			result = s[0];
		}
		if("result=1".equals(result.trim())){
			//证件号码和姓名通过接口验证匹配成功，审核通过并添加信用积分
			User user = new User();
			user.setUser_id(user_id);
			user.setReal_status(1);
			userService.modifyReal_status(user);
			// 证件实名认证积分
			userCreditService.updateUserCredit(user_id, 10, new Byte(Constant.OP_ADD),
					EnumIntegralTypeName.INTEGRAL_REAL_NAME.getValue());
			message("自动审核成功！","/admin/attestation/verifyRealname.html");
			//v1.6.7.2 sj 2013-12-23 start
			Global.setTransfer("type", "实名认证");
			BaseAccountLog blog = new UserCertifySuccLog(user_id);
			blog.doEvent();
			//v1.6.7.2 sj 2013-12-23 end
			return ADMINMSG;
		}else{
			//v1.6.7.2 sj 2013-12-23 start
			message("自动审核校验失败！用户实名申请信息不准确！","/admin/attestation/verifyRealname.html");
			Global.setTransfer("type", "实名认证");
			BaseAccountLog blog = new UserCertifyFailLog(user_id);
			blog.doEvent();
			//v1.6.7.2 sj 2013-12-23 end
			return ADMINMSG;
		}
	}
	//v1.6.7.2 RDPROJECT-571 sj 2013-12-12 end
	
	public String verifyPhone() {
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		String actiontype=paramString("type");
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String status = StringUtils.isNull(request.getParameter("status"));
		String phone = StringUtils.isNull(request.getParameter("phone"));
		IdentifySearchParam param = new IdentifySearchParam();
		if (status.isEmpty()) {
			param.setPhone_status("2");
		} else {
			param.setPhone_status(status);
		}
		param.setUsername(username);
		param.setPhone(phone);
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		param.setVerify_type(paramInt("verify_type"));
		param.setVerify_start_time(paramString("verify_start_time"));
		param.setVerify_end_time(paramString("verify_end_time"));
		if(StringUtils.isBlank(actiontype)){	
			//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
			PageDataList plist = null;
			plist = userService.getUserList(page, param);
			setPageAttribute(plist, param);
			request.setAttribute("verifyType", EnumIntegralTypeName.INTEGRAL_PHONE.getValue());
			//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "verifyPhone" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			String[] names,titles;
			names = new String[] { "username", "realname", "phone","phone_status","phone_verify_time"};
			titles = new String[] { "用户名称", "真实姓名", "手机号码", "状态（1：成功，2：审核中，-1:审核失败）",  "审核时间"};
			List list=userService.getUserList(param);
			try {
				ExcelHelper.writeExcel(infile, list, DetailUser.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		return SUCCESS;
	}

	public String verifyVideo() {
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		String actiontype=paramString("type");
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		CreditType emailType = userCreditService.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_VIDEO.getValue());
		long creditValue = emailType.getValue();
		request.setAttribute("creditValue", creditValue);
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String status = StringUtils.isNull(request.getParameter("status"));
		IdentifySearchParam param = new IdentifySearchParam();
		if (status.isEmpty()) {
			param.setVideo_status("2");
		} else {
			param.setVideo_status(status);
		}
		param.setUsername(username);
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		param.setVerify_type(paramInt("verify_type"));
		param.setVerify_start_time(paramString("verify_start_time"));
		param.setVerify_end_time(paramString("verify_end_time"));
		if(StringUtils.isBlank(actiontype)){	
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
			PageDataList plist = null;
			plist = userService.getUserList(page, param);
			setPageAttribute(plist, param);
			request.setAttribute("verifyType", EnumIntegralTypeName.INTEGRAL_VIDEO.getValue());
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "verifyVideo" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			String[] names,titles;
			names = new String[] { "username", "realname","video_status","video_verify_time"};
			titles = new String[] { "用户名称", "真实姓名",  "状态（1：成功，2：审核中，-1:审核失败）",  "审核时间"};
			List list=userService.getUserList(param);
			try {
				ExcelHelper.writeExcel(infile, list, DetailUser.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		return SUCCESS;
	}

	public String verifyScene() {
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		String actiontype=paramString("type");
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String status = StringUtils.isNull(request.getParameter("status"));
		IdentifySearchParam param = new IdentifySearchParam();
		if (status.isEmpty()) {
			param.setScene_status("2");
		} else {
			param.setScene_status(status);
		}
		param.setUsername(username);
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		param.setVerify_type(paramInt("verify_type"));
		param.setVerify_start_time(paramString("verify_start_time"));
		param.setVerify_end_time(paramString("verify_end_time"));
		if(StringUtils.isBlank(actiontype)){	
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
			PageDataList plist = null;
			plist = userService.getUserList(page, param);
			setPageAttribute(plist, param);
			request.setAttribute("verifyType", EnumIntegralTypeName.INTEGRAL_SCENE.getValue());
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "verifyScene" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			String[] names,titles;
			names = new String[] { "username", "realname","scene_status","scene_verify_time"};
			titles = new String[] { "用户名称", "真实姓名",  "状态（1：成功，2：审核中，-1:审核失败）",  "审核时间"};
			List list=userService.getUserList(param);
			try {
				ExcelHelper.writeExcel(infile, list, DetailUser.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		return SUCCESS;
	}

	public String vip() {
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		String actiontype=paramString("actiontype");
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		// vip状态
		int pages = NumberUtils.getInt(request.getParameter("page"));
		int status = NumberUtils.getInt(request.getParameter("status"));
		// type=1vip申请 type=2vip赠送申请
		int type = NumberUtils.getInt(request.getParameter("type"));
		SearchParam param = new SearchParam();
		param.setUsername(request.getParameter("username"));
		param.setKefu_username(request.getParameter("kefu_name"));
		// searchParam.setStatus(status);
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		param.setVerify_type(paramInt("verify_type"));
		param.setVerify_start_time(paramString("verify_start_time"));
		param.setVerify_end_time(paramString("verify_end_time"));
		int ruleStatus=0;
		Rule rule = Global.getRule(EnumRuleNid.RECHARGE_VIP_GIVETIME.getValue());
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			 ruleStatus = rule.getStatus();
		}
		if(StringUtils.isBlank(actiontype)){	
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
			PageDataList pageDataList = new PageDataList();
			if (ruleStatus == 1) {
				pageDataList = userinfoService.getUserVipinfo(pages, 10, status, type, param);
				request.setAttribute("usercache", pageDataList.getList());
				request.setAttribute("page", pageDataList.getPage());
				request.setAttribute("params", param.toMap());
				return SUCCESS;
			} else {
				pageDataList = userinfoService.getUserVipinfo(pages, 10, status, param);
				request.setAttribute("vipinfo", pageDataList.getList());
				request.setAttribute("page", pageDataList.getPage());
				request.setAttribute("params", param.toMap());
			}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "verifyVip" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			String[] names,titles;
			
			List list=new ArrayList();
			PageDataList pageDataList = new PageDataList();
			if (ruleStatus == 1) {
				names = new String[] { "username", "kefu_name","kefu_addtime","type_id","account_waitvip",
						"vip_give_status","vip_give_month","valid_vip_time","vip_end_time",
						"last_vip_time"};
				titles = new String[] { "用户名称", "客服名称",  "添加时间",  "用户类型",  "状态",   "vip赠送状态", 
						"vip赠送月份",  "vip有效期限",  "vip截止时间",  "vip剩余期限"};
				 list=userinfoService.getUserVipinfo(param,type,status,ruleStatus);
			}else{
				names = new String[] { "username", "kefu_name","kefu_addtime","type_id","account_waitvip",
						"valid_vip_time","vip_end_time",
						"last_vip_time"};
				titles = new String[] { "用户名称", "客服名称",  "添加时间",  "用户类型",  "状态",    
						 "vip有效期限",  "vip截止时间",  "vip剩余期限"};
				 list=userinfoService.getUserVipinfo(param,status);
			} 
			try {
				ExcelHelper.writeExcel(infile, list, UserCacheModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		return SUCCESS;
	}

	public String viewvip() {
		int vipStatus = NumberUtils.getInt(request.getParameter("vip_status"));
 		int vip_give_status = NumberUtils.getInt(request.getParameter("vip_give_status"));

		long user_id = NumberUtils.getLong(request.getParameter("user_id"));
		String vipVerifyRemark = request.getParameter("vip_verify_remark");
		String validcode = StringUtils.isNull(request.getParameter("validcode"));
		UserCacheModel vipinfo = userService.getUserCacheByUserid(user_id);
		User auth_user = (User) session.get(Constant.AUTH_USER);
		//
		Rule rule = Global.getRule(EnumRuleNid.RECHARGE_VIP_GIVETIME.getValue());
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			int type =2;
			int ruleStatus = rule.getStatus();
			if (ruleStatus == 1) {
				UserCache userCaches = userService.getUserCacheByUserid(user_id, vip_give_status,2);
				request.setAttribute("usercaches", userCaches);
				if (!StringUtils.isBlank(actionType)) {
					// 查看vip赠送信息添加验证码验证
					if ("".equals(validcode)) {
						message("验证码不能为空！", "/admin/attestation/viewvip.html?type=" + type + "&user_id=" + user_id);
						return ADMINMSG;
					}
					if (!this.checkValidImg(validcode)) {
						message("你输入的验证码错误！", "/admin/attestation/viewvip.html?type=" + type + "&user_id=" + user_id);
						return ADMINMSG;
					}

					// 操作日志
					DetailUser detailUser = userService.getDetailUser(auth_user.getUser_id());
					DetailUser newdetailUser = userService.getDetailUser(user_id);
					OperationLog operationLog = new OperationLog(user_id, auth_user.getUser_id(), "",
							this.getTimeStr(), this.getRequestIp(), "");

					if (vip_give_status == 1) {
						long userId = (long) user_id;
						UserCache userCache = userService.getUserCacheByUserid(userId);
						if (userCache.getVip_give_status() == 1) {
							message("该用户vip赠送申请已经审核通过！", "/admin/attestation/vip.html?status=2");
							return MSG;
						} else {
							userCache.setVip_give_status(vip_give_status);
							// 更新结束时间
							String vipEndTimeString = "";
							if (StringUtils.isBlank(userCache.getVip_end_time())) {
								if (StringUtils.isBlank(userCache.getVip_verify_time())) {
									vipEndTimeString = DateUtils.getTimeStr(DateUtils.rollMon(
											DateUtils.getDate(DateUtils.getNowTimeStr()),
											(int) userCache.getVip_give_month()));

								} else {
									vipEndTimeString = DateUtils.getTimeStr(DateUtils.rollMon(
											DateUtils.getDate(userCache.getVip_verify_time()),
											(int) userCache.getVip_give_month()));

								}
							} else {
								vipEndTimeString = DateUtils.getTimeStr(DateUtils.rollMon(
										DateUtils.getDate(userCache.getVip_end_time()),
										(int) userCache.getVip_give_month()));
							}
							userCache.setVip_end_time(vipEndTimeString);
							userCache.setVip_status(1);
							userCache.setVip_verify_time(DateUtils.getNowTimeStr());
							userService.saveUserCache(userCache);
							operationLog.setType(Constant.GIVE_VIP_SUCCESS);
							operationLog.setOperationResult("" + detailUser.getTypename() + "（"
									+ operationLog.getAddip() + "）用户名为" + detailUser.getUsername() + "的操作员审核赠送vip"+userCache.getVip_give_month()+"个月用户"
									+ newdetailUser.getUsername() + "成功通过");
							accountService.addOperationLog(operationLog);
							message("审核成功！", "/admin/attestation/vip.html?type=2&status=2");
							//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
							//TODO RDPROJECT-314 DONE
							// 审核成功后，发送邮件
							/*message = getSiteMessage(auth_user.getUser_id(), "申请VIP赠送成功！", "申请VIP成功！", user_id);
							messageService.addMessage(message);*/

							Global.setTransfer("type", "申请VIP");
							BaseAccountLog blog=new UserCertifySuccLog(user_id);
							blog.doEvent();
							//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
							return ADMINMSG;
						}

					} else {
						long userId = (long) user_id;
						UserCache userCache = userService.getUserCacheByUserid(userId);
						if (userCache.getVip_status() != 1) {
							userCache.setVip_status(-1);
						}
						userCache.setVip_give_status(vip_give_status);
						userCache.setVip_verify_remark(vipVerifyRemark);
						userCache.setVip_verify_time(this.getTimeStr());
						userService.saveUserCache(userCache);
						message("审核没有通过！", "/admin/attestation/vip.html");

						operationLog.setType(Constant.VIP_FAIL);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
								+ "）用户名为" + detailUser.getUsername() + "的操作员审核赠送vip"+userCache.getVip_give_month()+"个月的用户" + newdetailUser.getUsername()
								+ "失败未通过");
						accountService.addOperationLog(operationLog);
						Global.setTransfer("type", "申请VIP");
						BaseAccountLog blog=new UserCertifyFailLog(user_id);
						blog.doEvent();
						return ADMINMSG;
					}
				}
				return SUCCESS;
			}
		} else {
			if (!StringUtils.isBlank(actionType)) {
				// 查看vip信息添加验证码验证
				if ("".equals(validcode)) {
					message("验证码不能为空！", "/admin/attestation/viewvip.html?user_id=" + user_id);
					return ADMINMSG;
				}
				if (!this.checkValidImg(validcode)) {
					message("你输入的验证码错误！", "/admin/attestation/viewvip.html?user_id=" + user_id);
					return ADMINMSG;
				}

				// 操作日志
				DetailUser detailUser = userService.getDetailUser(auth_user.getUser_id());
				DetailUser newdetailUser = userService.getDetailUser(user_id);
				OperationLog operationLog = new OperationLog(user_id, auth_user.getUser_id(), "", this.getTimeStr(),
						this.getRequestIp(), "");

				if (vipStatus == 1) {
					long userId = (long) user_id;
					UserCache userCache = userService.getUserCacheByUserid(userId);
					if (userCache.getVip_status() == 1) {
						message("该用户vip申请已经审核通过！", "/admin/attestation/vip.html?status=2");
						return MSG;
					} else {
						userCache.setVip_status(1);
						userCache.setVip_verify_remark(vipVerifyRemark);
						userCache.setVip_verify_time(this.getTimeStr());
						// vip审核通过后期限默认为一年
						userCache.setVip_end_time(DateUtils.getTimeStr(DateUtils.rollYear(
								DateUtils.getDate(this.getTimeStr()), 1)));
						// User newUser = userService.getUserById(userId);
						// session.put(Constant.SESSION_USER, newUser);
						// userinfoService.VerifyVipSuccess(userCache);

						AccountLog accountLog = new AccountLog(userId, Constant.VIP_FEE, userId, this.getTimeStr(),
								this.getRequestIp());
						AccountLog inviteLog = new AccountLog(userId, Constant.INVITE_MONEY, userId, this.getTimeStr(),
								this.getRequestIp());
						userinfoService.VerifyVipSuccess(userCache, accountLog, inviteLog);
						operationLog.setType(Constant.VIP_SUCCESS);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
								+ "）用户名为" + detailUser.getUsername() + "的操作员审核vip用户" + newdetailUser.getUsername()
								+ "成功通过，扣除用户名为" + newdetailUser.getUsername() + "的用户vip费用" + Global.getValue("vip_fee")
								+ "");
						accountService.addOperationLog(operationLog);
						message("审核成功！", "/admin/attestation/vip.html?status=2");
						User user = userService.getUserById(user_id);
						// v1.6.6.2 RDPROJECT-287 zza 2013-10-24 start
						Rule inviteAward = Global.getRule(EnumRuleNid.INVITE_AWARD.getValue());
						if (inviteAward != null && !StringUtils.isBlank(user.getInvite_userid())
								&& inviteAward.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
							userCache = userService.getUserCacheByUserid(userId);
							rewardStatisticsService.updateReward(operationLog.getAddip(), userCache);
						}
						// v1.6.6.2 RDPROJECT-287 zza 2013-10-24 end
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						// 审核成功后，发送邮件
						/*message = getSiteMessage(auth_user.getUser_id(), "申请VIP成功！", "申请VIP成功！", user_id);
						messageService.addMessage(message);*/
						

						Global.setTransfer("type", "申请VIP");
						BaseAccountLog blog=new UserCertifySuccLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						return ADMINMSG;
					}
				// v1.6.7.1 RDPROJECT-100 zza 2013-11-11 start
				} else if (vipStatus == 2) { //免年费续期
					long userId = (long) user_id;
					UserCache userCache = userService.getUserCacheByUserid(userId);
					if (userCache.getVip_status() == 1) {
						message("该用户vip申请已经审核通过！", "/admin/attestation/vip.html?status=2");
						return MSG;
					} else {
						userCache.setVip_status(1);
						userCache.setVip_verify_remark(vipVerifyRemark);
						userCache.setVip_verify_time(this.getTimeStr());
						// vip审核通过后期限默认为一年
						userCache.setVip_end_time(DateUtils.getTimeStr(DateUtils.rollYear(
								DateUtils.getDate(this.getTimeStr()), 1)));
						AccountLog accountLog = new AccountLog(userId, Constant.VIP_FEE, userId, this.getTimeStr(),
								this.getRequestIp());
						userinfoService.verifyVipSuccess(userCache, accountLog, 2);
						operationLog.setType(Constant.VIP_SUCCESS);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
								+ "）用户名为" + detailUser.getUsername() + "的操作员为用户【" + newdetailUser.getUsername()
								+ "】免年费续期成功!");
						accountService.addOperationLog(operationLog);
						message("审核成功！", "/admin/attestation/vip.html?status=2");
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						// 审核成功后，发送邮件
						/*message = getSiteMessage(auth_user.getUser_id(), "申请VIP成功！", "申请VIP成功！", user_id);
						messageService.addMessage(message);*/
						

						Global.setTransfer("type", "申请VIP");
						BaseAccountLog blog=new UserCertifySuccLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						return ADMINMSG;
					}
					// v1.6.7.1 RDPROJECT-100 zza 2013-11-11 end
				} else {
					long userId = (long) user_id;
					UserCache userCache = userService.getUserCacheByUserid(userId);
					userCache.setVip_status(-1);
					userCache.setVip_verify_remark(vipVerifyRemark);
					userCache.setVip_verify_time(this.getTimeStr());
					// userinfoService.VerifyVipFail(userCache);

					AccountLog accountLog = new AccountLog(userId, Constant.VIP_FEE, userId, this.getTimeStr(),
							this.getRequestIp());
					userinfoService.VerifyVipFail(userCache, accountLog);
					message("审核没有通过！", "/admin/attestation/vip.html");

					operationLog.setType(Constant.VIP_FAIL);
					operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
							+ "）用户名为" + detailUser.getUsername() + "的操作员审核vip用户" + newdetailUser.getUsername()
							+ "失败未通过");
					accountService.addOperationLog(operationLog);
					//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
					//TODO RDPROJECT-314 DONE
					/*message = getSiteMessage(auth_user.getUser_id(), "申请VIP失败！", "申请VIP失败！</br>" + "审核信息："
							+ vipVerifyRemark, user_id);
					messageService.addMessage(message);*/
									

					Global.setTransfer("type", "申请VIP");
					BaseAccountLog blog=new UserCertifyFailLog(user_id);
					blog.doEvent();
					//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end

					return ADMINMSG;
				}
			}

			request.setAttribute("vipinfo", vipinfo);
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	public String typelist() {
		request.setAttribute("type", attestationService.getAttestationType());
		return SUCCESS;
	}

	public String typeaddoredit() {
		String type = request.getParameter("type");
		if (type.equals("add")) {
			attestationService.addAttestationType(attestationType);
		} else {
			if (type.equals("update")) {
				attestationService.updateAttestationTypeByList(attestationTypeslist);
			} else {
				return ERROR;
			}
		}
		return SUCCESS;
	}

	public String typeadd() {
		return SUCCESS;
	}

	// 查看用户详细信息
	public String viewUserInfo() {
		long user_id = 0L;
		String username = request.getParameter("username");
		String viewType = request.getParameter("viewType");
		User userInfo = userinfoService.getUserinfoByUsername(username);
		if (userInfo != null && userInfo.getUser_id() != 0L) {
			user_id = userInfo.getUser_id();
		}
		request.setAttribute("userCreditInfo", userService.getDetailUser(user_id));
		request.setAttribute("userUseMoneyInfo", userService.getDetailUser(user_id));
		request.setAttribute("userInfo", userInfo);
		if ("viewCard".equals(viewType)) {
			String viewcard = request.getParameter("viewcard");
			request.setAttribute("viewcard", viewcard);
			return "viewType";
		}
		return SUCCESS;
	}
	
	// 弹出审核信息审核窗口
	public String viewAudit() {
		// v1.6.7.1 RDPROJECT-289 sj 2013-11-19 start
		//获得重复证件号的个数
		String cardId = paramString("card_id");
		int isNotice = paramInt("isNotice");
		int count = userService.getCountCard(cardId);
		if(count>1 && isNotice!=1){
			message("多个用户使用该证件号码申请实名认证！", "/admin/attestation/verifyRealname.html?card_id="+cardId+"&isNotice=1");
			return ADMINMSG;
		}else{
			long user_id = paramLong("user_id");
			String type = paramString("type");
			CreditType emailType = userCreditService.getCreditTypeByNid(type);
			long creditValue = emailType.getValue();
			request.setAttribute("creditValue", creditValue);
			request.setAttribute("type", type);
			request.setAttribute("user_id", user_id);
			request.setAttribute("card_id", cardId);
			return SUCCESS;
		}
		// v1.6.7.1 RDPROJECT-289 sj 2013-11-19 end
	}
	
	
	// 弹出客户证明材料窗口
	public String userCertifyAudit() {
		/*
		 * long user_id = NumberUtils.getLong(request.getParameter("user_id")); String type_name =
		 * StringUtils.isNull(request.getParameter("type_name"));
		 */
		String type = StringUtils.isNull(request.getParameter("type"));
		String pid = StringUtils.isNull(request.getParameter("pid"));
		Attestation attestation = userinfoService.getAttestationById(pid);

		request.setAttribute("type_name", attestation.getType_name());
		request.setAttribute("user_id", attestation.getUser_id());
		request.setAttribute("type", type);
		request.setAttribute("pid", pid);
		return SUCCESS;
	}
	// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 start
//	/**
//	 * 一个用户只能使用唯一的手机号功能实现
//	 * @param newUser 用户实体
//	 * @param phone 用户手机号码
//	 * @return boolean
//	 */
//	private Boolean checkOnlyPhone(User newUser) {
//		// 一个用户只能使用唯一的手机号功能实现
//		List list = userService.getOnlyPhone(newUser);
//		if (list.size() > 0) {
//			for (int i = 0; i < list.size(); i++) {
//				User onlyPhoneUser = (User) list.get(i);
//				if (newUser.getUser_id() != onlyPhoneUser.getUser_id()) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
	// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 end
	/**
	 * 后台信息认证的审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String AuditUserInfo() {
		
		long user_id = NumberUtils.getLong(request.getParameter("user_id"));
		int status = NumberUtils.getInt(request.getParameter("status"));
		int value = NumberUtils.getInt(request.getParameter("jifen"));
		String auditContent = request.getParameter("content");
		String type = StringUtils.isNull(request.getParameter("type"));
		String actionType = request.getParameter("actionType");
		String msg = "审核成功！";
		// UserCredit userCredit = userinfoService.getUserCreditByUserId(user_id);
		// userCredit.setValue(userCredit.getValue() + value);
		User existsUser = userService.getUserById(user_id);

		User auth_user = (User) session.get(Constant.AUTH_USER);

		// 操作日志
		DetailUser detailUser = userService.getDetailUser(auth_user.getUser_id());
		DetailUser newdetailUser = userService.getDetailUser(user_id);
		OperationLog operationLog = new OperationLog(user_id, auth_user.getUser_id(), "", this.getTimeStr(),
				this.getRequestIp(), "");

		if (!StringUtils.isBlank(actionType)) {
			String validcode = request.getParameter("validcode");
			if (validcode.isEmpty()) {
				AuditType uType = AuditType.getAuditType(type);
				switch (uType) {
					case phoneAudit:
						message("你输入的校验码为空！", "/admin/attestation/verifyPhone.html");
						break;
					case realnameAudit:
						message("你输入的校验码为空！", "/admin/attestation/verifyRealname.html");
						break;
					default:
						message("你输入的校验码为空！", "/admin/attestation/verifyScene.html");
						break;
				}
				return ADMINMSG;
			}
			if (!this.checkValidImg(validcode)) {
				AuditType uType = AuditType.getAuditType(type);
				switch (uType) {
					case phoneAudit:
						message("你输入的验证码错误！", "/admin/attestation/verifyPhone.html");
						break;
					case realnameAudit:
						message("你输入的验证码错误！", "/admin/attestation/verifyRealname.html");
						break;
					default:
						message("你输入的验证码错误！", "/admin/attestation/verifyScene.html");
						break;

				}
				return ADMINMSG;
			}
			if (status == 1) {
				/*boolean flag=checkOnlyPhone(existsUser);
				if (!flag) {
					existsUser.setUser_id(user_id);
					existsUser.setPhone_status("-1");
					userService.modifyPhone_status(existsUser);
					message = getSiteMessage(auth_user.getUser_id(), "手机认证审核失败！", "该手机号码已经被其他用户使用，手机认证审核失败！</br>" + "审核信息："
							+ auditContent, user_id);
					message.setIs_Authenticate("1");
					messageService.addMessage(message);

					// 手机认证审核失败操作日志
					operationLog.setAddtime(this.getTimeStr());
					operationLog.setType(Constant.PHONE_FAIL);
					operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
							+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
							+ "的用户手机认证失败,因该手机号码已经被其他用户使用");
					accountService.addOperationLog(operationLog);
					message("该手机号码已经被其他用户使用,手机认证失败！", "/admin/attestation/verifyPhone.html");
					return ADMINMSG;
				}*/
				User user = new User();
				// userinfoService.updateUserCredit(userCredit);
				AuditType auditType = AuditType.getAuditType(type);
				// UserCreditLog userCreditLog = new
				// UserCreditLog(user_id,NumberUtils.getLong(Constant.OP_ADD),NumberUtils.getLong(this.getTimeStr()),this.getRequestIp());
				switch (auditType) {
					case phoneAudit:
						message("手机认证成功！", "/admin/attestation/verifyPhone.html");
						break;
					case realnameAudit:
						// v1.6.7.1 RDPROJECT-289 sj 2013-11-19 start
						String cardId = request.getParameter("card_id");
						if(!StringUtils.isBlank(cardId)){
							message("实名认证成功！", "/admin/attestation/verifyRealname.html?card_id="+cardId);
						}else{
							message("实名认证成功！", "/admin/attestation/verifyRealname.html");
						}
						// v1.6.7.1 RDPROJECT-289 sj 2013-11-19 end
						break;
					default:
						message("认证成功！", "/admin/attestation/verifyScene.html");
						break;
				}
				//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
				BaseAccountLog blog=null;
				//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
				switch (auditType) {
					case emailAudit:
						user.setUser_id(user_id);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
						user.setEmail_status(1);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
						userService.modifyEmail_status(user);
						// userCreditLog.setType_id(1);
						// userCreditLog.setValue(2);
						userCreditService.updateUserCredit(user_id, value, new Byte(Constant.OP_ADD),
								EnumIntegralTypeName.INTEGRAL_EMAIL.getValue());
						// userinfoService . addUserCreditLog(userCreditLog);
						/*
						 * User newEmailUser = userService.getUserById(user_id); session.put(Constant.SESSION_USER,
						 * newEmailUser);
						 */
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "邮件认证审核成功！", "邮件认证审核成功！", user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
						

						Global.setTransfer("type", "邮件认证审核");
						blog=new UserCertifySuccLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end

						break;

					case phoneAudit:
						// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 start
//						boolean flag=checkOnlyPhone(existsUser);
						boolean flag = userService.isPhoneExist(existsUser);
						// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 end
						if (!flag) {
							existsUser.setUser_id(user_id);
							// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
							existsUser.setPhone_status(-1);
							// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
							userService.modifyPhone_status(existsUser);
							//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
							//TODO RDPROJECT-314 DONE
							/*message = getSiteMessage(auth_user.getUser_id(), "手机认证审核失败！", "该手机号码已经被其他用户使用，手机认证审核失败！</br>" + "审核信息："
									+ auditContent, user_id);
							message.setIs_Authenticate("1");
							messageService.addMessage(message);*/
											

							Global.setTransfer("type", "手机认证审核");
							blog=new UserCertifyFailLog(user_id);
							blog.doEvent();
							//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end

							// 手机认证审核失败操作日志
							operationLog.setAddtime(this.getTimeStr());
							operationLog.setType(Constant.PHONE_FAIL);
							operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
									+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
									+ "的用户手机认证失败,因该手机号码已经被其他用户使用");
							accountService.addOperationLog(operationLog);
							message("该手机号码已经被其他用户使用,手机认证失败！", "/admin/attestation/verifyPhone.html");
							return ADMINMSG;
						}
						
						user.setUser_id(user_id);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
						user.setPhone_status(1);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
						userService.modifyPhone_status(user);
						/*
						 * User newPhoneUser = userService.getUserById(user_id); session.put(Constant.SESSION_USER,
						 * newPhoneUser);
						 */

						// userCreditLog.setType_id(3);
						// userCreditLog.setValue(2);
						// userinfoService.addUserCreditLog(userCreditLog);
						// 电话认证积分
						userCreditService.updateUserCredit(user_id, value, new Byte(Constant.OP_ADD),
								EnumIntegralTypeName.INTEGRAL_PHONE.getValue());

						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "手机认证审核成功！", "手机认证审核成功！", user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
						

						Global.setTransfer("type", "手机认证审核");
						blog=new UserCertifySuccLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						message(msg, "/admin/attestation/verifyPhone.html");

						// 手机认证审核成功操作日志
						operationLog.setAddtime(this.getTimeStr());
						operationLog.setType(Constant.PHONE_SUCCESS);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
								+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
								+ "的用户手机认证成功");
						accountService.addOperationLog(operationLog);
						break;

					case videoAudit:
						user.setUser_id(user_id);
						user.setVideo_status(1);
						userService.modifyVideo_status(user);
						/*
						 * User newVideoUser = userService.getUserById(user_id); session.put(Constant.SESSION_USER,
						 * newVideoUser);
						 */

						// userCreditLog.setType_id(4);
						// userCreditLog.setValue(10);
						// userinfoService.addUserCreditLog(userCreditLog);
						// 视频认证积分
						userCreditService.updateUserCredit(user_id, value, new Byte(Constant.OP_ADD),
								EnumIntegralTypeName.INTEGRAL_VIDEO.getValue());
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "视频认证审核成功！", "视频认证审核成功！", user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/

						Global.setTransfer("type", "视频认证审核");
						blog=new UserCertifySuccLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						message(msg, "/admin/attestation/verifyVideo.html");
						//v1.6.7.1 wcw 2013-11-25 start
						//视频认证审核成功操作日志
						operationLog.setAddtime(this.getTimeStr());        
						operationLog.setType(Constant.VIDEO_SUCCESS);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
										+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
										+ "的用户视频认证成功");
						accountService.addOperationLog(operationLog);
						//v1.6.7.1 wcw 2013-11-25 end
						break;

					case sceneAudit:
						user.setUser_id(user_id);
						user.setScene_status(1);
						userService.modifyScene_status(user);
						/*
						 * User newSceneUser = userService.getUserById(user_id); session.put(Constant.SESSION_USER,
						 * newSceneUser);
						 */

						// userCreditLog.setType_id(5);
						// userCreditLog.setValue(10);
						// 现场认证积分
						userCreditService.updateUserCredit(user_id, value, new Byte(Constant.OP_ADD),
								EnumIntegralTypeName.INTEGRAL_SCENE.getValue());

						// userinfoService.addUserCreditLog(userCreditLog);
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "现场认证审核成功！", "现场认证审核成功！", user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
						

						Global.setTransfer("type", "现场认证审核");
						blog=new UserCertifySuccLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						message(msg, "/admin/attestation/verifyScene.html");
						//v1.6.7.1 wcw 2013-11-25 start
						//现场认证审核成功操作日志
						operationLog.setAddtime(this.getTimeStr());
						operationLog.setType(Constant.SCENE_SUCCESS);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
										+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
										+ "的用户现场认证成功");
						accountService.addOperationLog(operationLog);
						//v1.6.7.1 wcw 2013-11-25 end
						break;

					case realnameAudit:
						User cardUser = userService.getUserByCardNO(existsUser.getCard_id());
						if (cardUser != null && cardUser.getUser_id() != existsUser.getUser_id()) {
							msg = "该证件号码已被他人使用,不能被实名认证！";
							message(msg, "");
							return ADMINMSG;
						}
						user.setUser_id(user_id);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
						user.setReal_status(1);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
						userService.modifyReal_status(user);

						// userCreditLog.setType_id(2);
						// userCreditLog.setValue(10);
						// 证件实名认证积分
						userCreditService.updateUserCredit(user_id, value, new Byte(Constant.OP_ADD),
								EnumIntegralTypeName.INTEGRAL_REAL_NAME.getValue());

						// userinfoService.addUserCreditLog(userCreditLog);

						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "实名认证审核成功！", "实名认证审核成功！", user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
						

						Global.setTransfer("type", "实名认证审核");
						blog=new UserCertifySuccLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						message(msg, "/admin/attestation/verifyRealname.html");

						//System.out.println(message.getName());

						// 实名认证审核成功操作日志
						operationLog.setAddtime(this.getTimeStr());
						operationLog.setType(Constant.REALNAME_SUCCESS);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
								+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
								+ "的用户实名认证成功");
						accountService.addOperationLog(operationLog);
						break;
					default:
						break;
				}

				return ADMINMSG;
			} else {

				User user = new User();
				message(msg, "/admin/attestation/verifyScene.html");
				AuditType auditType = AuditType.getAuditType(type);
				//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
				BaseAccountLog blog=null;
				//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
				switch (auditType) {
					case emailAudit:
						user.setUser_id(user_id);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
						user.setEmail_status(-1);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
						userService.modifyEmail_status(user);
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "邮箱认证审核失败！", "邮箱认证审核失败！</br>" + "审核信息："
								+ auditContent, user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
										

						Global.setTransfer("type", "邮箱认证审核");
						blog=new UserCertifyFailLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						break;

					case phoneAudit:
						user.setUser_id(user_id);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
						user.setPhone_status(-1);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
						userService.modifyPhone_status(user);
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "手机认证审核失败！", "手机认证审核失败！</br>" + "审核信息："
								+ auditContent, user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
										

						Global.setTransfer("type", "手机认证审核");
						blog=new UserCertifyFailLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end

						// 手机认证审核失败操作日志
						operationLog.setAddtime(this.getTimeStr());
						operationLog.setType(Constant.PHONE_FAIL);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
								+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
								+ "的用户手机认证失败");
						accountService.addOperationLog(operationLog);
						message("审核没有通过！", "/admin/attestation/verifyPhone.html");
						break;

					case videoAudit:
						user.setUser_id(user_id);
						user.setVideo_status(-1);
						userService.modifyVideo_status(user);
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "视频认证审核失败！", "视频认证审核失败！</br>" + "审核信息："
								+ auditContent, user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
										

						Global.setTransfer("type", "视频认证审核");
						blog=new UserCertifyFailLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						message("审核没有通过！", "/admin/attestation/verifyVideo.html");
						//v1.6.7.1 wcw 2013-11-25 start
						// 视频认证审核失败操作日志
						operationLog.setAddtime(this.getTimeStr());
						operationLog.setType(Constant.VIDEO_FAIL);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
										+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
										+ "的用户视频认证失败");
						accountService.addOperationLog(operationLog);
						//v1.6.7.1 wcw 2013-11-25 end
						break;

					case sceneAudit:
						user.setUser_id(user_id);
						user.setScene_status(-1);
						userService.modifyScene_status(user);
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "现场认证审核失败！", "现场认证审核失败！</br>" + "审核信息："
								+ auditContent, user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
										

						Global.setTransfer("type", "现场认证审核");
						blog=new UserCertifyFailLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						message("审核没有通过！", "/admin/attestation/verifyScene.html");
						//v1.6.7.1 wcw 2013-11-25 start
						//现场认证审核失败操作日志
						operationLog.setAddtime(this.getTimeStr());
						operationLog.setType(Constant.SCENE_FAIL);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
										+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
										+ "的用户现场认证失败");
						accountService.addOperationLog(operationLog);
						//v1.6.7.1 wcw 2013-11-25 end
						break;

					case realnameAudit:
						user.setUser_id(user_id);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
						user.setReal_status(-1);
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
						userService.modifyReal_status(user);
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
						//TODO RDPROJECT-314 DONE
						/*message = getSiteMessage(auth_user.getUser_id(), "实名认证审核失败！", "实名认证审核失败！</br>" + "审核信息："
								+ auditContent, user_id);
						message.setIs_Authenticate("1");
						messageService.addMessage(message);*/
										

						Global.setTransfer("type", "实名认证审核");
						blog=new UserCertifyFailLog(user_id);
						blog.doEvent();
						//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
						
						// 实名认证审核失败操作日志
						operationLog.setAddtime(this.getTimeStr());
						operationLog.setType(Constant.REALNAME_FAIL);
						operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
								+ "）用户名为" + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername()
								+ "的用户实名认证失败");
						accountService.addOperationLog(operationLog);
						message("审核没有通过！", "/admin/attestation/verifyRealname.html");
						break;
					default:
						break;
				}

				
				return ADMINMSG;
			}
		}

		request.setAttribute("user", userService.getUserById(user_id));
		return SUCCESS;
	}

	// v1.6.6.2 RDPROJECT-195 yl 2013-10-24 start
	/**
	 * 	用户认证撤消
	 * @return
	 */
	public String CancelUserAttestation(){
		long user_id = NumberUtils.getLong(request.getParameter("user_id"));
		String type = StringUtils.isNull(request.getParameter("type"));
		User user = userService.getUserById(user_id);
		User auth_user = (User) session.get(Constant.AUTH_USER);
		DetailUser detailUser = userService.getDetailUser(auth_user.getUser_id());
		OperationLog operationLog = new OperationLog(user_id, auth_user.getUser_id(), "", this.getTimeStr(),
				this.getRequestIp(), "");
		AuditType auditType = AuditType.getAuditType(type);
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
		BaseAccountLog blog=null;
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
		switch (auditType) {
		case cancelRealnameAudit:
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			user.setReal_status(2);
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
			userService.modifyReal_status(user);
			
			
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
			//TODO RDPROJECT-314 DONE
			/*message = getSiteMessage(auth_user.getUser_id(), "实名认证审核撤消！", "实名认证审核撤消，待客服重新审核，</br>为您带来不变，敬请谅解！</br>", user_id);
			message.setIs_Authenticate("1");
			messageService.addMessage(message);*/
							

			Global.setTransfer("type", "实名认证审核");
			blog=new UserCertifyCancelLog(user_id);
			blog.doEvent();
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end

			// 实名认证审核撤消操作日志
			operationLog.setAddtime(this.getTimeStr());
			operationLog.setType(Constant.REALNAME_CANCEL);
			operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
					+ "）用户名为" + detailUser.getUsername() + "的操作员撤消用户名为" + user.getUsername()
					+ "的用户实名认证");
			accountService.addOperationLog(operationLog);
			message("实名认证撤消成功！", "/admin/attestation/verifyRealname.html");
			break;
		case cancelPhoneAudit:
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			user.setPhone_status(2);
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
			userService.modifyPhone_status(user);
			
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
			//TODO RDPROJECT-314 DONE
			/*message = getSiteMessage(auth_user.getUser_id(), "手机认证审核撤消！", "手机认证审核撤消，待客服重新审核，</br>为您带来不变，敬请谅解！</br>", user_id);
			message.setIs_Authenticate("1");
			messageService.addMessage(message);*/
							

			Global.setTransfer("type", "手机认证审核");
			blog=new UserCertifyCancelLog(user_id);
			blog.doEvent();
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
			// 手机认证撤销审核操作日志
			operationLog.setAddtime(this.getTimeStr());
			operationLog.setType(Constant.PHONE_CANCEL);
			operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
					+ "）用户名为" + detailUser.getUsername() + "的操作员撤消用户名为" + user.getUsername()
					+ "的用户手机认证");
			accountService.addOperationLog(operationLog);
			message("手机认证撤消成功！", "/admin/attestation/verifyPhone.html");
			break;
		case cancelVideoAudit:
			user.setVideo_status(2);
			userService.modifyVideo_status(user);
			
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
			//TODO RDPROJECT-314 DONE
			/*message = getSiteMessage(auth_user.getUser_id(), "视频认证审核撤消！", "视频认证审核撤消，待客服重新审核，</br>为您带来不变，敬请谅解！</br>", user_id);
			message.setIs_Authenticate("1");
			messageService.addMessage(message);*/
							

			Global.setTransfer("type", "视频认证审核");
			blog=new UserCertifyCancelLog(user_id);
			blog.doEvent();
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
			// 视频认证撤销审核操作日志
			operationLog.setAddtime(this.getTimeStr());
			operationLog.setType(Constant.VIDEO_CANCEL);
			operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
					+ "）用户名为" + detailUser.getUsername() + "的操作员撤消用户名为" + user.getUsername()
					+ "的用户视频认证");
			accountService.addOperationLog(operationLog);
			message("视频认证撤消成功！", "/admin/attestation/verifyVideo.html");
			break;
		case cancelSceneAudit:
			user.setScene_status(2);
			userService.modifyScene_status(user);
			
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
			//TODO RDPROJECT-314 DONE
			/*message = getSiteMessage(auth_user.getUser_id(), "现场认证审核撤消！", "现场认证审核撤消，待客服重新审核，</br>为您带来不变，敬请谅解！</br>", user_id);
			message.setIs_Authenticate("1");
			messageService.addMessage(message);*/
							

			Global.setTransfer("type", "现场认证审核");
			blog=new UserCertifyCancelLog(user_id);
			blog.doEvent();
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end
			// 现场认证撤销审核操作日志
			operationLog.setAddtime(this.getTimeStr());
			operationLog.setType(Constant.VIDEO_CANCEL);
			operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip()
					+ "）用户名为" + detailUser.getUsername() + "的操作员撤消用户名为" + user.getUsername()
					+ "的用户现场认证");
			accountService.addOperationLog(operationLog);
			message("现场认证撤消成功！", "/admin/attestation/verifyScene.html");
			break;
		default:
			break;
		}
		return ADMINMSG;
	}
	// v1.6.6.2 RDPROJECT-195 yl 2013-10-24 end
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 start
	//TODO RDPROJECT-314 DELETE
	/*private Message getSiteMessage(long receive_user, String title, String content, long sent_user) {
		message.setSent_user(receive_user);
		message.setReceive_user(sent_user);
		message.setStatus(0);
		message.setType(Constant.SYSTEM);
		message.setName(title);
		message.setContent(content);
		message.setAddip(getRequestIp());
		message.setAddtime(getTimeStr());
		return message;
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-20 end

	@Override
	public AttestationType getModel() {
		return attestationType;
	}
}
