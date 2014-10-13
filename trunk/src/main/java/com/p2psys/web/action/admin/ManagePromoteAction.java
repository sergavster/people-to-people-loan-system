package com.p2psys.web.action.admin;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.domain.RulePromote;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.RulePromoteService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 推广奖励规则Action
 * 
 
 * @version 1.0
 * @since 2013-10-17
 */
public class ManagePromoteAction extends BaseAction implements ModelDriven<RulePromote> {
	
	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(ManagePromoteAction.class);
	
	/**
	 * model
	 */
	private RulePromote rulePromote = new RulePromote();
	
	/**
	 * rulePromoteService
	 */
	private RulePromoteService rulePromoteService;
	
	/**
	 * 获取规则列表
	 * @return 跳转页面
	 */
	public String showAllRulePromote() {
		int page = NumberUtils.getInt(request.getParameter("page"));
		String status = StringUtils.isNull(request.getParameter("status"));
		SearchParam param = new SearchParam();
		param.setStatus(status);
		PageDataList plist = rulePromoteService.getAllRulePromote(page, param);
		setPageAttribute(plist, param);
		// v1.6.6.2 RDPROJECT-238 zza 2013-10-21 start
		setMsgUrl("/admin/promote/showAllRulePromote.html");
		// v1.6.6.2 RDPROJECT-238 zza 2013-10-21 end
		return SUCCESS;
	}

	/**
	 * 添加新规则
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String addRulePromote() throws Exception {
		// 操作类型
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		if (!StringUtils.isBlank(actionType)) {
			// 创建时间
			rulePromote.setAddtime(DateUtils.getNowTimeStr());
			if (rulePromote.getCount_down() < rulePromote.getCount_up()) {
				message("成功邀请人数后面的数字不能比前面的小！", "/admin/promote/addRulePromote.html");
				return ADMINMSG;
			} else {
				rulePromoteService.addRulePromote(rulePromote);
				// v1.6.6.2 RDPROJECT-238 zza 2013-10-21 start
				message("增加成功！", "/admin/promote/showAllRulePromote.html");
				// v1.6.6.2 RDPROJECT-238 zza 2013-10-21 end
				return ADMINMSG;
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 修改规则
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String modifyRulePromote() throws Exception {
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		long id = NumberUtils.getLong(StringUtils.isNull(request.getParameter("id")));
		if (!StringUtils.isBlank(actionType)) {
			if (rulePromote.getCount_down() < rulePromote.getCount_up()) {
				message("成功邀请人数后面的数字不能比前面的小！", "");
				return ADMINMSG;
			} else {
				rulePromoteService.modifyRulePromote(rulePromote);
				// v1.6.6.2 RDPROJECT-238 zza 2013-10-21 start
				message("修改成功！", "/admin/promote/showAllRulePromote.html");
				// v1.6.6.2 RDPROJECT-238 zza 2013-10-21 end
				return ADMINMSG;
			}
		} else {
			RulePromote promote = rulePromoteService.getRulePromoteById(id);
			request.setAttribute("promote", promote);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除
	 * @return String
	 */
	public String delRulePromote() {
		long id = NumberUtils.getLong(request.getParameter("id"));
		RulePromote promote = rulePromoteService.getRulePromoteById(id);
		if (promote != null) {
			rulePromoteService.delRulePromote(id);
			// v1.6.6.2 RDPROJECT-238 zza 2013-10-21 start
			message("删除成功！", "/admin/promote/showAllRulePromote.html");
			// v1.6.6.2 RDPROJECT-238 zza 2013-10-21 end
		}
		return ADMINMSG;
	}

	/**
	 * 获取rulePromote
	 * 
	 * @return rulePromote
	 */
	public RulePromote getRulePromote() {
		return rulePromote;
	}

	/**
	 * 设置rulePromote
	 * 
	 * @param rulePromote 要设置的rulePromote
	 */
	public void setRulePromote(RulePromote rulePromote) {
		this.rulePromote = rulePromote;
	}

	/**
	 * 获取rulePromoteService
	 * 
	 * @return rulePromoteService
	 */
	public RulePromoteService getRulePromoteService() {
		return rulePromoteService;
	}

	/**
	 * 设置rulePromoteService
	 * 
	 * @param rulePromoteService 要设置的rulePromoteService
	 */
	public void setRulePromoteService(RulePromoteService rulePromoteService) {
		this.rulePromoteService = rulePromoteService;
	}

	@Override
	public RulePromote getModel() {
		return rulePromote;
	}

}
