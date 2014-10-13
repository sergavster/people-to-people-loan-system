package com.p2psys.web.action;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.CooperationLogin;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.EmailLog;
import com.p2psys.domain.PasswordToken;
import com.p2psys.domain.Rule;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.domain.UserCredit;
import com.p2psys.domain.UserTrack;
import com.p2psys.domain.Userinfo;
import com.p2psys.model.DetailUser;
import com.p2psys.model.RuleModel;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.noac.GetPayPwdLog;
import com.p2psys.model.accountlog.noac.GetPwdLog;
import com.p2psys.model.accountlog.noac.RegisterUserEmailLog;
import com.p2psys.service.AccountService;
import com.p2psys.service.AccountSumService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.CooperationLoginService;
import com.p2psys.service.EmailLogService;
import com.p2psys.service.MemberBorrowService;
import com.p2psys.service.MessageService;
import com.p2psys.service.PasswordTokenService;
import com.p2psys.service.RewardStatisticsService;
import com.p2psys.service.RuleService;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.tool.coder.BASE64Decoder;
import com.p2psys.tool.coder.MD5;
import com.p2psys.tool.iphelper.IPUtils;
import com.p2psys.tool.javamail.Mail;
import com.p2psys.tool.ucenter.UCenterHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.RandomUtil;
import com.p2psys.util.StringUtils;

public class UserAction extends BaseAction implements ModelDriven<User> {
	
	private static Logger logger = Logger.getLogger(UserAction.class);

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -7819826246025932070L;

	/**
	 * 通过Spring容器注入service
	 */
	private UserService userService;
	private UserinfoService userinfoService;
	private AccountService accountService;
	private BorrowService borrowService;
	private MemberBorrowService memberBorrowService;
	private PasswordTokenService passwordTokenService;
	private CooperationLoginService cooperationLoginService;
	private RewardStatisticsService rewardStatisticsService;
	private UserCreditService userCreditService;
	private EmailLogService emailLogService;
    private RuleService ruleService;
    private AccountSumService accountSumService;
    private MessageService messageService;
    
	private StringBuffer sb=new StringBuffer();

	/**
	 * 通过Struts2注入,并通过ModelDriven封装User变量
	 */
	private User user = new User();
	
	private UserCache userCache = new UserCache();
	
	private String validatecode;
	
	private StringBuffer msg = new StringBuffer();

	private String backUrl = "";
	
	

