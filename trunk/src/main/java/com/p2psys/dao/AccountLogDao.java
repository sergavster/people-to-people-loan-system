package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.AccountLog;
import com.p2psys.domain.AutoTenderOrder;
import com.p2psys.model.SearchParam;
import com.p2psys.model.accountlog.BaseAccountLog;

public interface AccountLogDao extends BaseDao {
	/**
	 * 新增资金记录 
	 * @param log
	 */
	public void addAccountLog(AccountLog log);
	
	/**
	 * 获取资金记录的交易金额的统计
	 * @param user_id
	 * @return
	 */
	public double getAccountLogTotalMoney(long user_id) ;
	
//	/**
//	 * 获取待还利息资金记录的交易金额的统计
//	 * @param user_id
//	 * @return
//	 */
//	public double getAccountLogInterestTotalMoney(long user_id) ;
	
	
	/**
	 * 获取用户的资金记录的数量
	 * @param user_id
	 * @return
	 */
	public int getAccountLogCount(long user_id,SearchParam param);
	
	/**
	 * 获取用户的资金记录，含分页
	 * @param user_id
	 * @return
	 */
	public List getAccountLogList(long user_id,int start,int end,SearchParam param);
	
	/**
	 * 获取用户的资金记录
	 * @param user_id
	 * @return
	 */
	public List getAccountLogList(long user_id) ;
	
	public List getAccountLogList(long user_id,SearchParam param);
	
	/**
	 * 获取所有资金记录的数量
	 * @param user_id
	 * @return
	 */
	public int getAccountLogCount(SearchParam param);
	
	/**
	 * 获取所有资金记录，含分页
	 * @param user_id
	 * @return
	 */
	public List getAccountLogList(int start,int pernum,SearchParam param);
	
	public List getAccountLogList(SearchParam param);
	
	public int getTenderLogCount(SearchParam param);
	
	public List getTenderLogList(int start,int pernum,SearchParam param);
	
	public List getTenderLogList(SearchParam param);
	
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	//奖励统计
	public double getAwardSum(long user_id);
	
	public double getAwardSum(long user_id,long starttime);
	
	//已赚统计
	public double getInvestInterestSum(long user_id);
//	public double getrepayInterestSum(long user_id);
	
	public double getAccountLogSum(String type);
	
	public List getAccountLogSumWithMonth(SearchParam param);
	
//	public int getAccountLogSumCount();
	
//	/**
//	 * 查询user的线下充值奖励
//	 * @param userId
//	 * @param type 类型
//	 * @param startTime 开始时间
//	 * @param endTime 结束时间
//	 * @return
//	 */
//	public double getOfflineRewardSum(long userId, String type , long startTime, long endTime);
	
//	public void addAccountLog(BaseAccountLog log);
	
	// v1.6.7.1 RDPROJECT-124 zza 2013-11-14 start
	/**
	 * 自动投标排名查询count
	 * @param param param
	 * @return int
	 */
	int getAutoTenderLogCount(SearchParam param);
	
	/**
	 * 自动投标排名查询列表
	 * @param start start
	 * @param pernum pernum
	 * @param param param
	 * @return list
	 */
	List<AutoTenderOrder> getAutoTenderLogList(int start, int pernum, SearchParam param);
	
	/**
	 * 导出
	 * @param param param
	 * @return list
	 */
	List<AutoTenderOrder> getAutoTenderLogList(SearchParam param);
	
	// v1.6.7.1 RDPROJECT-124 zza 2013-11-14 end
	
}
