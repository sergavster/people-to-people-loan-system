package com.p2psys.web.action;

import java.util.List;

import com.p2psys.context.Global;
import com.p2psys.domain.Article;
import com.p2psys.domain.Site;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.ArticleService;
import com.p2psys.service.UserService;
import com.p2psys.tool.Page;
import com.p2psys.util.NumberUtils;

public class ArticleAction extends BaseAction {

	/**
	 * Spring注入
	 */
	private ArticleService articleService;

	private UserService userService;

	/**
	 * 内容管理列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		String catalog = paramString("catalog");
		String nid = this.paramString("nid");
		int page = paramInt("page");
		long cid = NumberUtils.getLong(catalog);
		// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 start
		String articlename = paramString("articlename");
		String publish1 = paramString("publish1");
		String publish2 = paramString("publish2");
		SearchParam param = new SearchParam();
		param.setArticlename(articlename);
		param.setPublish1(publish1);
		param.setPublish2(publish2);
		// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 end
		Site site = null;
		// 支持根据nid来查找栏目
		if (cid > 1) {
			site = articleService.getSiteById(cid);
		} else {
			site = articleService.getSiteByNid(nid);
		}
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		catalog = site.getSite_id() + "";
		// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 start
		PageDataList plist = articleService.getList(catalog, page, param);
		// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 end
		List sublist = articleService.getSubSiteList(site.getPid());
		request.setAttribute("catalog", catalog);
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());
		setPageAttribute(plist, param);
		return "list";
	}

	/**
	 * 内容管理详细页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detail() throws Exception {
		String catalog = paramString("catalog");
		String nid = this.paramString("nid");
		long id = paramLong("id");
		Article article = articleService.getArticle(id);
		if (article == null) {
			return NOTFOUND;
		}
		long cid = NumberUtils.getLong(catalog);
		Site site=null;
		//支持根据nid来查找栏目
		if(cid>1){
			site = articleService.getSiteById(cid);
		}else{
			site = articleService.getSiteByNid(nid);
		}
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		List sublist = articleService.getSubSiteList(site.getPid());
		request.setAttribute("article", article);
		request.setAttribute("catalog", site.getSite_id());
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());
		return "detail";
	}

	/**
	 * 生产分页信息
	 * 
	 * @param p
	 * @return
	 */
	private String getPageStr(String catalog, Page p) {
		StringBuffer sb = new StringBuffer();
		int currentPage = p.getCurrentPage();
		int[] dispayPage = new int[5];
		if (p.getPages() < 5) {
			dispayPage = new int[p.getPages()];
			for (int i = 0; i < dispayPage.length; i++) {
				dispayPage[i] = i + 1;
			}
		} else {
			if (currentPage < 3) {
				for (int i = 0; i < 5; i++) {
					dispayPage[i] = i + 1;
				}
			} else if (currentPage > p.getPages() - 2) {
				for (int i = 0; i < 5; i++) {
					dispayPage[i] = p.getPages() - 4 + i;
				}
			} else {
				for (int i = 0; i < 5; i++) {
					dispayPage[i] = currentPage - 2 + i;
				}
			}
		}
		String typestr = "&catalog=" + catalog;
		// 输出分页信息
		for (int i = 0; i < dispayPage.length; i++) {
			if (dispayPage[i] == currentPage) {
				sb.append(" <span class='this_page'>" + currentPage + "</span>");
			} else {
				sb.append(" <a href='list.html?page=" + dispayPage[i] + typestr
						+ "'>" + dispayPage[i] + "</a>");
			}
		}
		return sb.toString();
	}

