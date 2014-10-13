package com.p2psys.model.award;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.p2psys.common.enums.EnumRewardStatisticsBackType;
import com.p2psys.common.enums.EnumRewardStatisticsStatus;
import com.p2psys.common.enums.EnumRewardStatisticsType;
import com.p2psys.common.enums.EnumRewardStatisticsWay;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.BorrowDao;
import com.p2psys.dao.RewardRecordDao;
import com.p2psys.dao.RewardStatisticsDao;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.RewardStatistics;
import com.p2psys.domain.Rule;
import com.p2psys.domain.Tender;
import com.p2psys.domain.User;
import com.p2psys.model.RuleCheckModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.award.AwardFirstTenderLog;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;

/**
 * 第一次投标奖励
 
 *
 */
public class FirstTenderAward implements InviteAwad {

	private RuleCheckModel checkModel;
	
	private Rule rule;
	
	private Borrow borrow;
	
	private Tender tender;
	
	private Account account;
	
	@Resource
	private RewardStatisticsDao rewardStatisticsDao;
	
	@Resource
	private AccountDao accountDao;
	
	@Resource
	private RewardRecordDao rewardRecordDao;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private BorrowDao borrowDao;

	List<RewardStatistics> rewawrdList = new ArrayList<RewardStatistics>();
	
	private int receiveWay;
	
	private double receiveRate;
	
	private double receiveAccount;
	
	private int backType;
	
	private double tenderAccount;
	
	private String ruleAddtime;
	
	public FirstTenderAward(Tender tender) {
		super();
		this.tender = tender;
		this.rule = Global.getRule(EnumRuleNid.FIRST_TENDER_REWARD.getValue());
		this.checkModel = JSON.parseObject(rule.getRule_check(), RuleCheckModel.class);
	}
	public FirstTenderAward instance(){
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if (ctx != null) {
			rewardStatisticsDao = (RewardStatisticsDao)ctx.getBean("rewardStatisticsDao");
			accountDao = (AccountDao) ctx.getBean("accountDao");
			userDao = (UserDao) ctx.getBean("userDao");
			borrowDao = (BorrowDao) ctx.getBean("borrowDao");
			rewardRecordDao = (RewardRecordDao) ctx.getBean("rewardRecordDao");
		}
		this.borrow = borrowDao.getBorrowById(tender.getBorrow_id());
		receiveWay = checkModel.getReceive_way();
		receiveRate = checkModel.getReceive_rate();
		receiveAccount = checkModel.getReceive_account();
		backType = checkModel.getBack_type();
		tenderAccount = Double.valueOf(tender.getAccount()); // 用户投资金额
    	ruleAddtime = DateUtils.getTimeStr(rule.getAddtime(), "yyyy-MM-dd HH:mm:ss");
		return this;
	}
	
	@Override
	public void giveAward() {
		if (rule == null || rule.getStatus() == 0) {
			return;
		}
		instance();
		if (check()) {
			create();
		}
		List<RewardStatistics> list = rewardStatisticsDao.getPassiveByReward(
				tender.getUser_id(), rule.getId(), ruleAddtime);
		for (RewardStatistics rs : list) {
			giveOne(rs);
		}
	}
	
	public void giveOne(RewardStatistics rs) {
		if (EnumRewardStatisticsBackType.AUTO_BACK.getValue() == backType
				&& rs.getReceive_status().equals(EnumRewardStatisticsStatus.CASH_UN_BACK.getValue()) ) {
			// 如果奖励是自动发放的，投资总额达到要求后直接发放奖励
			rs.setReceive_yesaccount(rs.getReceive_account());
			rs.setReceive_yestime(DateUtils.getNowTimeStr());
			rs.setReceive_status(EnumRewardStatisticsStatus.CASH_BACK.getValue());
			/*AwardGiveInviteInvestLog log = new AwardGiveInviteInvestLog(
				rs.getReceive_account(), rs.getReward_user_id(), 1L);*/
			
			accountDao.updateAccount(rs.getReceive_yesaccount(),
					rs.getReceive_yesaccount(), 0, rs.getReward_user_id());
			account = accountDao.getAccount(rs.getReward_user_id());
			Global.setTransfer("award", rs.getReceive_yesaccount());
			BaseAccountLog bLog = new AwardFirstTenderLog(
					rs.getReceive_yesaccount(), account, rs.getPassive_user_id());
			bLog.doEvent();
			
			// 奖励记录：第一次投标奖励
			rewardRecordDao.save(EnumRewardStatisticsType.FIRST_TENDER_REWARD.getValue(), 
					rs.getId(), rs.getReceive_yesaccount(), rs.getReward_user_id(), 1);
		}
		rewardStatisticsDao.updateReward(rs);
	}
	
