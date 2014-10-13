package com.p2psys.dao;
//v1.6.7.2 RDPROJECT-547 lx 2013-12-6 start
//新增类
//v1.6.7.2 RDPROJECT-547 lx 2013-12-6 end
import java.util.List;

import com.p2psys.model.RewardRecordModel;
import com.p2psys.model.SearchParam;

/**
 * 奖励记录Dao接口
 
 * @version 1.0
 * @since 2013年12月8日
 */
public interface RewardRecordDao extends BaseDao {
	/**
	 * 添加信息
	 * @param rewardRecord
	 */
	public void save(byte type, long fk_id, double account, long rewardUserId, long pasiveUserId);
	/**
	 * 取得总数
	 * @param param param
	 * @return int
	 */
	public int getCount(SearchParam param) ;
	/**
	 * 查出符合条件的奖励记录信息
	 * @param param param
	 * @param start start
	 * @param end end
	 * @return List
	 */
	public List<RewardRecordModel> getRewardRecordList(SearchParam param, int start, int pernum) ;
}
