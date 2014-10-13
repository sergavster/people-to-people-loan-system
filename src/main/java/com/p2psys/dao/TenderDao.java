package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Tender;
import com.p2psys.domain.TenderAwardYesAndNo;
import com.p2psys.model.BorrowTender;
import com.p2psys.model.RankModel;
import com.p2psys.model.SearchParam;

public interface TenderDao extends BaseDao {
	/**
	 * 根据borrowid获取Tender列表
	 * @param id
	 * @return
	 */
	public List<BorrowTender> getTenderListByBorrowid(long id);

	// v1.6.7.2 RDPROJECT-580 lhm 2013-12-11 start
//	/**
//	 *  根据borrowid获取Tender列表的前num条
//	 * @param id
//	 * @param num
//	 * @return
//	 */
//	
//	public List getTenderListByBorrowid(long id,int num);
	// v1.6.7.2 RDPROJECT-580 lhm 2013-12-11 
	
	public List getTenderListByBorrow(long id) ;
	
	public List getTenderListByBorrow(long id,long user_id) ;
	
	public List getTenderListByBorrowid(long id,int start,int pernum,SearchParam param);
	
	public int getTenderCountByBorrowid(long id,SearchParam param);
	
	public Tender addTender(Tender tender);
	
	public Tender modifyTender(Tender tender);
	
	public List getInvestTenderListByUserid(long user_id);
	
//	public List getInvestTenderingListByUserid(long user_id);
	
	public List getInvestTenderListByUserid(long user_id,int start,int end,SearchParam param);
	
	public int getInvestTenderCountByUserid(long user_id,SearchParam param);
	
	public List getInvestTenderingListByUserid(long user_id,int start,int end,SearchParam param);
	
	public int getInvestTenderingCountByUserid(long user_id,SearchParam param);
	
//	public List getSuccessTenderList(long user_id);
	
//	public List getSuccessTenderList(long user_id, int start, int end,SearchParam param);
	/**
	 * 统计某一时间段的投标金额合计
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public double countTenderAccount(String startTime,String endTime);
//	/**
//	 * 根据用户ID获取投标的总额
//	 * @param userid
//	 * @return
//	 */
//	public double getTotalTenderByUserid(long userid);
	
//	/**
//	 * 根据用户ID获取投标的次数
//	 * @param userid
//	 * @return
//	 */
//	public int getTotalTenderTimesByUserid(long userid);
	
	/**
	 * 分页
	 * @param param
	 * @return
	 */
	int getRankTotalByTime(SearchParam param);
	
	/**
	 * 根据时间取得投资总额
	 * @return
	 */
	public List<RankModel> getRankListByTime(int start, int pernum, SearchParam param);
	
	// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 start
	/**
	 * 首页投资排行榜
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param num 个数
	 * @param init 标种拼接的SQL
	 * @return list
	 */
	List<RankModel> getRankListByTime(String startTime, String endTime, int num, String init);
	// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 end
	
	public List<RankModel> getRankListByTime(SearchParam param);
	//及时雨获取更多排名
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
	public List getMoreRankListByTime(String startTime,String endTime,int num,String init);
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
	
	/**
	 * 国临创投投资排行榜
	 * @return
	 */
	// public List getRankList();
	/**
	 * 国临创投投资排行榜显示全部
	 * @return
	 */
	public List getAllRankList();
//	public List getTenderList();
	public List getNewTenderList();
	public List getAllTenderList(int start, int pernum, SearchParam param);
	public int getAllTenderCount(SearchParam param);
	public List getAllTenderList(SearchParam param);
	public List allTenderListBybid(long borrow_id);
	/**
	 * 根据ID获取tender对象
	 * @param tid
	 * @return
	 */
	public Tender getTenderById(long tid);
	/**
	 * 修改tender的待收本金和待收利息
	 * @param capital
	 * @param interest
	 * @param id
	 */
	public void modifyRepayTender(double capital,double interest,long id);
	//山水聚宝活动时间内累计投资总金额
	public double getTenderListByUserid(long user_id,String starttime,String endtime);
	//累计投资奖励是否启用修改
	public void updateTenderAwardYesAndNo(TenderAwardYesAndNo tenderAwardYesAndNo);
	public TenderAwardYesAndNo tenderAwardYesAndNo(int id);
	
	public int getAutoTenderByUserid(long userid,long is_auto_tender,long borrow_id,long status);
	/**
	 * 获取第一个未还款的Tender
	 * @param borrow_id
	 * @return
	 */
	public Tender getFirstNoRepayTenderByBorrow(long borrow_id);
	
	public List getProtocolTenderListByBorrowid(long id);
	
	//RDPROJECT-282 fxx 2013-10-18 start
	public double countTenderAccountByUserid(long userid,long start,long end,String extSql);
	//RDPROJECT-282 fxx 2013-10-18 end
	// v1.6.7.2 RDPROJECT-530 lx 2013-12-11 start
	/**
	 * 得到借出总额
	 * 
	 * @param 
	 *            user_id
	 * @return double
	 */
	public double sumTenderAccount(long user_id);
	// v1.6.7.2 RDPROJECT-530 lx 2013-12-11 end
	
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
	/**
	 * 得到投一个标的次数
	 * 
	 * @param borrow_id,user_id
	 *            
	 * @return int
	 */	
	public int getTenderTimes(long borrow_id,long user_id);
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
	// v1.6.7.2 RDPROJECT-529 lx 2013-12-17 start
	/**
	 * 修改Tender status
	 * @param borrow_id
	 */
	public void updateTenderStatusByBorrowId(long borrow_id,int status);
    // v1.6.7.2 RDPROJECT-529 lx 2013-12-17 end
}
