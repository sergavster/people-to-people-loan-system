package com.p2psys.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.p2psys.context.Constant;
import com.p2psys.dao.ArticleDao;
import com.p2psys.domain.Article;
import com.p2psys.domain.ArticleField;
import com.p2psys.domain.Site;
import com.p2psys.model.SearchParam;

/**
 * 文章相关的Dao操作类
 * 
 
 * @date 2012-7-10-下午3:07:56
 * @version
 * 
 *           (c)</b> 2012-51-<br/>
 * 
 */
public class ArticleDaoImpl extends BaseDaoImpl  implements ArticleDao {

	private static Logger logger = Logger.getLogger(ArticleDaoImpl.class);

	public List<Article> getList(String catalog, int start, int end) {
		long s=System.currentTimeMillis();
		// v1.6.5.3 RDPROJECT-159 xx 2013.09.12 start
		//'order' 改为 `order`
		String sql = "select * from dw_article where 1=1 and status=1 and site_id=? order by is_top desc, `order` asc, publish desc limit " + start+ "," + end;
		// v1.6.5.3 RDPROJECT-159 xx 2013.09.12 end
		// 拼装SQL
		logger.debug("SQL:" + sql);
		List<Article> list = new ArrayList();
		try {
//			list = getJdbcTemplate().query(sql, new Object[] { catalog },
//					new ArticleMapper());
			list = getJdbcTemplate().query(sql, new Object[] { catalog }, getBeanMapper(Article.class));
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		long e=System.currentTimeMillis();
		logger.info("GetArticleList Cost Time:"+(e-s));
		return list;
	}

	@Override
	public Article getArticleById(long id) {
		String sql = "select * from dw_article where id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + id);
		Article article = null;
		try {
//			article = getJdbcTemplate().queryForObject(sql,
//					new Object[] { id }, new ArticleMapper());
			article = getJdbcTemplate().queryForObject(sql,
					new Object[] { id }, getBeanMapper(Article.class));
		} catch (DataAccessException e) {
			logger.debug("BorrowDao.getBorrowById,数据库查询结果为空！");
		}
		return article;
	}

	@Override
	public List getSiteList() {
		String sql="select * from dw_site where pid=0 order by `order`";
		List list=new ArrayList();
//		list=getJdbcTemplate().query(sql, new SiteMapper());
		list=getJdbcTemplate().query(sql, getBeanMapper(Site.class));
		return list;
	}

	@Override
	public List getSubSiteList(long pid) {
		String sql="select * from dw_site where pid=? order by `order`";
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{pid},new SiteMapper());
		list=this.getJdbcTemplate().query(sql, new Object[]{pid}, getBeanMapper(Site.class));
		return list;
	}

	@Override
	public Site getSiteById(long id) {
		String sql="select * from dw_site where site_id=?";
		Site s=null;
		try {
//			s=getJdbcTemplate().queryForObject(sql, new Object[]{id}, new SiteMapper());
			s=getJdbcTemplate().queryForObject(sql, new Object[]{id}, getBeanMapper(Site.class));
		} catch (DataAccessException e) {
			logger.debug("getSiteById():"+e.getMessage());
		}
		return s;
	}
	
	/**
	 * 根据code查询site对象
	 * @param id
	 * @return
	 */
	@Override
	public List getSiteByCode(String code) {
		String sql="select * from dw_site where code=?";
		List list=new ArrayList();
		try {
//			list=getJdbcTemplate().query(sql, new Object[]{code}, new SiteMapper());
			list=getJdbcTemplate().query(sql, new Object[]{code}, getBeanMapper(Site.class));
		} catch (DataAccessException e) {
			logger.debug("getSiteById():"+e.getMessage());
		}
		return list;
	}
	
	@Override
	public List getSiteByNid(String nid) {
		String sql="select * from dw_site where nid=?";
		List list=new ArrayList();
		try {
//			list=getJdbcTemplate().query(sql, new Object[]{nid}, new SiteMapper());
			list=getJdbcTemplate().query(sql, new Object[]{nid}, getBeanMapper(Site.class));
		} catch (DataAccessException e) {
			logger.debug("getSiteById():"+e.getMessage());
		}
		return list;
	}

	@Override
	public void updateSite(Site s) {
		String sql="update "+Constant.DB_PREFIX+"site as site set site.name=?,site.url=?,site.nid=?," +
				"site.status=?,site.code=?,site.order=?,site.description=?,site.content=?,site.style=? where site.site_id=?";
		this.getJdbcTemplate().update(sql, s.getName(),s.getUrl(),s.getNid(),s.getStatus(),
				s.getCode(),s.getOrder(),s.getDescription(),s.getContent(),s.getStyle(),s.getSite_id());
	}

