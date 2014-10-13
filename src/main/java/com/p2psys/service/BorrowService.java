package com.p2psys.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Advanced;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.BorrowAuto;
import com.p2psys.domain.BorrowConfig;
import com.p2psys.domain.Collection;
import com.p2psys.domain.OperationLog;
import com.p2psys.domain.Protocol;
import com.p2psys.domain.Repayment;
import com.p2psys.domain.Reservation;
import com.p2psys.domain.RunBorrow;
import com.p2psys.domain.Tender;
import com.p2psys.domain.TenderAwardYesAndNo;
import com.p2psys.model.AccountTypeModel;
import com.p2psys.model.BorrowTender;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.PageDataList;
import com.p2psys.model.ProtocolModel;
import com.p2psys.model.RankModel;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserBorrowModel;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.model.invest.CollectionList;
import com.p2psys.model.invest.InvestBorrowList;

public interface BorrowService {
	/**
	 * 获取标的列表方法
	 * @return 
	 */
	public List getList();
	
//	public List getList(int type);

	public List getList(int type,int status);
	
//	public PageDataList getList(BorrowModel model,int perNum);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public PageDataList getList(BorrowModel model);
	
	public List getBorrowList(BorrowModel model);
	/**
	 * 获取首页推荐标列表
	 */
	public List getRecommendList();
	
	/**
	 * 更新为推荐标，在线金融首页显示
	 * @param borrow
	 */
	public void updateRecommendBorrow(Borrow borrow);
	
	/**
	 * 借款标的信息，不含借款人的信息
	 * @param id
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public BorrowModel getBorrow(long id);
	
//	/**
//	 * 根据id查询借款
//	 * @param tid
//	 * @return
//	 */
//	public Tender getTenderById(long tid);
	/**
	 * 借款标的信息，含借款人的基本信息
	 * @param id
	 * @return
	 */
	public UserBorrowModel getUserBorrow(long id);
	
	/**
	 * 获取借款标的投资列表,不含分页
	 * @param borrow_id
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public List getTenderList(long borrow_id);
	
	public List getTenderList(long borrow_id,long user_id);
	
	/**
	 * 获取借款标的投资列表,含分页
	 * @param borrow_id
	 * @return
	 */
	public PageDataList getTenderList(long borrow_id,int page,SearchParam param);
	
	/**
	 * 增加新的标
	 * 
	 * @param borrow
	 */
	public void addBorrow(BorrowModel borrow,AccountLog log);
	
	/**
	 * 修改标
	 * 
	 * @param borrow
	 */
	public void updateBorrow(Borrow borrow);
	
	public void verifyBorrow(BorrowModel borrow,AccountLog log,OperationLog operationLog) ;
	/**
	 * 
	 * @param borrow
	 */

	public void deleteBorrow(BorrowModel borrow,AccountLog log);
	
	/**
	 * 根据用户ID获取成功投资的标的列表
	 * @param user_id
	 * @param type
	 * @return
	 */
	public List getSuccessListByUserid(long user_id,String type);
	
	/**
	 * 获取成功投资列表，含分页
	 * @param user_id
	 * @param type
	 * @param param
	 * @return
	 */
	public InvestBorrowList getSuccessListByUserid(long user_id,String type,int startPage,SearchParam param);

	/**
	 * 首页的成功案例
	 * @return
	 */
	public List getSuccessListForIndex(String type,int pernum);
	
	/**
	 * 投标的业务方法
	 * @param tenderAccount 本次投标的金额
	 * @param tender 本次投标封装的Tender对象
	 * @param borrow  投标的借款对象
	 * @param log 本次资金记录变更Log
	 * @return 返回本次新增的Tender对象
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public Tender addTender(Tender tender, BorrowModel borrow, Account act) ;	
	// v1.6.6.1开始不用
//	public BorrowFlow addFlow(BorrowFlow flow,BorrowModel borrow,Account act,AccountLog log);
	// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 start
	/**
	 * 判断流转次数
	 * @param borrow
	 * @return
	 */
	public boolean checkFlowTime(BorrowModel borrow);
	// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 end
//	/**
//	 * 根据用户id,标的状态获取详细的还款情况
//	 * @param user_id
//	 * @param status
//	 * @return
//	 */
//	public List getDetailCollectionList(long user_id,int status);
	
