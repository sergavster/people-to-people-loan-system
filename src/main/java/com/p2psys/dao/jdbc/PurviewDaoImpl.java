package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.dao.PurviewDao;
import com.p2psys.domain.Purview;

public class PurviewDaoImpl extends BaseDaoImpl implements PurviewDao {
	private Logger logger=Logger.getLogger(PurviewDaoImpl.class);
	@Override
	public List getPurviewByPid(long pid) {
		String sql="select * from dw_purview where pid=?";
		logger.debug("getPurviewByPid():"+sql);
		List list=new ArrayList();
		try {
			list=getJdbcTemplate().query(sql, new Object[]{pid}, getBeanMapper(Purview.class));
		} catch (DataAccessException e) {
			logger.debug("getPurviewByPid():"+e.getMessage());
		}
		return list;
	}

	@Override
	public List getPurviewByUserid(long user_id) {
		String sql="select u.username,tp.user_type_id,p.* from dw_purview p " +
				"inner join dw_user_typepurview tp on p.id=tp.purview_id " +
				"left join dw_user u on u.type_id=tp.user_type_id where 1=1 " +
				"and u.user_id=? order by p.level,p.pid";
		logger.debug("getPurviewByUserid():"+sql);
		List list=new ArrayList();
		try {
			list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(Purview.class));
		} catch (DataAccessException e) {
			logger.debug("getPurviewByUserid():"+e.getMessage());
		}
		return list;
	}
	
	@Override
	public List getAllPurview() {
		String sql="select p.* from dw_purview p ";
		logger.debug("getPurviewByUserid():"+sql);
		List list=new ArrayList();
		try {
			list=this.getJdbcTemplate().query(sql, new Object[]{}, getBeanMapper(Purview.class));
		} catch (DataAccessException e) {
			logger.debug("getPurviewByUserid():"+e.getMessage());
		}
		return list;
	}
	
	@Override
	public List getAllCheckedPurview(long user_typeid) {
		String sql="select purview.*,temp.user_type_id " +
				"from dw_purview purview " +
				"left join (select typepurview.* from dw_user_typepurview typepurview, dw_user_type type " +
				"where type.type_id=typepurview.user_type_id and type.type_id=?) temp " +
				"on temp.purview_id=purview.id";
		logger.debug("getAllCheckedPurview():"+sql);
		List list=new ArrayList();
		try {
			list=this.getJdbcTemplate().query(sql, new Object[]{user_typeid}, getBeanMapper(Purview.class));
		} catch (DataAccessException e) {
			logger.debug("getPurviewByUserid():"+e.getMessage());
		}
		return list;
	}
	
	

	@Override
	public void addPurview(Purview p) {
		String sql="insert into dw_purview(name,purview,url,pid,level,remark) values(?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, p.getName(),p.getPurview(),p.getUrl(),p.getPid(),p.getLevel(),p.getRemark());
	}

	@Override
	public Purview getPurview(long id) {
		String sql="select * from dw_purview where id=?";
		Purview p=null;
		try {
			p=getJdbcTemplate().queryForObject(sql, new Object[]{id}, getBeanMapper(Purview.class));
		} catch (DataAccessException e) {
			logger.debug("getPurview():"+e.getMessage());
		}
		return p;
	}

	@Override
	public void delPurview(long id) {
		String sql="delete from dw_purview where id=?";
		this.getJdbcTemplate().update(sql,id);
	}
	
	@Override
	public boolean isRoleHasPurview(long id){
		String sql="select 1 from dw_user_typepurview tp left join dw_purview p " +
				"on p.id=tp.purview_id where 1=1 and p.id=?";
		SqlRowSet rowSet=getJdbcTemplate().queryForRowSet(sql,new Object[]{id});
		if(rowSet.next()){
			return true;
		}
		return false;
	}

	@Override
	public void modifyPurview(Purview p) {
		String sql="update dw_purview set name=?,purview=?,url=?,remark=? where id=?";
		getJdbcTemplate().update(sql,new Object[]{p.getName(),p.getPurview(),p.getUrl(),p.getRemark(),p.getId()});
	}

	@Override
	public void addUserTypePurviews(List<Integer> purviewid,long user_type_id) {
		String sql="insert into dw_user_typepurview(user_type_id,purview_id) values(?,?)";
		List argsList=new ArrayList();
		for(Integer id:purviewid){
			Object[] args=new Object[]{user_type_id,id};
			argsList.add(args);
		}
		getJdbcTemplate().batchUpdate(sql, argsList);
	}

	@Override
	public void delUserTypePurviews(long user_type_id) {
		String sql="delete from dw_user_typepurview where user_type_id=?";
		getJdbcTemplate().update(sql, new Object[]{user_type_id});
	}
	
}
