package com.p2psys.web.action.admin;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.p2psys.domain.AccountBank;
import com.p2psys.domain.OnlineBank;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.AccountService;
import com.p2psys.service.ManageBankService;
import com.p2psys.service.UpfilesService;
import com.p2psys.service.UserService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 充值银行管理Action
 * 
 
 * @date 2013-10-25 
 * @version
 * 
 *  (c)</b> 2012-rongdu-<br/>
 * 
 */
public class ManageBankAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ManageBankAction.class);

	private ManageBankService manageBankService;
	private UserService userService;
	private AccountService accountService;
	private UpfilesService upfilesService;

	public File getBank_logo() {
		return bank_logo;
	}

	public void setBank_logo(File bank_logo) {
		this.bank_logo = bank_logo;
	}

	private File litpic;
	private File bank_logo;
	private String filePath;

	private String sep = File.separator;
	private PageDataList pageList;

	public PageDataList getPageList() {
		return pageList;
	}

	public void setPageList(PageDataList pageList) {
		this.pageList = pageList;
	}

	public ManageBankService getmanageBankService() {
		return manageBankService;
	}

	public void setmanageBankService(ManageBankService manageBankService) {
		this.manageBankService = manageBankService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * 获取upfilesService
	 * 
	 * @return upfilesService
	 */
	public UpfilesService getUpfilesService() {
		return upfilesService;
	}

	/**
	 * 设置upfilesService
	 * 
	 * @param upfilesService
	 *            要设置的upfilesService
	 */
	public void setUpfilesService(UpfilesService upfilesService) {
		this.upfilesService = upfilesService;
	}
	
	public File getLitpic() {
		return litpic;
	}

	public void setLitpic(File litpic) {
		this.litpic = litpic;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSep() {
		return sep;
	}

	public void setSep(String sep) {
		this.sep = sep;
	}
	public String addRechargeDownLineBank() throws Exception{
		List list=manageBankService.getPaymentList(1);
		request.setAttribute("paymentList", list);
		String type=paramString("type");
		if(StringUtils.isBlank(type)){
			return SUCCESS;
		}
		AccountBank bank=new AccountBank();
		fillAccountBank(bank);
		if(StringUtils.isBlank(bank.getBank())){
			message("银行开户银行不能为空！！！", "/admin/bank/addDownLineBank.html");		
			return ADMINMSG;
		}
		if(StringUtils.isBlank(bank.getAccount())){
			message("银行账号不能为空！！！！", "/admin/bank/addDownLineBank.html");
			return ADMINMSG;
		}
		if(StringUtils.isBlank(bank.getBank_realname())){
			message("银行法人不能为空！！！", "/admin/bank/addDownLineBank.html");
			return ADMINMSG;
		}
		if(StringUtils.isBlank(bank.getBranch())){
			message("银行开户行名称不能为空！！！！", "/admin/bank/addDownLineBank.html");
			return ADMINMSG;
		}
		manageBankService.addRechargeDownLineBank(bank);
		message("操作成功！", "/admin/bank/downLineBankList.html");
		return ADMINMSG;
	}
	public String updateRechargeDownLineBank() throws Exception{
		List list=manageBankService.getPaymentList(1);
		request.setAttribute("paymentList", list);
		int id=paramInt("id");
		AccountBank bank=manageBankService.getDownLineBank(id);
		String type=paramString("type");
		if(StringUtils.isBlank(type)){
			request.setAttribute("bank", bank);
			return SUCCESS;
		}
		fillAccountBank(bank);
		if(StringUtils.isBlank(bank.getBank())){
			message("银行开户银行不能为空！！！", "/admin/bank/updateDownLineBank.html?id="+id);		
			return ADMINMSG;
		}
		if(StringUtils.isBlank(bank.getAccount())){
			message("银行账号不能为空！！！！", "/admin/bank/updateDownLineBank.html?id="+id);
			return ADMINMSG;
		}
		if(StringUtils.isBlank(bank.getBank_realname())){
			message("银行法人不能为空！！！", "/admin/bank/updateDownLineBank.html?id="+id);
			return ADMINMSG;
		}
		if(StringUtils.isBlank(bank.getBranch())){
			message("银行开户行名称不能为空！！！！", "/admin/bank/updateDownLineBank.html?id="+id);
			return ADMINMSG;
		}
		manageBankService.updateRechargeDownLineBank(bank);
		message("操作成功！", "/admin/bank/downLineBankList.html");
		return ADMINMSG;
	}
	public String rechargeDownLineBankList() throws Exception{
		int page = paramInt("page");
		pageList = manageBankService.getList(page);
		setPageAttribute(pageList, new SearchParam());
		return SUCCESS;
	}
	public AccountBank fillAccountBank(AccountBank bank){
		int id=paramInt("id");
		String bank_name = paramString("bank");
		String bank_account = paramString("account");
		String bank_persion = paramString("bank_realname");
		String branch = paramString("branch");
		String payment = paramString("payment");
		long order=paramLong("order");
		bank.setAccount(bank_account);
		bank.setBank_realname(bank_persion);
		bank.setBank(bank_name);
		bank.setBranch(branch);
		bank.setId(id);
		bank.setPayment(payment);
		bank.setOrder(order);
		return bank;
		
	}
	/**
	 * 第三方支付接口添加
	 * 
	 * @return
	 */
	public String addPayInterface() throws Exception{
		String actionType = paramString("type");
		List paylist=manageBankService.getPaymentList(0);
		request.setAttribute("paymentList", paylist);
		if (StringUtils.isBlank(actionType)) {
			return "success";
		}
		PaymentInterface paymentInterface = new PaymentInterface();
		paymentInterface = getParameter(paymentInterface);
		manageBankService.addPayInterface(paymentInterface);
		message("操作成功！", "/admin/bank/payInterfaceList.html");
		return ADMINMSG;
	}
	/**
	 * 修改第三方支付
	 * @return
	 * @throws Exception
	 */
	public String updatePayInterface() throws Exception{
		int id=paramInt("id");
		String actionType = paramString("type");
		List paylist=manageBankService.getPaymentList(0);
		request.setAttribute("paymentList", paylist);
		if (StringUtils.isBlank(actionType)) {
			PaymentInterface paymentInterface = manageBankService.getPayInterface(id);
			request.setAttribute("paymentInterface", paymentInterface);
			return "success";
		}
		PaymentInterface paymentInterface = new PaymentInterface();
		paymentInterface = getParameter(paymentInterface);
		manageBankService.updatePayInterface(paymentInterface);
		message("操作成功！", "/admin/bank/payInterfaceList.html");
		return ADMINMSG;
	}
	/**
	 * 第三方接口列表
	 * 
	 * @param paymentInterface
	 * @return
	 */
	public String payInterfaceList()throws Exception{
		int page=paramInt("page");
		PageDataList pageDataList=manageBankService.getPayInterfaceList(page, 0);
        setPageAttribute(pageDataList, new  SearchParam());
		return SUCCESS;
	}
	public PaymentInterface getParameter(PaymentInterface paymentInterface) {
		String name = paramString("name");
		long is_enable_single = paramLong("is_enable_single");
		long is_enable_unsingle = paramLong("is_enable_unsingle");
		String merchant_id = paramString("merchant_id");
		String key = paramString("key");
		String recharge_fee = paramString("recharge_fee");
		String return_url = paramString("return_url");
		String notice_url = paramString("notice_url");
		String chartset = paramString("chartset");
		String interface_Into_account = paramString("interface_Into_account");
		String interface_value = paramString("interface_value");
		//v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 start
        String signType=paramString("signType");
		//v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 end
		String pay_style = paramString("pay_style");
		String seller_email=paramString("seller_email");
		String transport=paramString("transport");
		String order_description=paramString("order_description");
		long id = paramLong("id");
        long order=paramLong("order");
      //v1.6.7.1 RDPROJECT-417 wcw 2013-11-14 start
        String gatewayUrl=paramString("gatewayUrl");
		//v1.6.7.1 RDPROJECT-417 wcw 2013-11-14 end
		paymentInterface.setName(name);
		paymentInterface.setIs_enable_single(is_enable_single);
		paymentInterface.setIs_enable_unsingle(is_enable_unsingle);
		paymentInterface.setMerchant_id(merchant_id);
		paymentInterface.setKey(key);
		paymentInterface.setRecharge_fee(recharge_fee);
		paymentInterface.setReturn_url(return_url);
		paymentInterface.setNotice_url(notice_url);
		paymentInterface.setChartset(chartset);
		paymentInterface.setInterface_Into_account(interface_Into_account);
		paymentInterface.setInterface_value(interface_value);
		paymentInterface.setId(id);
		paymentInterface.setSignType(signType);
		paymentInterface.setPay_style(pay_style);
		paymentInterface.setSeller_email(seller_email);
		paymentInterface.setTransport(transport);
		paymentInterface.setOrder_description(order_description);
		paymentInterface.setOrder(order);
       //v1.6.7.1 RDPROJECT-417 wcw 2013-11-14 start
		paymentInterface.setGatewayUrl(gatewayUrl);
       //v1.6.7.1 RDPROJECT-417 wcw 2013-11-14 end
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
		paymentInterface.setOrderInquireUrl(paramString("orderInquireUrl"));
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
		//v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
		paymentInterface.setCert_position(paramString("cert_position"));
		//v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
		return paymentInterface;
	}

	public String onlineBank() throws Exception {
		String actionType = StringUtils.isNull(request.getParameter("type"));
		String idString = StringUtils.isNull(request.getParameter("id"));
		OnlineBank onlineBank = new OnlineBank();
		List payInterfacelist = manageBankService
				.getPayInterfaceList(1);
		request.setAttribute("payInterfacelist", payInterfacelist);
		int id = 0;
		if (!StringUtils.isBlank(idString)) {
			id = Integer.parseInt(idString);
		}
		if (StringUtils.isBlank(actionType)) {
			if (id == 0) {
				return "success";
			}
			onlineBank = manageBankService.getOnlineBank(id);
			request.setAttribute("onlineBank", onlineBank);
			return "success";
		}
		request.setAttribute("actiontype", actionType);
		if ("list".equals(actionType)) {
			int page = NumberUtils.getInt(request.getParameter("page"));
			pageList = manageBankService.getOnlineBankList(page);
			setPageAttribute(pageList, new SearchParam());
		}else {
			onlineBank = fillOnlineBank(onlineBank);
			moveFile(onlineBank);
			onlineBank.setBank_logo(filePath);
			manageBankService.OnLineBank(onlineBank, idString);
			message("操作成功！", "/admin/bank/onlineBank.html?type=list");
			return ADMINMSG;
		}
		return SUCCESS;
	}

	private void moveFile(OnlineBank onlineBank) {
		String dataPath = ServletActionContext.getServletContext().getRealPath(
				"/data");
		String contextPath = ServletActionContext.getServletContext()
				.getRealPath("/");
		Date d1 = new Date();
		String upfiesDir = dataPath + sep + "upfiles" + sep + "images" + sep;
		String destfilename1 = upfiesDir + DateUtils.dateStr2(d1) + sep
				+ onlineBank.getId() + "_attestation" + "_" + d1.getTime()
				+ ".jpg";
		filePath = destfilename1;
		filePath = this.truncatUrl(filePath, contextPath);
		logger.info(destfilename1);
		File imageFile1 = null;
		try {
			imageFile1 = new File(destfilename1);
			FileUtils.copyFile(bank_logo, imageFile1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

	private String truncatUrl(String old, String truncat) {
		String url = "";
		url = old.replace(truncat, "");
		url = url.replace(sep, "/");
		return url;
	}

	public OnlineBank fillOnlineBank(OnlineBank onlineBank) {
		String bank_name = paramString("bank_name");
		String bank_value = paramString("bank_value");
		String payment_interface_id =paramString("payment_interface_id");
		String bank_logo = StringUtils
				.isNull(request.getParameter("bank_logo"));
		long id = NumberUtils.getLong(StringUtils.isNull(request
				.getParameter("id")));
		onlineBank.setBank_name(bank_name);
		onlineBank.setBank_value(bank_value);
		onlineBank.setPayment_interface_id(payment_interface_id);
		onlineBank.setBank_logo(bank_logo);
		onlineBank.setId(id);
		return onlineBank;
	}
	public String deletePaymentInterface() throws Exception  {
		 long id=paramInt("id");
		 manageBankService.deletePaymentInterface(id);
		 message("操作成功！", "/admin/bank/payInterfaceList.html");
		return ADMINMSG;	
	}
	public String deleteDownLineBank() throws Exception  {
		 long id=paramInt("id");
		 manageBankService.deleteRechargeDownBank(id);
		 message("操作成功！", "/admin/bank/downLineBankList.html");
		return ADMINMSG;	
	}
}
