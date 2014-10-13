package com.p2psys.model.accountlog.borrow.tender;

import org.apache.log4j.Logger;

import com.p2psys.context.Constant;
import com.p2psys.domain.Account;
import com.p2psys.domain.Tender;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.accountlog.BaseBorrowLog;
import com.p2psys.util.StringUtils;
/**
 * 投标log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class BorrowTenderLog extends BaseBorrowLog{

	private Logger logger = Logger.getLogger(BorrowTenderLog.class);
	 
	private static final long serialVersionUID = -8005181057803582928L;
	private String templateNid=Constant.TENDER;
  
	public BorrowTenderLog() {
		super();
	}
	public BorrowTenderLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public BorrowTenderLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	@Override
	public void updateAccount() {
		Tender tender=(Tender)transfer().get("tender");
		double validAccount=Double.parseDouble(StringUtils.isNull(transfer().get("money")));
		 int row = 0;
	        try {
	            row = accountDao.updateAccountNotZero(0, -validAccount, validAccount, tender.getUser_id());
	        } catch (Exception e) {
	            tenderLog("freeze account fail!", tender.getUser_id(), tender.getBorrow_id(), tender.getMoney(), tender.getAccount());
	            logger.error(e);
	        } finally {
	            if (row < 1)
	                throw new BorrowException("投资人冻结投资款失败！请注意您的可用余额。");
	        }
	        tenderLog("freeze account success!", tender.getUser_id(), tender.getBorrow_id(), tender.getMoney(), tender.getAccount());
	}
	
	private void tenderLog(String type, long user_id, long bid, String money, String account) {
        logger.info(type + " uid=" + user_id + ",bid=" + bid + ",money=" + money + ",account=" + account);
    }
}
