package com.p2psys.fund.web.action.front;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.domain.User;
import com.p2psys.fund.model.FundModel;
import com.p2psys.fund.service.FundService;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 基金信托
 
 * @version 1.0
 * @since 2013-10-25
 */

public class FundAction extends BaseAction implements ModelDriven<FundModel> {
	private static final Logger logger=Logger.getLogger(FundAction.class);
	
	@Resource
	private FundService fundService;
	
	FundModel model = new FundModel();
	
	@Override
	public FundModel getModel() {
		return model;
	}
	
	/**
	 * 我要理财
	 * @return
	 */
	public String index(){
		int status = paramInt("status");
		if(status==0){
			status = 1;
		}
		int page = paramInt("page");
		SearchParam param = new SearchParam();
		param.setStatus(status+"");
		param.setKeywords(paramString("keywords"));
		param.setType(paramInt("type")+"");
		param.setOrder(paramInt("order"));
		PageDataList plist=fundService.page(page, param);
		setPageAttribute(plist, param);
		
		return "index";
	}
	
	/**
	 * 我要理财-异步获取列表信息
	 * @throws IOException 
	 */
	public String ajaxList(){
		int page = paramInt("page");
		SearchParam param = new SearchParam();
		param.setStatus(paramInt("status")+"");
		param.setKeywords(paramString("keywords"));
		param.setType(paramInt("type")+"");
		param.setOrder(paramInt("order"));
		PageDataList plist=fundService.page(page, param);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("plist", plist);
		String json=JSON.toJSONString(map,true);
		try {
			printJson(json);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("我要理财-异步获取列表信息异常："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 基金信托详情页-提交认购意向
	 * @return
	 */
	public String detail(){
		long id = paramLong("fundId");
		model = (FundModel) fundService.get(id);
		model.setVisitTime(model.getVisitTime()+1);
		fundService.modify("visit_time", model.getVisitTime(), id);
		saveToken("fund_token");
		return "detail";
	}
	
	
	/**
	 * 提交认购意向
	 * @return
	 */
	public String tender(){
		long id = paramLong("fundId");
		double account = paramInt("account");
		String valicode = paramString("valicode");
		if(StringUtils.isBlank(valicode)){
			message("验证码不能为空！","/fund/detail.html?fundId="+id);
			return "tender";
		}else if(!checkValidImg(valicode)){
			message("验证码错误！","/fund/detail.html?fundId="+id);
			return "tender";
		}
		//表单防重复提交
		String tokenMsg=checkToken("fund_token");
		if(!StringUtils.isBlank(tokenMsg)){
			message(tokenMsg,"/fund/detail.html?fundId="+id);
			return MSG;
		}
		User user = getSessionUser();
		int r = fundService.addTender(id, account, user);
		if(r==1){
			message("认购意向提交成功！","/fund/detail.html?fundId="+id);
		}else{
			message("认购意向提交失败！","/fund/detail.html?fundId="+id);
		}
		return "tender";
	}

}
