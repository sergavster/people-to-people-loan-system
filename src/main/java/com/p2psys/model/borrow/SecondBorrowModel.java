package com.p2psys.model.borrow;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.domain.Account;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.borrow.NewBorrowFeeUnfreezeLog;


/**
 * 秒还标
 * 
 
 * @date 2012-9-5 下午4:35:59
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class SecondBorrowModel extends BaseBorrowModel {
	
	private static final long serialVersionUID = 7375703874958748525L;

	private BorrowModel model;

	public SecondBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_SECOND);
		this.model.setType(Constant.TYPE_SECOND);
		this.model.setNeedBorrowFee(false);
		this.model.setBorrow_fee(0.005);
		init();
	}
	
	@Override
	public void skipReview(){
		super.skipReview();
		if (model.getStatus() == 3) {
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			AccountDao accountDao = (AccountDao)ctx.getBean("accountDao"); 
	        double interest = calculateInterest();
	        double award = calculateBorrowAward();
	        double freeze = interest + award;
	        Account actBorrow = accountDao.getAccount(model.getUser_id());
	        Global.setTransfer("money", freeze);
	        Global.setTransfer("borrow", model);
	        BaseAccountLog unFreezeLog=new NewBorrowFeeUnfreezeLog(freeze, actBorrow, 1);
	        unFreezeLog.doEvent();
		}
	}

    @Override
    public double calculateInterest() {
        // TODO Auto-generated method stub
        return super.calculateInterest();
    }

    @Override
    public double calculateBorrowAward() {
        // TODO Auto-generated method stub
        return super.calculateBorrowAward();
    }

	
}
