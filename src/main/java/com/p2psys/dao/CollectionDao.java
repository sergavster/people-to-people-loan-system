package com.p2psys.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.p2psys.domain.Collection;
import com.p2psys.model.CollectionSumModel;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.SearchParam;

public interface CollectionDao extends BaseDao {
	
	public Collection addCollection(Collection c);
	
	public void addBatchCollection(List<Collection> list);
	
	public List getDetailCollectionList(long user_id,int status);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public List getCollectionList(long user_id,int status,int start,int end,SearchParam param);
	
	/*public List getBorrowList(long user_id,int status,int start,int end,SearchParam param);
	public int getBorrowListCount(long user_id,int status,SearchParam param);
	*/
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public int getCollectionListCount(long user_id,int status,SearchParam param);
	
	
	public DetailCollection getCollection(long id);
	
	public void modifyCollectionBonus(int order, double apr,List tlist);
	
	public List getCollectionLlistByBorrow(long bid,int start,int end,SearchParam param);
	
	public List getCollectionLlistByBorrow(long bid);
	
	public int getCollectionCountByBorrow(long bid,SearchParam param);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public double getCollectionInterestSum(long user_id, int status);
	public int getUnFinishFlowCount(SearchParam param,String nowTime);
	public List getUnFinishFlowList(SearchParam param,String nowTime, int start, int pernum);
	/**
	 *  到期未还款流转标，供导出
	 * @param param
	 * @return
	 */
	public List getUnFinishFlowList(SearchParam param);
	/**
	 * 根据tender查找collection列表
	 * @param tid
	 * @return
	 */
	public List getCollectionListByTender(long tid);
	/**
	 * 根据borrow_id查找collection列表
	 * @param tid
	 * @return
	 */
	public List getCollectionListByBorrow(long id);
	/**
	 * 批量修改collection
	 * @param collects
	 */
	public void modifyBatchCollection(List<Collection> collects);
	/**
	 * 根据borrow，order查找collection的类别
	 * @param bid
	 * @param order
	 * @return
	 */
	public List getCollectionLlistByBorrow(long bid,int order);
	/**
	 * 修改collection对象
	 * @param c
	 */
	public void modifyCollection(Collection c) ;
	
	/**
	 * 根据status查找所有的流转标记录
	 * @param status
	 * @return
	 *//*
	public List getAllFlowCollectList(int status);*/
	/**
	 * 根据status查找所有的流转标记录
	 * @param status
	 * @param aheadtime 还款时间提前
	 * @return
	 */
	public List getAllFlowCollectList(int status,int aheadtime);
	
	/**
	 * 根据status查找所有的流转标待还记录
	 * @param status
	 * @param aheadtime 还款时间提前
	 * @return
	 */
	public List<DetailCollection> getFlowRepayCollectList(int status,int aheadtime);
	
	public double getRepaymentAccount(int status);

	/**
	 * 根据用户id查询该用户已收总金额
	 * @param user_id
	 * @return
	 */
	double getAcceptSum(long user_id);
	
	/**
	 * 根据用户id查询该用户已收总金额
	 * @param user_id
	 * @return
	 */
	public List getAllDueColletion(Date date); 
	
	/**
	 * 查询某段时间内待收或已收的利息
	 * @param user_id
	 * @param status -1为查询所有类型的利息，0待收，1已收
	 * @param status_time 已收的开始时间
	 * @param end_time 已收的结束时间
	 * @return
	 */
	public double getCollectInterestSum(long user_id,int status , long start_time , long end_time);
	
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	 /**
//	  * 今日待收利息
//	  * @return
//	  */
//	 public double getTodayInterest();
//	 
//	 /**
//	  * 为投资人赚得利息
//	  * @return
//	  */
//	 public double getInterestTotal();
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 start
	 public int getFlowCount(SearchParam param,String nowTime,String type);
	 public List getFlowList(SearchParam param,String nowTime, int start, int pernum,String type);
		/**
		 *  到期未还款流转标，供导出
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
	/**
	 * 本金合计、本息合计、利息合计
	 * @param param param
	 * @param nowTime nowTime
	 * @param type type
	 * @return list
	 */
	CollectionSumModel getFlowSum(SearchParam param, String nowTime, String type);
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 end
	
	// v1.6.7.1 RDPROJECT-399 zza 2013-11-06 start
	/**
	 * 还款时奖励（未收）
	 * @param user_id 用户id
	 * @param status 状态
	 * @return 奖励
	 */
	double getRepayAward(long user_id, int status);
	// v1.6.7.1 RDPROJECT-399 zza 2013-11-06 end
	
	// v1.6.7.2 理财宝 zhangyz 2013-12-23 start
	/**
	 * 查询用户的待收金额
	 * @param map
	 * @return
	 */
	public double getUserWaitMoney(Map<String,Object> map);
	// v1.6.7.2 理财宝 zhangyz 2013-12-23 end
	
	// v1.6.7.2 债权转让 zhangyz 2013-12-25 start
	/**
	 * 查询用户的待收列表
	 * @param map
	 * @return
	 */
	public List<Collection> getCollectionList(Map<String,Object> map);
	
	/**
	 * 查询用户的待收列表
	 * @param id
	 * @return
	 */
	public Collection getCollectionById(long id);
	// v1.6.7.2  债权转让 zhangyz 2013-12-25 end
	
}
