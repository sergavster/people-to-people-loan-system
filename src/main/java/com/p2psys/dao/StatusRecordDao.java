package com.p2psys.dao;

import com.p2psys.domain.StatusRecord;

//v1.6.7.2 RDPROJECT-579 xx 2013-12-10 start
//新增
//v1.6.7.2 RDPROJECT-579 xx 2013-12-10 end
/**
 * 状态记录Dao
 
 * @version 1.0
 * @since 2013年12月10日 下午10:55:20
 */
public interface StatusRecordDao extends BaseDao {

	public long add(StatusRecord model);
}
