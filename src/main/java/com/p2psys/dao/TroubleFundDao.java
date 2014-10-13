package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.TroubleAwardRecord;
import com.p2psys.domain.TroubleDonateRecord;
import com.p2psys.domain.TroubleFundDonateRecord;

/**
 * 急难基金dao
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-7-26
 */
public interface TroubleFundDao extends BaseDao {
	/**
	 * 急难基金 添加捐赠记录
	 * @param t 急难基金捐赠记录实体
	 * @return
	 */
	public TroubleFundDonateRecord addTroubleFund(TroubleFundDonateRecord t);
	/**
	 * 急难基金功德榜
	 * @param t 急难基金捐赠记录实体
	 * @param trouble_apr 急难基金捐赠金额分配给基金的百分比
	 * @return
	 */
	public List getTroubleFund(TroubleFundDonateRecord t,double trouble_apr,int start,int end) ;
	/**
	 * 急难基金 添加奖励收支记录
	 * @param t 急难基金奖励收支实体
	 * @return
	 */
	public TroubleAwardRecord addTroubleAward(TroubleAwardRecord t);
	/**
	 * 急难基金 添加捐赠记录
	 * @param t 急难基金基金收支实体
	 * @return
	 */
	public TroubleDonateRecord addTroubleDonate(TroubleDonateRecord t);
	/**
	 * 急难基金 奖池列表
	 * @param status 0收入 1支出
	 * @return
	 */
	public List getTroubleAwardList(long status,int start,int end) ;
	/**
	 * 急难基金基金收支列表
	 * @param status 0收入 1支出
	 * @return
	 */
	public List getTroubleDonateList(long status,int start,int end);
	/**
	 * 急难基金 奖金池 金额统计
	 * @param status 0收入 1支出
	 * @return
	 */
	public double getTroubleAwardSum(long status);
	/**
	 * 急难基金 基金 收支金额统计
	 * @param status 0收入 1支出
	 * @return
	 */
	public double getTroubleDonateSum(long status);
	/**
	 * 奖励收支记录数量
	 */
	public int  getTroubleAwardCount(long status);
	/**
	 * 基金收支记录数量
	 */
	public int getTroubleDonateCount(long status);
	/**
	 * 捐赠记录数量
	 */
	public int getTroubleFundCount(TroubleFundDonateRecord t);
	/**
	 * 基金收支记录通过id查询
	 */
	public TroubleDonateRecord getTroubleDonateById(int id) ;
	/**
	 * 基金收支记录修改
	 */
	public TroubleDonateRecord updateTroubleDonate(TroubleDonateRecord t) ;
}
