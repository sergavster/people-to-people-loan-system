package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.AccountSum;
import com.p2psys.model.AccountSumModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

/**
 * 用户资金合计信息处理
 
 * @version 1.0
 * @since 2013-9-6
 */
public interface AccountSumService {

	/**
	 * 添加信息
	 * @param accountSum
	 */
	public void addAccountSum(AccountSum accountSum);
	
	/**
	 * 用户资金合计查询分页.
	 * @param page 分页信息
	 * @param param 查询条件分装类
	 * @return page
	 */
	public PageDataList getAccountSumPage(int page , SearchParam param);
	
	/**
	 * 用户资金合计日志查询分页.
	 * @param page 分页信息
	 * @param param 查询条件分装类
	 * @return page
	 */
	public PageDataList getAccountSumLogPage(int page, long user_id , SearchParam param);
	
	// v1.6.7.1 用户资金统计加导出 zza 2013-11-26 start
	/**
	 * 用户资金合计日志导出
	 * @param param 查询条件分装类
	 * @return list
	 */
	List<AccountSumModel> getAccountSum(SearchParam param);
	// v1.6.7.1 用户资金统计加导出 zza 2013-11-26 end
	
}
