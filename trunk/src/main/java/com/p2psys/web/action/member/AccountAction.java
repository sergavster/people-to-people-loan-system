package com.p2psys.web.action.member;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.eitop.platform.tools.encrypt.MD5Digest;
import com.eitop.platform.tools.encrypt.xStrEncrypt;
import com.p2psys.common.enums.EnumPayInterface;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountBank;
import com.p2psys.domain.AccountCash;
import com.p2psys.domain.AccountRecharge;
import com.p2psys.domain.Huikuan;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.exception.AccountException;
import com.p2psys.model.DetailUser;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.account.AccountCashList;
import com.p2psys.model.account.AccountLogList;
import com.p2psys.model.account.AccountLogModel;
import com.p2psys.model.account.AccountModel;
import com.p2psys.model.account.AccountRechargeList;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.noac.VerifyPhoneCodeLog;
import com.p2psys.payment.AllInPay;
import com.p2psys.payment.BaoFooPay;
import com.p2psys.payment.ChinaBankPay;
import com.p2psys.payment.Dinpay;
import com.p2psys.payment.EbatongPay;
import com.p2psys.payment.EcpssPay;
import com.p2psys.payment.Epay;
import com.p2psys.payment.Epaylinks;
import com.p2psys.payment.GoPay;
import com.p2psys.payment.IPSPay;
import com.p2psys.payment.RongPay;
import com.p2psys.payment.ShengPay;
import com.p2psys.payment.TranGood;
import com.p2psys.payment.Unspay;
import com.p2psys.payment.XsPay;
import com.p2psys.payment.YbPay;
import com.p2psys.payment.YiShengPay;
import com.p2psys.payment.tenpay.TenpayRequestHandler;
import com.p2psys.service.AccountService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.UserService;
import com.p2psys.tool.coder.MD5;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.util.tenpayUtil.EpayUtil;
import com.p2psys.web.action.BaseAction;

public class AccountAction extends BaseAction {

	private final static Logger logger = Logger.getLogger(AccountAction.class);

	private AccountService accountService;

	private UserService userService;
	
	private BorrowService borrowService;

	private String valicode;
	private String money;
	private String paypassword;
	private String type;
	// 银行账户相关
	private String account;
	private String branch;
	private String bank;

