package com.p2psys.web.action.member;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Attestation;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.model.RuleModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.noac.ReactiveUserEmailLog;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.tool.coder.BASE64Decoder;
import com.p2psys.tool.javamail.Mail;
import com.p2psys.util.DateUtils;
import com.p2psys.util.ImageUtil;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class IdentifyAction extends BaseAction {

	private static Logger logger = Logger.getLogger(IdentifyAction.class);

	private UserinfoService userinfoService;
	private UserService userService;
	private UserCreditService userCreditService;

	private File litpic;
	private String name;
	private int type_id;
	private String type;
	private String filePath;
	private String sep = File.separator;

	private String email;
	private String phone;
	
	private String verify_remark;
	private String verify_time;
	
	public UserinfoService getUserinfoService() {
		return userinfoService;
	}

	public void setUserinfoService(UserinfoService userinfoService) {
		this.userinfoService = userinfoService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserCreditService getUserCreditService() {
		return userCreditService;
	}

	public void setUserCreditService(UserCreditService userCreditService) {
		this.userCreditService = userCreditService;
	}

	// 参数Setter
	public File getLitpic() {
		return litpic;
	}

	public void setLitpic(File litpic) {
		this.litpic = litpic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public String getVerify_remark() {
		return verify_remark;
	}

	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
	}

	public String getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Action start
	public String list() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		String actionType = paramString("actionType");
		if (!StringUtils.isBlank(actionType)) {
			String validcode = paramString("validcode");
			
			if (this.litpic == null) {
				message("你上传的图片为空", "/member/identify/attestation.html");
				return MSG;
			}
			
			if(validcode.isEmpty()){
				message("你输入的校验码为空！", "/member/identify/attestation.html");
				return MSG;
			}
			
			if (!this.checkValidImg(validcode)) {
				message("你输入的校验码错误！", "/member/identify/attestation.html");
				return MSG;
			}
			// v1.6.7.1 RDPROJECT-441 xx 2013-11-11 start
			if (!ImageUtil.fileIsImage(litpic)) {
				message("您上传的图片无效，请重新上传！", "/member/identify/attestation.html");
				return MSG;
			}
			// v1.6.7.1 RDPROJECT-441 xx 2013-11-11 end

		}
		List<Attestation> list = new ArrayList();
		Attestation att = new Attestation();
		if (type != null && !type.equals("")) {
			moveFile(user);
			att.setUser_id(user.getUser_id());
			att.setName(name);
			att.setType_id(type_id);
			att.setAddtime((new Date()).getTime() / 1000 + "");
			att.setLitpic(filePath);
			att.setJifen(userinfoService.getAttestationTypeByTypeId(type_id).getJifen());
			att.setVerify_remark(verify_remark);
			att.setVerify_time(verify_time);
			userinfoService.addAttestation(att);
		}
		list = userinfoService.getAttestationListByUserid(user.getUser_id());
		request.setAttribute("attestations", list);
		return "success";
	}

	private void moveFile(User user) {
		String dataPath = ServletActionContext.getServletContext().getRealPath(
				"/data");
		String contextPath = ServletActionContext.getServletContext()
				.getRealPath("/");
		Date d1 = new Date();
		String upfiesDir = dataPath + sep + "upfiles" + sep + "images" + sep;
		String destfilename1 = upfiesDir + DateUtils.dateStr2(d1) + sep
				+ user.getUser_id() + "_attestation" + "_" + d1.getTime()
				+ ".jpg";
		filePath = destfilename1;
		filePath = this.truncatUrl(filePath, contextPath);
		logger.info(destfilename1);
		File imageFile1 = null;
		try {
			imageFile1 = new File(destfilename1);
			FileUtils.copyFile(litpic, imageFile1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}
	
	//我的账户，重新激活邮箱
	public String email() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		String emailUrl = "/member/identify/email.html";
		user=userService.getDetailUser(user.getUser_id());
		request.setAttribute("user", user);
		if ("".equals(getActionType())) {
			return SUCCESS;
		}
		String emailStatus=StringUtils.isNull(user.getEmail_status());
		if(emailStatus.equals("1")||emailStatus.equals("2")){
			message("邮箱认证已经审核成功或正在审核中！","");
			return MSG;
		}
		
		String errormsg = "";
		if (email == null || email.equals("")) {
			message("email不能为空！", emailUrl);
			return MSG;
		} else {
			if (user.getEmail() == null || !user.getEmail().equals(email)) {
				user.setEmail(email);
				User newUser = userService.modifyEmail(user);
				if (newUser != null) {
					session.put(Constant.SESSION_USER, newUser);
					request.setAttribute("user", newUser);
				}
			} 
			try {
				//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
				//TODO RDPROJECT-314  DOING
				long user_id = user.getUser_id();
				//TODO active content
				Mail m = Mail.getInstance();
				String activeUrl = "/member/identify/active.html?id=" + m.getdecodeIdStr(user);
				Global.setTransfer("activeUrl", activeUrl);
				BaseAccountLog blog=new ReactiveUserEmailLog(user_id);
				blog.doEvent();
				//sendMail(user);
				//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
			} catch (Exception e) {
				logger.error(e.getMessage());
				errormsg = "发送激活邮件成功！";
				message(errormsg, emailUrl);
				return MSG;
			}
		}
		errormsg = "发送激活邮件成功！";
		message(errormsg, emailUrl);
		return MSG;
	}

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
			errormsg = "链接无效，请重新获取邮件！";
			request.setAttribute("errormsg", errormsg);
			return "success";
		}
		long activeTime = NumberUtils.getLong(idstrArr[2]);
		/*long activeTime =33422;*/
		if (System.currentTimeMillis() - activeTime * 1000 > 1 * 24 * 60
				* 60 * 1000) {
			errormsg = "链接有效时间1天，已经失效，请重新获取邮件!";
			request.setAttribute("errormsg", errormsg);
			return "success";
		}
		if (idstrArr.length < 1) {
			errormsg = "解析激活码失败，请联系管理员！";
			request.setAttribute("errormsg", errormsg);
			return "success";
		} else {
			String useridStr = idstrArr[0];
			user = (User) session.get(Constant.SESSION_USER);
			if (user == null) {
				long user_id = 0;
				try {
					user_id = Long.parseLong(useridStr);
				} catch (Exception e) {
					user_id = 0;
				}
				if (user_id > 0) {
					user = userService.getUserById(user_id);
				} else {
					errormsg = "解析激活码失败，请联系管理员！";
					request.setAttribute("errormsg", errormsg);
					return "success";
				}
			}
			long user_id = user.getUser_id();
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			if(user.getEmail_status()!=1){
				errormsg = "您已进行邮箱认证！";
				request.setAttribute("errormsg", errormsg);
				return "success";
			}
			if (useridStr.equals(user_id + "")) {
				user.setEmail_status(1);
				User newUser = userService.modifyEmail_status(user);
				logger.debug("IdentifyAction:重新激活邮箱操作active()：user_id:"+user.getUser_id()+", username:"+user.getUsername()+", email_status:"+user.getEmail_status());
				if (newUser != null) {
					//session.put(Constant.SESSION_USER, newUser);
				}
				msg = "邮箱激活成功！";
			} else {
				errormsg = "激活失败，请重新激活！";
			}
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
		}
		if (errormsg.equals("")) {

			request.setAttribute("msg", msg);
		} else {
			request.setAttribute("errormsg", errormsg);
		}
		return "success";
	}
	//短信手机验证
	public String phoneSms() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		String valicode = paramString("valicode");
		String code_number = (String) session.get("code_number");
		String errormsg = "";
		String phoneUrl = "/member/identify/phone.html";
		String phone=paramString("mobile");
		//短信认证积分
		CreditType emailType = userCreditService.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_PHONE.getValue());
		long creditValue = emailType.getValue();
		if (phone == null || phone.equals("")) {
			errormsg = "手机号码不能为空！";
			json(errormsg);
			return null;
		}
		// 一个用户只能使用唯一的手机号功能实现
		// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 start
		//boolean flag = checkOnlyPhone(user, phone);
		user.setPhone(phone);
		boolean flag = userService.isPhoneExist(user);
		// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 end
		if (!flag) {
			errormsg = "该手机号码已经被其他用户使用！";
			json(errormsg);
			return null;
		}
		long date = DateUtils.getTime(new Date());
		long preDate=0;
		Object object=session.get("nowdate");
		if(object==null){
			object="";
		}
		if(!"".equals(object)){
			preDate=Long.parseLong(object.toString());
		}
		/*if(session.get("nowdate") != null && (date - preDate) > 60){
			  session.remove(code_number); session.remove(preDate);
			  code_number=code(); 
		} */
		if (!StringUtils.isBlank(code_number)) {
			if (!code_number.equals(valicode)) {
				errormsg = "短信验证码不正确！,请查看是否过期或者输入错误";
				json(errormsg);
				return null;
			} else {
				user.setPhone(phone);
				// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
				user.setPhone_status(1);
				// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
				userService.modifyPhone(user);
				userCreditService.updateUserCredit(user.getUser_id(), (int)creditValue, new Byte(Constant.OP_ADD), EnumIntegralTypeName.INTEGRAL_PHONE.getValue());
				session.put(Constant.SESSION_USER, user);
				errormsg="短信验证成功";
				json(errormsg);
				// v1.6.7.2 RDPROJECT-633 lx 2013-12-27 start
				session.remove("code_number");
				session.remove("nowdate");
				// v1.6.7.2 RDPROJECT-633 lx 2013-12-27 end
				return null;
			}
		} else {
			User newuser = userService.getUserById(user.getUser_id());
			if (newuser != null && newuser.getReal_status()==1) {
				session.put(Constant.SESSION_USER, newuser);
			}else {
				errormsg="请先进行实名认证！";
				json(errormsg);
				return null;
			}
		}
		return null;
	}
	// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 start