	@Override
	public void delSite(long id) {
		String sql="delete from dw_site where site_id=?";
		this.getJdbcTemplate().update(sql, id);
	}

	@Override
	public void addSite(Site s) {
		// v1.6.6.2 RDPROJECT-329  wfl 2013-10-23 start
//		String sql="insert into "+Constant.DB_PREFIX+"site(pid,name,url,nid,status,code,description,content) " +
//				"values(?,?,?,?,?,?,?,?)";
//		this.getJdbcTemplate().update(sql, s.getPid(),s.getName(),s.getUrl(),s.getNid(),s.getStatus(),
//				s.getCode(),s.getDescription(),s.getContent());
		String sql="insert into "+Constant.DB_PREFIX+"site(pid,name,url,nid,style,`order`,status,code,description,content) " +
				"values(?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, s.getPid(),s.getName(),s.getUrl(),s.getNid(),s.getStyle(),s.getOrder(),s.getStatus(),
				s.getCode(),s.getDescription(),s.getContent());
		// v1.6.6.2 RDPROJECT-329  wfl 2013-10-23 end
	}

	@Override
	public Article addArticle(Article article) {
		// v1.6.5.3 RDPROJECT-159 xx 2013.09.13 start
		final  String sql="insert into dw_article(site_id,name,littitle,status,litpic,flag,source,publish,author" +
				",summary,content,`order`,is_top,hits,user_id,addtime,addip) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// v1.6.5.3 RDPROJECT-159 xx 2013.09.13 end
		final Article a=article;

		KeyHolder keyHolder = new GeneratedKeyHolder();

		this.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, a.getSite_id());
				ps.setString(2, a.getName());
				ps.setString(3, a.getLittitle());
				ps.setInt(4, a.getStatus());
				ps.setString(5, a.getLitpic());
				ps.setString(6, a.getFlag());
				ps.setString(7, a.getSource());
				ps.setString(8, a.getPublish());
				ps.setString(9, a.getAuthor());
				ps.setString(10, a.getSummary());
				ps.setString(11, a.getContent());
				ps.setInt(12, a.getOrder());
				// v1.6.5.3 RDPROJECT-159 xx 2013.09.13 start
				ps.setInt(13, a.getIs_top());
				// v1.6.5.3 RDPROJECT-159 xx 2013.09.13 end
				ps.setInt(14, a.getHits());
				ps.setLong(15, a.getUser_id());
				ps.setString(16, a.getAddtime());
				ps.setString(17, a.getAddip());
				return ps;
			}
		}, keyHolder);
		long key=keyHolder.getKey().longValue();
		a.setId(key);
		return a;
		
		
	}

	String queryArticleSql=" from dw_article a  " +
			"left join dw_site s on s.site_id=a.site_id " +
			"where 1=1";
	
	@Override
	public int countArticle(SearchParam param) {
		String selectSql= "select count(a.id) ";
		String searchSql = param.getSearchParamSql();
		StringBuffer sb=new StringBuffer(selectSql);
		sb.append(queryArticleSql).append(searchSql);
		String sql=sb.toString();
		logger.debug("countArticle():"+sql);
		int total=0;
		total=this.count(sql, new Object[]{});
		return total;
	}

	@Override
	public List getArticleList(int start,int pernum,SearchParam param) {
		String selectSql= "select a.*,s.name as sitename ";
		String orderSql=" order by a.publish desc,a.id desc,a.addtime desc";
		String limitSql=" limit ?,?";
		String searchSql = param.getSearchParamSql();
		StringBuffer sb=new StringBuffer(selectSql);
		sb.append(queryArticleSql).append(searchSql).append(orderSql).append(limitSql);
		String sql=sb.toString();
		logger.debug("getArticleList():"+sql);
		List list=new ArrayList();
		list=getJdbcTemplate().query(sql,new Object[]{start,pernum}, getBeanMapper(Article.class));

		return list;
	}

	@Override
	public void modifyArticle(Article a) {
		// v1.6.5.3 RDPROJECT-159 xx 2013.09.13 start
		String sql="update dw_article set site_id=?,name=?,littitle=?,status=?,litpic=?,flag=?,source=?,publish=?,author=?" +
				",summary=?,content=?,`order`=?,is_top=?,hits=?,user_id=?,addtime=?,addip=? where id=?";
		// v1.6.5.3 RDPROJECT-159 xx 2013.09.13 end
		getJdbcTemplate().update(sql, a.getSite_id(),a.getName(),a.getLittitle(),a.getStatus(),a.getLitpic(),a.getFlag(),
				a.getSource(),a.getPublish(),a.getAuthor(),a.getSummary(),a.getContent(),a.getOrder(),a.getIs_top(),a.getHits(),
				a.getUser_id(),a.getAddtime(),a.getAddip(),a.getId());
	}
	
	public void delArticle(long id) {
		String sql="delete from dw_article where id=?";
		getJdbcTemplate().update(sql,id);
	}
	
	@Override
	public void batchDel(List bid) {
		String sql="delete from dw_article where id=?";
		List argList=new ArrayList();
		for(int i=0;i<bid.size();i++){
			Object[] args=new Object[]{bid.get(i)};
			argList.add(args);
		}
		this.getJdbcTemplate().batchUpdate(sql, argList);
	}
	
	@Override
	public void batchOrder(List bid,List order) {
		String sql="update dw_article set `order`=? where id=?";
		List argList=new ArrayList();
		for(int i=0;i<bid.size();i++){
			Object[] args=new Object[]{order.get(i),bid.get(i)};
			argList.add(args);
		}
		this.getJdbcTemplate().batchUpdate(sql, argList);
	}

	@Override
	public void batchStatus(List bid, int status) {
		String sql="update dw_article set status=? where id=?";
		List argList=new ArrayList();
		for(int i=0;i<bid.size();i++){
			Object[] args = new Object[]{status,bid.get(i)};
			argList.add(args);
		}
		this.getJdbcTemplate().batchUpdate(sql, argList);
	}

	@Override
	public void modifyArticleStatus(Article a) {
		String sql = "update dw_article set status=? where id=?";
		this.getJdbcTemplate().update(sql,a.getStatus(),a.getId());

	}

	@Override
	public void addArticleFields(long aid,String files) {
		String sql = "insert into  dw_article_fields(aid,files) values(?,?)";
		getJdbcTemplate().update(sql, aid,files);
	}

	@Override
	public List getArticleFields(long aid) {
		String sql="select * from dw_article_fields where aid=?";
		List list=new ArrayList();
		list=getJdbcTemplate().query(sql, new Object[]{aid}, new RowMapper(){
			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				ArticleField f=new ArticleField();
				f.setAid(rs.getLong("aid"));
				f.setFiles(rs.getString("files"));
				return f;
			}
		});
		return list;
	}

	@Override
	public void modifyArticleFields(long aid, String files) {
		String sql = "update dw_article_fields set files=? where aid=?";
		getJdbcTemplate().update(sql, files,aid);
	}

	@Override
	public void delArticleFields(long aid) {
		String sql = "delete from  dw_article_fields  where aid=?";
		getJdbcTemplate().update(sql,aid);
	}

	@Override
	public List getAllSiteType() {
		String sql = "select a.*,s.name as sitename from dw_article a left join dw_site s on a.site_id=s.site_id group by s.site_id";
		logger.debug("getAllSiteType():" + sql);
		List list = new ArrayList();
		try{
			list = this.getJdbcTemplate().query(sql, new Object[]{}, getBeanMapper(Article.class));

		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 start
	@Override
	public int count(String catalog, SearchParam param) {
		int total = 0;
		String selectSql = "select count(*) from dw_article as a where 1=1 and a.site_id = :catalog";
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		sb.append(searchSql);
		String sql = sb.toString();
		logger.debug("公告个数:" + sql);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("catalog", catalog);
			total = getNamedParameterJdbcTemplate().queryForInt(sql, map);
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}
	
	/**
	 * 获取文章列表
	 * @param catalog catalog
	 * @param start start
	 * @param end end
	 * @param param param
	 * @return list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Article> getList(String catalog, int start, int end, SearchParam param) {
		long s = System.currentTimeMillis();
		String selectSql = "select * from dw_article as a where 1=1 and a.status=1 and a.site_id = :catalog";
		String searchSql = param.getSearchParamSql();
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(searchSql).append(" order by a.is_top desc, a.order asc, a.publish desc limit :start,:end");
		String sql = sb.toString();
		logger.debug("公告List SQL:" + sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("catalog", catalog);
		map.put("start", start);
		map.put("end", end);
		long e = System.currentTimeMillis();
		logger.info("GetArticleList Cost Time:" + (e - s));
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(Article.class));
	}
	// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 end
	
}
