package com.p2psys.report.web.action.admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.p2psys.context.Constant;
import com.p2psys.model.PageDataList;
import com.p2psys.model.ReportBorrowModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserBorrowModel;
import com.p2psys.report.model.ReportModel;
import com.p2psys.report.service.ReportService;
import com.p2psys.service.BorrowService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 新报表统计v1.6.7.2
 
 *  新增
 *  
 */
public class NewReportAction extends BaseAction {
	private final static Logger logger = Logger.getLogger(NewReportAction.class);
	
	private BorrowService borrowService;
	
	private ReportService reportService;
	private int type; //标种期限 
	private int isday=0; //标种 
	
	/**
	 * 各借款项目数据统计
	 * @return
	 */
	public String allBorrowDataList(){
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		String type=StringUtils.isNull(request.getParameter("type"));
		int page = NumberUtils.getInt(request.getParameter("page"));
		String username = StringUtils.isNull(request.getParameter("username"));
		String verify_user = StringUtils.isNull(request.getParameter("verify_user"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		SearchParam param=new SearchParam();
		param.setType(String.valueOf(type));
		param.setStatusArray(new int[]{1,3,6,7,8});
		param.setUsername(username);
		param.setVerify_user(verify_user);
		if(!StringUtils.isEmpty(dotime1)){
			dotime1=dotime1.substring(0,10);
			dotime1+=" 00:00:00";
		}
		param.setInvite_startTime(dotime2);
		if(!StringUtils.isEmpty(dotime2)){
			dotime2=dotime2.substring(0,10);
			dotime2+=" 23:59:59";
		}
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		PageDataList plist=borrowService.getAllBorrowList(page, param);
		DecimalFormat df = new DecimalFormat("#0.000"); 
		if(plist.getList()!=null && plist.getList().size()>0){
			List<UserBorrowModel> ubList=plist.getList();
			for (UserBorrowModel userBorrowModel : ubList) {
				userBorrowModel.setTbMoney(df.format(((borrowService.getSumAccountByBorrowId(userBorrowModel.getId()))/10000))+""); //万为单位
			}
			plist.setList(ubList);
		}
		setPageAttribute(plist, param);
		setMsgUrl("/admin/newreport/allBorrowDataList.html");
		if(actionType.isEmpty()){
			request.setAttribute("startDate", dotime1);
			request.setAttribute("endDate", dotime2);
			request.setAttribute("listsize", borrowService.getBorrowCount(param));
			return SUCCESS;
		}else{
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="allborrow_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			String[] names=new String[]{"username","name","tbMoney","qslimit","flow_money","timeLimitStr","account","typeStr","isdayStr","addtime"};
			String[] titles=new String[]{"借款人","借款标题","投标金额（万）","借款期数（期）","成交分额","借款期限","借款金额","标种","类型","发布时间"};
			List<UserBorrowModel> allBorrowList = borrowService.getAllBorrowList(param);
			try {
				List<UserBorrowModel> exList=new ArrayList<UserBorrowModel>();
				for (UserBorrowModel um : allBorrowList) {
					um.setTbMoney(((borrowService.getSumAccountByBorrowId(um.getId()))/10000)+"");
					um.setQslimit((um.getOrder()+1)+"/"+um.getTime_limit());
					if(um.getFlow_money()!=0){
						um.setFlow_money((int)um.getAccount_yes()/um.getFlow_money());
					}
					if(("1").equals(um.getIsday()+"")){
						um.setTimeLimitStr(um.getTime_limit_day()+"天");
					}else{
						um.setTimeLimitStr(um.getTime_limit()+"月");
					}
					if(um.getType()==101){
						um.setTypeStr("秒还标");
					}else if(um.getType()==102){
						um.setTypeStr("信用标");
					}else if(um.getType()==103){
						um.setTypeStr("抵押标");
					}else if(um.getType()==104){
						um.setTypeStr("净值标");
					}else if(um.getType()==110){
						um.setTypeStr("流转标");
					}else if(um.getType()==112){
						um.setTypeStr("担保标");
					}
					if(um.getIsday()==1){
						um.setIsdayStr("天标");
					}else{
						um.setIsdayStr("月标");
					}
					um.setRepayment_account(um.getRepayment_account()-um.getRepayment_yesaccount());
					exList.add(um);
				}
				ExcelHelper.writeExcel(infile,exList, UserBorrowModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
	/**
	 * 各借款期限成交金额统计
	 * @return
	 * @throws Exception
	 */
	public String allPayAccount(){
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		String isDay=StringUtils.isNull(request.getParameter("isday"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		String startDate="";
		String endDate="";
		if(StringUtils.isEmpty(dotime1)){
			dotime1=DateUtils.dateStr2(new Date());
			String dateStr=DateUtils.dateStr2(new Date())+" 00:00:00";
			startDate=Long.toString(DateUtils.valueOf(dateStr).getTime()/1000);
			//startDate=Long.toString(DateUtils.getNowTime());
		}else{
			dotime1=dotime1.substring(0,10)+" 00:00:00";
			startDate=Long.toString(DateUtils.valueOf(dotime1).getTime()/1000);
		}
		if(StringUtils.isEmpty(dotime2)){
			dotime2=DateUtils.dateStr2(new Date());
			String dateStr=DateUtils.dateStr2(new Date())+" 23:59:59";
			endDate=Long.toString(DateUtils.valueOf(dateStr).getTime()/1000); 
			//endDate=Long.toString(DateUtils.getNowTime());
		}else{
			dotime2=dotime2.substring(0,10)+" 23:59:59";
			endDate=Long.toString(DateUtils.valueOf(dotime2).getTime()/1000); 
		}
		try{
			DecimalFormat df = new DecimalFormat("#0.000"); 
			List<ReportBorrowModel> bList=new ArrayList<ReportBorrowModel>();
			int num=0;
			//发标期限
			if(!StringUtils.isEmpty(isDay)){
				if(("1").equals(isDay)){
					num=30;
				}else if(("0").equals(isDay)){
					num=12;
				}
			}else{
				num=12;
				isDay="0";
			}
			for(int i=1;i<=num;i++){
				ReportBorrowModel borrow=new ReportBorrowModel();
				if(("1").equals(isDay)){
					borrow.setLimitDay(i+"天");
				}else if(("0").equals(isDay)){
					borrow.setLimitDay(i+"月");
				}
				//投标总金额
				double allTBMoney=borrowService.getAllTenderMoneyByDate(startDate, endDate,i+"",isDay,null);
				borrow.setAllTBMoney(df.format(allTBMoney/10000)+"");//万单位
				//发标笔数
				Integer countFB=borrowService.getCountBorrowByDate(startDate, endDate,i+"",isDay,null);
				borrow.setCountFB(countFB);
				//投标笔数
				Integer countTB=borrowService.getCountTenderBorrowByDate(startDate, endDate,i+"",isDay);
				borrow.setCountTB(countTB);
				//投标人数
				Integer countRS=borrowService.getCountTenderUserByDate(startDate, endDate,i+"",isDay);
				borrow.setCountRS(countRS);
				bList.add(borrow);
			}
			if(StringUtils.isEmpty(actionType)){
				request.setAttribute("isday", isDay);
				request.setAttribute("startDate", dotime1);
				request.setAttribute("endDate", dotime2);
				request.setAttribute("borrowList",bList);
				return SUCCESS;
			}else{
				String contextPath = ServletActionContext.getServletContext().getRealPath("/");
				String downloadFile="allborrow_"+System.currentTimeMillis()+".xls";
				String infile=contextPath+"/data/export/"+downloadFile;
				String[] names=new String[]{"limitDay","allTBMoney","countFB","countTB","countRS"};
				String[] titles=new String[]{"借款期限","投标金额（万）","发标笔数","投标笔数","投标人数"};
				ExcelHelper.writeExcel(infile,bList, UserBorrowModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			}
		}catch(Exception e){
			logger.info(e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 各标种借款期限统计
	 * @return
	 */
	
	public String allTypeAccount(){
		String type=StringUtils.isNull(request.getParameter("type"));
		String dotime1 = StringUtils.isNull(request.getParameter("dotime1"));
		String dotime2 = StringUtils.isNull(request.getParameter("dotime2"));
		String startDate="";
		String endDate="";
		if(StringUtils.isEmpty(dotime1)){
			dotime1=DateUtils.dateStr2(new Date());
			String dateStr=DateUtils.dateStr2(new Date())+" 00:00:00";
			startDate=Long.toString(DateUtils.valueOf(dateStr).getTime()/1000);
		//	startDate=Long.toString(DateUtils.getNowTime());
		}else{
			dotime1=dotime1.substring(0,10)+" 00:00:00";
			startDate=Long.toString(DateUtils.valueOf(dotime1).getTime()/1000);
		}
		if(StringUtils.isEmpty(dotime2)){
			dotime2=DateUtils.dateStr2(new Date());
			String dateStr=DateUtils.dateStr2(new Date())+" 23:59:59";
			endDate=Long.toString(DateUtils.valueOf(dateStr).getTime()/1000);
		//	endDate=Long.toString(DateUtils.getNowTime());
		}else{
			dotime2=dotime2.substring(0,10)+" 23:59:59";
			endDate=Long.toString(DateUtils.valueOf(dotime2).getTime()/1000); 
		}
		//秒标、信用标、抵押标、净标、担保标、流转标
		int [] typeStr={Constant.TYPE_SECOND,Constant.TYPE_CREDIT,Constant.TYPE_MORTGAGE,Constant.TYPE_PROPERTY,Constant.TYPE_OFFVOUCH,Constant.TYPE_FLOW};
		if(!StringUtils.isEmpty(type) && (!("100").equals(type))){ //100为全部
			typeStr=new int[1];
			typeStr[0]=Integer.parseInt(type);
		}
		try{
			double allMoney=borrowService.getAllTenderMoneyByDate(startDate, endDate, null, null,null); 
			List<ReportBorrowModel> bList=new ArrayList<ReportBorrowModel>();
			DecimalFormat df = new DecimalFormat("#0.000"); 
			for(int i=0;i<typeStr.length;i++){
				ReportBorrowModel borrow=new ReportBorrowModel();
				//所有投标总额
				borrow.setAllTBMoney(df.format(allMoney/10000));
				switch (typeStr[i]) {
					case 101:borrow.setName("秒还标");break;
					case 102:borrow.setName("信用标");break;
					case 103:borrow.setName("抵押标");break;
					case 104:borrow.setName("净值标");break;
					case 110:borrow.setName("流转标");break;
					case 112:borrow.setName("担保标");break;
					default:break;
				}
				//发标笔数
				Integer countFB=borrowService.getCountBorrowByDate(startDate, endDate,null,null,typeStr[i]);
				borrow.setCountFB(countFB);
				//此标总额
				double eachMoney=borrowService.getAllTenderMoneyByDate(startDate, endDate, null, null,typeStr[i]);
				if(eachMoney==0 || allMoney==0){
					borrow.setBl("0%");
				}else{
					borrow.setBl(df.format(eachMoney/allMoney*100)+"%");
				}
				//此标为天标 isday=1
				Integer tbCount=borrowService.getBorrowTypeByIsDay(startDate, endDate, 1, typeStr[i]);
				borrow.setTbCount(tbCount);;
				//月标
				Integer ybCount=borrowService.getBorrowTypeByIsDay(startDate, endDate, 0, typeStr[i]);
				borrow.setYbCount(ybCount);
				bList.add(borrow);
			}
			if(StringUtils.isEmpty(actionType)){
				request.setAttribute("startDate", dotime1);
				request.setAttribute("endDate", dotime2);
				request.setAttribute("borrowlist", bList);
				return SUCCESS;
			}else{
				String contextPath = ServletActionContext.getServletContext().getRealPath("/");
				String downloadFile="allborrow_"+System.currentTimeMillis()+".xls";
				String infile=contextPath+"/data/export/"+downloadFile;
				String[] names=new String[]{"name","allTBMoney","countFB","bl","tbCount","ybCount"};
				String[] titles=new String[]{"标名","成交总额（万）","发标笔数","比例","天标笔数","月标笔数"};
				ExcelHelper.writeExcel(infile,bList, UserBorrowModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			}
		}catch(Exception e){
			logger.info(e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 各月成交数据对比统计
	 * @return
	 */
	public String eachMonthPayDataList(){
		String isyear = StringUtils.isNull(request.getParameter("isyear"));
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		int month[]={1,2,3,4,5,6,7,8,9,10,11,12};
		if(StringUtils.isEmpty(isyear)){
			isyear=DateUtils.getTimeYear(new Date())+"";
		}
		List<ReportModel> rmList=new ArrayList<ReportModel>();
		DecimalFormat df = new DecimalFormat("#0.000"); 
		try{
			for (int i = 0; i < month.length; i++) {
				ReportModel reportModel=new ReportModel();
				reportModel.setMonth(month[i]+"月");
				long startDate=DateUtils.monthFirstDay(Integer.parseInt(isyear), month[i]).getTime()/1000;
				long endDate=DateUtils.monthLastDay(Integer.parseInt(isyear), month[i]).getTime()/1000;
				//成交总额
				double allMoney=borrowService.getAllTenderMoneyByDate(startDate+"", endDate+"", null, null,null); 
				reportModel.setTotalPay(df.format(allMoney/10000)+"");
				//借款项目
				reportModel.setCountBorrow("");
				//发标笔数
				Integer countFB=borrowService.getCountBorrowByDate(startDate+"", endDate+"",null,null,null);
				reportModel.setCountFB(countFB+"");
				//投标人数
				Integer countRS=borrowService.getCountTenderUserByDate(startDate+"", endDate+"",null,null);
				reportModel.setCountInvestor(countRS+"");
				Integer countRegister=reportService.registerCount(startDate, endDate);
				reportModel.setCountHY(countRegister+"");
				rmList.add(reportModel);
			}
			if(StringUtils.isEmpty(actionType)){
				request.setAttribute("rmlist", rmList);
				request.setAttribute("isyear", isyear);
				return SUCCESS;
			}else{
				String contextPath = ServletActionContext.getServletContext().getRealPath("/");
				String downloadFile="allborrow_"+System.currentTimeMillis()+".xls";
				String infile=contextPath+"/data/export/"+downloadFile;
				String[] names=new String[]{"month","totalPay","countBorrow","countFB","countInvestor","countHY"};
				String[] titles=new String[]{"月份","成交总额（万）","借款项目（个）","发标笔数（笔）","投资人数（人）","注册会员（人）"};
				ExcelHelper.writeExcel(infile,rmList, ReportModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			}
		}catch(Exception e){
			logger.info(e);
			e.printStackTrace();
		}
		return null;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BorrowService getBorrowService() {
		return borrowService;
	}
	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	public int getIsday() {
		return isday;
	}
	public void setIsday(int isday) {
		this.isday = isday;
	}
	public ReportService getReportService() {
		return reportService;
	}


	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
}
