package com.p2psys.web.action.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.p2psys.common.enums.EnumHuiKuanStatus;
import com.p2psys.common.enums.EnumPayInterface;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountBack;
import com.p2psys.domain.AccountBank;
import com.p2psys.domain.AccountCash;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.AccountRecharge;
import com.p2psys.domain.AutoTenderOrder;
import com.p2psys.domain.CreditCard;
import com.p2psys.domain.HongBao;
import com.p2psys.domain.Linkage;
import com.p2psys.domain.OperationLog;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.domain.Protocol;
import com.p2psys.domain.Upfiles;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAmount;
import com.p2psys.model.AccountBackModel;
import com.p2psys.model.DetailTender;
import com.p2psys.model.DetailUser;
import com.p2psys.model.HongBaoModel;
import com.p2psys.model.HuikuanModel;
import com.p2psys.model.IdentifySearchParam;
import com.p2psys.model.KefuAndUserInvest;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RankModel;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UpfilesExcelModel;
import com.p2psys.model.UserAccountSummary;
import com.p2psys.model.VIPStatisticModel;
import com.p2psys.model.account.AccountLogModel;
import com.p2psys.model.account.AccountLogSumModel;
import com.p2psys.model.account.AccountModel;
import com.p2psys.model.account.AccountOneDayModel;
import com.p2psys.model.account.AccountReconciliationModel;
import com.p2psys.model.account.AccountSumModel;
import com.p2psys.model.account.OperationLogModel;
import com.p2psys.model.account.TiChengModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.deduct.DeductAccountBackFreezeLog;
import com.p2psys.model.accountlog.recharge.OffRechargeVerifyFailLog;
import com.p2psys.service.AccountService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.CreditCardService;
import com.p2psys.service.LinkageService;
import com.p2psys.service.ManageBankService;
import com.p2psys.service.ManageCashService;
import com.p2psys.service.MessageService;
import com.p2psys.service.NoticeService;
import com.p2psys.service.UpfilesService;
import com.p2psys.service.UserAmountService;
import com.p2psys.service.UserService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.util.file.CsvReader;
import com.p2psys.web.action.BaseAction;

