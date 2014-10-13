package com.p2psys.dao;

import com.p2psys.domain.AccountSumLog;

/**
 * 用户资金合计日志
 
 * @version 1.0
 * @since 2013-9-5
 */
public interface AccountSumLogDao extends BaseDao {

	/**
	 * 添加用户资金合计日志
	 * @param accountSumLog
	 */
	public void addAccountSumLog(AccountSumLog accountSumLog);
	
}
