package com.p2psys.web.action.admin;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONArray;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.credit.domain.CreditLog;
import com.p2psys.credit.domain.GoodsCategory;
import com.p2psys.credit.domain.GoodsPic;
import com.p2psys.credit.model.CreditGoodsModel;
import com.p2psys.credit.model.CreditModel;
import com.p2psys.credit.model.GoodsCategoryModel;
import com.p2psys.credit.service.CreditConvertService;
import com.p2psys.domain.CreditConvert;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.Upfiles;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCredit;
import com.p2psys.model.CreditConvertModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UpfilesExcelModel;
import com.p2psys.model.UserCreditModel;
import com.p2psys.service.UpfilesService;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.CompressImg;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.util.file.CsvReader;
import com.p2psys.web.action.BaseAction;

public class ManageUserCreditAction extends BaseAction{
	
	private  UserCreditService userCreditService;
	
	private CreditConvertService creditConvertService;
	
	private UserService userService;
	
	private static Logger logger = Logger.getLogger(ManageUserCreditAction.class);
	
	private UserCredit userCredit;
	
	// v1.6.7.2 RDPROJECT-509 zza 2013-12-11 start
	/**
	 * upfilesService
	 */
	private UpfilesService upfilesService;
	
	/**
	 * excel
	 */
	private File excel;
	
    /**
     * excelFileName
     */
    private String excelFileName;
    // v1.6.7.2 RDPROJECT-509 zza 2013-12-11 end
    
    private File pic_url;
	
    public File getPic_url() {
		return pic_url;
	}

	public void setPic_url(File pic_url) {
		this.pic_url = pic_url;
	}
	

	/**
     * 查看会员积分
     * @return
     * @throws Exception  
     */
	public String showUserCredit() throws Exception {
		int pageNo = NumberUtils.getInt(request.getParameter("page"));
		int pageSize = NumberUtils.getInt(request.getParameter("pageSize"));
		String username=StringUtils.isNull(request.getParameter("username"));
		String type = StringUtils.isNull(request.getParameter("type"));
		SearchParam param = new SearchParam();
		param.setUsername(username);
		PageDataList pageDataList = userCreditService.getUserCreditPage(pageNo, pageSize, param);
		setPageAttribute(pageDataList, param);
		// v1.6.7.2 RDPROJECT-509 zza 2013-12-12 start
		if (StringUtils.isBlank(type)) {
			return SUCCESS;
		} else {
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "credit_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export" + downloadFile;
            String[] names = new String[] { "user_id", "userName", "value", "expense_value", "valid_value", "addtime",
                    "tender_value", "borrow_value", "gift_value","bbs_value" };
            String[] titles = new String[] { "ID", "会员", "综合积分", "消费积分", "有效积分", "添加时间", "投资积分", "借款积分", "赠送积分", "论坛积分" };
            List<UserCreditModel> list = userCreditService.getUserCredit(param);
            ExcelHelper.writeExcel(infile, list, UserCreditModel.class, Arrays.asList(names), Arrays.asList(titles));
            export(infile, downloadFile);
			return null;
		}
		// v1.6.7.2 RDPROJECT-509 zza 2013-12-12 end
	}
	
	/**
	 * 查看会员积分操作记录
	 * @return
	 */
	public String showUserCreditLog(){
		int page=NumberUtils.getInt(request.getParameter("page"));
		long type_id = NumberUtils.getInt(request.getParameter("type_id"));
		long user_id = NumberUtils.getInt(request.getParameter("user_id"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));//开始时间
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2")); 
		SearchParam param = new SearchParam();
		param.setUsername(username);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
		PageDataList pageDataList = userCreditService.getCreditLogPage(page, param, type_id);
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end
		List<CreditType> typeList = userCreditService.getCreditTypeAll();
		request.setAttribute("typeList", typeList);
		request.setAttribute("type_id", type_id);
		request.setAttribute("user_id", user_id);
		setPageAttribute(pageDataList, param);
		return SUCCESS;
	}

	//用户积分兑换记录
	public String cashLog(){
		int page=NumberUtils.getInt(request.getParameter("page"));//分页
		String username=request.getParameter("username");
		String status_ = request.getParameter("status");//积分兑换信息类型
		Byte status = null;
		if(status_ != null && status_.length() > 0 ){
			try {
				status = Byte.parseByte(status_);
			} catch (Exception e) {
			}
		}
		SearchParam param = new SearchParam();
		param.setUsername(username);
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
		param.setStatus(status_);
		//查询用户积分操作记录
		PageDataList pageDataList = creditConvertService.getCreditConvertPage(page, param);
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end
		//查询所有的积分；类型
		List list = pageDataList.getList();
		request.setAttribute("username",username);//开始时间
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-16 start
//		request.setAttribute("status", status);
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-16 end
		request.setAttribute("convertList", list);
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", param.toMap());
		return "success";
	}
	
	/**
	 * 积分兑换审核查询
	 * @return
	 * @throws Exception
	 */
	public String auditDetail()throws Exception{
		String id = request.getParameter("id");
		if(id != null && id.length() > 0 ){
			CreditConvertModel creditConvert = creditConvertService.getCreditConvertById(Long.parseLong(id));
			request.setAttribute("creditConvert", creditConvert);
		}
		return "success";
	}
	
	/**
	 * 积分兑换审核查询
	 * @return
	 * @throws Exception
	 */
	public String audit()throws Exception{
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		String money = request.getParameter("money");
		boolean result = true;
		if(id == null || id.length() <= 0 || status == null || status.length() <= 0 || money == null || money.length() <= 0) result = false;
		
		// 数据转换
		long id_ = 0 ;
		byte status_ = 0;
		double money_ = 0;
		try {
			id_ = Long.parseLong(id);
			status_ = Byte.parseByte(status);
			money_ = Double.parseDouble(money);
		} catch (Exception e) {
			logger.error("积分兑换数据转换错误，积分兑换信息ID为"+id);
		}
		if(id_ <= 0 || status_ <= 0 || money_ <= 0) result = false;
		
		if(result){
			CreditConvertModel creditConvert = creditConvertService.getCreditConvertById(Long.parseLong(id));
			User auth_user = (User) session.get(Constant.AUTH_USER);
			creditConvert.setVerify_user(auth_user.getUsername());
			creditConvert.setVerify_user_id(auth_user.getUser_id());
			creditConvert.setStatus(status_);
			creditConvert.setMoney(money_);
			creditConvertService.auditCreditConvert(creditConvert);
		}else logger.error("积分兑换错误，积分兑换信息ID为"+id);
		return "success";
	}
	