	public CollectionList getCollectionList(long user_id,int status,int startPage,SearchParam param);
	
	/**
	 * 根据borrowID进行查询待收记录
	 * @param borrow_id
	 * @param startPage
	 * @param param
	 * @return
	 */
	public PageDataList getCollectionListByBorrow(long borrow_id,int startPage,SearchParam param);
	
	public List getCollectionListByBorrow(long borrow_id);
	
//	/**
//	 * 根据用户ID获取标的投资列表,无分页
//	 * @param user_id
//	 * @return
//	 */
//	public List getInvestTenderListByUserid(long user_id);
	
	/**
	 * 根据用户ID获取标的投资列表,分页
	 * @param user_id
	 * @return
	 */
	public PageDataList getInvestTenderListByUserid(long user_id,int startPage,SearchParam param);
	
	
	/**
	 * 根据自动投标的列表
	 * @param user_id 用户ID
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public List getBorrowAutoList(long user_id) ;
	
	/**
	 * 新增自动投标设置
	 * @param auto
	 */
	public void addBorrowAuto(BorrowAuto auto);
	
	/**
	 * 修改自动投标设置
	 * @param auto
	 */
	public void modifyBorrowAuto(BorrowAuto auto);
	
	/**
	 * 删除自动投标
	 * @param id
	 */
	public void deleteBorrowAuto(long id);
	
	/**
	 * 根据用户ID获取账户信息
	 * @param user_id 用户ID
	 * @return
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public Account getAccount(long user_id);
	
	public List  getBorrowList(long user_id);
	
//	public PageDataList getBorrowList(int page, long user_id, SearchParam param);
	
//	public List  getInvestList(long user_id);
	
//	/**
//	 * 用户显示是的成功借款列表的条数
//	 * @param user_id
//	 * @return
//	 */
//	public int getBorrowCount(long user_id);
	
	/**
	 * 后台管理查出所有的标的列表
	 * @param startPage
	 * @param param
	 * @return
	 */
	public PageDataList getAllBorrowList(int startPage,SearchParam param);
	
	public List getAllBorrowList(SearchParam param);
	
	/**
	 * 后台管理查出所有已经满标的列表
	 * @param startPage
	 * @param param
	 * @return
	 */
	public PageDataList getFullBorrowList(int startPage,SearchParam param);
	
	public void verifyFullBorrow(BorrowModel model,OperationLog operationLog);
	
//	public void verifyFullBorrow(BorrowModel model,AccountLog log);

	public List unfinshBorrowList(long user_id);

	public List getBonusAprList(long borrow_id);
	
//	public void addBonusApr(Borrow borrow);
	
	public void modifyBonusAprById(long id,double apr);
	
//	/**
//	 * 正在投标的项目，但未满标审核
//	 * @param user_id
//	 * @return
//	 */
//	public List getInvestTenderingListByUserid(long user_id);
	
	/**
	 * 后台投资排行榜（总额，可查询）
	 * @param param
	 * @return
	 */
	public List<RankModel> getRankListByTime(SearchParam param);
	
	public PageDataList getRankListByTime(int page, SearchParam param);
	
	// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 start
	/**
	 * 首页投资排行榜
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param num 个数
	 * @param init 标种拼接的SQL
	 * @return list
	 */
	List<RankModel> getRankListByTime(String startTime, String endTime, String init, RuleModel rule);
	// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 end
	
	/**
	 * 国临创投排行榜
	 * @return
	 */
//	public List getRankList();
	/**
	 * 国临创投排行榜显示全部
	 * @return
	 */
	public List getAllRankList();
	
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
	public List getMoreRankListByTime(String startTime,String endTime,int num,String init);
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
	
	public PageDataList getInvestTenderingListByUserid(long user_id,int page,SearchParam param);
	
	public double hasTenderTotalPerBorrowByUserid(long borrow_id,long user_id) ;
	public List getNewTenderList() ;
//	public List getSuccessTenderList() ;
	/**
	 * 快速借款通道
	 */
	public void addJk(RunBorrow runBorrow);
	public PageDataList jk(int page, SearchParam param);
	
