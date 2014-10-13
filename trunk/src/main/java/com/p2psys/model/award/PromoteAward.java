package com.p2psys.model.award;

import java.util.Date;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumRewardStatisticsBackType;
import com.p2psys.common.enums.EnumRewardStatisticsStatus;
import com.p2psys.common.enums.EnumRewardStatisticsType;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.BorrowDao;
import com.p2psys.dao.RewardRecordDao;
import com.p2psys.dao.RewardStatisticsDao;
import com.p2psys.dao.RulePromoteDao;
import com.p2psys.dao.TenderDao;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.RewardStatistics;
import com.p2psys.domain.Rule;
import com.p2psys.domain.RulePromote;
import com.p2psys.domain.Tender;
import com.p2psys.domain.User;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.award.AwardPromoteLog;
import com.p2psys.util.BigDecimalUtil;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 【及时雨】推广奖励
 
 * @version 1.0
 * @since 2013年12月25日 下午3:20:24
 */
public class PromoteAward {
	
	private BorrowDao borrowDao;
	private TenderDao tenderDao;
	private UserDao userDao;
	private AccountDao accountDao;
	private RulePromoteDao rulePromoteDao;
	private RewardStatisticsDao rewardStatisticsDao;
	private RewardRecordDao rewardRecordDao;
	
	private Rule rule;
	private DetailCollection collection;
	private Borrow borrow;
	private Tender tender;
	private User tenderUser;
	private User inviteUser;
	
	public PromoteAward(DetailCollection collection) {
		super();
		this.rule = Global.getRule(EnumRuleNid.RULE_PROMOTE.getValue());
		this.collection = collection;
	}
	
	public PromoteAward instance(){
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		borrowDao = (BorrowDao)ctx.getBean("borrowDao");
		tenderDao = (TenderDao)ctx.getBean("tenderDao");
		userDao = (UserDao)ctx.getBean("userDao");
		accountDao = (AccountDao)ctx.getBean("accountDao");
		rulePromoteDao = (RulePromoteDao)ctx.getBean("rulePromoteDao");
		rewardStatisticsDao = (RewardStatisticsDao)ctx.getBean("rewardStatisticsDao");
		rewardRecordDao = (RewardRecordDao)ctx.getBean("rewardRecordDao");
		
		borrow = borrowDao.getBorrowById(collection.getBorrow_id());
		tender = tenderDao.getTenderById(collection.getTender_id());
		tenderUser = userDao.getUserById(collection.getUser_id());
		if (!StringUtils.isBlank(tenderUser.getInvite_userid())) {
			inviteUser = userDao.getUserById(Long.parseLong(tenderUser.getInvite_userid()));
		}
		
		return this;
	}
	
	public void giveAward() {
		if (rule == null || rule.getStatus() == 0) {
			return;
		}
		instance();
		// 如果利息管理费算出来后大于0并且还款方式不为“提前还息到期还本”，才去判断是否有推广奖励
		if(NumberUtils.getDouble(collection.getManage_fee())<=0 || "4".equals(borrow.getStyle())){
			return;
		}
		
		if (inviteUser!=null && inviteUser.getRule_promote_id() > 0) {
			RulePromote rulePromote = rulePromoteDao.getRulePromoteById(inviteUser.getRule_promote_id());
			double award = rulePromote.getAward_percent() * Double.valueOf(collection.getManage_fee());
			// 四舍五入后的奖励
			double award_round = BigDecimalUtil.round(award, 2);
			if (award_round >= 0.01) {
				accountDao.updateAccount(award_round, award_round, 0, inviteUser.getUser_id()); 
			}
			RewardStatistics rs = new RewardStatistics();
			rs.setType(EnumRewardStatisticsType.RULE_PROMOTE.getValue());
			rs.setReward_user_id(inviteUser.getUser_id());
			rs.setPassive_user_id(tenderUser.getUser_id());
			rs.setReceive_time(DateUtils.getTimeStr(new Date()));
			rs.setReceive_yestime(DateUtils.getTimeStr(new Date()));
			rs.setReceive_account(award_round);
			rs.setReceive_yesaccount(award_round);
			rs.setReceive_status(EnumRewardStatisticsStatus.CASH_BACK.getValue());
			rs.setAddtime(DateUtils.getTimeStr(new Date()));
			rs.setRule_id(rule.getId());
			rs.setBack_type(EnumRewardStatisticsBackType.AUTO_BACK.getValue());
			rs.setIs_show(1);
			rs.setTender_count(Double.valueOf(tender.getAccount()));
			if (award_round >= 0.01) {
				rewardStatisticsDao.addRewardStatistics(rs);
				Account act = accountDao.getAccount(inviteUser.getUser_id());
				Global.setTransfer("username", tenderUser.getUsername());
				Global.setTransfer("award", award_round);
				BaseAccountLog bLog = new AwardPromoteLog(award_round, act);
				bLog.doEvent();
				
				rewardRecordDao.save(EnumRewardStatisticsType.RULE_PROMOTE.getValue(),
						rs.getId(), rs.getReceive_yesaccount(), 
						rs.getReward_user_id(), Constant.ADMIN_ID);
			}
		}
		
	}
}
