package com.p2psys.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
//import org.apache.struts2.json.JSONUtil;


import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.Attestation;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Friend;
import com.p2psys.domain.User;
import com.p2psys.domain.Userinfo;
import com.p2psys.model.DetailUser;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UnionSearchParam;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.UserBorrowModel;
import com.p2psys.model.account.AccountModel;
import com.p2psys.model.borrow.BaseBorrowModel;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.CommentService;
import com.p2psys.service.FriendService;
import com.p2psys.service.MemberBorrowService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

public class PointsAction extends BaseAction implements ModelDriven<BorrowModel>{
	private static Logger logger = Logger.getLogger(PointsAction.class); 
	/**
	 * Spring注入
	 */
	private BorrowService borrowService;
	private UserService userService;
	private UserinfoService userinfoService;
	private CommentService commentService;
	private AccountService accountService;
	private MemberBorrowService memberBorrowService;
	private FriendService friendService;

	public FriendService getFriendService() {
		return friendService;
	}
	public void setFriendService(FriendService friendService) {
		this.friendService = friendService;
	}
	private PageDataList borrowList;
	
	BorrowModel model=new BorrowModel();


	@Override
	public BorrowModel getModel() {
		return model;
	}

	/**	
	 * 我要投资的首页
	 * @return
	 * @throws Exception
	 */
	public String index() throws Exception {
		String webid=Global.getValue("webid");
		if(webid!=null&&webid.equals("mdw")){
			List<UserBorrowModel> borrowList1=borrowService.getList();
		request.setAttribute("borrowList1", borrowList1);
	   }else{
		checkParams();
		// v1.6.7.1 dw_borrow表字段修改 lhm 2013-11-05 start
//		BorrowModel wrapModel=BorrowHelper.getHelper(Constant.TYPE_ALL,model);
		BorrowModel wrapModel = new BaseBorrowModel(model);
		// v1.6.7.1 dw_borrow表字段修改 lhm 2013-11-05 end
		model.setParam(getParam());
		
		
		borrowList=borrowService.getList(wrapModel);
		request.setAttribute("borrowList", borrowList.getList());
		
		List successList=borrowService.getSuccessListForIndex("", 5);
		request.setAttribute("successList", successList);
		session.put("newborrowlist", borrowList.getList());
		request.setAttribute("p", borrowList.getPage());
		//保存排序信息
		request.setAttribute("orderstr",wrapModel.getModel().getOrderStr());
		logger.info(wrapModel.getModel().getOrderStr());
		//保存搜索信息
		request.setAttribute("map", wrapModel.getModel().getParam().toMap());
		}
		return "success";
    }
	
