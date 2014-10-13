package com.p2psys.web.action.admin;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.p2psys.domain.Repayment;
import com.p2psys.model.AccountTypeModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.PaymentSumModel;
import com.p2psys.model.SearchParam;
import com.p2psys.service.AccountService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.UserService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 每日报表统计
 
 *
 */
public class ManageReportAction extends BaseAction {
	/**
	 * 输出日志 
	 */
	private final static Logger logger = Logger.getLogger(ManageReportAction.class);
	
	private UserService userService;
	
	private AccountService accountService;
	
	private BorrowService borrowService;
	
	/**
	 * 每日注册用户数
	 * @return String
	 */
	public String dayRegisterAccount() throws Exception {
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		// 每日新注册用户数
		int registerAccount = userService.getDayRegister();
		// 最近一周未完成四项认证的用户
		List weekRegisterList = userService.getWeekUnAttestation(dotime1, dotime2);
		request.setAttribute("dotime1", dotime1);
		request.setAttribute("dotime2", dotime2);
		request.setAttribute("registerAccount", registerAccount);
		request.setAttribute("weekRegisterList", weekRegisterList);
		return SUCCESS;
	}
	
	/**
	 * 每日充值合计
	 * @return String
	 */
	public String dayRechargeAccount() throws Exception {
		// 每日充值总额
		double sumAccount = accountService.getDayRechargeAccount();
		// 每日线下充值总额
		double offlineAccount = accountService.getDayOfflineRechargeAccount();
		// 每日网上充值总额
		double onlineAccount = accountService.getDayOnlineRechargeAccount();
		// 第三方支付每日充值总额
		List<PaymentSumModel> list = accountService.getDayPaymentAccount();
		request.setAttribute("sumAccount", sumAccount);
		request.setAttribute("offlineAccount", offlineAccount);
		request.setAttribute("onlineAccount", onlineAccount);
		request.setAttribute("list", list);
		return SUCCESS;
	}
	
	/**
	 * 每日融资数据统计
	 * @return String
	 */
	public String dayBorrowAccount() throws Exception {
		// 每日发标金额总计
		double firstVerifyAccount = borrowService.getDayFirstVerifyAccount();
		// 每日总的投标金额（复审通过）
		double fullVerifyAccount = borrowService.getDayFullVerifyAccount();
		// 每日复审利息总额
		double dayInterestAccount = borrowService.getDayInterestAccount();
		// 奖励发放总额
		double dayAwardAccount = borrowService.getDayAwardAccount();
		// 各标种投标总额
		List<AccountTypeModel> accountList = borrowService.getBorrowTypeAccount();
		// 各标种利息总额
		List<AccountTypeModel> awardList = borrowService.getBorrowAwardAccount();
		AccountTypeModel model = null;
		for (int i = 0; i < accountList.size(); i++) {
			model = accountList.get(i);
			model.setAward(awardList.get(i).getAward());
		}
		request.setAttribute("firstVerifyAccount", firstVerifyAccount);
		request.setAttribute("fullVerifyAccount", fullVerifyAccount);
		request.setAttribute("dayInterestAccount", dayInterestAccount);
		request.setAttribute("dayAwardAccount", dayAwardAccount);
		request.setAttribute("accountList", accountList);
		return SUCCESS;
	}
	
	/**
	 * 每日到期金额合计
	 * @return String
	 */
	/**
	 * @return
	 * @throws Exception
	 */
	public String dayAccount() throws Exception {
		// v1.6.7.1 添加分页  zza 2013-11-25 start
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		// v1.6.7.1 添加分页  zza 2013-11-25 end
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		// v1.6.7.1 添加分页  zza 2013-11-25 start
		int page = NumberUtils.getInt(request.getParameter("page"));
		SearchParam param = new SearchParam();
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		// v1.6.7.1 添加分页  zza 2013-11-25 end
		// 每日到期金额合计
		double dayMatureAccount = borrowService.getDayMatureAccount(dotime1, dotime2);
		// 每日到期本金合计
		double dayCapitalAccount = borrowService.getDayCapitalAccount(dotime1, dotime2);
		// 每日到期利息合计
		double dayMatureInterest = borrowService.getDayMatureInterest(dotime1, dotime2);
		// 每日投标时发放的奖励
		double dayTenderFunds = borrowService.getDayTenderFunds(dotime1, dotime2);
		// 每日还款时发放的奖励
		double dayRepaymentFunds = borrowService.getDayRepaymentFunds(dotime1, dotime2);
		// 每日还款本金合计
		double dayReapyCapital = borrowService.getDayRepayCapital(dotime1, dotime2);
		// 每日还款利息合计
		double dayRepayInterest = borrowService.getDayRepayInterest(dotime1, dotime2);
		// v1.6.7.1 添加分页  zza 2013-11-25 start
		// 当天需还款的借款标
		PageDataList dayMatureList = borrowService.getDayMatureList(page, param, dotime1, dotime2);
		// v1.6.7.1 添加分页  zza 2013-11-25 end
		request.setAttribute("dayMatureAccount", dayMatureAccount);
		request.setAttribute("dayCapitalAccount", dayCapitalAccount);
		request.setAttribute("dayMatureInterest", dayMatureInterest);
		request.setAttribute("dayTenderFunds", dayTenderFunds);
		request.setAttribute("dayRepaymentFunds", dayRepaymentFunds);
		request.setAttribute("dayReapyCapital", dayReapyCapital);
		request.setAttribute("dayRepayInterest", dayRepayInterest);
		// v1.6.7.1 添加分页  zza 2013-11-25 start
		request.setAttribute("page", dayMatureList.getPage());
		request.setAttribute("dayMatureList", dayMatureList.getList());
		request.setAttribute("param", param.toMap());
		if (StringUtils.isBlank(actionType)) {
			return SUCCESS;
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "dayAccount_" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "/data/export/" + downloadFile;
			String[] names = new String[] { "id", "username", "realname", "borrow_name", "real_order", "repayment_time",
					"repayment_account", "capital", "interest", "repayment_yestime", "status" };
			String[] titles = new String[] { "ID", "借款人", "真实姓名", "借款标题", "期数", "到期时间", "还款金额", "还款本金", "还款利息", "还款时间",
					"状态" };
			List<Repayment> list = borrowService.getDayMatureList(param, dotime1, dotime2);
			ExcelHelper.writeExcel(infile, list, Repayment.class, Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
		// v1.6.7.1 添加分页  zza 2013-11-25 end
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the accountService
	 */
	public AccountService getAccountService() {
		return accountService;
	}

	/**
	 * @param accountService the accountService to set
	 */
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * @return the borrowService
	 */
	public BorrowService getBorrowService() {
		return borrowService;
	}

	/**
	 * @param borrowService the borrowService to set
	 */
	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	
}
