package com.p2psys.web.action.award;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.p2psys.common.enums.EnumAwardErrorType;
import com.p2psys.domain.RuleAward;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAward;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.award.AwardResult;
import com.p2psys.model.award.Awardee;
import com.p2psys.service.AwardService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 抽奖模块
 * 
 
 * @version 1.0
 * @since 2013-7-23
 */
public class AwardAction extends BaseAction {
	/**
	 * 日志
	 */
	protected final Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 抽奖接口
	 */
	private AwardService awardService;

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

	/**
	 * 抽奖主程序
	 * 
	 * @return forward信息
	 * @throws Exception 异常
	 */
	public String award() throws Exception {

		// 1. 判断用户是否登录
		User user = getSessionUser();
		if (user == null) {
			AwardResult result = new AwardResult();
			result.setError(EnumAwardErrorType.RESULT_NO_REGISTER);
			awardPrintJson(result);
			return null;
		}

		// 2.判断ruleId
		long ruleId = NumberUtils.getLong(request.getParameter("id"));
		if (ruleId == 0) {
			AwardResult result = new AwardResult();
			result.setError(EnumAwardErrorType.RESULT_PARAMETER_ERROR);
			awardPrintJson(result);
			return null;
		}

		// 抽奖
		AwardResult result = awardService.award(ruleId, getSessionUser(), 0);
		// 输出抽奖结果
		awardPrintJson(result);
		return null;
	}

	/**
	 * 初期显示
	 * 
	 * @return forward信息
	 * @throws Exception 异常
	 */
	public String index() throws Exception {
		// 取得抽奖类型
		int type = NumberUtils.getInt(request.getParameter("type"));
		long ruleId = NumberUtils.getLong(request.getParameter("id"));
		RuleAward ruleAward = null;
		if (ruleId != 0) {
			ruleAward = awardService.getRuleAwardByRuleId(ruleId);
		} else {
			ruleAward = awardService.getRuleAwardByAwardType(type);
		}
		request.setAttribute("rule", ruleAward);
		return SUCCESS;
	}

	/**
	 * 显示中奖名单
	 * 
	 * @return forward信息
	 * @throws Exception 异常
	 */
	public String getAwardList() throws Exception {
		long ruleId = NumberUtils.getLong(request.getParameter("id"));
		boolean isOrderByLevel = false;
		if (!"".equals(StringUtils.isNull(request.getParameter("level")))) {
			isOrderByLevel = true;
		}
		awardPrintJson(getAwardeeList(ruleId, isOrderByLevel));
		return null;
	}

	/**
	 * 显示我的中奖名单
	 * 
	 * @return forward信息
	 * @throws Exception 异常
	 */
	public String getMyAwardList() throws Exception {
		long ruleId = NumberUtils.getLong(request.getParameter("id"));
		User user = getSessionUser();
		List<UserAward> list = null;
		if (user != null) {
			list = awardService.getMyAwardList(ruleId, getSessionUser().getUser_id());
		}
		awardPrintJson(list);
		return null;
	}

	/**
	 * 显示我的中奖名单记录
	 * 
	 * @return forward信息
	 * @throws Exception 异常
	 */
	public String getMyAwardLogList() throws Exception {
		int ruleId = NumberUtils.getInt(request.getParameter("ruleId"));
		int page = NumberUtils.getInt(request.getParameter("page"));
		int status = NumberUtils.getInt(request.getParameter("status"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		User user = getSessionUser();
		SearchParam param = new SearchParam();
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		PageDataList plist = new PageDataList();
		if (user != null) {
			plist = awardService.getMyAwardLogList(page, param, ruleId, status, (int) user.getUser_id());
		} else {
			return "error";
		}
		setPageAttribute(plist, param);
		return SUCCESS;
	}

	/**
	 * 取得中奖名单
	 * 
	 * @param ruleId 规则ID
	 * @param isOrderByLevel 是否按中奖级别排序
	 * @return 中奖名单列表
	 */
	private List<Awardee> getAwardeeList(long ruleId, boolean isOrderByLevel) {
		// 中奖名单取得
		List<UserAward> list = awardService.getAwardeeList(ruleId, 30, isOrderByLevel);
		List<Awardee> result = new ArrayList<Awardee>();
		for (UserAward userAward : list) {
			Awardee awardee = new Awardee();
			// 时间格式【yyyy-MM-dd HH:mm:ss】
			awardee.setTime(DateUtils.dateStr4(DateUtils.getDate(userAward.getAddtime())));
			awardee.setName(replaceSubString(userAward.getUser_name(), 3));
			awardee.setAward(userAward.getAward_name());
			result.add(awardee);
		}
		return result;
	}

	/**
	 * 输出JSON对象
	 * 
	 * @param obj JSON对象
	 * @throws Exception 异常
	 */
	private void awardPrintJson(Object obj) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", obj);
		printJson(JSON.toJSONString(map));
	}

	/**
	 * 把字符串的后n位用*号代替
	 * 
	 * @param str 要代替的字符串
	 * @param n 代替的位数
	 * @return *号替换后的字符串
	 */
	public static String replaceSubString(String str, int n) {
		String sub = "";
		try {
			int length = str.length();
			if (length == 1) {
				return str;
			} else if (length == 2) {
				n = 1;
			}
			sub = str.substring(0, str.length() - n);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < n; i++) {
				sb = sb.append("*");
			}
			sub += sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sub;
	}
}
