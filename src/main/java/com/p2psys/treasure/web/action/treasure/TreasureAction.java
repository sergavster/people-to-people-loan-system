package com.p2psys.treasure.web.action.treasure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.p2psys.common.enums.EnumTreasureAudit;
import com.p2psys.common.enums.EnumTreasureStatus;
import com.p2psys.domain.Account;
import com.p2psys.domain.User;
import com.p2psys.model.PageDataList;
import com.p2psys.model.account.AccountModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.UserService;
import com.p2psys.tool.coder.MD5;
import com.p2psys.treasure.domain.Treasure;
import com.p2psys.treasure.domain.TreasureRecharge;
import com.p2psys.treasure.domain.TreasureUser;
import com.p2psys.treasure.model.TreasureCashModel;
import com.p2psys.treasure.model.TreasureModel;
import com.p2psys.treasure.model.TreasureRechargeModel;
import com.p2psys.treasure.service.TreasureCashService;
import com.p2psys.treasure.service.TreasureRechargeService;
import com.p2psys.treasure.service.TreasureService;
import com.p2psys.treasure.service.TreasureUserService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 理财宝前台action
 
 * @version 1.0
 * @since 2013-11-30
 */
public class TreasureAction extends BaseAction {

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
	
	@Resource
	private AccountService accountService;
	
	private TreasureRecharge rechargeItem;
	
	public String detail(){
		User user = this.getSessionUser();
		long id = this.paramLong("id");
		if(id > 0){
			TreasureModel item = treasureService.getTreasureById(id);
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("treasure_id", id);
			map.put("status", Byte.parseByte("1"));
			map.put("size", 50);
			List<TreasureRechargeModel> rechargeList =treasureRechargeService.getTreasureRechargeList(map);
			request.setAttribute("item", item);
			request.setAttribute("rechargeList", rechargeList);
			
		}else return "fail";
		if(user != null){
			Account account = accountService.getAccount(user.getUser_id());
			request.setAttribute("account", account);
		}
		return "success";
	}
	
	/**
	 * 理财宝详情页
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
		map.put("audit_status", EnumTreasureAudit.PASS_AUDIT.getValue());
		map.put("status", EnumTreasureStatus.START.getValue());
		PageDataList page = treasureService.getTreasurePage(pageNo, map);
		List<Treasure> list = page.getList();
		request.setAttribute("itemList", list);
		request.setAttribute("page", page.getPage());
		if(map.size() > 0) request.setAttribute("params", map);
		return "success";
	}
	
	/**
	 * 理财宝详情页
	 * @return
	 */
	public String cash(){
		User user = this.getSessionUser();
		long id = this.paramLong("id");
		double money = this.paramDouble("money");
		if(id > 0 && user != null){
			TreasureModel item = treasureService.getTreasureById(id);
			AccountModel account = accountService.getAccount(user.getUser_id());
			TreasureUser treasureUser = treasureUserService.getTreasureUserByUserId(user.getUser_id());
			request.setAttribute("treasureUser", treasureUser);
			request.setAttribute("item", item);
			request.setAttribute("account", account);
		}else return "fail";
		request.setAttribute("money", money);
		return "success";
	}

	/**
	 * 理财宝投资
	 * @return
	 */
	public String invest(){
		User user = this.getSessionUser();
		String msg = "";
		String paypwdStr = this.paramString("paypassword");
		String mypaypwd = StringUtils.isNull(user.getPaypassword());
		String valicode = this.paramString("valicode");
		if(StringUtils.isBlank(valicode)){
			msg = "验证码不能为空。";
		}else if(!checkValidImg(valicode)){
			msg = "验证码不正确，请重新输入。";
		}
		if (paypwdStr.equals("")) {
			msg = "请输入支付密码。";
		}
		MD5 md5 = new MD5();
		paypwdStr = md5.getMD5ofStr(paypwdStr);
		if (!mypaypwd.equals(paypwdStr)) {
			msg = "支付密码不正确。";
		}
		if(msg.length() == 0 && rechargeItem != null && rechargeItem.getTreasure_id() > 0 && rechargeItem.getMoney() > 0){
			rechargeItem.setUser_id(user.getUser_id());
			boolean result = treasureService.invest(rechargeItem);
			if(result) msg = "投资成功！";
		}
		if(msg.length() == 0){
			 msg = "投资失败！";
		}
		request.setAttribute("msg", msg);
		TreasureModel item = treasureService.getTreasureById(rechargeItem.getTreasure_id());
		AccountModel account = accountService.getAccount(user.getUser_id());
		TreasureUser treasureUser = treasureUserService.getTreasureUserByUserId(user.getUser_id());
		request.setAttribute("treasureUser", treasureUser);
		request.setAttribute("item", item);
		request.setAttribute("account", account);
		return "success";
	}
	
	/**
	 * 用户理财宝资金转入信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String rechargeList(){
		User user = this.getSessionUser();
		int pageNo =this.paramInt("page");
		String name = this.paramString("name");
		String dotime1 = this.paramString("dotime1");
		String dotime2 = this.paramString("dotime2");
		Map<String, Object> map = new HashMap<String, Object>();
		if(name != null && name.length() > 0){
			map.put("name", name);
		}
		if(dotime1 != null && dotime1.length() > 0){
			long start_time = DateUtils.valueOf(dotime1).getTime() / 1000;
			if(start_time > 0) map.put("start_time", start_time);
		}
		if(dotime2 != null && dotime2.length() > 0){
			long end_time = DateUtils.valueOf(dotime1).getTime() / 1000;
			if(end_time > 0) map.put("end_time", end_time);
		}
		map.put("user_id", user.getUser_id());
		PageDataList page = treasureRechargeService.getTreasureRechargePage(pageNo, map);
		List<TreasureRechargeModel> list = page.getList();
		request.setAttribute("itemList", list);
		request.setAttribute("page", page.getPage());
		if(map.size() > 0) request.setAttribute("params", map);
		return "success";
	}
	
	/**
	 * 用户理财宝资金转出信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String cashList(){
		User user = this.getSessionUser();
		int pageNo =this.paramInt("page");
		String dotime1 = this.paramString("dotime1");
		String dotime2 = this.paramString("dotime2");
		Map<String, Object> map = new HashMap<String, Object>();
		if(dotime1 != null && dotime1.length() > 0){
			long start_time = DateUtils.valueOf(dotime1).getTime() / 1000;
			if(start_time > 0) map.put("start_time", start_time);
		}
		if(dotime2 != null && dotime2.length() > 0){
			long end_time = DateUtils.valueOf(dotime1).getTime() / 1000;
			if(end_time > 0) map.put("end_time", end_time);
		}
		map.put("user_id", user.getUser_id());
		PageDataList page = treasureCashService.getTreasureCashPage(pageNo, map);
		List<TreasureCashModel> list = page.getList();
		request.setAttribute("itemList", list);
		request.setAttribute("page", page.getPage());
		if(map.size() > 0) request.setAttribute("params", map);
		return "success";
	}
	
	/**
	 * 资金转出
	 * @return
	 */
	public String treasureCash(){
		long id = this.paramLong("id");
		long treasure_id = this.paramLong("treasure_id");
		User user = this.getSessionUser();
		if(treasure_id <= 0 || id <= 0 || user == null) return "fail";
		treasureCashService.treasureCash(treasure_id, id, user.getUser_id());
		return "success";
	}
	
	public TreasureRecharge getRechargeItem() {
		return rechargeItem;
	}

	public void setRechargeItem(TreasureRecharge rechargeItem) {
		this.rechargeItem = rechargeItem;
	}
}
