package com.p2psys.report.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.report.dao.ReportUserDao;
import com.p2psys.report.model.ReportNewRegisterModel;
import com.p2psys.report.model.ReportUserModel;
import com.p2psys.report.service.ReportUserService;
import com.p2psys.tool.Page;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;

/**
 * 用户相关统计
 
 * @version 1.0
 * @since 2013-11-13
 */
@Service
public class ReportUserServiceImpl implements ReportUserService {

	@Resource
	private ReportUserDao reportUserDao;
	
	@Override
	public PageDataList getUserList(int page,SearchParam param) {
		int total=reportUserDao.getUserCount(param);
		Page p = new Page(total, page);
		List<ReportUserModel> list=reportUserDao.getUserList(p.getStart(),p.getPernum(),param);
		return new PageDataList(p,list);
	}
	
	public List<ReportUserModel> getUserList(SearchParam param) {
		int total=reportUserDao.getUserCount(param);
		List<ReportUserModel> list=reportUserDao.getUserList(0,total,param);
		return list;
	}
	
	/**
	 * 每月新会员注册统计报表
	 * @return
	 */
	public ReportNewRegisterModel getNewRegisterUserNum(long start_time , long end_time){
		
		ReportNewRegisterModel model = new ReportNewRegisterModel();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		
		// 新注册用户统计
		int newUserNum = reportUserDao.getMonthNewRegister(map);
		model.setRegister_num(newUserNum);
		
		// 本月新注册用户实名认证统计
		map.put("real_status", 1);
		int realUserNum = reportUserDao.getMonthNewRegister(map);
		model.setReal_num(realUserNum);
		
		// 本月新注册用户手机认证统计
		map.remove("real_status");
		map.put("phone_status", 1);
		int phoneUserNum = reportUserDao.getMonthNewRegister(map);
		model.setPhone_num(phoneUserNum);
		
		// 本月新注册用户手机认证统计
		map.remove("phone_status");
		map.put("vip_status", 1);
		int vipUserNum = reportUserDao.getMonthNewRegister(map);
		model.setVip_num(vipUserNum);
		
		// 本月新注册用户投标统计
		map.remove("vip_status");
		int tenderUserNum = reportUserDao.getNewRegisterTender(map);
		model.setTender_num(tenderUserNum);
		
		//v1.6.7.1RDPROJECT-510 cx 2013-12-05 start
		//邮箱认证
		map.put("email_num", 1);
		int emailUserNum=reportUserDao.getMonthNewRegister(map);
		model.setEmail_num(emailUserNum);
		map.remove("email_num");
		//v1.6.7.1RDPROJECT-510 cx 2013-12-05 end
		
		String beforeTime = DateUtils.getTimeStr(DateUtils.rollMon(new Date(start_time * 1000),-1));
		if(NumberUtils.getLong(beforeTime) > 0) {
			Date beforeDay =new Date(NumberUtils.getLong(beforeTime) * 1000);
			Date beforeStartDay = DateUtils.currMonthFirstDay(beforeDay);
			Date beforeEndDay = DateUtils.currMonthLastDay(beforeDay);
			map.put("start_time", beforeStartDay.getTime() / 1000);
			map.put("end_time", beforeEndDay.getTime() / 1000);
			int beforeRegisterUserNum = reportUserDao.getMonthNewRegister(map);
			model.setBefore_register_num(beforeRegisterUserNum);
		}
		return model;
	}
	
}
