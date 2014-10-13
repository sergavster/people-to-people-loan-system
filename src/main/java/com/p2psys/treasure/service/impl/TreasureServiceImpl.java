package com.p2psys.treasure.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.p2psys.common.enums.EnumTreasureAudit;
import com.p2psys.common.enums.EnumTreasureRechStatus;
import com.p2psys.common.enums.EnumTreasureStatus;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.CollectionDao;
import com.p2psys.domain.Account;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.treasure.TreasureFreezeLog;
import com.p2psys.model.accountlog.treasure.TreasureSuccessLog;
import com.p2psys.tool.Page;
import com.p2psys.treasure.dao.TreasureDao;
import com.p2psys.treasure.dao.TreasureRechargeDao;
import com.p2psys.treasure.dao.TreasureUserDao;
import com.p2psys.treasure.domain.Treasure;
import com.p2psys.treasure.domain.TreasureRecharge;
import com.p2psys.treasure.domain.TreasureUser;
import com.p2psys.treasure.model.TreasureModel;
import com.p2psys.treasure.service.TreasureService;
import com.p2psys.util.DateUtils;

/**
 * 理财宝信息Service接口实现类
 
 * @version 1.0
 * @since 2013-11-27
 */
@Service
public class TreasureServiceImpl implements TreasureService {
	
	@Resource
	private TreasureDao treasureDao;
	
	@Resource
	private TreasureUserDao treasureUserDao;
	
	@Resource
	private TreasureRechargeDao treasureRechargeDao;
	
	@Resource
	private AccountDao accountDao;
	
	@Resource
	private CollectionDao collectionDao;

	/**
	 * 理财宝信息分页
	 * @param page 分页起始页
	 * @param map 查询参数
	 * @return
	 */
	public PageDataList getTreasurePage(int page,Map<String , Object> map){
		if(map == null) map = new HashMap<String, Object>();
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(treasureDao.getTreasureCount(map) , page );
		pageDataList.setList(treasureDao.getTreasurePage(pages.getStart(), pages.getPernum() , map));
		pageDataList.setPage(pages);
		return pageDataList;
	}

	/**
	 * 根据主键ID查询理财宝信息
	 * @param id
	 * @return
	 */
	public TreasureModel getTreasureById(long id){
		if(id > 0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			List<TreasureModel> itemList = treasureDao.getTreasure(map);
			if(itemList != null && itemList.size() > 0)return itemList.get(0);
		}
		return null;
	}
	
	/**
	 * 根据主键ID查询理财宝信息
	 * @param id
	 * @return
	 */
	public Treasure getTreasureItemById(long id){
		return treasureDao.getTreasureById(id);
	}
	
	/**
	 * 修改理财宝信息
	 * @param model
	 * @return
	 */
	public boolean editTreasure(Treasure model){
		if(model == null || model.getId() <= 0) return false;
		Treasure item = treasureDao.getTreasureById(model.getId());
		if(item == null) return false;
		item.setName(model.getName());
		item.setAccount(model.getAccount());
		item.setContent(model.getContent());
		item.setMin_account(model.getMin_account());
		item.setUpdate_time(DateUtils.getNowTime());
		item.setMax_account(model.getMax_account());
		item.setMost_account(model.getMost_account());
		item.setApr(model.getApr());
		item.setManager_apr(model.getManager_apr());
		item.setRule_check(model.getRule_check());
		return treasureDao.editTreasure(item);
	}
	
	/**
	 * 添加理财宝信息
	 * @param model
	 * @return
	 */
	public long addTreasure(Treasure item){
		item.setAdd_time(System.currentTimeMillis() / 1000);
		item.setAudit_status(EnumTreasureAudit.WAIT_AUDIT.getValue());
		item.setStatus(EnumTreasureStatus.STOP.getValue());
		return treasureDao.addTreasure(item);
	}
	
