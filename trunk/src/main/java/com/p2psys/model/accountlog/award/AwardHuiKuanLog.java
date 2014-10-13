package com.p2psys.model.accountlog.award;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumLogTemplateType;
import com.p2psys.common.enums.EnumRewardStatisticsType;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.ProtocolDao;
import com.p2psys.dao.RewardRecordDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Huikuan;
import com.p2psys.domain.Tender;
import com.p2psys.model.accountlog.AwardSuccessLog;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 回款续投奖励
 
 *
 */
public class AwardHuiKuanLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private final static String noticeTypeNid = Constant.NOTICE_RECEIVE_HUIKUAN_AWARD;
	private String templateNid = Constant.HUIKUAN_AWARD;
	
	public AwardHuiKuanLog() {
		super();
	}

	public AwardHuiKuanLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardHuiKuanLog(double money, Account act) {
		super(money, act);
	}
	
	public String getLogRemarkTemplate() {
		String remark=StringUtils.isNull((String) Global.getTransfer().get("remark"));
		if(StringUtils.isBlank(remark)){
		    return Global.getLogTempValue(EnumLogTemplateType.ACCOUNT_LOG.getValue(), templateNid);
		}else{
			return remark;
		}
	}
	
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	}

	@Override
	public void updateAccount() {
		accountDao.updateAccount(this.getMoney(), this.getMoney(), 0, this.getUser_id());
	}

	@Override
	public void extend() {
		Huikuan huikuan = (Huikuan) transfer().get("huikuan");
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		RewardRecordDao rewardRecordDao = (RewardRecordDao) ctx.getBean("rewardRecordDao");
		rewardRecordDao.save(EnumRewardStatisticsType.HUIKUAN_AWARD.getValue(),
				huikuan.getId(), this.getMoney(), huikuan.getUser_id(),
				this.getTo_user());

	} 
	
	

}
