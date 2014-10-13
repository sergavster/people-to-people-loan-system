package com.p2psys.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Global;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

public class SearchParam {
	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	private String search="";
	private String use;
	private String limittime;
	private String keywords;

	private String dotime1;
	private String dotime2;
	
	private String succtime1;
	private String succtime2;
	
	// 2013/08/27 zhengzhiai add end 满标复审时间
	private String fullVerifytime1;
	private String fullVerifytime2;

	private String account_type;

	// v1.6.7.2 RDPROJECT-603 xx 2013-12-17 start
	private long user_id;
	// v1.6.7.2 RDPROJECT-603 xx 2013-12-17 end
	private String username;
	private String status;
	private String realname;
	private String email;
	private String kefu_username;
	private String invite_username;
	protected String qq;
	private String vip_status;
	private String real_status;//实名认证状态
	private String email_status;//邮箱认证状态
	private String phone_status;//认证状态
	private String phone;
	private String typename;
	private String repayStatus;//repayment的状态
	//用户操作日志
	private String invite_userid;
	private String card_id;
	private String query;
	private String url;
	private String result;
	private String paymentname;
	private String audit_user;
	private String account;
	private String limit;
	private String apr;
	
	private String trade_no;
	private String payment;
	private String userPhone;
	private String borrow_style;
	private String verify_user;
	
	//及时雨还款时间段设置
	private String repayment_time1;
	private String repayment_time2;
	// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
	// 已还款时间（repayment表）
	private String repayment_yestime1;
	private String repayment_yestime2;
	// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
	//已还款时间
	private String repay_yestime1;
	private String repay_yestime2;

	private String user_type;
    //及时雨委托协议类型
	private String protocol_type;
	//抽奖时间
	private String gmt_create1;
	//推荐标
    private String recommend;
    //vip赠送审核
    private String vip_give_status;
    //抽奖用户名
    private String user_name;
    
    private String huikuan_use;
  //v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
  	private String verify_start_time;
  	private String verify_end_time;
  	private int verify_type;
  	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
  	//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 start
  	private String rule_id;
  //v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 end
  //v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
   
  	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 start
  	private String invite_startTime;
  	private String invite_endTime;
    public String getInvite_startTime() {
		return invite_startTime;
	}

	public void setInvite_startTime(String invite_startTime) {
		this.invite_startTime = invite_startTime;
	}

	public String getInvite_endTime() {
		return invite_endTime;
	}

	public void setInvite_endTime(String invite_endTime) {
		this.invite_endTime = invite_endTime;
	}

	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 end
	
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-16 start
	private String startDate;
	private String endDate;
	/**
	 * 积分值
	 */
	private String credit_value;
	/**
	 * 商品分类ID
	 */
	private String category_id;
	/**
	 * 积分范围  供页面选择
	 */
	private String credit_type;
  	public String getCredit_type() {
		return credit_type;
	}

