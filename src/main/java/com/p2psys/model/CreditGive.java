package com.p2psys.model;

import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.UserCreditDao;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.Tender;
import com.p2psys.domain.UserCredit;
import com.p2psys.model.creditlog.BaseCreditLog;
import com.p2psys.model.creditlog.tender.TenderInvestSuccessLog;
import com.p2psys.tool.credit.CreditCount;
import com.p2psys.util.BigDecimalUtil;

public class CreditGive {
	
	private RuleModel rule;
	private Borrow borrow;
	private Tender tender;
	
	List<Integer> typeNoList;// 从JSON中提取不能奖励积分的标种。
    double month_check_money ;//月标投标限制金额基数
    double day_check_money ;//天标投标限制金额基数
    byte is_month ;// 月标赠送积分是否启用
    byte is_day;// 天标赠送积分是否启用
    double decimal_manage;//计算小数限制
    byte isDayBorrow;
    int time = 0 ;
    double check_money = 0;

    private UserCreditDao userCreditDao;
    
    public CreditGive(Borrow borrow,Tender tender) {
		super();
		rule = new  RuleModel(Global.getRule(EnumRuleNid.INTEGRAL_TENDER.getValue()));//查询投标奖励积分规则
		//v1.6.7.2 wcw 2013-12-23 start
		this.borrow=borrow;
		this.tender=tender;
		//v1.6.7.2 wcw 2013-12-23 end
	}
	
    public CreditGive instance(){
    	 typeNoList = rule.getValueListByKey("borrow_type_no");// 从JSON中提取不能奖励积分的标种。
    	 month_check_money = rule.getValueDoubleByKey("month_base_money");//月标投标限制金额基数
    	 day_check_money = rule.getValueDoubleByKey("day_base_money");//天标投标限制金额基数
    	 is_month =  rule.getValueByteByKey("is_month");// 月标赠送积分是否启用
    	 is_day =  rule.getValueByteByKey("is_day");// 天标赠送积分是否启用
    	 decimal_manage = rule.getValueDoubleByKey("decimal_manage");//计算小数限制
    	 isDayBorrow = (byte) borrow.getIsday();
		return this;
	}
    
    public void giveCredit(){
    	if(rule == null || rule.getStatus() != 1) return ;
    	instance();
    	if(typeNoList==null||!typeNoList.contains(borrow.getType())) return ;
    	if(1 == isDayBorrow){//判断是否是天标，如果是，则提取投标的天数。
            if(is_day != 1) return;// 如果规则中，天标奖励积分不启用，则return
            time = borrow.getTime_limit_day();
            check_money = day_check_money;
        }else if(1 == isDayBorrow){//判断是否是月标，如果是，则提取投标的月份。
            if(is_month != 1) return;// 如果规则中，月标奖励积分不启用，则return
            time = Integer.parseInt(borrow.getTime_limit());
            check_money = month_check_money;
        }
    	int value = CreditCount.getTenderValue(time, Double.parseDouble(tender.getAccount()), check_money,decimal_manage);
    	
    	if(value > 0){
    		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
    		userCreditDao = (UserCreditDao)ctx.getBean("userCreditDao");
            //查询投标成功的积分类型信息
            CreditType creditType = userCreditDao.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_INVEST.getValue());
            //查询用户的积分信息
            UserCreditModel credit  = userCreditDao.getCreditModelById(tender.getUser_id());
            
            double rate = 1;
            RuleModel ruleCredit = new RuleModel(Global.getRule(EnumRuleNid.INTEGRAL_PERCENT.getValue()));
            if(ruleCredit != null && ruleCredit.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
                rate = ruleCredit.getValueDoubleByKey("tender_percent");
            }
            // 数据转换，避免计算错误 
            double total_value_ =BigDecimalUtil.mul(value, rate);
            int total_value = (int) BigDecimalUtil.round(total_value_, 0);
            Global.setTransfer("tender_value", value);
            
            if(credit != null && credit.getUser_id() > 0 ){// 如果查询的积分信息不为空，则update
            	// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
				// 有效积分
				int valid_value = credit.getValid_value() + total_value;
				BaseCreditLog bcl = new TenderInvestSuccessLog(new Byte(
						Constant.OP_ADD), credit.getUser_id(),
						creditType.getId(), credit.getUser_id(), value, valid_value);
				// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
                bcl.doEvent();
                userCreditDao.editTenderValue(tender.getUser_id(), value , total_value);
            }else if(credit == null  ||  credit.getUser_id() <= 0){//如果会员的积分为空或会员信息的会员ID为空，则add
                // 如果会员信息为空，则封装信息，添加
                
                // 用户积分表
                UserCredit uc = new UserCredit(tender.getUser_id(), 10,new Date().getTime() / 1000, "");
                uc.setValue(total_value);
                uc.setValid_value(total_value);
                uc.setTender_value(value);
                Global.setTransfer("tender_value", value);
                // v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
				BaseCreditLog bcl = new TenderInvestSuccessLog(new Byte(
						Constant.OP_ADD), credit.getUser_id(),
						creditType.getId(), credit.getUser_id(), value, total_value);
				// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
                bcl.doEvent();
                userCreditDao.addUserCredit(uc);
            }
        }
    	
    }
 
}
