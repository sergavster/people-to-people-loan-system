package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.RewardStatistics;
import com.p2psys.domain.User;
import com.p2psys.model.RewardStatisticsModel;
import com.p2psys.model.RewardStatisticsSumModel;
import com.p2psys.model.SearchParam;

/**
 * 奖励统计DAO
 */
public interface RewardStatisticsDao extends BaseDao {

	/**
	 * 根据规则表主键ID查询数据
	 * @param rewardStatistics rewardStatistics
	 */
	void addRewardStatistics(RewardStatistics rewardStatistics);
	
	/**
	 *  列表
	 * @param param param
	 * @return List
	 */
	List<RewardStatistics> getRewardStatistics(SearchParam param);
	
	/**
	 * 查出列表
	 * @param param param
	 * @param start start
	 * @param end end
	 * @return List
	 */
	List<RewardStatisticsModel> getRewardStatisticsList(SearchParam param, int start, int end);
	
	/**
	 * 取得总数
	 * @param param param
	 * @return int
	 */
	int getCount(SearchParam param);
	
	/**
	 * 更新
	 * @param r r
	 */
	void updateReward(RewardStatistics r);
	
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
	 * 根据邀请人Id和被邀请人注册时间，查询出被邀请人的Id
	 * @param inviteId inviteId
	 * @param addTime addTime
	 * @return User
	 */
	User getUserByInviteId(long inviteId, String addTime);
	
	/**
	 * 根据外键Id查出对应数据
	 * @param typePkId 外键Id
	 * @return RewardStatistics
	 */
//	RewardStatistics getByTypePkId(long typePkId);
	
	/**
	 * 根据被邀请人获取信息
	 * @param rewardUserId rewardUserId
	 * @param passiveUserId passiveUserId
	 * @param type type
	 * @return RewardStatisticsModel
	 */
	// v1.6.6.2 RDPROJECT-338 zza 2013-10-16 start
	List<RewardStatisticsModel> getRewardByPassiveId(long rewardUserId, long passiveUserId, byte type);
	// v1.6.6.2 RDPROJECT-338 zza 2013-10-16 end
	
	/**
	 * 根据规则的外键获取获取信息
	 * @param type type
	 * @param typeFkId typeFkId
	 * @return RewardStatistics
	 */
//	RewardStatistics getRewardByTypeFkId(byte type, long typeFkId);
	/**
	 * 更新应收金额
	 * @param account account
	 * @param id id
	 */
	void updateAccount(double account, long id);
	
//	public List<RewardStatisticsModel> getUnShowList();
	
	// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 start
	/**
	 * 根据投资人取得应该获得奖励的邀请人
	 * @param userId userId
	 * @param ruleId ruleId
 	 * @param addtime addtime
	 * @return list
	 */
	public List<RewardStatistics> getRewardUserByPassive(long userId, long ruleId, String addtime);
	// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 end
	
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-22 start
	/**
	 * 根据被邀请人Id获取信息
	 * @param passiveUserId passiveUserId
	 * @param receiveStatus receiveStatus
	 * @param rule_id rule_id
	 * @return RewardStatistics
	 */
	List<RewardStatistics> getRewardByPassiveId(long passiveUserId, Byte receiveStatus, long rule_id);
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-22 end
	
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-24 start
	/**
	 * 根据user的信息查询对应的投资记录
	 * @param userId userId 
	 * @param init init
	 * @return RewardStatisticsSumModel
	 */
	RewardStatisticsSumModel getSumAccount(long userId, String init);
	// v1.6.6.2 RDPROJECT-287 zza 2013-10-24 end
	
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-11 start
	/**
	 * 根据被邀请人获取已发奖励的信息
	 * @param userId userId
	 * @param type type
	 * @param addtime addtime
	 * @param verify verify
	 * @return list
	 */
//	List<RewardStatistics> getReceiveRewardUser(long userId, String type, String addtime, int verify);
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-11 end

	/**
	 * 根据获得奖励的id取得信息
	 * @param userId userId
	 * @param ruleId ruleId
	 * @param addtime addtime
	 * @return list
	 */
	List<RewardStatistics> getPassiveByReward(long userId, long ruleId, String addtime);
	
}
