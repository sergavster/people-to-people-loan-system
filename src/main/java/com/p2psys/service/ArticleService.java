package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.Article;
import com.p2psys.domain.ScrollPic;
import com.p2psys.domain.Site;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.tree.SiteTree;

public interface ArticleService {
	
	// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 start
	/**
	 * 首页查询
	 * @param catalog catalog
	 * @param page page
	 * @param param param
	 * @return PageDataList
	 */
	PageDataList getList(String catalog, int page, SearchParam param);
	// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 end
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public Article getArticle(long id);
	
	public SiteTree getSiteTree();
	
	public Site getSiteById(long id);
//	/**
//	 * 根据code查询Site对象
//	 * @param id
//	 * @return
//	 */
//	public Site getSiteByCode(String id);
	
	public Site getSiteByNid(String nid);
	
	public List getSubSiteList(long id);
	
	public void modifySite(Site s);
	
	public void delSite(long id);
	
	public void addSite(Site s);
	
	public void addArticle(Article a,String files);
	
	public List<Article> getList(String catalog, int start, int end);
	
	// V1.6.6.2 RDPROJECT-179 liujiduo 2013-10-18 start
	/**
	 * 通过栏目nid获取文章列表
	 * 
	 * @param nid 栏目nid
	 * @param start 起始位置
	 * @param end 结束位置
	 * @return 栏目下的文章列表
	 */
	public List<Article> getListByNid(String nid, int start, int end);
	// V1.6.6.2 RDPROJECT-179 liujiduo 2013-10-18 end
	
	public PageDataList getArticleList(int page, SearchParam param);
	
	public void modifyArticle(Article a,String files);
	
	public void delArticle(long id);
	
	public void modifyArticleStatus(Article a);
	
	public void batchOperateArticle(int type,List bid,List orders);
	
	public List getArticleFileds(long aid);
	
	public List getScrollPicList(int type_id,int start,int end);
	
	public List<ScrollPic> getScrollPicList(int start,int end);
	
	public void delScrollPic(long id);
	
	public void modifyScrollPic(ScrollPic sp);
	
	public ScrollPic getScrollPicListById(long id);
	
	public ScrollPic addScrollPic(ScrollPic sp);
	/**
	 * 获取后台文章所属栏目所有类型
	 * @return
	 */
	public List getAllSiteType();
//	/**
//	 * 获取后台图片所有类型
//	 * @return
//	 */
//	public List getScrollPicType();
	/**
	 * 图片显示列表
	 */
	public PageDataList getScrollPicList(int type_id,int page,SearchParam param) ;

}