package com.p2psys.web.action.admin;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.domain.Linkage;
import com.p2psys.domain.LinkageType;
import com.p2psys.model.LinkageTypeSearchParam;
import com.p2psys.model.PageDataList;
import com.p2psys.service.LinkageService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;
/**
 * 
 * 新增类 RDPROJECT-26 sj 2013-12-24 
 * 
 * 
 * */
public class LinkageTypeAction extends BaseAction implements ModelDriven<LinkageType> {
	
	private LinkageType linkageType = new LinkageType();
	
	private LinkageService linkageService;
	
	public LinkageService getLinkageService() {
		return linkageService;
	}

	public void setLinkageService(LinkageService linkageService) {
		this.linkageService = linkageService;
	}

	@Override
	public LinkageType getModel() {
		return linkageType;
	}
	
	public String list() {
		int page = NumberUtils.getInt(paramString("page"));
		LinkageTypeSearchParam searchParam = new LinkageTypeSearchParam();
		String name = StringUtils.isNull(paramString("name"));
		searchParam.setName(name);
		searchParam.setType_id(-1);
		PageDataList pageDataList = linkageService.getLinkageTypeListByPageNumber(page, searchParam);
		this.setPageAttribute(pageDataList, searchParam);
		
		return SUCCESS;
	}
	
	public String addLinkageType(){
		String actionType = paramString("actionType");
		if(!StringUtils.isBlank(actionType)){
			linkageType.setAddtime(Integer.parseInt(getTimeStr()));
			linkageType.setAddip(getRequestIp());
			
			linkageService.addLinkageType(linkageType);
			message("新增成功！","/admin/linkagetype/list.html");
			return ADMINMSG;
		}
		return SUCCESS;
	}
	
	public String modifyLinkageType(){
		long id = paramInt("id");
		
		if(!StringUtils.isBlank(actionType)){
			
			linkageType.setAddip(getRequestIp());
			linkageService.updateLinkageType(linkageType);
			
			message("修改成功！","/admin/linkagetype/list.html");
			return ADMINMSG;
		}
		LinkageType l = linkageService.getLinkageTypeById(id);
		request.setAttribute("linkageType", l);
		return SUCCESS;
	}
	
}
