package com.p2psys.model;

import java.util.HashMap;
import java.util.Map;

import com.p2psys.util.StringUtils;

public class UnionSearchParam extends SearchParam{
	private String type;
	private String apr;
	private String limit_time;
	private String account;
	
	private String order;

	Map map = new HashMap();
	
	public UnionSearchParam(String type, String apr, String limit_time,
			String account,String order) {
		super();
		this.type = type;
		this.apr = apr;
		this.limit_time = limit_time;
		this.account = account;
		this.order=order;
	}

	public String getType() {
		return type;
	}

	public String getApr() {
		return apr;
	}

	public String getLimit_time() {
		return limit_time;
	}

	public String getAccount() {
		return account;
	}

	public Map getMap() {
		return map;
	}
	
	public Map toMap(){
		map.put("search", "union");
		map.put("sType", type);
		map.put("sApr", apr);
		map.put("sLimit", limit_time);
		map.put("sAccount", account);
		return map;
	}
	
	public String getSearchParamSql() {
		StringBuffer sb = new StringBuffer();
		sb.append(getTypeSql(type));
		sb.append(getAprSql(apr));
		sb.append(getLimitTimeSql(limit_time));
		sb.append(getAccountSql(account));
		return sb.toString();
	}
	
	/*public String getOrderSql(){
		return getOrderSql(order);
	}*/
	
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
//			sql=" and p1.is_donation=1";
//		}
		return sql;
	}
	
	private String getAprSql(String apr){
		String sql=" ";
		if(apr.equals("0-12")){
			sql=" and p1.apr<=12 ";
		}else if(apr.equals("12-18")){
			sql=" and p1.apr>=12 and p1.apr<=18 ";
		}else if(apr.equals("18")){
			sql=" and p1.apr>=18 ";
		}
		return sql;
	}
	
	private String getLimitTimeSql(String time){
		String sql=" ";
		if(time.equals("1-3")){
			sql=" and p1.time_limit<=3 ";
		}else if(time.equals("3-6")){
			sql=" and p1.time_limit>=3 and p1.time_limit<=6 ";
		}else if(time.equals("6-9")){
			sql=" and p1.time_limit>=6 and p1.time_limit<=9 ";
		}else if(time.equals("9-12")){
			sql=" and p1.time_limit>=9 and p1.time_limit<=12 ";
		}else if(time.equals("12")){
			sql=" and p1.time_limit>=12 ";
		}
		return sql;
	}
	
	private String getAccountSql(String account){
		String sql=" ";
		if(account.equals("5")){
			sql=" and p1.account<=50000 ";
		}else if(account.equals("5-10")){
			sql=" and p1.account>=50000 and p1.account<=100000 ";
		}else if(account.equals("10-20")){
			sql=" and p1.account>=100000 and p1.account<=200000 ";
		}else if(account.equals("20-30")){
			sql=" and p1.account>=200000 and p1.account<=300000 ";
		}else if(account.equals("30")){
			sql=" and p1.account>=300000 ";
		}
		return sql;
	}
	
	/*private String getOrderSql(String order){
		String sql=" ";
		if(order.equals("1")){
			
		}else if(order.equals("-1")){
			
		}else if(order.equals("2")){
			
		}else if(order.equals("-2")){
			
		}else if(order.equals("3")){
			
		}else if(order.equals("-3")){
			
		}else if(order.equals("4")){
			
		}else if(order.equals("-4")){
			
		}
		return sql;
	}*/
	
}
