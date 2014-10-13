package com.p2psys.model.creditlog;

import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.common.enums.EnumLogTemplateType;
import com.p2psys.context.Global;
import com.p2psys.dao.RuleDao;
import com.p2psys.dao.UserCreditDao;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.UserCreditLog;
import com.p2psys.util.DateUtils;
import com.p2psys.util.FreemarkerUtil;

/**
 * 抽象积分日志类
 * 
 * @version 1.0
 * @since 2013-8-26
 */
public class BaseCreditLog extends UserCreditLog implements CreditLogEvent {
	
	private static final long serialVersionUID = 8585423859178082248L;

	protected UserCreditDao userCreditDao;
	
	protected UserDao userDao;
	
	protected RuleDao ruleDao;
	
	protected String logRemarkTemplate;
	
	// 积分日志信息类型
	protected String logType;
	
	public static boolean DEBUG=false;
	
	public BaseCreditLog() {
		super();
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			userCreditDao=(UserCreditDao)ctx.getBean("userCreditDao");
			userDao = (UserDao)ctx.getBean("userDao");
			ruleDao = (RuleDao)ctx.getBean("ruleDao");
		}
	}
	
	public BaseCreditLog(long op , long op_user , long credit_type , long user_id, long credit_value, long valid_value) {
		this();
		this.setAddtime(Long.parseLong(DateUtils.getNowTimeStr()));
		this.setOp(op);
		this.setOp_user(user_id);
		this.setType_id(credit_type);
		this.setUser_id(user_id);
		this.setValue(credit_value);
		this.setValid_value(valid_value);
	}
	
	/**
	 * 默认的事件执行
	 */
	@Override
	public void doEvent() {
		//调试时手动传参，服务器上通过Spring容器获取
		if(DEBUG){
			transfer();
		}
		addCreditLog();
		//操作日志
		addOperateLog();
		
	}

	/**
	 * 添加积分日志
	 */
	public void addCreditLog(){
		//积分日志模板
		logRemarkTemplate = Global.getLogTempValue(EnumLogTemplateType.CREDIT_LOG.getValue(), logType);
		this.setRemark(this.getLogRemark());
		userCreditDao.addUserCreditLog(this);
	}
	
	public String getLogRemark(){
		try {
			return FreemarkerUtil.renderTemplate(logRemarkTemplate, transfer());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	public Map<String, Object> transfer() {
		Map<String,Object> data=Global.getTransfer();
		if(userCreditDao==null){
			userCreditDao=(UserCreditDao)data.get("userCreditDao");
		}
		if(userDao == null){
			userDao = (UserDao)data.get("userDao");
		}
		if(ruleDao == null){
			ruleDao = (RuleDao)data.get("ruleDao");
		}
		return data;
	}

	public void setLogRemarkTemplate(String logRemarkTemplate) {
		this.logRemarkTemplate = logRemarkTemplate;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}
	
	@Override
	public void addOperateLog() {
		
	}
	
	public UserCreditDao getUserCreditDao() {
		return userCreditDao;
	}

	public void setUserCreditDao(UserCreditDao userCreditDao) {
		this.userCreditDao = userCreditDao;
	}

}
