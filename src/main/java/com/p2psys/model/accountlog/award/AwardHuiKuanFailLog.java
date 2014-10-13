package com.p2psys.model.accountlog.award;

import com.p2psys.common.enums.EnumLogTemplateType;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.AwardSuccessLog;
import com.p2psys.util.StringUtils;

/**
 * 回款续投奖励
 
 *
 */
public class AwardHuiKuanFailLog extends AwardSuccessLog{

	private static final long serialVersionUID = 6234630251774622982L;
	
	private String templateNid = Constant.HUIKUAN_AWARD;
	
	public AwardHuiKuanFailLog() {
		super();
	}

	public AwardHuiKuanFailLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public AwardHuiKuanFailLog(double money, Account act) {
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
	public void accountSumProperty() {
	}

	@Override
	public void addAccountSumLog() {
		
	}
	

}
