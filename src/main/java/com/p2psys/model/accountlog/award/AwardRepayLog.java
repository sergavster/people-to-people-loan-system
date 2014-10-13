package com.p2psys.model.accountlog.award;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumRewardStatisticsType;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.ProtocolDao;
import com.p2psys.dao.RewardRecordDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Protocol;
import com.p2psys.domain.Tender;
import com.p2psys.model.DetailUser;
import com.p2psys.model.accountlog.AwardSuccessLog;
import com.p2psys.util.DateUtils;

/**
 * 还款奖励
 
 *
 */
public class AwardRepayLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.REPAYMENT_AWARD;
	
	public AwardRepayLog() {
		super();
	}

	public AwardRepayLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardRepayLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}

	@Override
	public void updateAccount() {
		accountDao.updateAccount(this.getMoney(), this.getMoney(), 0, 0, this.getUser_id());
	}

	@Override
	public void extend() {
		Borrow borrow=(Borrow)transfer().get("borrow");
		Tender tender=(Tender)transfer().get("tender");
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		RewardRecordDao rewardRecordDao = (RewardRecordDao) ctx.getBean("rewardRecordDao");
		rewardRecordDao.save(EnumRewardStatisticsType.REPAY_AWARD.getValue(),
				borrow.getId(), this.getMoney(), tender.getUser_id(),
				borrow.getUser_id());
	
		if (Global.getWebid().equals("jsy")) {
			DetailUser user = userDao.getDetailUserByUserid(tender.getUser_id());
			Protocol protocol = new Protocol(0,	Constant.AWARD_PROTOCOL, DateUtils.getNowTimeStr(), Global.getIP(), "");
			protocol.setRemark("用户名为" + user.getUsername()	+ "的用户投标（标名为" + borrow.getName() + "的借款标）还款奖励金额" + this.getMoney() + "元，生成资金划转委托书");
			protocol.setMoney(this.getMoney());
			protocol.setUser_id(tender.getUser_id());
			ProtocolDao protocolDao = (ProtocolDao)ctx.getBean("protocolDao");
			protocolDao.addProtocol(protocol);
		}
	}
	
	
}