	private SearchParam getParam(){
		String search=StringUtils.isNull(request.getParameter("search"));
		String sBorrowType=StringUtils.isNull(request.getParameter("sType"));
		String sLilv=StringUtils.isNull(request.getParameter("sApr"));
		String sLimit=StringUtils.isNull(request.getParameter("sLimit"));
		String sAccount=StringUtils.isNull(request.getParameter("sAccount"));
		String sOrder=StringUtils.isNull(request.getParameter("order"));
		
		String sUse=StringUtils.isNull(request.getParameter("use"));
		String sKeywords=StringUtils.isNull(request.getParameter("keywords"));
		if(Global.getWebid().equals("wzdai")){
			 search=StringUtils.isBlank(search)?"select":search;
		}
		if(search.equals("union")){
			UnionSearchParam param=new UnionSearchParam(sBorrowType,sLilv,sLimit,sAccount,sOrder);
			String webid=Global.getValue("webid");
			if(StringUtils.isNull(webid).equals("zrzb")){
				param.setOrder(11);
			}else if(StringUtils.isNull(webid).equals("hndai")){
				if(sOrder!=null&&!"".equals(sOrder)){
					param.setOrder(Integer.parseInt(sOrder));
				}else{
					param.setOrder(0);
				}
			}else{
				param.setOrder(Constant.ORDER_BORROW_ADDTIME_DOWN);
			}
			param.setUse(sUse);
			param.setSearch(search);
			param.setKeywords(sKeywords);
			return param;
		}else if(search.equals("select")){
			String BorrowType=StringUtils.isNull(request.getParameter("type"));
			String Limit=StringUtils.isNull(request.getParameter("time_limit"));
			String borrow_style=StringUtils.isNull(request.getParameter("borrow_style"));
			if((Global.getWebid().equals("lhcf")||Global.getWebid().equals("hndai"))){
				UnionSearchParam param=new UnionSearchParam(sBorrowType,sLilv,sLimit,sAccount,sOrder);
				param.setKeywords(sKeywords);
				if(StringUtils.isBlank(sOrder)){
					param.setOrder(Constant.ORDER_BORROW_ADDTIME_DOWN);
					}else{
					int order=NumberUtils.getInt(sOrder);
					param.setOrder(order);
					}
					return param;
			}
			SearchParam param= new SearchParam(sUse,Limit,sKeywords);
			param.setType(BorrowType);
			param.setBorrow_style(borrow_style);
			if(StringUtils.isBlank(sOrder)){
				if(Global.getWebid().equals("wzdai")){
				   param.setOrder(Constant.ORDER_ACCOUNT_DOWN);
				}else{
			       param.setOrder(Constant.ORDER_BORROW_ADDTIME_DOWN);
				}
			}else{
			int order=NumberUtils.getInt(sOrder);
			param.setOrder(order);
			}
			return param;
		}else{
			SearchParam param=new SearchParam(sUse,sLimit,sKeywords);
			param.setType(sBorrowType);
			String webid=Global.getValue("webid");
			if(StringUtils.isNull(webid).equals("zrzb")){
				param.setOrder(11);
			}else if(StringUtils.isNull(webid).equals("hndai")){
				if(sOrder!=null&&!"".equals(sOrder)){
					param.setOrder(Integer.parseInt(sOrder));
				}else{
					param.setOrder(0);
				}
			}else{
				param.setOrder(Constant.ORDER_BORROW_TYPE_UP);
			}
			return param;
		}
	}
	