	/**
     * 修改会员积分查询
     * @return
     */
	public String editCreditInit(){
		long user_id = NumberUtils.getLong(request.getParameter("user_id"));
		UserCredit userCredit = userCreditService.getUserCreditByUserId(user_id);
		request.setAttribute("userCredit", userCredit);
		User user = userService.getUserById(user_id);
		request.setAttribute("user", user);
		return SUCCESS;
	}
	
	/**
     * 修改会员积分
     * @return
     */
	public String editCredit(){
		if(userCredit != null && userCredit.getUser_id() > 0){ 
			User auth_user = (User) session.get(Constant.AUTH_USER);
			userCredit.setOp_user((int)auth_user.getUser_id());
			
			String remark = request.getParameter("remark");
			if(remark != null && remark.length() >0 && remark.length() < 255 ){
				Global.setTransfer("editRemark", remark);
			}
			Boolean result = userCreditService.updateUserCredit(userCredit);
			if(result){
				message("修改会员积分成功！", "/admin/credit/showUserCredit.html");	
				return ADMINMSG;
			}
		}
		message("修改会员积分失败！", "/admin/credit/showUserCredit.html");	
		return ADMINMSG;
	}
	
	/**
     * 论坛积分处理
     * @return
     */
	public String editBbsInit(){
		long user_id = NumberUtils.getLong(request.getParameter("user_id"));
		UserCredit userCredit = userCreditService.getUserCreditByUserId(user_id);
		request.setAttribute("userCredit", userCredit);
		User user = userService.getUserById(user_id);
		request.setAttribute("user", user);
		return SUCCESS;
	}
	
	/**
     * 修改会员论坛积分
     * @return
     */
	public String editCreditBbs(){
		if(userCredit != null && userCredit.getUser_id() > 0){ 
			User auth_user = (User) session.get(Constant.AUTH_USER);
			userCredit.setOp_user((int)auth_user.getUser_id());
			String remark = request.getParameter("remark");
			if(remark != null && remark.length() >0 && remark.length() < 255 ){
				Global.setTransfer("editRemark", remark);
			}
			Boolean result = userCreditService.updateUserCreditBBS(userCredit);
			if(result){
				message("修改会员论坛积分成功！", "/admin/credit/showUserCredit.html");	
				return ADMINMSG;
			}
		}
		message("修改会员论坛积分失败！", "/admin/credit/showUserCredit.html");	
		return ADMINMSG;
	}
	
	
	
