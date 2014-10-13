package com.p2psys.web.action.admin;
//v1.6.7.1 RDPROJECT cx 2013-11-25 start
//新增类
//v1.6.7.1 RDPROJECT cx 2013-11-25 end
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.Rule;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.RuleService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 规则管理
 
 *
 */
public class ManageRuleAction extends BaseAction {
	
	private final static Logger logger = Logger.getLogger(ManageRuleAction.class);
	
	private RuleService ruleService;
	
	/**
	 * 规则重加载
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String reloadRule(){
		String result="false";
		try {
			Map ruleMap = new HashMap();
			List<Rule>  ruleList = this.ruleService.getRuleAll();
			for (int i = 0; i < ruleList.size(); i++) {
				Rule r = ruleList.get(i);
				if (r != null) {
					ruleMap.put(r.getNid(), r);
				}
			}
			Global.RULES = ruleMap;
			logger.info("规则重加载成功！");
			result="true";
		} catch (Exception e) {
			logger.info("规则重加载失败！");
			e.printStackTrace();
		}
		request.setAttribute("result", result);
		return SUCCESS;
	}
	
	/**
	 * 规则列表
	 * @return
	 */
	public String allRuleList(){
		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		SearchParam param=new SearchParam();
		PageDataList pList=pList=ruleService.ruleList(page, param);
		setPageAttribute(pList, new SearchParam());
		return SUCCESS;
	}
	
	 protected void setPageAttribute(PageDataList plist,SearchParam param){	
		 List<Rule> rulList=plist.getList();
		 if(rulList!=null && rulList.size()>0){
			 for (Rule rule :rulList) {
				 if(rule.getRule_check().length()>100){
					 rule.setRule_check(rule.getRule_check().substring(0,100)+"...");
				 }
				 if(rule.getRemark().length()>20){
					 rule.setRemark(rule.getRemark().substring(0,20)+"...");
				 }
			}
		 }
		  request.setAttribute("page", plist.getPage());
		  request.setAttribute("list", rulList);
		  request.setAttribute("param", param.toMap());
	  }
	
	 /**
	  * 修改
	  * @return
	  */
	 public String updateRule(){
		 Rule rule=new Rule();
		 String id=request.getParameter("id");
		 String actionType = StringUtils.isNull(request.getParameter("actionType"));
		 if(!StringUtils.isEmpty(actionType)&&(!StringUtils.isEmpty(id))){
			 rule=this.ruleService.getRuleById(id);
			 String name=StringUtils.isNull(request.getParameter("name"));
			 String status=StringUtils.isNull(request.getParameter("status"));
			 String addtime=StringUtils.isNull(request.getParameter("addtime"));
			 String nid=StringUtils.isNull(request.getParameter("nid"));
			 String remark=StringUtils.isNull(request.getParameter("remark"));
			 String rule_check=StringUtils.isNull(request.getParameter("content"));
			 rule.setName(name);
			 rule.setStatus(Byte.parseByte(status));
			 rule.setAddtime(addtime);
			 rule.setNid(nid);
			 rule.setRemark(remark);
			 rule.setRule_check(rule_check);
			 ruleService.updateRule(rule);
			 message("修改规则成功", "/admin/rule/allRuleList.html");
			 return ADMINMSG;
		 }else{
			 if(!StringUtils.isEmpty(id)){
				 rule=this.ruleService.getRuleById(id);
			 }
		 }
		 request.setAttribute("rule", rule);
		 return SUCCESS;
	 }

	 /**
	  * 保存
	  * @return
	  */
	 public String addRule(){
		 String actionType = StringUtils.isNull(request.getParameter("actionType"));
		 String name=StringUtils.isNull(request.getParameter("name"));
		 String status=StringUtils.isNull(request.getParameter("status"));
		 String addtime=StringUtils.isNull(request.getParameter("addtime"));
		 String nid=StringUtils.isNull(request.getParameter("nid"));
		 String remark=StringUtils.isNull(request.getParameter("remark"));
		 String rule_check=StringUtils.isNull(request.getParameter("content"));
		 if(!StringUtils.isEmpty(actionType)){
			 Rule rule=new Rule();
			 rule.setName(name);
			 rule.setStatus(Byte.parseByte(status));
			 rule.setAddtime(addtime);
			 rule.setNid(nid);
			 rule.setRemark(remark);
			 rule.setRule_check(rule_check);
			 ruleService.addRule(rule);
			 message("新增规则成功！","/admin/rule/allRuleList.html");
			 return ADMINMSG;
		 }
		 return SUCCESS;
	 }
	 
	 /**
	  * 报表显示直接跳转
	  * @return
	  */
	 public String index(){
		 return SUCCESS;
	 }
	 
	public RuleService getRuleService() {
		return ruleService;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

}
