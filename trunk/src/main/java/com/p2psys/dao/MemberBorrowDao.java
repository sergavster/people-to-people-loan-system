package com.p2psys.dao;

import java.util.List;

import com.p2psys.model.SearchParam;
import com.p2psys.model.UserBorrowModel;

public interface MemberBorrowDao extends BaseDao {

	public List getBorrowList(String type, long user_id, int start, int end,
			SearchParam param);

	public int getBorrowCount(String type, long user_id, SearchParam param);

	/**
	 * 新增方法：根据条件查询用户的借还款情况
	 * 修改日期：2013-3-21
	 * @param type
	 * @return
	 */
	// 标详情页面性能优化 lhm 2013/10/11 start
//	public List getRepamentList(String type, long user_id);
	public long getRepamentCount(String type, long user_id);
	// 标详情页面性能优化 lhm 2013/10/11 end
	
	// v1.6.7.2 RDPROJECT-471 zza 2013-11-29 start
	/**
	 * 审核不通过的借款标列表
	 * @param user_id
	 * @param start
	 * @param end
	 * @param param
	 * @param type
	 * @return
	 */
	public List<UserBorrowModel> getVerifyFailList(long user_id, int start, int end, SearchParam param, String type);
	
	/**
	 * 审核不通过的借款列表个数
	 * @param user_id
	 * @param param
	 * @param type
	 * @return
	 */
	public int getVerifyFailCount(long user_id, SearchParam param, String type);
	
	// v1.6.7.2 RDPROJECT-471 zza 2013-11-29 end

}