	/**
	 * 积分兑换VIP审核查询
	 * @return
	 * @throws Exception
	 */
	public String auditVipInit()throws Exception{
		String id = request.getParameter("id");
		if(id != null && id.length() > 0 ){
			CreditConvertModel creditConvert = creditConvertService.getCreditConvertById(Long.parseLong(id));
			// 提取积分兑换规则信息
			RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INTEGRAL_VIP.getValue()));
			int integral = rule.getValueIntByKey("integral");//兑换VIP需要的积分
			int vip_time = rule.getValueIntByKey("vip_time");//兑换VIP有效时间（月）
			int time_check = rule.getValueIntByKey("time_check");// 兑换vip间隔时间是否启用
			int convert_time = rule.getValueIntByKey("convert_time");// 兑换vip间隔多长时间（月）
			request.setAttribute("integral", integral);
			request.setAttribute("vip_time", vip_time);
			request.setAttribute("time_check", time_check);
			request.setAttribute("convert_time", convert_time);
			request.setAttribute("creditConvert", creditConvert);
		}
		return "success";
	}
	
	/**
	 * 积分兑换VIP审核
	 * @return
	 * @throws Exception
	 */
	public String auditVip()throws Exception{
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		boolean result = true;
		if(id == null || id.length() <= 0 || status == null || status.length() <= 0) result = false;
		
		// 数据转换
		long id_ = 0 ;
		byte status_ = 0;
		try {
			id_ = Long.parseLong(id);
			status_ = Byte.parseByte(status);
		} catch (Exception e) {
			logger.error("积分兑换数据转换错误，积分兑换信息ID为"+id);
		}
		if(id_ <= 0 || status_ <= 0) result = false;
		
		if(result){
			CreditConvertModel creditConvert = creditConvertService.getCreditConvertById(Long.parseLong(id));
			User auth_user = (User) session.get(Constant.AUTH_USER);
			creditConvert.setVerify_user(auth_user.getUsername());
			creditConvert.setVerify_user_id(auth_user.getUser_id());
			creditConvert.setStatus(status_);
			creditConvertService.auditCreditConvertVip(creditConvert);
		}else logger.error("积分兑换错误，积分兑换信息ID为"+id);
		return "success";
	}
	
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-12 start
	/**
	 * 积分等级报表
	 * @return
	 */
	public String integralLevel(){
		String type=StringUtils.isNull(request.getParameter("exportType"));
		int pageNo = NumberUtils.getInt(request.getParameter("page"));
		int pageSize = NumberUtils.getInt(request.getParameter("pageSize"));
		String username=StringUtils.isNull(request.getParameter("username"));
		SearchParam param = new SearchParam();
		param.setUsername(username);
		try{
			PageDataList pageDataList = userCreditService.getUserCreditPage(pageNo, pageSize, param);
			List<UserCreditModel> uList=new ArrayList<UserCreditModel>();
			for (UserCreditModel ucm :(List<UserCreditModel>)pageDataList.getList()) {
				ucm.setPerferTotalMoney(userService.perferTotalMoney(ucm.getUser_id()+""));
			}
			if(StringUtils.isEmpty(type)){
				setPageAttribute(pageDataList, param);
				return SUCCESS;
			}else{
				String contextPath = ServletActionContext.getServletContext().getRealPath("/");
				String downloadFile="credit_"+System.currentTimeMillis()+".xls";
				String infile=contextPath+"/data/export/"+downloadFile;
				String[] names=new String[]{"userName","realname","value","valid_value","user_credit_level","perferTotalMoney"};
				String[] titles=new String[]{"用户名","真实姓名","综合积分","有效积分","用户等级","已优惠利息管理费总额"};
				List<UserCreditModel> list=userCreditService.getUserCreditModelList(param);
				for (UserCreditModel ucm :list) {
					String money=userService.perferTotalMoney(ucm.getUser_id()+"");
					if(!StringUtils.isEmpty(money)){
						ucm.setPerferTotalMoney(money+"元");
					}else{
						ucm.setPerferTotalMoney("0元");
					}
				}
				ExcelHelper.writeExcel(infile, list, UserCreditModel.class, Arrays.asList(names), Arrays.asList(titles));
				export(infile, downloadFile);
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	//v1.6.7.1RDPROJECT-510 cx 2013-12-12 end
	
	// v1.6.7.2 RDPROJECT-509 zza 2013-12-12 start
	/**
     * 上传excel
     * @return String
     */
    public String uploadCredit() {
        User user = getAuthUser();
        String type = paramString("type");
        if ("upload".equals(type)) {
            if (this.excel == null) {
                message("你未上传任何文件！");
                return "adminmsg";
            }
            String sub = this.excelFileName.substring(this.excelFileName.length() - 3);
            if ("csv".equals(sub) || "xls".equals(sub)) {
                String newFileName = generateUploadFilename(this.excelFileName);
                String newUrl = "";
                try {
                    newUrl = upload(this.excel, newFileName, "/data/credit", newFileName);
                } catch (Exception e) {
                    logger.error("上传文件出错：" + e.getMessage());
                }
                String xls = ServletActionContext.getServletContext().getRealPath("/data/credit") + File.separator
                        + newFileName;
                List[] data = (List[]) null;
                try {
                    if ("csv".equals(sub)) {
                        List dataList = CsvReader.read(xls);
                        data = (List[])dataList.toArray(new List[0]);
                    } else if ("xls".equals(sub)) {
                        data = ExcelHelper.read(xls);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (data.length > 0) {
                    if (data.length > 10000) {
                        message("数据超过10000条，会导致上传速度过慢，请分批上传！", "/admin/credit/uploadCredit.html");
                        return ADMINMSG;
                    }
                    List<String> list = new ArrayList<String>();
                    List<String> nameList = new ArrayList<String>();
                    List<String> creditList = new ArrayList<String>();
                    List<String> remarkList = new ArrayList<String>();
                    int totalUser = 0;
                    int totalRecharge = 0;
                    for (int i = 0; i < data.length; i++) {
                        List row = data[i];
                        if ((row != null) && (row.size() >= 2)) {
                            String username = StringUtils.isNull(row.get(0));
                            String credit = StringUtils.isNull(row.get(1));
                            String remark = null;
                            if (row.size() > 2) {
                                remark = StringUtils.isNull(row.get(2));
                            }
                            if ((!"用户名".equals(username)) && (!username.equals(""))) {
                                list.add(username);
                                totalUser++;
                            } else if ("".equals(username)) {
                                message("第" + (i + 1) + "行用户名为空，请确认后重新上传！", "/admin/credit/uploadCredit.html");
                                return ADMINMSG;
                            }
                            if ((!"积分".equals(credit)) && (!credit.equals(""))) {
                            	if ("用户名".equals(username) && !"积分".equals(credit)) {
                            		message("第  " + (i + 1) + " 行请输入积分，不然系统无法识别！", "/admin/credit/uploadCredit.html");
                        			return ADMINMSG;
								} else if (!StringUtils.isInteger(credit)) {
									message("第  " + (i + 1) + " 行的积分必须为整数，请修改后重新上传！", "/admin/credit/uploadCredit.html");
                        			return ADMINMSG;
								} else if (credit.length() > 11 || Double.valueOf(credit) <= 0) {
                        			message("第" + (i + 1) + "行积分异常，请确认后重新上传！", "/admin/credit/uploadCredit.html");
                        			return ADMINMSG;
                        		} else {
                        			totalRecharge += Integer.parseInt(credit);
									creditList.add(credit);
                        		}
                            } else if ("".equals(credit)) {
                                message("第" + (i + 1) + "行积分为空，请确认后重新上传！", "/admin/credit/uploadCredit.html");
                                return ADMINMSG;
                            }
                            if (!"备注".equals(remark)) {
                                remarkList.add(remark);
                            }
                        } else {
                        	message("上传的文件第" + (i + 1) + "行不能为空，请修改后重新上传！", "/admin/credit/uploadCredit.html");
                            return ADMINMSG;
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
                            message("用户名：" + nameList.toString() + " 不存在，请确认后重新上传！", "/admin/credit/uploadCredit.html");
                            return ADMINMSG;
                        }
                    } else {
                        nameList = list;
                        message("用户名：" + nameList.toString() + " 不存在，请确认后重新上传！", "/admin/credit/uploadCredit.html");
                        return ADMINMSG;
                    }
                    Upfiles upfiles = new Upfiles();
                    upfiles.setFile_name(newFileName);
                    upfiles.setFile_path(newUrl);
                    upfiles.setUser_id(user.getUser_id());
                    upfiles.setAddtime(getTimeStr());
                    upfiles.setStatus(0);
                    upfiles.setType("50"); //批量导入积分
                    try {
                        upfilesService.addUpfiles(upfiles);
                        message("文件上传成功，本次共为"+totalUser+"人次导入积分，累计导入积分" + totalRecharge + "分。");
                        List<UpfilesExcelModel> excelModelList = new ArrayList<UpfilesExcelModel>();
                        for (int i = 0; i < list.size(); i++) {
                            UpfilesExcelModel model = new UpfilesExcelModel();
                            model.setUser_id((String) hashMap.get(list.get(i)));
                            model.setUsername((String) list.get(i));
                            String money = (String) creditList.get(i);
                            model.setMoney(money.replace(",", ""));
                            model.setRemark((String) remarkList.get(i));
                            model.setStatus("0");
                            excelModelList.add(model);
                        }
                        ExcelHelper.writeExcel(xls, excelModelList, UpfilesExcelModel.class);
                        return ADMINMSG;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    message("excel数据为空！", "/admin/credit/uploadCredit.html");
                    return ADMINMSG;
                }
            } else {
                message("请上传后缀名为.xls或.csv的文件！", "/admin/credit/uploadCredit.html");
                return ADMINMSG;
            }
        }
        return SUCCESS;
    }
    
    /**
     * 保存excel
     * @return String
     */
    public String saveCreditUpfiles() {
        long id = NumberUtils.getLong(request.getParameter("id"));
        String[] usernames = (String[])request.getParameterValues("username");
        String[] credits = (String[])request.getParameterValues("credit");
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
                    getRealPath("/data/credit") + File.separator + upfiles.getFile_name();
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
                	String credit = credits[i];
                	if (StringUtils.isBlank(credit)) {
                		message("用户名：" + usernames[i] + " 的积分为空，请修改后重新保存！", "");
                		return ADMINMSG;
					} else if (!StringUtils.isInteger(credit)) {
                		message("用户名：" + usernames[i] + " 的积分错误，积分只能输入不带小数点的数字，请修改后重新保存！", "");
                		return ADMINMSG;
					} else if ("0".equals(credit) || "".equals(credit)) {
                		message("用户名：" + usernames[i] + " 导入的积分有误，请修改后重新保存！", "");
                		return ADMINMSG;
                	} else if (credit.length() > 11) {
                		message("用户名：" + usernames[i] + " 导入的积分过大，最多只能上传11位，请修改后重新保存！", "");
                		return ADMINMSG;
                	}
                	model.setMoney(credit.replace(",", ""));
                	model.setRemark(remarks[i]);
                	model.setStatus("0");
                	list.add(model);
				}
            }
            try {
                ExcelHelper.writeExcel(xls, list, UpfilesExcelModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            message("保存成功!", "/admin/credit/showCreditUpfiles.html");
            return ADMINMSG;
        }
        List<UpfilesExcelModel> list = readExcel(upfiles);
        if (list == null) {
        	 message("文件不存在!", "/admin/credit/showCreditUpfiles.html");
             return ADMINMSG;
		}
        int totalUser = list.size();
        double totalCredit = 0;
        for (UpfilesExcelModel excelModel : list) {
            String money = excelModel.getMoney();
			totalCredit += Double.parseDouble(money);
        }
        DecimalFormat df = new DecimalFormat("0.00"); // 避免计算结果出现E
        request.setAttribute("upfileTotal", "本次共为"+totalUser+"人次导入积分，累计导入积分"+ df.format(totalCredit) + "分。");
        request.setAttribute("upfiles", upfiles);
        request.setAttribute("list", list);
        return SUCCESS;
    }
    
    /**
     * 审核excel
     * @return String
     */
    public String verifyCreditUpfiles() {
        String type = paramString("type");
        long id = NumberUtils.getLong(request.getParameter("id"));
        int status = NumberUtils.getInt(request.getParameter("status"));
        String remark = StringUtils.isNull(request.getParameter("file_remark"));
        String[] usernames = (String[])request.getParameterValues("username");
        String[] credits = (String[])request.getParameterValues("credit");
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
            for (int i = 0; i < usernames.length; i++) {
                if (!credits[i].isEmpty() && credits[i] != null) {
                    String credit = credits[i];
                    if (credit.length() > 11) {
                        message("用户名：" + usernames[i] + " 的积分有误，请修改后重新保存！", "");
                        return ADMINMSG;
                    }
                }
            }
            try {
                String xls = ServletActionContext.getServletContext().
                        getRealPath("/data/credit") + File.separator + upfiles.getFile_name();
                List<UpfilesExcelModel> list = readExcel(upfiles);
                if (list == null) {
                	message("文件不存在!", "/admin/credit/showCreditUpfiles.html");
                    return ADMINMSG;
                }
                User verifyUser = getAuthUser();
                upfiles.setVerify_user(verifyUser.getUsername());
            	upfiles.setVerify_user_id(verifyUser.getUser_id());
            	upfiles.setVerify_time(getTimeStr());
            	upfiles.setStatus(status);
            	upfiles.setFile_remark(remark);
            	upfilesService.modifyUpfiles(upfiles);
                if (status == 1) {
                	boolean result = userCreditService.verifyBbsCreidtExcel(xls, verifyUser, list);
                	if (result) {
                		message("审核成功!", "/admin/credit/showCreditUpfiles.html");
                	} else {
                		message("审核失败!", "/admin/credit/showCreditUpfiles.html");
                	}
				} else {
                	message("审核失败!", "/admin/credit/showCreditUpfiles.html");
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
     * 查看
     * @return String
     */
    public String viewCreditUpfiles() {
        long id = NumberUtils.getLong(request.getParameter("id"));
        Upfiles upfiles = upfilesService.getUpfilesById(id);
        if (upfiles == null) {
            message("非法操作！");
            return ADMINMSG;
        }
        List<UpfilesExcelModel> list = new ArrayList<UpfilesExcelModel>();
        try {
            list = readExcel(upfiles);
            if (list == null) {
           	 	message("文件不存在!", "/admin/credit/showCreditUpfiles.html");
                return ADMINMSG;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int totalUser = 0;
        double totalCredit = 0;
        totalUser = list.size();
        for (UpfilesExcelModel creditModel : list) {
        	String money = creditModel.getMoney();
        	double credit = Double.parseDouble(money);
			totalCredit += credit;
        }
        request.setAttribute("upfileTotal", "本次共为" + totalUser+"人次充值，累计充值" + totalCredit + "元。");
        request.setAttribute("upfiles", upfiles);
        request.setAttribute("list", list);
        return SUCCESS;
    }
    
    /**
     * 读取excel信息的公共方法
     * @param UserCreditModel model
     * @return list
     */
    private List<UpfilesExcelModel> readExcel(Upfiles upfiles) {
        String xls = ServletActionContext.getServletContext().
                getRealPath("/data/credit") + File.separator + upfiles.getFile_name(); 
        List[] data = null;
        try {
            data = ExcelHelper.read(xls);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        List<UpfilesExcelModel> list = null;
        if (data != null && data.length > 0) {
            try {
                list = userCreditService.addBatchRecharge(data);
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
    public String delCreditUpfiles() {
        long id = NumberUtils.getLong(request.getParameter("id"));
        Upfiles upfiles = upfilesService.getUpfilesById(id);
        if (upfiles != null) {
        	String xls = ServletActionContext.getServletContext().
                    getRealPath("/data/credit") + File.separator + upfiles.getFile_name();
            File delFile = new File(xls); 
            delFile.delete();
            upfilesService.delUpfiles(id);
            message("删除excel成功！", "/admin/credit/showCreditUpfiles.html");
        }
        return ADMINMSG;
    }
    
    /**
     * excel列表
     * @return String
     */
    public String showCreditUpfiles() {
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
        // 查询批量添加积分
        PageDataList plist = upfilesService.getAllUpfiles(page, param, "50");
        setPageAttribute(plist, param);
        setMsgUrl("/admin/credit/showCreditUpfiles.html");
        return SUCCESS;
    }
    // v1.6.7.2 RDPROJECT-509 zza 2013-12-12 end
	
    
    
   // v1.6.7.2 RDPROJECT-569 cx 2013-12-17 start
    /**
     * 商品添加列表展示
     * @return
     */
    public String creditGoodsList(){
    	int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
    	String name=StringUtils.isNull(request.getParameter("name"));
    	String startDate=StringUtils.isNull(request.getParameter("startDate"));
    	String endDate=StringUtils.isNull(request.getParameter("endDate"));
		SearchParam param=new SearchParam();
		param.setKeywords(name);
		if(!StringUtils.isBlank(startDate)){
			startDate=startDate+" 00:00:00";
		}
		if(!StringUtils.isBlank(endDate)){
			endDate=endDate+" 23:59:59";
		}
		param.setStartDate(startDate);
		param.setEndDate(endDate);
		PageDataList pList=this.creditConvertService.getGoodsList(page, param,10,1);//后台默认显示10条
		List<CreditGoodsModel> creList=pList.getList();
		for (CreditGoodsModel gm : creList) {
			if(gm.getAttribute().length()>20){
				gm.setAttribute(gm.getAttribute().substring(0,20)+"...");
			}
			if(gm.getDescription().length()>20){
				gm.setDescription(gm.getDescription().substring(0,20)+"...");
			}
		GoodsCategory category=this.creditConvertService.getGoodCategoryById(gm.getCategory_id());
		if(category!=null){
			gm.setCategoryName(category.getName());
		}
		
		}
		setPageAttribute(pList,param);
		return "success";
    }
    
    
    /**
     * 商品分类管理
     * @return
     */
    public String goodsCatagoryList(){
    	 List<GoodsCategoryModel> parentList=this.creditConvertService.getGoodsCategoryListByParentId(0, null);
		request.setAttribute("parentList", parentList);
		String catetoryType=paramString("catetoryType");
    	try{
	    	List<GoodsCategoryModel> cateList=this.creditConvertService.getCategoryList(1,catetoryType);
	    	for (GoodsCategoryModel goodsCategoryModel : cateList) {
	    		GoodsCategory category=this.creditConvertService.getGoodCategoryById(goodsCategoryModel.getParent_id());
	    		if(category!=null){
	    			goodsCategoryModel.setParentName(category.getName());
	    		}
			}
			request.setAttribute("cateList", cateList);
			request.setAttribute("catetoryType", catetoryType);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
    	return "success";
    }
    
    public String addGoodsCategory(){
    	String type=paramString("type");
    	String actionType=paramString("actionType");
    	try{
    		if(!StringUtils.isBlank(actionType)){
	    		GoodsCategory category=new GoodsCategory();
		    	if((("parent").equals(type))){
					String parentName=paramString("parent_name");
					category.setName(parentName);
					category.setParent_id(0);
					category.setAdd_time(Integer.parseInt(Long.toString(new Date().getTime()/1000)));
					category.setIs_virtual(3);
				}else if((("child").equals(type))){
					String childName=paramString("child_name");
					int isVirtual=paramInt("is_virtual");
					int parentId=paramInt("parent_id");
					category.setName(childName);
					category.setAdd_time(Integer.parseInt(Long.toString(new Date().getTime()/1000)));
					category.setIs_virtual(isVirtual);
					category.setParent_id(parentId);
				}
		    	this.creditConvertService.saveGoodsCategory(category);
				return "list";
    		}else{
    			if(("parent").equals(type)){
    	    		return "parent";
    	    	}else{
    	    		List<GoodsCategoryModel> cateList=this.creditConvertService.getGoodsCategoryListByParentId(0, null);
    				request.setAttribute("parentList", cateList);
    	    		return "child";
    	    	}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}	
    	return null;
    }
    
    public String delGoodsCategory(){
    	String cateId=paramString("cateId");
    	if(!StringUtils.isBlank(cateId)){
    		this.creditConvertService.delGoodsCategory(cateId);
    	}
    	return "success";
    }
    
    
    /**
     * 上传文件
     * @return
     */
    public String uploadImg()throws Exception{
    	 String savePath = ServletActionContext.getServletContext().getRealPath("/")+"/data/goodsImg/";
		 File f=new File(savePath);
		 if(!f.exists()){
			 f.mkdir();
		 }
		// String fileName=generateUploadFilename(pic_url.getName());
		// fileName=fileName.substring(0, fileName.lastIndexOf("."));
		 String fileName=this.convertFileName(pic_url.getName());
		 File file=new File(savePath+"/"+fileName+".jpg");
		 FileUtils.copyFile(pic_url, file);
		 String outPutPath=ServletActionContext.getServletContext().getRealPath("/")+"/data/goodsImg/compress";
		 File imgFile=new File(outPutPath);
		 if(!imgFile.exists()){
			 imgFile.mkdir();
		 }
		 new CompressImg().compressPic(savePath+"/",outPutPath+"/", fileName+".jpg",fileName+".jpg", 130, 120, true);
		 response.getWriter().print(JSONArray.toJSON(fileName+".jpg"));
    	 return null;
    }
    
    private String convertFileName(String fileName){
   	  return System.currentTimeMillis()+"";
    }
    
    
    /**
     * 新增兑换商品
     * @return
     */
    public String addGoods(){
    	String actionType = StringUtils.isNull(request.getParameter("actionType"));
		try{
	    	if(!StringUtils.isBlank(actionType)){
				 String name=StringUtils.isNull(request.getParameter("name"));
				 String credit_value=StringUtils.isNull(request.getParameter("credit_value"));
				 String store=StringUtils.isNull(request.getParameter("store"));
				 String category_id=StringUtils.isNull(request.getParameter("category_id"));
				 String cost=StringUtils.isNull(request.getParameter("cost"));
				 String market_price=StringUtils.isNull(request.getParameter("market_price"));
				 String attribute=StringUtils.isNull(request.getParameter("attribute"));
				 String description=StringUtils.isNull(request.getParameter("description"));
				 String remarks=StringUtils.isNull(request.getParameter("remarks"));
				 String shelves_time=StringUtils.isNull(request.getParameter("shelves_time"));
				// Boolean isImage = ImageUtil.fileIsImage(pic_url);
				// if(!isImage){
//						message("上传附件格式不正确!","/admin/credit/creditGoodsList.html");
//						return ADMINMSG;
//				 }
//				 String fileName=generateUploadFilename(pic_url.getName());
//				 fileName=fileName.substring(0, fileName.lastIndexOf("."));
//				 String contextPath = ServletActionContext.getServletContext().getRealPath("/")+"/data/goodsImg";
//				 File f=new File(contextPath);
//				 if(!f.exists()){
//					 f.mkdir();
//				 }
//				 File file=new File(contextPath+"/"+fileName+".jpg");
//				 FileUtils.copyFile(pic_url, file);
				 CreditGoodsModel model=new CreditGoodsModel();
				 model.setName(name);
				 model.setCredit_value(Integer.parseInt(credit_value));
				 model.setStore(Integer.parseInt(store));
				 model.setCategory_id(Integer.parseInt(category_id));
				 if(!StringUtils.isBlank(cost)){
					 model.setCost(Double.parseDouble(cost));
				 }
				 if(!StringUtils.isBlank(market_price)){
					 model.setMarket_price(Double.parseDouble(market_price));
				 }
				 model.setAttribute(attribute);
				 model.setDescription(description);
				 model.setRemarks(remarks);
				 model.setCreate_user_id(null);
				 model.setAdd_time(Integer.parseInt(String.valueOf(new Date().getTime()/1000)));
				 model.setUpdate_time(Integer.parseInt(String.valueOf(new Date().getTime()/1000)));
				 model.setShelves_time(Integer.parseInt(Long.toString(DateUtils.valueOf(shelves_time).getTime()/1000)));
//				 model.setPic_name(fileName);
//				 model.setPic_url("/data/goodsImg/"+fileName+".jpg");
//				 model.setOrdering(1);
//				 
//				 //等比率压缩图片
//				 String outPutPath=ServletActionContext.getServletContext().getRealPath("/")+"/data/goodsImg/compress";
//				 File imgFile=new File(outPutPath);
//				 if(!imgFile.exists()){
//					 imgFile.mkdir();
//				 }
//				 new CompressImg().compressPic(contextPath+"/",outPutPath+"/", fileName+".jpg",fileName+".jpg", 130, 120, true);
//				 model.setCompress_pic_url("/data/goodsImg/compress/"+fileName+".jpg");
				 long goodsId=this.creditConvertService.saveGoodsAndPic(model);
				 
				 String fileValueList=paramString("fileValue");
				 if(fileValueList.startsWith(",")){
					 fileValueList=fileValueList.substring(1, fileValueList.length());
				 }
				 List<GoodsPic> gpList=new ArrayList<GoodsPic>();
				 if(fileValueList!=null){
					 String [] str=fileValueList.split(",");
					 int order=1;
					 for(int i=0;i<str.length;i++){
						 if(StringUtils.isBlank(str[i])){
							 continue;
						 }
						 GoodsPic goodsPic=new GoodsPic();
						 goodsPic.setGoods_id(goodsId);
						 goodsPic.setPic_name(str[i]);
						 goodsPic.setPic_url("/data/goodsImg/"+str[i]);
						 goodsPic.setOrdering(order);
						 goodsPic.setCompress_pic_url("/data/goodsImg/compress/"+str[i]);
						 goodsPic.setAdd_time(Integer.parseInt((new Date().getTime()/1000)+""));
						 gpList.add(goodsPic);
						 order++;
					 }
					 
				 }
				 if(gpList!=null && gpList.size()>0){
					 for (GoodsPic goodsPic : gpList) {
						this.creditConvertService.saveGoodsPic(goodsPic);
					}
				 }else{
					 this.creditConvertService.delGoodsAndPic(Integer.parseInt(goodsId+""));
					 message("商品图未上传！","/admin/credit/creditGoodsList.html");
					 return ADMINMSG;
				 }
				 
				 message("新增商品成功！","/admin/credit/creditGoodsList.html");
				 return ADMINMSG;
			 }else{
				 List<GoodsCategoryModel> cateList=this.creditConvertService.getGoodsCategoryListByParentId(0, null);
				// List<CreditGoodsModel> cateList=this.creditConvertService.getCategoryList();
				 request.setAttribute("cateList", cateList);
				 request.setAttribute("sessionId", request.getSession().getId());
				 return SUCCESS;
			 }
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
    }
      
    /**
     * 修改商品
     * @return
     */
    public String updateGoods(){
    	// 参数为0 表示得到二级菜单
    	List<GoodsCategoryModel> cateList=this.creditConvertService.getCategoryList(0,null);
		request.setAttribute("cateList", cateList);
    	String id=StringUtils.isNull(request.getParameter("id"));
    	String actionType=StringUtils.isNull(request.getParameter("actionType"));
    	try{
	    	if(!StringUtils.isBlank(id)&&(!StringUtils.isBlank(actionType))){
	    		CreditGoodsModel model=this.creditConvertService.getGoodsById(Integer.parseInt(id));
	    		 String name=StringUtils.isNull(request.getParameter("name"));
				 String credit_value=StringUtils.isNull(request.getParameter("credit_value"));
				 String store=StringUtils.isNull(request.getParameter("store"));
				 String category_id=StringUtils.isNull(request.getParameter("category_id"));
				 String cost=StringUtils.isNull(request.getParameter("cost"));
				 String market_price=StringUtils.isNull(request.getParameter("market_price"));
				 String attribute=StringUtils.isNull(request.getParameter("attribute"));
				 String description=StringUtils.isNull(request.getParameter("description"));
				 String remarks=StringUtils.isNull(request.getParameter("remarks"));
				 String shelves_time=StringUtils.isNull(request.getParameter("shelves_time"));
				 
//				 if(pic_url!=null){
//					 String fileName=generateUploadFilename(pic_url.getName());
//					 fileName=fileName.substring(0, fileName.lastIndexOf("."));
//					 String contextPath = ServletActionContext.getServletContext().getRealPath("/")+"/data/goodsImg";
//					 File f=new File(contextPath);
//					 if(!f.exists()){
//						 f.mkdir();
//					 }
//					 File file=new File(contextPath+"/"+fileName+".jpg");
//					 FileUtils.copyFile(pic_url, file);
//					 model.setPic_name(fileName);
//					 model.setPic_url("/data/goodsImg/"+fileName+".jpg");
//					 
//					 //等比率压缩图片
//					 String outPutPath=ServletActionContext.getServletContext().getRealPath("/")+"/data/goodsImg/compress";
//					 File imgFile=new File(outPutPath);
//					 if(!imgFile.exists()){
//						 imgFile.mkdir();
//					 }
//					 new CompressImg().compressPic(contextPath+"/",outPutPath+"/", fileName+".jpg",fileName+".jpg", 130, 120, true);
//					 model.setCompress_pic_url("/data/goodsImg/compress/"+fileName+".jpg");
//				 }
				 String fileValueList=paramString("fileValue");
				 if(fileValueList.startsWith(",")){
					 fileValueList=fileValueList.substring(1, fileValueList.length());
				 }
				 List<GoodsPic> gpList=new ArrayList<GoodsPic>();
				 if(fileValueList!=null){
					 String [] str=fileValueList.split(",");
					 int order=1;
					 for(int i=0;i<str.length;i++){
						 if(StringUtils.isBlank(str[i])){
							 continue;
						 }
						 GoodsPic goodsPic=new GoodsPic();
						 goodsPic.setGoods_id(model.getId());
						 goodsPic.setPic_name(str[i]);
						 goodsPic.setPic_url("/data/goodsImg/"+str[i]);
						 goodsPic.setOrdering(order);
						 goodsPic.setCompress_pic_url("/data/goodsImg/compress/"+str[i]);
						 goodsPic.setAdd_time(Integer.parseInt((new Date().getTime()/1000)+""));
						 gpList.add(goodsPic);
						 order++;
					 }
				 }
				 if(gpList!=null && gpList.size()>0){
					 this.creditConvertService.delAllGoodsPicByGoodsId(Integer.parseInt(model.getId()+""));
					 for (GoodsPic goodsPic : gpList) {
						 this.creditConvertService.saveGoodsPic(goodsPic);
					}
				 }else{
					 message("商品图未上传！","/admin/credit/creditGoodsList.html");
					 return ADMINMSG;
				 }
				 
				 model.setName(name);
				 model.setCredit_value(Integer.parseInt(credit_value));
				 model.setStore(Integer.parseInt(store));
				 model.setCategory_id(Integer.parseInt(category_id));
				 model.setCost(Double.parseDouble(cost));
				 model.setMarket_price(Double.parseDouble(market_price));
				 model.setAttribute(attribute);
				 model.setDescription(description);
				 model.setRemarks(remarks);
				 model.setCreate_user_id(null);
				 model.setUpdate_time(Integer.parseInt(String.valueOf(new Date().getTime()/1000)));
				 model.setShelves_time(Integer.parseInt(Long.toString(DateUtils.valueOf(shelves_time).getTime()/1000)));
				 model.setUpdate_time(Integer.parseInt(String.valueOf(new Date().getTime()/1000)));
				 this.creditConvertService.updateGoodsAndPic(model);
				 message("修改商品成功！","/admin/credit/creditGoodsList.html");
				 return ADMINMSG;
	    	}else{
	    		CreditGoodsModel model=this.creditConvertService.getGoodsById(Integer.parseInt(id));
//	    		if(model.getCategory_id()!=null){
//	    			List<GoodsCategory> list=this.creditConvertService.getGoodsCategoryListByParentId(null, model.getCategory_id());
//	    			if(list!=null && list.size()>0){
//	    				GoodsCategory category=list.get(0);
//	    			}
//	    		}
	    		List<GoodsPic> gpList=this.creditConvertService.getGoodsPicByGoodsId(Integer.parseInt(model.getId()+""));
	    		String allFileName="";
	    		for (GoodsPic goodsPic : gpList) {
	    			allFileName+=goodsPic.getPic_name()+",";
				}
	    		if(allFileName.endsWith(",")){
	    			allFileName=allFileName.substring(0,allFileName.length()-1);
	    		}
	    		request.setAttribute("gpList", gpList);
	    		request.setAttribute("allFileName", allFileName);
	    		request.setAttribute("goods", model);
	    		request.setAttribute("sessionId", request.getSession().getId());
	    		return SUCCESS;
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public String delGoods(){
    	String id=StringUtils.isNull(request.getParameter("id"));
    	if(!StringUtils.isBlank(id)){
    		this.creditConvertService.delGoodsAndPic(Integer.parseInt(id));
    		 message("删除商品成功！","/admin/credit/creditGoodsList.html");
			 return ADMINMSG;
    	}
    	return SUCCESS;
    }
    
    /**
     * 积分兑换后台审核列表显示
     * @return
     */
    public String checkCreditGoods(){
    	int page=NumberUtils.getInt(request.getParameter("page"));//分页
		String username=StringUtils.isNull(request.getParameter("username"));
		String status_ = StringUtils.isNull(request.getParameter("status"));//积分兑换信息类型
		SearchParam param = new SearchParam();
		param.setUsername(username);
		if(!StringUtils.isBlank(status_)&&(!("-1").equals(status_))){
			param.setStatus(status_);
		}
		PageDataList pageDataList = creditConvertService.getCreditConvertPage(page, param);
		List<CreditConvert> list = pageDataList.getList();
		for (CreditConvert creditConvert : list) {
			CreditModel cm=this.creditConvertService.getCreditByUserId(creditConvert.getUser_id());
			if(cm!=null){
				creditConvert.setValid_value(cm.getValid_value()+"");
			}
			if(creditConvert.getGoods_id()!=null){
				CreditGoodsModel model=this.creditConvertService.getGoodsById(creditConvert.getGoods_id());
				if(model!=null){
					creditConvert.setGoods_name(model.getName());
					creditConvert.setPic_url(model.getPic_url());
				}
			}
		}
		request.setAttribute("username",username);//开始时间
		request.setAttribute("list", list);
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", param.toMap());
		return "success";
    }
    
    /**
     * 后台商品审核
     * @return
     */
    public String auditGoods(){
    	String id=StringUtils.isNull(request.getParameter("id"));
    	if(!StringUtils.isBlank(id)){
    		CreditConvertModel creditConvert = creditConvertService.getCreditConvertById(Long.parseLong(id));
    		if(creditConvert!=null && creditConvert.getGoods_id()!=null){
    			CreditModel cm=this.creditConvertService.getCreditByUserId(creditConvert.getUser_id());
    			if(cm!=null){
    				creditConvert.setValid_value(cm.getValid_value()+"");
    			}
    			CreditGoodsModel model=this.creditConvertService.getGoodsById(creditConvert.getGoods_id());
    			if(model!=null){
    				creditConvert.setGoods_name(model.getName());
    				creditConvert.setPic_url(model.getPic_url());
    			}
    		}
    		request.setAttribute("creditConvert", creditConvert);
        	return "success";
    	}
    	return null;
    }
    
    /**
     * 审核后保存
     * @return
     */
    public String saveAuditGoods(){
    	User user = (User) session.get(Constant.AUTH_USER);
		if (user == null) {
			return "login";
		}
    	String status=StringUtils.isNull(request.getParameter("status"));
    	String id=StringUtils.isNull(request.getParameter("id"));
    	CreditLog creditLog=new CreditLog();
    	if(!StringUtils.isBlank(id)){
    		CreditConvertModel convertModel = creditConvertService.getCreditConvertById(Long.parseLong(id));
    		String remark="";
    		//如果是虚拟 dw_goods_category is_vartural= 1:vip 2: 现金 特殊处理
    		//通过dw_credit_convert 得到商品ID 然后根据商品ID得到分类
    		String isVirtual=this.creditConvertService.getIsVirtualByConvertGoodsId(Integer.parseInt(convertModel.getId()+""));
	    		CreditModel creditModel=this.creditConvertService.getCreditByUserId(convertModel.getUser_id());
	    		if(("1").equals(status)){ //审核通过
	    			if(("0").equals(isVirtual)){  //实物
	    				this.creditConvertService.updateCreditConvertByStatus(user,Byte.parseByte(status), Integer.parseInt(id));
		    			creditLog.setOp(Long.parseLong("2")); //减少积分 
			    		remark="通过，扣除积分"+convertModel.getCredit_value();
			    		creditLog.setValid_value(creditModel.getValid_value());
	    			}else if(("1").equals(isVirtual)){ //vip
	    				remark="通过，扣除积分"+convertModel.getCredit_value();
	        			convertModel.setStatus(Byte.parseByte(status));
	    				creditConvertService.auditCreditConvertVip(convertModel);
	    				creditLog.setValid_value(creditModel.getValid_value());
	        		}else if(("2").equals(isVirtual)){ //现金
	        			remark="通过，扣除积分"+convertModel.getCredit_value();
	        			convertModel.setStatus(Byte.parseByte(status));
	        			creditConvertService.auditCreditConvert(convertModel);
	        			creditLog.setValid_value(creditModel.getValid_value());
	        		}
	    		}else{//审核不通过 积分不变
	    			this.creditConvertService.updateCreditConvertByStatus(user,Byte.parseByte(status), Integer.parseInt(id));
	    			creditLog.setOp(Long.parseLong("3")); 
	    			remark="不通过，积分不变！";
	    			//对应商品数量+
	    			this.creditConvertService.addGoodsStore(convertModel);
	    			//用户有效积分增加 消费积分减少
	    			this.creditConvertService.updateCreditValieByCredit(convertModel.getUser_id(),Integer.parseInt(convertModel.getCredit_value()+""));
	    			creditLog.setValid_value((creditModel.getValid_value())+Integer.parseInt(convertModel.getCredit_value()+""));
	    		}
    		
	    		//记录dw_credit_log明细
			    creditLog.setUser_id(Integer.parseInt(convertModel.getUser_id()+""));
			    creditLog.setType_id(21); //消费积分  对应dw_credit_type表
			    creditLog.setValue(Integer.parseInt(convertModel.getCredit_value()+""));
			    creditLog.setRemark("您所兑换的商品，经管理员审核"+remark);
			    creditLog.setOp_user(Integer.parseInt(user.getUser_id()+""));
			    creditLog.setAddip(user.getAddip());
			    creditLog.setAddtime(Integer.parseInt(Long.toString(new Date().getTime()/1000)+""));
			    this.creditConvertService.saveCreditLog(creditLog);
    		 message("提交成功", "/admin/credit/checkCreditGoods.html");
			 return ADMINMSG;
    	}
    	return "success";
    }
    // v1.6.7.2 RDPROJECT-569 cx 2013-12-17 end
    
    
   
    
	public UserCreditService getUserCreditService() {
		return userCreditService;
	}

	public void setUserCreditService(UserCreditService userCreditService) {
		this.userCreditService = userCreditService;
	}

	public CreditConvertService getCreditConvertService() {
		return creditConvertService;
	}

	public void setCreditConvertService(CreditConvertService creditConvertService) {
		this.creditConvertService = creditConvertService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserCredit getUserCredit() {
		return userCredit;
	}

	public void setUserCredit(UserCredit userCredit) {
		this.userCredit = userCredit;
	}

	public File getExcel() {
		return excel;
	}

	public void setExcel(File excel) {
		this.excel = excel;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public void setUpfilesService(UpfilesService upfilesService) {
		this.upfilesService = upfilesService;
	}
}
