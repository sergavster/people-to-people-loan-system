package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.ObjAward;
import com.p2psys.domain.RuleAward;
import com.p2psys.domain.UserAward;
import com.p2psys.model.SearchParam;

/**
 * 抽奖用Dao接口
 * 
 
 * @version 1.0
 * @since 2013-7-23
 */
public interface AwardDao extends BaseDao {
	/**
	 * 取得抽奖规则信息
	 * 
	 * @param ruleId 规则ID
	 * @return 抽奖规则信息
	 */
	RuleAward getRuleAwardById(long ruleId);

	/**
	 * 根据抽奖类型取得有效时间的规则ID
	 * 
	 * @param awardType 抽奖类型
	 * @return 规则ID
	 */
	long getRuleIdByAwardType(int awardType);

	/**
	 * 根据抽奖类型取得有效时间的规则信息
	 * 
	 * @param awardType 抽奖类型
	 * @return 规则信息
	 */
	RuleAward getRuleAwardByAwardType(int awardType);

	/**
	 * 取得抽奖规则列表
	 * 
	 * @return 抽奖规则列表
	 */
	List<RuleAward> getRuleAwardList();

	/**
	 * 更新抽奖规则表
	 * 
	 * @param ruleAward 规则信息
	 */
	void updateRuleAwardById(RuleAward ruleAward);

	/**
	 * 更新抽奖规则表的领用金额
	 * 
	 * @param ruleId 规则ID
	 * @param money 领用金额
	 */
	void updateBestowMoney(long ruleId, double money);

	/**
	 * 更新抽奖规则表的总金额
	 * 
	 * @param ruleId 规则ID
	 * @param money 总金额
	 */
	void updateTotalMoney(long ruleId, double money);

	/**
	 * 新增抽奖规则信息
	 * 
	 * @param ruleAward 抽奖规则信息
	 */
	void addRuleAward(RuleAward ruleAward);

	/**
	 * 根据规则ID取得奖品规则信息
	 * 
	 * @param ruleId 规则ID
	 * @return 奖品规则信息
	 */
	List<ObjAward> getObjectAwardListByRuleId(long ruleId);

	/**
	 * 根据奖品ID取得奖品信息
	 * 
	 * @param awardId 奖品ID
	 * @return 奖品信息
	 */
	ObjAward getObjectAwardById(long awardId);

	/**
	 * 更新奖品规则表
	 * 
	 * @param data 中奖规则信息
	 */
	void updateObjAward(ObjAward data);

	/**
	 * 更新奖品规则表的领用数量
	 * 
	 * @param ruleId 规则ID
	 * @param awardId 奖品ID
	 */
	void updateBestow(long ruleId, long awardId);

	/**
	 * 新增奖品规则信息
	 * 
	 * @param data 中奖规则信息
	 */
	void addObjAward(ObjAward data);

	/**
	 * 取得中奖信息
	 * 
	 * @param ruleId 规则ID
	 * @param num 条数
	 * @param isOrderByLevel 是否按中奖级别排序
	 * @return 中奖信息
	 */
	List<UserAward> getAwardeeList(long ruleId, int num, boolean isOrderByLevel);

	/**
	 * 取得用户中奖信息
	 * 
	 * @param ruleId 规则ID
	 * @param userId 用户ID
	 * @return 用户中奖信息
	 */
	List<UserAward> getMyAwardList(long ruleId, long userId);

	/**
	 * 根据规则ID，用户ID取得当天用户抽奖次数
	 * 
	 * @param ruleId 规则ID
	 * @param userId 用户ID
	 * @return 用户抽奖次数
	 */
	int getUserAwardDayCnt(long ruleId, long userId);

	/**
	 * 根据规则ID，用户ID取得当天用户抽奖次数
	 * 
	 * @param ruleId 规则ID
	 * @param userId 用户ID
	 * @return 用户抽奖次数
	 */
	int getUserAwardTotalCnt(long ruleId, long userId);

	/**
	 * 新增抽奖信息
	 * 
	 * @param data 抽奖信息
	 */
	void addUserAward(UserAward data);

	/**
	 * 取得抽奖用户列表
	 * 
	 * @param start 开始位置
	 * @param end 结束位置
	 * @param param 参数
	 * @return 抽奖用户列表
	 */
	List<UserAward> getUserAwardList(int start, int end, SearchParam param);

	/**
	 * 取得抽奖用户件数
	 * 
	 * @param param 参数
	 * @return 抽奖用户件数
	 */
	int getUserAwardCount(SearchParam param);

	/**
	 * 取得抽奖用户列表
	 * 
	 * @param param 参数
	 * @return 抽奖用户列表
	 */
	List<UserAward> getAllUserAwardList(SearchParam param);
	/**
	 * 根据条件查询获取抽奖记录面额总和
	 * @param param
	 * @return
	 */
	public double getUserAwardSum(SearchParam param);
	/**
	 * 获取该用户中奖记录数量
	 * @param ruleId
	 * @param userId
	 * @param status
	 * @return
	 */
	public int getUserAwardTotalCnt(long ruleId, long userId,long status,SearchParam param);
	/**
	 * 获取该用户中奖记录
	 * @param start
	 * @param end
	 * @param param
	 * @param ruleId
	 * @param status
	 * @param userId
	 * @return
	 */
	public List getUserAwardList(int start, int end, SearchParam param,int ruleId,int status,int userId);

}
