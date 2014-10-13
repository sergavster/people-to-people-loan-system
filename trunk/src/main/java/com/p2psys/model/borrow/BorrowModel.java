package com.p2psys.model.borrow;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.BorrowConfig;
import com.p2psys.domain.Collection;
import com.p2psys.domain.Repayment;
import com.p2psys.domain.Tender;
import com.p2psys.domain.User;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.quartz.JobQueue;
import com.p2psys.tool.Page;
import com.p2psys.tool.interest.InterestCalculator;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

public class BorrowModel extends Borrow implements SqlQueryable{
	
	private static final long serialVersionUID = 6227166783859660460L;
	//借款人姓名
	private String username;
	//投标的金额
	private double money;
	//支付密码
	private String paypassword;
	
	private SearchParam param;

	private String searchkeywords;
	
	private int pageStart;
	
	private int pageNum=Page.ROWS;
	
	private Page pager;
	
	private long borrowid;
	
	protected int borrowType=Constant.TYPE_ALL;
	
	private BorrowConfig config;
	
	private double borrow_fee;
	private boolean isNeedBorrowFee;
	
	private String search;
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	
	public List<Repayment> repaymentList;
	
	public List<Collection> collectionList;
	
	
	public BorrowModel() {
		super();
	}
	/*setter and getter*/
	public double getMoney() {
		return money;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	public String getPaypassword() {
		return paypassword;
	}
	public void setPaypassword(String paypassword) {
		this.paypassword = paypassword;
	}
	public SearchParam getParam() {
		return param;
	}
	public void setParam(SearchParam param) {
		this.param = param;
	}
	public String getSearchkeywords() {
		return searchkeywords;
	}
	public void setSearchkeywords(String searchkeywords) {
		this.searchkeywords = searchkeywords;
	}
	public int getPageStart() {
		return pageStart;
	}
	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public Page getPager() {
		return pager;
	}
	public void setPager(Page pager) {
		this.pager = pager;
	}
	public long getBorrowid() {
		return borrowid;
	}
	public void setBorrowid(long borrowid) {
		this.borrowid = borrowid;
	}
	public int getBorrowType() {
		return borrowType;
	}
	public void setBorrowType(int borrowType) {
		this.borrowType = borrowType;
	}
	
	public BorrowConfig getConfig() {
		return config;
	}
	public void setConfig(BorrowConfig config) {
		this.config = config;
	}
	public double getBorrow_fee() {
		return borrow_fee;
	}
	public void setBorrow_fee(double borrow_fee) {
		this.borrow_fee = borrow_fee;
	}
	public boolean isNeedBorrowFee() {
		return isNeedBorrowFee;
	}
	public void setNeedBorrowFee(boolean isNeedBorrowFee) {
		this.isNeedBorrowFee = isNeedBorrowFee;
	}
	/**
	 * 封装pager的每页的个数
	 * @param num
	 */
	public void fillPageNum(int num){
		Page p = new Page();
		p.setStart(0);
		p.setPernum(num);
		this.setPager(p);
	}
	
	@Override
	public String getTypeSql() {
		String typeSql = "";
		if ( getType() >100 ) {
			typeSql = "and p1.type =" + getType();
		}
		return typeSql;
	}
	//  v1.6.6.1 RDPROJECT-252  wcw 2013-09-30 start
	@Override
	public String getStatusSql() {
		String statusSql = " ";
		switch (getStatus()) {
		case 0:
			statusSql=" ";
			break;
		case 1:
			statusSql=" and  (p1.status=1 and ((p1.account>p1.account_yes+0 and p1.valid_time*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+") or (p1.type=" + Constant.TYPE_FLOW + " and p1.account>p1.account_yes+0) ))";
			break;
		case 2:
			statusSql=" and (p1.status=1 and p1.type!=" + Constant.TYPE_FLOW + " and (p1.account=p1.account_yes+0 and p1.valid_time*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+"))";
			break;
		case 3:
			statusSql=" and p1.status=3";
			break;
		case 4:
			statusSql=" and (p1.status=4 or p1.status=49)";
			break;
		case 5:
			statusSql=" and (p1.status=5 or p1.status=59)";
			break;
		case 6:
			statusSql=" and p1.status=6";
			break;
		case 7:
			statusSql=" and p1.status=7";
			break;
		case 8:
			statusSql=" and p1.status=8";
			break;
		case 9://流标状态
			statusSql=" and p1.status=1 and p1.account>p1.account_yes+0 and p1.verify_time+p1.valid_time*24*60*60 <"+ DateUtils.getTime(new Date());
			break;
		case 10: //还款中..
			// v1.6.6.2 RDPROJECT-311 wcw 2013-10-21 start
			statusSql=" and ((p1.status in (3,6,7) and p1.type!=" + Constant.TYPE_FLOW + ") or (p1.status in(1,3,6,7,8) and p1.type=" + Constant.TYPE_FLOW + " and p1.account_yes>0.0 and p1.repayment_account-p1.repayment_yesaccount>0.0)) ";
			// v1.6.6.2 RDPROJECT-311 wcw 2013-10-21 end
			break;
		case 11: //首页显示状态
			//statusSql=" and ((((p1.status=1 and p1.valid_time*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+") " +
			//		" or (p1.status in (3,6,7,8) and p1.is_mb!=1)) " +
			//		"and p1.is_flow!=1 ) or (p1.is_flow=1 and p1.status in (1,3,6,7))) ";
			// v1.6.6.2 RDPROJECT-311 wcw 2013-10-21 start
			statusSql=" and ((((p1.status=1 and p1.valid_time*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+") " +
					" or (p1.status in (3,6,7,8) and p1.type!=" + Constant.TYPE_SECOND + ")) " +
					"and p1.type!=" + Constant.TYPE_FLOW + " ) or (p1.type=" + Constant.TYPE_FLOW + " and p1.status in (1,8))) ";
			// v1.6.6.2 RDPROJECT-311 wcw 2013-10-21 end
			break;
		case 14://美贷首页显示状态
			/*statusSql=" and ((((p1.status=1 and link.value*24*60*60+p1.addtime>"+DateUtils.getNowTimeStr()+") " +
					") " +
					"and p1.is_flow!=1 ) or (p1.is_flow=1 and p1.account_yes<p1.account+0 and p1.status in (1,3,6,7))) ";*/
			statusSql=" and  (p1.status in(1,3,6,7,8) and ((1=1) " +
					"or (p1.type=" + Constant.TYPE_FLOW + ")))";
			break;
		case 12://成功状态
			// v1.6.6.2 RDPROJECT-311 wcw 2013-10-21 start
		      statusSql = " and ((p1.status in (3,6,7,8) and p1.type!=" + Constant.TYPE_FLOW + ") or (p1.status in(1,3,6,7,8) and p1.type=" + Constant.TYPE_FLOW + "  and p1.repayment_account-p1.repayment_yesaccount=0.0))";
			// v1.6.6.2 RDPROJECT-311 wcw 2013-10-21 start
		      break;
		case 13://中融资本首页显示
			statusSql=" and  (p1.status=1 and ((p1.account>p1.account_yes+0 and p1.valid_time*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+") " +
					"or (p1.type=" + Constant.TYPE_FLOW + " and p1.account>p1.account_yes+0)))";
		      break;  
		case 15://信达财富首页显示
			statusSql=" and  (p1.status=1 and ((p1.account>p1.account_yes+0 and p1.valid_time*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+") " +
					"or (p1.type=" + Constant.TYPE_FLOW + " and p1.account>p1.account_yes+0)))";
		      break;  
		case 19: //流标状态
			statusSql=" and (p1.status=1 and (p1.account=p1.account_yes+0 and p1.valid_time*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+"))";
			break;
		case 20: //等待复审状态
			statusSql=" and p1.status=1 and (p1.account_yes+0)=(p1.account+0) and p1.type<>"+Constant.TYPE_FLOW+"";
			break;
		case 21:
			statusSql=" and p1.repayment_time";
			break;
		case 22:
			statusSql=" and ((repay.repayment_time<"+DateUtils.getNowTimeStr()+" and repay.repayment_yestime is  null) or (repay.repayment_yestime is not null and repay.repayment_yestime>repay.repayment_time))" ;
					
		    break;
		case 23:
			statusSql=" and p1.status=1 and link.value*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+" and p1.type!=" + Constant.TYPE_FLOW + "  ";
		    break;
		case 24:
			statusSql=" and (((" +
					" (p1.status in (3,6,7,8) and p1.type!=" + Constant.TYPE_SECOND + ")) " +
					"and p1.type!=" + Constant.TYPE_FLOW + " ) or (p1.type=" + Constant.TYPE_FLOW + " and p1.status in (1,3,6,7))) ";
		    break;
		case 25: //在线金融首页查询推荐标
			statusSql=" and ((((p1.status=1 and link.value*24*60*60+p1.verify_time>"+DateUtils.getNowTimeStr()+") " +
					" or (p1.status in (3,6,7,8) and p1.type!=" + Constant.TYPE_SECOND + ")) " +
					"and p1.type!=" + Constant.TYPE_FLOW + " ) or (p1.type=" + Constant.TYPE_FLOW + " and p1.status in (1,3,6,7))) and p1.is_recommend=1";
			break;
		default:
			throw new RuntimeException("不正确的状态:"+getStatus());
		}
		return statusSql;
	}
