package com.p2psys.report.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.model.SearchParam;
import com.p2psys.report.model.ReportUserModel;

/**
 * 用户相关统计
 
 * @version 1.0
 * @since 2013-11-13
 */
public interface ReportUserDao {
	
	/**
	 * 用户相关数据统计（包括投标次数投标金额）
	 * @param p
	 * @return
	 */
	public int getUserCount(SearchParam p);
	
	/**
	 * 用户相关数据统计（包括投标次数投标金额）
	 * @param page
	 * @param pernum
	 * @param p
	 * @return
	 */
	public List<ReportUserModel> getUserList(int page, int pernum, SearchParam p);
	
	/**
	 * 查询用户本月新注册数和本月用户手机认证数等等
	 * @param map
	 * @return
	 */
	public int getMonthNewRegister(Map<String, Object> map);
	
	/**
	 * 查询本月新注册会员有多少人投标
	 * @param map
	 * @return
	 */
	public int getNewRegisterTender(Map<String, Object> map);
}
