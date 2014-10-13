package com.p2psys.model.accountlog.borrow;

import javax.annotation.Resource;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.ProtocolDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Collection;
import com.p2psys.domain.Protocol;
import com.p2psys.model.DetailUser;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;

public class TenderFlowRepaySuccLog extends BaseSimpleNoticeLog {

	private static final long serialVersionUID = -6837617034387772559L;

	@Resource
	public ProtocolDao protocolDao;
	
	public TenderFlowRepaySuccLog() {
		super();
	}

	public TenderFlowRepaySuccLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public TenderFlowRepaySuccLog(double money, Account act) {
		super(money, act);
	}

	private final static String noticeTypeNid = Constant.NOTICE_FLOW_REPAY_SUCC;
	@Override
	public String getNoticeTypeNid() {
		return this.noticeTypeNid;
	}

	@Override
	public void extend() {
		Borrow borrow = (Borrow)transfer().get("borrow");
		Collection collection = (Collection)transfer().get("collection");
		double repay = (Double)transfer().get("repay");
		
		//及时雨借款人归还借款生成借款人归还本息委托书
		if(Global.getWebid().equals("jsy")){
			DetailUser user = userDao.getDetailUserByUserid(borrow.getUser_id());
			Protocol protocol=new Protocol(0, Constant.REPAYMENT_ACCOUNT_PROTOCOL,DateUtils.getNowTimeStr(), "", "");
			protocol.setRemark("用户名为"+user.getUsername()+"的用户进行还款（标名为"+borrow.getName()+"的借款标）,归还本息"+repay+"元，生成借款人归还本息委托书");
			protocol.setUser_id(borrow.getUser_id());
			protocol.setMoney(repay);
			protocol.setRepayment_account(NumberUtils.getDouble(collection.getCapital()));
			protocol.setInterest(NumberUtils.getDouble(collection.getInterest()));
			protocol.setBorrow_id(borrow.getId());
			protocol.setRepayment_time(collection.getRepay_time());
			
			protocolDao.addProtocol(protocol);
		}
	}
	
}
