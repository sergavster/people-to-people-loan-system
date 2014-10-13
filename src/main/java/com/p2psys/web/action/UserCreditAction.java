package com.p2psys.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.p2psys.common.enums.EnumConvertStatus;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.credit.service.CreditConvertService;
import com.p2psys.domain.CreditConvert;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCredit;
import com.p2psys.model.CreditConvertModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCacheModel;
import com.p2psys.service.RuleService;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

//会员积分action
public class UserCreditAction extends BaseAction {

	// 用户积分service
	private  UserCreditService userCreditService;
	
	//规则service
	private RuleService ruleService;
	
	private CreditConvertService creditConvertService;
	
	private CreditConvert creditConvert;
	
	private UserService userService;
	
	//用户积分记录
	public String creditLog(){
		User user = (User) session.get(Constant.SESSION_USER);
		if(user == null) return "error";//如果登陆用户为空，则return登陆页面
		int page=NumberUtils.getInt(request.getParameter("page"));//分页
		String dotime1=request.getParameter("dotime1");//开始时间
		String dotime2=request.getParameter("dotime2");//结束时间
		long type_id = NumberUtils.getInt(request.getParameter("type_id"));//积分类型ID
//		long user_id = user.getUser_id();
		SearchParam param = new SearchParam();
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setUsername(user.getUsername());
		//查询用户积分操作记录
		PageDataList pageDataList = userCreditService.getCreditLogPage(page, param , type_id );
		//查询所有的积分；类型
		List<CreditType> typeList = userCreditService.getCreditTypeAll();
		List list = pageDataList.getList();
		request.setAttribute("typeList", typeList);
		request.setAttribute("type_id", type_id);
		request.setAttribute("creditList", list);
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", param.toMap());
		return "success";
		
	}

	/**
	 * 积分兑换
	 * @return
	 */
	public String creditCashDetail(){
		User user = (User) session.get(Constant.SESSION_USER);
		UserCredit credit = userCreditService.getUserCreditByUserId(user.getUser_id());
		request.setAttribute("credit", credit);
		saveToken("credit_cash_token");
		return "success";
		
	}
	
	/**
	 * 积分兑换验证
	 * @return
	 */
	public String creditCashValidate ()throws Exception{
		User user = (User) session.get(Constant.SESSION_USER);
		// 提取积分兑换规则信息
		RuleModel rule = new RuleModel(ruleService.getRuleByNid(EnumRuleNid.INTEGRAL_CONVERT.getValue()));
		int integral_basic = rule.getValueIntByKey("integral_basic");//积分兑换基数
		int integral_min = rule.getValueIntByKey("integral_min");//最小兑换基数
		UserCredit credit = userCreditService.getUserCreditByUserId(user.getUser_id());
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("item", credit);
		map.put("integral_basic", integral_basic);
		map.put("integral_min", integral_min);
		Map<String,Object> map2=new HashMap<String,Object>();
		map2.put("msg", "success");
		map2.put("infoMap", map);
		printJson(JSON.toJSONString(map2));
		return null;
	}
	
	/**
	 * 积分兑换
	 * @return
	 */
	public String creditCash ()throws Exception{
		
		String tokenMsg = checkToken("credit_cash_token");
		if (!StringUtils.isBlank(tokenMsg)) {
			request.setAttribute("msg", "请勿重复提交！");
			return "result";
		}
		
		User user = (User) session.get(Constant.SESSION_USER);
		// 提取积分兑换规则信息
		creditConvert.setUser_id(user.getUser_id());
		creditConvert.setType(EnumRuleNid.INTEGRAL_CONVERT.getValue());
		boolean result = userCreditService.integralConvert(creditConvert);
		if(result){
			request.setAttribute("msg", "积分兑换成功！");
		}else request.setAttribute("errormsg", "积分兑换失败！");
		UserCredit credit = userCreditService.getUserCreditByUserId(user.getUser_id());
		request.setAttribute("credit", credit);
		saveToken("credit_cash_token");
		return "result";
	}

	
	
