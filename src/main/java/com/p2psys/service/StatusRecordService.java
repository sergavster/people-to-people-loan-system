package com.p2psys.service;

import com.p2psys.domain.StatusRecord;

//v1.6.7.2 RDPROJECT-579 xx 2013-12-10 start
//新增
//v1.6.7.2 RDPROJECT-579 xx 2013-12-10 end
/**
 * 状态记录Service
 
 * @version 1.0
 * @since 2013年12月10日 下午10:57:19
 */
public interface StatusRecordService {

	public long add(StatusRecord model);
	
}
