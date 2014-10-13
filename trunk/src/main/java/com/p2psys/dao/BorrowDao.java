package com.p2psys.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.p2psys.domain.Advanced;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.FinancialData;
import com.p2psys.domain.Reservation;
import com.p2psys.domain.RunBorrow;
import com.p2psys.model.AccountTypeModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserBorrowModel;
import com.p2psys.model.borrow.BorrowModel;

public interface BorrowDao extends BaseDao {
	/**
	 * 
	 * @param type
	 * @param order
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List getList(BorrowModel model);
	
//	public List getList(BorrowModel model,String[] borrows);
	
//	public List getBorrowList(BorrowModel model,String[] borrows);
	
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-13 start
	public List<Borrow> getBorrowList(long user_id, SearchParam param, int start, int end);
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-13 end
	
	public int count(BorrowModel model);
	/**
	 * 首页个数查找
	 * @param model
	 * @return
	 */
	public int countIndex(BorrowModel model);
	/**
	 * 首页列表查询
	 * @param model
	 * @return
	 */
	public List getIndexList(BorrowModel model);
	
//	public int countWithSimple(BorrowModel model);
	
	//public List getListWithSimple(BorrowModel model);

	public BorrowModel getBorrowById(long id);
	
	public UserBorrowModel getUserBorrowById(long id);
	// v1.6.7.2 wcw 2013-12-20 start
	public long addBorrow(Borrow borrow);
	// v1.6.7.2 wcw 2013-12-20 end
	public void updateBorrow(Borrow borrow);
	
	public int updateBorrow(double account,int status,long id);
	
	/**
	 * 更新为推荐标，在线金融首页显示
	 * @param borrow
	 */
	public void updateRecommendBorrow(Borrow borrow);
	
//	public List<Borrow> getBorrowList(int start,int end,SearchParam param);

//	public int getBorrowCount(SearchParam param);
	
//	int borrowCount(long user_id, SearchParam param);
	
	public List getSuccessListByUserid(long user_id,String type);
	
	public List getSuccessBorrowList(long user_id,String type,int start,int end,SearchParam param);
	
//	public List getSuccessBorrowList(String type,int start,int end,SearchParam param);
	
	public int getSuccessBorrowCount(long user_id,String type,SearchParam param);
	
//	public List getListByUserid(long user_id);


	public void deleteBorrow(Borrow borrow);
	
//	public List<Borrow> getBorrowList(long user_id);
//	
//	public int getBorrowCount(long user_id);
	
	/**
	 * 查出所有Borrow的列表，含分页
	 * @param page，页数
	 * @param param
	 * @return
	 */
	public List getAllBorrowList(int start,int pernum,SearchParam param);
	
	public List getAllBorrowList(SearchParam param);
	
	public int getAllBorrowCount(SearchParam param);
	
	/**
	 * 查出所有满标Borrow的列表，含分页
	 * @param page，页数
	 * @param param
	 * @return
	 */
	public List getFullBorrowList(int start,int pernum,SearchParam param);
	
	public int getFullBorrowCount(SearchParam param);
	
	public List unfinshBorrowList(long user_id);
	// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 start
	public int updateBorrowFlowTime(Borrow borrow);
	// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 end

	public void updateBorrowFlow(Borrow borrow);
	
	/**
	 * 每人每标投标的总额
	 * @param borrow_id
	 * @return
	 */
	public double hasTenderTotalPerBorrowByUserid(long borrow_id,long user_id);
	/**
	 *快速借款通道
	 */
	public void addjk(RunBorrow runBorrow);
	
	public List jklist(int start, int end, SearchParam param);
	public int getjkCount(SearchParam param);
	
	/**
	 * 净值标待还本息
	 * @param user_id
	 * @return
	 */
	public double getRepayTotalWithJin(long user_id);

	 public void updateJinBorrow(Borrow borrow);
	 
	// v1.6.7.1 防止标重复审核 xx 2013-11-20 start
	/**
	 * 更新标的状态
	 * @param status 		目标状态
	 * @param lastStatus 	前一状态
	 * @param id			标ID
	 * @return
	 */
	public int updateBorrowStatus(int status, int lastStatus, long id);
	// v1.6.7.1 防止标重复审核 xx 2013-11-20 end
	 /**
	 * 更新标的状态
	 * @param status
	 * @param id
	 * @return
	 */
	public int updateBorrowStatus(int status,long id);
	/**
	 * 根据状态查找列表
	 * @param status
	 * @return
	 */
	public List getBorrowListByStatus(int status);
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	 public List advancedList();
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end 
//	 //融资总额
//	 public double getBorrowAccountSum();

//	 //当日融资总额
//	 public double getDayBorrowAccountSum(String startTime,String endTime);
		// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	 //实时财务
//	 public List getBorrowList(String type, int start,int end, SearchParam param) ;
//	 public int getBorrowCount(String type, SearchParam param);
		// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	 //预约模块
	 public void reservation_add(Reservation reservation) ;
	 public int getReservation_list_count(SearchParam parm) ;
	 public List getReservation_list(int start,int end,SearchParam param);
	 
//	//系统当前融资金额统计
//	 public double getBorrowSum() ;
//	 //当天应还本息总额
//	 public double getTotalRepayAccountAndInterest(String todayStartTime,String todayEndTime);
	 //借款金额统计
	 public double getSumBorrowAccount(SearchParam param);
	 //查看借款人是否借款
	 public int hasBorrowCountByUserid(long borrow_id,long user_id);
		// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	 public List getFlowBorrowList(String type, int start,int end, SearchParam param) ;
		// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
     /**
	  * 释放已经汇款的流转标资金
	  * @param account
	  */
	 public int releaseFlowBorrow(double account,long id);
	 