	/**
	 * 查看标的详细情况
	 * @return
	 * @throws Exception
	 */
	public String detail() throws Exception {
		// 获取制定id的borrow的详细
		Borrow b = borrowService.getBorrow(model.getBorrowid());
		if (b == null) {
			return "error";
		}
		// 获取发标人的详细情况
		DetailUser u = userService.getDetailUser(b.getUser_id());
		Userinfo info=userinfoService.getUserinfoByUserid(b.getUser_id());
		//如果采用异步加载投标记录，则此Action不加载投标记录
		if(Global.getInt("borrow_ajaxTenderListIsOn")!=1){
			List tenderList = borrowService.getTenderList(b.getId());
			request.setAttribute("tenderlist", tenderList);
		}
		  // v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 start
		//List<Attestation> attestationList = userinfoService
		//		.getAttestationListByUserid(b.getUser_id(), 1);
		  // v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 end
		Map map = commentService.getCommentListByBorrowid(b.getId());
		User kf=null;
		if(isSession()){
			User user = (User) session.get(Constant.SESSION_USER);
			long user_id = user.getUser_id();
			kf = accountService.getKf(user_id);
			request.setAttribute("kf", kf);
			//获取投资人的账户情况
			AccountModel act=accountService.getAccount(this.getSessionUser().getUser_id());
			request.setAttribute("account", act);
			String webid=Global.getValue("webid");
			if(StringUtils.isNull(webid).equals("wzdai")){
				List<Friend> list = friendService.getBlackList(user_id);
				int isBlackFriend = 0;
				if(b != null && b.getUser_id() != 0){
					for(int i = 0; i < list.size(); i++){
						if( b.getUser_id() == list.get(i).getFriends_userid() ){
							isBlackFriend = 1;
							break;
						}
					}
				}
				request.setAttribute("isBlackFriend", isBlackFriend);
			}
			
		}
		//获取借款人的账户情况
		AccountModel borrowAccount=accountService.getAccount(b.getUser_id());
		//UserAccountSummary uas=accountService.getUserAccountSummary(b.getUser_id());
		// 标详情页面性能优化 lhm 2013/10/11 start
//		List repament_scuess = memberBorrowService.getRepayMentList(// 已还款
//				"repaymentyes", u.getUser_id());
//		List repament_failure = memberBorrowService.getRepayMentList(// 未还款
//				"repayment", u.getUser_id());
//		List<Borrow> borrowFlowList = borrowService.getBorrowFlowListByuserId(u
//				.getUser_id());
//		List<Repayment> earlyRepaymentList = borrowService.getRepaymentList(// 提前还款
//				null, u.getUser_id());
//		List<Repayment> lateRepaymentList = borrowService.getRepaymentList(// 迟还款
//				"lateRepayment", u.getUser_id());
//		List<Repayment> overdueRepaymentList = borrowService.getRepaymentList(// 30天内逾期还款
//				"overdueRepayment", u.getUser_id());
//		List<Repayment> overdueRepaymentsList = borrowService.getRepaymentList(// 30天后逾期还款
//				"overdueRepayments", u.getUser_id());
//		List<Repayment> overdueNoRepaymentsList = borrowService// 逾期未还款
//				.getRepaymentList("overdueNoRepayments", u.getUser_id());

//		request.setAttribute("repament_scuess", repament_scuess.size());
//		request.setAttribute("repament_failure", repament_failure.size());
//		request.setAttribute("borrowFlowList", borrowFlowList.size());
//		request.setAttribute("earlyRepaymentList", earlyRepaymentList.size());
//		request.setAttribute("lateRepaymentList", lateRepaymentList.size());
//		request.setAttribute("overdueRepaymentList",
//				overdueRepaymentList.size());
//		request.setAttribute("overdueRepaymentsList",
//				overdueRepaymentsList.size());
//		request.setAttribute("overdueNoRepaymentsList",
//				overdueNoRepaymentsList.size());
// v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 start
		/*// 已还款
		long repament_scuess = memberBorrowService.getRepayMentCount("repaymentyes", u.getUser_id());
		// 未还款
		long repament_failure = memberBorrowService.getRepayMentCount("repayment", u.getUser_id());
		// 流标
		long borrowFlowCount = borrowService.getBorrowFlowCountByUserId(u.getUser_id());
		// 提前还款
		long earlyRepaymentCount = borrowService.getRepaymentCount(null, u.getUser_id());
		// 迟还款
		long lateRepaymentCount = borrowService.getRepaymentCount("lateRepayment", u.getUser_id());
		// 30天内逾期还款
		long overdueRepaymentCount = borrowService.getRepaymentCount("overdueRepayment", u.getUser_id());
		// 30天后逾期还款
		long overdueRepaymentsCount = borrowService.getRepaymentCount("overdueRepayments", u.getUser_id());
		// 逾期未还款
		long overdueNoRepaymentsCount = borrowService.getRepaymentCount("overdueNoRepayments", u.getUser_id());

		request.setAttribute("repament_scuess", repament_scuess);
		request.setAttribute("repament_failure", repament_failure);
		request.setAttribute("borrowFlowList", borrowFlowCount);
		request.setAttribute("earlyRepaymentList", earlyRepaymentCount);
		request.setAttribute("lateRepaymentList", lateRepaymentCount);
		request.setAttribute("overdueRepaymentList", overdueRepaymentCount);
		request.setAttribute("overdueRepaymentsList", overdueRepaymentsCount);
		request.setAttribute("overdueNoRepaymentsList", overdueNoRepaymentsCount);*/
// v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 end
		// 标详情页面性能优化 lhm 2013/10/11 end
		//获取借款人的待还款记录
		List waitRepayList = memberBorrowService.getRepaymentList(b.getUser_id());
		//request.setAttribute("summary",uas);
		request.setAttribute("borrow", b);
		request.setAttribute("user", u);
		request.setAttribute("borrowAccount", borrowAccount);
		request.setAttribute("info", info);
		request.setAttribute("commentlist", map.get("List"));
		request.setAttribute("commentCount", map.get("count"));
		  // v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 start
		//request.setAttribute("attestations", attestationList);
		  // v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 end
		request.setAttribute("waitRepayList", waitRepayList);
		request.setAttribute("nid", "invest");
		
		
		return "success";
	}

