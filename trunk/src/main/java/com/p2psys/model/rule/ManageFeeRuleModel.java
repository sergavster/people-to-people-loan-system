package com.p2psys.model.rule;
//RDPROJECT-282 fxx 2013-10-18 start
//新增类
//RDPROJECT-282 fxx 2013-10-18 end
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.p2psys.context.Global;
import com.p2psys.domain.Rule;

public class ManageFeeRuleModel {

	private Logger logger=Logger.getLogger(ManageFeeRuleModel.class);

	
	private Rule rule;
	private List<BorrowRule> borrowRules;
	private List<TenderRule> tenderRules;
	private int status;
	
	
	private ManageFeeRuleModel() {
		super();
		rule = Global.getRule("manage_fee");
		if(rule!=null){
			try {
				parseRule();
			} catch (Exception e) {
				logger.error("管理费规则错误！");
			}
		}else{
			logger.error("管理费规则错误！");
		}
	}

	public static ManageFeeRuleModel instance(){
		return new ManageFeeRuleModel();
	}
	
	
	
	public void parseRule(){
		JSONObject obj = JSON.parseObject(rule.getRule_check());
		String _borrowRules=obj.getString("borrowRules");
		String _tenderRules=obj.getString("tenderRules");
		status=obj.getIntValue("status");
		borrowRules=JSONArray.parseArray(_borrowRules, BorrowRule.class);
		tenderRules=JSONArray.parseArray(_tenderRules, TenderRule.class);
	}
	
	public String  getRuleSql(){
		StringBuffer typeSql=new StringBuffer();
		if(borrowRules!=null){
			typeSql.append(" and (");
			for(int i=0;i<borrowRules.size();i++){
				BorrowRule br=borrowRules.get(i);
				appendSqlParentheses(i,typeSql);
				typeSql.append(this.typeSql(br.getType()));
				statusSql(br.getStatus(),typeSql);
				typeSql.append(")");
			}
			typeSql.append(")");
		}
		return typeSql.toString();
	}
	
	private void appendSqlParentheses(int i,StringBuffer typeSql){
		if(i==0){
			typeSql.append(" (");
		}else{
			typeSql.append(" or (");
		}
	}
	
	private String typeSql(int type) {
		StringBuffer sql = new StringBuffer("");
		if (type != 100 ) {
			sql.append(" {alias}.type = ");
			sql.append(type);
		}
//		switch(type){
//			case 100:
//				break;
//			case 101:// 秒标 TYPE_MB
//				sql.append(" {alias}.is_mb =1 ");
//				break;
//			case 102:// 信用标 TYPE_XIN
//				sql.append(" {alias}.is_xin=1 ");
//				break;
//			case 103:
//				sql.append(" {alias}.is_fast=1 ");
//				break;
//			case 104:// 净标 TYPE_JIN
//				sql.append(" {alias}.is_jin=1  ");
//				break;
//			case 110:// 流转标
//				sql.append(" {alias}.is_flow=1  ");
//				break;
//			case 112://  保标 TYPE_VOUCH
//				sql.append("  {alias}.is_offvouch=1 ");
//				break;
//			default:// 未发布
//				
//			}
			return sql.toString();
	}
	
	public String statusSql(int[] arr,StringBuffer statusSql){
		statusSql.append(" and {alias}.status in (");
		for(int i=0;i<arr.length;i++){
			statusSql.append(arr[i]);
			if(i<arr.length-1){
				statusSql.append(",");
			}
		}
		statusSql.append(")");
		return statusSql.toString();
	}
	
	public TenderRule getTenderRate(double money){
		int moneyInt=((int)money)/10000;
		if(tenderRules!=null){
			for(TenderRule tr:tenderRules){
				if(tr.getLower()<=moneyInt&&tr.getUpper()>moneyInt){
					return tr;
				}
			}
		}
		return null;
	}
	

	public Rule getRule() {
		return rule;
	}

	public List<BorrowRule> getBorrowRules() {
		return borrowRules;
	}

	public List<TenderRule> getTenderRules() {
		return tenderRules;
	}

	public int getStatus() {
		return status;
	}
	
}
