package com.p2psys.web.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.BorrowConfigDao;
import com.p2psys.dao.NoticeTypeDao;
import com.p2psys.dao.UserCreditDao;
import com.p2psys.domain.BorrowConfig;
import com.p2psys.domain.CreditRank;
import com.p2psys.domain.LogTemplate;
import com.p2psys.domain.NoticeType;
import com.p2psys.domain.Rule;
import com.p2psys.domain.Site;
import com.p2psys.domain.User;
import com.p2psys.model.SystemInfo;
import com.p2psys.model.tree.Tree;
import com.p2psys.quartz.QuartzJob;
import com.p2psys.quartz.notice.NoticeJobQueue;
import com.p2psys.service.AccountService;
import com.p2psys.service.ArticleService;
import com.p2psys.service.AutoBorrowService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.LogTemplateService;
import com.p2psys.service.NoticeService;
import com.p2psys.service.RewardStatisticsService;
import com.p2psys.service.RuleService;
import com.p2psys.service.StatusRecordService;
import com.p2psys.service.SystemService;
import com.p2psys.service.UserService;
import com.p2psys.util.StringUtils;

public class WebConfigContextListener implements ServletContextListener,HttpSessionAttributeListener{
	private static Logger logger=Logger.getLogger(WebConfigContextListener.class);
	private Object lock=new Object();
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context=event.getServletContext();
		ApplicationContext ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		SystemService systemService=(SystemService)ctx.getBean("systemService");
		BorrowService borrowService=(BorrowService)ctx.getBean("borrowService");
		AutoBorrowService autoBorrowService=(AutoBorrowService)ctx.getBean("autoBorrowService");
		NoticeService noticeService=(NoticeService)ctx.getBean("noticeService");
		RuleService ruleService = (RuleService) ctx.getBean("ruleService");
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-05 start
		RewardStatisticsService rewardStatisticsService = (RewardStatisticsService) ctx.getBean("rewardStatisticsService");
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-05 end
		LogTemplateService logTemplateService = (LogTemplateService) ctx.getBean("logTemplateService");
	    // v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 start 
		AccountService accountService=(AccountService) ctx.getBean("accountService");
	    // v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 end 
		// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
		StatusRecordService statusRecordService = (StatusRecordService)ctx.getBean("statusRecordService");
		// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
		SystemInfo info=systemService.getSystemInfo();
		Global.SYSTEMINFO=info;
		setWebConfig(context,info);
		//检查系统、数据库版本是否一致！
		checkVersion();
		
		ArticleService articleService=(ArticleService)ctx.getBean("articleService");
		Tree<Site> tree=articleService.getSiteTree();
		context.setAttribute("tree", tree);
		//获取客服列表
		UserService userService = (UserService) ctx.getBean("userService");
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 start
		List<User> kefuList = userService.getKfList(1);
		//v1.6.7.2 RDPROJECT-572 wcw 2013-12-11 end
		context.setAttribute("kefuList", kefuList);
		
		BorrowConfigDao borrowConfigDao=(BorrowConfigDao)ctx.getBean("borrowConfigDao");
		List list=borrowConfigDao.getList();
		Map map=new HashMap();
		for(int i=0;i<list.size();i++){
			BorrowConfig config=(BorrowConfig)list.get(i);
			map.put(config.getId(), config);
		}
		Global.BORROWCONFIG=map;
		
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
		//通知配置
		/*NoticeConfigDao noticeConfigDao=(NoticeConfigDao) ctx.getBean("noticeConfigDao");
        List noticelist=noticeConfigDao.getList();
        Map noticemap=new HashMap();
		for(int i=0;i<noticelist.size();i++){
			NoticeConfig config=(NoticeConfig)noticelist.get(i);
			noticemap.put(config.getType(), config);
		}
		Global.NOTICECONFIG=noticemap;*/
		
		/*
		//短信类型配置
		SmstypeDao smstypeDao = (SmstypeDao)ctx.getBean("smstypeDao");
		List smstypeList = smstypeDao.getList();
        Map smstypeMap=new HashMap();
		for(int i=0;i<smstypeList.size();i++){
			Smstype smstype=(Smstype)smstypeList.get(i);
			smstypeMap.put(smstype.getNid(), smstype);
		}
		Global.SMSTYPECONFIG=smstypeMap;*/
		
		//读取通知类型配置
		NoticeTypeDao noticeTypeDao = (NoticeTypeDao)ctx.getBean("noticeTypeDao");
		List noticeTypeList = noticeTypeDao.getList();
        Map noticeTypeMap=new HashMap();
		for(int i=0;i<noticeTypeList.size();i++){
			NoticeType noticeType=(NoticeType)noticeTypeList.get(i);
			noticeTypeMap.put(noticeType.getNid() + "_" + noticeType.getNotice_type(), noticeType);
		}
		Global.NOTICE_TYPE_CONFIG=noticeTypeMap;
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
		
