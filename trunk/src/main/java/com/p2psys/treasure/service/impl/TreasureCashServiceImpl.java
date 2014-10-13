package com.p2psys.treasure.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.p2psys.common.enums.EnumTreasureCashStatus;
import com.p2psys.common.enums.EnumTreasureRechStatus;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.domain.Account;
import com.p2psys.model.PageDataList;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.treasure.TreasureInterestLog;
import com.p2psys.model.accountlog.treasure.TreasureManageFee;
import com.p2psys.model.accountlog.treasure.TreasureRepaidLog;
import com.p2psys.model.accountlog.treasure.TreasureUnfreezeLog;
import com.p2psys.tool.Page;
import com.p2psys.treasure.dao.TreasureCashDao;
import com.p2psys.treasure.dao.TreasureDao;
import com.p2psys.treasure.dao.TreasureRechargeDao;
import com.p2psys.treasure.dao.TreasureUserDao;
import com.p2psys.treasure.domain.Treasure;
import com.p2psys.treasure.domain.TreasureCash;
import com.p2psys.treasure.domain.TreasureRecharge;
import com.p2psys.treasure.model.TreasureCashModel;
import com.p2psys.treasure.service.TreasureCashService;
import com.p2psys.util.BigDecimalUtil;
import com.p2psys.util.DateUtils;

@Service
public class TreasureCashServiceImpl implements TreasureCashService {

	@Resource
	private TreasureCashDao treasureCashDao;
	
	@Resource
	private TreasureDao treasureDao;
	
	@Resource
	private TreasureUserDao treasureUserDao;
	
	@Resource
	private TreasureRechargeDao treasureRechargeDao;
	
	@Resource
	private AccountDao accountDao;
	
	@Override
	public PageDataList getTreasureCashPage(int page,Map<String, Object> map) {
		if(map == null) map = new HashMap<String, Object>();
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(treasureCashDao.getTreasureCashCount(map) , page );
		pageDataList.setList(treasureCashDao.getTreasureCashPage(pages.getStart(), pages.getPernum() , map));
		pageDataList.setPage(pages);
		return pageDataList;
	}

