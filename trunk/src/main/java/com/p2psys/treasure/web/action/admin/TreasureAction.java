package com.p2psys.treasure.web.action.admin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.common.enums.EnumTreasureAudit;
import com.p2psys.common.enums.EnumTreasureStatus;
import com.p2psys.context.Global;
import com.p2psys.domain.User;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.service.UserService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.treasure.domain.Treasure;
import com.p2psys.treasure.domain.TreasureCash;
import com.p2psys.treasure.model.TreasureCashModel;
import com.p2psys.treasure.model.TreasureModel;
import com.p2psys.treasure.model.TreasureRechargeModel;
import com.p2psys.treasure.model.TreasureUserModel;
import com.p2psys.treasure.service.TreasureCashService;
import com.p2psys.treasure.service.TreasureRechargeService;
import com.p2psys.treasure.service.TreasureService;
import com.p2psys.treasure.service.TreasureUserService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.EntityUtil;
import com.p2psys.web.action.BaseAction;

/**
 * 后台理财宝信息Action
 
 * @version 1.0
 * @since 2013-11-27
 */
public class TreasureAction extends BaseAction implements  ModelDriven<Treasure>{

	@Resource
	private TreasureService treasureService;
	
	@Resource
	private TreasureUserService treasureUserService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private TreasureRechargeService treasureRechargeService;
	
	@Resource
	private TreasureCashService treasureCashService;
	
	private Treasure model = new Treasure();
	
	/**
	 * 后台理财宝分页查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		int pageNo =this.paramInt("page");
		String name = this.paramString("name");
		Map<String, Object> map = new HashMap<String, Object>();
		if(name != null && name.length() > 0){
			map.put("name", name);
		}
		String audit_status = this.paramString("audit_status");
		if(audit_status != null && audit_status.length() > 0){
			map.put("audit_status", Byte.parseByte(audit_status));
		}else map.put("audit_status", Byte.parseByte("-1"));
		
		String status = paramString("status");
		if(status != null && status.length() > 0){
			map.put("status", Byte.parseByte(status));
		}else map.put("status", Byte.parseByte("-1"));
		
		PageDataList page = treasureService.getTreasurePage(pageNo, map);
		List<Treasure> list = page.getList();
		request.setAttribute("itemList", list);
		request.setAttribute("page", page.getPage());
		if(map.size() > 0) request.setAttribute("params", map);
		return "success";
	}
	
	/**
	 * 理财宝信息修改查询
	 * @return
	 */
	public String editInit(){
		long id = this.paramLong("id");
		if(id > 0){
			TreasureModel treasure = treasureService.getTreasureById(id);
			if(treasure != null && treasure.getStatus() != EnumTreasureStatus.STOP.getValue()){
				message("理财宝信息修改，必须先暂停信息后，才可以进行修改操作！", "/admin/treasure/list.html");	
				return ADMINMSG;
			}else{
				request.setAttribute("item", treasure);
				return "success";
			}
		}
		message("查询有误！", "/admin/treasure/list.html");	
		return ADMINMSG;
	}
	
	/**
	 * 理财宝信息修改
	 * @return
	 */
	public String edit(){
		String rsmsg = "修改信息不成功！";
		if(model != null && model.getId() > 0){
			boolean treasure = treasureService.editTreasure(model);
			if(treasure){
				rsmsg="修改信息成功！"	;
			}
		}
		message(rsmsg, "/admin/treasure/list.html");
		return ADMINMSG;
	}
	
	/**
	 * 理财宝信息修改
	 * @return
	 */
	public String add(){
		if(model != null && model.getName() != null){
			String rsmsg = "添加信息不成功！";
			long resultId = treasureService.addTreasure(model);
			if(resultId > 0){
				rsmsg="添加信息成功！"	;
			}
			message(rsmsg, "/admin/treasure/list.html");
			return ADMINMSG;
		}
		return "add";
	}
	
