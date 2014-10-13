package com.p2psys.treasure.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.p2psys.common.enums.EnumTreasureRechStatus;
import com.p2psys.model.PageDataList;
import com.p2psys.tool.Page;
import com.p2psys.treasure.dao.TreasureDao;
import com.p2psys.treasure.dao.TreasureRechargeDao;
import com.p2psys.treasure.domain.Treasure;
import com.p2psys.treasure.model.TreasureRechargeModel;
import com.p2psys.treasure.service.TreasureRechargeService;
import com.p2psys.util.BigDecimalUtil;
import com.p2psys.util.DateUtils;

@Service
public class TreasureRechargeServiceImpl implements TreasureRechargeService {

	@Resource
	private TreasureRechargeDao treasureRechargeDao;
	@Resource
	private TreasureDao treasureDao;
	
	@Override
	public PageDataList getTreasureRechargePage(int page, Map<String, Object> map) {
		if(map == null) map = new HashMap<String, Object>();
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(treasureRechargeDao.getTreasureRechargeCount(map) , page );
		pageDataList.setList(treasureRechargeDao.getTreasureRechargePage(pages.getStart(), pages.getPernum() , map));
		pageDataList.setPage(pages);
		return pageDataList;
	}

	/**
	 * 理财宝资金转入信息分页
	 * @param map 查询参数
	 * @return
	 */
	public List<TreasureRechargeModel> getTreasureRechargeList(Map<String , Object> map){
		return treasureRechargeDao.getTreasureRechargeList(map);
	}
	
	/**
	 * 理财宝投资利息定时计算
	 * @return
	 */
	public boolean doTreasureInterest(){
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("status", EnumTreasureRechStatus.PASS_AUDIT.getValue());
		// 审核通过的理财宝投资信息利息计算
		List<TreasureRechargeModel> rechargeList = treasureRechargeDao.getTreasureRechargeList(map);
		map.put("status", EnumTreasureRechStatus.BACK_FAIL.getValue());
		List<TreasureRechargeModel> rechargeBackFailList = treasureRechargeDao.getTreasureRechargeList(map);
		for(TreasureRechargeModel model : rechargeBackFailList){
			rechargeList.add(model);
		}
		if(rechargeList != null){
			long nowTime = DateUtils.getNowTime();
			for(TreasureRechargeModel model : rechargeList) {
				long startTime = model.getInterest_start_time();
				if(nowTime >= startTime){
					//当前时间处理
					Date nowStartTime_ = DateUtils.getDayStartTime(nowTime);
					Date nowEndTime_ = DateUtils.getDayEndTime(nowTime);
					long nowStartTime = nowStartTime_.getTime() / 1000;
					long nowEndTime = nowEndTime_.getTime() / 1000;
					//添加时间处理
					Date addEndTime_ = DateUtils.getDayEndTime(startTime);//获取添加时间的当天结束时间
					long addEndTime = addEndTime_.getTime() / 1000;
					long minusTime = addEndTime - startTime;
					
					//计算利息天数计算（已24点为分隔，过24点算一天，不过24点，不算一天）
					long day = nowStartTime - startTime - minusTime;
					day = day / 24 / 60 / 60;
					if(addEndTime != nowEndTime ) day = day + 1;
					
					double money = model.getMoney();
					double interest = model.getApr()/Double.parseDouble("100") * money * Double.parseDouble(day+"");
					interest = BigDecimalUtil.round(interest, 2);//保存两位小数，四舍五入
					
					Treasure treasure = treasureDao.getTreasureById(model.getTreasure_id());
					double manager_fee = interest * treasure.getManager_apr() / Double.parseDouble("100");
					manager_fee = BigDecimalUtil.round(manager_fee, 2);//保存两位小数，四舍五入
					
					Map<String , Object> rehargeMap = new HashMap<String, Object>();
					rehargeMap.put("id", model.getId());
					rehargeMap.put("interest", interest);
					rehargeMap.put("fee", manager_fee);
					double use_interest = interest - manager_fee;
					rehargeMap.put("use_interest", use_interest);
					rehargeMap.put("tender_day", day);
					rehargeMap.put("interest_end_time", nowStartTime);
					rehargeMap.put("update_time", nowTime);
					treasureRechargeDao.editRecharge(rehargeMap);
				}
			}
		}
		return true;
	}
}