	/**
	 * 资金转出
	 * @param recharge_id 转入资金ID
	 * @param user_id 会员ID
	 * @param treasure_id 理财宝信息ID
	 * @return
	 */
	public synchronized boolean  treasureCash(long treasure_id , long recharge_id , long user_id){
		
		Treasure treasure = treasureDao.getTreasureById(treasure_id);
		byte back_verify_type = treasure.getBack_verify_type();
		
		Account act=accountDao.getAccount(user_id);
		// 如果理财宝信息和用户账户为空，则返回
		if(treasure == null || act == null) return false;
		//投资份额赎回并且用户自动赎回
		if(treasure.getBack_type() != 0 || treasure.getStyle() != 0) return false;
		
		TreasureRecharge recharge = treasureRechargeDao.getTreasureRecharge(recharge_id);
		//如果转入信息为空,或者信息状态不为审核通过或转出失败，则返回
		byte pass_audit = EnumTreasureRechStatus.PASS_AUDIT.getValue();
		byte back_fail = EnumTreasureRechStatus.BACK_FAIL.getValue();
		boolean result = false;
		if(recharge != null && (recharge.getStatus() == pass_audit || recharge.getStatus() == back_fail )) result = true;
		if(!result) return false;
		long interestStartTime = recharge.getInterest_start_time();
		long nowTime = System.currentTimeMillis() / 1000;
		//如果现在时间小于添加时间，则返回
		if(nowTime < interestStartTime) return false;
		double money = recharge.getUse_money();
		if(money > 0){
			//当前时间处理
			Date nowStartTime_ = DateUtils.getDayStartTime(nowTime);
			Date nowEndTime_ = DateUtils.getDayEndTime(nowTime);
			long nowStartTime = nowStartTime_.getTime() / 1000;
			long nowEndTime = nowEndTime_.getTime() / 1000;
			//添加时间处理
			Date addEndTime_ = DateUtils.getDayEndTime(interestStartTime);//获取添加时间的当天结束时间
			long addEndTime = addEndTime_.getTime() / 1000;
			long minusTime = addEndTime - interestStartTime;
			
			//计算利息天数计算（已24点为分隔，过24点算一天，不过24点，不算一天）
			long day = nowStartTime - interestStartTime - minusTime;
			day = day / 24 / 60 / 60;
			if(addEndTime != nowEndTime ) day = day + 1;
			
			//如果赎回限制时间大于零，并且对间隔时间控制（过了当天24时算一天）
			double checkCashDay = nowTime - interestStartTime - minusTime;
			checkCashDay = checkCashDay / 24 / 60 / 60;
			checkCashDay = checkCashDay + 1;
			//如果限制时间大于零，并且限制天数大于等于实际限制天数，则返回
			if(treasure.getBack_time() > 0 && checkCashDay <= treasure.getBack_time()) return false;
			if(day < 0) return false;
			
			double interest = recharge.getApr()/Double.parseDouble("100") * money * Double.parseDouble(day+"");
			interest = BigDecimalUtil.round(interest, 2);//保存两位小数，四舍五入
			
			double manager_fee = interest * treasure.getManager_apr() / Double.parseDouble("100");
			manager_fee = BigDecimalUtil.round(manager_fee, 2);//保存两位小数，四舍五入
			
			//转出信息分装
			TreasureCash cash = new TreasureCash();
			cash.setAdd_time(nowTime);
			cash.setFee(manager_fee);
			cash.setInterest(interest);
			cash.setMoney(money);
			if(back_verify_type == 0){
				byte cash_pass_audit = EnumTreasureCashStatus.PASS_AUDIT.getValue();
				cash.setStatus(cash_pass_audit);
				treasureRechargeDao.editRechargeStatus(EnumTreasureRechStatus.BACK_SUCCESS.getValue(), recharge_id);
			}else{
				byte cash_wait_audit = EnumTreasureCashStatus.WAIT_AUDIT.getValue();
				cash.setStatus(cash_wait_audit);
				treasureRechargeDao.editRechargeStatus(EnumTreasureRechStatus.BACK_AUDIT.getValue(), recharge_id);
			}
			cash.setTreasure_id(treasure_id);
			cash.setUse_money(money);
			cash.setUser_id(user_id);
			cash.setTreasure_recharge_id(recharge.getId());
			treasureCashDao.addTreasureCash(cash);
			
			Map<String , Object> rehargeMap = new HashMap<String, Object>();
			rehargeMap.put("id", recharge_id);
			rehargeMap.put("interest", interest);
			rehargeMap.put("fee", manager_fee);
			double use_interest = interest - manager_fee;
			rehargeMap.put("use_interest", use_interest);
			rehargeMap.put("tender_day", day);
			rehargeMap.put("interest_end_time", nowStartTime);
			rehargeMap.put("update_time", nowTime);
			treasureRechargeDao.editRecharge(rehargeMap);
			
			if(treasure.getBack_verify_type() == 0){//赎回无需审核
				
				//投资人资金动向，1本金解冻，2获得利息，3扣除利息管理费,4投资理财宝账户总额加利息，可用减去本金，利息合金加上利息
				accountDao.updateAccount(0, money, -money, 0, user_id);
				act=accountDao.getAccount(user_id);
				Global.setTransfer("money", money);
				Global.setTransfer("item", treasure);
				BaseAccountLog log=new TreasureUnfreezeLog(money, act, treasure.getUser_id());
				log.doEvent();
				
				//投资人资金动向，获得利息处理
				accountDao.updateAccount(interest, interest, 0, 0, user_id);
				act=accountDao.getAccount(user_id);
				Global.setTransfer("money", interest);
				BaseAccountLog interestLog=new TreasureInterestLog(interest, act, treasure.getUser_id());
				interestLog.doEvent();
				
				//投资人资金动向，扣除利息管理费处理
				accountDao.updateAccount(-manager_fee, -manager_fee, 0, 0, user_id);
				act=accountDao.getAccount(user_id);
				Global.setTransfer("money", manager_fee);
				BaseAccountLog manageFeeLog=new TreasureManageFee(manager_fee, act, treasure.getUser_id());
				manageFeeLog.doEvent();
				//投资理财宝账户总额加利息，利息合计加上利息，可用减去本金
				treasureUserDao.editTreasureUser(interest, interest, -money, user_id);
				//投资人投资分额可用减掉
				treasureRechargeDao.editRechargeUserMoney(-money, recharge_id);
				//赎回时候，理财宝的投资金额减少
				treasureDao.editTreasureInvest(-money,treasure_id);
				
				//借款人资金动向，1可用减去本金和利息,2综合减去本金和利息
				double money_ =interest+money;
				accountDao.updateAccount(-money_, -money_, 0, 0, treasure.getUser_id());
				Account toAct=accountDao.getAccount(treasure.getUser_id());
				Global.setTransfer("money", money_);
				BaseAccountLog repaidLog = new TreasureRepaidLog(money_, toAct, treasure.getUser_id());
				repaidLog.doEvent();
			}
		}
		return true;
	}
	