	public String onlinekefu() throws Exception {
	    //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		List list = userService.getKfList(1);
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		request.setAttribute("kflist", list);

		// V1.6.6.2 RDPROJECT-352 ljd 2013-10-18 start
		//long cid = 98;
		//Site site = articleService.getSiteById(cid);
		String nid = paramString("nid");
		if(nid == null || "".equals(nid)) {
			nid = "onlinekefu"; // 如果没有传入nid，则将'onlinekefu'作为默认nid
		}
		Site site = articleService.getSiteByNid(nid);
		// V1.6.6.2 RDPROJECT-352 ljd 2013-10-18 end
		
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		List sublist = articleService.getSubSiteList(site.getPid());
		
		// V1.6.6.2 RDPROJECT-352 ljd 2013-10-18 start
		//request.setAttribute("catalog", cid);
		// V1.6.6.2 RDPROJECT-352 ljd 2013-10-18 end
		
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());
		return SUCCESS;
	}

	// 晋商贷晋商文化
	public String jin_culture() throws Exception {
		int pageKf = NumberUtils.getInt(Global.getValue("index_kf_num"));
		pageKf = pageKf > 0 ? pageKf : 8; 
		List list = userService.getKfListWithLimit(0, pageKf);
		request.setAttribute("kflist", list);

		long cid = 126;
		Site site = articleService.getSiteById(cid);
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		List sublist = articleService.getSubSiteList(site.getPid());
		request.setAttribute("catalog", cid);
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());
		// 晋商文化
		int page1 = NumberUtils.getInt(Global.getValue("index_jinculture_num"));
		page1 = page1 > 0 ? page1 : 15;
		List jswh = this.articleService.getList("126", 0, page1);
		this.request.setAttribute("jswh", jswh);
		// 晋商文化新手入门
		int page2 = NumberUtils.getInt(Global.getValue("index_xsrm_num"));
		List xsrm = this.articleService.getList("131", 0, page2);
		this.request.setAttribute("xsrm", xsrm);
		request.setAttribute("nid", "jin_culture");
		return SUCCESS;
	}

	public String jin_culture_content() throws Exception {
		// List list=userService.getKfList();
		// request.setAttribute("kflist", list);

		long cid = 126;
		Site site = articleService.getSiteById(cid);
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		List sublist = articleService.getSubSiteList(site.getPid());
		request.setAttribute("catalog", cid);
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());

		return SUCCESS;
	}

	// 晋商贷质押标介绍
	public String pledge_introduction() throws Exception {
		// List list=userService.getKfList();
		// request.setAttribute("kflist", list);

		long cid = 127;
		Site site = articleService.getSiteById(cid);
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		List sublist = articleService.getSubSiteList(site.getPid());
		request.setAttribute("catalog", cid);
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());

		return SUCCESS;
	}

	public String pledge_introduction_content() throws Exception {
		// List list=userService.getKfList();
		// request.setAttribute("kflist", list);

		long cid = 127;
		Site site = articleService.getSiteById(cid);
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		List sublist = articleService.getSubSiteList(site.getPid());
		request.setAttribute("catalog", cid);
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());

		return SUCCESS;
	}

	/**
	 * 安全保障
	 * 
	 * @return
	 * @throws Exception
	 */
	public String security() throws Exception {
		// List list=userService.getKfList();
		// request.setAttribute("kflist", list);

		long cid = Global.getInt("security");
		Site site = articleService.getSiteById(cid);
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		List sublist = articleService.getSubSiteList(cid);
		request.setAttribute("catalog", cid);
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());

		return SUCCESS;
	}

	// 国临创投国临专题
	public String guolin_special() throws Exception {
		long cid = 124;
		Site site = articleService.getSiteById(cid);
		if (site == null) {
			return NOTFOUND;
		}
		Site psite = articleService.getSiteById(site.getPid());
		if (psite == null) {
			psite = new Site();
			psite.setSite_id(0);
			psite.setName("首页");
		}
		List sublist = articleService.getSubSiteList(site.getPid());
		request.setAttribute("catalog", cid);
		request.setAttribute("psite", psite);
		request.setAttribute("site", site);
		request.setAttribute("sublist", sublist);
		request.setAttribute("nid", psite.getNid());

		return SUCCESS;
	}

	public String getCatalogName(String catalog) {
		String name = "";
		if ("59".equals(catalog)) {
			name = "媒体报道";
		} else if ("22".equals(catalog)) {
			name = "最新公告";
		} else if ("63".equals(catalog)) {
			name = "商业模式";
		} else if ("2".equals(catalog)) {
			name = "关于我们";
		} else if ("57".equals(catalog)) {
			name = "借贷费用";
		} else if ("3".equals(catalog)) {
			name = "政策法规";
		} else if ("64".equals(catalog)) {
			name = "工具箱";
		} else if ("71".equals(catalog)) {
			name = "下载中心";
		} else if ("12".equals(catalog)) {
			name = "网站规则";
		} else if ("9".equals(catalog)) {
			name = "新手上路";
		} else if ("69".equals(catalog)) {
			name = "关于我们";
		} else if ("11".equals(catalog)) {
			name = "常见问题";
		} else if ("6".equals(catalog)) {
			name = "如何投资";
		} else if ("67".equals(catalog)) {
			name = "如何贷款";
		}
		return name;
	}

	public ArticleService getArticleService() {
		return articleService;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}