/**
 * 贷款管理Action
 * 
 
 * @date 2012-9-14 下午2:54:43
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class ManageCashAction extends BaseAction {
    
    private static Logger logger = Logger.getLogger(ManageCashAction.class);
    
    private ManageCashService manageCashService;
    private UserService userService;
    private AccountService accountService;
    private UserAmountService userAmountService;
    private UpfilesService upfilesService;
    //V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 start 

    private LinkageService linkageService;
    //v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 start
    private ManageBankService manageBankService;
    public ManageBankService getManageBankService() {
        return manageBankService;
    }

    public void setManageBankService(ManageBankService manageBankService) {
        this.manageBankService = manageBankService;
    }
    //v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 end
    public LinkageService getLinkageService() {
        return linkageService;
    }

    public void setLinkageService(LinkageService linkageService) {
        this.linkageService = linkageService;
    }
    //V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 end 
    
    //v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
    private NoticeService noticeService;
    
    //v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end

    public NoticeService getNoticeService() {
        return noticeService;
    }

    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    public File getBank_logo() {
        return bank_logo;
    }

    public void setBank_logo(File bank_logo) {
        this.bank_logo = bank_logo;
    }
    //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
    //TODO RDPROJECT-314 DELETE
//  private Message message=new Message();
    //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
    
    private MessageService messageService;
    private CreditCardService creditCardService;
    private BorrowService borrowService;
    

    private File excel;
    private String excelFileName;
    
    private File litpic;
    private File bank_logo;
    private String filePath;
    
    private String sep = File.separator;
 
    CreditCard model = new CreditCard();
    private PageDataList pageList;
    public PageDataList getPageList() {
        return pageList;
    }

    public void setPageList(PageDataList pageList) {
        this.pageList = pageList;
    }

    public CreditCard getModel() {
        return model;
    }
    //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
    //TODO RDPROJECT-314 DELETE

    /*public Message getMessage() {
        return message;
    }
    public void setMessage(Message message) {
        this.message = message;
    }*/
    
    //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end

    public MessageService getMessageService() {
        return messageService;
    }
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
    public CreditCardService getCreditCardService() {
        return creditCardService;
    }
    public void setCreditCardService(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }
    public ManageCashService getManageCashService() {
        return manageCashService;
    }
    public void setManageCashService(ManageCashService manageCashService) {
        this.manageCashService = manageCashService;
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
    public UserAmountService getUserAmountService() {
        return userAmountService;
    }
    public void setUserAmountService(UserAmountService userAmountService) {
        this.userAmountService = userAmountService;
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
     * @param upfilesService 要设置的upfilesService
     */
    public void setUpfilesService(UpfilesService upfilesService) {
        this.upfilesService = upfilesService;
    }

    /**
     * 获取excel
     * 
     * @return excel
     */
    public File getExcel() {
        return excel;
    }

    /**
     * 设置excel
     * 
     * @param excel 要设置的excel
     */
    public void setExcel(File excel) {
        this.excel = excel;
    }

    /**
     * 获取excelFileName
     * 
     * @return excelFileName
     */
    public String getExcelFileName() {
        return excelFileName;
    }

    /**
     * 设置excelFileName
     * 
     * @param excelFileName 要设置的excelFileName
     */
    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
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
    public BorrowService getBorrowService() {
        return borrowService;
    }

    public void setBorrowService(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    public String showAllAccount() throws Exception{
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        String username = StringUtils.isNull(request.getParameter("username"));
        String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String realname = StringUtils.isNull(request.getParameter("realname"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        SearchParam param = new SearchParam();
        param.setUsername(username);
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        param.setRealname(realname);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        // v1.6.7.1 RDPROJECT-434 zza 2013-11-13 start
        AccountSumModel accountSumModel = manageCashService.getAccountSum(param);
        request.setAttribute("accountSumModel", accountSumModel);
        // v1.6.7.1 RDPROJECT-434 zza 2013-11-13 end
        // v1.6.7.1 RDPROJECT-434 zza 2013-11-13 原来写的计算总额方法删掉
        PageDataList plist = manageCashService.getUserAccount(page, param);
        setPageAttribute(plist, param);
        setMsgUrl("/admin/cash/showAllAccount.html");
        if (type.isEmpty()) {
            return SUCCESS;
        } else {

            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "account_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
            String[] names = new String[] { "id", "username", "realname", "total", "use_money", "no_use_money",
                    "wait_collect", "wait_repay", "net_assets","jin_wait_repay" };

            String[] titles = new String[] { "ID", "用户名", "真实姓名", "总余额", "可用余额", "冻结金额", "待收金额", "待还金额", "净资产","净值标待还金额" };
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
            List list = manageCashService.getUserAccount(param);
            ExcelHelper.writeExcel(infile, list, AccountSumModel.class, Arrays.asList(names), Arrays.asList(titles));
            export(infile, downloadFile);
            return null;
        }
    }
    
    /**
     * 中融资本专用
     * 每日资金, 读取 dw_account_tj（每天的资金记录）
     * By: timest  2013-03-15 
     * @return
     * @throws Exception
     */
    public String showOneDayAccount() throws Exception{
        String type=StringUtils.isNull(request.getParameter("type"));
        int page=NumberUtils.getInt(request.getParameter("page"));
        String username=StringUtils.isNull(request.getParameter("username"));
        SearchParam param=new SearchParam();
        param.setUsername(username);
        if(type.isEmpty()){
            PageDataList plist=manageCashService.getUserOneDayAcount(page, param);
            setPageAttribute(plist, param);
            setMsgUrl("/admin/cash/showOneDayAccount.html");
            return SUCCESS;
        }else{
            
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="account_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"data/export/"+downloadFile;
            String[] names=new String[]{"id","username","realname",
                    "total","use_money","no_use_money","wait_collect","wait_repay", "jin_money"};
            
            String[] titles=new String[]{"ID","用户名","真实姓名","总余额",
                    "可用余额","冻结金额","待收金额","待还金额", "净资产"};
            List list=manageCashService.getUserOneDayAcount(param);
            ExcelHelper.writeExcel(infile, list, AccountOneDayModel.class, Arrays.asList(names), Arrays.asList(titles));
            export(infile, downloadFile);
            return null;
        }
    }
    
    /**
     * 用户提成
     * By: timest  2013-03-24 
     * @return
     * @throws Exception
     */
    public String showTiChengAccount() throws Exception{
        String type=StringUtils.isNull(request.getParameter("type"));
        int page=NumberUtils.getInt(request.getParameter("page"));
        String username=StringUtils.isNull(request.getParameter("username"));
        SearchParam param=new SearchParam();
        param.setUsername(username);
        if(type.isEmpty()){
            PageDataList plist=manageCashService.getTiChengAcount(page, param);
            setPageAttribute(plist, param);
            setMsgUrl("/admin/cash/showTiChengAccount.html");
            return SUCCESS;
        }else{
            
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="account_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"data/export/"+downloadFile;
            String[] names=new String[]{"addtimes","username","money",
                    };
            
            String[] titles=new String[]{"时间","用户名","好友投资总额(月)"};
            List list=manageCashService.getTiChengAcount(param);
            ExcelHelper.writeExcel(infile, list, TiChengModel.class, Arrays.asList(names), Arrays.asList(titles));
            export(infile, downloadFile);
            return null;
        }
    }
    /**
     * 好友提成
     */
    public String showFriendTiChengAccount() throws Exception{
        String type=StringUtils.isNull(request.getParameter("type"));
        int page=NumberUtils.getInt(request.getParameter("page"));
        String username=StringUtils.isNull(request.getParameter("username"));
        SearchParam param=new SearchParam();
        param.setUsername(username);
        if(type.isEmpty()){
            PageDataList plist=manageCashService.getFriendTiChengAcount(page, param);
            setPageAttribute(plist, param);
            setMsgUrl("/admin/cash/showFriendTiChengAccount.html");
            return SUCCESS;
        }else{
            
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="account_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"data/export/"+downloadFile;
            String[] names=new String[]{"addtimes","username","money",
                    };
            
            String[] titles=new String[]{"时间","用户名","好友投资总额(月)"};
            List list=manageCashService.getFriendTiChengAcount(param);
            ExcelHelper.writeExcel(infile, list, TiChengModel.class, Arrays.asList(names), Arrays.asList(titles));
            export(infile, downloadFile);
            return null;
        }
    }
    /**
     * 提现列表
     * @return
     * @throws Exception
     */
    public String showCash() throws Exception {
        String searchType = StringUtils.isNull(request.getParameter("searchType"));
        if (!searchType.equals("")) {
            request.setAttribute("searchType", searchType);
        }
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        String username = paramString("username");
        String dotime1 = paramString("dotime1");
        String dotime2 = paramString("dotime2");
        String succtime1 = paramString("succtime1");
        String succtime2 = paramString("succtime2");
        String cashTotal_min = paramString("cashTotal_min");
        String cashTotal_max = paramString("cashTotal_max");
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String realname = StringUtils.isNull(request.getParameter("realname"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        SearchParam param = new SearchParam();
        // V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 start
        String select_type = paramString("select_type");
        param.setUsername(username);
        param.setSearch_type(select_type);
        param.setBank_id(paramString("bank_id"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        param.setRealname(realname);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        List<Linkage> linkageList = linkageService.linkageList(25);
        request.setAttribute("bankList", linkageList);
        // V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 end
        // V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 start
        // v1.6.5.3 RDPROJECT-186 xx 2013-09-18 start
        String status = null;
        if ("success".equals(searchType)) {
            status = "1";
        } else if ("fail".equals(searchType)) {
            status = "3,6,8";
        } else if ("fail3".equals(searchType)) {
            status = "3";
        } else if ("fail6".equals(searchType)) {
            status = "6";
        } else if ("fail8".equals(searchType)) {
            status = "8";
        } else if ("cancle".equals(searchType)) {
            status = "4";
        }
        // v1.6.5.3 RDPROJECT-186 xx 2013-09-18 end
        if (!StringUtils.isBlank(status)) {
            String[] statusArray = status.split(",");
            if (statusArray != null) {
                if (statusArray.length > 1) {
                    param.setStatusArray(StringUtils.strarr2intarr(statusArray));
                } else if (statusArray.length == 1) {
                    param.setStatus(statusArray[0]);
                }
            }
        }
        // V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 end
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        param.setCashTotal_min(cashTotal_min);
        param.setCashTotal_max(cashTotal_max);
        param.setSucctime1(succtime1);
        param.setSucctime2(succtime2);
        if (type.isEmpty()) {
            PageDataList plist = manageCashService.getAllCash(page, param);
            double creditSum = accountService.getRechargesum(param, 4);
            double totalSum = accountService.getRechargesum(param, 2);
            double feeSum = accountService.getRechargesum(param, 3);
            request.setAttribute("totalSum", totalSum);
            request.setAttribute("creditSum", creditSum);
            request.setAttribute("feeSum", feeSum);
            setPageAttribute(plist, param);
            setMsgUrl("/admin/cash/showCash.html");
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "cash_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            String[] names = new String[] { "id", "username", "realname", "account", "bankname", "branch", "total",
                    "credited", "fee", "addtime", "verify_time", "status", "verify_username" };
            String[] titles = new String[] { "ID", "用户名称", "真实姓名", "提现账号", "提现银行", "支行", "提现总额", "到账金额", "手续费", "提现时间",
                    "操作时间", "状态", "审核人" };
            List list = manageCashService.getAllCash(param);
            ExcelHelper.writeExcel(infile, list, AccountCash.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
    }
    
    public String viewCash() throws Exception{
        long id=NumberUtils.getLong(request.getParameter("id"));
        AccountCash cash=manageCashService.getAccountCash(id);
        if(cash==null){
            message("非法操作！");
            return ADMINMSG;
        }
        request.setAttribute("cash", cash);
        saveToken("verifycash_token");
        UserAccountSummary uas=accountService.getUserAccountSummary(cash.getUser_id());
        UserAmount amt=userAmountService.getUserAmount(cash.getUser_id());
        request.setAttribute("uas", uas);
        request.setAttribute("amt", amt);
        
        //V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 start
        String verifyStep = StringUtils.isNull(request.getParameter("verify_step"));
        Map verifyConf = getVerifyConf(verifyStep);
        String verifyAction = verifyConf.get("verifyAction").toString();
        request.setAttribute("verifyAction", verifyAction);
        int preStatus = Integer.parseInt(verifyConf.get("preStatus").toString());
        request.setAttribute("preStatus", preStatus);
        request.setAttribute("verifyConf", verifyConf);
        request.setAttribute("verifyStep", verifyStep);
        //V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 end
        
        return SUCCESS;
    }
    //V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 start
    /**
     * 获取提现审核配置
     * 
     
     * @version 1.0 
     * @since 2013-9-11
     */ 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getVerifyConf(String verifyStep) {
        //v1.6.6.1 RDPROJECT-178 liukun 2013-09-17 begin
        //String verifyConfStr = Global.getString("cash_verify_conf");
        RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.CASH_VERIFY_CONF.getValue()));//提现规则
        String verifyConfStr = rule.getRule_check();
        //v1.6.6.1 RDPROJECT-178 liukun 2013-09-17 end
        
        /*try {
            JSONArray ja = new JSONArray(verifyConfStr);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                if (jo.getString("stepName").equalsIgnoreCase(verifyStep)) {
                    verifyConf.put("stepName", jo.getString("stepName"));
                    verifyConf.put("stepIndex", jo.getInt("stepIndex"));
                    verifyConf.put("preStatus", jo.getInt("preStatus"));
                    verifyConf.put("succStatus", jo.getInt("succStatus"));
                    verifyConf.put("failStatus", jo.getInt("failStatus"));
                    verifyConf.put("verifyAction", jo.getString("verifyAction"));
                    verifyConf.put("need", jo.getInt("need"));
                    verifyConf.put("succInfo", jo.getString("succInfo"));
                    verifyConf.put("failInfo", jo.getString("failInfo"));
                    break;
                }
            }
        } catch (Exception e) {

        }*/
        
        // v1.6.7.2 改用FastJson xx 2013-12-12 start
        HashMap verifyConf = new HashMap();
        try {
        	JSONArray ja = JSONArray.parseArray(verifyConfStr);
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo.getString("stepName").equalsIgnoreCase(verifyStep)) {
                    verifyConf.put("stepName", jo.getString("stepName"));
                    verifyConf.put("stepIndex", jo.getIntValue("stepIndex"));
                    verifyConf.put("preStatus", jo.getIntValue("preStatus"));
                    verifyConf.put("succStatus", jo.getIntValue("succStatus"));
                    verifyConf.put("failStatus", jo.getIntValue("failStatus"));
                    verifyConf.put("verifyAction", jo.getString("verifyAction"));
                    verifyConf.put("need", jo.getIntValue("need"));
                    verifyConf.put("succInfo", jo.getString("succInfo"));
                    verifyConf.put("failInfo", jo.getString("failInfo"));
                    break;
                }
            }
        } catch (Exception e) {
        	logger.error(e);
        }
        // v1.6.7.2 改用FastJson xx 2013-12-12 end
        return verifyConf;
    }
    
    // v1.6.7.1 RDPROJECT-467 2013-11-19 start
    /**
     * 验证提现是否可审核通过
     * @param cash
     * @return
     */
    private boolean checkCash(AccountCash cash){
        boolean result = false;
        long userId = cash.getUser_id();
        double repayTotalWithJin = borrowService.getRepayTotalWithJin(userId);
        Account act = accountService.getAccount(userId);
        if(act.getUse_money()+act.getCollection()-repayTotalWithJin>=0){
            result = true;
        }
        return result;
    }
    // v1.6.7.1 RDPROJECT-467 2013-11-19 end
    
    /**
     * 提现审核中间审核步骤
     * 
     
     * @version 1.0 
     * @since 2013-9-11
     */ 
    public String verifyCashStep() throws Exception{
        long id=NumberUtils.getLong(request.getParameter("id"));
        String verify_remark=StringUtils.isNull(request.getParameter("verify_remark"));
        //这里没有具体的状态，只有本审核是审核通过还是不通过
        String verifyStep = request.getParameter("verify_step");
        int verifyResult = Integer.parseInt(request.getParameter("status"));
        
        Map verifyConf = getVerifyConf(verifyStep);
        int preStatus = Integer.parseInt(verifyConf.get("preStatus").toString());
        int succStatus = Integer.parseInt(verifyConf.get("succStatus").toString());
        int failStatus = Integer.parseInt(verifyConf.get("failStatus").toString());
        //v1.6.5.3 RDPROJECT-186 xx 2013-09-18 start
        String backUrl = null;
        if("kefuVerify".equals(verifyStep)){
            backUrl = "showFirstVerifyCash.html?verify_step=kefuVerify";
        } else if("caiwuVerify".equals(verifyStep)){
            backUrl = "showSecondVerifyCash.html?verify_step=caiwuVerify";
        } else if("chunaVerify".equals(verifyStep)){
            backUrl = "showThirdVerifyCash.html?verify_step=chunaVerify";
        }
        //v1.6.5.3 RDPROJECT-186 xx 2013-09-18 end
        AccountCash cash=manageCashService.getAccountCash(id);
        if(cash==null||StringUtils.isBlank(verify_remark)||
                (cash.getStatus()!=preStatus)){
            message("非法操作！",backUrl);
            return ADMINMSG;
        }

        String tokenMsg=checkToken("verifycash_token");
        if(!StringUtils.isBlank(tokenMsg)){
            message(tokenMsg);
            return ADMINMSG;
        }
        User authUser=getAuthUser();
        long verify_userid=1;
        if(authUser!=null) verify_userid=authUser.getUser_id();
        
        if(verifyResult==Constant.CASH_VERIFY_STEP_SUCC){
            cash.setStatus(succStatus);
            // v1.6.7.1 RDPROJECT-467 2013-11-19 start
            if(!checkCash(cash)){
                message("审核失败！用户资金情况不满足提现要求。",backUrl);
                return ADMINMSG;
            }
            // v1.6.7.1 RDPROJECT-467 2013-11-19 end
        }else{
            cash.setStatus(failStatus);
        }
        
        cash.setVerify_userid(verify_userid);
        cash.setVerify_remark(verify_remark);
        cash.setVerify_time(this.getTimeStr());
        
        AccountLog log=new AccountLog(cash.getUser_id(),Constant.CASH_SUCCESS,verify_userid,
                getTimeStr(),getRequestIp());
        OperationLog operationLog=new OperationLog(cash.getUser_id(), authUser.getUser_id(), Constant.CASH_SUCCESS, this.getTimeStr(), this.getRequestIp(), "");
        // v1.6.5.3 RDPROJECT-97 zza 2013-09-18 start
        double fee = NumberUtils.getDouble(request.getParameter("fee"));
        if (NumberUtils.getDouble(cash.getOld_fee()) != fee) {
            double credited = NumberUtils.getDouble(cash.getTotal()) - fee;
            cash.setCredited(StringUtils.isNull(credited));
            cash.setFee(StringUtils.isNull(fee));
            
        }
        // v1.6.5.3 RDPROJECT-97 zza 2013-09-18 end
        //v1.6.5.3 RDPROJECT-186 xx 2013-09-18 start
        manageCashService.verifyCashStep(cash, log,operationLog, verifyStep, verifyResult, verifyConf);
        //v1.6.5.3 RDPROJECT-186 xx 2013-09-18 end
        message("操作成功！",backUrl);
        
        return ADMINMSG;
    }   
    //V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 end
    
    
    //V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 start
    //此方法更改过多，重新定义，这里只是暂时保留
    /*public String _del_verifyCash() throws Exception{
        long id=NumberUtils.getLong(request.getParameter("id"));
        String verify_remark=StringUtils.isNull(request.getParameter("verify_remark"));
        int status=NumberUtils.getInt(request.getParameter("status"));
        AccountCash cash=manageCashService.getAccountCash(id);
        if(cash==null||StringUtils.isBlank(verify_remark)||
                (status<0||status>3)){
            message("非法操作！");
            return ADMINMSG;
        }
        if(cash.getStatus()==1){
            message("该提现记录已经审核，不允许重复操作！");
            return ADMINMSG;
        }
        if(cash.getStatus()==4){
            message("该提现记录已经取消，不允许审核！");
            return ADMINMSG;
        }
        String tokenMsg=checkToken("verifycash_token");
        if(!StringUtils.isBlank(tokenMsg)){
            message(tokenMsg);
            return ADMINMSG;
        }
        User authUser=getAuthUser();
        long verify_userid=1;
        if(authUser!=null) verify_userid=authUser.getUser_id();
        cash.setStatus(status);
        cash.setVerify_userid(verify_userid);
        cash.setVerify_remark(verify_remark);
        cash.setVerify_time(this.getTimeStr());
        
        AccountLog log=new AccountLog(cash.getUser_id(),Constant.CASH_SUCCESS,verify_userid,
                getTimeStr(),getRequestIp());
        OperationLog operationLog=new OperationLog(cash.getUser_id(), authUser.getUser_id(), Constant.CASH_SUCCESS, this.getTimeStr(), this.getRequestIp(), "");
        manageCashService.verifyCash(cash, log,operationLog);
        message("操作成功！","/admin/cash/showCash.html");
        
        //发送通知

        long userid = cash.getUser_id();
        User user=userService.getUserById(cash.getUser_id());
        Smstype smstype = null;
        boolean isSmssend = false;
        
        if (cash.getStatus() == 1) {
            smstype = Global.getSmstype(Constant.SMS_CASH_VERIFY_SUCC);
            isSmssend = userService.isSmssend(userid, smstype);
            if (isSmssend) {
                Notice s = new Sms();

                String p0 = Global.getValue("webname");
                String p1 = user.getUsername();
                String p2 = DateUtils.dateStr5(cash.getAddtime());
                String p3 = String.valueOf(cash.getTotal());
                String[] paras = new String[] { p0, p1, p2, p3 };

                s.setAddtime(cash.getAddtime());
                s.setContent(StringUtils.fillTemplet(smstype.getTemplet(),
                        Constant.SMS_TEMPLET_PHSTR, paras));
                s.setReceive_userid(userid);
                s.setSend_userid(1);
                NoticeJobQueue.NOTICE.offer(s);
            }
        } else {
            smstype = Global.getSmstype(Constant.SMS_CASH_VERIFY_FAIL);
            isSmssend = userService.isSmssend(userid, smstype);
            if (isSmssend) {
                Notice s = new Sms();

                String p0 = Global.getValue("webname");
                String p1 = user.getUsername();
                String p2 = DateUtils.dateStr5(cash.getAddtime());
                String p3 = String.valueOf(cash.getTotal());
                String[] paras = new String[] { p0, p1, p2, p3 };

                s.setAddtime(cash.getAddtime());
                s.setContent(StringUtils.fillTemplet(smstype.getTemplet(),
                        Constant.SMS_TEMPLET_PHSTR, paras));
                s.setReceive_userid(userid);
                s.setSend_userid(1);
                NoticeJobQueue.NOTICE.offer(s);
            }
        }
        
        return ADMINMSG;
    }*/
    
    public String verifyCash() throws Exception{
        long id=NumberUtils.getLong(request.getParameter("id"));
        String verify_remark=StringUtils.isNull(request.getParameter("verify_remark"));
//      int status=NumberUtils.getInt(request.getParameter("status"));
        
        String verifyStep = request.getParameter("verify_step");
        int verifyResult = Integer.parseInt(request.getParameter("status"));
        
        Map verifyConf = getVerifyConf(verifyStep);
        int preStatus = Integer.parseInt(verifyConf.get("preStatus").toString());
        int succStatus = Integer.parseInt(verifyConf.get("succStatus").toString());
        int failStatus = Integer.parseInt(verifyConf.get("failStatus").toString());
        //v1.6.5.3 RDPROJECT-186 xx 2013-09-18 start
        String backUrl = null;
        if("kefuVerify".equals(verifyStep)){
            backUrl = "showFirstVerifyCash.html?verify_step=kefuVerify";
        } else if("caiwuVerify".equals(verifyStep)){
            backUrl = "showSecondVerifyCash.html?verify_step=caiwuVerify";
        } else if("chunaVerify".equals(verifyStep)){
            backUrl = "showThirdVerifyCash.html?verify_step=chunaVerify";
        }
        //v1.6.5.3 RDPROJECT-186 xx 2013-09-18 end
        AccountCash cash=manageCashService.getAccountCash(id);
        if(cash==null||StringUtils.isBlank(verify_remark)||
                ((cash.getStatus()!=preStatus))){
            message("非法操作！",backUrl);
            return ADMINMSG;
        }
        String tokenMsg=checkToken("verifycash_token");
        if(!StringUtils.isBlank(tokenMsg)){
            message(tokenMsg);
            return ADMINMSG;
        }
        User authUser=getAuthUser();
        long verify_userid=1;
        if (null != authUser) {
            verify_userid = authUser.getUser_id();
        }
        
        if(verifyResult==Constant.CASH_VERIFY_STEP_SUCC){
            cash.setStatus(succStatus);
            // v1.6.7.1 RDPROJECT-467 2013-11-19 start
            if(!checkCash(cash)){
                message("审核失败！用户资金情况不满足提现要求。",backUrl);
                return ADMINMSG;
            }
            // v1.6.7.1 RDPROJECT-467 2013-11-19 end
        }else{
            cash.setStatus(failStatus);
        }
        
        cash.setVerify_userid(verify_userid);
        cash.setVerify_remark(verify_remark);
        cash.setVerify_time(this.getTimeStr());
        
        AccountLog log=new AccountLog(cash.getUser_id(),Constant.CASH_SUCCESS,verify_userid,
                getTimeStr(),getRequestIp());
        OperationLog operationLog=new OperationLog(cash.getUser_id(), authUser.getUser_id(), Constant.CASH_SUCCESS, this.getTimeStr(), this.getRequestIp(), "");
        // v1.6.5.3 RDPROJECT-97 zza 2013-09-18 start
        double fee = NumberUtils.getDouble(request.getParameter("fee"));
        if (NumberUtils.getDouble(cash.getOld_fee()) != fee) {
            double credited = NumberUtils.getDouble(cash.getTotal()) - fee;
            cash.setCredited(StringUtils.isNull(credited));
            cash.setFee(StringUtils.isNull(fee));
            
        }
        // v1.6.5.3 RDPROJECT-97 zza 2013-09-18 end
        //v1.6.5.3 RDPROJECT-186 xx 2013-09-18 start
        manageCashService.verifyCash(cash, log, operationLog, verifyConf);
        //v1.6.5.3 RDPROJECT-186 xx 2013-09-18 end
        message("操作成功！",backUrl);
        
        //发送通知

        //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
        /*long userid = cash.getUser_id();
        User user=userService.getUserById(cash.getUser_id());
        Smstype smstype = null;
        boolean isSmssend = false;
        Notice m = new Message();
        if (verifyResult==Constant.CASH_VERIFY_STEP_SUCC && cash.getStatus() == Constant.CASH_VERIFY_FINAL_STATUS_SUCC) {
            //上面的双重判断是为了保证是提现终审通过，可以给用户打款了，发送提现成功短信
            smstype = Global.getSmstype(Constant.SMS_CASH_VERIFY_SUCC);
            isSmssend = userService.isSmssend(userid, smstype);
            if (isSmssend) {
                Notice s = new Sms();

                //V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 start
//              String p0 = Global.getValue("webname");
//              String p1 = user.getUsername();
//              String p2 = DateUtils.dateStr5(cash.getAddtime());
//              String p3 = String.valueOf(cash.getTotal());
//              String[] paras = new String[] { p0, p1, p2, p3 };
                
                Global.setTransfer("webname", Global.getValue("webname"));
                Global.setTransfer("user", user);
                Global.setTransfer("cash", cash);

                s.setAddtime(cash.getAddtime());
//              s.setContent(StringUtils.fillTemplet(smstype.getTemplet(),
//                      Constant.SMS_TEMPLET_PHSTR, paras));
                s.setContent(StringUtils.fillTemplet(smstype.getTemplet()));
                //V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 end
                s.setReceive_userid(userid);
                s.setSend_userid(1);
                NoticeJobQueue.NOTICE.offer(s);
                
                m.setTitle("提现成功");
                m.setSend_userid(1);
                m.setReceive_userid(userid);
                m.setAddtime(DateUtils.getNowTimeStr());
                m.setContent("尊敬的用户"+ user.getUsername()+"，您于"+DateUtils.dateStr2(cash.getAddtime()) +
                "进行提现"+cash.getTotal()+"元已经审核成功，即将安排财务打款，请注意查收！");
                //v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
                //TODO
                //NoticeJobQueue.MESSAGE.offer(m);
                //v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
            }
        } else {
            smstype = Global.getSmstype(Constant.SMS_CASH_VERIFY_FAIL);
            isSmssend = userService.isSmssend(userid, smstype);
            if (isSmssend) {
                Notice s = new Sms();

                //V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 start
//              String p0 = Global.getValue("webname");
//              String p1 = user.getUsername();
//              String p2 = DateUtils.dateStr5(cash.getAddtime());
//              String p3 = String.valueOf(cash.getTotal());
//              String[] paras = new String[] { p0, p1, p2, p3 };
                
                Global.setTransfer("webname", Global.getValue("webname"));
                Global.setTransfer("user", user);
                Global.setTransfer("cash", cash);

                s.setAddtime(cash.getAddtime());
//              s.setContent(StringUtils.fillTemplet(smstype.getTemplet(),
//                      Constant.SMS_TEMPLET_PHSTR, paras));
                s.setContent(StringUtils.fillTemplet(smstype.getTemplet()));
                //V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 end
                s.setReceive_userid(userid);
                s.setSend_userid(1);
                NoticeJobQueue.NOTICE.offer(s);
                
                m.setTitle("提现失败");
                m.setSend_userid(1);
                m.setReceive_userid(userid);
                m.setAddtime(DateUtils.getNowTimeStr());
                m.setContent("尊敬的用户"+ user.getUsername()+"，您于"+DateUtils.dateStr2(cash.getAddtime()) +
                "进行提现"+cash.getTotal()+"元审核不通过，请登录网站查看详情！");
                //v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
                //TODO
                //NoticeJobQueue.MESSAGE.offer(m);
                //v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
            }
        }*/
        //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
        return ADMINMSG;
    }
    
    /**
     * 添加线下充值处理
     * @return forward路径
     * @throws Exception 异常
     */
    public String recharge() throws Exception {
        if (Global.getWebid().equals("jsy")) {
          //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
            List list = userService.getKfList(2);
          //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
            request.setAttribute("kflist", list);
        }
        String actionType = request.getParameter("actionType");
        if (StringUtils.isBlank(actionType)) {
            setMsgUrl("/admin/cash/recharge.html");
            return SUCCESS;
        } else {
            // 用户名
            String username = StringUtils.isNull(request.getParameter("username"));
            // 充值金额
            double money = NumberUtils.getDouble(request.getParameter("money"));
            // 备注
            String remark = StringUtils.isNull(request.getParameter("remark"));
            // 充值类型
            String type = StringUtils.isNull(request.getParameter("type"));
            if (StringUtils.isBlank(type)) {
                type = "2";
            }

            if (StringUtils.isBlank(username)) {
                message("用户名不能为空！");
                return ADMINMSG;
            } else if (money <= 0) {
                message("充值的金额必须大于0");
                return ADMINMSG;
            } else if (money >= 100000000) {
                message("你充值的金额过大,目前系统仅支持千万级别的充值");
                return ADMINMSG;
            } else if (StringUtils.isBlank(remark)) {
                message("备注不能为空！");
                return ADMINMSG;
            }

            User u = userService.getUserByName(username);
            if (u == null) {
                message("用户名:" + username + "不存在！");
                return ADMINMSG;
            }
            AccountRecharge r = new AccountRecharge();
            r.setUser_id(u.getUser_id());
            r.setMoney(money);
            r.setType(type);
            //v1.6.6.2 RDPROJECT-173 wcw 2013-10-28 start
            r.setPayment(EnumPayInterface.BACK_RECHARGE.getValue());
            //v1.6.6.2 RDPROJECT-173 wcw 2013-10-28 end
            r.setFee("0");
            r.setRemark(remark);
            r.setAddtime(getTimeStr());
            r.setAddip(getRequestIp());
            r.setTrade_no(StringUtils.generateTradeNO(u.getUser_id(), "E"));
            if (Global.getWebid().equals("jsy")) {
                String recharge_kefuid = StringUtils.isNull(request.getParameter("recharge_kefuid"));
                if (!StringUtils.isBlank(recharge_kefuid)) {
                    r.setRecharge_kefuid(NumberUtils.getLong(recharge_kefuid));
                } else {
                    message("请填写充值所属客服！！！");
                    return ADMINMSG;
                }
            }
            // 生成资金记录对象
            accountService.addRecharge(r);
            User auth_user = (User) session.get(Constant.AUTH_USER);
            DetailUser detailUser = userService.getDetailUser(auth_user.getUser_id());
            OperationLog operationLog = new OperationLog(u.getUser_id(), auth_user.getUser_id(),
                    Constant.BACKSTAGE_RECHARGE_APPLY, this.getTimeStr(), this.getRequestIp(), "");
            operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip() + "）用户名为"
                    + detailUser.getUsername() + "的操作员申请后台线下充值" + money + "元成功，等待审核");
            accountService.addOperationLog(operationLog);
            message("充值成功，等待审核！");
            return ADMINMSG;
        }
    }   
    
    public String back() throws Exception {
    	 // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 start
        String actionType = request.getParameter("actionType");
        if (StringUtils.isBlank(actionType)) {
            setMsgUrl("/admin/cash/back.html");
            request.setAttribute("actionType", "back");
            return SUCCESS;
        } else {
            String username = StringUtils.isNull(request.getParameter("username"));
            double money = NumberUtils.getDouble2(request.getParameter("money"));
            String remark = StringUtils.isNull(request.getParameter("remark"));
            if (StringUtils.isBlank(username)) {
                message("用户名不能为空！");
                return ADMINMSG;
            } else if (money <= 0) {
                message("扣款金额过小,请重新输入金额");
                return ADMINMSG;
            } else if (money >= 100000000) {
                message("你扣款的金额过大,目前系统仅支持千万级别的扣款");
                return ADMINMSG;
            } else if (StringUtils.isBlank(remark)) {
                message("备注不能为空！");
                return ADMINMSG;
            }

            User u = userService.getUserByName(username);
            if (u == null) {
                message("用户名:" + username + "不存在！");
                return ADMINMSG;
            }
            Account act = accountService.getAccount(u.getUser_id());
            if (money > act.getUse_money()) {
                message("余额不足！");
                return ADMINMSG;
            }
            AccountBack b = new AccountBack();
            b.setUser_id(u.getUser_id());
            b.setMoney(money);
            b.setType("2");
            b.setAddtime(getTimeStr());
            b.setAddip(getRequestIp());
            b.setRemark(remark);
            b.setVerify_remark(remark);
            b.setTrade_no(StringUtils.generateTradeNO(u.getUser_id(), "E"));
            accountService.addBack(b);
            // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 end
            accountService.updateAccount(0, -money, money, u.getUser_id());

            act = accountService.getAccount(u.getUser_id());
            // 生成资金记录对象
            User authUser = (User) session.get(Constant.AUTH_USER);
            AccountLog log = new AccountLog(u.getUser_id(), Constant.FREEZE, authUser.getUser_id(), getTimeStr(),
                    getRequestIp());

            // v1.6.6.1 liukun 2013-10-15 end
            /*
             * log.setMoney(money); log.setTotal(act.getTotal()); log.setUse_money(act.getUse_money());
             * log.setNo_use_money(act.getNo_use_money()); log.setCollection(act.getCollection());
             * log.setRemark("后台扣款冻结"+money+"元,备注:"+remark); accountService.addAccountLog(log);
             */

            Global.setTransfer("money", money);
            BaseAccountLog feeBLog = new DeductAccountBackFreezeLog(money, act);
            feeBLog.doEvent();

            // v1.6.6.1 liukun 2013-10-15 end

            DetailUser detailUser = userService.getDetailUser(authUser.getUser_id());
            OperationLog operationLog = new OperationLog(u.getUser_id(), authUser.getUser_id(),
                    Constant.BACKSTAGE_BACK_APPLY, this.getTimeStr(), this.getRequestIp(), "");
            // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 start
            //operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip() + "）用户名为"
            	//	+ detailUser.getUsername() + "的操作员申请后台扣款" + r.getUsername() + money + "元成功，等待审核");
            operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip() + "）用户名为"
            		+ detailUser.getUsername() + "的操作员申请后台扣款" +username +" "+ money + "元成功，等待审核");
            // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 end
            accountService.addOperationLog(operationLog);
            message("扣款成功,等待管理员审核！");
            return ADMINMSG;
        }
    }
    
    public String backlist() throws Exception{
        String type=StringUtils.isNull(request.getParameter("type"));
        int page=NumberUtils.getInt(request.getParameter("page"));
        SearchParam param=new SearchParam();
        param.setPayment(EnumPayInterface.BACK.getValue());
        fillSearchParam(param);
        if(type.isEmpty()){
        	PageDataList plist=accountService.getBackList(page, param);
        	setPageAttribute(plist, param);
        	setMsgUrl("/admin/cash/backlist.html");
            return SUCCESS;
        }else{
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="back_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"/data/export/"+downloadFile;
            String[] names=new String[]{"id","trade_no","username",
                    "realname","type","money","addtime","status"};
            String[] titles=new String[]{"ID","流水号","用户名称",
                    "真实姓名","类型","扣款金额","扣款时间","状态"};
            List list=accountService.getBackList(param);
            
            ExcelHelper.writeExcel(infile, list, AccountBackModel.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
     // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 end
    }
    
    /**
     * 充值记录查询、报表导出
     * 
     * @return forward路径
     * @throws Exception 异常
     */
    public String rechargelist() throws Exception {
        if (Global.getWebid().equals("jsy")) {
          //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
            List list = userService.getKfList(2);
          //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
            request.setAttribute("kflist", list);
        }
        List<PaymentInterface> interfaceList = accountService.paymentInterface_unsingle(1);
        request.setAttribute("interfaceList", interfaceList);
        //v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 start
        List downRechargeBankList=manageBankService.getDownRechargeBankList();
        request.setAttribute("downRechargeBankList", downRechargeBankList);
      //v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 end
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        SearchParam param = new SearchParam();
        if (Global.getWebid().equals("wzdai")) {
            param.setPayment("-50");// 50表示扣款,-50表示充值记录
        }
        fillSearchParam(param);
        PageDataList plist = accountService.getRechargeList(page, param);
        
        //v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start  
        request.setAttribute("listsize", accountService.getRechargeCount(param));
        //v1.6.7.1 RDPROJECT-510 cx 2013-12-04 end
        
        double sum = accountService.getRechargesum(param, 1);
        request.setAttribute("sum", sum);
        setPageAttribute(plist, param);
        setMsgUrl("/admin/cash/rechargelist.html");
        if (type.isEmpty()) {
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "recharge_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            String[] names;
            String[] titles;
            if (Global.getWebid().equals("jsy")) {
                names = new String[] { "id", "trade_no", "username", "realname", "type", "paymentname", "money", "fee",
                        "total", "addtime", "status", "recharge_kefu_username" };
                titles = new String[] { "ID", "流水号", "用户名称", "真实姓名", "类型", "所属银行", "充值金额", "费用", "到账金额", "充值时间", "状态",
                        "充值所属客服" };
            } else if ("huidai".equals(Global.getWebid())) { // 徽州贷
                names = new String[] { "id", "trade_no", "username", "realname", "type", "paymentname", "money", "fee",
                        "total", "addtime", "status", "remark" };
                titles = new String[] { "ID", "流水号", "用户名称", "真实姓名", "类型", "所属银行", "充值金额", "费用", "到账金额", "充值时间", "状态",
                        "备注" };
            } else {
                names = new String[] { "id", "trade_no", "username", "realname", "type", "paymentname", "money", "fee",
                        "total", "addtime", "status","verify_username" };
                titles = new String[] { "ID", "流水号", "用户名称", "真实姓名", "类型", "所属银行", "充值金额", "费用", "到账金额", "充值时间", "状态","审核人" };
            }
            List list = accountService.getRechargeList(param);

            ExcelHelper.writeExcel(infile, list, AccountRecharge.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
    }
    
    public String viewRecharge() throws Exception{
        long id=NumberUtils.getLong(request.getParameter("id"));
        AccountRecharge r=accountService.getRecharge(id);
        if(r==null){
            message("非法操作！");
            return ADMINMSG;
        }
        request.setAttribute("recharge", r);
        saveToken("verifyrecharge_token");
        return SUCCESS;
    }
    
    public String viewBack() throws Exception {
    	// v1.6.7.2 RDPROJECT-548 lx 2013-12-13 start
    	long id = NumberUtils.getLong(request.getParameter("id"));
    	AccountBack b = accountService.getBack(id);
    	if (b == null) {
    		message("非法操作！");
    		return ADMINMSG;
    	}
    	request.setAttribute("back", b);
    	saveToken("verifyback_token");
    	return SUCCESS;
    	// v1.6.7.2 RDPROJECT-548 lx 2013-12-13 end
    }
    
    public String verifyRecharge() throws Exception{
        setMsgUrl("/admin/cash/rechargelist.html");
        long id=NumberUtils.getLong(request.getParameter("id"));
        double money=NumberUtils.getDouble(request.getParameter("total"));
        String verify_remark=StringUtils.isNull(request.getParameter("verify_remark"));
        int status=NumberUtils.getInt(request.getParameter("status"));
        User auth_user=(User)session.get(Constant.AUTH_USER);
        String tokenMsg=checkToken("verifyrecharge_token");
        if(!StringUtils.isBlank(tokenMsg)){
            message(tokenMsg);
            return ADMINMSG;
        }
        if(id<=0){
            message("非法操作！");
            return ADMINMSG;
        }
        if(money<=0){
            message("当前充值金额为"+money+",充值金额不能为负数！");
            return ADMINMSG;
        }else if(StringUtils.isBlank(verify_remark)){
            message("审核备注不能为空！");
            return ADMINMSG;
        }
        AccountRecharge r=accountService.getRecharge(id);
        if(r==null||(status!=1&&status!=2)){
            message("非法操作！");
            return ADMINMSG;
        }
        if(r.getStatus()==1){
            message("该记录已经审核通过，不允许重复操作！");
            return ADMINMSG;
        }
        // V1.6.6.2 RDPROJECT-2 ljd 2013-10-21 start
        if(r.getStatus()==2){
            message("该记录已经审核不通过，不允许重复操作！");
            return ADMINMSG;
        }
        // V1.6.6.2 RDPROJECT-2 ljd 2013-10-21 end
        
        //审核信息
        r.setStatus(status);
        r.setVerify_time(getTimeStr());
        r.setVerify_userid(auth_user.getUser_id());
        r.setVerify_remark(verify_remark);
        //资金记录表
        
        // V1.6.6.1 liukun 2013-09-10 start
        AccountLog log=new AccountLog(r.getUser_id(),Constant.RECHARGE,getAuthUser().getUser_id(),
                getTimeStr(),getRequestIp());
        // TODO lk need new log recharge
        // V1.6.6.1 liukun 2013-09-10 end
        
        //操作记录表
        OperationLog operationLog=new OperationLog(r.getUser_id(), getAuthUser().getUser_id(), Constant.RECHARGE, getTimeStr(),getRequestIp(), "");
        
        //及时雨委托协议
        Protocol protocol=new Protocol(0, Constant.RECHARGE_PROTOCOL, this.getTimeStr(),this.getRequestIp(), "");
        String flag="";
        
        if(r.getStatus()!=1){
            log.setRemark("审核充值记录，充值"+r.getMoney()+"元失败！");
            //message.setName("充值失败");
            flag="失败";
        }else{
            log.setRemark("审核充值记录，充值"+r.getMoney()+"元成功！");
            //message.setName("充值成功");
            flag="成功";
        }
        log.setRemark(log.getRemark()+"备注信息:"+StringUtils.isNull(r.getVerify_remark()));
        if (NumberUtils.getInt(r.getType())==1) {
            //message.setContent("网上充值"+flag+",充值金额为"+r.getMoney()+",流水号："+r.getTrade_no());
            log.setRemark(StringUtils.isNull(log.getRemark())+"网上充值流水号:"+r.getTrade_no());
        }else {
            //message.setContent("线下充值"+flag+",充值金额为"+r.getMoney()+",流水号："+r.getTrade_no());
        }
        if(Global.getWebid().equals("wzdai")){
            String hongbao_apr=Global.getValue("hongbao_apr");
            double hongbao_add=NumberUtils.format2((r.getMoney())*(NumberUtils.getDouble(hongbao_apr)));
            HongBao hongbao=new HongBao();
            hongbao.setAddip(this.getRequestIp());
            hongbao.setAddtime(this.getTimeStr());
            hongbao.setHongbao_money(hongbao_add);
            hongbao.setRemark("");
            hongbao.setType(Constant.HONGBAO_ADD);
            hongbao.setUser_id(r.getUser_id());
            accountService.verifyRecharge(r, log, hongbao,operationLog);
        }else if(Global.getWebid().equals("jsy")){
            accountService.verifyRecharge(r, log,operationLog,protocol);
        }else{
            accountService.verifyRecharge(r, log,operationLog);
        }
        
        request.setAttribute("recharge", r);
        message("审核成功!","/admin/cash/rechargelist.html");
        
        //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
        //TODO RDPROJECT-314 DONE
        
        /*message.setSent_user(auth_user.getUser_id());
        message.setReceive_user(r.getUser_id());
        message.setStatus(0);
        message.setType(Constant.SYSTEM);
        
        
        message.setAddip(getRequestIp());
        message.setAddtime(getTimeStr());
        messageService.addMessage(message);
        //将recharge对象加到队列中

        long userid = r.getUser_id();
        User user=userService.getUserById(r.getUser_id());
        Smstype smstype = null;
        boolean isSmssend = false;
        
        if (r.getStatus() == 1) {
            
        } else {
            smstype = Global.getSmstype(Constant.SMS_DOWN_RECHARGE_VERIFY_FAIL);
            isSmssend = userService.isSmssend(userid, smstype);
            if (isSmssend) {
                Notice s = new Sms();

                //V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 start
                
                
                Global.setTransfer("webname", Global.getValue("webname"));
                Global.setTransfer("user", user);
                Global.setTransfer("recharge", r);
                
                

                s.setAddtime(r.getAddtime());
                
                s.setContent(StringUtils.fillTemplet(smstype.getTemplet()));
                //V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 end
                s.setReceive_userid(userid);
                s.setSend_userid(1);
                NoticeJobQueue.NOTICE.offer(s);
            }
        }*/
        if (r.getStatus() == 1) {
            
        } else {
            long userid = r.getUser_id();
            Account act=accountService.getAccount(userid);
            Global.setTransfer("recharge", r);
            BaseAccountLog blog=new OffRechargeVerifyFailLog(0, act,userid);
            blog.doEvent();
        }
        //v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
        return ADMINMSG;
    }
    
    public String verifyBack() throws Exception {
        User authUser = (User) session.get(Constant.AUTH_USER);
        setMsgUrl("/admin/cash/backlist.html");
        long id = NumberUtils.getLong(request.getParameter("id"));
        double money = NumberUtils.getDouble(request.getParameter("total"));
        String verify_remark = StringUtils.isNull(request.getParameter("verify_remark"));
        int status = NumberUtils.getInt(request.getParameter("status"));
        String tokenMsg = checkToken("verifyback_token");
        if (!StringUtils.isBlank(tokenMsg)) {
            message(tokenMsg);
            return ADMINMSG;
        }
        if (id <= 0) {
            message("非法操作！");
            return ADMINMSG;
        }
        if (money <= 0) {
            message("当前扣款金额为" + money + ",扣款金额必须为正数！");
            return ADMINMSG;
        } else if (StringUtils.isBlank(verify_remark)) {
            message("审核备注不能为空！");
            return ADMINMSG;
        }
        // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 start
       /* AccountRecharge r = accountService.getRecharge(id);
        if (r == null || (status != 1 && status != 2)) {
        	message("非法操作！");
        	return ADMINMSG;
        }
        if (r.getStatus() == 1) {
        	message("该记录已经审核通过，不允许重复操作！");
        	return ADMINMSG;
        }
        // 审核信息
        r.setStatus(status);
        r.setVerify_time(getTimeStr());
        r.setVerify_userid(authUser.getUser_id());
        r.setVerify_remark(verify_remark);
        // 资金记录表
        AccountLog log = new AccountLog(r.getUser_id(), Constant.ACCOUNT_BACK, getAuthUser().getUser_id(),
        		getTimeStr(), getRequestIp());
        
        // 操作记录表
        OperationLog operationLog = new OperationLog(r.getUser_id(), getAuthUser().getUser_id(), Constant.RECHARGE,
        		getTimeStr(), getRequestIp(), "");
        if (r.getStatus() != 1) {
        	log.setRemark("审核扣款记录，扣款" + r.getMoney() + "元不成功！备注:" + verify_remark);
        } else {
        	log.setRemark("审核扣款记录，扣款" + r.getMoney() + "元成功！备注:" + verify_remark);
        }
        accountService.verifyRecharge(r, log, operationLog);
        request.setAttribute("recharge", r);
        message("审核成功!");*/
        AccountBackModel b = accountService.getBack(id);
        if (b == null || (status != 1 && status != 2)) {
        	message("非法操作！");
        	return ADMINMSG;
        }
        if (b.getStatus() == 1) {
        	message("该记录已经审核通过，不允许重复操作！");
        	return ADMINMSG;
        }
        // 审核信息
        b.setStatus(status);
        b.setVerify_time(getTimeStr());
        b.setVerify_userid(authUser.getUser_id());
        b.setVerify_remark(verify_remark);
        // 资金记录表
        AccountLog log = new AccountLog(b.getUser_id(), Constant.ACCOUNT_BACK, getAuthUser().getUser_id(),
        		getTimeStr(), getRequestIp());
        
        // 操作记录表
        OperationLog operationLog = new OperationLog(b.getUser_id(), getAuthUser().getUser_id(), Constant.RECHARGE,
        		getTimeStr(), getRequestIp(), "");
        if (b.getStatus() != 1) {
        	log.setRemark("审核扣款记录，扣款" + b.getMoney() + "元不成功！备注:" + verify_remark);
        } else {
        	log.setRemark("审核扣款记录，扣款" + b.getMoney() + "元成功！备注:" + verify_remark);
        }
        accountService.verifyBack(b, log, operationLog);
        request.setAttribute("back", b);
        message("审核成功!");
        // v1.6.7.2 RDPROJECT-548 lx 2013-12-13 end

        // v1.6.6.2 RDPROJECT-397 lhm 2013-10-29 start
        // v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
        // TODO RDPROJECT-314 DONE OTHER accountService.verifyRecharge(r, log,operationLog);
        /*
         * if (r.getStatus() == 1) { auth_user = (User) session.get(Constant.AUTH_USER);
         * message.setSent_user(auth_user.getUser_id()); message.setReceive_user(r.getUser_id()); message.setStatus(0);
         * message.setType(Constant.SYSTEM); message.setName("扣款成功"); message.setContent("扣款成功,扣款金额为" + r.getMoney() +
         * ",流水号：" + r.getTrade_no()); message.setAddip(getRequestIp()); message.setAddtime(getTimeStr());
         * messageService.addMessage(message); }
         */
        // v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
        // v1.6.6.2 RDPROJECT-397 lhm 2013-10-29 end
        return ADMINMSG;
    }
    
    private void fillSearchParam(SearchParam param){
        String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
        String succtime1 = StringUtils.isNull(request.getParameter("succtime1"));
        String succtime2 = StringUtils.isNull(request.getParameter("succtime2"));
        String username = StringUtils.isNull(request.getParameter("username"));
        String status = StringUtils.isNull(request.getParameter("status"));
        String account_type = StringUtils.isNull(request.getParameter("account_type"));
        String paymentname = StringUtils.isNull(request.getParameter("paymentname"));
        String trade_no = StringUtils.isNull(request.getParameter("trade_no"));
        String recharge_kefu_username = StringUtils.isNull(request.getParameter("recharge_kefu_username"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String realname = StringUtils.isNull(request.getParameter("realname"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        param.setRecharge_kefu_username(recharge_kefu_username);
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        param.setSucctime1(succtime1);
        param.setSucctime2(succtime2);
        param.setUsername(username);
        param.setStatus(status);
        param.setAccount_type(account_type);
        param.setPaymentname(paymentname);
        param.setTrade_no(trade_no);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        param.setRealname(realname);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
    }
    
    /**
     * 资金使用记录
     * @return 跳转页面
     * @throws Exception 异常
     */
    public String log() throws Exception {
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        String account_Type = StringUtils.isNull(request.getParameter("account_type"));
        String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
        String username = StringUtils.isNull(request.getParameter("username"));
        String realname = StringUtils.isNull(request.getParameter("realname"));
        SearchParam param = new SearchParam();
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        param.setUsername(username);
        param.setRealname(realname);
        param.setAccount_type(account_Type);
        PageDataList plist = accountService.getAccountLogList(page, param);
        setPageAttribute(plist, param);

        // v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start
        request.setAttribute("listsize", accountService.getAccountlogCount(param));
        // v1.6.7.1 RDPROJECT-510 cx 2013-12-04 end
        if (type.isEmpty()) {
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "log_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            String[] names = new String[] { "username", "realname", "to_username", "typename", "money", "total",
                    "use_money", "no_use_money", "collection", "remark", "addtime", "addip" };
            String[] titles = new String[] { "用户名", "真实姓名", "交易对方", "交易类型", "操作金额", "账户总额", "可用金额", "冻结金额", "待收金额",
                    "备注", "时间", "IP" };
            List list = accountService.getAccountLogList(param);
            ExcelHelper.writeExcel(infile, list, AccountLogModel.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }

    }
    
    public String tenderLog() throws Exception{
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
        String username = StringUtils.isNull(request.getParameter("username"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String realname = StringUtils.isNull(request.getParameter("realname"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        SearchParam param = new SearchParam();
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        param.setUsername(username);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        param.setRealname(realname);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        PageDataList plist = accountService.getTenderLogList(page, param);
    
        // v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start
        List<DetailTender> tdlist=plist.getList();
        for (DetailTender td : tdlist) {
            if(("0").equals(td.getAward())){
                td.setFhMoney("没有");
            }else if(("1").equals(td.getAward())){  // 按投标金额比例
                td.setFhMoney(td.getPart_account());
            }else if(("2").equals(td.getAward())){  // 按固定金额分摊奖励
                //Double award = BigDecimalUtil.mul(Double.parseDouble(td.getFunds()), Double.parseDouble(td.getTender_account()));
                td.setFhMoney(td.getFunds());
            //  td.setFhMoney("￥"+String.valueOf(BigDecimalUtil.round(award, 2)));
            }
        }
        plist.setList(tdlist);
        
        request.setAttribute("listsize", accountService.getTenderlogCount(param));
        // v1.6.7.1 RDPROJECT-510 cx 2013-12-04 end
        
        double tenderInterestSum = accountService.getRechargesum(param, 5);
        request.setAttribute("sum", tenderInterestSum);
        // v1.6.6.1 RDPROJECT-265 zza 2013-10-08 start
        double tenderMoneySum = accountService.getRechargesum(param, 6);
        request.setAttribute("moneysum", tenderMoneySum);
        double tenderAccountSum = accountService.getRechargesum(param, 7);
        request.setAttribute("accountsum", tenderAccountSum);
        double tenderRepayAccountSum = accountService.getRechargesum(param, 8);
        request.setAttribute("repaysum", tenderRepayAccountSum);
        // v1.6.6.1 RDPROJECT-265 zza 2013-10-08 end
        setPageAttribute(plist, param);
        if (type.isEmpty()) {
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "tenderLog_" + System.currentTimeMillis() + ".xls";
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
            String infile = contextPath + "/data/export/" + downloadFile;
            String[] names = new String[] { "borrow_id", "borrow_name","time_limit", "username", "realname", "tender_money",
                "tender_account", "repayment_account", "apr", "interest","tbbz","part_account","skbz","funds","addtime" };
            String[] titles = new String[] { "借款标ID", "借款标名称","借款标期限", "投标人", "真实姓名", "操作金额", 
                "有效金额", "回款金额", "利率", "获得利息", "投标奖励标准","投标奖励","还款奖励标准","待收还款奖励","投标时间" };
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
            
            // v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start
            List<DetailTender> list = accountService.getTenderLogList(param);
            List exList=new ArrayList();
            for (DetailTender td : list) {
                if(("0").equals(td.getAward())){
                    td.setTbbz("-");
                    td.setAward("没有");
                }else if(("1").equals(td.getAward())){
                    td.setTbbz("按投标金额比例");
                    td.setPart_account((td.getPart_account()));
                }else if(("2").equals(td.getAward())){
                    //Double award = BigDecimalUtil.mul(Double.parseDouble(td.getFunds()), Double.parseDouble(td.getTender_account()));
                    //td.setAward("￥"+String.valueOf(BigDecimalUtil.round(award, 2)));
                    td.setSkbz("按固定金额分摊奖励");
                    td.setFunds(td.getFunds());
                }
                
                if(("1").equals(td.getIsday())){
                    td.setTime_limit(td.getTime_limit_day()+"天");
                }else{
                    td.setTime_limit(td.getTime_limit()+"月");
                }
                exList.add(td);
            }
            ExcelHelper.writeExcel(infile, exList, DetailTender.class, Arrays.asList(names), Arrays.asList(titles));
            // v1.6.7.1 RDPROJECT-510 cx 2013-12-04 end
            this.export(infile, downloadFile);
            return null;
        }
    }
    
    // v1.6.7.1 RDPROJECT-124 zza 2013-11-14 start
    /**
     * 自动投标排名
     * @return string
     * @throws Exception
     */
    public String autoTenderLog() throws Exception {
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        String username = StringUtils.isNull(request.getParameter("username"));
        SearchParam param = new SearchParam();
        param.setUsername(username);
        PageDataList plist = accountService.getAutoTenderLogList(page, param);
        setPageAttribute(plist, param);
        if (type.isEmpty()) {
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "autoTenderLog_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            String[] names = new String[] { "auto_order", "username", "auto_score", "use_money", "auto_money",
                "last_tender_time", "user_jifen"};
            String[] titles = new String[] { "排名", "用户名", "自动投标分值", "账户可用余额", "自动投标设置金额", 
                "最近一次投标时间", "用户积分"};
            List<AutoTenderOrder> list = accountService.getAutoTenderLogList(param);
            ExcelHelper.writeExcel(infile, list, AutoTenderOrder.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
    }
    // v1.6.7.1 RDPROJECT-124 zza 2013-11-14 end
    
    public String viptc() throws Exception{
        String type=StringUtils.isNull(request.getParameter("type"));
        int page=NumberUtils.getInt(request.getParameter("page"));
        String invite_username=StringUtils.isNull(request.getParameter("invite_username"));
        String username=StringUtils.isNull(request.getParameter("username"));
        SearchParam param=new SearchParam();
        param.setUsername(username);
        param.setInvite_username(invite_username);
        PageDataList plist=userService.getInviteUserList(page, param);
        setPageAttribute(plist, param);
        if(type.isEmpty()){
            return SUCCESS;
        }else{
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="viptc_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"data/export/"+downloadFile;
            String[] names=new String[]{"user_id","invite_username","username","realname",
                    "addtime","vip_status"};
            String[] titles=new String[]{"用户ID","推广者用户名","下线用户名","真实姓名",
                    "注册时间","是否VIP会员"};
            List list=userService.getInviteUserList(param);
            ExcelHelper.writeExcel(infile, list, DetailUser.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
    }
    
    public String vipstatistic(){
        String type=StringUtils.isNull(request.getParameter("type"));
        int page=NumberUtils.getInt(request.getParameter("page"));
        String kefu_username=StringUtils.isNull(request.getParameter("kefu_username"));
        String username=StringUtils.isNull(request.getParameter("username"));
        SearchParam param=new IdentifySearchParam();
        param.setUsername(username);
        param.setKefu_username(kefu_username);
        PageDataList plist=userService.getVipStatistic(page, param);
        setPageAttribute(plist, param);
        if(type.isEmpty()){
            return SUCCESS;
        }else{
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="vipstatistic_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"data/export/"+downloadFile;
            String[] names=new String[]{"kefu_username","user_id","username","realname",
                    "registertime","vip_verify_time","collection"};
            String[] titles=new String[]{"客服名","用户ID","用户名","真实姓名",
                    "注册时间","VIP审核时间","待收资金"};
            List list=userService.getVipStatistic(param);
            try {
                ExcelHelper.writeExcel(infile, list, VIPStatisticModel.class, Arrays.asList(names), Arrays.asList(titles));
                this.export(infile, downloadFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    //v1.7.7.2 RDPROJECT-470 wcw 2013-12-04 start
    public String manageUserCashBank() throws Exception {
        String type = paramString("actionType");
        long id=paramLong("id");
        long user_id=paramLong("user_id");
        AccountBank act=null;
        String bankaccount =paramString("bankaccount");
         act = accountService.getAccountByBankAccount(id);
        request.setAttribute("account", act);
        if (type.equals("show")) {
            User user=userService.getUserById(user_id);
            request.setAttribute("user", user);
            getAndSaveRef();
            return "show";
        } else if (type.equals("add")) {
            String bank = paramString("bank");
            String branch = paramString("branch");
            String account = paramString("account");
            if (StringUtils.isBlank(bank) || StringUtils.isBlank(branch) || StringUtils.isBlank(account)) {
                message("填写内容不能为空！", "");
                return ADMINMSG;
            }
            User authUser = getAuthUser();
            if (!StringUtils.isBlank(account) && !StringUtils.isBlank(branch)) {
                if(act==null){
                    act=new AccountBank();
                }
                act.setAccount(account);
                act.setBranch(branch);
                act.setUser_id(user_id);
                act.setBank(bank);
                act.setAddip(this.getRequestIp());
                act.setAddtime(DateUtils.getTimeStr(new Date()));
                act.setModify_username(authUser.getUsername());
                //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
                act.setProvince(paramInt("province"));
                act.setCity(paramInt("city"));
                act.setArea(paramInt("area"));
                //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
            }
            DetailUser detailUser = userService.getDetailUser(authUser.getUser_id());
            DetailUser newdetailUser = userService.getDetailUser(user_id);
            OperationLog operationLog = new OperationLog(act.getUser_id(), authUser.getUser_id(),
                    Constant.BANK_SUCCESS, this.getTimeStr(), this.getRequestIp(), "");
            if (id==0) {
                accountService.addBank(act);
                operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip() + "）用户名为"
                        + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername() + "的用户新增银行卡信息！");
            } else {
                accountService.modifyBankByAccount(act, bankaccount);
                operationLog.setOperationResult("" + detailUser.getTypename() + "（" + operationLog.getAddip() + "）用户名为"
                        + detailUser.getUsername() + "的操作员审核用户名为" + newdetailUser.getUsername() + "的用户修改银行卡信息！");
            }
            manageCashService.bankLog(operationLog);
            message("操作成功", getRef());
            return ADMINMSG;
        } else if (type.equals("export")) {
            String username = paramString("username");
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 start
            String realname = paramString("realname");
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 end
            SearchParam param = new SearchParam();
            param.setUsername(username);
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 start
            param.setRealname(realname);
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 end
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "bank_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            String[] names = new String[] { "user_id", "username", "realname", "bankname", "branch", "bankaccount",
                    "modify_username", "addtime" };
            String[] titles = new String[] { "用户ID", "用户名", "用户真实姓名", "开户银行", "开户银行名称", "提现银行卡卡号", "修改者", "修改时间" };
            List<AccountModel> list = accountService.getAccountList(param);
            ExcelHelper.writeExcel(infile, list, AccountModel.class, Arrays.asList(names), Arrays.asList(titles));
            export(infile, downloadFile);
            return null;
            
        } else {
            int page = paramInt("page");
            String username = paramString("username");
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 start
            String realname = paramString("realname");
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 end
            SearchParam param = new SearchParam();
            param.setUsername(username);
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 start
            param.setRealname(realname);
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 end
            PageDataList plist = accountService.getUserAccountModel(page, param);
            setPageAttribute(plist, param);
            return SUCCESS;
        }
    }
    //v1.7.7.2 RDPROJECT-470 wcw 2013-12-04 end
    public String showAllHuikuan(){
        String type = StringUtils.isNull(request.getParameter("type"));
        String status = StringUtils.isNull(request.getParameter("status"));
        String username = StringUtils.isNull(request.getParameter("username"));
        String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
        String huikuan = StringUtils.isNull(request.getParameter("huikuan"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String realname = StringUtils.isNull(request.getParameter("realname"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        SearchParam param = new SearchParam();
        param.setStatus(status);
        param.setUsername(username);
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        param.setRealname(realname);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        param.setHuikuan_use(huikuan);
        PageDataList plist = accountService.huikuanlist(page, param);
        List list = plist.getList();
        setPageAttribute(plist, param);
        request.setAttribute("list", list);
        if (type.isEmpty()) {
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "huikuan_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
            String[] names = new String[] { "id", "username", "realname", "huikuan_money", "huikuan_award", 
                "addtime", "remark", "status" };
            String[] titles = new String[] { "ID", "用户名称", "真实姓名", "回款金额", "回款奖励", "添加时间", "备注", "状态" };
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
            list = accountService.huikuanlist(param);

            try {
                ExcelHelper.writeExcel(infile, list, HuikuanModel.class, Arrays.asList(names), Arrays.asList(titles));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                this.export(infile, downloadFile);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }
    public String viewHuikuan(){
        String id=request.getParameter("id");
        HuikuanModel huikuan=null;
        if(!StringUtils.isNull(id).equals("")){
             huikuan=accountService.viewHuikuan(Integer.parseInt(id));
        }
         request.setAttribute("huikuan", huikuan);
        return "success";
    }
    public String verifyHuikuan(){
        setMsgUrl("/admin/cash/showAllHuikuan.html");
        String ids=request.getParameter("id");
        String user_ids=request.getParameter("user_id");
        String status=request.getParameter("status");
         // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 START
        String verify_remark=request.getParameter("verify_remark");
         // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 end
        // v1.6.7.2 RDPROJECT-547 lx 2013-12-6 start
        User user=(User)session.get(Constant.AUTH_USER);
        long verify_userid=user.getUser_id();
        // v1.6.7.2 RDPROJECT-547 lx 2013-12-6 end
        String payment="56";
        String type="3";
        String fee="0";
        String paymentname="";
        int id=0;
        int user_id=0;
        if(!StringUtils.isNull(user_ids).equals("")){
             user_id=Integer.parseInt(user_ids);
        }
        if(!StringUtils.isNull(ids).equals("")){
            id=Integer.parseInt(ids);
            if(id<=0){
                message("非法操作！");
                return ADMINMSG;
            }
            HuikuanModel h=accountService.viewHuikuan(id);
            String huikuan_fail = EnumHuiKuanStatus.HUIKUAN_FAIL.getValue();
            String huikuan_back = EnumHuiKuanStatus.HUIKUAN_BACK.getValue();
            String huikuan_success = EnumHuiKuanStatus.HUIKUAN_SUCCESS.getValue();
            if( h==null || (!status.equals(huikuan_success) && !status.equals(huikuan_fail) && !status.equals(huikuan_back))){
                message("非法操作！");
                return ADMINMSG;
            }
            if(h.getStatus().equals(huikuan_success) || h.getStatus().equals(huikuan_back) || h.getStatus().equals(huikuan_fail) ){
                message("该记录已经审核过，不允许重复操作！");
                return ADMINMSG;
            }
            
            //审核信息
            h.setStatus(status);
            AccountLog log=new AccountLog(user_id, Constant.HUIKUAN_AWARD, verify_userid, getTimeStr(), getRequestIp());
            AccountRecharge recharge=new AccountRecharge(StringUtils.generateTradeNO(h.getUser_id(),"K"), user_id, payment, type, fee, verify_userid,
                     getTimeStr(), getRequestIp(), paymentname);
            // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 START
            if("输入审核备注...".equals(verify_remark)){
                verify_remark="";
            }
            h.setVerify_remark(verify_remark);
            // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 end
            accountService.verifyHuikuan(h,log,recharge);
            request.setAttribute("huikuan", h);
            message("审核成功!","/admin/cash/showAllHuikuan.html");
                
        }
        
     
        return ADMINMSG;
    }
    
    /*public String excelRecharge() throws Exception{
        String type=paramString("type");
        if(type.equals("upload")){
            try {
                upload(excel, excelFileName, "/data/upload", excelFileName);
            } catch (Exception e) {
            }
            String xls=ServletActionContext.getServletContext().getRealPath( "/data/upload")+File.separator+excelFileName; 
            List[] data=ExcelHelper.read(xls);
            if(data!=null&&data.length>0){
                request.setAttribute("excelName", excelFileName);
                request.setAttribute("data", data);
                List[] retData=null;
                try {
                    retData=accountService.addBatchRecharge(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }   
                request.setAttribute("retData", retData);
            }
        }
        return SUCCESS;
    }*/
    
    /**
     * excel列表
     * @return String
     */
    public String showAllUpfiles() {
        int page = NumberUtils.getInt(request.getParameter("page"));
        String username = StringUtils.isNull(request.getParameter("username"));
        String status = StringUtils.isNull(request.getParameter("status"));
        String verify_user = StringUtils.isNull(request.getParameter("verify_user"));
        String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
        SearchParam param = new SearchParam();
        param.setUsername(username);
        param.setStatus(status);
        param.setVerify_user(verify_user);
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        // 查询批量充值的excel
        PageDataList plist = upfilesService.getAllUpfiles(page, param, "48");
        setPageAttribute(plist, param);
        setMsgUrl("/admin/cash/showAllUpfiles.html");
        return SUCCESS;
    }
    
    /**
     * 上传excel
     * @return String
     */
    public String excelRecharge() {
        User user = getAuthUser();
        String type = paramString("type");
        if ("upload".equals(type)) {
            if (this.excel == null) {
                message("你未上传任何文件！");
                return "adminmsg";
            }

            String sub = this.excelFileName.substring(this.excelFileName.length() - 3);
            //v1.6.6.1 RDPROJECT-301 liukun 2013-10-16 begin
            //if ("xls".equals(sub)) {
            // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 start
            if ("csv".equals(sub) || "xls".equals(sub)) {
            // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 end
            //v1.6.6.1 RDPROJECT-301 liukun 2013-10-16 end
                String newFileName = generateUploadFilename(this.excelFileName);
                String newUrl = "";
                try {
                    newUrl = upload(this.excel, newFileName, "/data/recharge", newFileName);
                } catch (Exception e) {
                    logger.error("上传文件出错：" + e.getMessage());
                }
                String xls = ServletActionContext.getServletContext().getRealPath("/data/recharge") + File.separator
                        + newFileName;
                List[] data = (List[]) null;
                try {
                    //v1.6.6.1 RDPROJECT-301 liukun 2013-10-16 begin
                    // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 start
                    if ("csv".equals(sub)) {
                        List dataList = CsvReader.read(xls);
                        data = (List[])dataList.toArray(new List[0]);
                    } else if ("xls".equals(sub)) {
                        data = ExcelHelper.read(xls);
                    }
                    // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 end
                    //v1.6.6.1 RDPROJECT-301 liukun 2013-10-16 end
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (data.length > 0) {
                    if (data.length > 10000) {
                        message("数据超过10000条，会导致上传速度过慢，请分批上传！", "/admin/cash/excelRecharge.html");
                        return "adminmsg";
                    }
                    List<String> list = new ArrayList<String>();
                    List<String> nameList = new ArrayList<String>();
                    List<String> moneyList = new ArrayList<String>();
                    List<String> remarkList = new ArrayList<String>();
                    // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 start
                    int totalUser = 0;
                    double totalRecharge = 0;
                    // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 end
                    for (int i = 0; i < data.length; i++) {
                        List row = data[i];
                        if ((row != null) && (row.size() >= 2)) {
                            String username = StringUtils.isNull(row.get(0));
                            String money = StringUtils.isNull(row.get(1));
                            String remark = null;
                            if (row.size() > 2) {
                                remark = StringUtils.isNull(row.get(2));
                            }
                            if ((!"用户名".equals(username)) && (!username.equals(""))) {
                                list.add(username);
                                
                                // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 start
                                totalUser++;
                                // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 end
                                
                            } else if ("".equals(username)) {
                                message("第" + (i + 1) + "行用户名为空，请确认后重新上传！", "/admin/cash/excelRecharge.html");
                                return ADMINMSG;
                            }
                            if ((!"充值金额".equals(money)) && (!money.equals(""))) {
                                // v1.6.6.1 RDPROJECT-10 zza 2013-09-24 start
                                // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 start
                                if (money.length() > 12 || Double.valueOf(money) <= 0) {
                                // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 end
                                    message("第" + (i + 1) + "行金额异常，请确认后重新上传！", "/admin/cash/excelRecharge.html");
                                    return ADMINMSG;
                                }
                                // v1.6.6.1 RDPROJECT-10 zza 2013-09-24 end
                                moneyList.add(money);
                                
                                // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 start
                                totalRecharge += Double.valueOf(money);
                                // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 end
                                
                            } else if ("".equals(money)) {
                                message("第" + (i + 1) + "行金额为空，请确认后重新上传！", "/admin/cash/excelRecharge.html");
                                return "adminmsg";
                            }
                            if (!"备注".equals(remark)) {
                                remarkList.add(remark);
                            }
                        }
                    }
                    Map hashMap = this.userService.getUserInName(list);
                    if (!hashMap.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            if ((!hashMap.containsKey(list.get(i))) && (list.get(i) != "")) {
                                nameList.add((String) list.get(i));
                            }
                        }
                        int size = nameList.size();
                        if (size > 0) {
                            File delFile = new File(xls);
                            delFile.delete();
                            message("用户名：" + nameList.toString() + " 不存在，请确认后重新上传！", "/admin/cash/excelRecharge.html");
                            return "adminmsg";
                        }
                    } else {
                        nameList = list;
                        message("用户名：" + nameList.toString() + " 不存在，请确认后重新上传！", "/admin/cash/excelRecharge.html");
                        return "adminmsg";
                    }
                    Upfiles upfiles = new Upfiles();
                    upfiles.setFile_name(newFileName);
                    upfiles.setFile_path(newUrl);
                    upfiles.setUser_id(user.getUser_id());
                    upfiles.setAddtime(getTimeStr());
                    upfiles.setStatus(0);
                    upfiles.setType("48");
                    try {
                        this.upfilesService.addUpfiles(upfiles);
                        // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 start
//                      message("excel上传成功。");
                        
                        // v1.6.7.1 RDPROJECT-513 liukun 2013-12-02 start
                        // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 start
                        DecimalFormat df = new DecimalFormat("0.00"); // 避免计算结果出现E
                        message("文件上传成功，本次共为"+totalUser+"人次充值，累计充值"+ df.format(totalRecharge)+"元。");
                        // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 end
                        // v1.6.7.1 RDPROJECT-513 liukun 2013-12-02 end
                        
                        // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 end
                        List<UpfilesExcelModel> excelModelList = new ArrayList<UpfilesExcelModel>();
                        for (int i = 0; i < list.size(); i++) {
                            UpfilesExcelModel model = new UpfilesExcelModel();
                            model.setUser_id((String) hashMap.get(list.get(i)));
                            model.setUsername((String) list.get(i));
                            String money = (String) moneyList.get(i);
                            model.setMoney(money.replace(",", ""));
                            model.setRemark((String) remarkList.get(i));
                            excelModelList.add(model);
                        }
                        ExcelHelper.writeExcel(xls, excelModelList, UpfilesExcelModel.class);
                        return "adminmsg";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    message("excel数据为空！", "/admin/cash/excelRecharge.html");
                    return "adminmsg";
                }
            } else {
                message("请上传后缀名为.xls的excel文件！", "/admin/cash/excelRecharge.html");
                return "adminmsg";
            }
        }
        return "success";

    }
    
    /**
     * 保存excel
     * @return String
     */
    public String saveExcel() {
        long id = NumberUtils.getLong(request.getParameter("id"));
        String[] usernames = (String[])request.getParameterValues("username");
        String[] moneys = (String[])request.getParameterValues("money");
        String[] remarks = (String[])request.getParameterValues("remark");
        String type = paramString("type");
        if (id <= 0) {
            message("非法操作！");
            return ADMINMSG;
        }
        Upfiles upfiles = upfilesService.getUpfilesById(id);
        if (upfiles == null) {
            message("非法操作！");
            return ADMINMSG;
        }
        if (!StringUtils.isBlank(type)) {
            String xls = ServletActionContext.getServletContext().
                    getRealPath("/data/recharge") + File.separator + upfiles.getFile_name();
            List<UpfilesExcelModel> list = new ArrayList<UpfilesExcelModel>();
            List<String> usernameList = new ArrayList<String>();
            for (int i = 0; i < usernames.length; i++) {
                if (!usernames[i].isEmpty() && usernames[i] != null) {
                    usernameList.add(usernames[i]);
                }
            }
            Map<String, String> hashMap = userService.getUserInName(usernameList);
            List<String> nameList = new ArrayList<String>();
            if (!hashMap.isEmpty()) {
                for (int i = 0; i < usernameList.size(); i++) {
                    // 把数据库里取出来的和excel里的做比较，不存在的添加到nameList里，页面上提示用户这些用户名不存在系统中
                    if (!hashMap.containsKey(usernameList.get(i)) && usernameList.get(i) != "") {
                        nameList.add(usernameList.get(i));
                    }
                }
            }
            int size = nameList.size();
            if (size > 0) {
                // 提醒用户上传的信息里有哪些不存在的用户名
                message("用户名：" + nameList.toString() + " 不存在！", "");
                return ADMINMSG;
            }
            for (int i = 0; i < usernames.length; i++) {
                if (!usernames[i].isEmpty() && usernames[i] != null) {
                    UpfilesExcelModel model = new UpfilesExcelModel();
                    String user_id = hashMap.get(usernames[i]);
                    model.setUser_id(user_id.replace(",", ""));
                    model.setUsername(usernames[i]);
                    String money = moneys[i];
                    if ("0".equals(money) || "".equals(money)) {
                        message("用户名：" + usernames[i] + " 的充值金额有误，请修改后重新保存！", "");
                        return ADMINMSG;
                    // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 start
                    } else if (money.length() > 12) {
                    // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 end
                        message("用户名：" + usernames[i] + " 的充值金额太大，最多只能上传12位，请修改后重新保存！", "");
                        return ADMINMSG;
                    }
                    model.setMoney(money.replace(",", ""));
                    model.setRemark(remarks[i]);
                    list.add(model);
                }
            }
            try {
                ExcelHelper.writeExcel(xls, list, UpfilesExcelModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            message("保存成功!", "/admin/cash/showAllUpfiles.html");
            return ADMINMSG;
        }
        List<AccountRecharge> list = readExcel(upfiles);
        // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 start
        int totalUser = 0;
        double totalRecharge = 0;
        if (list != null) {
        	 totalUser = list.size();
             for (AccountRecharge accountRecharge : list) {
                 totalRecharge += accountRecharge.getMoney();
             }
        }
        // v1.6.7.1 RDPROJECT-513 liukun 2013-12-02 start
        // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 start
        DecimalFormat df = new DecimalFormat("0.00"); // 避免计算结果出现E
        request.setAttribute("upfileTotal", "本次共为"+totalUser+"人次充值，累计充值"+ df.format(totalRecharge) + "元。");
        // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 end
        // v1.6.7.1 RDPROJECT-513 liukun 2013-12-02 end
        
        // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 end
        request.setAttribute("upfiles", upfiles);
        request.setAttribute("list", list);
        return SUCCESS;
    }
    
    /**
     * 审核excel
     * @return String
     */
    public String verifyExcel() {
        String type = paramString("type");
        long id = NumberUtils.getLong(request.getParameter("id"));
        int status = NumberUtils.getInt(request.getParameter("status"));
        String remark = StringUtils.isNull(request.getParameter("file_remark"));
        String[] usernames = (String[])request.getParameterValues("username");
        String[] moneys = (String[])request.getParameterValues("money");
        if (id <= 0) {
            message("非法操作！");
            return ADMINMSG;
        }
        Upfiles upfiles = upfilesService.getUpfilesById(id);
        if (!StringUtils.isBlank(type)) {
            if (upfiles == null || (status != 1 && status != 2)) {
                message("非法操作！");
                return ADMINMSG;
            }
            if (upfiles.getStatus() == 1) {
                message("该记录已经审核通过，不允许重复操作！");
                return ADMINMSG;
            }
            // v1.6.6.1 RDPROJECT-10 zza 2013-09-24 start
            for (int i = 0; i < usernames.length; i++) {
                if (!moneys[i].isEmpty() && moneys[i] != null) {
                    String money = moneys[i];
                    // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 start
                    if (money.length() > 12) {
                    // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 end
                        message("用户名：" + usernames[i] + " 的充值金额有误，请修改后重新保存！", "");
                        return ADMINMSG;
                    }
                }
            }
            // v1.6.6.1 RDPROJECT-10 zza 2013-09-24 end
            try {
                String xls = ServletActionContext.getServletContext().
                        getRealPath("/data/recharge") + File.separator + upfiles.getFile_name();
                List<AccountRecharge> list = readExcel(upfiles);
                User verifyUser = getAuthUser();
                accountService.addExcelRecharge(xls, verifyUser, list, status);
                upfiles.setVerify_user(verifyUser.getUsername());
                upfiles.setVerify_user_id(verifyUser.getUser_id());
                upfiles.setVerify_time(getTimeStr());
                upfiles.setStatus(status);
                upfiles.setFile_remark(remark);
                upfilesService.modifyUpfiles(upfiles);
                if (status != 1) {
                    message("审核失败!", "/admin/cash/showAllUpfiles.html");
                } else {
                    message("审核成功!", "/admin/cash/showAllUpfiles.html");
                }
                request.setAttribute("upfiles", upfiles);
                request.setAttribute("list", list);
                return ADMINMSG;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }
    
    /**
     * 检测修改的用户是否存在
     * @return String
     * @throws Exception
     */
    public String checkUsername() {
        String username = request.getParameter("username");
        boolean result = userService.checkUsername(username);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 直接输入响应的内容
        if (result) {
            out.println("1");
        } else {
            out.println("");
        }
        out.flush();
        out.close();
        return null;
    }
    
    /**
     * 查看
     * @return String
     */
    public String viewUpfiles() {
        long id = NumberUtils.getLong(request.getParameter("id"));
        Upfiles upfiles = upfilesService.getUpfilesById(id);
        if (upfiles == null) {
            message("非法操作！");
            return ADMINMSG;
        }
        List<AccountRecharge> list = new ArrayList<AccountRecharge>();
        try {
            list = readExcel(upfiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 start
        int totalUser = 0;
        double totalRecharge = 0;
        totalUser = list.size();
        for (AccountRecharge accountRecharge : list) {
            totalRecharge += accountRecharge.getMoney();
        }
        // v1.6.7.1 RDPROJECT-513 liukun 2013-12-02 start
        // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 start
        DecimalFormat df = new DecimalFormat("0.00"); // 避免计算结果出现E
        request.setAttribute("upfileTotal", "本次共为"+totalUser+"人次充值，累计充值"+ df.format(totalRecharge) + "元。");
        // v1.6.7.2 RDPROJECT-519 zza 2013-12-09 end
        // v1.6.7.1 RDPROJECT-513 liukun 2013-12-02 end
        
        // v1.6.7.1 RDPROJECT-435 liukun 2013-11-11 end
        
        request.setAttribute("upfiles", upfiles);
        request.setAttribute("list", list);
        return SUCCESS;
    }

    /**
     * 读取excel信息的公共方法
     * @param upfiles upfiles
     * @return list
     */
    private List<AccountRecharge> readExcel(Upfiles upfiles) {
        String xls = ServletActionContext.getServletContext().
                getRealPath("/data/recharge") + File.separator + upfiles.getFile_name(); 
        List[] data = null;
        try {
            data = ExcelHelper.read(xls);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        List<AccountRecharge> list = null;
        if (data != null && data.length > 0) {
            try {
                list = upfilesService.addBatchRecharge(data);
            } catch (Exception e) {
                e.printStackTrace();
            }   
        }
        return list;
    }
    
    /**
     * 删除
     * @return String
     */
    public String delUpfiles() {
        long id = NumberUtils.getLong(request.getParameter("id"));
        Upfiles upfiles = upfilesService.getUpfilesById(id);
        if (upfiles != null) {
            String xls = ServletActionContext.getServletContext().
                    getRealPath("/data/recharge") + File.separator + upfiles.getFile_name();
            File delFile = new File(xls); 
            delFile.delete();
            upfilesService.delUpfiles(id);
            message("删除excel成功！", "/admin/cash/showAllUpfiles.html");
        }
        return ADMINMSG;
    }
    
    public String addbank(){
        String type=StringUtils.isNull(request.getParameter("add"));
        if(type.equals("add")){
            String username=StringUtils.isNull(request.getParameter("username"));
            String branch=StringUtils.isNull(request.getParameter("branch"));
            String bank=StringUtils.isNull(request.getParameter("bank"));
            String account =StringUtils.isNull(request.getParameter("account"));
            User u = userService.getUserByName(username);
            try {
                AccountBank bk=new AccountBank();
                bk.setUser_id(u.getUser_id());
                bk.setAccount(account);
                bk.setBank(bank);
                bk.setBranch(branch);
                bk.setAddip("1.1.1.1");
                bk.setAddtime(DateUtils.getTimeStr(new Date()));
            
                bk=accountService.addBank(bk);
                //System.out.println(bk.getId());
                if(bk.getId()>0){
                    message("添加成功","/admin/cash/cashbank.html?search=true&username="+username);
                    return ADMINMSG;
                }else{
                    message("添加失败，请检查输入","/admin/cash/addbank.html");
                    return ADMINMSG;
                }
            } catch (Exception e) {
                message("添加失败，请检查输入","/admin/cash/addbank.html");
                return ADMINMSG;
            }
            
        }
        return SUCCESS;
    }
    //红包
    public String hongBaoList() throws Exception{
        String type=StringUtils.isNull(request.getParameter("type"));
        int page=NumberUtils.getInt(request.getParameter("page"));
        String account_Type=StringUtils.isNull(request.getParameter("account_type"));
        String username=StringUtils.isNull(request.getParameter("username"));
        SearchParam param=new SearchParam();
        param.setAccount_type(account_Type);
        param.setUsername(username);
        PageDataList plist=accountService.getHongBaoList(page, param);
        setPageAttribute(plist, param);
        setMsgUrl("/admin/cash/hongBaoList.html");
        if(type.isEmpty()){
            return SUCCESS;
            }else{
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="hongbao_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"data/export/"+downloadFile;
            String[] names=new String[]{"id","username",
                    "hongbao_money","typename","hongbao"
                    ,"addtime"};
            String[] titles=new String[]{"ID","用户名称",
                    "操作金额","类型","该用户现有红包","添加时间"};
            List list=accountService.getHongBaoList(param);
            
            ExcelHelper.writeExcel(infile, list, HongBaoModel.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
    }
    /**
     * 资金对账
     * @return string
     * @throws Exception Exception
     */
    public String accountReconciliationList() throws Exception{
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        String account_Type = StringUtils.isNull(request.getParameter("account_type"));
        String username = StringUtils.isNull(request.getParameter("username"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String realname = StringUtils.isNull(request.getParameter("realname"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        SearchParam param = new SearchParam();
        param.setAccount_type(account_Type);
        param.setUsername(username);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        param.setRealname(realname);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        PageDataList plist = accountService.getAccountReconciliationList(page, param);
        setPageAttribute(plist, param);
        setMsgUrl("/admin/cash/accountReconciliationList.html");
        if (type.isEmpty()) {
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "duizhang_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            // v1.6.5.3 RDPROJECT-175 zza 2013-09-18 start
            String[] names = new String[] { "username", "realname", "total", "use_money", "no_use_money", "collection",
                    "allcollection", "recharge_money", "log_recharge_money", "up_recharge_money",
                    "down_recharge_money", "houtai_recharge_money", "cash_money", "invest_award",
                    "invest_yeswait_interest", "wait_interest", "borrow_award", "borrow_fee", "repayment_interest",
                    "flow_repayment_interest", "repayment_principal", "flow_repayment_principal",
                    "yes_repayment_interest", "flow_yes_repayment_interest", "system_fee", "invite_money", "vip_money" };
            String[] titles = new String[] { "用户名", "真实姓名", "资金总额", "可用资金", "冻结资金", "待收资金（1）", "待收资金（2）", "充值资金（1）", "充值资金（2）",
                    "其中：线上", "其中：线下（1）", "其中：线下（2）", "成功提现金额", "投标奖励金额", "投标已收利息", "投标待收利息", "借款标奖励", "借款管理费", "待还利息",
                    "流转待还利息", "待还本金", "流标待还本金", "借款已还利息", "流转标已还利息", "系统扣费", "推广奖励", "vip扣费" };
            List<AccountReconciliationModel> list = accountService.getAccountReconciliationList(param);
            // v1.6.5.3 RDPROJECT-175 zza 2013-09-18 start
            ExcelHelper.writeExcel(infile, list, AccountReconciliationModel.class, Arrays.asList(names),
                    Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
    }
    
    
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//    //垫付资金
//    public String advanced_account(){
//        String actionType=StringUtils.isNull(request.getParameter("actionType"));
//        List list=manageCashService.getAdvancedList();
//        request.setAttribute("list", list);
//        if(StringUtils.isBlank(actionType)){
//            return "success";
//        }
//        Advanced advanced=new Advanced();
//        String advance_reserve=StringUtils.isNull(request.getParameter("advance_reserve"));
//        String no_advanced_account=StringUtils.isNull(request.getParameter("no_advanced_account"));
//        
//        
//        if(list.size()>0){
//              advanced=(Advanced) list.get(0);
//              advanced.setAdvance_reserve(NumberUtils.getDouble(advance_reserve));
//              advanced.setNo_advanced_account(NumberUtils.getDouble(no_advanced_account));
//              manageCashService.getAdvanced_update(advanced);   
//        }else{
//            advanced.setAdvance_reserve(NumberUtils.getDouble(advance_reserve));
//            advanced.setNo_advanced_account(NumberUtils.getDouble(no_advanced_account));
//            manageCashService.getAdvanced_insert(advanced);
//        }
//        
//        return "success";
//    }
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
    
    //操作记录
    public String operationLog(){
        String actionType=request.getParameter("type");
        int page=NumberUtils.getInt(request.getParameter("page"));
        String dotime1=StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2=StringUtils.isNull(request.getParameter("dotime2"));
        //  v1.6.6.1 RDPROJECT-205 zza 2013-09-26 start
        String username=StringUtils.isNull(request.getParameter("username"));
        //  v1.6.6.1 RDPROJECT-205 zza 2013-09-26 end
        String user_type=StringUtils.isNull(request.getParameter("user_type"));
        SearchParam param=new SearchParam();
        User user=(User) session.get(Constant.AUTH_USER);
        if(user.getType_id()!=1){
        	param.setUser_typeid(String.valueOf(user.getType_id()));
        	// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 start
        	param.setUser_id(user.getUser_id());
        	// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 end
        }
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        param.setType(user_type);
        //  v1.6.6.1 RDPROJECT-205 zza 2013-09-26 start
        param.setUsername(username);
        //  v1.6.6.1 RDPROJECT-205 zza 2013-09-26 end
        PageDataList list = accountService.operationLog(page, param);
        setPageAttribute(list, param);
        if(StringUtils.isBlank(actionType)){
           return "success";
        }else{
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="operationLog_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"data/export/"+downloadFile;
            String[] names=new String[]{"id","verify_username",
                    "typename","username","operationResult"
                    ,"addtime","addip"};
            String[] titles=new String[]{"ID","操作人员",
                    "操作类型","用户名","操作结果","操作时间","操作ip"};
            List<OperationLogModel> operationList = accountService.operationLog(param);
            
            try {
                ExcelHelper.writeExcel(infile, operationList, OperationLogModel.class, Arrays.asList(names), Arrays.asList(titles));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                this.export(infile, downloadFile);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }
    
    /**
     * 查看所有信用卡
     * @return
     * @throws Exception
     */
    public String showAllCreditCard() throws Exception {
        int page=NumberUtils.getInt(request.getParameter("page"));
        SearchParam searchParam = new SearchParam();
        PageDataList pageDataList = creditCardService.getList(page, searchParam);
        request.setAttribute("list", pageDataList);
        setPageAttribute(pageDataList, searchParam);
        setMsgUrl("/admin/cash/showAllCreditCard.html");
        return "success";
    }
    
    private String truncatUrl(String old, String truncat) {
        String url = "";
        url = old.replace(truncat, "");
        url = url.replace(sep, "/");
        return url;
    }
    
    private void moveFile(CreditCard creditCard) {
        String dataPath = ServletActionContext.getServletContext().getRealPath(
                "/data");
        String contextPath = ServletActionContext.getServletContext()
                .getRealPath("/");
        Date d1 = new Date();
        String upfiesDir = dataPath + sep + "upfiles" + sep + "images" + sep;
        String destfilename1 = upfiesDir + DateUtils.dateStr2(d1) + sep
                + creditCard.getCard_id() + "_attestation" + "_" + d1.getTime()
                + ".jpg";
        filePath = destfilename1;
        filePath = this.truncatUrl(filePath, contextPath);
        logger.info(destfilename1);
        File imageFile1 = null;
        try {
            imageFile1 = new File(destfilename1);
            FileUtils.copyFile(litpic, imageFile1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }
    
    /**
     * 新增信用卡
     * @return
     * @throws Exception
     */
    public String addCreditCard() throws Exception {
        String actionType=StringUtils.isNull(request.getParameter("add"));
        if(actionType.equals("add")){
            fillCreditCard(model);
            if (this.litpic == null) {
                message("你上传的图片为空", "/admin/cash/addCreditCard.html");
                return MSG;
            }
            moveFile(model);
            model.setLitpic(filePath);
            CreditCard creditCard = creditCardService.addCreditCard(model);
            request.setAttribute("creditCard", creditCard);
            message("新增信用卡成功！","/admin/cash/addCreditCard.html");
            return ADMINMSG;
        }
        return SUCCESS;
    }
    
    private CreditCard fillCreditCard(CreditCard creditCard){
        creditCard.setName(request.getParameter("name"));
        creditCard.setIssuing_bank(request.getParameter("issuing_bank"));
        creditCard.setIssuing_nstitution(request.getParameter("issuing_nstitution"));
        creditCard.setIssuing_status(request.getParameter("issuing_status"));
        creditCard.setCheck_style(request.getParameter("check_style"));
        creditCard.setInterest(request.getParameter("interest"));
        creditCard.setTel(request.getParameter("tel"));
        creditCard.setCurrency(request.getParameter("currency"));
        creditCard.setGrade(request.getParameter("grade"));
        creditCard.setBorrowing_limit(request.getParameter("borrowing_limit"));
        creditCard.setInterest_free(request.getParameter("interest_free"));
        creditCard.setIntegral_policy(request.getParameter("integral_policy"));
        creditCard.setIntegral_indate(request.getParameter("integral_indate"));
        creditCard.setCredit_rules(request.getParameter("credit_rules"));
        creditCard.setScoring_rules(request.getParameter("scoring_rules"));
        creditCard.setInstallment(request.getParameter("installment"));
        creditCard.setAmount(request.getParameter("amount"));
        creditCard.setFee_payment(request.getParameter("fee_payment"));
        creditCard.setApplicable_fee(request.getParameter("applicable_fee"));
        creditCard.setPrepayment(request.getParameter("prepayment"));
        creditCard.setOpen_card(request.getParameter("open_card"));
        creditCard.setRepea_card(request.getParameter("repea_card"));
        creditCard.setApp_condition(request.getParameter("app_condition"));
        creditCard.setApp_way(request.getParameter("app_way"));
        creditCard.setSubmit_info(request.getParameter("submit_info"));
        creditCard.setSupplement_num(request.getParameter("supplement_num"));
        creditCard.setSupplement_app(request.getParameter("supplement_app"));
        creditCard.setReport_loss(request.getParameter("report_loss"));
        creditCard.setLoss_protection(request.getParameter("loss_protection"));
        creditCard.setLoss_tel(request.getParameter("loss_tel"));
        creditCard.setLowest_refund(request.getParameter("lowest_refund"));
        creditCard.setAllopatry_back_fee(request.getParameter("allopatry_back_fee"));
        creditCard.setRmb_repayment(request.getParameter("rmb_repayment"));
        creditCard.setForeign_repayment(request.getParameter("foreign_repayment"));
        creditCard.setSpecial_repayment(request.getParameter("special_repayment"));
        creditCard.setCard_features(request.getParameter("card_features"));
        creditCard.setAdd_service(request.getParameter("add_service"));
        creditCard.setJoint_discount(request.getParameter("joint_discount"));
        creditCard.setMain_card_fee(request.getParameter("main_card_fee"));
        creditCard.setYear_cut_rules(request.getParameter("year_cut_rules"));
        creditCard.setSupplement_card_fee(request.getParameter("supplement_card_fee"));
        creditCard.setFee_date(request.getParameter("fee_date"));
        creditCard.setLocal_fee(request.getParameter("local_fee"));
        creditCard.setLocal_interbank_fee(request.getParameter("local_interbank_fee"));
        creditCard.setOffsite_fee(request.getParameter("offsite_fee"));
        creditCard.setOffsite_interbank_fee(request.getParameter("offsite_interbank_fee"));
        creditCard.setOverseas_pay_fee(request.getParameter("overseas_pay_fee"));
        creditCard.setOverseas_unpay_fee(request.getParameter("overseas_unpay_fee"));
        creditCard.setOverseas_meet_fee(request.getParameter("overseas_meet_fee"));
        creditCard.setEnchashment_limit(request.getParameter("enchashment_limit"));
        creditCard.setLocalback_fee(request.getParameter("localback_fee"));
        creditCard.setLocal_interbank_back_fee(request.getParameter("local_interbank_back_fee"));
        creditCard.setOffsite_overflow_back_fee(request.getParameter("offsite_overflow_back_fee"));
        creditCard.setOffsite_interbank_back_fee(request.getParameter("offsite_interbank_back_fee"));
        creditCard.setOverseas_pay_back_fee(request.getParameter("overseas_pay_back_fee"));
        creditCard.setOverseas_unpay_back_fee(request.getParameter("overseas_unpay_back_fee"));
        creditCard.setOverflow_back_rules(request.getParameter("overflow_back_rules"));
        creditCard.setMessage_money(request.getParameter("message_money"));
        creditCard.setOverseas_fee(request.getParameter("overseas_fee"));
        creditCard.setChange_card(request.getParameter("change_card"));
        creditCard.setAhead_change_card(request.getParameter("ahead_change_card"));
        creditCard.setExpress_fee(request.getParameter("express_fee"));
        creditCard.setStatement_fee(request.getParameter("statement_fee"));
        creditCard.setStatement_free_clause(request.getParameter("statement_free_clause"));
        creditCard.setLoss_fee(request.getParameter("loss_fee"));
        creditCard.setReset_password_fee(request.getParameter("reset_password_fee"));
        creditCard.setSelfdom_card_fee(request.getParameter("selfdom_card_fee"));
        creditCard.setForeign_convert_fee(request.getParameter("foreign_convert_fee"));
        creditCard.setSlip_fee(request.getParameter("slip_fee"));
        creditCard.setSlip_fee_copy(request.getParameter("slip_fee_copy"));
        creditCard.setSlip_fee_foreign(request.getParameter("slip_fee_foreign"));
        creditCard.setSlip_fee_copy_foreign(request.getParameter("slip_fee_copy_foreign"));
        creditCard.setOverdue_fine(request.getParameter("overdue_fine"));
        creditCard.setTransfinite_fee(request.getParameter("transfinite_fee"));
        creditCard.setType_value(Integer.valueOf(request.getParameter("type_value")));
        creditCard.setLitpic(StringUtils.isNull(request.getParameter("litpic")));
        return creditCard;
    }
    
    /**
     * 修改信用卡信息
     * @return
     * @throws Exception
     */
    public String modifyCreditCard() throws Exception {
        String actionType = StringUtils.isNull(request.getParameter("actionType"));
        int cardId=Integer.valueOf(request.getParameter("cardId"));
        if(!StringUtils.isBlank(actionType)){
            fillCreditCard(model);
            model.setCard_id(cardId);
            if (this.litpic == null) {
                CreditCard card =creditCardService.getCardById(cardId);
                model.setLitpic(card.getLitpic());
            } else {
                moveFile(model);
                model.setLitpic(filePath);
            }
            creditCardService.updateCreditCard(model);
            message("修改文章成功！","");
            return ADMINMSG;
        }
        CreditCard creditCard=creditCardService.getCardById(cardId);
        request.setAttribute("creditCard", creditCard);
        return SUCCESS;
    }
    
    /**
     * 根据Id删除信用卡
     * @return
     * @throws Exception
     */
    public String delCreditCard() throws Exception {
        int cardId=Integer.valueOf(request.getParameter("cardId"));
        creditCardService.delCreditCard(cardId);
        message("删除文章成功！","/admin/cash/showAllCreditCard.html");
        return ADMINMSG;
    }
    
    public String cashStatistic() throws Exception{
        //成功借出总额
        request.setAttribute("borrow_success", accountService.getAccountLogSum("borrow_success"));
        //逾期总额
        request.setAttribute("allLateSum", accountService.getAllLateSumWithYesRepaid()+accountService.getAllLateSumWithNoRepaid());
        //逾期已还款总额
        request.setAttribute("yesLateSum", accountService.getAllLateSumWithYesRepaid());
        //逾期未还款总额
        request.setAttribute("noLateSum", accountService.getAllLateSumWithNoRepaid());
        //已还款总额
        request.setAttribute("yesRepayment", accountService.getRepaymentAccount(1));
        //未还款总额
        request.setAttribute("noRepayment", accountService.getRepaymentAccount(0));
        String type=StringUtils.isNull(request.getParameter("type"));
        String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
        SearchParam param=new SearchParam();
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        request.setAttribute("param", param.toMap());
        List logSumList = accountService.getAccountLogSumWithMonth(param);
        request.setAttribute("logSumList", logSumList);
        if(type.isEmpty()){
            return SUCCESS;
        }else{
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="accountLogSum_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"data/export/"+downloadFile;
            String[] names=new String[]{"typename","sum"};
            String[] titles=new String[]{"类型名称","金额"};
            logSumList = accountService.getAccountLogSumWithMonth(param);
            ExcelHelper.writeExcel(infile, logSumList, AccountLogSumModel.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
        
    }
    /**
     * 投资排行版导出报表
     * @return
     * @throws Exception
     */
    public String exportTenderRank() throws Exception{
        String contextPath = ServletActionContext.getServletContext().getRealPath("/");
        String downloadFile="allRankList_"+System.currentTimeMillis()+".xls";
        String infile=contextPath+"data/export/"+downloadFile;
        String[] names=new String[]{"username","tenderMoney"};
        String[] titles=new String[]{"投资人","投资总额"};
        List AllRankList = borrowService.getAllRankList();
        ExcelHelper.writeExcel(infile, AllRankList, RankModel.class, Arrays.asList(names), Arrays.asList(titles));
        this.export(infile, downloadFile);
        return null;
    }
    //V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 start
    /**
     * 提现审核中的待审列表查询
     * 
     
     * @version 1.0 
     * @since 2013-9-11
     */
    public String showCashStep() throws Exception{
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        String username=StringUtils.isNull(request.getParameter("username"));
        String dotime1=StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2=StringUtils.isNull(request.getParameter("dotime2"));
        String succtime1=StringUtils.isNull(request.getParameter("succtime1"));
        String succtime2=StringUtils.isNull(request.getParameter("succtime2"));
        String cashTotal_min = StringUtils.isNull(request.getParameter("cashTotal_min"));
        String cashTotal_max = StringUtils.isNull(request.getParameter("cashTotal_max"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String realname = StringUtils.isNull(request.getParameter("realname"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        
        String verifyStep = StringUtils.isNull(request.getParameter("verify_step"));
        Map verifyConf = getVerifyConf(verifyStep);
        int preStatus = Integer.parseInt(verifyConf.get("preStatus").toString());
        
        SearchParam param=new SearchParam();
        param.setUsername(username);
        param.setStatus(String.valueOf(preStatus));
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        param.setCashTotal_min(cashTotal_min);
        param.setCashTotal_max(cashTotal_max);
        param.setSucctime1(succtime1);
        param.setSucctime2(succtime2);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        param.setRealname(realname);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        //V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 start 
        param.setBank_id(paramString("bank_id"));
        param.setSearch_type(paramString("select_type"));
        List<Linkage> linkageList=linkageService.linkageList(25);
        request.setAttribute("bankList", linkageList);
        //V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 end 
        if(type.isEmpty()){
            PageDataList plist=manageCashService.getAllCash(page, param);
            double creditSum=accountService.getRechargesum(param, 4);
            double totalSum=accountService.getRechargesum(param, 2);
            double feeSum=accountService.getRechargesum(param,3);
            request.setAttribute("totalSum", totalSum);
            request.setAttribute("creditSum", creditSum);
            request.setAttribute("feeSum", feeSum);
            
            //传入verifyConf, viewcash需要用到
            request.setAttribute("verifyConf", verifyConf);
            request.setAttribute("verifyStep", verifyStep);
            request.setAttribute("preStatus", preStatus);
            
            setPageAttribute(plist, param);
            setMsgUrl("/admin/cash/showCash.html");
            return SUCCESS;
        }else{
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile="cash_"+System.currentTimeMillis()+".xls";
            String infile=contextPath+"/data/export/"+downloadFile;
            String[] names=new String[]{"id","username","realname",
                    "account","bankname","branch","total","credited"
                    ,"fee","addtime","verify_time","status","verify_username"};
            String[] titles=new String[]{"ID","用户名称","真实姓名",
                    "提现账号","提现银行","支行","提现总额","到账金额"
                    ,"手续费","提现时间","操作时间","状态","审核人"};
            List list=manageCashService.getAllCash(param);
            ExcelHelper.writeExcel(infile, list, AccountCash.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
    }   
    //V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 end
    //V1.6.6.1 RDPROJECT-171 wcw 2013-10-08 start
    public String kefuUserInvest(){
      //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
        List kfList = userService.getKfList(2);
      //v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
        request.setAttribute("kfList", kfList);
        String type = StringUtils.isNull(request.getParameter("type"));
        int page = NumberUtils.getInt(request.getParameter("page"));
        String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
        String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
        String kefu_username = StringUtils.isNull(request.getParameter("kefu_name"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String realname = StringUtils.isNull(request.getParameter("realname"));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        SearchParam param = new SearchParam();
        param.setDotime1(dotime1);
        param.setDotime2(dotime2);
        param.setKefu_username(kefu_username);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        param.setRealname(realname);
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        PageDataList pageDataList = accountService.kefuUserInvest(page, param);
        this.setPageAttribute(pageDataList, param);
        // 统计用户总的投资额
        double kefuUserInvestSum = accountService.getKefuUserInvestSum(param);
        request.setAttribute("kefuUserInvestSum", kefuUserInvestSum);
        if (type.isEmpty()) {
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            logger.debug("path===" + contextPath);
            String downloadFile = "user_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            logger.debug("infile===" + infile);

            String[] names = new String[] { "user_id", "kefu_username", "username", "realname", "regiter_time",
                    "addtime", "account" };
            String[] titles = new String[] { "ID", "客服名称", "用户名", "真实姓名", "注册时间 ", "投标时间", "投资金额" };
            List list = accountService.kefuUserInvest(param);
            try {
                ExcelHelper.writeExcel(infile, list, KefuAndUserInvest.class, Arrays.asList(names),
                        Arrays.asList(titles));
                this.export(infile, downloadFile);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }
    }
    //V1.6.6.1 RDPROJECT-171 wcw 2013-10-08 end
    
    //v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 begin
    public String cashForbid() throws Exception{
        String actionType=StringUtils.isNull(request.getParameter("actionType"));
        long user_id = NumberUtils.getLong(request.getParameter("user_id"));
        User user = userService.getUserById(user_id);
        if(!StringUtils.isBlank(actionType)){
            String cashForbid = StringUtils.isNull(request.getParameter("cashForbid"));
            String statusDesc = StringUtils.isNull(request.getParameter("statusDesc"));
            
            userService.updateUserCashForbid(user_id, Integer.parseInt(cashForbid));
            userService.updateUserStatusDesc(user_id, statusDesc);
            
            /*smstype=new Smstype();
            String name = StringUtils.isNull(request.getParameter("name"));
            byte type = Byte.parseByte(StringUtils.isNull(request.getParameter("type")));
            byte send = Byte.parseByte(StringUtils.isNull(request.getParameter("send")));
            String remark = StringUtils.isNull(request.getParameter("remark"));
            
            smstype.setNid(nid);
            smstype.setName(name);
            smstype.setType(type);
            smstype.setSend(send);
            smstype.setTemplet(templet);
            smstype.setRemark(remark);
            smstype.setUpdatetime(Long.parseLong(getTimeStr()));
            smstype.setUpdateip(getRequestIp());
            smstypeService.update(smstype);
            
            //V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 start
            List smstypeList = smstypeService.getList();
            Map smstypeMap=new HashMap();
            for(int i=0;i<smstypeList.size();i++){
                Smstype smstype=(Smstype)smstypeList.get(i);
                smstypeMap.put(smstype.getNid(), smstype);
            }
            Global.SMSTYPECONFIG=smstypeMap;*/
            //V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 end

            //message("更新短信类型成功！", "/admin/smstype/smstypeList.html");
            String backUrl = "/admin/cash/showAllAccount.html";
            message("操作成功！",backUrl);
            return ADMINMSG;
        } else {
            int cashForbid = userService.isCashForbid(user_id);
            String statusDesc = userService.getUserStatusDesc(user_id);
            request.setAttribute("cashForbid", cashForbid);
            request.setAttribute("user", user);
            request.setAttribute("statusDesc", statusDesc);
        }

        return SUCCESS;
    }
    //v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 begin

}
