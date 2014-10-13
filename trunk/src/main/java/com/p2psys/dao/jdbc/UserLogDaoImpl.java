package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.p2psys.dao.UserLogDao;
import com.p2psys.domain.UserLog;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserLogModel;

public class UserLogDaoImpl extends BaseDaoImpl implements UserLogDao {

	private static Logger logger = Logger.getLogger(UserLogDaoImpl.class);  
	
	@Override
	public void addUserLog(UserLog userLog) {
		String sql = "insert into dw_user_log(user_id,query,url,result,addtime,addip)" +
						"values(?,?,?,?,?,?)";
		logger.debug("SQL:"+sql);
		this.getJdbcTemplate().update(sql,userLog.getUser_id(),userLog.getQuery(),userLog.getUrl(),
				    userLog.getResult(),userLog.getAddtime(),userLog.getAddip() );
		
	}

	@Override
	public int getLogCountByUserId(long userId,SearchParam param) {
		int logCount = 0;
		String searchParamSql = param.getUserLogSearchParamSql();
		String sql = "select count(1) from dw_user_log as p1 left join dw_user as p2 on p1.user_id = p2.user_id where p2.user_id = ?"+
						searchParamSql +"order by p1.addtime desc";
		logger.debug("user_id:"+userId);
		logger.debug("SQL:"+sql);
		logCount = count(sql, new Object[]{userId});
		
		return logCount;
	}

	@Override
	public List getLogListByUserId(long userId, int start, int end,
			SearchParam param) {
		String searchParamSql = param.getUserLogSearchParamSql();
		int pernum = end - start -1;
		String sql = "select p1.*,p2.username,p2.realname from dw_user_log as p1 left join dw_user as p2 on p1.user_id = p2.user_id where p2.user_id = ?"+
						searchParamSql +"order by p1.addtime desc limit ?,?";
		logger.debug("user_id:"+userId);
		logger.debug("pernum:"+pernum);
		logger.debug("SQL:"+sql);
		List list = new ArrayList();
//		list = getJdbcTemplate().query(sql, new Object[]{userId,start,pernum},new UserLogModelMapper());
		list = getJdbcTemplate().query(sql, new Object[]{userId,start,pernum},getBeanMapper(UserLogModel.class));
		
		return list;
	}

	@Override
	public int getLogCountByParam(SearchParam param) {
		int logCount = 0;
		String searchParamSql2Admin = param.getUserLogSearchParamSql();
		String sql = "select count(1) from dw_user_log as p1 left join dw_user as p2 on p1.user_id = p2.user_id where p2.user_id = ?"+
						searchParamSql2Admin +"order by p1.addtime desc";
		logger.debug("SQL:"+sql);
		logCount = count(sql);
		
		return logCount;
	}

	@Override
	public List getLogListByParams(int start, int end, SearchParam param) {
		
		String searchParamSql2Admin = param.getUserLogSearchParamSql();
		int pernum = end - start -1;
		String sql = "select p1.*,p2.username,p2.realname from dw_user_log as p1 left join dw_user as p2 on p1.user_id = p2.user_id where p2.user_id = ?"+
						searchParamSql2Admin +"order by p1.addtime desc limit ?,?";
		logger.debug("pernum:"+pernum);
		logger.debug("SQL:"+sql);
		List list = new ArrayList();
//		list = getJdbcTemplate().query(sql, new Object[]{start,pernum},new UserLogModelMapper());
		list = getJdbcTemplate().query(sql, new Object[]{start,pernum}, getBeanMapper(UserLogModel.class));
		
		return list;
	}

	
}
