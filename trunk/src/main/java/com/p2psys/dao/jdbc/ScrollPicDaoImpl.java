package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.ScrollPicDao;
import com.p2psys.domain.ScrollPic;
import com.p2psys.domain.ScrollPicType;
import com.p2psys.model.SearchParam;
public class ScrollPicDaoImpl extends BaseDaoImpl implements ScrollPicDao {

	private static Logger logger = Logger.getLogger(ScrollPicDaoImpl.class);

	@Override
	public List getList(int type_id, int start, int end) {
		long s = System.currentTimeMillis();
		String sql = "select * from dw_scrollpic  where 1=1 ";
		// 拼装SQL
		String ordersql = "";
		if (type_id != 0) {
			String wheresql = " and type_id=" + type_id + "";
			sql += wheresql;
			ordersql = " order by sort ";
		}
		String limitsql = "  limit " + start + "," + end + "";
		sql += ordersql + limitsql;
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
		try {
			list = getJdbcTemplate().query(sql, getBeanMapper(ScrollPic.class));
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		long e = System.currentTimeMillis();
		logger.info("GetScrillPicList Cost Time:" + (e - s));
		return list;
	}
	
	@Override
	public List<ScrollPic> getList(int start, int end) {
		long s = System.currentTimeMillis();
		String sql = "select * from dw_scrollpic where 1=1 order by sort limit " + start
				+ "," + end;
		// 拼装SQL
		logger.debug("SQL:" + sql);
		List<ScrollPic> list = new ArrayList<ScrollPic>();
		try {
			list = getJdbcTemplate().query(sql, getBeanMapper(ScrollPic.class));
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		long e = System.currentTimeMillis();
		logger.info("GetScrillPicList Cost Time:" + (e - s));
		return list;
	}
	
	@Override
	public int count(int type_id, SearchParam param) {
		String sql = "select count(1) from dw_scrollpic where 1=1 ";
		// 拼装SQL
		if(type_id!=0){
			String wheresql = " and type_id=" + type_id + "";
			sql += wheresql;
		}
		logger.debug("SQL:" + sql);
		String searchSql = param.getSearchParamSql();
		sql += searchSql;
		logger.debug("scrollPicCount():"+sql);
		int total = 0;
		total = this.count(sql);
		return total;
	}
	
	@Override
	public ScrollPic getScrollPicById(long id) {
		String sql = "select * from dw_scrollpic where id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + id);
		ScrollPic sp = null;
		try {
			sp = getJdbcTemplate().queryForObject(sql, new Object[] { id }, getBeanMapper(ScrollPic.class));
		} catch (DataAccessException e) {
			logger.debug("BorrowDao.getBorrowById,数据库查询结果为空！");
		}
		return sp;
	}

	@Override
	public void delScrollPic(long id) {
		String sql = "delete from dw_scrollpic where id=?";
		getJdbcTemplate().update(sql,id);
	}

	@Override
	public void modifyScrollPic(ScrollPic sp) {
		String sql="update dw_scrollpic set site_id=?,status=?,sort=?,flag=?,type_id=?,url=?,name=?,pic=?,summary=?" +
				",hits=?,addtime=?,addip=? where id=?";
		logger.info("SQL:" + sql);
		try{
			getJdbcTemplate().update(sql,sp.getSite_id(),sp.getStatus(),sp.getSort(),sp.getFlag(),sp.getType_id(),sp.getUrl(),
					sp.getName(),sp.getPic(),sp.getSummary(),sp.getHits(),sp.getAddtime(),sp.getAddip(),sp.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

	@Override
	public ScrollPic addScrollPic(ScrollPic sp) {
		String sql = "insert into dw_scrollpic(site_id,status,sort,flag,type_id,url,name,pic,summary,hits,addtime,addip)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,sp.getSite_id(),sp.getStatus(),sp.getSort(),sp.getFlag(),sp.getType_id(),sp.getUrl(),
				sp.getName(),sp.getPic(),sp.getSummary(),sp.getHits(),sp.getAddtime(),sp.getAddip());
		return sp;
	}

	@Override
	public List getScrollPicType(){
		String sql = "select * from dw_scrollpic_type ";
		logger.debug("getScrollpicType():" + sql);
		List list = new ArrayList();
		try{
			list = this.getJdbcTemplate().query(sql, new Object[]{}, getBeanMapper(ScrollPicType.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
}
