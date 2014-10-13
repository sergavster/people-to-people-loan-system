package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.UserLog;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

public interface UserLogService {
	
	/**
	 * 增加用户操作日志
	 * @param userLog
	 * @return
	 */
	public void addLog(UserLog userLog);
	
	/**
	 * 用户查询,根据查询条件得到所有符合的记录的和
	 * @param userId,param
	 * @return int 记录总数
	 */
	public int getLogCountByUserId(long userId,SearchParam param);
	
	/**
	 * 用户查询,根据查询条件得到所有符合的记录
	 * @param userId,start,end,param
	 * @return List 所有相关的记录 
	 */
	public List getLogListByUserId(long userId,int start,int end,SearchParam param);
	
	/**
	 * 管理员查询,根据查询条件得到所有符合的记录的和
	 * @param param
	 * @return int  记录总数
	 */
	public int getLogCountByParam(SearchParam param);
	
	/**
	 * 管理员查询,根据查询条件得到所有符合的记录
	 * @param start,end,param
	 * @return List 所有相关的记录 
	 */
	public List getLogListByParams(int start,int end,SearchParam param);
	
	
	public PageDataList getUserLogList(int currentPage,SearchParam param);

}
