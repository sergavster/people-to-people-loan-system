package com.p2psys.black.dao;

import java.util.List;

import com.p2psys.black.domain.LoanBlack;
import com.p2psys.dao.BaseDao;
import com.p2psys.model.AccountBackModel;
import com.p2psys.model.RewardRecordModel;
import com.p2psys.model.SearchParam;

/**
 * 黑名单Dao接口
 
 * @version 1.0
 * @since 2013年12月24日
 */
public interface LoanBlackDao extends BaseDao{
	/** 添加信息
	* @param lb
	*/
	public void saveLoanBlack(LoanBlack lb);
	/**
	 * 取得总数
	 * @param param param
	 * @return int
	 */
	public int getCount(SearchParam param) ;
	/**
	 * 查出符合条件的黑名单信息
	 * @param param param
	 * @param start start
	 * @param end end
	 * @return List
	 */
	public List<LoanBlack> getLoanBlackList(SearchParam param, int start, int pernum) ;
	/**
	 * 查询黑名单
	 * @param username
	 * @param identity
	 * @param mobile
	 * @param email
	 * @param qq
	 * @return
	 */
	public List<LoanBlack> getLoanBlack(String username, String identity,
			String mobile, String email, String qq);
	/**
	 * 是否存在该用户ID对应的黑名单信息
	 * @param user_id
	 * @return
	 */
	public LoanBlack getLoanBlackByFkId(long user_id);
	/**
	 * 修改黑名单信息
	 * @param lb
	 */
	public void updateLoanBlack(LoanBlack lb);
}
