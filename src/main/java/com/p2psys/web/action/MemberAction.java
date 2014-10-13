package com.p2psys.web.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.common.enums.EnumWatermarkPlace;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Areainfo;
import com.p2psys.domain.Article;
import com.p2psys.domain.AutoTenderOrder;
import com.p2psys.domain.PasswordToken;
import com.p2psys.domain.Rule;
import com.p2psys.domain.Site;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAmount;
import com.p2psys.domain.UserCache;
import com.p2psys.domain.UserCredit;
import com.p2psys.domain.UserTrack;
import com.p2psys.domain.Userinfo;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.DetailUser;
import com.p2psys.model.RuleModel;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.UserCacheModel;
import com.p2psys.model.UserinfoModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.ArticleService;
import com.p2psys.service.AutoBorrowService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.FriendService;
import com.p2psys.service.MessageService;
import com.p2psys.service.PasswordTokenService;
import com.p2psys.service.RuleService;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.ImageUtil;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

public class MemberAction extends BaseAction implements ModelDriven<UserinfoModel> {

	private static Logger logger = Logger.getLogger(MemberAction.class);

	private UserService userService;
	private UserinfoService userinfoService;
	private AccountService accountService;
	private ArticleService articleService;
	private FriendService friendService;
	private MessageService messageService;
	private BorrowService borrowService;
	private AutoBorrowService autoBorrowService;
	private RuleService ruleService;

	// 用户积分service
	private UserCreditService userCreditService;
	
	@Resource
	private PasswordTokenService passwordTokenService;
	
	private String valicode;

