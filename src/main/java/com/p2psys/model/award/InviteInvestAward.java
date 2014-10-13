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
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.RewardRecordDao;
import com.p2psys.dao.RewardStatisticsDao;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.RewardStatistics;
import com.p2psys.domain.Rule;
import com.p2psys.domain.Tender;
import com.p2psys.domain.User;
import com.p2psys.model.RuleCheckModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.award.AwardInviteLog;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 被邀请人奖励
 
 *
 */
public class InviteInvestAward implements InviteAwad {
	
	private RuleCheckModel checkModel;
	
	private Rule rule;
	
	private Account account;
	
	private Tender tender;
	
	@Resource
	private RewardStatisticsDao rewardStatisticsDao;
	
	@Resource
	private AccountDao accountDao;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private RewardRecordDao rewardRecordDao;
	
	List<RewardStatistics> rewawrdList = new ArrayList<RewardStatistics>();
	
	private double receiveRate;
	
	private double receiveAccount;
	
	private int backType;
	
	private String ruleAddtime;
	
	private double tenderCheckMoney;
	
	private int repayCount;
	
	public InviteInvestAward(Tender tender) {
		super();
		this.tender = tender;
		this.rule = Global.getRule(EnumRuleNid.INVITER_AWARD.getValue());
		this.checkModel = JSON.parseObject(rule.getRule_check(), RuleCheckModel.class);
	}
	public InviteInvestAward instance(){
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if (ctx != null) {
			rewardStatisticsDao = (RewardStatisticsDao) ctx.getBean("rewardStatisticsDao");
			accountDao = (AccountDao) ctx.getBean("accountDao");
			userDao = (UserDao) ctx.getBean("userDao");
			rewardRecordDao = (RewardRecordDao) ctx.getBean("rewardRecordDao");
		}
		this.account = accountDao.getAccount(tender.getUser_id());
		receiveRate = checkModel.getReceive_rate();
		receiveAccount = checkModel.getReceive_account();
		backType = checkModel.getBack_type();
    	ruleAddtime = DateUtils.getTimeStr(rule.getAddtime(), "yyyy-MM-dd HH:mm:ss");
    	tenderCheckMoney = checkModel.getTender_check_money();
    	repayCount = checkModel.getRepay_count();
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
		for (RewardStatistics rs : rewawrdList) {
			giveOne(rs);
		}
	}
	
	public void giveOne(RewardStatistics rs) {
		check(rs);
		if (rs.getTender_count() >= tenderCheckMoney && tenderCheckMoney > 0) {
            // 如果投资总额 >= 规则中的上线金额，把此条奖励显示到页面的列表里
            rs.setIs_show(1);
            if (backType == EnumRewardStatisticsBackType.AUTO_BACK.getValue()) {
                // 如果奖励是自动发放的，投资总额达到要求后直接发放奖励
                rs.setReceive_yesaccount(rs.getReceive_account());
                rs.setReceive_yestime(DateUtils.getNowTimeStr());
                rs.setReceive_status(EnumRewardStatisticsStatus.CASH_BACK.getValue());
                
//                AwardGiveInviteInvestLog log=new AwardGiveInviteInvestLog(rs.getReceive_account(),rs.getReward_user_id(),1L);
//                log.doEvent();
                
                accountDao.updateAccount(rs.getReceive_yesaccount(),
    					rs.getReceive_yesaccount(), 0, rs.getReward_user_id());
                Account act = accountDao.getAccount(rs.getReward_user_id());
				Global.setTransfer("award", rs.getReceive_yesaccount());
				BaseAccountLog bLog = new AwardInviteLog(rs.getReceive_yesaccount(), act);
				bLog.doEvent();
				// 奖励记录：被邀请人奖励
				rewardRecordDao.save(EnumRewardStatisticsType.INVITER_AWARD.getValue(), 
						rs.getId(), rs.getReceive_yesaccount(), rs.getReward_user_id(), 1);
            }
        }
        rewardStatisticsDao.updateReward(rs);
	}
	
	public boolean create(){
        rewawrdList = rewardStatisticsDao.getPassiveByReward(tender.getUser_id(), rule.getId(), ruleAddtime);
        if (rewawrdList==null ||rewawrdList.isEmpty()) {
        	newAward();
            return false;
        }
		return true;
	}
	
	public void check(RewardStatistics rs){
        // 投标金额
        double tender_count = Double.valueOf(tender.getAccount());
        if (repayCount == 1) {
            if (rs.getTender_count() < tenderCheckMoney) {
                rs.setTender_count(account.getCollection());
            }
        } else {
            // 结束时间不为空，并且复审时间在开始时间和结束时间之间，加上此次的投标金额
            rs.setTender_count(rs.getTender_count() + tender_count);
        }
        // 如果百分比不为空，奖励 = 总额*百分比
        if (receiveRate > 0) {
            rs.setReceive_account(receiveRate * rs.getTender_count());
        } else if (receiveAccount > 0) {
            // 如果规则中直接配置发放多少奖励，奖励=规则中的奖励
            rs.setReceive_account(receiveAccount);
        }
	}
	
