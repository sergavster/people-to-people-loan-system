package com.p2psys.model.accountlog;
//RDPROJECT-282 fxx 2013-10-18 start
//新增类
//RDPROJECT-282 fxx 2013-10-18 end
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.TenderDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Tender;
import com.p2psys.domain.UserCache;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;


public class InterestLog extends BaseAccountLog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1367730577983353192L;
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	//protected AccountLogDao accountLogDao;
	protected TenderDao tenderDao;
	//protected UserCacheDao userCacheDao;
	//protected AccountDao accountDao;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end

	protected double interest;
	protected Tender tender;
	protected BorrowModel model;
	// 日志模板类型
	protected String templateNid = Constant.MANAGE_FEE;
	
	public InterestLog() {
		super();
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx!=null){
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
			//accountLogDao=(AccountLogDao)ctx.getBean("accountLogDao");
			//accountDao=(AccountDao)ctx.getBean("accountDao");
			tenderDao=(TenderDao)ctx.getBean("tenderDao");
			//userCacheDao=(UserCacheDao)ctx.getBean("userCacheDao");
			//v1.6.7.1 RDPROJECT-314 liukun 2013-11-15 end
		}
	}
	
	public InterestLog(BorrowModel model,Tender tender,double interest){
		this();
		this.interest=interest;
		this.tender=tender;
		this.model=model;
	}
	
	public void doEvent(){
		//UserCache uc = userCacheDao.getUserCacheByUserid(tender.getUser_id());
		int vip_status = userCacheDao.getUserVipStatus(tender.getUser_id());
		double borrow_fee_precent;
		if (vip_status == 1 && !StringUtils.isBlank(Global.getString("vip_borrow_fee"))) {
			borrow_fee_precent = Global.getDouble("vip_borrow_fee");
		} else {
			borrow_fee_precent = Global.getDouble("borrow_fee");
		}
		
		double borrow_fee=borrow_fee_precent*interest;
		accountDao.updateAccount(-borrow_fee, -borrow_fee, 0, 0, tender.getUser_id());
		Account cAct=accountDao.getAccount(tender.getUser_id());
		AccountLog cLog=new AccountLog(tender.getUser_id(),this.getType(),1,DateUtils.getNowTimeStr(),"");
		fillAccountLog(cLog,cAct,borrow_fee);
		Global.setTransfer("borrow", model);
		Global.setTransfer("money", borrow_fee);
		cLog.setRemark(this.getLogRemark());
		accountLogDao.addAccountLog(cLog);
	}
	
	
	protected void fillAccountLog(AccountLog log,Account act,double operateValue){
		log.setMoney(operateValue);
		log.setTotal(act.getTotal());
		log.setUse_money(act.getUse_money());
		log.setNo_use_money(act.getNo_use_money());
		log.setCollection(act.getCollection());
	}
	
	public String getTemplateNid(){
		return templateNid;
	}
	
}