package com.p2psys.web.action.admin;

import java.util.List;

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
public class LinkageAction extends BaseAction implements ModelDriven<Linkage>{
	
	private Linkage linkage = new Linkage();

	private LinkageService linkageService;
	
	public LinkageService getLinkageService() {
		return linkageService;
	}

	public void setLinkageService(LinkageService linkageService) {
		this.linkageService = linkageService;
	}
	
	@Override
	public Linkage getModel() {
		return linkage;
	}
	
	//展示列表和查询
	public String showLinkage() {
		int page = NumberUtils.getInt(paramString("page"));
		LinkageTypeSearchParam searchParam = new LinkageTypeSearchParam();
		String name = StringUtils.isNull(paramString("name"));
		searchParam.setName(name);
		if(paramInt("type") != 1){
			searchParam.setType_id(-1);
		}else{
			if(StringUtils.isBlank(paramString("type_id"))){
				searchParam.setType_id(-1);
			}else{
				searchParam.setType_id(paramInt("type_id"));
			}
		}
		
		PageDataList pageDataList = linkageService.getLinkageListByPageNumber(page, searchParam);
		this.setPageAttribute(pageDataList, searchParam);
		return SUCCESS;
	}
	
	public String addLinkage(){
		List<LinkageType> list = linkageService.getTypeList();
		request.setAttribute("typeList", list);
		String actionType = paramString("actionType");
		if(!StringUtils.isBlank(actionType)){
			
			linkage.setAddtime(getTimeStr());
			linkage.setAddip(getRequestIp());
			
			linkageService.addLinkage(linkage);
			message("新增成功！","/admin/linkage/showLinkage.html");
			return ADMINMSG;
		}
		return SUCCESS;
	}
	
	public String modifyLinkage(){
		long id = paramInt("id");
		if(!StringUtils.isBlank(actionType)){
			
			linkage.setAddip(getRequestIp());
			linkageService.updateLinkage(linkage);
			
			message("修改成功！","/admin/linkage/showLinkage.html");
			return ADMINMSG;
		}
		Linkage linkage = linkageService.getLinkageById(id);
		request.setAttribute("linkage", linkage);
		List<LinkageType> list = linkageService.getTypeList();
		request.setAttribute("typeList", list);
		
		return SUCCESS;
	}
	
}