	/**
	 * 积分兑换Vip
	 * @return
	 */
	public String cashVipInit(){
		User user = (User) session.get(Constant.SESSION_USER);
		UserCredit credit = userCreditService.getUserCreditByUserId(user.getUser_id());
		// 提取积分兑换规则信息
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INTEGRAL_VIP.getValue()));
		boolean result = true; // 是否可以申请
		if(rule == null || rule.getStatus() != EnumRuleStatus.RULE_STATUS_YES.getValue()) result = false;
		
		int integral = rule.getValueIntByKey("integral");//兑换需要的积分
		int time_check = rule.getValueIntByKey("time_check");// 兑换vip间隔时间是否启用
		int convert_time = rule.getValueIntByKey("convert_time");// 兑换vip间隔多长时间（月）
		
		// 判断用户申请的间隔时间，是否可以再次申请兑换
		if(time_check > 0){
			List<CreditConvertModel> convertList = creditConvertService.getCreditConvert(user.getUser_id(), EnumRuleNid.INTEGRAL_VIP.getValue());
			for(CreditConvertModel model : convertList){
				if(model != null && model.getStatus() == EnumConvertStatus.WAIT_AUDIT.getValue() ){
					result = false;
					break;
				}else if(model != null && model.getStatus() == EnumConvertStatus.PASS_AUDIT.getValue()){
					String vipStartTime = DateUtils.getTimeStr(DateUtils.rollMon(DateUtils.getDate(DateUtils.getNowTimeStr()),-convert_time));
					long startTime = NumberUtils.getLong(vipStartTime);
					long verifyTime = NumberUtils.getLong(model.getVerify_time());
					if(verifyTime >= startTime) {
						result = false;
						break;
					}
				}
			}
		}
		UserCacheModel userCache = userService.getUserCacheByUserid(user.getUser_id());
		// 如果vip正在等待审核中，则不能申请,或者赠送vip正在审核中，则不能申请兑换vip
		if(userCache.getVip_status() == 2 || userCache.getVip_give_status() == 2 ){
			result = false;
		}
		request.setAttribute("result", result);
		request.setAttribute("credit", credit);
		request.setAttribute("integral", integral);
		saveToken("credit_cash_token");
		return "success";
		
	}
	
	/**
	 * 积分兑换VIP
	 * @return
	 */
	public String cashVip ()throws Exception{
		
		// 提取积分兑换规则信息
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INTEGRAL_VIP.getValue()));
		int integral = rule.getValueIntByKey("integral");//兑换需要的积分
		request.setAttribute("integral", integral);
		
		String tokenMsg = checkToken("credit_cash_token");
		if (!StringUtils.isBlank(tokenMsg)) {
			request.setAttribute("msg", "请勿重复提交！");
			return "result";
		}
		
		User user = (User) session.get(Constant.SESSION_USER);
		// 提取积分兑换规则信息
		creditConvert.setUser_id(user.getUser_id());
		creditConvert.setType(EnumRuleNid.INTEGRAL_VIP.getValue());
		creditConvert.setConvert_num(rule.getValueIntByKey("vip_time"));
		boolean result = userCreditService.integralVIP(creditConvert);
		if(result){
			request.setAttribute("msg", "积分兑换VIP成功！");
		}else request.setAttribute("errormsg", "积分兑换VIP失败！");
		UserCredit credit = userCreditService.getUserCreditByUserId(user.getUser_id());
		request.setAttribute("credit", credit);
		saveToken("credit_cash_token");
		return "result";
	}
	
	//用户积分兑换记录
	public String cashLog(){
		User user = (User) session.get(Constant.SESSION_USER);
		if(user == null) return "error";//如果登陆用户为空，则return登陆页面
		int page=NumberUtils.getInt(request.getParameter("page"));//分页
		String dotime1=request.getParameter("dotime1");//开始时间
		String dotime2=request.getParameter("dotime2");//结束时间
		String status_ = request.getParameter("status");//积分兑换信息类型
		Byte status = null;
		if(status_ != null && status_.length() > 0 ){
			try {
				status = Byte.parseByte(status_);
			} catch (Exception e) {
			}
		}
		
//		long user_id = user.getUser_id();
		SearchParam param = new SearchParam();
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setStatus(status_);
		param.setUsername(user.getUsername());
		//查询用户积分操作记录
		PageDataList pageDataList = creditConvertService.getCreditConvertPage(page, param);;
		//查询所有的积分；类型
		List list = pageDataList.getList();
		request.setAttribute("dotime1",dotime1);//开始时间
		request.setAttribute("dotime2",dotime2);//结束时间
		request.setAttribute("status", status);
		request.setAttribute("convertList", list);
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", param.toMap());
		return "success";
		
	}
	
	public void setUserCreditService(UserCreditService userCreditService) {
		this.userCreditService = userCreditService;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	public void setCreditConvertService(CreditConvertService creditConvertService) {
		this.creditConvertService = creditConvertService;
	}

	public CreditConvert getCreditConvert() {
		return creditConvert;
	}

	public void setCreditConvert(CreditConvert creditConvert) {
		this.creditConvert = creditConvert;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