		UserCreditDao userCreditDao=(UserCreditDao) ctx.getBean("userCreditDao");
		List<CreditRank> creditRankList=userCreditDao.getCreditRankList();
		Global.ALL_CREDIT_RANK=creditRankList;

		//规则配置
		Map ruleMap = new HashMap();
		ruleService = (RuleService) ctx.getBean("ruleService");
		List<Rule>  ruleList = ruleService.getRuleAll();
		for (int i = 0; i < ruleList.size(); i++) {
			Rule r = ruleList.get(i);
			if (r != null) {
				ruleMap.put(r.getNid(), r);
			}
		}
		Global.RULES = ruleMap;
		
		Map<String , LogTemplate> logTemplateMap = new HashMap<String , LogTemplate>();
		List<LogTemplate> logTemplateList = logTemplateService.getLogTemplateAll();
		for (int i = 0; i < logTemplateList.size(); i++) {
			LogTemplate logT = logTemplateList.get(i);
			if (logT != null) {
				String mapKey = logT.getType() + "_" +logT.getNid();
				logTemplateMap.put(mapKey, logT);
			}
		}
		Global.LOG_TEMPLATE_MAP = logTemplateMap;
		
		// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 start
		// 首页滚动图片
		List scrollPic = articleService.getScrollPicList(1, 0, 10);
		context.setAttribute("scrollPic", scrollPic);

		// 合作伙伴
		List cooperativePartnerPic = articleService.getScrollPicList(2, 0, 10);
		context.setAttribute("cooperativePartnerPic", cooperativePartnerPic);

		// 友情链接
		List linksPic = articleService.getScrollPicList(3, 0, 10);
		context.setAttribute("linksPic", linksPic);
		// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 end
		
		//后台进程设置，并启动
		QuartzJob job = new QuartzJob();
		job.setBorrowService(borrowService);
		job.setAutoBorrowService(autoBorrowService);
		job.setUserService(userService);
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-05 start
		job.setRewardStatisticsService(rewardStatisticsService);
		// v1.6.7.1 RDPROJECT-395 zza 2013-11-05 end
	    // v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 start 
		job.setAccountService(accountService);
	    // v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 end
		// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
		job.setStatusRecordService(statusRecordService);
		// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
		job.doTask();
		
		logger.debug(ctx.getBean("messageDao"));
		NoticeJobQueue.init(noticeService);
	}
	
	private void setWebConfig(ServletContext context,SystemInfo info){
		String[] webinfo=Global.SYSTEMNAME;
		for(String s:webinfo){
			logger.debug(s+":"+info.getValue(s));
			context.setAttribute(s, info.getValue(s));
			if(s.equals("theme_dir")&&StringUtils.isBlank(info.getValue(s))){
				context.setAttribute(s, "/themes/soonmes_default");
			}
		}
		context.setAttribute("webroot", context.getContextPath());
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		User user=getSessionUser(event);
        if(user!=null){
        	synchronized (lock) {
        		//刷新登录时间
        		Global.SESSION_MAP.put(user.getUsername(), System.currentTimeMillis());
//        		if(!Global.SESSION_MAP.containsKey(user.getUsername())){
//        			Global.SESSION_MAP.put(user.getUsername(), event.getValue());
        			//在全局变量中保存用户登录的最新时间
//        		}
             }
        }
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		User user=getSessionUser(event);
        if(user!=null){
        	synchronized (lock) {
        		if(Global.SESSION_MAP.containsKey(user.getUsername())){
        			//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start begin
        			//Global.SESSION_MAP.remove(user.getUsername());
        			//V1.6.6.1 RDPROJECT-237 liukun 2013-09-26 start end
        		}
             }
        }
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		User user=getSessionUser(event);
        if(user==null){
        	synchronized (lock) {
        		if(Global.SESSION_MAP.containsKey(event.getName())
        				&&event.getValue()==null){
        			Global.SESSION_MAP.remove(event.getName());
        		}
             }
        }
		
	}
	
	public User getSessionUser(HttpSessionBindingEvent event){
		if(StringUtils.isNull(event.getName()).equals(Constant.SESSION_USER)){
        	Object obj=event.getValue();
        	if(obj!=null){
        		return (User)obj;
        	}
        }
		return null;
	}
	
	public void checkVersion(){
		String dbVersion=Global.getVersion();
		String sysVersion=Global.VERSION;
		logger.info("数据库版本："+dbVersion);
		logger.info("系统版本:"+sysVersion);
		if(!Global.getVersion().equals(Global.VERSION)){
			throw new RuntimeException("数据库版本与系统版本不一致，请更新数据库！");
		}
	}
}