	/**
	 * 验证用户登录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 start
		//是否启用验证码规则
		Rule rule=Global.getRule(EnumRuleNid.LOGIN_VALIDE_ENABLE.getValue());
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 end
		User newUser = (User)session.get("newUser");
		// 联合登陆用户
		User openUser = (User) request.getAttribute("openUser");
		if(openUser != null && openUser.getUsername() != null){ 
			user = openUser;
		}
		//联合登陆与现有账号绑定的信息ID
		String openLoginId = paramString("openLoginId");
		
		String redirectURL=paramString("redirectURL");
		request.setAttribute("redirectURL", redirectURL);
        int loginFailMaxTimes=Global.getInt("login_fail_maxtimes");
        loginFailMaxTimes=(loginFailMaxTimes<1)?3:loginFailMaxTimes;
		String returnUrl = "/member/index.html";
		String source = paramString("source");
		//V1.6.5.3 RDPROJECT-146 liukun 2013-09-11 start
		String timeout = paramString("timeout");
		if(!timeout.equals("")){
			request.setAttribute("timeout", timeout);
		}
		//v1.6.6.1 RDPROJECT-167 liukun 2013-09-10 start
		String newlogin = paramString("newlogin");
		if(!newlogin.equals("")){
			request.setAttribute("newlogin", newlogin);
		}
		//V1.6.5.3 RDPROJECT-146 liukun 2013-09-11 end
		//v1.6.6.1 RDPROJECT-167 liukun 2013-09-10 end 
		if (!hasCookieValue() && openUser == null) {// 没有保存cookie进行登录校验，有直接读取cookie进行登录
			if (isSession()) {
				return "member";
			}
			String actionType = this.getActionType();
			if ("".equals(actionType)) {
				// 跳转登陆页面前，判断第三方账户是否是需要与已有的账户进行绑定
				if(openLoginId != null && openLoginId.length() > 0 &&  Long.parseLong(openLoginId) > 0 ){
					request.setAttribute("openLoginId", openLoginId);
				}
				//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 start
				request.setAttribute("rule", rule);
				//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 end
				// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 start
				initRSAME();
				// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 end
				return "login";
			}
			backUrl = "/user/login.html?redirectURL="+URLEncoder.encode(redirectURL,"UTF-8");
			String flag = loginValidate();
			// 执行登陆逻辑前校验是否存在非法数据，如果有直接返回错误页面
			if (flag.equals("fail")) {
				logger.info("存在非法数据");
				return flag;
			}
		}

		// 用户注册业务逻辑处理
		User u = null;
		if(openUser == null || openUser.getUsername() == null){
			try {
				// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 start
				user.setPassword(userService.getRSADecrypt(user.getPassword(), paramInt("encrypt")));
				// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 end
				
				u = userService.login(user.getUsername(), user.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			u = openUser;
		}
		if (u == null) {
			String errorMsg = "用户不存在或密码错误！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			//检查登录失败次数限制是否启用
			if(Global.getInt("login_fail_limit")==1){
				User failUser = userService.getUserByName(user.getUsername());
				if(failUser!=null){
					if(failUser.getIslock() == 1){
						errorMsg = "该账户" + failUser.getUsername() + "已经被锁定！";
						logger.info(errorMsg);
						msg.append(errorMsg);
						message(msg.toString(), backUrl);
						return "fail";
					} 
					//获取登录失败次数
				    int loginFailTimes = userService.getLoginFailTimes(failUser.getUser_id());
				  //登录失败插入一条登录失败数据
			        userService.updateLoginFailTimes(failUser.getUser_id());
			        if (loginFailTimes >= loginFailMaxTimes) {
			          errorMsg = "该账户" + failUser.getUsername() + "已经被锁定！";
			          logger.info(errorMsg);
			          msg.append(errorMsg);
			          message(msg.toString(), backUrl);
			          //登录失败次数大于等于3时，锁定账号
			          userService.updateUserIsLock(failUser.getUser_id());
			          //清除数据库登录失败次数
			          userService.cleanLoginFailTimes(failUser.getUser_id());
			          return "fail";
			        }
				}
			}
			
 			if (Global.getWebid().equals("mszb")) {
				return "login";
			} else {
				return "fail";
			}
		} else {
			int loginFailTimes = userService.getLoginFailTimes(u.getUser_id());
			if (u.getIslock() == 1) {
				String errorMsg = "该账户" + u.getUsername() + "已经被锁定！";
				logger.info(errorMsg);
				msg.append(errorMsg);
				message(msg.toString(), backUrl);
				if (Global.getWebid().equals("mszb")) {
					return "login";
				} else {
					return "fail";
				}
			} else if (u.getStatus() == 0) {
				String errorMsg = "该账户" + u.getUsername() + "已经被关闭！";
				logger.info(errorMsg);
				msg.append(errorMsg);
				message(msg.toString(), backUrl);
				// 民生创投登陆的时候如果密码错误不跳转页面
				if (Global.getWebid().equals("mszb")) {
					return "login";
				} else {
					return "fail";
				}
			} /*else if (Global.SESSION_MAP.containsKey(u.getUsername())) {
			      String errorMsg = "该账户" + u.getUsername() + "已经登录！不允许重复登录";
			      logger.info(errorMsg);
			      msg.append(errorMsg);
			      message(msg.toString(), backUrl);
			      logger.info("用户ID：" + u.getUser_id());
			      return "fail";
			}*/ else if (loginFailTimes >= loginFailMaxTimes) {
			      String errorMsg = "该账户" + u.getUsername() + "已经被锁定！";
			      logger.info(errorMsg);
			      msg.append(errorMsg);
			      message(msg.toString(), backUrl);
			      logger.info("用户ID：" + u.getUser_id());
			      userService.updateUserIsLock(u.getUser_id());
			      userService.cleanLoginFailTimes(u.getUser_id());
			      return "fail";
			 } else if (u.getType_id() > 0 && u.getType_id() == 999){//如果用户类型大于零，并且
				String errorMsg = "该账户" + u.getUsername() + "为临时用户，不能登录！";
				logger.info(errorMsg);
				msg.append(errorMsg);
				message(msg.toString(), backUrl);
				return "fail";
			 }else {// 用户正常登陆
				 
				 //v1.6.7.2  安全优化  sj 2013-12-3 start
				 //安全优化规则
				 RuleModel safetyRule = new RuleModel(Global.getRule(EnumRuleNid.SAFETY.getValue()));
				 String message = userService.fontFreezeUser(safetyRule,u);
				 if(!StringUtils.isBlank(message)){
					 message(message.toString(), backUrl);
					 return "fail";
				 }
				 
				boolean b1 = Pattern.compile("[0-9]").matcher(user.getPassword()).find();
				boolean b2 = Pattern.compile("(?i)[a-zA-Z]").matcher(user.getPassword()).find();
				boolean b3 = Pattern.compile("[~!@#$%^&*()]").matcher(user.getPassword()).find();
				if((b1 && b2) || (b1 && b3) || (b2 && b3) || (b1 && b2 && b3)){
					session.put("message", "");
				}else{
					session.put("message", "密码过于简单，请用户修改密码！");
				}
				//v1.6.7.2  安全优化  sj 2013-12-3 end
				
				u.hideChar();
				//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start begin
				session.put(Constant.SESSION_USER, null);
				session.put(Constant.SESSION_USER, u);
				session.put("logintime", System.currentTimeMillis());
				/*
				//登录成功后记录cookie用于单点登录
				String username = "";
				try { 
					username = URLEncoder.encode(u.getUsername(), "UTF-8");
				}
				catch(UnsupportedEncodingException e){
					
				}
				
				Cookie usernameCookie = new Cookie("username", username);
				Cookie logintimeCookie = new Cookie("logintime", String.valueOf(System.currentTimeMillis()));
				 
				usernameCookie.setMaxAge(3600);
				logintimeCookie.setMaxAge(3600);
				 
				usernameCookie.setPath("/");
				logintimeCookie.setPath("/");

				response.addCookie(usernameCookie);
				response.addCookie(logintimeCookie);
				*/
				//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start end

				long user_id = u.getUser_id();
				//登录成功一次清除登录失败次数
				userService.cleanLoginFailTimes(user_id);
				int unreadmsg = messageService.getUnreadMesage(user_id);
				request.setAttribute("unreadmsg", unreadmsg);
				// 获取本次信息
				Date nowDate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
				String nowMsg = "本次：" + getAreaByIp() + " "
						+ sdf.format(nowDate);
				UserTrack track = userService.getLastUserTrack(u.getUser_id());
				String lastMsg = "";
				if (track == null) {
					lastMsg = "第一次登陆！";
				} else {
					// 获取上次登录信息
					lastMsg = "上次：" + getAreaByIp(track.getLogin_ip()) + " "
							+ sdf.format(DateUtils.getDate(track
									.getLogin_time()));
					
					// v1.6.7.2 RDPROJECT-499 sj 2013-12-4 start
					u.setLasttime(String.valueOf(u.getLogintime()));
					u.setLogintime(NumberUtils.getLong(DateUtils.getNowTimeStr()));
					u.setLastip(this.getRequestIp());
					userService.updateUserLastInfo(u);
					// v1.6.7.2 RDPROJECT-499 sj 2013-12-4 end
					
				}
				String ipmsg = lastMsg + "<br/>" + nowMsg;

				// 插入用户登陆信息
				UserTrack newTrack = new UserTrack();
				newTrack.setUser_id(u.getUser_id() + "");
				newTrack.setLogin_ip(this.getRequestIp());
				newTrack.setLogin_time(nowDate.getTime() / 1000 + "");
				userService.addUserTrack(newTrack);

				String errorMsg = "登录成功<br/><font style=color:red>请确认您的上一次登录时间</font>"
						+ "<br/><font style=color:red>" + ipmsg + "</font>";
				logger.info(errorMsg);
				msg.append(errorMsg);
				//v1.6.6.1 RDPROJECT-65 xx 2013-09-26 start
				message(msg.toString(), returnUrl, "<span id='second'></span>进入用户中心");
				//v1.6.6.1 RDPROJECT-65 xx 2013-09-26 end
				// 是否显示登录时间（后台可配置）：1(显示) 0（不显示）
				if ("1".equals(Global.getValue(Constant.LOGIN_TIME))) {
					return MSG;
				}
				if(!redirectURL.isEmpty()){
					returnUrl=redirectURL;
					response.sendRedirect(redirectURL);
					return null;
				}
				if(openLoginId != null && openLoginId.length() > 0 &&  Long.parseLong(openLoginId) > 0 ){//判断参数不为空
					// 将用户需要绑定的账户ID写入联合登陆表，实现绑定
					cooperationLoginService.updateCooperationUserIdById(u.getUser_id(), Long.parseLong(openLoginId));
				}
				// 登陆成功进入用户中心
				if (source.equals("index")) {
					return "index";
				}
				return "member";
			}
		}
	}

	public String logout() throws Exception {
		Map session = (Map) ActionContext.getContext().getSession();
		// RDPROJECT-177 liukun 2013-09-17 begin
		/*
		// 配置是否启用单点登录
		if ("1".equals(Global.getValue("single_login"))) {
			// V1.6.5.3 RDPROJECT-146 liukun 2013-09-11 start
			// session.put("session_user", null);

			User user = (User) session.get(Constant.SESSION_USER);
			User authUser = (User) session.get(Constant.AUTH_USER);

			// 判断是否是最新登录，不是则视为因为当前浏览器会话过期，只能清除cookie，不能清除全局变量
        	//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start begin
	        //User authUser = (User) session.get(Constant.AUTH_USER);  
	        //if(user != null && authUser==null){
	        if(user != null){	
        	//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start end
				Cookie[] cookies = request.getCookies();// 这样便可以获取一个cookie数组
				String username = null;
				long logintime = 0;
				for (Cookie cookie : cookies) {
					if (cookie.getName().equalsIgnoreCase("username")) {
						try {
							username = URLDecoder.decode(cookie.getValue(),
									"UTF-8");
						} catch (UnsupportedEncodingException e) {

						}
					}
					if (cookie.getName().equalsIgnoreCase("logintime")) {
						logintime = Long.parseLong(cookie.getValue());
					}

				}

				long lastlogintime = 0;
				if (Global.SESSION_MAP.containsKey(username)) {
					lastlogintime = Long.parseLong(Global.SESSION_MAP.get(
							username).toString());
				}

				// 只有登录时间大于等于全局变量里的最新登录时间才表明是最新登录，才可以真正注销
				if (logintime >= lastlogintime) {
					session.put("session_user", null);
				}
				// 只要退出，都清除cookie,不需要判断是否是最新会话
				user = null;
				// 清除掉过期的cookie

				Cookie[] overdueCookies = request.getCookies();
				try {
					for (int i = 0; i < overdueCookies.length; i++) {
						// //System.out.println(cookies[i].getName() + ":" +
						// cookies[i].getValue());
						Cookie cookie = new Cookie(cookies[i].getName(), null);
						cookie.setMaxAge(0);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				} catch (Exception ex) {
					//System.out.println("清空Cookies发生异常！");
				}

			}

			// V1.6.5.3 RDPROJECT-146 liukun 2013-09-11 end
		} else {
			session.put("session_user", null);
		}
		*/
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 start
		Rule rule=Global.getRule(EnumRuleNid.LOGIN_VALIDE_ENABLE.getValue());
		if(rule!=null){
			request.setAttribute("rule", rule);
		}
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 end
		//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start begin
		session.put("session_user", null);
		session.put("logintime", null);
		//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start end
		// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 start
		initRSAME();
		// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 end
		return "success";
	}

	/**
	 * 用户注册
	 * 
	 * @return
	 * @throws Exception
	 * 
	 *             修改 日期:2012-09-13 后台添加用户
	 */
	public String register() throws Exception {
		//是否启用验证码规则
		Rule rule=Global.getRule(EnumRuleNid.VALIDE_ENABLE.getValue());
		// v1.6.6.2 RDPROJECT-367 lhm 2013-10-21 start
//		if (isSession()) {
//			return "member";
//		}
		// v1.6.6.2 RDPROJECT-367 lhm 2013-10-21 end
		
		//v1.6.7.2  安全优化  sj 2013-11-28 start
		request.setAttribute("weburl", Global.getValue("weburl"));
		RuleModel safetyRule = new RuleModel(Global.getRule(EnumRuleNid.SAFETY.getValue()));
		if(safetyRule != null && safetyRule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue() 
    			&& safetyRule.getValueIntByKey("adminStatus") == 1 && safetyRule.getValueLongByKey("adminLoginFailIsLock") == 1){
			String fontUnallowedUsername = safetyRule.getValueStrByKey("fontUnallowedUsername");
			request.setAttribute("fontUnallowedUsername", fontUnallowedUsername);
		}
		//v1.6.7.2  安全优化  sj 2013-11-28 end
		
		//取出通过好友邀请链接的inviteUser
		User inviteUser = (User)session.get("inviteUser");
		if(inviteUser != null){
			request.setAttribute("inviteUser", inviteUser);
		}
		String actionType = this.getActionType();
		boolean open = true;
		if ("".equals(actionType)) {
			request.setAttribute("rule", rule);
			// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 start
			initRSAME();
			// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 end
			return "register";
		} else {
			if (actionType.equals("adminadduser")) {
				open = false;
			}
			backUrl = open ? "/user/register.html" : "/admin/userinfo/adduser.html";
		}
		String flag = regValidate();
		// 执行登陆逻辑前校验是否存在非法数据，如果有直接返回错误页面
		if (flag.equals("fail")) {
			logger.debug("存在非法数据");
			return flag;
		}

		// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 start
		user.setPassword(userService.getRSADecrypt(user.getPassword(), paramInt("encrypt")));
		// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 end
		
		String pwd = user.getPassword();
		// 密码MD5加密
		MD5 md5 = new MD5();
		user.setPassword(md5.getMD5ofStr(user.getPassword()));
		user.setAddtime(this.getTimeStr());
		user.setAddip(this.getRequestIp());
		User newUser = userService.register(user);
		User u = userService.getUserByName(newUser.getUsername());
		HttpServletRequest request = ServletActionContext.getRequest();
		String realip = IPUtils.getRemortIP(request);
		// 用户积分表
		UserCredit uc = new UserCredit(u.getUser_id(), 0, new Date().getTime() / 1000, realip);
		userService.addUserCredit(uc);
		// 用户缓存
		UserCache cache = new UserCache();
		cache.setUser_id(u.getUser_id());
		userService.saveUserCache(cache);

		// 同步UCenter
		try {
			String retMsg=UCenterHelper.ucenter_register(user.getUsername(), pwd, user.getEmail());
			logger.info(user.getUsername());
			logger.info(retMsg);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 初始化账户
		try {
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
			//TODO RDPROJECT-314 DOING
			
			/*long user_id = user.getUser_id();
			//TODO active content
			String activeUrl = "<a href=''>active</a>";

			Global.setTransfer("activeUrl", activeUrl);
			
			BaseAccountLog blog=new ActiveUserEmailLog(user_id);
			blog.doEvent();*/
			//v1.6.7.2 RDPROJECT-533 lx 2013-12-24 start
			long user_id = newUser.getUser_id();
			Mail m = Mail.getInstance();
			String activeUrl = "/member/identify/active.html?id=" + m.getdecodeIdStr(newUser);
			Global.setTransfer("activeUrl", activeUrl);
			BaseAccountLog blog=new RegisterUserEmailLog(user_id);
			blog.doEvent();
			//sendActiveMail(newUser);
			//v1.6.7.2 RDPROJECT-533 lx 2013-12-24 end
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (open) {
			msg.append("注册成功！");
			if (!StringUtils.isBlank(newUser.getInvite_userid())) {
				rewardStatisticsService.addRewardStatistics(newUser.getUser_id(), getRequestIp());
				// v1.6.6.2 RDPROJECT-235 zza 2013-10-17 start
				RuleModel rulePromote = new RuleModel(Global.getRule(EnumRuleNid.RULE_PROMOTE.getValue()));
				if (rulePromote != null && rulePromote.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
					String startTime = rulePromote.getValueStrByKey("start_time");
					userService.updateRulePromoteId(newUser.getInvite_userid(), startTime);
				}
				// v1.6.6.2 RDPROJECT-235 zza 2013-10-17 end
			}
			AccountSum accountSum = new AccountSum();
			accountSum.setUser_id(u.getUser_id());
			accountSumService.addAccountSum(accountSum);
			// 提取联合登陆信息的ID，如果ID存在，则说明是通过联合登陆产生的账号，并绑定
			String openLoginId = paramString("openLoginId");
			if(openLoginId != null && openLoginId.length() > 0 &&  Long.parseLong(openLoginId) > 0 ){//判断参数不为空
				// 将新注册的user id写入联合登陆表，实现绑定
				cooperationLoginService.updateCooperationUserIdById(u.getUser_id(), Long.parseLong(openLoginId));
			}
			// v1.6.7.2 RDPROJECT-430 lx 2013-12-02 start
			request.setAttribute("rsmsg",msg.toString());
			String urltext="<a target='_blank' href="+userService.getEmailUrl(u.getEmail())+" >激活邮件已发送，请登陆邮箱激活></a>";
			request.setAttribute("backurl",urltext);
			// v1.6.7.2 RDPROJECT-430 lx 2013-12-02 end
			
			//是否启用注册成功自动登录
			RuleModel login_rule = new RuleModel(Global.getRule(EnumRuleNid.REGISTER_SUCCESS_LOGIN.getValue()));
			//v1.6.7.2 RDPROJECT-602 wcw 2013-12-17 start
			if(login_rule != null&&login_rule.getStatus()==EnumRuleStatus.RULE_STATUS_YES.getValue()){
			  //v1.6.7.2 RDPROJECT-602 wcw 2013-12-17 end
				int is_auto_login = login_rule.getValueIntByKey("is_auto_login");
				if(is_auto_login == 1){
					session.put(Constant.SESSION_USER, u);
					//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start begin
					session.put("logintime", System.currentTimeMillis());
					//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start end
					return "member";
				}
			}
		} else {
			msg.append("添加成功");
			message(msg.toString(), "/admin/userinfo/user.html", "点击返回！");
		}
		return "success";
	}

	private String checkValicode(String valicode){
		if(StringUtils.isBlank(valicode)){
			sb.append("验证码不能为空！");
			return "fail";
		}else if(!checkValidImg(valicode)){
			 message(sb.toString(), backUrl);
			 return "fail";
		}
		return "";
	}
	/**
	 * 联合登陆注册会员
	 * @return
	 */
	public String cooperationRegister(){
		
		// 提取联合登陆信息的ID，如果ID存在，则说明是通过联合登陆产生的账号，并绑定
		String openLoginId = paramString("openLoginId");
		if(openLoginId != null && openLoginId.length() > 0 &&  Long.parseLong(openLoginId) > 0 ){//判断参数不为空
			//如果联合登陆信息为空，或者联合点登陆信息已绑定，则跳转注册页面。
			CooperationLogin cooperation = cooperationLoginService.getCooperationLoginById(Long.parseLong(openLoginId));
			if(cooperation != null && cooperation.getUser_id() > 0){//如果联合登陆信息不为空，并联合点登陆信息已绑定，则跳转登陆页面
				try {
					User u = userService.getUserById(cooperation.getUser_id());
					if(u != null){
						request.setAttribute("openUser", u);
						request.getRequestDispatcher("/user/login.html").forward(request, response);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else if(cooperation == null){//如果联合登陆信息为空，则跳转注册页
				return "register";
			}
		}
		
		if (isSession()) {
			return "member";
		}
		boolean open = true;
		
		// 联合登陆随机产生用户名
		String webid = Global.getValue("webid");
		String username = "";
		if(webid != null && webid.length() > 0){
			username = webid+"_"+RandomUtil.getRandomStr(3)+RandomUtil.getRandomNumString(7);
		}else{
			username = RandomUtil.getRandomStr(3)+"_"+RandomUtil.getRandomStr(3)+RandomUtil.getRandomNumString(3);
		}
		// 如果随机产生的用户名已经存在，则规矩规则重新产生用户名，之后再判断是存在。如果不存在，则break，产生用户名
		boolean result = userService.checkUsername(username);
		while(!result){//如果此用户名义存在，则重新产生用户名
			if(webid != null && webid.length() > 0){
				username = webid+"_"+RandomUtil.getRandomStr(3)+RandomUtil.getRandomNumString(3);
			}else{
				username = RandomUtil.getRandomStr(3)+"_"+RandomUtil.getRandomStr(3)+RandomUtil.getRandomNumString(3);
			}
			boolean resultCheck = userService.checkUsername(username);
			if(resultCheck){
				break;
			}
		}
		user.setUsername(username);
		user.setPassword(username);//初始用户密码
		// 密码MD5加密
		MD5 md5 = new MD5();
		user.setPassword(md5.getMD5ofStr(user.getPassword()));
		user.setAddtime(this.getTimeStr());
		user.setAddip(this.getRequestIp());
		user.setType_id(999);//用户类型为临时用户
		User newUser = userService.register(user);
		User u = userService.getUserByName(newUser.getUsername());
		// 用户缓存
		UserCache cache = new UserCache();
		cache.setUser_id(u.getUser_id());
		userService.saveUserCache(cache);

		if (open) {
			msg.append("注册成功！");
			if (!StringUtils.isBlank(newUser.getInvite_userid())) {
				rewardStatisticsService.addRewardStatistics(newUser.getUser_id(), getRequestIp());
			}
			
			// 将新注册的user id写入联合登陆表，实现绑定
			cooperationLoginService.updateCooperationUserIdById(u.getUser_id(), Long.parseLong(openLoginId));
			request.setAttribute("openUser", u);
			try {
				request.getRequestDispatcher("/user/login.html").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			msg.append("添加成功");
			message(msg.toString(), "/admin/userinfo/user.html", "点击返回！");
		}
		return "success";
		
	}
	
	/**
	 * 用于用户注册时，判断用户名是否存在Action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkUsername() throws Exception {
		logger.debug(user);
		boolean result = userService.checkUsername(user.getUsername());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		// 直接输入响应的内容
		if (result) {
			out.println("1");
		} else {
			out.println("");
		}
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 用于用户注册时，判断Email是否存在Action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkEmail() throws Exception {
		boolean result = userService.checkEmail(user.getEmail());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		// 直接输入响应的内容
		if (result) {
			out.println("1");
		} else {
			out.println("");
		}
		out.flush();
		out.close();
		return null;
	}

	public String show() throws Exception {
		DetailUser showUser = userService.getDetailUser(user.getUser_id());
		Userinfo info = userinfoService.getUserinfoByUserid(user.getUser_id());
		UserAccountSummary uas = accountService.getUserAccountSummary(user
				.getUser_id());
		// v1.6.7.1 RDPROJECT-356 zza 2013-11-19 start（无分页）
		List<Borrow> borrowList = borrowService.getSuccessBorrowList(user.getUser_id());
		request.setAttribute("list", borrowList);
		// v1.6.7.1 RDPROJECT-356 zza 2013-11-19 end
//		List borrowList = borrowService.getBorrowList(page, user.getUser_id());
		List investList = borrowService.getSuccessListByUserid(
				user.getUser_id(), "1");

		/**
		 * 新加--
		 */
		// 标详情页面性能优化 lhm 2013/10/11 start
//		List repament_scuess = memberBorrowService.getRepayMentList(// 已还款
//				"repaymentyes", user.getUser_id());
//		List repament_failure = memberBorrowService.getRepayMentList(// 未还款
//				"repayment", user.getUser_id());
//		List<Borrow> borrowFlowList = borrowService// 流标
//				.getBorrowFlowListByuserId(user.getUser_id());
//		List<Repayment> earlyRepaymentList = borrowService.getRepaymentList(// 提前还款
//				null, user.getUser_id());
//		List<Repayment> lateRepaymentList = borrowService.getRepaymentList(// 迟还款
//				"lateRepayment", user.getUser_id());
//		List<Repayment> overdueRepaymentList = borrowService.getRepaymentList(// 30天内逾期还款
//				"overdueRepayment", user.getUser_id());
//		List<Repayment> overdueRepaymentsList = borrowService.getRepaymentList(// 30天后逾期还款
//				"overdueRepayments", user.getUser_id());
//		List<Repayment> overdueNoRepaymentsList = borrowService// 逾期未还款
//				.getRepaymentList("overdueNoRepayments", user.getUser_id());
//
//		request.setAttribute("repament_scuess", repament_scuess.size());
//		request.setAttribute("repament_failure", repament_failure.size());
//		request.setAttribute("borrowFlowList", borrowFlowList.size());
//		request.setAttribute("earlyRepaymentList", earlyRepaymentList.size());
//		request.setAttribute("lateRepaymentList", lateRepaymentList.size());
//		request.setAttribute("overdueRepaymentList",
//				overdueRepaymentList.size());
//		request.setAttribute("overdueRepaymentsList",
//				overdueRepaymentsList.size());
//		request.setAttribute("overdueNoRepaymentsList",
//				overdueNoRepaymentsList.size());
	// v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 start
		/*// 已还款
		long repament_scuess = memberBorrowService.getRepayMentCount("repaymentyes", user.getUser_id());
		// 未还款
		long repament_failure = memberBorrowService.getRepayMentCount("repayment", user.getUser_id());
		// 流标
		long borrowFlowCount = borrowService.getBorrowFlowCountByUserId(user.getUser_id());
		// 提前还款
		long earlyRepaymentCount = borrowService.getRepaymentCount(null, user.getUser_id());
		// 迟还款
		long lateRepaymentCount = borrowService.getRepaymentCount("lateRepayment", user.getUser_id());
		// 30天内逾期还款
		long overdueRepaymentCount = borrowService.getRepaymentCount("overdueRepayment", user.getUser_id());
		// 30天后逾期还款
		long overdueRepaymentsCount = borrowService.getRepaymentCount("overdueRepayments", user.getUser_id());
		// 逾期未还款
		long overdueNoRepaymentsCount = borrowService.getRepaymentCount("overdueNoRepayments", user.getUser_id());

		request.setAttribute("repament_scuess", repament_scuess);
		request.setAttribute("repament_failure", repament_failure);
		request.setAttribute("borrowFlowList", borrowFlowCount);
		request.setAttribute("earlyRepaymentList", earlyRepaymentCount);
		request.setAttribute("lateRepaymentList", lateRepaymentCount);
		request.setAttribute("overdueRepaymentList", overdueRepaymentCount);
		request.setAttribute("overdueRepaymentsList", overdueRepaymentsCount);
		request.setAttribute("overdueNoRepaymentsList", overdueNoRepaymentsCount);*/
		
		// 标详情页面性能优化 lhm 2013/10/11 end
		  // v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 end
		/**
		 * --新加结束
		 */
		request.setAttribute("u", showUser);
		request.setAttribute("info", info);
		request.setAttribute("summary", uas);
//		request.setAttribute("borrowList", borrowList);
		request.setAttribute("investList", investList);
		logger.info(showUser);
		return "success";
	}

	public String getpwd() throws Exception {
		if (isBlank()) {
	      this.request.setAttribute("getpwdtype", "pwd");
	      return "success";
	    }

	    String username =paramString("username");
	    String validcode = paramString("validcode");
	    this.request.setAttribute("getpwdtype", "pwd");
	    User u = null;
	    if (StringUtils.isNull(validcode).equals("")) {
			this.message("验证码不能为空！");
		    return MSG;
		} else if (StringUtils.isNull(username).equals("")) {
			this.message("用户名不能为空！");
		    return MSG;
		} else if (!this.checkValidImg(validcode)) {
			this.message("验证码不正确！");
		    return MSG;
		} else {
			u = userService.getUserByName(username);
			if (u == null) {
				this.message("用户名未注册！");
			    return MSG;
			} else {
	 	    	List<PasswordToken> tokenList = passwordTokenService.getPasswordTokenByUsername(username);
				if (tokenList.size() == 0) {
					return getPwdMail(u);
				} else {
					request.setAttribute("tokenList", tokenList);
					request.getRequestDispatcher("/user/getpwdByEmail.html").forward(request, response);
				}
	 	    }
	    }
	    return "pwd";
	}
	
	/**
	 * 根据用户发送邮件（登录密码）
	 * @param u
	 * @return
	 * @throws Exception
	 */
	private String getPwdMail(User u) throws Exception {
		sendGetPwdMail(u);
		String email = u.getEmail();
		email = (email.substring(0, 2)).concat("*").concat(
				email.substring(email.indexOf('@')));
		this.message("找回密码邮件发送至" + email + "！", "/user/login.html");
		return MSG;
	}
	
	/**
	 * 根据用户发送邮件（支付密码）
	 * @param u
	 * @return
	 * @throws Exception
	 */
	private String getPayPwdMail(User u) throws Exception {
		sendGetPayPwdMail(u);
		String email = u.getEmail();
		email = (email.substring(0, 2)).concat("*").concat(
				email.substring(email.indexOf('@')));
		this.message("找回密码邮件发送至" + email + "！", "/user/login.html");
		return MSG;
	}
	
	/**
	 * 根据密保获取邮件（登录密码）
	 * @return
	 * @throws Exception
	 */
	public String getpwdByEmail() throws Exception {
		request.getAttribute("tokenList");
		this.request.setAttribute("getpwdtype", "pwd");
		String htmlType = paramString("htmlType");
		if (StringUtils.isBlank(htmlType)) {
//			request.getAttribute("tokenList");
			request.setAttribute("getEmail", "pwdByEmail");
			request.setAttribute("pwdComUrl", "/user/getpwdByEmail.html");
			return "success";
		}
		String username = paramString("username");
		List<PasswordToken> tokenList = passwordTokenService.getPasswordTokenByUsername(username);
		request.setAttribute("getEmail", "pwdByEmail");
		request.setAttribute("tokenList", tokenList);
		User u = userService.getUserByName(username);
		String errormsg = "";
		if (username == null) {
		    this.message("用户名不能为空");
		    return MSG;
		} else {
			int size = tokenList.size();
			PasswordToken passwordToken = null;
			for (int i = 0; i < size; i++) {
				String answer = request.getParameter("answer" + (i+1) + "");
				passwordToken = tokenList.get(i);
				if (!StringUtils.isBlank(answer) && answer.equals(passwordToken.getAnswer())) {
					return getPwdMail(u);
				}
			}
		}
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-06 start
		errormsg = "答案错误，请重试！";
		request.setAttribute("errormsg", errormsg);
	    return SUCCESS;
	    // v1.6.7.2 RDPROJECT-505 zza 2013-12-06 end
	}
	
	/**
	 * 根据密保获取邮件（支付密码）
	 * @return
	 * @throws Exception
	 */
	public String getpaypwdByEmail() throws Exception {
		request.getAttribute("tokenList");
		this.request.setAttribute("getpwdtype", "paypwd");
		String htmlType = paramString("htmlType");
		if (StringUtils.isBlank(htmlType)) {
	      this.request.setAttribute("getEmail", "paypwdByEmail");
	      request.setAttribute("pwdComUrl", "/user/getpaypwdByEmail.html");
	      return "success";
	    }
		String username = paramString("username");
		List<PasswordToken> tokenList = passwordTokenService.getPasswordTokenByUsername(username);
		request.setAttribute("getEmail", "paypwdByEmail");
		request.setAttribute("tokenList", tokenList);
		User u = userService.getUserByName(username);
		String errormsg = "";
		if (username == null) {
			this.message("用户名不能为空");
		    return MSG;
		} else {
			int size = tokenList.size();
			PasswordToken passwordToken = null;
			for (int i = 0; i < size; i++) {
				String answer = request.getParameter("answer" + (i+1) + "");
				passwordToken = tokenList.get(i);
				if (!StringUtils.isBlank(answer) && answer.equals(passwordToken.getAnswer())) {
					return getPayPwdMail(u);
				}
			}
		}
		// v1.6.7.2 RDPROJECT-505 zza 2013-12-06 start
		errormsg = "答案错误，请重试！";
		request.setAttribute("errormsg", errormsg);
	    return SUCCESS;
	    // v1.6.7.2 RDPROJECT-505 zza 2013-12-06 end
	}

	public String getpaypwd() throws Exception {
		if (isBlank()) {
			request.setAttribute("getpwdtype", "paypwd");
			return SUCCESS;
		}
		String username = paramString("username");
		String validcode = paramString("validcode");
		request.setAttribute("getpwdtype", "paypwd");
		User u = null;
		if (StringUtils.isNull(validcode).equals("")) {
			this.message("验证码不能为空！");
		    return MSG;
		} else if (StringUtils.isNull(username).equals("")) {
			this.message("用户名不能为空！");
		    return MSG;
		} else if (!this.checkValidImg(validcode)) {
			this.message("验证码不正确！");
		    return MSG;
		} else {
			u = userService.getUserByName(username);
			if (u == null) {
				this.message("用户名未注册！");
			    return MSG;
			} else {
				List<PasswordToken> tokenList = passwordTokenService.getPasswordTokenByUsername(username);
				if (tokenList.size() == 0) {
					return getPayPwdMail(u);
				} else {
					request.setAttribute("tokenList", tokenList);
					request.getRequestDispatcher("/user/getpaypwdByEmail.html").forward(request, response);
				}
			}
		}
		return "paypwd";
	}

	public String setpwd() throws Exception {
		String type = paramString("type");
		String idstr = paramString("id");
		String errormsg = "";
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] idstrBytes = decoder.decodeBuffer(idstr);
		String decodeidstr = new String(idstrBytes);
		// [user_id,userIDaddtime的MD5加密串,timestr,随机码]
		String[] idstrArr = decodeidstr.split(",");
		EmailLog e = emailLogService.getEmailLog(idstr,0);
		//判断该链接状态，如果为1，说明已经被使用过，不能再次使用
		if(e.getStatus() == 1){
			errormsg = "该链接已失效，请重新获取！";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}
		if (idstrArr.length < 4) {
			errormsg = "链接无效，请<a href='" + request.getContextPath()
					+ "/getpwd.html'>点此</a>重新获取邮件！";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}
		long setpwdTime = NumberUtils.getLong(idstrArr[2]);
		
		if (System.currentTimeMillis() - setpwdTime * 1000 > 1 * 24 * 60
				* 60 * 1000) {
			errormsg = "链接有效时间1天，已经失效，请<a href='"
					+ request.getContextPath()
					+ "/getpwd.html'>点此</a>重新获取邮件!";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}
		User u = userService.getUserById(NumberUtils.getLong(idstrArr[0]));
		if (u == null) {
			errormsg = "用户不存在，请联系客服!";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}
		//检查用户的id+addtime的MD5加密串与传参的加密串进行比较，是否一致
		String userIDAddtime = u.getUser_id()+u.getAddtime();
		MD5 md5=new MD5();
		userIDAddtime =  md5.getMD5ofStr(userIDAddtime);
		if(!StringUtils.isNull(idstrArr[1]).equals(userIDAddtime)){
			errormsg = "找回密码失败，请联系客服!";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}

		if (type.equals("")) {// 进入找回密码界面
			request.setAttribute("u", u);
			return SUCCESS;
		} else {// 重新设置密码
			String password = StringUtils.isNull(request
					.getParameter("password"));
			String confirm_password = StringUtils.isNull(request
					.getParameter("confirm_password"));

			User user = u;
			if (password.equals("") || confirm_password.equals("")) {
				errormsg = "新密码不能为空！";
			} else if (!password.equals(confirm_password)) {
				errormsg = "两次密码不一致！";
			}else {
				user.setPassword(md5.getMD5ofStr(password));
				userService.modifyUserPwd(user);
				e.setStatus(1);
				e.setType(0);
				e.setContent(idstr);
				//通过链接修改完密码后把该链接状态改为1
				emailLogService.updateEmailLog(e);
				this.message("重置密码成功！", "/user/login.html", "点此登录");
				return OK;
			}
			request.setAttribute("u", user);
			request.setAttribute("errormsg", errormsg);
			return SUCCESS;
		}
	}

	public String setpaypwd() throws Exception {
		String type = paramString("type");
		String idstr = paramString("id");
		String errormsg = "";
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] idstrBytes = decoder.decodeBuffer(idstr);
		String decodeidstr = new String(idstrBytes);
		// [user_id,userIDaddtime的MD5加密串,timestr,随机码]
		String[] idstrArr = decodeidstr.split(",");
		EmailLog e = emailLogService.getEmailLog(idstr,1);
		if(e.getStatus() == 1){
			errormsg = "该链接已失效，请重新获取！";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}
		if (idstrArr.length < 4) {
			errormsg = "链接无效，请<a href='" + request.getContextPath()
					+ "/getpwd.html'>点此</a>重新获取邮件！";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}
		long setpwdTime = NumberUtils.getLong(idstrArr[2]);
		if (System.currentTimeMillis() - setpwdTime * 1000 > 1 * 24 * 60
				* 60 * 1000) {
			errormsg = "链接有效时间1天，已经失效，请<a href='"
					+ request.getContextPath()
					+ "/getpwd.html'>点此</a>重新获取邮件!";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}
		User u = userService.getUserById(NumberUtils.getLong(idstrArr[0]));
		if (u == null) {
			errormsg = "用户不存在，请联系客服!";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}
		//检查用户的id+addtime的MD5加密串与传参的加密串进行比较，是否一致
		String userIDAddtime = u.getUser_id()+u.getAddtime();
		MD5 md5=new MD5();
		userIDAddtime =  md5.getMD5ofStr(userIDAddtime);
		if(!StringUtils.isNull(idstrArr[1]).equals(userIDAddtime)){
			errormsg = "招回密码失败，请联系客服!";
			this.message(errormsg, "/getpwd.html");
			return FAIL;
		}

		if (type.equals("")) {// 进入找回密码界面
			request.setAttribute("u", u);
			return SUCCESS;
		} else {// 重新设置密码
			String password = StringUtils.isNull(request
					.getParameter("password"));
			String confirm_password = StringUtils.isNull(request
					.getParameter("confirm_password"));

			User user = u;
			if (password.equals("") || confirm_password.equals("")) {
				errormsg = "新密码不能为空！";
			} else if (!password.equals(confirm_password)) {
				errormsg = "两次密码不一致！";
			}else {
				user.setPaypassword(md5.getMD5ofStr(password));
				userService.modifyPayPwd(user);
				e.setStatus(1);
				e.setType(1);
				e.setContent(idstr);
				emailLogService.updateEmailLog(e);
				this.message("重置密码成功！", "/user/login.html", "点此登录");
				return OK;
			}
			request.setAttribute("u", user);
			request.setAttribute("errormsg", errormsg);
			return SUCCESS;
		}
	}

	public String regInvite() throws Exception {
		//是否启用验证码规则
		Rule rule=Global.getRule(EnumRuleNid.VALIDE_ENABLE.getValue());
		request.setAttribute("rule", rule);
		String u = paramString("u");
		BASE64Decoder decoder = new BASE64Decoder();
		String user_id = decoder.decodeStr(u);
		long userid = NumberUtils.getLong(user_id);
		User inviteUser = userService.getUserById(userid);
		request.setAttribute("inviteUser", inviteUser);
		session.put("inviteUser", inviteUser);
		return SUCCESS;
	}

	// 校验表单信息是否非法
	private String loginValidate() {
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 start
		//是否启用验证码规则
		Rule rule=Global.getRule(EnumRuleNid.LOGIN_VALIDE_ENABLE.getValue());
		if(rule!=null && rule.getStatus()==1){
			String valicode=paramString("valicode");
			if(StringUtils.isBlank(valicode)){
				sb.append("验证码不能为空！");
				message(sb.toString(), backUrl);
				return "fail";
			}else if(!checkValidImg(valicode)){
				sb.append("验证码错误！");
				message(sb.toString(), backUrl);
				return "fail";
			}
		}
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 end
		// 检查用户名和密码是否为空
		if (StringUtils.isNull(user.getUsername()).equals("")) {
			String errorMsg = "用户名不能为空！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		if (StringUtils.isNull(user.getPassword()).equals("")) {
			String errorMsg = "密码不能为空！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		return "success";
	}

	/**
	 * 保存cookie信息
	 */
	private void setCookie() {
		String cookieValue = StringUtils.isNull(request
				.getParameter("login_cookie"));
		if (!cookieValue.isEmpty()) {
			int cookieDate = 0;
			if (cookieValue != null && cookieValue != "") {
				cookieDate = Integer.parseInt(cookieValue);
			}
			Cookie loginNameCookie = new Cookie("cookie_username",
					user.getUsername());
			Cookie loginPassCookie = new Cookie("cookie_password",
					user.getPassword());
			loginNameCookie.setMaxAge(cookieDate * 60);
			loginPassCookie.setMaxAge(cookieDate * 60);
			response.addCookie(loginNameCookie);
			response.addCookie(loginPassCookie);
		}
	}

	/**
	 * 判断浏览器中是否有cookie保证用户密码
	 * 
	 * @return
	 */
	private boolean hasCookieValue() {
		String username = "";
		String password = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if ("cookie_username".equals(StringUtils.isNull(cookie
						.getName()))) {
					username = cookie.getValue();
				}
				if ("cookie_password".equals(StringUtils.isNull(cookie
						.getName()))) {
					password = cookie.getValue();
				}
			}
			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				return false;
			} else {
				user.setUsername(username);
				user.setPassword(password);
				return true;
			}
		}
		return false;
	}

	private String regValidate() {
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 start
		//是否启用验证码规则
		Rule rule = Global.getRule(EnumRuleNid.VALIDE_ENABLE.getValue());
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			String valicode = paramString("valicode");
			if (StringUtils.isBlank(valicode)) {
				sb.append("验证码不能为空！");
				message(sb.toString(), backUrl);
				return "fail";
			} else if (!checkValidImg(valicode)) {
				sb.append("验证码错误！");
				message(sb.toString(), backUrl);
				return "fail";
			}
		}
		if (StringUtils.isNull(user.getUsername()).equals("")) {
			String errorMsg = "用户名不能为空！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		// v1.6.6.2 注册时加强真实姓名验证 start
		if (!StringUtils.isChinese(user.getRealname())) {	
			String errorMsg = "真实姓名格式不正确！";
			logger.info(errorMsg+ ":"+user.getRealname());
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		// v1.6.6.2 注册时加强真实姓名验证 end
		if (StringUtils.isNull(user.getPassword()).equals("")) {
			String errorMsg = "密码不能为空！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		//v1.6.6.2 RDPROJECT-365 yl 2013-10-22 end
		User existUser = userService.getUserByName(user.getUsername());
		if (existUser != null) {
			String errorMsg = "用户名已经存在！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		if (StringUtils.isNull(user.getRealname()).equals("")) {
			String errorMsg = "真实姓名不能为空！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		if (!StringUtils.isEmail(user.getEmail())) {
			String errorMsg = "邮箱格式不对！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		boolean isHasEmail = userService.checkEmail(user.getEmail());
		if (!isHasEmail) {
			String errorMsg = "邮箱已经存在！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		
		// v1.6.6.2 RDPROJECT-307 lhm 2013-10-28 start
		if (!"".equals(StringUtils.isNull(user.getPhone())) && !StringUtils.isPhone(user.getPhone())) {
			String errorMsg = "手机格式不对！";
			logger.info(errorMsg);
			msg.append(errorMsg);
			message(msg.toString(), backUrl);
			return "fail";
		}
		// v1.6.6.2 RDPROJECT-307 lhm 2013-10-28 end
		return "success";
	}
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	//TODO RDPROJECT-314 DELETE

	private void sendActiveMail(User user) throws Exception {
		String to = user.getEmail();
		Mail m = Mail.getInstance();
		m.setSubject("用户注册邮箱激活");
		m.setTo(to);
		m.readActiveMailMsg();
		m.replace(user.getUsername(), to,
				"/user/active.html?id=" + m.getdecodeIdStr(user));
		logger.debug("Email_msg:" + m.getBody());
		m.sendMail();
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end

	public String active() throws Exception {
		User user = null;
		String msg = "";
		String errormsg = "";
		String idstr = paramString("id");
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] idstrBytes = decoder.decodeBuffer(idstr);
		String decodeidstr = new String(idstrBytes);
		String[] idstrArr = decodeidstr.split(",");
		
		if (idstrArr.length < 4) {
			errormsg = "链接无效，请<a href='" + request.getContextPath()
					+ "/user/login.html'>点此登录</a>，重新获取邮件！";
			message(errormsg, "/user/login.html");
			return MSG;
		}
		long activeTime = NumberUtils.getLong(idstrArr[2]);
		/*long activeTime=1343940596;*/
		if (System.currentTimeMillis() - activeTime * 1000 > 1 * 24 * 60
				* 60 * 1000) {
			errormsg = "链接有效时间1天，已经失效，请<a href='" + request.getContextPath()
					+ "/user/login.html'>点此登录</a>，重新获取邮件!";
			message(errormsg, "/user/login.html");
			return MSG;
		}
		if (idstrArr.length < 1) {
			errormsg = "解析激活码失败，请联系管理员！";
			message(errormsg, "/member/index.html");
			return MSG;
		} else {
			String useridStr = idstrArr[0];
			long user_id = 0;
			try {
				user_id = Long.parseLong(useridStr);
			} catch (Exception e) {
				user_id = 0;
			}
			if (user_id > 0) {
				user = userService.getUserById(user_id);
				if (user == null) {
					errormsg = "当前用户不存在，请重新注册！";
					message(errormsg,"/user/register.html");
					return MSG;
				}
			}else {
				errormsg = "解析激活码失败，请联系管理员！";
				message(errormsg,"/member/index.html");
				return MSG;
			}
			String addtime = user.getAddtime();
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			if(user.getEmail_status() == 1){
				errormsg = "您已通过邮箱认证！请勿再次点击链接！";
				message(errormsg,"/member/index.html");
				return MSG;
			}
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
			MD5 md5 = new MD5();
			String m = md5.getMD5ofStr(user_id + addtime);
			if (useridStr.equals(user_id + "") && StringUtils.isNull(idstrArr[1]).toLowerCase().equals(m.toLowerCase())) {
				// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
				user.setEmail_status(1);
				// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
				//邮箱认证积分
				CreditType emailType = userCreditService.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_EMAIL.getValue());
				if(emailType != null){
					List logList = userCreditService.getCreditLogList(user_id, emailType.getId());
					if(logList == null || logList.size() <= 0){
						userCreditService.updateUserCredit(user_id, (int)emailType.getValue(), new Byte(Constant.OP_ADD), EnumIntegralTypeName.INTEGRAL_EMAIL.getValue());
					}
				}
				
				User newUser = userService.modifyEmail_status(user);
				logger.debug("UserAction:注册激活邮箱操作active()：user_id:"+user.getUser_id()+", username:"+user.getUsername()+", email_status:"+user.getEmail_status());
				msg = "邮箱激活成功！";
			} else {
				errormsg = "激活失败，请重新激活！";
			}
		}
		if (errormsg.equals("")) {
			message(msg, "/member/index.html");
		} else {
			message(errormsg, "/member/index.html");
		}
		return MSG;
	}

	private void sendGetPwdMail(User user) throws Exception {
		String to = user.getEmail();
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
		//TODO RDPROJECT-314 DOING
		long user_id = user.getUser_id();
		//TODO get content
		Mail m = Mail.getInstance();
		String content = m.getdecodeIdStr(user);
		String getUrl = "/user/setpwd.html?id=" + java.net.URLEncoder.encode(content,"UTF-8");
		Global.setTransfer("getUrl", getUrl);
		BaseAccountLog blog=new GetPwdLog(user_id);
		blog.doEvent();

		EmailLog e = new EmailLog();
		e.setUser_id(user.getUser_id());
		e.setStatus(0);
		e.setType(0);
		e.setContent(content);
		e.setAddtime(DateUtils.getNowTimeStr());
		emailLogService.addEmailLog(e);
		
		/*Mail m = Mail.getInstance();
		String content = m.getdecodeIdStr(user);
		m.setTo(to);
		m.readGetpwdMailMsg();
		m.replace(user.getUsername(), to,
				"/user/setpwd.html?id=" + java.net.URLEncoder.encode(content,"UTF-8"));
		logger.debug("Email_msg:" + m.getBody());
		m.sendMail();*/
		
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	}

	private void sendGetPayPwdMail(User user) throws Exception {
		String to = user.getEmail();
		
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
		//TODO RDPROJECT-314 DOING
		long user_id = user.getUser_id();
		//TODO get content
		Mail m = Mail.getInstance();
		String content = m.getdecodeIdStr(user);
		String getUrl = "/user/setpaypwd.html?id=" + java.net.URLEncoder.encode(content, "UTF-8");

		Global.setTransfer("getUrl", getUrl);
		BaseAccountLog blog=new GetPayPwdLog(user_id);
		blog.doEvent();
		
		EmailLog e = new EmailLog();
		e.setUser_id(user.getUser_id());
		e.setStatus(0);
		e.setType(1);
		e.setContent(content);
		e.setAddtime(DateUtils.getNowTimeStr());
		emailLogService.addEmailLog(e);
		
		/*Mail m = Mail.getInstance();
		String content = m.getdecodeIdStr(user);
		m.setTo(to);
		m.readGetpwdMailMsg();
		m.replace(user.getUsername(), to,
				"/user/setpaypwd.html?id=" + java.net.URLEncoder.encode(content, "UTF-8"));
		logger.debug("Email_msg:" + m.getBody());
		
		m.sendMail();*/
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	}

	/**
	 * 
	 * 更新用户信息
	 * 
	 * @return
	 */
	public String updateuser() {
		String actionType = StringUtils.isNull(request
				.getParameter("actionType"));

		if (user != null && user.getPassword().length() > 0) {
			MD5 md5 = new MD5();
			user.setPassword(md5.getMD5ofStr(user.getPassword()));
			user.setUptime(this.getTimeStr());
			user.setUpip(this.getRequestIp());
		}
		userService.updateuser(user);
		//v1.6.7.1 安全优化 sj 2013-11-13 start
		//v1.6.7.1 安全优化 sj 2013-11-18 start
		//定期（时间可配置，比如：1个月）对密码进行修改,修改后更新dw_user_cache表的时间
//		UserCache cache = new UserCache();
//		cache.setPwd_modify_time(this.getTimeStr());
//		cache.setUser_id(user.getUser_id());
//		userService.updateUserCacheTime(cache);
		//v1.6.7.1 安全优化 sj 2013-11-18 end
		//v1.6.7.1 安全优化 sj 2013-11-13 end
		context = ServletActionContext.getServletContext();
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		context.setAttribute("kefuList", userService.getKfList(1));
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		//更新用户所属客服等信息
		UserCache userCache =userService.getUserCacheByUserid(user.getUser_id());
		long kefu_userid =  paramLong("kefu_userid");
		String kefu_username = null;
		if(kefu_userid > 0 ){
			kefu_username = userService.getUserById(kefu_userid).getUsername();		
		}
		userCache.setKefu_userid(kefu_userid);
		userCache.setKefu_username(kefu_username);
		userinfoService.updateUserCache(userCache);		
		
		if (actionType.isEmpty()) {
			msg.append("更新成功");
			message(msg.toString(), "");
			return SUCCESS;
		} else {
			message("更新成功", "/admin/userinfo/user.html");
			return ADMINMSG;
		}
	}

	//v1.6.7.1 安全优化 sj 2013-11-13 start
	public String add() throws Exception {
		RuleModel safetyRule = new RuleModel(Global.getRule(EnumRuleNid.SAFETY.getValue()));
		if(safetyRule != null && safetyRule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue() 
    			&& safetyRule.getValueIntByKey("adminStatus") == 1 && safetyRule.getValueLongByKey("adminLoginFailIsLock") == 1){
			String adminUnallowedUsername = safetyRule.getValueStrByKey("adminUnallowedUsername");
			request.setAttribute("adminUnallowedUsername", adminUnallowedUsername);
		}
		
		String actionType = paramString("actionType");
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		List kfList = userService.getKfList(1);
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		request.setAttribute("kfList", kfList);
		//v1.6.7.1 安全优化 sj 2013-11-18 start
		request.setAttribute("weburl", Global.getValue("weburl"));
		//v1.6.7.1 安全优化 sj 2013-11-18 end
		if(!StringUtils.isBlank(actionType)){
			String pwd = user.getPassword();
			// 密码MD5加密
			MD5 md5 = new MD5();
			user.setPassword(md5.getMD5ofStr(user.getPassword()));
			user.setAddtime(this.getTimeStr());
			userService.addUser(user);
			
			UserCache userCache = new UserCache();
			Long user_id = userService.getUserByName(user.getUsername()).getUser_id();
			userCache.setUser_id(user_id);
			userCache.setPwd_modify_time(Long.parseLong(this.getTimeStr()));
			userService.insertRegisterUserCache(userCache);
			
			message("新增成功！","/admin/userinfo/user.html");
			return ADMINMSG;
		}
		return SUCCESS;
	}
	
	public String modifypassword() throws Exception{
		userService.modifypassword(user);
		//定期（时间可配置，比如：1个月）对密码进行修改,修改后更新dw_user_cache表的时间
		UserCache userCache = new UserCache();
		userCache.setPwd_modify_time(Long.parseLong(this.getTimeStr()));
		userCache.setUser_id(user.getUser_id());
		userService.updateUserCacheTime(userCache);
		return SUCCESS;
	}
	
	public String modifypass(){
		
		return SUCCESS;
	}
	//v1.6.7.1 安全优化 sj 2013-11-13 end
	
	// 下面通过Struts2注入
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public UserCache getUserCache() {
		return userCache;
	}

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
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

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public BorrowService getBorrowService() {
		return borrowService;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	public PasswordTokenService getPasswordTokenService() {
		return passwordTokenService;
	}

	public void setPasswordTokenService(PasswordTokenService passwordTokenService) {
		this.passwordTokenService = passwordTokenService;
	}

	public String getValidatecode() {
		return validatecode;
	}

	public void setValidatecode(String validatecode) {
		this.validatecode = validatecode;
	}

	public StringBuffer getMsg() {
		return msg;
	}

	public void setMsg(StringBuffer msg) {
		this.msg = msg;
	}

	public User getModel() {
		return user;
	}

	public CooperationLoginService getCooperationLoginService() {
		return cooperationLoginService;
	}

	public void setCooperationLoginService(
			CooperationLoginService cooperationLoginService) {
		this.cooperationLoginService = cooperationLoginService;
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
	public void setRewardStatisticsService(
			RewardStatisticsService rewardStatisticsService) {
		this.rewardStatisticsService = rewardStatisticsService;
	}

	/**
	 * 获取userCreditService
	 * 
	 * @return userCreditService
	 */
	public UserCreditService getUserCreditService() {
		return userCreditService;
	}

	/**
	 * 设置userCreditService
	 * 
	 * @param userCreditService 要设置的userCreditService
	 */
	public void setUserCreditService(UserCreditService userCreditService) {
		this.userCreditService = userCreditService;
	}

	/**
	 * 获取memberBorrowService
	 * 
	 * @return memberBorrowService
	 */
	public MemberBorrowService getMemberBorrowService() {
		return memberBorrowService;
	}

	public EmailLogService getEmailLogService() {
		return emailLogService;
	}

	public void setEmailLogService(EmailLogService emailLogService) {
		this.emailLogService = emailLogService;
	}

	public RuleService getRuleService() {
		return ruleService;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	public void setMemberBorrowService(MemberBorrowService memberBorrowService) {
		this.memberBorrowService = memberBorrowService;
	}
	
	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public void setAccountSumService(AccountSumService accountSumService) {
		this.accountSumService = accountSumService;
	}
	
}
