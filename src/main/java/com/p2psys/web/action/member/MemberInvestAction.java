package com.p2psys.web.action.member;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.context.Constant;
import com.p2psys.domain.User;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.invest.CollectionList;
import com.p2psys.model.invest.InvestBorrowList;
import com.p2psys.service.BorrowService;
import com.p2psys.service.UserService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class MemberInvestAction extends BaseAction {

	private static Logger logger = Logger.getLogger(MemberInvestAction.class);
	private UserService userService;
	private BorrowService borrowService;
	
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public BorrowService getBorrowService() {
		return borrowService;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	
	public String list() {
		logger.info("成功投资列表");
		User user=(User)session.get(Constant.SESSION_USER);
		long user_id=user.getUser_id();
		String type=request.getParameter("type");
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		InvestBorrowList list=borrowService.getSuccessListByUserid(user_id, type, page, new SearchParam());
		request.setAttribute("borrow", list.getList());
		Map map=param.toMap();
		map.put("type", type);
		request.setAttribute("param",map);
		request.setAttribute("page", list.getPage());
		request.setAttribute("type", type);
		return "success";
	}

	public String collect() {
		User user=(User)session.get(Constant.SESSION_USER);
		long user_id=user.getUser_id();
		int page=NumberUtils.getInt(request.getParameter("page"));
		int status=NumberUtils.getInt(request.getParameter("status"));
		
		// V1.6.6.2 RDPROJECT-313 ljd 2013-10-22 start
		// 查询条件
		// v1.6.6.2 RDPROJECT-313 zza 2013-10-31 start
		String repay_time1 = StringUtils.isNull(request.getParameter("repay_time1"));
		String repay_time2 = StringUtils.isNull(request.getParameter("repay_time2"));
		// v1.6.6.2 RDPROJECT-313 zza 2013-10-31 end
		String keywords = StringUtils.isNull(request.getParameter("keywords"));
		SearchParam param=new SearchParam();
		param.setKeywords(keywords);
		// v1.6.6.2 RDPROJECT-313 zza 2013-10-31 start
		param.setRepay_time1(repay_time1);
		param.setRepay_time2(repay_time2);
		// v1.6.6.2 RDPROJECT-313 zza 2013-10-31 end
		// V1.6.6.2 RDPROJECT-313 ljd 2013-10-22 end
		CollectionList cList=borrowService.getCollectionList(user_id, status, page, param);
		
		java.util.Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        request.setAttribute("currentTime",sdf.format(date));   
        
		request.setAttribute("collect", cList.getList());
		request.setAttribute("page", cList.getPage());
		//用户分页中的地址处理
		param.setStatus(status+"");
		request.setAttribute("param", param.toMap());
		request.setAttribute("status", status);
		//给前台页面传入利息管理费borrow_fee(之前页面是写死的0.1)
/*		double vip_borrow_fee = Global.getDouble("vip_borrow_fee");
		if ( vip_borrow_fee > 0 ) {
			UserCacheModel u = userService.getUserCacheByUserid(user_id);
			int vip_status = u.getVip_status();
			if(vip_status==1){
				request.setAttribute("borrow_fee", vip_borrow_fee);
			}else{
				request.setAttribute("borrow_fee", Global.getDouble("borrow_fee"));
			}
		}else{
			request.setAttribute("borrow_fee", Global.getDouble("borrow_fee"));
		}*/
		
		return "success";
	}
	
	public String tender() {
		User user=(User)session.get(Constant.SESSION_USER);
		long user_id=user.getUser_id();
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		PageDataList pList=borrowService.getInvestTenderListByUserid(user_id, page, param);
		request.setAttribute("tender", pList.getList());
		request.setAttribute("page", pList.getPage());
		request.setAttribute("param", param.toMap());
		return "success";
	}
	/**
	 * 正在投标的记录，但是没有满标审核
	 * @return
	 */
	public String bid() {
		User user=getSessionUser();
		long user_id=user.getUser_id();
		int page=NumberUtils.getInt(request.getParameter("page"));
		SearchParam param=new SearchParam();
		PageDataList pList=borrowService.getInvestTenderingListByUserid(user_id, page, param);
		request.setAttribute("tender", pList.getList());
		request.setAttribute("page", pList.getPage());
		request.setAttribute("param", param.toMap());
		return "success";
	}

	
}
