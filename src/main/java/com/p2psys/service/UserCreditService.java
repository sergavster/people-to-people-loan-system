package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.CreditConvert;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCredit;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UpfilesExcelModel;
import com.p2psys.model.UserCreditModel;

public interface UserCreditService {

	/**
	 * 会员积分查询分页.
	 * @param pageNo 当前页
	 * @param param 查询条件分装类
	 * @param pageSize 每页显示条数，参数为空，默认显示10条 
	 * @return page
	 */
	public PageDataList getUserCreditPage(int pageNo , int pageSize , SearchParam param);

	/**
	 * 根据会员ID修改会员有效积分
	 * @param user_id 会员ID 必传参数
	 * @param value 操作积分，为正数，可选参数，不传，则根据NID类型操作积分
	 * @param status 积分操作状态：1加分加，2积分减 ，可选参数
	 * @param nid 积分类型(dw_credit_type)必传参数
	 * 注：value 和 status可选参数，两者同时传或同时不传
	 */
	public Boolean updateUserCredit(long user_id , int value , byte status , String nid );

	/**
	 * 根据会员ID查询会员的积分信息
	 * @param user_id 会员ID
	 * @return UserCredit
	 */
	public UserCredit getUserCreditByUserId(long user_id);

	/**
	 * 根据会员ID查询该会员的积分
	 * @param user_id 会员ID
	 * @param type 查询积分类型：1投标积分,2借款积分,3赠送积分,4消费积分,5可用积分,6总积分
	 * @return 积分
	 */
	public int getCreditValueByUserId(long user_id , Byte type);
	
	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
	/**
	 * 会员操作记录查询 
	 * @param page
	 * @param param 查询条件分装类
	 * @return page
	 */
	public PageDataList getCreditLogPage(int pageNo, SearchParam param, long type_id);
	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end
	
	/**
	 * 根据user id 查询该用户拥有哪些积分类型
	 * @param user_id
	 * @return
	 */
	public List<CreditType> getCreditTypeByUserId(long user_id);
	
	/**
	 * 查询所有的积分类型
	 * @param user_id
	 * @return
	 */
	public List<CreditType> getCreditTypeAll();
	
	/**
	 * 根据nid查询积分类型信息
	 * @param nid积分类型表nid
	 * @return
	 */
	public CreditType getCreditTypeByNid(String nid);

	/**
	 * 查询积分记录信息
	 * @param user_id
	 * @param type_id积分类型表主键ID
	 * @return
	 */
	public List getCreditLogList(long user_id , long type_id);
	
	/**
	 * 积分兑换方法
	 * @param creditConvert 兑换参数
	 */
	public boolean integralConvert(CreditConvert creditConvert);
	
	/**
	 * 用户积分修改
	 * @param userCredit
	 * @return
	 */
	public Boolean updateUserCredit(UserCredit userCredit);
	
	/**
	 * 根据会员综合积分和论坛积分设置会员积分等级
	 */
	public boolean doUpdateCreditLevel();
	
	/**
	 * 用户论坛积分修改
	 * @param userCredit
	 * @return
	 */
	public Boolean updateUserCreditBBS(UserCredit userCredit);
	
	/**
	 * 积分兑换VIP方法
	 * @param creditConvert 兑换参数
	 */
	public boolean integralVIP(CreditConvert creditConvert);
	
	// v1.6.7.2 RDPROJECT-509 zza 2013-12-11 start
	/**
	 * bbs积分导入
	 * @param data
	 * @return
	 */
	List<UpfilesExcelModel> addBatchRecharge(List[] data);

	/**
	 * 导入论坛积分
	 * @param xls
	 * @param verifyUser
	 * @param list
	 * @return
	 */
	boolean verifyBbsCreidtExcel(String xls, User verifyUser, List<UpfilesExcelModel> list);
	
	/**
	 * 积分导出
	 * @param p
	 * @return
	 */
	List<UserCreditModel> getUserCredit(SearchParam p);
	// v1.6.7.2 RDPROJECT-509 zza 2013-12-11 end

	//v1.6.7.2 RDPROJECT-510 cx 2013-12-12 start
	/**
	 * 积分等级报表 
	 * @param param
	 * @return
	 */
	public List<UserCreditModel> getUserCreditModelList(SearchParam param);
	//v1.6.7.2 RDPROJECT-510 cx 2013-12-12 end
	
}
