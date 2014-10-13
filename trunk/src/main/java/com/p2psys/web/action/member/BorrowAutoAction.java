package com.p2psys.web.action.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.context.Constant;
import com.p2psys.domain.BorrowAuto;
import com.p2psys.domain.User;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.SearchParam;
import com.p2psys.service.AutoBorrowService;
import com.p2psys.service.BorrowService;
import com.p2psys.util.NumberUtils;
import com.p2psys.web.action.BaseAction;

public class BorrowAutoAction extends BaseAction implements SessionAware, ServletRequestAware , ModelDriven<BorrowAuto> { 
	/**
	 * Spring注入
	 */
	private static Logger logger=Logger.getLogger(BorrowAutoAction.class);
	private BorrowService borrowService;
    private AutoBorrowService autoBorrowService;
	public AutoBorrowService getAutoBorrowService() {
		return autoBorrowService;
	}

	public void setAutoBorrowService(AutoBorrowService autoBorrowService) {
		this.autoBorrowService = autoBorrowService;
	}
	private Map session;
	
	private BorrowAuto auto = new BorrowAuto();

	/**
	 * 我自动投标的相关操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String auto() {
		User user=(User)session.get(Constant.SESSION_USER);
		long user_id=user.getUser_id();
		String type=paramString("type");
		List list=new ArrayList();
		String errormsg=null;
		if(type.equals("list")||type.equals("")){
			list=borrowService.getBorrowAutoList(user_id);
			request.setAttribute("auto", list);
			request.setAttribute("type", type);
			return "success";
		}else if(type.equals("add")){
			list=borrowService.getBorrowAutoList(user_id);
			if(list.size()>0){
				errormsg="您已经添加了1条自动投标，最多只能添加1条，您可以删除或者修改 ！";
			}else{
				auto.setUser_id(user_id);
				auto.setAddtime(NumberUtils.getInt(this.getTimeStr()));
				borrowService.addBorrowAuto(auto);
				list=borrowService.getBorrowAutoList(user_id);
			}
			request.setAttribute("errormsg", errormsg);
			request.setAttribute("auto", list);
			return "success";
		}else if(type.equals("modify")){
			String idStr=paramString("id");
			long id=NumberUtils.getLong(idStr);
			if(id<1){
				errormsg="修改自动投标失败！";
				request.setAttribute("errormsg", errormsg);
			}
			
			//auto.setUser_id(user_id);
			List autolist=borrowService.getBorrowAutoList(user_id);
			BorrowAuto newauto = null;
			if(autolist.size()>0){
				// v1.6.6.1 RDPROJECT-199  wcw 2013-09-29 start
				 checkBorrowAuto(auto);
				// v1.6.6.1 RDPROJECT-199  wcw 2013-09-29 end
				 newauto=(BorrowAuto) autolist.get(0);
				 auto.setId(newauto.getId());
				 auto.setUser_id(user_id);
				 auto.setAddtime(NumberUtils.getInt(this.getTimeStr()));
				 logger.debug("auto============"+auto);
				 borrowService.modifyBorrowAuto(auto);
				
			}
			list=borrowService.getBorrowAutoList(user_id);
			request.setAttribute("auto", list);
			return "success";
		}else if(type.equals("showAuto")){
			list=borrowService.getBorrowAutoList(user_id);
			if(list.size()<1){
				return "notfound";
			}
			BorrowAuto ba=(BorrowAuto)list.get(0);
			request.setAttribute("auto", ba);
			//兼容未修改的自动投标页面
			request.setAttribute("ba", ba);
			request.setAttribute("type", type);
			SearchParam param=new SearchParam();
			param.setTimelimit_day_first(ba.getTimelimit_day_first());
			param.setTimelimit_day_last(ba.getTimelimit_day_last());
			param.setTimelimit_month_first(ba.getTimelimit_month_first());
			param.setTimelimit_month_last(ba.getTimelimit_month_last());
			param.setApr_first(ba.getApr_first());
			param.setApr_last(ba.getApr_last());
			param.setAward_first(ba.getAward_first());
			request.setAttribute("map", param.toMap());
			return "query";
		}else if(type.equals("delete")){
			borrowService.deleteBorrowAuto(user_id);
			list=borrowService.getBorrowAutoList(user_id);
			request.setAttribute("auto", list);
			return "success";
		}else{
			return "notfound";
		}
	}
	// v1.6.6.1 RDPROJECT-199  wcw 2013-09-29 start
	/**
	 * 自动投标设置校验
	 * @param auto
	 */
    public void checkBorrowAuto(BorrowAuto auto){
    	if(auto.getTimelimit_status()==1){
	    	if(auto.getTimelimit_day_first()>auto.getTimelimit_day_last()){
	    		throw new BorrowException("设置借款时间天数时间段有误");
	    	}
	    	if(auto.getTimelimit_month_first()>auto.getTimelimit_month_last()){
	    		throw new BorrowException("设置借款时间月份时间段有误");
	    	}
	    	//v1.6.7.2 RDPROJECT-583 wcw 2013-12-16 start
/*	    	if(auto.getTimelimit_day_last()!=0&&auto.getTimelimit_month_last()!=0){
	    		throw new BorrowException("借款时间月份时间段和天数时间段不能同时设置");
	    	}*/
	    	//v1.6.7.2 RDPROJECT-583 wcw 2013-12-16 end
    	}
    	if(auto.getApr_status()==1){
    		if(auto.getApr_first()>auto.getApr_last()){
    		throw new BorrowException("设置利率范围有误");
    	    }
    	}
    }
 // v1.6.6.1 RDPROJECT-199  wcw 2013-09-29 end
	 /**
	  * 自动投标排名
	  * 
	  */
	/*public String autoTenderOrder(){
		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		PageDataList pList=autoBorrowService.getAutoTenderOrderList(page);
		SearchParam searchParam=new SearchParam();
		setPageAttribute(pList,searchParam);
		return "success";
	}*/
	/**
	 * 
	 * @return
	 */
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	@Override
	public BorrowAuto getModel() {
		return auto;
	}
	public BorrowService getBorrowService() {
		return borrowService;
	}
	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	
}