	public void setCredit_type(String credit_type) {
		this.credit_type = credit_type;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCredit_value() {
		return credit_value;
	}

	public void setCredit_value(String credit_value) {
		this.credit_value = credit_value;
	}

	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-16 end
	
	public String getVerify_start_time() {
  		return verify_start_time;
  	}
  	public void setVerify_start_time(String verify_start_time) {
  		this.verify_start_time = verify_start_time;
  	}
  	public String getVerify_end_time() {
  		return verify_end_time;
  	}
  	public void setVerify_end_time(String verify_end_time) {
  		this.verify_end_time = verify_end_time;
  	}
  	public int getVerify_type() {
  		return verify_type;
  	}
  	public void setVerify_type(int verify_type) {
  		this.verify_type = verify_type;
  	}
  	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
    /**
     * 奖励类型
     */
    private String awardType;
    
    /**
     * 奖励状态
     */
    private String receive_status;
    
	public String getHuikuan_use() {
		return huikuan_use;
	}

	public void setHuikuan_use(String huikuan_use) {
		this.huikuan_use = huikuan_use;
	}

	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @return the vip_give_status
	 */
	public String getVip_give_status() {
		return vip_give_status;
	}

	/**
	 * @param vip_give_status the vip_give_status to set
	 */
	public void setVip_give_status(String vip_give_status) {
		this.vip_give_status = vip_give_status;
	}
	// 2013/08/13 linhuimin add start
	// dw_borrow_collection表的应还时间
	/** 应还开始时间 */
	private String repay_time1;
	/** 应还结束时间 */
	private String repay_time2;

	/**
	 * 获取repay_time1
	 * 
	 * @return repay_time1
	 */
	public String getRepay_time1() {
		return repay_time1;
	}

	/**
	 * 设置repay_time1
	 * 
	 * @param repay_time1 要设置的repay_time1
	 */
	public void setRepay_time1(String repay_time1) {
		this.repay_time1 = repay_time1;
	}

	/**
	 * 获取repay_time2
	 * 
	 * @return repay_time2
	 */
	public String getRepay_time2() {
		return repay_time2;
	}

	/**
	 * 设置repay_time2
	 * 
	 * @param repay_time2 要设置的repay_time2
	 */
	public void setRepay_time2(String repay_time2) {
		this.repay_time2 = repay_time2;
	}

	// 2013/08/13 linhuimin add end
	//文章标题
    private String articlename;
    private String site_id;
    
    // v1.6.6.2 RDPROJECT-116 zza 2013-10-29 start
	/**
	 * 发布时间
	 */
	private String publish1;
	
	/**
	 * 发布时间
	 */
	private String publish2;
	// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 end

	public String getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getArticlename() {
		return articlename;
	}

	public void setArticlename(String articlename) {
		this.articlename = articlename;
	}

	/**
	 * 获取publish1
	 * 
	 * @return publish1
	 */
	public String getPublish1() {
		return publish1;
	}

	/**
	 * 设置publish1
	 * 
	 * @param publish1 要设置的publish1
	 */
	public void setPublish1(String publish1) {
		this.publish1 = publish1;
	}

	/**
	 * 获取publish2
	 * 
	 * @return publish2
	 */
	public String getPublish2() {
		return publish2;
	}

	/**
	 * 设置publish2
	 * 
	 * @param publish2 要设置的publish2
	 */
	public void setPublish2(String publish2) {
		this.publish2 = publish2;
	}

	public String getGmt_create1() {
		return gmt_create1;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setGmt_create1(String gmt_create1) {
		this.gmt_create1 = gmt_create1;
	}

	public String getGmt_create2() {
		return gmt_create2;
	}

	public void setGmt_create2(String gmt_create2) {
		this.gmt_create2 = gmt_create2;
	}
	private String gmt_create2;
	
	public String getProtocol_type() {
		return protocol_type;
	}

	public void setProtocol_type(String protocol_type) {
		this.protocol_type = protocol_type;
	}
	//用户类型
	private String user_typeid;
	
	//是否具有奖励
	private String is_award;
	//金额范围
	private String account_min;
	private String account_max;
	private String cashTotal_min;
	private String cashTotal_max;
	
	//自动投标参数设置
	private int timelimit_month_first;
	private int timelimit_month_last;
	private int timelimit_day_first;
	private int timelimit_day_last;
	private int apr_first;
	private int apr_last;
	private double award_first;
	
	//奖励给予者
	private String passive_username;
	public String getPassive_username() {
		return passive_username;
	}

	public void setPassive_username(String passive_username) {
		this.passive_username = passive_username;
	}

	public int getTimelimit_month_first() {
		return timelimit_month_first;
	}

	public void setTimelimit_month_first(int timelimit_month_first) {
		this.timelimit_month_first = timelimit_month_first;
	}

	public int getTimelimit_month_last() {
		return timelimit_month_last;
	}

	public void setTimelimit_month_last(int timelimit_month_last) {
		this.timelimit_month_last = timelimit_month_last;
	}

	public int getTimelimit_day_first() {
		return timelimit_day_first;
	}

	public void setTimelimit_day_first(int timelimit_day_first) {
		this.timelimit_day_first = timelimit_day_first;
	}

	public int getTimelimit_day_last() {
		return timelimit_day_last;
	}

	public void setTimelimit_day_last(int timelimit_day_last) {
		this.timelimit_day_last = timelimit_day_last;
	}

	public int getApr_first() {
		return apr_first;
	}

	public void setApr_first(int apr_first) {
		this.apr_first = apr_first;
	}

	public int getApr_last() {
		return apr_last;
	}

	public void setApr_last(int apr_last) {
		this.apr_last = apr_last;
	}


	public double getAward_first() {
		return award_first;
	}

	public void setAward_first(double award_first) {
		this.award_first = award_first;
	}

	public String getCashTotal_min() {
		return cashTotal_min;
	}

	public void setCashTotal_min(String cashTotal_min) {
		this.cashTotal_min = cashTotal_min;
	}

	public String getCashTotal_max() {
		return cashTotal_max;
	}

	public void setCashTotal_max(String cashTotal_max) {
		this.cashTotal_max = cashTotal_max;
	}

	public String getUser_typeid() {
		return user_typeid;
	}

	public void setUser_typeid(String user_typeid) {
		this.user_typeid = user_typeid;
	}
	//审核用户名查询
	private String verify_username;
	
	

	public String getVerify_username() {
		return verify_username;
	}

	public void setVerify_username(String verify_username) {
		this.verify_username = verify_username;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getRepayment_time1() {
		return repayment_time1;
	}

	public void setRepayment_time1(String repayment_time1) {
		this.repayment_time1 = repayment_time1;
	}

	public String getRepayment_time2() {
		return repayment_time2;
	}

	public void setRepayment_time2(String repayment_time2) {
		this.repayment_time2 = repayment_time2;
	}

	/**
	 * 获取repayment_yestime1
	 * 
	 * @return repayment_yestime1
	 */
	public String getRepayment_yestime1() {
		return repayment_yestime1;
	}

	/**
	 * 设置repayment_yestime1
	 * 
	 * @param repayment_yestime1 要设置的repayment_yestime1
	 */
	public void setRepayment_yestime1(String repayment_yestime1) {
		this.repayment_yestime1 = repayment_yestime1;
	}

	/**
	 * 获取repayment_yestime2
	 * 
	 * @return repayment_yestime2
	 */
	public String getRepayment_yestime2() {
		return repayment_yestime2;
	}

	/**
	 * 设置repayment_yestime2
	 * 
	 * @param repayment_yestime2 要设置的repayment_yestime2
	 */
	public void setRepayment_yestime2(String repayment_yestime2) {
		this.repayment_yestime2 = repayment_yestime2;
	}

	public String getVerify_user() {
		return verify_user;
	}

	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	private String recharge_kefu_username;

	

	public String getRecharge_kefu_username() {
		return recharge_kefu_username;
	}

	public void setRecharge_kefu_username(String recharge_kefu_username) {
		this.recharge_kefu_username = recharge_kefu_username;

	}

	public String getBorrow_style() {
		return borrow_style;
	}

	public void setBorrow_style(String borrow_style) {
		this.borrow_style = borrow_style;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

	public String getAudit_user() {
		return audit_user;
	}

	public void setAudit_user(String audit_user) {
		this.audit_user = audit_user;
	}

	public String getPaymentname() {
		return paymentname;
	}

	public void setPaymentname(String paymentname) {
		this.paymentname = paymentname;
	}

	public String getAccount_min() {
		return account_min;
	}

	public void setAccount_min(String account_min) {
		this.account_min = account_min;
	}

	public String getAccount_max() {
		return account_max;
	}

	public void setAccount_max(String account_max) {
		this.account_max = account_max;
	}
	private int[] statusArray;
	
	private int order;
	// v1.6.7.1 RDPROJECT-141 xx 2013-11-13 start
	private String indexOrderStr;//首页排序
	// v1.6.7.1 RDPROJECT-141 xx 2013-11-13 end
	private String type;
	//V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 start 
	//银行id
	private String bank_id;
	//精确搜索用户名
	private  String  search_type;
	

	public String getSearch_type() {
		return search_type;
	}

	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}
	//V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 end 

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getInvite_userid() {
		return invite_userid;
	}
	public void setInvite_userid(String invite_userid) {
		this.invite_userid = invite_userid;
	}
	public String getInvite_username() {
		return invite_username;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getVip_status() {
		return vip_status;
	}

	public void setVip_status(String vip_status) {
		this.vip_status = vip_status;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getIs_award() {
		return is_award;
	}

	public void setIs_award(String is_award) {
		this.is_award = is_award;
	}
	Map map = new HashMap();

	

	public SearchParam() {
		super();
	}
	
	public SearchParam(String use, String limittime, String keywords) {
		super();
		this.use = use;
		this.limittime = limittime;
		this.keywords = keywords;
	}
	
	public SearchParam(String use, String limittime, String keywords,
			long user_id) {
		super();
		this.use = use;
		this.limittime = limittime;
		this.keywords = keywords;
	}
	
	public String getKefu_username() {
		return kefu_username;
	}

	public void setKefu_username(String kefu_username) {
		this.kefu_username = kefu_username;
	}

	public String getUse() {
		return use;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getLimittime() {
		return limittime;
	}

	public void setLimittime(String limittime) {
		this.limittime = limittime;
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

	public String getAccount_type() {
		return account_type;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public void setInvite_username(String invite_username) {
		this.invite_username = invite_username;
	}
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getIndexOrderStr() {
		return indexOrderStr;
	}

	public void setIndexOrderStr(String indexOrderStr) {
		this.indexOrderStr = indexOrderStr;
	}

	public int[] getStatusArray() {
		return statusArray;
	}

	public void setStatusArray(int[] statusArray) {
		this.statusArray = statusArray;
	}
	public String getSucctime1() {
		return succtime1;
	}
	public void setSucctime1(String succtime1) {
		this.succtime1 = succtime1;
	}

	public String getSucctime2() {
		return succtime2;
	}

	public void setSucctime2(String succtime2) {
		this.succtime2 = succtime2;
	}

	/**
	 * 获取awardType
	 * 
	 * @return awardType
	 */
	public String getAwardType() {
		return awardType;
	}

	/**
	 * 设置awardType
	 * 
	 * @param awardType 要设置的awardType
	 */
	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	/**
	 * 获取receive_status
	 * 
	 * @return receive_status
	 */
	public String getReceive_status() {
		return receive_status;
	}

	/**
	 * 设置receive_status
	 * 
	 * @param receive_status 要设置的receive_status
	 */
	public void setReceive_status(String receive_status) {
		this.receive_status = receive_status;
	}

	/**
	 * 获取fullVerifytime1
	 * 
	 * @return fullVerifytime1
	 */
	public String getFullVerifytime1() {
		return fullVerifytime1;
	}

	/**
	 * 设置fullVerifytime1
	 * 
	 * @param fullVerifytime1 要设置的fullVerifytime1
	 */
	public void setFullVerifytime1(String fullVerifytime1) {
		this.fullVerifytime1 = fullVerifytime1;
	}

	/**
	 * 获取fullVerifytime2
	 * 
	 * @return fullVerifytime2
	 */
	public String getFullVerifytime2() {
		return fullVerifytime2;
	}

	/**
	 * 设置fullVerifytime2
	 * 
	 * @param fullVerifytime2 要设置的fullVerifytime2
	 */
	public void setFullVerifytime2(String fullVerifytime2) {
		this.fullVerifytime2 = fullVerifytime2;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	/**
	 * 将所有的参数信息封装成Map
	 * @return
	 */
	public Map toMap(){
		map.put("search", "");
		map.put("use", use);
		map.put("time_limit", limittime);
		map.put("keywords", keywords);
		map.put("dotime1", dotime1);
		map.put("dotime2", dotime2);
		map.put("succtime1", succtime1);
		map.put("succtime2", succtime2);
		map.put("fullVerifytime1", fullVerifytime1);
		map.put("fullVerifytime2", fullVerifytime2);
		map.put("type", type);
		map.put("trade_no", trade_no);
		map.put("payment", payment);
		map.put("order", order+"");
		map.put("is_award", is_award);
		map.put("cashTotal_min", cashTotal_min);
	    map.put("cashTotal_max", cashTotal_max);
	    if(!StringUtils.isBlank(recommend)){
	        map.put("recommend", recommend);
	    }
	    if(!StringUtils.isBlank(gmt_create1)){
		    map.put("gmt_create1", gmt_create1);
		}
	    if(!StringUtils.isBlank(gmt_create2)){
		    map.put("gmt_create2", gmt_create2);
		}
	    if(timelimit_month_first!=0){
	    map.put("timelimit_month_first", timelimit_month_first);
	    }
	    if(timelimit_month_last!=0){
	    map.put("timelimit_month_last", timelimit_month_last);
	    }
	    if(timelimit_day_first!=0){
	    map.put("timelimit_day_first", timelimit_day_first);
	    }
	    if(timelimit_day_last!=0){
	    map.put("timelimit_day_last", timelimit_day_last);
	    }
	    if(apr_first!=0){
	    map.put("apr_first", apr_first);
	    }
	    if(apr_last!=0){
	    map.put("apr_last", apr_last);
	    }
	    if(award_first!=0){
	    map.put("award_first", award_first);
	    }
		if(account_type!=null&&!account_type.equals("0")){
			map.put("account_type", account_type);	
		}
		if(protocol_type!=null){
			map.put("protocol_type", protocol_type);	
		}
		if(!StringUtils.isBlank(borrow_style)){
			map.put("borrow_style",  borrow_style);
		}
		if(audit_user!=null&&!audit_user.equals("0")){
			map.put("audit_user", audit_user);	
		}
		if(paymentname!=null&&!paymentname.equals("0")){
			map.put("paymentname", paymentname);	
		}
		if(!StringUtils.isBlank(username)){
			map.put("username", username);
		}
		if(!StringUtils.isBlank(user_name)){
			map.put("user_name", user_name);
		}
		if(!StringUtils.isBlank(account)){
			map.put("account", account);
		}
		if(!StringUtils.isBlank(apr)){
			map.put("apr", apr);
		}
		if(!StringUtils.isBlank(limit)){
			map.put("limit", limit);
		}
		if(!StringUtils.isBlank(repayment_time1)){
			map.put("repayment_time1", repayment_time1);
		}
		if(!StringUtils.isBlank(repayment_time2)){
			map.put("repayment_time2", repayment_time2);
		}
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		if(!StringUtils.isBlank(repayment_yestime1)){
			map.put("repayment_yestime1", repayment_yestime1);
		}
		if(!StringUtils.isBlank(repayment_yestime2)){
			map.put("repayment_yestime2", repayment_yestime2);
		}
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
		if(!StringUtils.isBlank(repay_yestime1)){
			map.put("repay_yestime1", repay_yestime1);
		}
		if(!StringUtils.isBlank(repay_yestime2)){
			map.put("repay_yestime2", repay_yestime2);
		}
		if(!StringUtils.isBlank(status)){
			int statusInt=NumberUtils.getInt(status);
			if(statusInt>=0){
				map.put("status", status);
			}
		}
		if(!StringUtils.isBlank(vip_status)){
			int statusInt=NumberUtils.getInt(vip_status);
			if(statusInt>=0){
				map.put("vip_status", vip_status);
			}
		}
		if(!StringUtils.isBlank(recharge_kefu_username)){
			
				map.put("recharge_kefu_username", recharge_kefu_username);
		}
		if(!StringUtils.isBlank(user_type)){
		  map.put("user_type", user_type);
		 }
		if(!StringUtils.isBlank(verify_username)){
			  map.put("verify_username", verify_username);
			 }
		map.put("realname", realname);
		map.put("email", email);
		map.put("kefu_name", kefu_username);
		if(!StringUtils.isBlank(invite_username)){
			map.put("invite_username", invite_username);
		}
		// 2013/08/13 linhuimin add start 待还款流转标增加应还时间查询
		if(!StringUtils.isBlank(repay_time1)){
			map.put("repay_time1", repay_time1);
		}
		if(!StringUtils.isBlank(repay_time2)){
			map.put("repay_time2", repay_time2);
		}
		// 2013/08/13 linhuimin add end
		if(!StringUtils.isBlank(articlename)){
			map.put("articlename", articlename);
		}
		if(!StringUtils.isBlank(site_id)){
			map.put("site_id", site_id);
		}
		// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 start 
		if(!StringUtils.isBlank(publish1)){
			map.put("publish1", publish1);
		}
		if(!StringUtils.isBlank(publish2)){
			map.put("publish2", publish2);
		}
		// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 end
		//vip赠送时间状态
		if(!StringUtils.isBlank(vip_give_status)){
			map.put("vip_give_status", vip_give_status);
		}
		if(!StringUtils.isBlank(huikuan_use)){
			map.put("huikuan", huikuan_use);
		}
		if(!StringUtils.isBlank(awardType)){
			map.put("awardType", awardType);
		}
		if(!StringUtils.isBlank(receive_status)){
			map.put("receive_status", receive_status);
		}
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 start
		if(!StringUtils.isBlank(repayStatus)){
			map.put("repayStatus", repayStatus);
		}
		// v1.6.6.1 RDPROJECT-190 xx 2013-09-27 end
		//V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 start 
		if(!StringUtils.isBlank(bank_id)){
			map.put("bank_id", bank_id);
		}
		if(!StringUtils.isBlank(search_type)){
			map.put("search_type", search_type);
		}
		//V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 end 
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		if(verify_type!=0){
			map.put("verify_type", verify_type+"");
		}
		if(!StringUtils.isBlank(verify_start_time)){
			map.put("verify_start_time", verify_start_time);
		}
		if(!StringUtils.isBlank(verify_end_time)){
			map.put("verify_end_time", verify_end_time);
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 start
		if(!StringUtils.isBlank(rule_id)){
			map.put("rule_id", rule_id);
		}
		
		//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 end
		
		
		//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 start
		if(!StringUtils.isBlank(invite_startTime)){
			map.put("invite_startTime", invite_startTime);
		}
		if(!StringUtils.isBlank(invite_endTime)){
			map.put("invite_endTime", invite_endTime);
		}
		if(!StringUtils.isBlank(startDate)){
			map.put("startDate", startDate);
		}
		if(!StringUtils.isBlank(endDate)){
			map.put("endDate", endDate);
		}
		if(!StringUtils.isBlank(credit_value)){
			map.put("credit_value", credit_value);
		}
		if(!StringUtils.isBlank(category_id)){
			map.put("category_id", category_id);
		}
		if(!StringUtils.isBlank(credit_type)){
			map.put("credit_type", credit_type);
		}
		if(user_id>0){
			map.put("user_id", user_id);
		}
		//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 end
		return map;
		
	}
	
	public void addMap(String key,String value){
		map.put(key, value);
	}
	
	/**
	 * 根据时间字符串、时间搜索是否显示时分秒
	 * 获取实际需要的时间戳
	 * @param time
	 * @param type
	 * @return
	 */
	public String getSearchTime(String time, int type){
		RuleModel rule=new RuleModel(Global.getRule(EnumRuleNid.TIME_HOUR_ENABLE.getValue()));
		int hour_enable = 0;
		if(rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
			hour_enable=rule.getValueIntByKey("enable");
		}
		Date d=DateUtils.valueOf(time);
		if(hour_enable==1){
			return Long.toString(d.getTime()/1000);
		}
		if(type==0){//DayStartTime
			return Long.toString(DateUtils.getDayStartTime(d.getTime()/1000).getTime()/1000);
		}
		if(type==1){//DayEndTime
			return Long.toString(DateUtils.getDayEndTime(d.getTime()/1000).getTime()/1000);
		}
		return "";
	}
	
	public String getSearchParamSql() {
		RuleModel rule=new RuleModel(Global.getRule(EnumRuleNid.TIME_HOUR_ENABLE.getValue()));
		int hour_enable = 0;
		if(rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()){
			hour_enable=rule.getValueIntByKey("enable");
		}
		StringBuffer sb = new StringBuffer();
		if(!StringUtils.isBlank(this.recommend)) {
		    sb.append(" and p1.is_recommend=" + recommend.trim());
		}
		if (NumberUtils.getInt(getUse())>0) {
			sb.append(" and p1.use=" + use);
		}
		if ( NumberUtils.getInt(limittime)>0) {
			sb.append(" and p1.time_limit=" + limittime);
		}
		if(!StringUtils.isBlank(this.audit_user)) {
			sb.append(" and m.sent_user ="+audit_user.trim());
		}
		if(!StringUtils.isBlank(this.keywords)) {
			sb.append(" and p1.name like '%"
					+ StringUtils.isNull(keywords) + "%'");
		}
		if(!StringUtils.isBlank(borrow_style)){
			if(!borrow_style.equals("0")){
			sb.append(" and p1.style="+borrow_style);
			}
		}
		if(!StringUtils.isBlank(huikuan_use)){
			if("1".endsWith(huikuan_use)){
				sb.append(" and p1.cash_id = 0");
			}
			if("2".endsWith(huikuan_use)){
				sb.append(" and p1.cash_id != 0");
			}
		}
		if(!StringUtils.isBlank(recharge_kefu_username)){
			sb.append(" and recharge_kefu.username="+"'"+recharge_kefu_username.trim()+"'");
		}
		String dotimeStr1=null,dotimeStr2=null;
		try {
			dotimeStr1=Long.toString(DateUtils.valueOf(dotime1).getTime()/1000);   	
		} catch (Exception e) {
			dotimeStr1="";
		}
		try {
			Date d=DateUtils.valueOf(dotime2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}	
			dotimeStr2=Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			dotimeStr2="";
		}
		if(!StringUtils.isBlank(dotimeStr1)) {
			sb.append(" and p1.addtime>="+dotimeStr1+" ");
		}
		if(!StringUtils.isBlank(dotimeStr2)){
			sb.append(" and p1.addtime<="+dotimeStr2+" ");
		}
		String succtimeStr1=null,succtimeStr2=null;
		try {
			succtimeStr1=Long.toString(DateUtils.valueOf(succtime1).getTime()/1000);
		} catch (Exception e) {
			succtimeStr1="";
		}
		try {
			Date d=DateUtils.valueOf(succtime2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}				
			succtimeStr2=Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			succtimeStr2="";
		}
		if(!StringUtils.isBlank(succtimeStr1)) {
			sb.append(" and p1.verify_time>"+succtimeStr1);
		}
		if(!StringUtils.isBlank(succtimeStr2)){
			sb.append(" and p1.verify_time<"+succtimeStr2);
		}
		// 2013/08/27 zhengzhiai add end 满标复审时间查询
		String fullVerifytimeStr1 = null, fullVerifytimeStr2 = null;
		try {
			fullVerifytimeStr1 = Long.toString(DateUtils.valueOf(fullVerifytime1).getTime()/1000);
		} catch (Exception e) {
			fullVerifytimeStr1 = "";
		}
		try {
			Date d = DateUtils.valueOf(fullVerifytime2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}	
			fullVerifytimeStr2 = Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			fullVerifytimeStr2 = "";
		}
		if(!StringUtils.isBlank(fullVerifytimeStr1)) {
			sb.append(" and p1.full_verifytime>=" + fullVerifytimeStr1);
		}
		if(!StringUtils.isBlank(fullVerifytimeStr2)){
			sb.append(" and p1.full_verifytime<=" + fullVerifytimeStr2);
		}
		String gmt_createStr1=null,gmt_createStr2=null;
		try {
			gmt_createStr1=Long.toString(DateUtils.valueOf(gmt_create1).getTime()/1000);
		} catch (Exception e) {
			succtimeStr1="";
		}
		try {
			Date d=DateUtils.valueOf(gmt_create2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}				
			gmt_createStr2=Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			gmt_createStr2="";
		}
		if(!StringUtils.isBlank(gmt_createStr1)) {
			sb.append(" and p1.gmt_create>"+gmt_createStr1);
		}
		if(!StringUtils.isBlank(gmt_createStr2)){
			sb.append(" and p1.gmt_create<"+gmt_createStr2);
		}
		String repayment_timeStr1=null,repayment_timeStr2=null;
		try {
			repayment_timeStr1=Long.toString(DateUtils.valueOf(repayment_time1).getTime()/1000);
		} catch (Exception e) {
			repayment_timeStr1="";
		}
		try {
			Date d=DateUtils.valueOf(repayment_time2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}	
			repayment_timeStr2=Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			repayment_timeStr2="";
		}
		if(!StringUtils.isBlank(repayment_timeStr1)) {
			sb.append(" and p3.repayment_time>"+repayment_timeStr1);
		}
		if(!StringUtils.isBlank(repayment_timeStr2)){
			sb.append(" and p3.repayment_time<"+repayment_timeStr2);
		}
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		String repayment_yestimeStr1 = null, repayment_yestimeStr2 = null;
		try {
			repayment_yestimeStr1 = Long.toString(DateUtils.valueOf(repayment_yestime1).getTime() / 1000);
		} catch (Exception e) {
			repayment_yestimeStr1 = "";
		}
		try {
			Date d = DateUtils.valueOf(repayment_yestime2);
			if (hour_enable != 1) {
				d = DateUtils.rollDay(d, 1);
			}
			repayment_yestimeStr2 = Long.toString(d.getTime() / 1000);
		} catch (Exception e) {
			repayment_yestimeStr2 = "";
		}
		if (!StringUtils.isBlank(repayment_yestimeStr1)) {
			sb.append(" and p3.repayment_yestime>" + repayment_yestimeStr1);
		}
		if (!StringUtils.isBlank(repayment_yestimeStr2)) {
			sb.append(" and p3.repayment_yestime<" + repayment_yestimeStr2);
		}
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
		if(!StringUtils.isBlank(account_type)&&!account_type.equals("0")){
			sb.append(" and p1.type=")
			.append("'")
			.append(account_type)
			.append("'");
		}
		
		if(!StringUtils.isBlank(paymentname)&&!paymentname.equals("0")){
			sb.append(" and p1.payment=")
			.append("'")
			.append(paymentname)
			.append("'");
		}
		//V1.6.6.2 RDPROJECT-286 wcw 2013-10-24 start 
		if(!StringUtils.isBlank(username)){
			if("0".equals(search_type)||StringUtils.isBlank(search_type)){
			    sb.append(" and p2.username like '%").append(username.trim()).append("%'");
			}else{
				sb.append(" and p2.username="+"'"+username+"'");
			}
		}
		//V1.6.6.2 RDPROJECT-286 wcw 2013-10-24 end 
		if(!StringUtils.isBlank(user_name)){
			sb.append(" and p1.user_name like '%").append(user_name.trim()).append("%'");
		}
		if(!StringUtils.isBlank(verify_user)){
			sb.append(" and p1.verify_user like '%").append(verify_user.trim()).append("%'");
		}
		if(!StringUtils.isBlank(articlename)){
			sb.append(" and a.name like '%").append(articlename.trim()).append("%'");
		}
		// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 start
		String publishStr1 = null, publishStr2 = null;
		try {
			Date publish = DateUtils.valueOf(publish1);
			publishStr1 = DateUtils.dateStr4(publish);
		} catch (Exception e) {
			publishStr1 = "";
		}
		try {
			Date d = DateUtils.valueOf(publish2);
			if (hour_enable != 1) {
				d = DateUtils.rollDay(d, 1);
			}
			publishStr2 = DateUtils.dateStr4(d);
		} catch (Exception e) {
			publishStr2 = "";
		}
		if (!StringUtils.isBlank(publishStr1)) {
			sb.append(" and a.publish > '" + publishStr1).append("'");
		}
		if (!StringUtils.isBlank(publishStr2)) {
			sb.append(" and a.publish < '" + publishStr2).append("'");
		}
		// v1.6.6.2 RDPROJECT-116 zza 2013-10-29 end
		if(statusArray!=null&&statusArray.length>1){
			sb.append(" and p1.status in (").append(StringUtils.array2Str(statusArray)).append(")");
		}else{
			if(!StringUtils.isBlank(status)){
				int statusInt=NumberUtils.getInt(status);
				if(statusInt>=0){
					sb.append(" and p1.status=").append(statusInt);
				}
			}
			if(!StringUtils.isBlank(vip_status)){
				int statusInt=NumberUtils.getInt(vip_status);
				if(statusInt>=0){
					sb.append(" and p.vip_status=").append(statusInt);
				}
			}
		}
		if (!StringUtils.isBlank(awardType)) {
			int typeInt = NumberUtils.getInt(awardType);
			if (typeInt >= 0) {
				sb.append(" and p1.type = ").append(typeInt);
			}
		}
		if (!StringUtils.isBlank(receive_status)) {
			int statusInt = NumberUtils.getInt(receive_status);
			if (statusInt >= 0) {
				sb.append(" and p1.receive_status = ").append(statusInt);
			}
		}
		if (!StringUtils.isBlank(realname)) {
			sb.append(" and p2.realname like '%" + StringUtils.isNull(realname.trim())
					+ "%'");
		}
		if (!StringUtils.isBlank(email)) {
			sb.append(" and p2.email like '%" + StringUtils.isNull(email.trim()) + "%'");
		}
		if (!StringUtils.isBlank(kefu_username)) {
			sb.append(" and kf.username like '%"
					+ StringUtils.isNull(kefu_username.trim()) + "%'");
		}
		
		if(!StringUtils.isBlank(invite_username)){
			sb.append(" and invite.username like '%").append(invite_username.trim()).append("%'");
		}
		if(!StringUtils.isBlank(type)){
			sb.append(getTypeSql(type));
		}
		if (!StringUtils.isBlank(account)) {
			sb.append(" and p1.account=" + account);
		}
		if (!StringUtils.isBlank(apr)) {
			sb.append(" and p1.apr=" + apr);
		}
		if (!StringUtils.isBlank(site_id)){
			sb.append(" and a.site_id=" + site_id);
		}
		if (!StringUtils.isBlank(limit)) {
			sb.append(" and p1.time_limit=" + limit);
		}
		if (!StringUtils.isBlank(trade_no)) {
			sb.append(" and p1.trade_no like '%"
					+ StringUtils.isNull(trade_no.trim()) + "%'");
		}
		if (!StringUtils.isBlank(payment)) {
			if(StringUtils.isNull(payment).equals("50")){
				sb.append("  and p1.payment = '50'");
			}else if(StringUtils.isNull(payment).equals("-50")){
				sb.append("  and p1.payment != '50'");
			}else{
				sb.append("  and p1.payment = '"+payment+"'");
			}
		}
		if (!StringUtils.isBlank(user_type)){
			sb.append(" and p1.type='"
					+ StringUtils.isNull(user_type) + "'");
		}
		
		// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 start （sql里要查p5，这边定的是p2）
		//用户类型
		/*if (!StringUtils.isBlank(user_typeid)){
			sb.append(" and p2.type_id='"
					+ StringUtils.isNull(user_typeid) + "'");
		}*/
		// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 end
		//审核用户名
		if (!StringUtils.isBlank(verify_username)){
			sb.append(" and p2.username like '%").append(verify_username.trim()).append("%'");
		}
		//及时雨委托协议类型
		if (!StringUtils.isBlank(protocol_type)){
			sb.append("  and p1.protocol_type = '"+protocol_type+"'");
		}
		/**
		 * 判断是否有奖励
		 * 0：无限制
		 * 1：有
		 * 2：没有
		 */
		if(!StringUtils.isBlank(is_award)){
			if(!is_award.equals("0")&&(is_award.equals("1"))){
				sb.append(" and p1.award>0");
			}else if(!is_award.equals("0")&&(is_award.equals("2"))){
				sb.append(" and p1.award=0");
			}
		}
		/**
		 * 金额搜索范围
		 */
		if(!StringUtils.isBlank(account_min)){
			sb.append(" and p1.account>="+account_min);
		}
		if(!StringUtils.isBlank(account_max)){
			sb.append(" and p1.account<="+account_max);
		}
		if (!StringUtils.isBlank(cashTotal_min)) {
		      sb.append(" and p1.total>=" + cashTotal_min);
		    }
		    if (!StringUtils.isBlank(cashTotal_max)) {
		      sb.append(" and p1.total<=" + cashTotal_max);
		    }
		    
		// 2013/08/13 linhuimin add start 待还款流转标增加应还时间查询
		String repay_timeStr1 = null;
		String repay_timeStr2 = null;
		try {
			repay_timeStr1 = Long.toString(DateUtils.valueOf(repay_time1).getTime() / 1000);
		} catch (Exception e) {
			repay_timeStr1 = "";
		}
		try {
			Date d = DateUtils.valueOf(repay_time2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}	
			repay_timeStr2 = Long.toString(d.getTime() / 1000);
		} catch (Exception e) {
			repay_timeStr2 = "";
		}
		if (!StringUtils.isBlank(repay_timeStr1)) {
			sb.append(" and p3.repay_time>" + repay_timeStr1);
		}
		if (!StringUtils.isBlank(repay_timeStr2)) {
			sb.append(" and p3.repay_time<" + repay_timeStr2);
		}
		if (!StringUtils.isBlank(repayStatus)) {
			sb.append(" and p3.status=" + repayStatus);
		}
		// 2013/08/13 linhuimin add end
		
		//已还时间
		String repay_yestimeStr1=null,repay_yestimeStr2=null;
		try {
			repay_yestimeStr1=Long.toString(DateUtils.valueOf(repay_yestime1).getTime()/1000);
		} catch (Exception e) {
			repay_yestimeStr1="";
		}
		try {
			Date d=DateUtils.valueOf(repay_yestime2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}	
			repay_yestimeStr2=Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			repay_yestimeStr2="";
		}
		if(!StringUtils.isBlank(repay_yestimeStr1)) {
			sb.append(" and p3.repay_yestime>"+repay_yestimeStr1);
		}
		if(!StringUtils.isBlank(repay_yestimeStr2)){
			sb.append(" and p3.repay_yestime<"+repay_yestimeStr2);
		}
		//V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 start 
		if(!StringUtils.isBlank(bank_id)){
			sb.append(" and p1.bank="+bank_id);
		}
		//V1.6.6.2 RDPROJECT-286 wcw 2013-10-15 end 
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		String verifyStartTimeStr=null,verifyEndTimeStr=null;
		try {
			verifyStartTimeStr=Long.toString(DateUtils.valueOf(verify_start_time).getTime()/1000);
		} catch (Exception e) {
			verifyStartTimeStr="";
		}
		try {
			Date d=DateUtils.valueOf(verify_end_time);
			d=DateUtils.rollDay(d, 1);	
			verifyEndTimeStr=Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			verifyEndTimeStr="";
		}
		if(!StringUtils.isBlank(verifyStartTimeStr)) {
			if(verify_type==1){
				sb.append(" and p4.phone_verify_time>"+verifyStartTimeStr+" ");

			}else if(verify_type==2){
				sb.append(" and p4.realname_verify_time>"+verifyStartTimeStr+" ");

			}else if(verify_type==3){
				sb.append(" and p4.video_verify_time>"+verifyStartTimeStr+" ");

			}else if(verify_type==4){
				sb.append(" and p4.scene_verify_time>"+verifyStartTimeStr+" ");
			}else if(verify_type==5){
				sb.append(" and p.vip_verify_time>"+verifyStartTimeStr+" ");
			}
			
		}
		if(!StringUtils.isBlank(verifyEndTimeStr)){
			if(verify_type==1){
				sb.append(" and p4.phone_verify_time<"+verifyEndTimeStr+" ");

			}else if(verify_type==2){
				sb.append(" and p4.realname_verify_time<"+verifyEndTimeStr+" ");

			}else if(verify_type==3){
				sb.append(" and p4.video_verify_time<"+verifyEndTimeStr+" ");

			}else if(verify_type==4){
				sb.append(" and p4.scene_verify_time<"+verifyEndTimeStr+" ");
			}else if(verify_type==5){
				sb.append(" and p.vip_verify_time<"+verifyEndTimeStr+" ");
			}
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 start
		if(!StringUtils.isBlank(rule_id)){
			sb.append(" and p1.rule_id="+rule_id);
		}
		//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 end
		
		//v1.6.7.1RDPROJECT-510 cx 2013-12-09 start
		if(!StringUtils.isBlank(invite_startTime) && !StringUtils.isBlank(invite_endTime)){
			String startTime=Long.toString(DateUtils.valueOf(invite_startTime).getTime()/1000);
			String endTime=Long.toString(DateUtils.valueOf(invite_endTime).getTime()/1000);
			sb.append(" and p2.addtime between '"+startTime+"' and '"+endTime+"'");
		}
		//v1.6.7.1RDPROJECT-510 cx 2013-12-09 end	
		
		
		//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 start
		if(!StringUtils.isBlank(startDate) && (!StringUtils.isBlank(endDate))){
			String startTime=Long.toString(DateUtils.valueOf(startDate).getTime()/1000);
			String endTime=Long.toString(DateUtils.valueOf(endDate).getTime()/1000);
			sb.append(" and p1.add_time between '"+startTime+"' and '"+endTime+"'");
		}
		if(!StringUtils.isBlank(credit_value)){
			String[] str=credit_value.split(",");
			if(str.length>1){
				sb.append(" and p1.credit_value between '"+str[0]+"' and '"+str[1]+"'");
			}else{
				sb.append(" and p1.credit_value >="+str[0]);
			}
		}
		if(!StringUtils.isBlank(category_id)){
			sb.append(" and p1.category_id="+category_id);
		}
		//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 end
		return sb.toString();
	}
	
	private String getTypeSql(String type){
		String sql=" ";
		if (StringUtils.isBlank(type)) {
			type = "0";
		}
		int borrowType = Integer.parseInt(type);
		if (borrowType > 100) {
			sql=" and p1.type= " + borrowType ;
		}
//		if(type.equals("1")){
//			sql=" and p1.is_mb=1 ";
//		}else if(type.equals("2")){
//			sql=" and p1.is_xin=1 ";
//		}else if(type.equals("3")){
//			sql=" and p1.is_fast=1 ";
//		}else if(type.equals("4")){
//			sql=" and p1.is_jin=1 ";
//		}else if(type.equals("5")){
//			sql=" and p1.is_vouch=1 ";
//		}else if(type.equals("6")){
//			sql=" and p1.is_art=1 ";
//		}else if(type.equals("7")){
//			sql=" and p1.is_charity=1 ";
//		}else if(type.equals("8")){
//			sql="  ";
//		}else if(type.equals("9")){
//			sql=" and p1.is_project=1 ";
//		}else if(type.equals("10")){
//			sql=" and p1.is_flow=1 ";
//		}else if(type.equals("11")){
//			sql=" and p1.is_student=1 ";
//		}else if(type.equals("12")){
//			sql=" and p1.is_offvouch=1 ";
//		}else if(type.equals("13")){
//			sql=" and p1.is_pledge=1 ";
//		}
		return sql;
	}
	
	/**
	 * 用户查询,得到用户操作日志的查询语句的参数
	 * @param 
	 * @return string 
	 */
	
	public String getUserLogSearchParamSql() {
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.TIME_HOUR_ENABLE.getValue()));
		int hour_enable = 0;
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			hour_enable = rule.getValueIntByKey("enable");
		}
		StringBuffer sb = new StringBuffer();
		String dotimeStr1=null,dotimeStr2=null;
		if (!StringUtils.isBlank(query)) {
			sb.append(" and p1.query like '%" + StringUtils.isNull(query) + "%'");
		}
		
		if (!StringUtils.isBlank(url)) {
			sb.append(" and p1.url like '%" + StringUtils.isNull(url) + "%'");
		}
		
		if (!StringUtils.isBlank(result)) {
			sb.append(" and p1.result like '%" + StringUtils.isNull(result) + "%'");
		}
		
		try {
			dotimeStr1=Long.toString(DateUtils.valueOf(dotime1).getTime()/1000);
		} catch (Exception e) {
			dotimeStr1="";
		}
		try {
			Date d=DateUtils.valueOf(dotime2);
			if(hour_enable!=1){
				d=DateUtils.rollDay(d, 1);
			}	
			dotimeStr2=Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			dotimeStr2="";
		}
		if(!StringUtils.isBlank(dotimeStr1)) {
			sb.append(" and p1.addtime>"+dotimeStr1);
		}
		if(!StringUtils.isBlank(dotimeStr2)){
			sb.append(" and p1.addtime<"+dotimeStr2);
		}
		
		if(!StringUtils.isBlank(username)){
			sb.append(" and p2.username like '%").append(username.trim()).append("%'");
		}
		
		if (!StringUtils.isBlank(realname)) {
			sb.append(" and p2.realname like '%" + StringUtils.isNull(realname.trim()) + "%'");
		}
		
		if (!StringUtils.isBlank(email)) {
			sb.append(" and p2.email like '%" + StringUtils.isNull(email.trim()) + "%'");
		}
		
		if (!StringUtils.isBlank(invite_userid)) {
			sb.append("  p2.invite_userid like '%" + StringUtils.isNull(invite_userid) + "%'");
		}
		
		if (!StringUtils.isBlank(card_id)) {
			sb.append("  p2.card_id like '%" + StringUtils.isNull(card_id) + "%'");
		}
		
		return sb.toString();
	}
	
	public String getOrderSql(){
		String orderSql="";
		switch (order) {
		case 0:
			orderSql = " ";
			break;
		case 1:
			orderSql = " order by p1.account+0 asc";
			break;
		case -1:
			orderSql = " order by p1.account+0 desc";
			break;
		case 2:
			orderSql = " order by p1.apr asc";
			break;
		case -2:
			orderSql = " order by p1.apr desc";
			break;
		case 3:
			orderSql = " order by scales asc";
			break;
		case -3:
			orderSql = " order by scales desc";
			break;
		case 4:
			orderSql = " order by p3.value asc";
			break;
		case -4:
			orderSql = " order by p3.value desc";
			break;
		case 5:
			orderSql=" order by p1.addtime ";
			break;
		case -5:
			orderSql=" order by p1.addtime desc";
			break;
		case 6:
			orderSql=" order by p1.is_recommend desc,p1.type asc,p1.id desc";
			break;
		case 11:
			orderSql = " order by p1.status,p1.account_yes/p1.account ,p1.addtime desc ";
			break;
		case 21:
			orderSql = " order by t.addtime ";
			break;
		case -21:
			orderSql = " order by p.addtime desc ";
			break;
		case 7:
			orderSql = " order by p1.verify_time ";
			break;
		case -7:
			//p1.apr desc, scales asc, 9月17日去掉
			orderSql = " order by p1.verify_time desc ";
			break;
		case 8:
			orderSql = " order by p1.type,p1.addtime desc ";
			break;
		// v1.6.6.2  RDPROJECT-123 wfl 2013-10-23 start
//		case 9://和信贷排序规则
//			orderSql = " order by p1.status asc, scales asc, p1.apr desc, p1.verify_time desc ";
//			break;
		//快速项目 进度升序审核时间降序。
		case 9:
			orderSql = " order by scales asc, p1.verify_time desc ";
			break;
		case 10://和信贷排序规则
			orderSql = " order by p1.status asc, scales asc, p1.apr desc, p1.verify_time desc ";
			break;
		// v1.6.6.2  RDPROJECT-123 wfl 2013-10-23 start
		default:
			orderSql=" ";
		}
		return orderSql;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	/**
	 * @return the repay_yestime1
	 */
	public String getRepay_yestime1() {
		return repay_yestime1;
	}

	/**
	 * @param repay_yestime1 the repay_yestime1 to set
	 */
	public void setRepay_yestime1(String repay_yestime1) {
		this.repay_yestime1 = repay_yestime1;
	}

	/**
	 * @return the repay_yestime2
	 */
	public String getRepay_yestime2() {
		return repay_yestime2;
	}

	/**
	 * @param repay_yestime2 the repay_yestime2 to set
	 */
	public void setRepay_yestime2(String repay_yestime2) {
		this.repay_yestime2 = repay_yestime2;
	}

	public String getReal_status() {
		return real_status;
	}

	public void setReal_status(String real_status) {
		this.real_status = real_status;
	}

	public String getEmail_status() {
		return email_status;
	}

	public void setEmail_status(String email_status) {
		this.email_status = email_status;
	}

	public String getPhone_status() {
		return phone_status;
	}

	public void setPhone_status(String phone_status) {
		this.phone_status = phone_status;
	}
	//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 start

	public String getRule_id() {
		return rule_id;
	}

	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	
	
	//v1.6.7.2 RDPROJECT-83 wcw 2013-11-28 end
}
