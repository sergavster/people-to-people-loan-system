package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.CreditRank;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.UserCredit;
import com.p2psys.domain.UserCreditLog;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCreditLogModel;
import com.p2psys.model.UserCreditModel;

public interface UserCreditDao extends BaseDao {

	public void addUserCredit(UserCredit uc);

	
	/**
	 
	 * @param Page
	 * @param Max
	 * @return
	 * 
	 */
	public List getUserCreditByPageNumber(int Page, int Max);
	
	public void updateUserCredit(UserCredit userCredit); 
	
	public void updateCreditTenderValue(UserCredit userCredit); 
	
	public UserCredit getUserCreditByUserId(long user_id);
	
	public int getCreditValueByUserId(long user_id);
	
	/** 
	 * @param SearchParam
	 * @return int
	 */
	
	public int getUserCreditCount(SearchParam p);
	
	/** 根据搜索条件,获取用户积分列表
	 * @param page,max,SearchParam
	 * @return list
	 */
	
	public List getUserCredit(int page, int max, SearchParam p);
	
	// v1.6.7.2 RDPROJECT-509 zza 2013-12-12 start
	/**
	 * 积分导出
	 * @param p
	 * @return
	 */
	public List<UserCreditModel> getUserCredit(SearchParam p);
	// v1.6.7.2 RDPROJECT-509 zza 2013-12-12 end
	
	public void addUserCreditLog(UserCreditLog userCreditLog);
	
	public int getCreditLogCount(SearchParam p);
	
	public List getCreditLog(int page, int max,SearchParam p);
	
	public UserCreditLogModel getCreditLogByUserId(long user_id);
	
	public UserCreditModel getCreditModelById(long user_id);
	
	/**
	 * 根据积分类型代码查询积分类型
	 * @param nid 积分类型代码
	 * @return 积分类型CreditType
	 */
	public CreditType getCreditTypeByNid(String nid);
	
	/**
	 * 根据积分类型代码查询积分类型
	 * @param id 积分类型主键ID
	 * @return 积分类型CreditType
	 */
	public CreditType getCreditTypeById(long id);
	
	/**
	 * 根据积分种类查询积分类型
	 * @param credit_category 积分种类
	 * @return 积分类型 List
	 */
	public List<CreditType> getCreditTypeList(String credit_category);
	
	/**
	 * 根据user_id修改有效积分（加减）。
	 * @param user_id
	 * @param value
	 */
	public void editCreditValue(long user_id , int value); 
	
	/**
	 * 根据user_id修改投资积分（加减）。
	 * @param user_id
	 * @param value
	 */
	public void editTenderValue(long user_id , int value , int total_value); 
	
	/**
	 * 根据user_id修改借款积分（加减）。
	 * @param user_id
	 * @param value
	 */
	public void editBorrowValue(long user_id , int value); 
	
	/**
	 * 根据user_id修改赠送积分（加减）。
	 * @param user_id
	 * @param value 赠送积分
	 * @param total_value 综合积分
	 * @param valid_value 有效积分
	 */
	public void editGiftValue(long user_id , int value , int total_value , int valid_value); 
	
	/**
	 * 根据user_id修改消费积分（加减）。
	 * @param user_id
	 * @param value
	 */
	public void editExpenseValue(long user_id , int value); 
	
	/**
	 * 根据user id 查询该用户拥有哪些积分类型
	 * @param user_id
	 * @return
	 */
	public List<CreditType> getCreditTypeByUserId(long user_id);
	
	/**
	 * 根基条件查询积分记录信息
	 * @param p
	 * @param type_id 积分类型ID
	 * @param user_id 会员ID
	 * @return
	 */
	public int getUserCreditLogCount(SearchParam p,long type_id);
	
	/**
	 * 根基条件查询积分记录信息
	 * @param p
	 * @param type_id 积分类型ID
	 * @param user_id 会员ID
	 * @return
	 */
	public List getCreditLogPage(int page, int max,SearchParam p , long type_id);
	
	/**
	 * 查询所有的积分类型
	 * @param user_id
	 * @return
	 */
	public List<CreditType> getCreditTypeAll();
	
	/**
	 * 查询积分记录信息
	 * @param user_id
	 * @param type_id积分类型表主键ID
	 * @return
	 */
	public List getCreditLogList(long user_id , long type_id);
	
	/**
	 * 查询有效积分
	 * @param user_id
	 * @return 有效积分
	 */
	public int getValidValueByUserId(long user_id);
	
	public List<CreditRank> getCreditRankList();
	
	/**
	 * 后台修改会员积分
	 * @param value总积分变动值
	 * @param valid_value有效积分变动值
	 * @param expense_value消费积分变动值
	 * @param user_id 会员ID
	 */
	public void updateCredit(long value , long valid_value , long expense_value , long user_id);
	
	/**
	 * 查询所有的会员积分
	 * @return
	 */
	public List<UserCredit> getCreditAll();
	
	/**
	 * 更具条件查询一段时间内的积分总和
	 * @param start_time
	 * @param end_time
	 * @param user_id
	 * @param type_id
	 * @return
	 */
	public int getCreditLogCount(long start_time , long end_time , long user_id , long type_id);
	
	/**
	 * 根据等级查询会员积分等级
	 * @param bank 等级
	 * @return
	 */
	public CreditRank getCreditRank(int rank);
	
	/**
	 * 根据会员ID修改会员积分等级
	 * @param bank 等级
	 * @param user_id
	 * @return
	 */
	public void updateCreditLevel(int rank , long user_id);
	
	/**
	 * 根据综合积分确定积分等级
	 * @param value 综合积分
	 * @return
	 */
	public CreditRank getCreditRankByValue(int value);
	
	/**
	 * 修改会员论坛积分
	 * @param value总积分变动值
	 * @param valid_value有效积分变动值
	 * @param bbs_value论坛积分变动值
	 * @param user_id 会员ID
	 */
	public void updateCreditBBS(long value , long valid_value , long bbs_value , long user_id);

	//v1.6.7.2 RDPROJECT-510 cx 2013-12-12 start
	/**
	 * 积分等级报表 
	 * @param param
	 * @return
	 */
	public List<UserCreditModel> getUserCreditModelList(SearchParam param);
	//v1.6.7.2 RDPROJECT-510 cx 2013-12-12 end
}
