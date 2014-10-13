package com.p2psys.model.accountlog.borrow.verifyfullBorrow;

import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.ProtocolDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Huikuan;
import com.p2psys.domain.Protocol;
import com.p2psys.domain.Tender;
import com.p2psys.model.DetailUser;
import com.p2psys.model.RuleModel;
import com.p2psys.model.accountlog.BaseBorrowLog;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
/**
 * 扣除冻结投标金额，生成待收本金log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public  class BorrowDecuctFreezeLog extends BaseBorrowLog {

	private static final long serialVersionUID = -1751294712144079532L;
	
	private String templateNid = Constant.INVEST;
	
	private final static String noticeTypeNid = Constant.NOTICE_INVEST_SUCC;

	public BorrowDecuctFreezeLog() {
		super();
	}

	public BorrowDecuctFreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public BorrowDecuctFreezeLog(double money, Account act) {
		super(money, act);
	}

	@Override
	public String getTemplateNid(){
		return templateNid;
	}

	@Override
	public void updateAccount() {
		accountDao.updateAccount(0, 0, -this.getMoney(), this.getMoney(), this.getUser_id());
	}

	@Override
	public void extend() {
		Borrow borrow=(Borrow)transfer().get("borrow");
		Tender tender=(Tender)transfer().get("tender");
		if (Global.getWebid().equals("jsy")) {
			Protocol protocol=new Protocol();
            DetailUser user = userDao.getDetailUserByUserid(this.getUser_id());
            protocol.setRemark("用户名为" + user.getUsername() + "的用户投标（标名为" + borrow.getName() + "的借款标）金额"
                    + tender.getAccount() + "元");
            protocol.setProtocol_type(Constant.TENDER_PROTOCOL);
            protocol.setUser_id(tender.getUser_id());
            protocol.setBorrow_id(tender.getBorrow_id());
            protocol.setPid(tender.getId());
            protocol.setMoney(NumberUtils.getDouble(tender.getAccount()));
            WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
            ProtocolDao protocolDao = (ProtocolDao)ctx.getBean("protocolDao");
            protocolDao.addProtocol(protocol);
        }
	}
	
	@Override
	public void accountSumProperty() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.getAccountSum(this.getUser_id());
		// 取出可能扣款的值
		double huikuan = accountSum.getHuikuan();
		double usedHuikuan = accountSum.getUsed_huikuan();
		double useHuikuan = huikuan - usedHuikuan;

		// 增加account_sum_log日志
		double money = this.getMoney();
		long user_id = this.getUser_id();
		/*long to_user_id = this.getTo_user();
		String addtime = String.valueOf(DateUtils.getTime(new Date()));
		String addip = "";*/

		// 扣除回款的account_sum_log
		double currHuikuan = 0;
		// 本次使用的回款最大不能超过可用回款，
		currHuikuan = (useHuikuan >= money) ? money : (useHuikuan);
		if (currHuikuan > 0) {
			int enableHuikuan = Global.getInt("huikuan_enable");
			Map tranData = transfer();
			BorrowModel borrow = (BorrowModel)(tranData.get("borrow"));
			Tender tender = (Tender)(tranData.get("tender"));

			// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 start
			RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.HUIKUAN_CONF.getValue()));
			if(rule!=null && rule.getStatus()==1){
				String borrowTypeOfUnable = rule.getValueStrByKey("borrowTypeOfUnable");//投标不使用回款续投的标种
				// v1.6.7.2 RDPROJECT-436 xx 2013-12-16 start
				double lowestHuikuan = rule.getValueDoubleByKey("lowest_huikuan");
				if (!StringUtils.isBlank(borrowTypeOfUnable) && !borrowTypeOfUnable.contains(borrow.getType()+"")
						&&currHuikuan>=lowestHuikuan
						// v1.6.7.2 RDPROJECT-436 xx 2013-12-16 end
						&&enableHuikuan==1&&tender.getAuto_repurchase()==1){
				// v1.6.7.1 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种） xx 2013-11-19 end	
					
					/*double huikuan_before_money = usedHuikuan;
					double huikuan_money = currHuikuan;
					double huikuan_after_money = usedHuikuan + currHuikuan;
					Global.setTransfer("user_cash", huikuan_money);
					AccountSumLog caseFeeAccountSumLog = new AccountSumLog();
					caseFeeAccountSumLog.setUser_id(user_id);
					caseFeeAccountSumLog.setTo_user_id(to_user_id);
					caseFeeAccountSumLog.setBefore_money(huikuan_before_money);
					caseFeeAccountSumLog.setMoney(huikuan_money);
					caseFeeAccountSumLog.setAfter_money(huikuan_after_money);
					caseFeeAccountSumLog.setAddip(addip);
					caseFeeAccountSumLog.setAddtime(addtime);
					this.setAccountSumLogRemarkTemplate(usedHuikuanRemark);
					caseFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
					caseFeeAccountSumLog.setType(EnumAccountSumProperty.USED_HUIKUAN.getValue());
					accountSumLogDao.addAccountSumLog(caseFeeAccountSumLog);
					accountSumDao.editSumByProperty(EnumAccountSumProperty.USED_HUIKUAN.getValue(), huikuan_money,this.getUser_id());
				*/
					this.huikuanManage(currHuikuan);
					double huikuan_money = currHuikuan;
					if(huikuan_money>=0.01){
						// v1.6.7.2 RDPROJECT-645 xx 2013-12-28 start
						double huikuan_award = Global.getDouble("huikuan_award");;
						RuleModel huikuan_award_rule = rule.getRuleByKey("huikuan_award");
						int cal_style = huikuan_award_rule.getValueIntByKey("cal_style");
						if(borrow.getIsday()==0){
							switch(cal_style){
							case 2:
								RuleModel month_rate_rule = huikuan_award_rule.getRuleByKey("month_rate");
								huikuan_award = month_rate_rule.getValueDoubleByKey(borrow.getTime_limit());
							}
						}
						// v1.6.7.2 RDPROJECT-645 xx 2013-12-28 end
						
						double huikuan_award_value=huikuan_money*huikuan_award;
						if(huikuan_award_value>=0.01){
							Huikuan hk=new Huikuan();
							hk.setUser_id(user_id);
							hk.setHuikuan_money(NumberUtils.format2Str(huikuan_money));
							hk.setRemark("回款续投"+NumberUtils.format2Str(huikuan_money)+"元");
							hk.setStatus("0");
							hk.setAddtime(DateUtils.getNowTimeStr());
							if(huikuan_award_value>=0.01){
								hk.setHuikuan_award(NumberUtils.format2Str(huikuan_money*huikuan_award));
							}
							accountDao.addHuikuanMoney(hk);
						}
					}
				}
			}
		}
	}
	
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	} 

}
