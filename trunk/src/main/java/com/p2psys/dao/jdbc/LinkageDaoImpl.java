package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.LinkageDao;
import com.p2psys.domain.Areainfo;
import com.p2psys.domain.Linkage;
import com.p2psys.domain.LinkageType;
import com.p2psys.model.SearchParam;
import com.p2psys.util.StringUtils;

public class LinkageDaoImpl extends BaseDaoImpl implements LinkageDao {

	private static Logger logger = Logger.getLogger(LinkageDaoImpl.class);

	@Override
	public List<Linkage> getLinkageByTypeid(int typeid, String type) {
		String sql = "select a.* from dw_linkage a where a.type_id=? "
				//v1.6.7.2 RDPROJECT-573 liukun 2013-12-11 start
				+ " and a.status = 1 "
				//v1.6.7.2 RDPROJECT-573 liukun 2013-12-11 end
				+ getOrderSql(type);
		List<Linkage> list = new ArrayList<Linkage>();
		list = this.getJdbcTemplate().query(sql, new Object[] { typeid }, getBeanMapper(Linkage.class));
		return list;
	}

	@Override
	public List<Linkage> getLinkageByNid(String nid, String type) {

		String sql = "select a.* from dw_linkage a,dw_linkage_type b where a.type_id=b.id and b.nid=? "
				//v1.6.7.2 RDPROJECT-573 liukun 2013-12-11 start
				+ " and a.status = 1 "
				//v1.6.7.2 RDPROJECT-573 liukun 2013-12-11 end
				+ getOrderSql(type);
		List<Linkage> list = new ArrayList<Linkage>();
		list = this.getJdbcTemplate().query(sql, new Object[] { nid }, getBeanMapper(Linkage.class));
		return list;
	}

	@Override
	public List<Areainfo> getAreainfoByPid(String pid) {
		String sql = "select p.* from dw_area p where p.pid=?";
		List<Areainfo> list = null;
		try {
//			list = this.getJdbcTemplate().query(sql, new Object[] { pid },
//					new AreainfoMapper());
			list = this.getJdbcTemplate().query(sql, new Object[] { pid }, getBeanMapper(Areainfo.class));
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Linkage getLinkageById(long id) {
		String sql = "select a.* from dw_linkage a,dw_linkage_type b where a.type_id=b.id and a.id=?";
		Linkage a = null;
		try {
			a = this.getJdbcTemplate().queryForObject(sql, new Object[] { id },
					getBeanMapper(Linkage.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public Linkage getLinkageByValue(String nid, String value) {
		String sql = "select a.* from dw_linkage a,dw_linkage_type b where a.type_id=b.id and b.nid=? and a.value=?";
		Linkage a = null;
		try {
			a = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { nid, value }, getBeanMapper(Linkage.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return a;
	}

	private String getOrderSql(String type) {
		String orderSql = "";
		//v1.6.7.2 RDPROJECT-573 liukun 2013-12-11 start
		/*if (type.equals("value")) {
			orderSql = "order by value+\"\"";
		} else {
			orderSql = "order by a.id";
		}*/
		orderSql = " order by a.`order` asc ";
		//v1.6.7.2 RDPROJECT-573 liukun 2013-12-11 end
		return orderSql;
	}

	@Override
	public String getLinkageNameByid(long id) {
		String sql = "SELECT `name` FROM dw_linkage where id=?";
		try {
			return getJdbcTemplate().queryForObject(sql, new Object[] { id },
					String.class);
		} catch (Exception e) {
			return "-";
		} finally {

		}
	}

	@Override
	public String getAreaByPid(String id) {
		//v1.6.7.1 安全优化 sj 2013-11-23 start
		if(StringUtils.isBlank(id) || Integer.parseInt(id)<1){
			return "-";
		}
		//v1.6.7.1 安全优化 sj 2013-11-23 end
		String sql = "select name from dw_area where id =?";
		try {
			return getJdbcTemplate().queryForObject(sql, new Object[] { id },
					String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return "-";
		} finally {

		}
	}

	//v1.6.7.2 RDPROJECT-26 sj 2013-12-24 start
	@Override
	public int getLinkageTypeCount(SearchParam searchParam) {
		String sql = "select count(*) from dw_linkage_type where 1=1 ";
		sql=sql + searchParam.getSearchParamSql();
		return getJdbcTemplate().queryForInt(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getLinkageTypeBySearch(int start, int pernum,
			SearchParam searchParam) {
		String sql = "select l.* from dw_linkage_type l where 1=1 ";
		sql += searchParam.getSearchParamSql();
		sql += " order by l.id desc ";
		sql += "  limit ?,?";
		return getJdbcTemplate().query(sql, new Object[] { start , pernum }, getBeanMapper(LinkageType.class));
	}

	@Override
	public void addLinkageType(LinkageType linkageType) {
		String sql = "insert into dw_linkage_type(`order`,`name`,nid,addtime,addip) values(?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, linkageType.getOrder(),linkageType.getName(),
				linkageType.getNid(),linkageType.getAddtime(),linkageType.getAddip());
		
	}

	@Override
	public int getLinkageCount(SearchParam searchParam) {
		String sql = "select count(*) from dw_linkage where 1=1 ";
		sql=sql + searchParam.getSearchParamSql();
		return getJdbcTemplate().queryForInt(sql);
	}

	@Override
	public List getLinkageBySearch(int start, int pernum,
			SearchParam searchParam) {
		String sql = "select * from dw_linkage where 1=1 ";
		sql += searchParam.getSearchParamSql();
		sql += " order by id desc ";
		sql += "  limit ?,?";
		return getJdbcTemplate().query(sql, new Object[] { start , pernum }, getBeanMapper(Linkage.class));
	}

	@Override
	public List<LinkageType> getTypeList() {
		String sql = "select * from dw_linkage_type ";
		
		return this.getJdbcTemplate().query(sql, new Object[]{},getBeanMapper( LinkageType.class));
	}

	@Override
	public void addLinkage(Linkage linkage) {
		String sql = "insert into dw_linkage(status,`order`,type_id,pid,`name`,`value`,addtime,addip) values(?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, linkage.getStatus(),linkage.getOrder(),linkage.getType_id(),linkage.getPid(),
				linkage.getName(),linkage.getValue(),linkage.getAddtime(),linkage.getAddip());
	}

	@Override
	public void updateLinkage(Linkage linkage) {
		String sql = "update dw_linkage set status=?,`order`=?,type_id=?,pid=?,`name`=?,`value`=?,addip=? where id=?";
		this.getJdbcTemplate().update(sql, linkage.getStatus(),linkage.getOrder(),linkage.getType_id(),linkage.getPid(),
				linkage.getName(),linkage.getValue(),linkage.getAddip(),linkage.getId());
	}

	@Override
	public LinkageType getLinkageTypeById(long id) {
		String sql = "select b.* from dw_linkage_type b where b.id=?";
		LinkageType a = null;
		try {
			a = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { id }, getBeanMapper(LinkageType.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public void updateLinkageType(LinkageType linkageType) {
		String sql = "update dw_linkage_type set `order`=?,`name`=?,nid=?,addip=? where id=?";
		this.getJdbcTemplate().update(sql, linkageType.getOrder(),
				linkageType.getName(),linkageType.getNid(),linkageType.getAddip(),linkageType.getId());
	}
	//v1.6.7.2 RDPROJECT-26 sj 2013-12-24 end
}