	// 搜索条件
	// 查询条件
	private String dotime1;
	private String dotime2;
	private int page;
	private String account_type;
	private int token;
	private String status;
	private String huikuan_money;
	private String huikuan_award;
	private String remark;
	private long is_day;
	//v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
	private int province;
	private int city;
	private int area;

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}
	//v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
	// Action方法
	public String list() {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();

		request.setAttribute("dotime2", DateUtils.dateStr2(new Date()));
		String dotime1 = DateUtils.dateStr2(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		request.setAttribute("dotime1", dotime1);

		UserAccountSummary summary = accountService.getUserAccountSummary(user_id);

		request.setAttribute("summary", summary);

		return "success";
	}

	private void fillSearchParam(SearchParam param) {
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setAccount_type(account_type);
		param.setStatus(status);
	}

	public String log() throws Exception {
		String type = StringUtils.isNull(request.getParameter("type"));
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		SearchParam param = new SearchParam();
		fillSearchParam(param);
		AccountLogList accountLogList = accountService.getAccountLogList(user_id, page, param);
		double total = accountService.getAccountLogTotalMoney(user_id);
		request.setAttribute("log", accountLogList.getList());
		request.setAttribute("p", accountLogList.getPage());
		// 保存资金流水量
		request.setAttribute("total", total);
		request.setAttribute("param", param.toMap());
		if (type.isEmpty()) {
			return "success";
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile = "log_" + System.currentTimeMillis() + ".xls";
			String infile = contextPath + "data/export/" + downloadFile;
			String[] names = new String[] { "username", "to_username", "typename", "money", "total", "use_money",
					"no_use_money", "collection", "remark", "addtime" };
			String[] titles = new String[] { "用户名", "交易对方", "交易类型", "操作金额", "账户总额", "可用金额", "冻结金额", "待收金额", "备注", "时间" };
			List list = accountService.getAccountLogList(user_id, param);
			ExcelHelper.writeExcel(infile, list, AccountLogModel.class, Arrays.asList(names), Arrays.asList(titles));
			this.export(infile, downloadFile);
			return null;
		}
	}

	public String cash() {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		SearchParam param = new SearchParam();
		fillSearchParam(param);
		int start = NumberUtils.getInt(request.getParameter("page"));
		AccountCashList cash = accountService.getAccountCashList(user_id, start, param);
		UserAccountSummary summary = accountService.getUserAccountSummary(user_id);
		request.setAttribute("cash", cash.getList());
		request.setAttribute("p", cash.getPage());
		request.setAttribute("param", param.toMap());
		request.setAttribute("summary", summary);
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
			accountService.newCash(cash, detailAct, NumberUtils.getDouble(money), false);
			map.put("msg", "success");
			map.put("data", cash);
		}
		printJson(JSON.toJSONString(map));
		return null;
	}
	//短信手机验证
		public boolean phoneSms() throws Exception {
			User user = (User) session.get(Constant.SESSION_USER);
			String valicode = StringUtils.isNull(request.getParameter("valicode"));
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
			/*if(session.get("nowdate") != null && (date - preDate) > 60){
				  session.remove(code_number); session.remove(preDate);
				  code_number=code(); 
			} */
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
		public void json(String returnmessage){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("data", returnmessage);
			try {
				printJson(JSON.toJSONString(map));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	public String mobileaccess(){
		String code_number = code();
		// 获取当前系统时间
		long date = DateUtils.getTime(new Date());
		String returnmessage = "";
		if (null != session.get("nowdate")) {
			logger.info("当前系统时间" + date + "上一次获取时间" + session.get("nowdate"));
			long preDate = Long.parseLong(session.get("nowdate").toString());
			if (session.get("nowdate") != null && (date - preDate) < Global.getInt("verify_code_time")) {
				returnmessage = "本次短信验证码已发出，请60秒后重试。如果超过60秒还没有输该验证码，请重新获取";
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("data", returnmessage);
				try {
					printJson(JSON.toJSONString(map));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			/*if(session.get("nowdate") != null && (date - preDate) > 60){
				session.remove(code_number);
				session.remove(preDate);
				code_number=code();
			}*/
		}
		// 随机数
		User user = (User) session.get(Constant.SESSION_USER);

		session.put("code_number", code_number);
		date = DateUtils.getTime(new Date());
		session.put("nowdate", date);
		
		/*
		NoticeConfig noticeConfig = Global.getNoticeConfig("phone_code");
		String mobile = request.getParameter("mobile");
		Notice s = new Sms();
		if (noticeConfig.getSms() == 1) {
			s.setReceive_userid(user.getUser_id());
			s.setSend_userid(1);
			s.setContent("尊敬的" + Global.getValue("webname") + "用户["
					+ user.getUsername() + "]，您于"
					+ DateUtils.dateStr5(s.getAddtime()) + "所获取手机验证码为"
					+ code_number);
			s.setMobile(mobile);
			NoticeJobQueue.NOTICE.offer(s);
		}
		*/
		
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
		//TODO RDPROJECT-314 DONE

		/*long userid = user.getUser_id();
		Smstype smstype = Global.getSmstype(Constant.SMS_PHONE_CODE);
		boolean isSmssend = userService.isSmssend(userid, smstype);
		if(isSmssend){
			Notice s=new Sms();
			
			//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 start

			
			Global.setTransfer("webname", Global.getValue("webname"));
			Global.setTransfer("user", user);
			Global.setTransfer("sms", s);
			Global.setTransfer("code", code_number);
			Global.setTransfer("vtime", Global.getString("verify_code_time"));
			
		   s.setAddtime(s.getAddtime());
//		   s.setContent(StringUtils.fillTemplet(smstype.getTemplet(), Constant.SMS_TEMPLET_PHSTR, paras));
		   s.setContent(StringUtils.fillTemplet(smstype.getTemplet()));
		   //V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 end
		   s.setReceive_userid(userid);
		   s.setSend_userid(1);
		   NoticeJobQueue.NOTICE.offer(s);
		}*/
		long user_id = user.getUser_id();
		Global.setTransfer("code", code_number);
		Global.setTransfer("vtime", Global.getString("verify_code_time"));
		Global.setTransfer("vtime", Global.getString("verify_code_time"));
		BaseAccountLog blog=new VerifyPhoneCodeLog(user_id);
		blog.doEvent();
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end

		return null;
	}
	/*
	 * 四位随机数生成 
	 */
	public static String code() {
		Set<Integer> set = GetRandomNumber();
		// 使用迭代器
		Iterator<Integer> iterator = set.iterator();
		// 临时记录数据
		String temp = "";
		while (iterator.hasNext()) {
			temp += iterator.next();
			// System.out.print(iterator.next());
		}
		return temp;
	}
	public static Set<Integer> GetRandomNumber() {
		// 使用SET以此保证写入的数据不重复
		Set<Integer> set = new HashSet<Integer>();
		// 随机数
		Random random = new Random();

		while (set.size() < 4) {
			// nextInt返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）
			// 和指定值（不包括）之间均匀分布的 int 值。
			set.add(random.nextInt(10));
		}
		return set;
	}
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public String newcash() {
		boolean isOk = true;
		String checkMsg = "";
		long user_id = getSessionUser().getUser_id();
		User user = userService.getUserById(user_id);
		// update_start
		// AccountModel detailAct=accountService.getAccount(user_id);
		String account = request.getParameter("account");
		AccountModel detailAct = null;
		if (StringUtils.isBlank(account)) {
			detailAct = accountService.getAccount(user_id);
		} else {
			detailAct = accountService.getAccountByBankAccount(user_id, account);
		}
		// update_end
		String errormsg = "";
		if (detailAct == null || StringUtils.isNull(detailAct.getBankaccount()).equals("")) {
			errormsg = "您的银行账号还没填写，请先<a href=\"bank.html\"><font color='red'><strong>填写</strong></font></a>";
		}
		// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
		if (user.getReal_status()!=1) {
			errormsg = "您还未通过实名认证，请先<a href=\"" + request.getContextPath()
					+ "/member/identify/realname.html\"><font color='red'><strong>填写</strong></font></a>";
		}
		if (user.getPhone_status()!=1) {
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// v1.6.5.3 RDPROJECT-96 xx 2013.09.10 start
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
		// v1.6.5.3 RDPROJECT-96 xx 2013.09.10 end
		if (StringUtils.isNull(type).equals("newcash")) {
			errormsg = checkNewCash(user, detailAct);
			if (errormsg.equals("")) {
				AccountCash cash = new AccountCash();
				if (Global.getWebid().equals("wzdai")) {
					String bankAccountString = request.getParameter("account");
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
				// V1.6.6.1 liukun 2013-09-10 start
				//AccountLog log = new AccountLog(user_id, Constant.CASH_FROST, 1, getTimeStr(), getRequestIp());
				//log.setRemark("提现冻结" + money + "元");
				// TODO lk need new log cash_frost
				
				//Global.setTransfer("cash", cash);
				//BaseAccountLog bLog = new CashApplyLog(moneyd, detailAct);
				// V1.6.6.1 liukun 2013-09-10 end
				
				try {
					// V1.6.6.1 liukun 2013-09-10 start
					//accountService.newCash(cash, detailAct, moneyd, log);
					accountService.newCash(cash, detailAct, moneyd);
					
					// V1.6.6.1 liukun 2013-09-10 end
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
					// v1.6.5.3 RDPROJECT-155 xx 2013.09.12 start
					message(checkMsg, "/member/account/newcash.html");
					// v1.6.5.3 RDPROJECT-155 xx 2013.09.12 end
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
			
			//v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 begin
			int cash_forbid = userService.isCashForbid(user_id);
			if (cash_forbid==1){
				String userStatusDesc = userService.getUserStatusDesc(user_id);
				request.setAttribute("cash_forbid", cash_forbid);
				request.setAttribute("errormsg", "您的帐户当前被禁止提现，无法继续进行提现申请！<br>" + userStatusDesc);
				
			}
			//v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 end
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

	public String cancelcash() throws Exception {
		String checkMsg = "";
		boolean isOk = true;
		long id = NumberUtils.getLong(request.getParameter("id"));
		// V1.6.6.1 liukun 2013-09-10 start
		// TODO lk need new log cash_cancel
		/*AccountLog log = new AccountLog(getSessionUser().getUser_id(), Constant.CASH_CANCEL, getSessionUser()
				.getUser_id(), getTimeStr(), getRequestIp());*/
		// V1.6.6.1 liukun 2013-09-10 end
		

		
		try {
			// V1.6.6.1 liukun 2013-09-10 start
			//accountService.cancelCash(id, log);
			accountService.cancelCash(id);
			// V1.6.6.1 liukun 2013-09-10 end
		} catch (AccountException e) {
			isOk = false;
			checkMsg = e.getMessage();
		} catch (Exception e) {
			isOk = false;
			checkMsg = "系统异常";
		}
		if (isOk) {
			message("取消提现成功！", "/member/account/cash.html");
		} else {
			message(checkMsg, "/member/account/cash.html");
		}
		return MSG;
	}

	public String bank() {
		String wzlx = request.getParameter("wzlx");
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		AccountModel detailAct = accountService.getAccount(user_id);

		String typeStr = StringUtils.isNull(type);
		if (typeStr.equals("add")) {
			if (detailAct == null) {
				Account act = new Account();
				act.setUser_id(user_id);
				accountService.addAccount(act);
			}
			AccountBank actbank = new AccountBank();
			if (StringUtils.isNull(branch).equals("")) {
				request.setAttribute("errormsg", "开户行名称不能为空");
				message("开户行名称不能为空", "/member/account/bank.html");
				return "error";
			}
			if (StringUtils.isNull(account).equals("")) {
				request.setAttribute("errormsg", "银行账号不能为空");
				message("银行账号不能为空", "/member/account/bank.html");
				return "error";
			}
			//v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
			if(province==0||city==0){
				request.setAttribute("errormsg", "省市不能为空");
				message("省市不能为空", "/member/account/bank.html");
				return "error";
			}
			//v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
			if (StringUtils.isNull(wzlx).equals("")) {
				actbank.setAccount(account);
			} else if (StringUtils.isNull(wzlx).equals("hxdai")) {
				String d = "";
				String a = "";
				int j = account.length();
				int k = j / 4 * 4;
				for (int i = 0; i < k; i++) {
					if (i % 4 == 0) {
						if (j > i + 4) {
							d = account.substring(i, i + 4);
						}
						a = a + d + " ";

					}
				}
				if (j - k == 3) {
					String e = account.substring(k, j);
					a = a + e;
				} else if (j - k == 2) {
					String e = account.substring(k, j);
					a = a + e;
				} else if (j - k == 1) {
					String e = account.substring(k, j);
					a = a + e;
				}
				logger.debug("a=====" + a);
				actbank.setAccount(a);
			}
			actbank.setBranch(branch);
			actbank.setUser_id(user_id);
			actbank.setBank(bank);
			//v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
			actbank.setProvince(province);
			actbank.setCity(city);
			actbank.setArea(area);
			//v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
			if (detailAct.getBank() == null || detailAct.getBank().equals("")) {
				accountService.addBank(actbank);
				message("添加成功！", "/member/account/bank.html");
				return MSG;
			} else {
				accountService.modifyBank(actbank);
			}
			detailAct = accountService.getAccount(user_id);
			if (detailAct != null) {
				if (detailAct.getBank() != null) {
					detailAct.setBankaccount(StringUtils.hideLastChar(detailAct.getBankaccount(), 4));
				}
				request.setAttribute("act", detailAct);
			}
		} else {
			if (detailAct != null) {
				SearchParam param = new SearchParam();
				// v1.6.7.2 RDPROJECT-603 xx 2013-12-17 start
				param.setUser_id(user.getUser_id());
				request.setAttribute("banklist", accountService.getAccountList(param));
				// v1.6.7.2 RDPROJECT-603 xx 2013-12-17 end
				if (detailAct.getBank() != null) {
					detailAct.setBankaccount(StringUtils.hideLastChar(detailAct.getBankaccount(), 4));
				}
				request.setAttribute("act", detailAct);
			}
		}

		return "success";
	}

	/**
	 * 添加银行卡
	 * 
	 * @return
	 */
	public String addbank() {
		String wzlx = request.getParameter("wzlx");
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		String typeStr = StringUtils.isNull(type);
		int countTotal = accountService.getAccountBankCount(user_id);
		int defaultTotal = Global.getInt("bank_num");
		if (countTotal >= defaultTotal) {
			request.setAttribute("errormsg", "添加银行不能超过最大数");
			message("添加银行不能超过最大数", "/member/account/bank.html");
			return "error";
		}
		if ("add".equals(typeStr)) {
			AccountBank actbank = new AccountBank();
			if (StringUtils.isNull(branch).equals("")) {
				request.setAttribute("errormsg", "开户行名称不能为空");
				message("开户行名称不能为空", "/member/account/bank.html");
				return "error";
			}
			if (StringUtils.isNull(account).equals("")) {
				request.setAttribute("errormsg", "银行账号不能为空");
				message("银行账号不能为空", "/member/account/bank.html");
				return "error";
			}
			actbank.setAccount(account);
			actbank.setBranch(branch);
			actbank.setUser_id(user_id);
			actbank.setBank(bank);
			accountService.addBank(actbank);
			message("添加成功！", "/member/account/bank.html");
			return MSG;
		}
		return SUCCESS;
	}

	public String recharge() {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		SearchParam param = new SearchParam();
		fillSearchParam(param);
		AccountRechargeList accountRechargeList = accountService.getRechargeList(user_id, page, param);
		UserAccountSummary summary = accountService.getUserAccountSummary(user_id);
		double rechargeSum = accountService.getRechargesum(param, 1);
		request.setAttribute("recharge", accountRechargeList.getList());

		request.setAttribute("p", accountRechargeList.getPage());
		request.setAttribute("param", param.toMap());
		request.setAttribute("rechargeSum", rechargeSum);
		request.setAttribute("summary", summary);
		return "success";
	}
	
	/**
	 * 充值前的各种认证效验
	 */
	public String checkNewRechargeAttestation(User u){
		//获取充值认证规则
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.NEWRECHARGE_ATTESTATION.getValue()));
		if(rule != null&& rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
			int email_attestation = rule.getValueIntByKey("email");
			int phone_attestation = rule.getValueIntByKey("phone");
			int video_attestation = rule.getValueIntByKey("video");
			int scene_attestation = rule.getValueIntByKey("scene");
			int realname_attestation = rule.getValueIntByKey("realname");
			int vip_attestation = rule.getValueIntByKey("vip");
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			if(realname_attestation == 1){
				if(u.getReal_status()!=1){
					message("请先进行实名认证！", "");
					return MSG;
				}
			}
			if(phone_attestation == 1){
				if(u.getPhone_status()!=1){
					message("请先进行手机认证！", "");
					return MSG;
				}
			}
			if(email_attestation == 1){
				if(u.getEmail_status()!=1){
					message("请先进行邮箱认证！", "");
					return MSG;
				}
			}
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
			if(video_attestation == 1){
				if(u.getVideo_status() != 1){
					message("请先通过视频认证！", "");
					return MSG;
				}
			}
			if(scene_attestation == 1){
				if(u.getScene_status() != 1){
					message("请先通过现场认证！", "");
					return MSG;
				}
			}
			if(vip_attestation == 1){
				if(u.getVip_status() != 1){
					message("请先通过VIP认证！", "");
					return MSG;
				}
			}
		}
		return "";
	}
	
	public String newrecharge() {
		if (Global.getWebid().equals("jsy")) {
			User user = (User) session.get(Constant.SESSION_USER);
			long user_id = user.getUser_id();
			DetailUser detailUser = userService.getDetailUser(user_id);
			//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
			List list = userService.getKfList(1);
			//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
			request.setAttribute("kflist", list);
			request.setAttribute("detailUser", detailUser);
		}
		if (StringUtils.isBlank(type)) {
			User u= userService.getUserById(this.getSessionUser().getUser_id());
			//充值前各种认证效验
			String msg = checkNewRechargeAttestation(u);
			if("msg".equals(msg)){
				return MSG;
			}
			List downBankList = accountService.downBank();
			List<PaymentInterface> interfaceList = accountService.paymentInterface_unsingle(1);
			List<PaymentInterface> singleInterfaceList = accountService.paymentInterface_single(1);
			PaymentInterface paymentInterface=new PaymentInterface();
			List onlineBankList=new ArrayList();
			List list=new ArrayList();
			for(int i=0;i<singleInterfaceList.size();i++){
				 paymentInterface=singleInterfaceList.get(i);
				 list = accountService.onlineBank(paymentInterface.getInterface_value());
				 onlineBankList.addAll(list);
			}
			if (onlineBankList.size() > 0) {
				request.setAttribute("onlineBankList", onlineBankList);
			}
			if (interfaceList.size() > 0) {
				request.setAttribute("interfaceList", interfaceList);
			}
			if (downBankList.size() > 0) {
				request.setAttribute("downBankList", downBankList);
			}
			return SUCCESS;
		} else {
			String payment = StringUtils.isNull(request.getParameter("payment1"));
			String payment2 = StringUtils.isNull(request.getParameter("payment2"));
			if (StringUtils.isNull(type).equals("1") && StringUtils.isBlank(payment)) {
				// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 start
				message("网上充值的充值方式不能为空！", "/member/account/newrecharge.html");
				// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 end
				return MSG;
			}
			if (StringUtils.isNull(type).equals("2") && StringUtils.isBlank(payment2)) {
				// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 start
				message("线下充值的充值银行不能为空！", "/member/account/newrecharge.html");
				// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 end
				return MSG;
			}
			if (NumberUtils.getDouble(money) <= 0) {
				// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 start
				message("充值的金额必须大于0！", "/member/account/newrecharge.html");
				// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 end
				return MSG;
			}
			if (!Global.getWebid().equals("wzdai")) {
				if (!checkValidImg(StringUtils.isNull(valicode))) {
					// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 start
					message("验证码不正确！", "/member/account/newrecharge.html");
					// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 end
					return MSG;
				}
			}
			/*if ("51".equals(payment)) {
				int num = money.indexOf(".");
				if (num > 0) {
					money = money.substring(0, num);
				}
			}*/
			AccountRecharge r = new AccountRecharge();
			User sessionUser = this.getSessionUser();
			r.setUser_id(sessionUser.getUser_id());
			r.setMoney(NumberUtils.getDouble(money));
			r.setType(type);
			if (Global.getWebid().equals("jsy")) {
				String recharge_kefu_id = request.getParameter("recharge_kefuid");
				if (!StringUtils.isBlank(recharge_kefu_id)) {
					r.setRecharge_kefuid(NumberUtils.getLong(recharge_kefu_id));
				} else {
					// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 start
					message("请填写充值专属客服！", "/member/account/newrecharge.html");
					// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 end
					return MSG;
				}
			}
			request.setAttribute("money", money);
			r.setAddtime(this.getTimeStr());
			r.setAddip(this.getRequestIp());

			// V1.6.6.1 RDPROJECT-97 liukun 2013-09-10 start	
			/*AccountLog log = new AccountLog(sessionUser.getUser_id(), Constant.RECHARGE, sessionUser.getUser_id(),
					this.getTimeStr(), this.getRequestIp());*/
			// V1.6.6.1 RDPROJECT-97 liukun 2013-09-10 end	
			
			//TODO lk need new log recharge
			double rechargefee = 0.0;

			if (StringUtils.isNull(type).equals("1")) {
				r.setRemark("用户网上充值" + r.getMoney() + "元");
/*				rechargefee = NumberUtils.getDouble(Global.getValue("online_rechargefee"));
				r.setFee(NumberUtils.format2(r.getMoney() * rechargefee) + "");*/
				r.setStatus(0);
				// V1.6.6.1 RDPROJECT-97 liukun 2013-09-10 start
				//log.setRemark("用户网上充值" + r.getMoney() + "元");
				// V1.6.6.1 RDPROJECT-97 liukun 2013-09-10 end
				
				// v1.6.7.2 RDPROJECT-587 lhm 2013-12-16 start
				String returnStr = "";
				// v1.6.7.2 RDPROJECT-587 lhm 2013-12-16 end

				r.setPayment(payment);
				/*if (payment.equals("32")) {*/
				if (payment.equals(EnumPayInterface.GOPAY.getValue())) {
					// 国付宝充值
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "E"));
					gopayment("1", "", r);
				} else if (payment.endsWith("_t")) {
					// 国付宝直连
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "E"));
					r.setPayment(EnumPayInterface.GOPAY.getValue());
					String backcode = payment.replace("_t", "");
					gopayment("1", backcode, r);
					logger.info(backcode);
				/*} else if (payment.equals("10")) {*/
				} else if (payment.equals(EnumPayInterface.IPS_PAY.getValue())) {
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "I"));
					ipspayment("", r);
					returnStr = "ips";
				} else if (payment.endsWith("_s")) {
					// 环讯直连
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "I"));
					r.setPayment(EnumPayInterface.IPS_PAY.getValue());
					String backcode = payment.replace("_s", "");
					ipspayment(backcode, r);
					returnStr =  "ips";
/*				} else if (payment.equals("11")) {
*/				} else if (payment.equals(EnumPayInterface.XS_PAY.getValue())) {
					// 新生支付
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "H"));
					xspayment(payment, r);
					returnStr =  "xsp";
				/*} else if (payment.equals("12")) {*/
                  } else if (payment.equals(EnumPayInterface.YB_PAY.getValue())) {
					// 易宝支付
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "Y"));
					ybpayment(payment, r);
					returnStr =  "ybp";
				} else if (payment.endsWith("_baofoo")) {
					// 宝付直连
					// 宝付支付 充值页面有标签 "_baofoo" 标注 是 宝付支付类型 ;
					logger.info("用户进入宝付直连...");
					// 获取订单号 带回有待确定 暂时用 B吧
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "B"));
					r.setPayment(EnumPayInterface.BAOFOO_PAY.getValue());
					// 返回前端信息
					// 获得 充值方式 编码： 比如交通银行 1020
					String bankCode = payment.split("_")[0];
					baoFooPayMent(bankCode, r);
					r.setPayment("");
					returnStr =  "baofoo";

				} else if (EnumPayInterface.BAOFOO_PAY.getValue().equals(payment)) {
					// 宝付 链接到 网银支付（总）区别于宝付支付 银行直连
					logger.info("用户进入宝付支付...");
					// 获取订单号 带回有待确定 暂时用 B吧
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "B"));
					// 返回前端信息
					// 获得 充值方式 编码： 比如交通银行 1020
					String bankCode = "1000";
					baoFooPayMent(bankCode, r);
					returnStr =  "baofoo";
				} else if (payment.equals(EnumPayInterface.CHINABANK_PAY.getValue())) {
					logger.info("用户进入网银在线支付...");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "W"));
					try {
						chinaBankPayment("", r);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					returnStr =  "cbpay";
				} else if (payment.endsWith("_w")) {
					// 网银在线直连
					logger.info("欢迎进入网银在线支付...");
					String bankCode = payment.replace("_w", "");
					// v1.6.5.3 RDPROJECT-153 xx 2013.09.12 start
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "W"));					
					r.setPayment(EnumPayInterface.CHINABANK_PAY.getValue());
					// v1.6.5.3 RDPROJECT-153 xx 2013.09.12 end
					logger.info(r.getTrade_no());
					try {
						chinaBankPayment(bankCode, r);
						returnStr =  "cbpay";
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (EnumPayInterface.ALLIN_PAY.getValue().equals(payment)) {
					logger.info("用户进入通联支付...");
					// 获取订单号
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "T"));
					allInpayment("1", r);
					r.setPayment(EnumPayInterface.ALLIN_PAY.getValue());
					returnStr =  "allinpay";
				} else if (EnumPayInterface.SHENG_PAY.getValue().equals(payment)) {
					logger.info("用户进入盛付通支付...");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "S"));
					ShengPayment("", r);
					returnStr =  "shengpay";
				} else if (payment.endsWith("_shengpay")) {
					logger.info("用户进入盛付通直连支付...");
					String bankCode = payment.replace("_shengpay", "");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "S"));
					r.setPayment(EnumPayInterface.SHENG_PAY.getValue());
					logger.info(r.getTrade_no());
					ShengPayment(bankCode, r);
					returnStr =  "shengpay";
				} else if (EnumPayInterface.DINPAY.getValue().equals(payment)) {
					logger.info("用户进入智付支付...");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "D"));
					// String bankCode = "1000";
					Dinpayment(r);
					returnStr =  "dinpay";
				} else if (EnumPayInterface.ECPSSPAY.getValue().equals(payment)) {
					logger.info("用户进入汇潮支付...");
					// 订单编号暂时以C开头
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "C"));
					Ecpsspayment("", r);
					returnStr =  "ecpssPay";
				} else if (payment.endsWith("_c")) {
					// 汇潮直连
					logger.info("用户进入汇潮直连支付...");
					String bankCode = payment.replace("_c", "");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "C"));
					r.setPayment(EnumPayInterface.ECPSSPAY.getValue());
					logger.info(r.getTrade_no());
					Ecpsspayment(bankCode, r);
					returnStr =  "ecpssPay";
				}else if (payment.endsWith("_ecpsschina")) {
                    // 汇潮中行积分直连通道
                    logger.info("用户进入汇潮中行积分通道直连支付...");
                    String bankCode = payment.replace("_ecpsschina", "");
                    r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "Z"));
                    r.setPayment(EnumPayInterface.ECPSSCHINA_PAY.getValue());
                    logger.info(r.getTrade_no());
                    Ecpsschinapay(bankCode, r);
                    returnStr = "ecpssPay";
                }else if (EnumPayInterface.TENPAY.getValue().equals(payment)) {
					logger.info("用户进入腾讯财付通支付...");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "Q"));
					try {
						Tenpay("DEFAULT", r);
						returnStr = "tenpay";
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (payment.endsWith("_q")) {
					// 财付通直连
					logger.info("用户进入财付通直连支付...");
					String bankCode = payment.replace("_q", "");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "Q"));
					r.setPayment(EnumPayInterface.TENPAY.getValue());
					logger.info(r.getTrade_no());
					try {
						Tenpay(bankCode, r);
						returnStr = "tenpay";
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (EnumPayInterface.RONGBAO_PAY.getValue().equals(payment)) {
					// 融宝支付
					logger.info("欢迎进入融宝支付...");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "R"));
					try {
						rongpayment("", r);
						returnStr = "rongpay";
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (payment.endsWith("_r")) {
					// 融宝直连
					logger.info("欢迎进入融宝支付...");
					String bankCode = payment.replace("_r", "");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "R"));
					r.setPayment(EnumPayInterface.RONGBAO_PAY.getValue());
					logger.info(r.getTrade_no());
					try {
						rongpayment(bankCode, r);
						returnStr = "rongpay";
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if(EnumPayInterface.ECPSSFAST_PAY.getValue().equals(payment)){
					logger.info("用户进入汇潮支付快捷支付...");
					r.setTrade_no(StringUtils.generateTradeNO(
							sessionUser.getUser_id(), "A"));
					EcpsspaymentFast("", r);
					returnStr = "ecpssPayFast";
				} 
				// v1.6.5.5 RDPROJECT-148 xx 2013-09-23 start
				else if (EnumPayInterface.YISHENG_PAY.getValue().equals(payment)) {
					// 易生支付
					logger.info("欢迎进入易生支付...");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "Y"));
					try {
						yishengpayment("", r);
						returnStr = "yishengpay";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else if (payment.endsWith("_yisheng")) {
					// 易生支付直连
					logger.info("欢迎进入易生支付...");
					String bankCode = payment.replace("_yisheng", "");
					r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "Y"));
					r.setPayment(EnumPayInterface.YISHENG_PAY.getValue());
					try {
						yishengpayment(bankCode, r);
						returnStr = "yishengpay";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				// v1.6.5.5 RDPROJECT-148 xx 2013-09-23 end
				// v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 start
				else if (EnumPayInterface.EPAY.getValue().equals(payment)) {
					// 双乾支付
					logger.info("欢迎进入双乾支付...");
					String tradeNo = StringUtils.generateTradeNO(sessionUser.getUser_id(), "F");		
					try {
						epay(tradeNo, "");
						r.setTrade_no(tradeNo);
						returnStr = "epay";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else if (payment.endsWith("_95epay")) {
					// 双乾支付直连
					logger.info("欢迎进入双乾支付...");
					String tradeNo = StringUtils.generateTradeNO(sessionUser.getUser_id(), "F");
					try {
						epay(tradeNo, payment.replace("_95epay", ""));
						r.setTrade_no(tradeNo);
						r.setPayment(EnumPayInterface.EPAY.getValue());
						returnStr = "epay";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				// v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 start
				else if(EnumPayInterface.EPAYLINKS.getValue().equals(payment)){
					// 易票联支付
					logger.info("欢迎进入易票联支付...");
					String tradeNo = StringUtils.generateTradeNO(sessionUser.getUser_id(), "M");
					try {
						r.setTrade_no(tradeNo);
						epaylinks("",tradeNo);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}else if(payment.endsWith("_epaylinks")){
					// 易票联支付
					String bankCode = payment.replace("_epaylinks", "");
					logger.info("欢迎进入易票联支付...");
					String tradeNo = StringUtils.generateTradeNO(sessionUser.getUser_id(), "M");
					try {
						r.setTrade_no(tradeNo);
						r.setPayment(EnumPayInterface.EPAYLINKS.getValue());
						epaylinks(bankCode,tradeNo);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}	//v1.6.7.1  RDPROJECT-410 wcw 2013-11-16 start
				else if(EnumPayInterface.UNSPAY.getValue().equals(payment)){
					// 银生支付
					logger.info("欢迎进入银生支付...");
					String tradeNo = StringUtils.generateTradeNO(sessionUser.getUser_id(), "N");
					try {
						r.setTrade_no(tradeNo);
						unspay("",tradeNo);
						r.setPayment(EnumPayInterface.UNSPAY.getValue());
						returnStr = "unspay";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}else if(payment.endsWith("_unspay")){
					// 银生支付
					logger.info("欢迎进入银生支付...");
					String bankCode = payment.replace("_unspay", "");
					String tradeNo = StringUtils.generateTradeNO(sessionUser.getUser_id(), "N");
					try {
						r.setTrade_no(tradeNo);
						unspay(bankCode,tradeNo);
						r.setPayment(EnumPayInterface.UNSPAY.getValue());
						returnStr = "unspay";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}	//v1.6.7.1  RDPROJECT-410 wcw 2013-11-16 end
				else if(EnumPayInterface.EBATONG_PAY.getValue().equals(payment)){
					//易八通支付接口
					logger.info("欢迎进入易八通支付...");
					String tradeNo = StringUtils.generateTradeNO(sessionUser.getUser_id(), "O");
					try {
						r.setTrade_no(tradeNo);
						r.setPayment(EnumPayInterface.EBATONG_PAY.getValue());
						ebaotongpay("",tradeNo);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}else if(payment.endsWith("_ebatong")){
					//易八通支付接口
					logger.info("欢迎进入易八通直连支付...");
					String bankCode = payment.replace("_ebatong", "");
					String tradeNo = StringUtils.generateTradeNO(sessionUser.getUser_id(), "O");
					try {
						r.setTrade_no(tradeNo);
						r.setPayment(EnumPayInterface.EBATONG_PAY.getValue());
						ebaotongpay(bankCode,tradeNo);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				// v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 end
				// v1.6.7.2 RDPROJECT-587 lhm 2013-12-16 start
				accountService.addRecharge(r);
				if (!"".equals(returnStr)) {
					return returnStr;
				}
				// v1.6.7.2 RDPROJECT-587 lhm 2013-12-16 end
			} else if (StringUtils.isNull(type).equals("2")) {
				r.setRemark(StringUtils.isNull(request.getParameter("remark")));
				r.setTrade_no(StringUtils.generateTradeNO(sessionUser.getUser_id(), "E"));
				r.setPayment(payment2);
				r.setStatus(0);
				rechargefee = NumberUtils.getDouble(Global.getValue("online_rechargefee"));
				r.setFee(NumberUtils.format2(r.getMoney() * rechargefee) + "");
				accountService.addRecharge(r);
			} else {
				// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 start
				message("暂不支持该充值方式！", "/member/account/newrecharge.html");
				// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 end
				return MSG;
			}
			// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 start
			message("充值成功，等待管理审核！", "/member/account/newrecharge.html");
			// v1.6.7.2 RDPROJECT-600 zza 2013-12-17 end
			return MSG;
		}
	}

	/**
	 * 国付宝充值
	 */
	private void gopayment(String type, String bankCode, AccountRecharge r) {
		PaymentInterface paymentInterface =getpPaymentInterface(EnumPayInterface.GOPAY.getValue());
		TranGood good = this.createTranGood(r);
		GoPay pay = new GoPay();
		pay.setGood(good);
		pay.setTranIP(this.getRequestIp());
		pay.setUserType(type);
		if (!StringUtils.isBlank(bankCode)) {
			pay.setBankCode(bankCode);
		}
		pay.init(pay,paymentInterface);
		String url = pay.submitGet();
		logger.debug("Submit Url:" + url);
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 新生支付
	 * 
	 * @param type
	 * @param bankCode
	 * @param r
	 */
	private void xspayment(String type, AccountRecharge r) {
		PaymentInterface paymentInterface =getpPaymentInterface(EnumPayInterface.XS_PAY.getValue());
		XsPay xspay = new XsPay();
		xspay.setOrderID(r.getTrade_no());
		String money = String.valueOf((long) (r.getMoney() * 100));
		// String money = s.substring(0,s.indexOf("."));
		xspay.setTotalAmount(money);
		xspay.setOrderAmount(money);
		xspay.setGoodsCount("1");
		xspay.setCustomerIP(this.getRequestIp());
		xspay.setType(type);
		xspay.setRemark(r.getRemark());
		xspay.init(xspay,paymentInterface);
		request.setAttribute("xspay", xspay);
		String url = xspay.submitGet();
		logger.debug("Submit Url:" + url);
	}

	private void ybpayment(String type, AccountRecharge r) {
		// try {
		// request.setCharacterEncoding("gbk");
        PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.YB_PAY.getValue());
		YbPay ybpay = new YbPay();
		String money = String.valueOf((long) (r.getMoney()));
		// String money = s.substring(0,s.indexOf("."));
		ybpay.init(ybpay, paymentInterface);
		ybpay.setP0_Cmd("Buy");
		ybpay.setP2_Order(r.getTrade_no());
		ybpay.setP3_Amt(money);
		ybpay.setP4_Cur("CNY");
		ybpay.setP5_Pid(Global.getValue("webid"));
		ybpay.setP6_Pcat(Global.getValue("webid"));
		ybpay.setP7_Pdesc(r.getRemark());
		ybpay.setP9_SAF("1");
		ybpay.setPa_MP("");
		ybpay.setPd_FrpId("");
		ybpay.setPr_NeedResponse("1");
		ybpay.setNodeAuthorizationURL(YbPay.getInstance().getValue("yeepayCommonReqURL"));
		// request.setAttribute("p5_Pid", ybpay.getP5_Pid());
		// request.setAttribute("p6_Pcat", ybpay.getP6_Pcat());
		// request.setAttribute("p7_Pdesc", ybpay.getP7_Pdesc());
		// 获得MD5-HMAC签名
		String hmac = "";
		try {
			hmac = YbPay.getReqMd5HmacForOnlinePayment(ybpay.getP0_Cmd(), ybpay.getP1_MerId(), ybpay.getP2_Order(),
					ybpay.getP3_Amt(), ybpay.getP4_Cur(), new String(ybpay.getP5_Pid().getBytes("utf-8"), "gbk"),
					new String(ybpay.getP6_Pcat().getBytes("utf-8"), "gbk"),
					new String(ybpay.getP7_Pdesc().getBytes("utf-8"), "gbk"), ybpay.getP8_Url(), ybpay.getP9_SAF(),
					ybpay.getPa_MP(), ybpay.getPd_FrpId().toUpperCase(), ybpay.getPr_NeedResponse(),
					ybpay.getKeyValue());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ybpay.setHmac(hmac);
		request.setAttribute("ybpay", ybpay);
		logger.debug("ybpay=======" + ybpay);
		// logger.debug("Submit Url:"+url);
		// } catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
	}

	private void ipspayment(String bankco, AccountRecharge r) {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.IPS_PAY.getValue());
		String tranAamt = NumberUtils.format2Str(NumberUtils.getDouble(money));
		IPSPay ips = new IPSPay();
		ips.init(ips, paymentInterface);
		ips.setBillno(r.getTrade_no());
		ips.setAmount(tranAamt);
		ips.setDate(DateUtils.dateStr(new Date(), "yyyyMMdd"));
		ips.setGateway_Type("01");
		ips.setCurrency_Type("RMB");
		ips.setLang("GB");
		ips.setErrorUrl("");
		ips.setAttach("");
		ips.setOrderEncodeType("5");
		ips.setRetEncodeType("17");
		ips.setRettype("1");
		if (!StringUtils.isBlank(bankco)) {
			ips.setDoCredit("1");
			ips.setBankco(bankco);
		}
		ips.encodeSignMD5();
		logger.info("封装IPSPay对象:" + ips);
		request.setAttribute("ips", ips);
	}

	/**
	 * 融宝支付
	 */
	private void rongpayment(String bankco, AccountRecharge r) throws UnsupportedEncodingException {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.RONGBAO_PAY.getValue());
		String tranAamt = NumberUtils.format2Str(NumberUtils.getDouble(money));
		RongPay rp = new RongPay();
		rp.init(rp,paymentInterface);
		if (!StringUtils.isBlank(bankco)) {
				rp.setPaymethod("directPay");
				rp.setDefaultbank(bankco);
		} else {
			rp.setPaymethod("bankPay");
			rp.setDefaultbank("");
		}
		rp.setBuyer_email("");
		rp.setOrder_no(r.getTrade_no());
		rp.setTotal_fee(tranAamt);
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		rp.setSign(rp.getSign(rp,paymentInterface));
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		request.setAttribute("rp", rp);
		logger.info("封装RongPay对象:" + rp);
		// logger.info("提交路径:"+rp.BuildForm(rp));

	}
	
	// v1.6.5.5 RDPROJECT-148 xx 2013-09-23 start
	/**
	 * 易生支付
	 */
	private void yishengpayment(String bankco, AccountRecharge r) throws UnsupportedEncodingException {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.YISHENG_PAY.getValue());
		String tranAamt = NumberUtils.format2Str(NumberUtils.getDouble(money));
		YiShengPay ysp = new YiShengPay();
		ysp.init(ysp,paymentInterface);
		if (!StringUtils.isBlank(bankco)) {
			ysp.setPaymethod("bankDirect");
			ysp.setDefaultbank(bankco);
		} else {
			ysp.setPaymethod("bankPay");
			ysp.setDefaultbank("");
		}
		ysp.setBuyer_email("");
		ysp.setOut_trade_no(r.getTrade_no());
		ysp.setTotal_fee(tranAamt);
		  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		ysp.setSign(ysp.getSign(ysp,paymentInterface));
		  //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		request.setAttribute("ysp", ysp);
	}
	// v1.6.5.5 RDPROJECT-148 xx 2013-09-23 end
	
	// v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 start
	/**
	 * 双乾支付
	 * 
	 * @param tradeNo 订单号
	 * @param paymentType 支付银行类型
	 * @throws UnsupportedEncodingException 异常
	 */
	private void epay(String tradeNo, String paymentType) throws UnsupportedEncodingException {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.EPAY.getValue());
		String amount = NumberUtils.format2Str(NumberUtils.getDouble(money));
		Epay epay = new Epay();
		epay.init(epay,paymentInterface);
		// 该笔订单总金额
		epay.setAmount(amount);
		// 订单号
		epay.setBillNo(tradeNo);
		// 支付银行类型
		epay.setPaymentType(paymentType);
		// 参数加密
		EpayUtil epayUtil = new EpayUtil();
		String md5key="";
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			 md5key = paymentInterface.getKey();
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		String md5info = epayUtil.signMap(
				new String[] { epay.getMerNo(), epay.getBillNo(), epay.getAmount(), epay.getReturnURL() }, md5key,
				"REQ");
		epay.setMd5info(md5info);
		request.setAttribute("epay", epay);
	}
	// v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 end

	/**
	 * 网银在线
	 * 
	 * @param bankco
	 * @param r
	 */
	private void chinaBankPayment(String bankco, AccountRecharge r)throws UnsupportedEncodingException  {
		String tranAamt = NumberUtils.format2Str(NumberUtils.getDouble(money));
		ChinaBankPay cbp = new ChinaBankPay();
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.CHINABANK_PAY.getValue());
		String key="";
		cbp.init(cbp, paymentInterface);

		String v_oid = r.getTrade_no();
		if (v_oid != null && !v_oid.equals("")) {
			cbp.setV_oid(v_oid);
		} else {
			Date currTime = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd-" + cbp.getV_mid() + "-hhmmss", Locale.US);
			v_oid = sf.format(currTime); // 推荐订单号构成格式为 年月日-商户号-小时分钟秒
		}
		if (!StringUtils.isBlank(bankco)) {
			cbp.setPmode_id(bankco);
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			 key = paymentInterface.getKey();
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		String returnurl=cbp.getV_url();
		String v_mid=cbp.getV_mid();
		cbp.setV_oid(v_oid);
		cbp.setV_amount(tranAamt);
		String v_moneytype = "CNY";
		cbp.setV_moneytype(v_moneytype);
		String v_md5info = tranAamt + v_moneytype + v_oid + v_mid + returnurl + key; // 拼凑加密串
		MD5 md5 = new MD5();
		cbp.setV_md5info(md5.getMD5ofStr(v_md5info));
		cbp.setRemark1(r.getRemark());
		cbp.setRemark2(r.getRemark());
		logger.info("封装ChinaBankPay对象:" + cbp);
		request.setAttribute("cbp", cbp);
	}

	/**
	 * 盛付通
	 * 
	 * @param bankCode
	 * @param r
	 */
	private void ShengPayment(String bankCode, AccountRecharge r) {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.SHENG_PAY.getValue());
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String amount = NumberUtils.format2Str(NumberUtils.getDouble(money));
		ShengPay spay = new ShengPay();
		spay.init(spay, paymentInterface);
		spay.setName("B2CPayment");
		spay.setVersion("V4.1.1.1.1");
		spay.setCharset("UTF-8");

		spay.setSendTime(df.format(new Date()));
		spay.setOrderNo(r.getTrade_no());
		spay.setOrderAmount(amount);
		spay.setOrderTime(df.format(new Date()));
		spay.setPayType("");
		spay.setInstCode(bankCode);
	
		spay.setProductName("用户充值" + amount + "元");
		spay.setBuyerContact("");
		spay.setBuyerIp(request.getRemoteAddr());
		spay.setExt1("");
		spay.setSignType("MD5");
		ShengPay paySignMsg = spay.signFormRequest(spay, spay.getSignKey());
		spay.setSignMsg(paySignMsg.getSignMsg());
		request.setAttribute("spay", spay);
	}

	/**
	 * 宝付支付
	 */
	private void baoFooPayMent(String bankCode, AccountRecharge r) {
		// 封装支付信息对象
		BaoFooPay baoFoo = new BaoFooPay();
		PaymentInterface paymentInterface=accountService.getPaymentInterface(EnumPayInterface.BAOFOO_PAY.getValue());
		baoFoo.init(baoFoo, paymentInterface);
		// 通知 方式 1 是 服务器通知 和 页面跳转
		baoFoo.setNoticeType("1");
		// 订单金额 必须换算成 分
		String orderMoney;
		if (!"".equals(money)) {
			double a;
			a = Double.parseDouble(money) * 100; // 使用分进行提交
			orderMoney = String.valueOf(a);
		} else {
			orderMoney = "0";
		}
		baoFoo.setOrderMoney(orderMoney);
		// 支付方式 使用默认的银联模式
		baoFoo.setPayID(bankCode);
		
		// 订单交易的 交易时间
		baoFoo.setTradeDate(DateUtils.dateStr3(new Date()));
		// 生成商户流水号
		baoFoo.setTransID(r.getTrade_no());
		// 生成 签名字段
		String Md5key="";
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			 Md5key = paymentInterface.getKey();// md5密钥（KEY）
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		String md5Sign = baoFoo.getMerchantID() + baoFoo.getPayID() + baoFoo.getTradeDate() + baoFoo.getTransID()
				+ baoFoo.getOrderMoney() + baoFoo.getMerchant_url() + baoFoo.getReturn_url() + baoFoo.getNoticeType()
				+ Md5key; // MD5签名格式
		MD5 md5 = new MD5();
		String Md5Sign = md5.getMD5ofStr(md5Sign);// 计算MD5值
		baoFoo.setMd5Sign(Md5Sign);

		request.setAttribute("baoFoo", baoFoo);
	}

	/**
	 * 通联支付
	 */
	private void allInpayment(String type,AccountRecharge r){
		AllInPay tpay = new AllInPay();
		PaymentInterface paymentInterface=accountService.getPaymentInterface(EnumPayInterface.ALLIN_PAY.getValue());
		tpay.init(tpay,paymentInterface);
		String payType = request.getParameter("payType");
		tpay.setPayerEmail(request.getParameter("payerEmail"));
		if(payType!= null &&  payType.equals("3")){
			String payerIDCard = request.getParameter("payerIDCard");
			String payerName = request.getParameter("payerName");
			String payerTelephone= request.getParameter("payerTelephone");
			String pan =  request.getParameter("pan");
			if(payerIDCard == null|| payerIDCard.equals("") || payerName == null|| payerName.equals("") 
				||  payerTelephone == null|| payerTelephone.equals("") || pan == null ||pan.equals("") ){
				logger.info( "电话支付中 ,支付卡号、支付姓名、和支付电话不能为空") ;
			}else {
				tpay.setPayerIDCard(payerIDCard);
				tpay.setPayerName(payerName);
				tpay.setPayerTelephone(payerTelephone);
				tpay.setPan(pan);
			}
			tpay.setPayType(payType);
		}else {
			tpay.setPayType("0");
		}	
		logger.info("tpay.getPayType()" + tpay.getPayType());
		tpay.setOrderNo(r.getTrade_no());
		tpay.setOrderAmount(String.valueOf((long)(r.getMoney()*100)));
		tpay.setOrderDatetime(DateUtils.dateStr3(new Date()));
		tpay.setOrderExpireDatetime(request.getParameter("orderExpireDatetime"));
		tpay.setProductName(Global.getValue("webname"));
		tpay.setProductDesc("用户充值："+money+"RMB");
		tpay.setProductPrice(String.valueOf((long)(r.getMoney()*100)));
		tpay.setIssuerId(request.getParameter("issuerId"));
		String signMsg = tpay.getSignMsg(tpay,paymentInterface);
		tpay.setSignMsg(signMsg);
		request.setAttribute("tpay", tpay);
	}
	/**
	 * 智付 yinliang 2013-04-09
	 * 
	 * @param bankCode
	 * @param r
	 */
	private void Dinpayment(AccountRecharge r) {
		// 订单金额
		String tranAamt = NumberUtils.format2Str(NumberUtils.getDouble(money));
		String weburl = Global.getValue("weburl");
		// String weburl = "http://localhost:8080";
		Dinpay dinpay = new Dinpay();
		// dinpay.setP_Bank("CMB");
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.DINPAY.getValue());
		dinpay.init(dinpay, paymentInterface);
		String key ="";
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			key =paymentInterface.getKey();
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		dinpay.setM_orderID(r.getTrade_no());
		dinpay.setM_amount(tranAamt);
		dinpay.setM_currency("1");
		dinpay.setM_language("1");
		dinpay.setM_date(DateUtils.dateStr3(new Date()));
		dinpay.setState("0");
		// 组织订单信息
		String m_info = dinpay.getM_id() + "|" + dinpay.getM_orderID() + "|" + dinpay.getM_amount() + "|"
				+ dinpay.getM_currency() + "|" + dinpay.getM_url() + "|" + dinpay.getM_language();
		String s_info = dinpay.getS_name() + "|" + dinpay.getS_address() + "|" + dinpay.getS_postcode() + "|"
				+ dinpay.getS_telephone() + "|" + dinpay.getS_email() + "|" + dinpay.getR_name();
		String r_info = dinpay.getR_address() + "|" + dinpay.getR_postcode() + "|" + dinpay.getR_telephone() + "|"
				+ dinpay.getR_email() + "|" + dinpay.getM_comment() + "|" + dinpay.getState() + "|"
				+ dinpay.getM_date();
		String orderInfo = m_info + "|" + s_info + "|" + r_info;

		//System.out.println(orderInfo);


		orderInfo = xStrEncrypt.StrEncrypt(orderInfo, key);

		dinpay.setOrderInfo(orderInfo);

		dinpay.setMd5Sign(MD5Digest.encrypt(orderInfo + key));

		request.setAttribute("dinpay", dinpay);

	}
	/**
     * 汇潮支付 
     * 
     * @param type
     * @param ecpssPay
     */
    private void Ecpss(String bankCode, AccountRecharge r,PaymentInterface paymentInterface) {
        String weburl = Global.getValue("weburl");
        EcpssPay ecpssPay = new EcpssPay();
        //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
        if(paymentInterface!=null){
            // md5Key
            ecpssPay.setMd5Key(paymentInterface.getKey());
            // 商户ID
            ecpssPay.setMerNo(paymentInterface.getMerchant_id());
            // 页面显示地址
            ecpssPay.setReturnUrl(weburl +  paymentInterface.getReturn_url());
            // 后台回调地址
            ecpssPay.setAdviceUrl(weburl +paymentInterface.getNotice_url());
        }
        //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
        ecpssPay.setBillNo(r.getTrade_no());
        ecpssPay.setAmount(money);
        ecpssPay.setDefaultBankNumber(bankCode);
        ecpssPay.setOrderTime(DateUtils.dateStr3(new Date()));
        ecpssPay.setProducts("用户充值" + money + "RMB");
        request.setAttribute("ecpssPay", ecpssPay);
    }
	/**
	 * 汇潮支付 yinliang
	 * 
	 * @param type
	 * @param ecpssPay
	 */
	private void Ecpsspayment(String bankCode, AccountRecharge r) {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.ECPSSPAY.getValue());
		Ecpss(bankCode, r, paymentInterface);
	}
	
	/**
	 * 汇潮支付 yinliang
	 * 河南贷汇潮快捷支付（实现方法其实是一样的，只是其使用了两个不同的商户号）
	 * @param type
	 * @param ecpssPay
	 */
	private void EcpsspaymentFast(String bankCode, AccountRecharge r) {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.ECPSSFAST_PAY.getValue());
		Ecpss(bankCode, r, paymentInterface);
	}
	/**
     * 汇潮中行积分直连通道支付 yinliang
     * @param type
     * @param ecpssPay
     */
    private void Ecpsschinapay(String bankCode, AccountRecharge r) {
        PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.ECPSSCHINA_PAY.getValue());
        Ecpss(bankCode, r, paymentInterface);
    }
	/**
	 * 财付通 yinliang 2013-06-14
	 * 
	 * @param bank
	 * @param r
	 * @throws UnsupportedEncodingException
	 */
	private void Tenpay(String bankCode, AccountRecharge r) throws UnsupportedEncodingException {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.TENPAY.getValue());
		String weburl = Global.getValue("weburl");
		String merNo="";
		String returnUrl ="";
		String notifyUrl="";
		String key="";
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
		if(paymentInterface!=null){
			// 商户号
			 merNo = paymentInterface.getMerchant_id();
			// 交易完成后跳转页面
			 returnUrl =paymentInterface.getReturn_url();
			// 接收财付通通知页面
			 notifyUrl =paymentInterface.getNotice_url();
			// 密钥
			 key = paymentInterface.getKey();
		}
		//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
		TenpayRequestHandler tenpay = new TenpayRequestHandler(request, response);
		tenpay.init();
		tenpay.setParameter("partner", merNo);
		tenpay.setKey(key);
		tenpay.setParameter("out_trade_no", r.getTrade_no());
		// 订单金额，以分为单位
		tenpay.setParameter("total_fee", Integer.valueOf((int) (Double.valueOf(money) * 100)) + "");
		tenpay.setParameter("return_url", weburl + returnUrl);
		tenpay.setParameter("notify_url", weburl + notifyUrl);
		tenpay.setParameter("body", "用户充值" + money + "元");
		// 银行类型
		tenpay.setParameter("bank_type", bankCode);
		// 用户公网IP
		tenpay.setParameter("spbill_create_ip", request.getRemoteAddr());
		tenpay.setParameter("fee_type", "1");

		tenpay.setParameter("input_charset", "UTF-8");
		logger.info("requestUrl：" + tenpay.getRequestURL());

		request.setAttribute("tenpay", tenpay);

	}
	//v1.6.7.1  RDPROJECT-417 wcw 2013-11-04 start
	/**
	 * 易票联支付
	 * 
	 * @param tradeNo 订单号
	 * @param paymentType 支付银行类型
	 * @throws UnsupportedEncodingException 异常
	 */
	private void epaylinks(String bankCode,String tradeNo) throws UnsupportedEncodingException {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.EPAYLINKS.getValue());
		String amount = NumberUtils.format2Str(NumberUtils.getDouble(money));
		Epaylinks epay = new Epaylinks();
		if(paymentInterface!=null){
		   epay.init(epay,paymentInterface);
		}
		// 该笔订单总金额
		epay.setParameter("total_fee",amount);
		// 订单号
		epay.setParameter("out_trade_no",tradeNo);
		if(!StringUtils.isBlank(bankCode)){
			epay.setParameter("pay_id", bankCode);
		}
		// 参数加密
		String url = epay.getRequestURL();
		logger.debug("Submit Url:" + url);
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}		
	}
	//v1.6.7.1  RDPROJECT-417 wcw 2013-11-04 end
	//v1.6.7.1  RDPROJECT-410 wcw 2013-11-07 start
		/**
		 * 银生支付
		 * 
		 * @param tradeNo 订单号
		 * @param paymentType 支付银行类型
		 * @throws UnsupportedEncodingException 异常
		 */
		private void unspay(String bankCode,String tradeNo) throws UnsupportedEncodingException {
			PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.UNSPAY.getValue());
			String amount = NumberUtils.format2Str(NumberUtils.getDouble(money));
			Unspay unspay = new Unspay();
			if(!StringUtils.isBlank(bankCode)){
				unspay.setBankCode(bankCode);
			}
			unspay.init(unspay,paymentInterface,amount,tradeNo);
		    request.setAttribute("unspay", unspay);
		}
		//v1.6.7.1  RDPROJECT-410 wcw 2013-11-07 end
	//v1.6.7.2  RDPROJECT-512 wcw 2013-12-03 start
	/**
	 * 易八通支付
	 * 
	 * @param tradeNo 订单号     
	 * @param paymentType 支付银行类型
	 * @throws UnsupportedEncodingException 异常
	 */
	private void ebaotongpay(String bankCode,String tradeNo) throws UnsupportedEncodingException {
		PaymentInterface paymentInterface=getpPaymentInterface(EnumPayInterface.EBATONG_PAY.getValue());
		String amount = NumberUtils.format2Str(NumberUtils.getDouble(money));
		EbatongPay ebatongPay = new EbatongPay();
		if(!StringUtils.isBlank(bankCode)){
			ebatongPay.setDefault_bank(bankCode);
		}
		String ip=this.getRequestIp();
		String url=ebatongPay.init(ebatongPay,paymentInterface,amount,tradeNo,ip);
		logger.debug("Submit Url:" + url);
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}	
	}
	//v1.6.7.2  RDPROJECT-512 wcw 2013-12-03 END
	private String checkNewCash(User user, AccountModel detailAct) {
		String errormsg = "";
		String paypwdStr = StringUtils.isNull(paypassword);
		String mypaypwd = StringUtils.isNull(user.getPaypassword());
		String site_id = StringUtils.isNull(Global.getValue("webid"));
		UserAccountSummary uas = accountService.getUserAccountSummary(user.getUser_id());
		// v1.6.7.1 RDPROJECT-350 xx 2013-11-07 start
		double userOwnMoney = NumberUtils.getDouble(Global.getValue("userownmoney"));
		double borrowOwnMoneyRatio = NumberUtils.getDouble(Global.getValue("borrow_ownmoney_ratio"));
		// v1.6.7.1 RDPROJECT-350 xx 2013-11-07 end
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
		// v1.6.7.1 RDPROJECT-350 xx 2013-11-07 start
		else if ((site_id.equals("jsdai")) && (borrowService.getRepayTotalWithJin(user.getUser_id()) > 0) && (uas.getRechargeTotal() -uas.getCashTotal() < userOwnMoney)) {
		     errormsg = "您还存在未还款的净值标,您的(成功充值总额-已成功提现总额)必须大于"+userOwnMoney+"元,方可进行提现";
		     return errormsg;   
		}else if ((site_id.equals("jsdai")) && (borrowService.getRepayTotalWithJin(user.getUser_id()) > 0) && (cash_money > (uas.getRechargeTotal() -uas.getCashTotal() -(borrowService.getRepayTotalWithJin(user.getUser_id())/borrowOwnMoneyRatio)))) {
		     errormsg = "您还存在未还款的净值标,您的提现金额不能大于你的(成功充值总额￥"+uas.getRechargeTotal()+"-已成功提现总额￥"+uas.getCashTotal()+"-(净值待还总额￥"+String.valueOf(borrowService.getRepayTotalWithJin(user.getUser_id()))+"除以净值借款倍率)(现净值借款倍率为"+borrowOwnMoneyRatio+"倍))";
		     return errormsg; 
		}
		// v1.6.7.1 RDPROJECT-350 xx 2013-11-07 end
		else if (Math.floor(cash_money) != cash_money && site_id.equals("huidai")) {
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

	private TranGood createTranGood(AccountRecharge r) {
		TranGood good = new TranGood();
		// 设置交易时间
		Date d = new Date();
		good.setTranDateTime(DateUtils.dateStr3(d));
		// 设置订单号
		User user = (User) session.get(Constant.SESSION_USER);
		good.setMerOrderNum(r.getTrade_no());
		// 设置交易金额
		double tranAamt = NumberUtils.format4(NumberUtils.getDouble(money));
		double feeAmt = tranAamt * NumberUtils.getDouble(Global.getValue("online_rechargefee"));
		good.setTranAmt(NumberUtils.format2(tranAamt) + "");
		if (feeAmt > 0.01) {
			good.setFeeAmt(NumberUtils.format2(feeAmt) + "");
		}
		// 设置交易商品信息
		good.setGoodsName(Global.getValue("webname"));
		good.setGoodsDetail("用户充值：" + money + "RMB");
		good.setBuyerName(Global.getValue("webname"));
		good.setBuyerContact(user.getProvince() + user.getCity() + user.getArea());

		return good;
	}

	// 中融资本专用 作者国平，请勿删除/改动 start
	/**
	 * 回款
	 */
	public String huikuan() {
		String webid = Global.getValue("webid");
		User user = (User) session.get(Constant.SESSION_USER);
		request.setAttribute("session_user", user);
		String huikuan_award = Global.getValue("huikuan_award");
		request.setAttribute("huikuan_award", huikuan_award);
		type = request.getParameter("type");
		if (!StringUtils.isNull(type).equals("")) {
			if (type.equals("huikuan")) {
				if (StringUtils.isNull(webid).equals("zrzb")) {
					if (!StringUtils.isNull(huikuan_money).equals("")) {
						double money = NumberUtils.getDouble(huikuan_money);
						if (money < 5000.0) {
							request.setAttribute("errormsg", "回款金额小于5000元,不能进行申请");//手动申请回款
							return "success";
						}
					}
				}
				Huikuan huikuan = new Huikuan();
				huikuan.setUser_id(user.getUser_id());
				huikuan.setHuikuan_money(huikuan_money);
				if (Global.getWebid().equals("zrzb") || Global.getWebid().equals("xdcf")) {
					String huikuan_award_day = Global.getValue("huikuan_award_day");
					String huikuan_award_month = Global.getValue("huikuan_award_month");
					if (is_day == 1) {
						huikuan.setHuikuan_award(huikuan_award_day);
					} else {
						huikuan.setHuikuan_award(huikuan_award_month);
					}
				} else {
					huikuan.setHuikuan_award(huikuan_award);
				}
				huikuan.setRemark(remark);
				huikuan.setStatus("0");
				huikuan.setAddtime(this.getTimeStr());
				accountService.huikuan(huikuan);
				request.setAttribute("msg", "申请回款成功");
			}
		}
		return "success";
	}
	// 中融资本专用 作者国平，请勿删除/改动 end
	
	
	/**
	 * 用户红包列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String hongBaoList() throws Exception {
		int page = NumberUtils.getInt(request.getParameter("page"));
		String account_type = "hongbao";
		String username = getSessionUser().getUsername();
		SearchParam param = new SearchParam();
		param.setAccount_type(account_type);
		param.setUsername(username);
		PageDataList plist = accountService.getHongBaoList(page, param);
		request.setAttribute("list", plist.getList());
		request.setAttribute("p", plist.getPage());
		request.setAttribute("param", param.toMap());
		setMsgUrl("/member/account/hongbao.html");
		return SUCCESS;
	}
	//V1.6.6.1 RDPROJECT-300 wcw 2013-10-10 start
	/**
	 * 申请专属客服
	 */
	public String specificKefu() {
		User user = getSessionUser();
		UserCache userCache = userService.getUserCacheByUserid(user.getUser_id());
		String actionType = StringUtils.isNull(request.getParameter("type"));
		if (StringUtils.isBlank(actionType)) {
			request.setAttribute("userCache", userCache);
			//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
			List kflist = userService.getKfList(1);
			//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
			User kefuUser = userService.getDetailUser(userCache.getKefu_userid());
			request.setAttribute("kefuUser", kefuUser);
			request.setAttribute("kflist", kflist);
			return SUCCESS;
		} else {
			long kefu_userid = NumberUtils.getLong(StringUtils.isNull(request.getParameter("kefu_userid")));
			User kefuUser = userService.getDetailUser(kefu_userid);
			userCache.setKefu_userid(kefu_userid);
			userCache.setKefu_username(kefuUser.getUsername());
			userService.updateusercache(userCache);

			message("专属客服申请成功！", "/member/index.html", "点击返回！");
			return MSG;
		}

	}
	//V1.6.6.1 RDPROJECT-300 wcw 2013-10-10 end
	private PaymentInterface getpPaymentInterface(String interface_value){
		PaymentInterface paymentInterface=accountService.getPaymentInterface(interface_value);
		return paymentInterface;
	}
	public long getIs_day() {
		return is_day;
	}

	public void setIs_day(long is_day) {
		this.is_day = is_day;
	}

	public String getHuikuan_money() {
		return huikuan_money;
	}

	public void setHuikuan_money(String huikuan_money) {
		this.huikuan_money = huikuan_money;
	}

	public String getHuikuan_award() {
		return huikuan_award;
	}

	public void setHuikuan_award(String huikuan_award) {
		this.huikuan_award = huikuan_award;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public BorrowService getBorrowService() {
		return borrowService;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	public String getValicode() {
		return valicode;
	}

	public void setValicode(String valicode) {
		this.valicode = valicode;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPaypassword() {
		return paypassword;
	}

	public void setPaypassword(String paypassword) {
		this.paypassword = paypassword;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getDotime1() {
		return dotime1;
	}

	public void setDotime1(String dotime1) {
		this.dotime1 = dotime1;
	}

	public String getDotime2() {
		return dotime2;
	}

	public void setDotime2(String dotime2) {
		this.dotime2 = dotime2;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
}
