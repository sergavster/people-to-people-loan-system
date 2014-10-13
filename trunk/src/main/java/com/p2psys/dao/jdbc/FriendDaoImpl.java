package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.FriendDao;
import com.p2psys.domain.Friend;

public class FriendDaoImpl extends BaseDaoImpl implements FriendDao {

	private static Logger logger = Logger.getLogger(BorrowDaoImpl.class);

	public List<Friend> getFriendsRequest(long user_id) {
		String sql = "select a.*,b.username as username from dw_friends_request a,dw_user b where a.user_id=b.user_id and a.friends_userid=? and a.status=0 order by a.addtime desc";

		logger.info("SQL:" + sql);
		logger.info("SQL:" + user_id);
		List<Friend> list = new ArrayList<Friend>();
//		list = this.getJdbcTemplate().query(sql, new Object[] { user_id },
//				new FriendsMapper());
		list = this.getJdbcTemplate().query(sql, new Object[] { user_id }, getBeanMapper(Friend.class));
		return list;
	}

	@Override
	public int countFriendsRequest(long user_id) {
		int total = 0;
		String sql = "select count(1) from dw_friends_request a,dw_user b where a.friends_userid=b.user_id and a.user_id=? and a.status=0";

		logger.info("SQL:" + sql);
		logger.info("SQL:" + user_id);
		try {
			total = this.getJdbcTemplate().queryForInt(sql,
					new Object[] { user_id });
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}
	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 start 
	@Override
	public List<Friend> getFriendsByUserId(long user_id, int status, String username) {
		String sql = "select a.*,b.username as username from dw_friends a,dw_user b where a.friends_userid=b.user_id and a.user_id=? and a.status=? ";
		Object[] obj = null;
		if (username != null && !"".equals(username)) {
			sql = sql + "and b.username like ?";
			obj = new Object[] { user_id, status, "%" + username + "%" };
		} else {
			obj = new Object[] { user_id, status };
		}
		sql = sql + "order by a.addtime desc";

		logger.info("SQL:" + sql);
		logger.info("SQL:" + user_id);
		List<Friend> list = new ArrayList<Friend>();
//		list = this.getJdbcTemplate().query(sql, obj, new FriendsMapper());
		list = this.getJdbcTemplate().query(sql, obj,  getBeanMapper(Friend.class));
		return list;
	}
	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 end
	@Override
	public List<Friend> getUnVerifyList(long userid, long friendId, int status) {
		String sql = "select a.*,b.username as username from dw_friends a,dw_user b where a.friends_userid=b.user_id " +
				"and a.user_id=? and a.friends_userid=? and a.status=? order by a.addtime desc";
		logger.info("SQL:" + sql);
		logger.info("SQL:" + userid);
		logger.info("SQL:" + friendId);
		List<Friend> list = new ArrayList<Friend>();
//		list = this.getJdbcTemplate().query(sql, new Object[] { userid, friendId, status },
//				new FriendsMapper());
		list = this.getJdbcTemplate().query(sql, new Object[] { userid, friendId, status }, getBeanMapper(Friend.class));
		return list;
	}
	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 start 
	@Override
	public int countFriendsByUserId(long user_id, String username) {
		int total = 0;
		String sql = "";
		Object[] obj = null;
		if (username != null && !"".equals(username)) {
			sql = "select count(1) from dw_friends a dw_user b where a.friends_userid=b.user_id and a.user_id=? and b.username like ?";
			obj = new Object[] { user_id, "%" + username + "%" };
		} else {
			sql = "select count(1) from dw_friends a where a.user_id=?";
			obj = new Object[] { user_id };
		}

		logger.info("SQL:" + sql);
		logger.info("SQL:" + user_id);
		try {
			total = this.getJdbcTemplate().queryForInt(sql, obj);
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}
	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 end 

	@Override
	public void addFriend(Friend friend) {
		String sql = "insert into dw_friends(user_id,friends_userid,status,type,content,addtime,addip) " +
				"values(?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, friend.getUser_id(),friend.getFriends_userid(),friend.getStatus(),
				friend.getType(),friend.getContent(),friend.getAddtime(),friend.getAddip());
	}
	@Override
	public void addFriendRequest(Friend friend) {
		String sql = "insert into dw_friends_request(user_id,friends_userid,status,type,content,addtime,addip) " +
				"values(?,?,?,?,?,?,?)";
		logger.info("SQL:"+sql);
		this.getJdbcTemplate().update(sql, friend.getUser_id(),friend.getFriends_userid(),friend.getStatus(),
				friend.getType(),friend.getContent(),friend.getAddtime(),friend.getAddip());
	}
	@Override
	public void modifyFriendRequest(Friend friend) {
		String sql="update dw_friends_request set status=?,type=?,content=? " +
				"where (user_id=? and friends_userid=?) " +
				"or (friends_userid=? and user_id=?)";
		logger.info(sql);
		this.getJdbcTemplate().update(sql, friend.getStatus(),friend.getType(),friend.getContent(),
				friend.getUser_id(),friend.getFriends_userid(),friend.getUser_id(),friend.getFriends_userid());
	}

	@Override
	public void modifyFriend(Friend friend) {
		String sql="update dw_friends set status=?,type=?,content=? " +
				"where (user_id=? and friends_userid=?) " +
				"or (friends_userid=? and user_id=?)";
		logger.info(sql);
		this.getJdbcTemplate().update(sql, friend.getStatus(),friend.getType(),friend.getContent(),
				friend.getUser_id(),friend.getFriends_userid(),friend.getUser_id(),friend.getFriends_userid());
	}
	
	@Override
	public void modifyFriendStatus(int status,long user_id,long frienduser_id) {
		String sql="update dw_friends set status=? " +
				"where (user_id=? and friends_userid=?) ";
		logger.info(sql);
		this.getJdbcTemplate().update(sql,status, user_id,frienduser_id);
	}

	@Override
	public void deleteFriend(long user_id, long frienduser_id) {
		String sql="delete from dw_friends where (user_id=? and friends_userid=?) ";
		logger.info(sql);
		this.getJdbcTemplate().update(sql,user_id,frienduser_id);
	}

	@Override
	public void deleteFriendRequest(long user_id, long frienduser_id) {
		String sql="delete from dw_friends_request where (user_id=? and friends_userid=?) ";
		logger.info(sql);
		this.getJdbcTemplate().update(sql,user_id,frienduser_id);
	}
	
	
	
}
