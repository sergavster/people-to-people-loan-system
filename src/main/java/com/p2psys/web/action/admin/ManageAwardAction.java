package com.p2psys.web.action.admin;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.domain.ObjAward;
import com.p2psys.domain.RuleAward;
import com.p2psys.domain.UserAward;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.AwardService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class ManageAwardAction extends BaseAction implements ModelDriven<RuleAward> {

	/** 规则对象类 */
	private RuleAward ruleAward = new RuleAward();
	
	/** 抽奖接口 */
	private AwardService awardService;

	/**
	 * 获取ruleAward
	 * 
	 * @return ruleAward
	 */
	public RuleAward getRuleAward() {
		return ruleAward;
	}

	/**
	 * 设置ruleAward
	 * 
	 * @param ruleAward 要设置的ruleAward
	 */
	public void setRuleAward(RuleAward ruleAward) {
		this.ruleAward = ruleAward;
	}

	/**
	 * 获取awardService
	 * 
	 * @return awardService
	 */
	public AwardService getAwardService() {
		return awardService;
	}

	/**
	 * 设置awardService
	 * 
	 * @param awardService 要设置的awardService
	 */
	public void setAwardService(AwardService awardService) {
		this.awardService = awardService;
	}

	@Override
	public RuleAward getModel() {
		return ruleAward;
	}

	/**
	 * 所有规则配置页面
	 * 
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String allRule() throws Exception {
		List<RuleAward> list = awardService.getRuleAwardList();
		setMsgUrl("/admin/lottery/allRule.html");
		request.setAttribute("list", list);
		return SUCCESS;
	}

	/**
	 * 添加新规则
	 * 
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String addRule() throws Exception {

		// 操作类型
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		if (!StringUtils.isBlank(actionType)) {
			// 创建时间
			ruleAward.setAddtime(DateUtils.getNowTimeStr());
			// 创建IP
			ruleAward.setAddip(getRequestIp());
			awardService.addRuleAward(ruleAward);
			message("增加成功！", "/admin/lottery/allRule.html");
			return ADMINMSG;
		}

		return SUCCESS;
	}

	/**
	 * 修改规则
	 * 
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String modifyRule() throws Exception {
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		long id = NumberUtils.getLong(StringUtils.isNull(request.getParameter("id")));
		if (!StringUtils.isBlank(actionType)) {
			awardService.updateRuleAward(ruleAward);
			message("修改成功！", "/admin/lottery/allRule.html");
			return ADMINMSG;
		} else {
			RuleAward ruleAward = awardService.getRuleAwardByRuleId(id);
			request.setAttribute("p", ruleAward);
		}

		return SUCCESS;
	}

	/**
	 * 所有规则配置页面
	 * 
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String allObject() throws Exception {
		long id = NumberUtils.getLong(StringUtils.isNull(request.getParameter("id")));
		List<ObjAward> list = awardService.getObjectAwardListByRuleId(id);
		setMsgUrl("/admin/lottery/allObject.html");
		request.setAttribute("list", list);
		request.setAttribute("id", id);
		return SUCCESS;
	}

	/**
	 * 添加新奖品
	 * 
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String addObject() throws Exception {
		// 操作类型
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		if (!StringUtils.isBlank(actionType)) {
			ObjAward data = setObjAward(actionType);
			awardService.addObjAward(data);
			long ruleId = NumberUtils.getLong(StringUtils.isNull(request.getParameter("rule_id")));
			message("增加成功！", "/admin/lottery/allObject.html?id=" + ruleId);
			return ADMINMSG;
		}
		return SUCCESS;
	}

	/**
	 * 修改奖品信息
	 * 
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String modifyObject() throws Exception {
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		long id = NumberUtils.getLong(StringUtils.isNull(request.getParameter("id")));
		if (!StringUtils.isBlank(actionType)) {
			ObjAward data = setObjAward(actionType);
			awardService.updateObjAward(data);
			long ruleId = NumberUtils.getLong(StringUtils.isNull(request.getParameter("rule_id")));
			message("修改成功！", "/admin/lottery/allObject.html?id=" + ruleId);
			return ADMINMSG;
		} else {
			ObjAward objectAward = awardService.getObjectAwardById(id);
			request.setAttribute("p", objectAward);
		}
		return SUCCESS;
	}

	/**
	 * 设定奖品信息
	 * 
	 * @param actionType 操作类型 add：新增 modify:修改
	 * @return 奖品信息
	 */
	private ObjAward setObjAward(String actionType) {
		ObjAward data = new ObjAward();
		// 奖品名
		data.setName(StringUtils.isNull(request.getParameter("name")));
		// 规则ID
		data.setRule_id(NumberUtils.getInt(StringUtils.isNull(request.getParameter("rule_id"))));
		// 奖品级别
		data.setLevel(NumberUtils.getInt(StringUtils.isNull(request.getParameter("level"))));
		// 奖品中奖率
		BigDecimal rate = new BigDecimal(StringUtils.isNull(request.getParameter("rate")));
		data.setRate(rate.multiply(new BigDecimal(100000000)).intValue());
		// 奖品限制
		data.setAward_limit(NumberUtils.getInt(StringUtils.isNull(request.getParameter("award_limit"))));
		// 奖品总量
		data.setTotal(NumberUtils.getInt(StringUtils.isNull(request.getParameter("total"))));
		// 倍率
		data.setRatio(NumberUtils.getDouble(StringUtils.isNull(request.getParameter("ratio"))));
		// v1.6.6.2 RDPROJECT-285 lhm 2013-10-25 start
		// 奖品类型
		data.setType(NumberUtils.getInt(StringUtils.isNull(request.getParameter("type"))));
		// v1.6.6.2 RDPROJECT-285 lhm 2013-10-25 end
		// 奖品属性值(如面额)
		data.setObj_value(NumberUtils.getInt(StringUtils.isNull(request.getParameter("obj_value"))));
		// 奖品描述
		data.setDescription(StringUtils.isNull(request.getParameter("description")));
		// 奖品规则描述
		data.setObject_rule(StringUtils.isNull(request.getParameter("object_rule")));
		if ("add".equals(actionType)) {
			// 创建时间
			data.setAddtime(DateUtils.getNowTimeStr());
			// 创建IP
			data.setAddip(getRequestIp());
		} else {
			data.setId(NumberUtils.getLong(StringUtils.isNull(request.getParameter("id"))));
		}
		return data;
	}

	/**
	 * 取得抽奖列表
	 * 
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String userAwardList() throws Exception {
		//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 start
		List<RuleAward> ruleAwardList=awardService.getRuleAwardList();
		request.setAttribute("ruleAwardList", ruleAwardList);
		//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 end
		//
		String type = StringUtils.isNull(request.getParameter("type"));
		// 页数
		int page = NumberUtils.getInt(request.getParameter("page"));
		String user_name = StringUtils.isNull(request.getParameter("user_name"));
		// 抽奖时间起
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		// 抽奖时间止
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		// 状态（0：未中奖 1：中奖）
		String status = StringUtils.isNull(request.getParameter("status"));
		SearchParam param = new SearchParam();
		param.setUser_name(user_name);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setStatus(status);
		//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 start
		param.setRule_id(paramString("rule_id"));
		//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 end
		PageDataList plist = awardService.getUserAwardList(page, param);
		setPageAttribute(plist, param);
		double awardSum = awardService.getUserAwardSum(param);
		request.setAttribute("awardSum", awardSum);
		setMsgUrl("/admin/lottery/userAwardList.html");
		if (type.isEmpty()) {
			return SUCCESS;
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "award_" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "data/export/" + downloadFile;
			String[] names = new String[] { "id", "user_name", "level", "award_name", "addtime", "status" };
			String[] titles = new String[] { "ID", "用户名", "奖品等级", "奖品名称", "抽奖时间", "状态" };
			List<UserAward> list = awardService.getAllUserAwardList(param);
			ExcelHelper.writeExcel(infile, list, UserAward.class, Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
	}
}