	public List<BorrowTender> getAllTenderList(long borrow_id);
 	
	public double getRepayTotalWithJin(long user_id);

    // 标详情页面性能优化 lhm 2013/10/11 start
	/**
	 * 新增方法：获得流标
	 * 修改日期：2013-3-21
	 * @param user_id
	 * @return
	 */
//	public List getBorrowFlowListByuserId(long user_id);
	public long getBorrowFlowCountByUserId(long user_id);
	
	/**
	 * 新增方法：根据条件获得提前还款，迟还款，逾期还款，逾期未还款
	 * 修改日期：2013-3-21
	 * @param search
	 * @param user_id
	 * @return
	 */

//	public List getRepaymentList(String search,long user_id);
	public long getRepaymentCount(String search,long user_id);
	// 标详情页面性能优化 lhm 2013/10/11 end
	public PageDataList getAllBorrowTenderList(int startPage, SearchParam param);
	public List getAllBorrowTenderList(SearchParam param);
	public void updateJinBorrow(Borrow borrow);
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	//垫付资金
//	public List advancedList();
//	public Advanced borrowAccountSum(String startTime,String endTime) ;
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	//实时财务
//	public PageDataList getBorrowList(String type,int startPage,SearchParam param) ;
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	//借款金额统计
		public double getSumBorrowAccount(SearchParam param);
//	//系统当前融资总额
//		public double getBorrowTotal();
//		//当天应还本息总额
//		public double getTotalRepayAccountAndInterest(String todayStartTime,String todayEndTime);
		//查询借款人是否借款
		public int hasBorrowCountByUserid(long borrow_id, long user_id);
		//山水聚宝活动时间内累计投资总金额
		public double hasTenderListByUserid(long user_id, String starttime,String endtime);
		 //累计投资奖励是否启用
		public void updateTenderAward(TenderAwardYesAndNo tenderAwardYesAndNo);
		public TenderAwardYesAndNo TenderAward(int id);
	/**
	 * 根据webstatus获取对应的还款列表
	 * @param webstatus
	 * @return
	 */
	public List getAllRepaymentList(int webstatus) ;
	/**
	 * 根据status查找
	 * @param status
	 * @return
	 */
	public List<DetailCollection> getFlowRepayCollectList(int status);
	/**
	 * 根据状态查找标
	 * @param status
	 * @return
	 */
	public List getBorrowListByStatus(int status);
	//预约模块
	public void reservation_add(Reservation reservation);
	public PageDataList reservation_list(SearchParam param,int page) ;
	public void verifyFullBorrow(BorrowModel model,OperationLog operationLog,Protocol protocol) ;
	//及时雨委托协议
	public PageDataList protocolList(SearchParam param,int page) ;
	public ProtocolModel getProtocolByid(long id);

	 /**
    * 催款添加记录
    * @param params
    */
	public void addLateLog(Map<String, Object> params);
	/**
	 * get催款记录
	 */
	public List<Map<String, Object>> getLateLog(String borrowid);
	
	/**
	 * 获取成交笔数
	 */
	public int getBorrowCountForSuccess();
	
//	/**
//	 * 用户投标测试
//	 */
//	public int getTotalTenderTimesByUserid(long userid);

	/**
	 * 给力标到期(含分页)
	 * @param param
	 * @param page
	 * @return
	 */
	public PageDataList getFastExpireList(SearchParam param,int page);
	/**
	 * 给力标到期（不含分页）
	 * @param param
	 * @return
	 */
	public List getFastExpireList(SearchParam param); 
	
	/**
	 * 未完成的净值标
	 * @param user_id user_id
	 * @return List
	 */
	public List<Borrow> unfinshJinBorrowList(long user_id);
	/**
	 * 统计某个时间段的有效投标金额
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public double countTenderAccount(String startTime,String endTime) ;
	
	/**
	 * 每日发标金额（初审通过）
	 * @return
	 */
	public double getDayFirstVerifyAccount();
	