	/**
	 * 审核信息理财宝信息
	 * @param item
	 * @return
	 */
	public boolean auditTreasure(Treasure item){
		Treasure item_ = treasureDao.getTreasureById(item.getId());
		if(item_ == null) return false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", item.getId());
		map.put("operator", item.getOperator());
		map.put("verify_remark", item.getVerify_remark());
		map.put("verify_user_id", item.getVerify_user_id());
		map.put("verify_time", System.currentTimeMillis() / 1000);
		map.put("update_time", System.currentTimeMillis() / 1000);
		map.put("audit_status_", EnumTreasureAudit.WAIT_AUDIT.getValue());
		map.put("audit_status", item.getAudit_status());
		return treasureDao.specialEdit(map);
	}
	
	/**
	 * 修改理财宝信息状态
	 * @param id 主键
	 * @param status 0停用/1启用
	 * @return
	 */
	public boolean editTreasureStatus(long id , byte status,String operator){
		Treasure item = treasureDao.getTreasureById(id);
		if(item == null) return false;
		byte stop = EnumTreasureStatus.STOP.getValue();
		byte start = EnumTreasureStatus.START.getValue();
		if( (status == stop || status == start ) && status != item.getStatus() ){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("operator", operator);
			map.put("update_time", System.currentTimeMillis() / 1000);
			map.put("audit_status_", EnumTreasureAudit.PASS_AUDIT.getValue());
			map.put("status", status);
			return treasureDao.specialEdit(map);
		}
		return false;
	}
	
	/**
	 * 理财宝投资
	 * @param item
	 * @return
	 */
	public synchronized boolean invest(TreasureRecharge item){
		Treasure treasure = treasureDao.getTreasureById(item.getTreasure_id());
		byte start = EnumTreasureStatus.START.getValue();
		byte pass = EnumTreasureAudit.PASS_AUDIT.getValue();
		double money = item.getMoney();
		//如果审核状态不通过，或者理财宝状态不启用，则返回
		if(treasure == null || treasure.getStatus() != start || treasure.getAudit_status() != pass) return false;
		// 如果投资额度小于最小投资额度，或者大于最大投资额度，则返回
		if( (treasure.getMin_account() > 0 && item.getMoney() < treasure.getMin_account() ) || (treasure.getMax_account() > 0 && item.getMoney() > treasure.getMax_account())) return false;
		
		double z = treasure.getAccount() - treasure.getInvest();// 此理财宝剩余多少金额
		if(treasure.getAccount() > 0 && treasure.getInvest() >= treasure.getAccount()) return false;
		// 如果投资额度大于可投资额度，须特殊处理
		if(treasure.getAccount() > 0 && z < money){
			money = z;
		}
		
		Map<String ,Object> map = new HashMap<String, Object>();
		List<Byte> status = new ArrayList<Byte>();
		status.add(EnumTreasureRechStatus.WAIT_AUDIT.getValue());
		status.add(EnumTreasureRechStatus.PASS_AUDIT.getValue());
		status.add(EnumTreasureRechStatus.BACK_AUDIT.getValue());
		status.add(EnumTreasureRechStatus.BACK_FAIL.getValue());
		map.put("status", status);
		map.put("user_id", item.getUser_id());
		map.put("treasure_id", item.getTreasure_id());
		double rechargeMoney = treasureRechargeDao.getRechargeMoney(map);
		if(treasure.getMost_account() > 0 && treasure.getMost_account() < rechargeMoney) return false;
		
		//发布人不能投资自己发布的理财宝信息
		if(treasure.getUser_id() ==  item.getUser_id()) return false;
		
		Account act=accountDao.getAccount(item.getUser_id());
		if(act == null || act.getUse_money() < money || money <= 0) return false;
		Account toAct=accountDao.getAccount(treasure.getUser_id());
		if(toAct == null) return false;
		
		boolean result = investCheck(item.getTreasure_id(),item.getUser_id() ,money,rechargeMoney);
		if(!result) return false;
		long nowTime = DateUtils.getNowTime();
		item.setMoney(money);
		item.setUse_money(money);
		item.setApr(treasure.getApr());
		item.setAdd_time(nowTime);
		item.setUpdate_time(nowTime);
		item.setStatus(Byte.parseByte("1"));
		int interest_time = treasure.getInterest_time();
		if(interest_time == 0){//生息间隔时间 ,为零，代表即时生息
			item.setInterest_start_time(nowTime);
		}else{
			//当前时间处理
			Date nowEndTime_ = DateUtils.getDayEndTime(nowTime);
			long interestStartTime = nowEndTime_.getTime() / 1000 + 1;
			interest_time = interest_time -1;
			long afterDay = interest_time * 24 * 60 * 60 ;
			interestStartTime = interestStartTime + afterDay;
			item.setInterest_start_time(interestStartTime);
		}
		
		TreasureUser treasureUser = treasureUserDao.getTreasureUserByUserId(item.getUser_id());
		if(treasureUser == null) return false;
		accountDao.updateAccount(0, -money, money, 0, item.getUser_id());
		act=accountDao.getAccount(item.getUser_id());
		Global.setTransfer("money", money);
		Global.setTransfer("item", treasure);
		BaseAccountLog log=new TreasureFreezeLog(money, act, treasure.getUser_id());
		log.doEvent();
		treasureUserDao.editTreasureUser(money, 0, money, item.getUser_id());
		treasureDao.editTreasureInvest(money,item.getTreasure_id());
		//发布理财资金处理
		accountDao.updateAccount(money, money, 0, 0, treasure.getUser_id());
		toAct=accountDao.getAccount(treasure.getUser_id());
		Global.setTransfer("money", money);
		BaseAccountLog successLog = new TreasureSuccessLog(money, toAct, item.getUser_id());
		successLog.doEvent();
		treasureRechargeDao.addTreasureRecharge(item);
		return true;
	}
	
