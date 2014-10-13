package com.p2psys.service;

import com.p2psys.domain.TroubleAwardRecord;
import com.p2psys.domain.TroubleDonateRecord;
import com.p2psys.domain.TroubleFundDonateRecord;
import com.p2psys.model.PageDataList;

/**
 * 急难基金service
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-7-26
 */
public interface TroubleFundService {
	/**
	 * 急难基金 捐赠操作
	 * @param t  急难基金捐赠记录实体
	 * @param paypassword 会员交易密码
	 * @return 急难基金捐赠记录实体
	 */
	public TroubleFundDonateRecord troubleFund(TroubleFundDonateRecord t,String paypassword,double trouble_apr);
	/**
	 * 急难基金 捐赠记录 
	 * @param t   急难基金捐赠记录实体
	 * @param trouble_apr  捐赠转入基金比例
	 * @param page  分页页数
	 * @return  PageDataList
	 */
	public PageDataList getTroubleFundList(TroubleFundDonateRecord t,double trouble_apr,int page);
	/**
	 * 急难基金 基金操作 
	 * @param r 急难基金 基金收支实体
	 * @param trouble_apr   捐赠转入基金比例
	 * @return
	 */
	public TroubleDonateRecord troubleDonate(TroubleFundDonateRecord r,double trouble_apr);
	/**
	 * 统计基金、奖励收支金额
	 * @param type
	 * @return
	 */
	public double getTroubleSum(long type);
	/**
	 * 添加基金收支操作
	 * @param t
	 * @return
	 */
	public TroubleDonateRecord addTroubleDonate(TroubleDonateRecord t);

	/**
	 * 奖励收支操作
	 * @param r
	 * @param trouble_apr
	 * @param status
	 * @param award_money
	 * @return
	 */
	public TroubleAwardRecord troubleAward(TroubleFundDonateRecord r,double trouble_apr,long status,double award_money);
    /**
     * 基金收支列表
     */
	public PageDataList getTroubleDonateList(long status,int page);
	/**
	 * 基金收支通过id查询
	 */
	public TroubleDonateRecord getTroubleDonateById(int id);
	/**
	 * 基金收支记录修改
	 */
	public TroubleDonateRecord updateTroubleDonate(TroubleDonateRecord t) ;
}