//	/**
//	 * 一个用户只能使用唯一的手机号功能实现
//	 * @param newUser 用户实体
//	 * @param phone 用户手机号码
//	 * @return boolean
//	 */
//	private Boolean checkOnlyPhone(User newUser, String phone) {
//		// 一个用户只能使用唯一的手机号功能实现
//		newUser.setPhone(phone);
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
   //我的账号，手机认证
	public String phone() throws Exception {
		// String valicode=StringUtils.isNull(request.getParameter("valicode"));
		User newUser = null;
		String phoneUrl = "/member/identify/phone.html";
		User user = getSessionUser();
		String errormsg = "";
		if (isBlank()) {
			newUser = userService.getUserById(user.getUser_id());
			newUser.hideChar();
			request.setAttribute("user", newUser);
			return SUCCESS;
		}
		newUser = userService.getUserById(user.getUser_id());
		String phonelStatus = StringUtils.isNull(newUser.getPhone_status());
		if (phonelStatus.equals("1") || phonelStatus.equals("2")) {
			message("手机认证已经审核成功或正在审核中！", "");
			return MSG;
		}
		if (phone == null || phone.equals("")) {
			errormsg = "手机号码不能为空！";
			message(errormsg, phoneUrl);
			return MSG;
		}
		// 一个用户只能使用唯一的手机号功能实现		
		// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 start
//		boolean flag = checkOnlyPhone(newUser, phone);
		newUser.setPhone(phone);
		boolean flag = userService.isPhoneExist(newUser);
		// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 end
		if (!flag) {
			errormsg = "该手机号码已经被其他用户使用！";
			message(errormsg, phoneUrl);
			return MSG;
		}
		/*
		 * String code_number=(String) session.get("code_number"); long date =
		 * DateUtils.getTime(new Date()); long preDate =
		 * Long.parseLong(session.get("nowdate").toString());
		 * if(session.get("nowdate") != null && (date - preDate) > 60){
		 * session.remove(code_number); session.remove(preDate);
		 * code_number=code(); } if(!StringUtils.isBlank(code_number)){
		 * if(!code_number.equals(valicode)){ errormsg =
		 * "短信验证码不正确！,请查看是否过期或者输入错误"; message(errormsg, phoneUrl); return MSG;
		 * }else{ user.setPhone(phone); user.setPhone_status("1");
		 * userService.modifyPhone(user); session.put(Constant.SESSION_USER,
		 * user); message("手机认证成功！", phoneUrl); return MSG; } }else{
		 */
		User newuser = userService.getUserById(user.getUser_id());
	
		/*RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.NEWRECHARGE_ATTESTATION.getValue()));
		if(rule != null){
			if(rule.getValueIntByKey("realname_phone") == 1){*/
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.PHONE_REALNAME.getValue()));
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			if (rule.getValueIntByKey("is_enable") == 1) {
				if (!StringUtils.isNull(newuser.getReal_status()).equals("")
						&& StringUtils.isNull(newuser.getReal_status()).equals("1")) {
					user.setPhone(phone);
					// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
					user.setPhone_status(2);
					// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
					newuser = userService.modifyPhone(user);
					if (newuser != null) {
						session.put(Constant.SESSION_USER, newuser);
					}
					errormsg = "手机认证申请提交成功，等待管理员审核！";
					message(errormsg, phoneUrl);
					return MSG;
				} else {
					message("请先进行实名认证！", "/member/identify/realname.html");
					return MSG;
				}
			}
		}
		user.setPhone(phone);
		// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
		user.setPhone_status(2);
		// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
		newuser = userService.modifyPhone(user);
		if (newuser != null) {
			session.put(Constant.SESSION_USER, newuser);
		}
		errormsg = "手机认证申请提交成功，等待管理员审核！";
		message(errormsg, phoneUrl);
		/* } */
		
		
		return MSG;
	}
	
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
	public String mobileaccess() {
		User user = (User) session.get(Constant.SESSION_USER);
		String phone = paramString("mobile");
		long date = DateUtils.getTime(new Date());
		logger.info("当前系统时间" + date + "上一次获取时间" + session.get("nowdate"));
		String returnmessage = userService.phoneCode(phone, user, session, date);
		return jsonString(returnmessage);
	}
	
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


	private String truncatUrl(String old, String truncat) {
		String url = "";
		url = old.replace(truncat, "");
		url = url.replace(sep, "/");
		return url;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	//TODO RDPROJECT-314 DELETE

	/*private void sendMail(User user) throws Exception {
		String to = user.getEmail();
		Mail m = Mail.getInstance();
		m.setTo(to);
		m.readActiveMailMsg();
		m.replace(user.getUsername(), to, "/member/identify/active.html?id="
				+ m.getdecodeIdStr(user));
		logger.debug("Email_msg:" + m.getBody());
		m.sendMail();
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	
	public String scene() throws Exception {
		User user = getSessionUser();
		user=userService.getDetailUser(user.getUser_id());
		request.setAttribute("user", user);
		if (isBlank()) {
			return SUCCESS;
		}
		
		user.setScene_status(2);
		User newUser = userService.modifyScene_status(user);
		if (newUser != null) {
			session.put(Constant.SESSION_USER, newUser);
			request.setAttribute("user", newUser);
		}
		String msg = "现场认证申请提交成功，等待管理员审核！";
		message(msg,"/member/identify/scene.html");
		return MSG;
	}
	
	public String video() throws Exception {
		User user =(User) session.get(Constant.SESSION_USER);
		user=userService.getDetailUser(user.getUser_id());
		request.setAttribute("user", user);
		if (isBlank()) {
			return SUCCESS;
		}
		user.setVideo_status(2);
		User newUser = userService.modifyVideo_status(user);
		if (newUser != null) {
			UserCache userCache=userService.getUserCacheByUserid(user.getUser_id());
			request.setAttribute("user", newUser);
			request.setAttribute("cache", userCache);
		}
		String msg = "视频认证申请提交成功，等待管理员审核！";
		message(msg,"/member/identify/video.html");
		return MSG;
	} 
	
}