	private void checkParams(){
		if(Global.getWebid().equals("wzdai")){
		    String status=request.getParameter("status");
		  //  String borrow_style=request.getParameter("borrow_style");
			model.setStatus(NumberUtils.getInt(status));
		//	model.setStyle(borrow_style);
			
		}
		model.setPageStart(NumberUtils.getInt(request.getParameter("page")));
		if(model.getOrder()<-4||model.getOrder()>4){model.setOrder(0);}
		if(model.getPageStart()<1){model.setPageStart(1);}
		if(model.getStatus()<1){model.setStatus(1);}
	}
	
	public String detailTenderForJson() throws Exception {
		int page=paramInt("page");
		int order=paramInt("order");
		SearchParam param=new SearchParam();
		if(order==22){
			order=21;
			param.setOrder(order);
		}
		String webid = Global.getValue("webid");
	    if (StringUtils.isNull(webid).equals("xdcf")) {
	      param.setOrder(-21);
	    }
		Borrow b = borrowService.getBorrow(model.getBorrowid());
		Map<String,Object> map=new HashMap<String,Object>();
		if (b == null) {
			map.put("msg", "error");
		}else{
			 PageDataList list;
		      if (StringUtils.isNull(webid).equals("xdcf"))
		         list = this.borrowService.getTenderList(this.model.getBorrowid(), page, param);
		      else {
			     list=borrowService.getTenderList(model.getBorrowid(), page, new SearchParam());
		      }
		      map.put("msg", "success");
		      map.put("data", list);
		}
		printJson(JSON.toJSONString(map));
		return null;
	}
	/**
	 * 浙融汇标详情页
	 * @return
	 * @throws Exception
	 */
	public String detailForJson() throws Exception {
		Borrow b = borrowService.getBorrow(model.getBorrowid());
		//Borrow b=new Borrow();
		//b.setId(model.getBorrowid());
		//b.setName("ddd");
        Account account=accountService.getAccount(1);
        Map<String,Object> map=new HashMap<String,Object>();
		logger.debug("account====="+account);
		logger.debug("borrow====="+b);
		//List<Borrow> list=new ArrayList<Borrow>();
		//list.add(b);
		if(model.getBorrowid()!=0){
//			b.setLitpic("0");
			b.setFlag("0");
			b.setVouch_award("0");	
			b.setVouch_user("0");
			b.setVouch_account("0");
			b.setSource("0");
			b.setPublish("0");
//			b.setCustomer("0");
//			b.setNumber_id("0");
			b.setVerify_user("0");
			b.setVerify_remark("0");
//			b.setMonthly_repayment("0");
			b.setRepayment_time("0");
			b.setRepayment_remark("0");
//			b.setSuccess_time("0");
//			b.setEach_time("0");
//			b.setEnd_time("0");
//			b.setIs_false("0");
			b.setPwd("0");
			b.setBorrow_time("0");
			b.setBorrow_account(0);
			b.setOpen_credit("0");
			b.setOpen_account("0");
			b.setOpen_borrow("0");
			b.setOpen_tender("0");
			b.setPayment_account(0);
			b.setBorrow_time_limit("0");
			b.setContent("0");
			b.setAddip("0");
			b.setRepayment_account(0);
			logger.debug("borrow====="+b);
			map.put("data",b);
		}else{
			map.put("data",account);
		}
		printJson(JSON.toJSONString(map));
		return null;
	}

	/**
	 * 
	 * @return
	 */
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

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;  
	}

	public UserinfoService getUserinfoService() {
		return userinfoService;
	}

	public void setUserinfoService(UserinfoService userinfoService) {
		this.userinfoService = userinfoService;
	}
	public CommentService getCommentService() {
		return commentService;
	}
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}
	public AccountService getAccountService() {
		return accountService;
	}
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	public MemberBorrowService getMemberBorrowService() {
		return memberBorrowService;
	}

	public void setMemberBorrowService(MemberBorrowService memberBorrowService) {
		this.memberBorrowService = memberBorrowService;
	}
}