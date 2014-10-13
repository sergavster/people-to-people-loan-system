package com.p2psys.web.action.admin;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.Attestation;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.domain.UserCredit;
import com.p2psys.domain.UserTrack;
import com.p2psys.domain.UserType;
import com.p2psys.model.AccountSumModel;
import com.p2psys.model.AttestationModel;
import com.p2psys.model.DetailUser;
import com.p2psys.model.IdentifySearchParam;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserinfoModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.noac.UserCertifyFailLog;
import com.p2psys.model.accountlog.noac.UserCertifySuccLog;
import com.p2psys.service.AccountService;
import com.p2psys.service.AccountSumService;
import com.p2psys.service.MessageService;
import com.p2psys.service.PasswordTokenService;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.tool.coder.MD5;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class UserInfoAction extends BaseAction implements ModelDriven<UserinfoModel> {
	private final static Logger logger = Logger.getLogger(UserInfoAction.class);
	private UserService userService;
	private UserinfoService userInfoService;
	private UserinfoModel UserinfoModel = new UserinfoModel();
	private AttestationModel AttestationModel = new AttestationModel();
	private String updatetype;
	private List<UserType> typeList;
	private UserType usertypes;

	// v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	// TODO RDPROJECT-314 DELETE
	// private Message message=new Message();
	// v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end

	private MessageService messageService;
	private AccountService accountService;
	private UserCreditService userCreditService;
	private AccountSumService accountSumService;
	// v1.6.6.2 RDPROJECT-315 gc 2013-10-28 start
	private PasswordTokenService passwordTokenService;

	public PasswordTokenService getPasswordTokenService() {
		return passwordTokenService;
	}

	public void setPasswordTokenService(PasswordTokenService passwordTokenService) {
		this.passwordTokenService = passwordTokenService;
	}

	// v1.6.6.2 RDPROJECT-315 gc 2013-10-28 end

	public String userinfolist() {
		int i = NumberUtils.getInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
		SearchParam searchParam = new SearchParam();
		String username = StringUtils.isNull(request.getParameter("username"));
		searchParam.setUsername(username);
		PageDataList pageDataList = userInfoService.getUserInfoListByPageNumber(i, searchParam);
		List list = pageDataList.getList();
		for (Object u : list) {
			((UserinfoModel) u).getAllStatus();
		}
		request.setAttribute("userInfoList", list);
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", searchParam.toMap());
		return "success";
	}

	public String index() {
		return "success";
	}

	public String search() {
		int page = NumberUtils.getInt(request.getParameter("page"));
		// 来源地址类型 , 用来区别个个模块
		String temp = request.getParameter("types") == null ? SUCCESS : request.getParameter("types");
		SearchParam searchParam = new SearchParam();
		PageDataList pageDataList;
		searchParam.setUsername(request.getParameter("username"));
		//
		if (temp != null && temp.indexOf("user") != -1) {
			// 用户管理模块
			searchParam.setRealname(request.getParameter("realname"));
			searchParam.setEmail(request.getParameter("email"));
			pageDataList = userInfoService.getSearchUserInfo(page, searchParam);
			request.setAttribute("detailuserlist", pageDataList.getList());
		} else {
			// 客户信息管理模块
			pageDataList = userInfoService.getSearchUserInfo(searchParam, page);
			for (Object u : pageDataList.getList()) {
				((UserinfoModel) u).getAllStatus();
			}
			request.setAttribute("userInfoList", pageDataList.getList());
		}
		searchParam.addMap("types", temp);
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", searchParam.toMap());
		return temp;
	}

	public String userAttestation() {
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam searchParam = new SearchParam();
		searchParam.setUsername(request.getParameter("username"));
		searchParam.setRealname(request.getParameter("realname"));
		String vip_status = StringUtils.isNull(request.getParameter("vip_status"));
		String real_status = StringUtils.isNull(request.getParameter("real_status"));
		String email_status = StringUtils.isNull(request.getParameter("email_status"));
		String phone_status = StringUtils.isNull(request.getParameter("phone_status"));
		searchParam.setVip_status(vip_status);
		searchParam.setReal_status(real_status);
		searchParam.setEmail_status(email_status);
		searchParam.setPhone_status(phone_status);
		String audit_user = request.getParameter("audit_user");
		String type = StringUtils.isNull(request.getParameter("type"));
		logger.debug("audit=====" + audit_user);
		if (!StringUtils.isBlank(audit_user)) {
			User user = userService.getUserByName(request.getParameter("audit_user"));
			int userid = (int) user.getUser_id();
			searchParam.setAudit_user(String.valueOf(userid));
			logger.debug("audit=====" + user.getUser_id());
		}
		PageDataList pageDataList = userInfoService.getSearchUserInfo(page, searchParam);
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		List list = userService.getKfList(2);
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		request.setAttribute("kflist", list);
		request.setAttribute("detailuserlist", pageDataList.getList());
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", searchParam.toMap());

		if (type.isEmpty()) {
			return SUCCESS;
		} else {

			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "attestation_" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "data/export/" + downloadFile;
			List userList = userService.getUserList(searchParam);
			String[] names = new String[] { "user_id", "username", "realname", "real_status", "email_status",
					"phone_status", "vip_status", "addtime", "vip_verify_time" };
			String[] titles = new String[] { "用户ID", "用户名", "真实姓名", "实名认证", "邮箱认证", "手机认证", "是否vip", "注册时间", "VIP审核时间" };

			try {
				ExcelHelper.writeExcel(infile, userList, DetailUser.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	public String user() {
	  //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		List kflist = userService.getKfList(2);
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		request.setAttribute("kflist", kflist);
		// 获取用户所有类型
		List userTypeList = userService.getAllUserType();
		request.setAttribute("userTypeList", userTypeList);
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String email = StringUtils.isNull(request.getParameter("email"));
		String realname = StringUtils.isNull(request.getParameter("realname"));
		String type = StringUtils.isNull(request.getParameter("type"));
		String kefu_username = StringUtils.isNull(request.getParameter("kefu_username"));
		String card_id = StringUtils.isNull(request.getParameter("card_id"));
		String userPhone = StringUtils.isNull(request.getParameter("userphone"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		String qq = StringUtils.isNull(request.getParameter("qq"));
		String phone = StringUtils.isNull(request.getParameter("phone"));
		String user_type = StringUtils.isNull(request.getParameter("user_type"));
		SearchParam param = new IdentifySearchParam();
		param.setTypename(user_type);
		param.setPhone(phone);
		param.setUsername(username);
		param.setCard_id(card_id);
		param.setEmail(email);
		param.setRealname(realname);
		param.setKefu_username(kefu_username);
		param.setUserPhone(userPhone);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setQq(qq);

		PageDataList pageDataList = userService.getUserList(page, param);
		this.setPageAttribute(pageDataList, param);
		if (type.isEmpty()) {
			return SUCCESS;
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			logger.debug("path===" + contextPath);
			String downloadFile = "user_" + System.currentTimeMillis() + ".xls";
			// v1.6.7.2 RDPROJECT-499 sj 2013-12-4 start
			String infile = contextPath + "\\data\\export\\" + downloadFile;
			String[] names = new String[] { "user_id", "username", "realname", "sex", "email", "qq", "phone",
					"provincetext", "citytext", "areatext", "card_id", "addtime", "status", "type_id", "kefu_username","lastip" };
			String[] titles = new String[] { "ID", "用户名称", "真实姓名", "性别	", "邮箱", "qq", "手机", "省", "市", "县", "身份证号",
					"添加时间", "状态", "用户类型", "所属客服","登录IP" };
			// v1.6.7.2 RDPROJECT-499 sj 2013-12-4 end
			List list = userService.getUserList(param);
			try {
				ExcelHelper.writeExcel(infile, list, DetailUser.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
	}

	public String viewuserAttestation() {
		UserinfoModel userinfoModel = userInfoService.getUserALLInfoModelByUserid(UserinfoModel.getUser_id());
		userinfoModel.getAllStatus();
		UserTrack t = userService.getLastUserTrack(UserinfoModel.getUser_id());
		int count = userService.userTrackCount(UserinfoModel.getUser_id());
		request.setAttribute("user", userinfoModel);
		request.setAttribute("userLoginCount", count);
		request.setAttribute("track", t);
		return "success";
	}

	public String userCertify() {
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam searchParam = new SearchParam();
		searchParam.setUsername(request.getParameter("username"));
		searchParam.setRealname(request.getParameter("realname"));
		PageDataList pageDataList = userInfoService.getSearchUserCertify(page, searchParam);

		request.setAttribute("userCertifyList", pageDataList.getList());
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", searchParam.toMap());
		return SUCCESS;
	}

	public String verifyUserCertify() {
		int pid = NumberUtils.getInt(request.getParameter("pid"));
		long user_id = NumberUtils.getLong(request.getParameter("user_id"));
		int status = NumberUtils.getInt(request.getParameter("status"));
		int value = NumberUtils.getInt(request.getParameter("jifen"));
		String type_name = request.getParameter("type_name");
		String verifyRemark = request.getParameter("verify_remark");
		String actionType = request.getParameter("actionType");
		String msg = "审核成功！";
		UserCredit userCredit = userInfoService.getUserCreditByUserId(user_id);
		userCredit.setValue(userCredit.getValue() + value);
		Attestation userAttestation = new Attestation();

		User auth_user = (User) session.get(Constant.AUTH_USER);
		if (!StringUtils.isBlank(actionType)) {
			String validcode = request.getParameter("validcode");
			if (validcode.isEmpty()) {
				message("你输入的校验码为空！", "/admin/attestation/verifyScene.html");
				return ADMINMSG;
			}
			if (!this.checkValidImg(validcode)) {
				message("你输入的校验码错误！", "/admin/attestation/verifyScene.html");
				return ADMINMSG;
			}
			// v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
			// TODO RDPROJECT-314 DONE

			if (status == 1) {
				userCreditService.updateUserCredit(user_id, value, new Byte(Constant.OP_ADD),
						EnumIntegralTypeName.INTEGRAL_CERTIFICATE.getValue());
				userAttestation.setId(pid);
				userAttestation.setStatus(1);
				userAttestation.setVerify_remark(verifyRemark);
				userAttestation.setVerify_time(getTimeStr());
				userInfoService.modifyUserCertifyStatus(userAttestation);

				/*
				 * message(msg, "/admin/attestation/userCertify.html"); message = getSiteMessage(auth_user.getUser_id(),
				 * "客户证明资料认证审核成功！", "客户证明资料-"+type_name+",认证审核成功！", user_id); messageService.addMessage(message);
				 */
				Account act = accountService.getAccount(user_id);
				Global.setTransfer("type", type_name);
				BaseAccountLog blog = new UserCertifySuccLog(0, act, user_id);
				blog.doEvent();
				message("审核成功！", "/admin/attestation/userCertify.html");
				return ADMINMSG;
			} else {
				userAttestation.setId(pid);
				userAttestation.setStatus(3);
				userAttestation.setVerify_remark(verifyRemark);
				userAttestation.setVerify_time(getTimeStr());
				userInfoService.modifyUserCertifyStatus(userAttestation);
				Account act = accountService.getAccount(user_id);
				Global.setTransfer("type", type_name);
				BaseAccountLog blog = new UserCertifyFailLog(0, act, user_id);
				blog.doEvent();
				message("审核不通过！", "/admin/attestation/userCertify.html");
				/*
				 * message("审核没有通过！", "/admin/attestation/userCertify.html"); message =
				 * getSiteMessage(auth_user.getUser_id(), "客户证明资料认证审核失败！",
				 * "客户证明资料-"+type_name+",认证审核失败！</br>"+"审核信息："+verifyRemark, user_id);
				 * messageService.addMessage(message);
				 */
				return ADMINMSG;
			}
			// v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end

		}

		return SUCCESS;

	}

	/*
	 * public String viewUserCertify(){ AttestationModel attestationModel =
	 * userInfoService.getUserAllCertifyByUserId(AttestationModel.getUser_id()); request.setAttribute("userCertify",
	 * attestationModel); return SUCCESS; }
	 */

	// 显示用户积分
	public String userCredit() {
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam searchParam = new SearchParam();
		searchParam.setUsername(request.getParameter("username"));
		searchParam.setRealname(request.getParameter("realname"));
		PageDataList pageDataList = userInfoService.getUserCreditList(page, searchParam);

		request.setAttribute("userCreditList", pageDataList.getList());
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", searchParam.toMap());
		return SUCCESS;
	}

	public String viewUserCreditDetail() {

		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam searchParam = new SearchParam();
		PageDataList pageDataList = userInfoService.getCreditLogList(page, searchParam);

		request.setAttribute("ucLog", pageDataList.getList());
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", searchParam.toMap());

		return SUCCESS;
	}

	public String editUserCredit() {
		return SUCCESS;
	}

	public String edituserinfo() {
		UserinfoModel userinfoModel = userInfoService.getUserALLInfoModelByUserid(UserinfoModel.getUser_id());
		userinfoModel.getAllStatus();
		// System.out.println(userinfoModel.getAddress());
		request.setAttribute("user", userinfoModel);
		return SUCCESS;
	}

	public String updateuser() {
		int i = NumberUtils.getInt(updatetype);
		switch (i) {
			case 0: {
				// userInfoService.updateUserinfo(UserinfoModel);
			}
				break;
			case 1: {
				userInfoService.updateUserinfo(UserinfoModel);
			}
				break;
			case 2: {
				userInfoService.updateBuilding(UserinfoModel);
			}
				break;
			case 3: {
				userInfoService.updateCompany(UserinfoModel);
			}
				break;
			case 4: {
				userInfoService.updateFirm(UserinfoModel);
			}
				break;
			case 5: {
				userInfoService.updateFinance(UserinfoModel);
			}
				break;
			case 6: {
				userInfoService.updateContact(UserinfoModel);
			}
				break;
			case 7: {
				userInfoService.updateMate(UserinfoModel);
			}
				break;
			case 8: {
				userInfoService.updateEducation(UserinfoModel);
			}
				break;
			case 9: {
				userInfoService.updateOtherinfo(UserinfoModel);
			}
				break;
		}

		UserinfoModel userinfoModel = userInfoService.getUserALLInfoModelByUserid(UserinfoModel.getUser_id());
		userinfoModel.getAllStatus();
		request.setAttribute("user", userinfoModel);

		return SUCCESS;
	}

	public String adduser() {
	  //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		request.setAttribute("kf", userService.getKfList(2));
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		return SUCCESS;
	}

	public String edituser() {
		request.setAttribute("user", userService.getDetailUser(UserinfoModel.getUser_id()));
		request.setAttribute("kefu", userService.getUserCacheByUserid(UserinfoModel.getUser_id()));
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		List kfList = userService.getKfList(2);
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		request.setAttribute("kfList", kfList);
		return SUCCESS;
	}

	public String typelist() {
		request.setAttribute("usertype", userService.getAllUserType());
		return SUCCESS;
	}

	public String typeyupdateoradd() {
		String temp = request.getServletPath();
		if (temp.indexOf("typeupdate") != -1) {
			userService.updateUserTypeByList(typeList);
		} else {
			if (temp.indexOf("typeadd") != -1) {
				usertypes.setAddip(getRequestIp());
				usertypes.setAddtime(getTimeStr());
				userService.addUserType(usertypes);
			} else {
				if (temp.indexOf("typedelete") != -1) {
					userService.deleteUserTypeById(NumberUtils.getLong(request.getParameter("type_id").toString()));
				}

			}
		}
		return SUCCESS;
	}

	public String viewTenderValue() {
		long user_id = paramLong("user_id");
		UserCredit credit = userInfoService.getCreditModelById(user_id);
		request.setAttribute("credit", credit);
		return SUCCESS;
	}

	public String editTenderValue() {
		if (checkValidImg(this.paramString("valid"))) {
			message("验证码不对！", "");
			return ADMINMSG;
		}
		int opvalue = paramInt("opvalue");
		long user_id = paramLong("user_id");
		UserCredit uc = userInfoService.getUserCreditByUserId(user_id);
		if (uc == null) {
			message("ID：" + user_id + "用户不存在！", "");
			return ADMINMSG;
		}
		uc.setTender_value(uc.getTender_value() + opvalue);
		// userInfoService.updateCreditTenderValue(uc);
		message("操作成功！", "");
		return ADMINMSG;
	}

	public String remindVipBirth() {
		int page = paramInt("page");
		String username = request.getParameter("username");
		SearchParam param = new SearchParam();
		param.setUsername(username);
		Calendar cal = Calendar.getInstance();
		String nowStr = DateUtils.dateStr8(cal.getTime());
		String endStr = DateUtils.dateStr8(DateUtils.rollDay(cal.getTime(), 3));
		PageDataList plist = userService.getUserWithBirth(nowStr, endStr, param, page);
		request.setAttribute("username", username);
		request.setAttribute("list", plist.getList());
		request.setAttribute("page", plist.getPage());
		request.setAttribute("param", param.toMap());
		return SUCCESS;
	}

	// v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	// TODO RDPROJECT-314 DELETE

	/*
	 * private Message getSiteMessage(long receive_user,String title,String content,long sent_user){
	 * message.setSent_user(receive_user); message.setReceive_user(sent_user); message.setStatus(0);
	 * message.setType(Constant.SYSTEM); message.setName(title); message.setContent(content);
	 * message.setAddip(getRequestIp()); message.setAddtime(getTimeStr()); return message; }
	 */
	// v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end

	@Override
	public UserinfoModel getModel() {
		return UserinfoModel;
	}

	public String onlinekefu() {
		// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 start
		int page = paramInt("page");
		SearchParam param = new SearchParam();
		PageDataList plist = userService.getKfList(param, page);
		setPageAttribute(plist, param);
		return SUCCESS;
		// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 end
	}

	public String delOnlineKf() {
		long id = NumberUtils.getLong(request.getParameter("id"));
		// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 start
		User onlineKf=new User();
		onlineKf.setUser_id(id);
		onlineKf.setType_id(2);
		userService.modifyUserType(onlineKf);
		// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 end
		//userService.deleteOnlineKfById(id);
		context = ServletActionContext.getServletContext();
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		context.setAttribute("kefuList", userService.getKfList(2));
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		message("操作成功！", "/admin/userinfo/onlinekefu.html");
		return ADMINMSG;
	}

	public String modifyOnlineKf() {
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		long user_id = NumberUtils.getLong(request.getParameter("id"));
		if (!StringUtils.isBlank(actionType)) {
			User kf = userService.getUserById(user_id);
			String username = request.getParameter("username");
			// v1.6.7.2 RDPROJECT-543 lx 2013-12-10 start
			boolean result = userService.checkUsername(username);
			if (!result && !username.equals(kf.getUsername())) {
				message("用户名重复，请更换用户名!", "/admin/userinfo/onlinekefu.html");
				return ADMINMSG;
			}
			// v1.6.7.2 RDPROJECT-543 lx 2013-12-10 end
			kf.setUsername(username);
			String qq = request.getParameter("qq");
			kf.setQq(qq);
			if (username.isEmpty()) {
				message("用户名不能为空!", "/admin/userinfo/onlinekefu.html");
				return ADMINMSG;
			}
			if (qq.isEmpty()) {
				message("QQ号不能为空!", "/admin/userinfo/onlinekefu.html");
				return ADMINMSG;
			}
			userService.modifyOnlineKf(kf);
			context = ServletActionContext.getServletContext();
			//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
			context.setAttribute("kefuList", userService.getKfList(2));
			//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
			message("修改成功！", "/admin/userinfo/onlinekefu.html");
			return ADMINMSG;
		}
		User kf = userService.getUserById(user_id);
		request.setAttribute("kf", kf);
		return SUCCESS;
	}

	// public String addOnlineKf(){
	// String actionType = StringUtils.isNull(request.getParameter("actionType"));
	// if(!StringUtils.isBlank(actionType)){
	// User kf = new User();
	// kf.setUsername(request.getParameter("username"));
	// kf.setQq(request.getParameter("qq"));
	// kf.setType_id(3);
	// kf.setAddtime(DateUtils.getNowTimeStr());
	// userService.addUser(kf);
	// message("添加成功！","/admin/userinfo/onlinekefu.html");
	// return ADMINMSG;
	// }
	// return SUCCESS;
	// }

	/**
	 * 用户资金合计分页
	 * 
	 * @return string
	 * @throws Exception e
	 */
	public String accountSumLogPage() throws Exception {
		String type = StringUtils.isNull(request.getParameter("type"));
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam searchParam = new SearchParam();
		String userName = request.getParameter("username");
		if (userName != null && userName.length() > 0) {
			searchParam.setUsername(userName);
		}
		PageDataList pageDataList = accountSumService.getAccountSumPage(page, searchParam);
		request.setAttribute("accountSumList", pageDataList.getList());
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", searchParam.toMap());
		// v1.6.7.1 用户资金统计加导出 zza 2013-11-26 start
		if (type.isEmpty()) {
			return SUCCESS;
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "sumLogDetail_" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			String[] names = new String[] { "user_id", "username", "recharge", "used_recharge", "award", "used_award",
					"interest", "used_interest", "borrow_cash", "used_borrow_cash", "cash", "cash_fee", "huikuan",
					"used_huikuan", "interest_fee", "deduct", "repay_cash" };
			String[] titles = new String[] { "用户ID", "用户名", "充值统计", "已使用充值统计", "奖励统计", "已使用奖励统计", "利息统计", "已使用利息统计",
					"借款入账统计", "已使用利息统计", "提现统计", "提现手续费统计", "回款统计", "已使用回款统计", "利息手续费统计", "扣费统计", "已还款统计" };
			List<AccountSumModel> list = accountSumService.getAccountSum(searchParam);
			ExcelHelper.writeExcel(infile, list, AccountSumModel.class, Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
		// v1.6.7.1 用户资金统计加导出 zza 2013-11-26 end
	}

	/**
	 * 用户资金合计日志
	 * 
	 * @return
	 * @throws Exception
	 */
	public String sumLogDetail() throws Exception {
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam searchParam = new SearchParam();
		long userId = NumberUtils.getLong(request.getParameter("user_id"));
		if (userId > 0) {
			User user = userService.getUserById(userId);
			request.setAttribute("sumUser", user);
			PageDataList pageDataList = accountSumService.getAccountSumLogPage(page, userId, searchParam);
			request.setAttribute("accountSumLogList", pageDataList.getList());
			request.setAttribute("page", pageDataList.getPage());
			request.setAttribute("params", searchParam.toMap());
			request.setAttribute("user_id", userId);
		}
		return SUCCESS;
	}

	// v1.6.6.2 RDPROJECT-315 gc 2013-10-28 start
	/**
	 * @return
	 */
	public String passwordToken() {
		int page = paramInt("page");
		String username = StringUtils.isNull(request.getParameter("username"));
		SearchParam param = new SearchParam();
		param.setUsername(username);
		PageDataList plist = passwordTokenService.getPasswordTokenList(param, page);
		request.setAttribute("list", plist.getList());
		request.setAttribute("page", plist.getPage());
		request.setAttribute("param", param.toMap());
		return SUCCESS;
	}

	// v1.6.6.2 RDPROJECT-315 gc 2013-10-28 end

	// v1.6.7.1 安全优化 sj 2013-11-26 start
	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public String modifyPwd() {
		String actionType = paramString("actionType");
		User user = (User) session.get(Constant.AUTH_USER);
		request.setAttribute("username", user.getUsername());
		request.setAttribute("password", user.getPassword());
		request.setAttribute("user_id", user.getUser_id());
		if (!StringUtils.isBlank(actionType)) {
			String pwd = UserinfoModel.getPassword();
			if(!new MD5().getMD5ofStr(pwd).equals(user.getPassword())){
				message("原密码错误！","/admin/modifyPwd.html");
				return ADMINMSG;
			}
			User u = new User();
			u.setUsername(user.getUsername());
			u.setFirstpwd(UserinfoModel.getFirstpwd());
			userService.modifypassword(u);
			// 修改后更新dw_user_cache表的时间
			UserCache userCache = new UserCache();
			userCache.setPwd_modify_time(Long.parseLong(this.getTimeStr()));
			userCache.setUser_id(user.getUser_id());
			userService.updateUserCacheTime(userCache);

			message("密码修改成功！", "/admin/system/welcome.html");
			return ADMINMSG;
		}

		return SUCCESS;
	}

	// v1.6.7.1 安全优化 sj 2013-11-26 end
	
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 start
	/**
	 * 好友邀请统计报表
	 * @return
	 */
	public String invitationUsers(){
		String type=StringUtils.isNull(request.getParameter("exportType"));
		int page=NumberUtils.getInt(request.getParameter("page"));
		String invite_username=StringUtils.isNull(request.getParameter("invite_username"));
		String username=StringUtils.isNull(request.getParameter("username"));
		String startTime=StringUtils.isNull(request.getParameter("invite_startTime"));
		String endTime=StringUtils.isNull(request.getParameter("invite_endTime"));
		try{
			SearchParam param=new SearchParam();
			param.setUsername(username);
			param.setInvite_username(invite_username);
			if(!StringUtils.isEmpty(startTime)){
				startTime=startTime.substring(0,10);
				startTime+=" 00:00:00";
			}
			param.setInvite_startTime(startTime);
			if(!StringUtils.isEmpty(endTime)){
				endTime=endTime.substring(0,10);
				endTime+=" 23:59:59";
			}
			param.setInvite_endTime(endTime);
			if(type.isEmpty()){
				PageDataList plist=userService.getInviteUserList(page, param);
				for (DetailUser user :(List<DetailUser>)plist.getList()) {
					 user.setPerferTotalMoney(userService.perferTotalMoney(user.getUser_id()+""));
				}
				setPageAttribute(plist, param);
				request.setAttribute("listsize", userService.getInviteUserCount(param));
				return SUCCESS;
			}else{
				String contextPath = ServletActionContext.getServletContext().getRealPath("/");
				String downloadFile="invitation_"+System.currentTimeMillis()+".xls";
				String infile=contextPath+"/data/export/"+downloadFile;
				String[] names=new String[]{"invite_username","username","addtime","perferTotalMoney"};
				String[] titles=new String[]{"邀请人","被邀请人","邀请时间","已优惠利息管理费总额"};
				List<DetailUser> list=userService.getInviteUserList(param);
				for (DetailUser user :list) {
					String money=userService.perferTotalMoney(user.getUser_id()+"");
					if(!StringUtils.isEmpty(money)){
					 user.setPerferTotalMoney(money+"元");
					}else{
						 user.setPerferTotalMoney("0元");
					}
				}
				ExcelHelper.writeExcel(infile, list, DetailUser.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 end

	public void setUserCreditService(UserCreditService userCreditService) {
		this.userCreditService = userCreditService;
	}

	public AccountService getAccountServiceu() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	// v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	// TODO RDPROJECT-314 DELETE

	/*
	 * public Message getMessage() { return message; } public void setMessage(Message message) { this.message = message;
	 * }
	 */

	// v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public AttestationModel getAttestationModel() {
		return AttestationModel;
	}

	public void setAttestationModel(AttestationModel AttestationModel) {
		this.AttestationModel = AttestationModel;
	}

	public UserType getUsertypes() {
		return usertypes;
	}

	public void setUsertypes(UserType usertypes) {
		this.usertypes = usertypes;
	}

	public String getUpdatetype() {
		return updatetype;
	}

	public List<UserType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<UserType> typeList) {
		this.typeList = typeList;
	}

	public void setUpdatetype(String updatetype) {
		this.updatetype = updatetype;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserinfoService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(UserinfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	public void setAccountSumService(AccountSumService accountSumService) {
		this.accountSumService = accountSumService;
	}

}