	/**
	 * 今日复审通过的投标总金额
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
	 * 当天需要还款的借款标
	 * @param page page
	 * @param param param
	 * @param dotimeStr1 dotimeStr1
	 * @param dotimeStr2 dotimeStr2
	 * @return list
	 */
	PageDataList getDayMatureList(int page, SearchParam param, String dotime1,String dotime2);
	
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
	
//	public List<Repayment> getRepaymentByBorrowId(long borrow_id);
	
	public List getFlowCollection(long id);
	/**
	 * 借款标配置列表
	 */
	public List getBorrowConfig();
	/**
	 * 借款标配置通过id显示
	 */
	 public BorrowConfig viewConfig(long id);
	 /**
	  * 借款标配置修改
	  * @param borrowConfig
	  * @return
	  */
	 public void updateConfig(BorrowConfig borrowConfig);
	 
	 public List getProtocolTenderListByBorrowid(long id); 
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
	 
	 /**
	   * 统计单个用户某个时间段对某种类型的标的投标次数
	   * @return
	   */	 
	 public Map getUserTenderNum(long userid, String biao_type, Date beginDate, Date endDate);
	 
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	 	 /**
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
//	  * 今日待收利息
//	  * @return
//	  */
//	 public double getTodayInterest();
//	 /**
//	  * 融资总金额
//	  * @return
//	  */
//	 public double getInvestTotal();
//	 /**
//	  * 待还款总额
//	  * @return
//	  */
//	 public double getRepayingTotal();
//	 /**
//	  * 为投资人赚得利息
//	  * @return
//	  */
//	 public double getInterestTotal();
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end

	 public void stopTender(BorrowModel borrowModel);
	 
	// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 start
    //标已还款总额合计
	public double getSumBorrowRepaymentYesAccount(SearchParam param);
	public double getSumBorrowUnRepaymentAccount(SearchParam param);
	// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 end
	
	//v1.6.6.2 RDPROJECT-360 liukun 2013-10-25 start
	/**
	 * 修改标
	 * 
	 * @param model
	 */
	public void updateBorrow(BorrowModel model, HashMap oldModel);
	//v1.6.6.2 RDPROJECT-360 liukun 2013-10-25 end
	//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 start
	 /**
	  * 获取投资人的投标收款计划
	  * @param tender_id
	  * @return
	  */
	 public List getCollectionByTender(long tender_id);
	//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 end
	
	// v1.6.7.1 RDPROJECT-439 zza 2013-11-12 start
	/**
	 * 每天早上7点定时查询平台的借款总金额和待收总金额
	 */
	void updateTenderAndCollection();
	// v1.6.7.1 RDPROJECT-439 zza 2013-11-12 end
	
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-19 start（无分页）
	List<Borrow> getSuccessBorrowList(long user_id);
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-19 end
	
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-06 start
	public double getSumAccountByBorrowId(long borrow_id); //根据borrow_id得到当前所有投标金额
	public Integer getBorrowCount(SearchParam param);
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
	 * @param startPage 开始页
	 * @param param 分页用
	 * @return 实时财务列表
	 */
	PageDataList getFinancialList(int type, int startPage, SearchParam param);	

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
	
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
	/**
	 * 得到投一个标的次数
	 * 
	 * @param borrow_id,user_id
	 * 
	 * @return int
	 */	
	public int getTenderTimes(long id, long user_id);
	/**
	 * 通过设置投标天数获得待收金额
	 * 
	 * @return double
	 */
	public double getCollectionByBorrowTimeLimit(int tender_days);
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
	
	// v1.6.7.2 债权转让 zhangyz 2013-12-25 start
	/**
	 * 查询用户的待收列表
	 * @param map
	 * @return
	 */
	public List<Collection> getCollectionList(Map<String,Object> map);
	
	/**
	 * 根据ID获取tender对象
	 * @param tid
	 * @return
	 */
	public Tender getTenderById(long tid);
	
	/**
	 * 查询用户的待收列表
	 * @param id
	 * @return
	 */
	public Collection getCollectionById(long id);
	// v1.6.7.2  债权转让 zhangyz 2013-12-25 end
}
