package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.UserLog;
import com.p2psys.model.SearchParam;

public interface UserLogDao extends BaseDao {

	public void addUserLog(UserLog userLog);

	/**
	 * 用户查询操作日志
	 * @param userId,param
	 * @return int  记录总数
	 */
	public int getLogCountByUserId(long userId,SearchParam param);

	/**
	 * 用户查询操作日志
	 * @param userId,start,end,param
	 * @return List 所有相关的记录 
	 */
	public List getLogListByUserId(long userId,int start,int end,SearchParam param);
	
	/**
	 * 管理员查询操作日志
	 * @param param
	 * @return int  记录总数
	 */
	public int getLogCountByParam(SearchParam param);

	/**
	 * 管理员查询操作日志
	 * @param start,end,param
	 * @return List 所有相关的记录 
	 */
	public List getLogListByParams(int start,int end,SearchParam param);
}