	 /**
	  * 获取成交数量
	  */
	 public int getBorrowCountForSuccess();
	 
	 /**
	  * 未完成的净值标
	 * @param user_id
	 * @return
	 */
	public List unfinshJinBorrowList(long user_id);
	/**
	 * 获得正在流转并且100%完成的流转标
	 * @return
	 */
	public List getFullFlowBorrow();
	
	/**
	 * 每日发标金额（初审通过）
	 * @return
	 */
	public double getDayFirstVerifyAccount();
	
	/**
	 * 每日复审通过的投标总金额
	 * @return
	 */
	public double getDayFullVerifyAccount();
	
	/**
	 * 今日复审利息总额
	 * @return
	 */
	public double getDayInterestAccount();
	
	/**
	 * 今日投标奖励发放总额
	 * @return
	 */
	public double getDayAwardAccount();
	
	/**
	 * 各标种投标总额
	 * @return
	 */
	public List<AccountTypeModel> getBorrowTypeAccount();
	
	/**
	 * 各标种奖励总额
	 * @return
	 */
	public List<AccountTypeModel> getBorrowAwardAccount();
	
	/**
	 * 修改复审时间
	 * @param borrow
	 */
	void updateFullBorrow(Borrow borrow);
	
	/**
	 * 商富贷统计借入笔数
	 * @return
	 */
	public int getApplyBorrowCount();
	
	/**
	 * 商富贷统计借入总额
	 * @return
	 */
	public double getApplyBorrowTotal();
	public int updateBorrowFlowStatus(int flow_status, long id);
	
	/**
	 * 统计单个用户某个时间段对某种类型的标的投标次数
	 * 
	 * @return
	 */
	public Map getUserTenderNum(long userid, String biao_type, Date beginDate, Date endDate);
	
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
		
//	 /**
//	  * 今日发标总额
//	  * @return
//	  */
//	 public double getTodayBorrowTotal();
//	 /**
//	  * 今日已融资总额
//	  * @return
//	  */
//	 public double getTodayInvestTotal();
//	 /**
//	  * 今日成交笔数
//	  * @return
//	  */
//	 public int getTodayDealCount();
//	 /**
//	  * 融资总金额
//	  * @return
//	  */
//	 public double getInvestTotal();
	 
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	 
	//v1.6.6.2 RDPROJECT-118 wcw 2013-10-21 start
	public int updateBorrowAndRepay(BorrowModel b);
	//v1.6.6.2 RDPROJECT-118 wcw 2013-10-21 end
	// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 start
	public double getSumBorrowRepaymentYesAccount(SearchParam param);
	public double getSumBorrowUnRepaymentAccount(SearchParam param);
	// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 end
	
	/**
	 * 查询流标  
	 *  从BorrowFlowDao迁移过来（lhm）
	 * @param user_id
	 * @return
	 */
	public long getBorrowFlowCountByUserId(long user_id);
	
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-19 start（无分页）
	public List<Borrow> getSuccessBorrowList(long user_id);
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-19 end
		
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 start
	public double getSumAccountByBorrowId(long borrow_id);//根据borrow_id得到当前所有投标金额
	public double getAllTenderMoneyByDate(String startDate,String endDate,String validTime,String isday,Integer type); //投标总金额
	public Integer getCountBorrowByDate(String startDate,String endDate,String validTime,String isday,Integer type);  //发标笔数
	public Integer getCountTenderBorrowByDate(String startDate,String endDate,String validTime,String isday);//投标笔数
	public Integer getCountTenderUserByDate(String startDate,String endDate,String validTime,String isday); //投标人次
	public Integer getBorrowTypeByIsDay(String startDate,String endDate,Integer isday,Integer type);//天标、月标
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 end
	
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
	/**
	 * 取得实时财务列表
	 * 
	 * @param type 数据类型:1一周内应收回欠款，2逾期10天内未归还，3逾期10天以上未归还，4逾期已归还
	 * @param start 开始
	 * @param num 取得条数
	 * @return 实时财务列表
	 */
	List<FinancialData> getFinancialList(int type, int start, int num);
	
	/**
	 * 取得实时财务列表总数
	 * 
	 * @param type 数据类型:1一周内应收回欠款，2逾期10天内未归还，3逾期10天以上未归还，4逾期已归还
	 * @return 实时财务列表总数
	 */
	int getFinancialCount(int type);
	
	/**
	 * 取得实时财务统计信息（及时雨）
	 * 
	 * @return 实时财务统计信息
	 */
	Advanced getAdvanced();
	
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 start
	public List<BorrowModel> getJinBorrowListByStatusAndType(int status,
			String type);
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 end
	
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-16 start
	/**
	 * 通过设置投标天数获得待收金额
	 * 
	 * @return double
	 */
	public double getCollectionByBorrowTimeLimit(int tender_days);
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-16 start
}