	public boolean check(){
		// 规则添加时间
		int ruleAddTime = NumberUtils.getInt(ruleAddtime);
		// 投标时间
		int tenderTime = NumberUtils.getInt(tender.getAddtime());
		User user = userDao.getUserById(tender.getUser_id());
		// 用户注册时间
		int addtime = NumberUtils.getInt(user.getAddtime());
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
		} else if (tenderTime > ruleAddTime && addtime > ruleAddTime) {
			// 投标时间 > 规则添加时间并且用户注册时间>规则添加时间，符合条件返回true
			return true;
		}
		return false;
	}

	private void newAward() {
		User tenderUser = userDao.getUserById(tender.getUser_id());
		if (!StringUtils.isBlank(tenderUser.getInvite_userid())) {
			RewardStatistics rewardStatistics = new RewardStatistics();
			rewardStatistics.setType(EnumRewardStatisticsType.INVITER_AWARD.getValue()); // 类型：邀请人奖励
			// 邀请人(user.getInvite_userid())的奖励
			rewardStatistics.setReward_user_id(tenderUser.getUser_id());
			Account tenderAct = accountDao.getAccount(tender.getUser_id());
			if (repayCount == 1) {
				if (tenderAct.getCollection() > tenderCheckMoney) {
					// 如果待收金额 > 规则里的上线金额，该奖励记录标记为可以显示
					rewardStatistics.setIs_show(1);
					rewardStatistics.setPassive_user_id(1L); // 会员ID
					rewardStatistics.setReceive_time(DateUtils.getTimeStr(new Date())); // 应收时间
					if (backType == EnumRewardStatisticsBackType.TIME_BACK.getValue()) { 
						// 返现方式：定时返现
						rewardStatistics.setBack_type(EnumRewardStatisticsBackType.TIME_BACK.getValue());
					} else if (backType == EnumRewardStatisticsBackType.AUTO_BACK.getValue()) {
						// 返现方式：自动返现
						rewardStatistics.setBack_type(EnumRewardStatisticsBackType.AUTO_BACK.getValue());
					} else if (backType == EnumRewardStatisticsBackType.ARTIFICIAL_BACK.getValue()) { 
						// 返现方式：人工返现
						rewardStatistics.setBack_type(EnumRewardStatisticsBackType.ARTIFICIAL_BACK.getValue());
					}
					rewardStatistics.setTender_count(tenderAct.getCollection());
					if (receiveRate > 0) {
						rewardStatistics.setReceive_account(receiveRate * rewardStatistics.getTender_count());
					} else if (receiveAccount > 0) {
						// 如果规则中直接配置发放多少奖励，奖励=规则中的奖励
						rewardStatistics.setReceive_account(receiveAccount);
					}
					rewardStatistics.setReceive_status(EnumRewardStatisticsStatus.CASH_UN_BACK.getValue());
					rewardStatistics.setAddtime(tender.getAddtime());
					rewardStatistics.setEndtime(null);
					rewardStatistics.setRule_id(rule.getId());
					rewardStatistics.setType_fk_id(0);
				}
			} else {
				// 投标金额
				Double tenderCount = Double.valueOf(tender.getAccount());
				rewardStatistics.setTender_count(tenderCount);
				if (tenderCount > tenderCheckMoney) {
					// 如果投标金额 > 规则里的上线金额，该奖励记录标记为可以显示
					rewardStatistics.setIs_show(1);
				} else {
					rewardStatistics.setIs_show(0);
				}
				// 如果百分比不为空，奖励=总额*百分比
				if (receiveRate > 0) {
					rewardStatistics.setReceive_account(receiveRate * rewardStatistics.getTender_count());
				} else if (receiveAccount > 0) {
					// 如果规则中直接配置发放多少奖励，奖励=规则中的奖励
					rewardStatistics.setReceive_account(receiveAccount);
				}
				rewardStatistics.setPassive_user_id(1L); // 会员ID
				rewardStatistics.setReceive_time(DateUtils.getTimeStr(new Date())); // 应收时间
				if (backType == EnumRewardStatisticsBackType.TIME_BACK.getValue()) { // 返现方式：定时返现
					rewardStatistics.setBack_type(EnumRewardStatisticsBackType.TIME_BACK.getValue());
				} else if (backType  == EnumRewardStatisticsBackType.AUTO_BACK.getValue()) {
					// 返现方式：自动返现
					rewardStatistics.setBack_type(EnumRewardStatisticsBackType.AUTO_BACK.getValue());
				} else if (backType  == EnumRewardStatisticsBackType.ARTIFICIAL_BACK.getValue()) { // 返现方式：人工返现
					rewardStatistics.setBack_type(EnumRewardStatisticsBackType.ARTIFICIAL_BACK.getValue());
				}
				rewardStatistics.setReceive_yesaccount(0); // 实收金额
				rewardStatistics.setReceive_status(EnumRewardStatisticsStatus.CASH_UN_BACK.getValue());
				rewardStatistics.setAddtime(tender.getAddtime());
				rewardStatistics.setEndtime(null);
				rewardStatistics.setRule_id(rule.getId());
				rewardStatistics.setType_fk_id(0);
			}
			// 根据对应的规则保存到统计表中
			rewardStatisticsDao.addRewardStatistics(rewardStatistics);
			rewawrdList.add(rewardStatistics);
		}
	}

}