//  v1.6.6.1 RDPROJECT-252  wcw 2013-09-30 end
	@Override
	public String getOrderSql() {
		// v1.6.7.1 RDPROJECT-141 xx 2013-11-13 start
		SearchParam p = getParam();
		if(p!=null && !StringUtils.isBlank(p.getIndexOrderStr())){//首页排序
			return p.getIndexOrderStr();
		}else if(p!=null){
			return p.getOrderSql();
		}
		return "";
		// v1.6.7.1 RDPROJECT-141 xx 2013-11-13 end
	}
	@Override
	public String getSearchParamSql() {
		return getParam().getSearchParamSql();
	}
	@Override
	public String getLimitSql() {
		int borrownum=NumberUtils.getInt(Global.getValue("index_borrownum"));
		int xinborrownum=NumberUtils.getInt(Global.getValue("fast_borrownum"));
		if(this.pager==null){
			// 中海财富首页要显示三条信用标，六条抵押标
			if(Global.getWebid().equals("zhcf")){
			   if(getBorrowType()==103){
				    return " limit 0," + xinborrownum + "";
			   }	
			}
			return " limit 0,"+borrownum+" ";
		}else{
			return  " limit " + this.getPager().getStart() + "," +this.getPager().getPernum();
		}
	}
	@Override
	public String getPageStr(Page p) {
		StringBuffer sb=new StringBuffer();
		int currentPage=p.getCurrentPage();
		int[] dispayPage=new int[5];
		if(p.getPages()<5){
			dispayPage=new int[p.getPages()];
			for(int i=0;i<dispayPage.length;i++){
				dispayPage[i]=i+1;
			}
		}else{
			if(currentPage<3){
				for(int i=0;i<5;i++){
					dispayPage[i]=i+1;
				}
			}else if(currentPage>p.getPages()-2){
				for(int i=0;i<5;i++){
					dispayPage[i]=p.getPages()-4+i;
				}
			}else{
				for(int i=0;i<5;i++){
					dispayPage[i]=currentPage-2+i;
				}
			}
		}
		String statusstr="&status="+getStatus();
		String searchstr=getSerachStr();
		String orderstr="&order="+getOrder();
		String paramStr=statusstr+searchstr+orderstr;
		//输出分页信息
		for(int i=0;i<dispayPage.length;i++){
			if(dispayPage[i]==currentPage){
				sb.append(" <span class='this_page'>"+currentPage+"</span>");
			}else{
				sb.append(" <a href='index.html?page="+dispayPage[i]+paramStr+"'>"+dispayPage[i]+"</a>");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String getSerachStr(){
		StringBuffer sb=new StringBuffer();
		SearchParam param=getParam();
		if(param!=null){
			if(!StringUtils.isBlank(param.getUse())){
				sb.append("&use="+param.getUse());
			}
			if(!StringUtils.isBlank(param.getLimittime())){
				sb.append("&limittime="+param.getLimittime());
			}
			if(!StringUtils.isBlank(param.getKeywords())){
				sb.append("&keywords="+StringUtils.isNull(param.getKeywords())+"");
			}
		}else{
			sb.append("");
		}
		return sb.toString();
	}
	
	@Override
	public String getOrderStr() {
		StringBuffer sb=new StringBuffer("");
		sb.append(getCurOrderStr(-1,"金额"));
		sb.append(getCurOrderStr(-2,"利率"));
		sb.append(getCurOrderStr(-3,"进度"));
		sb.append(getCurOrderStr(-4,"信用"));
		return sb.toString();
	}
	
	public String getCurOrderStr(int ordertype,String text){
		StringBuffer sb=new StringBuffer();
		sb.append("<span><a  class=\'searchbtn'\' href=\"?status="+getStatus()+"&search="+getSearch()+"&time_limit="+getTime_limit()+"&type="+getType()+"&pageNum="+getPageNum());
		if(Math.abs(getOrder()) == Math.abs(ordertype)){
			if(getOrder()>0){
				sb.append("&order="+(-getOrder())+"\">");
				sb.append("<font color=\"#FF0000\">"+text+"↑</font>");
			}else{
				sb.append("&order="+(Math.abs(getOrder()))+"\">");
				sb.append("<font color=\"#FF0000\">"+text+"↓</font>");
			}
		}else{
			sb.append("&order="+(ordertype)+"\">");
			sb.append(text);
		}
		sb.append("</a> </span>");
		return sb.toString();
	}
	/**
	 * 是否跳过初审
	 */
	public void skipTrial(){
		
	}
	/**
	 * 是否跳过满表复审
	 */
	public void skipReview(){
		
	}
	public void skipStatus(){
		
	}
	public void verify(int status,int verifyStatus){
		
	}
	public BorrowModel getModel(){
		return null;
	}
	
	/**
	 * 计算借款标的利息
	 * @return
	 */
	public double calculateInterest(){
		return 0.0;
	}
	/**
	 * 计算借款标的利息
	 * @return
	 */
	public double calculateInterest(double validAccount){return 0.0;}
	
	public InterestCalculator interestCalculator(){
		return null;
	}
	
	public InterestCalculator interestCalculator(double validAccount){return null;}
	
	/**
	 * 计算借款标的手续费
	 * @return
	 */
	public double calculateBorrowFee(){return 0.0;}
	
	public double calculateBorrowAward(){return 0.0;}
	
	public Repayment[] getRepayment(){return null;}
	
	public Repayment getFlowRepayment(){return null;};
	
	public Date getRepayTime(int period){
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(0);
		return cal.getTime();
	}
	
	public boolean isNeedRealName(){return true;}
	public boolean isNeedVIP() {return true;}
	public boolean isNeedEmail() {return true;}
	public boolean isNeedPhone() {return true;}
	public boolean isNeedVideo() {return true;}
	public boolean isNeedScene() {return true;}
	
	public boolean checkIdentify(User u) {throw new BorrowException("借款标的配置出错！");}
	
	public boolean checkModelData() {throw new BorrowException("借款标的配置出错！");}
	public boolean checkModelData(RuleModel rule) {throw new BorrowException("借款标的配置出错！");}

	public boolean isLastPeriod(int order){return false;}
	
	public double getManageFee(){return 0;};
	public double getManageFee(double account){return 0;}
	/**
	 * 招商贷需求，新增一种手续类似的管理费，一次性收取，无关期限
	 * @return
	 */
	public double getTransactionFee(){return 0;}
	/**
	 * 计算奖励情况
	 * @return
	 */
	public double calculateAward(){return 0;}
	public double calculateAward(double account){return 0;}
	public Date getFlowRepayTime(int period) {
		return null;
	}
	//v1.6.7.1 RDPROJECT-461 wcw 2013-11-21 start
	public boolean allowFullSuccess() {
		return true;
	}
	//v1.6.7.1 RDPROJECT-461 wcw 2013-11-21 end
	
	public List<Collection> createCollectionList(Tender tender,InterestCalculator ic){
		return null;
	}
	
	public String calCollectionRepayTime(Tender tender, int period) {
		return "";
	}
	public void prepareTender(Tender tender) {}
	public double validAccount(Tender tender){
		return 0;
	}
	
	/**
	 * 投标校验
	 * @param tender
	 */
	public void checkTender(Tender tender){
		
	}
	
	public void tenderSuccess(Tender tender,InterestCalculator ic) {
	}
	
	public List<Repayment> createFlowRepaymentList(List<Collection> clist) {
		return null;
	}
	
	/**
	 * 满标复审通过后，处理tender、collection
	 */
	public void handleTenderAfterFullSuccess(){
		
	}
	/**
	 * 满标复审通过后，处理borrow、repayment
	 */
	public void handleBorrowAfterFullSuccess(){
		
	}
	/**
	 * 满标复审未通过后，处理tender
	 */
	public void handleTenderAfterFullFail(){
		
	}
	
	/**
	 * 投标后立即生息，流转标
	 */
	public void immediateInterestAfterTender(Tender tender){
		
	}
	
	/**
	 * 投完标后立即还款，秒标、提前还息的几个
	 */
	public void immediateRepayAfterTender(){
		
	}
	
	public void immediateInviteAwardAfterTender(){
		
	}
	
	/**
	 * 计算展期利息
	 * @param repay
	 * @return
	 */
	public double calExtensionInterest(Repayment repay){
		return 0;
	}
	
	/**
	 * 还款 处理借款人资金
	 * @param repay
	 */
	public void borrowRepayHandleBorrow(Repayment repay, double lateInterest, double repayLateInterest){
		
	}
	
	/**
	 * 还款 处理投资人资金
	 * @param repay
	 */
	public void borrowRepayHandleTender(Repayment repay, double repayLateInterest){

	}
	
	/**
	 * 还款 处理利息及利息管理费
	 * @param c
	 * @param t
	 * @return
	 */
	public DetailCollection getInvestRepayInterest(DetailCollection c, Tender t){
		return null;
	}
	
	
	/**
	 * 流转标 还款
	 * @param c
	 */
	public void flowBorrowRepay(DetailCollection c){
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getId() == 0) ? 0 : (int)this.getId());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(obj instanceof Borrow){
			BorrowModel thisModel=this.getModel();
			BorrowModel borrowModel=(BorrowModel)obj;
			if(thisModel==null){
				if(this.getId()<=0) return false;
				if(this.getId()==borrowModel.getId()) return true;
			}else{
				return thisModel.equals(borrowModel.getModel());
			}
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		JobQueue<BorrowModel> queue=new JobQueue<BorrowModel>();
		BorrowModel b1=new BorrowModel();
		b1.setId(1);
		BorrowModel b2=new BorrowModel();
		b2.setId(1);
		
		BorrowModel w1=new BaseBorrowModel(b1);
		BorrowModel w2=new BaseBorrowModel(new BorrowModel());
		
		queue.offer(w1);
		queue.offer(w2);
		System.out.println(queue.size());
	}
	
}
