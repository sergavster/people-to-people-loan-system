package com.p2psys.web.action.admin;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.p2psys.common.enums.EnumRewardStatisticsType;
import com.p2psys.context.Constant;
import com.p2psys.domain.RewardStatistics;
import com.p2psys.domain.Rule;
import com.p2psys.domain.User;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RewardRecordModel;
import com.p2psys.model.RewardStatisticsModel;
import com.p2psys.model.RuleCheckModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.borrow.BorrowHelper;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.service.RewardStatisticsService;
import com.p2psys.service.RuleService;
import com.p2psys.service.UserService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 奖励统计
 
 *
 */
public class ManageRewardAction extends BaseAction {
	/**
	 * 输出日志 
	 */
	private final static Logger logger = Logger.getLogger(ManageRewardAction.class);
	
	/**
	 * 规则service
	 */
	private RewardStatisticsService rewardStatisticsService;
	
	/**
	 * 用户service
	 */
	private UserService userService;
	
	private RuleService ruleService;
	
	BorrowModel model = new BorrowModel();
	
	/**
	 * @return the model
	 */
	public BorrowModel getModel() {
		return model;
	}

	/**
	 * 获取符合条件的list
	 * @return String
	 * @throws Exception Exception
	 */
	public String showAllReward() throws Exception {
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-06 start
		rewardStatisticsService.getRewardList();
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-06 end
		BorrowModel wrapModel = BorrowHelper.getHelper(Constant.TYPE_ALL, model);
		String choiceType = StringUtils.isNull(request.getParameter("choiceType"));
		int page = NumberUtils.getInt(request.getParameter("page"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String awardType = StringUtils.isNull(request.getParameter("awardType"));
		String receive_status = StringUtils.isNull(request.getParameter("receive_status"));
		SearchParam param = new SearchParam();
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setUsername(username);
		param.setAwardType(awardType);
		param.setReceive_status(receive_status);
		PageDataList plist = rewardStatisticsService.getRewardStatisticsList(param, page);
		setPageAttribute(plist, param);
		request.setAttribute("newDate", DateUtils.getTimeStr(new Date()));
		request.setAttribute("map", wrapModel.getModel().getParam().toMap());
		if (choiceType.isEmpty()) {
			return SUCCESS;
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "log_" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "data/export/" + downloadFile;
			String[] names = new String[]{"username", "passive_username", "receive_account", "receive_yesaccount"};
			String[] titles = new String[]{"收到奖励者", "发放奖励者", "应收金额", "实收金额"};
			List<RewardStatistics> list = rewardStatisticsService.getRewardStatistics(param);
			ExcelHelper.writeExcel(infile, list, RewardStatisticsModel.class, 
					Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
	}
	
	public String viewReward() throws Exception{
		long id = NumberUtils.getLong(request.getParameter("id"));
		RewardStatistics r = rewardStatisticsService.getRewardStatisticsById(id);
		if (r == null) {
			message("非法操作！");
			return ADMINMSG;
		}
		User user = userService.getUserById(r.getReward_user_id());
		User passiveName = userService.getDetailUser(r.getPassive_user_id());
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 start
		Rule rule = ruleService.getRule(r.getRule_id());
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 end
		User auth = (User) session.get(Constant.AUTH_USER);
		
		// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 start
		if (r.getType() != EnumRewardStatisticsType.FIRST_TENDER_REWARD.getValue() 
				&& r.getType() != EnumRewardStatisticsType.INVITE_FIRST_TENDER_AWARD.getValue()) {
			// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 end
			int vipStatus = 0;
			if (passiveName.getVip_status() == 1) {
				vipStatus = 1;
			}
			RuleCheckModel checkModel = JSON.parseObject(rule.getRule_check(), RuleCheckModel.class);
			request.setAttribute("tenderCheckMoney", checkModel.getTender_check_money());
			request.setAttribute("vipStatus", vipStatus);
		}
		request.setAttribute("reward", r);
		request.setAttribute("username", user.getUsername());
		request.setAttribute("passiveName", passiveName.getUsername());
		request.setAttribute("auth", auth);
		saveToken("verifyReward_token");
		return SUCCESS;
	}
	
	/**
	 * 审核
	 * @return string
	 * @throws Exception
	 */
	public String verifyReward() throws Exception{
		setMsgUrl("/admin/reward/showAllReward.html");
		long id = NumberUtils.getLong(request.getParameter("id"));
		double receiveYesAccount = NumberUtils.getDouble(request
				.getParameter("receiveYesAccount"));
		String status = request.getParameter("status");
		User auth_user = (User) session.get(Constant.AUTH_USER);
		String tokenMsg = checkToken("verifyReward_token");
		if (!StringUtils.isBlank(tokenMsg)) {
			message(tokenMsg);
			return ADMINMSG;
		}
		if (id <= 0) {
			message("非法操作！");
			return ADMINMSG;
		}
		if (receiveYesAccount <= 0) {
			message("发放奖励金额为" + receiveYesAccount + ",发放奖励不能为负数！");
			return ADMINMSG;
		}
		RewardStatistics r = rewardStatisticsService
				.getRewardStatisticsById(id);
		if (r.getReceive_status() == 2) {
			message("该记录已经审核通过，不允许重复操作！");
			return ADMINMSG;
		}
		// v1.6.6.1 实发金额取页面的值 zza 2013-09-25 start
		r.setReceive_yesaccount(receiveYesAccount);
		// v1.6.6.1 实发金额取页面的值 zza 2013-09-25 end
		try {
			rewardStatisticsService.verifyReward(r, status, auth_user,
					getRequestIp());
			message("审核成功!", "/admin/reward/showAllReward.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ADMINMSG;
	}
	
	// v1.6.7.2 RDPROJECT-547 lx 2013-12-6 start
	/**
	 * 获取符合条件的奖励记录list
	 * @return String
	 * @throws Exception Exception
	 */
	public String showRewardRecord() {
		String choiceType = paramString("choiceType");
		int page = paramInt("page");
		String dotime1 = paramString("dotime1");
		String dotime2 = paramString("dotime2");
		String username = paramString("username");
		String type = paramString("type");
		String passive_username = paramString("passive_username");
		SearchParam param = new SearchParam();
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setUsername(username);
		param.setPassive_username(passive_username);
		param.setType(type);
		if (StringUtils.isBlank(choiceType)) {
			PageDataList plist = rewardStatisticsService.getRewardRecordList(param, page);
			setPageAttribute(plist, param);
			return SUCCESS;
		} else {
			try {
				String contextPath = ServletActionContext.getServletContext().getRealPath("/");
				String downloadFile = "rewardRecord_" + System.currentTimeMillis() + ".xls";
				String infile = contextPath + "/data/export/" + downloadFile;
				String[] names = new String[]{"id","type","fk_id","username", "passive_username", "reward_account", "addtime"};
				String[] titles = new String[]{"ID", "类型", "外键ID", "获得者", "奖励提供者", "奖励金额", "奖励时间"};
				List<RewardRecordModel> list = rewardStatisticsService.getRewardRecordList(param);
				ExcelHelper.writeExcel(infile, list, RewardRecordModel.class,Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
				return null;
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return null;
	}
	// v1.6.7.2 RDPROJECT-547 lx 2013-12-6 end
	
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
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the ruleService
	 */
	public RuleService getRuleService() {
		return ruleService;
	}

	/**
	 * @param ruleService the ruleService to set
	 */
	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}
}
