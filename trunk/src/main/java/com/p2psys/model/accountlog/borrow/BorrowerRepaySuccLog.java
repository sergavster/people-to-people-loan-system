package com.p2psys.model.accountlog.borrow;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.ProtocolDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Protocol;
import com.p2psys.domain.Repayment;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;

public class BorrowerRepaySuccLog extends BaseSimpleNoticeLog {

	private static final long serialVersionUID = -6837617034387772559L;

	private final static String noticeTypeNid = Constant.NOTICE_REPAY_SUCC;
	
	public BorrowerRepaySuccLog() {
		super();
	}

	public BorrowerRepaySuccLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public BorrowerRepaySuccLog(double money, Account act) {
		super(money, act);
	}

	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	}

	@Override
	public void extend() {
		Borrow borrow=(Borrow)transfer().get("borrow");
		Repayment repay = (Repayment)transfer().get("repay");
		
		//及时雨借款人归还借款生成借款人归还本息委托书
		if(Global.getWebid().equals("jsy")){
			double capital = NumberUtils.getDouble(repay.getCapital());
			double interest = NumberUtils.getDouble(repay.getInterest());
			
			Protocol protocol = new Protocol(0, Constant.REPAYMENT_ACCOUNT_PROTOCOL,DateUtils.getNowTimeStr(), "", "");
			protocol.setRemark("用户名为"+repay.getUsername()+"的用户进行还款（标名为"+borrow.getName()+"的借款标）,归还本息"+capital+interest+"元，生成借款人归还本息委托书");
			protocol.setUser_id(borrow.getUser_id());
			protocol.setMoney(capital+interest);
			protocol.setRepayment_account(capital);
			protocol.setInterest(interest);
			protocol.setBorrow_id(repay.getBorrow_id());
			protocol.setRepayment_time(repay.getRepayment_time());
			WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
			ProtocolDao protocolDao = (ProtocolDao)ctx.getBean("protocolDao");
			protocolDao.addProtocol(protocol);
		}
	}
	
	
}
