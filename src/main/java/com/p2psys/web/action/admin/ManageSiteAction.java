package com.p2psys.web.action.admin;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.domain.Site;
import com.p2psys.model.tree.Tree;
import com.p2psys.service.ArticleService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class ManageSiteAction extends BaseAction implements  ModelDriven<Site> {
	
	ArticleService articleService;
	Site model=new Site();

	@Override
	public Site getModel() {
		return model;
	}

	public ArticleService getArticleService() {
		return articleService;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	public String siteTree(){
		response.setContentType("text/json;charset=UTF-8");
		
		return null;
	}
	
	public String showSite() throws Exception {
		Site site=articleService.getSiteById(model.getSite_id());
		if(site!=null){
			Site psite=articleService.getSiteById(site.getPid());
			request.setAttribute("site", site);
			if(psite==null){
				psite=new Site();
				psite.setName("根目录");
				psite.setPid(0);
			}
			request.setAttribute("psite", psite);
		}
		return SUCCESS;
	}
	
	public String addSite() throws Exception {
		String actionType=request.getParameter("actionType");
		long pid=NumberUtils.getLong(request.getParameter("pid"));
		if(!StringUtils.isBlank(actionType)){
			articleService.addSite(model);
			Tree<Site> tree=articleService.getSiteTree();
			context.setAttribute("tree", tree);
			message("新增栏目成功！","/admin/article/showSite.html");
			return ADMINMSG;
		}
		
		Site psite=articleService.getSiteById(pid);
		if(psite==null){
			psite=new Site();
			psite.setName("根目录");
			psite.setPid(0);
		}
		Tree<Site> tree=articleService.getSiteTree();
		context.setAttribute("tree", tree);
		request.setAttribute("psite", psite);
		return SUCCESS;
	}
	
	public String delSite() throws Exception {
		long pid=NumberUtils.getLong(request.getParameter("pid"));
		Site psite=articleService.getSiteById(pid);
		if(psite!=null&&pid>0){
			articleService.delSite(psite.getSite_id());
			Tree<Site> tree=articleService.getSiteTree();
			context.setAttribute("tree", tree);
			message("删除成功","/admin/article/showSite.html");
		}else{
			message("非法操作！","/admin/article/showSite.html");
		}
		return SUCCESS;
	}
	
	public String modifySite() throws Exception {
		articleService.modifySite(model);
		Tree<Site> tree=articleService.getSiteTree();
		context.setAttribute("tree", tree);
		message("修改栏目成功！","/admin/article/showSite.html");
		return ADMINMSG;
	}
	
}