	public RuleService getRuleService() {
		return ruleService;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	public String getValicode() {
		return valicode;
	}

	public void setValicode(String valicode) {
		this.valicode = valicode;
	}

	public AutoBorrowService getAutoBorrowService() {
		return autoBorrowService;
	}

	public void setAutoBorrowService(AutoBorrowService autoBorrowService) {
		this.autoBorrowService = autoBorrowService;
	}

	private UserinfoModel userinfo = new UserinfoModel();

	private String sep = File.separator;

	@Override
	public UserinfoModel getModel() {
		return userinfo;
	}

	public String index() throws Exception {
		try {
			User user = (User) session.get(Constant.SESSION_USER);
			if (user == null) {
				return "login";
			}
			long user_id = user.getUser_id();
			// v1.6.7.2 RDPROJECT-505 zza 2013-12-26 start
			RuleModel tokenRule = new RuleModel(Global.getRule(EnumRuleNid.SAFETY.getValue()));
			if (tokenRule != null && tokenRule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
				if (tokenRule.getValueIntByKey("password_token") == 1) {
					List<PasswordToken> tokenList = passwordTokenService.getPasswordTokenByUserId(user_id);
					boolean isToken = false;
					if (tokenList == null || tokenList.isEmpty()) {
						isToken = true;
					}
					request.setAttribute("isToken", isToken);
				}
			}
			// v1.6.7.2 RDPROJECT-505 zza 2013-12-26 end
			// 获取用户上一次登录记录
			UserTrack userTrack = userService.getLastUserTrack(user_id);
			request.setAttribute("userTrack", userTrack);
			DetailUser detailUser = userService.getDetailUser(user_id);
			//vip赠送规则
			Rule rule = Global.getRule(EnumRuleNid.RECHARGE_VIP_GIVETIME.getValue());
			if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
				UserCache userCache = userService.getUserCacheByUserid(user_id, 0, 2);
				request.setAttribute("userCache", userCache);
			}
			UserCacheModel cache = userService.getUserCacheByUserid(user_id);
			if (Global.getWebid().equals("wzdai") || Global.getWebid().equals("hndai")) {
				try {
					if (cache.getVip_verify_time() != null && cache.getVip_status() != 2) {
						int vipEndTime = (Integer.parseInt(cache.getVip_verify_time()) + 365 * 24 * 60 * 60);
						Date date = Calendar.getInstance().getTime();
						int today = Integer.parseInt(DateUtils.getTimeStr(date));
						if (vipEndTime - today < 0) {
							cache = userService.validUserVip(user_id);
						}
					}
				} catch (Exception e) {
					logger.error(e);
				}
			}
			UserAccountSummary summary = accountService.getUserAccountSummary(user_id);
			UserAmount amount = accountService.getUserAmount(user_id);
			Account account = accountService.getAccount(user_id);
			User kf = accountService.getKf(user_id);
			double sum = accountService.getAwardSum(user_id); // 奖励统计
			UserCredit uc = userinfoService.getCreditModelById(user_id);
			Userinfo userinfo = userinfoService.getUserinfoByUserid(user_id);
			int unreadmsg = messageService.getUnreadMesage(user_id);
			int friendrequests = friendService.countFriendsRequest(user_id);
			// double investInterest=accountService.getInvestInterestSum(user_id); //已赚利息统计
			// double yes_interest=investInterest-summary.getRepayInterest();
			// 获取各种资料的完整性
			if (userinfo != null) {
				userinfo.getAllStatus();
			}
			// 该用户自动投标排名
			AutoTenderOrder autoTenderOrder = autoBorrowService.getAutoTenderOrderByUserid(user.getUser_id());
			if (autoTenderOrder != null) {
				request.setAttribute("autoTenderOrder", autoTenderOrder);
			}
			List logList = accountService.getAccountLogList(user_id);
			UserCredit userCredit = userCreditService.getUserCreditByUserId(user_id);
			request.setAttribute("userCredit", userCredit);
			request.setAttribute("logList", logList);
			request.setAttribute("credit", uc);
			request.setAttribute("detailuser", detailUser);
			request.setAttribute("cache", cache);
			request.setAttribute("summary", summary);
			request.setAttribute("amount", amount);
			request.setAttribute("account", account);
			request.setAttribute("kf", kf);
			request.setAttribute("userinfo", userinfo);
			request.setAttribute("sum", sum); // 奖励统计
			request.setAttribute("unreadmsg", unreadmsg);
			request.setAttribute("friendrequests", friendrequests);
			// request.setAttribute("yes_interest", yes_interest);//已赚利息统计
			request.setAttribute("nid", "member");

			// V1.6.6.2 RDPROJECT-179 ljd 2013-10-18 start
			/*// 网站公告
			List<Article> ggList = articleService.getList("22", 0, 5);
			request.setAttribute("ggList", ggList);
			// 媒体报道
			List<Article> bdList = articleService.getList("59", 0, 5);
			request.setAttribute("bdList", bdList);

			// 常见问题
			List<Article> cjList = articleService.getList("98", 0, 5);
			request.setAttribute("cjList", cjList);

			// 正在投资明细列表
			List investList = borrowService.getInvestList(user_id);
			request.setAttribute("investList", investList);
			int page = NumberUtils.getInt(Global.getValue("index_other_num"));
			page = page > 0 ? page : 5;

			// 网站公告
			List<Article> tsrzxm = articleService.getList("122", 0, page);
			request.setAttribute("tsrzxm", tsrzxm);
			// 公司动态
			List<Article> jrzx = articleService.getList("126", 0, page);
			request.setAttribute("jrzx", jrzx);*/
			/* 
			// 网站公告
			List<Article> notice = articleService.getListByNid("notice", 0, 5);
			request.setAttribute("notice", notice);
			// 媒体报道
			List<Article> mtbd = articleService.getListByNid("mtbd", 0, 5);
			request.setAttribute("mtbd", mtbd);

			// 常见问题
			List<Article> cjwt = articleService.getListByNid("cjwt", 0, 5);
			request.setAttribute("cjwt", cjwt);

			// 正在投资明细列表
			List investList = borrowService.getInvestList(user_id);
			request.setAttribute("investList", investList);
			int page = NumberUtils.getInt(Global.getValue("index_other_num"));
			page = page > 0 ? page : 5;

			// 发标公告
			List<Article> fbgg = articleService.getListByNid("fbgg", 0, page);
			request.setAttribute("fbgg", fbgg);
			// 公司动态
			List<Article> gsdt = articleService.getListByNid("gsdt", 0, page);
			request.setAttribute("gsdt", gsdt);*/
			// V1.6.6.2 RDPROJECT-179 ljd 2013-10-18 end
			
			// V1.6.7.1 RDPROJECT-418 liukun 2013-11-11 start
			
			/*RuleModel acRule = new RuleModel(Global.getRule(EnumRuleNid.AC_RIGHT_SIDE_CONF.getValue()));
			String acConfStr = null;
			if (acRule != null && acRule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
				acConfStr = acRule.getRule_check();
			}
			int page = NumberUtils.getInt(Global.getValue("index_other_num"));
			page = page > 0 ? page : 5;
			ArrayList siteArticleList = new ArrayList();
			try {
				JSONObject jsonConf = new JSONObject(acConfStr);
				
				com.p2psys.util.json.JSONArray siteNids = (com.p2psys.util.json.JSONArray)jsonConf.get("siteNids");
				for (int i = 0 ; i < siteNids.length(); i++) {
					String siteNid = siteNids.get(i).toString();
					HashMap saHm = new HashMap();
					Site site = articleService.getSiteByNid(siteNid.toString());
					saHm.put("site", site);
					List<Article> aList = new ArrayList<Article>();
					aList = articleService.getListByNid(siteNid.toString(), 0, page);
					saHm.put("aList", aList);
					siteArticleList.add(saHm);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("saList", siteArticleList);*/
			
			// v1.6.7.2 改用FastJson xx 2013-12-12 start
			int page = NumberUtils.getInt(Global.getValue("index_other_num"));
			page = page > 0 ? page : 5;
			RuleModel acRule = new RuleModel(Global.getRule(EnumRuleNid.AC_RIGHT_SIDE_CONF.getValue()));
			if (acRule != null && acRule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
				ArrayList siteArticleList = new ArrayList();
				try {
					JSONArray siteNids = JSONArray.parseArray(acRule.getValueStrByKey("siteNids"));
					for (int i = 0 ; i < siteNids.size(); i++) {
						String siteNid = siteNids.get(i).toString();
						HashMap saHm = new HashMap();
						Site site = articleService.getSiteByNid(siteNid.toString());
						saHm.put("site", site);
						List<Article> aList = new ArrayList<Article>();
						aList = articleService.getListByNid(siteNid.toString(), 0, page);
						saHm.put("aList", aList);
						siteArticleList.add(saHm);
					}
				} catch (Exception e) {
					logger.error(e);
				}
				request.setAttribute("saList", siteArticleList);
				// V1.6.7.1 RDPROJECT-418 liukun 2013-11-11 end
			}
			// v1.6.7.2 改用FastJson xx 2013-12-12 end
			
			// 信达财富网站规则简版
			List<Article> rzxm = articleService.getList("128", 0, page);
			request.setAttribute("rzxm", rzxm);
			// 正在借款明细列表

			List borrowList = borrowService.getBorrowList(user_id);
			request.setAttribute("borrowList", borrowList);
			// 晋商贷账户中心网站规则
			List<Article> wzgz = articleService.getList("11", 0, page);
			request.setAttribute("wzgz", wzgz);
		} catch (Exception e) {
			logger.error(e);
		}
		return "success";
	}