	/**
	 * 审核理财宝信息
	 * @return
	 */
	public String audit(){
		String rsmsg = "审核信息失败！";
		if(model != null && model.getId() > 0 && model.getAudit_status() > 0){
			model.setVerify_user_id(this.getAuthUserId());
			model.setOperator(this.getAuthUserName());
			boolean result = treasureService.auditTreasure(model);
			if(result) rsmsg = "审核信息成功。";
		}else{
			long id = this.paramLong("id");
			TreasureModel treasure = treasureService.getTreasureById(id);
			request.setAttribute("item", treasure);
			return "audit";
		}
		message(rsmsg, "/admin/treasure/list.html");
		return ADMINMSG;
	}
	
	/**
	 * 修改理财宝状态，启用/停用
	 * @return
	 */
	public String editSatus(){
		String rsmsg = "信息未审核通过，修改理财宝状态信息失败！";
		long id = this.paramLong("id");
		String status = this.paramString("status");
		if(id > 0 && status != null){
			TreasureModel treasure = treasureService.getTreasureById(id);
			if(treasure != null && treasure.getAudit_status() == EnumTreasureAudit.PASS_AUDIT.getValue()){
				boolean result = treasureService.editTreasureStatus(id, Byte.parseByte(status), this.getAuthUserName());
				if(result)rsmsg= "修改理财宝状态信息成功！";
			}else{
				rsmsg = "理财宝审核通过，才可以进行停用/启用操作，";
			}
		}
		message(rsmsg, "/admin/treasure/list.html");
		return ADMINMSG;
	}
	
	/**
	 * 用户理财宝信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String userList(){
		int pageNo =this.paramInt("page");
		String username = this.paramString("username");
		Map<String, Object> map = new HashMap<String, Object>();
		if(username != null && username.length() > 0){
			map.put("username", username);
		}
		PageDataList page = treasureUserService.getTreasureUserPage(pageNo, map);
		List<TreasureUserModel> list = page.getList();
		request.setAttribute("itemList", list);
		request.setAttribute("page", page.getPage());
		if(map.size() > 0) request.setAttribute("params", map);
		return "success";
	}
	
	/**
	 * 用户理财宝资金转入信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String rechargeList(){
		int pageNo =this.paramInt("page");
		String username = this.paramString("username");
		String name = this.paramString("name");
		String dotime1 = this.paramString("dotime1");
		String dotime2 = this.paramString("dotime2");
		String status_ = this.paramString("status");
		RuleModel rule=new RuleModel(Global.getRule(EnumRuleNid.TIME_HOUR_ENABLE.getValue()));
		int hour_enable = 0;
		if(rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
			hour_enable=rule.getValueIntByKey("enable");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(username != null && username.length() > 0){
			map.put("username", username);
		}
		if(name != null && name.length() > 0){
			map.put("name", name);
		}
		if(dotime1 != null && dotime1.length() > 0){
			long start_time = DateUtils.valueOf(dotime1).getTime() / 1000;
			if(start_time > 0) map.put("start_time", start_time);
		}
		if(dotime2 != null && dotime2.length() > 0){
			Date d=DateUtils.valueOf(dotime2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}	
			long end_time = (d).getTime() / 1000;
			map.put("end_time", end_time);
		}
		if(status_ != null && status_.length() > 0){
			Byte status = Byte.parseByte(status_);
			map.put("status", status);
		}
		if("export".equals(actionType)){//导出报表
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="treasure_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			String[] names= EntityUtil.findFields(TreasureRechargeModel.class);
			String[] titles=EntityUtil.getExcelTitle(TreasureRechargeModel.class);
			List<TreasureRechargeModel> treasureTenderList =  treasureRechargeService.getTreasureRechargeList(map);
			try {
				ExcelHelper.writeExcel(infile,treasureTenderList, TreasureRechargeModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else{
			PageDataList page = treasureRechargeService.getTreasureRechargePage(pageNo, map);
			List<TreasureRechargeModel> list = page.getList();
			map.remove("start_time");
			map.remove("end_time");
			map.put("dotime1", dotime1);
			map.put("dotime2", dotime2);
			request.setAttribute("itemList", list);
			request.setAttribute("page", page.getPage());
			if(map.size() > 0) request.setAttribute("params", map);
			return "success";
		}
	}
	
	/**
	 * 用户理财宝资金转出信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String cashList(){
		int pageNo =this.paramInt("page");
		String username = this.paramString("username");
		String dotime1 = this.paramString("dotime1");
		String dotime2 = this.paramString("dotime2");
		Map<String, Object> map = new HashMap<String, Object>();
		if(username != null && username.length() > 0){
			map.put("username", username);
		}
		if(dotime1 != null && dotime1.length() > 0){
			long start_time = DateUtils.valueOf(dotime1).getTime() / 1000;
			if(start_time > 0) map.put("start_time", start_time);
		}
		if(dotime2 != null && dotime2.length() > 0){
			long end_time = DateUtils.valueOf(dotime1).getTime();
			if(end_time > 0) map.put("end_time", end_time);
		}
		PageDataList page = treasureCashService.getTreasureCashPage(pageNo, map);
		List<TreasureCashModel> list = page.getList();
		request.setAttribute("itemList", list);
		request.setAttribute("page", page.getPage());
		if(map.size() > 0) request.setAttribute("params", map);
		return "success";
	}
	
	/**
	 * 赎回审核停用/启用
	 * @return
	 */
	public	String editBackStatus(){
		String rsmsg = "信息未审核通过，修改理财宝状态信息失败！";
		long id = this.paramLong("id");
		String back_verify_type = this.paramString("back_verify_type");
		if(id > 0 && back_verify_type != null){
			TreasureModel treasure = treasureService.getTreasureById(id);
			byte pass_audit = EnumTreasureAudit.PASS_AUDIT.getValue();
			byte stop = EnumTreasureStatus.STOP.getValue();
			if(treasure != null && treasure.getAudit_status() == pass_audit && treasure.getStatus() == stop){
				boolean result = treasureService.editBackStatus(id, Byte.parseByte(back_verify_type), this.getAuthUserName());
				if(result)rsmsg= "修改理财宝赎回审核状态信息成功！";
			}else{
				rsmsg = "理财宝审核通过并且停用理财宝，才可以进行赎回审核停用/启用操作，";
			}
		}
		message(rsmsg, "/admin/treasure/list.html");
		return ADMINMSG;
	}
	
