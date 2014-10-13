package com.p2psys.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.p2psys.domain.Repayment;
import com.p2psys.model.CollectionSumModel;
import com.p2psys.model.SearchParam;

public interface RepaymentDao extends BaseDao {
	/**
	 * 获取还款列表
	 * @param user_id
	 * @return
	 */
	public List getRepaymentList(long user_id);
	
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 start
	/**
	 * 还款明细
	 * @param param Repayment
	 * @param userId userId
	 * @param borrowId borrowId
	 * @return list
	 */
	List<Repayment> getRepaymentList(SearchParam param, long userId, long borrowId);
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 end
	
	// V1.6.6.2 RDPROJECT-346 ljd 2013-10-21 start
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 start
	/**
	 * 根据user_id获取用户的还款记录数量
	 * @param param param
	 * @param userId 指定用户id
	 * @return 用户的还款明细数量
	 */
	int getRepaymentCount(SearchParam param, long userId);
	
	/**
	 * 根据user_id分页获取用户的还款明细
	 * @param param param
	 * @param userId 指定用户id
	 * @param start 查询起始位置
	 * @param end 查询数量
	 * @return 用户的还款明细记录
	 */
	List<Repayment> getRepaymentList(SearchParam param, long userId, int start, int end);
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 end
	// V1.6.6.2 RDPROJECT-346 ljd 2013-10-21 end
	
	public Repayment getRepayment(long repay_id) ;
	
	public Repayment getRepayment(int order,long borrow_id) ;
	/**
	 * 根据tender_id获取还款记录
	 * @param tender_id
	 * @return
	 */
	public Repayment getRepaymentByTenderIdAndPeriod(long tender_id, long peroid);
	
	/**
	 * 新增还款表
	 * @param repays
	 */
	public void addBatchRepayment(Repayment[] repays);
	
	public void addFlowRepayment(Repayment repay);
	
	public void modifyRepayment(Repayment repay);
	
	public void modifyRepaymentBonus(Repayment repay);
	/**
	 * 修改还款计划的已还数据
	 * @param repay
	 */
	// v1.6.7.1 防止标重复审核 xx 2013-11-20 start
	public int modifyRepaymentYes(Repayment repay);
	// v1.6.7.1 防止标重复审核 xx 2013-11-20 end
	
	public void modifyFlowRepaymentYes(Repayment repay);
	
	/**
	 * 分页查找所有已经还款的标
	 * @param param
	 * @return
	 */
	public List getRepaymentList(SearchParam param,int start,int pernum);
	
	public int getRepaymentCount(SearchParam param);
	public int getCountRepaid(int user_id);
	public int getCountExprire(int user_id);
	
	/**
	 * 分页查找逾期的标
	 * @param param
	 * @return
	 */
	public List getLateList(SearchParam param,String nowTime,int start,int pernum);
	
	public int getLateCount(SearchParam param,String nowTime);
	
	public boolean hasRepaymentAhead(int period,long borrow_id);
	
	/**
	 * 新增方法：根据搜索条件查询提前还款，迟还款，逾期还款，逾期未还款的标
	 * 修改日期：2013-3-21
	 * @param search
	 * @param user_id
	 * @return
	 */
    // 标详情页面性能优化 lhm 2013/10/11 start
	//public List getRepaymentList(String search,long user_id);
	public long getRepaymentCount(String search,long user_id);
    // 标详情页面性能优化 lhm 2013/10/11 end
	/**
	 * 根据webstatus查找所有的还款计划
	 * @param status
	 * @return
	 */
	public List getAllRepaymentList(int webstatus);
	/**
	 * 从since开始到现在未还款的还款计划列表
	 * @param webstatus
	 * @param since
	 * @return
	 */
	public List getAllRepaymentList(int webstatus, long start,long end);

	// 逾期的借款标列表
	public List getOverdueList(SearchParam param, String nowTime, int start, int pernum);

	public List getOverdueList(SearchParam param, String nowTime);

	// 逾期的借款标count
	public int getOverdueCount(SearchParam param, String nowTime);

	public List getRepaymentList(SearchParam param);

	/**
	 * 统计待还款总额
	 * 
	 * @param param
	 * @return
	 */
	public double getRepaymentSum(SearchParam param);

	public void modifyRepaymentStatus(long id,int status,int webstatus);

	
	/**
	 * 用户还款
	 * 设置webstatus=1
	 * @param id
	 * @return
	 */
	public int predictRepay(long id);
	