	/**
	 * 查询转出信息
	 * @param id
	 * @return
	 */
	public TreasureCashModel getTreasureCash(long id){
		return treasureCashDao.getTreasureCash(id);
	}
	
	/**
	 * 理财宝资金转出审核
	 * @param cash
	 * @return
	 */
	public boolean auditCash(TreasureCash cash){
		
		if(cash == null || cash.getStatus() == EnumTreasureCashStatus.WAIT_AUDIT.getValue() ) return false;
		TreasureCashModel model = treasureCashDao.getTreasureCash(cash.getId());
		if(model == null || model.getStatus() != EnumTreasureCashStatus.WAIT_AUDIT.getValue()) return false;
		
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("status", cash.getStatus());
		map.put("verify_time", cash.getVerify_time());
		map.put("verify_user_id", cash.getVerify_user_id());
		map.put("remark", cash.getRemark());
		map.put("verify_user", cash.getVerify_user());
		map.put("status_",  EnumTreasureCashStatus.WAIT_AUDIT.getValue());
		map.put("id",  cash.getId());
		
		long recharge_id = model.getTreasure_recharge_id();
		long treasure_id = model.getTreasure_id();
		
		if(cash.getStatus() == EnumTreasureCashStatus.NOT_PASS_AUDIT.getValue()){
			treasureRechargeDao.editRechargeStatus(EnumTreasureRechStatus.BACK_FAIL.getValue(), recharge_id);
			treasureCashDao.eidtTreasureCash(map);
		}else if(cash.getStatus() == EnumTreasureCashStatus.PASS_AUDIT.getValue()){
			long user_id = model.getUser_id();
			Account act=accountDao.getAccount(user_id);
			Treasure treasure = treasureDao.getTreasureById(treasure_id);
			double money = model.getUse_money();
			double interest = model.getInterest();
			double manager_fee = interest * treasure.getManager_apr() / Double.parseDouble("100");
			manager_fee = BigDecimalUtil.round(manager_fee, 2);//保存两位小数，四舍五入
			
			//投资人资金动向，1本金解冻，2获得利息，3扣除利息管理费,4投资理财宝账户总额加利息，可用减去本金，利息合金加上利息
			accountDao.updateAccount(0, money, -money, 0, user_id);
			act=accountDao.getAccount(user_id);
			Global.setTransfer("money", money);
			Global.setTransfer("item", treasure);
			BaseAccountLog log=new TreasureUnfreezeLog(money, act, treasure.getUser_id());
			log.doEvent();
			
			//投资人资金动向，获得利息处理
			accountDao.updateAccount(interest, interest, 0, 0, user_id);
			act=accountDao.getAccount(user_id);
			Global.setTransfer("money", interest);
			BaseAccountLog interestLog=new TreasureInterestLog(interest, act, treasure.getUser_id());
			interestLog.doEvent();
			
			//投资人资金动向，扣除利息管理费处理
			accountDao.updateAccount(-manager_fee, -manager_fee, 0, 0, user_id);
			act=accountDao.getAccount(user_id);
			Global.setTransfer("money", manager_fee);
			BaseAccountLog manageFeeLog=new TreasureManageFee(manager_fee, act, treasure.getUser_id());
			manageFeeLog.doEvent();
			//投资理财宝账户总额加利息，利息合计加上利息，可用减去本金
			treasureUserDao.editTreasureUser(interest, interest, -money, user_id);
			//投资人投资分额可用减掉
			treasureRechargeDao.editRechargeUserMoney(-money, recharge_id);
			//赎回时候，理财宝的投资金额减少
			treasureDao.editTreasureInvest(-money,treasure_id);
			
			//借款人资金动向，1可用减去本金和利息,2综合减去本金和利息
			double money_ =interest+money;
			accountDao.updateAccount(-money_, -money_, 0, 0, treasure.getUser_id());
			Account toAct=accountDao.getAccount(treasure.getUser_id());
			Global.setTransfer("money", money_);
			BaseAccountLog repaidLog = new TreasureRepaidLog(money_, toAct, treasure.getUser_id());
			repaidLog.doEvent();
			
			treasureRechargeDao.editRechargeStatus(EnumTreasureRechStatus.BACK_SUCCESS.getValue(), recharge_id);
			treasureCashDao.eidtTreasureCash(map);
		}
		return true;
	}
}
