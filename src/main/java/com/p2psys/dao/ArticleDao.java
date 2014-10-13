package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Article;
import com.p2psys.domain.Site;
import com.p2psys.model.SearchParam;

public interface ArticleDao extends BaseDao {
	public List<Article> getList(String catalog, int start, int end);

	// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 start
	/**
	 * 首页查询
	 * @param catalog catalog
	 * @param start start
	 * @param end end
	 * @param param param
	 * @return list
	 */
	List<Article> getList(String catalog, int start, int end, SearchParam param);
	
	/**
	 * 首页查询
	 * @param catalog catalog
	 * @param param param
	 * @return int
	 */
	int count(String catalog, SearchParam param);
	// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 end

	public Article getArticleById(long id);
	
	public List getSiteList();
	
	public List getSubSiteList(long pid);
	
	public Site getSiteById(long id);
	/**
	 * 根据code查询site对象
	 * @param code
	 * @return
	 */
	public List getSiteByCode(String code);
	
	public List getSiteByNid(String nid);
	
	public void updateSite(Site s);
	
	public void delSite(long id);
	
	public void addSite(Site s);
	
	public Article addArticle(Article a);
	
	public int countArticle(SearchParam param);
	
	public List getArticleList(int start,int pernum,SearchParam param);

	public void modifyArticle(Article a);
	
	public void delArticle(long id);
	
	public void batchDel(List bid);
	
	public void batchOrder(List bid,List order);
	
	public void batchStatus(List bid,int status);
	
	public void modifyArticleStatus(Article a);
	
	public void addArticleFields(long aid,String files);
	
	public List getArticleFields(long aid);
	
	public void modifyArticleFields(long aid,String files);
	
	public void delArticleFields(long aid);
	
	/**
	 * 获取后台文章所属栏目所有类型
	 * @return
	 */
	public List getAllSiteType();
	
}
