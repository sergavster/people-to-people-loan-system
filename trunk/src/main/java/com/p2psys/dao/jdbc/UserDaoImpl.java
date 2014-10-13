package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.p2psys.dao.UserDao;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.domain.UserType;
import com.p2psys.model.DetailUser;
import com.p2psys.model.SearchParam;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 用户表新增、删除、查找Dao类
 * 
 
 * @date 2012-7-2-下午2:08:27
 * @version 1
 * 
 *           (c)</b> 2012-51-<br/>
 * 
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	/**
	 * 新增用户
	 * 
	 * @param u
	 */
	public void addUser(User u) {
		// v1.6.6.2 RDPROJECT-307 lhm 2013-10-28 start
		// 增加手机号码
		String sql = "insert into  dw_user(username,password,email,realname,invite_userid,status,type_id,qq,addtime,addip,email_status,real_status,phone) "
				+ "values (?,?, ?, ?, ?,?,?,?,?,?,?,'0',?)";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, u.getUsername(), u.getPassword(),
				u.getEmail(), u.getRealname(), u.getInvite_userid(),
				u.getStatus(), u.getType_id(),u.getQq(), u.getAddtime(), u.getAddip(),u.getEmail_status(),u.getPhone());
		// v1.6.6.2 RDPROJECT-307 lhm 2013-10-28 end
	}

	/**
	 * 检查是否存在username的用户记录
	 */
	public User getUserByUsername(String username) {
		String sql = "select * from dw_user where username = ?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + username);
		User user = null;
		try {
			user = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { username }, getBeanMapper(User.class));
		} catch (DataAccessException e) {
			logger.debug("getUserByUsername() " + e.getMessage());
		}
		return user;
	}
	
	// v1.6.7.1 注册校验，防止表中有多个相同的用户，把对象改成count zza 2013-11-26 start
	@Override
	public int getCountByUsername(String username) {
		String sql = "select count(1) from dw_user where username = ?";
		logger.debug("getUserCount():" + sql);
		return getJdbcTemplate().queryForInt(sql, new Object[] {username});
		
	}
	// v1.6.7.1 注册校验，防止表中有多个相同的用户，把对象改成count zza 2013-11-26 end
	
	/**
	 * 检查是否存在username的用户记录
	 */
	public List<Map<String, Object>> getUserInUsername(List<String> nameList) {
		String sql = "select user_id,username from dw_user";
		StringBuffer sb = new StringBuffer(sql);
		int size = nameList.size();
		if (!nameList.isEmpty() && size > 0) {
			sb.append(" where username in (");
			String name = null;
			for (int i = 0; i < size; i++) {
				if (size - i > 0) {
					name = nameList.get(i);
					sb.append("'" + name + "'"); 
					if (size - i != 1) {
						sb.append(",");
					}
				}
			}
			sb.append(")");
		}
		logger.debug("username " + sb.toString());
		List<Map<String, Object>> list = null;
		try {
			list = this.getJdbcTemplate().queryForList(sb.toString());
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return list;
	}

	public User getUserByEmail(String email) {
		String sql = "select user_id, email from dw_user where email = ?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + email);
		User user = null;
		try {
			user = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { email }, new RowMapper<User>() {
						public User mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							User user = new User();
							user.setUser_id(rs.getLong("user_id"));
							user.setEmail(rs.getString("email"));
							return user;
						}
					});
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return user;
	}
	
	public int countByEmail(String email) {
		String sql = "select count(1) from dw_user where email = :email";
		Map<String , Object> map = new HashMap<String, Object>();
		try {
			map.put("email", email);
			return getNamedParameterJdbcTemplate().queryForInt(sql, map);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public User getUserByUsernameAndPassword(String username, String password) {
		String sql = "select * from dw_user where username = ? and password=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + username + "," + password);
		User user = null;
		try {
			user = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { username, password }, getBeanMapper(User.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return user;
	}

	public User getUserByUserid(long userid) {
		String sql = "select * from dw_user where user_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + userid);
		User user = null;
		try {
			user = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { userid }, getBeanMapper(User.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return user;
	}

	String queryUserSql = " from dw_user as p1 "
			+ "left join dw_credit as p2 on p1.user_id=p2.user_id "
			+ "left join dw_account as p9 on p1.user_id=p9.user_id "
			+ "left join dw_credit_rank as p3 on p2.value<=p3.point2  and p2.value>=p3.point1 "
			+ "left join dw_user_cache as p4 on p1.user_id=p4.user_id "
			+ "left join dw_area as p5 on p5.id=p1.province "
			+ "left join dw_area as p6 on p6.id=p1.city "
			+ "left join dw_area as p7 on p7.id=p1.area "
			+ "left join dw_user_type p8 on p8.type_id=p1.type_id "
			
			+ "where 1=1 ";
	
	// v1.6.7.1 RDPROJECT-446 zza 2013-11-20 start
	String selectSql = "select p1.*,p2.value as credit_jifen,p9.use_money ,p3.pic as credit_pic,"
			+ "p4.vip_status,p4.vip_verify_time,p4.kefu_addtime,p4.kefu_username,p4.type,p4.vip_end_time,"
			//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
			+ "p4.phone_verify_time,p4.realname_verify_time,p4.video_verify_time,p4.scene_verify_time,"
			//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
			+ "p5.name as provincetext,p6.name as citytext,p7.name as areatext,p8.name as typename";
	// v1.6.7.1 RDPROJECT-446 zza 2013-11-20 end
	
	/**
	 * 获取用户的信息，以及相应的积分
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DetailUser getDetailUserByUserid(long userid) {
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(queryUserSql).append("and p1.user_id = ?");
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + userid);
		DetailUser user = null;
		try {
			user = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { userid }, getBeanMapper(DetailUser.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void updateUser(User user) {
		String sql = "update dw_user  set realname=?,sex=?,phone=?,province=?,city=?,area=?,invite_money=? where user_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user.getRealname());
		logger.debug("SQL:" + user.getPhone());
		this.getJdbcTemplate().update(sql, user.getRealname(), user.getSex(),
				user.getPhone(), user.getProvince(), user.getCity(),
				user.getArea(), user.getInvite_money(), user.getUser_id());
	}

	@Override
	public int realnameIdentify(User user) {
		// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 start
		String sql = "";
		int result = 0;
		if(user.getNature()==2){
			sql = "update dw_user set real_status=?,realname=?,birthday=?,card_id=?,province=?,city=?,area=?,card_pic1=?,card_pic2=?,nature=?,address=? where user_id=?";
		}else{
			sql = "update dw_user set real_status=?,realname=?,sex=?,nation=?,birthday=?,card_type=?,card_id=?,province=?,city=?,area=?,card_pic1=?,card_pic2=? where user_id=?";
		}
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user.getRealname());
		if(user.getNature()==2){
			result = this.getJdbcTemplate().update(sql, 
					user.getReal_status(),user.getRealname(),
					user.getBirthday(),user.getCard_id(),
					user.getProvince(), user.getCity(), user.getArea(),
					user.getCard_pic1(), user.getCard_pic2(), 
					user.getNature(),user.getAddress(),
					user.getUser_id());
		}else{
			result = this.getJdbcTemplate().update(sql, 
					user.getReal_status(),user.getRealname(),
					user.getSex(), user.getNation(),
					user.getBirthday(),user.getCard_type(), user.getCard_id(),
					user.getProvince(), user.getCity(), user.getArea(),
					user.getCard_pic1(), user.getCard_pic2(), user.getUser_id());
		}
		return result;
		// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 end
	}


	@Override
	public User modifyUserPwd(User user) {
		String sql = "update dw_user set password=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getPassword(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}
	
	@Override
	public User modifyPayPwd(User user) {
		String sql = "update dw_user set paypassword=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getPaypassword(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}

	@Override
	public User modifyAnswer(User user) {
		String sql = "update dw_user set question=?,answer=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getQuestion(),
				user.getAnswer(), user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}

	@Override
	public List<User> getAllKfList(int type) {
		//v1.6.6.2 RDPROJECT-266 wcw 2013-10-28 start
	    //type=1  前台范围   type==2   后台范围
	    String sql ="";
	    if(type==1){
		    sql = "select * from dw_user where type_id=3 order by rand() ";
		}else{
		    sql = "select * from dw_user where type_id=3";
		}
		//v1.6.6.2 RDPROJECT-266 wcw 2013-10-28 end
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(User.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<User> getAllVerifyUser() {
		String sql="select * from dw_user where type_id=9";
		logger.debug("SQL" + sql);
		List list=new ArrayList();
		try{
			list=this.getJdbcTemplate().query(sql,getBeanMapper(User.class));
		}catch(DataAccessException e){
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return list;
	}
	String inviteUserSql=" from dw_user p2 " +
			"left join dw_user invite on invite.user_id= p2.invite_userid " +
			"left join dw_user_cache c on c.user_id=p2.user_id " +
			"where 1=1 and invite.username is not null ";
	
	@Override
	public List getInvitedUserByUserid(long user_id) {
		StringBuffer sb=new StringBuffer("select p2.*,invite.username as invite_username,c.* ");
		sb.append(inviteUserSql).append(" and p2.invite_userid=?");
		String sql = sb.toString();
		List list = new ArrayList();
//		list = this.getJdbcTemplate().query(sql, new Object[] { user_id },
//				new VipUserMapper());
		list = this.getJdbcTemplate().query(sql, new Object[] { user_id },	getBeanMapper(DetailUser.class));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public User getUserById(long user_id) {
		String sql = "select * from dw_user where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(User.class));
		/*Object obj = super.findObjByProperty(User.class, "user_id", user_id);
		if(obj!=null){
			return (User)obj;
		}
    	return null;*/
	}
	
	String queryCountSql=" from dw_user as p1 left join dw_user_cache p4 on p1.user_id=p4.user_id left join dw_user_type as p8 on p1.type_id=p8.type_id where 1=1 ";
	
	public List getUserList(int start, int pernum,SearchParam p) {
		String searchSql=p.getSearchParamSql();
		String orderSql = " order by p1.addtime desc ";
		String limitSql=" limit ?,? ";
		StringBuffer sb=new StringBuffer(selectSql).append(queryUserSql).append(searchSql).append(orderSql).append(limitSql);
		String sql=sb.toString();
		logger.debug("getUserList():"+sql);
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		List list = getJdbcTemplate().query(sql,new Object[] { start , pernum }, getBeanMapper(DetailUser.class));
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		return list;
	}
	public List getUserList(SearchParam p) {
		String searchSql=p.getSearchParamSql();
		StringBuffer sb=new StringBuffer(selectSql).append(queryUserSql).append(searchSql);
		String sql=sb.toString();
		String orderSql = " order by p1.addtime desc ";
        sql+=orderSql;
		logger.debug("getUserList():"+sql);
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		List list = getJdbcTemplate().query(sql,getBeanMapper(DetailUser.class));
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end	
		return list;	
	}

	@Override
	public int getUserCount(SearchParam p) {
		StringBuffer sb= new StringBuffer("select count(*) ");
		sb.append(queryCountSql).append(p.getSearchParamSql());
		String sql=sb.toString();
		logger.debug("getUserCount():"+sql);
		int total=count(sb.toString());
		return total;
	}

	@Override
	public List getAllUserType() {
		String sql = "select * from dw_user_type";
//		return getJdbcTemplate().query(sql, new UserTypeMapper());
		return getJdbcTemplate().query(sql, getBeanMapper(UserType.class));
	}

	@Override
	public void updateUser(User user, boolean modify) {
		String sql = modify ? "update dw_user  set password=?,realname=?,tel=?,type_id=?,status=?,invite_userid=?,invite_money=?,qq=?,wangwang=?,address=?,serial_id=?,islock=?,sex=?,nation=?,birthday=?,card_type=?,card_id=?,phone=?,province=?,city=?,area=?,upip=?,uptime=?,email=?,logintime=?  where user_id=?"
				: "update dw_user  set realname=?,tel=?,type_id=?,status=?,invite_userid=?,invite_money=?,qq=?,wangwang=?,address=?,serial_id=?,islock=?,sex=?,nation=?,birthday=?,card_type=?,card_id=?,phone=?,province=?,city=?,area=? ,upip=?,uptime=?,email=?,logintime=? where user_id=?";
		if (modify) {
			getJdbcTemplate()
					.update(sql,
							new Object[] { user.getPassword(),
									user.getRealname(), user.getTel(),
									user.getType_id(), user.getStatus(),
									user.getInvite_userid(),
									user.getInvite_money(), user.getQq(),
									user.getWangwang(), user.getAddress(),
									user.getSerial_id(), user.getIslock(),
									user.getSex(), user.getNation(),
									user.getBirthday(), user.getCard_type(),
									user.getCard_id(), user.getPhone(),
									user.getProvince(), user.getCity(),
									user.getArea(), user.getUpip(),
									user.getUptime(),user.getEmail(),user.getLogintime(),user.getUser_id() });
		} else {
			getJdbcTemplate()
					.update(sql,
							new Object[] { user.getRealname(), user.getTel(),
									user.getType_id(), user.getStatus(),
									user.getInvite_userid(),
									user.getInvite_money(), user.getQq(),
									user.getWangwang(), user.getAddress(),
									user.getSerial_id(), user.getIslock(),
									user.getSex(), user.getNation(),
									user.getBirthday(), user.getCard_type(),
									user.getCard_id(), user.getPhone(),
									user.getProvince(), user.getCity(),
									user.getArea(), user.getUpip(),
									user.getUptime(), user.getEmail(),user.getLogintime(),user.getUser_id() });
		}
	}

	@Override
	public List getSearchUserOrRealName(int page, SearchParam p) {
		String sql = "SELECT p.user_id,p.username,p.realname,p1.vip_status,p.email_status,p.phone_status,p1.vip_status,p1.vip_verify_time from dw_user as p LEFT JOIN dw_user_cache as p1 on p.user_id=p1.user_id  where ";
		// sql += Isrealname ? "p.realname like ?" : "p.username like ?";
		logger.debug(sql);
		return getJdbcTemplate().query(sql,
				new Object[] { p.getSearchParamSql() },
				new RowMapper<DetailUser>() {
					@Override
					public DetailUser mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						DetailUser user = new DetailUser();
						user.setUser_id(rs.getLong("user_id"));
						user.setUsername(rs.getString("username"));
						user.setRealname(rs.getString("realname"));
						user.setVip_status(rs.getInt("vip_status"));
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
						user.setEmail_status(rs.getInt("email_status"));
						// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
						user.setPhone(rs.getString("phone_status"));
						user.setVip_verify_time(rs.getLong("vip_verify_time"));
						return user;
					}
				});
	}

	/**
	 * 查找用户结果的总数
	 * 
	 * @return
	 */
	public int getSearchUserOrRealName(SearchParam p) {
		String sql = "SELECT p.user_id,p.username,p.realname,p1.vip_status,p.email_status,p.phone_status,p1.vip_status,p1.vip_verify_time from dw_user as p LEFT JOIN dw_user_cache as p1 on p.user_id=p1.user_id  where ";
		return getJdbcTemplate().queryForInt(sql,
				new Object[] { p.getSearchParamSql() });
	}

	@Override
	public String getUserTypeByid(String s) {
		String sql = "SELECT `name` from dw_user_type where type_id=?";
		return getJdbcTemplate().queryForObject(sql, new Object[] { s },
				String.class);
	}

	@Override
	public void updateUserType(List<UserType> list) {
		String sql = "update dw_user_type set name=?,purview=?,`order`=?,status=?,type=?,summary=?,remark=?,addtime=? where type_id=? ";
		for (UserType u : list) {
			if (u == null)
				continue;
			getJdbcTemplate().update(
					sql,
					new Object[] { u.getName(), u.getPurview(), u.getOrder(),
							u.getStatus(), u.getType(), u.getSummary(),
							u.getRemark(), u.getAddtime(), u.getType_id() });
		}
	}

	@Override
	public void addUserType(UserType userType) {
		String sql = "insert into dw_user_type(`name`,purview,`order`,`status`,type,summary,remark,addtime,addip) VALUES(?,?,?,?,?,?,?,?,?)";
		getJdbcTemplate().update(
				sql,
				new Object[] { userType.getName(), userType.getPurview(),
						userType.getOrder(), userType.getStatus(),
						userType.getType(), userType.getSummary(),
						userType.getRemark(), userType.getAddtime(),
						userType.getAddip() });
	}

	@Override
	public void deleteUserTypeById(long id) {
		String sql = "delete from dw_user_type where type_id=?";
		getJdbcTemplate().update(sql, new Object[] { id });
	}

	/*@Override
	public List getDetailUserByUserid(SearchParam p, int page, int Max,
			int status) {
		String sql = "SELECT p1.user_id,p1.qq,p1.type_id,p1.province,p1.city,p1.area,p1.sex,p1.email,p1.username,p1.status,p1.real_status,p1.card_type,p1.card_id,p1.phone,p1.video_status,p1.scene_status,p1.realname,p2.vip_status,p1.email_status,p1.phone_status,p2.vip_status,p2.vip_verify_time,p1.addtime from dw_user as p1 LEFT JOIN dw_user_cache as p2 on p1.user_id=p2.user_id  ";
		Object[] o;
		if (status != -1) {
			o = new Object[] { status, page, Max };
			sql += "where p1.real_status=? and ";
		} else {
			o = new Object[] { page , Max };
		}
		sql = p.getSearchParamSql(sql);
		sql += " LIMIT ?,?";
		List list = getJdbcTemplate().query(sql, o,
				new RowMapper<DetailUser>() {
					@Override
					public DetailUser mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						DetailUser user = new DetailUser();
						user.setUser_id(rs.getLong("user_id"));
						user.setUsername(rs.getString("username"));
						user.setRealname(rs.getString("realname"));
						user.setReal_status(rs.getString("real_status"));
						user.setVip_status(rs.getInt("vip_status"));
						user.setEmail_status(rs.getString("email_status"));
						user.setPhone_status(rs.getString("phone_status"));
						user.setPhone(rs.getString("phone"));
						user.setEmail(rs.getString("email"));
						user.setProvince(rs.getString("province"));
						user.setCity(rs.getString("city"));
						user.setQq(rs.getString("qq"));
						user.setArea(rs.getString("area"));
						user.setCard_id(rs.getString("card_id"));
						user.setStatus(rs.getInt("status"));
						user.setSex(rs.getString("sex"));
						user.setType_id(rs.getInt("type_id"));
						user.setVip_verify_time(rs.getLong("vip_verify_time"));
						user.setAddtime(rs.getString("addtime"));
						return user;
					}
				});
		return list;
	}*/

	@Override
	public int getInviteUserCount(SearchParam param) {
		StringBuffer sb=new StringBuffer("select count(p2.user_id) ");
		sb.append(inviteUserSql).append(param.getSearchParamSql());
		String sql=sb.toString();
		int total=count(sql);
		return total;
	}

	@Override
	public List getInviteUserList(int start,int pernum, SearchParam param) {
		StringBuffer sb=new StringBuffer("select p2.*,invite.username as invite_username,c.*  ");
		String orderSql=" order by p2.addtime desc ";
		String limitSql=" limit ?,?";
		sb.append(inviteUserSql).append(param.getSearchParamSql()).append(orderSql).append(limitSql);
		String sql=sb.toString();
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{start,pernum}, new VipUserMapper());
		list=this.getJdbcTemplate().query(sql, new Object[]{start,pernum}, getBeanMapper(DetailUser.class));
		return list;
	}

	@Override
	public UserType getUserTypeById(long id) {
		
		String sql = "SELECT * from dw_user_type where type_id=?";
		UserType userType=null;
		try {
//			userType=getJdbcTemplate().queryForObject(sql, new Object[] { id },
//					new UserTypeMapper());
			userType=getJdbcTemplate().queryForObject(sql, new Object[] { id }, getBeanMapper(UserType.class));
		} catch (DataAccessException e) {
			logger.debug("getUserTypeById():not data found!");
		}
		return userType;
	}

	@Override
	public boolean isRoleHasPurview(long id) {
		boolean isRoleHasPurview = false;
		String sql="select * from dw_user where type_id=? ";
		SqlRowSet rowSet=getJdbcTemplate().queryForRowSet(sql,new Object[]{id});
		if(rowSet.next()){
			isRoleHasPurview = true;
		}
		return isRoleHasPurview;
	}
	

	@Override
	public User modifyEmail_status(User user) {
		String sql = "update dw_user set email_status=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getEmail_status(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		logger.debug("UserDaoImpl: 邮箱激活操作modifyEmail_status()：user_id:"+user.getUser_id()+", username:"+user.getUsername()+", email_status:"+user.getEmail_status()+", result："+result);
		return user;
	}
	
	@Override
	public User modifyRealname(User user) {
		String sql = "update dw_user set realname=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getRealname(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}
	
	
	@Override
	public User modifyEmail(User user) {
		String sql = "update dw_user set email=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getEmail(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}

	
	@Override
	public User modifyPhone(User user) {
		String sql = "update dw_user set phone=?,phone_status=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getPhone(),user.getPhone_status(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}
	


	@Override
	public User modifyPhone_status(User user) {
		String sql = "update dw_user set phone_status=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getPhone_status(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}

	@Override
	public User modifyVideo_status(User user) {
		String sql = "update dw_user set video_status=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getVideo_status(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}

	@Override
	public User modifyScene_status(User user) {
		String sql = "update dw_user set scene_status=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getScene_status(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}

	@Override
	public User modifyReal_status(User user) {
		String sql = "update dw_user set real_status=? where user_id=?";
		int result = this.getJdbcTemplate().update(sql, user.getReal_status(),
				user.getUser_id());
		if (result < 1) {
			user = null;
		}
		return user;
	}

	@Override
	public int getUserCount(SearchParam p, int type) {
		
		String searchSql = p.getSearchParamSql();
		String csql = "select count(*) from dw_user as p1 left join dw_user_cache c on p1.user_id=c.user_id where p1.type_id =? ";
		StringBuffer sb = new StringBuffer(csql).append(searchSql);
		String sql = sb.toString();
		logger.debug("getUserCount():"+sql);
		return getJdbcTemplate().queryForInt(sql,new Object[] {type});
		
	}

	@Override
	public List getUserList(int start, int pernum, SearchParam p, int type) {
		
		String searchSql=p.getSearchParamSql();
		String limitSql=" limit ?,? ";
		StringBuffer sb=new StringBuffer(selectSql).append(" from dw_user as p1 "
				+ "left join dw_credit as p2 on p1.user_id=p2.user_id "
				+ "left join dw_account as p9 on p1.user_id=p9.user_id "
				+ "left join dw_credit_rank as p3 on p2.value<=p3.point2  and p2.value>=p3.point1 "
				+ "left join dw_user_cache as p4 on p1.user_id=p4.user_id "
				+ "left join dw_area as p5 on p5.id=p1.province "
				+ "left join dw_area as p6 on p6.id=p1.city "
				+ "left join dw_area as p7 on p7.id=p1.area "
				+ "left join dw_user_type p8 on p8.type_id=p1.type_id "
				+ "where p1.type_id=? ").append(searchSql).append(limitSql);
		String sql=sb.toString();
		logger.debug("getUserList():"+sql);
//		List list = getJdbcTemplate().query(sql,new Object[] { type, start , pernum }, new DetailUserMapper());
		List list = getJdbcTemplate().query(sql,new Object[] { type, start , pernum }, getBeanMapper(DetailUser.class));
		return list;
	}

	@Override
	public DetailUser getDetailUser(long userid, int type) {
		
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(queryUserSql).append(" and p1.user_id =? and p1.type_id =?");
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + userid);
		DetailUser user = null;
		try {
//			user = this.getJdbcTemplate().queryForObject(sql,
//					new Object[] { userid,type }, new DetailUserMapper());
			user = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { userid,type }, getBeanMapper(DetailUser.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return user;
	}
	
	// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 start
	@Override
	public void modifyUserType(User user){
		String sql = "update dw_user set type_id=:type_id where user_id=:user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user.getUser_id());
		map.put("type_id", user.getType_id());
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}
	@Override
	public int getCountKf(SearchParam param){
		StringBuffer sql = new StringBuffer("select count(*) from dw_user where type_id=3  ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
		}
		return getNamedParameterJdbcTemplate().queryForInt(sql.toString(),map);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getKfList(SearchParam param, int start, int pernum){
		StringBuffer sql = new StringBuffer("select * from dw_user p1 where type_id=3");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			
		}
		sql.append(" order by p1.user_id desc ");
		if (pernum > 0) {// 分页查询，若为0，则取所有（导出报表时取所有）
			sql.append(" LIMIT :start,:pernum");
			map.put("start", start);
			map.put("pernum", pernum);
		}
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(User.class));
	
	}
	// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 end
	@Override
	public void updateUserLastInfo(User user) {
		// v1.6.7.2 RDPROJECT-499 2013-12-4 start
		String sql = "update dw_user  set lasttime =?,logintime=?,lastip=? where user_id =?"; 
		getJdbcTemplate().update(sql,
				new Object[] {user.getLasttime() ,user.getLogintime(),user.getLastip(),user.getUser_id() });
		// v1.6.7.2 RDPROJECT-499 2013-12-4 end
		
	}

	@Override
	public User getUserByCard(String card) {
		//String sql="select * from dw_user where real_status in (1,2) and card_id=?";
		// v1.6.7.1 RDPROJECT-289 sj 2013.11.8 start
		String sql = " select * from dw_user where real_status = 1 and card_id = ? ";
		// v1.6.7.1 RDPROJECT-289 sj 2013.11.8 end
		List<User> list=getJdbcTemplate().query(sql, new Object[]{card}, getBeanMapper(User.class));
		if(list!=null&&list.size()>0){
			return (User)list.get(0);
		}
		return null;
	}

	@Override
	public List<User> getAllUser(int type) {
		String sql = "select * from dw_user where type_id=?";
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
		try {
			list = this.getJdbcTemplate().query(sql, new Object[]{type},getBeanMapper(User.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getUserCount() {
		String sql = "select count(*) from dw_user";
		logger.debug("getUserCount():"+sql);
		return getJdbcTemplate().queryForInt(sql);
	}

	@Override
	public List getInviteUserList(SearchParam param) {
		StringBuffer sb=new StringBuffer("select p2.*,invite.username as invite_username,c.*  ");
		String orderSql=" order by p2.addtime desc ";
		sb.append(inviteUserSql).append(param.getSearchParamSql()).append(orderSql);
		String sql=sb.toString();
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new VipUserMapper());
		list=this.getJdbcTemplate().query(sql, getBeanMapper(DetailUser.class));
		return list;
	}

	@Override
	public void updateUserIsLock(long userid) {
		 String sql = "update dw_user set islock=1 where user_id=?";
		 logger.debug("SQL:" + sql);
		 getJdbcTemplate().update(sql, new Object[] { userid});
		
	}

	@Override
	public List getKfListWithLimit(int start, int end) {
		String sql = "select * from dw_user where type_id=3 limit " + start + "," + end;
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(User.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取vip用户
	 * @return
	 */
	public List<User> getVipUser() {
		String sql = "select u.* from dw_user as u left join dw_user_cache as uc on uc.user_id = u.user_id where uc.vip_status = 1;";
		List<User> list = new ArrayList<User>();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(User.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getUserCountWithBirth(SearchParam param ,String startDate, String nowDate) {
		String sql = "select count(1) from dw_user p2 left join dw_user_cache p1 on p1.user_id = p2.user_id" +
				" where substring(p2.birthday,6)>? and substring(p2.birthday,6)<? and p1.vip_status = 1";
		sql += param.getSearchParamSql();
		return getJdbcTemplate().queryForInt(sql,new Object[]{startDate,nowDate});
	}

	@Override
	public List getUserListWithBirth(String startDate, String nowDate,SearchParam param , int start, int pernum) {
		String sql = "select p2.* from dw_user p2 left join dw_user_cache p1 on p1.user_id = p2.user_id" +
				" where substring(p2.birthday,6)>? and substring(p2.birthday,6)<? and p1.vip_status = 1 ";
		sql += param.getSearchParamSql();
		sql += " limit ?,?  ";
		List<User> list = new ArrayList<User>();
		try {
			list = this.getJdbcTemplate().query(sql,new Object[]{startDate,nowDate,start,pernum}, getBeanMapper(User.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 每日新注册用户人数统计
	 */
	@Override
	public int getDayRegister() {
		String sql = "select count(1) from dw_user where addtime>="
				+ DateUtils.getIntegralTime().getTime() / 1000 + " and addtime<="
				+ DateUtils.getLastIntegralTime().getTime() / 1000;
		logger.debug("SQL执行语句: " + sql);
		int count = this.getJdbcTemplate().queryForInt(sql);
		return count;
	}
	
	/**
	 * 一周前未认证的用户
	 */
	public List getWeekUnAttestation(String addTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		Date before = DateUtils.rollDay(new Date(), -7);
		String now = null;
		String weekAgo = null;
		// 如果用户没有选择时间，默认查询一周前未认证的用户（从今天起往前推7天）,否则就根据用户输入的时间段查询
		if (StringUtils.isBlank(addTime) && StringUtils.isBlank(endTime)) { 
			now = sdf.format(new Date()) + " 23:59:59";
			weekAgo = sdf.format(before) + " 00:00:00";
			addTime = DateUtils.getTime(weekAgo) + "";
			endTime = DateUtils.getTime(now) + "";
		} else {
			now = endTime + " 23:59:59";
			weekAgo = addTime + " 00:00:00";
			addTime = DateUtils.getTime(weekAgo) + "";
			endTime = DateUtils.getTime(now) + "";
		}
		String sql = "select p1.*,p.vip_status,p.vip_verify_time,p1.username as invite_username,p.* " +
				"from dw_user as p1 left join dw_user_cache as p on p.user_id=p1.user_id " +
				"where (p.vip_status = 0 or p1.real_status = 0 or p1.email_status = 0 or p1.phone_status = 0) " +
				"and (p1.addtime >= ? and p1.addtime <= ?)";
		logger.debug("SQL执行语句: "+sql);
//		return getJdbcTemplate().query(sql, new Object[] { addTime, endTime }, new VipUserMapper());
		return getJdbcTemplate().query(sql, new Object[] { addTime, endTime }, getBeanMapper(DetailUser.class));
	}


	/**
	 * 根据user id修改临时会员用户名
	 * @param user_id
	 * @param username 会员名
	 * @param type_id 会员类型，必须为999才可以修改，999为临时账户、
	 * 注：临时会员有一次修改用户名的机会，修改之后，将会员类型转为普通用户
	 */
	public void editUser(long user_id ,int type_id, String username){
		String sql = "update dw_user set username = ? , type_id = ? where user_id = ?";
		this.getJdbcTemplate().update(sql , username , type_id , user_id);
	}

	@Override
	public void deleteOnlineKfById(long id) {
		String sql = "delete from dw_user where user_id=?";
		this.getJdbcTemplate().update(sql,id);
	}

	@Override
	public void modifyOnlineKf(User user) {
		String sql = "update dw_user set username=?,qq=? where user_id=?";
		this.getJdbcTemplate().update(sql,user.getUsername(),user.getQq(),user.getUser_id());
	}
	@Override
	public List getOnlyPhone(String phone){
		String sql = "select * from dw_user where phone="+phone;
	logger.debug("SQL执行语句: " + sql);
	List list=new ArrayList();
	try {
		 list = this.getJdbcTemplate().query(sql, new Object[]{}, getBeanMapper(User.class));
	} catch (Exception e) {
		// TODO: handle exception
	}
	return list;
	}
	// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 start
	@Override
	public boolean isPhoneExist(User user) {
		StringBuffer sql = new StringBuffer("select count(*)  from  dw_user where phone =:phone and phone_status=1 and user_id <> :user_id");
		Map<String , Object> paramMap = new HashMap<String, Object>();
		paramMap.put("phone", user.getPhone());
		paramMap.put("user_id", user.getUser_id());
		int count = this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), paramMap);
		return count == 0;
	}
	// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 end

	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
	public int getUserCount(int phoneStatus){
		String sql = "select count(1) from dw_user where phone_status="+phoneStatus;
		return count(sql);
	}
	public List getUserList(int phoneStatus, int start, int pernum){
		String sql = "select user_id, username, realname, phone from dw_user a where not exists(select 1 from dw_user where phone=a.phone and user_id>a.user_id  AND phone_status = "+phoneStatus+") AND a.phone_status = "+phoneStatus+" ORDER BY user_id DESC limit "+start+","+pernum;
//		return getJdbcTemplate().query(sql, new UserSmsMapper());
		return getJdbcTemplate().query(sql,getBeanMapper(User.class));
	}
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 end
	
	// v1.6.6.2 RDPROJECT-235 zza 2013-10-17 start
	/**
	 * 
	 * @param invite_userid 邀请人Id
	 * @param addtime 注册时间
	 * @return int
	 */
	public int getInviteCount(String invite_userid, String addtime) {
		String sql = "select count(1) from dw_user where invite_userid = :invite_userid and `addtime` > :addtime";
		Map<String , Object> map = new HashMap<String, Object>();
		try {
			map.put("invite_userid", invite_userid);
			map.put("addtime", addtime);
			return getNamedParameterJdbcTemplate().queryForInt(sql, map);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void updateRulePromoteId(long rule_promote_id, long user_id) {
		String sql = "update dw_user set rule_promote_id = :rule_promote_id where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rule_promote_id", rule_promote_id);
		map.put("user_id", user_id);
		logger.debug("SQL:" + sql);
		logger.debug("更新user_id为:" + user_id + "的推广等级为" + rule_promote_id);
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}
	
	// v1.6.6.2 RDPROJECT-235 zza 2013-10-17 end
	
	//v1.6.6.1 RDPROJECT-301 liukun 2013-10-28 begin
	/**
	 * 查询某种类型的用户列表，只返回用户名和用户ID
	 * @param type 类型 all全部用户
	 * @return list
	 */
	@Override
	public List getUserList(String type){
		String sql = "select user_id,username from dw_user order by username asc";
		StringBuffer sb = new StringBuffer(sql);
		
		List<Map<String, Object>> list = null;
		try {
			list = this.getJdbcTemplate().queryForList(sb.toString());
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return list;
	}
	//v1.6.6.1 RDPROJECT-301 liukun 2013-10-28 begin

	// v1.6.7.1 RDPROJECT-289 sj 2013.11.8 start
	/**
	 * 查询重复的证件号码
	 * @param type card_id 证件号码
	 * @return int
	 */
	@Override
	public int getCountCard(String card_id) {
		Map<String,String> map = new HashMap<String,String>();
		String sql = " select count(*) from dw_user where real_status = 2 and card_id = :card_id ";
		map.put("card_id", card_id);
		return this.getNamedParameterJdbcTemplate().queryForInt(sql, map);
		//return this.getJdbcTemplate().queryForInt(sql, card_id);
		
	}
	// v1.6.7.1 RDPROJECT-289 sj 2013.11.8 end
	
	// v1.6.7.1 安全优化  sj 2013-11-13 start
	//v1.6.7.1 安全优化 sj 2013-11-18 start
	/**
	 * 修改密码
	 * 
	 * */
	//v1.6.7.1 安全优化 sj 2013-11-18 end
	@Override
	public void modifypassword(User user) {
		String sql = "update dw_user set password = :password where username = :username";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("password", user.getPassword());
		map.put("username", user.getUsername());
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}
	//v1.6.7.1 安全优化 sj 2013-11-18 start
	/**
	 * 查询修改时间
	 * 
	 * */
	//v1.6.7.1 安全优化 sj 2013-11-18 end
	@Override
	public String selectModifyTime(long user_id) {
		String sql = "select pwd_modify_time from dw_user_cache where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, String.class);
	}
	//v1.6.7.1 安全优化 sj 2013-11-18 start
	/**
	 * 新增用户插入到dw_user_cahce一条数据
	 * 
	 * */
	//v1.6.7.1 安全优化 sj 2013-11-18 end
	@Override
	public void insertRegisterUserCache(UserCache userCache) {
		String sql = "insert into  dw_user_cache(user_id,pwd_modify_time) "
				+ "values (?,?)";
		this.getJdbcTemplate().update(sql, userCache.getUser_id(), userCache.getPwd_modify_time());
	}
	//v1.6.7.1 安全优化 sj 2013-11-18 start
//	@Override
//	public void updateUserCacheTime(UserCache userCache) {
//		String sql = "update dw_user_cache set pwd_modify_time = :pwd_modify_time where user_id = :user_id";
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("pwd_modify_time", userCache.getPwd_modify_time());
//		map.put("user_id", userCache.getUser_id());
//		this.getNamedParameterJdbcTemplate().update(sql, map);
//	}
	//v1.6.7.1 安全优化 sj 2013-11-18 end
	// v1.6.7.1 安全优化  sj 2013-11-13 end
	
	//v1.6.7.1 安全优化 sj 2013-11-20 start
	@Override
	public void updateIsLock(long user_id, int islock) {
		String sql = "update dw_user set islock = :islock where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("islock", islock);
		map.put("user_id", user_id);
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}
	//v1.6.7.1 安全优化 sj 2013-11-20 end
	
	//v1.6.7.1 安全优化 sj 2013-11-27 start
	@Override
	public void updateLoginTime(long user_id) {
		String sql = "update dw_user set logintime = :logintime where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("logintime", NumberUtils.getLong(DateUtils.getNowTimeStr()));
		map.put("user_id", user_id);
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}
	//v1.6.7.1 安全优化 sj 2013-11-27 end

	//v1.6.7.2 安全优化 sj 2013-12-03 start
	@Override
	public String getAutoAenderAddTime(long user_id) {
		String sql = "select addtime from dw_borrow_tender where user_id = :user_id order by addtime desc";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		List<String> list = this.getNamedParameterJdbcTemplate().queryForList(sql, map, String.class);
		if(list.size() == 0){
			return "0";
		}else{
			return list.get(0);
		}
	}
	//v1.6.7.2 安全优化 sj 2013-12-03 end
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 start
	public String perferTotalMoney(String userId){
		if(!StringUtils.isEmpty(userId)){
			String sql="select sum(money) as money from dw_account_log where user_id="+Long.parseLong(userId)+" and type ='back_manage_fee'";
			SqlRowSet sr=this.jdbcTemplate.queryForRowSet(sql);
			if(sr.next()){
				return sr.getString("money");
			}
		}
		return "0";
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 end
	
}