	/**
	 * 赎回审核
	 * @return
	 */
	public String auditCash(){
		String rsmsg = "理财宝赎回审核失败！";
		long id = this.paramLong("id");
		if(id > 0){
			String status_ = this.paramString("status");
			if(status_ == null || status_.length() <= 0){
				TreasureCashModel cashModel = treasureCashService.getTreasureCash(model.getId());
				TreasureModel treasureModel = treasureService.getTreasureById(cashModel.getTreasure_id());
				request.setAttribute("cashModel", cashModel);
				request.setAttribute("treasureModel", treasureModel);
				return "audit";
			}else{
				TreasureCash cash = new TreasureCash();
				cash.setVerify_time(System.currentTimeMillis() / 1000);
				cash.setVerify_user_id(this.getAuthUserId());
				cash.setVerify_user(this.getAuthUserName());
				cash.setStatus(Byte.parseByte(status_));
				cash.setId(id);
				cash.setRemark(this.paramString("remark"));
				boolean result = treasureCashService.auditCash(cash);
				if(result) rsmsg = "理财宝赎回审核成功！";
			}
		}
		message(rsmsg, "/admin/treasure/cashList.html");
		return ADMINMSG;
	}
	
	
	
	
	
	/**
	 * 理财宝用户验证
	 * @return
	 */
	public String checkUser(){
		try {
			String username = this.paramString("username");
			if(username == null || username.length() <= 0) return null;
			User user = userService.getUserByName(username);
			if(user == null || user.getUsername() == null || !username.equals(user.getUsername())) return null;
			printJson(JSON.toJSONString(user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public Treasure getModel() {
		// TODO Auto-generated method stub
		return model;
	}

}
