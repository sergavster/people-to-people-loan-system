package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Repayment;
import com.p2psys.model.CollectionSumModel;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.UserRepaymentInfo;

public interface MemberBorrowService {
	/**
	 * 获取我的借款
	 * 
	 * @param type 
	 * @param start
	 * @param end
	 * @param dotime1
	 * @param dotime2
	 * @param keyword
	 * @return
	 */
	public PageDataList getList(String type,long user_id, int startPage,SearchParam param);
	
	
	/**
	 * 获取用户的需要还款列表
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
	 * 根据user_id分页获取用户的还款明细
	 * @param param param
	 * @param userid 指定用户id
	 * @param page 当前页数
	 * @return 用户的还款明细记录
	 */
	PageDataList getRepaymentList(SearchParam param, long userid, int page);
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 end
	// V1.6.6.2 RDPROJECT-346 ljd 2013-10-21 end
	
	public Repayment getRepayment(long repay_id);
	
	/**
	 * 获取我是借款者的借款信息列表
	 * @param user_id
	 * @return
	 */
	public List getBorrowTenderListByUserid(long user_id);
	
	public List getBorrowTenderListByUserid(long user_id,long borrowId);
	/**
	 * 获取
	 * @param cid
	 * @return
	 */
	public DetailCollection getCollection(long cid);
	
	//还款操作
	public void doRepay(Repayment repay,Account act);
	
	public PageDataList getRepaymentList(SearchParam param,int page);
		/**
	 * 查询待还款记录，供导出
	 * @param param
	 * @return
	 */
	public List getRepaymentList(SearchParam param) ;
	public PageDataList getLateList(SearchParam param,int page);
	

	/**
	 * 新增方法：根据条件获得用户的未还款和还款的信息
	 * 修改日期：2013-3-21
	 * @param type
	 * @param user_id
	 * @return
	 */
	// 标详情页面性能优化 lhm 2013/10/11 start
//	public List getRepayMentList(String type,long user_id);
	public long getRepayMentCount(String type,long user_id);
	// 标详情页面性能优化 lhm 2013/10/11 end
	
	/**
	 * 到期还款流转标
	 */
	public PageDataList getUnFinishFlowList(SearchParam param, int page) ;
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 start
	/**
	 * 到期未还款、还款流转标
	 */
	public PageDataList getFlowList(SearchParam param, int page,String type) ;
	/**
	 *  到期未还款、还款流转标，供导出
	 * @param param
	 * @return
	 */
	public List getFlowList(SearchParam param,String type);
	
	/**
	 * 本金合计、本息合计、利息合计
	 * @param param param
	 * @param nowTime nowTime
	 * @param type type
	 * @return list
	 */
	CollectionSumModel getFlowSum(SearchParam param, String nowTime, String type);
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 end
	//逾期的借款标
		public PageDataList getOverdueList(SearchParam param, int page);
		public List getOverdueList(SearchParam param) ;
		public double getRepaymentSum(SearchParam param);
		
	 /**
	 *  到期未还款流转标，供导出
	 * @param param
	 * @return
	 */
	public List getUnFinishFlowList(SearchParam param);
   
	/**
	 * 根据用户id获取用户逾期未还款的信息
	 * 修改日期：2013-05-31
	 * @param user_id
	 * @return
	 */
	public List getLateRepaymentByBorrowid(long borrow_id);
	
	// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
	/**
	 * 已还款、未还款本金合计、本息合计、利息合计
	 * @param param param
	 * @param type type
	 * @return list
	 */
	CollectionSumModel getRepaySum(SearchParam param, String type);
	// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
	
	// v1.6.7.2 RDPROJECT-471 zza 2013-11-29 start
	/**
	 * 审核不通过的借款标列表
	 * @param user_id
	 * @param startPage
	 * @param param
	 * @return
	 */
	public PageDataList getVerifyFailList(long user_id, int startPage, SearchParam param, String type);
	// v1.6.7.2 RDPROJECT-471 zza 2013-11-29 end
	
	
	// v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start
	public Integer getRepaymentCount(SearchParam param);
	public Integer getFlowCount(SearchParam param,String type);
	// v1.6.7.1 RDPROJECT-510 cx 2013-12-04 end
	
	 //v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 start
	public UserRepaymentInfo getUserRepamentInfo(long user_id);
	 public UserAccountSummary getUserBorrowSummary(long user_id);
	 //v1.6.7.2 RDPROJECT-272 wcw 2013-12-25 end
}
