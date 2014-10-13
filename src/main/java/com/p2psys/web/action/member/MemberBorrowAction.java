package com.p2psys.web.action.member;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Repayment;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAmount;
import com.p2psys.domain.UserAmountApply;
import com.p2psys.domain.UserCache;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.AccountService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.MemberBorrowService;
import com.p2psys.service.UserAmountService;
import com.p2psys.service.UserService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;
import com.p2psys.web.action.BorrowAction;

public class MemberBorrowAction extends BaseAction implements ModelDriven<UserAmountApply> {
	
	private static Logger logger = Logger.getLogger(BorrowAction.class);
	
	private MemberBorrowService memberBorrowService;
	private UserService userService;
	private AccountService accountService;
	private UserAmountService userAmountService;
	private BorrowService borrowService;
	public BorrowService getBorrowService() {
		return borrowService;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	private UserAmountApply amount=new UserAmountApply();

	/**
	 * 查看用户的借款情况
	 * @return
	 */
	public String borrow() {
		User user = (User) session.get(Constant.SESSION_USER);
		SearchParam param=new SearchParam();
		String type = StringUtils.isNull(request.getParameter("type"));
		if(StringUtils.isBlank(type)){
			type="publish";
		}
		
		// 查询条件
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		String keywords = StringUtils.isNull(request.getParameter("keywords"));
		int page=NumberUtils.getInt(request.getParameter("page"));
		
		param.setKeywords(keywords);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.addMap("type", type);
		

		PageDataList list = memberBorrowService.getList(type, user.getUser_id(), page, param);

		request.setAttribute("borrowList", list.getList());
		request.setAttribute("type", type);
		request.setAttribute("p", list.getPage());
		request.setAttribute("param", param.toMap());
		// 保存分页信息
		
		return "success";
	}
	
	// v1.6.7.2 RDPROJECT-471 zza 2013-11-29 start
	/**
	 * 查看审核不通过的借款标
	 * @return String
	 */
	public String borrowVerifyFail() {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		String type = StringUtils.isNull(request.getParameter("type"));
		if (StringUtils.isBlank(type)) {
			type = "verifyFail";
		}
		int page = NumberUtils.getInt(request.getParameter("page"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		String keywords = StringUtils.isNull(request.getParameter("keywords"));
		SearchParam param = new SearchParam();
		param.setKeywords(keywords);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.addMap("type", type);
		PageDataList list = memberBorrowService.getVerifyFailList(user_id,
				page, param, type);
		request.setAttribute("failList", list.getList());
		request.setAttribute("type", type);
		request.setAttribute("p", list.getPage());
		request.setAttribute("param", param.toMap());
		return "success";
	}
	// v1.6.7.2 RDPROJECT-471 zza 2013-11-29 end
	
	public String limitapp() {
		User user=(User)session.get(Constant.SESSION_USER);
		long user_id=user.getUser_id();
		String action_type=request.getParameter("action_type");
		int page=NumberUtils.getInt(StringUtils.isNull(request.getAttribute("page")));
		//申请中的信用申请列表
		List amountlist=userAmountService.getUserAmountApply(user_id);
		UserCache userCache=userService.getUserCacheByUserid(user_id);
		User newUser=userService.getUserById(user.getUser_id());
		session.put(Constant.SESSION_USER, newUser);
		
		if(amountlist.size()>0){
			request.setAttribute("amountlist", amountlist);
			request.setAttribute("cache", userCache);
		}else{
			if(StringUtils.isNull(action_type).equals("add")){
				amount.setUser_id(user_id);
				amount.setAddtime(new Date().getTime()/1000+"");
				amount.setAddip("");
				amount.setStatus(2);//默认提交管理员审核
				//v1.6.7.1 RDPROJECT-433 liukun 2013-11-08 start
				UserAmount ua=userAmountService.getUserAmount(user_id);
				amount.setAccount_old(ua.getCredit());
				//v1.6.7.1 RDPROJECT-433 liukun 2013-11-08 end
				userAmountService.add(amount);
			}
			amountlist=userAmountService.getUserAmountApply(user_id);
			request.setAttribute("amountlist", amountlist);
			request.setAttribute("cache", userCache);
		}
		PageDataList hasApplyList=userAmountService.getAmountApplyByUserid(user_id,page, new SearchParam());
		request.setAttribute("list", hasApplyList.getList());
		request.setAttribute("param", hasApplyList.getPage());
		return SUCCESS;
	}
	
	
	/**
	 * 还款明细
	 * @return String
	 */
	public String repaymentdetail() {
		User user = (User) session.get(Constant.SESSION_USER);
		long userId = user.getUser_id();
		long borrowId = NumberUtils.getLong(request.getParameter("borrowId"));
		// 查询条件
		String repayment_time1 = StringUtils.isNull(request.getParameter("repayment_time1"));
		String repayment_time2 = StringUtils.isNull(request.getParameter("repayment_time2"));
		String keywords = StringUtils.isNull(request.getParameter("keywords"));
		SearchParam param = new SearchParam();
		param.setKeywords(keywords);
		param.setRepayment_time1(repayment_time1);
		param.setRepayment_time2(repayment_time2);
		
		// V1.6.6.2 RDPROJECT-346 ljd 2013-10-21 start
		List<Repayment> repay = null;
		if (borrowId > 0) {
			// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 start
			repay = memberBorrowService.getRepaymentList(param, userId, borrowId);
			// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 end
		} else {
			int page = NumberUtils.getInt(request.getParameter("page"));
			PageDataList pDataList = memberBorrowService.getRepaymentList(param, userId, page);
			repay = pDataList.getList();
			request.setAttribute("page", pDataList.getPage());
			request.setAttribute("param", param.toMap());
		}
		
		/*List repay=null;
		if(borrowId>0){
			repay=memberBorrowService.getRepaymentList(user_id,borrowId);
		}else{
			repay = memberBorrowService.getRepaymentList(user_id);
		}*/
		// V1.6.6.2 RDPROJECT-346 ljd 2013-10-21 end
		
		java.util.Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        request.setAttribute("currentTime", sdf.format(date)); 
		request.setAttribute("today", DateUtils.getTimeStr(date));
		request.setAttribute("repay", repay);
		return "success";
	}
	
	public String borrowdetail() {
		User user=(User)session.get(Constant.SESSION_USER);
		long user_id=user.getUser_id();
		long borrowId=NumberUtils.getLong(request.getParameter("borrowId"));
		List list=memberBorrowService.getBorrowTenderListByUserid(user_id);
		request.setAttribute("tender", list);
		return "success";
	}
	
	/**
	 * 查找具体还款信息
	 * @return
	 */
	public String repayment(){
		String cidstr=request.getParameter("");
		int cid=NumberUtils.getInt(cidstr);
		DetailCollection dc=memberBorrowService.getCollection(cid);
		if(cid==0||dc==null){
			return "notfound";
		}
		//具体还款信息
		
		return "success";
	}

	/**
	 * 还款操作
	 * @return
	 */
	public String repay(){
		boolean isOk = true;
		String checkMsg = "";
		long user_id = this.getSessionUser().getUser_id();
		Account act = accountService.getAccount(user_id);
		long repayid = paramLong("id");
		Repayment repay = memberBorrowService.getRepayment(repayid);
		if (repay == null) {
			message("未找到对应的还款计划，还款失败！", "/member/borrow/borrow.html?type=repayment");
			return MSG;
		}
		
		try {
			memberBorrowService.doRepay(repay,act);
		} catch (Exception e) {
			isOk=false;
			checkMsg=e.getMessage();
		}
		if (isOk) {
			message("还款成功！", "/member/borrow/borrow.html?type=repayment");
		} else {
			message(checkMsg, "/member/borrow/borrow.html?type=repayment");
		}
		return MSG;
	}
    
	
	public MemberBorrowService getMemberBorrowService() {
		return memberBorrowService;
	}
	public void setMemberBorrowService(MemberBorrowService memberBorrowService) {
		this.memberBorrowService = memberBorrowService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Override
	public UserAmountApply getModel() {
		return amount;
	}
	public UserAmountService getUserAmountService() {
		return userAmountService;
	}
	public void setUserAmountService(UserAmountService userAmountService) {
		this.userAmountService = userAmountService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
}
