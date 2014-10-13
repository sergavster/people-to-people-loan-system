package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.RewardStatistics;
import com.p2psys.domain.Rule;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RewardRecordModel;
import com.p2psys.model.SearchParam;

/**
 * 奖励统计Service
 */
public interface RewardStatisticsService {

	/**
	 * 列表
	 * @param param param
	 * @return List
	 */
	List<RewardStatistics> getRewardStatistics(SearchParam param);
	
	/**
	 * 查出符合条件的借款信息
	 * @param param param
	 * @param startPage startPage
	 * @return List
	 */
	PageDataList getRewardStatisticsList(SearchParam param, int startPage);
	
	/**
	 * 更新
	 * @param r r
	 * @param status status
	 * @param authUser authUser
	 * @param ip ip
	 */
	void verifyReward(RewardStatistics r, String status, User authUser, String ip);
	
	/**
	 * 根据id获取信息
	 * @param id id
	 * @return RewardStatistics
	 */
	RewardStatistics getRewardStatisticsById(long id);
	
	/**
	 * 根据获得奖励的用户id获取信息
	 * @param userId userId
	 * @return RewardStatistics
	 */
//	RewardStatistics getRewardByRewardUserId(long userId);
	
	/**
	 * 查出所有列表
	 * @return List
	 */
	List<RewardStatistics> getRewardList();
	
	/**
	 * 添加记录
	 * @param id id
	 * @param ip ip
	 */
	void addRewardStatistics(long id, String ip);
	
	/**
	 * vip审核后更新状态
	 * @param ip ip
	 * @param userCache userCache
	 */
	void updateReward(String ip, UserCache userCache);
	
	/**
	 * 根据nid取得规则
	 * @param rule rule
	 * @param init init
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return String
	 */
	String getRule(Rule rule, String init, String startTime, String endTime);
	
	/**
	 * 更新应收金额
	 * @param account account
	 * @param id id
	 */
	void updateAccount(double account, long id);
	
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 start
	/**
	 * 根据规则计算被邀请人的投资总额
	 * @param borrow borrow
	 * @param rule rule
	 */
//	void tenderCount(Borrow borrow, Rule rule);
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 end

	/**
	 * 根据获得奖励的id取得信息
	 * @param userId userId
	 * @param type type
	 * @param addtime addtime
	 * @return list
	 */
//	List<RewardStatistics> getPassive(long userId, String type, String addtime);
	
	// v1.6.7.2 RDPROJECT-547 lx 2013-12-6 start
	/**
	 * 查询奖励记录-分页查询
	 * @param param
	 * @param startPage
	 * @return
	 */
	public PageDataList getRewardRecordList(SearchParam param, int startPage);
	
	/**
	 * 查询奖励记录
	 * @param param
	 * @return
	 */
	public List<RewardRecordModel> getRewardRecordList(SearchParam param);
	// v1.6.7.2 RDPROJECT-547 lx 2013-12-6 end
	
}
