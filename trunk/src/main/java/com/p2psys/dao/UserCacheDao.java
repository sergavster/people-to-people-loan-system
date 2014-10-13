package com.p2psys.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.domain.UserCache;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCacheModel;

public interface UserCacheDao extends BaseDao {

	public void addUserCache(UserCache cache);

	public void updateUserCache(UserCache cache);

	public UserCacheModel getUserCacheByUserid(long userid);
	public UserCacheModel getUserCacheByUserid(long userid,long vip_give_status,long type);
	public UserCacheModel validUserVip(long userid);
	/**
	 * 根据页数,返回条数Vip状态换回List
	 * 
	 * @param page
	 * @param Max
	 * @param status
	 * @return
	 */
	public List getUserVipinfo(long page, int Max, int status,SearchParam p);
	public List getUserVipinfo(long page, int Max, int status,int type, SearchParam p) ;
	public int getUserVipinfoCount(int status, int type,SearchParam p) ;
	/**
	 * 根据VIp状态返回总条数
	 */
	public int getUserVipinfo(int status,SearchParam p);
	
	public int getVipStatistic(SearchParam param);
	
	public List getVipStatisticList(int start, int pernum,SearchParam param);
	
	public List getVipStatisticList(SearchParam param);
	
	public int getLoginFailTimes(long userid);
	
	public void cleanLoginFailTimes(long userid);
	
	public void updateLoginFailTimes(long userid);
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
	public Map getSmstypeConfig(long userid);
	/*
	public void updateSmstypeConfig(long userid, String smstypeConfig);*/
	
	public void updateSmspayEndtime(long userid, long smspayEndtime);
	
	/*
	public boolean isSmssend(long userid, Smstype smstype);
	*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 end
	/**
	 * 获取vip到期的用户
	 * @return list
	 */
	List<UserCacheModel> getExpireUser();
	
	//v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 begin
	/**
	 * 获取用户的提现状态（是否禁止提现）
	 
	 * @since V1.6.6.2
	 * @param long userid
	 * @return boolean
	 */
	public int isCashForbid(long userid);
	/**
	 * 更新用户的状态说明
	 * @param userid 用户id
	 * @param cashForbid 用户的提现状态
	 * @return 更新成功与否
	 */
	public boolean upateUserCashForbid(long userid, int cashForbid);
	/**
	 * 获取用户的状态说明
	 
	 * @since V1.6.6.2
	 * @param long userid
	 * @return String
	 */
	public String getStatusDesc(long userid);
	/**
	 * 更新用户的状态说明
	 * @since V1.6.6.2
	 * @param userid 用户id
	 * @param statusDesc 用户的状态说明
	 * @return 更新成功与否
	 */
	public boolean upateUserStatusDesc(long userid, String statusDesc);
	//v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 end
	//v1.6.7.1 RDPROJECT-384 wcw 2013-11-08 start
	public List getUserVipinfo(int status, int type,SearchParam p,int ruleStatus) ;
	public List getUserVipinfoList(int status,SearchParam p);
	//v1.6.7.1 RDPROJECT-384 wcw 2013-11-08 end
	//v1.6.7.1 安全优化 sj 2013-11-20 start
	/**
	 * 得到锁定时间
	 * 
	 * */
	public String getLockTime(long user_id);
	/**
	 * 更新锁定时间
	 * 
	 * */
	public void updateLockTime(long user_id,String lock_time);
	/**
	 *更新登录失败次数为1
	 * 
	 * */	
	public void updateFailTimes(long user_id);
	//v1.6.7.1 安全优化 sj 2013-11-20 end
	
	/**
	 * 获取用户VIP状态
	 * @param user_id
	 * @return
	 */
	public int getUserVipStatus(long user_id);

}
