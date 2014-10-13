package com.p2psys.model.accountlog.borrow.tender;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.ProtocolDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Protocol;
import com.p2psys.domain.Tender;
import com.p2psys.model.DetailUser;
import com.p2psys.model.accountlog.borrow.verifyfullBorrow.BorrowDecuctFreezeLog;
import com.p2psys.util.NumberUtils;
/**
 * 流转标扣除冻结投标金额，生成待收本金log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class FlowBorrowDecuctFreezeLog extends BorrowDecuctFreezeLog {

	private static final long serialVersionUID = 7416556004359684135L;
	
	private String templateNid = Constant.INVEST;

	public FlowBorrowDecuctFreezeLog() {
		super();
	}

	public FlowBorrowDecuctFreezeLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}

	public FlowBorrowDecuctFreezeLog(double money, Account act) {
		super(money, act);
	}
	
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(0, 0, -this.getMoney(), this.getMoney(), this.getUser_id());
	}

	@Override
	public void extend() {
		Borrow borrow=(Borrow)transfer().get("borrow");
		Tender tender=(Tender)transfer().get("tender");
		if (Global.getWebid().equals("jsy")) {
			Protocol protocol=new Protocol();
            DetailUser user = userDao.getDetailUserByUserid(this.getUser_id());
            protocol.setRemark("用户名为" + user.getUsername() + "的用户投标（标名为" + borrow.getName() + "的借款标）金额"
                    + tender.getAccount() + "元");
            protocol.setProtocol_type(Constant.TENDER_PROTOCOL);
            protocol.setUser_id(tender.getUser_id());
            protocol.setBorrow_id(tender.getBorrow_id());
            protocol.setPid(tender.getId());
            protocol.setMoney(NumberUtils.getDouble(tender.getAccount()));
            WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
            ProtocolDao protocolDao = (ProtocolDao)ctx.getBean("protocolDao");
            protocolDao.addProtocol(protocol);
        }
	}
}
