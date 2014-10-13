package com.p2psys.web.action.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.AccountCash;
import com.p2psys.domain.User;
import com.p2psys.exception.AccountException;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.account.AccountModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.UserService;
import com.p2psys.tool.coder.MD5;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class AccountCashAction extends BaseAction {

	private final static Logger logger = Logger.getLogger(AccountCashAction.class);

	private AccountService accountService;

	private UserService userService;
	
	private BorrowService borrowService;
	
	private String valicode;
	private String money;
	private String paypassword;
	private String type;

	
	public String accountCash() {
		boolean isOk = true;
		String checkMsg = "";
		long user_id = getSessionUser().getUser_id();
		User user = userService.getUserById(user_id);
		String account = paramString("account");
		AccountModel detailAct = null;
		if (StringUtils.isBlank(account)) {
			detailAct = accountService.getAccount(user_id);
		} else {
			detailAct = accountService.getAccountByBankAccount(user_id, account);
		}
		String errormsg = "";
		if (detailAct == null || StringUtils.isNull(detailAct.getBankaccount()).equals("")) {
			errormsg = "您的银行账号还没填写，请先<a href=\"bank.html\"><font color='red'><strong>填写</strong></font></a>";
		}
		// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
		if (user.getReal_status() != 1) {
			errormsg = "您还未通过实名认证，请先<a href=\"" + request.getContextPath()
					+ "/member/identify/realname.html\"><font color='red'><strong>填写</strong></font></a>";
		}
		if (user.getPhone_status() != 1) {
			errormsg = "您还未通过手机认证，请先<a href=\"" + request.getContextPath()
					+ "/member/identify/phone.html\"><font color='red'><strong>填写</strong></font></a>";
		}
		// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
		if (!errormsg.equals("")) {
			request.setAttribute("account", detailAct);
			request.setAttribute("errormsg", errormsg);
			return "success";
		}
		try {
			boolean result=phoneSms();
			if(!result){
				errormsg = "短信验证码不正确！,请查看是否过期或者输入错误";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 每日提现额度设置
		RuleModel rule = new RuleModel(Global.getRule("cash_limit"));
		if(rule!=null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue() && rule.getValueIntByKey("daily") == EnumRuleStatus.RULE_STATUS_YES.getValue()){
			double dailyMostCash = Double.parseDouble(Global.getValue("daily_most_cash"));
			double lowestCash = Double.parseDouble(Global.getValue("lowest_cash"));
			double dailySum = accountService.getAccountCashDailySum(user_id);
			double dailySurplusCash = dailyMostCash - dailySum;
			if(dailySurplusCash < lowestCash){
				message("您今日提现（包括提现成功和正在申请的提现）已达到每日额度" + dailyMostCash + "元，请明天再来提现", "/member/index.html");
				return MSG;
			} else if(!"newcash".equals(type)){
				request.setAttribute("dailySurplusCash", dailySurplusCash);
			}
			if("newcash".equals(type) && StringUtils.isNumber(money) && Double.parseDouble(money) > dailySurplusCash){//提现时，若money大于今日提现剩余额度
				money = dailySurplusCash+"";
			}
		}
		if (StringUtils.isNull(type).equals("newcash")) {
			errormsg = checkNewCash(user, detailAct);
			if (errormsg.equals("")) {
				AccountCash cash = new AccountCash();
				if (Global.getWebid().equals("wzdai")) {
					String bankAccountString = paramString("account");
					if (StringUtils.isBlank(bankAccountString)) {
						String[] bank = bankAccountString.split(",");
						String bankAccount = bank[2];
						detailAct = accountService.getAccountByBankAccount(user.getUser_id(), bankAccount);
					}
				}
				if (StringUtils.isNull(Global.getWebid()).equals("jsy")) {
					int cash_Num = Global.getInt("cash_Num");
					int cashNum = accountService.getAccountCashList(user_id, 1);
					if (cashNum >= cash_Num) {
						message("今日已成功提现" + cash_Num + "次，请明天再来提现", "/member/cash.html");
						return MSG;
					}

				}
				cash.setUser_id(user_id);
				cash.setBank(detailAct.getBank());
				cash.setAccount(detailAct.getBankaccount());
				cash.setBranch(detailAct.getBranch());
				cash.setAddtime(new Date().getTime() / 1000 + "");
				cash.setAddip(getRequestIp());
				cash.setTotal(money);
				double moneyd = NumberUtils.getDouble(money);
				try {
					accountService.accountCash(cash, detailAct, moneyd,true);
				} catch (AccountException e) {
					isOk = false;
					checkMsg = e.getMessage();
					logger.error(e.getMessage(), e.getCause());
				} catch (Exception e) {
					isOk = false;
					checkMsg = "系统繁忙，提现失败,请稍后再试！";
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				if (!isOk) {
					logger.debug("提现失败！");
					message(checkMsg, "/member/account/newcash.html");
					return MSG;
				}

				String msg = "申请提现成功，请等待管理员审核！<a href=\"cash.html\" >查看提现记录</a>&nbsp;&nbsp;<a href=\"newcash.action\">继续提现</a>";
				request.setAttribute("msg", msg);
			} else {
				request.setAttribute("errormsg", errormsg);
			}
		} else {
			saveToken("newcash_token");
			//获取当日的提现金额
			Date today=DateUtils.getLastSecIntegralTime(new Date());
			Date lastDay=DateUtils.rollDay(today, -1);
			double cashSum = accountService.getAccountApplyCashSum(user_id, DateUtils.getTime(lastDay), DateUtils.getTime(today));
			request.setAttribute("cashSum", cashSum);
		}
		if (detailAct != null && detailAct.getBankaccount() != null) {
			detailAct.setBankaccount(StringUtils.hideLastChar(detailAct.getBankaccount(), 4));
		}
		UserAccountSummary uas = accountService.getUserAccountSummary(user_id);
		SearchParam param = new SearchParam();
		// v1.6.7.2 RDPROJECT-603 xx 2013-12-17 start
		param.setUser_id(user.getUser_id());
		request.setAttribute("banklist", accountService.getAccountList(param));
		// v1.6.7.2 RDPROJECT-603 xx 2013-12-17 end
		request.setAttribute("uas", uas);
		request.setAttribute("account", detailAct);
		request.setAttribute("lowest_cash", Global.getValue("lowest_cash"));
		return "success";
	}
	
	public String getAvailableCashMoney() throws Exception {
		long user_id = getSessionUser().getUser_id();
		AccountCash cash = new AccountCash();
		cash.setUser_id(user_id);
		cash.setTotal(money);
		AccountModel detailAct = accountService.getAccount(user_id);
		Map<String,Object> map=new HashMap<String,Object>();
		
		if (detailAct == null) {
			map.put("msg", "用户id=" + user_id + "不存在！");
		} else {
			accountService.accountCash(cash, detailAct, NumberUtils.getDouble(money), false);
			map.put("msg", "success");
			map.put("data", cash);
		}
		printJson(JSON.toJSONString(map));
		return null;
	}
	
	private String checkNewCash(User user, AccountModel detailAct) {
		String errormsg = "";
		String paypwdStr = StringUtils.isNull(paypassword);
		String mypaypwd = StringUtils.isNull(user.getPaypassword());
		String site_id = StringUtils.isNull(Global.getValue("webid"));
		double repayTotalWithJin = borrowService.getRepayTotalWithJin(user.getUser_id());
		UserAccountSummary uas = accountService.getUserAccountSummary(user.getUser_id());

		double userOwnMoney = NumberUtils.getDouble(Global.getValue("userownmoney"));
		double borrowOwnMoneyRatio = NumberUtils.getDouble(Global.getValue("borrow_ownmoney_ratio"));
		
		if (paypwdStr.equals("")) {
			errormsg = "请输入支付密码，请先<a href=" + request.getContextPath()
					+ "'/memberSecurity/paypwd.html'><font color='red'><strong>填写</strong></font></a>";
			return errormsg;
		}
		MD5 md5 = new MD5();
		paypwdStr = md5.getMD5ofStr(paypwdStr);
		if (!mypaypwd.equals(paypwdStr)) {
			errormsg = "支付密码不正确，请先<a href=\"" + request.getContextPath()
					+ "/memberSecurity/paypwd.html\"><font color='red'><strong>填写</strong></font></a>";
			return errormsg;
		}
		double lowest_cash = NumberUtils.getDouble(Global.getValue("lowest_cash"));
		double cash_money = NumberUtils.getDouble(money);

		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>您还存在未还款的净值标,您的提现金额大于(可用余额+待收总额-净值标待还本息):");
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>可用余额:"+uas.getAccountUseMoney());
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>待收总额:"+uas.getCollectTotal());
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>净值标待还本息:"+repayTotalWithJin);
		
		if (money == null) {
			errormsg = "您的提现金额不能为空！";
			return errormsg;
		} else if (cash_money > NumberUtils.format2(detailAct.getUse_money())) {
			errormsg = "您的提现金额大于你所有的可用余额";
			return errormsg;
		} else if (cash_money > uas.getAccountOwnMoney() && (site_id.equals("zrzb") || site_id.equals("xdcf"))) {
			errormsg = "您的提现金额大于你的净资产";
			return errormsg;
		}
		// v1.6.7.1 RDPROJECT-467 2013-11-19 start
		else if (cash_money > (uas.getAccountUseMoney()+uas.getCollectTotal()-repayTotalWithJin)) {
			errormsg = "您还存在未还款的净值标,您的提现金额大于(可用余额+待收总额-净值标待还本息)";
			return errormsg;
		}
		// v1.6.7.1 RDPROJECT-467 2013-11-19 end
		else if ((site_id.equals("jsdai")) && (borrowService.getRepayTotalWithJin(user.getUser_id()) > 0) && (uas.getRechargeTotal() -uas.getCashTotal() < userOwnMoney)) {
		     errormsg = "您还存在未还款的净值标,您的(成功充值总额-已成功提现总额)必须大于"+userOwnMoney+"元,方可进行提现";
		     return errormsg;   
		}else if ((site_id.equals("jsdai")) && (borrowService.getRepayTotalWithJin(user.getUser_id()) > 0) && (cash_money > (uas.getRechargeTotal() -uas.getCashTotal() -(borrowService.getRepayTotalWithJin(user.getUser_id())/borrowOwnMoneyRatio)))) {
		     errormsg = "您还存在未还款的净值标,您的提现金额不能大于你的(成功充值总额￥"+uas.getRechargeTotal()+"-已成功提现总额￥"+uas.getCashTotal()+"-(净值待还总额￥"+String.valueOf(borrowService.getRepayTotalWithJin(user.getUser_id()))+"除以净值借款倍率)(现净值借款倍率为"+borrowOwnMoneyRatio+"倍))";
		     return errormsg; 
		}else if (Math.floor(cash_money) != cash_money && site_id.equals("huidai")) {
			errormsg = "提现金额只能为整数！";
			return errormsg;
		} else if (cash_money < lowest_cash) {
			errormsg = "不能小于最低提现金额";
			return errormsg;
		} else {
			String tokenMsg = checkToken("newcash_token");
			if (!StringUtils.isBlank(tokenMsg)) {
				return tokenMsg;
			}
		}
		return errormsg;
	}
	
	//短信手机验证
	public boolean phoneSms() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		String valicode = paramString("valicode");
		String code_number = (String) session.get("code_number");
		String errormsg = "";
		String phoneUrl = "/member/identify/phone.html";
		String phone=StringUtils.isNull(user.getPhone());
		if (phone == null || phone.equals("")) {
			errormsg = "手机号码为空,不能进行验证！";
			return false;
		}
		long date = DateUtils.getTime(new Date());
		long preDate=0;
		Object object=session.get("nowdate");
		if(object==null){
			object="";
		}
		if(!"".equals(object)){
			preDate=Long.parseLong(object.toString());
		}
		if (!StringUtils.isBlank(code_number)) {
			if (!code_number.equals(valicode)) {
				errormsg = "短信验证码不正确！,请查看是否过期或者输入错误";
				return false;
			} else {
				return true;
			}
		} 
		return false;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	public void setValicode(String valicode) {
		this.valicode = valicode;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public void setPaypassword(String paypassword) {
		this.paypassword = paypassword;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}