	/**
	 * 理财宝投资金额限制
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean investCheck(long treasure_id , long invest_user_id , double invest_money , double rechargeMoney){
		Treasure treasure = treasureDao.getTreasureById(treasure_id);
		String rule_check = treasure.getRule_check();
		if( rule_check == null || rule_check.length() == 0) return true;
		RuleModel rule = new RuleModel();
		rule.setRule_check(rule_check);
		
		int check_money_type = rule.getValueIntByKey("check_money_type");
		Map<String ,Object> map = new HashMap<String, Object>();
		if(check_money_type == 1){//投资金额限制类型为1，代表是待收限制
			int is_day = rule.getValueIntByKey("is_day");
			List<Byte> borrow_type = rule.getValueListByKey("borrow_type");
			map.put("isday", is_day);
			map.put("borrow_type", borrow_type);
			map.put("user_id", invest_user_id);//
			double collectionMoney =  collectionDao.getUserWaitMoney(map);
			double money_ratio = rule.getValueDoubleByKey("money_ratio");// 投资资金倍数
			double canInvest = collectionMoney * money_ratio;
			canInvest = canInvest - rechargeMoney;
			if(canInvest >= invest_money ) return true;
		}
		return false;
	}
	
	/**
	 * 查询理财宝信息
	 * @param map
	 * @return
	 */
	public List<TreasureModel> getTreasure(Map<String , Object> map){
		return treasureDao.getTreasure(map);
	}
	
	/**
	 * 赎回审核停用/启用
	 * @param id 主键
	 * @param back_verify_type 0不需要审核/1需要审核
	 * @param operator 操作者
	 * @return
	 */
	public boolean editBackStatus(long id , byte back_verify_type ,String operator){
		Treasure item = treasureDao.getTreasureById(id);
		if(item == null) return false;
		byte stop = EnumTreasureStatus.STOP.getValue();
		byte pass_audit = EnumTreasureAudit.PASS_AUDIT.getValue();
		if( (back_verify_type == 0 || back_verify_type == 1 ) && back_verify_type != item.getBack_verify_type() && item.getAudit_status() == pass_audit && item.getStatus() == stop ){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("operator", operator);
			map.put("update_time", System.currentTimeMillis() / 1000);
			map.put("audit_status_", pass_audit);
			map.put("status_", stop);
			map.put("back_verify_type", back_verify_type);
			return treasureDao.specialEdit(map);
		}
		return false;
	}
}
