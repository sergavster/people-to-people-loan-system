package com.p2psys.model.fee;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumCreditRank;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.UserCreditDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.UserCredit;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.RuleModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.borrow.BackManageFeeLevelLog;
import com.p2psys.util.BigDecimalUtil;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 利息管理费优惠返还     会员积分等级
 
 * @version 1.0
 * @since 2013年12月25日 下午5:34:31
 */
public class BackManageFeeByUserLevel {
	
	private UserCreditDao userCreditDao;
	private AccountDao accountDao;
	
	private DetailCollection collection;
	private Account tenderAct;
	private double manageFee;
	private double levelFreeMoney;
	
	
	public BackManageFeeByUserLevel(DetailCollection collection){
		this.collection = collection;
	}
	
	public BackManageFeeByUserLevel instance(){
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		accountDao = (AccountDao)ctx.getBean("accountDao");
		userCreditDao = (UserCreditDao)ctx.getBean("userCreditDao");
		
		tenderAct = accountDao.getAccountByUserId(collection.getUser_id());
		
		if(!StringUtils.isBlank(collection.getManage_fee())){
			manageFee = NumberUtils.getDouble(collection.getManage_fee());
			levelFreeMoney = userLevelManageFree(collection.getUser_id(), manageFee);
		}
		return this;
	}
	
	public double backFee(){
		instance();
		double feeMoney = manageFee;
		if(manageFee>levelFreeMoney && levelFreeMoney > 0){
			feeMoney = manageFee - levelFreeMoney;
			Global.setTransfer("money", levelFreeMoney);
			BaseAccountLog backManageFeeLog = new BackManageFeeLevelLog(levelFreeMoney, tenderAct);
			backManageFeeLog.doEvent();
			Global.setTransfer("manage_fee", feeMoney);
		}
		return feeMoney;
	}
	
	/**
	 * 根据用户的等级算出管理费的优惠额度
	 * @param user_id
	 * @param fee
	 * @return
	 */
	public double userLevelManageFree(long user_id , double fee){
		UserCredit credit = userCreditDao.getUserCreditByUserId(user_id);
		// 如果会员的积分等级实体为空或者其等级小于等于零，则无优惠额度
		if(credit == null || credit.getUser_credit_level() <= 0) return 0;
		
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.CREDIT_LEVE_FREE.getValue()));
		// 如果规则为空，或者规则不启用，则无优惠额度
		if(rule == null || rule.getStatus() != EnumRuleStatus.RULE_STATUS_YES.getValue()) return 0;
		
		// 如会员等级小于一级，则无优惠额度
		if(credit.getUser_credit_level() < EnumCreditRank.RANK_1.getValue()) return 0;
		
		// 完全免费的最大等级
		int free_max_level = rule.getValueIntByKey("free_max_level");
		if(credit.getUser_credit_level() >= free_max_level) return fee;//如果用户的等级大于等于完全免费的最大等级，则完全免费
		
		String jsonKey = "rate_rank_"+credit.getUser_credit_level();
		double freeRate = rule.getValueDoubleByKey(jsonKey);
		if(freeRate > 0){// 如果提取的免费百分比大于零，则优惠额度等于收费额度乘以优惠的百分比
			return BigDecimalUtil.mul(fee, freeRate);
		}else if(freeRate  <= 0){// 如果提取的免费百分比小于等于零，则无优惠额度
			return 0;
		}
		return 0;
	}
	
}