	/**
	 * 查询所有未还款的计划，每个标取最小的一个
	 * @return
	 */
	public List<Map<String, Object>> getToPayRepayMent();
	//查询出所有逾期未还的标
	public List<Map<String, Object>> getLateList();
	//查询出这个标逾期未还的所有总额
	public double getSumLateMoney(long id);
	//更新borrow_repayment的还款计划表的逾期信息
	public void updateLaterepayment(long lateDay,long id,double late_interest);
	//根据标id查询逾期未还的标
	public List getLateRepaymentByBorrowid(long borrow);
	//查出逾期金额
	public double getAllSumLateMoney(int status);
	/**
	 * 统计给力标到期（含分页）
	 * @param param
	 * @param nowTime
	 * @return
	 */
	public List getFastExpireList(SearchParam param,int start, int pernum);
	/**
	 * 统计给力标到期（不含分页）
	 * @param param
	 * @return
	 */
	public List getFastExpireList(SearchParam param);
	/**
	 * 统计给力标条数
	 * @param param
	 * @return
	 */
	public int getFastExpireCount(SearchParam param);
	/**
	 * 逾期未还款总额
	 * @return
	 */
	public double getAllLateSumWithNoRepaid();
	/**
	 * 逾期已还款总额
	 * @return
	 */
	public double getAllLateSumWithYesRepaid();
	
	/**
	 * 每日到期金额合计
	 * @return
	 */
	public double getDayMatureAccount(String dotime1,String dotime2);
	
	/**
	 * 每日到期本金合计
	 * @return
	 */
	public double getDayCapitalAccount(String dotime1,String dotime2);
	
	/**
	 * 每日到期利息合计
	 * @return
	 */
	public double getDayMatureInterest(String dotime1,String dotime2);
	
	/**
	 * 每日还款本金合计
	 * @return
	 */
	public double getDayRepayCapital(String dotime1,String dotime2);
	
	/**
	 * 每日还款利息合计
	 * @return
	 */
	public double getDayRepayInterest(String dotime1,String dotime2);
	
	// v1.6.7.1 添加分页  zza 2013-11-25 start
	/**
	 * 用户相关数据统计（包括投标次数投标金额）
	 * @param p p
	 * @param dotimeStr1 dotimeStr1
	 * @param dotimeStr2 dotimeStr2
	 * @return int
	 */
	int getDayMatureCount(SearchParam p, String dotime1,String dotime2);
	
	/**
	 * 当天需要还款的借款标
	 * @param page page
	 * @param pernum pernum
	 * @param p p
	 * @param dotimeStr1 dotimeStr1
	 * @param dotimeStr2 dotimeStr2
	 * @return list
	 */
	List<Repayment> getDayMatureList(int page, int pernum, SearchParam p, String dotime1,String dotime2);
	
	/**
	 * 当天需要还款的借款标（导出）
	 * @param param param
	 * @param dotimeStr1 dotimeStr1
	 * @param dotimeStr2 dotimeStr2
	 * @return list
	 */
	List<Repayment> getDayMatureList(SearchParam param, String dotime1,String dotime2);
	// v1.6.7.1 添加分页  zza 2013-11-25 end
	
	/**
	 * 每日投标时发放的奖励
	 * @return
	 */
	public double getDayTenderFunds(String dotime1,String dotime2);
	
	/**
	 * 每日还款时发放的奖励
	 */
	public double getDayRepaymentFunds(String dotime1,String dotime2);
	
	
	public List<Repayment> getRepaymentByBorrowId(long borrow_id);
	
	public List<Repayment> getRepayListByBorrowId(long borrow_id);
	
	/**
	 * 统计不同类型某时间段的用户总额
	 * @param user_id
	 * @param start 统计时间开始时间
	 * @param end 统计时间结束时间
	 * @param status 还款利息状态 ,如果status为-1，则为所有类型,0待还，1已还
	 * @return
	 */
	public double getRepayInterestSum(long user_id,int status, long start_time , long end_time);
	/**
	 * 查询所有未还款的计划，每个标取最小的一个
	 * @return
	 */
	public List<Map<String, Object>> getAllDueRepayMent(Date date);	 
	
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	/**
//	  * 待还款总额
//	  * @return
//	  */
//	 public double getRepayingTotal();
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start  
	/**
	 * 修改展期利息
	 * 
	 */
	public void modifyRepaymentExtensionInterest(Repayment repay);
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end  
	
	// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
	/**
	 * 已还款、未还款本金合计、本息合计、利息合计
	 * @param param param
	 * @param type type
	 * @return list
	 */
	CollectionSumModel getRepaySum(SearchParam param, String type);
	// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
}
