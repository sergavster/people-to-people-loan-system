package com.p2psys.model.accountlog.award;

import javax.annotation.Resource;

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

/**
 * 投标奖励
 
 *
 */
public class AwardTenderAwardLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private final static String noticeTypeNid = Constant.NOTICE_RECEIVE_TENDER_AWARD;
	private String templateNid = Constant.AWARD_ADD;
	
	@Resource
	public RewardRecordDao rewardRecordDao ;
	@Resource
	public ProtocolDao protocolDao;
	
	public AwardTenderAwardLog() {
		super();
	}

	public AwardTenderAwardLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardTenderAwardLog(double money, Account act) {
		super(money, act);
	}

	public String getTemplateNid(){
		return templateNid;
	}
	
	
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	}

	@Override
	public void updateAccount() {
		accountDao.updateAccount(this.getMoney(), this.getMoney(), 0, 0, this.getUser_id());
	}

	@Override
	public void extend() {
		Borrow borrow=(Borrow)transfer().get("borrow");
		Tender tender=(Tender)transfer().get("tender");
		rewardRecordDao.save(EnumRewardStatisticsType.TENDER_AWARD.getValue(), borrow.getId(), 
				this.getMoney(), tender.getUser_id(), borrow.getUser_id());
		
		if (Global.getWebid().equals("jsy")) {
            DetailUser user = userDao.getDetailUserByUserid(tender.getUser_id());
            Protocol protocol=new Protocol();
            protocol.setRemark("用户名为" + user.getUsername() + "的用户投标（标名为" + borrow.getName() + "的借款标）奖励金额"
                    + this.getMoney() + "元");
            protocol.setUser_id(tender.getUser_id());
            protocol.setMoney(this.getMoney());
            protocol.setProtocol_type(Constant.AWARD_PROTOCOL);
            protocol.setBorrow_id(tender.getBorrow_id());
            protocol.setPid(tender.getId());
            protocolDao.addProtocol(protocol);
        }
	} 
	
	
}
