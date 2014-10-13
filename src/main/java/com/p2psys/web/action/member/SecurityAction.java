package com.p2psys.web.action.member;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.PasswordToken;
import com.p2psys.domain.User;
import com.p2psys.model.RuleModel;
import com.p2psys.model.UserinfoModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.noac.PasswordUpdateLog;
import com.p2psys.model.accountlog.noac.PaypwdUpdateLog;
import com.p2psys.service.AccountService;
import com.p2psys.service.ArticleService;
import com.p2psys.service.FriendService;
import com.p2psys.service.PasswordTokenService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.tool.coder.MD5;
import com.p2psys.tool.ucenter.UCenterHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class SecurityAction extends BaseAction implements ModelDriven<UserinfoModel> {
	
	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(SecurityAction.class);

	private UserService userService;
	private UserinfoService userinfoService;
	private AccountService accountService;
	private ArticleService articleService;
	private FriendService friendService; 
	
	/**
	 * 密保service
	 */
	private PasswordTokenService passwordTokenService;
	
	/**
	 * userinfo
	 */
	private UserinfoModel userinfo = new UserinfoModel();

	@Override
	public UserinfoModel getModel() {
		return userinfo;
	}
	
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-02 start
	/**
	 * 修改密码需要回答的密保问题
	 * @return
	 * @throws Exception
	 */
	public String passwordTokenPwd() throws Exception {
		return passwordTokenAnswer("userpwd");
	}
	
	/**
	 * 修改支付密码需要回答的密保问题
	 * @return
	 * @throws Exception
	 */
	public String passwordTokenPayPwd() throws Exception {
		return passwordTokenAnswer("paypwd");

	}

	/**
	 * 回答密保公共方法
	 * @param type
	 * @return String
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	private String passwordTokenAnswer(String type)
			throws ServletException, IOException, Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		String actionType = StringUtils.isNull(request.getParameter("pwdType"));
		request.setAttribute("query_type", type);
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.SAFETY.getValue()));
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			if (rule.getValueIntByKey("password_token") == 1) {
				List<PasswordToken> tokenList = passwordTokenService.getPasswordTokenByUserId(user.getUser_id());
				request.setAttribute("tokenList", tokenList);
				if (tokenList.isEmpty() || tokenList.size() == 0) { // 如果没有密保问题，直接跳到修改密码（或支付密码）的页面
					tokenDispatcher(type);
				} else {
					havePasswordToken(type, actionType, tokenList);
				}
			} else {
				tokenDispatcher(type);
			}
		}
		return SUCCESS;
	}

	private String havePasswordToken(String type, String actionType,
			List<PasswordToken> tokenList) throws ServletException, IOException {
		String errormsg = null;
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-02 start
		if (!StringUtils.isBlank(actionType)) {
			int size = tokenList.size();
			PasswordToken passwordToken = null;
			for (int i = 0; i < size; i++) {
				String answer = request.getParameter("answer" + (i+1) + "");
				passwordToken = tokenList.get(i);
				if (!StringUtils.isBlank(answer) && answer.equals(passwordToken.getAnswer())) {
					tokenDispatcher(type);
				}
			}
			errormsg = "密保问题不对，请重试！";
			request.setAttribute("errormsg", errormsg);
		    return MSG;
		}
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-02 end
		return SUCCESS;
	}

	/**
	 * 页面跳转共通
	 * @param type
	 * @throws ServletException
	 * @throws IOException
	 */
	private void tokenDispatcher(String type) throws ServletException,
			IOException {
		if ("userpwd".equals(type)) {
			// 登录密码
//			request.getRequestDispatcher("/memberSecurity/userpwd.html").forward(request, response);
			response.sendRedirect("/memberSecurity/userpwd.html");
		} else {
			// 支付密码
//			request.getRequestDispatcher("/memberSecurity/paypwd.html").forward(request, response);
			response.sendRedirect("/memberSecurity/paypwd.html");
		}
	}
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-02 end

	public String userpwd() throws Exception {
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
		int phoneSms = 0;
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.SAFETY.getValue()));
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			phoneSms = rule.getValueIntByKey("phone_sms");
		}
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
		User user = (User) session.get(Constant.SESSION_USER);
		User u = userService.getUserById(user.getUser_id());
		String errormsg = "";
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		String chgpwdType = request.getParameter("actionType");
		request.setAttribute("query_type", "userpwd");
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
		request.setAttribute("phoneSms", phoneSms);
		request.setAttribute("user", u);
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
		if (!StringUtils.isNull(chgpwdType).equals("")) {
			if (newpassword != null) {
				errormsg = checkUserpwd(oldpassword, newpassword);
				if (!errormsg.equals("")) {
					request.setAttribute("errormsg", errormsg);
					return SUCCESS;
				}
				user.setPassword(new MD5().getMD5ofStr(newpassword));
				// 同步修改密码
				try {
					UCenterHelper.ucenter_edit(user.getUsername(), oldpassword,
							newpassword, user.getEmail());
				} catch (Exception e) {
					e.printStackTrace();
				}
				// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
				if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
					if (phoneSms == 1 && user.getPhone_status() == 1) {
						return phoneSms("userpwd");
					} else {
						String msg = "密码修改成功！";
						request.setAttribute("msg", msg);
						// v1.6.7.2  安全优化  sj 2013-12-05 start
						session.put("message", "");
						// v1.6.7.2  安全优化  sj 2013-12-05 end
						userService.modifyUserPwd(user);
						if (user != null) {
							long userid = user.getUser_id();
							Account act = accountService.getAccount(userid);
							BaseAccountLog blog = new PasswordUpdateLog(0, act,
									userid);
							blog.doEvent();
						}
					}
				}
				// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
			} else {
				errormsg = "修改密码失败！";
				request.setAttribute("errormsg", errormsg);
				return SUCCESS;
			}
		}
		return SUCCESS;
	}
	
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
	/**
	 * 短信手机验证
	 * @return
	 * @throws Exception
	 */
	public String phoneSms(String type) throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		String valicode = paramString("phone_valicode");
		String code_number = (String) session.get("code_number");
		String errormsg = "";
		Object object = session.get("nowdate");
		if (object == null) {
			object = "";
		}
		if (!StringUtils.isBlank(code_number) && !StringUtils.isBlank(valicode)) {
			if (!code_number.equals(valicode)) {
				errormsg = "短信验证码不正确！请查看是否过期或者输入错误";
				request.setAttribute("errormsg", errormsg);
				return SUCCESS;
			} else {
				// v1.6.7.2 RDPROJECT-633 lx 2013-12-27 start
				session.remove("code_number");
				session.remove("nowdate");
				// v1.6.7.2 RDPROJECT-633 lx 2013-12-27 end
				return modifyPwd(type, user);
			}
		} else {
			errormsg = "请输入您收到的验证码！";
			request.setAttribute("errormsg", errormsg);
			return SUCCESS;
		}
	}
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end

	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
	/**
	 * 修改密码（或交易密码）
	 * @param type
	 * @param user
	 * @return
	 */
	private String modifyPwd(String type, User user) {
		String errormsg;
		if ("paypwd".equals(type)) {
			userService.modifyPayPwd(user);
			if (user != null) {
				long userid = user.getUser_id();
				Account act = accountService.getAccount(userid);
				BaseAccountLog blog = new PaypwdUpdateLog(0, act,userid);
				blog.doEvent();
			}
		} else {
			userService.modifyUserPwd(user);
			if (user != null) {
				long userid = user.getUser_id();
				Account act = accountService.getAccount(userid);
				BaseAccountLog blog = new PasswordUpdateLog(0, act,userid);
				blog.doEvent();
			}
		}
		errormsg = "密码修改成功！";
		request.setAttribute("msg", errormsg);
		return SUCCESS;
	}
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
	
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
	public String mobileaccess() {
		User user = (User) session.get(Constant.SESSION_USER);
		String phone = paramString("mobile");
		long date = DateUtils.getTime(new Date());
		logger.info("当前系统时间" + date + "上一次获取时间" + session.get("nowdate"));
		String returnmessage = userService.phoneCode(phone, user, session, date);
		return jsonString(returnmessage);
	}
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
	
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
	private String jsonString(String returnmessage) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", returnmessage);
		try {
			printJson(JSON.toJSONString(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end

	private String checkUserpwd(String oldpassword, String newpassword) {
		User user = (User) session.get(Constant.SESSION_USER);
		if (userService.login(user.getUsername(), oldpassword) == null) {
			return "密码不正确，请输入您的旧密码 ";
		//v1.6.7.2  安全优化  sj 2013-11-28 start	
		} else if (newpassword.length() < 8 || newpassword.length() > 16) {
			return "新密码长度在8到16之间";
		//v1.6.7.2  安全优化  sj 2013-11-28 end	
		}
		return "";
	}

	public String paypwd() throws Exception {
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
		int phoneSms = 0;
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.SAFETY.getValue()));
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			phoneSms = rule.getValueIntByKey("phone_sms");
		}
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
		User user = (User) session.get(Constant.SESSION_USER);
		User u = userService.getUserById(user.getUser_id());
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		String valicode = request.getParameter("valicode");
		String pwdType = request.getParameter("pwdType");
		request.setAttribute("query_type", "paypwd");
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
		request.setAttribute("phoneSms", phoneSms);
		request.setAttribute("user", u);
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
		String errormsg = "";
		if (!StringUtils.isBlank(pwdType)) {
			if (newpassword != null) {
				errormsg = checkPaypwd(oldpassword, newpassword, valicode);
				if (!errormsg.equals("")) {
					request.setAttribute("errormsg", errormsg);
					return SUCCESS;
				}
				user.setPaypassword(new MD5().getMD5ofStr(newpassword));
				// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
				if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
					if (phoneSms == 1 && user.getPhone_status() == 1) {
						return phoneSms("paypwd");
					} else {
						String msg = "修改交易密码成功！";
						request.setAttribute("msg", msg);
						userService.modifyPayPwd(user);
						if (user != null) {
							long userid = user.getUser_id();
							Account act = accountService.getAccount(userid);
							BaseAccountLog blog = new PaypwdUpdateLog(0, act, userid);
							blog.doEvent();
						}
					}
				}
				// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
			} else {
				errormsg = "修改密码失败！";
				request.setAttribute("errormsg", errormsg);
				return SUCCESS;
			}
		}
		return SUCCESS;
	}

	private String checkPaypwd(String oldpassword, String newpassword,String valicode) {
		User user = (User) session.get(Constant.SESSION_USER);
		MD5 md5 = new MD5();
		String oldpwdmd5=md5.getMD5ofStr(oldpassword);
		String userpaypwd=StringUtils.isNull(user.getPaypassword());
		if(userpaypwd.isEmpty()) userpaypwd=user.getPassword();
		String userpwd=StringUtils.isNull(user.getPassword());
		//v1.6.7.2  安全优化  sj 2013-11-28 start
		if (newpassword.length() < 8 || newpassword.length() > 16) {
			return "新密码长度在8到16之间";
		//v1.6.7.2  安全优化  sj 2013-11-28 end	
		}else if(StringUtils.isNull(oldpassword).equals("")){
			return "原始交易密码不能为空！ ";
		}else if(!oldpwdmd5.equals(userpaypwd)){
			return "原始交易密码不正确，请输入您的原始交易密码 ";
		}else if(userpaypwd.equals("")&&!oldpwdmd5.equals(userpwd)){
			return "还未设定交易密码，原始密码请输入您的登录密码！ ";
		//v1.6.7.2  安全优化  sj 2013-11-28 start	
		}else if(md5.getMD5ofStr(newpassword).equals(userpwd)){
			return "新交易密码不能和登录密码一样！ ";
		}
		else if (newpassword.length() < 8 || newpassword.length() > 16) {
			return "新密码长度在8到16之间";
		//v1.6.7.2  安全优化  sj 2013-11-28 end
		}else if(!checkValidImg(valicode)){
			return "验证码不正确！";
		}
		return "";
	}

	public String protection() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);

		user = userService.getUserById(user.getUser_id());

		String question = StringUtils.isNull(request.getParameter("question"));
		String answer = StringUtils.isNull(request.getParameter("answer"));
		String type = StringUtils.isNull(request.getParameter("type"));
		String mType =StringUtils.isNull(request.getParameter("mType"));
		if (answer != null) {
			if ("1".equals(type)) {
				if(user.getAnswer().equals(answer)){
					request.setAttribute("mType", "2");
					request.setAttribute("msg", "请重新设置密码保护！");
				}else{
					request.setAttribute("errormsg", "请输入正确的旧密码保护！");
				}
			}else if ("2".equals(mType) ) {
				String errormsg = checkAnswer(answer);
				String msg = "密码保护设置成功！";
				user.setQuestion(question);
				user.setAnswer(answer);
				userService.modifyAnswer(user);
				if (errormsg.equals("")) {
					request.setAttribute("msg", msg);
				} else {
					request.setAttribute("errormsg", errormsg);
				}
			}
		}else{
			if(StringUtils.isBlank(user.getAnswer())){
				request.setAttribute("mType", "2");
			}
		}
		request.setAttribute("query_type", "protection");
		request.setAttribute("user", user);
		return "success";
	}

	private String checkAnswer(String answer) {
		if (answer.length() > 15) {
			return "答案长度小于50";
		}
		if(answer.isEmpty()){
			return "答案不能为空!";
		}
		return "";
	}

	public String serialStatusSet() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);

		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		if (newpassword != null) {
			String msg = "密码修改成功！";
			String errormsg = checkUserpwd(oldpassword, newpassword);
			user.setPassword(newpassword);
			userService.modifyUserPwd(user);
			if (errormsg.equals("")) {
				request.setAttribute("msg", msg);
			} else {
				request.setAttribute("errormsg", errormsg);
			}
		}
		request.setAttribute("query_type", "serialStatusSet");

		return "success";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public UserinfoService getUserinfoService() {
		return userinfoService;
	}

	public void setUserinfoService(UserinfoService userinfoService) {
		this.userinfoService = userinfoService;
	}

	public ArticleService getArticleService() {
		return articleService;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public FriendService getFriendService() {
		return friendService;
	}

	public void setFriendService(FriendService friendService) {
		this.friendService = friendService;
	}

	public PasswordTokenService getPasswordTokenService() {
		return passwordTokenService;
	}

	public void setPasswordTokenService(PasswordTokenService passwordTokenService) {
		this.passwordTokenService = passwordTokenService;
	}

}
