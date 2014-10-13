package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.ObjAward;
import com.p2psys.domain.RuleAward;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAward;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.award.AwardResult;

/**
 * 抽奖业务处理接口
 * 
 
 * @version 1.0
 * @since 2013-7-24
 */
public interface AwardService {

	/**
	 * 抽奖
	 * 
	 * @param ruleId 抽奖规则ID
	 * @param user 用户信息
	 * @param money 倍率抽奖时用（其他抽奖时值为0）
	 * @return 抽奖结果
	 */
	AwardResult award(long ruleId, User user, double money);

	/**
	 * 根据规则ID取得抽奖规则信息
	 * 
	 * @param ruleId 规则ID
	 * @return 抽奖规则信息
	 */
	RuleAward getRuleAwardByRuleId(long ruleId);

	/**
	 * 取得抽奖规则列表
	 * 
	 * @return 抽奖规则列表
	 */
	List<RuleAward> getRuleAwardList();

	/**
	 * 更新抽奖规则表
	 * 
	 * @param data 规则信息
	 */
	void updateRuleAward(RuleAward data);

	/**
	 * 新增抽奖规则信息
	 * 
	 * @param data 抽奖规则信息
	 */
	void addRuleAward(RuleAward data);

	/**
	 * 取得最新中奖信息
	 * 
	 * @param ruleId 规则ID
	 * @param num 条数
	 * @param isOrderByLevel 是否按中奖级别排序
	 * @return 中奖信息
	 */
	List<UserAward> getAwardeeList(long ruleId, int num, boolean isOrderByLevel);

	/**
	 * 更新抽奖规则表的总金额
	 * 
	 * @param ruleId 规则ID
	 * @param money 总金额
	 */
	void updateTotalMoney(long ruleId, double money);

	/**
	 * 取得抽奖用户信息列表
	 * 
	 * @param page 页数
	 * @param param 参数
	 * @return 抽奖用户信息列表
	 */
	PageDataList getUserAwardList(int page, SearchParam param);

	/**
	 * 根据规则ID取得奖品规则信息
	 * 
	 * @param ruleId 规则ID
	 * @return 奖品规则信息
	 */
	List<ObjAward> getObjectAwardListByRuleId(long ruleId);

	/**
	 * 新增奖品规则信息
	 * 
	 * @param data 中奖规则信息
	 */
	void addObjAward(ObjAward data);

	/**
	 * 更新奖品规则表
	 * 
	 * @param data 中奖规则信息
	 */
	void updateObjAward(ObjAward data);

	/**
	 * 根据奖品ID取得奖品信息
	 * 
	 * @param awardId 奖品ID
	 * @return 奖品信息
	 */
	ObjAward getObjectAwardById(long awardId);

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
	 * 取得抽奖用户列表
	 * 
	 * @param param 参数
	 * @return 抽奖用户列表
	 */
	List<UserAward> getAllUserAwardList(SearchParam param);
	
	/**
	 * 取得用户中奖信息
	 * 
	 * @param ruleId 规则ID
	 * @param userId 用户ID
	 * @return 用户中奖信息
	 */
	List<UserAward> getMyAwardList(long ruleId, long userId);
	/**
	 * 根据条件查询获取抽奖记录面额总和
	 * @param param
	 * @return
	 */
	public double getUserAwardSum(SearchParam param);
	/**
	 * 获取该用户中奖记录
	 * @param currentPage
	 * @param param
	 * @param ruleId
	 * @param status
	 * @param userId
	 * @return
	 */
	public PageDataList getMyAwardLogList(int currentPage, SearchParam param,int ruleId,int status,int userId);
}