	public String userinfo() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();

		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateUserinfo(this.userinfo);
			// 读取更新后的个人资料

			info = userinfoService.getUserinfoByUserid(user_id);
			User updateuser = userService.getUserByName(user.getUsername());
			if (updateuser != null) {
				updateuser.hideChar();
			}
			session.put(Constant.SESSION_USER, updateuser);
			String msg = "修改个人信息成功！";
			String errormsg = checkUserinfo();
			if (errormsg.equals("")) {
				request.setAttribute("msg", msg);
			} else {
				request.setAttribute("errormsg", errormsg);
			}
		}
		request.setAttribute("info", info);
		return "success";
	}

	/**
	 * 用于Ajax的获得地区的json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showarea() throws Exception {
		HttpServletRequest req = ServletActionContext.getRequest();
		String pid = (String) req.getParameter("pid");
		List<Areainfo> areas = userinfoService.getAreainfoByPid(pid);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(areas));
		out.close();
		return null;
	}

	public String building() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateBuilding(this.userinfo);
			info = userinfoService.getUserinfoByUserid(user_id);
			String msg = "修改房产信息成功！";
			request.setAttribute("msg", msg);
		}

		request.setAttribute("info", info);
		return "success";
	}

	public String company() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateCompany(this.userinfo);
			info = userinfoService.getUserinfoByUserid(user_id);
			String msg = "修改单位信息成功！";
			request.setAttribute("msg", msg);
		}

		request.setAttribute("info", info);
		return "success";
	}

	public String firm() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateFirm(this.userinfo);
			info = userinfoService.getUserinfoByUserid(user_id);
			String msg = "修改私营业主信息成功！";
			request.setAttribute("msg", msg);
		}

		request.setAttribute("info", info);
		return "success";
	}

	public String finance() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateFinance(this.userinfo);
			info = userinfoService.getUserinfoByUserid(user_id);
			String msg = "修改财务状况信息成功！";
			request.setAttribute("msg", msg);
		}

		request.setAttribute("info", info);
		return "success";
	}

	public String contact() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateContact(this.userinfo);
			info = userinfoService.getUserinfoByUserid(user_id);
			User newuser = userService.getUserById(user_id);
			newuser.setQq(info.getQq());
			newuser.setTel(info.getTel());
			userService.updateuser(newuser);
			String msg = "修改联系方式成功！";
			request.setAttribute("msg", msg);
		}
		request.setAttribute("info", info);
		return "success";
	}

	public String mate() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateMate(this.userinfo);
			info = userinfoService.getUserinfoByUserid(user_id);
			String msg = "修改联系方式成功！";
			request.setAttribute("msg", msg);
		}

		request.setAttribute("info", info);
		return "success";
	}

	public String education() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateEducation(this.userinfo);
			info = userinfoService.getUserinfoByUserid(user_id);
			String msg = "修改教育背景成功！";
			request.setAttribute("msg", msg);
		}

		request.setAttribute("info", info);
		return "success";
	}

	public String other() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		Userinfo info = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		if (type == null || type.equals("")) {
			info = userinfoService.getUserinfoByUserid(user_id);
		} else {
			userinfo.setUser_id(user_id);
			userinfoService.updateOtherinfo(this.userinfo);
			info = userinfoService.getUserinfoByUserid(user_id);
			String msg = "修改信息成功！";
			request.setAttribute("msg", msg);
		}

		request.setAttribute("info", info);
		return "success";
	}

	// 我的帐号，实名认证
	public String realname() {
		User sessionuser = (User) session.get(Constant.SESSION_USER);
		long user_id = sessionuser.getUser_id();
		User newUser = null;
		// 根据type区分是否查询或者更新-
		String type = userinfo.getType();
		
		//v1.6.7.2 sj 2013-12-23 start
		int need_upload_card_pic = 1;
		RuleModel realCardCheck = new RuleModel(Global.getRule(EnumRuleNid.REALCARDCHECK.getValue()));
		if(realCardCheck != null && realCardCheck.getStatus() == 1 && "0".equals(realCardCheck.getValueStrByKey("need_upload_card_pic"))){				
			need_upload_card_pic = 0;
		}
		request.setAttribute("need_upload_card_pic", need_upload_card_pic);
		//v1.6.7.2 sj 2013-12-23 end
		
		if (type == null || type.equals("")) {
			newUser = userService.getDetailUser(user_id);
		} else {
			String realStatus = StringUtils.isNull(sessionuser.getReal_status());
			if (realStatus.equals("1") || realStatus.equals("2")) {
				message("实名认证已经审核成功或正在审核中！", "");
				return MSG;
			}
			String errormsg = checkRealname();
			if (!errormsg.equals("")) {
				newUser = userService.getDetailUser(user_id);
				request.setAttribute("errormsg", errormsg);
			} else if (!checkValidImg(StringUtils.isNull(valicode))) {
				message("验证码不正确！", "");
				return MSG;
			} else {
				
				//v1.6.7.2 sj 2013-12-23 start
				userinfo.setUser_id(user_id);
				userinfo.setReal_status(2);
				
				if(need_upload_card_pic == 0 && userinfo.getNature() != 2){				
					userinfo.setCard_pic1_path("");
					userinfo.setCard_pic2_path("");
				}else{
				//v1.6.7.2 sj 2013-12-23 end
					// v1.6.7.1 RDPROJECT-441 xx 2013-11-11 start
					if (!ImageUtil.fileIsImage(userinfo.getCard_pic1()) || !ImageUtil.fileIsImage(userinfo.getCard_pic2())) {
						message("您上传的图片无效，请重新上传！", "");
						return MSG;
					}
					// v1.6.7.1 RDPROJECT-441 xx 2013-11-11 end
					
					// 更新前对文件进行处理
					String contextPath = ServletActionContext.getServletContext().getRealPath("/");
					String upfiesDir = contextPath + "data/upfiles/images";
					String destfilename1 = upfiesDir + getUploadRealnameFileName(user_id, "_1");
					String card_pic1_path = destfilename1.replace(sep, "/");
					String destfilename2 = upfiesDir + getUploadRealnameFileName(user_id, "_2");
					String card_pic2_path = destfilename2.replace(sep, "/");
					;
					card_pic1_path = truncatUrl(card_pic1_path, contextPath);
					card_pic2_path = truncatUrl(card_pic2_path, contextPath);
					logger.info(destfilename1);
					logger.info(destfilename2);
					File imageFile1 = null;
					File imageFile2 = null;
					try {
						imageFile1 = new File(destfilename1);
						imageFile2 = new File(destfilename2);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e.getMessage());
					}
					try {
						FileUtils.copyFile(userinfo.getCard_pic1(), imageFile1);
						FileUtils.copyFile(userinfo.getCard_pic2(), imageFile2);
	
						// 实名认证加水印处理
						RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.IMAGE_WATERMARK.getValue()));
						if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
							String watermarkPath = contextPath + rule.getValueStrByKey("watermark_path");// 水印图片地址
							String watermark_path = watermarkPath.replace(sep, "/");
							File waterFile = new File(watermark_path);
							int watermark_place = rule.getValueIntByKey("watermark_place");// 水印图片位置
							float alpha = rule.getValueFloatByKey("alpha");// 水印图片位置
	
							watermarkManager(imageFile1, waterFile, (byte) watermark_place, alpha);// 正面加水应
							watermarkManager(imageFile2, waterFile, (byte) watermark_place, alpha);// 背面加水印
						}
	
					} catch (IOException e) {
						e.printStackTrace();
					}
					userinfo.setCard_pic1_path(card_pic1_path);
					userinfo.setCard_pic2_path(card_pic2_path);
				}
				
				// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 start
				newUser = userService.getDetailUser(user_id);
				int result = userService.realnameIdentify(userinfo);
				if(result==1){
					request.setAttribute("msg", "提交实名认证申请成功，请等待管理员审核 ");
				}else{
					request.setAttribute("msg", "提交实名认证失败！");
				}
				// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 end
			}
		}
		if (newUser != null) {
			newUser.hideChar();
		}
		request.setAttribute("user", newUser);
		
		return "success";
	}

	/**
	 * 水印处理方法
	 * 
	 * @param sourseFile原图片路径
	 * @param waterFile水印图片路径
	 * @param watermark_place 水印位置：1右下角,2右上角,3左上角,4左下角,5中心
	 * @param alpha 透明度0.0 -- 1.0
	 * @return
	 */
	public Boolean watermarkManager(File sourseFile, File waterFile, byte watermark_place, float alpha) {

		try {

			BufferedImage bis = ImageIO.read(sourseFile);
			int sourseHeight = bis.getHeight();
			int sourseWidth = bis.getWidth();

			BufferedImage biw = ImageIO.read(waterFile);
			int waterHeight = biw.getHeight();
			int waterWidth = biw.getWidth();

			if (alpha < 0 || alpha > 1) {// 透明度范围为0.0与1.0之间，否则return false
				return false;
			}

			// 如果加水印的原图高小于水印图片高，或加水印的原图宽小于水印图片宽，则return
			if (sourseHeight < waterHeight || sourseWidth < waterWidth)
				return false;

			int x = 0;// 水印图片X坐标
			int y = 0;// 水印图片Y坐标

			if (watermark_place == EnumWatermarkPlace.PLACE_RIGHT_UP.getValue()) {// 1右上角
				x = sourseWidth;
			} else if (watermark_place == EnumWatermarkPlace.PLACE_RIGHT_DOWN.getValue()) {// 2右下角
				x = sourseWidth;
				y = sourseHeight;
			} else if (watermark_place == EnumWatermarkPlace.PLACE_LEFT_UP.getValue()) {// 左上角.x和y都是0
				x = 0;
				y = 0;
			} else if (watermark_place == EnumWatermarkPlace.PLACE_LEFT_DOWN.getValue()) {// 左下角
				y = sourseHeight;
			} else if (watermark_place == EnumWatermarkPlace.PLACE_CENTER.getValue()) {// 水印加在中心已经在水印方法中处理，只要传入参数小于零的就可以。
				x = -1;
				x = -1;
			} else
				return false;

			String soursePath = sourseFile.getPath();
			String waterPath = waterFile.getPath();
			String imageType = ImageUtil.getFileTypeTwo(soursePath);
			// 加水印
			ImageUtil.pressImage(soursePath, waterPath, imageType, x, y, alpha);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private String getUploadRealnameFileName(long userid, String flag) {
		Date d = new Date();
		StringBuffer sb = new StringBuffer();
		sb.append("/").append(DateUtils.dateStr2(d)).append("/").append(d.getTime()).append("_").append(userid)
				.append(flag).append(".jpg");
		return sb.toString();
	}

	public String privacy() throws Exception {
		return "success";
	}

	public String vip() throws Exception {
		User sessionUser = this.getSessionUser();
		DetailUser detailUser = userService.getDetailUser(sessionUser.getUser_id());
		UserCacheModel userCache = null;
		String type = request.getParameter("type");
		String vipGiveMonth = request.getParameter("vip_give_month");
		// 获取充值赠送vip规则
		Rule rule = Global.getRule(EnumRuleNid.RECHARGE_VIP_GIVETIME.getValue());
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		List list = userService.getKfList(1);
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		Account act = accountService.getAccount(sessionUser.getUser_id());
		userCache = userService.getUserCacheByUserid(sessionUser.getUser_id());
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			String vipType = request.getParameter("vipType");
			if (StringUtils.isBlank(vipType)) {
				request.setAttribute("act", act);
				request.setAttribute("kflist", list);
				request.setAttribute("detailUser", detailUser);
				request.setAttribute("rule", rule);
				request.setAttribute("userCache", userCache);
				return SUCCESS;
			}
			int ruleStatus = rule.getStatus();
			if (ruleStatus == 1) {
				UserCache inituserCache = userService.getUserCacheByUserid(detailUser.getUser_id(), 2, 2);
				if (inituserCache != null) {
					message("你现在有未审核的赠送vip,不能再进行申请", "/member/vip.html");
					return MSG;
				}
				
				UserCache newuserCache = userService.getUserCacheByUserid(detailUser.getUser_id());
				
				String valicode = StringUtils.isNull(request.getParameter("valicode"));

				boolean valid = this.checkValidImg(valicode);
				if (!valid) {
					message("验证码错误", "/member/vip.html");
					return MSG;
				}
				if( newuserCache.getVip_status() != 1){
					String user_id_str = request.getParameter("kefu_userid");
					int kf_user_id = NumberUtils.getInt(user_id_str);
					// 招商贷特殊处理，不需要选择客服，直接传一个用户id
					if (Global.getWebid().equals("zsdai")) {
						kf_user_id = 1;
					} else {
						if (kf_user_id <= 0) {
							message("请选择客服人员！", "/member/vip.html");
							return "fail";
						}
					}
	                User newuser = userService.getUserById(kf_user_id);
					newuserCache.setKefu_userid(kf_user_id);
					if(newuser!=null){
					   newuserCache.setKefu_username(newuser.getUsername());
					}
				}
				newuserCache.setVip_give_status(2);
				if (newuserCache.getVip_status() !=1) {
					newuserCache.setVip_status(2);
				}
				// vip赠送月份
				long vip_give_month = NumberUtils.getLong(vipGiveMonth);
				newuserCache.setVip_give_month(vip_give_month);
				newuserCache.setType(2);
				String vip_remark = request.getParameter("vip_remark");
				newuserCache.setVip_remark(vip_remark);
				if (newuserCache == null) {
					newuserCache = new UserCache();
				}
				newuserCache.setKefu_addtime(getTimeStr());
				newuserCache.setUser_id(sessionUser.getUser_id());
				userService.saveUserCache(newuserCache);
				message("vip赠送申请成功！", "/member/vip.html");
				return MSG;
			}
		} else {
			if (StringUtils.isNull(type).equals("")) {
				//V1.6.6.1 RDPROJECT-300 wcw 2013-10-10 start
				int enable_specificKefu=NumberUtils.getInt(StringUtils.isNull(Global.getValue("enable_specificKefu")));
				enable_specificKefu=(enable_specificKefu==1?enable_specificKefu:0);
				if(enable_specificKefu==1){
					UserCache uc = userService.getUserCacheByUserid(sessionUser.getUser_id());
					
					
					if(uc!=null){
						if(uc.getKefu_userid()==0&&StringUtils.isBlank(uc.getKefu_username())){
							message("请先申请专属客服！", "/member/account/specificKefu.html","进入专属客服页面！");
							return "fail";
						}else{
							User kefuUser=userService.getDetailUser(uc.getKefu_userid());
							request.setAttribute("onlyKefuUser", kefuUser);
						}
					}
				}
				//V1.6.6.1 RDPROJECT-300 wcw 2013-10-10 end
				request.setAttribute("act", act);
				request.setAttribute("kflist", list);
				request.setAttribute("detailUser", detailUser);
				request.setAttribute("userCache", userCache);
			} else {
				UserCache uc = userService.getUserCacheByUserid(sessionUser.getUser_id());
				if (uc == null) {
					uc = new UserCache();
				}
				if (uc.getVip_status() == 2 || uc.getVip_status() == 1) {
					message("不允许重复申请！", "/member/vip.html");
					return "fail";
				}
				String valicode = StringUtils.isNull(request.getParameter("valicode"));

				boolean valid = this.checkValidImg(valicode);
				if (!valid) {
					message("验证码错误", "/member/vip.html");
					return MSG;
				} else {
					String user_id_str = request.getParameter("kefu_userid");
					int kf_user_id = NumberUtils.getInt(user_id_str);
					// 招商贷特殊处理，不需要选择客服，直接传一个用户id
					if (Global.getWebid().equals("zsdai")) {
						kf_user_id = 1;
					} else {
						if (kf_user_id <= 0) {
							message("请选择客服人员！", "/member/vip.html");
							return "fail";
						}
					}
					User newuser = userService.getUserById(kf_user_id);
					String vip_remark = request.getParameter("vip_remark");
					uc.setVip_remark(vip_remark);
					uc.setKefu_userid(kf_user_id);
					uc.setKefu_username(newuser.getUsername());
					uc.setVip_status(2);
					uc.setKefu_addtime(getTimeStr());
					uc.setUser_id(sessionUser.getUser_id());
					// 更新后的用户缓存
					AccountLog accountLog = new AccountLog(sessionUser.getUser_id(), Constant.VIP_FEE, 1,
							this.getTimeStr(), this.getRequestIp());
					boolean isOk = true;
					String checkMsg = "";
					try {
						userCache = userService.applyVip(uc, accountLog);
					} catch (BorrowException e) {
						isOk = false;
						checkMsg = e.getMessage();
					} catch (Exception e) {
						isOk = false;
						checkMsg = "系统出错，联系管理员！";
						logger.error(e.getMessage());
					}
					if (!isOk) {
						message(checkMsg, "");
						return MSG;
					}
				}

			}

			request.setAttribute("detailUser", detailUser);
			request.setAttribute("userCache", userCache);
		}
		return "success";

	}

	private String checkUserinfo() {
		String msg = "";
		/*
		 * if (StringUtils.isNull(userinfo.getRealname()).equals("")) { msg = "真实姓名不能为空！"; return msg; }
		 */
		if (StringUtils.isNull(userinfo.getShebaoid()).equals("")
				&& StringUtils.isNull(userinfo.getShebao()).equals("")) {
			msg = "社保号不能为空！";
		}
		return msg;
	}

	private String checkRealname() {
		String msg = "";
		// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 start
		byte nature = userinfo.getNature();
		if (StringUtils.isEmpty(userinfo.getRealname())) {
			if(nature==2){
				msg = "公司名不能为空,请填写！";
			}else{
				msg = "真实姓名不能为空,请填写！";
			}
			return msg;
		}
		if (nature!=2 && NumberUtils.getInt(userinfo.getNation()) == 0) {
			msg = "民族不能为空,请填写！";
			return msg;
		}
		if (StringUtils.isEmpty(userinfo.getBirthday())) {
			if(nature==2){
				msg = "注册时间不能为空,请填写！";
			}else{
				msg = "出生日期不能为空,请填写！";
			}
			return msg;
		}
		if (StringUtils.isEmpty(userinfo.getCard_id())) {
			msg = "证件号码不能为空,请填写！";
			return msg;
		}
		if (nature!=2 && StringUtils.isNull(userinfo.getCard_type()).equals("385")) {
			if (!StringUtils.isCard(userinfo.getCard_id())) {
				msg = "证件号码格式不正确,请重新填写！";
				return msg;
			}
		}
		User u = userService.getUserByCardNO(userinfo.getCard_id());
		if (u != null) {
			msg = "该证件号码已被他人使用！";
			return msg;
		}
		if (userinfo.getProvince() == null || userinfo.getCity() == null || userinfo.getArea() == null
				|| "0".equals(userinfo.getProvince()) || "0".equals(userinfo.getArea())
				|| "0".equals(userinfo.getCity())) {
			if(nature==2){
				msg = "公司所在地不能为空,请填写！";
			}else{
				msg = "籍贯不能为空,请填写！";
			}
			return msg;
		}
		if (nature==2 && StringUtils.isBlank(userinfo.getAddress())) {
			msg = "注册地址不能为空,请填写！";
			return msg;
		}
		
		//v1.6.7.2 sj 2013-12-23 start
		int need_upload_card_pic = (Integer) request.getAttribute("need_upload_card_pic");
		if (need_upload_card_pic == 1 || nature == 2){
		//v1.6.7.2 sj 2013-12-23 end
			String cardName1="身份证正面";
			String cardName2="身份证反面";
			if(nature==2){
				cardName1="公司营业执照";
				cardName2="组织机构代码";
			}
			if (userinfo.getCard_pic1() == null) {
				msg = cardName1+"文件不能为空！";
				return msg;
			}
			if (userinfo.getCard_pic2() == null) {
				msg = cardName2+"文件不能为空！";
				return msg;
			}
			if (userinfo.getCard_pic1().length() > 1024 * 1024) {
				msg = cardName1+"文件大小必须小于1MB.";
				return msg;
			}
			if (userinfo.getCard_pic2().length() > 1024 * 1024) {
				msg = cardName2+"文件大小必须小于1MB.";
				return msg;
			}
			if (!userinfo.getCard_pic1FileName().matches(".*(jpg|png|gif|JPG|PNG|GIF)")) {
				msg = cardName1+"文件类型必须为.jpg, .gif或者.png类型";
				return msg;
			}
			if (!userinfo.getCard_pic2FileName().matches(".*(jpg|png|gif|JPG|PNG|GIF)")) {
				msg = cardName2+"文件类型必须为.jpg, .gif或者.png类型";
			}
		}
		
		// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 end
		return msg;
	}

	private String truncatUrl(String old, String truncat) {
		old = old.replace(sep, "/");
		truncat = truncat.replace(sep, "/");
		String url = "";
		url = old.replace(truncat, "");
		return url;
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

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
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

}
