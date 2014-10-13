package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.AccountSumModel;
import com.p2psys.model.SearchParam;

/**
 * 
 * AccountSum表的相关操作
 * 
 * @version 1.0
 * @since 2013-8-26
 */
public interface AccountSumDao extends BaseDao {

	/**
	 * 根据用户ID修改信息
	 * @param type
	 * @param value
	 * @param user_id
	 */
	public void editSumByProperty(String type,double value,long user_id);
	
	/**
	 * 添加信息
	 * @param accountSum
	 */
	public void addAccountSum(AccountSum accountSum);
	
	/**
	 * 根据用户ID查询信息
	 * @param user_id
	 * @return
	 */
	public AccountSum getAccountSum(long user_id);
	
	/**
	 * 资金合计分页汇总
	 * @param p
	 * @return
	 */
	public int getAccountSumCount(SearchParam p);
	
	/** 根据搜索条件,获取资金合计列表
	 * @param page,max,SearchParam
	 * @return list
	 */
	
	public List getAccountSum(int page, int max, SearchParam p);
	
	/**
	 * 资金合计日志分页汇总
	 * @param p
	 * @return
	 */
	public int getAccountSumLogCount(SearchParam p,long user_id);
	
	/** 根据搜索条件,获取资金合计日志列表
	 * @param page,max,SearchParam
	 * @return list
	 */
	
	public List<AccountSumLog> getAccountSumLogPage(int page, int max, long user_id ,SearchParam p);
	
	// v1.6.7.1 用户资金统计加导出 zza 2013-11-26 start
	/** 根据搜索条件,导出资金合计日志
	 * @param p p
	 * @return list
	 */
	List<AccountSumModel> getAccountSum(SearchParam p);
	// v1.6.7.1 用户资金统计加导出 zza 2013-11-26 end
}
