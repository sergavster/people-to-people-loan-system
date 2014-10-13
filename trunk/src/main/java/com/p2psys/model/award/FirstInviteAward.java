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
import com.p2psys.model.accountlog.award.AwardInviteFirstTenderLog;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 *  类型：民生创投邀请好友首次投标奖励
 
 *
 */
public class FirstInviteAward implements InviteAwad {
	
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
	private UserDao userDao;
	
	@Resource
	private RewardRecordDao rewardRecordDao;

	List<RewardStatistics> rewawrdList=new ArrayList<RewardStatistics>();
	
	private int isDay;
	
	private int backType;
	
	private double ruleCheckMoney;
	private double tenderAccount;
	private String ruleAddtime;
	
	public FirstInviteAward(Tender tender) {
		super();
		this.tender = tender;
		this.rule = Global.getRule(EnumRuleNid.INVITE_FIRST_TENDER_AWARD.getValue());
		this.checkModel = JSON.parseObject(rule.getRule_check(), RuleCheckModel.class);
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		BorrowDao borrowDao=(BorrowDao)ctx.getBean("borrowDao");
		this.borrow = borrowDao.getBorrowById(tender.getBorrow_id());
	}
	public FirstInviteAward instance(){
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if (ctx != null) {
			rewardStatisticsDao = (RewardStatisticsDao) ctx.getBean("rewardStatisticsDao");
			accountDao = (AccountDao) ctx.getBean("accountDao");
			userDao = (UserDao) ctx.getBean("userDao");
			rewardRecordDao = (RewardRecordDao) ctx.getBean("rewardRecordDao");
		}
		tenderAccount = Double.valueOf(tender.getAccount()); // 用户投资金额
        ruleCheckMoney = checkModel.getTender_check_money();
    	ruleAddtime = DateUtils.getTimeStr(rule.getAddtime(), "yyyy-MM-dd HH:mm:ss");
    	if (StringUtils.isBlank(checkModel.getBorrow_category())) {
    		isDay = -1;
		} else {
			isDay = Integer.parseInt(checkModel.getBorrow_category());
		}
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
		if (check(rs)) {
			if (rs.getTender_count() >= ruleCheckMoney && ruleCheckMoney > 0) {
				// 如果投资总额 >= 规则中的上线金额，把此条奖励显示到页面的列表里
				rs.setIs_show(1);
				if (EnumRewardStatisticsBackType.AUTO_BACK.equal(backType)) {
					// 如果奖励是自动发放的，投资总额达到要求后直接发放奖励
					rs.setReceive_yesaccount(rs.getReceive_account());
					rs.setReceive_yestime(DateUtils.getNowTimeStr());
					rs.setReceive_status(EnumRewardStatisticsStatus.CASH_BACK.getValue());
					
//                AwardGiveInviteInvestLog log=new AwardGiveInviteInvestLog(rs.getReceive_account(),rs.getReward_user_id(),1L);
					
					accountDao.updateAccount(rs.getReceive_yesaccount(),
							rs.getReceive_yesaccount(), 0, rs.getReward_user_id());
					account = accountDao.getAccount(rs.getReward_user_id());
					Global.setTransfer("award", rs.getReceive_yesaccount());
					BaseAccountLog bLog = new AwardInviteFirstTenderLog(rs.getReceive_yesaccount(), account);
					bLog.doEvent();
					// 奖励记录：被邀请人奖励
					rewardRecordDao.save(EnumRewardStatisticsType.INVITER_AWARD.getValue(), 
							rs.getId(), rs.getReceive_yesaccount(), rs.getReward_user_id(), 1);
				}
			}
			rewardStatisticsDao.updateReward(rs);
		}
	}
	/**
	 * 检查是否已经存在， 不存在新建实例
	 * @return
	 */
	public boolean create(){
        rewawrdList = rewardStatisticsDao.getRewardUserByPassive(tender.getUser_id(), rule.getId(), ruleAddtime);
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
	
	public boolean check(RewardStatistics rs){
		String time = DateUtils.getTimeStr(rule.getAddtime(), "yyyy-MM-dd HH:mm:ss");
		int ruleAddTime = Integer.parseInt(time);
		int userAddtime = NumberUtils.getInt(tender.getAddtime()); // 投资用户注册时间
		if (userAddtime > ruleAddTime ) {
			return false;
		}
		if (isDay > 0 || isDay != borrow.getIsday()) {
			return false;
		}
		return true;
	}

	private void newAward() {
		RewardStatistics rs = new RewardStatistics();
        List<Integer> borrowTypeList = checkModel.getBorrow_type();
        if(!borrowTypeList.contains(borrow.getType())) return ;
        User tenderUser = userDao.getUserById(tender.getUser_id());
        if(tenderUser==null||StringUtils.isBlank(tenderUser.getInvite_userid())) return ;
        // 类型：民生创投邀请好友首次投标奖励
        rs.setType(EnumRewardStatisticsType.INVITE_FIRST_TENDER_AWARD.getValue()); 
        // 邀请人(user.getInvite_userid())的奖励
        rs.setReward_user_id(Long.valueOf(tenderUser.getInvite_userid()));
        if (tenderAccount < ruleCheckMoney) {
            rs.setReceive_account(0); // 应收金额
            rs.setIs_show(0);
            rs.setReceive_status(Byte.valueOf("5")); //无用数据
        } else {
            // 首次投标需要达到的金额（规则里取）
            double number = tenderAccount / ruleCheckMoney;
            // 实收金额 = 投资金额/首次投标需要达到的金额*10(规则里的现金)
            double receive_account = Math.floor(number) * checkModel.getReceive_account();
            rs.setReceive_account(receive_account);
            rs.setIs_show(1);
            rs.setReceive_status(EnumRewardStatisticsStatus.CASH_UN_BACK.getValue()); // 未返现
        }
        rs.setReceive_yesaccount(0); // 实收金额
        rs.setPassive_user_id(tenderUser.getUser_id()); // 会员ID
        rs.setReceive_time(DateUtils.getTimeStr(new Date())); // 应收时间
        rs.setEndtime(checkModel.getEnd_time());
        rewardStatisticsDao.addRewardStatistics(rs);
        rewawrdList.add(rs);
	}

}