	/**
	 * 检查是否已经存在， 不存在新建实例
	 * @return
	 */
	public boolean create(){
        rewawrdList = rewardStatisticsDao.getPassiveByReward(tender.getUser_id(), rule.getId(), ruleAddtime);
        if (rewawrdList==null ||rewawrdList.isEmpty()) {
        	newAward();
            return false;
        }
		return true;
	}

	/**
	 * 校验时间是否满足条件
	 * @return
	 */
	public boolean check(){
		// 规则添加时间
		int ruleAddTime = NumberUtils.getInt(ruleAddtime);
		// 投标时间
		int tenderTime = NumberUtils.getInt(tender.getAddtime());
		// 规则开始时间
		int startTime = NumberUtils.getInt(checkModel.getStart_time());
		// 规则结束时间
		int endTime = NumberUtils.getInt(checkModel.getEnd_time());
		if (startTime > 0 && endTime >0) { // 如果开始时间和结束时间>0
			// 规则开始时间 < 投标时间 < 规则结束时间
			if (tenderTime > startTime && tenderTime < endTime) {
				// 符合条件，返回true
				return true;
			}
		} else if (tenderTime > ruleAddTime) {
			// 投标时间 > 规则添加时间，符合条件返回true
			return true;
		}
		return false;
	}

	private void newAward() {
		RewardStatistics rs = new RewardStatistics();
        List<Integer> borrowTypeList = checkModel.getBorrow_type();
        if (!borrowTypeList.contains(borrow.getType())) return ;
        User tenderUser = userDao.getUserById(tender.getUser_id());
        if (tenderUser == null) return ;
        // 类型：第一次投标奖励
        rs.setType(EnumRewardStatisticsType.FIRST_TENDER_REWARD.getValue()); 
        // 邀请人(user.getInvite_userid())的奖励
        rs.setReward_user_id(tenderUser.getUser_id());
        if (receiveWay == EnumRewardStatisticsWay.PERCENT.getValue()) { // 百分比
			rs.setReceive_account(tenderAccount * receiveRate);
		} else if (receiveWay == EnumRewardStatisticsWay.MONEY.getValue()) { // 现金
			rs.setReceive_account(receiveAccount);
		}
        if (backType == EnumRewardStatisticsBackType.TIME_BACK.getValue()) { 
			// 返现方式：定时返现
			rs.setBack_type(EnumRewardStatisticsBackType.TIME_BACK.getValue());
		} else if (backType == EnumRewardStatisticsBackType.AUTO_BACK.getValue()) {
			// 返现方式：自动返现
			rs.setBack_type(EnumRewardStatisticsBackType.AUTO_BACK.getValue());
		} else if (backType == EnumRewardStatisticsBackType.ARTIFICIAL_BACK.getValue()) { 
			// 返现方式：人工返现
			rs.setBack_type(EnumRewardStatisticsBackType.ARTIFICIAL_BACK.getValue());
		}
        rs.setReceive_status(EnumRewardStatisticsStatus.CASH_UN_BACK.getValue());
        rs.setIs_show(1);
        rs.setTender_count(tenderAccount);
        rs.setPassive_user_id(1L); // 会员ID
        rs.setAddtime(DateUtils.getTimeStr(new Date()));
        rs.setRule_id(rule.getId());
        rs.setReceive_time(DateUtils.getTimeStr(new Date())); // 应收时间
        rs.setEndtime(checkModel.getEnd_time());
        rewardStatisticsDao.addRewardStatistics(rs);
        rewawrdList.add(rs);
	}

}
