package com.p2psys.report.service;

import java.util.List;

import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.report.model.ReportNewRegisterModel;
import com.p2psys.report.model.ReportUserModel;

/**
 * 用户相关统计
 
 * @version 1.0
 * @since 2013-11-13
 */
public interface ReportUserService {

	
	/**
	 * 用户相关数据统计（包括投标次数投标金额）
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getUserList(int page,SearchParam param);
	
	/**
	 * 用户相关数据统计（包括投标次数投标金额）
	 * @param param
	 * @return
	 */
	public List<ReportUserModel> getUserList(SearchParam param);
	
	/**
	 * 每月新会员注册统计报表
	 * @return
	 */
	public ReportNewRegisterModel getNewRegisterUserNum(long start_time , long end_time);
	
}
