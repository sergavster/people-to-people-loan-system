package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.UserCacheDao;
import com.p2psys.domain.UserCache;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCacheModel;
import com.p2psys.model.VIPStatisticModel;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;

public class UserCacheDaoImpl extends BaseDaoImpl implements UserCacheDao {

	private static Logger logger = Logger.getLogger(UserCacheDaoImpl.class);

	/**
	 * 新增用户缓存
	 */
	public void addUserCache(UserCache uc) {
		String sql = "insert into dw_user_cache(user_id,kefu_userid,kefu_addtime,vip_status,vip_remark) "
				+ "values (?,?,?,?,?)";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, uc.getUser_id(),
				uc.getKefu_userid(), uc.getKefu_addtime(), uc.getVip_status(),
				uc.getVip_remark());
	}

	public void updateUserCache(UserCache uc) {
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		String sql = "update dw_user_cache set kefu_userid=?,vip_verify_time=?,vip_status=?,vip_verify_remark=?,kefu_username=?,kefu_addtime=?,type=?,vip_give_status=?,vip_end_time=?,vip_give_month=?,phone_verify_time=?,realname_verify_time=?,video_verify_time=?,scene_verify_time=? where user_id=?";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, uc.getKefu_userid(),
				uc.getVip_verify_time(), uc.getVip_status(),
				uc.getVip_verify_remark(), uc.getKefu_username(),
				uc.getKefu_addtime(), uc.getType(), uc.getVip_give_status(),
				uc.getVip_end_time(), uc.getVip_give_month(), 
				uc.getPhone_verify_time(),uc.getRealname_verify_time(),
				uc.getVideo_verify_time(),uc.getScene_verify_time(),
				uc.getUser_id());
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
	}

	/**
	 * 获取用户的信息，以及相应的积分
	 */
	public UserCacheModel getUserCacheByUserid(long userid) {
		String sql = "select p1.*,p3.username as kefu_name,p3.realname as kefu_realname, p4.pic as credit_pic,p2.value as credit_jifen ,p5.username as username "
				+ "from dw_user_cache as p1 "
				+ "left join dw_credit as p2 on p1.user_id=p2.user_id "
				+ "left join dw_user as p3 on p1.kefu_userid = p3.user_id "
				+ "left join dw_credit_rank as p4 on p2.value<=p4.point2 and p2.value>=p4.point1 "
				+ "left join dw_user as p5 on p1.user_id=p5.user_id "
				+ "where p1.user_id =?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + userid);
		UserCacheModel cache = null;
		try {
			cache = this.getJdbcTemplate().queryForObject(sql,
					//v1.6.7.2 RDPROJECT-502 wcw 2013-11-28 start
					new Object[] { userid }, getBeanMapper(UserCacheModel.class));
		         	//v1.6.7.2 RDPROJECT-502 wcw 2013-11-28 end
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return cache;
	}

	public UserCacheModel getUserCacheByUserid(long userid,
			long vip_give_status, long type) {
		String sql = "select p1.*,p3.username as kefu_name,p3.realname as kefu_realname, p4.pic as credit_pic,p2.value as credit_jifen ,p5.username as username "
				+ "from dw_user_cache as p1 "
				+ "left join dw_credit as p2 on p1.user_id=p2.user_id "
				+ "left join dw_user as p3 on p1.kefu_userid = p3.user_id "
				+ "left join dw_credit_rank as p4 on p2.value<=p4.point2 and p2.value>=p4.point1 "
				+ "left join dw_user as p5 on p1.user_id=p5.user_id "
				+ "where 1=1 ";
		UserCacheModel cache = null;
		if (userid != 0) {
			sql += " and p5.user_id=? ";
		}
		if (userid == 0) {
			sql += " and 0=? ";
		}
		if (vip_give_status != 0) {
			sql += " and p1.vip_give_status=? ";

		}
		if (vip_give_status == 0) {
			sql += " and 0=? ";

		}
		if (type != 0) {
			sql += " and p1.type=? ";
		}
		if (type == 0) {
			sql += " and 0=? ";
		}
		logger.debug("SQL:" + sql);
		logger.debug("userid:" + userid);
		logger.debug("vip_give_status:" + vip_give_status);
		try {
			cache = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { userid, vip_give_status, type },
					getBeanMapper(UserCacheModel.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}

		return cache;
	}

	public List getUserVipinfo(long page, int Max, int status, SearchParam p) {
		String sql = "SELECT p.vip_give_month,p.type,p.vip_end_time,CAST(((p.vip_end_time-p.vip_verify_time)/3600/24) as SIGNED) as valid_vip_time,CAST(((p.vip_end_time-"+DateUtils.getNowTime()+")/3600/24) as SIGNED) as last_vip_time,p.vip_give_status,p.user_id,p2.username,p2.type_id,kf.username as kefu_name,p.kefu_addtime,p.vip_status,p.account_waitvip,p.vip_verify_time from dw_user_cache as p LEFT JOIN dw_user as kf on p.kefu_userid=kf.user_id  LEFT JOIN dw_user as p2 on p.user_id=p2.user_id  where 1=1 ";
		if (status!=0) {
			sql += " and vip_status=? ";
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc ";
			sql += " LIMIT ?,? ";
			logger.debug(sql);
			//System.out.println("if=============================" + sql);
			return getJdbcTemplate()
					.query(sql, new Object[] { status, page, Max },
							getBeanMapper(UserCacheModel.class));
		} else {
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc ";
			sql += " LIMIT ?,?";
			//System.out.println("else=============================" + sql);
			return getJdbcTemplate().query(sql, new Object[] { page, Max },
					getBeanMapper(UserCacheModel.class));

		}

	}

	public List getUserVipinfo(long page, int Max, int status, int type,
			SearchParam p) {
		//v1.6.7.1 RDPROJECT-384 wcw 2013-11-05 start
		String sql = "SELECT p.vip_give_month,p.type,p.vip_end_time,CAST(((p.vip_end_time-p.vip_verify_time)/3600/24) as SIGNED) as valid_vip_time,CAST(((p.vip_end_time-"+DateUtils.getNowTime()+")/3600/24) as SIGNED) as last_vip_time,p.vip_give_status,p.user_id,p2.username,p2.type_id,kf.username as kefu_name,p.kefu_addtime,p.vip_status,p.account_waitvip,p.vip_verify_time from dw_user_cache as p LEFT JOIN dw_user as kf on p.kefu_userid=kf.user_id  LEFT JOIN dw_user as p2 on p.user_id=p2.user_id where 1=1 ";
		List list=new ArrayList();
		if (status != 0) {
			sql += "and   p.vip_give_status=? ";
			if (type != -1) {
				sql += "and  p.type=? ";
			}
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc ";
			sql += " LIMIT ?,? ";
			logger.debug(sql);
			//System.out.println("if=============================" + sql);
			return getJdbcTemplate().query(sql,
					new Object[] { status, type, page, Max },
					getBeanMapper(UserCacheModel.class));
		} else {
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc ";
			sql += " LIMIT ?,?";
			//System.out.println("else=============================" + sql);
			try {
				list= getJdbcTemplate().query(sql,
						new Object[] {page, Max },
						getBeanMapper(UserCacheModel.class));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list;
		}
		//v1.6.7.1 RDPROJECT-384 wcw 2013-11-05 end

	}
	//v1.6.7.1 RDPROJECT-384 wcw 2013-11-08 start
	@Override
	public List getUserVipinfo(int status, int type,
			SearchParam p,int ruleStatus) {	
		String sql = "SELECT p.vip_give_month,p.type,p.vip_end_time,CAST(((p.vip_end_time-p.vip_verify_time)/3600/24) as SIGNED) as valid_vip_time,CAST(((p.vip_end_time-"+DateUtils.getNowTime()+")/3600/24) as SIGNED) as last_vip_time,p.vip_give_status,p.user_id,p2.username,p2.type_id,kf.username as kefu_name,p.kefu_addtime,p.vip_status,p.account_waitvip,p.vip_verify_time from dw_user_cache as p LEFT JOIN dw_user as kf on p.kefu_userid=kf.user_id  LEFT JOIN dw_user as p2 on p.user_id=p2.user_id where 1=1 ";
		List list=new ArrayList();
		if (status != 0) {
			if(ruleStatus==1){
			    sql += "and   p.vip_give_status=? ";
			}else{
				sql += "and   p.vip_status=? ";
			}
			if (type != -1) {
				sql += "and  p.type=? ";
			}
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc ";
			logger.debug(sql);
			//System.out.println("if=============================" + sql);
			try {
				list= getJdbcTemplate().query(sql,
						new Object[] { status, type},getBeanMapper(UserCacheModel.class));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list;
		} else {
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc ";
			//System.out.println("else=============================" + sql);
			try {
				list= getJdbcTemplate().query(sql,
						new Object[] {},getBeanMapper(UserCacheModel.class));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list;
		}
	}
	@Override
	public List getUserVipinfoList(int status,
			SearchParam p) {	
		String sql = "SELECT p.vip_give_month,p.type,p.vip_end_time,CAST(((p.vip_end_time-p.vip_verify_time)/3600/24) as SIGNED) as valid_vip_time,CAST(((p.vip_end_time-"+DateUtils.getNowTime()+")/3600/24) as SIGNED) as last_vip_time,p.vip_give_status,p.user_id,p2.username,p2.type_id,kf.username as kefu_name,p.kefu_addtime,p.vip_status,p.account_waitvip,p.vip_verify_time from dw_user_cache as p LEFT JOIN dw_user as kf on p.kefu_userid=kf.user_id  LEFT JOIN dw_user as p2 on p.user_id=p2.user_id where 1=1 ";
		List list=new ArrayList();
		if (status != 0) {
			sql += "and   p.vip_status=? ";
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc ";
			logger.debug(sql);
			//System.out.println("if=============================" + sql);
			try {
				list= getJdbcTemplate().query(sql,
						new Object[] { status},getBeanMapper(UserCacheModel.class));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list;
		} else {
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc ";
			//System.out.println("else=============================" + sql);
			try {
				list= getJdbcTemplate().query(sql,
						new Object[] {},getBeanMapper(UserCacheModel.class));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list;
		}
	}
	//v1.6.7.1 RDPROJECT-384 wcw 2013-11-08 end
	@Override
	public int getUserVipinfoCount(int status, int type, SearchParam p) {
		String sql = "SELECT count(*) from dw_user_cache as p LEFT JOIN dw_user as kf on p.kefu_userid=kf.user_id  LEFT JOIN dw_user as p2 on p.user_id=p2.user_id  where 1=1 ";
		if (type != -1) {
			sql += "and  p.type=? ";
		}
		if (status != -1) {
			sql += "and  p.vip_give_status=? ";
		}
		sql += p.getSearchParamSql();
		sql += "   order by p2.user_id desc";
		logger.debug(sql);

		return getJdbcTemplate()
				.queryForInt(sql, new Object[] { type, status });
	}

	@Override
	public int getUserVipinfo(int status, SearchParam p) {
		//v1.6.7.1 RDPROJECT-384 wcw 2013-11-19 start
		String sql = "SELECT count(*) from dw_user_cache as p LEFT JOIN dw_user as kf on p.kefu_userid=kf.user_id  LEFT JOIN dw_user as p2 on p.user_id=p2.user_id  where 1=1 ";
		if (status!=0) {
			sql += " and vip_status=? ";
			sql += p.getSearchParamSql();
			sql += "   order by p2.user_id desc";
			logger.debug(sql);
			return getJdbcTemplate().queryForInt(sql, new Object[] { status });
		}
		sql += p.getSearchParamSql();
		sql += "   order by p2.user_id desc";
		logger.debug(sql);
		return getJdbcTemplate().queryForInt(sql, new Object[] {});
		//v1.6.7.1 RDPROJECT-384 wcw 2013-11-19 end
	}

	@Override
	public UserCacheModel validUserVip(long user_id) {
		String sql = "update dw_user_cache set vip_verify_time=0,vip_status=0 where user_id=?";
		this.getJdbcTemplate().update(sql, user_id);
		return getUserCacheByUserid(user_id);
	}

	@Override
	public int getVipStatistic(SearchParam param) {
		String selectSql = "select count(1) from dw_user p1 LEFT JOIN dw_user_cache p4 ON p4.user_id=p1.user_id "
				+ "where 1=1 ";
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		String querySql = sb.append(searchSql).toString();
		logger.debug("getVipStatistic:" + querySql);
		int count = 0;
		count = count(querySql);
		return count;
	}

	@Override
	public List getVipStatisticList(int start, int pernum, SearchParam param) {
		String selectSql = "select p4.*,p1.username,p1.realname,p1.addtime as registertime,p3.collection,p4.kefu_username from dw_user p1 LEFT JOIN dw_user_cache p4 ON p4.user_id=p1.user_id LEFT JOIN dw_account p3 ON p3.user_id=p1.user_id "
				+ "where 1=1 ";
		String orderSql = "order by p1.addtime desc,p1.user_id desc ";
		String limitSql = "limit ?,?";
		String groupbySql = " GROUP BY p1.user_id ";
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		String querySql = sb.append(searchSql).append(groupbySql)
				.append(orderSql).append(limitSql).toString();
		logger.debug("getVipStatisticList:" + querySql);
		List list = getJdbcTemplate().query(querySql,
				new Object[] { start, pernum },
				new RowMapper<VIPStatisticModel>() {
					@Override
					public VIPStatisticModel mapRow(ResultSet rs, int num)
							throws SQLException {
						VIPStatisticModel vipsm = new VIPStatisticModel();
						vipsm.setUsername(rs.getString("username"));
						vipsm.setRealname(rs.getString("realname"));
						vipsm.setRegistertime(rs.getString("registertime"));
						vipsm.setCollection(rs.getString("collection"));
						vipsm.setKefu_username(rs.getString("kefu_username"));
						vipsm.setVip_verify_time(rs
								.getString("vip_verify_time"));
						return vipsm;
					}
				});
		return list;
	}

	@Override
	public List getVipStatisticList(SearchParam param) {
		String selectSql = "select p4.*,p1.user_id,p1.username,p1.realname,p1.addtime as registertime,p3.collection,p4.kefu_username from dw_user p1 LEFT JOIN dw_user_cache p4 ON p4.user_id=p1.user_id LEFT JOIN dw_account p3 ON p3.user_id=p1.user_id "
				+ "where 1=1 ";
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		String orderSql = "order by p1.addtime desc,p1.user_id desc ";
		String groupbySql = " GROUP BY p1.user_id ";
		String querySql = sb.append(searchSql).append(groupbySql)
				.append(orderSql).toString();
		logger.debug("SQL:" + querySql);
		List list = new ArrayList();
		list = getJdbcTemplate().query(querySql,
				new RowMapper<VIPStatisticModel>() {
					@Override
					public VIPStatisticModel mapRow(ResultSet rs, int num)
							throws SQLException {
						VIPStatisticModel vipsm = new VIPStatisticModel();
						vipsm.setUser_id(rs.getLong("user_id"));
						vipsm.setUsername(rs.getString("username"));
						vipsm.setRealname(rs.getString("realname"));
						vipsm.setRegistertime(rs.getString("registertime"));
						vipsm.setCollection(rs.getString("collection"));
						vipsm.setKefu_username(rs.getString("kefu_username"));
						vipsm.setVip_verify_time(rs
								.getString("vip_verify_time"));
						return vipsm;
					}
				});
		return list;
	}

	@Override
	public int getLoginFailTimes(long user_id) {
		String sql = "select login_fail_times from dw_user_cache where user_id=?";
		logger.debug("SQL:" + sql);
		return getJdbcTemplate().queryForInt(sql, new Object[] { user_id });
	}

	@Override
	public void cleanLoginFailTimes(long user_id) {
		String sql = "update dw_user_cache set login_fail_times=0 where user_id=?";
		logger.debug("SQL:" + sql);
		getJdbcTemplate().update(sql, new Object[] { user_id });

	}

	@Override
	public void updateLoginFailTimes(long user_id) {
		String sql = "update dw_user_cache set login_fail_times=login_fail_times+1 where user_id=?";
		logger.debug("SQL:" + sql);
		getJdbcTemplate().update(sql, new Object[] { user_id });

	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
	@Override
	public Map getSmstypeConfig(long user_id) {
		String sql = "select ur.phone, ur.phone_status, uc.smspay_endtime, uc.smstype_config from dw_user_cache uc, dw_user ur where uc.user_id=? and uc.user_id = ur.user_id ";

		logger.debug("SQL:" + sql);
		return getJdbcTemplate().queryForMap(sql, new Object[] { user_id });
	}
	/*
	@Override
	public void updateSmstypeConfig(long user_id, String smstypeConfig) {
		String sql = "update dw_user_cache set smstype_config=? where user_id=?";
		logger.debug("SQL:" + sql);
		getJdbcTemplate().update(sql, new Object[] { smstypeConfig, user_id });

	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 end

	@Override
	public void updateSmspayEndtime(long user_id, long smspayEndtime) {
		String sql = "update dw_user_cache set smspay_endtime=? where user_id=?";
		logger.debug("SQL:" + sql);
		getJdbcTemplate().update(sql, new Object[] { smspayEndtime, user_id });

	}

	//V1.6.6.1 RDPROJECT-245 liukun 2013-10-09 start
	/*@Override
	public boolean isSmssend(long userid, Smstype smstype) {
		boolean smssend = false;
		// 后台配置为发送
		// 接收手机号已经认证，除非发送的是手机激活认证码
		// 系统短信或者(用户短信且用户没有拒绝接收，且已经付费)
		if (smstype.getSend() == Constant.SMS_SEND) {

			Map userSmstypeConfig = getSmstypeConfig(userid);
			String phone_status = StringUtils.isNull(userSmstypeConfig
					.get("phone_status"));
			if (smstype.getType() == Constant.SYSTEM_SMS) {
				if (phone_status.equals("1")) {
					smssend = true;
				} else {
					if (smstype.getNid().equalsIgnoreCase("phone_code")) {
						smssend = true;
					}
				}
			} else {
				long smspayEndtime = 0;
				try {
					smspayEndtime = Long.parseLong(userSmstypeConfig.get(
							"smspay_endtime").toString());
				} catch (Exception e1) {
				}
				String smstypeConfJson = StringUtils.isNull(userSmstypeConfig
						.get("smstype_config"));
				byte smsReceive = Constant.SMS_SEND;
				try {
					JSONObject jsonObject = new JSONObject(smstypeConfJson);
					smsReceive = Byte.parseByte(String.valueOf(jsonObject
							.getInt(smstype.getNid())));
				} catch (Exception e) {
				}
				// 用户短信发送条件：用户短信&&后台配置为发送&&用户配置为接收&&用户支付了短信费用
				if (phone_status.equals("1")) {
					if (smsReceive == Constant.SMS_SEND
							&& smspayEndtime > DateUtils.getTime(new Date())) {
						smssend = true;
					}
				} else {
					if (smstype.getNid().equalsIgnoreCase("phone_code")) {
						if (smsReceive == Constant.SMS_SEND
								&& smspayEndtime > DateUtils
										.getTime(new Date())) {
							smssend = true;
						}
					}
				}
			}
		}
		return smssend;
	}*/
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
	/*public boolean isSmssend(long userid, Smstype smstype) {
		boolean smssend = false;
		// 后台配置为发送
		// 手机激活认证码肯定会发送
		// (系统短信且用户没有拒绝接收）
		// (用户短信且用户没有拒绝接收，且已经付费)
		
		// 后台配置为发送
		if (smstype.getSend() == Constant.SMS_SEND) {

			// 手机激活验证码肯定发送
			if (smstype.getNid().equalsIgnoreCase("phone_code")) {
				smssend = true;
				return smssend;
			}

			// 用户是否接收
			Map userSmstypeConfig = getSmstypeConfig(userid);
			String smstypeConfJson = StringUtils.isNull(userSmstypeConfig
					.get("smstype_config"));
			byte smsReceive = Constant.SMS_SEND;
			try {
				JSONObject jsonObject = new JSONObject(smstypeConfJson);
				smsReceive = Byte.parseByte(String.valueOf(jsonObject
						.getInt(smstype.getNid())));
			} catch (Exception e) {
			}

			if (smsReceive == Constant.SMS_SEND) {
				// 系统短信只要用户不拒绝接收，不用检测是否付费都发送
				if (smstype.getType() == Constant.SYSTEM_SMS) {
					smssend = true;
				}
				else {
					long smspayEndtime = 0;
					try {
						smspayEndtime = Long.parseLong(userSmstypeConfig.get(
								"smspay_endtime").toString());
					} catch (Exception e1) {
					}
					// 如果是用户短信，需要付费才能接收
					if (smspayEndtime > DateUtils.getTime(new Date())) {
						smssend = true;
					}
				}
			}

		}
			
		return smssend;
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 end
	//V1.6.6.1 RDPROJECT-245 liukun 2013-10-09 end

	@Override
	public List<UserCacheModel> getExpireUser() {
		String sql = "select * from dw_user_cache where vip_status = 1 "
				+ "and vip_end_time < " + DateUtils.getNowTimeStr();
		logger.debug("SQL:" + sql);
		List<UserCacheModel> list = new ArrayList<UserCacheModel>();
		try {
			list = this.getJdbcTemplate().query(sql,
					getBeanMapper(UserCache.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return list;
	}

	//v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 begin
	@Override
	public int isCashForbid(long userid) {
		String sql = "select uc.cash_forbid from dw_user_cache uc where uc.user_id=? ";

		logger.debug("SQL:" + sql);
		return getJdbcTemplate().queryForInt(sql, new Object[] { userid });

	}
	

	@Override
	public boolean upateUserCashForbid(long userid, int cashForbid) {
		String sql = "update dw_user_cache set cash_forbid=? where user_id=?";
		logger.debug("SQL:" + sql);
		getJdbcTemplate().update(sql, new Object[] { cashForbid, userid });
		return true;
	}

	@Override
	public String getStatusDesc(long userid) {
		String sql = "select uc.status_desc from dw_user_cache uc where uc.user_id=? ";

		logger.debug("SQL:" + sql);
		Map resultMap =  getJdbcTemplate().queryForMap(sql, new Object[] { userid });
		String statusDesc = StringUtils.isNull(resultMap
				.get("status_desc"));
		return statusDesc;
	}

	@Override
	public boolean upateUserStatusDesc(long userid, String statusDesc) {
		String sql = "update dw_user_cache set status_desc=? where user_id=?";
		logger.debug("SQL:" + sql);
		getJdbcTemplate().update(sql, new Object[] { statusDesc, userid });
		return true;
	}
	
	
	//v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 end
	//v1.6.7.1 安全优化 sj 2013-11-20 start
	@Override
	public String getLockTime(long user_id) {
		String sql = "select lock_time from dw_user_cache where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, String.class);
	}
	
	@Override
	public void updateLockTime(long user_id,String lock_time) {
		String sql = "update dw_user_cache set lock_time = :lock_time where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lock_time", lock_time);
		map.put("user_id", user_id);
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}

	@Override
	public void updateFailTimes(long user_id) {
		String sql = "update dw_user_cache set login_fail_times = 1 where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}
	//v1.6.7.1 安全优化 sj 2013-11-20 end

	@Override
	public int getUserVipStatus(long user_id) {
		String sql = "SELECT vip_status FROM dw_user_cache WHERE user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		return this.getNamedParameterJdbcTemplate().queryForInt(sql, map);
	}
	
	
}
