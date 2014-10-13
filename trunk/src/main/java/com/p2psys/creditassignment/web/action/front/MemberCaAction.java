package com.p2psys.creditassignment.web.action.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.creditassignment.domain.CreditAssignment;
import com.p2psys.creditassignment.service.CreditAssignmentService;
import com.p2psys.domain.Account;
import com.p2psys.domain.Collection;
import com.p2psys.domain.Tender;
import com.p2psys.domain.User;
import com.p2psys.model.CaSearchParam;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.UserService;
import com.p2psys.tool.coder.MD5;
import com.p2psys.treasure.domain.Treasure;
import com.p2psys.util.CaUtil;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class MemberCaAction extends BaseAction {
	
	@Resource
	private CreditAssignmentService creditAssignmentService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private BorrowService borrowService;
	
	@Resource
	private AccountService accountService;
	
	public String listCas(){
		int page=this.paramInt("page");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("status", Constant.CA_STATUS_VERIFY_SUCC);
		PageDataList pList=creditAssignmentService.getPageCAS(page, map);
		request.setAttribute("itemList", pList.getList());
		request.setAttribute("page", pList.getPage());
		request.setAttribute("param",map);
		return SUCCESS;
	}
	
	/**
	 * 查看债权
	 * @return
	 */
	public String view(){
		long caId = this.paramLong("caId");
		CreditAssignment creditAssignment = creditAssignmentService.getOne(caId);
		if(creditAssignment == null || creditAssignment.getRelated_borrow_id() <= 0){
			message("债权异常，请联系系统管理。");
			return MSG;
		}
		if(creditAssignment.getStatus() != Constant.CA_STATUS_VERIFY_SUCC ){
			message("债权转让所处状态不能被购买。");
			return MSG;
		}
		long borrowId = creditAssignment.getRelated_borrow_id();
		long caUserId = creditAssignment.getSell_user_id();
		BorrowModel borrowModel =  borrowService.getBorrow(borrowId);
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("borrowId", borrowId);
		map.put("user_id", caUserId);
		List<Collection> collectionList = borrowService.getCollectionList(map);
		User borrowUser = userService.getUserById(borrowModel.getUser_id());
		User caUser = userService.getUserById(caUserId);
		request.setAttribute("collectionList", collectionList);
		request.setAttribute("caUser", caUser);
		request.setAttribute("ca", creditAssignment);
		request.setAttribute("borrow", borrowModel);
		request.setAttribute("borrowUser", borrowUser);
		return SUCCESS;
	}
	
	
	/**
	 * 发布债权转让
	 * @return
	 */
	public String add(){
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		
		long fk_id = paramLong("fkId");
		double creditPrice = paramDouble("creditPrice");
		String pwd = this.paramString("pwd");
		String buyEndTime = this.paramString("buyEndTime");
		byte type = Byte.parseByte(this.paramString("type"));
		long buy_end_time = 0;
		if(buyEndTime != null && buyEndTime.length() > 0){
			buy_end_time = DateUtils.valueOf(buyEndTime).getTime() / 1000;
		}
		CreditAssignment ca = new CreditAssignment();
		// 债权转让定向密码，可以不设置定向密码
		if(pwd != null && pwd.length() > 0){
			MD5 md5 = new MD5();
			String pwdStr = md5.getMD5ofStr(pwd);
			ca.setPwd(pwdStr);
		}
		
		if(type == Constant.CA_TYPE_BORROW){
			ca.setRelated_borrow_id(fk_id);
		}else if(type == Constant.CA_TYPE_TENDER){
			Tender tender = borrowService.getTenderById(fk_id);
			ca.setRelated_tender_id(fk_id);
			ca.setRelated_borrow_id(tender.getBorrow_id());
		}else if(type == Constant.CA_TYPE_COLLECTION){
			Collection coll = borrowService.getCollectionById(fk_id);
			ca.setRelated_borrow_id(coll.getBorrow_id());
			ca.setRelated_tender_id(coll.getTender_id());
			ca.setRelated_collection_id(fk_id);
		}
		ca.setType(type);
		ca.setSell_user_id(user_id);
		ca.setCredit_price(creditPrice);
		ca.setCredit_value(creditAssignmentService.calCaValue(user_id, fk_id,type));
		ca.setBuy_end_time(buy_end_time);
		
		// 验证债权信息
		Map<String , Object> ckr = checkAddCa(ca);
		boolean ckResult = Boolean.valueOf(ckr.get("result").toString()).booleanValue();
		String message = ckr.get("message").toString();
		
		if (!ckResult){
			message(message);
			return MSG;
		}
		//验证通过
		boolean rusult = creditAssignmentService.add(ca);
		if(rusult){
			message("债权发布成功，进入待审核状态。");
		}else{
			message("债权发布失败。");
		}
		return MSG;
	}
	
	public String cancel(){
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		
		long caId = NumberUtils.getLong(request.getParameter("caId"));
		
		double buyAccount = NumberUtils.getDouble2(request.getParameter("buyAccount"));
		
		//获取购买者的帐户实体和债权转让的实体并传到下层调用者，避免重复获取
		Account sellerAct = accountService.getAccount(user_id);
		
		CreditAssignment ca = creditAssignmentService.getOne(caId);
		
		Map<String , Object> ckr = checkCancelCa(sellerAct, ca);
		
		boolean ckResult = Boolean.valueOf(ckr.get("result").toString()).booleanValue();
		String message = ckr.get("message").toString();
		
		if (!ckResult){
			request.setAttribute("message", message);
			return FAIL;
		}
		//执行撤回的核心方法
		creditAssignmentService.cancel(sellerAct, ca);
		
		return SUCCESS;
	}
	
	public String listOutCas(){
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		Map<String ,Object> param=new HashMap<String, Object>();
		PageDataList pList=null;
		pList=creditAssignmentService.getPageCAS(page, param);
		request.setAttribute("param", param);
		return SUCCESS;
	}
	
	public String buy(){
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		long caId = this.paramLong("caId");
		double buyAccount = this.paramDouble("buyAccount");
		String paypassword = this.paramString("paypassword");
		String pwd = this.paramString("pwd");
		
		//获取购买者的帐户实体和债权转让的实体并传到下层调用者，避免重复获取
		Account buyerAct = accountService.getAccount(user_id);
		
		CreditAssignment ca = creditAssignmentService.getOne(caId);
		
		//检测是否可以购买
		Map<String , Object> ckr = checkBuyCa(buyerAct, ca, buyAccount,paypassword,pwd);
		
		
		boolean ckResult = Boolean.valueOf(ckr.get("result").toString()).booleanValue();
		String message = ckr.get("message").toString();
		
		if (!ckResult){
			message(message);
			return MSG;
		}
		
		//获取实际能认购的金额
		double realBuyAccount = calRealBuyAccount(buyerAct, ca, buyAccount);
		
		//调用核心方法认购债权
		creditAssignmentService.buy(buyerAct, ca, realBuyAccount);
		
		return SUCCESS;
	}
	
	public String listInCas(){
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		
		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		CaSearchParam param=new CaSearchParam();
		PageDataList pList=null;
		
		pList=creditAssignmentService.listInCas(page, param);
		
		setPageAttribute(pList, param);
		request.setAttribute("param", param.toMap());
		return SUCCESS;
	}
	
	/**
	 * 债权转让发布数据有效性检查
	 * @return
	 */
	private Map<String , Object> checkAddCa(CreditAssignment ca){
		
		String message = "ok";
		long user_id = ca.getSell_user_id();
		long fk_id = this.caFkID(ca);
		byte type = ca.getType();
		Map<String , Object> mapCa = new HashMap<String, Object>();
		long buyEndTime  = ca.getBuy_end_time();
		double credit_price =  ca.getCredit_price();
		
		User user = (User) session.get(Constant.SESSION_USER);
		long session_user_id = user.getUser_id();
		
		//是否慢足系统设置的转让规则
		if(null != Global.getRule(EnumRuleNid.CA_ADD_CHECK.getValue())){
			//message = "没有配置验证规则";
			//TODO 处理规则
			RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.CA_ADD_CHECK.getValue()));
		}
		
		//只能发布自己的债权
		if(user_id <= 0 || session_user_id <= 0 || session_user_id != user_id){
			message = "转让的债权用户账户不正确";
		}
		
		long currTime = DateUtils.getNowTime();
		if(buyEndTime <= currTime){
			message = "转让的债权截止时间不正确";
		}
		
		//必须还有待收债权
		double creditValue = creditAssignmentService.calCaValue(user_id, fk_id , type);
		if (0 >= creditValue){
			message = "没有可以转让的债权";
		}
		
		if(credit_price <= 0){
			message = "您债权的转让价格有误，请核对后再进行转让";
		}
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("sell_user_id", user_id);
		map.put(this.caFkStrName(ca), fk_id);
		List<Byte> statusList = new ArrayList<Byte>();
		statusList.add(Constant.CA_STATUS_INIT);
		statusList.add(Constant.CA_STATUS_VERIFY_SUCC);
		statusList.add(Constant.CA_STATUS_INIT);
		map.put("statusList", statusList);
		List<CreditAssignment> creaditAssList = creditAssignmentService.getList(map);
		if(creaditAssList != null && creaditAssList.size() > 0){
			message = "您的此债权已经在转让中，不能再次转让。";
		}
		
		Map<String , Object> resultHM = new HashMap<String , Object>();
		resultHM.put("message", message);
		if(!"ok".equalsIgnoreCase(message)){
			resultHM.put("result", false);
		}else{
			resultHM.put("result", true);
		}
		
		return resultHM;
	}
	
	/**
	 * 撤回债权检查
	 * @param sellerAct 操作者资金帐户实体数据
	 * @param creditAssignment 债权实体数据
	 * @return
	 */		
	private Map<String , Object> checkCancelCa(Account sellerAct, CreditAssignment ca){
		String message = "ok";
		//检查是否是自己发布的债权转让
		if (ca.getSell_user_id() != sellerAct.getUser_id()){
			message = "只能撤回自己发布的债权转让";
		}
		
		//检查债权转让状态是否有效，只有初审通过的状态可以被撤回
		if(ca.getStatus() != Constant.CA_STATUS_VERIFY_SUCC){
			message = "债权转让所处状态不能被撤回";
		}
		
		Map<String , Object> resultHM = new HashMap<String , Object>();
		resultHM.put("message", message);
		if(!"ok".equalsIgnoreCase(message)){
			resultHM.put("result", false);
		}else{
			resultHM.put("result", true);
		}
		
		return resultHM;
	}
	
	private double calRealBuyAccount(Account buyerAct, CreditAssignment ca, double buyAccount){
		//判断是否还有足够的债权份额供购买
		double total_account = ca.getCredit_price();
		double sold_account = ca.getSold_account();
		
		//如果剩余可购份数小于准备购买的份数，那么实际购买份数就是可购份数
		double can_sell_account = ((total_account - sold_account) < buyAccount)?(total_account - sold_account):buyAccount;
		
		return can_sell_account;
	}
	
	
	/**
	 * 认购债权检查
	 * @param buyerAct 认购者资金帐户实体数据
	 * @param creditAssignment 债权实体数据
	 * @param buyAccount 认购金额
	 * @return
	 */	
	private Map<String , Object> checkBuyCa(Account buyerAct, CreditAssignment ca, double buyAccount , String paypassword , String pwd){
		String message = "ok";
		
		// 检查用的支付密码
		User user = (User) session.get(Constant.SESSION_USER);
		if(paypassword != null && paypassword.length() > 0){
			MD5 md5 = new MD5();
			String paypwdStr = md5.getMD5ofStr(paypassword);
			String myPayPwd = user.getPaypassword();
			if(myPayPwd == null || myPayPwd.length() <= 0) message="请设置支付密码。";
			if (!myPayPwd.equals(paypwdStr)) message = "支付密码不正确。";
		}else{
			message="请输入支付密码。";
		}
		
		// 如果用户设置了定向密码，则检查购买者收入的定向密码
		String myPwd = ca.getPwd();
		if(myPwd != null && myPwd.length() > 0){
			MD5 md5 = new MD5();
			String pwdStr = md5.getMD5ofStr(pwd);
			if(pwd == null || pwd.length() <= 0) message="请输入定向密码。";
			if (!myPwd.equals(pwdStr)) message = "您收入的定向密码有误。";
		}
		
		//检查是否是自己购买债权
		if (ca.getSell_user_id() == buyerAct.getUser_id()){
			message = "不能购买自己转让的债权";
		}
		
		//检查债权转让状态是否有效，只有初审通过的状态可以被认购
		if(ca.getStatus() != Constant.CA_STATUS_VERIFY_SUCC){
			message = "债权转让所处状态不能被购买";
		}
		
		//算出实际能购买的金额，并用这个来计算手续费和实际冻结的总金额
		double realAccount = calRealBuyAccount(buyerAct, ca, buyAccount);
		double buyFee = CaUtil.calBuyFee(realAccount);
		//
		//检查是否有足够的可用余额
		if(buyerAct.getUse_money() < (realAccount + buyFee)){
			message = "可用余额不足。（当前您 的可用余额为"+NumberUtils.format2(buyerAct.getUse_money())+"元，需要支付认购金额"+NumberUtils.format2(realAccount)+"元，认购手续费"+NumberUtils.format2(buyFee)+"元）";
		}
		
		//债权转让已经过期
		if(ca.getBuy_end_time() < DateUtils.getNowTime()){
			message = "债权转让已经过期，不能购买";
		}
		
		//检测是否还有可认购份额
		if(ca.getCredit_price() <= ca.getSold_account()){
			message = "此债权已经全部认购完";
		}
		
		long currTime = DateUtils.getNowTime();
		if(ca.getBuy_end_time() <= currTime){
			message = "此债权已经截止";
		}
		
		Map<String , Object> resultHM = new HashMap<String , Object>();
		resultHM.put("message", message);
		if(!"ok".equalsIgnoreCase(message)){
			resultHM.put("result", false);
		}else{
			resultHM.put("result", true);
		}
		
		return resultHM;
	}
	
	public String publish(){
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		long fkId = this.paramLong("fkId");
		byte type = Byte.parseByte(this.paramString("type"));
		if(fkId <= 0 || type <= 0){
			message("发布债权异常，请联系系统管理。");
			return MSG;
		}
		Map<String , Object> mapCas = new HashMap<String, Object>();
		Map<String , Object> map = new HashMap<String, Object>();
		if(type == Constant.CA_TYPE_BORROW){// 此发布的债券的级别borrow类型
			mapCas.put("related_borrow_id", fkId);
			map.put("borrowId", fkId);
		}else if(type == Constant.CA_TYPE_TENDER){// 此发布的债券的级别tender的类型
			mapCas.put("related_tender_id", fkId);
			map.put("tenderId", fkId);
		}else if(type == Constant.CA_TYPE_COLLECTION){// 此发布的债券的级别collection的类型
			mapCas.put("related_collection_id", fkId);
			map.put("collectionId", fkId);
		}else{
			message("发布债权异常，请联系系统管理。");
			return MSG;
		}
		mapCas.put("sell_user_id", user_id);
		List<Byte> statusList = new ArrayList<Byte>();
		statusList.add(Constant.CA_STATUS_INIT);
		statusList.add(Constant.CA_STATUS_VERIFY_SUCC);
		statusList.add(Constant.CA_STATUS_INIT);
		mapCas.put("statusList", statusList);
		List<CreditAssignment> creaditAssList = creditAssignmentService.getList(mapCas);
		if(creaditAssList != null && creaditAssList.size() > 0){
			request.setAttribute("isPublish", false);
		}else{
			request.setAttribute("isPublish", true);
		}
		map.put("user_id", user_id);
		List<Collection> collectionList = borrowService.getCollectionList(map);
		Collection coll = new Collection();
		if(collectionList != null && collectionList.size() > 0){
			coll = collectionList.get(0);
		}else{
			message("此债权无待收信息，不能发布债权信息。");
			return MSG;
		}
		
		BorrowModel borrowModel = borrowService.getBorrow(coll.getBorrow_id());
		double caValue = creditAssignmentService.calCaValue(user_id, fkId , type);
		User borrowUser = userService.getUserById(borrowModel.getUser_id());
		request.setAttribute("borrowUser", borrowUser);
		request.setAttribute("borrow", borrowModel);
		request.setAttribute("caValue", caValue);
		request.setAttribute("fkId", fkId);
		request.setAttribute("type", type);
		request.setAttribute("collectionList", collectionList);
		return SUCCESS;
	}
	
	/**
	 * 获取本债权的关联主键ID
	 * @param ca
	 * @return
	 */
	public long caFkID(CreditAssignment ca){
		long fk_id = 0;
		byte type = ca.getType();
		if(type == Constant.CA_TYPE_BORROW){
			fk_id = ca.getRelated_borrow_id();
		}else if(type == Constant.CA_TYPE_TENDER){
			fk_id = ca.getRelated_tender_id();
		}else if(type == Constant.CA_TYPE_COLLECTION){
			fk_id = ca.getRelated_collection_id();
		}
		return fk_id;
	}
	

	/**
	 * 获取本债权的关联主键ID
	 * @param ca
	 * @return
	 */
	public String caFkStrName(CreditAssignment ca){
		String fk_id_name = "";
		byte type = ca.getType();
		if(type == Constant.CA_TYPE_BORROW){
			fk_id_name = "related_borrow_id";
		}else if(type == Constant.CA_TYPE_TENDER){
			fk_id_name = "related_tender_id";
		}else if(type == Constant.CA_TYPE_COLLECTION){
			fk_id_name = "related_collection_id";
		}
		return fk_id_name;
	}
}
