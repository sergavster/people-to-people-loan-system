package com.p2psys.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.p2psys.domain.BorrowConfig;
import com.p2psys.domain.CreditRank;
import com.p2psys.domain.LogTemplate;
import com.p2psys.domain.NoticeType;
import com.p2psys.domain.Rule;
import com.p2psys.model.SystemInfo;
import com.p2psys.tool.Utils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
/**
 * 
 * 
 
 * @date 2012-8-29 下午4:28:22
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class Global {
	
	public static SystemInfo SYSTEMINFO;
	public static Map BORROWCONFIG;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-25 start
	//public static Map NOTICECONFIG;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	public static Map<String,Rule> RULES = new HashMap<String, Rule>();
	public static Map<String,LogTemplate> LOG_TEMPLATE_MAP = new HashMap<String, LogTemplate>();
	public static List<CreditRank> ALL_CREDIT_RANK;
	
	public static Set TRADE_NO_SET= Collections.synchronizedSet(new HashSet());
	public static Set TENDER_SET= Collections.synchronizedSet(new HashSet());
	public static Map TENDER_MAP=Collections.synchronizedMap(new HashMap<String,String>());
	public static Map<String,Object> SESSION_MAP=Collections.synchronizedMap(new HashMap<String,Object>());

	public static String[] SYSTEMNAME=new String[]{"webname","meta_keywords","meta_description",
		"beian","copyright","fuwutel","address","weburl","vip_fee","most_cash","theme_dir","version",
		"normal_rate","overdue_rate","bad_rate","enable_online_recharge"};
	
	public static ThreadLocal ipThreadLocal= new ThreadLocal();
	
	public static final ThreadLocal transferThreadLocal = new ThreadLocal();
	
	public static String VERSION="v1.6.7.1";
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
	public static Map SMSTYPECONFIG;
	
	public static Map NOTICE_TYPE_CONFIG;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	
	public static BorrowConfig getBorrowConfig(int borrowType){
		BorrowConfig config=null;
		if(BORROWCONFIG==null){
			return null;
		}
		Object obj=BORROWCONFIG.get(new Long(borrowType));
		if(obj==null) return null;
		config=(BorrowConfig)obj;
		return config;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-25 start
	//RDPROJECT-314 DELETE
	/*public static NoticeConfig getNoticeConfig(String noticeType){
		NoticeConfig config= new NoticeConfig();
		if(NOTICECONFIG==null){
			return config;
		}
		Object obj=NOTICECONFIG.get(noticeType);
		if(obj==null) return config;
		config=(NoticeConfig)obj;
		return config;
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-25 end
	public static String getValue(String key){
		Object o=null;
		if(SYSTEMINFO!=null){
			o=SYSTEMINFO.getValue(key);
		}
		if(o==null){
			return "";
		}
		return o.toString();
	}
	
	public static String getString(String key){
		String s=StringUtils.isNull(getValue(key));
		return s;
	}
	
	public static int getInt(String key){
		int i=NumberUtils.getInt(getValue(key));
		return i;
	}
	
	public static double getDouble(String key){
		double i=NumberUtils.getDouble(getValue(key));
		return i;
	}
	
//	public static String getBorrowTypeName(int type){
//		switch(type){
//		case 100: return "month";//default is month
//		case 101:return "miaobiao";//second
//		case 102:return "month"; //credit
//		case 103:return "fast";//mortgage
//		case 104:return "jin";//property
//		case 105:return "vouch";
//		case 106:return "art";
//		case 107:return "charity";
//		case 108:return "preview";
//		case 109:return "project";
//		case 110:return "flow";
//		case 111:return "student";//友友贷的学信标
//		case 112:return "offvouch";//线下担保标
//		case 113:return "pledge";//质押标
//		// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 start
//		case 114:return "donation";//爱心捐助标，金联创投
//		case 115:return "industry";//实业标，贷深圳
//		case 116:return "jointguarantee";//联名担保标
//		// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 end
//		default: return "";
//		}
//	}
	
	
	public static int getBorrowType(String type){
		if(type.equals("month")||type.equals("")){
			return 102;
		}else if(type.equals("miaobiao")){
			return 101;
		}else if(type.equals("xin")){
			return 102;
		}else if(type.equals("fast")){
			return 103;
		}else if(type.equals("jin")){
			return 104;
		}else if(type.equals("charity")){
			return 107;
		}else if(type.equals("project")){
			return 109;
		}else if(type.equals("flow")){
			return 110;
		}else if(type.equals("student")){
			return 111;
		}else if(type.equals("offvouch")){
			return 112;
		}else if(type.equals("pledge")){
			return 113;
		}else if(type.equals("donation")){
			return 114;
		}
		// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 start
		else if(type.equals("industry")){
			return 115;
		}
		else if(type.equals("jtguarantee")){
			return 116;
		}
		// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 end
		else{
			return 102;
		}
	}
	
	public static synchronized Object getTenderLock(String borrowId){
		return TENDER_MAP.get(borrowId);
	}
	
	/*public static double getCash(double x,double y,double r,double maxCash){
		String site_id=StringUtils.isNull(Global.getValue("webid"));
		double fee=0.0;
		if(site_id.equals("zrzb")){
			fee=Utils.getCashFeeForZrzbZero(x, y, r, maxCash);
		}else{
			fee=Utils.getCashFeeForZRZB(x, y, r, maxCash);
		}
		return fee;
	}*/
	
	public static double getCash(double x, double r, double money,
			double maxCash) {
		String site_id = StringUtils.isNull(Global.getValue("webid"));
		double fee = 0.0;
		if (site_id.equals("zrzb") || site_id.equals("zdvci")
				|| site_id.equals("xdcf") || site_id.equals("lhcf") ||site_id.equals("bfct")) {
			fee = Utils.getCashFeeForZrzbZero(x, r, money);
		} else if (site_id.equals("huidai")) {
			fee = Utils.getCashFeeForHuidai(x, money, r);
		} else if (site_id.equals("lhdai") || site_id.equals("glct") || site_id.equals("baoducf")  || site_id.equals("hycf") || site_id.equals("zhiyacf")) {
			fee = Utils.getCashFeeForlhd(x, money, r, maxCash);
		} else if (site_id.equals("rydai")) {
			fee = Utils.getCashFeeForRYD(x, r, money);
		}else {
			fee = Utils.getCashFeeForZRZB(x, money, r, maxCash);
		}
		return fee;
	}
	public static double getCash(double x,double r,double money,double maxCash,double tender_award){
		double fee=0.0;
		fee=Utils.getCashFeeForSSJB(x, money, r, maxCash,tender_award);
		return fee;
	}
	public static double getCashForWzdai(double use_money,double ownmoney,double x,double r,double money,double maxCash){
		String site_id=StringUtils.isNull(Global.getValue("webid"));
		double fee=0.0;
		if(site_id.equals("jsy")){
			fee=Utils.getCashFeeForJJY(x, r,money, maxCash);
		}else if(site_id.equals("zsdai") || site_id.equals("aidai") || site_id.equals("baoducf") || site_id.equals("lihect")){
			fee=Utils.GetZsdCashFee(x, r, money, maxCash);
		}
		else{
			if(use_money>=200000&&ownmoney>=200000&&x>=200000){
				fee=Utils.GetLargeCashFee(x, r, money, maxCash);
			}else{
				fee=Utils.GetCashFee(x, r, money, maxCash);
			}
		}
		return fee;
	}
	
	/**
	 * 和信贷提现规则
	 * 指定日期前提现收取相应比例提现手续费，不足最低收费标准按最低收费标准收取；指定日期后提现免费；
	 * @param use_money
	 * @param ownmoney
	 * @param x
	 * @param r
	 * @param money
	 * @param maxCash
	 * @param lowestCashFee最低收费标准
	 * @return
	 */
	public static double getCashForHeXinDai(double use_money,double ownmoney,double x,double r,double money,double maxCash,double lowestCashFee){
		double fee=0.0;
			fee=Utils.getCashFeeForHeXinDai(x, r, money, maxCash,lowestCashFee);
		return fee;
	}
	
	/**
	 * 爱贷提现规则
	 * @param use_money可用余额 
	 * @param ownmoney净资产
	 * @param x提现额度 
	 * @param r免费额度
	 * @param money 提现费率 
	 * @param maxCash最高提醒额度
	 * @return
	 */
	public static double getCashForAidai(double use_money,double ownmoney,double x,double r,double money,double maxCash){
		double fee = Utils.GetZsdCashFee(x, r, money, maxCash);
		return fee;
	}

	public static String getWebid(){
		return StringUtils.isNull(Global.getValue("webid"));
	}

	/**
	 * 河南贷提现
	 * 
	 * @param x
	 * @param r
	 * @param money
	 * @param maxCash
	 * @param o
	 * @param t
	 * @param or
	 * @return
	 */
	public static double getCashForHndai(double x, double r, double money,
			double maxCash, double o, double t, double or) {
		double fee = 0.0;
		fee = Utils.getCashFeeForHndai(x, money, r, maxCash, o, t, or);
		return fee;
	}
	
	/**
	 * 国临创投提现规则
	 * 抵押、流转标回款金额提现免手续费
	 * 超出回款金额以外的部分如果为15天之内（包含15天）收取手续费，超出部分免手续费
	 * @param x
	 * @param r
	 * @param money
	 * @param maxCash
	 * @param lastSum //15天之内充值总额
	 * @return
	 */
	public static double getCashForJlct(double x, double r, double money,
			double maxCash,double lastSum) {
		double fee = 0.0;
		fee = Utils.getCashFeeForJlct(x, r, money,lastSum);
		return fee;
	}
	
	public static String getIP(){
		Object retObj=Global.ipThreadLocal.get();
		return retObj==null?"":retObj.toString();
	}
	
	public static String getVersion(){
		return Global.getString("version");
	}
	
	public static Rule getRule(String nid){
		return Global.RULES.get(nid);
	}
	
	public static String getLogTempValue(byte type , String nid){
		if(type <= 0 || nid == null || nid.length() <= 0){
			return null;
		}
		String key = type + "_" + nid;
		LogTemplate temp = Global.LOG_TEMPLATE_MAP.get(key);
		if(temp == null) return null;
		return temp.getValue();
	}
	
	public static String getLogType(byte type , String nid){
		if(type <= 0 || nid == null || nid.length() <= 0){
			return null;
		}
		String key = type + "_" + nid;
		LogTemplate temp = Global.LOG_TEMPLATE_MAP.get(key);
		if(temp == null) return null;
		return temp.getLog_type();
	}
	
	public static CreditRank getCreditRank(int value){
		CreditRank backCreaditRank = null;
		for(int i=0;i<Global.ALL_CREDIT_RANK.size();i++){
			CreditRank cr = (CreditRank)Global.ALL_CREDIT_RANK.get(i);
			if(value>=cr.getPoint1()&&value<=cr.getPoint2()){
				backCreaditRank = cr;
				break;
			} 
		}
		return backCreaditRank;
	}
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
	/*public static Smstype getSmstype(String nid){
		Smstype smstype= new Smstype();
		if(SMSTYPECONFIG==null){
			return smstype;
		}
		Object obj=SMSTYPECONFIG.get(nid);
		if(obj==null) return smstype;
		smstype=(Smstype)obj;
		return smstype;
	}*/
	
	public static NoticeType getNoticeType(String noticeTypeNid, byte notice_type){
		NoticeType noticeType = new NoticeType();
		if(NOTICE_TYPE_CONFIG==null){
			return noticeType;
		}
		Object obj=NOTICE_TYPE_CONFIG.get(noticeTypeNid + "_" + notice_type);
		if(obj==null) return noticeType;
		noticeType=(NoticeType)obj;
		return noticeType;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	
	public static Map<String,Object> getTransfer() {  
		Map<String,Object> map = (Map<String,Object>) transferThreadLocal.get();  
		if (map == null) {  
            map = new HashMap<String,Object>();  
            transferThreadLocal.set(map);  
        }  
	    return map;  
	}  
	
	public static void setTransfer(String key,Object value) {  
		Map<String,Object> map = getTransfer();
		map.put(key, value); 
		transferThreadLocal.set(map);  
	}
	

	
}
