package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.dao.RewardStatisticsDao;
import com.p2psys.domain.RewardStatistics;
import com.p2psys.domain.User;
import com.p2psys.model.RewardStatisticsModel;
import com.p2psys.model.RewardStatisticsSumModel;
import com.p2psys.model.SearchParam;

/**
 * 奖励统计Dao实现类
 */
public class RewardStatisticsDaoImpl extends BaseDaoImpl implements RewardStatisticsDao {

	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(RewardStatisticsDaoImpl.class);

	// v1.6.6.2 RDPROJECT-287 zza 2013-10-23 start
	@Override
	public void addRewardStatistics(RewardStatistics rewardStatistics) {
		final String sql = "insert into dw_reward_statistics(type, reward_user_id, passive_user_id, " +
				"receive_time, receive_yestime, receive_account, receive_yesaccount, receive_status," +
				"addtime, endtime, rule_id, back_type, type_fk_id, is_show, tender_count) " +
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		logger.debug("SQL: " + sql);
		this.getJdbcTemplate().update(sql, rewardStatistics.getType(), rewardStatistics.getReward_user_id(),
				rewardStatistics.getPassive_user_id(), rewardStatistics.getReceive_time(), 
				rewardStatistics.getReceive_yestime(), rewardStatistics.getReceive_account(), 
				rewardStatistics.getReceive_yesaccount(), rewardStatistics.getReceive_status(),
				rewardStatistics.getAddtime(), rewardStatistics.getEndtime(), 
				rewardStatistics.getRule_id(), rewardStatistics.getBack_type(), 
				rewardStatistics.getType_fk_id(), rewardStatistics.getIs_show(), rewardStatistics.getTender_count());
	}
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-23 end
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateReward(RewardStatistics r) {
		// v1.6.6.2 RDPROJECT-287 zza 2013-10-23 start
		String sql = "update dw_reward_statistics set receive_account=?, receive_yestime=?, " +
				"receive_yesaccount=?,receive_status=?, is_show=?, tender_count=? where id=?";
		logger.debug("SQL: " + sql);
		this.getJdbcTemplate().update(sql, r.getReceive_account(), r.getReceive_yestime(), 
				r.getReceive_yesaccount(), r.getReceive_status(), r.getIs_show(), r.getTender_count(), r.getId());
		// v1.6.6.2 RDPROJECT-287 zza 2013-10-23 end
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateAccount(double account, long id) {
		String sql = "update dw_reward_statistics set receive_account=? where id=?";
		logger.debug("SQL: " + sql);
		this.getJdbcTemplate().update(sql, account, id);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RewardStatistics> getRewardStatistics(SearchParam param) {
		String sql = "select p1.*, p2.username, us.username as passive_username from dw_reward_statistics as p1 " +
				"left join dw_user as p2 on p2.user_id = p1.reward_user_id " +
				"left join dw_user as us on us.user_id = p1.passive_user_id where p1.is_show = 1 ";
		StringBuffer sb = new StringBuffer(sql);
		String searchSql = param.getSearchParamSql();
		String selectSql = sb.append(searchSql).toString();
		logger.debug(" SQL: " + selectSql);
		List<RewardStatistics> list = new ArrayList<RewardStatistics>();
		list = getJdbcTemplate().query(selectSql, getBeanMapper(RewardStatisticsModel.class));
		return list;
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RewardStatistics> getRewardList() {
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-06 start
		String sql = "select p1.*, p2.username, us.username as passive_username from dw_reward_statistics as p1 " +
				"left join dw_user as p2 on p2.user_id = p1.reward_user_id " +
				"left join dw_user as us on us.user_id = p1.passive_user_id";
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-06 end
		logger.debug(" SQL: " + sql);
		List<RewardStatistics> list = new ArrayList<RewardStatistics>();
		list = getJdbcTemplate().query(sql, getBeanMapper(RewardStatisticsModel.class));
		return list;
		
	}
	
	public String orderSql = " order by p1.addtime desc limit ?, ?;";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RewardStatisticsModel> getRewardStatisticsList(SearchParam param, int start, int end) {
		String sql = "select p1.*, p2.username, us.username as passive_username from dw_reward_statistics as p1 " +
				"left join dw_user as p2 on p2.user_id = p1.reward_user_id " +
				"left join dw_user as us on us.user_id = p1.passive_user_id where p1.is_show = 1 ";
		StringBuffer sb = new StringBuffer(sql);
		String searchSql = param.getSearchParamSql();
		sb.append(searchSql).append(orderSql);
		String selectSql = sb.toString();
		logger.debug("SQL:" + selectSql);
		List<RewardStatisticsModel> list = new ArrayList<RewardStatisticsModel>();
		list = this.getJdbcTemplate().query(selectSql, new Object[]{start, end},  getBeanMapper(RewardStatisticsModel.class));
		return list;
	}
	
	/*@Override
	public List<RewardStatisticsModel> getUnShowList() {
		String sql = "select p1.*, p2.username, us.username as passive_username from dw_reward_statistics as p1 " +
				"left join dw_user as p2 on p2.user_id = p1.reward_user_id " +
				"left join dw_user as us on us.user_id = p1.passive_user_id where p1.is_show = 0;";
		logger.debug("SQL:" + sql);
		List<RewardStatisticsModel> list = new ArrayList<RewardStatisticsModel>();
		list = this.getJdbcTemplate().query(sql, getBeanMapper(RewardStatisticsModel.class));
		return list;
	}*/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int getCount(SearchParam param) {
		int total = 0;
		String sql = "select count(*) from dw_reward_statistics as p1 " +
				"left join dw_user as p2 on p2.user_id = p1.reward_user_id " +
				"left join dw_user as us on us.user_id = p1.passive_user_id where p1.is_show = 1 ";
		StringBuffer sb = new StringBuffer(sql);
		String searchSql = param.getSearchParamSql();
		sb.append(searchSql);
		String selectSql = sb.toString();
		logger.debug("SQL:" + selectSql);
		try {
			total = getJdbcTemplate().queryForInt(selectSql);
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RewardStatistics getRewardStatisticsById(long id) {
		String sql = "select rs.*, u.username, us.username as passive_username from dw_reward_statistics as rs " +
				"left join dw_user as u on u.user_id = rs.reward_user_id " +
				"left join dw_user as us on us.user_id = rs.passive_user_id where rs.id = ?";
		logger.debug("SQL: " + sql);
		RewardStatistics r = null;
		try {
			r = this.getJdbcTemplate().queryForObject(sql, new Object[]{id}, 
					getBeanMapper(RewardStatistics.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return r;
	}
	
	/*@Override
	public RewardStatistics getRewardByRewardUserId(long userId) {
		String sql = "select rs.*, u.username, us.username as passive_username from dw_reward_statistics as rs " +
				"left join dw_user as u on u.user_id = rs.reward_user_id " +
				"left join dw_user as us on us.user_id = rs.passive_user_id where rs.reward_user_id = ?";
		logger.debug("SQL: " + sql);
		RewardStatistics r = null;
		try {
			r = this.getJdbcTemplate().queryForObject(sql, new Object[]{userId}, getBeanMapper(RewardStatistics.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return r;
	}*/
	
	// v1.6.5.3 第一次投标奖励代码移到AutoBorrowServiceImpl.java里  zza 2013-09-18 start
	
	// v1.6.5.5 RDPROJECT-254 zza 2013-09-29 start
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RewardStatistics> getRewardUserByPassive(long userId, long ruleId, String addtime) {
		String sql = "select rs.*, u.username, us.username as passive_username from dw_reward_statistics as rs " +
				"left join dw_user as u on u.user_id = rs.reward_user_id " +
				"left join dw_user as us on us.user_id = rs.passive_user_id " + 
				"where rs.passive_user_id = :passive_user_id and rs.rule_id = :rule_id and rs.addtime > :addtime";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("passive_user_id", userId);
		map.put("rule_id", ruleId);
		map.put("addtime", addtime);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(RewardStatistics.class));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RewardStatistics> getPassiveByReward(long userId, long ruleId, String addtime) {
		String sql = "select *, u.username, us.username as passive_username " +
				"from dw_reward_statistics as rs " + 
				"left join dw_user as u on u.user_id = rs.reward_user_id " + 
				"left join dw_user as us on us.user_id = rs.passive_user_id " + 
				"where rs.reward_user_id = :reward_user_id and rs.rule_id = :rule_id and rs.addtime > :addtime";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reward_user_id", userId);
		map.put("rule_id", ruleId);
		map.put("addtime", addtime);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(RewardStatistics.class));
	}
	
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-11 start
	/*@Override
	public List<RewardStatistics> getReceiveRewardUser(long userId, String type, String addtime, int verify) {
		String sql = "select rs.*, u.username, us.username as passive_username from dw_reward_statistics as rs "
				+ "left join dw_user as u on u.user_id = rs.reward_user_id "
				+ "left join dw_user as us on us.user_id = rs.passive_user_id "
				+ "where rs.passive_user_id = ? and rs.type = ? and receive_status = 2";
		StringBuffer sb = new StringBuffer(sql);
		if (verify == 1) {
			sb.append(" and rs.addtime > ?");
		} else {
			sb.append(" and rs.addtime = ?");
		}
		logger.debug("SQL: " + sb.toString());
		List list = new ArrayList();
		try {
			list = this.getJdbcTemplate().query(sb.toString(), new Object[]{userId, type, addtime},  getBeanMapper(RewardStatistics.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return list;
	}*/
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-11 end
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public User getUserByInviteId(long inviteId, String addTime) {
		String sql = "select * from dw_user where invite_userid = ? and addtime = ?";
		logger.debug("SQL: " + sql);
		User user = null;
		try {
			user = this.getJdbcTemplate().queryForObject(sql, new Object[]{inviteId, addTime}, getBeanMapper(User.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return user;
	}
	
	/*@Override
	public RewardStatistics getByTypePkId(long typePkId) {
		String sql = "select rs.*, u.username, us.username as passive_username from dw_reward_statistics as rs " +
				"left join dw_user as u on u.user_id = rs.reward_user_id " +
				"left join dw_user as us on us.user_id = rs.passive_user_id where rs.type_pk_id = ?";
		logger.debug("SQL: " + sql);
		RewardStatistics r = null;
		try {
			r = this.getJdbcTemplate().queryForObject(sql, new Object[]{typePkId}, getBeanMapper(RewardStatistics.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return r;
	}*/
	
	// v1.6.6.2 RDPROJECT-338 zza 2013-10-16 start
	/**
	 * 根据被邀请人获取信息
	 * @param rewardUserId rewardUserId
	 * @param passiveUserId passiveUserId
	 * @param type type
	 * @return RewardStatisticsModel
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RewardStatisticsModel> getRewardByPassiveId(long rewardUserId, long passiveUserId, byte type) {
		String sql = "select rs.*,r.nid as nid from dw_reward_statistics as rs " +
				"left join dw_rule as r on r.id = rs.rule_id " +
				"where rs.reward_user_id = :reward_user_id and " +
				"rs.passive_user_id = :passive_user_id and rs.type = :type";
		logger.debug("SQL: " + sql);
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("reward_user_id", rewardUserId);
		map.put("passive_user_id", passiveUserId);
		map.put("type", type);
		return this.getNamedParameterJdbcTemplate().query(sql, map, 
				getBeanMapper(RewardStatisticsModel.class));
	}
	// v1.6.6.2 RDPROJECT-338 zza 2013-10-16 end
	
	/*@Override
	public RewardStatistics getRewardByTypeFkId(byte type, long typeFkId) {
		String sql = "select * from dw_reward_statistics where type = ?, type_fk_id = ?";
		logger.debug("SQL: " + sql);
		RewardStatistics r = null;
		try {
			r = this.getJdbcTemplate().queryForObject(sql, new Object[]{type, typeFkId}, getBeanMapper(RewardStatistics.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return r;
	}*/
	
	/**
	 * 查询user的邀请好友奖励
	 * @param userId
	 * @param type 奖励统计类型
	 * @param status 信息状态
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public double getInviteRewardSum(long userId, byte type, byte status , long startTime, long endTime){
		
		if(userId <= 0){
			logger.error("user_id is null.");
			return 0;
		}
		
		StringBuffer sql = new StringBuffer("select sum(drs.receive_yesaccount) as num from dw_reward_statistics drs where drs.reward_user_id = ? ");
		// 查询数据数据封装 
		List<Object> list = new ArrayList<Object>();
		list.add(userId);
		
		//待还状态
		if(status >= 0){
			list.add(status);
			sql.append(" and drs.back_type = ? ");
		}
		
		//奖励成功开始时间
		if(startTime > 0){
			list.add(startTime);
			sql.append(" and  drs.receive_yestime >= ? ");
		}
		
		//奖励成功结束时间
		if(endTime > 0){
			list.add(endTime);
			sql.append(" and drs.receive_yestime <= ? ");
		}
		
		if(type > 0){
			list.add(type);
			sql.append(" and drs.type = ? ");
		}
		
		// 新建obj数组，用于JDBC查询，数组程度为list size
		Object[] obj = new Object[list.size()]; 
		// 遍历list，将值存入obj数组中
		for(int i = 0 ; i < list.size() ; i++){
			obj[i] = list.get(i);
		}
		
		double sum=0.0;
		SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql.toString(),obj);
		if(rs.next()){
			sum=rs.getDouble("num");
		}
		return sum;
	}
	
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-22 start
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RewardStatistics> getRewardByPassiveId(long passiveUserId, Byte receiveStatus, long rule_id) {
		String sql = "select * from dw_reward_statistics where passive_user_id = :passive_user_id "
				+ "and receive_status = :receive_status and rule_id = :rule_id";
		logger.debug("SQL: " + sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("passive_user_id", passiveUserId);
		map.put("receive_status", receiveStatus);
		map.put("rule_id", rule_id);
		return this.getNamedParameterJdbcTemplate().query(sql, map,  getBeanMapper(RewardStatistics.class));
	}
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-22 end
	
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-24 start
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RewardStatisticsSumModel getSumAccount(long userId, String init) {
		String selectSql = "select u.username,sum(bt.account) as account from dw_borrow_tender as bt " +
				"left join dw_borrow as b on bt.borrow_id = b.id " +
				"left join dw_user as u on bt.user_id = u.user_id " +
				"where bt.user_id = ? and ";
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(init).append("group by u.username;");
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		RewardStatisticsSumModel rewardStatistics = new RewardStatisticsSumModel();
		try {
			rewardStatistics = getJdbcTemplate().queryForObject(sql, 
					new Object[] { userId }, getBeanMapper(RewardStatisticsSumModel.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return rewardStatistics;
	}
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-24 end

}
